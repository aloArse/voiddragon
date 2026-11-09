#!/usr/bin/env python3
"""Minimal flat-shaded software renderer used to sanity-check geometry.

This is a diagnostic tool: it ignores the texture entirely and paints each box
with one flat tone from its group, so silhouette, proportions and joint gaps are
visible without any texture noise. tools/render_textured.py is the "real" 
preview that shows the in-game skin.

Run: python3 tools/render_simple.py
"""
import math
import os
import sys

import numpy as np
from PIL import Image

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import dragon_spec as ds  # noqa: E402
from dragon_spec import PARTS, FLY_ROOT, FLY_FORE, FLY_HAND  # noqa: E402
from _pose_lib import pose_matrix, apply  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
OUT = os.path.join(ROOT, "preview")

COLORS = {
    "torso": (0.62, 0.58, 0.55),
    "neck": (0.72, 0.68, 0.62),
    "head": (0.80, 0.76, 0.70),
    "tail": (0.54, 0.50, 0.48),
    "wing": (0.46, 0.30, 0.34),
    "membrane": (0.60, 0.28, 0.30),
    "leg": (0.50, 0.46, 0.44),
    "blade": (0.34, 0.32, 0.34),
    "gear": (0.70, 0.60, 0.30),
}


def group(name):
    if name.startswith(("saddle", "armor")):
        return "gear"
    if name.startswith(("wing",)):
        return "membrane" if "_m" in name else "wing"
    if name.startswith(("neck",)):
        return "neck"
    if name.startswith(("head", "muzzle", "snout", "jaw", "chin", "brow", "eye", "horn",
                        "cheek", "teeth", "nostril", "jaw_", "head_")):
        return "head"
    if name.startswith("tail") or name.startswith("spade"):
        return "tail"
    if name.startswith("leg_"):
        return "leg"
    if name.startswith(("ridge", "scapula", "hip_spur")):
        return "blade"
    return "torso"


def poses(flying):
    out = {}
    if flying:
        out["wing_l"] = (0, 0, 0) + FLY_ROOT
        out["wing_r"] = (0, 0, 0, FLY_ROOT[0], -FLY_ROOT[1], -FLY_ROOT[2])
        out["wing_l_fore"] = (0, 0, 0) + FLY_FORE
        out["wing_r_fore"] = (0, 0, 0, FLY_FORE[0], -FLY_FORE[1], -FLY_FORE[2])
        out["wing_l_hand"] = (0, 0, 0) + FLY_HAND
        out["wing_r_hand"] = (0, 0, 0, FLY_HAND[0], -FLY_HAND[1], -FLY_HAND[2])
    else:
        root, fore, hand = ds.FOLD_ROOT, ds.FOLD_FORE, ds.FOLD_HAND
        out["wing_l"] = (0, 0, 0) + root
        out["wing_r"] = (0, 0, 0, root[0], -root[1], -root[2])
        out["wing_l_fore"] = (0, 0, 0) + fore
        out["wing_r_fore"] = (0, 0, 0, fore[0], -fore[1], -fore[2])
        out["wing_l_hand"] = (0, 0, 0) + hand
        out["wing_r_hand"] = (0, 0, 0, hand[0], -hand[1], -hand[2])
        for suf, ang in ds.folded_fingers().items():
            out[f"wing_l_{suf}"] = (0, 0, 0, None, ang, None)
            out[f"wing_r_{suf}"] = (0, 0, 0, None, -ang, None)
    return out


