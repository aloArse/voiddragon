#!/usr/bin/env python3
"""Single source of truth for the Void Dragon model geometry.

Art direction (taken from the reference the user supplied):
  * gaunt, jagged black-violet void silhouette with glowing molten cracks
  * a long "S" neck that rises steeply from the shoulders and then levels out,
    so the skull juts forward like a hunting animal
  * narrow angular skull, swept-back ringed bone horns, crest blades, glowing
    slit eyes, hinged jaw full of needle teeth
  * huge tattered wings: bone arm, clawed wrist, three fingers, four torn
    membrane panels painted with molten ribs
  * a spine ridge of void blades running from the neck to the spade-tipped tail
  * powerful digitigrade legs with gripping talons

Coordinate system: model units, 1 unit = 1/16 block, +X = left, +Y = up,
-Z = forward (the direction the dragon faces). Poses are
(x, y, z, rotX, rotY, rotZ) and are applied exactly like Minecraft's
PartPose / ModelPart (translation then Z-Y-X rotation).

The same data drives:
  tools/gen_model.py       -> src/main/java/com/voiddragon/client/DragonRig.java
  tools/gen_textures.py    -> 512x512 entity atlas (+ emissive glow atlas)
  tools/render_textured.py -> offline preview using Minecraft's exact box UVs
"""

# The pivot the whole rig hangs from. Everything below is relative to it, so
# moving this single number lifts or lowers the entire dragon without changing
# the pose, and the animation code can bank/bob the whole body.
BODY_PIVOT_Y = 26.4


def P(name, parent, pose, box, painter, mirror=False, anim=False):
    return dict(name=name, parent=parent, pose=pose, box=box, painter=painter,
                mirror=mirror, anim=anim)


# --------------------------------------------------------------------- neck
# A five segment chain. Each entry is (length, box_w, box_h, absolute_angle).
# Absolute angles (not relative ones) are what make an s-curve readable: the
# first segment leaves the shoulders almost vertically, the rest progressively
# flatten out, ending horizontal at the skull.
NECK = [
    (5.4, 7.0, 8.0, 1.16),
    (4.8, 6.2, 7.2, 0.68),
    (4.4, 5.4, 6.4, 0.30),
    (4.0, 4.8, 5.8, 0.06),
    (3.6, 4.2, 5.2, -0.10),
]
HEAD_ANGLE = -0.16


def _neck_parts():
    """Builds neck1..neck5 + their dorsal spikes from the NECK table."""
    out = []
    parent = "body"
    prev_abs = 0.0
    # the chain starts here, on top of the chest
    first_off = (0.0, 5.6, -10.4)
    for i, (length, w, h, ab) in enumerate(NECK, start=1):
        if i == 1:
            pivot = first_off
        else:
            pivot = (0.0, 0.0, -NECK[i - 2][0])
        local = ab - (prev_abs if i > 1 else 0.0)
        out.append(P(f"neck{i}", parent, (*pivot, local, 0.0, 0.0),
                     (-w / 2, -h / 2, -length, w, h, length), "hide", anim=True))
        # dorsal spike sitting on the back of each neck segment
        out.append(P(f"neck{i}_spike", f"neck{i}", (0.0, -h / 2 + 0.3, -0.8, 0.0, 0.0, 0.0),
                     (-0.7, -3.2, -length * 0.82, 1.4, 3.4, length * 0.9), "blade"))
        parent = f"neck{i}"
        prev_abs = ab
    return out


# --------------------------------------------------------------------- wings
# (pivot x on the forearm, yaw sweep, length, width) for the three fingers
FINGERS = [
    (0.5, 0.02, 21.5, 1.1),
    (0.5, -0.52, 18.5, 1.0),
    (0.5, -1.06, 14.5, 0.9),
]
# (pivot x, yaw sweep, length, chord, trailing offset, pitch) for membrane panels
PANELS = [
    (0.7, -0.16, 19.5, 13.0, 0.8, 0.30),
    (0.7, -0.64, 16.5, 12.0, 0.5, 0.46),
    (0.7, -1.10, 12.5, 10.0, 0.2, 0.62),
    (1.3, 0.48, 15.0, 11.0, -2.4, 0.16),
]
ARM_LEN = 15.0
FORE_LEN = 14.0


