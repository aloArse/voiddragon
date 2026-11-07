#!/usr/bin/env python3
"""Textured software renderer for previewing the real in-game skin.

Unlike tools/render_preview.py (which approximates face mapping), this one uses
**Minecraft's exact box-UV layout** ("cube net" corners) for all six faces, so the
preview shows precisely what the texture will look like in game. It also applies
the emissive glow layer.

Run: python3 tools/render_textured.py [--gear] [--flying] [--stage adult]
"""
import argparse
import json
import math
import os
import sys

import numpy as np
from PIL import Image, ImageFilter

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from dragon_spec import PARTS, GEAR, TEXTURE_SIZE  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
ASSETS = os.path.join(ROOT, "src/main/resources/assets/voiddragon/textures")
UVF = os.path.join(ROOT, "tools/atlas_uv.json")
OUTDIR = os.path.join(ROOT, "preview")


# ------------------------------------------------------------------ matrices
def m_id():
    return np.eye(4)


def m_trans(x, y, z):
    m = np.eye(4)
    m[0, 3], m[1, 3], m[2, 3] = x, y, z
    return m


def m_rx(a):
    c, s = math.cos(a), math.sin(a)
    m = np.eye(4)
    m[1, 1], m[1, 2], m[2, 1], m[2, 2] = c, -s, s, c
    return m


def m_ry(a):
    c, s = math.cos(a), math.sin(a)
    m = np.eye(4)
    m[0, 0], m[0, 2], m[2, 0], m[2, 2] = c, s, -s, c
    return m


def m_rz(a):
    c, s = math.cos(a), math.sin(a)
    m = np.eye(4)
    m[0, 0], m[0, 1], m[1, 0], m[1, 1] = c, -s, s, c
    return m


def pose_matrix(pose):
    # matches ModelPart.translateAndRotate -> translate * rotateZYX(z, y, x)
    m = m_trans(pose[0], pose[1], pose[2])
    m = m @ m_rz(pose[5])
    m = m @ m_ry(pose[4])
    m = m @ m_rx(pose[3])
    return m


def apply(m, pts):
    pts = np.asarray(pts, dtype=float)
    h = np.concatenate([pts, np.ones((len(pts), 1))], axis=1)
    return (m @ h.T).T[:, :3]


# ------------------------------------------------------- Minecraft cube net
def face_data(part, uv):
    """Six faces with MC's real cube-net corner order and UV coordinates."""
    ox, oy, oz, w, h, d = part["box"]
    u, v = uv[0], uv[1]
    x0, x1 = ox, ox + w
    y0, y1 = oy, oy + h
    z0, z1 = oz, oz + d
    mirror = part["mirror"]

    faces = {
        # name: (corners in MC order, uv per corner in MC order)
        "front": ([(x0, y1, z0), (x1, y1, z0), (x1, y0, z0), (x0, y0, z0)],
                  [(u + d, v + d), (u + d + w, v + d), (u + d + w, v + d + h), (u + d, v + d + h)]),
        "back": ([(x1, y1, z1), (x0, y1, z1), (x0, y0, z1), (x1, y0, z1)],
                 [(u + d + w + d, v + d), (u + 2 * d + w, v + d), (u + d + w + d, v + d), (u + 2 * d + w, v + d)]),
        "right": ([(x1, y1, z1), (x1, y1, z0), (x1, y0, z0), (x1, y0, z1)],
                  [(u, v + d), (u + d, v + d), (u + d, v + d + h), (u, v + d + h)]),
        "left": ([(x0, y1, z0), (x0, y1, z1), (x0, y0, z1), (x0, y0, z0)],
                 [(u + d + w, v + d), (u + d + w + d, v + d), (u + d + w + d, v + d + h), (u + d + w, v + d + h)]),
        "top": ([(x0, y0, z1), (x1, y0, z1), (x1, y0, z0), (x0, y0, z0)],
                [(u + d + w, v), (u + d + w, v + d), (u + d, v + d), (u + d, v)]),
        "bottom": ([(x0, y1, z0), (x1, y1, z0), (x1, y1, z1), (x0, y1, z1)],
                   [(u + d, v), (u + d + w, v), (u + d + w, v + d), (u + d, v + d)]),
    }
    if mirror:
        out = {}
        for name, (corners, uvs) in faces.items():
            us = [p[0] for p in uvs]
            span = max(us) + min(us)
            out[name] = (corners, [(span - a, b) for a, b in uvs])
        faces = out
    return faces


