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
    (5.8, 6.0, 7.2, 1.06),
    (5.4, 5.2, 6.2, 0.78),
    (5.0, 4.6, 5.4, 0.56),
    (4.6, 4.0, 4.8, 0.40),
    (4.2, 3.6, 4.2, 0.30),
]
# the skull is carried level, as a hunting animal holds it, not tipped down
HEAD_ANGLE = -0.30


def _neck_parts():
    """Builds neck1..neck5 + their dorsal spikes from the NECK table."""
    out = []
    parent = "body"
    prev_abs = 0.0
    # the chain starts here, on top of the chest
    first_off = (0.0, 4.6, -9.6)
    for i, (length, w, h, ab) in enumerate(NECK, start=1):
        if i == 1:
            pivot = first_off
        else:
            pivot = (0.0, 0.0, -NECK[i - 2][0])
        local = ab - (prev_abs if i > 1 else 0.0)
        out.append(P(f"neck{i}", parent, (*pivot, local, 0.0, 0.0),
                     (-w / 2, -h / 2, -length * 1.16, w, h, length * 1.22), "hide", anim=True))
        parent = f"neck{i}"
        prev_abs = ab
    return out


# --------------------------------------------------------------------- wings
# Anatomy: humerus -> forearm -> hand -> four fingers of two phalanges each.
# The membrane is generated from the finger table, so the cloth always fills the
# gaps between the digits and reaches the tips instead of leaving bare rods.
ARM_LEN = 14.0          # humerus
FORE_LEN = 15.0         # radius / ulna
# (yaw, first phalanx, second phalanx, width, curl of the second phalanx)
FINGERS = [
    (0.34, 15.0, 12.0, 1.6, 0.10),
    (0.02, 19.0, 15.0, 1.4, 0.16),
    (-0.42, 18.0, 14.0, 1.3, 0.22),
    (-0.86, 14.0, 10.5, 1.1, 0.28),
    (-1.24, 9.5, 6.5, 0.9, 0.34),
]
# membrane chord as a fraction of the panel length (deep wing, scalloped tips)
CHORD = 0.62


def _wings():
    """Builds mirrored left/right wings so the two sides can never drift apart.

    Rest pose: the humerus leaves the shoulder up and out, the forearm angles
    back, and the fingers fan into a swept fan of membrane with torn scalloped
    tips -- the silhouette of the reference art rather than a flat plank.
    """
    out = []
    for side, sgn in (("l", 1.0), ("r", -1.0)):
        m = sgn < 0
        # shoulder joint: the arm lifts above the back and opens outward
        out.append(P(f"wing_{side}", "body",
                     (sgn * 6.0, 6.4, -7.2, 0.16, sgn * -0.42, sgn * 0.62),
                     (0.0, -2.1, -2.4, ARM_LEN, 4.2, 4.8), "hide", anim=True, mirror=m))
        # deltoid mass so the shoulder is not a bare stick
        out.append(P(f"wing_{side}_deltoid", f"wing_{side}",
                     (sgn * 0.6, 0.6, -1.6, 0.0, 0.0, 0.0),
                     (0.0, -2.6, -1.8, 6.4, 5.6, 5.6), "hide", mirror=m))
        # elbow -> wrist
        out.append(P(f"wing_{side}_fore", f"wing_{side}",
                     (sgn * ARM_LEN, 0.0, 0.0, -0.22, sgn * -0.16, sgn * -0.18),
                     (0.0, -1.6, -1.8, FORE_LEN, 3.2, 3.6), "hide", anim=True, mirror=m))
        # wrist / hand: everything below fans from this one point
        out.append(P(f"wing_{side}_hand", f"wing_{side}_fore",
                     (sgn * FORE_LEN, 0.0, 0.0, 0.0, 0.0, 0.0),
                     None, "pivot", anim=True, mirror=m))
        out.append(P(f"wing_{side}_claw", f"wing_{side}_hand",
                     (0.0, 0.9, -0.6, -0.55, 0.0, 0.0),
                     (0.0, -0.8, -0.8, 1.6, 1.6, 5.0), "claw", mirror=m))

        # fingers: two phalanges each, the second curling under so the leading
        # edge arches instead of running dead straight
        for i, (ang, seg1, seg2, wid, curl) in enumerate(FINGERS, start=1):
            out.append(P(f"wing_{side}_f{i}a", f"wing_{side}_hand",
                         (0.0, 0.0, 0.0, sgn * -0.05 * i, sgn * ang, sgn * -0.03 * i),
                         (0.0, -wid / 2, -wid / 2, seg1, wid, wid), "wingbone", mirror=m))
            out.append(P(f"wing_{side}_f{i}b", f"wing_{side}_f{i}a",
                         (sgn * seg1, 0.0, 0.0, curl * sgn, 0.0, 0.0),
                         (0.0, -wid / 2 * 0.8, -wid / 2 * 0.8, seg2, wid * 0.8, wid * 0.8),
                         "wingbone", mirror=m))
        # one claw on the leading digit tip, as on a real bat wing
        lead1, lead2 = FINGERS[0][1], FINGERS[0][2]
        out.append(P(f"wing_{side}_tipclaw", f"wing_{side}_f1b",
                     (sgn * lead2, 0.0, 0.0, 0.0, 0.0, 0.0),
                     (0.0, -0.45, -0.45, 2.4, 0.9, 0.9), "claw", mirror=m))

        # membrane: one continuous panel per gap between neighbouring digits,
        # plus a cove in front of the first digit and a sail behind the last one.
        # Chord is taken from the longer finger of each pair so the cloth reaches
        # the tips, and the panel is skewed back to follow the sweep of the fan.
        spans = []
        for i in range(len(FINGERS) - 1):
            a_ang, a1, a2 = FINGERS[i][0], FINGERS[i][1], FINGERS[i][2]
            b_ang, b1, b2 = FINGERS[i + 1][0], FINGERS[i + 1][1], FINGERS[i + 1][2]
            spans.append(((a_ang + b_ang) * 0.5, min(a1 + a2, b1 + b2) * 0.98,
                          max(a1 + a2, b1 + b2) * CHORD * 1.10))
        spans.append((FINGERS[0][0] + 0.52, (FINGERS[0][1] + FINGERS[0][2]) * 0.66,
                      (FINGERS[0][1] + FINGERS[0][2]) * CHORD * 0.70))
        spans.append((FINGERS[-1][0] - 0.50, (FINGERS[-1][1] + FINGERS[-1][2]) * 0.62,
                      (FINGERS[-1][1] + FINGERS[-1][2]) * CHORD * 0.58))
        for i, (ang, length, chord) in enumerate(spans, start=1):
            out.append(P(f"wing_{side}_m{i}", f"wing_{side}_hand",
                         (0.4, 0.0, -chord * 0.34, 0.0, sgn * ang, sgn * -0.05 * i),
                         (0.0, -0.28, 0.0, length, 0.56, chord), "membrane", mirror=m))
    return out


