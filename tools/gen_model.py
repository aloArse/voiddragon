#!/usr/bin/env python3
"""Generates DragonRig.java (model geometry + PartPose tree) from dragon_spec.py.

The generator assigns UV offsets automatically (packing each cube's unwrapped
box into the 256x256 atlas) and writes them into an atlas_uv.json file that the
texture painter reads, so geometry, UVs and textures can never disagree.
"""
import json
import os
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from dragon_spec import PARTS, TEXTURE_SIZE  # noqa: E402

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
JAVA_OUT = os.path.join(ROOT, "src/main/java/com/voiddragon/client/DragonRig.java")
UV_OUT = os.path.join(ROOT, "tools/atlas_uv.json")


def box_size(part):
    x, y, z, w, h, d = part["box"]
    return max(0.0, w), max(0.0, h), max(0.0, d)


def unwrapped_size(part):
    w, h, d = box_size(part)
    return (2.0 * d + 2.0 * w, d + h + max(d, 0.0))


class Packer:
    """Simple shelf packer for the box UV islands."""

    def __init__(self, size):
        self.size = size
        self.shelf_y = 0
        self.shelf_h = 0
        self.cursor_x = 0
        self.uv = {}

    def place(self, name, w, h):
        w = int(round(w))
        h = int(round(h))
        # keep a small gutter between islands so mipmapping does not bleed
        if self.cursor_x + w + 1 > self.size:
            self.shelf_y += self.shelf_h + 1
            self.cursor_x = 0
            self.shelf_h = 0
        if self.shelf_y + h + 1 > self.size:
            raise SystemExit(f"atlas overflow while packing {name} ({w}x{h})")
        u, v = self.cursor_x, self.shelf_y
        self.uv[name] = (u, v, w, h)
        self.cursor_x += w + 1
        self.shelf_h = max(self.shelf_h, h)
        return u, v, w, h


def build_java(uv):
    lines = []
    a = lines.append
    a("package com.voiddragon.client;")
    a("")
    a("import net.minecraft.client.model.geom.PartPose;")
    a("import net.minecraft.client.model.geom.builders.CubeListBuilder;")
    a("import net.minecraft.client.model.geom.builders.MeshDefinition;")
    a("import net.minecraft.client.model.geom.builders.PartDefinition;")
    a("")
    a("/**")
    a(" * GENERATED FILE - do not edit by hand.")
    a(" * Geometry lives in tools/dragon_spec.py; run tools/gen_model.py to regenerate.")
    a(" * Texture UVs are packed by the same script into a " + str(TEXTURE_SIZE) + "x" + str(TEXTURE_SIZE) + " atlas")
    a(" * (tools/atlas_uv.json) and painted by tools/gen_textures.py.")
    a(" */")
    a("public final class DragonRig {")
    a("    private DragonRig() {")
    a("    }")
    a("")
    a("    public static MeshDefinition createMesh() {")
    a("        MeshDefinition mesh = new MeshDefinition();")
    a("        PartDefinition root = mesh.getRoot();")
    for part in PARTS:
        name, parent, pose = part["name"], part["parent"], part["pose"]
        ox, oy, oz, w, h, d = part["box"]
        u, v = uv[name][0], uv[name][1]
        pose_call = (f"PartPose.offsetAndRotation({pose[0]:.1f}F, {pose[1]:.1f}F, {pose[2]:.1f}F, "
                     f"{pose[3]:.2f}F, {pose[4]:.2f}F, {pose[5]:.2f}F)") if any(pose[3:]) else \
                    (f"PartPose.offset({pose[0]:.1f}F, {pose[1]:.1f}F, {pose[2]:.1f}F)")
        mirror = ".mirror()" if part["mirror"] else ""
        a(f"        {parent}.addOrReplaceChild(\"{name}\",")
        a(f"                CubeListBuilder.create().texOffs({u}, {v}){mirror}")
        a(f"                        .addBox({ox:.1f}F, {oy:.1f}F, {oz:.1f}F, {w:.1f}F, {h:.1f}F, {d:.1f}F),")
        a(f"                {pose_call});")
    a("        return mesh;")
    a("    }")
    a("}")
    return "\n".join(lines) + "\n"


def main():
    packer = Packer(TEXTURE_SIZE)
    uv = {}
    # pack big torso pieces first for a tidier atlas
    order = sorted(PARTS, key=lambda p: -(p["box"][3] * p["box"][5] + p["box"][4]))
    for part in order:
        w, h = unwrapped_size(part)
        u, v, pw, ph = packer.place(part["name"], w, h)
        uv[part["name"]] = [u, v, pw, ph]

    os.makedirs(os.path.dirname(JAVA_OUT), exist_ok=True)
    with open(JAVA_OUT, "w") as f:
        f.write(build_java(uv))
    with open(UV_OUT, "w") as f:
        json.dump(uv, f, indent=1, sort_keys=True)
    print(f"wrote {JAVA_OUT} and {UV_OUT} ({len(PARTS)} cubes, atlas used to y={packer.shelf_y + packer.shelf_h})")


if __name__ == "__main__":
    main()
