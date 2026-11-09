#!/usr/bin/env python3
"""Numeric ruler for the dragon rig.

Prints, in model units (1 unit = 1/16 block), the world-space bounding box of
every part group plus the derived silhouette numbers that actually decide
whether the dragon reads as a dragon:

  * shoulder / back / head height above the ground
  * body length, neck length, tail length, total nose-to-tail
  * wing extent and how far the membrane rises above the spine
  * limb thickness (mean cross section) versus torso depth

Run: python3 tools/measure.py
"""
import math
import os
import sys

import numpy as np

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from dragon_spec import PARTS  # noqa: E402
from render_textured import animated, pose_matrix, apply  # noqa: E402


def world_matrix(parts, name):
    by_name = {p["name"]: p for p in parts}
    chain = []
    cur = name
    while cur in by_name:
        chain.append(by_name[cur])
        cur = by_name[cur]["parent"]
    m = np.eye(4)
    for p in reversed(chain):
        m = m @ pose_matrix(p["pose"])
    return m


def bbox(parts, names):
    lo = np.array([1e9, 1e9, 1e9])
    hi = -lo
    any_box = False
    for p in parts:
        if p["name"] not in names or p["box"] is None:
            continue
        any_box = True
        ox, oy, oz, w, h, d = p["box"]
        corners = np.array([[ox + dx * w, oy + dy * h, oz + dz * d]
                            for dx in (0, 1) for dy in (0, 1) for dz in (0, 1)])
        wm = world_matrix(parts, p["name"])
        pts = apply(wm, corners)
        lo = np.minimum(lo, pts.min(axis=0))
        hi = np.maximum(hi, pts.max(axis=0))
    return (lo, hi) if any_box else (None, None)


def fmt(name, lo, hi):
    if lo is None:
        return f"{name:<18} (no boxes)"
    return (f"{name:<18} x {lo[0]:7.1f} .. {hi[0]:6.1f}   y {lo[1]:7.1f} .. {hi[1]:6.1f}"
            f"   z {lo[2]:7.1f} .. {hi[2]:6.1f}   ({hi[0]-lo[0]:5.1f} x {hi[1]-lo[1]:5.1f} x {hi[2]-lo[2]:5.1f})")


def main():
    # keep the ANIMATED pose (the furled wing fan lives in animated(), not in
    # the authored pose) by copying it back onto the part dicts
    parts = []
    for part, pose in animated(PARTS):
        q = dict(part)
        q["pose"] = tuple(pose)
        parts.append(q)
    names = {p["name"] for p in PARTS}

    def sel(prefix):
        return {n for n in names if n.startswith(prefix)}

    groups = {
        "torso": {"chest", "torso", "waist", "hips", "belly_chest", "belly_torso",
                  "belly_waist", "belly_hips", "ridge_chest", "ridge_torso",
                  "ridge_waist", "ridge_hips"},
        "neck": sel("neck"),
        "head": {"head", "muzzle", "nasal_bridge", "snout_tip", "nostril_l", "nostril_r",
                 "jaw", "chin", "teeth_l", "teeth_r", "jaw_muscle_l", "jaw_muscle_r",
                 "cheek_ridge_l", "cheek_ridge_r", "brow_l", "brow_r", "eye_l", "eye_r",
                 "horn_l1", "horn_r1"},
        "tail": sel("tail") | sel("spade"),
        "wing_l": {n for n in names if n.startswith("wing_l")},
        "wing_r": {n for n in names if n.startswith("wing_r")},
        "legs_front": sel("leg_f"),
        "legs_hind": sel("leg_b"),
        "ALL": names,
    }

    print("== group bounding boxes (model units, y=0 is the ground) ==")
    boxes = {}
    for g, ns in groups.items():
        lo, hi = bbox(parts, ns)
        boxes[g] = (lo, hi)
        print(fmt(g, lo, hi))

    lo, hi = boxes["ALL"]
    torso_lo, torso_hi = boxes["torso"]
    head_lo, head_hi = boxes["head"]
    tail_lo, tail_hi = boxes["tail"]
    wl_lo, wl_hi = boxes["wing_l"]

    print()
    print("== silhouette ==")
    print(f"total height        {hi[1]:6.1f}  ({hi[1]/16:.2f} blocks)")
    print(f"nose to tail tip    {hi[2]-lo[2]:6.1f}  ({(hi[2]-lo[2])/16:.2f} blocks, z {lo[2]:.1f}..{hi[2]:.1f})")
    print(f"widest              {hi[0]-lo[0]:6.1f}  ({(hi[0]-lo[0])/16:.2f} blocks)")
    print(f"torso length        {torso_hi[2]-torso_lo[2]:6.1f}   depth {torso_hi[1]-torso_lo[1]:5.1f}   width {torso_hi[0]-torso_lo[0]:5.1f}")
    print(f"chest bottom (belly){torso_lo[1]:6.1f}   back top {torso_hi[1]:6.1f}")
    print(f"head length         {head_hi[2]-head_lo[2]:6.1f}   height {head_hi[1]-head_lo[1]:5.1f}   width {head_hi[0]-head_lo[0]:5.1f}")
    print(f"head centre y       {(head_hi[1]+head_lo[1])/2:6.1f}   (back top {torso_hi[1]:.1f})")
    print(f"tail length         {tail_hi[2]-tail_lo[2]:6.1f}   tip y {tail_hi[1]:6.1f}")
    print(f"wing L span         {wl_hi[0]-wl_lo[0]:6.1f}   rises {wl_hi[1]:6.1f} above ground"
          f"  ({wl_hi[1]-torso_hi[1]:+.1f} vs back top)")

    print()
    print("== joint positions ==")
    def jp(name, off=(0.0, 0.0, 0.0)):
        m = world_matrix(parts, name)
        x, y, z = (m @ np.array([off[0], off[1], off[2], 1.0]))[:3]
        return f"{name:<18} ({x:6.1f}, {y:6.1f}, {z:6.1f})"

    for n in ("body", "neck1", "neck5", "head", "tail1", "tail7",
              "wing_l", "wing_l_fore", "wing_l_hand"):
        print(jp(n))
    for n in ("wing_l_f1b", "wing_l_f5b"):
        print(jp(n, (float(parts[[p["name"] for p in parts].index(n)]["box"][3] if False else 0), 0, 0)))
    # fingertips
    for i in (1, 3, 5):
        n = f"wing_l_f{i}b"
        box = next(p for p in parts if p["name"] == n)["box"]
        print(jp(n + ":tip", (box[3], 0.0, 0.0)))
    for n in ("leg_fl_foot", "leg_fr_foot", "leg_bl_foot", "leg_br_foot"):
        print(jp(n))

    # limb thickness: mean of the smallest two cross-section axes of the limbs
    print()
    print("== limb cross sections (thin = spindly) ==")
    for pre in ("leg_fl_thigh", "leg_fl_shin", "leg_bl_thigh", "leg_bl_shin"):
        for p in parts:
            if p["name"] == pre:
                ox, oy, oz, w, h, d = p["box"]
                print(f"{pre:<16} {w:5.1f} x {d:5.1f}   (torso depth {torso_hi[1]-torso_lo[1]:.1f})")
    return 0


if __name__ == "__main__":
    sys.exit(main())
