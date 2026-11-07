#!/usr/bin/env python3
"""Offline software renderer for the Void Dragon model.

Reads the geometry from tools/dragon_spec.py and the UVs from
tools/atlas_uv.json, so the preview always matches what Minecraft will draw.
Renders a three-quarter, a side and a front view into preview/*.png.

Run:  python3 tools/render_preview.py [--gear] [--flying]
"""
import json
import math
import os
import sys

from PIL import Image, ImageDraw, ImageFilter

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from dragon_spec import PARTS, GEAR, TEXTURE_SIZE  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
TEX = os.path.join(ROOT, "src/main/resources/assets/voiddragon/textures/entity/void_dragon.png")
GLOW = os.path.join(ROOT, "src/main/resources/assets/voiddragon/textures/entity/void_dragon_glow.png")
UVF = os.path.join(ROOT, "tools/atlas_uv.json")
OUTDIR = os.path.join(ROOT, "preview")


# --------------------------------------------------------------- math helpers
def mat_identity():
    return [[1, 0, 0, 0], [0, 1, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]


def mat_mul(a, b):
    return [[sum(a[i][k] * b[k][j] for k in range(4)) for j in range(4)] for i in range(4)]


def mat_translate(x, y, z):
    m = mat_identity()
    m[0][3], m[1][3], m[2][3] = x, y, z
    return m


def mat_rot_x(a):
    c, s = math.cos(a), math.sin(a)
    return [[1, 0, 0, 0], [0, c, -s, 0], [0, s, c, 0], [0, 0, 0, 1]]


def mat_rot_y(a):
    c, s = math.cos(a), math.sin(a)
    return [[c, 0, s, 0], [0, 1, 0, 0], [-s, 0, c, 0], [0, 0, 0, 1]]


def mat_rot_z(a):
    c, s = math.cos(a), math.sin(a)
    return [[c, -s, 0, 0], [s, c, 0, 0], [0, 0, 1, 0], [0, 0, 0, 1]]


def xform(m, p):
    x, y, z = p
    return (m[0][0] * x + m[0][1] * y + m[0][2] * z + m[0][3],
            m[1][0] * x + m[1][1] * y + m[1][2] * z + m[1][3],
            m[2][0] * x + m[2][1] * y + m[2][2] * z + m[2][3])


def pose_matrix(pose):
    m = mat_translate(pose[0], pose[1], pose[2])
    m = mat_mul(m, mat_rot_y(pose[4]))
    m = mat_mul(m, mat_rot_x(pose[3]))
    m = mat_mul(m, mat_rot_z(pose[5]))
    return m


# --------------------------------------------------------------- animation
def animated_poses(parts, flying=False, t=0.0):
    """A neutral idle (or flight) pose overlay, mirroring the Java setupAnim."""
    over = {}
    flap = math.sin(t)
    for part in parts:
        name = part["name"]
        pose = list(part["pose"])
        if flying:
            over["wing_l"] = (0, 0, 0, 0, -0.12, -0.25 - flap * 0.7)
            over["wing_r"] = (0, 0, 0, 0, 0.12, 0.25 + flap * 0.7)
            over["wing_l_fore"] = (0, 0, 0, 0, 0.3, 0)
            over["wing_r_fore"] = (0, 0, 0, 0, -0.3, 0)
            over["leg_fl_thigh"] = (0, 0, 0, -0.35, 0, 0)
            over["leg_fr_thigh"] = (0, 0, 0, -0.35, 0, 0)
            over["leg_bl_thigh"] = (0, 0, 0, 0.3, 0, 0)
            over["leg_br_thigh"] = (0, 0, 0, 0.3, 0, 0)
            over["leg_bl_shin"] = (0, 0, 0, 0.25, 0, 0)
            over["leg_br_shin"] = (0, 0, 0, 0.25, 0, 0)
            over["tail2"] = (0, 0, 0, 0.06 + flap * 0.05, 0, 0)
            over["tail3"] = (0, 0, 0, 0.05 + flap * 0.06, 0, 0)
        else:
            over["wing_l"] = (0, 0, 0, 0, -1.75, 0.35)
            over["wing_r"] = (0, 0, 0, 0, 1.75, -0.35)
            over["wing_l_fore"] = (0, 0, 0, 0, -3.0, 0)
            over["wing_r_fore"] = (0, 0, 0, 0, 3.0, 0)
            over["tail2"] = (0, 0, 0, -0.06, 0.1, 0)
            over["tail3"] = (0, 0, 0, -0.05, 0.16, 0)
            over["tail4"] = (0, 0, 0, -0.03, 0.2, 0)
        if name in over:
            o = over[name]
            pose = [pose[0] + o[0], pose[1] + o[1], pose[2] + o[2],
                    pose[3] + o[3], pose[4] + o[4], pose[5] + o[5]]
        yield part, pose


# --------------------------------------------------------------- geometry
def face_quads(part, uv):
    """Returns list of (corners[4] in model space, uv[4]) for the 6 faces."""
    ox, oy, oz, w, h, d = part["box"]
    u, v = uv[0], uv[1]
    x0, x1 = ox, ox + w
    y0, y1 = oy, oy + h
    z0, z1 = oz, oz + d
    mirror = part["mirror"]

    def uv_front(x, y, z):
        return (u + d + (x - x0), v + d + (y1 - y))

    def uv_back(x, y, z):
        return (u + d + w + (x1 - x), v + d + (y1 - y))

    def uv_right(x, y, z):
        return (u + (z1 - z), v + d + (y1 - y))

    def uv_left(x, y, z):
        return (u + d + w + (z - z0), v + d + (y1 - y))

    def uv_top(x, y, z):
        return (u + d + (x - x0), v + (z - z0))

    def uv_bottom(x, y, z):
        return (u + d + w + (x1 - x), v + (z - z0))

    faces = [
        ([  # front (-z)
            (x0, y0, z0), (x1, y0, z0), (x1, y1, z0), (x0, y1, z0)], uv_front),
        ([  # back (+z)
            (x1, y0, z1), (x0, y0, z1), (x0, y1, z1), (x1, y1, z1)], uv_back),
        ([  # right (+x)
            (x1, y0, z1), (x1, y0, z0), (x1, y1, z0), (x1, y1, z1)], uv_right),
        ([  # left (-x)
            (x0, y0, z0), (x0, y0, z1), (x0, y1, z1), (x0, y1, z0)], uv_left),
        ([  # top (+y)
            (x0, y1, z0), (x1, y1, z0), (x1, y1, z1), (x0, y1, z1)], uv_top),
        ([  # bottom (-y)
            (x0, y0, z1), (x1, y0, z1), (x1, y0, z0), (x0, y0, z0)], uv_bottom),
    ]
    out = []
    for corners, fn in faces:
        uvs = [fn(*c) for c in corners]
        if mirror:
            us = [p[0] for p in uvs]
            span = max(us) + min(us)
            uvs = [(span - p[0], p[1]) for p in uvs]
        out.append((corners, uvs))
    return out


LIGHT = (-0.45, 0.62, -0.65)


def shade_of(normal):
    n = math.sqrt(sum(c * c for c in normal)) or 1.0
    dot = sum((normal[i] / n) * LIGHT[i] for i in range(3))
    return 0.62 + 0.52 * max(0.0, dot)


# --------------------------------------------------------------- renderer
def render_view(yaw_deg, pitch_deg, size=(1000, 780), scale=9.0, distance=95.0,
               flying=False, gear=False, t=0.9, name="three_quarter"):
    tex = Image.open(TEX).convert("RGBA")
    glow = Image.open(GLOW).convert("RGBA")
    uvmap = json.load(open(UVF))
    parts = [p for p in PARTS if gear or p["name"] not in
             (GEAR["saddle"] + GEAR["armor"])]

    # resolve part matrices
    by_name = {p["name"]: p for p in parts}
    world = {}

    def world_of(name, stack=()):
        if name in world:
            return world[name]
        part = by_name[name]
        parent = part["parent"]
        if parent == "root":
            pm = mat_identity()
        else:
            pm = world_of(parent, stack + (name,))
        m = mat_mul(pm, pose_matrix(dict(PARTS_BY_NAME[name]["pose"])))
        world[name] = m
        return m

    global PARTS_BY_NAME
    PARTS_BY_NAME = {p["name"]: p for p in PARTS}

    # animation overrides: build matrices from animated poses
    anim_pose = {part['name']: pose for part, pose in animated_poses(parts, flying=flying, t=t)}
    matrix_cache = {}

    def matrix_for(part):
        name = part["name"]
        if name in matrix_cache:
            return matrix_cache[name]
        parent = part["parent"]
        pm = mat_identity() if parent == "root" else matrix_for(PARTS_BY_NAME[parent]) if parent in \
            {p["name"] for p in parts} else mat_identity()
        m = mat_mul(pm, pose_matrix(anim_pose[name]))
        matrix_cache[name] = m
        return m

    width, height = size
    img = Image.new("RGBA", (width, height), (9, 6, 14, 255))

    # soft violet glow behind the model
    glow_bg = Image.new("RGBA", (width, height), (0, 0, 0, 0))
    gd = ImageDraw.Draw(glow_bg)
    for i in range(26, 0, -1):
        gd.ellipse([width / 2 - i * 20, height / 2 - i * 14,
                    width / 2 + i * 20, height / 2 + i * 14],
                   fill=(40 + i, 22 + i, 66 + i, 4))
    img = Image.alpha_composite(img, glow_bg.filter(ImageFilter.GaussianBlur(6)))

    centre = (0.0, 9.0, 2.0)
    yaw = math.radians(yaw_deg)
    pitch = math.radians(pitch_deg)

    def project(p):
        x, y, z = p[0] - centre[0], p[1] - centre[1], p[2] - centre[2]
        cy, sy = math.cos(yaw), math.sin(yaw)
        x, z = x * cy + z * sy, -x * sy + z * cy
        cp, sp = math.cos(pitch), math.sin(pitch)
        y, z = y * cp - z * sp, y * sp + z * cp
        f = distance / max(1.0, distance + z)
        return (width / 2 + x * scale * f, height / 2 - y * scale * f, z)

    zbuf = [[1e9] * width for _ in range(height)]

    quads = []
    for part, pose in animated_poses(parts, flying=flying, t=t):
        m = matrix_for(part)
        for corners, uvs in face_quads(part, uvmap[part["name"]]):
            pts3 = [xform(m, c) for c in corners]
            proj = [project(p) for p in pts3]
            ax = [pts3[1][i] - pts3[0][i] for i in range(3)]
            bx = [pts3[2][i] - pts3[0][i] for i in range(3)]
            n = (ax[1] * bx[2] - ax[2] * bx[1],
                 ax[2] * bx[0] - ax[0] * bx[2],
                 ax[0] * bx[1] - ax[1] * bx[0])
            # backface culling in view space
            cam = (pts3[0][0] - centre[0], pts3[0][1] - centre[1], pts3[0][2] - centre[2])
            if sum(n[i] * cam[i] for i in range(3)) > 0:
                continue
            quads.append((sum(p[2] for p in proj) / 4.0, proj, uvs, n))

    quads.sort(key=lambda q: q[0])

    pixels = img.load()
    texpx = tex.load()
    glowpx = glow.load()

    for depth, proj, uvs, normal in quads:
        s = shade_of(normal)
        pts = [(p[0], p[1]) for p in proj]
        minx = max(0, int(min(p[0] for p in pts)) - 1)
        maxx = min(width - 1, int(max(p[0] for p in pts)) + 1)
        miny = max(0, int(min(p[1] for p in pts)) - 1)
        maxy = min(height - 1, int(max(p[1] for p in pts)) + 1)
        if maxx <= minx or maxy <= miny:
            continue
        for tri in ((0, 1, 2), (0, 2, 3)):
            a, b, c = tri
            ax_, ay_ = pts[a]
            bx_, by_ = pts[b]
            cx_, cy_ = pts[c]
            den = (by_ - cy_) * (ax_ - cx_) + (cx_ - bx_) * (ay_ - cy_)
            if abs(den) < 1e-7:
                continue
            tminx = max(minx, int(min(ax_, bx_, cx_)) - 1)
            tmaxx = min(maxx, int(max(ax_, bx_, cx_)) + 1)
            tminy = max(miny, int(min(ay_, by_, cy_)) - 1)
            tmaxy = min(maxy, int(max(ay_, by_, cy_)) + 1)
            ua, va = uvs[a]
            ub, vb = uvs[b]
            uc, vc = uvs[c]
            for py in range(tminy, tmaxy + 1):
                for px in range(tminx, tmaxx + 1):
                    w0 = ((by_ - cy_) * (px + 0.5 - cx_) + (cx_ - bx_) * (py + 0.5 - cy_)) / den
                    w1 = ((cy_ - ay_) * (px + 0.5 - cx_) + (ax_ - cx_) * (py + 0.5 - cy_)) / den
                    w2 = 1.0 - w0 - w1
                    if w0 < -0.004 or w1 < -0.004 or w2 < -0.004:
                        continue
                    if depth >= zbuf[py][px]:
                        continue
                    tu = (w0 * ua + w1 * ub + w2 * uc) % TEXTURE_SIZE
                    tv = (w0 * va + w1 * vb + w2 * vc) % TEXTURE_SIZE
                    tp = texpx[int(tu) % TEXTURE_SIZE, int(tv) % TEXTURE_SIZE]
                    if tp[3] < 32:
                        continue
                    zbuf[py][px] = depth
                    col = [min(255, int(tp[i] * s)) for i in range(3)]
                    gp = glowpx[int(tu) % TEXTURE_SIZE, int(tv) % TEXTURE_SIZE]
                    if gp[3] > 40:
                        g = gp[3] / 255.0
                        col = [min(255, int(col[i] * (1 - g * 0.85) + gp[i] * g)) for i in range(3)]
                    pixels[px, py] = (col[0], col[1], col[2], 255)

    # gentle bloom for the emissive bits
    bloom = img.filter(ImageFilter.GaussianBlur(2.4))
    img = Image.blend(img, Image.blend(img, bloom, 0.55), 0.5)

    os.makedirs(OUTDIR, exist_ok=True)
    path = os.path.join(OUTDIR, f"{name}.png")
    img.convert("RGB").save(path)
    print("wrote", path)
    return path


PARTS_BY_NAME = {p["name"]: p for p in PARTS}


def main():
    gear = "--gear" in sys.argv
    flying = "--flying" in sys.argv
    suffix = ("_gear" if gear else "") + ("_flying" if flying else "")
    render_view(38, 14, name=f"void_dragon{suffix}", flying=flying, gear=gear)
    render_view(88, 8, name=f"void_dragon_side{suffix}", flying=flying, gear=gear, scale=8.4)
    render_view(6, 10, name=f"void_dragon_front{suffix}", flying=flying, gear=gear, scale=8.4)


if __name__ == "__main__":
    main()
