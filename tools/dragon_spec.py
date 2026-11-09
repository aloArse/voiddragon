#!/usr/bin/env python3
"""Single source of truth for the Void Dragon model geometry.

Art direction, taken from the reference the user supplied (a House-of-the-Dragon
style dragon):

  * a heavy, low-slung quadruped: a deep barrel chest, a pinched waist and
    powerful haunches. The mass sits in the body, never in the limbs.
  * a SHORT, thick neck that leaves the shoulders in a shallow S and carries the
    skull forward and slightly down, the way a hunting animal holds its head --
    not a giraffe stalk.
  * a long, narrow, crocodilian skull: a wedge cranium, brow ridges over the
    eyes, a long muzzle, a hinged lower jaw full of needle teeth, cheek spines
    and a pair of thick horns swept back over the neck.
  * a continuous dorsal ridge of blades from the skull to the spade tail.
  * ENORMOUS bat wings. Bone arm, clawed wrist, five long fingers, and deep
    crimson leather stretched between them, torn along the trailing edge. On the
    ground the fan collapses into a tall bundle that rides above the flanks; in
    the air it opens to its full span.
  * soot-black hide, oxblood membrane, ember light in the seams, pale bone
    horns and claws. (The colours live in tools/gen_textures.py.)

Coordinate system: model units, 1 unit = 1/16 block, +X = left, +Y = up,
-Z = forward (the direction the dragon faces). Feet stand on y = 0. Poses are
(x, y, z, rotX, rotY, rotZ) and are applied exactly like Minecraft's PartPose /
ModelPart (translation, then Z-Y-X rotation).

MIRRORING: every paired part is authored once, for the LEFT side, and the right
side is produced by mirror_part():

    x  -> -x            rotX ->  rotX
    y  ->  y            rotY -> -rotY
    z  ->  z            rotZ -> -rotZ
    box.x -> -(box.x + box.w)

which is the exact reflection of the whole transform chain through x = 0
(Minecraft's .mirror() flag only flips texture UVs, never geometry, so the box
has to be flipped by hand).

The same data drives:
  tools/gen_model.py       -> src/main/java/com/voiddragon/client/DragonRig.java
                              and DragonPose.java (the animation constants)
  tools/gen_textures.py    -> 1024x1024 entity atlas (+ emissive glow atlas)
  tools/render_textured.py -> offline preview using Minecraft's exact box UVs
  tools/measure.py         -> numeric ruler (run it after every proportion edit)
"""

import math
import re

# The pivot the whole rig hangs from. Everything below is relative to it, so
# moving this single number lifts or lowers the entire dragon without changing
# the pose. Chosen so the belly clears the ground and all four feet land on y = 0
# (verified by tools/measure.py).
BODY_PIVOT_Y = 28.8


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


# ---------------------------------------------------------------------- neck
# Four segments. Each entry is (length, box_w, box_h, absolute_angle).
# Absolute angles (not relative ones) are what make the S readable: the neck
# leaves the shoulders steeply, flattens fast and lets the skull hang forward.
# Every box is drawn ~30% longer than its bone so neighbouring boxes overlap and
# no seam can open at any camera angle.
NECK = [
    (6.8, 13.0, 13.6, 1.16),
    (6.2, 11.2, 11.8, 0.72),
    (5.6, 9.6, 10.2, 0.38),
]
# the skull is carried nearly level, tipped a hair down like a hunting animal
HEAD_ANGLE = -0.10


