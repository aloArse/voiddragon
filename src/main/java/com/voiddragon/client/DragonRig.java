package com.voiddragon.client;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * GENERATED FILE - do not edit by hand.
 * Geometry lives in tools/dragon_spec.py; run tools/gen_model.py to regenerate.
 * Texture UVs are packed by the same script into the 1024x1024 atlas
 * (tools/atlas_uv.json) and painted by tools/gen_textures.py.
 *
 * The whole rig hangs from one extra part, {@code rig}. Minecraft renders entity
 * models through scale(-1, -1, 1), so model space has +Y pointing DOWN and the
 * model origin sits 1.5 blocks above the entity's feet. tools/dragon_spec.py is
 * authored the readable way (+Y up, feet on y = 0), so {@code rig} applies the
 * 180 degree roll that cancels Minecraft's flip and the 24.016 unit drop that
 * puts the feet back on the ground. Net effect: the dragon renders exactly as
 * authored, right way up and standing on the block it occupies.
 */
public final class DragonRig {
    /** Cancels Minecraft's model-space flip: 180 degrees about Z. */
    public static final String RIG = "rig";
    /** 1.5 blocks in model units: where the flipped rig has to sit to be grounded. */
    public static final float RIG_DROP = 24.016F;

    private DragonRig() {
    }

