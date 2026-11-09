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
from dragon_spec import (PARTS, TEXTURE_SIZE, TEXELS_PER_UNIT,  # noqa: E402
                         FOLD_ROOT, FOLD_FORE, FOLD_HAND, FOLD_FAN,
                         FLAP_ROOT_Z, FLAP_FORE_Z, FLAP_FINGER_LAG, ROAR_UNFURL,
                         folded_fingers, FINGERS)

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
JAVA_OUT = os.path.join(ROOT, "src/main/java/com/voiddragon/client/DragonRig.java")
POSE_OUT = os.path.join(ROOT, "src/main/java/com/voiddragon/client/DragonPose.java")
UV_OUT = os.path.join(ROOT, "tools/atlas_uv.json")


# Minecraft addressable texture space is whole texels, so a cube thinner than one
# texel would round down to zero and get no island at all.  Clamp to 1 so flat
# pieces (wing membranes, horn tips) still receive a painted island.
MIN_TEXELS = 1


def box_size(part):
    if part["box"] is None:
        return 0.0, 0.0, 0.0
    x, y, z, w, h, d = part["box"]
    return (max(MIN_TEXELS, round(max(0.0, w) * TEXELS_PER_UNIT)),
            max(MIN_TEXELS, round(max(0.0, h) * TEXELS_PER_UNIT)),
            max(MIN_TEXELS, round(max(0.0, d) * TEXELS_PER_UNIT)))


def unwrapped_size(part):
    w, h, d = box_size(part)
    if w == 0 and h == 0 and d == 0:
        return (0.0, 0.0)
    return (2.0 * d + 2.0 * w, d + h + d)


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
        if w <= 0 or h <= 0:
            self.uv[name] = (0, 0, 0, 0)
            return 0, 0, 0, 0
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


def var_name(name):
    head, *rest = name.split("_")
    return head + "".join(p[:1].upper() + p[1:] for p in rest)


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
    a(" * Texture UVs are packed by the same script into the " + str(TEXTURE_SIZE) + "x" + str(TEXTURE_SIZE) + " atlas")
    a(" * (tools/atlas_uv.json) and painted by tools/gen_textures.py.")
    a(" *")
    a(" * The whole rig hangs from one extra part, {@code rig}. Minecraft renders entity")
    a(" * models through scale(-1, -1, 1), so model space has +Y pointing DOWN and the")
    a(" * model origin sits 1.5 blocks above the entity's feet. tools/dragon_spec.py is")
    a(" * authored the readable way (+Y up, feet on y = 0), so {@code rig} applies the")
    a(" * 180 degree roll that cancels Minecraft's flip and the 24.016 unit drop that")
    a(" * puts the feet back on the ground. Net effect: the dragon renders exactly as")
    a(" * authored, right way up and standing on the block it occupies.")
    a(" */")
    a("public final class DragonRig {")
    a("    /** Cancels Minecraft's model-space flip: 180 degrees about Z. */")
    a("    public static final String RIG = \"rig\";")
    a("    /** 1.5 blocks in model units: where the flipped rig has to sit to be grounded. */")
    a("    public static final float RIG_DROP = 24.016F;")
    a("")
    a("    private DragonRig() {")
    a("    }")
    a("")
    a("    public static MeshDefinition createMesh() {")
    a("        MeshDefinition mesh = new MeshDefinition();")
    a("        PartDefinition root = mesh.getRoot();")
    a("        PartDefinition rig = root.addOrReplaceChild(RIG, CubeListBuilder.create(),")
    a("                PartPose.offsetAndRotation(0.0F, RIG_DROP, 0.0F, 0.0F, 0.0F, (float) Math.PI));")
    vars_ = {"root": "rig"}
    for part in PARTS:
        name, parent, pose = part["name"], part["parent"], part["pose"]
        var = var_name(name)
        vars_[name] = var
        parent_var = vars_.get(parent, "root")
        pose_call = (f"PartPose.offsetAndRotation({pose[0]:.1f}F, {pose[1]:.1f}F, {pose[2]:.1f}F, "
                     f"{pose[3]:.2f}F, {pose[4]:.2f}F, {pose[5]:.2f}F)") if any(pose[3:]) else \
                    (f"PartPose.offset({pose[0]:.1f}F, {pose[1]:.1f}F, {pose[2]:.1f}F)")
        if part["box"] is None:
            a(f"        PartDefinition {var} = {parent_var}.addOrReplaceChild(\"{name}\",")
            a("                CubeListBuilder.create(),")
            a(f"                {pose_call});")
        else:
            ox, oy, oz, w, h, d = part["box"]
            u, v = uv[name][0], uv[name][1]
            mirror = ".mirror()" if part["mirror"] else ""
            a(f"        PartDefinition {var} = {parent_var}.addOrReplaceChild(\"{name}\",")
            a(f"                CubeListBuilder.create().texOffs({u}, {v}){mirror}")
            a(f"                        .addBox({ox:.1f}F, {oy:.1f}F, {oz:.1f}F, {w:.1f}F, {h:.1f}F, {d:.1f}F),")
            a(f"                {pose_call});")
    a("        return mesh;")
    a("    }")
    a("}")
    return "\n".join(lines) + "\n"


