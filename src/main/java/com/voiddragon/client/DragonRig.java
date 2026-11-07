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
                PartPose.offset(0.0F, 20.0F, 0.0F));
        PartDefinition torso = body.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(278, 0)
                        .addBox(-8.8F, -8.5F, -14.0F, 17.6F, 15.0F, 27.0F),
                PartPose.offset(0.0F, 0.0F, 1.0F));
        PartDefinition chest = body.addOrReplaceChild("chest",
                CubeListBuilder.create().texOffs(243, 70)
                        .addBox(-10.2F, -9.2F, -8.0F, 20.4F, 16.4F, 10.0F),
                PartPose.offset(0.0F, 1.0F, -12.0F));
        PartDefinition hips = body.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(421, 70)
                        .addBox(-8.4F, -7.8F, -1.0F, 16.8F, 13.6F, 10.0F),
                PartPose.offset(0.0F, 0.0F, 12.0F));
        PartDefinition belly = body.addOrReplaceChild("belly",
                CubeListBuilder.create().texOffs(166, 70)
                        .addBox(-6.6F, -9.6F, -12.0F, 13.2F, 2.0F, 25.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition ridge = body.addOrReplaceChild("ridge",
                CubeListBuilder.create().texOffs(409, 156)
                        .addBox(-1.0F, -11.2F, -13.0F, 2.0F, 2.8F, 26.0F),
                PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition fin0 = body.addOrReplaceChild("fin0",
                CubeListBuilder.create().texOffs(412, 212)
                        .addBox(-0.8F, -5.0F, -3.0F, 1.6F, 5.5F, 6.0F),
                PartPose.offset(0.0F, -9.0F, -9.0F));
        PartDefinition fin1 = body.addOrReplaceChild("fin1",
                CubeListBuilder.create().texOffs(376, 212)
                        .addBox(-0.8F, -6.0F, -3.0F, 1.6F, 6.5F, 7.0F),
                PartPose.offset(0.0F, -9.6F, -3.0F));
        PartDefinition fin2 = body.addOrReplaceChild("fin2",
                CubeListBuilder.create().texOffs(358, 212)
                        .addBox(-0.8F, -6.5F, -3.0F, 1.6F, 7.0F, 7.0F),
                PartPose.offset(0.0F, -9.6F, 3.0F));
        PartDefinition fin3 = body.addOrReplaceChild("fin3",
                CubeListBuilder.create().texOffs(394, 212)
                        .addBox(-0.8F, -5.5F, -3.0F, 1.6F, 6.0F, 7.0F),
                PartPose.offset(0.0F, -9.2F, 9.0F));
        PartDefinition fin4 = body.addOrReplaceChild("fin4",
                CubeListBuilder.create().texOffs(468, 212)
                        .addBox(-0.7F, -4.5F, -2.5F, 1.4F, 5.0F, 6.0F),
                PartPose.offset(0.0F, -8.4F, 15.0F));
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(68, 156)
                        .addBox(-5.2F, -5.2F, -6.0F, 10.4F, 10.4F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, -14.0F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck1Fin = neck1.addOrReplaceChild("neck1_fin",
                CubeListBuilder.create().texOffs(51, 235)
                        .addBox(-0.7F, -3.4F, -3.0F, 1.4F, 3.6F, 6.0F),
                PartPose.offset(0.0F, -4.4F, -3.0F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(102, 156)
                        .addBox(-4.9F, -4.9F, -6.0F, 9.8F, 9.8F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.32F, 0.00F, 0.00F));
        PartDefinition neck2Fin = neck2.addOrReplaceChild("neck2_fin",
                CubeListBuilder.create().texOffs(67, 235)
                        .addBox(-0.7F, -3.2F, -3.0F, 1.4F, 3.4F, 6.0F),
                PartPose.offset(0.0F, -3.9F, -3.0F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(198, 156)
                        .addBox(-4.6F, -4.6F, -6.0F, 9.2F, 9.2F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.16F, 0.00F, 0.00F));
        PartDefinition neck3Fin = neck3.addOrReplaceChild("neck3_fin",
                CubeListBuilder.create().texOffs(83, 235)
                        .addBox(-0.6F, -3.0F, -3.0F, 1.2F, 3.0F, 6.0F),
                PartPose.offset(0.0F, -3.4F, -3.0F));
        PartDefinition neck4 = neck3.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(380, 156)
                        .addBox(-4.3F, -4.3F, -5.5F, 8.6F, 8.6F, 5.5F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.02F, 0.00F, 0.00F));
        PartDefinition neck5 = neck4.addOrReplaceChild("neck5",
                CubeListBuilder.create().texOffs(178, 212)
                        .addBox(-4.0F, -4.0F, -4.5F, 8.0F, 8.0F, 4.5F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.5F, -0.14F, 0.00F, 0.00F));
        PartDefinition head = neck5.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(55, 123)
                        .addBox(-6.5F, -5.5F, -10.0F, 13.0F, 11.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.5F, -4.5F, 0.12F, 0.00F, 0.00F));
        PartDefinition snout = head.addOrReplaceChild("snout",
                CubeListBuilder.create().texOffs(166, 156)
                        .addBox(-4.2F, -3.4F, -7.0F, 8.4F, 6.4F, 7.0F),
                PartPose.offset(0.0F, 1.0F, -9.0F));
        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(455, 123)
                        .addBox(-4.2F, 0.0F, -10.0F, 8.4F, 2.6F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 3.4F, -1.0F, 0.04F, 0.00F, 0.00F));
        PartDefinition hornLBase = head.addOrReplaceChild("horn_l_base",
                CubeListBuilder.create().texOffs(295, 212)
                        .addBox(-1.2F, -1.2F, 0.0F, 2.4F, 2.4F, 8.0F),
                PartPose.offsetAndRotation(4.8F, -4.4F, -1.0F, 0.50F, -0.28F, 0.00F));
        PartDefinition hornLTip = hornLBase.addOrReplaceChild("horn_l_tip",
                CubeListBuilder.create().texOffs(428, 212)
                        .addBox(-0.8F, -0.8F, 0.0F, 1.6F, 1.6F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.45F, -0.15F, 0.00F));
        PartDefinition hornRBase = head.addOrReplaceChild("horn_r_base",
                CubeListBuilder.create().texOffs(317, 212).mirror()
                        .addBox(-1.2F, -1.2F, 0.0F, 2.4F, 2.4F, 8.0F),
                PartPose.offsetAndRotation(-4.8F, -4.4F, -1.0F, 0.50F, 0.28F, 0.00F));
        PartDefinition hornRTip = hornRBase.addOrReplaceChild("horn_r_tip",
                CubeListBuilder.create().texOffs(448, 212).mirror()
                        .addBox(-0.8F, -0.8F, 0.0F, 1.6F, 1.6F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.45F, 0.15F, 0.00F));
        PartDefinition browL = head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(484, 212)
                        .addBox(-0.9F, -0.9F, 0.0F, 1.8F, 1.8F, 6.0F),
                PartPose.offsetAndRotation(6.2F, 1.6F, -2.0F, 0.00F, -1.00F, 0.00F));
        PartDefinition browR = head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(0, 235).mirror()
                        .addBox(-0.9F, -0.9F, 0.0F, 1.8F, 1.8F, 6.0F),
                PartPose.offsetAndRotation(-6.2F, 1.6F, -2.0F, 0.00F, 1.00F, 0.00F));
        PartDefinition cheekL = head.addOrReplaceChild("cheek_l",
                CubeListBuilder.create().texOffs(98, 235)
                        .addBox(-0.8F, -0.8F, 0.0F, 1.6F, 1.6F, 5.0F),
                PartPose.offsetAndRotation(5.8F, 1.8F, 3.5F, 0.00F, -1.75F, 0.00F));
        PartDefinition cheekR = head.addOrReplaceChild("cheek_r",
                CubeListBuilder.create().texOffs(112, 235).mirror()
                        .addBox(-0.8F, -0.8F, 0.0F, 1.6F, 1.6F, 5.0F),
                PartPose.offsetAndRotation(-5.8F, 1.8F, 3.5F, 0.00F, 1.75F, 0.00F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(226, 123)
                        .addBox(-5.0F, -4.5F, 0.0F, 10.0F, 9.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, 14.0F, -0.04F, 0.00F, 0.00F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(305, 123)
                        .addBox(-4.2F, -3.8F, 0.0F, 8.4F, 7.6F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, -0.06F, 0.00F, 0.00F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(33, 156)
                        .addBox(-3.4F, -3.2F, 0.0F, 6.8F, 6.4F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, -0.05F, 0.00F, 0.00F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(287, 156)
                        .addBox(-2.6F, -2.6F, 0.0F, 5.2F, 5.2F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, -0.03F, 0.00F, 0.00F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(204, 212)
                        .addBox(-1.9F, -2.0F, 0.0F, 3.8F, 4.0F, 9.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 10.0F, 0.02F, 0.00F, 0.00F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(254, 212)
                        .addBox(-1.3F, -1.4F, 0.0F, 2.6F, 2.8F, 8.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 9.0F, 0.05F, 0.00F, 0.00F));
        PartDefinition tailFinTop = tail6.addOrReplaceChild("tail_fin_top",
                CubeListBuilder.create().texOffs(126, 235)
                        .addBox(-0.5F, -3.4F, 0.0F, 1.0F, 3.4F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -1.4F, 7.5F, -0.22F, 0.00F, 0.00F));
        PartDefinition tailFinBot = tail6.addOrReplaceChild("tail_fin_bot",
                CubeListBuilder.create().texOffs(141, 235)
                        .addBox(-0.5F, 0.0F, 0.8F, 1.0F, 2.8F, 4.5F),
                PartPose.offsetAndRotation(0.0F, 0.6F, 7.5F, 0.15F, 0.00F, 0.00F));
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(102, 123)
                        .addBox(0.0F, -1.8F, -2.0F, 26.0F, 3.6F, 4.5F),
                PartPose.offset(8.5F, -6.0F, -6.0F));
        PartDefinition wingLMembrane = wingL.addOrReplaceChild("wing_l_membrane",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(0.0F, -0.5F, 0.0F, 26.0F, 1.0F, 20.0F),
                PartPose.offset(0.0F, 0.6F, 2.2F));
        PartDefinition wingLFinger = wingL.addOrReplaceChild("wing_l_finger",
                CubeListBuilder.create().texOffs(0, 212)
                        .addBox(0.0F, -0.7F, -1.1F, 22.0F, 1.4F, 2.2F),
                PartPose.offsetAndRotation(25.0F, 0.0F, 1.0F, 0.00F, 0.35F, 0.00F));
        PartDefinition wingLMembrane2 = wingLFinger.addOrReplaceChild("wing_l_membrane2",
                CubeListBuilder.create().texOffs(0, 123)
                        .addBox(-1.0F, -0.4F, 0.0F, 15.0F, 0.8F, 12.0F),
                PartPose.offset(1.0F, 0.0F, 1.0F));
        PartDefinition wingLFore = wingL.addOrReplaceChild("wing_l_fore",
                CubeListBuilder.create().texOffs(343, 123)
                        .addBox(0.0F, -1.2F, -1.8F, 24.0F, 2.4F, 3.6F),
                PartPose.offsetAndRotation(26.0F, 0.0F, 0.0F, 0.00F, -0.45F, 0.00F));
        PartDefinition wingLMembrane3 = wingLFore.addOrReplaceChild("wing_l_membrane3",
                CubeListBuilder.create().texOffs(368, 0)
                        .addBox(0.0F, -0.5F, 0.0F, 24.0F, 1.0F, 19.0F),
                PartPose.offset(0.0F, 0.6F, 1.8F));
        PartDefinition wingLClaw = wingLFore.addOrReplaceChild("wing_l_claw",
                CubeListBuilder.create().texOffs(17, 235)
                        .addBox(-0.9F, -0.9F, -0.9F, 1.8F, 1.8F, 6.0F),
                PartPose.offsetAndRotation(24.0F, 0.0F, 0.0F, -0.35F, 0.00F, 0.00F));
        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(164, 123).mirror()
                        .addBox(-26.0F, -1.8F, -2.0F, 26.0F, 3.6F, 4.5F),
                PartPose.offset(-8.5F, -6.0F, -6.0F));
        PartDefinition wingRMembrane = wingR.addOrReplaceChild("wing_r_membrane",
                CubeListBuilder.create().texOffs(93, 0).mirror()
                        .addBox(-26.0F, -0.5F, 0.0F, 26.0F, 1.0F, 20.0F),
                PartPose.offset(0.0F, 0.6F, 2.2F));
        PartDefinition wingRFinger = wingR.addOrReplaceChild("wing_r_finger",
                CubeListBuilder.create().texOffs(49, 212).mirror()
                        .addBox(-22.0F, -0.7F, -1.1F, 22.0F, 1.4F, 2.2F),
                PartPose.offsetAndRotation(-25.0F, 0.0F, 1.0F, 0.00F, -0.35F, 0.00F));
        PartDefinition wingRMembrane2 = wingRFinger.addOrReplaceChild("wing_r_membrane2",
                CubeListBuilder.create().texOffs(87, 70).mirror()
                        .addBox(-22.0F, -0.5F, 0.0F, 22.0F, 1.0F, 17.0F),
                PartPose.offset(-1.5F, 0.0F, 1.1F));
        PartDefinition wingRFore = wingR.addOrReplaceChild("wing_r_fore",
                CubeListBuilder.create().texOffs(399, 123).mirror()
                        .addBox(-24.0F, -1.2F, -1.8F, 24.0F, 2.4F, 3.6F),
                PartPose.offsetAndRotation(-26.0F, 0.0F, 0.0F, 0.00F, 0.45F, 0.00F));
        PartDefinition wingRMembrane3 = wingRFore.addOrReplaceChild("wing_r_membrane3",
                CubeListBuilder.create().texOffs(0, 70).mirror()
                        .addBox(-24.0F, -0.5F, 0.0F, 24.0F, 1.0F, 19.0F),
                PartPose.offset(0.0F, 0.6F, 1.8F));
        PartDefinition wingRClaw = wingRFore.addOrReplaceChild("wing_r_claw",
                CubeListBuilder.create().texOffs(34, 235).mirror()
                        .addBox(-0.9F, -0.9F, -0.9F, 1.8F, 1.8F, 6.0F),
                PartPose.offsetAndRotation(-24.0F, 0.0F, 0.0F, -0.35F, 0.00F, 0.00F));
        PartDefinition legFlThigh = body.addOrReplaceChild("leg_fl_thigh",
                CubeListBuilder.create().texOffs(135, 156)
                        .addBox(-3.6F, -9.0F, -4.0F, 7.2F, 10.0F, 8.0F),
                PartPose.offsetAndRotation(6.8F, -6.5F, -9.5F, 0.28F, 0.00F, 0.00F));
        PartDefinition legFlShin = legFlThigh.addOrReplaceChild("leg_fl_shin",
                CubeListBuilder.create().texOffs(98, 212)
                        .addBox(-3.0F, -9.0F, -3.2F, 6.0F, 9.0F, 6.4F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.52F, 0.00F, 0.00F));
        PartDefinition legFlFoot = legFlShin.addOrReplaceChild("leg_fl_foot",
                CubeListBuilder.create().texOffs(124, 212)
                        .addBox(-3.0F, -1.2F, -5.0F, 6.0F, 2.2F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.24F, 0.00F, 0.00F));
        PartDefinition legFrThigh = body.addOrReplaceChild("leg_fr_thigh",
                CubeListBuilder.create().texOffs(466, 156).mirror()
                        .addBox(-3.0F, -9.0F, -3.5F, 6.0F, 10.0F, 7.0F),
                PartPose.offset(-7.0F, -6.0F, -9.0F));
        PartDefinition legFrShin = legFrThigh.addOrReplaceChild("leg_fr_shin",
                CubeListBuilder.create().texOffs(231, 212).mirror()
                        .addBox(-2.6F, -9.0F, -2.8F, 5.2F, 9.0F, 5.6F),
                PartPose.offset(0.0F, -9.0F, 0.0F));
        PartDefinition legFrFoot = legFrShin.addOrReplaceChild("leg_fr_foot",
                CubeListBuilder.create().texOffs(151, 212).mirror()
                        .addBox(-3.0F, -1.2F, -5.0F, 6.0F, 2.2F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, 0.24F, 0.00F, 0.00F));
        PartDefinition legBlThigh = body.addOrReplaceChild("leg_bl_thigh",
                CubeListBuilder.create().texOffs(267, 123)
                        .addBox(-4.2F, -11.0F, -5.0F, 8.4F, 12.0F, 10.0F),
                PartPose.offsetAndRotation(7.4F, -6.5F, 9.5F, -0.34F, 0.00F, 0.00F));
        PartDefinition legBlShin = legBlThigh.addOrReplaceChild("leg_bl_shin",
                CubeListBuilder.create().texOffs(229, 156)
                        .addBox(-3.4F, -9.0F, -3.6F, 6.8F, 9.0F, 7.2F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.72F, 0.00F, 0.00F));
        PartDefinition legBlFoot = legBlShin.addOrReplaceChild("leg_bl_foot",
                CubeListBuilder.create().texOffs(318, 156)
                        .addBox(-3.4F, -1.4F, -5.6F, 6.8F, 2.4F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.38F, 0.00F, 0.00F));
        PartDefinition legBrThigh = body.addOrReplaceChild("leg_br_thigh",
                CubeListBuilder.create().texOffs(0, 156).mirror()
                        .addBox(-3.6F, -11.0F, -4.5F, 7.2F, 12.0F, 9.0F),
                PartPose.offset(-8.0F, -6.0F, 9.0F));
        PartDefinition legBrShin = legBrThigh.addOrReplaceChild("leg_br_shin",
                CubeListBuilder.create().texOffs(258, 156).mirror()
                        .addBox(-3.4F, -9.0F, -3.6F, 6.8F, 9.0F, 7.2F),
                PartPose.offsetAndRotation(0.0F, -11.0F, 0.0F, 0.72F, 0.00F, 0.00F));
        PartDefinition legBrFoot = legBrShin.addOrReplaceChild("leg_br_foot",
                CubeListBuilder.create().texOffs(349, 156).mirror()
                        .addBox(-3.4F, -1.4F, -5.6F, 6.8F, 2.4F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, 0.0F, -0.38F, 0.00F, 0.00F));
        PartDefinition saddle = body.addOrReplaceChild("saddle",
                CubeListBuilder.create().texOffs(366, 70)
                        .addBox(-6.5F, -1.2F, -7.0F, 13.0F, 2.4F, 14.0F),
                PartPose.offset(0.0F, -8.2F, 1.0F));
        PartDefinition saddlePommel = saddle.addOrReplaceChild("saddle_pommel",
                CubeListBuilder.create().texOffs(276, 212)
                        .addBox(-2.2F, -3.0F, -2.2F, 4.4F, 3.2F, 4.4F),
                PartPose.offset(0.0F, 0.6F, -6.5F));
        PartDefinition saddleCantle = saddle.addOrReplaceChild("saddle_cantle",
                CubeListBuilder.create().texOffs(339, 212)
                        .addBox(-2.8F, -2.4F, -1.6F, 5.6F, 2.6F, 3.2F),
                PartPose.offset(0.0F, 0.6F, 6.5F));
        PartDefinition armor = body.addOrReplaceChild("armor",
                CubeListBuilder.create().texOffs(186, 0)
                        .addBox(-9.4F, -8.4F, -14.4F, 18.8F, 15.8F, 26.8F),
                PartPose.offset(0.0F, 0.0F, 1.0F));
        PartDefinition armorChest = body.addOrReplaceChild("armor_chest",
                CubeListBuilder.create().texOffs(305, 70)
                        .addBox(-10.4F, -8.9F, -8.4F, 20.8F, 15.8F, 9.4F),
                PartPose.offset(0.0F, 1.0F, -12.0F));
        return mesh;
    }
}