    public static MeshDefinition createMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition rig = root.addOrReplaceChild(RIG, CubeListBuilder.create(),
                PartPose.offsetAndRotation(0.0F, RIG_DROP, 0.0F, 0.0F, 0.0F, (float) Math.PI));
        PartDefinition body = rig.addOrReplaceChild("body",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 28.8F, 0.0F));
        PartDefinition chest = body.addOrReplaceChild("chest",
                CubeListBuilder.create().texOffs(848, 0)
                        .addBox(-9.0F, -10.6F, -7.0F, 18.0F, 21.2F, 14.0F),
                PartPose.offsetAndRotation(0.0F, 2.2F, -9.0F, 0.06F, 0.00F, 0.00F));
        PartDefinition torso = body.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(562, 0)
                        .addBox(-7.8F, -10.0F, -9.4F, 15.6F, 20.0F, 19.6F),
                PartPose.offset(0.0F, 0.6F, 1.0F));
        PartDefinition waist = body.addOrReplaceChild("waist",
                CubeListBuilder.create().texOffs(660, 61)
                        .addBox(-6.0F, -7.8F, -5.0F, 12.0F, 15.6F, 9.6F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 11.0F, -0.05F, 0.00F, 0.00F));
        PartDefinition hips = body.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(193, 61)
                        .addBox(-8.0F, -8.6F, -2.2F, 16.0F, 17.2F, 12.2F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 16.4F, -0.08F, 0.00F, 0.00F));
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(750, 61)
                        .addBox(-6.5F, -6.8F, -8.4F, 13.0F, 13.6F, 8.8F),
                PartPose.offsetAndRotation(0.0F, 4.6F, -10.2F, 1.16F, 0.00F, 0.00F));
        PartDefinition neck1Throat = neck1.addOrReplaceChild("neck1_throat",
                CubeListBuilder.create().texOffs(639, 103)
                        .addBox(-4.4F, -0.9F, -3.5F, 8.8F, 1.8F, 7.1F),
                PartPose.offset(0.0F, -6.4F, -3.7F));
        PartDefinition neck1Spine = neck1.addOrReplaceChild("neck1_spine",
                CubeListBuilder.create().texOffs(410, 170)
                        .addBox(-0.6F, 0.0F, -2.2F, 1.1F, 4.2F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 6.0F, -3.7F, 0.60F, 0.00F, 0.00F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 103)
                        .addBox(-5.6F, -5.9F, -7.7F, 11.2F, 11.8F, 8.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.8F, -0.44F, 0.00F, 0.00F));
        PartDefinition neck2Throat = neck2.addOrReplaceChild("neck2_throat",
                CubeListBuilder.create().texOffs(904, 103)
                        .addBox(-3.8F, -0.9F, -3.2F, 7.6F, 1.8F, 6.5F),
                PartPose.offset(0.0F, -5.5F, -3.4F));
        PartDefinition neck2Spine = neck2.addOrReplaceChild("neck2_spine",
                CubeListBuilder.create().texOffs(417, 170)
                        .addBox(-0.6F, 0.0F, -2.2F, 1.1F, 4.2F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 5.1F, -3.4F, 0.60F, 0.00F, 0.00F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(336, 103)
                        .addBox(-4.8F, -5.1F, -6.9F, 9.6F, 10.2F, 7.3F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.2F, -0.34F, 0.00F, 0.00F));
        PartDefinition neck3Throat = neck3.addOrReplaceChild("neck3_throat",
                CubeListBuilder.create().texOffs(276, 135)
                        .addBox(-3.3F, -0.9F, -2.9F, 6.5F, 1.8F, 5.9F),
                PartPose.offset(0.0F, -4.7F, -3.1F));
        PartDefinition neck3Spine = neck3.addOrReplaceChild("neck3_spine",
                CubeListBuilder.create().texOffs(424, 170)
                        .addBox(-0.6F, 0.0F, -2.2F, 1.1F, 4.2F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 4.3F, -3.1F, 0.60F, 0.00F, 0.00F));
        PartDefinition head = neck3.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(705, 61)
                        .addBox(-4.9F, -5.4F, -5.8F, 9.8F, 11.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 0.4F, -4.2F, -0.10F, 0.00F, 0.00F));
        PartDefinition headPlate = head.addOrReplaceChild("head_plate",
                CubeListBuilder.create().texOffs(935, 103)
                        .addBox(-4.4F, -1.2F, -1.6F, 8.8F, 3.2F, 5.4F),
                PartPose.offsetAndRotation(0.0F, 4.0F, 3.2F, 0.40F, 0.00F, 0.00F));
        PartDefinition muzzle1 = head.addOrReplaceChild("muzzle1",
                CubeListBuilder.create().texOffs(765, 103)
                        .addBox(-3.9F, -2.8F, -6.4F, 7.8F, 5.4F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -1.8F, -5.6F, 0.02F, 0.00F, 0.00F));
        PartDefinition muzzle2 = muzzle1.addOrReplaceChild("muzzle2",
                CubeListBuilder.create().texOffs(964, 103)
                        .addBox(-3.3F, -2.3F, -6.6F, 6.6F, 4.2F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -0.2F, -5.8F, -0.04F, 0.00F, 0.00F));
        PartDefinition muzzle3 = muzzle2.addOrReplaceChild("muzzle3",
                CubeListBuilder.create().texOffs(389, 135)
                        .addBox(-2.8F, -2.1F, -5.4F, 5.6F, 3.8F, 5.6F),
                PartPose.offsetAndRotation(0.0F, -0.1F, -6.0F, -0.05F, 0.00F, 0.00F));
        PartDefinition nose = muzzle3.addOrReplaceChild("nose",
                CubeListBuilder.create().texOffs(279, 170)
                        .addBox(-2.2F, -1.2F, -1.2F, 4.4F, 2.4F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 0.5F, -5.0F, 0.10F, 0.00F, 0.00F));
        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(928, 61)
                        .addBox(-3.3F, -2.0F, -17.4F, 6.6F, 2.3F, 17.4F),
                PartPose.offsetAndRotation(0.0F, -4.0F, -0.6F, 0.08F, 0.00F, 0.00F));
        PartDefinition jawChin = jaw.addOrReplaceChild("jaw_chin",
                CubeListBuilder.create().texOffs(206, 170)
                        .addBox(-2.0F, -1.2F, -2.0F, 4.0F, 1.4F, 2.2F),
                PartPose.offsetAndRotation(0.0F, 0.1F, -16.4F, 0.10F, 0.00F, 0.00F));
        PartDefinition jawHingeL = head.addOrReplaceChild("jaw_hinge_l",
                CubeListBuilder.create().texOffs(169, 170)
                        .addBox(-1.2F, -2.0F, -2.6F, 1.4F, 4.0F, 5.6F),
                PartPose.offset(4.0F, -2.6F, -1.2F));
        PartDefinition browL = head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(36, 170)
                        .addBox(-1.1F, -0.7F, -4.4F, 2.2F, 1.4F, 6.6F),
                PartPose.offsetAndRotation(4.3F, 1.6F, -3.2F, 0.00F, -0.08F, -0.22F));
        PartDefinition browR = head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(55, 170).mirror()
                        .addBox(-1.1F, -0.7F, -4.4F, 2.2F, 1.4F, 6.6F),
                PartPose.offsetAndRotation(-4.3F, 1.6F, -3.2F, 0.00F, 0.08F, 0.22F));
        PartDefinition eyeL = head.addOrReplaceChild("eye_l",
                CubeListBuilder.create().texOffs(587, 170)
                        .addBox(-0.2F, -0.8F, -2.0F, 0.6F, 1.7F, 4.0F),
                PartPose.offset(4.9F, 0.3F, -3.4F));
        PartDefinition eyeR = head.addOrReplaceChild("eye_r",
                CubeListBuilder.create().texOffs(598, 170).mirror()
                        .addBox(-0.4F, -0.8F, -2.0F, 0.6F, 1.7F, 4.0F),
                PartPose.offset(-4.9F, 0.3F, -3.4F));
        PartDefinition cheekL = head.addOrReplaceChild("cheek_l",
                CubeListBuilder.create().texOffs(478, 170)
                        .addBox(-0.5F, -0.5F, 0.0F, 0.9F, 0.9F, 4.6F),
                PartPose.offsetAndRotation(4.7F, -1.6F, 4.6F, 0.00F, 0.00F, -0.35F));
        PartDefinition cheekR = head.addOrReplaceChild("cheek_r",
                CubeListBuilder.create().texOffs(491, 170).mirror()
                        .addBox(-0.5F, -0.5F, 0.0F, 0.9F, 0.9F, 4.6F),
                PartPose.offsetAndRotation(-4.7F, -1.6F, 4.6F, 0.00F, -0.00F, 0.35F));
        PartDefinition cheekL2 = head.addOrReplaceChild("cheek_l2",
                CubeListBuilder.create().texOffs(670, 170)
                        .addBox(-0.4F, -0.4F, 0.0F, 0.8F, 0.8F, 3.8F),
                PartPose.offsetAndRotation(4.2F, -2.9F, 5.2F, 0.00F, 0.00F, -0.70F));
        PartDefinition cheekR2 = head.addOrReplaceChild("cheek_r2",
                CubeListBuilder.create().texOffs(681, 170).mirror()
                        .addBox(-0.4F, -0.4F, 0.0F, 0.8F, 0.8F, 3.8F),
                PartPose.offsetAndRotation(-4.2F, -2.9F, 5.2F, 0.00F, -0.00F, 0.70F));
        PartDefinition cheekPlateL = head.addOrReplaceChild("cheek_plate_l",
                CubeListBuilder.create().texOffs(135, 170)
                        .addBox(-1.1F, -2.2F, -2.0F, 1.6F, 4.0F, 6.4F),
                PartPose.offset(4.5F, -1.2F, -1.0F));
        PartDefinition cheekPlateR = head.addOrReplaceChild("cheek_plate_r",
                CubeListBuilder.create().texOffs(152, 170).mirror()
                        .addBox(-0.5F, -2.2F, -2.0F, 1.6F, 4.0F, 6.4F),
                PartPose.offset(-4.5F, -1.2F, -1.0F));
        PartDefinition nostrilL = nose.addOrReplaceChild("nostril_l",
                CubeListBuilder.create().texOffs(773, 170)
                        .addBox(-0.2F, -0.4F, -0.5F, 0.5F, 0.8F, 1.2F),
                PartPose.offset(1.5F, 1.2F, -0.4F));
        PartDefinition nostrilR = nose.addOrReplaceChild("nostril_r",
                CubeListBuilder.create().texOffs(778, 170).mirror()
                        .addBox(-0.2F, -0.4F, -0.5F, 0.5F, 0.8F, 1.2F),
                PartPose.offset(-1.5F, 1.2F, -0.4F));
        PartDefinition teethL = muzzle2.addOrReplaceChild("teeth_l",
                CubeListBuilder.create().texOffs(358, 170)
                        .addBox(-0.3F, -1.8F, -0.9F, 0.6F, 1.8F, 7.6F),
                PartPose.offset(2.9F, -2.2F, -5.4F));
        PartDefinition teethR = muzzle2.addOrReplaceChild("teeth_r",
                CubeListBuilder.create().texOffs(377, 170).mirror()
                        .addBox(-0.3F, -1.8F, -0.9F, 0.6F, 1.8F, 7.6F),
                PartPose.offset(-2.9F, -2.2F, -5.4F));
        PartDefinition jawTeethL = jaw.addOrReplaceChild("jaw_teeth_l",
                CubeListBuilder.create().texOffs(313, 170)
                        .addBox(-0.3F, -1.5F, -0.8F, 0.6F, 1.5F, 8.4F),
                PartPose.offset(2.6F, 2.0F, -6.0F));
        PartDefinition jawTeethR = jaw.addOrReplaceChild("jaw_teeth_r",
                CubeListBuilder.create().texOffs(332, 170).mirror()
                        .addBox(-0.3F, -1.5F, -0.8F, 0.6F, 1.5F, 8.4F),
                PartPose.offset(-2.6F, 2.0F, -6.0F));
        PartDefinition hornL1 = head.addOrReplaceChild("horn_l1",
                CubeListBuilder.create().texOffs(628, 135)
                        .addBox(-1.4F, -1.4F, -3.2F, 2.7F, 2.7F, 9.6F),
                PartPose.offsetAndRotation(2.5F, 4.4F, 1.4F, -0.16F, 0.22F, 0.00F));
        PartDefinition hornR1 = head.addOrReplaceChild("horn_r1",
                CubeListBuilder.create().texOffs(655, 135).mirror()
                        .addBox(-1.4F, -1.4F, -3.2F, 2.7F, 2.7F, 9.6F),
                PartPose.offsetAndRotation(-2.5F, 4.4F, 1.4F, -0.16F, -0.22F, -0.00F));
        PartDefinition hornL2 = hornL1.addOrReplaceChild("horn_l2",
                CubeListBuilder.create().texOffs(74, 170)
                        .addBox(-0.9F, -0.9F, 0.0F, 1.8F, 1.8F, 7.6F),
                PartPose.offsetAndRotation(0.0F, 0.4F, 9.0F, -0.34F, 0.00F, 0.00F));
        PartDefinition hornR2 = hornR1.addOrReplaceChild("horn_r2",
                CubeListBuilder.create().texOffs(95, 170).mirror()
                        .addBox(-0.9F, -0.9F, 0.0F, 1.8F, 1.8F, 7.6F),
                PartPose.offsetAndRotation(-0.0F, 0.4F, 9.0F, -0.34F, -0.00F, -0.00F));
        PartDefinition hornL3 = head.addOrReplaceChild("horn_l3",
                CubeListBuilder.create().texOffs(431, 170)
                        .addBox(-0.6F, -0.6F, 0.0F, 1.1F, 1.1F, 4.6F),
                PartPose.offsetAndRotation(4.1F, 1.8F, 2.6F, -0.18F, 0.62F, 0.00F));
        PartDefinition hornR3 = head.addOrReplaceChild("horn_r3",
                CubeListBuilder.create().texOffs(444, 170).mirror()
                        .addBox(-0.6F, -0.6F, 0.0F, 1.1F, 1.1F, 4.6F),
                PartPose.offsetAndRotation(-4.1F, 1.8F, 2.6F, -0.18F, -0.62F, -0.00F));
        PartDefinition bellyChest = body.addOrReplaceChild("belly_chest",
                CubeListBuilder.create().texOffs(311, 61)
                        .addBox(-6.0F, -12.0F, -6.6F, 12.0F, 2.2F, 13.0F),
                PartPose.offsetAndRotation(0.0F, 2.2F, -9.0F, 0.06F, 0.00F, 0.00F));
        PartDefinition bellyTorso = body.addOrReplaceChild("belly_torso",
                CubeListBuilder.create().texOffs(250, 61)
                        .addBox(-5.4F, -11.4F, -9.2F, 10.8F, 2.2F, 19.0F),
                PartPose.offset(0.0F, 0.6F, 1.0F));
        PartDefinition bellyWaist = body.addOrReplaceChild("belly_waist",
                CubeListBuilder.create().texOffs(371, 103)
                        .addBox(-4.2F, -9.2F, -4.8F, 8.4F, 2.0F, 9.2F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 11.0F, -0.05F, 0.00F, 0.00F));
        PartDefinition bellyHips = body.addOrReplaceChild("belly_hips",
                CubeListBuilder.create().texOffs(613, 61)
                        .addBox(-5.6F, -10.0F, -2.0F, 11.2F, 2.0F, 11.8F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 16.4F, -0.08F, 0.00F, 0.00F));
        PartDefinition ridge0 = body.addOrReplaceChild("ridge_0",
                CubeListBuilder.create().texOffs(471, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 3.2F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.6F, -12.4F, 0.34F, 0.00F, 0.00F));
        PartDefinition ridge1 = body.addOrReplaceChild("ridge_1",
                CubeListBuilder.create().texOffs(457, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 4.0F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.6F, -9.0F, 0.30F, 0.00F, 0.00F));
        PartDefinition ridge2 = body.addOrReplaceChild("ridge_2",
                CubeListBuilder.create().texOffs(306, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 4.8F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.6F, -5.4F, 0.26F, 0.00F, 0.00F));
        PartDefinition ridge3 = body.addOrReplaceChild("ridge_3",
                CubeListBuilder.create().texOffs(292, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 5.4F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.6F, -1.6F, 0.20F, 0.00F, 0.00F));
        PartDefinition ridge4 = body.addOrReplaceChild("ridge_4",
                CubeListBuilder.create().texOffs(299, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 5.0F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.6F, 2.6F, 0.16F, 0.00F, 0.00F));
        PartDefinition ridge5 = body.addOrReplaceChild("ridge_5",
                CubeListBuilder.create().texOffs(351, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 4.4F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.6F, 6.8F, 0.14F, 0.00F, 0.00F));
        PartDefinition ridge6 = body.addOrReplaceChild("ridge_6",
                CubeListBuilder.create().texOffs(464, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 3.6F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.3F, 11.0F, 0.10F, 0.00F, 0.00F));
        PartDefinition ridge7 = body.addOrReplaceChild("ridge_7",
                CubeListBuilder.create().texOffs(504, 170)
                        .addBox(-0.6F, 0.0F, -3.0F, 1.1F, 3.0F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 7.3F, 15.0F, 0.06F, 0.00F, 0.00F));
        PartDefinition ridgeSideL = chest.addOrReplaceChild("ridge_side_l",
                CubeListBuilder.create().texOffs(396, 170)
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 3.4F, 2.0F),
                PartPose.offsetAndRotation(6.4F, 6.0F, -1.0F, 0.00F, 0.00F, -0.75F));
        PartDefinition ridgeSideR = chest.addOrReplaceChild("ridge_side_r",
                CubeListBuilder.create().texOffs(403, 170).mirror()
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 3.4F, 2.0F),
                PartPose.offsetAndRotation(-6.4F, 6.0F, -1.0F, 0.00F, -0.00F, 0.75F));
        PartDefinition scapulaL = chest.addOrReplaceChild("scapula_l",
                CubeListBuilder.create().texOffs(184, 170)
                        .addBox(-1.1F, 0.0F, -2.6F, 2.3F, 5.8F, 2.6F),
                PartPose.offsetAndRotation(7.8F, 5.4F, -0.8F, 0.00F, 0.00F, -0.50F));
        PartDefinition scapulaR = chest.addOrReplaceChild("scapula_r",
                CubeListBuilder.create().texOffs(195, 170).mirror()
                        .addBox(-1.1F, 0.0F, -2.6F, 2.3F, 5.8F, 2.6F),
                PartPose.offsetAndRotation(-7.8F, 5.4F, -0.8F, 0.00F, -0.00F, 0.50F));
        PartDefinition hipSpurL = hips.addOrReplaceChild("hip_spur_l",
                CubeListBuilder.create().texOffs(257, 170)
                        .addBox(-0.8F, 0.0F, -2.4F, 1.7F, 5.4F, 2.6F),
                PartPose.offsetAndRotation(8.0F, 4.4F, 2.6F, 0.00F, 0.00F, -0.95F));
        PartDefinition hipSpurR = hips.addOrReplaceChild("hip_spur_r",
                CubeListBuilder.create().texOffs(268, 170).mirror()
                        .addBox(-0.8F, 0.0F, -2.4F, 1.7F, 5.4F, 2.6F),
                PartPose.offsetAndRotation(-8.0F, 4.4F, 2.6F, 0.00F, -0.00F, 0.95F));
        PartDefinition legFlThigh = body.addOrReplaceChild("leg_fl_thigh",
                CubeListBuilder.create().texOffs(441, 103)
                        .addBox(-3.6F, -12.0F, -4.6F, 7.2F, 12.0F, 9.2F),
                PartPose.offsetAndRotation(7.0F, -5.8F, -8.8F, 0.18F, 0.00F, 0.00F));
        PartDefinition legFrThigh = body.addOrReplaceChild("leg_fr_thigh",
                CubeListBuilder.create().texOffs(474, 103).mirror()
                        .addBox(-3.6F, -12.0F, -4.6F, 7.2F, 12.0F, 9.2F),
                PartPose.offsetAndRotation(-7.0F, -5.8F, -8.8F, 0.18F, -0.00F, -0.00F));
        PartDefinition legFlHaunch = legFlThigh.addOrReplaceChild("leg_fl_haunch",
                CubeListBuilder.create().texOffs(168, 103)
                        .addBox(-4.0F, -4.4F, -5.4F, 8.0F, 8.8F, 10.8F),
                PartPose.offset(0.6F, -1.8F, -0.6F));
        PartDefinition legFrHaunch = legFrThigh.addOrReplaceChild("leg_fr_haunch",
                CubeListBuilder.create().texOffs(207, 103).mirror()
                        .addBox(-4.0F, -4.4F, -5.4F, 8.0F, 8.8F, 10.8F),
                PartPose.offset(-0.6F, -1.8F, -0.6F));
        PartDefinition legFlShin = legFlThigh.addOrReplaceChild("leg_fl_shin",
                CubeListBuilder.create().texOffs(199, 135)
                        .addBox(-2.8F, -9.8F, -3.1F, 5.6F, 9.8F, 6.2F),
                PartPose.offsetAndRotation(0.0F, -11.6F, 0.4F, -0.30F, 0.00F, 0.00F));
        PartDefinition legFrShin = legFrThigh.addOrReplaceChild("leg_fr_shin",
                CubeListBuilder.create().texOffs(224, 135).mirror()
                        .addBox(-2.8F, -9.8F, -3.1F, 5.6F, 9.8F, 6.2F),
                PartPose.offsetAndRotation(-0.0F, -11.6F, 0.4F, -0.30F, -0.00F, -0.00F));
        PartDefinition legFlCalf = legFlShin.addOrReplaceChild("leg_fl_calf",
                CubeListBuilder.create().texOffs(796, 103)
                        .addBox(-3.1F, -3.6F, -3.7F, 6.2F, 7.2F, 7.4F),
                PartPose.offset(0.0F, -3.2F, -0.8F));
        PartDefinition legFrCalf = legFrShin.addOrReplaceChild("leg_fr_calf",
                CubeListBuilder.create().texOffs(823, 103).mirror()
                        .addBox(-3.1F, -3.6F, -3.7F, 6.2F, 7.2F, 7.4F),
                PartPose.offset(-0.0F, -3.2F, -0.8F));
        PartDefinition legFlFoot = legFlShin.addOrReplaceChild("leg_fl_foot",
                CubeListBuilder.create().texOffs(993, 103)
                        .addBox(-3.0F, -1.8F, -4.6F, 6.0F, 3.6F, 7.4F),
                PartPose.offsetAndRotation(0.0F, -9.4F, -0.2F, 0.22F, 0.00F, 0.00F));
        PartDefinition legFrFoot = legFrShin.addOrReplaceChild("leg_fr_foot",
                CubeListBuilder.create().texOffs(0, 135).mirror()
                        .addBox(-3.0F, -1.8F, -4.6F, 6.0F, 3.6F, 7.4F),
                PartPose.offsetAndRotation(-0.0F, -9.4F, -0.2F, 0.22F, -0.00F, -0.00F));
        PartDefinition legFlTalon1 = legFlFoot.addOrReplaceChild("leg_fl_talon1",
                CubeListBuilder.create().texOffs(717, 170)
                        .addBox(-0.5F, -1.0F, -1.8F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(1.9F, -1.4F, -3.8F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon1 = legFrFoot.addOrReplaceChild("leg_fr_talon1",
                CubeListBuilder.create().texOffs(724, 170).mirror()
                        .addBox(-0.5F, -1.0F, -1.8F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(-1.9F, -1.4F, -3.8F, 0.55F, -0.00F, -0.00F));
        PartDefinition legFlTalon2 = legFlFoot.addOrReplaceChild("leg_fl_talon2",
                CubeListBuilder.create().texOffs(731, 170)
                        .addBox(-0.5F, -1.0F, -1.8F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.4F, -3.8F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon2 = legFrFoot.addOrReplaceChild("leg_fr_talon2",
                CubeListBuilder.create().texOffs(738, 170).mirror()
                        .addBox(-0.5F, -1.0F, -1.8F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(-0.0F, -1.4F, -3.8F, 0.55F, -0.00F, -0.00F));
        PartDefinition legFlTalon3 = legFlFoot.addOrReplaceChild("leg_fl_talon3",
                CubeListBuilder.create().texOffs(745, 170)
                        .addBox(-0.5F, -1.0F, -1.8F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(-1.9F, -1.4F, -3.8F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon3 = legFrFoot.addOrReplaceChild("leg_fr_talon3",
                CubeListBuilder.create().texOffs(752, 170).mirror()
                        .addBox(-0.5F, -1.0F, -1.8F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(1.9F, -1.4F, -3.8F, 0.55F, -0.00F, -0.00F));
        PartDefinition legFlHeel = legFlFoot.addOrReplaceChild("leg_fl_heel",
                CubeListBuilder.create().texOffs(692, 170)
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 2.6F),
                PartPose.offsetAndRotation(0.0F, -0.9F, 3.2F, -0.55F, 0.00F, 0.00F));
        PartDefinition legFrHeel = legFrFoot.addOrReplaceChild("leg_fr_heel",
                CubeListBuilder.create().texOffs(701, 170).mirror()
                        .addBox(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 2.6F),
                PartPose.offsetAndRotation(-0.0F, -0.9F, 3.2F, -0.55F, -0.00F, -0.00F));
        PartDefinition legBlThigh = body.addOrReplaceChild("leg_bl_thigh",
                CubeListBuilder.create().texOffs(795, 61)
                        .addBox(-4.9F, -12.8F, -5.8F, 9.8F, 12.8F, 11.6F),
                PartPose.offsetAndRotation(7.4F, -5.0F, 10.2F, -0.34F, 0.00F, 0.00F));
        PartDefinition legBrThigh = body.addOrReplaceChild("leg_br_thigh",
                CubeListBuilder.create().texOffs(840, 61).mirror()
                        .addBox(-4.9F, -12.8F, -5.8F, 9.8F, 12.8F, 11.6F),
                PartPose.offsetAndRotation(-7.4F, -5.0F, 10.2F, -0.34F, -0.00F, -0.00F));
        PartDefinition legBlHaunch = legBlThigh.addOrReplaceChild("leg_bl_haunch",
                CubeListBuilder.create().texOffs(515, 61)
                        .addBox(-5.4F, -4.8F, -6.6F, 10.8F, 9.6F, 13.0F),
                PartPose.offset(0.6F, -2.0F, -0.6F));
        PartDefinition legBrHaunch = legBrThigh.addOrReplaceChild("leg_br_haunch",
                CubeListBuilder.create().texOffs(564, 61).mirror()
                        .addBox(-5.4F, -4.8F, -6.6F, 10.8F, 9.6F, 13.0F),
                PartPose.offset(-0.6F, -2.0F, -0.6F));
        PartDefinition legBlShin = legBlThigh.addOrReplaceChild("leg_bl_shin",
                CubeListBuilder.create().texOffs(850, 103)
                        .addBox(-3.1F, -10.6F, -3.4F, 6.2F, 10.6F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -12.4F, 0.0F, 0.66F, 0.00F, 0.00F));
        PartDefinition legBrShin = legBrThigh.addOrReplaceChild("leg_br_shin",
                CubeListBuilder.create().texOffs(877, 103).mirror()
                        .addBox(-3.1F, -10.6F, -3.4F, 6.2F, 10.6F, 6.8F),
                PartPose.offsetAndRotation(-0.0F, -12.4F, 0.0F, 0.66F, -0.00F, -0.00F));
        PartDefinition legBlCalf = legBlShin.addOrReplaceChild("leg_bl_calf",
                CubeListBuilder.create().texOffs(672, 103)
                        .addBox(-3.4F, -3.8F, -4.0F, 6.8F, 7.6F, 8.0F),
                PartPose.offset(0.0F, -3.4F, -0.8F));
        PartDefinition legBrCalf = legBrShin.addOrReplaceChild("leg_br_calf",
                CubeListBuilder.create().texOffs(703, 103).mirror()
                        .addBox(-3.4F, -3.8F, -4.0F, 6.8F, 7.6F, 8.0F),
                PartPose.offset(-0.0F, -3.4F, -0.8F));
        PartDefinition legBlFoot = legBlShin.addOrReplaceChild("leg_bl_foot",
                CubeListBuilder.create().texOffs(507, 103)
                        .addBox(-3.8F, -2.0F, -5.6F, 7.6F, 3.8F, 8.8F),
                PartPose.offsetAndRotation(0.0F, -10.0F, -0.4F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBrFoot = legBrShin.addOrReplaceChild("leg_br_foot",
                CubeListBuilder.create().texOffs(542, 103).mirror()
                        .addBox(-3.8F, -2.0F, -5.6F, 7.6F, 3.8F, 8.8F),
                PartPose.offsetAndRotation(-0.0F, -10.0F, -0.4F, -0.32F, -0.00F, -0.00F));
        PartDefinition legBlTalon1 = legBlFoot.addOrReplaceChild("leg_bl_talon1",
                CubeListBuilder.create().texOffs(609, 170)
                        .addBox(-0.5F, -1.1F, -2.0F, 1.1F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(2.0F, -1.5F, -4.4F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon1 = legBrFoot.addOrReplaceChild("leg_br_talon1",
                CubeListBuilder.create().texOffs(618, 170).mirror()
                        .addBox(-0.5F, -1.1F, -2.0F, 1.1F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(-2.0F, -1.5F, -4.4F, 0.55F, -0.00F, -0.00F));
        PartDefinition legBlTalon2 = legBlFoot.addOrReplaceChild("leg_bl_talon2",
                CubeListBuilder.create().texOffs(627, 170)
                        .addBox(-0.5F, -1.1F, -2.0F, 1.1F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(0.0F, -1.5F, -4.4F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon2 = legBrFoot.addOrReplaceChild("leg_br_talon2",
                CubeListBuilder.create().texOffs(636, 170).mirror()
                        .addBox(-0.5F, -1.1F, -2.0F, 1.1F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(-0.0F, -1.5F, -4.4F, 0.55F, -0.00F, -0.00F));
        PartDefinition legBlTalon3 = legBlFoot.addOrReplaceChild("leg_bl_talon3",
                CubeListBuilder.create().texOffs(645, 170)
                        .addBox(-0.5F, -1.1F, -2.0F, 1.1F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(-2.0F, -1.5F, -4.4F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon3 = legBrFoot.addOrReplaceChild("leg_br_talon3",
                CubeListBuilder.create().texOffs(654, 170).mirror()
                        .addBox(-0.5F, -1.1F, -2.0F, 1.1F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(2.0F, -1.5F, -4.4F, 0.55F, -0.00F, -0.00F));
        PartDefinition legBlHeel = legBlFoot.addOrReplaceChild("leg_bl_heel",
                CubeListBuilder.create().texOffs(562, 170)
                        .addBox(-0.6F, -0.6F, 0.0F, 1.1F, 1.1F, 3.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, 3.8F, -0.60F, 0.00F, 0.00F));
        PartDefinition legBrHeel = legBrFoot.addOrReplaceChild("leg_br_heel",
                CubeListBuilder.create().texOffs(571, 170).mirror()
                        .addBox(-0.6F, -0.6F, 0.0F, 1.1F, 1.1F, 3.0F),
                PartPose.offsetAndRotation(-0.0F, -1.0F, 3.8F, -0.60F, -0.00F, -0.00F));
        PartDefinition saddle = body.addOrReplaceChild("saddle",
                CubeListBuilder.create().texOffs(464, 61)
                        .addBox(-5.8F, -1.0F, -6.6F, 11.6F, 2.0F, 13.2F),
                PartPose.offset(0.0F, 7.2F, 1.2F));
        PartDefinition saddlePommel = saddle.addOrReplaceChild("saddle_pommel",
                CubeListBuilder.create().texOffs(823, 135)
                        .addBox(-2.0F, -2.8F, -2.0F, 4.0F, 3.0F, 4.0F),
                PartPose.offset(0.0F, 1.0F, -5.8F));
        PartDefinition saddleCantle = saddle.addOrReplaceChild("saddle_cantle",
                CubeListBuilder.create().texOffs(19, 170)
                        .addBox(-2.3F, -2.4F, -1.5F, 4.6F, 2.6F, 3.0F),
                PartPose.offset(0.0F, 1.0F, 5.8F));
        PartDefinition armorChest = body.addOrReplaceChild("armor_chest",
                CubeListBuilder.create().texOffs(913, 0)
                        .addBox(-9.4F, -9.8F, -6.6F, 18.8F, 17.0F, 13.2F),
                PartPose.offsetAndRotation(0.0F, 1.4F, -9.4F, 0.08F, 0.00F, 0.00F));
        PartDefinition armorTorso = body.addOrReplaceChild("armor_torso",
                CubeListBuilder.create().texOffs(635, 0)
                        .addBox(-8.2F, -9.2F, -9.4F, 16.4F, 16.2F, 18.8F),
                PartPose.offset(0.0F, 0.0F, 0.6F));
        PartDefinition armorHips = body.addOrReplaceChild("armor_hips",
                CubeListBuilder.create().texOffs(134, 61)
                        .addBox(-8.4F, -8.0F, -2.0F, 16.8F, 14.6F, 11.8F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 15.8F, -0.08F, 0.00F, 0.00F));
        PartDefinition armorCollar = body.addOrReplaceChild("armor_collar",
                CubeListBuilder.create().texOffs(977, 61)
                        .addBox(-7.4F, -1.8F, -3.6F, 14.8F, 3.4F, 7.0F),
                PartPose.offsetAndRotation(0.0F, 4.6F, -8.0F, 0.45F, 0.00F, 0.00F));
        PartDefinition saddleStrapL = body.addOrReplaceChild("saddle_strap_l",
                CubeListBuilder.create().texOffs(703, 135)
                        .addBox(-0.6F, -9.4F, -5.4F, 1.3F, 11.6F, 11.0F),
                PartPose.offset(6.8F, 0.0F, 1.2F));
        PartDefinition saddleStrapR = body.addOrReplaceChild("saddle_strap_r",
                CubeListBuilder.create().texOffs(728, 135).mirror()
                        .addBox(-0.6F, -9.4F, -5.4F, 1.3F, 11.6F, 11.0F),
                PartPose.offset(-6.8F, 0.0F, 1.2F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(885, 61)
                        .addBox(-5.2F, -5.2F, -1.6F, 10.4F, 10.4F, 10.6F),
                PartPose.offsetAndRotation(0.0F, -2.4F, 15.4F, -0.16F, 0.00F, 0.00F));
        PartDefinition tail1Spike = tail1.addOrReplaceChild("tail1_spike",
                CubeListBuilder.create().texOffs(529, 170)
                        .addBox(-0.5F, 0.0F, -2.4F, 1.0F, 3.2F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 0.55F, 0.00F, 0.00F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(129, 103)
                        .addBox(-4.5F, -4.5F, -1.6F, 9.0F, 9.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.6F, -0.05F, 0.00F, 0.00F));
        PartDefinition tail2Spike = tail2.addOrReplaceChild("tail2_spike",
                CubeListBuilder.create().texOffs(580, 170)
                        .addBox(-0.5F, 0.0F, -2.4F, 1.0F, 2.8F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 4.3F, 2.0F, 0.55F, 0.00F, 0.00F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(406, 103)
                        .addBox(-3.8F, -3.8F, -1.6F, 7.6F, 7.6F, 9.4F),
                PartPose.offset(0.0F, 0.0F, 7.0F));
        PartDefinition tail3Spike = tail3.addOrReplaceChild("tail3_spike",
                CubeListBuilder.create().texOffs(663, 170)
                        .addBox(-0.5F, 0.0F, -2.4F, 1.0F, 2.3F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 3.6F, 2.0F, 0.55F, 0.00F, 0.00F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(734, 103)
                        .addBox(-3.1F, -3.1F, -1.6F, 6.2F, 6.2F, 8.8F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 6.4F, 0.05F, 0.00F, 0.00F));
        PartDefinition tail4Spike = tail4.addOrReplaceChild("tail4_spike",
                CubeListBuilder.create().texOffs(710, 170)
                        .addBox(-0.5F, 0.0F, -2.4F, 1.0F, 1.9F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 2.9F, 2.0F, 0.55F, 0.00F, 0.00F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(249, 135)
                        .addBox(-2.4F, -2.4F, -1.6F, 4.8F, 4.8F, 8.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.8F, 0.09F, 0.00F, 0.00F));
        PartDefinition tail5Spike = tail5.addOrReplaceChild("tail5_spike",
                CubeListBuilder.create().texOffs(759, 170)
                        .addBox(-0.5F, 0.0F, -2.4F, 1.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 2.2F, 2.0F, 0.55F, 0.00F, 0.00F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(682, 135)
                        .addBox(-1.7F, -1.7F, -1.6F, 3.4F, 3.4F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.2F, 0.12F, 0.00F, 0.00F));
        PartDefinition tail6Spike = tail6.addOrReplaceChild("tail6_spike",
                CubeListBuilder.create().texOffs(766, 170)
                        .addBox(-0.5F, 0.0F, -2.4F, 1.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 1.5F, 2.0F, 0.55F, 0.00F, 0.00F));
        PartDefinition tail7 = tail6.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(840, 135)
                        .addBox(-1.2F, -1.2F, -1.6F, 2.4F, 2.4F, 6.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 4.4F, 0.13F, 0.00F, 0.00F));
        PartDefinition spadeTop = tail7.addOrReplaceChild("spade_top",
                CubeListBuilder.create().texOffs(859, 135)
                        .addBox(-0.9F, -0.2F, 0.0F, 1.8F, 4.2F, 7.6F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 5.6F, -0.32F, 0.00F, 0.00F));
        PartDefinition spadeBot = tail7.addOrReplaceChild("spade_bot",
                CubeListBuilder.create().texOffs(116, 170)
                        .addBox(-0.9F, 0.0F, 0.0F, 1.8F, 3.2F, 6.6F),
                PartPose.offsetAndRotation(0.0F, 0.2F, 5.8F, 0.06F, 0.00F, 0.00F));
        PartDefinition spadeLeft = tail7.addOrReplaceChild("spade_left",
                CubeListBuilder.create().texOffs(536, 170)
                        .addBox(-0.3F, -0.6F, 0.0F, 0.7F, 1.2F, 5.2F),
                PartPose.offsetAndRotation(0.8F, 0.0F, 5.2F, 0.00F, 0.00F, -0.72F));
        PartDefinition spadeRight = tail7.addOrReplaceChild("spade_right",
                CubeListBuilder.create().texOffs(549, 170).mirror()
                        .addBox(-0.3F, -0.6F, 0.0F, 0.7F, 1.2F, 5.2F),
                PartPose.offsetAndRotation(-0.8F, 0.0F, 5.2F, 0.00F, -0.00F, 0.72F));
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(39, 103)
                        .addBox(0.0F, -2.7F, -2.9F, 16.5F, 5.4F, 5.8F),
                PartPose.offsetAndRotation(7.2F, 3.0F, -6.4F, 0.06F, -0.20F, -0.06F));
        PartDefinition wingLDeltoid = wingL.addOrReplaceChild("wing_l_deltoid",
                CubeListBuilder.create().texOffs(577, 103)
                        .addBox(0.0F, -3.6F, -2.6F, 8.4F, 7.6F, 7.4F),
                PartPose.offset(0.6F, 0.8F, -2.2F));
        PartDefinition wingLFore = wingL.addOrReplaceChild("wing_l_fore",
                CubeListBuilder.create().texOffs(246, 103)
                        .addBox(0.0F, -2.0F, -2.2F, 17.5F, 4.0F, 4.4F),
                PartPose.offsetAndRotation(16.5F, 0.0F, 0.0F, 0.00F, 0.10F, 0.04F));
        PartDefinition wingLHand = wingLFore.addOrReplaceChild("wing_l_hand",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(17.5F, 0.0F, 0.0F, 0.00F, 0.02F, -0.04F));
        PartDefinition wingLClaw = wingLHand.addOrReplaceChild("wing_l_claw",
                CubeListBuilder.create().texOffs(988, 135)
                        .addBox(0.0F, -1.0F, -1.0F, 2.2F, 2.2F, 6.6F),
                PartPose.offsetAndRotation(0.2F, 1.0F, -0.8F, -0.60F, 0.00F, 0.00F));
        PartDefinition wingLF1a = wingLHand.addOrReplaceChild("wing_l_f1a",
                CubeListBuilder.create().texOffs(117, 135)
                        .addBox(0.0F, -1.3F, -1.3F, 17.0F, 2.6F, 2.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.04F, 0.36F, -0.03F));
        PartDefinition wingLF1b = wingLF1a.addOrReplaceChild("wing_l_f1b",
                CubeListBuilder.create().texOffs(488, 135)
                        .addBox(0.0F, -1.0F, -1.0F, 14.0F, 2.1F, 2.1F),
                PartPose.offsetAndRotation(17.0F, 0.0F, 0.0F, 0.08F, 0.00F, 0.00F));
        PartDefinition wingLF2a = wingLHand.addOrReplaceChild("wing_l_f2a",
                CubeListBuilder.create().texOffs(27, 135)
                        .addBox(0.0F, -1.1F, -1.1F, 19.5F, 2.3F, 2.3F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.08F, 0.08F, -0.06F));
        PartDefinition wingLF2b = wingLF2a.addOrReplaceChild("wing_l_f2b",
                CubeListBuilder.create().texOffs(414, 135)
                        .addBox(0.0F, -0.9F, -0.9F, 16.0F, 1.8F, 1.8F),
                PartPose.offsetAndRotation(19.5F, 0.0F, 0.0F, 0.14F, 0.00F, 0.00F));
        PartDefinition wingLF3a = wingLHand.addOrReplaceChild("wing_l_f3a",
                CubeListBuilder.create().texOffs(303, 135)
                        .addBox(0.0F, -1.0F, -1.0F, 19.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.12F, -0.20F, -0.09F));
        PartDefinition wingLF3b = wingLF3a.addOrReplaceChild("wing_l_f3b",
                CubeListBuilder.create().texOffs(753, 135)
                        .addBox(0.0F, -0.8F, -0.8F, 15.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(19.0F, 0.0F, 0.0F, 0.20F, 0.00F, 0.00F));
        PartDefinition wingLF4a = wingLHand.addOrReplaceChild("wing_l_f4a",
                CubeListBuilder.create().texOffs(554, 135)
                        .addBox(0.0F, -0.8F, -0.8F, 16.0F, 1.7F, 1.7F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.16F, -0.50F, -0.12F));
        PartDefinition wingLF4b = wingLF4a.addOrReplaceChild("wing_l_f4b",
                CubeListBuilder.create().texOffs(880, 135)
                        .addBox(0.0F, -0.7F, -0.7F, 12.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(16.0F, 0.0F, 0.0F, 0.26F, 0.00F, 0.00F));
        PartDefinition wingLF5a = wingLHand.addOrReplaceChild("wing_l_f5a",
                CubeListBuilder.create().texOffs(934, 135)
                        .addBox(0.0F, -0.7F, -0.7F, 11.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.20F, -0.86F, -0.15F));
        PartDefinition wingLF5b = wingLF5a.addOrReplaceChild("wing_l_f5b",
                CubeListBuilder.create().texOffs(219, 170)
                        .addBox(0.0F, -0.6F, -0.6F, 8.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(11.5F, 0.0F, 0.0F, 0.32F, 0.00F, 0.00F));
        PartDefinition wingLTipclaw = wingLF1b.addOrReplaceChild("wing_l_tipclaw",
                CubeListBuilder.create().texOffs(511, 170)
                        .addBox(0.0F, -0.6F, -0.6F, 3.0F, 1.2F, 1.2F),
                PartPose.offset(14.0F, 0.0F, 0.0F));
        PartDefinition wingLM1 = wingLHand.addOrReplaceChild("wing_l_m1",
                CubeListBuilder.create().texOffs(202, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 31.6F, 0.6F, 14.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -4.8F, 0.00F, 0.22F, -0.05F));
        PartDefinition wingLM2 = wingLHand.addOrReplaceChild("wing_l_m2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 34.7F, 0.6F, 14.8F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -5.0F, 0.00F, -0.06F, -0.10F));
        PartDefinition wingLM3 = wingLHand.addOrReplaceChild("wing_l_m3",
                CubeListBuilder.create().texOffs(388, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 28.6F, 0.6F, 14.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -4.8F, 0.00F, -0.35F, -0.15F));
        PartDefinition wingLM4 = wingLHand.addOrReplaceChild("wing_l_m4",
                CubeListBuilder.create().texOffs(0, 61)
                        .addBox(0.0F, -0.3F, 0.0F, 19.9F, 0.6F, 13.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -4.5F, 0.00F, -0.68F, -0.20F));
        PartDefinition wingLM5 = wingLHand.addOrReplaceChild("wing_l_m5",
                CubeListBuilder.create().texOffs(706, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 22.9F, 0.6F, 12.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -4.1F, 0.00F, 0.70F, -0.25F));
        PartDefinition wingLM6 = wingLHand.addOrReplaceChild("wing_l_m6",
                CubeListBuilder.create().texOffs(362, 61)
                        .addBox(0.0F, -0.3F, 0.0F, 15.2F, 0.6F, 10.3F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -3.5F, 0.00F, -1.16F, -0.30F));
        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(84, 103).mirror()
                        .addBox(-16.5F, -2.7F, -2.9F, 16.5F, 5.4F, 5.8F),
                PartPose.offsetAndRotation(-7.2F, 3.0F, -6.4F, 0.06F, 0.20F, 0.06F));
        PartDefinition wingRDeltoid = wingR.addOrReplaceChild("wing_r_deltoid",
                CubeListBuilder.create().texOffs(608, 103).mirror()
                        .addBox(-8.4F, -3.6F, -2.6F, 8.4F, 7.6F, 7.4F),
                PartPose.offset(-0.6F, 0.8F, -2.2F));
        PartDefinition wingRFore = wingR.addOrReplaceChild("wing_r_fore",
                CubeListBuilder.create().texOffs(291, 103).mirror()
                        .addBox(-17.5F, -2.0F, -2.2F, 17.5F, 4.0F, 4.4F),
                PartPose.offsetAndRotation(-16.5F, 0.0F, 0.0F, 0.00F, -0.10F, -0.04F));
        PartDefinition wingRHand = wingRFore.addOrReplaceChild("wing_r_hand",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-17.5F, 0.0F, 0.0F, 0.00F, -0.02F, 0.04F));
        PartDefinition wingRClaw = wingRHand.addOrReplaceChild("wing_r_claw",
                CubeListBuilder.create().texOffs(0, 170).mirror()
                        .addBox(-2.2F, -1.0F, -1.0F, 2.2F, 2.2F, 6.6F),
                PartPose.offsetAndRotation(-0.2F, 1.0F, -0.8F, -0.60F, -0.00F, -0.00F));
        PartDefinition wingRF1a = wingRHand.addOrReplaceChild("wing_r_f1a",
                CubeListBuilder.create().texOffs(158, 135).mirror()
                        .addBox(-17.0F, -1.3F, -1.3F, 17.0F, 2.6F, 2.6F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.04F, -0.36F, 0.03F));
        PartDefinition wingRF1b = wingRF1a.addOrReplaceChild("wing_r_f1b",
                CubeListBuilder.create().texOffs(521, 135).mirror()
                        .addBox(-14.0F, -1.0F, -1.0F, 14.0F, 2.1F, 2.1F),
                PartPose.offsetAndRotation(-17.0F, 0.0F, 0.0F, 0.08F, -0.00F, -0.00F));
        PartDefinition wingRF2a = wingRHand.addOrReplaceChild("wing_r_f2a",
                CubeListBuilder.create().texOffs(72, 135).mirror()
                        .addBox(-19.5F, -1.1F, -1.1F, 19.5F, 2.3F, 2.3F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.08F, -0.08F, 0.06F));
        PartDefinition wingRF2b = wingRF2a.addOrReplaceChild("wing_r_f2b",
                CubeListBuilder.create().texOffs(451, 135).mirror()
                        .addBox(-16.0F, -0.9F, -0.9F, 16.0F, 1.8F, 1.8F),
                PartPose.offsetAndRotation(-19.5F, 0.0F, 0.0F, 0.14F, -0.00F, -0.00F));
        PartDefinition wingRF3a = wingRHand.addOrReplaceChild("wing_r_f3a",
                CubeListBuilder.create().texOffs(346, 135).mirror()
                        .addBox(-19.0F, -1.0F, -1.0F, 19.0F, 2.0F, 2.0F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.12F, 0.20F, 0.09F));
        PartDefinition wingRF3b = wingRF3a.addOrReplaceChild("wing_r_f3b",
                CubeListBuilder.create().texOffs(788, 135).mirror()
                        .addBox(-15.0F, -0.8F, -0.8F, 15.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(-19.0F, 0.0F, 0.0F, 0.20F, -0.00F, -0.00F));
        PartDefinition wingRF4a = wingRHand.addOrReplaceChild("wing_r_f4a",
                CubeListBuilder.create().texOffs(591, 135).mirror()
                        .addBox(-16.0F, -0.8F, -0.8F, 16.0F, 1.7F, 1.7F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.16F, 0.50F, 0.12F));
        PartDefinition wingRF4b = wingRF4a.addOrReplaceChild("wing_r_f4b",
                CubeListBuilder.create().texOffs(907, 135).mirror()
                        .addBox(-12.0F, -0.7F, -0.7F, 12.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(-16.0F, 0.0F, 0.0F, 0.26F, -0.00F, -0.00F));
        PartDefinition wingRF5a = wingRHand.addOrReplaceChild("wing_r_f5a",
                CubeListBuilder.create().texOffs(961, 135).mirror()
                        .addBox(-11.5F, -0.7F, -0.7F, 11.5F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.20F, 0.86F, 0.15F));
        PartDefinition wingRF5b = wingRF5a.addOrReplaceChild("wing_r_f5b",
                CubeListBuilder.create().texOffs(238, 170).mirror()
                        .addBox(-8.0F, -0.6F, -0.6F, 8.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(-11.5F, 0.0F, 0.0F, 0.32F, -0.00F, -0.00F));
        PartDefinition wingRTipclaw = wingRF1b.addOrReplaceChild("wing_r_tipclaw",
                CubeListBuilder.create().texOffs(520, 170).mirror()
                        .addBox(-3.0F, -0.6F, -0.6F, 3.0F, 1.2F, 1.2F),
                PartPose.offset(-14.0F, 0.0F, 0.0F));
        PartDefinition wingRM1 = wingRHand.addOrReplaceChild("wing_r_m1",
                CubeListBuilder.create().texOffs(295, 0).mirror()
                        .addBox(-31.6F, -0.3F, 0.0F, 31.6F, 0.6F, 14.2F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -4.8F, 0.00F, -0.22F, 0.05F));
        PartDefinition wingRM2 = wingRHand.addOrReplaceChild("wing_r_m2",
                CubeListBuilder.create().texOffs(101, 0).mirror()
                        .addBox(-34.7F, -0.3F, 0.0F, 34.7F, 0.6F, 14.8F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -5.0F, 0.00F, 0.06F, 0.10F));
        PartDefinition wingRM3 = wingRHand.addOrReplaceChild("wing_r_m3",
                CubeListBuilder.create().texOffs(475, 0).mirror()
                        .addBox(-28.6F, -0.3F, 0.0F, 28.6F, 0.6F, 14.2F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -4.8F, 0.00F, 0.35F, 0.15F));
        PartDefinition wingRM4 = wingRHand.addOrReplaceChild("wing_r_m4",
                CubeListBuilder.create().texOffs(67, 61).mirror()
                        .addBox(-19.9F, -0.3F, 0.0F, 19.9F, 0.6F, 13.2F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -4.5F, 0.00F, 0.68F, 0.20F));
        PartDefinition wingRM5 = wingRHand.addOrReplaceChild("wing_r_m5",
                CubeListBuilder.create().texOffs(777, 0).mirror()
                        .addBox(-22.9F, -0.3F, 0.0F, 22.9F, 0.6F, 12.2F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -4.1F, 0.00F, -0.70F, 0.25F));
        PartDefinition wingRM6 = wingRHand.addOrReplaceChild("wing_r_m6",
                CubeListBuilder.create().texOffs(413, 61).mirror()
                        .addBox(-15.2F, -0.3F, 0.0F, 15.2F, 0.6F, 10.3F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -3.5F, 0.00F, 1.16F, 0.30F));
        return mesh;
    }
}
