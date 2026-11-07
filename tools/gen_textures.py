#!/usr/bin/env python3
"""Paints every texture the Void Dragon mod uses, straight from the model spec.

The 512x512 entity atlas is painted face-by-face using the UV islands packed by
tools/gen_model.py (tools/atlas_uv.json), so scales line up with the geometry,
eyes land on the front of the skull, veins follow the wing membranes and the
armour plates wrap the torso correctly.

Outputs
  textures/entity/void_dragon.png           base atlas
  textures/entity/void_dragon_armored.png   armoured variant
  textures/entity/void_dragon_glow.png      fullbright overlay (eyes, veins, runes)
  textures/item/*.png                       shard / saddle / armour icons
  textures/gui/ability_*.png                HUD ability icons

Run:  python3 tools/gen_textures.py
"""
import json
import math
import os
import random
import sys

from PIL import Image, ImageDraw, ImageFilter

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from dragon_spec import PARTS, TEXTURE_SIZE  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src/main/resources/assets/voiddragon/textures")
UV_FILE = os.path.join(ROOT, "tools/atlas_uv.json")

# ------------------------------------------------------------------ palette
BLACK = (10, 7, 15)
HIDE = (52, 40, 74)
HIDE_DARK = (26, 18, 38)
HIDE_LIGHT = (92, 70, 128)
SCALE_EDGE = (28, 20, 42)
RIDGE = (74, 44, 116)
RIDGE_HI = (118, 74, 176)
BELLY = (60, 50, 78)
BELLY_LINE = (38, 30, 50)
MEMBRANE = (52, 26, 76)
MEMBRANE_DARK = (34, 16, 50)
MEMBRANE_VEIN = (92, 48, 132)
HORN = (96, 88, 116)
HORN_TIP = (176, 166, 196)
FIN = (68, 36, 102)
FIN_EDGE = (128, 78, 186)
SKULL = (52, 40, 72)
SKULL_PLATE = (78, 58, 106)
JAW = (56, 46, 72)
LIMB = (38, 28, 54)
FOOT = (30, 22, 44)

VOID = (168, 92, 255)
VOID_BRIGHT = (214, 158, 255)
VOID_PALE = (240, 222, 255)
EYE_CORE = (232, 200, 255)

LEATHER = (54, 30, 44)
LEATHER_HI = (84, 48, 66)
STEEL = (44, 42, 62)
STEEL_HI = (96, 92, 124)
STEEL_DARK = (24, 22, 34)
RUNE = (150, 80, 240)


def clamp(v, lo=0, hi=255):
    return max(lo, min(hi, int(round(v))))


def mix(a, b, t):
    t = max(0.0, min(1.0, t))
    return tuple(clamp(a[i] + (b[i] - a[i]) * t) for i in range(3))


def jitter(color, amount, rng, alpha=255):
    return (clamp(color[0] + rng.randint(-amount, amount)),
            clamp(color[1] + rng.randint(-amount, amount)),
            clamp(color[2] + rng.randint(-amount, amount)), alpha)


def shade(color, f, alpha=255):
    return (clamp(color[0] * f), clamp(color[1] * f), clamp(color[2] * f), alpha)


# face rectangles in the box unwrap -------------------------------------------
def face_rects(u, v, w, h, d):
    w, h, d = int(w), int(h), int(d)
    return {
        "top":    (u + d, v, w, d),
        "bottom": (u + d + w, v, w, d),
        "right":  (u, v + d, d, h),
        "front":  (u + d, v + d, w, h),
        "left":   (u + d + w, v + d, d, h),
        "back":   (u + d + w + d, v + d, w, h),
    }


FACE_SHADE = {"top": 1.10, "bottom": 0.72, "front": 1.0, "back": 0.92, "left": 0.88, "right": 0.96}