def _neck_parts():
    """Builds neck1..neck4 plus the dorsal blades on the crest of each."""
    out = []
    parent = "body"
    prev_abs = 0.0
    # start the chain inside the shoulder mass, so the neck grows out of the
    # chest instead of being glued onto it
    first_off = (0.0, 4.6, -10.2)
    for i, (length, w, h, ab) in enumerate(NECK, start=1):
        pivot = first_off if i == 1 else (0.0, 0.0, -NECK[i - 2][0])
        local = ab - (prev_abs if i > 1 else 0.0)
        out.append(P(f"neck{i}", parent, (*pivot, local, 0.0, 0.0),
                     (-w / 2, -h / 2, -length * 1.24, w, h, length * 1.30),
                     "hide", anim=True))
        # throat plates: a lighter band under every neck segment
        out.append(P(f"neck{i}_throat", f"neck{i}",
                     (0.0, -h / 2 + 0.4, -length * 0.55, 0.0, 0.0, 0.0),
                     (-w * 0.34, -0.9, -length * 0.52, w * 0.68, 1.8, length * 1.05),
                     "belly"))
        # spine blade on the crest, leaning back along the neck
        out.append(P(f"neck{i}_spine", f"neck{i}",
                     (0.0, h / 2 - 0.8, -length * 0.55, 0.60, 0.0, 0.0),
                     (-0.55, 0.0, -2.2, 1.1, 4.2, 1.8), "blade"))
        parent = f"neck{i}"
        prev_abs = ab
    return out


# --------------------------------------------------------------------- wings
# Anatomy: humerus -> forearm -> hand -> five fingers of two phalanges.
# The membrane is one panel per gap between fingers, sized from the tip-to-tip
# gap of the two bones it spans, so the cloth always reaches the fingers and the
# bones stay proud of it instead of dissolving into a slab.
ARM_LEN = 16.5          # humerus
FORE_LEN = 17.5         # radius / ulna
# (yaw, first phalanx, second phalanx, width, curl of the second phalanx)
# Yaws are the OPEN, in-flight fan: a wide sweep from the leading edge (f1) to
# the trailing edge (f5) -- this is what gives a HotD wing its bat-like shape.
FINGERS = [
    (0.36, 17.0, 14.0, 2.6, 0.08),
    (0.08, 19.5, 16.0, 2.3, 0.14),
    (-0.20, 19.0, 15.0, 2.0, 0.20),
    (-0.50, 16.0, 12.0, 1.7, 0.26),
    (-0.86, 11.5, 8.0, 1.4, 0.32),
]
# Membrane depth multiplier: a touch over 1 lets the cloth sag between the bones.
CHORD = 1.34

# --- the pose the geometry is AUTHORED in: wings spread, as in the reference
# art. The renderer/model file bakes this, and the animation layer then folds the
# wing up on the ground or beats it in the air (see the constants below).
REST_ROOT = (0.06, -0.20, -0.06)      # (xRot, yRot, zRot)
REST_FORE = (0.00, 0.10, 0.04)
REST_HAND = (0.00, 0.02, -0.04)

# --- folded (grounded) pose of the LEFT wing, applied by the animation layer.
# The arm swings back along the ribs and rolls down so the fan hangs behind the
# shoulder; the forearm folds the fan forward again. The renderer's preview uses
# exactly these numbers, so what the preview shows is what the game shows.
FOLD_ROOT = (-0.16, -1.02, 0.52)
FOLD_FORE = (0.00, -0.62, -0.34)
FOLD_HAND = (0.00, 0.00, -0.34)

# --- flight pose of the LEFT wing (mid-beat); the model adds the flap on top.
FLY_ROOT = (0.06, -0.20, -0.06)
FLY_FORE = (0.00, 0.10, 0.04)
FLY_HAND = (0.00, 0.02, -0.04)

# Amplitude of the beat, in radians, applied on top of the flight pose.
FLAP_ROOT_Z = 0.34
FLAP_FORE_Z = 0.16
FLAP_FINGER_LAG = 0.10

# How much of the open fan survives when the wing is folded on the ground.
FOLD_FAN = 0.50
# Extra unfurl when the dragon rears up to roar / breathe.
ROAR_UNFURL = 0.30


