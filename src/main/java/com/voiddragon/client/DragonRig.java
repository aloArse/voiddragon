package com.voiddragon.client;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;

/**
 * GENERATED FILE - do not edit by hand.
 * Geometry lives in tools/dragon_spec.py; run tools/gen_model.py to regenerate.
 * Texture UVs are packed by the same script into the 512x512 atlas
 * (tools/atlas_uv.json) and painted by tools/gen_textures.py.
 */
public final class DragonRig {
    private DragonRig() {
    }

    public static MeshDefinition createMesh() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 26.4F, 0.0F));
        PartDefinition chest = body.addOrReplaceChild("chest",
                CubeListBuilder.create().texOffs(157, 51)
                        .addBox(-7.8F, -6.2F, -7.8F, 15.6F, 12.4F, 8.8F),
                PartPose.offsetAndRotation(0.0F, 0.8F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition torso = body.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(196, 0)
                        .addBox(-6.2F, -6.2F, -8.8F, 12.4F, 12.4F, 17.6F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition waist = body.addOrReplaceChild("waist",
                CubeListBuilder.create().texOffs(467, 51)
                        .addBox(-5.2F, -5.0F, -3.6F, 10.4F, 10.0F, 7.2F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition hips = body.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(207, 51)
                        .addBox(-7.0F, -6.4F, -0.8F, 14.0F, 12.8F, 8.8F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(395, 87)
                        .addBox(-3.5F, -4.0F, -5.4F, 7.0F, 8.0F, 5.4F),
                PartPose.offsetAndRotation(0.0F, 5.6F, -10.4F, 1.16F, 0.00F, 0.00F));
        PartDefinition neck1Spike = neck1.addOrReplaceChild("neck1_spike",
                CubeListBuilder.create().texOffs(323, 155)
                        .addBox(-0.7F, -3.2F, -4.4F, 1.4F, 3.4F, 4.9F),
                PartPose.offset(0.0F, -3.7F, -0.8F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(51, 126)
                        .addBox(-3.1F, -3.6F, -4.8F, 6.2F, 7.2F, 4.8F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.4F, -0.48F, 0.00F, 0.00F));
        PartDefinition neck2Spike = neck2.addOrReplaceChild("neck2_spike",
                CubeListBuilder.create().texOffs(352, 155)
                        .addBox(-0.7F, -3.2F, -3.9F, 1.4F, 3.4F, 4.3F),
                PartPose.offset(0.0F, -3.3F, -0.8F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(207, 126)
                        .addBox(-2.7F, -3.2F, -4.4F, 5.4F, 6.4F, 4.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.8F, -0.38F, 0.00F, 0.00F));
        PartDefinition neck3Spike = neck3.addOrReplaceChild("neck3_spike",
                CubeListBuilder.create().texOffs(425, 155)
                        .addBox(-0.7F, -3.2F, -3.6F, 1.4F, 3.4F, 4.0F),
                PartPose.offset(0.0F, -2.9F, -0.8F));
        PartDefinition neck4 = neck3.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(327, 126)
                        .addBox(-2.4F, -2.9F, -4.0F, 4.8F, 5.8F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.4F, -0.24F, 0.00F, 0.00F));
        PartDefinition neck4Spike = neck4.addOrReplaceChild("neck4_spike",
                CubeListBuilder.create().texOffs(451, 155)
                        .addBox(-0.7F, -3.2F, -3.3F, 1.4F, 3.4F, 3.6F),
                PartPose.offset(0.0F, -2.6F, -0.8F));
        PartDefinition neck5 = neck4.addOrReplaceChild("neck5",
                CubeListBuilder.create().texOffs(438, 126)
                        .addBox(-2.1F, -2.6F, -3.6F, 4.2F, 5.2F, 3.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.16F, 0.00F, 0.00F));
        PartDefinition neck5Spike = neck5.addOrReplaceChild("neck5_spike",
                CubeListBuilder.create().texOffs(462, 155)
                        .addBox(-0.7F, -3.2F, -3.0F, 1.4F, 3.4F, 3.2F),
                PartPose.offset(0.0F, -2.3F, -0.8F));
        PartDefinition bellyChest = body.addOrReplaceChild("belly_chest",
                CubeListBuilder.create().texOffs(428, 51)
                        .addBox(-5.4F, -7.4F, -7.2F, 10.8F, 1.5F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition bellyTorso = body.addOrReplaceChild("belly_torso",
                CubeListBuilder.create().texOffs(53, 51)
                        .addBox(-4.8F, -7.0F, -8.4F, 9.6F, 1.5F, 16.8F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bellyWaist = body.addOrReplaceChild("belly_waist",
                CubeListBuilder.create().texOffs(239, 87)
                        .addBox(-4.0F, -5.8F, -3.4F, 8.0F, 1.4F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition bellyHips = body.addOrReplaceChild("belly_hips",
                CubeListBuilder.create().texOffs(389, 51)
                        .addBox(-5.2F, -7.2F, -0.6F, 10.4F, 1.5F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition ridgeChest = body.addOrReplaceChild("ridge_chest",
                CubeListBuilder.create().texOffs(80, 155)
                        .addBox(-1.0F, -8.4F, -7.4F, 2.0F, 2.8F, 8.2F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition ridgeTorso = body.addOrReplaceChild("ridge_torso",
                CubeListBuilder.create().texOffs(471, 87)
                        .addBox(-1.0F, -8.0F, -8.6F, 2.0F, 3.2F, 17.2F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition ridgeWaist = body.addOrReplaceChild("ridge_waist",
                CubeListBuilder.create().texOffs(222, 155)
                        .addBox(-0.8F, -6.6F, -3.4F, 1.6F, 2.6F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition ridgeHips = body.addOrReplaceChild("ridge_hips",
                CubeListBuilder.create().texOffs(101, 155)
                        .addBox(-0.9F, -8.0F, -0.6F, 1.8F, 2.8F, 8.6F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition scapulaL = chest.addOrReplaceChild("scapula_l",
                CubeListBuilder.create().texOffs(271, 155)
                        .addBox(-0.9F, 0.0F, -2.8F, 1.8F, 6.2F, 2.8F),
                PartPose.offsetAndRotation(7.0F, 5.2F, -0.6F, 0.00F, 0.00F, -0.55F));
        PartDefinition scapulaL2 = chest.addOrReplaceChild("scapula_l2",
                CubeListBuilder.create().texOffs(377, 155)
                        .addBox(-0.8F, 0.0F, -2.2F, 1.6F, 5.2F, 2.4F),
                PartPose.offsetAndRotation(8.2F, 4.2F, 2.2F, 0.00F, 0.00F, -0.75F));
        PartDefinition scapulaR = chest.addOrReplaceChild("scapula_r",
                CubeListBuilder.create().texOffs(281, 155).mirror()
                        .addBox(-0.9F, 0.0F, -2.8F, 1.8F, 6.2F, 2.8F),
                PartPose.offsetAndRotation(-7.0F, 5.2F, -0.6F, 0.00F, 0.00F, 0.55F));
        PartDefinition scapulaR2 = chest.addOrReplaceChild("scapula_r2",
                CubeListBuilder.create().texOffs(386, 155).mirror()
                        .addBox(-0.8F, 0.0F, -2.2F, 1.6F, 5.2F, 2.4F),
                PartPose.offsetAndRotation(-8.2F, 4.2F, 2.2F, 0.00F, 0.00F, 0.75F));
        PartDefinition hipSpurL = hips.addOrReplaceChild("hip_spur_l",
                CubeListBuilder.create().texOffs(500, 155)
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(6.6F, 3.6F, 2.6F, 0.00F, 0.00F, -0.90F));
        PartDefinition hipSpurR = hips.addOrReplaceChild("hip_spur_r",
                CubeListBuilder.create().texOffs(0, 176).mirror()
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(-6.6F, 3.6F, 2.6F, 0.00F, 0.00F, 0.90F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(99, 87)
                        .addBox(-3.6F, -3.6F, 0.0F, 7.2F, 7.2F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -1.6F, 13.4F, -0.12F, 0.00F, 0.00F));
        PartDefinition tail1Spike = tail1.addOrReplaceChild("tail1_spike",
                CubeListBuilder.create().texOffs(337, 155)
                        .addBox(-0.6F, -2.8F, -2.4F, 1.2F, 3.0F, 5.6F),
                PartPose.offset(0.0F, -3.3F, 2.2F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(209, 87)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 8.4F, -0.03F, 0.00F, 0.00F));
        PartDefinition tail2Spike = tail2.addOrReplaceChild("tail2_spike",
                CubeListBuilder.create().texOffs(437, 155)
                        .addBox(-0.6F, -2.6F, -2.2F, 1.1F, 2.8F, 5.2F),
                PartPose.offset(0.0F, -2.8F, 2.2F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(367, 87)
                        .addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.1F, 8.4F, 0.05F, 0.00F, 0.00F));
        PartDefinition tail3Spike = tail3.addOrReplaceChild("tail3_spike",
                CubeListBuilder.create().texOffs(8, 176)
                        .addBox(-0.5F, -2.4F, -2.0F, 1.0F, 2.6F, 4.8F),
                PartPose.offset(0.0F, -2.3F, 2.2F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 126)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.10F, 0.00F, 0.00F));
        PartDefinition tail4Spike = tail4.addOrReplaceChild("tail4_spike",
                CubeListBuilder.create().texOffs(47, 176)
                        .addBox(-0.5F, -2.0F, -1.8F, 0.9F, 2.2F, 4.4F),
                PartPose.offset(0.0F, -1.8F, 2.2F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(228, 126)
                        .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.13F, 0.00F, 0.00F));
        PartDefinition tail5Spike = tail5.addOrReplaceChild("tail5_spike",
                CubeListBuilder.create().texOffs(69, 176)
                        .addBox(-0.4F, -1.7F, -1.6F, 0.8F, 1.8F, 4.0F),
                PartPose.offset(0.0F, -1.3F, 2.2F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(123, 155)
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.14F, 0.00F, 0.00F));
        PartDefinition tail7 = tail6.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(306, 155)
                        .addBox(-0.7F, -0.7F, 0.0F, 1.4F, 1.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.4F, 0.14F, 0.00F, 0.00F));
        PartDefinition spadeTop = tail7.addOrReplaceChild("spade_top",
                CubeListBuilder.create().texOffs(254, 155)
                        .addBox(-0.7F, -0.2F, 0.0F, 1.4F, 3.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 5.8F, -0.34F, 0.00F, 0.00F));
        PartDefinition spadeBot = tail7.addOrReplaceChild("spade_bot",
                CubeListBuilder.create().texOffs(291, 155)
                        .addBox(-0.7F, 0.0F, 0.0F, 1.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 0.2F, 6.0F, 0.05F, 0.00F, 0.00F));
        PartDefinition spadeLeft = tail7.addOrReplaceChild("spade_left",
                CubeListBuilder.create().texOffs(174, 176)
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 5.4F, 0.00F, 0.00F, -0.70F));
        PartDefinition spadeRight = tail7.addOrReplaceChild("spade_right",
                CubeListBuilder.create().texOffs(185, 176).mirror()
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 5.4F, 0.00F, 0.00F, 0.70F));
        PartDefinition head = neck5.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(340, 87)
                        .addBox(-3.3F, -2.7F, -6.4F, 6.6F, 5.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.2F, -0.16F, 0.00F, 0.00F));
        PartDefinition skullTop = head.addOrReplaceChild("skull_top",
                CubeListBuilder.create().texOffs(162, 126)
                        .addBox(-2.7F, -0.9F, -5.6F, 5.4F, 1.9F, 5.8F),
                PartPose.offset(0.0F, 2.6F, -1.0F));
        PartDefinition browL = head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(21, 176)
                        .addBox(-0.6F, -0.6F, -2.4F, 1.1F, 1.1F, 4.8F),
                PartPose.offsetAndRotation(2.5F, 1.8F, -3.6F, 0.00F, 0.00F, -0.25F));
        PartDefinition browR = head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(34, 176).mirror()
                        .addBox(-0.6F, -0.6F, -2.4F, 1.1F, 1.1F, 4.8F),
                PartPose.offsetAndRotation(-2.5F, 1.8F, -3.6F, 0.00F, 0.00F, 0.25F));
        PartDefinition muzzle = head.addOrReplaceChild("muzzle",
                CubeListBuilder.create().texOffs(185, 126)
                        .addBox(-2.4F, -1.9F, -5.8F, 4.8F, 3.6F, 5.8F),
                PartPose.offset(0.0F, -0.7F, -6.0F));
        PartDefinition nostrilL = muzzle.addOrReplaceChild("nostril_l",
                CubeListBuilder.create().texOffs(266, 176)
                        .addBox(-0.5F, -0.8F, -0.7F, 0.9F, 1.5F, 1.3F),
                PartPose.offset(1.1F, 0.7F, -4.7F));
        PartDefinition nostrilR = muzzle.addOrReplaceChild("nostril_r",
                CubeListBuilder.create().texOffs(271, 176)
                        .addBox(-0.5F, -0.8F, -0.7F, 0.9F, 1.5F, 1.3F),
                PartPose.offset(-1.1F, 0.7F, -4.7F));
        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(26, 126)
                        .addBox(-2.4F, 0.0F, -7.4F, 4.8F, 1.8F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 1.9F, -1.2F, 0.05F, 0.00F, 0.00F));
        PartDefinition teethL = head.addOrReplaceChild("teeth_l",
                CubeListBuilder.create().texOffs(138, 176)
                        .addBox(-0.4F, 0.0F, -0.9F, 0.8F, 1.5F, 2.4F),
                PartPose.offset(1.9F, 0.7F, -5.6F));
        PartDefinition teethR = head.addOrReplaceChild("teeth_r",
                CubeListBuilder.create().texOffs(145, 176)
                        .addBox(-0.4F, 0.0F, -0.9F, 0.8F, 1.5F, 2.4F),
                PartPose.offset(-1.9F, 0.7F, -5.6F));
        PartDefinition fangs = jaw.addOrReplaceChild("fangs",
                CubeListBuilder.create().texOffs(364, 155)
                        .addBox(-1.8F, 0.0F, -1.0F, 3.6F, 1.4F, 2.2F),
                PartPose.offset(0.0F, 0.0F, -6.4F));
        PartDefinition eyeL = head.addOrReplaceChild("eye_l",
                CubeListBuilder.create().texOffs(120, 176)
                        .addBox(-0.3F, -0.6F, -1.5F, 0.8F, 1.2F, 3.0F),
                PartPose.offset(2.9F, 0.6F, -4.3F));
        PartDefinition eyeR = head.addOrReplaceChild("eye_r",
                CubeListBuilder.create().texOffs(129, 176).mirror()
                        .addBox(-0.3F, -0.6F, -1.5F, 0.8F, 1.2F, 3.0F),
                PartPose.offset(-2.9F, 0.6F, -4.3F));
        PartDefinition hornL1 = head.addOrReplaceChild("horn_l1",
                CubeListBuilder.create().texOffs(472, 155)
                        .addBox(-0.6F, -0.7F, 0.0F, 1.2F, 1.4F, 5.2F),
                PartPose.offsetAndRotation(1.9F, 2.3F, 0.6F, -0.30F, -0.22F, 0.00F));
        PartDefinition hornL2 = hornL1.addOrReplaceChild("horn_l2",
                CubeListBuilder.create().texOffs(98, 176)
                        .addBox(-0.4F, -0.4F, 0.0F, 0.8F, 0.9F, 4.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.2F, -0.34F, -0.16F, 0.00F));
        PartDefinition hornL3 = hornL2.addOrReplaceChild("horn_l3",
                CubeListBuilder.create().texOffs(276, 176)
                        .addBox(-0.2F, -0.2F, 0.0F, 0.3F, 0.4F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 4.2F, -0.40F, 0.00F, 0.00F));
        PartDefinition hornR1 = head.addOrReplaceChild("horn_r1",
                CubeListBuilder.create().texOffs(486, 155).mirror()
                        .addBox(-0.6F, -0.7F, 0.0F, 1.2F, 1.4F, 5.2F),
                PartPose.offsetAndRotation(-1.9F, 2.3F, 0.6F, -0.30F, 0.22F, 0.00F));
        PartDefinition hornR2 = hornR1.addOrReplaceChild("horn_r2",
                CubeListBuilder.create().texOffs(109, 176).mirror()
                        .addBox(-0.4F, -0.4F, 0.0F, 0.8F, 0.9F, 4.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.2F, -0.34F, 0.16F, 0.00F));
        PartDefinition hornR3 = hornR2.addOrReplaceChild("horn_r3",
                CubeListBuilder.create().texOffs(284, 176).mirror()
                        .addBox(-0.2F, -0.2F, 0.0F, 0.3F, 0.4F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 4.2F, -0.40F, 0.00F, 0.00F));
        PartDefinition crestL = head.addOrReplaceChild("crest_l",
                CubeListBuilder.create().texOffs(59, 176)
                        .addBox(-0.3F, 0.0F, -0.6F, 0.6F, 4.2F, 1.3F),
                PartPose.offsetAndRotation(1.6F, 1.1F, 2.5F, 0.92F, -0.85F, 0.00F));
        PartDefinition crestR = head.addOrReplaceChild("crest_r",
                CubeListBuilder.create().texOffs(64, 176).mirror()
                        .addBox(-0.3F, 0.0F, -0.6F, 0.6F, 4.2F, 1.3F),
                PartPose.offsetAndRotation(-1.6F, 1.1F, 2.5F, 0.92F, 0.85F, 0.00F));
        PartDefinition cheekL = head.addOrReplaceChild("cheek_l",
                CubeListBuilder.create().texOffs(80, 176)
                        .addBox(-0.3F, -1.2F, 0.0F, 0.7F, 2.4F, 3.2F),
                PartPose.offsetAndRotation(3.1F, -0.5F, 0.8F, 0.00F, -1.45F, 0.35F));
        PartDefinition cheekR = head.addOrReplaceChild("cheek_r",
                CubeListBuilder.create().texOffs(89, 176).mirror()
                        .addBox(-0.3F, -1.2F, 0.0F, 0.7F, 2.4F, 3.2F),
                PartPose.offsetAndRotation(-3.1F, -0.5F, 0.8F, 0.00F, 1.45F, -0.35F));
        PartDefinition barbelL = jaw.addOrReplaceChild("barbel_l",
                CubeListBuilder.create().texOffs(152, 176)
                        .addBox(-0.3F, 0.0F, 0.0F, 0.6F, 3.0F, 0.6F),
                PartPose.offsetAndRotation(1.6F, 0.3F, -1.0F, -0.50F, 0.00F, 0.25F));
        PartDefinition barbelR = jaw.addOrReplaceChild("barbel_r",
                CubeListBuilder.create().texOffs(155, 176).mirror()
                        .addBox(-0.3F, 0.0F, 0.0F, 0.6F, 3.0F, 0.6F),
                PartPose.offsetAndRotation(-1.6F, 0.3F, -1.0F, -0.50F, 0.00F, -0.25F));
        PartDefinition legFlThigh = body.addOrReplaceChild("leg_fl_thigh",
                CubeListBuilder.create().texOffs(421, 87)
                        .addBox(-2.5F, -9.5F, -3.4F, 5.0F, 10.5F, 7.0F),
                PartPose.offsetAndRotation(6.0F, -5.0F, -8.6F, 0.14F, 0.00F, 0.00F));
        PartDefinition legFlShin = legFlThigh.addOrReplaceChild("leg_fl_shin",
                CubeListBuilder.create().texOffs(293, 126)
                        .addBox(-1.9F, -9.0F, -2.2F, 3.8F, 9.0F, 4.4F),
                PartPose.offsetAndRotation(0.0F, -9.5F, 0.4F, -0.34F, 0.00F, 0.00F));
        PartDefinition legFlFoot = legFlShin.addOrReplaceChild("leg_fl_foot",
                CubeListBuilder.create().texOffs(251, 126)
                        .addBox(-2.3F, -1.4F, -3.6F, 4.6F, 2.4F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -0.2F, 0.20F, 0.00F, 0.00F));
        PartDefinition legFlTalon1 = legFlFoot.addOrReplaceChild("leg_fl_talon1",
                CubeListBuilder.create().texOffs(238, 176)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon2 = legFlFoot.addOrReplaceChild("leg_fl_talon2",
                CubeListBuilder.create().texOffs(224, 176)
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon3 = legFlFoot.addOrReplaceChild("leg_fl_talon3",
                CubeListBuilder.create().texOffs(245, 176)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrThigh = body.addOrReplaceChild("leg_fr_thigh",
                CubeListBuilder.create().texOffs(446, 87).mirror()
                        .addBox(-2.5F, -9.5F, -3.4F, 5.0F, 10.5F, 7.0F),
                PartPose.offsetAndRotation(-6.0F, -5.0F, -8.6F, 0.14F, 0.00F, 0.00F));
        PartDefinition legFrShin = legFrThigh.addOrReplaceChild("leg_fr_shin",
                CubeListBuilder.create().texOffs(310, 126).mirror()
                        .addBox(-1.9F, -9.0F, -2.2F, 3.8F, 9.0F, 4.4F),
                PartPose.offsetAndRotation(0.0F, -9.5F, 0.4F, -0.34F, 0.00F, 0.00F));
        PartDefinition legFrFoot = legFrShin.addOrReplaceChild("leg_fr_foot",
                CubeListBuilder.create().texOffs(272, 126).mirror()
                        .addBox(-2.3F, -1.4F, -3.6F, 4.6F, 2.4F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -0.2F, 0.20F, 0.00F, 0.00F));
        PartDefinition legFrTalon1 = legFrFoot.addOrReplaceChild("leg_fr_talon1",
                CubeListBuilder.create().texOffs(252, 176).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon2 = legFrFoot.addOrReplaceChild("leg_fr_talon2",
                CubeListBuilder.create().texOffs(231, 176).mirror()
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon3 = legFrFoot.addOrReplaceChild("leg_fr_talon3",
                CubeListBuilder.create().texOffs(259, 176).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlThigh = body.addOrReplaceChild("leg_bl_thigh",
                CubeListBuilder.create().texOffs(37, 87)
                        .addBox(-3.6F, -11.0F, -3.8F, 7.2F, 12.0F, 8.0F),
                PartPose.offsetAndRotation(6.8F, -4.6F, 10.0F, -0.30F, 0.00F, 0.00F));
        PartDefinition legBlShin = legBlThigh.addOrReplaceChild("leg_bl_shin",
                CubeListBuilder.create().texOffs(120, 126)
                        .addBox(-2.3F, -9.5F, -2.6F, 4.6F, 9.5F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.62F, 0.00F, 0.00F));
        PartDefinition legBlFoot = legBlShin.addOrReplaceChild("leg_bl_foot",
                CubeListBuilder.create().texOffs(74, 126)
                        .addBox(-2.6F, -1.5F, -4.0F, 5.2F, 2.5F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBlTalon1 = legBlFoot.addOrReplaceChild("leg_bl_talon1",
                CubeListBuilder.create().texOffs(196, 176)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon2 = legBlFoot.addOrReplaceChild("leg_bl_talon2",
                CubeListBuilder.create().texOffs(158, 176)
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon3 = legBlFoot.addOrReplaceChild("leg_bl_talon3",
                CubeListBuilder.create().texOffs(203, 176)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrThigh = body.addOrReplaceChild("leg_br_thigh",
                CubeListBuilder.create().texOffs(68, 87).mirror()
                        .addBox(-3.6F, -11.0F, -3.8F, 7.2F, 12.0F, 8.0F),
                PartPose.offsetAndRotation(-6.8F, -4.6F, 10.0F, -0.30F, 0.00F, 0.00F));
        PartDefinition legBrShin = legBrThigh.addOrReplaceChild("leg_br_shin",
                CubeListBuilder.create().texOffs(141, 126).mirror()
                        .addBox(-2.3F, -9.5F, -2.6F, 4.6F, 9.5F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.62F, 0.00F, 0.00F));
        PartDefinition legBrFoot = legBrShin.addOrReplaceChild("leg_br_foot",
                CubeListBuilder.create().texOffs(97, 126).mirror()
                        .addBox(-2.6F, -1.5F, -4.0F, 5.2F, 2.5F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBrTalon1 = legBrFoot.addOrReplaceChild("leg_br_talon1",
                CubeListBuilder.create().texOffs(210, 176).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon2 = legBrFoot.addOrReplaceChild("leg_br_talon2",
                CubeListBuilder.create().texOffs(166, 176).mirror()
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon3 = legBrFoot.addOrReplaceChild("leg_br_talon3",
                CubeListBuilder.create().texOffs(217, 176).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition saddle = body.addOrReplaceChild("saddle",
                CubeListBuilder.create().texOffs(346, 51)
                        .addBox(-4.8F, -0.9F, -5.6F, 9.6F, 1.8F, 11.2F),
                PartPose.offset(0.0F, 6.2F, 1.5F));
        PartDefinition saddlePommel = saddle.addOrReplaceChild("saddle_pommel",
                CubeListBuilder.create().texOffs(143, 155)
                        .addBox(-1.7F, -2.4F, -1.7F, 3.4F, 2.6F, 3.4F),
                PartPose.offset(0.0F, 0.9F, -5.0F));
        PartDefinition saddleCantle = saddle.addOrReplaceChild("saddle_cantle",
                CubeListBuilder.create().texOffs(240, 155)
                        .addBox(-2.0F, -2.0F, -1.3F, 4.0F, 2.2F, 2.6F),
                PartPose.offset(0.0F, 0.9F, 5.0F));
        PartDefinition saddleStrapL = body.addOrReplaceChild("saddle_strap_l",
                CubeListBuilder.create().texOffs(455, 126)
                        .addBox(-0.6F, -7.6F, -4.6F, 1.1F, 9.6F, 9.2F),
                PartPose.offset(5.6F, 0.0F, 1.5F));
        PartDefinition saddleStrapR = body.addOrReplaceChild("saddle_strap_r",
                CubeListBuilder.create().texOffs(477, 126).mirror()
                        .addBox(-0.6F, -7.6F, -4.6F, 1.1F, 9.6F, 9.2F),
                PartPose.offset(-5.6F, 0.0F, 1.5F));
        PartDefinition armorChest = body.addOrReplaceChild("armor_chest",
                CubeListBuilder.create().texOffs(373, 0)
                        .addBox(-8.5F, -7.1F, -8.1F, 17.0F, 14.2F, 9.1F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition armorTorso = body.addOrReplaceChild("armor_torso",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.7F, -6.7F, -9.3F, 13.4F, 13.4F, 18.1F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition armorHips = body.addOrReplaceChild("armor_hips",
                CubeListBuilder.create().texOffs(107, 51)
                        .addBox(-7.5F, -6.9F, -1.3F, 15.0F, 13.8F, 9.3F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition armorCollar = body.addOrReplaceChild("armor_collar",
                CubeListBuilder.create().texOffs(0, 87)
                        .addBox(-6.2F, -1.3F, -2.8F, 12.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 3.6F, -7.6F, 0.45F, 0.00F, 0.00F));
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(131, 87)
                        .addBox(0.0F, -1.9F, -2.0F, 15.0F, 3.8F, 4.0F),
                PartPose.offsetAndRotation(6.2F, 4.4F, -7.6F, 0.00F, -0.30F, 0.52F));
        PartDefinition wingLFore = wingL.addOrReplaceChild("wing_l_fore",
                CubeListBuilder.create().texOffs(270, 87)
                        .addBox(0.0F, -1.5F, -1.6F, 14.0F, 3.0F, 3.2F),
                PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.00F, -0.22F, -0.92F));
        PartDefinition wingLClaw = wingLFore.addOrReplaceChild("wing_l_claw",
                CubeListBuilder.create().texOffs(395, 155)
                        .addBox(0.0F, -0.7F, -0.7F, 1.4F, 1.4F, 5.4F),
                PartPose.offsetAndRotation(14.0F, 0.0F, -0.4F, -0.80F, 0.00F, 0.00F));
        PartDefinition wingLF1 = wingLFore.addOrReplaceChild("wing_l_f1",
                CubeListBuilder.create().texOffs(346, 126)
                        .addBox(0.0F, -0.6F, -0.6F, 21.5F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.00F, 0.02F, -0.05F));
        PartDefinition wingLF2 = wingLFore.addOrReplaceChild("wing_l_f2",
                CubeListBuilder.create().texOffs(0, 155)
                        .addBox(0.0F, -0.5F, -0.5F, 18.5F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.00F, -0.52F, -0.10F));
        PartDefinition wingLF3 = wingLFore.addOrReplaceChild("wing_l_f3",
                CubeListBuilder.create().texOffs(158, 155)
                        .addBox(0.0F, -0.5F, -0.5F, 14.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.00F, -1.06F, -0.15F));
        PartDefinition wingLM1 = wingLFore.addOrReplaceChild("wing_l_m1",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(0.0F, -0.2F, 0.8F, 19.5F, 0.4F, 13.0F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 0.0F, 0.30F, -0.16F, 0.00F));
        PartDefinition wingLM2 = wingLFore.addOrReplaceChild("wing_l_m2",
                CubeListBuilder.create().texOffs(257, 0)
                        .addBox(0.0F, -0.2F, 0.5F, 16.5F, 0.4F, 12.0F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 0.0F, 0.46F, -0.64F, 0.00F));
        PartDefinition wingLM3 = wingLFore.addOrReplaceChild("wing_l_m3",
                CubeListBuilder.create().texOffs(254, 51)
                        .addBox(0.0F, -0.2F, 0.2F, 12.5F, 0.4F, 10.0F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 0.0F, 0.62F, -1.10F, 0.00F));
        PartDefinition wingLM4 = wingLFore.addOrReplaceChild("wing_l_m4",
                CubeListBuilder.create().texOffs(426, 0)
                        .addBox(0.0F, -0.2F, -2.4F, 15.0F, 0.4F, 11.0F),
                PartPose.offsetAndRotation(1.3F, 0.0F, 0.0F, 0.16F, 0.48F, 0.00F));
        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(170, 87).mirror()
                        .addBox(0.0F, -1.9F, -2.0F, 15.0F, 3.8F, 4.0F),
                PartPose.offsetAndRotation(-6.2F, 4.4F, -7.6F, 0.00F, 0.30F, -0.52F));
        PartDefinition wingRFore = wingR.addOrReplaceChild("wing_r_fore",
                CubeListBuilder.create().texOffs(305, 87).mirror()
                        .addBox(0.0F, -1.5F, -1.6F, 14.0F, 3.0F, 3.2F),
                PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, 0.00F, 0.22F, 0.92F));
        PartDefinition wingRClaw = wingRFore.addOrReplaceChild("wing_r_claw",
                CubeListBuilder.create().texOffs(410, 155).mirror()
                        .addBox(0.0F, -0.7F, -0.7F, 1.4F, 1.4F, 5.4F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, -0.4F, -0.80F, 0.00F, 0.00F));
        PartDefinition wingRF1 = wingRFore.addOrReplaceChild("wing_r_f1",
                CubeListBuilder.create().texOffs(392, 126).mirror()
                        .addBox(0.0F, -0.6F, -0.6F, 21.5F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.00F, -0.02F, 0.05F));
        PartDefinition wingRF2 = wingRFore.addOrReplaceChild("wing_r_f2",
                CubeListBuilder.create().texOffs(40, 155).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 18.5F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.00F, 0.52F, 0.10F));
        PartDefinition wingRF3 = wingRFore.addOrReplaceChild("wing_r_f3",
                CubeListBuilder.create().texOffs(190, 155).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 14.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.00F, 1.06F, 0.15F));
        PartDefinition wingRM1 = wingRFore.addOrReplaceChild("wing_r_m1",
                CubeListBuilder.create().texOffs(130, 0).mirror()
                        .addBox(0.0F, -0.2F, 0.8F, 19.5F, 0.4F, 13.0F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, -0.30F, 0.16F, 0.00F));
        PartDefinition wingRM2 = wingRFore.addOrReplaceChild("wing_r_m2",
                CubeListBuilder.create().texOffs(315, 0).mirror()
                        .addBox(0.0F, -0.2F, 0.5F, 16.5F, 0.4F, 12.0F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, -0.46F, 0.64F, 0.00F));
        PartDefinition wingRM3 = wingRFore.addOrReplaceChild("wing_r_m3",
                CubeListBuilder.create().texOffs(300, 51).mirror()
                        .addBox(0.0F, -0.2F, 0.2F, 12.5F, 0.4F, 10.0F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, -0.62F, 1.10F, 0.00F));
        PartDefinition wingRM4 = wingRFore.addOrReplaceChild("wing_r_m4",
                CubeListBuilder.create().texOffs(0, 51).mirror()
                        .addBox(0.0F, -0.2F, -2.4F, 15.0F, 0.4F, 11.0F),
                PartPose.offsetAndRotation(-1.3F, 0.0F, 0.0F, -0.16F, -0.48F, 0.00F));
        return mesh;
    }
}
