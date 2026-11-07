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
                CubeListBuilder.create().texOffs(0, 51)
                        .addBox(-7.8F, -6.2F, -7.8F, 15.6F, 12.4F, 8.8F),
                PartPose.offsetAndRotation(0.0F, 0.8F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition torso = body.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(64, 0)
                        .addBox(-6.2F, -6.2F, -8.8F, 12.4F, 12.4F, 17.6F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition waist = body.addOrReplaceChild("waist",
                CubeListBuilder.create().texOffs(396, 51)
                        .addBox(-5.2F, -5.0F, -3.6F, 10.4F, 10.0F, 7.2F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition hips = body.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(50, 51)
                        .addBox(-7.0F, -6.4F, -0.8F, 14.0F, 12.8F, 8.8F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(327, 82)
                        .addBox(-3.5F, -4.0F, -5.4F, 7.0F, 8.0F, 5.4F),
                PartPose.offsetAndRotation(0.0F, 5.6F, -10.4F, 1.16F, 0.00F, 0.00F));
        PartDefinition neck1Spike = neck1.addOrReplaceChild("neck1_spike",
                CubeListBuilder.create().texOffs(306, 150)
                        .addBox(-0.7F, -3.2F, -4.4F, 1.4F, 3.4F, 4.9F),
                PartPose.offset(0.0F, -3.7F, -0.8F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 121)
                        .addBox(-3.1F, -3.6F, -4.8F, 6.2F, 7.2F, 4.8F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.4F, -0.48F, 0.00F, 0.00F));
        PartDefinition neck2Spike = neck2.addOrReplaceChild("neck2_spike",
                CubeListBuilder.create().texOffs(335, 150)
                        .addBox(-0.7F, -3.2F, -3.9F, 1.4F, 3.4F, 4.3F),
                PartPose.offset(0.0F, -3.3F, -0.8F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(156, 121)
                        .addBox(-2.7F, -3.2F, -4.4F, 5.4F, 6.4F, 4.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.8F, -0.38F, 0.00F, 0.00F));
        PartDefinition neck3Spike = neck3.addOrReplaceChild("neck3_spike",
                CubeListBuilder.create().texOffs(408, 150)
                        .addBox(-0.7F, -3.2F, -3.6F, 1.4F, 3.4F, 4.0F),
                PartPose.offset(0.0F, -2.9F, -0.8F));
        PartDefinition neck4 = neck3.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(276, 121)
                        .addBox(-2.4F, -2.9F, -4.0F, 4.8F, 5.8F, 4.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.4F, -0.24F, 0.00F, 0.00F));
        PartDefinition neck4Spike = neck4.addOrReplaceChild("neck4_spike",
                CubeListBuilder.create().texOffs(434, 150)
                        .addBox(-0.7F, -3.2F, -3.3F, 1.4F, 3.4F, 3.6F),
                PartPose.offset(0.0F, -2.6F, -0.8F));
        PartDefinition neck5 = neck4.addOrReplaceChild("neck5",
                CubeListBuilder.create().texOffs(385, 121)
                        .addBox(-2.1F, -2.6F, -3.6F, 4.2F, 5.2F, 3.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, -0.16F, 0.00F, 0.00F));
        PartDefinition neck5Spike = neck5.addOrReplaceChild("neck5_spike",
                CubeListBuilder.create().texOffs(445, 150)
                        .addBox(-0.7F, -3.2F, -3.0F, 1.4F, 3.4F, 3.2F),
                PartPose.offset(0.0F, -2.3F, -0.8F));
        PartDefinition bellyChest = body.addOrReplaceChild("belly_chest",
                CubeListBuilder.create().texOffs(357, 51)
                        .addBox(-5.4F, -7.4F, -7.2F, 10.8F, 1.5F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition bellyTorso = body.addOrReplaceChild("belly_torso",
                CubeListBuilder.create().texOffs(302, 0)
                        .addBox(-4.8F, -7.0F, -8.4F, 9.6F, 1.5F, 16.8F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bellyWaist = body.addOrReplaceChild("belly_waist",
                CubeListBuilder.create().texOffs(171, 82)
                        .addBox(-4.0F, -5.8F, -3.4F, 8.0F, 1.4F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition bellyHips = body.addOrReplaceChild("belly_hips",
                CubeListBuilder.create().texOffs(318, 51)
                        .addBox(-5.2F, -7.2F, -0.6F, 10.4F, 1.5F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition ridgeChest = body.addOrReplaceChild("ridge_chest",
                CubeListBuilder.create().texOffs(446, 121)
                        .addBox(-1.0F, -8.4F, -7.4F, 2.0F, 2.8F, 8.2F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition ridgeTorso = body.addOrReplaceChild("ridge_torso",
                CubeListBuilder.create().texOffs(403, 82)
                        .addBox(-1.0F, -8.0F, -8.6F, 2.0F, 3.2F, 17.2F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition ridgeWaist = body.addOrReplaceChild("ridge_waist",
                CubeListBuilder.create().texOffs(111, 150)
                        .addBox(-0.8F, -6.6F, -3.4F, 1.6F, 2.6F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition ridgeHips = body.addOrReplaceChild("ridge_hips",
                CubeListBuilder.create().texOffs(467, 121)
                        .addBox(-0.9F, -8.0F, -0.6F, 1.8F, 2.8F, 8.6F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition scapulaL = chest.addOrReplaceChild("scapula_l",
                CubeListBuilder.create().texOffs(220, 150)
                        .addBox(-0.9F, 0.0F, -2.8F, 1.8F, 6.2F, 2.8F),
                PartPose.offsetAndRotation(7.0F, 5.2F, -0.6F, 0.00F, 0.00F, -0.55F));
        PartDefinition scapulaL2 = chest.addOrReplaceChild("scapula_l2",
                CubeListBuilder.create().texOffs(360, 150)
                        .addBox(-0.8F, 0.0F, -2.2F, 1.6F, 5.2F, 2.4F),
                PartPose.offsetAndRotation(8.2F, 4.2F, 2.2F, 0.00F, 0.00F, -0.75F));
        PartDefinition scapulaR = chest.addOrReplaceChild("scapula_r",
                CubeListBuilder.create().texOffs(230, 150).mirror()
                        .addBox(-0.9F, 0.0F, -2.8F, 1.8F, 6.2F, 2.8F),
                PartPose.offsetAndRotation(-7.0F, 5.2F, -0.6F, 0.00F, 0.00F, 0.55F));
        PartDefinition scapulaR2 = chest.addOrReplaceChild("scapula_r2",
                CubeListBuilder.create().texOffs(369, 150).mirror()
                        .addBox(-0.8F, 0.0F, -2.2F, 1.6F, 5.2F, 2.4F),
                PartPose.offsetAndRotation(-8.2F, 4.2F, 2.2F, 0.00F, 0.00F, 0.75F));
        PartDefinition hipSpurL = hips.addOrReplaceChild("hip_spur_l",
                CubeListBuilder.create().texOffs(455, 150)
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(6.6F, 3.6F, 2.6F, 0.00F, 0.00F, -0.90F));
        PartDefinition hipSpurR = hips.addOrReplaceChild("hip_spur_r",
                CubeListBuilder.create().texOffs(463, 150).mirror()
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(-6.6F, 3.6F, 2.6F, 0.00F, 0.00F, 0.90F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(31, 82)
                        .addBox(-3.6F, -3.6F, 0.0F, 7.2F, 7.2F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -1.6F, 13.4F, -0.12F, 0.00F, 0.00F));
        PartDefinition tail1Spike = tail1.addOrReplaceChild("tail1_spike",
                CubeListBuilder.create().texOffs(320, 150)
                        .addBox(-0.6F, -2.8F, -2.4F, 1.2F, 3.0F, 5.6F),
                PartPose.offset(0.0F, -3.3F, 2.2F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(141, 82)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 8.4F, -0.03F, 0.00F, 0.00F));
        PartDefinition tail2Spike = tail2.addOrReplaceChild("tail2_spike",
                CubeListBuilder.create().texOffs(420, 150)
                        .addBox(-0.6F, -2.6F, -2.2F, 1.1F, 2.8F, 5.2F),
                PartPose.offset(0.0F, -2.8F, 2.2F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(299, 82)
                        .addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.1F, 8.4F, 0.05F, 0.00F, 0.00F));
        PartDefinition tail3Spike = tail3.addOrReplaceChild("tail3_spike",
                CubeListBuilder.create().texOffs(471, 150)
                        .addBox(-0.5F, -2.4F, -2.0F, 1.0F, 2.6F, 4.8F),
                PartPose.offset(0.0F, -2.3F, 2.2F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(442, 82)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.10F, 0.00F, 0.00F));
        PartDefinition tail4Spike = tail4.addOrReplaceChild("tail4_spike",
                CubeListBuilder.create().texOffs(0, 168)
                        .addBox(-0.5F, -2.0F, -1.8F, 0.9F, 2.2F, 4.4F),
                PartPose.offset(0.0F, -1.8F, 2.2F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(177, 121)
                        .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.13F, 0.00F, 0.00F));
        PartDefinition tail5Spike = tail5.addOrReplaceChild("tail5_spike",
                CubeListBuilder.create().texOffs(50, 168)
                        .addBox(-0.4F, -1.7F, -1.6F, 0.8F, 1.8F, 4.0F),
                PartPose.offset(0.0F, -1.3F, 2.2F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(76, 150)
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.14F, 0.00F, 0.00F));
        PartDefinition tail7 = tail6.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(289, 150)
                        .addBox(-0.7F, -0.7F, 0.0F, 1.4F, 1.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.4F, 0.14F, 0.00F, 0.00F));
        PartDefinition spadeTop = tail7.addOrReplaceChild("spade_top",
                CubeListBuilder.create().texOffs(143, 150)
                        .addBox(-0.7F, -0.2F, 0.0F, 1.4F, 3.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 5.8F, -0.34F, 0.00F, 0.00F));
        PartDefinition spadeBot = tail7.addOrReplaceChild("spade_bot",
                CubeListBuilder.create().texOffs(240, 150)
                        .addBox(-0.7F, 0.0F, 0.0F, 1.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 0.2F, 6.0F, 0.05F, 0.00F, 0.00F));
        PartDefinition spadeLeft = tail7.addOrReplaceChild("spade_left",
                CubeListBuilder.create().texOffs(115, 168)
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 5.4F, 0.00F, 0.00F, -0.70F));
        PartDefinition spadeRight = tail7.addOrReplaceChild("spade_right",
                CubeListBuilder.create().texOffs(126, 168).mirror()
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 5.4F, 0.00F, 0.00F, 0.70F));
        PartDefinition head = neck5.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(272, 82)
                        .addBox(-3.3F, -2.7F, -6.4F, 6.6F, 5.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -3.2F, -0.16F, 0.00F, 0.00F));
        PartDefinition skullTop = head.addOrReplaceChild("skull_top",
                CubeListBuilder.create().texOffs(111, 121)
                        .addBox(-2.7F, -0.9F, -5.6F, 5.4F, 1.9F, 5.8F),
                PartPose.offset(0.0F, 2.6F, -1.0F));
        PartDefinition browL = head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(484, 150)
                        .addBox(-0.6F, -0.6F, -2.4F, 1.1F, 1.1F, 4.8F),
                PartPose.offsetAndRotation(2.5F, 1.8F, -3.6F, 0.00F, 0.00F, -0.25F));
        PartDefinition browR = head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(497, 150).mirror()
                        .addBox(-0.6F, -0.6F, -2.4F, 1.1F, 1.1F, 4.8F),
                PartPose.offsetAndRotation(-2.5F, 1.8F, -3.6F, 0.00F, 0.00F, 0.25F));
        PartDefinition muzzle = head.addOrReplaceChild("muzzle",
                CubeListBuilder.create().texOffs(134, 121)
                        .addBox(-2.4F, -1.9F, -5.8F, 4.8F, 3.6F, 5.8F),
                PartPose.offset(0.0F, -0.7F, -6.0F));
        PartDefinition nostrilL = muzzle.addOrReplaceChild("nostril_l",
                CubeListBuilder.create().texOffs(221, 168)
                        .addBox(-0.5F, -0.8F, -0.7F, 0.9F, 1.5F, 1.3F),
                PartPose.offset(1.1F, 0.7F, -4.7F));
        PartDefinition nostrilR = muzzle.addOrReplaceChild("nostril_r",
                CubeListBuilder.create().texOffs(226, 168)
                        .addBox(-0.5F, -0.8F, -0.7F, 0.9F, 1.5F, 1.3F),
                PartPose.offset(-1.1F, 0.7F, -4.7F));
        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(468, 82)
                        .addBox(-2.4F, 0.0F, -7.4F, 4.8F, 1.8F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 1.9F, -1.2F, 0.05F, 0.00F, 0.00F));
        PartDefinition teethL = head.addOrReplaceChild("teeth_l",
                CubeListBuilder.create().texOffs(79, 168)
                        .addBox(-0.4F, 0.0F, -0.9F, 0.8F, 1.5F, 2.4F),
                PartPose.offset(1.9F, 0.7F, -5.6F));
        PartDefinition teethR = head.addOrReplaceChild("teeth_r",
                CubeListBuilder.create().texOffs(86, 168)
                        .addBox(-0.4F, 0.0F, -0.9F, 0.8F, 1.5F, 2.4F),
                PartPose.offset(-1.9F, 0.7F, -5.6F));
        PartDefinition fangs = jaw.addOrReplaceChild("fangs",
                CubeListBuilder.create().texOffs(347, 150)
                        .addBox(-1.8F, 0.0F, -1.0F, 3.6F, 1.4F, 2.2F),
                PartPose.offset(0.0F, 0.0F, -6.4F));
        PartDefinition eyeL = head.addOrReplaceChild("eye_l",
                CubeListBuilder.create().texOffs(179, 168)
                        .addBox(-0.3F, -0.5F, -1.2F, 0.7F, 1.0F, 2.4F),
                PartPose.offset(2.9F, 0.5F, -4.4F));
        PartDefinition eyeR = head.addOrReplaceChild("eye_r",
                CubeListBuilder.create().texOffs(186, 168).mirror()
                        .addBox(-0.3F, -0.5F, -1.2F, 0.7F, 1.0F, 2.4F),
                PartPose.offset(-2.9F, 0.5F, -4.4F));
        PartDefinition hornL1 = head.addOrReplaceChild("horn_l1",
                CubeListBuilder.create().texOffs(255, 150)
                        .addBox(-0.7F, -0.7F, 0.0F, 1.3F, 1.4F, 6.8F),
                PartPose.offsetAndRotation(2.1F, 1.9F, 1.4F, 0.92F, -0.14F, 0.00F));
        PartDefinition hornL2 = hornL1.addOrReplaceChild("horn_l2",
                CubeListBuilder.create().texOffs(24, 168)
                        .addBox(-0.4F, -0.5F, 0.0F, 0.9F, 1.0F, 5.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 6.8F, -0.40F, -0.10F, 0.00F));
        PartDefinition hornL3 = hornL2.addOrReplaceChild("horn_l3",
                CubeListBuilder.create().texOffs(231, 168)
                        .addBox(-0.2F, -0.2F, 0.0F, 0.4F, 0.5F, 3.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.2F, -0.44F, 0.00F, 0.00F));
        PartDefinition hornR1 = head.addOrReplaceChild("horn_r1",
                CubeListBuilder.create().texOffs(272, 150).mirror()
                        .addBox(-0.7F, -0.7F, 0.0F, 1.3F, 1.4F, 6.8F),
                PartPose.offsetAndRotation(-2.1F, 1.9F, 1.4F, 0.92F, 0.14F, 0.00F));
        PartDefinition hornR2 = hornR1.addOrReplaceChild("horn_r2",
                CubeListBuilder.create().texOffs(37, 168).mirror()
                        .addBox(-0.4F, -0.5F, 0.0F, 0.9F, 1.0F, 5.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 6.8F, -0.40F, 0.10F, 0.00F));
        PartDefinition hornR3 = hornR2.addOrReplaceChild("horn_r3",
                CubeListBuilder.create().texOffs(239, 168).mirror()
                        .addBox(-0.2F, -0.2F, 0.0F, 0.4F, 0.5F, 3.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.2F, -0.44F, 0.00F, 0.00F));
        PartDefinition crestL = head.addOrReplaceChild("crest_l",
                CubeListBuilder.create().texOffs(12, 168)
                        .addBox(-0.4F, 0.0F, -0.8F, 0.8F, 4.6F, 1.6F),
                PartPose.offsetAndRotation(1.8F, 1.2F, 2.6F, 1.00F, -0.75F, 0.00F));
        PartDefinition crestR = head.addOrReplaceChild("crest_r",
                CubeListBuilder.create().texOffs(18, 168).mirror()
                        .addBox(-0.4F, 0.0F, -0.8F, 0.8F, 4.6F, 1.6F),
                PartPose.offsetAndRotation(-1.8F, 1.2F, 2.6F, 1.00F, 0.75F, 0.00F));
        PartDefinition cheekL = head.addOrReplaceChild("cheek_l",
                CubeListBuilder.create().texOffs(61, 168)
                        .addBox(-0.3F, -1.2F, 0.0F, 0.7F, 2.4F, 3.2F),
                PartPose.offsetAndRotation(3.1F, -0.5F, 0.8F, 0.00F, -1.45F, 0.35F));
        PartDefinition cheekR = head.addOrReplaceChild("cheek_r",
                CubeListBuilder.create().texOffs(70, 168).mirror()
                        .addBox(-0.3F, -1.2F, 0.0F, 0.7F, 2.4F, 3.2F),
                PartPose.offsetAndRotation(-3.1F, -0.5F, 0.8F, 0.00F, 1.45F, -0.35F));
        PartDefinition barbelL = jaw.addOrReplaceChild("barbel_l",
                CubeListBuilder.create().texOffs(93, 168)
                        .addBox(-0.3F, 0.0F, 0.0F, 0.6F, 3.0F, 0.6F),
                PartPose.offsetAndRotation(1.6F, 0.3F, -1.0F, -0.50F, 0.00F, 0.25F));
        PartDefinition barbelR = jaw.addOrReplaceChild("barbel_r",
                CubeListBuilder.create().texOffs(96, 168).mirror()
                        .addBox(-0.3F, 0.0F, 0.0F, 0.6F, 3.0F, 0.6F),
                PartPose.offsetAndRotation(-1.6F, 0.3F, -1.0F, -0.50F, 0.00F, -0.25F));
        PartDefinition legFlThigh = body.addOrReplaceChild("leg_fl_thigh",
                CubeListBuilder.create().texOffs(353, 82)
                        .addBox(-2.5F, -9.5F, -3.4F, 5.0F, 10.5F, 7.0F),
                PartPose.offsetAndRotation(6.0F, -5.0F, -8.6F, 0.14F, 0.00F, 0.00F));
        PartDefinition legFlShin = legFlThigh.addOrReplaceChild("leg_fl_shin",
                CubeListBuilder.create().texOffs(242, 121)
                        .addBox(-1.9F, -9.0F, -2.2F, 3.8F, 9.0F, 4.4F),
                PartPose.offsetAndRotation(0.0F, -9.5F, 0.4F, -0.34F, 0.00F, 0.00F));
        PartDefinition legFlFoot = legFlShin.addOrReplaceChild("leg_fl_foot",
                CubeListBuilder.create().texOffs(200, 121)
                        .addBox(-2.3F, -1.4F, -3.6F, 4.6F, 2.4F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -0.2F, 0.20F, 0.00F, 0.00F));
        PartDefinition legFlTalon1 = legFlFoot.addOrReplaceChild("leg_fl_talon1",
                CubeListBuilder.create().texOffs(193, 168)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon2 = legFlFoot.addOrReplaceChild("leg_fl_talon2",
                CubeListBuilder.create().texOffs(165, 168)
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon3 = legFlFoot.addOrReplaceChild("leg_fl_talon3",
                CubeListBuilder.create().texOffs(200, 168)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrThigh = body.addOrReplaceChild("leg_fr_thigh",
                CubeListBuilder.create().texOffs(378, 82).mirror()
                        .addBox(-2.5F, -9.5F, -3.4F, 5.0F, 10.5F, 7.0F),
                PartPose.offsetAndRotation(-6.0F, -5.0F, -8.6F, 0.14F, 0.00F, 0.00F));
        PartDefinition legFrShin = legFrThigh.addOrReplaceChild("leg_fr_shin",
                CubeListBuilder.create().texOffs(259, 121).mirror()
                        .addBox(-1.9F, -9.0F, -2.2F, 3.8F, 9.0F, 4.4F),
                PartPose.offsetAndRotation(0.0F, -9.5F, 0.4F, -0.34F, 0.00F, 0.00F));
        PartDefinition legFrFoot = legFrShin.addOrReplaceChild("leg_fr_foot",
                CubeListBuilder.create().texOffs(221, 121).mirror()
                        .addBox(-2.3F, -1.4F, -3.6F, 4.6F, 2.4F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -0.2F, 0.20F, 0.00F, 0.00F));
        PartDefinition legFrTalon1 = legFrFoot.addOrReplaceChild("leg_fr_talon1",
                CubeListBuilder.create().texOffs(207, 168).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon2 = legFrFoot.addOrReplaceChild("leg_fr_talon2",
                CubeListBuilder.create().texOffs(172, 168).mirror()
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon3 = legFrFoot.addOrReplaceChild("leg_fr_talon3",
                CubeListBuilder.create().texOffs(214, 168).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlThigh = body.addOrReplaceChild("leg_bl_thigh",
                CubeListBuilder.create().texOffs(469, 51)
                        .addBox(-3.6F, -11.0F, -3.8F, 7.2F, 12.0F, 8.0F),
                PartPose.offsetAndRotation(6.8F, -4.6F, 10.0F, -0.30F, 0.00F, 0.00F));
        PartDefinition legBlShin = legBlThigh.addOrReplaceChild("leg_bl_shin",
                CubeListBuilder.create().texOffs(69, 121)
                        .addBox(-2.3F, -9.5F, -2.6F, 4.6F, 9.5F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.62F, 0.00F, 0.00F));
        PartDefinition legBlFoot = legBlShin.addOrReplaceChild("leg_bl_foot",
                CubeListBuilder.create().texOffs(23, 121)
                        .addBox(-2.6F, -1.5F, -4.0F, 5.2F, 2.5F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBlTalon1 = legBlFoot.addOrReplaceChild("leg_bl_talon1",
                CubeListBuilder.create().texOffs(137, 168)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon2 = legBlFoot.addOrReplaceChild("leg_bl_talon2",
                CubeListBuilder.create().texOffs(99, 168)
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon3 = legBlFoot.addOrReplaceChild("leg_bl_talon3",
                CubeListBuilder.create().texOffs(144, 168)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrThigh = body.addOrReplaceChild("leg_br_thigh",
                CubeListBuilder.create().texOffs(0, 82).mirror()
                        .addBox(-3.6F, -11.0F, -3.8F, 7.2F, 12.0F, 8.0F),
                PartPose.offsetAndRotation(-6.8F, -4.6F, 10.0F, -0.30F, 0.00F, 0.00F));
        PartDefinition legBrShin = legBrThigh.addOrReplaceChild("leg_br_shin",
                CubeListBuilder.create().texOffs(90, 121).mirror()
                        .addBox(-2.3F, -9.5F, -2.6F, 4.6F, 9.5F, 5.2F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.62F, 0.00F, 0.00F));
        PartDefinition legBrFoot = legBrShin.addOrReplaceChild("leg_br_foot",
                CubeListBuilder.create().texOffs(46, 121).mirror()
                        .addBox(-2.6F, -1.5F, -4.0F, 5.2F, 2.5F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBrTalon1 = legBrFoot.addOrReplaceChild("leg_br_talon1",
                CubeListBuilder.create().texOffs(151, 168).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon2 = legBrFoot.addOrReplaceChild("leg_br_talon2",
                CubeListBuilder.create().texOffs(107, 168).mirror()
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon3 = legBrFoot.addOrReplaceChild("leg_br_talon3",
                CubeListBuilder.create().texOffs(158, 168).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition saddle = body.addOrReplaceChild("saddle",
                CubeListBuilder.create().texOffs(193, 51)
                        .addBox(-4.8F, -0.9F, -5.6F, 9.6F, 1.8F, 11.2F),
                PartPose.offset(0.0F, 6.2F, 1.5F));
        PartDefinition saddlePommel = saddle.addOrReplaceChild("saddle_pommel",
                CubeListBuilder.create().texOffs(96, 150)
                        .addBox(-1.7F, -2.4F, -1.7F, 3.4F, 2.6F, 3.4F),
                PartPose.offset(0.0F, 0.9F, -5.0F));
        PartDefinition saddleCantle = saddle.addOrReplaceChild("saddle_cantle",
                CubeListBuilder.create().texOffs(129, 150)
                        .addBox(-2.0F, -2.0F, -1.3F, 4.0F, 2.2F, 2.6F),
                PartPose.offset(0.0F, 0.9F, 5.0F));
        PartDefinition saddleStrapL = body.addOrReplaceChild("saddle_strap_l",
                CubeListBuilder.create().texOffs(402, 121)
                        .addBox(-0.6F, -7.6F, -4.6F, 1.1F, 9.6F, 9.2F),
                PartPose.offset(5.6F, 0.0F, 1.5F));
        PartDefinition saddleStrapR = body.addOrReplaceChild("saddle_strap_r",
                CubeListBuilder.create().texOffs(424, 121).mirror()
                        .addBox(-0.6F, -7.6F, -4.6F, 1.1F, 9.6F, 9.2F),
                PartPose.offset(-5.6F, 0.0F, 1.5F));
        PartDefinition armorChest = body.addOrReplaceChild("armor_chest",
                CubeListBuilder.create().texOffs(249, 0)
                        .addBox(-8.5F, -7.1F, -8.1F, 17.0F, 14.2F, 9.1F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition armorTorso = body.addOrReplaceChild("armor_torso",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-6.7F, -6.7F, -9.3F, 13.4F, 13.4F, 18.1F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition armorHips = body.addOrReplaceChild("armor_hips",
                CubeListBuilder.create().texOffs(462, 0)
                        .addBox(-7.5F, -6.9F, -1.3F, 15.0F, 13.8F, 9.3F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition armorCollar = body.addOrReplaceChild("armor_collar",
                CubeListBuilder.create().texOffs(432, 51)
                        .addBox(-6.2F, -1.3F, -2.8F, 12.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 3.6F, -7.6F, 0.45F, 0.00F, 0.00F));
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(63, 82)
                        .addBox(0.0F, -1.9F, -2.0F, 15.0F, 3.8F, 4.0F),
                PartPose.offsetAndRotation(6.6F, 5.0F, -7.2F, 0.00F, -0.24F, 0.86F));
        PartDefinition wingLFore = wingL.addOrReplaceChild("wing_l_fore",
                CubeListBuilder.create().texOffs(202, 82)
                        .addBox(0.0F, -1.5F, -1.6F, 14.0F, 3.0F, 3.2F),
                PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.00F, -0.40F, -0.72F));
        PartDefinition wingLClaw = wingLFore.addOrReplaceChild("wing_l_claw",
                CubeListBuilder.create().texOffs(378, 150)
                        .addBox(0.0F, -0.7F, -0.7F, 1.4F, 1.4F, 5.4F),
                PartPose.offsetAndRotation(14.0F, 0.0F, -0.4F, -0.80F, 0.00F, 0.00F));
        PartDefinition wingLF1 = wingLFore.addOrReplaceChild("wing_l_f1",
                CubeListBuilder.create().texOffs(295, 121)
                        .addBox(0.0F, -0.5F, -0.5F, 21.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.00F, -0.10F, -0.05F));
        PartDefinition wingLF2 = wingLFore.addOrReplaceChild("wing_l_f2",
                CubeListBuilder.create().texOffs(0, 150)
                        .addBox(0.0F, -0.5F, -0.5F, 17.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.00F, -0.58F, -0.10F));
        PartDefinition wingLF3 = wingLFore.addOrReplaceChild("wing_l_f3",
                CubeListBuilder.create().texOffs(160, 150)
                        .addBox(0.0F, -0.4F, -0.4F, 13.5F, 0.8F, 0.8F),
                PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.00F, -1.06F, -0.15F));
        PartDefinition wingLM1 = wingLFore.addOrReplaceChild("wing_l_m1",
                CubeListBuilder.create().texOffs(125, 0)
                        .addBox(0.0F, -0.2F, 0.8F, 19.5F, 0.4F, 11.0F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 0.0F, 0.30F, -0.28F, 0.00F));
        PartDefinition wingLM2 = wingLFore.addOrReplaceChild("wing_l_m2",
                CubeListBuilder.create().texOffs(356, 0)
                        .addBox(0.0F, -0.2F, 0.5F, 16.0F, 0.4F, 10.0F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 0.0F, 0.46F, -0.74F, 0.00F));
        PartDefinition wingLM3 = wingLFore.addOrReplaceChild("wing_l_m3",
                CubeListBuilder.create().texOffs(236, 51)
                        .addBox(0.0F, -0.2F, 0.2F, 12.0F, 0.4F, 8.0F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 0.0F, 0.64F, -1.18F, 0.00F));
        PartDefinition wingLM4 = wingLFore.addOrReplaceChild("wing_l_m4",
                CubeListBuilder.create().texOffs(97, 51)
                        .addBox(0.0F, -0.2F, -2.2F, 14.5F, 0.4F, 9.0F),
                PartPose.offsetAndRotation(1.3F, 0.0F, 0.0F, 0.18F, 0.36F, 0.00F));
        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(102, 82).mirror()
                        .addBox(0.0F, -1.9F, -2.0F, 15.0F, 3.8F, 4.0F),
                PartPose.offsetAndRotation(-6.6F, 5.0F, -7.2F, 0.00F, 0.24F, -0.86F));
        PartDefinition wingRFore = wingR.addOrReplaceChild("wing_r_fore",
                CubeListBuilder.create().texOffs(237, 82).mirror()
                        .addBox(0.0F, -1.5F, -1.6F, 14.0F, 3.0F, 3.2F),
                PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, 0.00F, 0.40F, 0.72F));
        PartDefinition wingRClaw = wingRFore.addOrReplaceChild("wing_r_claw",
                CubeListBuilder.create().texOffs(393, 150).mirror()
                        .addBox(0.0F, -0.7F, -0.7F, 1.4F, 1.4F, 5.4F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, -0.4F, -0.80F, 0.00F, 0.00F));
        PartDefinition wingRF1 = wingRFore.addOrReplaceChild("wing_r_f1",
                CubeListBuilder.create().texOffs(340, 121).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 21.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.00F, 0.10F, 0.05F));
        PartDefinition wingRF2 = wingRFore.addOrReplaceChild("wing_r_f2",
                CubeListBuilder.create().texOffs(38, 150).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 17.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.00F, 0.58F, 0.10F));
        PartDefinition wingRF3 = wingRFore.addOrReplaceChild("wing_r_f3",
                CubeListBuilder.create().texOffs(190, 150).mirror()
                        .addBox(0.0F, -0.4F, -0.4F, 13.5F, 0.8F, 0.8F),
                PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.00F, 1.06F, 0.15F));
        PartDefinition wingRM1 = wingRFore.addOrReplaceChild("wing_r_m1",
                CubeListBuilder.create().texOffs(187, 0).mirror()
                        .addBox(0.0F, -0.2F, 0.8F, 19.5F, 0.4F, 11.0F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, -0.30F, 0.28F, 0.00F));
        PartDefinition wingRM2 = wingRFore.addOrReplaceChild("wing_r_m2",
                CubeListBuilder.create().texOffs(409, 0).mirror()
                        .addBox(0.0F, -0.2F, 0.5F, 16.0F, 0.4F, 10.0F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, -0.46F, 0.74F, 0.00F));
        PartDefinition wingRM3 = wingRFore.addOrReplaceChild("wing_r_m3",
                CubeListBuilder.create().texOffs(277, 51).mirror()
                        .addBox(0.0F, -0.2F, 0.2F, 12.0F, 0.4F, 8.0F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 0.0F, -0.64F, 1.18F, 0.00F));
        PartDefinition wingRM4 = wingRFore.addOrReplaceChild("wing_r_m4",
                CubeListBuilder.create().texOffs(145, 51).mirror()
                        .addBox(0.0F, -0.2F, -2.2F, 14.5F, 0.4F, 9.0F),
                PartPose.offsetAndRotation(-1.3F, 0.0F, 0.0F, -0.18F, -0.36F, 0.00F));
        return mesh;
    }
}
