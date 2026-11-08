#!/usr/bin/env python3
"""Single source of truth for the Void Dragon model geometry.

Art direction (taken from the reference the user supplied):
  * gaunt, jagged black-violet void silhouette with glowing molten cracks
  * a deep-barrelled quadruped body carried low on four thick limbs -- the mass
    sits in the chest and haunches, not in the wings
  * a thick neck that rises out of the shoulders in a shallow S and carries a
    long, heavy skull level, like a hunting animal
  * narrow angular skull, swept-back ringed bone horns, crest blades, glowing
    slit eyes, hinged jaw full of needle teeth
  * huge tattered wings: bone arm, clawed wrist, five fingers, torn membrane
    panels painted with molten ribs. At rest they furl into a bundle that lies
    along the flanks; in flight the fan opens to its full span.
  * a spine ridge of void blades running from the neck to the spade-tipped tail
  * powerful digitigrade legs with gripping talons

Coordinate system: model units, 1 unit = 1/16 block, +X = left, +Y = up,
-Z = forward (the direction the dragon faces). Poses are
(x, y, z, rotX, rotY, rotZ) and are applied exactly like Minecraft's
PartPose / ModelPart (translation then Z-Y-X rotation).

MIRRORING: every paired part is authored once, for the LEFT side, and the right
side is produced by mirror_part(). The rule is

    x  -> -x            rotX ->  rotX
    y  ->  y            rotY -> -rotY
    z  ->  z            rotZ -> -rotZ
    box.x -> -(box.x + box.w)

which is the exact reflection of the whole transform chain through x = 0
(Minecraft's .mirror() flag only flips texture UVs, never geometry, so the box
has to be flipped by hand). Hand-authoring both sides is how the two wings ended
up sweeping in opposite directions once already.

The same data drives:
  tools/gen_model.py       -> src/main/java/com/voiddragon/client/DragonRig.java
  tools/gen_textures.py    -> 1024x1024 entity atlas (+ emissive glow atlas)
  tools/render_textured.py -> offline preview using Minecraft's exact box UVs
  tools/measure.py         -> numeric ruler (run it after every proportion edit)
"""

import math
import re

# The pivot the whole rig hangs from. Everything below is relative to it, so
# moving this single number lifts or lowers the entire dragon without changing
# the pose, and the animation code can bank/bob the whole body.
# Chosen so the deepest part of the belly clears the ground and all four feet
# land on y = 0 (verified by tools/measure.py).
BODY_PIVOT_Y = 27.7


def P(name, parent, pose, box, painter, mirror=False, anim=False):
    return dict(name=name, parent=parent, pose=pose, box=box, painter=painter,
                mirror=mirror, anim=anim)


# ------------------------------------------------------------------ mirroring
_NAME_PAIRS = (("_fl_", "_fr_"), ("_bl_", "_br_"), ("_left", "_right"),
               ("_l_", "_r_"))


def mirror_name(name):
    """wing_l -> wing_r, leg_fl_thigh -> leg_fr_thigh. None if unpaired."""
    for a, b in _NAME_PAIRS:
        if a in name:
            return name.replace(a, b, 1)
    m = re.match(r"^(.*)_l(\d+)$", name)
    if m:                                   # horn_l1, scapula_l2, ...
        return f"{m.group(1)}_r{m.group(2)}"
    if name.endswith("_l"):
        return name[:-2] + "_r"
    return None


def mirror_part(part):
    """The reflection of a left-side part through the x = 0 plane."""
    pose = part["pose"]
    box = part["box"]
    mname = mirror_name(part["name"])
    if mname is None:
        raise SystemExit(f"part {part['name']} has no mirrored name")
    mparent = mirror_name(part["parent"]) or part["parent"]
    mpose = (-pose[0], pose[1], pose[2], pose[3], -pose[4], -pose[5])
    mbox = None if box is None else (-(box[0] + box[3]), box[1], box[2],
                                     box[3], box[4], box[5])
    return P(mname, mparent, mpose, mbox, part["painter"], mirror=True,
             anim=part["anim"])


def PM(name, parent, pose, box, painter, **kw):
    """A left part plus its guaranteed-exact mirrored twin."""
    left = P(name, parent, pose, box, painter, mirror=False, **kw)
    return [left, mirror_part(left)]