def _wings():
    """Builds mirrored left/right wings so the two sides can never drift apart.

    Rest pose: the upper arm leaves the shoulder going up and out, the forearm
    folds down and back, and the fingers fan backwards carrying the membrane.
    Nothing crosses the spine, so the folded wing can never clip the body.
    """
    out = []
    for side, sgn in (("l", 1.0), ("r", -1.0)):
        m = sgn < 0
        out.append(P(f"wing_{side}", "body",
                     (sgn * 6.2, 4.4, -7.6, 0.0, sgn * -0.30, sgn * 0.52),
                     (0.0, -1.9, -2.0, ARM_LEN, 3.8, 4.0), "hide", anim=True, mirror=m))
        out.append(P(f"wing_{side}_fore", f"wing_{side}",
                     (sgn * ARM_LEN, 0.0, 0.0, 0.0, sgn * -0.22, sgn * -0.92),
                     (0.0, -1.5, -1.6, FORE_LEN, 3.0, 3.2), "hide", anim=True, mirror=m))
        out.append(P(f"wing_{side}_claw", f"wing_{side}_fore",
                     (sgn * FORE_LEN, 0.0, -0.4, -0.80, 0.0, 0.0),
                     (0.0, -0.7, -0.7, 1.4, 1.4, 5.4), "claw", mirror=m))
        for i, (px, ang, length, wid) in enumerate(FINGERS, start=1):
            out.append(P(f"wing_{side}_f{i}", f"wing_{side}_fore",
                         (sgn * px, 0.0, 0.0, 0.0, sgn * ang, sgn * -0.05 * i),
                         (0.0, -wid / 2, -wid / 2, length, wid, wid), "wingbone", mirror=m))
        for i, (px, ang, length, chord, trail, pitch) in enumerate(PANELS, start=1):
            out.append(P(f"wing_{side}_m{i}", f"wing_{side}_fore",
                         (sgn * px, 0.0, 0.0, sgn * pitch, sgn * ang, 0.0),
                         (0.0, -0.22, trail, length, 0.44, chord), "membrane", mirror=m))
    return out