class Canvas:
    def __init__(self, size):
        self.size = size
        self.img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
        self.px = self.img.load()
        self.rng = random.Random(4242)

    def put(self, x, y, color, alpha=255):
        if 0 <= x < self.size and 0 <= y < self.size:
            if alpha >= 255:
                self.px[x, y] = (color[0], color[1], color[2], 255)
            else:
                base = self.px[x, y]
                a = alpha / 255.0
                self.px[x, y] = (clamp(base[0] * (1 - a) + color[0] * a),
                                 clamp(base[1] * (1 - a) + color[1] * a),
                                 clamp(base[2] * (1 - a) + color[2] * a),
                                 max(base[3], alpha))

    def fill(self, rect, color, factor=1.0, rng_amount=0, rng=None):
        x, y, w, h = rect
        rng = rng or self.rng
        for iy in range(y, y + h):
            for ix in range(x, x + w):
                c = shade(color, factor)
                if rng_amount:
                    c = jitter(c, rng_amount, rng)
                self.put(ix, iy, c)

    def border(self, rect, color, factor=0.55):
        x, y, w, h = rect
        for ix in range(x, x + w):
            self.put(ix, y, shade(color, factor))
            self.put(ix, y + h - 1, shade(color, factor * 0.85))
        for iy in range(y, y + h):
            self.put(x, iy, shade(color, factor))
            self.put(x + w - 1, iy, shade(color, factor * 0.9))


# ------------------------------------------------------------------ patterns
def soft_ao(canvas, rect, strength=0.85, depth=3):
    """Gentle darkening toward the bottom edge of a face - reads as ambient occlusion."""
    x, y, w, h = rect
    for i in range(depth):
        f = strength + (1.0 - strength) * (i / depth)
        for ix in range(x, x + w):
            c = canvas.px[ix, y + h - 1 - i]
            canvas.put(ix, y + h - 1 - i, shade(c[:3], f, c[3]))
    for ix in range(x, x + w):
        c = canvas.px[ix, y]
        canvas.put(ix, y, shade(c[:3], 1.06, c[3]))

