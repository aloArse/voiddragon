#!/usr/bin/env python3
"""Single source of truth for the Void Dragon model geometry.

Every part is one cube (extra cubes are child parts with a zero rotation and an
offset). The same data drives:

  * tools/gen_model.py  -> src/main/java/com/voiddragon/client/DragonRig.java
  * tools/gen_textures.py -> the 256x256 entity atlas (box UV islands)
  * tools/render_preview.py -> offline preview render

Coordinates are model units (1 unit = 1/16 block): +X left, +Y up, -Z forward.
Poses are (x, y, z, rotX, rotY, rotZ) exactly like PartPose.
"""

# painter keys drive the texture generator
#   body       scaled hide
#   belly      ventral plates
#   ridge      spine blade
#   fin        sail / spade membrane
#   membrane   wing membrane
#   horn       horn / claw / tooth
#   skull      head plates (eyes are painted on the front face)
#   jaw        lower jaw + palate
#   limb       legs
#   foot       feet with claws
#   saddle / armor : gear islands


def P(name, parent, pose, box, painter, mirror=False, anim=False):
    """One cube. pose = (x, y, z, rx, ry, rz); box = (x, y, z, w, h, d)."""
    return dict(name=name, parent=parent, pose=pose, box=box, painter=painter,
                mirror=mirror, anim=anim)


