#!/usr/bin/env python3
"""Procedurally paints every texture used by the Void Dragon mod.

The dragon model is built from boxes whose UVs live in a 256x256 atlas.  This
script paints those atlas islands (plus the fullbright glow atlas and the
armoured variant) with a coherent void palette: obsidian-black scales, violet
ridge highlights, glowing arcane eyes and rune-like bands on the armour.

Run:  python3 tools/gen_textures.py
"""
import math
import os
import random

from PIL import Image, ImageDraw, ImageFilter

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src/main/resources/assets/voiddragon/textures")

# ----------------------------------------------------------------- palette
DARK = (17, 12, 26)
BODY = (30, 21, 43)
BODY2 = (40, 28, 58)
RIDGE = (66, 42, 100)
RIDGE_HI = (104, 68, 158)
PLATE = (24, 23, 36)
PLATE_HI = (52, 50, 74)
WING_MEMBRANE = (44, 22, 62)
WING_VEIN = (86, 48, 120)
HORN = (78, 70, 96)
HORN_TIP = (150, 140, 172)
BELLY = (48, 40, 62)
VOID = (170, 90, 255)
VOID_BRIGHT = (214, 158, 255)
VOID_PALE = (238, 214, 255)
STEEL = (36, 20, 56)
STEEL_HI = (120, 66, 190)


def clamp(v, lo=0, hi=255):
    return max(lo, min(hi, int(round(v))))


def mix(a, b, t):
    t = max(0.0, min(1.0, t))
    return tuple(clamp(a[i] + (b[i] - a[i]) * t) for i in range(3))


def rand_around(color, amount, rng):
    d = rng.randint(-amount, amount)
    return tuple(clamp(c + d) for c in color)