def mirrored_pairs(parts):
    """[left parts] -> [left parts..., mirrored right parts...] (order kept)."""
    return list(parts) + [mirror_part(p) for p in parts]


# --------------------------------------------------------------------- neck
# Five segments. Each entry is (length, box_w, box_h, absolute_angle).
# Absolute angles (not relative ones) are what make an s-curve readable. The
# chain is SHORT and THICK: it leaves the shoulders at ~57 degrees, flattens
# fast, and the skull is carried only ~8 units above the spine instead of on a
# giraffe stalk. Every segment is drawn 30% longer than its bone so neighbouring
# boxes overlap by ~1.5 units and no seam can open at any camera angle.
NECK = [
    (6.4, 8.8, 10.0, 1.00),
    (6.0, 7.4, 8.4, 0.74),
    (5.6, 6.2, 7.0, 0.50),
    (5.2, 5.4, 6.0, 0.30),
    (4.6, 4.8, 5.2, 0.14),
]
# the skull is carried level, as a hunting animal holds it, not tipped down
HEAD_ANGLE = -0.12


def _neck_parts():
    """Builds neck1..neck5 + their dorsal spikes from the NECK table."""
    out = []
    parent = "body"
    prev_abs = 0.0
    # the chain starts on top of the chest, well inside the shoulder mass so the
    # neck grows out of the torso instead of being glued to it
    first_off = (0.0, 3.2, -10.6)
    for i, (length, w, h, ab) in enumerate(NECK, start=1):
        if i == 1:
            pivot = first_off
        else:
            pivot = (0.0, 0.0, -NECK[i - 2][0])
        local = ab - (prev_abs if i > 1 else 0.0)
        out.append(P(f"neck{i}", parent, (*pivot, local, 0.0, 0.0),
                     (-w / 2, -h / 2, -length * 1.24, w, h, length * 1.30), "hide", anim=True))
        # a spine fin on the crest of every neck segment, leaning back along the neck
        out.append(P(f"neck{i}_spine", f"neck{i}",
                     (0.0, h / 2 - 0.7, -length * 0.55, 0.62, 0.0, 0.0),
                     (-0.6, 0.0, -1.5, 1.2, 3.8, 1.8), "blade"))
        parent = f"neck{i}"
        prev_abs = ab
    return out


# --------------------------------------------------------------------- wings
# Anatomy: humerus -> forearm -> hand -> five fingers of two phalanges each.
# The membrane is generated from the finger table, so the cloth always fills the
# gaps between the digits and reaches the tips instead of leaving bare rods.
ARM_LEN = 13.0          # humerus
FORE_LEN = 13.5         # radius / ulna
# (yaw, first phalanx, second phalanx, width, curl of the second phalanx)
# Yaws are the OPEN, in-flight fan. The bones are thick enough to read through
# the cloth: at 1 texel per unit a 1-unit finger is two texels wide and simply
# disappears.
FINGERS = [
    (0.30, 14.0, 11.0, 2.4, 0.10),
    (0.05, 16.5, 13.0, 2.1, 0.16),
    (-0.22, 16.0, 12.0, 1.8, 0.22),
    (-0.52, 13.0, 9.5, 1.5, 0.28),
    (-0.85, 9.0, 6.0, 1.2, 0.34),
]
# How tightly the fan closes at rest: 1.0 = the full flight fan, 0.0 = every
# finger stacked on the same line. 0.30 leaves the digits legible as separate
# bones while collapsing the wing into a bundle that lies along the flank
# instead of standing up as a slab.
WING_FOLD = 0.90
# Membrane depth multiplier: 1.0 makes each panel exactly as deep as the gap it
# fills; a little over 1 lets the cloth sag between the bones.
CHORD = 1.15

# Furled rest pose (LEFT side). The humerus sweeps back along the ribs, the
# forearm lifts to a wrist above the hips, and the hand trails the closed fan
# backwards. Nothing rises above the spine: measured, the furled wing tops out
# within a unit of the back and never drops below the belly line.
WING_ROOT_POSE = (0.00, -1.157, -0.455)   # (xRot, yRot, zRot)
WING_FORE_POSE = (0.00, -0.639, 0.280)
WING_HAND_POSE = (0.00, 0.500, -0.200)


