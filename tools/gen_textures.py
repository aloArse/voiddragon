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
from dragon_spec import PARTS, TEXTURE_SIZE, TEXELS_PER_UNIT  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src/main/resources/assets/voiddragon/textures")
UV_FILE = os.path.join(ROOT, "tools/atlas_uv.json")

# ------------------------------------------------------------------- palette
OBSIDIAN = (26, 22, 36)
# Hide: matte blue-black slate, a touch of colour so form reads on a dark model.
HIDE = (48, 40, 72)
HIDE_MID = (62, 52, 92)
HIDE_HI = (100, 86, 140)
EDGE_DARK = (22, 18, 34)
CRACK = (150, 92, 214)
CRACK_HOT = (214, 150, 255)

# Ventral plates: warmer, lighter, clearly a different material.
BELLY = (78, 70, 96)
BELLY_LINE = (32, 26, 48)

# Dorsal ridge / spurs: darker slate to separate them from the hide.
RIDGE = (36, 30, 54)
RIDGE_HI = (78, 64, 116)

# Blades (spurs, crest, spade): dark bone-slate, bright edge.
BLADE = (54, 46, 76)
BLADE_EDGE = (176, 112, 250)

# Wing membrane: the darkest surface on the model, so it can silhouette.
MEMBRANE_DEEP = (26, 16, 40)
MEMBRANE_MID = (48, 32, 74)
MEMBRANE_VEIN = (128, 82, 196)

# Bone: pale warm grey. The only light material, which is what sells the horns.
BONE = (206, 204, 208)
BONE_SHADE = (150, 148, 158)
BONE_TIP = (240, 240, 246)
EYE_GLOW = (212, 158, 255)

# Skull: same slate as the hide but cooler and with pale plate edges.
SKULL = (54, 46, 78)
SKULL_PLATE = (92, 80, 124)
MUZZLE = (58, 49, 82)
JAW = (42, 35, 60)

LIMB = (46, 39, 68)
FOOT = (34, 28, 50)

VOID = (168, 92, 255)
VOID_BRIGHT = (214, 158, 255)
VOID_PALE = (242, 226, 255)
EYE_CORE = (238, 210, 255)