class Atlas:
    """A 256x256 RGBA canvas with helpers for painting box islands."""

    def __init__(self, size=256, seed=1337):
        self.size = size
        self.img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
        self.px = self.img.load()
        self.rng = random.Random(seed)

    # -- primitives ---------------------------------------------------
    def rect(self, x, y, w, h, color):
        for iy in range(y, min(y + h, self.size)):
            for ix in range(x, min(x + w, self.size)):
                self.px[ix, iy] = (*color, 255) if len(color) == 3 else color

    def noise_rect(self, x, y, w, h, base, amount, rng=None):
        rng = rng or self.rng
        for iy in range(y, min(y + h, self.size)):
            for ix in range(x, min(x + w, self.size)):
                if 0 <= ix < self.size:
                    self.px[ix, iy] = (*rand_around(base, amount, rng), 255)

    def vgrad(self, x, y, w, h, top, bottom):
        for iy in range(h):
            t = iy / max(1, h - 1)
            self.rect(x, y + iy, w, 1, mix(top, bottom, t))

    def hgrad(self, x, y, w, h, left, right):
        for ix in range(w):
            t = ix / max(1, w - 1)
            self.rect(x + ix, y, 1, h, mix(left, right, t))

    def noise(self, x, y, w, h, base, amount):
        self.noise_rect(x, y, w, h, base, amount)

    def scale_pattern(self, x, y, w, h, base, light, dark, step=4, jitter=10):
        """Fish-scale rows: overlapping arcs, darker in the groove, lighter on the ridge."""
        self.noise_rect(x, y, w, h, base, jitter)
        cw, ch = 6, 4
        for iy in range(y, y + h):
            for ix in range(x, x + w):
                row = (iy - y) // ch
                u = (ix - x + (cw // 2) * (row % 2)) % cw
                v = (iy - y) % ch
                # distance from the centre of a scale arc (ellipse)
                d = ((u - cw / 2.0) / (cw / 2.0)) ** 2 + ((v - ch / 2.0) / (ch / 2.0)) ** 2
                if 0.55 <= d <= 0.95:
                    self.px[ix, iy] = (*dark, 255)
                elif d < 0.20:
                    self.px[ix, iy] = (*light, 255)

    # -- box islands ---------------------------------------------------
    def paint_box(self, off, w, h, d, painter="body", rng=None):
        """
        Paints one cube's UV island starting at `off` (texOffs).
        Layout matches the vanilla ModelBox unwrap:
        [d][w][d][w]  /  [d][h] / [d][h] ...
        """
        rng = rng or self.rng
        ox, oy = off
        # side faces (left/right) and front/back
        self._face(ox + d, oy, w, d, painter, rng, "top")
        self._face(ox, oy + d, d, h, painter, rng, "side")
        self._face(ox + d, oy + d, w, h, painter, rng, "side")
        self._face(ox + d + w, oy + d, d, h, painter, rng, "side")
        self._face(ox + d + w + d, oy + d, w, h, painter, rng, "side")
        self._face(ox + d, oy + d + h, w, d, painter, rng, "bottom")

    def _face(self, x, y, w, h, painter, rng, kind):
        if w <= 0 or h <= 0:
            return
        if painter == "body":
            base, light, dark = BODY, RIDGE, DARK
            if kind == "top":
                base, light, dark = BODY2, RIDGE_HI, BODY
            self.scale_pattern(x, y, w, h, base, light, dark, step=max(3, min(5, h // 2)), jitter=9)
            # spine highlight along the middle of top faces
            if kind == "top":
                mx = x + w // 2
                self.rect(mx, y, 1, h, RIDGE)
        elif painter == "belly":
            self.noise(x, y, w, h, BELLY, 8)
            for ix in range(x, x + w):
                if (ix - x) % 5 == 0:
                    self.rect(ix, y, 1, h, mix(BELLY, DARK, 0.5))
        elif painter == "plate":
            self.noise(x, y, w, h, PLATE, 7)
            # riveted plates
            for iy in range(y + 1, y + h, 5):
                for ix in range(x + 1, x + w, 5):
                    self.px[ix, iy] = (*PLATE_HI, 255)
            if kind == "top":
                self.rect(x, y, w, 1, mix(PLATE_HI, VOID, 0.25))
        elif painter == "horn":
            self.vgrad(x, y, w, h, HORN_TIP, HORN)
            self.noise(x, y, w, h, HORN, 10)
        elif painter == "membrane":
            self.noise(x, y, w, h, WING_MEMBRANE, 8)
            # translucent membrane: eyes of the model are alpha cutout, so keep it opaque
            for ix in range(x, x + w):
                for iy in range(y, y + h):
                    dx = (ix - x) / max(1, w)
                    dy = (iy - y) / max(1, h)
                    if abs(math.sin(dx * 9.0 + dy * 4.0)) < 0.08:
                        self.px[ix, iy] = (*WING_VEIN, 255)
        elif painter == "gear":
            self.noise(x, y, w, h, (46, 26, 40), 7)
            if kind == "top":
                self.rect(x, y + h // 2, w, 1, VOID)
                self.noise(x, y, w, h // 2, (58, 33, 50), 6)
        elif painter == "armorplate":
            self.noise(x, y, w, h, PLATE, 8)
            for iy in range(y + 2, y + h - 1, 7):
                for ix in range(x + 2, x + w - 1, 7):
                    self.px[ix, iy] = (*PLATE_HI, 255)
            # rune ridge down the spine of the shell
            mx = x + w // 2
            for iy in range(y + 1, y + h - 1, 2):
                self.px[mx, iy] = (*mix(STEEL, VOID, 0.4), 255)
            # glowing trim on the outer edges
            self.rect(x, y, w, 1, mix(PLATE_HI, VOID, 0.55))
            self.rect(x, y + h - 1, w, 1, mix(PLATE_HI, VOID, 0.35))
        elif painter == "eye":
            self.rect(x, y, w, h, DARK)
            for ix in range(x + w // 3, x + 2 * w // 3 + 1):
                for iy in range(y + h // 3, y + 2 * h // 3 + 1):
                    self.px[ix, iy] = (*VOID_BRIGHT, 255)
            self.px[x + w // 2, y + h // 2] = (*VOID_PALE, 255)


# ----------------------------------------------------------------- model box list
# (name, texOffs, w, h, d, painter)
BODY_BOXES = [
    ("body", (0, 0), 16, 14, 26, "body"),
    *[(f"spike_{i}", (200, 0), 2, 5, 2, "horn") for i in range(5)],
    ("neck1", (0, 40), 8, 8, 6, "body"),
    ("neck1_spike", (200, 0), 2, 5, 2, "horn"),
    ("neck2", (28, 40), 7, 7, 6, "body"),
    ("neck2_spike", (200, 0), 2, 5, 2, "horn"),
    ("neck3", (54, 40), 6, 6, 6, "body"),
    ("neck3_spike", (200, 0), 2, 5, 2, "horn"),
    ("head", (84, 0), 10, 8, 10, "body"),
    ("snout", (124, 0), 6, 4, 6, "body"),
    ("jaw", (148, 0), 8, 2, 8, "belly"),
    ("horn", (180, 0), 2, 2, 8, "horn"),
    ("brow", (180, 0), 2, 2, 5, "horn"),
    ("tail1", (78, 40), 10, 9, 12, "body"),
    ("tail1_spike", (200, 0), 2, 5, 2, "horn"),
    ("tail2", (122, 40), 8, 7, 12, "body"),
    ("tail2_spike", (200, 0), 2, 5, 2, "horn"),
    ("tail3", (162, 40), 6, 5, 12, "body"),
    ("tail3_spike", (200, 0), 2, 4, 2, "horn"),
    ("tail4", (0, 61), 4, 4, 12, "body"),
    ("tail_tip", (198, 40), 6, 1, 8, "horn"),
    ("wing_arm", (32, 61), 14, 3, 3, "body"),
    ("wing_membrane", (0, 82), 18, 1, 16, "membrane"),
    ("wing_arm2", (66, 61), 14, 2, 2, "body"),
    ("wing_membrane2", (0, 82), 14, 1, 12, "membrane"),
    ("wing_claw", (180, 0), 2, 2, 6, "horn"),
    ("leg_fl", (98, 61), 5, 14, 5, "body"),
    ("foot", (142, 61), 5, 2, 4, "plate"),
    ("leg_bl", (118, 61), 6, 14, 6, "body"),
    # gear islands (painted on their own row so they never bleed into the dragon)
    ("saddle_seat", (100, 120), 12, 2, 14, "gear"),
    ("saddle_pommel", (100, 140), 4, 3, 4, "gear"),
    ("saddle_cantle", (116, 140), 5, 2, 3, "gear"),
    ("armor_shell", (0, 140), 17, 15, 27, "armorplate"),
]


def paint_atlas(size=256, armor=False):
    a = Atlas(size, seed=9001 if armor else 1337)
    for name, off, w, h, d, painter in BODY_BOXES:
        if armor and painter in ("body", "belly"):
            painter = "plate" if name not in ("wing_membrane",) else "membrane"
        a.paint_box(off, w, h, d, painter)
        if armor and painter == "plate":
            # glowing rune band across plates
            ox, oy = off
            for ix in range(ox + d, ox + d + w):
                if (ix - ox) % 4 == 0:
                    a.px[ix, oy + d + h // 2] = (*VOID, 255)
    return a.img


def _island_rects():
    """Rough atlas rectangles occupied by the model, used to clip glow painting."""
    rects = []
    for name, off, w, h, d, painter in BODY_BOXES:
        ox, oy = off
        rects.append((ox, oy, d + 2 * w + d, d + h + d))
    return rects


def paint_glow(size=256):
    """Fullbright overlay: only the parts that should emit light are opaque."""
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    rng = random.Random(77)
    rects = _island_rects()

    def inside(x, y):
        return any(rx <= x < rx + rw and ry <= y < ry + rh for rx, ry, rw, rh in rects)

    # Eyes: front face of the head island (head box is 10x8x10 at 84,0 -> front face 84,10).
    head_front = (84 + 10, 0 + 10, 10, 8)
    hx, hy, hw, hh = head_front
    for ex in (hx + 2, hx + 2 + 4):
        for i in range(2):
            img.putpixel((ex + i, hy + 3), (*VOID_PALE, 255))
            img.putpixel((ex + i, hy + 4), (*VOID_BRIGHT, 235))
        img.putpixel((ex, hy + 2), (*VOID, 170))

    # Veins: a handful of smooth sine streaks over the body, neck and tail islands.
    for (ox, oy, rw, rh) in [(0, 0, 74, 58), (0, 40, 60, 30), (78, 40, 96, 26), (0, 61, 60, 20)]:
        for i in range(3):
            phase = rng.random() * 6.28
            amp = 2.0 + rng.random() * 3.0
            for x in range(ox, min(ox + rw, size)):
                yy = int(oy + rh * (0.25 + 0.25 * i) + math.sin((x - ox) * 0.28 + phase) * amp)
                if oy <= yy < oy + rh:
                    img.putpixel((x, yy), (*VOID, 175))
                    if rng.random() < 0.22:
                        img.putpixel((x, max(oy, yy - 1)), (*VOID_BRIGHT, 110))

    # Faint motes only inside the atlas islands.
    for _ in range(420):
        x, y = rng.randrange(size), rng.randrange(size)
        if inside(x, y) and rng.random() < 0.7:
            img.putpixel((x, y), (*VOID_BRIGHT, rng.randint(60, 150)))

    # Horn tips glow.
    for (ox, oy, rw, rh) in [(180, 0, 12, 12), (200, 0, 8, 9)]:
        for x in range(ox, ox + rw):
            for y in range(oy, oy + rh):
                if (x + y) % 4 == 0:
                    img.putpixel((x, y), (*VOID_BRIGHT, 120))
    return img


def paint_shard(size=16):
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    # faceted crystal
    body = [(8, 1), (13, 6), (11, 14), (5, 14), (3, 6)]
    d.polygon(body, fill=DARK)
    d.polygon([(8, 2), (11, 6), (8, 13), (5, 6)], fill=(58, 32, 88))
    d.polygon([(8, 2), (10, 6), (8, 13)], fill=(96, 52, 146))
    d.polygon([(5, 6), (8, 13), (8, 2)], fill=(42, 22, 66))
    d.line([(8, 2), (8, 13)], fill=VOID)
    img.putpixel((8, 4), (*VOID_BRIGHT, 255))
    img.putpixel((7, 7), (*VOID_PALE, 255))
    img.putpixel((9, 9), (*VOID, 255))
    d.rectangle([0, 0, 0, 0], fill=(0, 0, 0, 0))
    return img


def paint_saddle(size=16):
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.rounded_rectangle([1, 5, 14, 12], radius=2, fill=(38, 22, 34))
    d.rounded_rectangle([2, 6, 13, 10], radius=2, fill=(58, 34, 52))
    d.line([(2, 8), (13, 8)], fill=VOID)
    d.rectangle([6, 2, 9, 5], fill=(30, 30, 44))
    d.rectangle([7, 3, 8, 4], fill=VOID_BRIGHT)
    d.line([(1, 11), (0, 14)], fill=(24, 18, 28))
    d.line([(14, 11), (15, 14)], fill=(24, 18, 28))
    return img


def paint_armor_item(size=16):
    img = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    d = ImageDraw.Draw(img)
    d.polygon([(8, 1), (14, 5), (12, 14), (4, 14), (2, 5)], fill=PLATE)
    d.line([(8, 1), (14, 5), (12, 14)], fill=PLATE_HI)
    d.line([(8, 1), (2, 5), (4, 14)], fill=(20, 18, 30))
    d.line([(8, 4), (8, 12)], fill=STEEL)
    d.polygon([(8, 5), (10, 8), (8, 11), (6, 8)], fill=VOID)
    img.putpixel((8, 8), (*VOID_PALE, 255))
    img.putpixel((7, 4), (*VOID_BRIGHT, 255))
    img.putpixel((9, 12), (*VOID, 255))
    return img


def paint_hud_icons(outdir):
    """36x18 ability icons used by the custom HUD (drawn programmatically in code, kept as reference)."""
    os.makedirs(outdir, exist_ok=True)
    for name, color in (("breath", VOID), ("rush", VOID_BRIGHT), ("flap", (150, 190, 255)),
                        ("roar", (200, 90, 255)), ("portal", (120, 220, 255))):
        img = Image.new("RGBA", (32, 32), (0, 0, 0, 0))
        d = ImageDraw.Draw(img)
        d.ellipse([1, 1, 30, 30], outline=(*DARK, 220), width=2)
        d.ellipse([3, 3, 28, 28], fill=(*DARK, 170))
        if name == "breath":
            d.line([(8, 22), (24, 10)], fill=color, width=3)
            d.ellipse([20, 6, 27, 13], fill=color)
        elif name == "rush":
            d.polygon([(6, 8), (24, 16), (6, 24), (11, 16)], fill=color)
        elif name == "flap":
            d.arc([4, 8, 28, 28], start=200, end=340, fill=color, width=3)
            d.line([(16, 12), (16, 22)], fill=color, width=3)
        elif name == "roar":
            d.ellipse([10, 10, 22, 22], outline=color, width=2)
            d.ellipse([14, 14, 18, 18], fill=color)
            for r in (4, 7, 10):
                d.arc([16 - r, 16 - r, 16 + r, 16 + r], start=300, end=60, fill=color)
        else:
            d.ellipse([7, 7, 25, 25], outline=color, width=3)
            d.ellipse([12, 12, 20, 20], fill=color)
        img.save(os.path.join(outdir, f"ability_{name}.png"))


def main():
    entity_dir = os.path.join(ASSETS, "entity")
    item_dir = os.path.join(ASSETS, "item")
    os.makedirs(entity_dir, exist_ok=True)
    os.makedirs(item_dir, exist_ok=True)

    paint_atlas(256, armor=False).save(os.path.join(entity_dir, "void_dragon.png"))
    paint_atlas(256, armor=True).save(os.path.join(entity_dir, "void_dragon_armored.png"))
    paint_glow(256).save(os.path.join(entity_dir, "void_dragon_glow.png"))

    paint_shard().save(os.path.join(item_dir, "void_shard.png"))
    paint_saddle().save(os.path.join(item_dir, "void_saddle.png"))
    paint_armor_item().save(os.path.join(item_dir, "void_armor.png"))
    paint_hud_icons(os.path.join(ASSETS, "gui"))
    print("textures written to", ASSETS)


if __name__ == "__main__":
    main()