def _wing_side(side):
    """One wing, left or right (the right one is produced by mirror_part)."""
    out = []
    sgn = 1.0 if side == "l" else -1.0
    # local +X is the direction a bone points; the box runs 0..length so it can
    # be mirrored by flipping the box to -length..0
    rx, ry, rz = WING_ROOT_POSE
    out.append(P(f"wing_{side}", "body",
                 (sgn * 6.8, 2.8, -5.4, rx, sgn * ry, sgn * rz),
                 (0.0, -2.4, -2.6, ARM_LEN, 4.8, 5.2), "hide", anim=True))
    # shoulder mass so the arm does not sprout from a bare stick
    out.append(P(f"wing_{side}_deltoid", f"wing_{side}",
                 (sgn * 0.8, 0.6, -1.8, 0.0, 0.0, 0.0),
                 (0.0, -3.0, -2.0, 7.0, 6.4, 6.4), "hide"))
    fx, fy, fz = WING_FORE_POSE
    out.append(P(f"wing_{side}_fore", f"wing_{side}",
                 (sgn * ARM_LEN, 0.0, 0.0, fx, sgn * fy, sgn * fz),
                 (0.0, -1.8, -2.0, FORE_LEN, 3.6, 4.0), "hide", anim=True))
    hx, hy, hz = WING_HAND_POSE
    out.append(P(f"wing_{side}_hand", f"wing_{side}_fore",
                 (sgn * FORE_LEN, 0.0, 0.0, hx, sgn * hy, sgn * hz),
                 None, "pivot", anim=True))
    # thumb claw at the wrist
    out.append(P(f"wing_{side}_claw", f"wing_{side}_hand",
                 (0.0, 0.9, -0.6, -0.55, 0.0, 0.0),
                 (0.0, -0.9, -0.9, 1.8, 1.8, 5.4), "claw"))
    # fingers: two phalanges each, the second curling under so the leading edge
    # arches instead of running dead straight
    for i, (ang, seg1, seg2, wid, curl) in enumerate(FINGERS, start=1):
        out.append(P(f"wing_{side}_f{i}a", f"wing_{side}_hand",
                     (0.0, 0.0, 0.0, sgn * -0.05 * i, sgn * ang, sgn * -0.03 * i),
                     (0.0, -wid / 2, -wid / 2, seg1, wid, wid), "wingbone"))
        out.append(P(f"wing_{side}_f{i}b", f"wing_{side}_f{i}a",
                     (sgn * seg1, 0.0, 0.0, curl * sgn, 0.0, 0.0),
                     (0.0, -wid / 2 * 0.8, -wid / 2 * 0.8, seg2, wid * 0.8, wid * 0.8),
                     "wingbone"))
    lead2 = FINGERS[0][2]
    out.append(P(f"wing_{side}_tipclaw", f"wing_{side}_f1b",
                 (sgn * lead2, 0.0, 0.0, 0.0, 0.0, 0.0),
                 (0.0, -0.5, -0.5, 2.6, 1.0, 1.0), "claw"))
    # membrane: one panel per gap between neighbouring digits, plus the
    # propatagium in front of the first digit and a trailing sail behind the
    # last. The cloth between two fingers is a LONG NARROW wedge -- its depth is
    # the tip-to-tip gap, 2*L*sin(spread/2), not a fraction of the bone. Panels
    # sized off the bone length turned the wing into one 18-unit-thick slab and
    # hid every finger; these leave the bones standing proud of the cloth.
    spans = []
    for i in range(len(FINGERS) - 1):
        a_ang, a1, a2 = FINGERS[i][0], FINGERS[i][1], FINGERS[i][2]
        b_ang, b1, b2 = FINGERS[i + 1][0], FINGERS[i + 1][1], FINGERS[i + 1][2]
        la, lb = a1 + a2, b1 + b2
        spans.append(((a_ang + b_ang) * 0.5, min(la, lb) * 0.98,
                      2.0 * (la + lb) * 0.5 * math.sin(abs(a_ang - b_ang) * 0.5) * CHORD + 1.5))
    lead = FINGERS[0][1] + FINGERS[0][2]
    last = FINGERS[-1][1] + FINGERS[-1][2]
    spans.append((FINGERS[0][0] + 0.30, lead * 0.62, lead * 0.20 * CHORD + 1.2))
    spans.append((FINGERS[-1][0] - 0.28, last * 0.70, last * 0.30 * CHORD + 1.2))
    for i, (ang, length, chord) in enumerate(spans, start=1):
        out.append(P(f"wing_{side}_m{i}", f"wing_{side}_hand",
                     (0.4, 0.0, -chord * 0.34, 0.0, sgn * ang, sgn * -0.05 * i),
                     (0.0, -0.30, 0.0, length, 0.60, chord), "membrane"))
    return out


