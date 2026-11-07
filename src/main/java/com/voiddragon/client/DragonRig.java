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
                CubeListBuilder.create().texOffs(303, 194)
                        .addBox(-6.4F, -6.0F, -8.0F, 12.8F, 12.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 0.9F, -8.6F, 0.10F, 0.00F, 0.00F));
        PartDefinition torso = body.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(808, 94)
                        .addBox(-5.2F, -5.8F, -9.0F, 10.4F, 11.6F, 18.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition waist = body.addOrReplaceChild("waist",
                CubeListBuilder.create().texOffs(207, 266)
                        .addBox(-4.0F, -4.4F, -3.6F, 8.0F, 8.8F, 7.2F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 8.4F, -0.06F, 0.00F, 0.00F));
        PartDefinition hips = body.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(392, 194)
                        .addBox(-5.8F, -5.6F, -0.8F, 11.6F, 11.2F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -0.5F, 12.6F, -0.10F, 0.00F, 0.00F));
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(664, 266)
                        .addBox(-3.0F, -3.6F, -6.7F, 6.0F, 7.2F, 7.1F),
                PartPose.offsetAndRotation(0.0F, 4.6F, -9.6F, 1.06F, 0.00F, 0.00F));
        PartDefinition neck1Spine = neck1.addOrReplaceChild("neck1_spine",
                CubeListBuilder.create().texOffs(883, 461)
                        .addBox(-0.5F, 0.0F, -1.3F, 1.0F, 3.4F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 3.0F, -3.2F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 327)
                        .addBox(-2.6F, -3.1F, -6.3F, 5.2F, 6.2F, 6.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.8F, -0.28F, 0.00F, 0.00F));
        PartDefinition neck2Spine = neck2.addOrReplaceChild("neck2_spine",
                CubeListBuilder.create().texOffs(894, 461)
                        .addBox(-0.5F, 0.0F, -1.3F, 1.0F, 3.4F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 2.5F, -3.0F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(320, 327)
                        .addBox(-2.3F, -2.7F, -5.8F, 4.6F, 5.4F, 6.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.4F, -0.22F, 0.00F, 0.00F));
        PartDefinition neck3Spine = neck3.addOrReplaceChild("neck3_spine",
                CubeListBuilder.create().texOffs(905, 461)
                        .addBox(-0.5F, 0.0F, -1.3F, 1.0F, 3.4F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 2.1F, -2.8F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck4 = neck3.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(646, 327)
                        .addBox(-2.0F, -2.4F, -5.3F, 4.0F, 4.8F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, -0.16F, 0.00F, 0.00F));
        PartDefinition neck4Spine = neck4.addOrReplaceChild("neck4_spine",
                CubeListBuilder.create().texOffs(916, 461)
                        .addBox(-0.5F, 0.0F, -1.3F, 1.0F, 3.4F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 1.8F, -2.5F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck5 = neck4.addOrReplaceChild("neck5",
                CubeListBuilder.create().texOffs(139, 405)
                        .addBox(-1.8F, -2.1F, -4.9F, 3.6F, 4.2F, 5.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, -0.10F, 0.00F, 0.00F));
        PartDefinition neck5Spine = neck5.addOrReplaceChild("neck5_spine",
                CubeListBuilder.create().texOffs(927, 461)
                        .addBox(-0.5F, 0.0F, -1.3F, 1.0F, 3.4F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 1.5F, -2.3F, 0.62F, 0.00F, 0.00F));
        PartDefinition bellyChest = body.addOrReplaceChild("belly_chest",
                CubeListBuilder.create().texOffs(716, 194)
                        .addBox(-4.4F, -7.2F, -7.4F, 8.8F, 1.6F, 8.2F),
                PartPose.offsetAndRotation(0.0F, 1.3F, -8.6F, 0.10F, 0.00F, 0.00F));
        PartDefinition bellyTorso = body.addOrReplaceChild("belly_torso",
                CubeListBuilder.create().texOffs(204, 194)
                        .addBox(-3.8F, -6.6F, -8.6F, 7.6F, 1.6F, 17.2F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bellyWaist = body.addOrReplaceChild("belly_waist",
                CubeListBuilder.create().texOffs(827, 266)
                        .addBox(-3.0F, -5.2F, -3.4F, 6.0F, 1.4F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 8.4F, -0.06F, 0.00F, 0.00F));
        PartDefinition bellyHips = body.addOrReplaceChild("belly_hips",
                CubeListBuilder.create().texOffs(785, 194)
                        .addBox(-4.2F, -6.4F, -0.6F, 8.4F, 1.5F, 8.6F),
                PartPose.offsetAndRotation(0.0F, -0.5F, 12.6F, -0.10F, 0.00F, 0.00F));
        PartDefinition ridgeChest = body.addOrReplaceChild("ridge_chest",
                CubeListBuilder.create().texOffs(174, 405)
                        .addBox(-1.1F, -7.8F, -7.6F, 2.2F, 3.2F, 8.4F),
                PartPose.offsetAndRotation(0.0F, 1.3F, -8.6F, 0.10F, 0.00F, 0.00F));
        PartDefinition ridgeTorso = body.addOrReplaceChild("ridge_torso",
                CubeListBuilder.create().texOffs(145, 327)
                        .addBox(-1.0F, -7.4F, -8.8F, 2.0F, 3.4F, 17.6F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition ridgeWaist = body.addOrReplaceChild("ridge_waist",
                CubeListBuilder.create().texOffs(221, 461)
                        .addBox(-0.8F, -6.6F, -3.4F, 1.6F, 2.6F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition ridgeHips = body.addOrReplaceChild("ridge_hips",
                CubeListBuilder.create().texOffs(424, 405)
                        .addBox(-0.9F, -8.0F, -0.6F, 1.8F, 2.8F, 8.6F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition scapulaL = chest.addOrReplaceChild("scapula_l",
                CubeListBuilder.create().texOffs(639, 461)
                        .addBox(-0.9F, 0.0F, -2.2F, 1.8F, 4.6F, 2.2F),
                PartPose.offsetAndRotation(6.2F, 4.4F, -0.4F, 0.00F, 0.00F, -0.55F));
        PartDefinition scapulaL2 = chest.addOrReplaceChild("scapula_l2",
                CubeListBuilder.create().texOffs(753, 461)
                        .addBox(-0.8F, 0.0F, -1.8F, 1.6F, 3.8F, 2.0F),
                PartPose.offsetAndRotation(6.9F, 3.2F, 1.9F, 0.00F, 0.00F, -0.75F));
        PartDefinition scapulaR = chest.addOrReplaceChild("scapula_r",
                CubeListBuilder.create().texOffs(656, 461).mirror()
                        .addBox(-0.9F, 0.0F, -2.2F, 1.8F, 4.6F, 2.2F),
                PartPose.offsetAndRotation(-6.2F, 4.4F, -0.4F, 0.00F, 0.00F, 0.55F));
        PartDefinition scapulaR2 = chest.addOrReplaceChild("scapula_r2",
                CubeListBuilder.create().texOffs(768, 461).mirror()
                        .addBox(-0.8F, 0.0F, -1.8F, 1.6F, 3.8F, 2.0F),
                PartPose.offsetAndRotation(-6.9F, 3.2F, 1.9F, 0.00F, 0.00F, 0.75F));
        PartDefinition hipSpurL = hips.addOrReplaceChild("hip_spur_l",
                CubeListBuilder.create().texOffs(698, 461)
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(6.6F, 3.6F, 2.6F, 0.00F, 0.00F, -0.90F));
        PartDefinition hipSpurR = hips.addOrReplaceChild("hip_spur_r",
                CubeListBuilder.create().texOffs(713, 461).mirror()
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(-6.6F, 3.6F, 2.6F, 0.00F, 0.00F, 0.90F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(268, 266)
                        .addBox(-2.9F, -2.9F, 0.0F, 5.8F, 5.8F, 9.6F),
                PartPose.offsetAndRotation(0.0F, -1.9F, 13.0F, -0.16F, 0.00F, 0.00F));
        PartDefinition tail1Spike = tail1.addOrReplaceChild("tail1_spike",
                CubeListBuilder.create().texOffs(472, 461)
                        .addBox(-0.6F, -2.8F, -2.4F, 1.2F, 3.0F, 5.6F),
                PartPose.offset(0.0F, -3.3F, 2.2F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(717, 266)
                        .addBox(-2.4F, -2.4F, 0.0F, 4.8F, 4.8F, 9.2F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 9.6F, -0.02F, 0.00F, 0.00F));
        PartDefinition tail2Spike = tail2.addOrReplaceChild("tail2_spike",
                CubeListBuilder.create().texOffs(673, 461)
                        .addBox(-0.6F, -2.6F, -2.2F, 1.1F, 2.8F, 5.2F),
                PartPose.offset(0.0F, -2.8F, 2.2F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(47, 327)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -0.1F, 9.2F, 0.04F, 0.00F, 0.00F));
        PartDefinition tail3Spike = tail3.addOrReplaceChild("tail3_spike",
                CubeListBuilder.create().texOffs(728, 461)
                        .addBox(-0.5F, -2.4F, -2.0F, 1.0F, 2.6F, 4.8F),
                PartPose.offset(0.0F, -2.3F, 2.2F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(224, 327)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.10F, 0.00F, 0.00F));
        PartDefinition tail4Spike = tail4.addOrReplaceChild("tail4_spike",
                CubeListBuilder.create().texOffs(783, 461)
                        .addBox(-0.5F, -2.0F, -1.8F, 0.9F, 2.2F, 4.4F),
                PartPose.offset(0.0F, -1.8F, 2.2F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(685, 327)
                        .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.13F, 0.00F, 0.00F));
        PartDefinition tail5Spike = tail5.addOrReplaceChild("tail5_spike",
                CubeListBuilder.create().texOffs(938, 461)
                        .addBox(-0.4F, -1.7F, -1.6F, 0.8F, 1.8F, 4.0F),
                PartPose.offset(0.0F, -1.3F, 2.2F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(653, 405)
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.14F, 0.00F, 0.00F));
        PartDefinition tail7 = tail6.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(345, 461)
                        .addBox(-0.7F, -0.7F, 0.0F, 1.4F, 1.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.4F, 0.14F, 0.00F, 0.00F));
        PartDefinition spadeTop = tail7.addOrReplaceChild("spade_top",
                CubeListBuilder.create().texOffs(283, 461)
                        .addBox(-0.7F, -0.2F, 0.0F, 1.4F, 3.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 5.8F, -0.34F, 0.00F, 0.00F));
        PartDefinition spadeBot = tail7.addOrReplaceChild("spade_bot",
                CubeListBuilder.create().texOffs(316, 461)
                        .addBox(-0.7F, 0.0F, 0.0F, 1.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 0.2F, 6.0F, 0.05F, 0.00F, 0.00F));
        PartDefinition spadeLeft = tail7.addOrReplaceChild("spade_left",
                CubeListBuilder.create().texOffs(122, 497)
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 5.4F, 0.00F, 0.00F, -0.70F));
        PartDefinition spadeRight = tail7.addOrReplaceChild("spade_right",
                CubeListBuilder.create().texOffs(143, 497).mirror()
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 5.4F, 0.00F, 0.00F, 0.70F));
        PartDefinition head = neck5.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(558, 194)
                        .addBox(-4.8F, -3.4F, -9.4F, 9.6F, 6.8F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.2F, -3.6F, -0.30F, 0.00F, 0.00F));
        PartDefinition muzzle = head.addOrReplaceChild("muzzle",
                CubeListBuilder.create().texOffs(77, 266)
                        .addBox(-3.2F, -2.0F, -10.4F, 6.4F, 4.0F, 10.4F),
                PartPose.offsetAndRotation(0.0F, -1.3F, -7.4F, 0.04F, 0.00F, 0.00F));
        PartDefinition nasalBridge = head.addOrReplaceChild("nasal_bridge",
                CubeListBuilder.create().texOffs(437, 327)
                        .addBox(-2.8F, -0.7F, -5.0F, 5.6F, 1.4F, 5.0F),
                PartPose.offset(0.0F, 1.9F, -5.0F));
        PartDefinition snoutTip = muzzle.addOrReplaceChild("snout_tip",
                CubeListBuilder.create().texOffs(620, 405)
                        .addBox(-2.4F, -1.6F, -3.0F, 4.8F, 3.2F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.8F, -0.10F, 0.00F, 0.00F));
        PartDefinition nostrilL = snoutTip.addOrReplaceChild("nostril_l",
                CubeListBuilder.create().texOffs(324, 497)
                        .addBox(-0.1F, -0.3F, -0.5F, 0.3F, 0.7F, 1.0F),
                PartPose.offset(2.1F, 0.2F, -1.4F));
        PartDefinition nostrilR = snoutTip.addOrReplaceChild("nostril_r",
                CubeListBuilder.create().texOffs(331, 497).mirror()
                        .addBox(-0.1F, -0.3F, -0.5F, 0.3F, 0.7F, 1.0F),
                PartPose.offset(-2.1F, 0.2F, -1.4F));
        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(637, 194)
                        .addBox(-3.0F, -2.0F, -13.4F, 6.0F, 2.0F, 13.4F),
                PartPose.offsetAndRotation(0.0F, -2.2F, -3.8F, 0.05F, 0.00F, 0.00F));
        PartDefinition teethL = muzzle.addOrReplaceChild("teeth_l",
                CubeListBuilder.create().texOffs(0, 497)
                        .addBox(-0.3F, -1.1F, -1.0F, 0.6F, 1.1F, 6.0F),
                PartPose.offset(2.0F, -1.9F, -4.4F));
        PartDefinition teethR = muzzle.addOrReplaceChild("teeth_r",
                CubeListBuilder.create().texOffs(27, 497).mirror()
                        .addBox(-0.3F, -1.1F, -1.0F, 0.6F, 1.1F, 6.0F),
                PartPose.offset(-2.0F, -1.9F, -4.4F));
        PartDefinition jawMuscleL = head.addOrReplaceChild("jaw_muscle_l",
                CubeListBuilder.create().texOffs(151, 461)
                        .addBox(-0.7F, -1.8F, -3.6F, 1.4F, 3.6F, 7.2F),
                PartPose.offsetAndRotation(3.5F, -1.4F, -4.0F, 0.00F, 0.00F, 0.18F));
        PartDefinition jawMuscleR = head.addOrReplaceChild("jaw_muscle_r",
                CubeListBuilder.create().texOffs(186, 461).mirror()
                        .addBox(-0.7F, -1.8F, -3.6F, 1.4F, 3.6F, 7.2F),
                PartPose.offsetAndRotation(-3.5F, -1.4F, -4.0F, 0.00F, 0.00F, -0.18F));
        PartDefinition cheekRidgeL = head.addOrReplaceChild("cheek_ridge_l",
                CubeListBuilder.create().texOffs(959, 461)
                        .addBox(-0.4F, -0.4F, -1.2F, 0.8F, 0.8F, 5.0F),
                PartPose.offsetAndRotation(4.2F, 0.4F, -6.6F, 0.00F, -0.22F, -0.55F));
        PartDefinition cheekRidgeR = head.addOrReplaceChild("cheek_ridge_r",
                CubeListBuilder.create().texOffs(984, 461).mirror()
                        .addBox(-0.4F, -0.4F, -1.2F, 0.8F, 0.8F, 5.0F),
                PartPose.offsetAndRotation(-4.2F, 0.4F, -6.6F, 0.00F, 0.22F, 0.55F));
        PartDefinition chin = jaw.addOrReplaceChild("chin",
                CubeListBuilder.create().texOffs(806, 461)
                        .addBox(-1.6F, -0.8F, -1.4F, 3.2F, 1.0F, 1.4F),
                PartPose.offsetAndRotation(0.0F, 0.2F, -12.4F, 0.10F, 0.00F, 0.00F));
        PartDefinition browL = head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(692, 405)
                        .addBox(-1.1F, -0.7F, -4.4F, 2.2F, 1.4F, 7.0F),
                PartPose.offsetAndRotation(3.1F, 1.7F, -5.4F, 0.00F, 0.00F, -0.34F));
        PartDefinition browR = head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(729, 405).mirror()
                        .addBox(-1.1F, -0.7F, -4.4F, 2.2F, 1.4F, 7.0F),
                PartPose.offsetAndRotation(-3.1F, 1.7F, -5.4F, 0.00F, 0.00F, 0.34F));
        PartDefinition eyeL = head.addOrReplaceChild("eye_l",
                CubeListBuilder.create().texOffs(54, 497)
                        .addBox(-0.2F, -0.8F, -2.0F, 0.6F, 1.6F, 4.0F),
                PartPose.offset(4.7F, 0.6F, -5.6F));
        PartDefinition eyeR = head.addOrReplaceChild("eye_r",
                CubeListBuilder.create().texOffs(73, 497).mirror()
                        .addBox(-0.2F, -0.8F, -2.0F, 0.6F, 1.6F, 4.0F),
                PartPose.offset(-4.7F, 0.6F, -5.6F));
        PartDefinition hornL1 = head.addOrReplaceChild("horn_l1",
                CubeListBuilder.create().texOffs(299, 405)
                        .addBox(-0.9F, -1.1F, -2.4F, 1.9F, 2.1F, 9.0F),
                PartPose.offsetAndRotation(2.0F, 1.6F, -6.4F, -0.30F, 0.16F, 0.00F));
        PartDefinition hornR1 = head.addOrReplaceChild("horn_r1",
                CubeListBuilder.create().texOffs(344, 405).mirror()
                        .addBox(-0.9F, -1.1F, -2.4F, 1.9F, 2.1F, 9.0F),
                PartPose.offsetAndRotation(-2.0F, 1.6F, -6.4F, -0.30F, -0.16F, 0.00F));
        PartDefinition legFlThigh = body.addOrReplaceChild("leg_fl_thigh",
                CubeListBuilder.create().texOffs(100, 327)
                        .addBox(-2.2F, -10.4F, -3.0F, 4.4F, 11.4F, 6.4F),
                PartPose.offsetAndRotation(5.9F, -4.4F, -8.4F, 0.16F, 0.00F, 0.00F));
        PartDefinition legFlShin = legFlThigh.addOrReplaceChild("leg_fl_shin",
                CubeListBuilder.create().texOffs(79, 405)
                        .addBox(-1.7F, -9.8F, -2.1F, 3.4F, 9.8F, 4.2F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 0.3F, -0.30F, 0.00F, 0.00F));
        PartDefinition legFlFoot = legFlShin.addOrReplaceChild("leg_fl_foot",
                CubeListBuilder.create().texOffs(864, 327)
                        .addBox(-2.1F, -1.5F, -3.8F, 4.2F, 2.6F, 5.4F),
                PartPose.offsetAndRotation(0.0F, -9.4F, -0.2F, 0.22F, 0.00F, 0.00F));
        PartDefinition legFlTalon1 = legFlFoot.addOrReplaceChild("leg_fl_talon1",
                CubeListBuilder.create().texOffs(272, 497)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon2 = legFlFoot.addOrReplaceChild("leg_fl_talon2",
                CubeListBuilder.create().texOffs(246, 497)
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon3 = legFlFoot.addOrReplaceChild("leg_fl_talon3",
                CubeListBuilder.create().texOffs(285, 497)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrThigh = body.addOrReplaceChild("leg_fr_thigh",
                CubeListBuilder.create().texOffs(400, 327).mirror()
                        .addBox(-1.7F, -10.0F, -2.6F, 3.4F, 11.0F, 5.6F),
                PartPose.offsetAndRotation(-5.9F, -4.4F, -8.4F, 0.16F, 0.00F, 0.00F));
        PartDefinition legFrShin = legFrThigh.addOrReplaceChild("leg_fr_shin",
                CubeListBuilder.create().texOffs(597, 405).mirror()
                        .addBox(-1.3F, -9.4F, -1.6F, 2.6F, 9.4F, 3.2F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 0.3F, -0.30F, 0.00F, 0.00F));
        PartDefinition legFrFoot = legFrShin.addOrReplaceChild("leg_fr_foot",
                CubeListBuilder.create().texOffs(389, 405).mirror()
                        .addBox(-1.7F, -1.3F, -3.4F, 3.4F, 2.3F, 4.8F),
                PartPose.offsetAndRotation(0.0F, -9.4F, -0.2F, 0.22F, 0.00F, 0.00F));
        PartDefinition legFrTalon1 = legFrFoot.addOrReplaceChild("leg_fr_talon1",
                CubeListBuilder.create().texOffs(298, 497).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon2 = legFrFoot.addOrReplaceChild("leg_fr_talon2",
                CubeListBuilder.create().texOffs(259, 497).mirror()
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon3 = legFrFoot.addOrReplaceChild("leg_fr_talon3",
                CubeListBuilder.create().texOffs(311, 497).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlThigh = body.addOrReplaceChild("leg_bl_thigh",
                CubeListBuilder.create().texOffs(146, 266)
                        .addBox(-3.3F, -12.0F, -3.4F, 6.6F, 13.0F, 8.4F),
                PartPose.offsetAndRotation(6.4F, -4.2F, 9.6F, -0.34F, 0.00F, 0.00F));
        PartDefinition legBlShin = legBlThigh.addOrReplaceChild("leg_bl_shin",
                CubeListBuilder.create().texOffs(363, 327)
                        .addBox(-2.1F, -10.2F, -2.5F, 4.2F, 10.2F, 5.0F),
                PartPose.offsetAndRotation(0.0F, -11.6F, 0.0F, 0.66F, 0.00F, 0.00F));
        PartDefinition legBlFoot = legBlShin.addOrReplaceChild("leg_bl_foot",
                CubeListBuilder.create().texOffs(774, 266)
                        .addBox(-3.0F, -1.7F, -4.4F, 6.0F, 2.9F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBlTalon1 = legBlFoot.addOrReplaceChild("leg_bl_talon1",
                CubeListBuilder.create().texOffs(164, 497)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon2 = legBlFoot.addOrReplaceChild("leg_bl_talon2",
                CubeListBuilder.create().texOffs(92, 497)
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon3 = legBlFoot.addOrReplaceChild("leg_bl_talon3",
                CubeListBuilder.create().texOffs(177, 497)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrThigh = body.addOrReplaceChild("leg_br_thigh",
                CubeListBuilder.create().texOffs(611, 266).mirror()
                        .addBox(-2.8F, -11.6F, -3.0F, 5.6F, 12.6F, 7.6F),
                PartPose.offsetAndRotation(-6.4F, -4.2F, 9.6F, -0.34F, 0.00F, 0.00F));
        PartDefinition legBrShin = legBrThigh.addOrReplaceChild("leg_br_shin",
                CubeListBuilder.create().texOffs(110, 405).mirror()
                        .addBox(-1.6F, -9.8F, -2.0F, 3.2F, 9.8F, 4.2F),
                PartPose.offsetAndRotation(0.0F, -11.6F, 0.0F, 0.66F, 0.00F, 0.00F));
        PartDefinition legBrFoot = legBrShin.addOrReplaceChild("leg_br_foot",
                CubeListBuilder.create().texOffs(275, 327).mirror()
                        .addBox(-2.6F, -1.5F, -4.0F, 5.2F, 2.5F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBrTalon1 = legBrFoot.addOrReplaceChild("leg_br_talon1",
                CubeListBuilder.create().texOffs(190, 497).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon2 = legBrFoot.addOrReplaceChild("leg_br_talon2",
                CubeListBuilder.create().texOffs(107, 497).mirror()
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon3 = legBrFoot.addOrReplaceChild("leg_br_talon3",
                CubeListBuilder.create().texOffs(203, 497).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition saddle = body.addOrReplaceChild("saddle",
                CubeListBuilder.create().texOffs(475, 194)
                        .addBox(-4.8F, -0.9F, -5.6F, 9.6F, 1.8F, 11.2F),
                PartPose.offset(0.0F, 6.2F, 1.5F));
        PartDefinition saddlePommel = saddle.addOrReplaceChild("saddle_pommel",
                CubeListBuilder.create().texOffs(122, 461)
                        .addBox(-1.7F, -2.4F, -1.7F, 3.4F, 2.6F, 3.4F),
                PartPose.offset(0.0F, 0.9F, -5.0F));
        PartDefinition saddleCantle = saddle.addOrReplaceChild("saddle_cantle",
                CubeListBuilder.create().texOffs(256, 461)
                        .addBox(-2.0F, -2.0F, -1.3F, 4.0F, 2.2F, 2.6F),
                PartPose.offset(0.0F, 0.9F, 5.0F));
        PartDefinition saddleStrapL = body.addOrReplaceChild("saddle_strap_l",
                CubeListBuilder.create().texOffs(217, 405)
                        .addBox(-0.6F, -7.6F, -4.6F, 1.1F, 9.6F, 9.2F),
                PartPose.offset(5.6F, 0.0F, 1.5F));
        PartDefinition saddleStrapR = body.addOrReplaceChild("saddle_strap_r",
                CubeListBuilder.create().texOffs(258, 405).mirror()
                        .addBox(-0.6F, -7.6F, -4.6F, 1.1F, 9.6F, 9.2F),
                PartPose.offset(-5.6F, 0.0F, 1.5F));
        PartDefinition armorChest = body.addOrReplaceChild("armor_chest",
                CubeListBuilder.create().texOffs(0, 194)
                        .addBox(-8.5F, -7.1F, -8.1F, 17.0F, 14.2F, 9.1F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -8.6F, 0.06F, 0.00F, 0.00F));
        PartDefinition armorTorso = body.addOrReplaceChild("armor_torso",
                CubeListBuilder.create().texOffs(443, 94)
                        .addBox(-6.7F, -6.7F, -9.3F, 13.4F, 13.4F, 18.1F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition armorHips = body.addOrReplaceChild("armor_hips",
                CubeListBuilder.create().texOffs(105, 194)
                        .addBox(-7.5F, -6.9F, -1.3F, 15.0F, 13.8F, 9.3F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition armorCollar = body.addOrReplaceChild("armor_collar",
                CubeListBuilder.create().texOffs(854, 194)
                        .addBox(-6.2F, -1.3F, -2.8F, 12.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 3.6F, -7.6F, 0.45F, 0.00F, 0.00F));
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(927, 194)
                        .addBox(0.0F, -2.1F, -2.4F, 14.0F, 4.2F, 4.8F),
                PartPose.offsetAndRotation(6.0F, 2.6F, -7.2F, -0.20F, -0.55F, 1.35F));
        PartDefinition wingLDeltoid = wingL.addOrReplaceChild("wing_l_deltoid",
                CubeListBuilder.create().texOffs(880, 266)
                        .addBox(0.0F, -2.6F, -1.8F, 6.4F, 5.6F, 5.6F),
                PartPose.offset(0.6F, 0.6F, -1.6F));
        PartDefinition wingLFore = wingL.addOrReplaceChild("wing_l_fore",
                CubeListBuilder.create().texOffs(461, 266)
                        .addBox(0.0F, -1.6F, -1.8F, 15.0F, 3.2F, 3.6F),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.40F, 0.30F, -1.05F));
        PartDefinition wingLHand = wingLFore.addOrReplaceChild("wing_l_hand",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.35F, 0.00F, 0.35F));
        PartDefinition wingLClaw = wingLHand.addOrReplaceChild("wing_l_claw",
                CubeListBuilder.create().texOffs(499, 461)
                        .addBox(0.0F, -0.8F, -0.8F, 1.6F, 1.6F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.9F, -0.6F, -0.55F, 0.00F, 0.00F));
        PartDefinition wingLF1a = wingLHand.addOrReplaceChild("wing_l_f1a",
                CubeListBuilder.create().texOffs(730, 327)
                        .addBox(0.0F, -0.8F, -0.8F, 15.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.05F, 0.34F, -0.03F));
        PartDefinition wingLF1b = wingLF1a.addOrReplaceChild("wing_l_f1b",
                CubeListBuilder.create().texOffs(766, 405)
                        .addBox(0.0F, -0.6F, -0.6F, 12.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.10F, 0.00F, 0.00F));
        PartDefinition wingLF2a = wingLHand.addOrReplaceChild("wing_l_f2a",
                CubeListBuilder.create().texOffs(480, 327)
                        .addBox(0.0F, -0.7F, -0.7F, 19.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.10F, 0.02F, -0.06F));
        PartDefinition wingLF2b = wingLF2a.addOrReplaceChild("wing_l_f2b",
                CubeListBuilder.create().texOffs(467, 405)
                        .addBox(0.0F, -0.6F, -0.6F, 15.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(19.0F, 0.0F, 0.0F, 0.16F, 0.00F, 0.00F));
        PartDefinition wingLF3a = wingLHand.addOrReplaceChild("wing_l_f3a",
                CubeListBuilder.create().texOffs(903, 327)
                        .addBox(0.0F, -0.7F, -0.7F, 18.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.15F, -0.42F, -0.09F));
        PartDefinition wingLF3b = wingLF3a.addOrReplaceChild("wing_l_f3b",
                CubeListBuilder.create().texOffs(0, 461)
                        .addBox(0.0F, -0.5F, -0.5F, 14.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(18.0F, 0.0F, 0.0F, 0.22F, 0.00F, 0.00F));
        PartDefinition wingLF4a = wingLHand.addOrReplaceChild("wing_l_f4a",
                CubeListBuilder.create().texOffs(876, 405)
                        .addBox(0.0F, -0.6F, -0.6F, 14.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.20F, -0.86F, -0.12F));
        PartDefinition wingLF4b = wingLF4a.addOrReplaceChild("wing_l_f4b",
                CubeListBuilder.create().texOffs(378, 461)
                        .addBox(0.0F, -0.4F, -0.4F, 10.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.28F, 0.00F, 0.00F));
        PartDefinition wingLF5a = wingLHand.addOrReplaceChild("wing_l_f5a",
                CubeListBuilder.create().texOffs(553, 461)
                        .addBox(0.0F, -0.5F, -0.5F, 9.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.25F, -1.24F, -0.15F));
        PartDefinition wingLF5b = wingLF5a.addOrReplaceChild("wing_l_f5b",
                CubeListBuilder.create().texOffs(825, 461)
                        .addBox(0.0F, -0.4F, -0.4F, 6.5F, 0.7F, 0.7F),
                PartPose.offsetAndRotation(9.5F, 0.0F, 0.0F, 0.34F, 0.00F, 0.00F));
        PartDefinition wingLTipclaw = wingLF1b.addOrReplaceChild("wing_l_tipclaw",
                CubeListBuilder.create().texOffs(216, 497)
                        .addBox(0.0F, -0.5F, -0.5F, 2.4F, 0.9F, 0.9F),
                PartPose.offset(12.0F, 0.0F, 0.0F));
        PartDefinition wingLM1 = wingLHand.addOrReplaceChild("wing_l_m1",
                CubeListBuilder.create().texOffs(438, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 26.5F, 0.6F, 23.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -7.9F, 0.00F, 0.18F, -0.05F));
        PartDefinition wingLM2 = wingLHand.addOrReplaceChild("wing_l_m2",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 31.4F, 0.6F, 23.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -7.9F, 0.00F, -0.20F, -0.10F));
        PartDefinition wingLM3 = wingLHand.addOrReplaceChild("wing_l_m3",
                CubeListBuilder.create().texOffs(836, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 24.0F, 0.6F, 21.8F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -7.4F, 0.00F, -0.64F, -0.15F));
        PartDefinition wingLM4 = wingLHand.addOrReplaceChild("wing_l_m4",
                CubeListBuilder.create().texOffs(185, 94)
                        .addBox(0.0F, -0.3F, 0.0F, 15.7F, 0.6F, 16.7F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -5.7F, 0.00F, -1.05F, -0.20F));
        PartDefinition wingLM5 = wingLHand.addOrReplaceChild("wing_l_m5",
                CubeListBuilder.create().texOffs(570, 94)
                        .addBox(0.0F, -0.3F, 0.0F, 17.8F, 0.6F, 11.7F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -4.0F, 0.00F, 0.86F, -0.25F));
        PartDefinition wingLM6 = wingLHand.addOrReplaceChild("wing_l_m6",
                CubeListBuilder.create().texOffs(331, 266)
                        .addBox(0.0F, -0.3F, 0.0F, 9.9F, 0.6F, 5.8F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -2.0F, 0.00F, -1.74F, -0.30F));
        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(0, 266).mirror()
                        .addBox(0.0F, -2.1F, -2.4F, 14.0F, 4.2F, 4.8F),
                PartPose.offsetAndRotation(-6.0F, 2.6F, -7.2F, -0.20F, 0.55F, -1.35F));
        PartDefinition wingRDeltoid = wingR.addOrReplaceChild("wing_r_deltoid",
                CubeListBuilder.create().texOffs(929, 266).mirror()
                        .addBox(0.0F, -2.6F, -1.8F, 6.4F, 5.6F, 5.6F),
                PartPose.offset(-0.6F, 0.6F, -1.6F));
        PartDefinition wingRFore = wingR.addOrReplaceChild("wing_r_fore",
                CubeListBuilder.create().texOffs(536, 266).mirror()
                        .addBox(0.0F, -1.6F, -1.8F, 15.0F, 3.2F, 3.6F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.40F, -0.30F, 1.05F));
        PartDefinition wingRHand = wingRFore.addOrReplaceChild("wing_r_hand",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, 0.35F, -0.00F, -0.35F));
        PartDefinition wingRClaw = wingRHand.addOrReplaceChild("wing_r_claw",
                CubeListBuilder.create().texOffs(526, 461).mirror()
                        .addBox(0.0F, -0.8F, -0.8F, 1.6F, 1.6F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.9F, -0.6F, -0.55F, 0.00F, 0.00F));
        PartDefinition wingRF1a = wingRHand.addOrReplaceChild("wing_r_f1a",
                CubeListBuilder.create().texOffs(797, 327).mirror()
                        .addBox(0.0F, -0.8F, -0.8F, 15.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.05F, -0.34F, 0.03F));
        PartDefinition wingRF1b = wingRF1a.addOrReplaceChild("wing_r_f1b",
                CubeListBuilder.create().texOffs(821, 405).mirror()
                        .addBox(0.0F, -0.6F, -0.6F, 12.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, -0.10F, 0.00F, 0.00F));
        PartDefinition wingRF2a = wingRHand.addOrReplaceChild("wing_r_f2a",
                CubeListBuilder.create().texOffs(563, 327).mirror()
                        .addBox(0.0F, -0.7F, -0.7F, 19.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.10F, -0.02F, 0.06F));
        PartDefinition wingRF2b = wingRF2a.addOrReplaceChild("wing_r_f2b",
                CubeListBuilder.create().texOffs(532, 405).mirror()
                        .addBox(0.0F, -0.6F, -0.6F, 15.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(-19.0F, 0.0F, 0.0F, -0.16F, 0.00F, 0.00F));
        PartDefinition wingRF3a = wingRHand.addOrReplaceChild("wing_r_f3a",
                CubeListBuilder.create().texOffs(0, 405).mirror()
                        .addBox(0.0F, -0.7F, -0.7F, 18.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.15F, 0.42F, 0.09F));
        PartDefinition wingRF3b = wingRF3a.addOrReplaceChild("wing_r_f3b",
                CubeListBuilder.create().texOffs(61, 461).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 14.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-18.0F, 0.0F, 0.0F, -0.22F, 0.00F, 0.00F));
        PartDefinition wingRF4a = wingRHand.addOrReplaceChild("wing_r_f4a",
                CubeListBuilder.create().texOffs(937, 405).mirror()
                        .addBox(0.0F, -0.6F, -0.6F, 14.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.20F, 0.86F, 0.12F));
        PartDefinition wingRF4b = wingRF4a.addOrReplaceChild("wing_r_f4b",
                CubeListBuilder.create().texOffs(425, 461).mirror()
                        .addBox(0.0F, -0.4F, -0.4F, 10.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, -0.28F, 0.00F, 0.00F));
        PartDefinition wingRF5a = wingRHand.addOrReplaceChild("wing_r_f5a",
                CubeListBuilder.create().texOffs(596, 461).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 9.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.25F, 1.24F, 0.15F));
        PartDefinition wingRF5b = wingRF5a.addOrReplaceChild("wing_r_f5b",
                CubeListBuilder.create().texOffs(854, 461).mirror()
                        .addBox(0.0F, -0.4F, -0.4F, 6.5F, 0.7F, 0.7F),
                PartPose.offsetAndRotation(-9.5F, 0.0F, 0.0F, -0.34F, 0.00F, 0.00F));
        PartDefinition wingRTipclaw = wingRF1b.addOrReplaceChild("wing_r_tipclaw",
                CubeListBuilder.create().texOffs(231, 497).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 2.4F, 0.9F, 0.9F),
                PartPose.offset(-12.0F, 0.0F, 0.0F));
        PartDefinition wingRM1 = wingRHand.addOrReplaceChild("wing_r_m1",
                CubeListBuilder.create().texOffs(637, 0).mirror()
                        .addBox(0.0F, -0.3F, 0.0F, 26.5F, 0.6F, 23.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -7.9F, 0.00F, -0.18F, 0.05F));
        PartDefinition wingRM2 = wingRHand.addOrReplaceChild("wing_r_m2",
                CubeListBuilder.create().texOffs(219, 0).mirror()
                        .addBox(0.0F, -0.3F, 0.0F, 31.4F, 0.6F, 23.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -7.9F, 0.00F, 0.20F, 0.10F));
        PartDefinition wingRM3 = wingRHand.addOrReplaceChild("wing_r_m3",
                CubeListBuilder.create().texOffs(0, 94).mirror()
                        .addBox(0.0F, -0.3F, 0.0F, 24.0F, 0.6F, 21.8F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -7.4F, 0.00F, 0.64F, 0.15F));
        PartDefinition wingRM4 = wingRHand.addOrReplaceChild("wing_r_m4",
                CubeListBuilder.create().texOffs(314, 94).mirror()
                        .addBox(0.0F, -0.3F, 0.0F, 15.7F, 0.6F, 16.7F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -5.7F, 0.00F, 1.05F, 0.20F));
        PartDefinition wingRM5 = wingRHand.addOrReplaceChild("wing_r_m5",
                CubeListBuilder.create().texOffs(689, 94).mirror()
                        .addBox(0.0F, -0.3F, 0.0F, 17.8F, 0.6F, 11.7F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -4.0F, 0.00F, -0.86F, 0.25F));
        PartDefinition wingRM6 = wingRHand.addOrReplaceChild("wing_r_m6",
                CubeListBuilder.create().texOffs(396, 266).mirror()
                        .addBox(0.0F, -0.3F, 0.0F, 9.9F, 0.6F, 5.8F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -2.0F, 0.00F, 1.74F, 0.30F));
        return mesh;
    }
}