LEATHER = (52, 30, 44)
LEATHER_HI = (84, 50, 68)
STEEL = (44, 42, 62)
STEEL_HI = (98, 94, 128)
STEEL_DARK = (24, 22, 32)
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
        """Large overlapping plates.

        A dragon hide reads at gameplay distance because the plates are big and
        calm, not because every texel is noisy. Each plate gets one flat tone from
        a small palette, a lit top lip and a dark seam, which keeps the surface
        legible even when the whole texture is 40 px wide on screen.
        """
        x, y, w, h = rect
        if w <= 0 or h <= 0:
            return
        yy, xx = np.mgrid[0:h, 0:w]
        row = yy // ch
        col = (xx + (cw // 2) * (row % 2)) // cw
        u = (xx + (cw // 2) * (row % 2)) % cw
        v = yy % ch
        # only 4 tones, chosen per plate -- flat facets instead of gradients
        hv = (row * 31 + col * 57 + seed_shift * 13) % 4
        tones = np.array([0.86, 1.00, 1.10, 0.94], dtype=np.float32)[hv]
        base_c = np.array(shade(base, factor), dtype=np.float32)[None, None, :]
        hi_c = np.array(shade(hi, factor), dtype=np.float32)[None, None, :]
        dark_c = np.array(shade(dark, factor), dtype=np.float32)
        reg = base_c * tones[..., None]
        # lit upper lip and hard dark seam at the bottom of each plate
        reg[v <= 1] = hi_c * tones[..., None][v <= 1]
        reg[v >= ch - 1] = dark_c
        reg[u >= cw - 1] = dark_c
        # a single soft highlight in the middle of the plate
        mid = (((u - cw * 0.45) / (cw * 0.42)) ** 2 + ((v - ch * 0.45) / (ch * 0.5)) ** 2) < 0.55
        reg[mid] = (base_c * (tones[..., None] + 0.14))[mid]
        self.rgb[y:y + h, x:x + w] = np.clip(reg, 0, 255)
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

    def streaks(self, rect, glow, factor=1.0, count=3, seed=0):
        """Long molten fissures running along the long axis of a face.

        Short random walks read as noise at model scale; fissures that travel a
        good fraction of the face read as cracked stone.
        """
        x, y, w, h = rect
        if w <= 2 or h <= 1:
            return
        rng = random.Random(5000 + seed)
        horizontal = w >= h
        span = w if horizontal else h
        cross = h if horizontal else w
        mask = np.zeros((h, w), dtype=bool)
        for _ in range(count):
            if horizontal:
                py = rng.uniform(1, max(2, cross - 1))
                px = rng.uniform(0, span * 0.25)
            else:
                px = rng.uniform(1, max(2, cross - 1))
                py = rng.uniform(0, span * 0.25)
            ang = 0.0 if horizontal else math.pi / 2
            length = span * rng.uniform(0.45, 1.0)
            steps = int(length)
            for _s in range(steps):
                ang += rng.uniform(-0.16, 0.16)
                px += math.cos(ang) * 1.0
                py += math.sin(ang) * 1.0
                ix, iy = int(px), int(py)
                if 0 <= ix < w and 0 <= iy < h:
                    mask[iy, ix] = True
                    # occasional side branch
                    if rng.random() > 0.93:
                        bx, by = ix, iy
                        for _b in range(rng.randint(2, max(3, int(cross * 0.5)))):
                            bx += rng.choice((-1, 1))
                            by += rng.choice((-1, 0, 1))
                            if 0 <= bx < w and 0 <= by < h:
                                mask[by, bx] = True
                if not (0 <= px < w and 0 <= py < h):
                    break
        if not mask.any():
            return
        cold = np.array(shade(CRACK, factor), dtype=np.float32)
        hot = np.array(shade(CRACK_HOT, factor), dtype=np.float32)
        region = self.rgb[y:y + h, x:x + w]
        region[mask] = cold
        core = mask & (self.nprng.random((h, w)) > 0.45)
        region[core] = hot
        self.rgb[y:y + h, x:x + w] = region
        if glow is not None:
            g = glow.rgb[y:y + h, x:x + w]
            g[mask] = hot
            g[core] = np.array(VOID_BRIGHT, dtype=np.float32)
            glow.rgb[y:y + h, x:x + w] = g
            ga = glow.a[y:y + h, x:x + w]
            ga[mask] = 0.16
            ga[core] = 0.30
            glow.a[y:y + h, x:x + w] = ga

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
    rx, ry, rw, rh = rect
    cv.scales(rect, HIDE, HIDE_HI, EDGE_DARK, f, cw=max(10, rw // 3), ch=max(8, rh // 4))
    if face == "top":
        # spine groove with glowing crack down the middle
        x, y, w, h = rect
        mid = x + w // 2
        cv.rgb[y:y + h, mid - 1:mid + 1] = np.array(shade(RIDGE, f), dtype=np.float32)
        if h > 6:
            glow.rgb[y + 2:y + h - 2, mid] = np.array(VOID, dtype=np.float32)
            glow.a[y + 2:y + h - 2, mid] = 1.0
    if face == "bottom":
        # underside stays dark: only the plated belly strips are light
        cv.scales(rect, mix(HIDE, BELLY, 0.22), mix(HIDE_MID, BELLY, 0.30), EDGE_DARK,
                  f, cw=16, ch=8)
    # one single fissure on the biggest faces only, and it glows faintly: the
    # hide must stay matte or the whole dragon turns into lavender confetti
    cv.ao(rect, 0.70 if face in ("front", "back") else 0.55)


def paint_belly(cv, glow, part, rects, face, rng):
    """Ventral plates: overlapping bone-coloured scutes stacked nose-to-tail.

    They are drawn as rows of rounded plates with a lit upper lip and a dark
    shadow beneath, so the belly reads as armour rather than stripes.
    """
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    if w <= 0 or h <= 0:
        return
    yy, xx = np.mgrid[0:h, 0:w]
    cv.fill(rect, BELLY, f, noise=4)
    row_h = max(3.0, h / 5.0)
    chin = np.array(shade(mix(BELLY, BONE, 0.18), f * 1.02), dtype=np.float32)
    deep = np.array(shade(mix(BELLY, EDGE_DARK, 0.55), f), dtype=np.float32)
    seam = np.array(shade(BELLY_LINE, f), dtype=np.float32)
    reg = cv.rgb[y:y + h, x:x + w].copy()
    v = (yy % row_h) / row_h                      # 0 at the top lip of a plate
    plate = np.floor(yy / row_h).astype(np.int32)
    bulge = np.clip(1.0 - v, 0.0, 1.0) ** 1.5
    reg[:] = deep[None, None, :] + (chin - deep)[None, None, :] * bulge[..., None] * 0.9
    # staggered vertical seams so plates interlock
    seam_x = (xx + (plate % 2) * (w / 6.0)) % max(3.0, w / 3.0)
    reg[seam_x < 0.8] = seam[None, :][: int((seam_x < 0.8).sum())] if False else seam
    reg[v < 0.09] = chin
    reg[v > 0.88] = seam
    reg += cv.nprng.integers(-3, 4, size=(h, w, 1)).astype(np.float32)
    cv.rgb[y:y + h, x:x + w] = reg
    cv.a[y:y + h, x:x + w] = 1.0
    if glow is not None:
        # a faint violet glow seeps from between the plates
        g = glow.rgb[y:y + h, x:x + w]
        ga = glow.a[y:y + h, x:x + w]
        gap = (v > 0.93)
        g[gap] = np.array(CRACK_HOT, dtype=np.float32) * 0.12
        ga[gap] = 0.05
        glow.rgb[y:y + h, x:x + w] = g
        glow.a[y:y + h, x:x + w] = ga
    cv.ao(rect, 0.60)


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
    """Horn / spine blades: obsidian root with a molten violet edge that glows."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    yy, xx = np.mgrid[0:h, 0:w]
    t = np.zeros((h, w), dtype=np.float32)
    if face in ("left", "right"):
        t = yy / max(1.0, h - 1.0)              # tip at the bottom of the side faces
    elif face in ("front", "back"):
        t = yy / max(1.0, h - 1.0)
    else:
        t = xx / max(1.0, w - 1.0)               # tips point outward on top/bottom
    base = np.array(shade(BLADE, f), dtype=np.float32)[None, None, :]
    edge = np.array(shade(BLADE_EDGE, f), dtype=np.float32)[None, None, :]
    reg = base + (edge - base) * (t[..., None] ** 2.6)
    reg += rng.integers(-4, 5, size=(h, w, 1)).astype(np.float32)
    # a bright rim exactly on the tip edge
    rim = t > 0.94
    reg[rim] = np.array(BLADE_EDGE, dtype=np.float32) * 0.85
    cv.rgb[y:y + h, x:x + w] = reg
    cv.a[y:y + h, x:x + w] = 1.0
    if glow is not None:
        g = glow.rgb[y:y + h, x:x + w]
        g[:] = np.array(CRACK_HOT, dtype=np.float32) * (t[..., None] ** 2.0) * 0.9
        glow.rgb[y:y + h, x:x + w] = g
        ga = glow.a[y:y + h, x:x + w]
        ga[:] = np.clip(t ** 2.0, 0.0, 1.0) * 0.75
        glow.a[y:y + h, x:x + w] = ga
    cv.ao(rect, 0.45)


def paint_membrane(cv, glow, part, rects, face, rng):
    """Wing leather: near-black violet sheet, lit from the leading edge.

    The wing has to read as *cloth* first and magic second, so the base is a
    smooth vertical gradient with soft mottling, the structure comes from dark
    bone ribs fanning out of the wrist, and the molten light is confined to the
    root third plus a thin seam along the leading edge.
    """
    x, y, w, h = rects[face]
    if w <= 0 or h <= 0:
        return
    f = FACE_TINT[face]
    yy, xx = np.mgrid[0:h, 0:w]
    along = xx / max(1.0, w - 1.0)          # 0 = wrist (root), 1 = wing tip
    across = yy / max(1.0, h - 1.0)         # 0 = leading edge, 1 = trailing edge

    # smooth gradient: brighter at the root and along the leading edge
    lum = 1.00 - 0.46 * along - 0.30 * across
    base = np.array(MEMBRANE_DEEP, dtype=np.float32)[None, None, :]
    lit = np.array(MEMBRANE_MID, dtype=np.float32)[None, None, :]
    sheet = base + (lit - base) * np.clip(lum, 0.0, 1.0)[..., None]

    # soft mottled clouds so the leather is not a flat ramp
    noise = np.zeros((h, w), dtype=np.float32)
    for _ in range(5):
        cxp, cyp = rng.uniform(0, w), rng.uniform(0, h)
        rx, ry = rng.uniform(w * 0.12, w * 0.38) + 1.0, rng.uniform(h * 0.18, h * 0.5) + 1.0
        blob = np.exp(-(((xx - cxp) / rx) ** 2 + ((yy - cyp) / ry) ** 2))
        noise += blob * rng.uniform(-0.10, 0.14)
    sheet *= (1.0 + noise)[..., None]
    sheet *= f

    # dark bone ribs fanning from the wrist through the leather
    ribs = max(2, int(h / 3))
    for k in range(ribs):
        c = (k + 0.5) * h / ribs
        spread = (along - 0.4) * (h * 0.55)
        dist = np.abs(yy - (c + spread))
        rib = np.clip(1.0 - dist / 0.95, 0.0, 1.0)
        shade_v = 0.40 + 0.25 * along
        sheet *= (1.0 - 0.55 * rib * shade_v)[..., None]

    # leading edge: a taut pale seam of bone
    edge = np.clip(1.0 - across / 1.5, 0.0, 1.0)
    seam = np.array(MEMBRANE_VEIN, dtype=np.float32)[None, None, :] * 0.42
    sheet = sheet * (1.0 - edge[..., None] * 0.5) + seam * edge[..., None] * 0.5

    # molten cracks: only in the root third, thin and irregular
    root = along < 0.34
    crack = np.zeros((h, w), dtype=np.float32)
    for _ in range(4):
        px = rng.uniform(0, w * 0.32)
        py = rng.uniform(0, h)
        ang = rng.uniform(-0.9, 0.9)
        for _s in range(int(max(4, w * 0.3))):
            px += math.cos(ang) * 0.8
            py += math.sin(ang) * 0.8
            ang += rng.uniform(-0.30, 0.30)
            ix, iy = int(px), int(py)
            if 0 <= ix < w and 0 <= iy < h and root[min(iy, h - 1), min(ix, w - 1)]:
                crack[iy, ix] = 1.0
    cv.rgb[y:y + h, x:x + w] = sheet
    cv.a[y:y + h, x:x + w] = 1.0
    hot = np.array(CRACK_HOT, dtype=np.float32)
    if crack.any():
        reg = cv.rgb[y:y + h, x:x + w]
        reg[crack > 0.5] = hot * 0.85
        cv.rgb[y:y + h, x:x + w] = reg
    if glow is not None:
        g = glow.rgb[y:y + h, x:x + w]
        ga = glow.a[y:y + h, x:x + w]
        g[crack > 0.5] = hot
        ga[crack > 0.5] = 0.9
        # faint light bleeding out of the ribs near the root
        bleed = np.clip(1.0 - along / 0.45, 0.0, 1.0) * 0.10
        g += hot[None, None, :] * bleed[..., None]
        ga = np.maximum(ga, bleed)
        glow.rgb[y:y + h, x:x + w] = g
        glow.a[y:y + h, x:x + w] = ga

    # ragged trailing edge + a few worn holes towards the tip
    ragged = (across > 0.94) & (rng.random((h, w)) > 0.82 + 0.14 * along)
    holes = (rng.random((h, w)) > 0.988) & (across > 0.45) & (along > 0.55)
    drop = ragged | holes
    if drop.any():
        a = cv.a[y:y + h, x:x + w]
        a[drop] = 0.0
        cv.a[y:y + h, x:x + w] = a
        if glow is not None:
            ga = glow.a[y:y + h, x:x + w]
            ga[drop] = 0.0
            glow.a[y:y + h, x:x + w] = ga

    cv.ao(rects[face], 0.72)


def paint_eye(cv, glow, part, rects, face, rng):
    """A molten violet eye: lens-shaped iris, black socket, vertical slit pupil."""
    rect = rects[face]
    x, y, w, h = rect
    if w <= 0 or h <= 0:
        return
    yy, xx = np.mgrid[0:h, 0:w]
    fx = (xx - (w - 1) / 2.0) / max(1.0, w / 2.0)
    fy = (yy - (h - 1) / 2.0) / max(1.0, h / 2.0)
    # an almond, not a disc: narrow at the corners, tall in the middle
    lens = fx * fx + (fy / (0.92 - 0.35 * np.abs(fx))) ** 2
    hot = np.array(EYE_CORE, dtype=np.float32)
    mid = np.array(EYE_GLOW, dtype=np.float32)
    rim = np.array(VOID, dtype=np.float32) * 0.55
    reg = np.empty((h, w, 3), dtype=np.float32)
    reg[:] = (9, 4, 14)
    m1 = lens < 1.0
    m2 = lens < 0.55
    m3 = lens < 0.22
    reg[m1] = rim
    reg[m2] = mid
    reg[m3] = hot
    slit = (np.abs(fx) < 0.16) & m1
    reg[slit] = (12, 3, 20)
    # a bright spark in the upper corner of the iris
    spark = ((fx + 0.28) ** 2 + (fy - 0.34) ** 2) < 0.045
    reg[spark] = hot
    cv.rgb[y:y + h, x:x + w] = reg
    cv.a[y:y + h, x:x + w] = 1.0
    if glow is not None:
        g = np.zeros((h, w, 3), dtype=np.float32)
        ga = np.zeros((h, w), dtype=np.float32)
        g[m1] = rim
        g[m2] = mid
        g[m3] = hot
        ga[m1] = 0.45
        ga[m2] = 0.90
        ga[m3] = 1.0
        ga[slit] = 0.0
        g[slit] = 0.0
        g[spark] = hot
        ga[spark] = 1.0
        glow.rgb[y:y + h, x:x + w] = g
        glow.a[y:y + h, x:x + w] = ga


def paint_wingbone(cv, glow, part, rects, face, rng):
    """Wing bones: dark ash-grey bone, lighter along the ridge, with one thin
    ember seam. The bones must stay dark or the whole wing turns into a glow slab."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    if w <= 0 or h <= 0:
        return
    yy, xx = np.mgrid[0:h, 0:w]
    # lighter along the length, darker at the cross-section edges
    along = (xx if w >= h else yy) / max(1.0, (w if w >= h else h) - 1.0)
    across = (yy if w >= h else xx) / max(1.0, (h if w >= h else w) - 1.0)
    mid = np.exp(-((across - 0.5) / 0.34) ** 2)
    dark = np.array((34, 28, 44), dtype=np.float32)
    lit = np.array((96, 88, 112), dtype=np.float32)
    reg = dark + (lit - dark) * (0.30 + 0.70 * mid)[..., None]
    reg *= (0.80 + 0.20 * along)[..., None]
    # bone rings every few texels
    reg[(along * max(2.0, (w if w >= h else h) / 3.0) % 1.0) < 0.10] *= 0.86
    reg += rng.integers(-5, 6, size=(h, w, 1)).astype(np.float32)
    cv.rgb[y:y + h, x:x + w] = np.clip(reg * f, 0, 255)
    cv.a[y:y + h, x:x + w] = 1.0
    cv.ao(rect, 0.70)
    if glow is not None:
        # a single hairline of ember down the middle, nothing more
        g = np.zeros((h, w, 3), dtype=np.float32)
        ga = np.zeros((h, w), dtype=np.float32)
        seam = np.abs(across - 0.5) < 0.055
        g[seam] = np.array(CRACK_HOT, dtype=np.float32)
        ga[seam] = 0.55
        glow.rgb[y:y + h, x:x + w] = g
        glow.a[y:y + h, x:x + w] = ga


def paint_horn(cv, glow, part, rects, face, rng):
    """Pale bone horn: cool ivory with growth rings and a molten seam at the root."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    if w <= 0 or h <= 0:
        return
    yy, xx = np.mgrid[0:h, 0:w]
    # the horn's long axis is its Z depth, which lands on the wide side of the
    # unwrapped island, so pick the gradient axis from the island's aspect
    long_ax = xx if w >= h else yy
    across = yy if w >= h else xx
    span = (w if w >= h else h)
    t = long_ax / max(1.0, span - 1.0)
    # cool ivory at the tip, warm shadow at the root -- never flat cream
    root_c = np.array((120, 118, 130), dtype=np.float32)
    mid_c = np.array(BONE_SHADE, dtype=np.float32)
    tip_c = np.array(BONE_TIP, dtype=np.float32)
    reg = np.empty((h, w, 3), dtype=np.float32)
    t3 = t[..., None]
    reg[:] = root_c + (mid_c - root_c) * np.clip(t3 * 3.0, 0, 1)
    reg[:] = reg + (tip_c - reg) * np.clip((t3 - 0.35) / 0.65, 0, 1)
    # growth rings across the horn
    ring = ((t * max(3.0, span / 3.0)) % 1.0) < 0.12
    reg[ring] *= 0.82
    # a shallow groove along the shaft, darker, so the cylinder reads as round
    ctr = (h if w >= h else w) / 2.0
    groove = np.exp(-((across - ctr) / max(1.2, ctr * 0.30)) ** 2)
    reg *= (1.0 - 0.30 * groove[..., None])
    # mottling so it looks grown, not moulded
    reg += rng.integers(-6, 7, size=(h, w, 1)).astype(np.float32)
    # molten seam at the root
    seam = (t < 0.16) & (np.abs(across - ctr) < max(1.0, ctr * 0.35))
    reg[seam] = np.array(BLADE_EDGE, dtype=np.float32) * 0.92
    cv.rgb[y:y + h, x:x + w] = np.clip(reg * f, 0, 255)
    cv.a[y:y + h, x:x + w] = 1.0
    if glow is not None:
        g = glow.rgb[y:y + h, x:x + w]
        ga = glow.a[y:y + h, x:x + w]
        g[:] = 0.0
        ga[:] = 0.0
        g[seam] = np.array(CRACK_HOT, dtype=np.float32)
        ga[seam] = 0.85
        glow.rgb[y:y + h, x:x + w] = g
        glow.a[y:y + h, x:x + w] = ga
    cv.ao(rect, 0.50)


def paint_claw(cv, glow, part, rects, face, rng):
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    for i in range(h):
        t = i / max(1, h - 1)
        cv.rgb[y + i, x:x + w] = np.array(shade(mix((58, 52, 62), BONE, t), f), dtype=np.float32)
    cv.a[y:y + h, x:x + w] = 1.0


def paint_tooth(cv, glow, part, rects, face, rng):
    """Needle teeth: ivory at the point, dark and wet at the gum line."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    if w <= 0 or h <= 0:
        return
    yy, xx = np.mgrid[0:h, 0:w]
    # the teeth strip is drawn along the jaw, so the gum runs along the short axis
    t = (yy if h >= w else xx) / max(1.0, (h if h >= w else w) - 1.0)
    gum = np.array((46, 40, 54), dtype=np.float32)
    body = np.array((192, 190, 196), dtype=np.float32)
    tip = np.array(BONE_TIP, dtype=np.float32)
    reg = gum + (body - gum) * np.clip(t[..., None] * 2.4, 0, 1)
    reg = reg + (tip - reg) * np.clip((t[..., None] - 0.55) / 0.45, 0, 1)
    # vertical tooth separations
    reg[(xx % max(2.0, w / 6.0)) < 0.7] *= 0.55
    reg += rng.integers(-5, 6, size=(h, w, 1)).astype(np.float32)
    cv.rgb[y:y + h, x:x + w] = np.clip(reg * f, 0, 255)
    cv.a[y:y + h, x:x + w] = 1.0
    if glow is not None:
        glow.a[y:y + h, x:x + w] = 0.0
    cv.ao(rect, 0.60)


def paint_skull(cv, glow, part, rects, face, rng):
    """Angular obsidian skull plating with a molten seam and eye sockets."""
    rect = rects[face]
    f = FACE_TINT[face]
    x, y, w, h = rect
    cv.fill(rect, SKULL, f, noise=5)
    yy, xx = np.mgrid[0:h, 0:w]
    # broad angular facets rather than a regular pattern
    facets = (((xx // 6) * 7 + (yy // 5) * 11 + (xx + yy) // 9) % 5)
    tone = np.choose(np.clip(facets, 0, 4), [0.0, 0.25, 0.45, 0.6, 0.9])[..., None]
    base = np.array(shade(SKULL, f), dtype=np.float32)[None, None, :]
    plate = np.array(shade(SKULL_PLATE, f), dtype=np.float32)[None, None, :]
    reg = base + (plate - base) * tone.astype(np.float32)
    reg += rng.integers(-4, 5, size=(h, w, 1)).astype(np.float32)
    cv.rgb[y:y + h, x:x + w] = reg
    # dark separation lines between facets + one running fissure
    seam = ((xx % 6) == 0) | ((yy % 5) == 0)
    seam &= rng.random((h, w)) > 0.55
    reg2 = cv.rgb[y:y + h, x:x + w]
    reg2[seam] *= 0.55
    cv.rgb[y:y + h, x:x + w] = reg2
    cv.cracks(rect, glow, f, density=0.06, seed=hash(part["name"] + face) % 9999)
    cv.ao(rect, 0.78)
    # NOTE: do not paint eye sockets here. The eyes are separate geometry with
    # their own emissive texture; painting a socket on the skull only produced a
    # bright blob on the cheek that swallowed the real eye.
    del yy, xx


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
        w = max(1, int(round(box[3] * TEXELS_PER_UNIT)))
        h = max(1, int(round(box[4] * TEXELS_PER_UNIT)))
        d = max(1, int(round(box[5] * TEXELS_PER_UNIT)))
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