def folded_yaws():
    """{part suffix -> absolute yRot at rest} for the LEFT wing.

    Every finger closes towards the MEAN of the open fan, so folding the wing
    does not swing the whole bundle sideways. The membrane panels follow the
    average of the two fingers they hang between.
    """
    mean = sum(f[0] for f in FINGERS) / len(FINGERS)
    out = {}
    for i, (ang, _s1, _s2, _w, _c) in enumerate(FINGERS, start=1):
        out[f"f{i}a"] = mean + (ang - mean) * WING_FOLD
    for i in range(1, len(FINGERS)):
        out[f"m{i}"] = (out[f"f{i}a"] + out[f"f{i + 1}a"]) * 0.5
    out["m5"] = out["f1a"] + 0.52 * WING_FOLD
    out["m6"] = out["f5a"] - 0.50 * WING_FOLD
    return out


def _wings():
    """Left wing followed by its mirrored twin."""
    return mirrored_pairs(_wing_side("l"))


# ============================================================== tail
# Seven segments. Each box is drawn 1.4 units longer than its bone and the next
# segment starts 1.2 units BEFORE the end of the previous one, so the chain
# overlaps by ~2.6 units: the old tail broke into floating blocks at 3/4 views
# because segment met segment at a single point.
_TAIL = [
    (10.0, 8.0),
    (9.6, 7.0),
    (9.2, 6.0),
    (8.6, 5.0),
    (8.0, 4.0),
    (7.0, 3.0),
    (6.0, 2.0),
]
_TAIL_X = (-0.20, -0.06, 0.00, 0.05, 0.09, 0.12, 0.12)


def _tail_parts():
    out = []
    parent = "body"
    for i, ((length, w), xr) in enumerate(zip(_TAIL, _TAIL_X), start=1):
        if i == 1:
            pivot = (0.0, -2.0, 15.0)
        else:
            pivot = (0.0, 0.0, _TAIL[i - 2][0] - 1.2)
        out.append(P(f"tail{i}", parent, (*pivot, xr, 0.0, 0.0),
                     (-w / 2, -w / 2, -1.4, w, w, length + 1.4), "hide", anim=True))
        if 2 <= i <= 6:
            out.append(P(f"tail{i}_spike", f"tail{i}", (0.0, w / 2 + 0.1, 2.6, 0.0, 0.0, 0.0),
                         (-0.65, -0.2, -2.6, 1.30, 3.4, 6.0), "blade"))
        parent = f"tail{i}"
    # spade tip: a cross shaped void blade
    out.append(P("spade_top", "tail7", (0.0, -0.2, 5.4, -0.34, 0.0, 0.0),
                 (-0.8, -0.2, 0.0, 1.6, 3.8, 6.8), "blade"))
    out.append(P("spade_bot", "tail7", (0.0, 0.2, 5.6, 0.05, 0.0, 0.0),
                 (-0.8, 0.0, 0.0, 1.6, 2.9, 6.0), "blade"))
    out += PM("spade_left", "tail7", (0.75, 0.0, 5.0, 0.0, 0.0, -0.7),
              (-0.30, -0.55, 0.0, 0.60, 1.10, 4.8), "blade")
    return out