def _wing_side(side):
    """One wing, left or right (the right one is produced by mirror_part)."""
    out = []
    sgn = 1.0 if side == "l" else -1.0
    rx, ry, rz = REST_ROOT
    out.append(P(f"wing_{side}", "body",
                 (sgn * 7.2, 3.0, -6.4, rx, sgn * ry, sgn * rz),
                 (0.0, -2.7, -2.9, ARM_LEN, 5.4, 5.8), "hide", anim=True))
    # shoulder mass so the arm does not sprout from a bare stick
    out.append(P(f"wing_{side}_deltoid", f"wing_{side}",
                 (sgn * 0.6, 0.8, -2.2, 0.0, 0.0, 0.0),
                 (0.0, -3.6, -2.6, 8.4, 7.6, 7.4), "hide"))
    fx, fy, fz = REST_FORE
    out.append(P(f"wing_{side}_fore", f"wing_{side}",
                 (sgn * ARM_LEN, 0.0, 0.0, fx, sgn * fy, sgn * fz),
                 (0.0, -2.0, -2.2, FORE_LEN, 4.0, 4.4), "hide", anim=True))
    hx, hy, hz = REST_HAND
    out.append(P(f"wing_{side}_hand", f"wing_{side}_fore",
                 (sgn * FORE_LEN, 0.0, 0.0, hx, sgn * hy, sgn * hz),
                 None, "pivot", anim=True))
    # thumb claw at the wrist, hooked forward
    out.append(P(f"wing_{side}_claw", f"wing_{side}_hand",
                 (sgn * 0.2, 1.0, -0.8, -0.60, 0.0, 0.0),
                 (0.0, -1.0, -1.0, 2.2, 2.2, 6.6), "claw"))
    # fingers: two phalanges each, the second curling so the leading edge arches
    for i, (ang, seg1, seg2, wid, curl) in enumerate(FINGERS, start=1):
        out.append(P(f"wing_{side}_f{i}a", f"wing_{side}_hand",
                     (0.0, 0.0, 0.0, sgn * -0.04 * i, sgn * ang, sgn * -0.03 * i),
                     (0.0, -wid / 2, -wid / 2, seg1, wid, wid), "wingbone"))
        out.append(P(f"wing_{side}_f{i}b", f"wing_{side}_f{i}a",
                     (sgn * seg1, 0.0, 0.0, curl * sgn, 0.0, 0.0),
                     (0.0, -wid / 2 * 0.8, -wid / 2 * 0.8, seg2, wid * 0.8, wid * 0.8),
                     "wingbone"))
    lead2 = FINGERS[0][2]
    out.append(P(f"wing_{side}_tipclaw", f"wing_{side}_f1b",
                 (sgn * lead2, 0.0, 0.0, 0.0, 0.0, 0.0),
                 (0.0, -0.6, -0.6, 3.0, 1.2, 1.2), "claw"))
    # membrane: one panel per gap between neighbouring digits, plus the
    # propatagium in front of the first digit and the trailing sail behind the
    # last. The cloth between two fingers is a LONG NARROW wedge whose depth is
    # the tip-to-tip gap, not a fraction of the bone length.
    spans = []
    for i in range(len(FINGERS) - 1):
        a_ang, a1, a2 = FINGERS[i][0], FINGERS[i][1], FINGERS[i][2]
        b_ang, b1, b2 = FINGERS[i + 1][0], FINGERS[i + 1][1], FINGERS[i + 1][2]
        la, lb = a1 + a2, b1 + b2
        spans.append(((a_ang + b_ang) * 0.5, min(la, lb) * 1.02,
                      2.0 * (la + lb) * 0.5 * math.sin(abs(a_ang - b_ang) * 0.5) * CHORD + 1.8))
    lead = FINGERS[0][1] + FINGERS[0][2]
    last = FINGERS[-1][1] + FINGERS[-1][2]
    spans.append((FINGERS[0][0] + 0.34, lead * 0.74, lead * 0.26 * CHORD + 1.4))
    spans.append((FINGERS[-1][0] - 0.30, last * 0.78, last * 0.34 * CHORD + 1.4))
    for i, (ang, length, chord) in enumerate(spans, start=1):
        out.append(P(f"wing_{side}_m{i}", f"wing_{side}_hand",
                     (0.4, 0.0, -chord * 0.34, 0.0, sgn * ang, sgn * -0.05 * i),
                     (0.0, -0.32, 0.0, length, 0.64, chord), "membrane"))
    return out