def scales(canvas, rect, rng, base, light, dark, cw=6, ch=4):
    """Overlapping fish scales, brighter on the leading edge, dark in the groove."""
    x, y, w, h = rect
    for iy in range(y, y + h):
        for ix in range(x, x + w):
            row = (iy - y) // ch
            u = (ix - x + (cw // 2) * (row % 2)) % cw
            v = (iy - y) % ch
            dx = (u - cw / 2.0) / (cw / 2.0)
            dy = (v - ch / 2.0) / (ch / 2.0)
            d = dx * dx + dy * dy
            if d > 1.25:
                c = mix(base, dark, 0.55)
            elif d > 0.8:
                c = mix(base, dark, 0.85)
            elif d < 0.18:
                c = mix(base, light, 0.5)
            else:
                c = mix(base, light, 0.08)
            canvas.put(ix, iy, jitter(c, 3, rng))


def plate_rows(canvas, rect, rng, base, hi, dark, step=6):
    x, y, w, h = rect
    for iy in range(y, y + h):
        for ix in range(x, x + w):
            band = (iy - y) // step
            local = (iy - y) % step
            c = base
            if local == 0:
                c = dark
            elif local == 1:
                c = mix(base, hi, 0.35)
            if (ix - x + band * 3) % step == 0:
                c = mix(c, dark, 0.55)
            canvas.put(ix, iy, jitter(c, 5, rng))


def membrane_texture(canvas, rect, rng, origin_corner="left", mirror=False):
    """Dark translucent-looking membrane with veins fanning out from the arm."""
    x, y, w, h = rect
    canvas.fill(rect, MEMBRANE, 1.0, 6, rng)
    # veins fan from one edge toward the opposite trailing edge
    fan_x = x if origin_corner == "left" else x + w
    for i in range(7):
        ty = y + 1 + i * max(1, (h - 2) // 6)
        steps = max(w, h) * 2
        for s in range(steps):
            t = s / steps
            px = int(fan_x + (w - 2) * t * (1 if fan_x == x else -1))
            py = int(y + 1 + (ty - y) * (0.15 + 0.85 * t))
            if y <= py < y + h and x <= px < x + w:
                canvas.put(px, py, MEMBRANE_VEIN, 200)
                if s % 9 == 0:
                    canvas.put(px, py + 1, mix(MEMBRANE_VEIN, MEMBRANE, 0.4), 150)
    # scalloped trailing edge
    for ix in range(x, x + w):
        for dy in range(2):
            wave = int(1.5 * math.sin((ix - x) * 0.7)) + dy
            py = y + h - 1 - wave
            canvas.put(ix, py, mix(MEMBRANE, MEMBRANE_DARK, 0.35))
    canvas.border(rect, MEMBRANE_DARK, 0.85)


def fin_texture(canvas, rect, rng, vertical=True):
    x, y, w, h = rect
    for iy in range(y, y + h):
        t = (iy - y) / max(1, h - 1)
        for ix in range(x, x + w):
            base = mix(FIN, mix(FIN, BLACK, 0.6), t)
            canvas.put(ix, iy, jitter(base, 5, rng))
    # rim highlight on the top/outer edge
    for ix in range(x, x + w):
        canvas.put(ix, y, mix(FIN, FIN_EDGE, 0.85))
        canvas.put(ix, y + 1, mix(FIN, FIN_EDGE, 0.45))
    canvas.border(rect, BLACK, 0.9)


def horn_texture(canvas, rect, rng, from_base=True):
    x, y, w, h = rect
    for iy in range(y, y + h):
        t = (iy - y) / max(1, h - 1)
        c = mix(HORN, HORN_TIP, t if from_base else 1 - t)
        for ix in range(x, x + w):
            canvas.put(ix, iy, jitter(c, 5, rng))
    # growth ridges
    for iy in range(y, y + h, 4):
        for ix in range(x, x + w):
            canvas.put(ix, iy, shade(mix(HORN, HORN_TIP, 0.4), 0.72))
    canvas.border(rect, HORN, 0.7)


def skull_face(canvas, rect, rng, glow_canvas=None):
    """Front face of the head: brow ridges, angular eyes and a nasal ridge."""
    x, y, w, h = rect
    canvas.fill(rect, SKULL, 1.0, 5, rng)
    # dorsal plates on the forehead
    for iy in range(y, y + h // 2):
        for ix in range(x, x + w):
            if ((ix - x) + (iy - y)) % 6 == 0:
                canvas.put(ix, iy, mix(SKULL, SKULL_PLATE, 0.8))
    # eye sockets (wider, angular, angled forward-down)
    eye_y = y + int(h * 0.38)
    eye_w = max(3, int(w * 0.24))
    eye_h = max(2, int(h * 0.16))
    for side in (0, 1):
        ex = x + int(w * (0.14 if side == 0 else 0.60))
        for iy in range(eye_h):
            for ix in range(eye_w):
                # taper the inner corner for an angry slant
                taper = 1.0 if (side == 0 and ix > eye_w * 0.25) or (side == 1 and ix < eye_w * 0.75) else 0.45
                if iy / max(1, eye_h - 1) > taper:
                    continue
                canvas.put(ex + ix, eye_y + iy, shade(BLACK, 1.0))
                if glow_canvas is not None:
                    core = mix(VOID_BRIGHT, EYE_CORE, 1.0 - (iy / max(1, eye_h - 1)))
                    glow_canvas.put(ex + ix, eye_y + iy, core, 255)
        # brow shadow
        for ix in range(ex - 1, ex + eye_w + 1):
            canvas.put(ix, eye_y - 1, shade(BLACK, 1.0))
    # nasal ridge
    mid = x + w // 2
    for iy in range(eye_y + eye_h, y + h):
        canvas.put(mid, iy, mix(SKULL, SKULL_PLATE, 0.55))
        canvas.put(mid - 1, iy, shade(SKULL, 0.8))
    canvas.border(rect, BLACK, 0.8)


def snout_face(canvas, rect, rng, glow_canvas=None):
    x, y, w, h = rect
    canvas.fill(rect, SKULL, 0.95, 5, rng)
    # nostrils
    for fx in (0.28, 0.66):
        nx = x + int(w * fx)
        ny = y + int(h * 0.30)
        for iy in range(2):
            for ix in range(2):
                canvas.put(nx + ix, ny + iy, shade(BLACK, 0.9))
    # plate seams
    for iy in range(y, y + h, 5):
        for ix in range(x, x + w):
            canvas.put(ix, iy, shade(SKULL, 0.7), 120)
    canvas.border(rect, BLACK, 0.85)


def paint_part(canvas, glow, part, uv, armored=False):
    name = part["name"]
    painter = part["painter"]
    box = part["box"]
    if box is None or painter == "pivot":
        return
    w, h, d = int(box[3]), int(box[4]), int(box[5])
    u, v = uv[0], uv[1]
    rects = face_rects(u, v, w, h, d)
    rng = canvas.rng

    if painter == "none":
        return

    for face, rect in rects.items():
        if rect[2] <= 0 or rect[3] <= 0:
            continue
        f = FACE_SHADE[face]
        if painter in ("body", "limb", "foot"):
            base = {"body": HIDE, "limb": LIMB, "foot": FOOT}[painter]
            light = HIDE_LIGHT
            dark = SCALE_EDGE
            scales(canvas, rect, rng, shade(base, f), shade(light, f), shade(dark, f))
            if face == "top" and painter == "body":
                # spine groove along the middle of the back
                mx = rect[0] + rect[2] // 2
                for iy in range(rect[1], rect[1] + rect[3]):
                    canvas.put(mx, iy, shade(RIDGE, f))
                    canvas.put(mx - 1, iy, shade(mix(RIDGE, HIDE, 0.5), f))
                    canvas.put(mx + 1, iy, shade(mix(RIDGE, HIDE, 0.5), f))
                    if glow is not None and iy % 3 == 0:
                        glow.put(mx, iy, VOID, 120)
            if face == "bottom" and painter == "body":
                scales(canvas, rect, rng, shade(BELLY, f), shade(mix(BELLY, HIDE_LIGHT, 0.6), f),
                       shade(BELLY_LINE, f), cw=7, ch=4)
            soft_ao(canvas, rect, 0.82)
        elif painter == "belly":
            plate_rows(canvas, rect, rng, shade(BELLY, f), shade(mix(BELLY, HIDE_LIGHT, 0.7), f),
                       shade(BELLY_LINE, f), step=4)
            soft_ao(canvas, rect, 0.86)
        elif painter == "ridge":
            for iy in range(rect[1], rect[1] + rect[3]):
                t = (iy - rect[1]) / max(1, rect[3] - 1)
                c = mix(RIDGE, RIDGE_HI, t)
                for ix in range(rect[0], rect[0] + rect[2]):
                    canvas.put(ix, iy, jitter(shade(c, f), 6, rng))
            canvas.border(rect, BLACK, 0.9)
        elif painter == "fin":
            fin_texture(canvas, rect, rng)
            if glow is not None:
                # glowing edge along the top of the sail
                for ix in range(rect[0], rect[0] + rect[2]):
                    glow.put(ix, rect[1], FIN_EDGE, 130)
        elif painter == "membrane":
            membrane_texture(canvas, rect, rng, origin_corner="left" if not part["mirror"] else "right")
            if glow is not None:
                for ix in range(rect[0], rect[0] + rect[2]):
                    if (ix - rect[0]) % 3 == 0:
                        glow.put(ix, rect[1], VOID, 70)
        elif painter == "horn":
            horn_texture(canvas, rect, rng)
            if glow is not None and rect[3] > 3:
                for ix in range(rect[0], rect[0] + rect[2]):
                    glow.put(ix, rect[1], VOID_BRIGHT, 90)
        elif painter == "skull":
            if face == "front" and name == "head":
                skull_face(canvas, rect, rng, glow)
            elif face == "front" and name == "snout":
                snout_face(canvas, rect, rng, glow)
            else:
                scales(canvas, rect, rng, shade(SKULL, f), shade(SKULL_PLATE, f), shade(BLACK, f),
                       cw=7, ch=5)
            canvas.border(rect, BLACK, 0.85)
        elif painter == "jaw":
            canvas.fill(rect, JAW, f, 6, rng)
            # teeth along the front edge of the lower jaw
            if face in ("left", "right", "front"):
                ty = rect[1] + 1
                for ix in range(rect[0] + 1, rect[0] + rect[2] - 1, 2):
                    canvas.put(ix, ty, mix(HORN_TIP, (255, 255, 255), 0.4))
            canvas.border(rect, BLACK, 0.85)
        elif painter == "saddle":
            canvas.fill(rect, LEATHER, f, 7, rng)
            if face == "top":
                plate_rows(canvas, rect, rng, shade(LEATHER_HI, f), shade(mix(LEATHER_HI, VOID, 0.3), f),
                           shade(LEATHER, f), step=5)
                for ix in range(rect[0] + 1, rect[0] + rect[2] - 1, 4):
                    canvas.put(ix, rect[1] + rect[3] // 2, mix(VOID, LEATHER, 0.25))
            for ix in range(rect[0], rect[0] + rect[2]):
                canvas.put(ix, rect[1], mix(LEATHER_HI, VOID, 0.4))
            canvas.border(rect, shade(LEATHER, 0.6), 0.9)
        elif painter == "armor":
            plate_rows(canvas, rect, rng, shade(STEEL, f), shade(STEEL_HI, f), shade(STEEL_DARK, f), step=7)
            # glowing rune band
            if face in ("left", "right", "front", "back", "top"):
                ry = rect[1] + rect[3] // 2
                for ix in range(rect[0] + 1, rect[0] + rect[2] - 1):
                    if (ix - rect[0]) % 3 != 2:
                        canvas.put(ix, ry, mix(RUNE, STEEL, 0.15))
                        if glow is not None:
                            glow.put(ix, ry, RUNE, 190)
            # rivets
            for iy in range(rect[1] + 2, rect[1] + rect[3] - 1, 9):
                for ix in range(rect[0] + 2, rect[0] + rect[2] - 1, 9):
                    canvas.put(ix, iy, STEEL_HI)
            canvas.border(rect, STEEL_DARK, 0.85)


def build_atlas(armored=False):
    uv = json.load(open(UV_FILE))
    canvas = Canvas(TEXTURE_SIZE)
    glow = Canvas(TEXTURE_SIZE)
    for part in PARTS:
        paint_part(canvas, glow, part, uv[part["name"]], armored)
    return canvas.img, glow.img


def paint_items():
    item_dir = os.path.join(ASSETS, "item")
    os.makedirs(item_dir, exist_ok=True)

    # ---- Void Shard: a faceted crystal with an inner void glow
    shard = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(shard)
    d.polygon([(8, 0), (13, 5), (12, 13), (8, 15), (4, 13), (3, 5)], fill=(14, 9, 21))
    d.polygon([(8, 1), (12, 5), (11, 12), (8, 14), (5, 12), (4, 5)], fill=(46, 26, 74))
    d.polygon([(8, 1), (12, 5), (8, 9)], fill=(96, 54, 150))
    d.polygon([(8, 1), (4, 5), (8, 9)], fill=(64, 34, 102))
    d.polygon([(8, 9), (11, 12), (8, 14)], fill=(38, 20, 60))
    d.polygon([(8, 9), (5, 12), (8, 14)], fill=(28, 15, 46))
    for p in [(8, 3), (8, 4), (7, 5), (9, 6), (8, 8)]:
        shard.putpixel(p, (*VOID_BRIGHT, 255))
    shard.putpixel((8, 6), (*VOID_PALE, 255))
    shard.putpixel((4, 5), (*VOID, 255))
    shard.putpixel((11, 12), (*RUNE, 255))
    shard.save(os.path.join(item_dir, "void_shard.png"))

    # ---- Void Saddle
    saddle = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(saddle)
    d.rounded_rectangle([1, 6, 14, 12], radius=2, fill=(30, 17, 24))
    d.rounded_rectangle([2, 7, 13, 11], radius=2, fill=(66, 38, 56))
    d.line([(2, 9), (13, 9)], fill=(120, 64, 160))
    d.rectangle([6, 2, 9, 6], fill=(24, 22, 32))
    d.rectangle([7, 3, 8, 5], fill=(*VOID_BRIGHT, 255))
    d.line([(1, 12), (0, 15)], fill=(40, 24, 34))
    d.line([(14, 12), (15, 15)], fill=(40, 24, 34))
    d.point([(3, 8), (12, 8)], fill=(150, 88, 196))
    saddle.save(os.path.join(item_dir, "void_saddle.png"))

    # ---- Void Dragon Armor
    armor = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    d = ImageDraw.Draw(armor)
    d.polygon([(8, 0), (15, 5), (13, 15), (3, 15), (1, 5)], fill=(20, 19, 30))
    d.polygon([(8, 1), (14, 5), (12, 14), (4, 14), (2, 5)], fill=(48, 46, 68))
    d.polygon([(8, 1), (14, 5), (8, 8)], fill=(72, 70, 98))
    d.line([(8, 4), (8, 13)], fill=(30, 26, 44))
    d.polygon([(8, 5), (11, 8), (8, 12), (5, 8)], fill=(*RUNE, 255))
    d.polygon([(8, 6), (10, 8), (8, 11), (6, 8)], fill=(*VOID_BRIGHT, 255))
    armor.putpixel((8, 8), (*VOID_PALE, 255))
    for p in [(3, 6), (12, 7), (4, 12), (11, 12)]:
        armor.putpixel(p, (120, 116, 150, 255))
    armor.save(os.path.join(item_dir, "void_armor.png"))


def paint_hud_icons():
    outdir = os.path.join(ASSETS, "gui")
    os.makedirs(outdir, exist_ok=True)
    spec = {
        "breath": lambda d, c: (d.line([(7, 25), (25, 9)], fill=c, width=3),
                                d.ellipse([20, 4, 28, 12], fill=c)),
        "rush": lambda d, c: d.polygon([(5, 7), (26, 16), (5, 25), (11, 16)], fill=c),
        "flap": lambda d, c: (d.arc([3, 9, 29, 27], start=200, end=340, fill=c, width=3),
                              d.line([(16, 13), (16, 23)], fill=c, width=3)),
        "roar": lambda d, c: (d.ellipse([10, 10, 22, 22], outline=c, width=2),
                              d.ellipse([14, 14, 18, 18], fill=c),
                              [d.arc([16 - r, 16 - r, 16 + r, 16 + r], start=300, end=60, fill=c)
                               for r in (5, 8, 11)]),
        "portal": lambda d, c: (d.ellipse([6, 6, 26, 26], outline=c, width=3),
                                d.ellipse([12, 12, 20, 20], fill=c)),
    }
    for name, fn in spec.items():
        img = Image.new("RGBA", (32, 32), (0, 0, 0, 0))
        d = ImageDraw.Draw(img)
        d.ellipse([1, 1, 30, 30], fill=(14, 9, 21, 235))
        d.ellipse([1, 1, 30, 30], outline=(80, 50, 118, 255), width=1)
        fn(d, (*VOID_BRIGHT, 255))
        img = img.filter(ImageFilter.SMOOTH)
        img.save(os.path.join(outdir, f"ability_{name}.png"))


def main():
    entity_dir = os.path.join(ASSETS, "entity")
    os.makedirs(entity_dir, exist_ok=True)

    base, glow = build_atlas(armored=False)
    base.save(os.path.join(entity_dir, "void_dragon.png"))
    glow.save(os.path.join(entity_dir, "void_dragon_glow.png"))

    armored, armor_glow = build_atlas(armored=True)
    armored.save(os.path.join(entity_dir, "void_dragon_armored.png"))

    paint_items()
    paint_hud_icons()
    print("textures written:", ASSETS, "(atlas", TEXTURE_SIZE, "px)")


if __name__ == "__main__":
    main()