# -------------------------------------------------------------- animation
def animated(parts, flying=False, gear=False, t=0.0, stage_scale=1.0):
    out = []
    flap = math.sin(t * 2.4)
    for part in parts:
        name = part["name"]
        if part["box"] is None:
            pose = list(part["pose"])
        else:
            pose = list(part["pose"])
        if not gear and name in GEAR["saddle"] + GEAR["armor"]:
            continue
        over = {}
        if flying:
            # wings sweep out to the sides and beat; legs tuck; tail streams back
            over = {
                "wing_l": (0, 0, 0, -0.05 + flap * 0.10, 0.10, -0.10 + flap * 0.34),
                "wing_r": (0, 0, 0, -0.05 + flap * 0.10, -0.10, 0.10 - flap * 0.34),
                "wing_l_fore": (0, 0, 0, 0.0, 0.02, 0.06 + flap * 0.12),
                "wing_r_fore": (0, 0, 0, 0.0, -0.02, -0.06 - flap * 0.12),
                "leg_fl_thigh": (0, 0, 0, -0.34, 0, 0),
                "leg_fr_thigh": (0, 0, 0, -0.34, 0, 0),
                "leg_bl_thigh": (0, 0, 0, 0.28, 0, 0),
                "leg_br_thigh": (0, 0, 0, 0.28, 0, 0),
                "tail2": (0, 0, 0, 0.06 + flap * 0.04, 0.06, 0),
                "tail3": (0, 0, 0, 0.06 + flap * 0.05, 0.09, 0),
                "tail4": (0, 0, 0, 0.05 + flap * 0.05, 0.11, 0),
                "tail5": (0, 0, 0, 0.05 + flap * 0.05, 0.12, 0),
            }
        if name in over:
            o = over[name]
            if flying:
                # flight pose is authored as absolute joint angles
                pose = [pose[0], pose[1], pose[2], o[3], o[4], o[5]]
            else:
                pose = [pose[i] + o[i] for i in range(6)]
        out.append((part, pose))
    return out


LIGHT = np.array([-0.42, 0.60, -0.68])
LIGHT /= np.linalg.norm(LIGHT)