# =============================================================== body parts
def _body_parts():
    out = [
        # a pure pivot at the shoulder: every animation can breathe/bank from here
        P("body", "root", (0.0, BODY_PIVOT_Y, 0.0, 0.0, 0.0, 0.0), None, "pivot", anim=True),

        # Deep barrel: the chest is the widest and deepest station, the waist
        # pinches, the hips flare again. ~17 units deep (~1.1 blocks) so the
        # dragon has mass instead of being a lizard on stilts.
        P("chest", "body", (0.0, 0.8, -9.8, 0.10, 0.0, 0.0), (-7.5, -8.5, -5.4, 15.0, 17.0, 10.8), "hide"),
        P("torso", "body", (0.0, 0.0, 0.4, 0.0, 0.0, 0.0), (-6.5, -8.2, -9.6, 13.0, 16.4, 19.2), "hide"),
        P("waist", "body", (0.0, -0.6, 9.6, -0.05, 0.0, 0.0), (-5.1, -5.9, -4.2, 10.2, 11.8, 8.4), "hide"),
        P("hips", "body", (0.0, -0.5, 14.2, -0.08, 0.0, 0.0), (-6.8, -6.9, -1.0, 13.6, 13.8, 11.2), "hide"),

        # ventral plating and the dorsal spine ridge
        P("belly_chest", "body", (0.0, 1.2, -9.8, 0.10, 0.0, 0.0), (-5.2, -9.7, -5.0, 10.4, 1.8, 10.0), "belly"),
        P("belly_torso", "body", (0.0, 0.0, 0.4, 0.0, 0.0, 0.0), (-4.6, -9.6, -9.4, 9.2, 1.8, 18.8), "belly"),
        P("belly_waist", "body", (0.0, -0.6, 9.6, -0.05, 0.0, 0.0), (-3.6, -7.6, -4.0, 7.2, 1.6, 8.0), "belly"),
        P("belly_hips", "body", (0.0, -0.5, 14.2, -0.08, 0.0, 0.0), (-5.0, -8.5, -0.8, 10.0, 1.7, 10.4), "belly"),

        P("ridge_chest", "body", (0.0, 1.2, -9.8, 0.10, 0.0, 0.0), (-1.3, -10.6, -5.2, 2.6, 3.6, 10.4), "ridge"),
        P("ridge_torso", "body", (0.0, 0.0, 0.4, 0.0, 0.0, 0.0), (-1.2, -10.4, -9.4, 2.4, 3.8, 18.8), "ridge"),
        P("ridge_waist", "body", (0.0, -0.7, 9.6, -0.05, 0.0, 0.0), (-1.0, -8.4, -4.0, 2.0, 2.8, 8.0), "ridge"),
        P("ridge_hips", "body", (0.0, -0.6, 14.2, -0.08, 0.0, 0.0), (-1.1, -9.8, -0.8, 2.2, 3.2, 10.4), "ridge"),
    ]

    # shoulder blades and hip spurs: swept spikes, mirrored
    out += PM("scapula_l", "chest", (7.2, 5.0, -0.6, 0.0, 0.0, -0.55),
              (-1.05, 0.0, -2.4, 2.1, 5.2, 2.4), "blade")
    out += PM("scapula_l2", "chest", (8.0, 3.6, 2.0, 0.0, 0.0, -0.75),
              (-0.95, 0.0, -2.0, 1.9, 4.4, 2.2), "blade")
    out += PM("hip_spur_l", "hips", (7.6, 4.0, 2.8, 0.0, 0.0, -0.9),
              (-0.8, 0.0, -2.2, 1.6, 5.0, 2.4), "blade")

    # ---------------------------------------------------------------- head
    # Deliberately few, large forms. Minecraft's own dragons read because the
    # head is one big cranium, one big muzzle and one big jaw; slicing it into
    # many small boxes just makes visual noise at 16 texels per block.
    # The skull is ~23 units long against a 40 unit torso -- big enough to carry
    # the horns and the jaw without looking like a lizard's pin-head.
    out += [
        P("head", "neck5", (0.0, 0.4, -4.4, HEAD_ANGLE, 0.0, 0.0),
          (-5.6, -4.4, -10.8, 11.2, 8.8, 11.5), "skull", anim=True),
        P("muzzle", "head", (0.0, -1.8, -8.4, 0.04, 0.0, 0.0),
          (-3.6, -2.4, -11.0, 7.2, 4.8, 11.0), "muzzle"),
        P("nasal_bridge", "head", (0.0, 2.4, -5.6, 0.0, 0.0, 0.0),
          (-3.1, -0.8, -5.4, 6.2, 1.6, 5.4), "muzzle"),
        P("snout_tip", "muzzle", (0.0, 0.0, -9.0, -0.10, 0.0, 0.0),
          (-2.7, -1.9, -3.2, 5.4, 3.8, 3.2), "muzzle"),
        P("jaw", "head", (0.0, -2.9, -4.2, 0.16, 0.0, 0.0),
          (-3.4, -2.4, -14.4, 6.8, 2.4, 14.4), "jaw", anim=True),
        P("chin", "jaw", (0.0, 0.2, -13.4, 0.10, 0.0, 0.0),
          (-1.8, -0.9, -1.5, 3.6, 1.1, 1.5), "jaw"),
    ]
    out += PM("nostril_l", "snout_tip", (2.75, 0.2, -1.5, 0.0, 0.0, 0.0),
              (-0.20, -0.45, -0.60, 0.40, 0.9, 1.2), "small")
    out += PM("teeth_l", "muzzle", (2.3, -2.3, -4.6, 0.0, 0.0, 0.0),
              (-0.34, -2.40, -1.1, 0.68, 2.40, 6.4), "tooth")
    out += PM("jaw_muscle_l", "head", (5.15, -1.5, -4.2, 0.0, 0.0, 0.18),
              (-0.8, -2.0, -3.8, 1.6, 4.0, 7.6), "skull")
    out += PM("cheek_ridge_l", "head", (5.75, 0.5, -6.4, 0.0, -0.22, -0.55),
              (-0.55, -0.55, -1.3, 1.10, 1.10, 5.4), "blade")
    out += PM("brow_l", "head", (4.60, 1.9, -5.2, 0.0, 0.0, -0.34),
              (-1.25, -0.8, -4.6, 2.5, 1.6, 7.4), "skull")
    out += PM("eye_l", "head", (5.45, 0.6, -5.4, 0.0, 0.0, 0.0),
              (-0.22, -0.90, -2.1, 0.66, 1.8, 4.2), "eye")
    # crown horns: one pair, swept up and back, thick at the root and tapering.
    # (Multiple pairs turned into pale clutter around the skull at this scale.)
    out += PM("horn_l1", "head", (2.4, 3.4, -7.8, -0.46, 0.18, 0.0),
              (-1.15, -1.25, -3.0, 2.30, 2.50, 11.5), "horn")

    # ---------------------------------------------------------------- front legs
    # digitigrade: thigh forward, shin back, foot forward, three gripping talons.
    # Each limb is a thick upper mass (haunch), a tapered shin with a calf bulge
    # and a broad foot -- the thigh alone is 7 units wide against a 17 unit deep
    # chest, so the dragon stands on legs, not on poles.
    out += PM("leg_fl_thigh", "body", (7.0, -5.5, -8.6, 0.16, 0.0, 0.0),
              (-3.5, -11.6, -4.5, 7.0, 11.6, 9.0), "hide", anim=True)
    out += PM("leg_fl_haunch", "leg_fl_thigh", (0.6, -1.8, -0.6, 0.0, 0.0, 0.0),
              (-3.9, -4.2, -5.2, 7.8, 8.4, 10.4), "hide")
    out += PM("leg_fl_shin", "leg_fl_thigh", (0.0, -11.2, 0.4, -0.30, 0.0, 0.0),
              (-2.7, -9.4, -3.0, 5.4, 9.4, 6.0), "hide", anim=True)
    out += PM("leg_fl_calf", "leg_fl_shin", (0.0, -3.0, -0.8, 0.0, 0.0, 0.0),
              (-3.0, -3.5, -3.6, 6.0, 7.0, 7.2), "hide")
    out += PM("leg_fl_foot", "leg_fl_shin", (0.0, -9.0, -0.2, 0.22, 0.0, 0.0),
              (-2.9, -1.7, -4.4, 5.8, 3.4, 7.0), "foot", anim=True)
    out += PM("leg_fl_talon1", "leg_fl_foot", (1.9, -1.3, -3.6, 0.55, 0.0, 0.0),
              (-0.48, -1.0, -1.7, 0.96, 1.1, 2.2), "claw")
    out += PM("leg_fl_talon2", "leg_fl_foot", (0.0, -1.3, -3.6, 0.55, 0.0, 0.0),
              (-0.48, -1.0, -1.9, 0.96, 1.1, 2.4), "claw")
    out += PM("leg_fl_talon3", "leg_fl_foot", (-1.9, -1.3, -3.6, 0.55, 0.0, 0.0),
              (-0.48, -1.0, -1.7, 0.96, 1.1, 2.2), "claw")

    # ---------------------------------------------------------------- hind legs
    # powerful haunches, longer than the arms, back-swept hock
    out += PM("leg_bl_thigh", "body", (7.4, -4.8, 10.0, -0.34, 0.0, 0.0),
              (-4.7, -12.4, -5.6, 9.4, 12.4, 11.2), "hide", anim=True)
    out += PM("leg_bl_haunch", "leg_bl_thigh", (0.6, -2.0, -0.6, 0.0, 0.0, 0.0),
              (-5.2, -4.6, -6.2, 10.4, 9.2, 12.4), "hide")
    out += PM("leg_bl_shin", "leg_bl_thigh", (0.0, -12.0, 0.0, 0.66, 0.0, 0.0),
              (-3.0, -10.2, -3.3, 6.0, 10.2, 6.6), "hide", anim=True)
    out += PM("leg_bl_calf", "leg_bl_shin", (0.0, -3.2, -0.8, 0.0, 0.0, 0.0),
              (-3.3, -3.6, -3.9, 6.6, 7.2, 7.8), "hide")
    out += PM("leg_bl_foot", "leg_bl_shin", (0.0, -9.6, -0.4, -0.32, 0.0, 0.0),
              (-3.7, -1.9, -5.4, 7.4, 3.6, 8.6), "foot", anim=True)
    out += PM("leg_bl_talon1", "leg_bl_foot", (2.0, -1.4, -4.2, 0.55, 0.0, 0.0),
              (-0.52, -1.1, -1.9, 1.04, 1.2, 2.4), "claw")
    out += PM("leg_bl_talon2", "leg_bl_foot", (0.0, -1.4, -4.2, 0.55, 0.0, 0.0),
              (-0.52, -1.1, -2.1, 1.04, 1.2, 2.6), "claw")
    out += PM("leg_bl_talon3", "leg_bl_foot", (-2.0, -1.4, -4.2, 0.55, 0.0, 0.0),
              (-0.52, -1.1, -1.9, 1.04, 1.2, 2.4), "claw")

    # ---------------------------------------------------------------- gear
    out += [
        P("saddle", "body", (0.0, 6.4, 1.5, 0.0, 0.0, 0.0), (-5.6, -1.0, -6.4, 11.2, 2.0, 12.8), "saddle"),
        P("saddle_pommel", "saddle", (0.0, 1.0, -5.6, 0.0, 0.0, 0.0), (-1.9, -2.6, -1.9, 3.8, 2.8, 3.8), "saddle"),
        P("saddle_cantle", "saddle", (0.0, 1.0, 5.6, 0.0, 0.0, 0.0), (-2.2, -2.2, -1.4, 4.4, 2.4, 2.8), "saddle"),

        P("armor_chest", "body", (0.0, 1.2, -9.8, 0.10, 0.0, 0.0), (-9.0, -8.6, -5.5, 18.0, 15.6, 11.0), "armor"),
        P("armor_torso", "body", (0.0, 0.0, 0.4, 0.0, 0.0, 0.0), (-7.8, -8.3, -9.7, 15.6, 15.0, 19.4), "armor"),
        P("armor_hips", "body", (0.0, -0.6, 14.2, -0.08, 0.0, 0.0), (-8.1, -7.0, -1.1, 16.2, 14.0, 11.4), "armor"),
        P("armor_collar", "body", (0.0, 3.8, -8.4, 0.45, 0.0, 0.0), (-7.0, -1.5, -3.2, 14.0, 3.0, 6.4), "armor"),
    ]
    out += PM("saddle_strap_l", "body", (6.6, 0.0, 1.5, 0.0, 0.0, 0.0),
              (-0.62, -8.6, -5.2, 1.24, 10.8, 10.4), "saddle")
    return out


def _all_parts():
    # body first (the pivot every other part hangs from), then the neck, then
    # everything else, then the tail chain, then the wings
    return (_body_parts()[:5] + _neck_parts() + _body_parts()[5:]
            + _tail_parts() + _wings())


PARTS = _all_parts()

# groups hidden unless the dragon is saddled / armoured
GEAR = {
    "saddle": ["saddle", "saddle_pommel", "saddle_cantle", "saddle_strap_l", "saddle_strap_r"],
    "armor": ["armor_chest", "armor_torso", "armor_hips", "armor_collar"],
}

TEXTURE_SIZE = 1024
# Texels per model unit. Minecraft sizes a cube's UV island straight from the box
# dimensions (2*(w+d) by (d+h) texels, whatever the atlas size), so this MUST
# stay at 1: painter and sampler then agree and every painted texel lands on the
# model. Any other value allocates an island the game never samples, which crops
# the art to a fraction of itself -- that is why the wings used to look like
# flat slabs of one colour.
TEXELS_PER_UNIT = 1