def folded_fingers():
    """Absolute finger yaws at rest, LEFT wing.

    Every finger closes towards the MEAN of the open fan, so folding the wing
    does not swing the whole bundle sideways; the membrane panels follow the
    average of the two fingers they hang between.
    """
    mean = sum(f[0] for f in FINGERS) / len(FINGERS)
    out = {}
    for i, (ang, _s1, _s2, _w, _c) in enumerate(FINGERS, start=1):
        out[f"f{i}a"] = mean + (ang - mean) * FOLD_FAN
    for i in range(1, len(FINGERS)):
        out[f"m{i}"] = (out[f"f{i}a"] + out[f"f{i + 1}a"]) * 0.5
    out["m5"] = out["f1a"] + 0.55 * FOLD_FAN
    out["m6"] = out["f5a"] - 0.52 * FOLD_FAN
    return out


# Backwards-compatible alias (older tools referred to folded_yaws()).
folded_yaws = folded_fingers


def open_fingers():
    """Absolute finger yaws of the open fan, LEFT wing."""
    out = {}
    for i, (ang, _s1, _s2, _w, _c) in enumerate(FINGERS, start=1):
        out[f"f{i}a"] = ang
    for i in range(1, len(FINGERS)):
        out[f"m{i}"] = (FINGERS[i - 1][0] + FINGERS[i][0]) * 0.5
    out["m5"] = FINGERS[0][0] + 0.34
    out["m6"] = FINGERS[-1][0] - 0.30
    return out


def _wings():
    """Left wing followed by its mirrored twin."""
    return mirrored_pairs(_wing_side("l"))


# ---------------------------------------------------------------------- tail
# Seven segments. Each box is drawn longer than its bone and the next segment
# starts before the end of the previous one, so the chain overlaps and can never
# break into floating blocks at three-quarter views.
_TAIL = [
    (9.0, 10.4),
    (8.4, 9.0),
    (7.8, 7.6),
    (7.2, 6.2),
    (6.6, 4.8),
    (5.8, 3.4),
    (5.0, 2.4),
]
_TAIL_X = (-0.16, -0.05, 0.00, 0.05, 0.09, 0.12, 0.13)


def _tail_parts():
    out = []
    parent = "body"
    for i, ((length, w), xr) in enumerate(zip(_TAIL, _TAIL_X), start=1):
        pivot = (0.0, -2.4, 15.4) if i == 1 else (0.0, 0.0, _TAIL[i - 2][0] - 1.4)
        out.append(P(f"tail{i}", parent, (*pivot, xr, 0.0, 0.0),
                     (-w / 2, -w / 2, -1.6, w, w, length + 1.6), "hide", anim=True))
        if i <= 6:
            out.append(P(f"tail{i}_spike", f"tail{i}", (0.0, w / 2 + 0.1, 2.2, 0.25, 0.0, 0.0),
                         (-0.7, -0.3, -3.0, 1.4, 4.0, 6.6), "blade"))
        parent = f"tail{i}"
    # spade tip: a cross shaped blade, the classic dragon tail fin
    out.append(P("spade_top", "tail7", (0.0, -0.2, 5.6, -0.32, 0.0, 0.0),
                 (-0.9, -0.2, 0.0, 1.8, 4.2, 7.6), "blade"))
    out.append(P("spade_bot", "tail7", (0.0, 0.2, 5.8, 0.06, 0.0, 0.0),
                 (-0.9, 0.0, 0.0, 1.8, 3.2, 6.6), "blade"))
    out += PM("spade_left", "tail7", (0.85, 0.0, 5.2, 0.0, 0.0, -0.72),
              (-0.34, -0.6, 0.0, 0.68, 1.20, 5.2), "blade")
    return out