def render(yaw_deg, pitch_deg, width=1100, height=760, flying=False, gear=False,
           flat=False, t=0.7, out="dragon.png", ss=1.6):
    atlas = Image.open(os.path.join(ASSETS, "entity/void_dragon.png")).convert("RGBA")
    glow = Image.open(os.path.join(ASSETS, "entity/void_dragon_glow.png")).convert("RGBA")
    uvmap = json.load(open(UVF))

    W, H = int(width * ss), int(height * ss)
    colour = np.zeros((H, W, 3), dtype=np.float32)
    alpha = np.zeros((H, W), dtype=bool)
    zbuf = np.full((H, W), 1e9, dtype=np.float32)
    total = np.zeros((H, W, 3), dtype=np.float32)
    total_weight = np.zeros((H, W), dtype=np.float32)
    emit = np.zeros((H, W, 3), dtype=np.float32)

    tex = np.asarray(atlas, dtype=np.float32) / 255.0
    glo = np.asarray(glow, dtype=np.float32) / 255.0

    parts = animated(PARTS, flying=flying, gear=gear, t=t)
    by_name = {p["name"]: p for p in PARTS}
    anim_pose = {p["name"]: pose for p, pose in parts}

    world = {}

    def world_of(name):
        if name in world:
            return world[name]
        part = next(p for p, _ in parts if p["name"] == name)
        parent = part["parent"]
        pm = np.eye(4) if parent == "root" else world_of(parent)
        m = pm @ pose_matrix(anim_pose[part["name"]])
        world[name] = m
        return m

    # pass 1: project all geometry to size the view
    quads = []
    for part, pose in parts:
        if part["box"] is None:
            continue
        m = world_of(part["name"])
        for fname, (corners, uvs) in face_data(part, uvmap[part["name"]]).items():
            p3 = apply(m, corners)
            n = np.cross(p3[1] - p3[0], p3[2] - p3[0])
            ln = np.linalg.norm(n)
            if ln > 1e-6:
                n = n / ln
            quads.append((p3, np.array(uvs, dtype=float), n, part))

    allp = np.concatenate([q[0] for q in quads])
    centre = (allp.min(axis=0) + allp.max(axis=0)) / 2.0

    yaw, pitch = math.radians(yaw_deg), math.radians(pitch_deg)
    cam_rot = m_rx(pitch) @ m_ry(yaw)

    def to_cam(p):
        pts = np.asarray(p, dtype=float)
        h = np.concatenate([pts - centre, np.ones((len(pts), 1))], axis=1)
        return (cam_rot @ h.T).T[:, :3]

    cam_pts = np.concatenate([to_cam(q[0]) for q in quads])
    span_x = float(np.ptp(cam_pts[:, 0]))
    span_y = float(np.ptp(cam_pts[:, 1]))
    dist = 140.0
    scale = min(W * 0.86 / max(1e-6, span_x), H * 0.90 / max(1e-6, span_y))
    cx, cy = (cam_pts[:, 0].min() + cam_pts[:, 0].max()) / 2, (cam_pts[:, 1].min() + cam_pts[:, 1].max()) / 2
    del span_x, span_y

    def project(cp):
        x = W / 2 + (cp[:, 0] - cx) * scale
        y = H / 2 - (cp[:, 1] - cy) * scale
        return np.stack([x, y], axis=1), cp[:, 2]

    cam_quads = []
    # camera-space backface culling
    def cam_normals(p3):
        cp = to_cam(p3)
        n = np.cross(cp[1] - cp[0], cp[2] - cp[0])
        ln = np.linalg.norm(n)
        return n / ln if ln > 1e-6 else n

    for (p3, uvs, n, part) in quads:
        cp = to_cam(p3)
        cn = np.cross(cp[1] - cp[0], cp[2] - cp[0])
        # flip the winding so the quad's outward normal faces the camera
        if np.dot(cn, np.array([0, 0, -1.0])) < 0:
            cp = cp[::-1]
            uvs = uvs[::-1]
        cam_quads.append((cp, uvs, n))

    world_normals = []
    for (p3, uvs, n, part) in quads:
        world_normals.append(n)

    for idx, (cp, uvs, n) in enumerate(cam_quads):
        scr, depth = project(cp)
        shade = 0.70 + 0.55 * max(0.0, float(np.dot(world_normals[idx], LIGHT)))
        rim = 0.18 * max(0.0, 1.0 - abs(float(np.dot(world_normals[idx], LIGHT))))
        tris = [(0, 1, 2), (0, 2, 3)]
        for tri in tris:
            xs = scr[list(tri), 0]
            ys = scr[list(tri), 1]
            us = uvs[list(tri), 0]
            vs = uvs[list(tri), 1]
            zs = depth[list(tri)]
            x0, x1 = int(max(0, np.floor(xs.min()))), int(min(W - 1, np.ceil(xs.max())))
            y0, y1 = int(max(0, np.floor(ys.min()))), int(min(H - 1, np.ceil(ys.max())))
            if x1 <= x0 or y1 <= y0:
                continue
            gx, gy = np.meshgrid(np.arange(x0, x1 + 1) + 0.5, np.arange(y0, y1 + 1) + 0.5)
            d = (ys[1] - ys[2]) * (xs[0] - xs[2]) + (xs[2] - xs[1]) * (ys[0] - ys[2])
            if abs(d) < 1e-9:
                continue
            w0 = ((ys[1] - ys[2]) * (gx - xs[2]) + (xs[2] - xs[1]) * (gy - ys[2])) / d
            w1 = ((ys[2] - ys[0]) * (gx - xs[2]) + (xs[0] - xs[2]) * (gy - ys[2])) / d
            w2 = 1.0 - w0 - w1
            inside = (w0 > -0.06) & (w1 > -0.06) & (w2 > -0.06)
            if not inside.any():
                continue
            tu = (w0 * us[0] + w1 * us[1] + w2 * us[2]) % TEXTURE_SIZE
            tv = (w0 * vs[0] + w1 * vs[1] + w2 * vs[2]) % TEXTURE_SIZE
            ti = np.clip(tu.astype(int), 0, TEXTURE_SIZE - 1)
            tj = np.clip(tv.astype(int), 0, TEXTURE_SIZE - 1)
            texel = tex[tj, ti]
            tex_a = texel[..., 3]
            depth_px = w0 * zs[0] + w1 * zs[1] + w2 * zs[2]

            ys_idx, xs_idx = np.nonzero(inside)
            if len(ys_idx) == 0:
                continue
            gy_full = y0 + ys_idx
            gx_full = x0 + xs_idx
            visible = tex_a[ys_idx, xs_idx] > 0.35
            nearer = depth_px[ys_idx, xs_idx] < zbuf[gy_full, gx_full]
            keep = visible & nearer
            if not keep.any():
                continue
            r = gy_full[keep]
            c = gx_full[keep]
            zbuf[r, c] = depth_px[ys_idx, xs_idx][keep]
            base = texel[ys_idx, xs_idx][keep, :3]
            gl = glo[tj[ys_idx, xs_idx], ti[ys_idx, xs_idx]][keep]
            out_c = base * shade + rim * 0.35
            em = gl[:, :3] * gl[:, 3:4]
            out_c = out_c * (1 - em) + em
            colour[r, c] = out_c
            alpha[r, c] = True
            emit[r, c] = gl[:, :3] * gl[:, 3:4]

    if flat:
        bg = np.array([0.09, 0.06, 0.13], dtype=np.float32)
    else:
        bg = np.array([0.055, 0.035, 0.085], dtype=np.float32)
    img = np.where(alpha[..., None], colour, bg[None, None, :])
    img = np.clip(img, 0, 1)

    im = Image.fromarray((img * 255).astype(np.uint8))
    # bloom around emissive pixels
    if not flat:
        gl = Image.fromarray((np.clip(emit, 0, 1) * 255).astype(np.uint8))
        blurred = gl.filter(ImageFilter.GaussianBlur(6 * ss))
        im = Image.blend(im, Image.blend(im, blurred, 0.6), 0.55)

    im = im.resize((width, height), Image.LANCZOS)
    os.makedirs(OUTDIR, exist_ok=True)
    path = os.path.join(OUTDIR, out)
    im.save(path)
    return path


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--gear", action="store_true")
    ap.add_argument("--flying", action="store_true")
    ap.add_argument("--flat", action="store_true", help="flat UV check mode")
    args = ap.parse_args()
    suffix = ("_gear" if args.gear else "") + ("_flying" if args.flying else "") + ("_uv" if args.flat else "")
    print(render(38, 12, flying=args.flying, gear=args.gear, flat=args.flat,
                 out=f"tex_three{suffix}.png"))
    print(render(90, 6, flying=args.flying, gear=args.gear, flat=args.flat,
                 out=f"tex_side{suffix}.png"))
    print(render(0, 8, flying=args.flying, gear=args.gear, flat=args.flat,
                 out=f"tex_front{suffix}.png"))


if __name__ == "__main__":
    main()