# =============================================================== body parts
_BODY_PARTS = [
    # a pure pivot at the shoulder: every animation can breathe/bank from here
    P("body", "root", (0.0, BODY_PIVOT_Y, 0.0, 0.0, 0.0, 0.0), None, "pivot", anim=True),

    # chest is broad and deep, the waist pinches, the hips flare again
    P("chest", "body", (0.0, 0.9, -8.6, 0.10, 0.0, 0.0), (-6.4, -6.0, -8.0, 12.8, 12.0, 9.0), "hide"),
    P("torso", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-5.2, -5.8, -9.0, 10.4, 11.6, 18.0), "hide"),
    P("waist", "body", (0.0, -0.6, 8.4, -0.06, 0.0, 0.0), (-4.0, -4.4, -3.6, 8.0, 8.8, 7.2), "hide"),
    P("hips", "body", (0.0, -0.5, 12.6, -0.10, 0.0, 0.0), (-5.8, -5.6, -0.8, 11.6, 11.2, 9.0), "hide"),

    # ventral plating and the dorsal spine ridge
    P("belly_chest", "body", (0.0, 1.3, -8.6, 0.10, 0.0, 0.0), (-4.4, -7.2, -7.4, 8.8, 1.6, 8.2), "belly"),
    P("belly_torso", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-3.8, -6.6, -8.6, 7.6, 1.6, 17.2), "belly"),
    P("belly_waist", "body", (0.0, -0.6, 8.4, -0.06, 0.0, 0.0), (-3.0, -5.2, -3.4, 6.0, 1.4, 6.8), "belly"),
    P("belly_hips", "body", (0.0, -0.5, 12.6, -0.10, 0.0, 0.0), (-4.2, -6.4, -0.6, 8.4, 1.5, 8.6), "belly"),

    P("ridge_chest", "body", (0.0, 1.3, -8.6, 0.10, 0.0, 0.0), (-1.1, -7.8, -7.6, 2.2, 3.2, 8.4), "ridge"),
    P("ridge_torso", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-1.0, -7.4, -8.8, 2.0, 3.4, 17.6), "ridge"),
    P("ridge_waist", "body", (0.0, -0.8, 8.4, -0.05, 0.0, 0.0), (-0.8, -6.6, -3.4, 1.6, 2.6, 6.8), "ridge"),
    P("ridge_hips", "body", (0.0, -0.6, 12.6, -0.08, 0.0, 0.0), (-0.9, -8.0, -0.6, 1.8, 2.8, 8.6), "ridge"),

    # shoulder blades: two swept spikes each side, mirrored
    P("scapula_l", "chest", (6.2, 4.4, -0.4, 0.0, 0.0, -0.55), (-0.9, 0.0, -2.2, 1.8, 4.6, 2.2), "blade"),
    P("scapula_l2", "chest", (6.9, 3.2, 1.9, 0.0, 0.0, -0.75), (-0.8, 0.0, -1.8, 1.6, 3.8, 2.0), "blade"),
    P("scapula_r", "chest", (-6.2, 4.4, -0.4, 0.0, 0.0, 0.55), (-0.9, 0.0, -2.2, 1.8, 4.6, 2.2), "blade", mirror=True),
    P("scapula_r2", "chest", (-6.9, 3.2, 1.9, 0.0, 0.0, 0.75), (-0.8, 0.0, -1.8, 1.6, 3.8, 2.0), "blade", mirror=True),

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
    # Deliberately few, large forms. Minecraft's own dragons read because the
    # head is one big cranium, one big muzzle and one big jaw; slicing it into
    # many small boxes just makes visual noise at 16 texels per block.
    P("head", "neck5", (0.0, 0.2, -3.6, HEAD_ANGLE, 0.0, 0.0),
      (-4.8, -3.4, -9.4, 9.6, 6.8, 10.0), "skull", anim=True),
    # muzzle: one long box, slightly narrower and lower than the cranium
    P("muzzle", "head", (0.0, -1.3, -8.2, 0.04, 0.0, 0.0),
      (-3.1, -2.0, -9.0, 6.2, 4.0, 9.0), "muzzle"),
    # a short, blunt snout tip so the profile is not a perfect rectangle
    P("snout_tip", "muzzle", (0.0, 0.0, -8.8, -0.10, 0.0, 0.0),
      (-2.4, -1.6, -3.0, 4.8, 3.2, 3.0), "muzzle"),
    P("nostril_l", "snout_tip", (1.5, 1.1, -3.0, 0.0, 0.0, 0.0),
      (-0.45, -0.5, -0.5, 0.9, 1.0, 0.9), "small"),
    P("nostril_r", "snout_tip", (-1.5, 1.1, -3.0, 0.0, 0.0, 0.0),
      (-0.45, -0.5, -0.5, 0.9, 1.0, 0.9), "small", mirror=True),
    # lower jaw: one long box hinged at the back of the skull
    P("jaw", "head", (0.0, 2.6, -2.6, 0.04, 0.0, 0.0),
      (-2.9, 0.0, -15.0, 5.8, 1.8, 15.0), "jaw", anim=True),
    # a single tooth strip per side, inset so only the points show against the jaw
    P("teeth_l", "muzzle", (2.2, 1.9, -5.4, 0.0, 0.0, 0.0),
      (-0.35, 0.0, -1.0, 0.7, 1.2, 6.0), "tooth"),
    P("teeth_r", "muzzle", (-2.2, 1.9, -5.4, 0.0, 0.0, 0.0),
      (-0.35, 0.0, -1.0, 0.7, 1.2, 6.0), "tooth", mirror=True),
    # eyes: small, deep-set, under a heavy brow that casts a shadow
    P("brow_l", "head", (3.1, 1.7, -5.4, 0.0, 0.0, -0.34),
      (-1.1, -0.7, -4.4, 2.2, 1.4, 7.0), "skull"),
    P("brow_r", "head", (-3.1, 1.7, -5.4, 0.0, 0.0, 0.34),
      (-1.1, -0.7, -4.4, 2.2, 1.4, 7.0), "skull", mirror=True),
    P("eye_l", "head", (4.70, 0.55, -5.6, 0.0, 0.0, 0.0),
      (-0.20, -0.80, -2.0, 0.60, 1.6, 4.0), "eye"),
    P("eye_r", "head", (-4.70, 0.55, -5.6, 0.0, 0.0, 0.0),
      (-0.20, -0.80, -2.0, 0.60, 1.6, 4.0), "eye", mirror=True),
    # crown horns: one pair, swept up and back, thick at the root and tapering.
    # (Multiple pairs turned into pale clutter around the skull at this scale.)
    P("horn_l1", "head", (2.1, 2.8, -5.2, -0.62, 0.16, 0.0),
      (-1.05, -1.15, -1.0, 2.10, 2.30, 12.0), "horn"),
    P("horn_r1", "head", (-2.1, 2.8, -5.2, -0.62, -0.16, 0.0),
      (-1.05, -1.15, -1.0, 2.10, 2.30, 12.0), "horn", mirror=True),

    # ---------------------------------------------------------------- front legs
    # digitigrade: thigh forward, shin back, foot forward, three gripping talons
    P("leg_fl_thigh", "body", (5.4, -4.4, -8.4, 0.16, 0.0, 0.0), (-1.7, -10.0, -2.6, 3.4, 11.0, 5.6), "hide", anim=True),
    P("leg_fl_shin", "leg_fl_thigh", (0.0, -10.0, 0.3, -0.30, 0.0, 0.0), (-1.3, -9.4, -1.6, 2.6, 9.4, 3.2), "hide", anim=True),
    P("leg_fl_foot", "leg_fl_shin", (0.0, -9.4, -0.2, 0.22, 0.0, 0.0), (-1.7, -1.3, -3.4, 3.4, 2.3, 4.8), "foot", anim=True),
    P("leg_fl_talon1", "leg_fl_foot", (1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw"),
    P("leg_fl_talon2", "leg_fl_foot", (0.0, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.7, 0.84, 1.0, 2.2), "claw"),
    P("leg_fl_talon3", "leg_fl_foot", (-1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw"),

    P("leg_fr_thigh", "body", (-5.4, -4.4, -8.4, 0.16, 0.0, 0.0), (-1.7, -10.0, -2.6, 3.4, 11.0, 5.6), "hide", anim=True, mirror=True),
    P("leg_fr_shin", "leg_fr_thigh", (0.0, -10.0, 0.3, -0.30, 0.0, 0.0), (-1.3, -9.4, -1.6, 2.6, 9.4, 3.2), "hide", anim=True, mirror=True),
    P("leg_fr_foot", "leg_fr_shin", (0.0, -9.4, -0.2, 0.22, 0.0, 0.0), (-1.7, -1.3, -3.4, 3.4, 2.3, 4.8), "foot", anim=True, mirror=True),
    P("leg_fr_talon1", "leg_fr_foot", (1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw", mirror=True),
    P("leg_fr_talon2", "leg_fr_foot", (0.0, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.7, 0.84, 1.0, 2.2), "claw", mirror=True),
    P("leg_fr_talon3", "leg_fr_foot", (-1.5, -1.1, -3.1, 0.55, 0.0, 0.0), (-0.42, -0.9, -1.5, 0.84, 1.0, 2.0), "claw", mirror=True),

    # ---------------------------------------------------------------- hind legs
    # powerful haunches, longer than the arms, back-swept hock
    P("leg_bl_thigh", "body", (5.6, -4.2, 9.6, -0.34, 0.0, 0.0), (-2.8, -11.6, -3.0, 5.6, 12.6, 7.6), "hide", anim=True),
    P("leg_bl_shin", "leg_bl_thigh", (0.0, -11.6, 0.0, 0.66, 0.0, 0.0), (-1.6, -9.8, -2.0, 3.2, 9.8, 4.2), "hide", anim=True),
    P("leg_bl_foot", "leg_bl_shin", (0.0, -9.5, -0.3, -0.32, 0.0, 0.0), (-2.6, -1.5, -4.0, 5.2, 2.5, 6.0), "foot", anim=True),
    P("leg_bl_talon1", "leg_bl_foot", (1.7, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.7, 0.92, 1.1, 2.2), "claw"),
    P("leg_bl_talon2", "leg_bl_foot", (0.0, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.9, 0.92, 1.1, 2.4), "claw"),
    P("leg_bl_talon3", "leg_bl_foot", (-1.7, -1.2, -3.5, 0.55, 0.0, 0.0), (-0.46, -1.0, -1.7, 0.92, 1.1, 2.2), "claw"),

    P("leg_br_thigh", "body", (-5.6, -4.2, 9.6, -0.34, 0.0, 0.0), (-2.8, -11.6, -3.0, 5.6, 12.6, 7.6), "hide", anim=True, mirror=True),
    P("leg_br_shin", "leg_br_thigh", (0.0, -11.6, 0.0, 0.66, 0.0, 0.0), (-1.6, -9.8, -2.0, 3.2, 9.8, 4.2), "hide", anim=True, mirror=True),
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

TEXTURE_SIZE = 1024
# Texels per model unit. The rig is only ~48 units tall, so at 1:1 the whole head
# is barely 8 texels wide and no detail survives. Two texels per unit doubles the
# addressable detail on every surface while keeping the atlas inside 1024.
TEXELS_PER_UNIT = 2
