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
                CubeListBuilder.create().texOffs(140, 266)
                        .addBox(-4.0F, -4.4F, -3.6F, 8.0F, 8.8F, 7.2F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 8.4F, -0.06F, 0.00F, 0.00F));
        PartDefinition hips = body.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(392, 194)
                        .addBox(-5.8F, -5.6F, -0.8F, 11.6F, 11.2F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -0.5F, 12.6F, -0.10F, 0.00F, 0.00F));
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(707, 266)
                        .addBox(-3.0F, -3.6F, -6.7F, 6.0F, 7.2F, 7.1F),
                PartPose.offsetAndRotation(0.0F, 4.6F, -9.6F, 1.06F, 0.00F, 0.00F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(966, 266)
                        .addBox(-2.6F, -3.1F, -6.3F, 5.2F, 6.2F, 6.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.8F, -0.28F, 0.00F, 0.00F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(220, 322)
                        .addBox(-2.3F, -2.7F, -5.8F, 4.6F, 5.4F, 6.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.4F, -0.22F, 0.00F, 0.00F));
        PartDefinition neck4 = neck3.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(617, 322)
                        .addBox(-2.0F, -2.4F, -5.3F, 4.0F, 4.8F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.0F, -0.16F, 0.00F, 0.00F));
        PartDefinition neck5 = neck4.addOrReplaceChild("neck5",
                CubeListBuilder.create().texOffs(29, 400)
                        .addBox(-1.8F, -2.1F, -4.9F, 3.6F, 4.2F, 5.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -4.6F, -0.10F, 0.00F, 0.00F));
        PartDefinition bellyChest = body.addOrReplaceChild("belly_chest",
                CubeListBuilder.create().texOffs(722, 194)
                        .addBox(-4.4F, -7.2F, -7.4F, 8.8F, 1.6F, 8.2F),
                PartPose.offsetAndRotation(0.0F, 1.3F, -8.6F, 0.10F, 0.00F, 0.00F));
        PartDefinition bellyTorso = body.addOrReplaceChild("belly_torso",
                CubeListBuilder.create().texOffs(204, 194)
                        .addBox(-3.8F, -6.6F, -8.6F, 7.6F, 1.6F, 17.2F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition bellyWaist = body.addOrReplaceChild("belly_waist",
                CubeListBuilder.create().texOffs(815, 266)
                        .addBox(-3.0F, -5.2F, -3.4F, 6.0F, 1.4F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 8.4F, -0.06F, 0.00F, 0.00F));
        PartDefinition bellyHips = body.addOrReplaceChild("belly_hips",
                CubeListBuilder.create().texOffs(791, 194)
                        .addBox(-4.2F, -6.4F, -0.6F, 8.4F, 1.5F, 8.6F),
                PartPose.offsetAndRotation(0.0F, -0.5F, 12.6F, -0.10F, 0.00F, 0.00F));
        PartDefinition ridgeChest = body.addOrReplaceChild("ridge_chest",
                CubeListBuilder.create().texOffs(64, 400)
                        .addBox(-1.1F, -7.8F, -7.6F, 2.2F, 3.2F, 8.4F),
                PartPose.offsetAndRotation(0.0F, 1.3F, -8.6F, 0.10F, 0.00F, 0.00F));
        PartDefinition ridgeTorso = body.addOrReplaceChild("ridge_torso",
                CubeListBuilder.create().texOffs(0, 322)
                        .addBox(-1.0F, -7.4F, -8.8F, 2.0F, 3.4F, 17.6F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition ridgeWaist = body.addOrReplaceChild("ridge_waist",
                CubeListBuilder.create().texOffs(0, 456)
                        .addBox(-0.8F, -6.6F, -3.4F, 1.6F, 2.6F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.8F, 8.4F, -0.05F, 0.00F, 0.00F));
        PartDefinition ridgeHips = body.addOrReplaceChild("ridge_hips",
                CubeListBuilder.create().texOffs(259, 400)
                        .addBox(-0.9F, -8.0F, -0.6F, 1.8F, 2.8F, 8.6F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 12.6F, -0.08F, 0.00F, 0.00F));
        PartDefinition scapulaL = chest.addOrReplaceChild("scapula_l",
                CubeListBuilder.create().texOffs(418, 456)
                        .addBox(-0.9F, 0.0F, -2.2F, 1.8F, 4.6F, 2.2F),
                PartPose.offsetAndRotation(6.2F, 4.4F, -0.4F, 0.00F, 0.00F, -0.55F));
        PartDefinition scapulaL2 = chest.addOrReplaceChild("scapula_l2",
                CubeListBuilder.create().texOffs(532, 456)
                        .addBox(-0.8F, 0.0F, -1.8F, 1.6F, 3.8F, 2.0F),
                PartPose.offsetAndRotation(6.9F, 3.2F, 1.9F, 0.00F, 0.00F, -0.75F));
        PartDefinition scapulaR = chest.addOrReplaceChild("scapula_r",
                CubeListBuilder.create().texOffs(435, 456).mirror()
                        .addBox(-0.9F, 0.0F, -2.2F, 1.8F, 4.6F, 2.2F),
                PartPose.offsetAndRotation(-6.2F, 4.4F, -0.4F, 0.00F, 0.00F, 0.55F));
        PartDefinition scapulaR2 = chest.addOrReplaceChild("scapula_r2",
                CubeListBuilder.create().texOffs(547, 456).mirror()
                        .addBox(-0.8F, 0.0F, -1.8F, 1.6F, 3.8F, 2.0F),
                PartPose.offsetAndRotation(-6.9F, 3.2F, 1.9F, 0.00F, 0.00F, 0.75F));
        PartDefinition hipSpurL = hips.addOrReplaceChild("hip_spur_l",
                CubeListBuilder.create().texOffs(477, 456)
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(6.6F, 3.6F, 2.6F, 0.00F, 0.00F, -0.90F));
        PartDefinition hipSpurR = hips.addOrReplaceChild("hip_spur_r",
                CubeListBuilder.create().texOffs(492, 456).mirror()
                        .addBox(-0.7F, 0.0F, -2.0F, 1.4F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(-6.6F, 3.6F, 2.6F, 0.00F, 0.00F, 0.90F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(77, 266)
                        .addBox(-3.6F, -3.6F, 0.0F, 7.2F, 7.2F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -1.6F, 13.4F, -0.12F, 0.00F, 0.00F));
        PartDefinition tail1Spike = tail1.addOrReplaceChild("tail1_spike",
                CubeListBuilder.create().texOffs(251, 456)
                        .addBox(-0.6F, -2.8F, -2.4F, 1.2F, 3.0F, 5.6F),
                PartPose.offset(0.0F, -3.3F, 2.2F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(542, 266)
                        .addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 8.4F, -0.03F, 0.00F, 0.00F));
        PartDefinition tail2Spike = tail2.addOrReplaceChild("tail2_spike",
                CubeListBuilder.create().texOffs(452, 456)
                        .addBox(-0.6F, -2.6F, -2.2F, 1.1F, 2.8F, 5.2F),
                PartPose.offset(0.0F, -2.8F, 2.2F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(760, 266)
                        .addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.1F, 8.4F, 0.05F, 0.00F, 0.00F));
        PartDefinition tail3Spike = tail3.addOrReplaceChild("tail3_spike",
                CubeListBuilder.create().texOffs(507, 456)
                        .addBox(-0.5F, -2.4F, -2.0F, 1.0F, 2.6F, 4.8F),
                PartPose.offset(0.0F, -2.3F, 2.2F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(79, 322)
                        .addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.10F, 0.00F, 0.00F));
        PartDefinition tail4Spike = tail4.addOrReplaceChild("tail4_spike",
                CubeListBuilder.create().texOffs(562, 456)
                        .addBox(-0.5F, -2.0F, -1.8F, 0.9F, 2.2F, 4.4F),
                PartPose.offset(0.0F, -1.8F, 2.2F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(656, 322)
                        .addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.4F, 0.13F, 0.00F, 0.00F));
        PartDefinition tail5Spike = tail5.addOrReplaceChild("tail5_spike",
                CubeListBuilder.create().texOffs(697, 456)
                        .addBox(-0.4F, -1.7F, -1.6F, 0.8F, 1.8F, 4.0F),
                PartPose.offset(0.0F, -1.3F, 2.2F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(511, 400)
                        .addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.14F, 0.00F, 0.00F));
        PartDefinition tail7 = tail6.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(124, 456)
                        .addBox(-0.7F, -0.7F, 0.0F, 1.4F, 1.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.4F, 0.14F, 0.00F, 0.00F));
        PartDefinition spadeTop = tail7.addOrReplaceChild("spade_top",
                CubeListBuilder.create().texOffs(62, 456)
                        .addBox(-0.7F, -0.2F, 0.0F, 1.4F, 3.4F, 6.4F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 5.8F, -0.34F, 0.00F, 0.00F));
        PartDefinition spadeBot = tail7.addOrReplaceChild("spade_bot",
                CubeListBuilder.create().texOffs(95, 456)
                        .addBox(-0.7F, 0.0F, 0.0F, 1.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 0.2F, 6.0F, 0.05F, 0.00F, 0.00F));
        PartDefinition spadeLeft = tail7.addOrReplaceChild("spade_left",
                CubeListBuilder.create().texOffs(786, 456)
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(0.7F, 0.0F, 5.4F, 0.00F, 0.00F, -0.70F));
        PartDefinition spadeRight = tail7.addOrReplaceChild("spade_right",
                CubeListBuilder.create().texOffs(807, 456).mirror()
                        .addBox(-0.3F, -0.5F, 0.0F, 0.5F, 1.0F, 4.4F),
                PartPose.offsetAndRotation(-0.7F, 0.0F, 5.4F, 0.00F, 0.00F, 0.70F));
        PartDefinition head = neck5.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(558, 194)
                        .addBox(-4.8F, -3.4F, -9.4F, 9.6F, 6.8F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.2F, -3.6F, -0.30F, 0.00F, 0.00F));
        PartDefinition muzzle = head.addOrReplaceChild("muzzle",
                CubeListBuilder.create().texOffs(201, 266)
                        .addBox(-3.1F, -2.0F, -9.0F, 6.2F, 4.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, -1.3F, -8.2F, 0.04F, 0.00F, 0.00F));
        PartDefinition snoutTip = muzzle.addOrReplaceChild("snout_tip",
                CubeListBuilder.create().texOffs(478, 400)
                        .addBox(-2.4F, -1.6F, -3.0F, 4.8F, 3.2F, 3.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -8.8F, -0.10F, 0.00F, 0.00F));
        PartDefinition nostrilL = snoutTip.addOrReplaceChild("nostril_l",
                CubeListBuilder.create().texOffs(988, 456)
                        .addBox(-0.5F, -0.5F, -0.5F, 0.9F, 1.0F, 0.9F),
                PartPose.offset(1.5F, 1.1F, -3.0F));
        PartDefinition nostrilR = snoutTip.addOrReplaceChild("nostril_r",
                CubeListBuilder.create().texOffs(997, 456).mirror()
                        .addBox(-0.5F, -0.5F, -0.5F, 0.9F, 1.0F, 0.9F),
                PartPose.offset(-1.5F, 1.1F, -3.0F));
        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(637, 194)
                        .addBox(-2.9F, 0.0F, -15.0F, 5.8F, 1.8F, 15.0F),
                PartPose.offsetAndRotation(0.0F, 2.6F, -2.6F, 0.04F, 0.00F, 0.00F));
        PartDefinition teethL = muzzle.addOrReplaceChild("teeth_l",
                CubeListBuilder.create().texOffs(643, 456)
                        .addBox(-0.3F, 0.0F, -1.0F, 0.7F, 1.2F, 6.0F),
                PartPose.offset(2.2F, 1.9F, -5.4F));
        PartDefinition teethR = muzzle.addOrReplaceChild("teeth_r",
                CubeListBuilder.create().texOffs(670, 456).mirror()
                        .addBox(-0.3F, 0.0F, -1.0F, 0.7F, 1.2F, 6.0F),
                PartPose.offset(-2.2F, 1.9F, -5.4F));
        PartDefinition browL = head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(550, 400)
                        .addBox(-1.1F, -0.7F, -4.4F, 2.2F, 1.4F, 7.0F),
                PartPose.offsetAndRotation(3.1F, 1.7F, -5.4F, 0.00F, 0.00F, -0.34F));
        PartDefinition browR = head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(587, 400).mirror()
                        .addBox(-1.1F, -0.7F, -4.4F, 2.2F, 1.4F, 7.0F),
                PartPose.offsetAndRotation(-3.1F, 1.7F, -5.4F, 0.00F, 0.00F, 0.34F));
        PartDefinition eyeL = head.addOrReplaceChild("eye_l",
                CubeListBuilder.create().texOffs(718, 456)
                        .addBox(-0.2F, -0.8F, -2.0F, 0.6F, 1.6F, 4.0F),
                PartPose.offset(4.7F, 0.6F, -5.6F));
        PartDefinition eyeR = head.addOrReplaceChild("eye_r",
                CubeListBuilder.create().texOffs(737, 456).mirror()
                        .addBox(-0.2F, -0.8F, -2.0F, 0.6F, 1.6F, 4.0F),
                PartPose.offset(-4.7F, 0.6F, -5.6F));
        PartDefinition hornL1 = head.addOrReplaceChild("horn_l1",
                CubeListBuilder.create().texOffs(503, 322)
                        .addBox(-1.1F, -1.1F, -1.0F, 2.1F, 2.3F, 12.0F),
                PartPose.offsetAndRotation(2.1F, 2.8F, -5.2F, -0.62F, 0.16F, 0.00F));
        PartDefinition hornR1 = head.addOrReplaceChild("horn_r1",
                CubeListBuilder.create().texOffs(560, 322).mirror()
                        .addBox(-1.1F, -1.1F, -1.0F, 2.1F, 2.3F, 12.0F),
                PartPose.offsetAndRotation(-2.1F, 2.8F, -5.2F, -0.62F, -0.16F, 0.00F));
        PartDefinition legFlThigh = body.addOrReplaceChild("leg_fl_thigh",
                CubeListBuilder.create().texOffs(263, 322)
                        .addBox(-1.7F, -10.0F, -2.6F, 3.4F, 11.0F, 5.6F),
                PartPose.offsetAndRotation(5.4F, -4.4F, -8.4F, 0.16F, 0.00F, 0.00F));
        PartDefinition legFlShin = legFlThigh.addOrReplaceChild("leg_fl_shin",
                CubeListBuilder.create().texOffs(432, 400)
                        .addBox(-1.3F, -9.4F, -1.6F, 2.6F, 9.4F, 3.2F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 0.3F, -0.30F, 0.00F, 0.00F));
        PartDefinition legFlFoot = legFlShin.addOrReplaceChild("leg_fl_foot",
                CubeListBuilder.create().texOffs(189, 400)
                        .addBox(-1.7F, -1.3F, -3.4F, 3.4F, 2.3F, 4.8F),
                PartPose.offsetAndRotation(0.0F, -9.4F, -0.2F, 0.22F, 0.00F, 0.00F));
        PartDefinition legFlTalon1 = legFlFoot.addOrReplaceChild("leg_fl_talon1",
                CubeListBuilder.create().texOffs(936, 456)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon2 = legFlFoot.addOrReplaceChild("leg_fl_talon2",
                CubeListBuilder.create().texOffs(910, 456)
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFlTalon3 = legFlFoot.addOrReplaceChild("leg_fl_talon3",
                CubeListBuilder.create().texOffs(949, 456)
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrThigh = body.addOrReplaceChild("leg_fr_thigh",
                CubeListBuilder.create().texOffs(300, 322).mirror()
                        .addBox(-1.7F, -10.0F, -2.6F, 3.4F, 11.0F, 5.6F),
                PartPose.offsetAndRotation(-5.4F, -4.4F, -8.4F, 0.16F, 0.00F, 0.00F));
        PartDefinition legFrShin = legFrThigh.addOrReplaceChild("leg_fr_shin",
                CubeListBuilder.create().texOffs(455, 400).mirror()
                        .addBox(-1.3F, -9.4F, -1.6F, 2.6F, 9.4F, 3.2F),
                PartPose.offsetAndRotation(0.0F, -10.0F, 0.3F, -0.30F, 0.00F, 0.00F));
        PartDefinition legFrFoot = legFrShin.addOrReplaceChild("leg_fr_foot",
                CubeListBuilder.create().texOffs(224, 400).mirror()
                        .addBox(-1.7F, -1.3F, -3.4F, 3.4F, 2.3F, 4.8F),
                PartPose.offsetAndRotation(0.0F, -9.4F, -0.2F, 0.22F, 0.00F, 0.00F));
        PartDefinition legFrTalon1 = legFrFoot.addOrReplaceChild("leg_fr_talon1",
                CubeListBuilder.create().texOffs(962, 456).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon2 = legFrFoot.addOrReplaceChild("leg_fr_talon2",
                CubeListBuilder.create().texOffs(923, 456).mirror()
                        .addBox(-0.4F, -0.9F, -1.7F, 0.8F, 1.0F, 2.2F),
                PartPose.offsetAndRotation(0.0F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon3 = legFrFoot.addOrReplaceChild("leg_fr_talon3",
                CubeListBuilder.create().texOffs(975, 456).mirror()
                        .addBox(-0.4F, -0.9F, -1.5F, 0.8F, 1.0F, 2.0F),
                PartPose.offsetAndRotation(-1.5F, -1.1F, -3.1F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlThigh = body.addOrReplaceChild("leg_bl_thigh",
                CubeListBuilder.create().texOffs(601, 266)
                        .addBox(-2.8F, -11.6F, -3.0F, 5.6F, 12.6F, 7.6F),
                PartPose.offsetAndRotation(5.6F, -4.2F, 9.6F, -0.34F, 0.00F, 0.00F));
        PartDefinition legBlShin = legBlThigh.addOrReplaceChild("leg_bl_shin",
                CubeListBuilder.create().texOffs(993, 322)
                        .addBox(-1.6F, -9.8F, -2.0F, 3.2F, 9.8F, 4.2F),
                PartPose.offsetAndRotation(0.0F, -11.6F, 0.0F, 0.66F, 0.00F, 0.00F));
        PartDefinition legBlFoot = legBlShin.addOrReplaceChild("leg_bl_foot",
                CubeListBuilder.create().texOffs(130, 322)
                        .addBox(-2.6F, -1.5F, -4.0F, 5.2F, 2.5F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBlTalon1 = legBlFoot.addOrReplaceChild("leg_bl_talon1",
                CubeListBuilder.create().texOffs(828, 456)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon2 = legBlFoot.addOrReplaceChild("leg_bl_talon2",
                CubeListBuilder.create().texOffs(756, 456)
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBlTalon3 = legBlFoot.addOrReplaceChild("leg_bl_talon3",
                CubeListBuilder.create().texOffs(841, 456)
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrThigh = body.addOrReplaceChild("leg_br_thigh",
                CubeListBuilder.create().texOffs(654, 266).mirror()
                        .addBox(-2.8F, -11.6F, -3.0F, 5.6F, 12.6F, 7.6F),
                PartPose.offsetAndRotation(-5.6F, -4.2F, 9.6F, -0.34F, 0.00F, 0.00F));
        PartDefinition legBrShin = legBrThigh.addOrReplaceChild("leg_br_shin",
                CubeListBuilder.create().texOffs(0, 400).mirror()
                        .addBox(-1.6F, -9.8F, -2.0F, 3.2F, 9.8F, 4.2F),
                PartPose.offsetAndRotation(0.0F, -11.6F, 0.0F, 0.66F, 0.00F, 0.00F));
        PartDefinition legBrFoot = legBrShin.addOrReplaceChild("leg_br_foot",
                CubeListBuilder.create().texOffs(175, 322).mirror()
                        .addBox(-2.6F, -1.5F, -4.0F, 5.2F, 2.5F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -9.5F, -0.3F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBrTalon1 = legBrFoot.addOrReplaceChild("leg_br_talon1",
                CubeListBuilder.create().texOffs(854, 456).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon2 = legBrFoot.addOrReplaceChild("leg_br_talon2",
                CubeListBuilder.create().texOffs(771, 456).mirror()
                        .addBox(-0.5F, -1.0F, -1.9F, 0.9F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon3 = legBrFoot.addOrReplaceChild("leg_br_talon3",
                CubeListBuilder.create().texOffs(867, 456).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 0.9F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.7F, -1.2F, -3.5F, 0.55F, 0.00F, 0.00F));
        PartDefinition saddle = body.addOrReplaceChild("saddle",
                CubeListBuilder.create().texOffs(475, 194)
                        .addBox(-4.8F, -0.9F, -5.6F, 9.6F, 1.8F, 11.2F),
                PartPose.offset(0.0F, 6.2F, 1.5F));
        PartDefinition saddlePommel = saddle.addOrReplaceChild("saddle_pommel",
                CubeListBuilder.create().texOffs(978, 400)
                        .addBox(-1.7F, -2.4F, -1.7F, 3.4F, 2.6F, 3.4F),
                PartPose.offset(0.0F, 0.9F, -5.0F));
        PartDefinition saddleCantle = saddle.addOrReplaceChild("saddle_cantle",
                CubeListBuilder.create().texOffs(35, 456)
                        .addBox(-2.0F, -2.0F, -1.3F, 4.0F, 2.2F, 2.6F),
                PartPose.offset(0.0F, 0.9F, 5.0F));
        PartDefinition saddleStrapL = body.addOrReplaceChild("saddle_strap_l",
                CubeListBuilder.create().texOffs(107, 400)
                        .addBox(-0.6F, -7.6F, -4.6F, 1.1F, 9.6F, 9.2F),
                PartPose.offset(5.6F, 0.0F, 1.5F));
        PartDefinition saddleStrapR = body.addOrReplaceChild("saddle_strap_r",
                CubeListBuilder.create().texOffs(148, 400).mirror()
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
                CubeListBuilder.create().texOffs(860, 194)
                        .addBox(-6.2F, -1.3F, -2.8F, 12.4F, 2.6F, 5.6F),
                PartPose.offsetAndRotation(0.0F, 3.6F, -7.6F, 0.45F, 0.00F, 0.00F));
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(933, 194)
                        .addBox(0.0F, -2.1F, -2.4F, 14.0F, 4.2F, 4.8F),
                PartPose.offsetAndRotation(6.0F, 6.4F, -7.2F, 0.16F, -0.42F, 0.62F));
        PartDefinition wingLDeltoid = wingL.addOrReplaceChild("wing_l_deltoid",
                CubeListBuilder.create().texOffs(868, 266)
                        .addBox(0.0F, -2.6F, -1.8F, 6.4F, 5.6F, 5.6F),
                PartPose.offset(0.6F, 0.6F, -1.6F));
        PartDefinition wingLFore = wingL.addOrReplaceChild("wing_l_fore",
                CubeListBuilder.create().texOffs(392, 266)
                        .addBox(0.0F, -1.6F, -1.8F, 15.0F, 3.2F, 3.6F),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, -0.22F, -0.16F, -0.18F));
        PartDefinition wingLHand = wingLFore.addOrReplaceChild("wing_l_hand",
                CubeListBuilder.create(),
                PartPose.offset(15.0F, 0.0F, 0.0F));
        PartDefinition wingLClaw = wingLHand.addOrReplaceChild("wing_l_claw",
                CubeListBuilder.create().texOffs(278, 456)
                        .addBox(0.0F, -0.8F, -0.8F, 1.6F, 1.6F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.9F, -0.6F, -0.55F, 0.00F, 0.00F));
        PartDefinition wingLF1a = wingLHand.addOrReplaceChild("wing_l_f1a",
                CubeListBuilder.create().texOffs(701, 322)
                        .addBox(0.0F, -0.8F, -0.8F, 15.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.05F, 0.34F, -0.03F));
        PartDefinition wingLF1b = wingLF1a.addOrReplaceChild("wing_l_f1b",
                CubeListBuilder.create().texOffs(624, 400)
                        .addBox(0.0F, -0.6F, -0.6F, 12.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(15.0F, 0.0F, 0.0F, 0.10F, 0.00F, 0.00F));
        PartDefinition wingLF2a = wingLHand.addOrReplaceChild("wing_l_f2a",
                CubeListBuilder.create().texOffs(337, 322)
                        .addBox(0.0F, -0.7F, -0.7F, 19.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.10F, 0.02F, -0.06F));
        PartDefinition wingLF2b = wingLF2a.addOrReplaceChild("wing_l_f2b",
                CubeListBuilder.create().texOffs(302, 400)
                        .addBox(0.0F, -0.6F, -0.6F, 15.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(19.0F, 0.0F, 0.0F, 0.16F, 0.00F, 0.00F));
        PartDefinition wingLF3a = wingLHand.addOrReplaceChild("wing_l_f3a",
                CubeListBuilder.create().texOffs(835, 322)
                        .addBox(0.0F, -0.7F, -0.7F, 18.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.15F, -0.42F, -0.09F));
        PartDefinition wingLF3b = wingLF3a.addOrReplaceChild("wing_l_f3b",
                CubeListBuilder.create().texOffs(856, 400)
                        .addBox(0.0F, -0.5F, -0.5F, 14.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(18.0F, 0.0F, 0.0F, 0.22F, 0.00F, 0.00F));
        PartDefinition wingLF4a = wingLHand.addOrReplaceChild("wing_l_f4a",
                CubeListBuilder.create().texOffs(734, 400)
                        .addBox(0.0F, -0.6F, -0.6F, 14.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.20F, -0.86F, -0.12F));
        PartDefinition wingLF4b = wingLF4a.addOrReplaceChild("wing_l_f4b",
                CubeListBuilder.create().texOffs(157, 456)
                        .addBox(0.0F, -0.4F, -0.4F, 10.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.28F, 0.00F, 0.00F));
        PartDefinition wingLF5a = wingLHand.addOrReplaceChild("wing_l_f5a",
                CubeListBuilder.create().texOffs(332, 456)
                        .addBox(0.0F, -0.5F, -0.5F, 9.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.25F, -1.24F, -0.15F));
        PartDefinition wingLF5b = wingLF5a.addOrReplaceChild("wing_l_f5b",
                CubeListBuilder.create().texOffs(585, 456)
                        .addBox(0.0F, -0.4F, -0.4F, 6.5F, 0.7F, 0.7F),
                PartPose.offsetAndRotation(9.5F, 0.0F, 0.0F, 0.34F, 0.00F, 0.00F));
        PartDefinition wingLTipclaw = wingLF1b.addOrReplaceChild("wing_l_tipclaw",
                CubeListBuilder.create().texOffs(880, 456)
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
                CubeListBuilder.create().texOffs(262, 266)
                        .addBox(0.0F, -0.3F, 0.0F, 9.9F, 0.6F, 5.8F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -2.0F, 0.00F, -1.74F, -0.30F));
        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(0, 266).mirror()
                        .addBox(0.0F, -2.1F, -2.4F, 14.0F, 4.2F, 4.8F),
                PartPose.offsetAndRotation(-6.0F, 6.4F, -7.2F, 0.16F, 0.42F, -0.62F));
        PartDefinition wingRDeltoid = wingR.addOrReplaceChild("wing_r_deltoid",
                CubeListBuilder.create().texOffs(917, 266).mirror()
                        .addBox(0.0F, -2.6F, -1.8F, 6.4F, 5.6F, 5.6F),
                PartPose.offset(-0.6F, 0.6F, -1.6F));
        PartDefinition wingRFore = wingR.addOrReplaceChild("wing_r_fore",
                CubeListBuilder.create().texOffs(467, 266).mirror()
                        .addBox(0.0F, -1.6F, -1.8F, 15.0F, 3.2F, 3.6F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, -0.22F, 0.16F, 0.18F));
        PartDefinition wingRHand = wingRFore.addOrReplaceChild("wing_r_hand",
                CubeListBuilder.create(),
                PartPose.offset(-15.0F, 0.0F, 0.0F));
        PartDefinition wingRClaw = wingRHand.addOrReplaceChild("wing_r_claw",
                CubeListBuilder.create().texOffs(305, 456).mirror()
                        .addBox(0.0F, -0.8F, -0.8F, 1.6F, 1.6F, 5.0F),
                PartPose.offsetAndRotation(0.0F, 0.9F, -0.6F, -0.55F, 0.00F, 0.00F));
        PartDefinition wingRF1a = wingRHand.addOrReplaceChild("wing_r_f1a",
                CubeListBuilder.create().texOffs(768, 322).mirror()
                        .addBox(0.0F, -0.8F, -0.8F, 15.0F, 1.6F, 1.6F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.05F, -0.34F, 0.03F));
        PartDefinition wingRF1b = wingRF1a.addOrReplaceChild("wing_r_f1b",
                CubeListBuilder.create().texOffs(679, 400).mirror()
                        .addBox(0.0F, -0.6F, -0.6F, 12.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(-15.0F, 0.0F, 0.0F, -0.10F, 0.00F, 0.00F));
        PartDefinition wingRF2a = wingRHand.addOrReplaceChild("wing_r_f2a",
                CubeListBuilder.create().texOffs(420, 322).mirror()
                        .addBox(0.0F, -0.7F, -0.7F, 19.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.10F, -0.02F, 0.06F));
        PartDefinition wingRF2b = wingRF2a.addOrReplaceChild("wing_r_f2b",
                CubeListBuilder.create().texOffs(367, 400).mirror()
                        .addBox(0.0F, -0.6F, -0.6F, 15.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(-19.0F, 0.0F, 0.0F, -0.16F, 0.00F, 0.00F));
        PartDefinition wingRF3a = wingRHand.addOrReplaceChild("wing_r_f3a",
                CubeListBuilder.create().texOffs(914, 322).mirror()
                        .addBox(0.0F, -0.7F, -0.7F, 18.0F, 1.3F, 1.3F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.15F, 0.42F, 0.09F));
        PartDefinition wingRF3b = wingRF3a.addOrReplaceChild("wing_r_f3b",
                CubeListBuilder.create().texOffs(917, 400).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 14.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-18.0F, 0.0F, 0.0F, -0.22F, 0.00F, 0.00F));
        PartDefinition wingRF4a = wingRHand.addOrReplaceChild("wing_r_f4a",
                CubeListBuilder.create().texOffs(795, 400).mirror()
                        .addBox(0.0F, -0.6F, -0.6F, 14.0F, 1.1F, 1.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.20F, 0.86F, 0.12F));
        PartDefinition wingRF4b = wingRF4a.addOrReplaceChild("wing_r_f4b",
                CubeListBuilder.create().texOffs(204, 456).mirror()
                        .addBox(0.0F, -0.4F, -0.4F, 10.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, -0.28F, 0.00F, 0.00F));
        PartDefinition wingRF5a = wingRHand.addOrReplaceChild("wing_r_f5a",
                CubeListBuilder.create().texOffs(375, 456).mirror()
                        .addBox(0.0F, -0.5F, -0.5F, 9.5F, 0.9F, 0.9F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.25F, 1.24F, 0.15F));
        PartDefinition wingRF5b = wingRF5a.addOrReplaceChild("wing_r_f5b",
                CubeListBuilder.create().texOffs(614, 456).mirror()
                        .addBox(0.0F, -0.4F, -0.4F, 6.5F, 0.7F, 0.7F),
                PartPose.offsetAndRotation(-9.5F, 0.0F, 0.0F, -0.34F, 0.00F, 0.00F));
        PartDefinition wingRTipclaw = wingRF1b.addOrReplaceChild("wing_r_tipclaw",
                CubeListBuilder.create().texOffs(895, 456).mirror()
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
                CubeListBuilder.create().texOffs(327, 266).mirror()
                        .addBox(0.0F, -0.3F, 0.0F, 9.9F, 0.6F, 5.8F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -2.0F, 0.00F, 1.74F, 0.30F));
        return mesh;
    }
}