def build_pose_java():
    """Writes the animation-anchoring constants so Java and the preview tools
    can never disagree about how a wing folds."""
    fold = folded_fingers()
    open_ = {f"f{i}a": f[0] for i, f in enumerate(FINGERS, start=1)}
    mean = sum(f[0] for f in FINGERS) / len(FINGERS)
    for i in range(1, len(FINGERS)):
        open_[f"m{i}"] = (FINGERS[i - 1][0] + FINGERS[i][0]) * 0.5
    open_["m5"] = FINGERS[0][0] + 0.34
    open_["m6"] = FINGERS[-1][0] - 0.30
    lines = []
    a = lines.append
    a("package com.voiddragon.client;")
    a("")
    a("/**")
    a(" * GENERATED FILE - do not edit by hand.")
    a(" * Wing animation anchors, derived from tools/dragon_spec.py by tools/gen_model.py.")
    a(" *")
    a(" * <p>The model geometry is authored with the wings SPREAD (rest pose at the")
    a(" * back of this file). A grounded dragon folds them with {@link #FOLD_ROOT},")
    a(" * {@link #FOLD_FORE} and {@link #FOLD_HAND}, and the five finger fans collapse")
    a(" * to {@link #FOLDED_FINGER} / {@link #FOLDED_MEMBRANE}. In flight the fans are")
    a(" * the authored values and the arm beats by {@link #FLAP_ROOT_Z}.</p>")
    a(" */")
    a("public final class DragonPose {")
    a("    private DragonPose() {")
    a("    }")
    a(f"    public static final float FINGER_COUNT = {len(FINGERS)}F;")
    a("")
    a("    /** xRot / yRot / zRot of the arm and forearm while the dragon is grounded. */")
    for name, vals in (("FOLD_ROOT", FOLD_ROOT), ("FOLD_FORE", FOLD_FORE), ("FOLD_HAND", FOLD_HAND)):
        for k, axis in enumerate("XYZ"):
            a(f"    public static final float {name}_{axis} = {vals[k]:.3f}F;")
    a("")
    a("    /** Amplitude of the down-stroke, in radians. */")
    a(f"    public static final float FLAP_ROOT_Z = {FLAP_ROOT_Z:.3f}F;")
    a(f"    public static final float FLAP_FORE_Z = {FLAP_FORE_Z:.3f}F;")
    a(f"    public static final float FLAP_FINGER_LAG = {FLAP_FINGER_LAG:.3f}F;")
    a(f"    public static final float ROAR_UNFURL = {ROAR_UNFURL:.3f}F;")
    a(f"    public static final float FOLD_FAN = {FOLD_FAN:.3f}F;")
    a("")
    a("    /** Absolute yRot of each finger / membrane panel while grounded. */")
    a("    public static final float[] FOLDED_FINGER = {")
    a("            " + ", ".join(f"{fold[k]:.3f}F" for k in sorted(fold, key=_suffix_key)) + "};")
    a("    public static final float[] OPEN_FINGER = {")
    a("            " + ", ".join(f"{open_[k]:.3f}F" for k in sorted(open_, key=_suffix_key)) + "};")
    a("}")
    return "\n".join(lines) + "\n"


def _suffix_key(k):
    """f1a, f2a, ... m1, m2, ... -- stable order so arrays line up with the rig."""
    import re
    m = re.match(r"([fm])(\d+)", k)
    return (m.group(1) != "f", int(m.group(2)))


def main():
    packer = Packer(TEXTURE_SIZE)
    uv = {}
    # pack big torso pieces first for a tidier atlas
    order = sorted(PARTS, key=lambda p: -((p["box"][3] * p["box"][5] + p["box"][4]) if p["box"] else -1))
    for part in order:
        w, h = unwrapped_size(part)
        u, v, pw, ph = packer.place(part["name"], w, h)
        uv[part["name"]] = [u, v, pw, ph]

    os.makedirs(os.path.dirname(JAVA_OUT), exist_ok=True)
    with open(JAVA_OUT, "w") as f:
        f.write(build_java(uv))
    with open(POSE_OUT, "w") as f:
        f.write(build_pose_java())
    with open(UV_OUT, "w") as f:
        json.dump(uv, f, indent=1, sort_keys=True)
    print(f"wrote {JAVA_OUT} and {UV_OUT} ({len(PARTS)} cubes, atlas used to y={packer.shelf_y + packer.shelf_h})")


if __name__ == "__main__":
    main()