# =============================================================== body parts
_BODY_PARTS = [
    # a pure pivot at the shoulder: every animation can breathe/bank from here
    P("body", "root", (0.0, BODY_PIVOT_Y, 0.0, 0.0, 0.0, 0.0), None, "pivot", anim=True),

    # chest is broad and deep, the waist pinches, the hips flare again
    P("chest", "body", (0.0, 0.8, -8.6, 0.06, 0.0, 0.0), (-7.8, -6.2, -7.8, 15.6, 12.4, 8.8), "hide"),
    P("torso", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-6.2, -6.2, -8.8, 12.4, 12.4, 17.6), "hide"),
    P("waist", "body", (0.0, -0.8, 8.4, -0.05, 0.0, 0.0), (-5.2, -5.0, -3.6, 10.4, 10.0, 7.2), "hide"),
    P("hips", "body", (0.0, -0.6, 12.6, -0.08, 0.0, 0.0), (-7.0, -6.4, -0.8, 14.0, 12.8, 8.8), "hide"),

    # ventral plating and the dorsal spine ridge
    P("belly_chest", "body", (0.0, 1.2, -8.6, 0.06, 0.0, 0.0), (-5.4, -7.4, -7.2, 10.8, 1.5, 8.0), "belly"),
    P("belly_torso", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-4.8, -7.0, -8.4, 9.6, 1.5, 16.8), "belly"),
    P("belly_waist", "body", (0.0, -0.8, 8.4, -0.05, 0.0, 0.0), (-4.0, -5.8, -3.4, 8.0, 1.4, 6.8), "belly"),
    P("belly_hips", "body", (0.0, -0.6, 12.6, -0.08, 0.0, 0.0), (-5.2, -7.2, -0.6, 10.4, 1.5, 8.4), "belly"),

    P("ridge_chest", "body", (0.0, 1.2, -8.6, 0.06, 0.0, 0.0), (-1.0, -8.4, -7.4, 2.0, 2.8, 8.2), "ridge"),
    P("ridge_torso", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-1.0, -8.0, -8.6, 2.0, 3.2, 17.2), "ridge"),
    P("ridge_waist", "body", (0.0, -0.8, 8.4, -0.05, 0.0, 0.0), (-0.8, -6.6, -3.4, 1.6, 2.6, 6.8), "ridge"),
    P("ridge_hips", "body", (0.0, -0.6, 12.6, -0.08, 0.0, 0.0), (-0.9, -8.0, -0.6, 1.8, 2.8, 8.6), "ridge"),

    # shoulder blades: two swept spikes each side, mirrored
    P("scapula_l", "chest", (7.0, 5.2, -0.6, 0.0, 0.0, -0.55), (-0.9, 0.0, -2.8, 1.8, 6.2, 2.8), "blade"),
    P("scapula_l2", "chest", (8.2, 4.2, 2.2, 0.0, 0.0, -0.75), (-0.8, 0.0, -2.2, 1.6, 5.2, 2.4), "blade"),
    P("scapula_r", "chest", (-7.0, 5.2, -0.6, 0.0, 0.0, 0.55), (-0.9, 0.0, -2.8, 1.8, 6.2, 2.8), "blade", mirror=True),
    P("scapula_r2", "chest", (-8.2, 4.2, 2.2, 0.0, 0.0, 0.75), (-0.8, 0.0, -2.2, 1.6, 5.2, 2.4), "blade", mirror=True),

    # hip spurs
    P("hip_spur_l", "hips", (6.6, 3.6, 2.6, 0.0, 0.0, -0.9), (-0.7, 0.0, -2.0, 1.4, 4.4, 2.2), "blade"),
    P("hip_spur_r", "hips", (-6.6, 3.6, 2.6, 0.0, 0.0, 0.9), (-0.7, 0.0, -2.0, 1.4, 4.4, 2.2), "blade", mirror=True),

    # ---------------------------------------------------------------- tail
    # leans down out of the hips, then flattens and lifts into the spade
    P("tail1", "body", (0.0, -1.6, 13.4, -0.12, 0.0, 0.0), (-3.6, -3.6, 0.0, 7.2, 7.2, 8.4), "hide", anim=True),
    P("tail1_spike", "tail1", (0.0, -3.3, 2.2, 0.0, 0.0, 0.0), (-0.6, -2.8, -2.4, 1.2, 3.0, 5.6), "blade"),
    P("tail2", "tail1", (0.0, -0.2, 8.4, -0.03, 0.0, 0.0), (-3.0, -3.0, 0.0, 6.0, 6.0, 8.4), "hide", anim=True),
    P("tail2_spike", "tail2", (0.0, -2.8, 2.2, 0.0, 0.0, 0.0), (-0.55, -2.6, -2.2, 1.1, 2.8, 5.2), "blade"),
    P("tail3", "tail2", (0.0, -0.1, 8.4, 0.05, 0.0, 0.0), (-2.5, -2.5, 0.0, 5.0, 5.0, 8.4), "hide", anim=True),
    P("tail3_spike", "tail3", (0.0, -2.3, 2.2, 0.0, 0.0, 0.0), (-0.5, -2.4, -2.0, 1.0, 2.6, 4.8), "blade"),
    P("tail4", "tail3", (0.0, 0.0, 8.4, 0.10, 0.0, 0.0), (-2.0, -2.0, 0.0, 4.0, 4.0, 8.4), "hide", anim=True),
    P("tail4_spike", "tail4", (0.0, -1.8, 2.2, 0.0, 0.0, 0.0), (-0.45, -2.0, -1.8, 0.9, 2.2, 4.4), "blade"),
    P("tail5", "tail4", (0.0, 0.0, 8.4, 0.13, 0.0, 0.0), (-1.5, -1.5, 0.0, 3.0, 3.0, 8.0), "hide", anim=True),
    P("tail5_spike", "tail5", (0.0, -1.3, 2.2, 0.0, 0.0, 0.0), (-0.4, -1.7, -1.6, 0.8, 1.8, 4.0), "blade"),
    P("tail6", "tail5", (0.0, 0.0, 8.0, 0.14, 0.0, 0.0), (-1.0, -1.0, 0.0, 2.0, 2.0, 7.4), "hide", anim=True),
    P("tail7", "tail6", (0.0, 0.0, 7.4, 0.14, 0.0, 0.0), (-0.7, -0.7, 0.0, 1.4, 1.4, 6.4), "hide", anim=True),
    # spade tip: a cross shaped void blade
    P("spade_top", "tail7", (0.0, -0.2, 5.8, -0.34, 0.0, 0.0), (-0.7, -0.2, 0.0, 1.4, 3.4, 6.4), "blade"),
    P("spade_bot", "tail7", (0.0, 0.2, 6.0, 0.05, 0.0, 0.0), (-0.7, 0.0, 0.0, 1.4, 2.6, 5.6), "blade"),
    P("spade_left", "tail7", (0.65, 0.0, 5.4, 0.0, 0.0, -0.7), (-0.26, -0.5, 0.0, 0.52, 1.0, 4.4), "blade"),
    P("spade_right", "tail7", (-0.65, 0.0, 5.4, 0.0, 0.0, 0.7), (-0.26, -0.5, 0.0, 0.52, 1.0, 4.4), "blade", mirror=True),

    # ---------------------------------------------------------------- head
    # long angular skull: cranium, skull plate, deep boxy snout, hinged jaw
    P("head", "neck5", (0.0, 0.0, -3.2, HEAD_ANGLE, 0.0, 0.0), (-3.3, -2.7, -6.4, 6.6, 5.4, 6.4), "skull", anim=True),
    P("skull_top", "head", (0.0, 2.6, -1.0, 0.0, 0.0, 0.0), (-2.7, -0.9, -5.6, 5.4, 1.9, 5.8), "skull"),
    # brow ridges over the eyes
    P("brow_l", "head", (2.5, 1.8, -3.6, 0.0, 0.0, -0.25), (-0.55, -0.55, -2.4, 1.1, 1.1, 4.8), "blade"),
    P("brow_r", "head", (-2.5, 1.8, -3.6, 0.0, 0.0, 0.25), (-0.55, -0.55, -2.4, 1.1, 1.1, 4.8), "blade", mirror=True),
    P("muzzle", "head", (0.0, -0.7, -6.0, 0.0, 0.0, 0.0), (-2.4, -1.9, -5.8, 4.8, 3.6, 5.8), "muzzle"),
    P("nostril_l", "muzzle", (1.1, 0.7, -4.7, 0.0, 0.0, 0.0), (-0.45, -0.75, -0.65, 0.9, 1.5, 1.3), "small"),
    P("nostril_r", "muzzle", (-1.1, 0.7, -4.7, 0.0, 0.0, 0.0), (-0.45, -0.75, -0.65, 0.9, 1.5, 1.3), "small"),
    P("jaw", "head", (0.0, 1.9, -1.2, 0.05, 0.0, 0.0), (-2.4, 0.0, -7.4, 4.8, 1.8, 7.4), "jaw", anim=True),
    P("teeth_l", "head", (1.9, 0.7, -5.6, 0.0, 0.0, 0.0), (-0.38, 0.0, -0.9, 0.76, 1.5, 2.4), "tooth"),
    P("teeth_r", "head", (-1.9, 0.7, -5.6, 0.0, 0.0, 0.0), (-0.38, 0.0, -0.9, 0.76, 1.5, 2.4), "tooth"),
    P("fangs", "jaw", (0.0, 0.0, -6.4, 0.0, 0.0, 0.0), (-1.8, 0.0, -1.0, 3.6, 1.4, 2.2), "tooth"),

    # glowing slit eyes set into the cheeks
    P("eye_l", "head", (2.92, 0.55, -4.3, 0.0, 0.0, 0.0), (-0.30, -0.62, -1.5, 0.8, 1.24, 3.0), "eye"),
    P("eye_r", "head", (-2.92, 0.55, -4.3, 0.0, 0.0, 0.0), (-0.30, -0.62, -1.5, 0.8, 1.24, 3.0), "eye", mirror=True),

    # brow horns: swept back over the neck, ringed bone, tapering to a point
    P("horn_l1", "head", (1.9, 2.3, 0.6, -0.30, -0.22, 0.0), (-0.62, -0.70, 0.0, 1.24, 1.40, 5.2), "horn"),
    P("horn_l2", "horn_l1", (0.0, 0.0, 5.2, -0.34, -0.16, 0.0), (-0.38, -0.44, 0.0, 0.76, 0.88, 4.2), "horn"),
    P("horn_l3", "horn_l2", (0.0, 0.0, 4.2, -0.40, 0.0, 0.0), (-0.16, -0.2, 0.0, 0.32, 0.4, 3.0), "horn"),
    P("horn_r1", "head", (-1.9, 2.3, 0.6, -0.30, 0.22, 0.0), (-0.62, -0.70, 0.0, 1.24, 1.40, 5.2), "horn", mirror=True),
    P("horn_r2", "horn_r1", (0.0, 0.0, 5.2, -0.34, 0.16, 0.0), (-0.38, -0.44, 0.0, 0.76, 0.88, 4.2), "horn", mirror=True),
    P("horn_r3", "horn_r2", (0.0, 0.0, 4.2, -0.40, 0.0, 0.0), (-0.16, -0.2, 0.0, 0.32, 0.4, 3.0), "horn", mirror=True),
    # crest blades fanning off the back of the skull
    P("crest_l", "head", (1.6, 1.1, 2.5, 0.92, -0.85, 0.0), (-0.32, 0.0, -0.6, 0.64, 4.2, 1.3), "blade"),
    P("crest_r", "head", (-1.6, 1.1, 2.5, 0.92, 0.85, 0.0), (-0.32, 0.0, -0.6, 0.64, 4.2, 1.3), "blade", mirror=True),
    # cheek fins and jaw barbels
    P("cheek_l", "head", (3.1, -0.5, 0.8, 0.0, -1.45, 0.35), (-0.35, -1.2, 0.0, 0.7, 2.4, 3.2), "blade"),
    P("cheek_r", "head", (-3.1, -0.5, 0.8, 0.0, 1.45, -0.35), (-0.35, -1.2, 0.0, 0.7, 2.4, 3.2), "blade", mirror=True),
    P("barbel_l", "jaw", (1.6, 0.3, -1.0, -0.5, 0.0, 0.25), (-0.28, 0.0, 0.0, 0.56, 3.0, 0.56), "blade"),
    P("barbel_r", "jaw", (-1.6, 0.3, -1.0, -0.5, 0.0, -0.25), (-0.28, 0.0, 0.0, 0.56, 3.0, 0.56), "blade", mirror=True),

    # ---------------------------------------------------------------- front legs
    # digitigrade: thigh forward, shin back, foot forward, three gripping talons
    P("leg_fl_thigh", "body", (6.0, -5.0, -8.6, 0.14, 0.0, 0.0), (-2.5, -9.5, -3.4, 5.0, 10.5, 7.0), "hide", anim=True),
    P("leg_fl_shin", "leg_fl_thigh", (0.0, -9.5, 0.4, -0.34, 0.0, 0.0), (-1.9, -9.0, -2.2, 3.8, 9.0, 4.4), "hide", anim=True),
    P("leg_fl_foot", "leg_fl_shin", (0.0, -9.0, -0.2, 0.20, 0.0, 0.0), (-2.3, -1.4, -3.6, 4.6, 2.4, 5.2), "foot", anim=True),
    P("leg_fl_talon1", "leg_fl_foot", (1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw"),
    P("leg_fl_talon2", "leg_fl_foot", (0.0, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.7, 0.84, 1.0, 2.2), "claw"),
    P("leg_fl_talon3", "leg_fl_foot", (-1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw"),

    P("leg_fr_thigh", "body", (-6.0, -5.0, -8.6, 0.14, 0.0, 0.0), (-2.5, -9.5, -3.4, 5.0, 10.5, 7.0), "hide", anim=True, mirror=True),
    P("leg_fr_shin", "leg_fr_thigh", (0.0, -9.5, 0.4, -0.34, 0.0, 0.0), (-1.9, -9.0, -2.2, 3.8, 9.0, 4.4), "hide", anim=True, mirror=True),
    P("leg_fr_foot", "leg_fr_shin", (0.0, -9.0, -0.2, 0.20, 0.0, 0.0), (-2.3, -1.4, -3.6, 4.6, 2.4, 5.2), "foot", anim=True, mirror=True),
    P("leg_fr_talon1", "leg_fr_foot", (1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw", mirror=True),
    P("leg_fr_talon2", "leg_fr_foot", (0.0, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.7, 0.84, 1.0, 2.2), "claw", mirror=True),
    P("leg_fr_talon3", "leg_fr_foot", (-1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw", mirror=True),

    # ---------------------------------------------------------------- hind legs
    # powerful haunches, longer than the arms, back-swept hock
    P("leg_bl_thigh", "body", (6.8, -4.6, 10.0, -0.30, 0.0, 0.0), (-3.6, -11.0, -3.8, 7.2, 12.0, 8.0), "hide", anim=True),
    P("leg_bl_shin", "leg_bl_thigh", (0.0, -11.0, 0.0, 0.62, 0.0, 0.0), (-2.3, -9.5, -2.6, 4.6, 9.5, 5.2), "hide", anim=True),
    P("leg_bl_foot", "leg_bl_shin", (0.0, -9.5, -0.3, -0.32, 0.0, 0.0), (-2.6, -1.5, -4.0, 5.2, 2.5, 6.0), "foot", anim=True),
    P("leg_bl_talon1", "leg_bl_foot", (1.7, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.7, 0.92, 1.1, 2.2), "claw"),
    P("leg_bl_talon2", "leg_bl_foot", (0.0, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.9, 0.92, 1.1, 2.4), "claw"),
    P("leg_bl_talon3", "leg_bl_foot", (-1.7, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.7, 0.92, 1.1, 2.2), "claw"),

    P("leg_br_thigh", "body", (-6.8, -4.6, 10.0, -0.30, 0.0, 0.0), (-3.6, -11.0, -3.8, 7.2, 12.0, 8.0), "hide", anim=True, mirror=True),
    P("leg_br_shin", "leg_br_thigh", (0.0, -11.0, 0.0, 0.62, 0.0, 0.0), (-2.3, -9.5, -2.6, 4.6, 9.5, 5.2), "hide", anim=True, mirror=True),
    P("leg_br_foot", "leg_br_shin", (0.0, -9.5, -0.3, -0.32, 0.0, 0.0), (-2.6, -1.5, -4.0, 5.2, 2.5, 6.0), "foot", anim=True, mirror=True),
    P("leg_br_talon1", "leg_br_foot", (1.7, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.7, 0.92, 1.1, 2.2), "claw", mirror=True),
    P("leg_br_talon2", "leg_br_foot", (0.0, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.9, 0.92, 1.1, 2.4), "claw", mirror=True),
    P("leg_br_talon3", "leg_br_foot", (-1.7, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.7, 0.92, 1.1, 2.2), "claw", mirror=True),

    # ---------------------------------------------------------------- gear
    P("saddle", "body", (0.0, 6.2, 1.5, 0.0, 0.0, 0.0), (-4.8, -0.9, -5.6, 9.6, 1.8, 11.2), "saddle"),
    P("saddle_pommel", "saddle", (0.0, 0.9, -5.0, 0.0, 0.0, 0.0), (-1.7, -2.4, -1.7, 3.4, 2.6, 3.4), "saddle"),
    P("saddle_cantle", "saddle", (0.0, 0.9, 5.0, 0.0, 0.0, 0.0), (-2.0, -2.0, -1.3, 4.0, 2.2, 2.6), "saddle"),
    P("saddle_strap_l", "body", (5.6, 0.0, 1.5, 0.0, 0.0, 0.0), (-0.55, -7.6, -4.6, 1.1, 9.6, 9.2), "saddle"),
    P("saddle_strap_r", "body", (-5.6, 0.0, 1.5, 0.0, 0.0, 0.0), (-0.55, -7.6, -4.6, 1.1, 9.6, 9.2), "saddle", mirror=True),

    P("armor_chest", "body", (0.0, 1.2, -8.6, 0.06, 0.0, 0.0), (-8.5, -7.1, -8.1, 17.0, 14.2, 9.1), "armor"),
    P("armor_torso", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-6.7, -6.7, -9.3, 13.4, 13.4, 18.1), "armor"),
    P("armor_hips", "body", (0.0, -0.6, 12.6, -0.08, 0.0, 0.0), (-7.5, -6.9, -1.3, 15.0, 13.8, 9.3), "armor"),
    P("armor_collar", "body", (0.0, 3.6, -7.6, 0.45, 0.0, 0.0), (-6.2, -1.3, -2.8, 12.4, 2.6, 5.6), "armor"),
]


def _all_parts():
    # neck1..5 go right after the hips/pivot block, wings last
    neck = _neck_parts()
    return _BODY_PARTS[:1] + _BODY_PARTS[1:5] + neck + _BODY_PARTS[5:] + _wings()


PARTS = _all_parts()

# groups hidden unless the dragon is saddled / armoured
GEAR = {
    "saddle": ["saddle", "saddle_pommel", "saddle_cantle", "saddle_strap_l", "saddle_strap_r"],
    "armor": ["armor_chest", "armor_torso", "armor_hips", "armor_collar"],
}

TEXTURE_SIZE = 512