# ================================================================= body + head
def _head_parts():
    """Skull, muzzle, jaw, horns, eyes, teeth and cheek spines.

    Built like a crocodilian skull, from the back forwards:
      cranium  -- a deep, narrow box that carries the horns and the jaw hinge
      brow     -- a ridge over each eye, overhanging it
      muzzle   -- THREE tapering boxes that step down and narrow towards the nose
      jaw      -- a thin lower jaw that closes against the muzzle with one line
    The head origin sits at the end of the neck, so the whole skull hangs
    forward of the pivot and the neck animation swings it naturally.
    """
    out = [
        # cranium: narrow (9.6) and deep (9.0), long enough to root the horns
        P("head", "neck3", (0.0, 0.4, -4.2, HEAD_ANGLE, 0.0, 0.0),
          (-4.9, -5.4, -5.8, 9.8, 11.0, 12.0), "skull", anim=True),
        # occipital shelf: the flat plate the horns grow out of
        P("head_plate", "head", (0.0, 4.0, 3.2, 0.40, 0.0, 0.0),
          (-4.4, -1.2, -1.6, 8.8, 3.2, 5.4), "skull"),
        # ---- muzzle: three steps, each narrower and lower than the last, so the
        # profile tapers to the nose instead of ending in a blunt brick
        P("muzzle1", "head", (0.0, -1.8, -5.6, 0.02, 0.0, 0.0),
          (-3.9, -2.8, -6.4, 7.8, 5.4, 7.0), "muzzle"),
        P("muzzle2", "muzzle1", (0.0, -0.2, -5.8, -0.04, 0.0, 0.0),
          (-3.3, -2.3, -6.6, 6.6, 4.2, 7.0), "muzzle"),
        P("muzzle3", "muzzle2", (0.0, -0.1, -6.0, -0.05, 0.0, 0.0),
          (-2.8, -2.1, -5.4, 5.6, 3.8, 5.6), "muzzle"),
        # horny nose cap and nostrils
        P("nose", "muzzle3", (0.0, 0.5, -5.0, 0.10, 0.0, 0.0),
          (-2.2, -1.2, -1.2, 4.4, 2.4, 1.6), "skull"),
    ]
    # ---- lower jaw: thin, hinged under the cranium, closing with the muzzle
    out += [
        P("jaw", "head", (0.0, -4.0, -0.6, 0.08, 0.0, 0.0),
          (-3.3, -2.0, -17.4, 6.6, 2.3, 17.4), "jaw", anim=True),
        P("jaw_chin", "jaw", (0.0, 0.1, -16.4, 0.10, 0.0, 0.0),
          (-2.0, -1.2, -2.0, 4.0, 1.4, 2.2), "jaw"),
        # jaw hinge blocks so the jaw is not a floating slab
        P("jaw_hinge_l", "head", (4.0, -2.6, -1.2, 0.0, 0.0, 0.0),
          (-1.2, -2.0, -2.6, 1.4, 4.0, 5.6), "skull"),
    ]
    # ---- brow ridge over the eye, overhanging it, sweeping back
    out += PM("brow_l", "head", (4.35, 1.6, -3.2, 0.0, -0.08, -0.22),
              (-1.1, -0.7, -4.4, 2.2, 1.4, 6.6), "skull")
    out += PM("eye_l", "head", (4.9, 0.35, -3.4, 0.0, 0.0, 0.0),
              (-0.20, -0.85, -2.0, 0.64, 1.7, 4.0), "eye")
    # ---- cheek spines pointing back from the jaw hinge
    out += PM("cheek_l", "head", (4.7, -1.6, 4.6, 0.0, 0.0, -0.35),
              (-0.45, -0.45, 0.0, 0.9, 0.9, 4.6), "blade")
    out += PM("cheek_l2", "head", (4.2, -2.9, 5.2, 0.0, 0.0, -0.70),
              (-0.40, -0.40, 0.0, 0.8, 0.8, 3.8), "blade")
    out += PM("cheek_plate_l", "head", (4.5, -1.2, -1.0, 0.0, 0.0, 0.0),
              (-1.1, -2.2, -2.0, 1.6, 4.0, 6.4), "skull")
    # ---- nostrils on the nose cap
    out += PM("nostril_l", "nose", (1.5, 1.2, -0.4, 0.0, 0.0, 0.0),
              (-0.25, -0.4, -0.5, 0.5, 0.8, 1.2), "small")
    # ---- teeth: two rows, needle thin, only where the jaw closes
    out += PM("teeth_l", "muzzle2", (2.9, -2.2, -5.4, 0.0, 0.0, 0.0),
              (-0.30, -1.8, -0.9, 0.60, 1.8, 7.6), "tooth")
    out += PM("jaw_teeth_l", "jaw", (2.6, 2.0, -6.0, 0.0, 0.0, 0.0),
              (-0.30, -1.5, -0.8, 0.60, 1.5, 8.4), "tooth")
    # ---- crown horns: rooted at the back of the cranium, thick, swept back
    out += PM("horn_l1", "head", (2.5, 4.4, 1.4, -0.16, 0.22, 0.0),
              (-1.35, -1.35, -3.2, 2.7, 2.7, 9.6), "horn")
    out += PM("horn_l2", "horn_l1", (0.0, 0.4, 9.0, -0.34, 0.0, 0.0),
              (-0.90, -0.90, 0.0, 1.8, 1.8, 7.6), "horn")
    # secondary pair, lower on the temple, shorter and more lateral
    out += PM("horn_l3", "head", (4.1, 1.8, 2.6, -0.18, 0.62, 0.0),
              (-0.55, -0.55, 0.0, 1.1, 1.1, 4.6), "horn")
    return out