PARTS = [
    # ------------------------------------------------ torso stack
    P("body", "root", (0.0, 20.0, 0.0, 0.0, 0.0, 0.0), None, "pivot", anim=True),
    P("torso", "body", (0.0, 0.0, 1.0, 0.0, 0.0, 0.0), (-8.8, -8.5, -14.0, 17.6, 15.0, 27.0), "body"),
    P("chest", "body", (0.0, 1.0, -12.0, 0.0, 0.0, 0.0), (-10.2, -9.2, -8.0, 20.4, 16.4, 10.0), "body"),
    P("hips", "body", (0.0, 0.0, 12.0, 0.0, 0.0, 0.0), (-8.4, -7.8, -1.0, 16.8, 13.6, 10.0), "body"),
    P("belly", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-6.6, -9.6, -12.0, 13.2, 2.0, 25.0), "belly"),
    P("ridge", "body", (0.0, 0.0, 0.0, 0.0, 0.0, 0.0), (-1.0, -11.2, -13.0, 2.0, 2.8, 26.0), "ridge"),

    # dorsal fins along the spine (blades, not rods)
    P("fin0", "body", (0.0, -9.0, -9.0, 0.0, 0.0, 0.0), (-0.8, -5.0, -3.0, 1.6, 5.5, 6.0), "fin"),
    P("fin1", "body", (0.0, -9.6, -3.0, 0.0, 0.0, 0.0), (-0.8, -6.0, -3.0, 1.6, 6.5, 7.0), "fin"),
    P("fin2", "body", (0.0, -9.6, 3.0, 0.0, 0.0, 0.0), (-0.8, -6.5, -3.0, 1.6, 7.0, 7.0), "fin"),
    P("fin3", "body", (0.0, -9.2, 9.0, 0.0, 0.0, 0.0), (-0.8, -5.5, -3.0, 1.6, 6.0, 7.0), "fin"),
    P("fin4", "body", (0.0, -8.4, 15.0, 0.0, 0.0, 0.0), (-0.7, -4.5, -2.5, 1.4, 5.0, 6.0), "fin"),

    # ------------------------------------------------ neck (chained, tapering)
    P("neck1", "body", (0.0, -3.0, -14.0, 0.52, 0.0, 0.0), (-5.4, -5.4, -6.5, 10.8, 10.8, 6.5), "body", anim=True),
    P("neck1_fin", "neck1", (0.0, -4.4, -3.0, 0.0, 0.0, 0.0), (-0.7, -3.4, -3.0, 1.4, 3.6, 6.0), "fin"),
    P("neck2", "neck1", (0.0, 0.0, -6.5, -0.12, 0.0, 0.0), (-5.0, -5.0, -6.0, 10.0, 10.0, 6.0), "body", anim=True),
    P("neck2_fin", "neck2", (0.0, -3.9, -3.0, 0.0, 0.0, 0.0), (-0.7, -3.2, -3.0, 1.4, 3.4, 6.0), "fin"),
    P("neck3", "neck2", (0.0, 0.0, -6.0, -0.20, 0.0, 0.0), (-4.6, -4.6, -5.5, 9.2, 9.2, 5.5), "body", anim=True),
    P("neck3_fin", "neck3", (0.0, -3.4, -3.0, 0.0, 0.0, 0.0), (-0.6, -3.0, -3.0, 1.2, 3.0, 6.0), "fin"),
    P("neck4", "neck3", (0.0, 0.0, -5.5, -0.16, 0.0, 0.0), (-4.2, -4.2, -5.0, 8.4, 8.4, 5.0), "body", anim=True),
    P("neck5", "neck4", (0.0, 0.0, -5.5, -0.14, 0.0, 0.0), (-4.0, -4.0, -4.5, 8.0, 8.0, 4.5), "body", anim=True),

    # ------------------------------------------------ head
    P("head", "neck4", (0.0, 0.5, -4.5, 0.04, 0.0, 0.0), (-6.6, -5.8, -11.0, 13.2, 11.6, 11.0), "skull", anim=True),
    P("snout", "head", (0.0, 1.0, -10.0, 0.0, 0.0, 0.0), (-4.2, -3.4, -7.0, 8.4, 6.4, 7.0), "skull"),
    P("jaw", "head", (0.0, 3.6, -1.0, 0.04, 0.0, 0.0), (-4.2, 0.0, -11.0, 8.4, 2.8, 11.0), "jaw", anim=True),
    P("horn_l_base", "head", (4.9, -4.6, -1.0, 0.5, -0.28, 0.0), (-1.5, -1.5, 0.0, 3.0, 3.0, 10.0), "horn"),
    P("horn_l_tip", "horn_l_base", (0.0, 0.0, 10.0, 0.45, -0.15, 0.0), (-1.0, -1.0, 0.0, 2.0, 2.0, 10.0), "horn"),
    P("horn_r_base", "head", (-4.9, -4.6, -1.0, 0.5, 0.28, 0.0), (-1.5, -1.5, 0.0, 3.0, 3.0, 10.0), "horn", mirror=True),
    P("horn_r_tip", "horn_r_base", (0.0, 0.0, 10.0, 0.45, 0.15, 0.0), (-1.0, -1.0, 0.0, 2.0, 2.0, 10.0), "horn", mirror=True),
    P("brow_l", "head", (6.4, 1.8, -3.0, 0.0, -1.0, 0.0), (-1.0, -1.0, 0.0, 2.0, 2.0, 7.0), "horn"),
    P("brow_r", "head", (-6.4, 1.8, -3.0, 0.0, 1.0, 0.0), (-1.0, -1.0, 0.0, 2.0, 2.0, 7.0), "horn", mirror=True),
    P("cheek_l", "head", (5.9, 2.0, 4.2, 0.0, -1.75, 0.0), (-1.0, -1.0, 0.0, 2.0, 2.0, 6.0), "horn"),
    P("cheek_r", "head", (-5.9, 2.0, 4.2, 0.0, 1.75, 0.0), (-1.0, -1.0, 0.0, 2.0, 2.0, 6.0), "horn", mirror=True),

    # ------------------------------------------------ tail (chained, tapering)
    P("tail1", "body", (0.0, -1.0, 14.0, -0.04, 0.0, 0.0), (-5.0, -4.5, 0.0, 10.0, 9.0, 10.0), "body", anim=True),
    P("tail2", "tail1", (0.0, 0.0, 10.0, -0.06, 0.0, 0.0), (-4.2, -3.8, 0.0, 8.4, 7.6, 10.0), "body", anim=True),
    P("tail3", "tail2", (0.0, 0.0, 10.0, -0.05, 0.0, 0.0), (-3.4, -3.2, 0.0, 6.8, 6.4, 10.0), "body", anim=True),
    P("tail4", "tail3", (0.0, 0.0, 10.0, -0.03, 0.0, 0.0), (-2.6, -2.6, 0.0, 5.2, 5.2, 10.0), "body", anim=True),
    P("tail5", "tail4", (0.0, 0.0, 10.0, 0.02, 0.0, 0.0), (-1.9, -2.0, 0.0, 3.8, 4.0, 9.0), "body", anim=True),
    P("tail6", "tail5", (0.0, 0.0, 9.0, 0.05, 0.0, 0.0), (-1.3, -1.4, 0.0, 2.6, 2.8, 8.0), "body", anim=True),
    P("tail_fin_top", "tail6", (0.0, -1.4, 7.5, -0.22, 0.0, 0.0), (-0.5, -3.4, 0.0, 1.0, 3.4, 6.0), "fin"),
    P("tail_fin_bot", "tail6", (0.0, 0.6, 7.5, 0.15, 0.0, 0.0), (-0.5, 0.0, 0.8, 1.0, 2.8, 4.5), "fin"),

    # ------------------------------------------------ wings
    P("wing_l", "body", (8.5, -6.0, -6.0, 0.0, 0.0, 0.0), (0.0, -1.8, -2.0, 26.0, 3.6, 4.5), "body", anim=True),
    P("wing_l_membrane", "wing_l", (0.0, 0.6, 2.25, 0.0, 0.0, 0.0), (0.0, -0.5, 0.0, 26.0, 1.0, 20.0), "membrane"),
    P("wing_l_finger", "wing_l", (25.0, 0.0, 1.0, 0.0, 0.35, 0.0), (0.0, -0.7, -1.1, 22.0, 1.4, 2.2), "body"),
    P("wing_l_membrane2", "wing_l_finger", (1.0, 0.0, 1.0, 0.0, 0.0, 0.0), (-1.0, -0.4, 0.0, 15.0, 0.8, 12.0), "membrane"),
    P("wing_l_fore", "wing_l", (26.0, 0.0, 0.0, 0.0, -0.45, 0.0), (0.0, -1.2, -1.8, 24.0, 2.4, 3.6), "body", anim=True),
    P("wing_l_membrane3", "wing_l_fore", (0.0, 0.6, 1.8, 0.0, 0.0, 0.0), (0.0, -0.5, 0.0, 24.0, 1.0, 19.0), "membrane"),
    P("wing_l_claw", "wing_l_fore", (24.0, 0.0, 0.0, -0.35, 0.0, 0.0), (-0.9, -0.9, -0.9, 1.8, 1.8, 6.0), "horn"),

    P("wing_r", "body", (-8.5, -6.0, -6.0, 0.0, 0.0, 0.0), (-26.0, -1.8, -2.0, 26.0, 3.6, 4.5), "body", anim=True, mirror=True),
    P("wing_r_membrane", "wing_r", (0.0, 0.6, 2.25, 0.0, 0.0, 0.0), (-26.0, -0.5, 0.0, 26.0, 1.0, 20.0), "membrane", mirror=True),
    P("wing_r_finger", "wing_r", (-25.0, 0.0, 1.0, 0.0, -0.35, 0.0), (-22.0, -0.7, -1.1, 22.0, 1.4, 2.2), "body", mirror=True),
    P("wing_r_membrane2", "wing_r_finger", (-1.5, 0.0, 1.1, 0.0, 0.0, 0.0), (-22.0, -0.5, 0.0, 22.0, 1.0, 17.0), "membrane", mirror=True),
    P("wing_r_fore", "wing_r", (-26.0, 0.0, 0.0, 0.0, 0.45, 0.0), (-24.0, -1.2, -1.8, 24.0, 2.4, 3.6), "body", anim=True, mirror=True),
    P("wing_r_membrane3", "wing_r_fore", (0.0, 0.6, 1.8, 0.0, 0.0, 0.0), (-24.0, -0.5, 0.0, 24.0, 1.0, 19.0), "membrane", mirror=True),
    P("wing_r_claw", "wing_r_fore", (-24.0, 0.0, 0.0, -0.35, 0.0, 0.0), (-0.9, -0.9, -0.9, 1.8, 1.8, 6.0), "horn", mirror=True),

    # ------------------------------------------------ front legs
    P("leg_fl_thigh", "body", (6.8, -6.5, -9.5, 0.28, 0.0, 0.0), (-3.6, -9.0, -4.0, 7.2, 10.0, 8.0), "limb", anim=True),
    P("leg_fl_shin", "leg_fl_thigh", (0.0, -9.0, 0.0, -0.52, 0.0, 0.0), (-3.0, -9.0, -3.2, 6.0, 9.0, 6.4), "limb", anim=True),
    P("leg_fl_foot", "leg_fl_shin", (0.0, -9.0, 0.0, 0.24, 0.0, 0.0), (-3.0, -1.2, -5.0, 6.0, 2.2, 7.0), "foot", anim=True),
    P("leg_fr_thigh", "body", (-7.0, -6.0, -9.0, 0.0, 0.0, 0.0), (-3.0, -9.0, -3.5, 6.0, 10.0, 7.0), "limb", anim=True, mirror=True),
    P("leg_fr_shin", "leg_fr_thigh", (0.0, -9.0, 0.0, 0.0, 0.0, 0.0), (-2.6, -9.0, -2.8, 5.2, 9.0, 5.6), "limb", anim=True, mirror=True),
    P("leg_fr_foot", "leg_fr_shin", (0.0, -9.0, 0.0, 0.24, 0.0, 0.0), (-3.0, -1.2, -5.0, 6.0, 2.2, 7.0), "foot", anim=True, mirror=True),

    # ------------------------------------------------ hind legs
    P("leg_bl_thigh", "body", (7.4, -6.5, 9.5, -0.34, 0.0, 0.0), (-4.2, -11.0, -5.0, 8.4, 12.0, 10.0), "limb", anim=True),
    P("leg_bl_shin", "leg_bl_thigh", (0.0, -11.0, 0.0, 0.72, 0.0, 0.0), (-3.4, -9.0, -3.6, 6.8, 9.0, 7.2), "limb", anim=True),
    P("leg_bl_foot", "leg_bl_shin", (0.0, -9.0, 0.0, -0.38, 0.0, 0.0), (-3.4, -1.4, -5.6, 6.8, 2.4, 8.0), "foot", anim=True),
    P("leg_br_thigh", "body", (-8.0, -6.0, 9.0, 0.0, 0.0, 0.0), (-3.6, -11.0, -4.5, 7.2, 12.0, 9.0), "limb", anim=True, mirror=True),
    P("leg_br_shin", "leg_br_thigh", (0.0, -11.0, 0.0, 0.72, 0.0, 0.0), (-3.4, -9.0, -3.6, 6.8, 9.0, 7.2), "limb", anim=True, mirror=True),
    P("leg_br_foot", "leg_br_shin", (0.0, -9.0, 0.0, -0.38, 0.0, 0.0), (-3.4, -1.4, -5.6, 6.8, 2.4, 8.0), "foot", anim=True, mirror=True),

    # ------------------------------------------------ gear
    P("saddle", "body", (0.0, 7.6, 1.0, 0.0, 0.0, 0.0), (-6.5, -1.2, -7.0, 13.0, 2.4, 14.0), "saddle"),
    P("saddle_pommel", "saddle", (0.0, 0.6, -6.5, 0.0, 0.0, 0.0), (-2.2, -3.0, -2.2, 4.4, 3.2, 4.4), "saddle"),
    P("saddle_cantle", "saddle", (0.0, 0.6, 6.5, 0.0, 0.0, 0.0), (-2.8, -2.4, -1.6, 5.6, 2.6, 3.2), "saddle"),
    P("armor", "body", (0.0, 0.0, 1.0, 0.0, 0.0, 0.0), (-9.4, -8.4, -14.4, 18.8, 15.8, 26.8), "armor"),
    P("armor_chest", "body", (0.0, 1.0, -12.0, 0.0, 0.0, 0.0), (-10.4, -8.9, -8.4, 20.8, 15.8, 9.4), "armor"),
]

# groups that are hidden unless the dragon is saddled / armoured
GEAR = {
    "saddle": ["saddle", "saddle_pommel", "saddle_cantle"],
    "armor": ["armor", "armor_chest"],
}

TEXTURE_SIZE = 512
