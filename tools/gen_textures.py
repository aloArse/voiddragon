#!/usr/bin/env python3
"""Paints the Void Dragon texture atlas straight from the model spec.

Art direction: the reference creature is a black obsidian dragon whose hide is
cracked with molten violet energy, crowned by pale bone horns and an inner glow.
The atlas is painted face-by-face on the exact UV islands that tools/gen_model.py
packs, so scales, plates, eyes and glow veins land precisely where the geometry
expects them.

Outputs
  textures/entity/void_dragon.png            base atlas
  textures/entity/void_dragon_glow.png       emissive (eyes, cracks, membranes)
  textures/entity/void_dragon_armored.png    armoured variant
  textures/item/*.png                        shard / saddle / armour icons
  textures/gui/ability_*.png                 HUD icons

Run: python3 tools/gen_textures.py
"""
import json
import math
import os
import random
import sys

import numpy as np
from PIL import Image, ImageDraw, ImageFilter

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from dragon_spec import PARTS, TEXTURE_SIZE  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src/main/resources/assets/voiddragon/textures")
UV_FILE = os.path.join(ROOT, "tools/atlas_uv.json")

# ------------------------------------------------------------------- palette
OBSIDIAN = (23, 18, 31)
HIDE = (62, 48, 92)
HIDE_MID = (84, 64, 122)
HIDE_HI = (118, 88, 176)
EDGE_DARK = (26, 19, 38)
CRACK = (92, 44, 140)
CRACK_HOT = (176, 92, 255)

BELLY = (74, 60, 98)
BELLY_LINE = (38, 29, 52)

RIDGE = (76, 44, 116)
RIDGE_HI = (96, 56, 148)

BLADE = (58, 38, 88)
BLADE_EDGE = (138, 84, 208)

MEMBRANE_DEEP = (62, 28, 92)
MEMBRANE_MID = (78, 36, 116)
MEMBRANE_VEIN = (126, 60, 188)

BONE = (196, 190, 176)
EYE_GLOW = (206, 148, 255)
BONE_SHADE = (150, 144, 134)
BONE_TIP = (236, 232, 222)

SKULL = (56, 44, 84)
SKULL_PLATE = (74, 58, 110)
MUZZLE = (60, 47, 90)
JAW = (48, 37, 72)

LIMB = (54, 42, 80)
FOOT = (42, 33, 62)

VOID = (168, 92, 255)
VOID_BRIGHT = (214, 158, 255)
VOID_PALE = (242, 226, 255)
EYE_CORE = (236, 206, 255)

LEATHER = (48, 26, 40)
LEATHER_HI = (78, 44, 62)
STEEL = (40, 38, 56)
STEEL_HI = (92, 88, 120)
STEEL_DARK = (22, 20, 30)
RUNE = (150, 80, 240)

FACE_TINT = {"top": 1.12, "bottom": 0.66, "front": 1.0, "back": 0.86, "left": 0.82, "right": 0.94}


# ----------------------------------------------------------------- helpers
def clamp(v, lo=0, hi=255):
    return max(lo, min(hi, int(v)))


def mix(a, b, t):
    t = max(0.0, min(1.0, t))
    return tuple(clamp(a[i] + (b[i] - a[i]) * t) for i in range(3))


def shade(c, f):
    return tuple(clamp(v * f) for v in c)


def face_rects(u, v, w, h, d):
    return {
        "top": (u + d, v, w, d),
        "bottom": (u + d + w, v, w, d),
        "right": (u, v + d, d, h),
        "front": (u + d, v + d, w, h),
        "left": (u + d + w, v + d, d, h),
        "back": (u + d + w + d, v + d, w, h),
    }