def render(yaw_deg, pitch_deg, width=900, height=650, flying=False, gear=False,
           out="simple.png", ss=1.4, hide=(), only=None):
    parts = {p["name"]: p for p in PARTS}
    overrides = poses(flying)
    world = {}

    def world_of(name):
        if name in world:
            return world[name]
        p = parts[name]
        pose = list(p["pose"])
        if name in overrides:
            o = overrides[name]
            if o[3] is None:
                pose[4] = o[4]
            else:
                pose[3:] = list(o[3:])
        parent = p["parent"]
        pm = np.eye(4) if parent == "root" else world_of(parent)
        world[name] = pm @ pose_matrix(pose)
        return world[name]

    W, H = int(width * ss), int(height * ss)
    zbuf = np.full((H, W), 1e9, dtype=np.float32)
    img = np.full((H, W, 3), 0.0, dtype=np.float32)
    alpha = np.zeros((H, W), dtype=bool)

    quads = []
    for name, p in parts.items():
        if p["box"] is None:
            continue
        if only is not None and not name.startswith(tuple(only)):
            continue
        if hide and name.startswith(tuple(hide)):
            continue
        m = world_of(name)
        x, y, z, w, h, d = p["box"]
        x0, x1, y0, y1, z0, z1 = x, x + w, y, y + h, z, z + d
        faces = [
            [(x0, y0, z0), (x1, y0, z0), (x1, y1, z0), (x0, y1, z0)],   # front (-z)
            [(x1, y0, z1), (x0, y0, z1), (x0, y1, z1), (x1, y1, z1)],   # back (+z)
            [(x1, y0, z0), (x1, y0, z1), (x1, y1, z1), (x1, y1, z0)],   # +x
            [(x0, y0, z1), (x0, y0, z0), (x0, y1, z0), (x0, y1, z1)],   # -x
            [(x0, y1, z0), (x1, y1, z0), (x1, y1, z1), (x0, y1, z1)],   # top
            [(x0, y0, z1), (x1, y0, z1), (x1, y0, z0), (x0, y0, z0)],   # bottom
        ]
        for f in faces:
            p3 = apply(m, np.array(f, float))
            n = np.cross(p3[1] - p3[0], p3[2] - p3[0])
            ln = np.linalg.norm(n)
            if ln < 1e-9:
                continue
            quads.append((p3, n / ln, name))

    allp = np.concatenate([q[0] for q in quads])
    centre = (allp.min(axis=0) + allp.max(axis=0)) / 2.0

    yaw, pitch = math.radians(yaw_deg), math.radians(pitch_deg)
    cy_, sy_ = math.cos(yaw), math.sin(yaw)
    cp_, sp_ = math.cos(pitch), math.sin(pitch)
    ry = np.array([[cy_, 0, sy_], [0, 1, 0], [-sy_, 0, cy_]])
    rx = np.array([[1, 0, 0], [0, cp_, -sp_], [0, sp_, cp_]])
    rot = rx @ ry

    def to_cam(p):
        return (rot @ (np.asarray(p, float) - centre).T).T

    cam_pts = np.concatenate([to_cam(q[0]) for q in quads])
    span_x = float(np.ptp(cam_pts[:, 0]))
    span_y = float(np.ptp(cam_pts[:, 1]))
    scale = min(W * 0.88 / max(1e-6, span_x), H * 0.90 / max(1e-6, span_y))
    cx = (cam_pts[:, 0].min() + cam_pts[:, 0].max()) / 2
    cy2 = (cam_pts[:, 1].min() + cam_pts[:, 1].max()) / 2

    light = np.array([-0.45, 0.68, -0.58])
    light /= np.linalg.norm(light)

    for (p3, n, name) in quads:
        cp = to_cam(p3)
        cn = np.cross(cp[1] - cp[0], cp[2] - cp[0])
        if cn[2] > 0:      # camera looks down -z
            continue
        base = np.array(COLORS[group(name)], dtype=np.float32)
        key = max(0.0, float(np.dot(n, light)))
        shade = 0.30 + 0.80 * key
        col = np.clip(base * shade, 0, 1)
        xs = W / 2 + (cp[:, 0] - cx) * scale
        ys = H / 2 - (cp[:, 1] - cy2) * scale
        zs = cp[:, 2]
        for tri in ((0, 1, 2), (0, 2, 3)):
            tx, ty, tz = xs[list(tri)], ys[list(tri)], zs[list(tri)]
            x_min, x_max = int(max(0, np.floor(tx.min()))), int(min(W - 1, np.ceil(tx.max())))
            y_min, y_max = int(max(0, np.floor(ty.min()))), int(min(H - 1, np.ceil(ty.max())))
            if x_max <= x_min or y_max <= y_min:
                continue
            gx, gy = np.meshgrid(np.arange(x_min, x_max + 1) + 0.5,
                                 np.arange(y_min, y_max + 1) + 0.5)
            den = (ty[1] - ty[2]) * (tx[0] - tx[2]) + (tx[2] - tx[1]) * (ty[0] - ty[2])
            if abs(den) < 1e-9:
                continue
            w0 = ((ty[1] - ty[2]) * (gx - tx[2]) + (tx[2] - tx[1]) * (gy - ty[2])) / den
            w1 = ((ty[2] - ty[0]) * (gx - tx[2]) + (tx[0] - tx[2]) * (gy - ty[2])) / den
            w2 = 1.0 - w0 - w1
            inside = (w0 >= -0.002) & (w1 >= -0.002) & (w2 >= -0.002)
            if not inside.any():
                continue
            depth = w0 * tz[0] + w1 * tz[1] + w2 * tz[2]
            iy, ix = np.nonzero(inside)
            gy_f, gx_f = y_min + iy, x_min + ix
            d = depth[iy, ix]
            keep = d < zbuf[gy_f, gx_f]
            img[gy_f[keep], gx_f[keep]] = col
            zbuf[gy_f[keep], gx_f[keep]] = d[keep]
            alpha[gy_f[keep], gx_f[keep]] = True

    yy = np.linspace(0, 1, H, dtype=np.float32)[:, None, None]
    bg = np.broadcast_to(0.72 - 0.35 * yy, (H, W, 3)).copy()
    out_img = np.where(alpha[..., None], img, bg)
    im = Image.fromarray((np.clip(out_img, 0, 1) * 255).astype(np.uint8))
    im = im.resize((width, height), Image.LANCZOS)
    os.makedirs(OUT, exist_ok=True)
    im.save(os.path.join(OUT, out))
    return os.path.join(OUT, out)


if __name__ == "__main__":
    print(render(90, 4, out="simple_side.png"))
    print(render(35, 12, out="simple_3q.png"))
    print(render(35, 12, flying=True, out="simple_3q_fly.png"))
    print(render(0, 6, out="simple_front.png"))