def _body_parts():
    out = [
        # a pure pivot at the shoulder: every animation can breathe/bank from here
        P("body", "root", (0.0, BODY_PIVOT_Y, 0.0, 0.0, 0.0, 0.0), None, "pivot", anim=True),

        # Deep barrel: the chest is the widest and deepest station, the waist
        # pinches, the hips flare again. Mass lives here, not in the limbs.
        P("chest", "body", (0.0, 2.2, -9.0, 0.06, 0.0, 0.0), (-9.0, -10.6, -7.0, 18.0, 21.2, 14.0), "hide"),
        P("torso", "body", (0.0, 0.6, 1.0, 0.0, 0.0, 0.0), (-7.8, -10.0, -9.4, 15.6, 20.0, 19.6), "hide"),
        P("waist", "body", (0.0, -0.8, 11.0, -0.05, 0.0, 0.0), (-6.0, -7.8, -5.0, 12.0, 15.6, 9.6), "hide"),
        P("hips", "body", (0.0, -0.6, 16.4, -0.08, 0.0, 0.0), (-8.0, -8.6, -2.2, 16.0, 17.2, 12.2), "hide"),

        # ventral plating
        P("belly_chest", "body", (0.0, 2.2, -9.0, 0.06, 0.0, 0.0), (-6.0, -12.0, -6.6, 12.0, 2.2, 13.0), "belly"),
        P("belly_torso", "body", (0.0, 0.6, 1.0, 0.0, 0.0, 0.0), (-5.4, -11.4, -9.2, 10.8, 2.2, 19.0), "belly"),
        P("belly_waist", "body", (0.0, -0.8, 11.0, -0.05, 0.0, 0.0), (-4.2, -9.2, -4.8, 8.4, 2.0, 9.2), "belly"),
        P("belly_hips", "body", (0.0, -0.6, 16.4, -0.08, 0.0, 0.0), (-5.6, -10.0, -2.0, 11.2, 2.0, 11.8), "belly"),
    ]

    # dorsal spine ridge: separate blades so the silhouette is jagged
    ridge = [(-12.4, 3.2, 0.34), (-9.0, 4.0, 0.30), (-5.4, 4.8, 0.26),
             (-1.6, 5.4, 0.20), (2.6, 5.0, 0.16), (6.8, 4.4, 0.14),
             (11.0, 3.6, 0.10), (15.0, 3.0, 0.06)]
    for i, (z, height, tilt) in enumerate(ridge):
        # tall thin fins, leaning back, drawn as narrow slivers
        out.append(P(f"ridge_{i}", "body",
                     (0.0, 7.6 + (-0.3 if z > 9 else 0.0), z, tilt, 0.0, 0.0),
                     (-0.55, 0.0, -3.0, 1.1, height, 1.8), "blade"))
    out += PM("ridge_side_l", "chest", (6.4, 6.0, -1.0, 0.0, 0.0, -0.75),
              (-0.7, 0.0, -2.0, 1.4, 3.4, 2.0), "blade")

    # shoulder blades and hip spurs, swept back
    out += PM("scapula_l", "chest", (7.8, 5.4, -0.8, 0.0, 0.0, -0.50),
              (-1.15, 0.0, -2.6, 2.3, 5.8, 2.6), "blade")
    out += PM("hip_spur_l", "hips", (8.0, 4.4, 2.6, 0.0, 0.0, -0.95),
              (-0.85, 0.0, -2.4, 1.7, 5.4, 2.6), "blade")

    # ---------------------------------------------------------------- front legs
    # digitigrade: thigh forward, shin back, foot forward, three gripping talons
    out += PM("leg_fl_thigh", "body", (7.0, -5.8, -8.8, 0.18, 0.0, 0.0),
              (-3.6, -12.0, -4.6, 7.2, 12.0, 9.2), "hide", anim=True)
    out += PM("leg_fl_haunch", "leg_fl_thigh", (0.6, -1.8, -0.6, 0.0, 0.0, 0.0),
              (-4.0, -4.4, -5.4, 8.0, 8.8, 10.8), "hide")
    out += PM("leg_fl_shin", "leg_fl_thigh", (0.0, -11.6, 0.4, -0.30, 0.0, 0.0),
              (-2.8, -9.8, -3.1, 5.6, 9.8, 6.2), "hide", anim=True)
    out += PM("leg_fl_calf", "leg_fl_shin", (0.0, -3.2, -0.8, 0.0, 0.0, 0.0),
              (-3.1, -3.6, -3.7, 6.2, 7.2, 7.4), "hide")
    out += PM("leg_fl_foot", "leg_fl_shin", (0.0, -9.4, -0.2, 0.22, 0.0, 0.0),
              (-3.0, -1.8, -4.6, 6.0, 3.6, 7.4), "foot", anim=True)
    for k, dx in enumerate((1.9, 0.0, -1.9), start=1):
        out += PM(f"leg_fl_talon{k}", "leg_fl_foot", (dx, -1.4, -3.8, 0.55, 0.0, 0.0),
                  (-0.50, -1.0, -1.8, 1.0, 1.1, 2.4), "claw")
    out += PM("leg_fl_heel", "leg_fl_foot", (0.0, -0.9, 3.2, -0.55, 0.0, 0.0),
              (-0.5, -0.5, 0.0, 1.0, 1.0, 2.6), "claw")

    # ---------------------------------------------------------------- hind legs
    # powerful haunches, longer than the arms, back-swept hock
    out += PM("leg_bl_thigh", "body", (7.4, -5.0, 10.2, -0.34, 0.0, 0.0),
              (-4.9, -12.8, -5.8, 9.8, 12.8, 11.6), "hide", anim=True)
    out += PM("leg_bl_haunch", "leg_bl_thigh", (0.6, -2.0, -0.6, 0.0, 0.0, 0.0),
              (-5.4, -4.8, -6.6, 10.8, 9.6, 13.0), "hide")
    out += PM("leg_bl_shin", "leg_bl_thigh", (0.0, -12.4, 0.0, 0.66, 0.0, 0.0),
              (-3.1, -10.6, -3.4, 6.2, 10.6, 6.8), "hide", anim=True)
    out += PM("leg_bl_calf", "leg_bl_shin", (0.0, -3.4, -0.8, 0.0, 0.0, 0.0),
              (-3.4, -3.8, -4.0, 6.8, 7.6, 8.0), "hide")
    out += PM("leg_bl_foot", "leg_bl_shin", (0.0, -10.0, -0.4, -0.32, 0.0, 0.0),
              (-3.8, -2.0, -5.6, 7.6, 3.8, 8.8), "foot", anim=True)
    for k, dx in enumerate((2.0, 0.0, -2.0), start=1):
        out += PM(f"leg_bl_talon{k}", "leg_bl_foot", (dx, -1.5, -4.4, 0.55, 0.0, 0.0),
                  (-0.54, -1.1, -2.0, 1.08, 1.2, 2.6), "claw")
    out += PM("leg_bl_heel", "leg_bl_foot", (0.0, -1.0, 3.8, -0.60, 0.0, 0.0),
              (-0.55, -0.55, 0.0, 1.1, 1.1, 3.0), "claw")

    # ---------------------------------------------------------------- gear
    out += [
        P("saddle", "body", (0.0, 7.2, 1.2, 0.0, 0.0, 0.0), (-5.8, -1.0, -6.6, 11.6, 2.0, 13.2), "saddle"),
        P("saddle_pommel", "saddle", (0.0, 1.0, -5.8, 0.0, 0.0, 0.0), (-2.0, -2.8, -2.0, 4.0, 3.0, 4.0), "saddle"),
        P("saddle_cantle", "saddle", (0.0, 1.0, 5.8, 0.0, 0.0, 0.0), (-2.3, -2.4, -1.5, 4.6, 2.6, 3.0), "saddle"),

        P("armor_chest", "body", (0.0, 1.4, -9.4, 0.08, 0.0, 0.0), (-9.4, -9.8, -6.6, 18.8, 17.0, 13.2), "armor"),
        P("armor_torso", "body", (0.0, 0.0, 0.6, 0.0, 0.0, 0.0), (-8.2, -9.2, -9.4, 16.4, 16.2, 18.8), "armor"),
        P("armor_hips", "body", (0.0, -0.8, 15.8, -0.08, 0.0, 0.0), (-8.4, -8.0, -2.0, 16.8, 14.6, 11.8), "armor"),
        P("armor_collar", "body", (0.0, 4.6, -8.0, 0.45, 0.0, 0.0), (-7.4, -1.8, -3.6, 14.8, 3.4, 7.0), "armor"),
    ]
    out += PM("saddle_strap_l", "body", (6.8, 0.0, 1.2, 0.0, 0.0, 0.0),
              (-0.64, -9.4, -5.4, 1.28, 11.6, 11.0), "saddle")
    return out


def _all_parts():
    # body first (the pivot every other part hangs from), then the neck and the
    # skull, then everything else, then the tail chain, then the wings
    body = _body_parts()
    return body[:5] + _neck_parts() + _head_parts() + body[5:] + _tail_parts() + _wings()


PARTS = _all_parts()

# groups hidden unless the dragon is saddled / armoured
GEAR = {
    "saddle": ["saddle", "saddle_pommel", "saddle_cantle", "saddle_strap_l", "saddle_strap_r"],
    "armor": ["armor_chest", "armor_torso", "armor_hips", "armor_collar"],
}

TEXTURE_SIZE = 1024
# Texels per model unit. Minecraft sizes a cube's UV island straight from the box
# dimensions (2*(w+d) by (d+h) texels, whatever the atlas size), so this MUST stay
# at 1: painter and sampler then agree and every painted texel lands on the model.
TEXELS_PER_UNIT = 1