class Canvas:
    """RGBA paint canvas backed by numpy for speed."""

    def __init__(self, size, seed=2026):
        self.size = size
        self.rgb = np.zeros((size, size, 3), dtype=np.float32)
        self.a = np.zeros((size, size), dtype=np.float32)
        self.rng = random.Random(seed)
        self.nprng = np.random.default_rng(seed)

    # -- low level
    def fill(self, rect, color, factor=1.0, noise=0):
        x, y, w, h = rect
        if w <= 0 or h <= 0:
            return
        patch = np.zeros((h, w, 3), dtype=np.float32)
        patch[:] = shade(color, factor)
        if noise:
            patch += self.nprng.integers(-noise, noise + 1, size=(h, w, 1)).astype(np.float32)
        self.rgb[y:y + h, x:x + w] = patch
        self.a[y:y + h, x:x + w] = 1.0

    def put(self, x, y, color, alpha=1.0):
        if 0 <= x < self.size and 0 <= y < self.size:
            cur = self.rgb[y, x]
            self.rgb[y, x] = cur * (1 - alpha) + np.array(color, dtype=np.float32) * alpha
            self.a[y, x] = max(self.a[y, x], alpha)

    def rect_mask(self, rect):
        x, y, w, h = rect
        m = np.zeros((self.size, self.size), dtype=bool)
        m[y:y + h, x:x + w] = True
        return m

    # -- procedural brushes
    def scales(self, rect, base, hi, dark, factor=1.0, cw=9, ch=7, seed_shift=0):
        """Overlapping rounded scales: dark seams, per-scale value jitter, lit crown."""
        x, y, w, h = rect
        if w <= 0 or h <= 0:
            return
        yy, xx = np.mgrid[0:h, 0:w]
        row = yy // ch
        col = (xx + (cw // 2) * (row % 2)) // cw
        u = (xx + (cw // 2) * (row % 2)) % cw
        v = yy % ch
        dx = (u - cw / 2.0) / (cw / 2.0)
        dy = (v - ch * 0.62) / (ch / 2.0)
        d = (dx * dx) * 0.85 + (dy * dy)
        # deterministic per-scale brightness so no two scales match
        hv = (row * 73 + col * 151 + seed_shift * 17) % 23
        jit = 0.80 + hv / 23.0 * 0.45
        base_c = np.array(shade(base, factor), dtype=np.float32)[None, None, :] * jit[..., None]
        hi_c = np.array(shade(hi, factor), dtype=np.float32)[None, None, :] * jit[..., None]
        dark_c = np.array(shade(dark, factor), dtype=np.float32)
        shell = np.empty((h, w, 3), dtype=np.float32)
        shell[:] = dark_c
        body_mask = d <= 1.06
        shell[body_mask] = (base_c * 0.92)[body_mask]
        shell[d <= 0.72] = base_c[d <= 0.72]
        m2 = d <= 0.34
        shell[m2] = (base_c * 0.55 + hi_c * 0.45)[m2]
        m3 = (d <= 0.86) & (dy > 0.38)
        shell[m3] = (hi_c * 0.55 + base_c * 0.45)[m3]
        shell += self.nprng.integers(-3, 4, size=(h, w, 1)).astype(np.float32)
        self.rgb[y:y + h, x:x + w] = shell
        self.a[y:y + h, x:x + w] = 1.0

    def cracks(self, rect, glow, factor=1.0, density=0.16, seed=0):
        """Molten fissures running across a scale patch; mirrored into the glow atlas."""
        x, y, w, h = rect
        if w <= 1 or h <= 1:
            return
        rng = random.Random(1000 + seed)
        yy, xx = np.mgrid[0:h, 0:w]
        mask = np.zeros((h, w), dtype=bool)
        steps = max(2, int((w + h) * density))
        for _ in range(steps):
            px = rng.uniform(0, w - 1)
            py = rng.uniform(0, h - 1)
            ang = rng.uniform(0, math.tau)
            length = rng.uniform(0.25, 0.8) * max(w, h)
            for s in range(int(length)):
                px += math.cos(ang) * 0.9
                py += math.sin(ang) * 0.9
                ang += rng.uniform(-0.35, 0.35)
                ix, iy = int(px), int(py)
                if 0 <= ix < w and 0 <= iy < h:
                    # only crack where there is already hide (keeps alpha edges clean)
                    if self.a[y + iy, x + ix] > 0.5:
                        mask[iy, ix] = True
                if not (0 <= px < w and 0 <= py < h):
                    break
        if not mask.any():
            return
        cold = np.array(shade(CRACK, factor), dtype=np.float32)
        hot = np.array(shade(CRACK_HOT, factor), dtype=np.float32)
        region = self.rgb[y:y + h, x:x + w]
        region[mask] = cold
        # brighter core on a subset for depth
        core = mask & (self.nprng.random((h, w)) > 0.55)
        region[core] = hot
        self.rgb[y:y + h, x:x + w] = region
        if glow is not None:
            g = glow.rgb[y:y + h, x:x + w]
            g[mask] = hot
            g[core] = np.array(VOID_BRIGHT, dtype=np.float32)
            glow.rgb[y:y + h, x:x + w] = g
            glow.a[y:y + h, x:x + w] = np.where(mask | (glow.a[y:y + h, x:x + w] > 0), 1.0, 0.0)

    def ao(self, rect, strength=0.62, depth=4):
        """Darken the bottom rows and brighten the top row of a face."""
        x, y, w, h = rect
        if w <= 0 or h <= 0:
            return
        for i in range(depth):
            f = 1.0 - (1.0 - strength) * (1.0 - i / depth)
            self.rgb[y + h - 1 - i, x:x + w] *= f
        self.rgb[y, x:x + w] = np.clip(self.rgb[y, x:x + w] * 1.10, 0, 255)

    def image(self):
        arr = np.concatenate([np.clip(self.rgb, 0, 255), (np.clip(self.a, 0, 1) * 255)[..., None]], axis=2)
        return Image.fromarray(arr.astype(np.uint8))


# --------------------------------------------------------------- part painting
def paint_hide(cv, glow, part, rects, face, rng, armoured=False):
    rect = rects[face]
    f = FACE_TINT[face]
    cv.scales(rect, HIDE, HIDE_HI, EDGE_DARK, f, cw=10, ch=8)
    if face == "top":
        # spine groove with glowing crack down the middle
        x, y, w, h = rect
        mid = x + w // 2
        cv.rgb[y:y + h, mid - 1:mid + 1] = np.array(shade(RIDGE, f), dtype=np.float32)
        if h > 6:
            glow.rgb[y + 2:y + h - 2, mid] = np.array(VOID, dtype=np.float32)
            glow.a[y + 2:y + h - 2, mid] = 1.0
    if face == "bottom":
        cv.scales(rect, BELLY, mix(BELLY, HIDE_HI, 0.55), BELLY_LINE, f, cw=8, ch=4)
    cv.cracks(rect, glow, f, density=0.10 if face in ("front", "back") else 0.06,
              seed=hash(part["name"] + face) % 9999)
    cv.ao(rect, 0.70 if face in ("front", "back") else 0.55)


def paint_belly(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, BELLY, f, noise=5)
    # horizontal abdominal bars
    for i in range(1, h, 4):
        cv.rgb[y + i, x:x + w] = np.array(shade(BELLY_LINE, f), dtype=np.float32)
        cv.rgb[y + i + 1, x:x + w] = np.array(shade(mix(BELLY, HIDE_MID, 0.5), f), dtype=np.float32)
    cv.ao(rect, 0.62)


def paint_ridge(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    for i in range(h):
        t = i / max(1, h - 1)
        cv.rgb[y + i, x:x + w] = np.array(shade(mix(RIDGE, RIDGE_HI, t), f), dtype=np.float32)
    cv.a[y:y + h, x:x + w] = 1.0
    if face in ("top", "left", "right"):
        glow.rgb[y + 1:y + h:2, x:x + w] = np.array(VOID_BRIGHT, dtype=np.float32) * 0.75
        glow.a[y + 1:y + h:2, x:x + w] = 1.0
    cv.ao(rect, 0.55)


def paint_blade(cv, glow, part, rects, face, rng):
    """Jagged crest / spine blades: dark base fading to a violet-lit edge."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    for i in range(h):
        t = i / max(1, h - 1)
        c = mix(BLADE, mix(BLADE, BLADE_EDGE, 0.85), t)
        cv.rgb[y + i, x:x + w] = np.array(shade(c, f), dtype=np.float32)
    cv.a[y:y + h, x:x + w] = 1.0
    cv.rgb[y:y + h, x:x + w] += rng.integers(-3, 4, size=(h, w, 1)).astype(np.float32)
    if face in ("left", "right", "front", "back"):
        glow.rgb[y, x:x + w] = np.array(BLADE_EDGE, dtype=np.float32)
        glow.a[y, x:x + w] = 1.0
        glow.rgb[y + 1, x:x + w] = np.array(VOID, dtype=np.float32) * 0.6
        glow.a[y + 1, x:x + w] = 1.0
    cv.ao(rect, 0.5)


def paint_membrane(cv, glow, part, rects, face, rng):
    """Dark tattered leather webbed with molten violet ribs (reference wings)."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, MEMBRANE_DEEP, f, noise=6)
    yy, xx = np.mgrid[0:h, 0:w]
    # smoky clouds of lighter membrane
    for _ in range(6):
        cxp, cyp = rng.uniform(0, w), rng.uniform(0, h)
        rr = rng.uniform(max(3.0, w * 0.15), max(5.0, w * 0.45))
        blot = ((xx - cxp) ** 2 + (yy - cyp) ** 2) < rr * rr
        cv.rgb[y:y + h, x:x + w][blot] = np.array(shade(MEMBRANE_MID, f * 0.8), dtype=np.float32)
    # radiating ribs from the wrist corner (left edge) fanning right
    along = xx / max(1, w - 1)
    for k in range(7):
        base_y = (k + 0.5) * h / 7.0
        line = np.abs((yy - (base_y + (along - 0.5) * (h * 0.10))) % (h / 7.0))
        line = np.minimum(line, (h / 7.0) - line)
        width = 1.15 - along * 0.45
        mask = (line < width) & (along > 0.02)
        reg = cv.rgb[y:y + h, x:x + w]
        reg[mask] = np.array(shade(MEMBRANE_VEIN, f), dtype=np.float32)
        cv.rgb[y:y + h, x:x + w] = reg
        if glow is not None:
            g = glow.rgb[y:y + h, x:x + w]
            g[mask] = np.array(CRACK_HOT, dtype=np.float32)
            glow.rgb[y:y + h, x:x + w] = g
            ga = glow.a[y:y + h, x:x + w]
            ga[mask] = 0.9
            # faint inner haze so the wing glows softly, not flat black
            haze = (glow.a[y:y + h, x:x + w] < 0.05) & (rng.random((h, w)) > 0.55)
            g2 = glow.rgb[y:y + h, x:x + w]
            g2[haze] = np.array(CRACK_HOT, dtype=np.float32) * 0.25
            glow.rgb[y:y + h, x:x + w] = g2
            ga[haze] = 0.30
            glow.a[y:y + h, x:x + w] = ga
    # bright leading edge
    edge = xx < max(1, int(w * 0.05))
    cv.rgb[y:y + h, x:x + w][edge] = np.array(shade(MEMBRANE_VEIN, f * 1.1), dtype=np.float32)
    if glow is not None:
        g = glow.rgb[y:y + h, x:x + w]
        g[edge] = np.array(CRACK_HOT, dtype=np.float32)
        glow.rgb[y:y + h, x:x + w] = g
        ga = glow.a[y:y + h, x:x + w]
        ga[edge] = 0.8
        glow.a[y:y + h, x:x + w] = ga
    # torn holes along the trailing edge
    holes = ((xx > w * 0.5) & (rng.random((h, w)) > 0.80))
    region_a = cv.a[y:y + h, x:x + w]
    region_a[holes] = 0.0
    cv.a[y:y + h, x:x + w] = region_a
    cv.ao(rect, 0.7)


def paint_eye(cv, glow, part, rects, face, rng):
    """Molten slit pupil set in a black socket."""
    rect = rects[face]
    x, y, w, h = rect
    cv.fill(rect, (14, 8, 22), 1.0)
    yy, xx = np.mgrid[0:h, 0:w]
    cx, cy = (w - 1) / 2.0, (h - 1) / 2.0
    norm = (((xx - cx) / max(1.0, w * 0.46)) ** 2 + ((yy - cy) / max(1.0, h * 0.34)) ** 2)
    iris = norm <= 1.0
    reg = cv.rgb[y:y + h, x:x + w]
    reg[iris] = np.array(EYE_GLOW, dtype=np.float32)
    slit = (np.abs(xx - cx) < max(0.6, w * 0.09)) & iris
    reg[slit] = np.array((24, 6, 40), dtype=np.float32)
    cv.rgb[y:y + h, x:x + w] = reg
    if glow is not None:
        g = glow.rgb[y:y + h, x:x + w]
        g[iris] = np.array(EYE_GLOW, dtype=np.float32)
        glow.rgb[y:y + h, x:x + w] = g
        ga = glow.a[y:y + h, x:x + w]
        ga[iris] = 1.0
        glow.a[y:y + h, x:x + w] = ga


def paint_wingbone(cv, glow, part, rects, face, rng):
    """Dark leathery wing bones with a faint violet seam."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, MEMBRANE_DEEP, f * 0.9, noise=4)
    cv.cracks(rect, glow, f, density=0.10, seed=hash(part["name"] + face) % 9999)
    cv.ao(rect, 0.85)
    if glow is not None:
        g = glow.rgb[y:y + h, x:x + w]
        g[:] = np.array(shade(CRACK_HOT, f), dtype=np.float32) * 0.22
        glow.rgb[y:y + h, x:x + w] = g
        ga = glow.a[y:y + h, x:x + w]
        ga[:] = np.maximum(ga, 0.34)
        glow.a[y:y + h, x:x + w] = ga


def paint_horn(cv, glow, part, rects, face, rng):
    """Pale bone horns with dark rings, like the reference crown."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    for i in range(h):
        t = i / max(1, h - 1)
        c = mix(BONE_SHADE, BONE_TIP, t)
        cv.rgb[y + i, x:x + w] = np.array(shade(c, f), dtype=np.float32)
    cv.a[y:y + h, x:x + w] = 1.0
    for i in range(0, h, 4):
        cv.rgb[y + i, x:x + w] *= 0.80
    cv.rgb[y:y + h, x:x + w] += rng.integers(-4, 5, size=(h, w, 1)).astype(np.float32)
    if face in ("top", "left", "right"):
        glow.rgb[y:y + h, x:x + w] = np.array(VOID, dtype=np.float32) * 0.35
        glow.a[y:y + h, x:x + w] = 0.45
    cv.ao(rect, 0.72)


def paint_claw(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    for i in range(h):
        t = i / max(1, h - 1)
        cv.rgb[y + i, x:x + w] = np.array(shade(mix((58, 52, 62), BONE, t), f), dtype=np.float32)
    cv.a[y:y + h, x:x + w] = 1.0


def paint_tooth(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    cv.fill(rect, BONE, f, noise=6)
    cv.ao(rect, 0.7)


def paint_skull(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, SKULL, f, noise=4)
    # angular plate facets
    for i in range(h):
        for j in range(0, w, 5):
            if ((i + j) // 4) % 3 == 0:
                cv.rgb[y + i, x + j] = np.array(shade(SKULL_PLATE, f), dtype=np.float32)
    if face == "front" and part["name"] in ("head", "skull_top"):
        # deep-set, slanted eyes + brow ridge
        eye_y = y + max(1, int(h * 0.42))
        eye_w = max(2, int(w * 0.20))
        eye_h = max(1, int(h * 0.10))
        for side in (0, 1):
            ex = x + int(w * (0.13 if side == 0 else 0.67))
            for iy in range(eye_h):
                for ix in range(eye_w):
                    taper = 1.0 if (side == 0 and ix >= eye_w * 0.3) or (side == 1 and ix <= eye_w * 0.7) else 0.4
                    if iy / max(1, eye_h - 1) > taper:
                        continue
                    cv.rgb[eye_y + iy, ex + ix] = np.array((30, 8, 46), dtype=np.float32)
                    glow.rgb[eye_y + iy, ex + ix] = np.array(mix(VOID, EYE_CORE, 1.0 - iy / max(1, eye_h - 1)),
                                                              dtype=np.float32)
                    glow.a[eye_y + iy, ex + ix] = 1.0
            cv.rgb[eye_y - 1, ex - 1:ex + eye_w + 1] *= 0.35
    if face == "front":
        # snout seam
        mid = x + w // 2
        cv.rgb[y + int(h * 0.45):y + h, mid] = np.array(shade(SKULL_PLATE, f * 0.8), dtype=np.float32)
    cv.cracks(rect, glow, f, density=0.05, seed=hash(part["name"] + face) % 9999)
    cv.ao(rect, 0.72)


def paint_muzzle(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, MUZZLE, f, noise=4)
    for i in range(h):
        for j in range(0, w, 4):
            if ((i * 3 + j) // 5) % 4 == 0:
                cv.rgb[y + i, x + j] = np.array(shade(SKULL_PLATE, f), dtype=np.float32)
    if face == "front":
        for fx in (0.24, 0.64):
            nx = x + int(w * fx)
            ny = y + int(h * 0.34)
            cv.rgb[ny:ny + 2, nx:nx + 2] = np.array((14, 10, 20), dtype=np.float32)
    cv.cracks(rect, glow, f, density=0.04, seed=hash(part["name"]) % 9999)
    cv.ao(rect, 0.7)


def paint_jaw(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, JAW, f, noise=3)
    if face in ("top", "bottom"):
        # gums + palate teeth row
        cv.rgb[y, x:x + w] = np.array(shade((70, 24, 44), f), dtype=np.float32)
        for j in range(0, w, 2):
            cv.rgb[y + 1, x + j] = np.array(shade(BONE, f), dtype=np.float32)
    cv.ao(rect, 0.6)


def paint_limb(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    cv.scales(rect, LIMB, mix(LIMB, HIDE_HI, 0.7), EDGE_DARK, f, cw=9, ch=7)
    cv.cracks(rect, glow, f, density=0.06, seed=hash(part["name"] + face) % 9999)
    cv.ao(rect, 0.66)


def paint_foot(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    cv.scales(rect, FOOT, HIDE_MID, EDGE_DARK, f, cw=8, ch=6)
    cv.ao(rect, 0.6)


def paint_small(cv, glow, part, rects, face, rng):
    rect = rects[face]
    cv.fill(rect, (12, 8, 16), FACE_TINT[face])


def paint_saddle(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, LEATHER, f, noise=5)
    if face == "top":
        for i in range(0, h, 3):
            cv.rgb[y + i, x:x + w] = np.array(shade(LEATHER_HI, f), dtype=np.float32)
        mid = y + h // 2
        cv.rgb[mid, x:x + w] = np.array(shade(mix(LEATHER_HI, VOID, 0.35), f), dtype=np.float32)
        glow.rgb[mid, x + 1:x + w - 1] = np.array(VOID, dtype=np.float32) * 0.5
        glow.a[mid, x + 1:x + w - 1] = 0.6
    cv.rgb[y, x:x + w] = np.array(shade(LEATHER_HI, f * 1.2), dtype=np.float32)
    cv.ao(rect, 0.66)


def paint_armor(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, STEEL, f, noise=4)
    for i in range(0, h, 5):
        cv.rgb[y + i, x:x + w] = np.array(shade(STEEL_DARK, f), dtype=np.float32)
        cv.rgb[y + i + 1, x:x + w] = np.array(shade(STEEL_HI, f * 0.85), dtype=np.float32)
    if face in ("top", "left", "right", "front", "back"):
        ry = y + h // 2
        for j in range(0, w, 3):
            cv.rgb[ry, x + j] = np.array(shade(RUNE, f), dtype=np.float32)
            glow.rgb[ry, x + j] = np.array(RUNE, dtype=np.float32) * 0.9
            glow.a[ry, x + j] = 1.0
    for i in range(2, h, 9):
        for j in range(2, w, 9):
            cv.rgb[y + i, x + j] = np.array(STEEL_HI, dtype=np.float32)
    cv.ao(rect, 0.68)


PAINTERS = {
    "hide": paint_hide,
    "belly": paint_belly,
    "ridge": paint_ridge,
    "blade": paint_blade,
    "membrane": paint_membrane,
    "eye": paint_eye,
    "wingbone": paint_wingbone,
    "horn": paint_horn,
    "claw": paint_claw,
    "tooth": paint_tooth,
    "skull": paint_skull,
    "muzzle": paint_muzzle,
    "jaw": paint_jaw,
    "limb": paint_limb,
    "foot": paint_foot,
    "small": paint_small,
    "saddle": paint_saddle,
    "armor": paint_armor,
}


def build_atlas(armoured=False):
    uv = json.load(open(UV_FILE))
    cv = Canvas(TEXTURE_SIZE, seed=4242)
    glow = Canvas(TEXTURE_SIZE, seed=99)
    for part in PARTS:
        box = part["box"]
        if box is None:
            continue
        painter = part["painter"]
        if painter == "pivot":
            continue
        w, h, d = int(round(box[3])), int(round(box[4])), int(round(box[5]))
        if w <= 0 or h <= 0 or d <= 0:
            continue
        u, v = uv[part["name"]][0], uv[part["name"]][1]
        rects = face_rects(u, v, w, h, d)
        rng = np.random.default_rng(abs(hash(part["name"])) % (2 ** 31))
        for face in rects:
            if armoured and painter in ("hide", "belly", "limb", "foot"):
                paint_armor(cv, glow, part, rects, face, rng)
            else:
                PAINTERS[painter](cv, glow, part, rects, face, rng)
    return cv.image(), glow.image()


# ------------------------------------------------------------------- items
def paint_items():
    item_dir = os.path.join(ASSETS, "item")
    os.makedirs(item_dir, exist_ok=True)

    shard = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(shard)
    d.polygon([(8, 0), (13, 5), (12, 13), (8, 15), (4, 13), (3, 5)], fill=(12, 8, 18))
    d.polygon([(8, 1), (12, 5), (11, 12), (8, 14), (5, 12), (4, 5)], fill=(44, 24, 70))
    d.polygon([(8, 1), (12, 5), (8, 9)], fill=(104, 56, 164))
    d.polygon([(8, 1), (4, 5), (8, 9)], fill=(68, 34, 110))
    d.polygon([(8, 9), (11, 12), (8, 14)], fill=(40, 20, 64))
    d.polygon([(8, 9), (5, 12), (8, 14)], fill=(26, 13, 44))
    for p in [(8, 3), (8, 4), (7, 6), (9, 7), (8, 10)]:
        shard.putpixel(p, (*VOID_BRIGHT, 255))
    shard.putpixel((8, 6), (*VOID_PALE, 255))
    shard.putpixel((4, 5), (*VOID, 255))
    shard.save(os.path.join(item_dir, "void_shard.png"))

    saddle = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(saddle)
    d.rounded_rectangle([1, 6, 14, 12], radius=2, fill=(28, 15, 22))
    d.rounded_rectangle([2, 7, 13, 11], radius=2, fill=(62, 34, 52))
    d.line([(2, 9), (13, 9)], fill=(120, 64, 160))
    d.rectangle([6, 2, 9, 6], fill=(22, 20, 30))
    d.rectangle([7, 3, 8, 5], fill=(*VOID_BRIGHT, 255))
    saddle.save(os.path.join(item_dir, "void_saddle.png"))

    armor = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(armor)
    d.polygon([(8, 0), (15, 5), (13, 15), (3, 15), (1, 5)], fill=(18, 17, 26))
    d.polygon([(8, 1), (14, 5), (12, 14), (4, 14), (2, 5)], fill=(46, 44, 66))
    d.polygon([(8, 1), (14, 5), (8, 8)], fill=(70, 68, 96))
    d.polygon([(8, 5), (11, 8), (8, 12), (5, 8)], fill=(*RUNE, 255))
    d.polygon([(8, 6), (10, 8), (8, 11), (6, 8)], fill=(*VOID_BRIGHT, 255))
    armor.save(os.path.join(item_dir, "void_armor.png"))


def paint_hud_icons():
    outdir = os.path.join(ASSETS, "gui")
    os.makedirs(outdir, exist_ok=True)
    shapes = {
        "breath": lambda d, c: (d.line([(7, 25), (25, 9)], fill=c, width=3), d.ellipse([20, 4, 28, 12], fill=c)),
        "rush": lambda d, c: d.polygon([(5, 7), (26, 16), (5, 25), (11, 16)], fill=c),
        "flap": lambda d, c: (d.arc([3, 9, 29, 27], start=200, end=340, fill=c, width=3),
                              d.line([(16, 13), (16, 23)], fill=c, width=3)),
        "roar": lambda d, c: (d.ellipse([10, 10, 22, 22], outline=c, width=2),
                              d.ellipse([14, 14, 18, 18], fill=c)),
        "portal": lambda d, c: (d.ellipse([6, 6, 26, 26], outline=c, width=3),
                                d.ellipse([12, 12, 20, 20], fill=c)),
        "godstorm": lambda d, c: (d.line([(16, 3), (10, 15)], fill=c, width=3),
                                  d.line([(10, 15), (18, 15)], fill=c, width=3),
                                  d.line([(18, 15), (12, 28)], fill=c, width=3)),
        "ascend": lambda d, c: (d.polygon([(16, 3), (23, 14), (16, 28), (9, 14)], fill=None, outline=c),
                                d.line([(16, 6), (16, 25)], fill=c, width=2)),
    }
    for name, fn in shapes.items():
        img = Image.new("RGBA", (32, 32), (0, 0, 0, 0))
        d = ImageDraw.Draw(img)
        d.ellipse([1, 1, 30, 30], fill=(12, 8, 18, 235))
        d.ellipse([1, 1, 30, 30], outline=(82, 48, 120, 255), width=1)
        fn(d, (*VOID_BRIGHT, 255))
        img.filter(ImageFilter.SMOOTH).save(os.path.join(outdir, f"ability_{name}.png"))


def main():
    entity_dir = os.path.join(ASSETS, "entity")
    os.makedirs(entity_dir, exist_ok=True)
    base, glow = build_atlas(False)
    base.save(os.path.join(entity_dir, "void_dragon.png"))
    glow.save(os.path.join(entity_dir, "void_dragon_glow.png"))
    armored, armor_glow = build_atlas(True)
    armored.save(os.path.join(entity_dir, "void_dragon_armored.png"))
    paint_items()
    paint_hud_icons()
    print("textures written ->", entity_dir)


if __name__ == "__main__":
    main()
