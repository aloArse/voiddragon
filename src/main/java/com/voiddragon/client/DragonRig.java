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
                PartPose.offset(0.0F, 27.7F, 0.0F));
        PartDefinition chest = body.addOrReplaceChild("chest",
                CubeListBuilder.create().texOffs(664, 0)
                        .addBox(-7.5F, -8.5F, -5.4F, 15.0F, 17.0F, 10.8F),
                PartPose.offsetAndRotation(0.0F, 0.8F, -9.8F, 0.10F, 0.00F, 0.00F));
        PartDefinition torso = body.addOrReplaceChild("torso",
                CubeListBuilder.create().texOffs(221, 0)
                        .addBox(-6.5F, -8.2F, -9.6F, 13.0F, 16.4F, 19.2F),
                PartPose.offset(0.0F, 0.0F, 0.4F));
        PartDefinition waist = body.addOrReplaceChild("waist",
                CubeListBuilder.create().texOffs(438, 55)
                        .addBox(-5.1F, -5.9F, -4.2F, 10.2F, 11.8F, 8.4F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 9.6F, -0.05F, 0.00F, 0.00F));
        PartDefinition hips = body.addOrReplaceChild("hips",
                CubeListBuilder.create().texOffs(774, 0)
                        .addBox(-6.8F, -6.9F, -1.0F, 13.6F, 13.8F, 11.2F),
                PartPose.offsetAndRotation(0.0F, -0.5F, 14.2F, -0.08F, 0.00F, 0.00F));
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(664, 55)
                        .addBox(-4.4F, -5.0F, -7.9F, 8.8F, 10.0F, 8.3F),
                PartPose.offsetAndRotation(0.0F, 3.2F, -10.6F, 1.00F, 0.00F, 0.00F));
        PartDefinition neck1Spine = neck1.addOrReplaceChild("neck1_spine",
                CubeListBuilder.create().texOffs(829, 133)
                        .addBox(-0.6F, 0.0F, -1.5F, 1.2F, 3.8F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 4.3F, -3.5F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(0, 90)
                        .addBox(-3.7F, -4.2F, -7.4F, 7.4F, 8.4F, 7.8F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.4F, -0.26F, 0.00F, 0.00F));
        PartDefinition neck2Spine = neck2.addOrReplaceChild("neck2_spine",
                CubeListBuilder.create().texOffs(836, 133)
                        .addBox(-0.6F, 0.0F, -1.5F, 1.2F, 3.8F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 3.5F, -3.3F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(229, 90)
                        .addBox(-3.1F, -3.5F, -6.9F, 6.2F, 7.0F, 7.3F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, -0.24F, 0.00F, 0.00F));
        PartDefinition neck3Spine = neck3.addOrReplaceChild("neck3_spine",
                CubeListBuilder.create().texOffs(843, 133)
                        .addBox(-0.6F, 0.0F, -1.5F, 1.2F, 3.8F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 2.8F, -3.1F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck4 = neck3.addOrReplaceChild("neck4",
                CubeListBuilder.create().texOffs(515, 90)
                        .addBox(-2.7F, -3.0F, -6.4F, 5.4F, 6.0F, 6.8F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.6F, -0.20F, 0.00F, 0.00F));
        PartDefinition neck4Spine = neck4.addOrReplaceChild("neck4_spine",
                CubeListBuilder.create().texOffs(850, 133)
                        .addBox(-0.6F, 0.0F, -1.5F, 1.2F, 3.8F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 2.3F, -2.9F, 0.62F, 0.00F, 0.00F));
        PartDefinition neck5 = neck4.addOrReplaceChild("neck5",
                CubeListBuilder.create().texOffs(776, 90)
                        .addBox(-2.4F, -2.6F, -5.7F, 4.8F, 5.2F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -5.2F, -0.16F, 0.00F, 0.00F));
        PartDefinition neck5Spine = neck5.addOrReplaceChild("neck5_spine",
                CubeListBuilder.create().texOffs(857, 133)
                        .addBox(-0.6F, 0.0F, -1.5F, 1.2F, 3.8F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 1.9F, -2.5F, 0.62F, 0.00F, 0.00F));
        PartDefinition bellyChest = body.addOrReplaceChild("belly_chest",
                CubeListBuilder.create().texOffs(274, 55)
                        .addBox(-5.2F, -9.7F, -5.0F, 10.4F, 1.8F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -9.8F, 0.10F, 0.00F, 0.00F));
        PartDefinition bellyTorso = body.addOrReplaceChild("belly_torso",
                CubeListBuilder.create().texOffs(717, 0)
                        .addBox(-4.6F, -9.6F, -9.4F, 9.2F, 1.8F, 18.8F),
                PartPose.offset(0.0F, 0.0F, 0.4F));
        PartDefinition bellyWaist = body.addOrReplaceChild("belly_waist",
                CubeListBuilder.create().texOffs(31, 90)
                        .addBox(-3.6F, -7.6F, -4.0F, 7.2F, 1.6F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 9.6F, -0.05F, 0.00F, 0.00F));
        PartDefinition bellyHips = body.addOrReplaceChild("belly_hips",
                CubeListBuilder.create().texOffs(315, 55)
                        .addBox(-5.0F, -8.5F, -0.8F, 10.0F, 1.7F, 10.4F),
                PartPose.offsetAndRotation(0.0F, -0.5F, 14.2F, -0.08F, 0.00F, 0.00F));
        PartDefinition ridgeChest = body.addOrReplaceChild("ridge_chest",
                CubeListBuilder.create().texOffs(799, 90)
                        .addBox(-1.3F, -10.6F, -5.2F, 2.6F, 3.6F, 10.4F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -9.8F, 0.10F, 0.00F, 0.00F));
        PartDefinition ridgeTorso = body.addOrReplaceChild("ridge_torso",
                CubeListBuilder.create().texOffs(418, 90)
                        .addBox(-1.2F, -10.4F, -9.4F, 2.4F, 3.8F, 18.8F),
                PartPose.offset(0.0F, 0.0F, 0.4F));
        PartDefinition ridgeWaist = body.addOrReplaceChild("ridge_waist",
                CubeListBuilder.create().texOffs(279, 133)
                        .addBox(-1.0F, -8.4F, -4.0F, 2.0F, 2.8F, 8.0F),
                PartPose.offsetAndRotation(0.0F, -0.7F, 9.6F, -0.05F, 0.00F, 0.00F));
        PartDefinition ridgeHips = body.addOrReplaceChild("ridge_hips",
                CubeListBuilder.create().texOffs(981, 90)
                        .addBox(-1.1F, -9.8F, -0.8F, 2.2F, 3.2F, 10.4F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 14.2F, -0.08F, 0.00F, 0.00F));
        PartDefinition scapulaL = chest.addOrReplaceChild("scapula_l",
                CubeListBuilder.create().texOffs(676, 133)
                        .addBox(-1.1F, 0.0F, -2.4F, 2.1F, 5.2F, 2.4F),
                PartPose.offsetAndRotation(7.2F, 5.0F, -0.6F, 0.00F, 0.00F, -0.55F));
        PartDefinition scapulaR = chest.addOrReplaceChild("scapula_r",
                CubeListBuilder.create().texOffs(685, 133).mirror()
                        .addBox(-1.1F, 0.0F, -2.4F, 2.1F, 5.2F, 2.4F),
                PartPose.offsetAndRotation(-7.2F, 5.0F, -0.6F, 0.00F, -0.00F, 0.55F));
        PartDefinition scapulaL2 = chest.addOrReplaceChild("scapula_l2",
                CubeListBuilder.create().texOffs(712, 133)
                        .addBox(-0.9F, 0.0F, -2.0F, 1.9F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(8.0F, 3.6F, 2.0F, 0.00F, 0.00F, -0.75F));
        PartDefinition scapulaR2 = chest.addOrReplaceChild("scapula_r2",
                CubeListBuilder.create().texOffs(721, 133).mirror()
                        .addBox(-0.9F, 0.0F, -2.0F, 1.9F, 4.4F, 2.2F),
                PartPose.offsetAndRotation(-8.0F, 3.6F, 2.0F, 0.00F, -0.00F, 0.75F));
        PartDefinition hipSpurL = hips.addOrReplaceChild("hip_spur_l",
                CubeListBuilder.create().texOffs(694, 133)
                        .addBox(-0.8F, 0.0F, -2.2F, 1.6F, 5.0F, 2.4F),
                PartPose.offsetAndRotation(7.6F, 4.0F, 2.8F, 0.00F, 0.00F, -0.90F));
        PartDefinition hipSpurR = hips.addOrReplaceChild("hip_spur_r",
                CubeListBuilder.create().texOffs(703, 133).mirror()
                        .addBox(-0.8F, 0.0F, -2.2F, 1.6F, 5.0F, 2.4F),
                PartPose.offsetAndRotation(-7.6F, 4.0F, 2.8F, 0.00F, -0.00F, 0.90F));
        PartDefinition head = neck5.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(964, 0)
                        .addBox(-5.6F, -4.4F, -10.8F, 11.2F, 8.8F, 11.5F),
                PartPose.offsetAndRotation(0.0F, 0.4F, -4.4F, -0.12F, 0.00F, 0.00F));
        PartDefinition muzzle = head.addOrReplaceChild("muzzle",
                CubeListBuilder.create().texOffs(590, 55)
                        .addBox(-3.6F, -2.4F, -11.0F, 7.2F, 4.8F, 11.0F),
                PartPose.offsetAndRotation(0.0F, -1.8F, -8.4F, 0.04F, 0.00F, 0.00F));
        PartDefinition nasalBridge = head.addOrReplaceChild("nasal_bridge",
                CubeListBuilder.create().texOffs(753, 90)
                        .addBox(-3.1F, -0.8F, -5.4F, 6.2F, 1.6F, 5.4F),
                PartPose.offset(0.0F, 2.4F, -5.6F));
        PartDefinition snoutTip = muzzle.addOrReplaceChild("snout_tip",
                CubeListBuilder.create().texOffs(162, 133)
                        .addBox(-2.7F, -1.9F, -3.2F, 5.4F, 3.8F, 3.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -9.0F, -0.10F, 0.00F, 0.00F));
        PartDefinition jaw = head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(356, 55)
                        .addBox(-3.4F, -2.4F, -14.4F, 6.8F, 2.4F, 14.4F),
                PartPose.offsetAndRotation(0.0F, -2.9F, -4.2F, 0.16F, 0.00F, 0.00F));
        PartDefinition chin = jaw.addOrReplaceChild("chin",
                CubeListBuilder.create().texOffs(816, 133)
                        .addBox(-1.8F, -0.9F, -1.5F, 3.6F, 1.1F, 1.5F),
                PartPose.offsetAndRotation(0.0F, 0.2F, -13.4F, 0.10F, 0.00F, 0.00F));
        PartDefinition nostrilL = snoutTip.addOrReplaceChild("nostril_l",
                CubeListBuilder.create().texOffs(1018, 133)
                        .addBox(-0.2F, -0.5F, -0.6F, 0.4F, 0.9F, 1.2F),
                PartPose.offset(2.8F, 0.2F, -1.5F));
        PartDefinition nostrilR = snoutTip.addOrReplaceChild("nostril_r",
                CubeListBuilder.create().texOffs(0, 165).mirror()
                        .addBox(-0.2F, -0.5F, -0.6F, 0.4F, 0.9F, 1.2F),
                PartPose.offset(-2.8F, 0.2F, -1.5F));
        PartDefinition teethL = muzzle.addOrReplaceChild("teeth_l",
                CubeListBuilder.create().texOffs(756, 133)
                        .addBox(-0.3F, -2.4F, -1.1F, 0.7F, 2.4F, 6.4F),
                PartPose.offset(2.3F, -2.3F, -4.6F));
        PartDefinition teethR = muzzle.addOrReplaceChild("teeth_r",
                CubeListBuilder.create().texOffs(771, 133).mirror()
                        .addBox(-0.3F, -2.4F, -1.1F, 0.7F, 2.4F, 6.4F),
                PartPose.offset(-2.3F, -2.3F, -4.6F));
        PartDefinition jawMuscleL = head.addOrReplaceChild("jaw_muscle_l",
                CubeListBuilder.create().texOffs(390, 133)
                        .addBox(-0.8F, -2.0F, -3.8F, 1.6F, 4.0F, 7.6F),
                PartPose.offsetAndRotation(5.2F, -1.5F, -4.2F, 0.00F, 0.00F, 0.18F));
        PartDefinition jawMuscleR = head.addOrReplaceChild("jaw_muscle_r",
                CubeListBuilder.create().texOffs(411, 133).mirror()
                        .addBox(-0.8F, -2.0F, -3.8F, 1.6F, 4.0F, 7.6F),
                PartPose.offsetAndRotation(-5.2F, -1.5F, -4.2F, 0.00F, -0.00F, -0.18F));
        PartDefinition cheekRidgeL = head.addOrReplaceChild("cheek_ridge_l",
                CubeListBuilder.create().texOffs(730, 133)
                        .addBox(-0.6F, -0.6F, -1.3F, 1.1F, 1.1F, 5.4F),
                PartPose.offsetAndRotation(5.8F, 0.5F, -6.4F, 0.00F, -0.22F, -0.55F));
        PartDefinition cheekRidgeR = head.addOrReplaceChild("cheek_ridge_r",
                CubeListBuilder.create().texOffs(743, 133).mirror()
                        .addBox(-0.6F, -0.6F, -1.3F, 1.1F, 1.1F, 5.4F),
                PartPose.offsetAndRotation(-5.8F, 0.5F, -6.4F, 0.00F, 0.22F, 0.55F));
        PartDefinition browL = head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(241, 133)
                        .addBox(-1.2F, -0.8F, -4.6F, 2.5F, 1.6F, 7.4F),
                PartPose.offsetAndRotation(4.6F, 1.9F, -5.2F, 0.00F, 0.00F, -0.34F));
        PartDefinition browR = head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(260, 133).mirror()
                        .addBox(-1.2F, -0.8F, -4.6F, 2.5F, 1.6F, 7.4F),
                PartPose.offsetAndRotation(-4.6F, 1.9F, -5.2F, 0.00F, -0.00F, 0.34F));
        PartDefinition eyeL = head.addOrReplaceChild("eye_l",
                CubeListBuilder.create().texOffs(864, 133)
                        .addBox(-0.2F, -0.9F, -2.1F, 0.7F, 1.8F, 4.2F),
                PartPose.offset(5.5F, 0.6F, -5.4F));
        PartDefinition eyeR = head.addOrReplaceChild("eye_r",
                CubeListBuilder.create().texOffs(875, 133).mirror()
                        .addBox(-0.4F, -0.9F, -2.1F, 0.7F, 1.8F, 4.2F),
                PartPose.offset(-5.5F, 0.6F, -5.4F));
        PartDefinition hornL1 = head.addOrReplaceChild("horn_l1",
                CubeListBuilder.create().texOffs(900, 90)
                        .addBox(-1.1F, -1.2F, -3.0F, 2.3F, 2.5F, 11.5F),
                PartPose.offsetAndRotation(2.4F, 3.4F, -7.8F, -0.46F, 0.18F, 0.00F));
        PartDefinition hornR1 = head.addOrReplaceChild("horn_r1",
                CubeListBuilder.create().texOffs(929, 90).mirror()
                        .addBox(-1.1F, -1.2F, -3.0F, 2.3F, 2.5F, 11.5F),
                PartPose.offsetAndRotation(-2.4F, 3.4F, -7.8F, -0.46F, -0.18F, -0.00F));
        PartDefinition legFlThigh = body.addOrReplaceChild("leg_fl_thigh",
                CubeListBuilder.create().texOffs(699, 55)
                        .addBox(-3.5F, -11.6F, -4.5F, 7.0F, 11.6F, 9.0F),
                PartPose.offsetAndRotation(7.0F, -5.5F, -8.6F, 0.16F, 0.00F, 0.00F));
        PartDefinition legFrThigh = body.addOrReplaceChild("leg_fr_thigh",
                CubeListBuilder.create().texOffs(732, 55).mirror()
                        .addBox(-3.5F, -11.6F, -4.5F, 7.0F, 11.6F, 9.0F),
                PartPose.offsetAndRotation(-7.0F, -5.5F, -8.6F, 0.16F, -0.00F, -0.00F));
        PartDefinition legFlHaunch = legFlThigh.addOrReplaceChild("leg_fl_haunch",
                CubeListBuilder.create().texOffs(516, 55)
                        .addBox(-3.9F, -4.2F, -5.2F, 7.8F, 8.4F, 10.4F),
                PartPose.offset(0.6F, -1.8F, -0.6F));
        PartDefinition legFrHaunch = legFrThigh.addOrReplaceChild("leg_fr_haunch",
                CubeListBuilder.create().texOffs(553, 55).mirror()
                        .addBox(-3.9F, -4.2F, -5.2F, 7.8F, 8.4F, 10.4F),
                PartPose.offset(-0.6F, -1.8F, -0.6F));
        PartDefinition legFlShin = legFlThigh.addOrReplaceChild("leg_fl_shin",
                CubeListBuilder.create().texOffs(540, 90)
                        .addBox(-2.7F, -9.4F, -3.0F, 5.4F, 9.4F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -11.2F, 0.4F, -0.30F, 0.00F, 0.00F));
        PartDefinition legFrShin = legFrThigh.addOrReplaceChild("leg_fr_shin",
                CubeListBuilder.create().texOffs(563, 90).mirror()
                        .addBox(-2.7F, -9.4F, -3.0F, 5.4F, 9.4F, 6.0F),
                PartPose.offsetAndRotation(-0.0F, -11.2F, 0.4F, -0.30F, -0.00F, -0.00F));
        PartDefinition legFlCalf = legFlShin.addOrReplaceChild("leg_fl_calf",
                CubeListBuilder.create().texOffs(310, 90)
                        .addBox(-3.0F, -3.5F, -3.6F, 6.0F, 7.0F, 7.2F),
                PartPose.offset(0.0F, -3.0F, -0.8F));
        PartDefinition legFrCalf = legFrShin.addOrReplaceChild("leg_fr_calf",
                CubeListBuilder.create().texOffs(337, 90).mirror()
                        .addBox(-3.0F, -3.5F, -3.6F, 6.0F, 7.0F, 7.2F),
                PartPose.offset(-0.0F, -3.0F, -0.8F));
        PartDefinition legFlFoot = legFlShin.addOrReplaceChild("leg_fl_foot",
                CubeListBuilder.create().texOffs(461, 90)
                        .addBox(-2.9F, -1.7F, -4.4F, 5.8F, 3.4F, 7.0F),
                PartPose.offsetAndRotation(0.0F, -9.0F, -0.2F, 0.22F, 0.00F, 0.00F));
        PartDefinition legFrFoot = legFrShin.addOrReplaceChild("leg_fr_foot",
                CubeListBuilder.create().texOffs(488, 90).mirror()
                        .addBox(-2.9F, -1.7F, -4.4F, 5.8F, 3.4F, 7.0F),
                PartPose.offsetAndRotation(-0.0F, -9.0F, -0.2F, 0.22F, -0.00F, -0.00F));
        PartDefinition legFlTalon1 = legFlFoot.addOrReplaceChild("leg_fl_talon1",
                CubeListBuilder.create().texOffs(990, 133)
                        .addBox(-0.5F, -1.0F, -1.7F, 1.0F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.9F, -1.3F, -3.6F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon1 = legFrFoot.addOrReplaceChild("leg_fr_talon1",
                CubeListBuilder.create().texOffs(997, 133).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 1.0F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.9F, -1.3F, -3.6F, 0.55F, -0.00F, -0.00F));
        PartDefinition legFlTalon2 = legFlFoot.addOrReplaceChild("leg_fl_talon2",
                CubeListBuilder.create().texOffs(976, 133)
                        .addBox(-0.5F, -1.0F, -1.9F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(0.0F, -1.3F, -3.6F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon2 = legFrFoot.addOrReplaceChild("leg_fr_talon2",
                CubeListBuilder.create().texOffs(983, 133).mirror()
                        .addBox(-0.5F, -1.0F, -1.9F, 1.0F, 1.1F, 2.4F),
                PartPose.offsetAndRotation(-0.0F, -1.3F, -3.6F, 0.55F, -0.00F, -0.00F));
        PartDefinition legFlTalon3 = legFlFoot.addOrReplaceChild("leg_fl_talon3",
                CubeListBuilder.create().texOffs(1004, 133)
                        .addBox(-0.5F, -1.0F, -1.7F, 1.0F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(-1.9F, -1.3F, -3.6F, 0.55F, 0.00F, 0.00F));
        PartDefinition legFrTalon3 = legFrFoot.addOrReplaceChild("leg_fr_talon3",
                CubeListBuilder.create().texOffs(1011, 133).mirror()
                        .addBox(-0.5F, -1.0F, -1.7F, 1.0F, 1.1F, 2.2F),
                PartPose.offsetAndRotation(1.9F, -1.3F, -3.6F, 0.55F, -0.00F, -0.00F));
        PartDefinition legBlThigh = body.addOrReplaceChild("leg_bl_thigh",
                CubeListBuilder.create().texOffs(98, 55)
                        .addBox(-4.7F, -12.4F, -5.6F, 9.4F, 12.4F, 11.2F),
                PartPose.offsetAndRotation(7.4F, -4.8F, 10.0F, -0.34F, 0.00F, 0.00F));
        PartDefinition legBrThigh = body.addOrReplaceChild("leg_br_thigh",
                CubeListBuilder.create().texOffs(139, 55).mirror()
                        .addBox(-4.7F, -12.4F, -5.6F, 9.4F, 12.4F, 11.2F),
                PartPose.offsetAndRotation(-7.4F, -4.8F, 10.0F, -0.34F, -0.00F, -0.00F));
        PartDefinition legBlHaunch = legBlThigh.addOrReplaceChild("leg_bl_haunch",
                CubeListBuilder.create().texOffs(874, 0)
                        .addBox(-5.2F, -4.6F, -6.2F, 10.4F, 9.2F, 12.4F),
                PartPose.offset(0.6F, -2.0F, -0.6F));
        PartDefinition legBrHaunch = legBrThigh.addOrReplaceChild("leg_br_haunch",
                CubeListBuilder.create().texOffs(919, 0).mirror()
                        .addBox(-5.2F, -4.6F, -6.2F, 10.4F, 9.2F, 12.4F),
                PartPose.offset(-0.6F, -2.0F, -0.6F));
        PartDefinition legBlShin = legBlThigh.addOrReplaceChild("leg_bl_shin",
                CubeListBuilder.create().texOffs(364, 90)
                        .addBox(-3.0F, -10.2F, -3.3F, 6.0F, 10.2F, 6.6F),
                PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.66F, 0.00F, 0.00F));
        PartDefinition legBrShin = legBrThigh.addOrReplaceChild("leg_br_shin",
                CubeListBuilder.create().texOffs(391, 90).mirror()
                        .addBox(-3.0F, -10.2F, -3.3F, 6.0F, 10.2F, 6.6F),
                PartPose.offsetAndRotation(-0.0F, -12.0F, 0.0F, 0.66F, -0.00F, -0.00F));
        PartDefinition legBlCalf = legBlShin.addOrReplaceChild("leg_bl_calf",
                CubeListBuilder.create().texOffs(62, 90)
                        .addBox(-3.3F, -3.6F, -3.9F, 6.6F, 7.2F, 7.8F),
                PartPose.offset(0.0F, -3.2F, -0.8F));
        PartDefinition legBrCalf = legBrShin.addOrReplaceChild("leg_br_calf",
                CubeListBuilder.create().texOffs(93, 90).mirror()
                        .addBox(-3.3F, -3.6F, -3.9F, 6.6F, 7.2F, 7.8F),
                PartPose.offset(-0.0F, -3.2F, -0.8F));
        PartDefinition legBlFoot = legBlShin.addOrReplaceChild("leg_bl_foot",
                CubeListBuilder.create().texOffs(940, 55)
                        .addBox(-3.7F, -1.9F, -5.4F, 7.4F, 3.6F, 8.6F),
                PartPose.offsetAndRotation(0.0F, -9.6F, -0.4F, -0.32F, 0.00F, 0.00F));
        PartDefinition legBrFoot = legBrShin.addOrReplaceChild("leg_br_foot",
                CubeListBuilder.create().texOffs(973, 55).mirror()
                        .addBox(-3.7F, -1.9F, -5.4F, 7.4F, 3.6F, 8.6F),
                PartPose.offsetAndRotation(-0.0F, -9.6F, -0.4F, -0.32F, -0.00F, -0.00F));
        PartDefinition legBlTalon1 = legBlFoot.addOrReplaceChild("leg_bl_talon1",
                CubeListBuilder.create().texOffs(930, 133)
                        .addBox(-0.5F, -1.1F, -1.9F, 1.0F, 1.2F, 2.4F),
                PartPose.offsetAndRotation(2.0F, -1.4F, -4.2F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon1 = legBrFoot.addOrReplaceChild("leg_br_talon1",
                CubeListBuilder.create().texOffs(937, 133).mirror()
                        .addBox(-0.5F, -1.1F, -1.9F, 1.0F, 1.2F, 2.4F),
                PartPose.offsetAndRotation(-2.0F, -1.4F, -4.2F, 0.55F, -0.00F, -0.00F));
        PartDefinition legBlTalon2 = legBlFoot.addOrReplaceChild("leg_bl_talon2",
                CubeListBuilder.create().texOffs(912, 133)
                        .addBox(-0.5F, -1.1F, -2.1F, 1.0F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(0.0F, -1.4F, -4.2F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon2 = legBrFoot.addOrReplaceChild("leg_br_talon2",
                CubeListBuilder.create().texOffs(921, 133).mirror()
                        .addBox(-0.5F, -1.1F, -2.1F, 1.0F, 1.2F, 2.6F),
                PartPose.offsetAndRotation(-0.0F, -1.4F, -4.2F, 0.55F, -0.00F, -0.00F));
        PartDefinition legBlTalon3 = legBlFoot.addOrReplaceChild("leg_bl_talon3",
                CubeListBuilder.create().texOffs(944, 133)
                        .addBox(-0.5F, -1.1F, -1.9F, 1.0F, 1.2F, 2.4F),
                PartPose.offsetAndRotation(-2.0F, -1.4F, -4.2F, 0.55F, 0.00F, 0.00F));
        PartDefinition legBrTalon3 = legBrFoot.addOrReplaceChild("leg_br_talon3",
                CubeListBuilder.create().texOffs(951, 133).mirror()
                        .addBox(-0.5F, -1.1F, -1.9F, 1.0F, 1.2F, 2.4F),
                PartPose.offsetAndRotation(2.0F, -1.4F, -4.2F, 0.55F, -0.00F, -0.00F));
        PartDefinition saddle = body.addOrReplaceChild("saddle",
                CubeListBuilder.create().texOffs(825, 0)
                        .addBox(-5.6F, -1.0F, -6.4F, 11.2F, 2.0F, 12.8F),
                PartPose.offset(0.0F, 6.4F, 1.5F));
        PartDefinition saddlePommel = saddle.addOrReplaceChild("saddle_pommel",
                CubeListBuilder.create().texOffs(354, 133)
                        .addBox(-1.9F, -2.6F, -1.9F, 3.8F, 2.8F, 3.8F),
                PartPose.offset(0.0F, 1.0F, -5.6F));
        PartDefinition saddleCantle = saddle.addOrReplaceChild("saddle_cantle",
                CubeListBuilder.create().texOffs(432, 133)
                        .addBox(-2.2F, -2.2F, -1.4F, 4.4F, 2.4F, 2.8F),
                PartPose.offset(0.0F, 1.0F, 5.6F));
        PartDefinition armorChest = body.addOrReplaceChild("armor_chest",
                CubeListBuilder.create().texOffs(550, 0)
                        .addBox(-9.0F, -8.6F, -5.5F, 18.0F, 15.6F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 1.2F, -9.8F, 0.10F, 0.00F, 0.00F));
        PartDefinition armorTorso = body.addOrReplaceChild("armor_torso",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-7.8F, -8.3F, -9.7F, 15.6F, 15.0F, 19.4F),
                PartPose.offset(0.0F, 0.0F, 0.4F));
        PartDefinition armorHips = body.addOrReplaceChild("armor_hips",
                CubeListBuilder.create().texOffs(609, 0)
                        .addBox(-8.1F, -7.0F, -1.1F, 16.2F, 14.0F, 11.4F),
                PartPose.offsetAndRotation(0.0F, -0.6F, 14.2F, -0.08F, 0.00F, 0.00F));
        PartDefinition armorCollar = body.addOrReplaceChild("armor_collar",
                CubeListBuilder.create().texOffs(475, 55)
                        .addBox(-7.0F, -1.5F, -3.2F, 14.0F, 3.0F, 6.4F),
                PartPose.offsetAndRotation(0.0F, 3.8F, -8.4F, 0.45F, 0.00F, 0.00F));
        PartDefinition saddleStrapL = body.addOrReplaceChild("saddle_strap_l",
                CubeListBuilder.create().texOffs(0, 133)
                        .addBox(-0.6F, -8.6F, -5.2F, 1.2F, 10.8F, 10.4F),
                PartPose.offset(6.6F, 0.0F, 1.5F));
        PartDefinition saddleStrapR = body.addOrReplaceChild("saddle_strap_r",
                CubeListBuilder.create().texOffs(23, 133).mirror()
                        .addBox(-0.6F, -8.6F, -5.2F, 1.2F, 10.8F, 10.4F),
                PartPose.offset(-6.6F, 0.0F, 1.5F));
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(399, 55)
                        .addBox(-4.0F, -4.0F, -1.4F, 8.0F, 8.0F, 11.4F),
                PartPose.offsetAndRotation(0.0F, -2.0F, 15.0F, -0.20F, 0.00F, 0.00F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(627, 55)
                        .addBox(-3.5F, -3.5F, -1.4F, 7.0F, 7.0F, 11.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.8F, -0.06F, 0.00F, 0.00F));
        PartDefinition tail2Spike = tail2.addOrReplaceChild("tail2_spike",
                CubeListBuilder.create().texOffs(601, 133)
                        .addBox(-0.7F, -0.2F, -2.6F, 1.3F, 3.4F, 6.0F),
                PartPose.offset(0.0F, 3.6F, 2.6F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(839, 55)
                        .addBox(-3.0F, -3.0F, -1.4F, 6.0F, 6.0F, 10.6F),
                PartPose.offset(0.0F, 0.0F, 8.4F));
        PartDefinition tail3Spike = tail3.addOrReplaceChild("tail3_spike",
                CubeListBuilder.create().texOffs(616, 133)
                        .addBox(-0.7F, -0.2F, -2.6F, 1.3F, 3.4F, 6.0F),
                PartPose.offset(0.0F, 3.1F, 2.6F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(198, 90)
                        .addBox(-2.5F, -2.5F, -1.4F, 5.0F, 5.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, 0.05F, 0.00F, 0.00F));
        PartDefinition tail4Spike = tail4.addOrReplaceChild("tail4_spike",
                CubeListBuilder.create().texOffs(631, 133)
                        .addBox(-0.7F, -0.2F, -2.6F, 1.3F, 3.4F, 6.0F),
                PartPose.offset(0.0F, 2.6F, 2.6F));
        PartDefinition tail5 = tail4.addOrReplaceChild("tail5",
                CubeListBuilder.create().texOffs(586, 90)
                        .addBox(-2.0F, -2.0F, -1.4F, 4.0F, 4.0F, 9.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 7.4F, 0.09F, 0.00F, 0.00F));
        PartDefinition tail5Spike = tail5.addOrReplaceChild("tail5_spike",
                CubeListBuilder.create().texOffs(646, 133)
                        .addBox(-0.7F, -0.2F, -2.6F, 1.3F, 3.4F, 6.0F),
                PartPose.offset(0.0F, 2.1F, 2.6F));
        PartDefinition tail6 = tail5.addOrReplaceChild("tail6",
                CubeListBuilder.create().texOffs(958, 90)
                        .addBox(-1.5F, -1.5F, -1.4F, 3.0F, 3.0F, 8.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 6.8F, 0.12F, 0.00F, 0.00F));
        PartDefinition tail6Spike = tail6.addOrReplaceChild("tail6_spike",
                CubeListBuilder.create().texOffs(661, 133)
                        .addBox(-0.7F, -0.2F, -2.6F, 1.3F, 3.4F, 6.0F),
                PartPose.offset(0.0F, 1.6F, 2.6F));
        PartDefinition tail7 = tail6.addOrReplaceChild("tail7",
                CubeListBuilder.create().texOffs(371, 133)
                        .addBox(-1.0F, -1.0F, -1.4F, 2.0F, 2.0F, 7.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 5.8F, 0.12F, 0.00F, 0.00F));
        PartDefinition spadeTop = tail7.addOrReplaceChild("spade_top",
                CubeListBuilder.create().texOffs(447, 133)
                        .addBox(-0.8F, -0.2F, 0.0F, 1.6F, 3.8F, 6.8F),
                PartPose.offsetAndRotation(0.0F, -0.2F, 5.4F, -0.34F, 0.00F, 0.00F));
        PartDefinition spadeBot = tail7.addOrReplaceChild("spade_bot",
                CubeListBuilder.create().texOffs(512, 133)
                        .addBox(-0.8F, 0.0F, 0.0F, 1.6F, 2.9F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 0.2F, 5.6F, 0.05F, 0.00F, 0.00F));
        PartDefinition spadeLeft = tail7.addOrReplaceChild("spade_left",
                CubeListBuilder.create().texOffs(886, 133)
                        .addBox(-0.3F, -0.6F, 0.0F, 0.6F, 1.1F, 4.8F),
                PartPose.offsetAndRotation(0.8F, 0.0F, 5.0F, 0.00F, 0.00F, -0.70F));
        PartDefinition spadeRight = tail7.addOrReplaceChild("spade_right",
                CubeListBuilder.create().texOffs(899, 133).mirror()
                        .addBox(-0.3F, -0.6F, 0.0F, 0.6F, 1.1F, 4.8F),
                PartPose.offsetAndRotation(-0.8F, 0.0F, 5.0F, 0.00F, -0.00F, 0.70F));
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(765, 55)
                        .addBox(0.0F, -2.4F, -2.6F, 13.0F, 4.8F, 5.2F),
                PartPose.offsetAndRotation(6.8F, 2.8F, -5.4F, 0.00F, -1.16F, -0.46F));
        PartDefinition wingLDeltoid = wingL.addOrReplaceChild("wing_l_deltoid",
                CubeListBuilder.create().texOffs(256, 90)
                        .addBox(0.0F, -3.0F, -2.0F, 7.0F, 6.4F, 6.4F),
                PartPose.offset(0.8F, 0.6F, -1.8F));
        PartDefinition wingLFore = wingL.addOrReplaceChild("wing_l_fore",
                CubeListBuilder.create().texOffs(124, 90)
                        .addBox(0.0F, -1.8F, -2.0F, 13.5F, 3.6F, 4.0F),
                PartPose.offsetAndRotation(13.0F, 0.0F, 0.0F, 0.00F, -0.64F, 0.28F));
        PartDefinition wingLHand = wingLFore.addOrReplaceChild("wing_l_hand",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(13.5F, 0.0F, 0.0F, 0.00F, 0.50F, -0.20F));
        PartDefinition wingLClaw = wingLHand.addOrReplaceChild("wing_l_claw",
                CubeListBuilder.create().texOffs(571, 133)
                        .addBox(0.0F, -0.9F, -0.9F, 1.8F, 1.8F, 5.4F),
                PartPose.offsetAndRotation(0.0F, 0.9F, -0.6F, -0.55F, 0.00F, 0.00F));
        PartDefinition wingLF1a = wingLHand.addOrReplaceChild("wing_l_f1a",
                CubeListBuilder.create().texOffs(687, 90)
                        .addBox(0.0F, -1.2F, -1.2F, 14.0F, 2.4F, 2.4F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.05F, 0.30F, -0.03F));
        PartDefinition wingLF1b = wingLF1a.addOrReplaceChild("wing_l_f1b",
                CubeListBuilder.create().texOffs(108, 133)
                        .addBox(0.0F, -1.0F, -1.0F, 11.0F, 1.9F, 1.9F),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, 0.10F, 0.00F, 0.00F));
        PartDefinition wingLF2a = wingLHand.addOrReplaceChild("wing_l_f2a",
                CubeListBuilder.create().texOffs(613, 90)
                        .addBox(0.0F, -1.1F, -1.1F, 16.5F, 2.1F, 2.1F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.10F, 0.05F, -0.06F));
        PartDefinition wingLF2b = wingLF2a.addOrReplaceChild("wing_l_f2b",
                CubeListBuilder.create().texOffs(46, 133)
                        .addBox(0.0F, -0.8F, -0.8F, 13.0F, 1.7F, 1.7F),
                PartPose.offsetAndRotation(16.5F, 0.0F, 0.0F, 0.16F, 0.00F, 0.00F));
        PartDefinition wingLF3a = wingLHand.addOrReplaceChild("wing_l_f3a",
                CubeListBuilder.create().texOffs(826, 90)
                        .addBox(0.0F, -0.9F, -0.9F, 16.0F, 1.8F, 1.8F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.15F, -0.22F, -0.09F));
        PartDefinition wingLF3b = wingLF3a.addOrReplaceChild("wing_l_f3b",
                CubeListBuilder.create().texOffs(300, 133)
                        .addBox(0.0F, -0.7F, -0.7F, 12.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(16.0F, 0.0F, 0.0F, 0.22F, 0.00F, 0.00F));
        PartDefinition wingLF4a = wingLHand.addOrReplaceChild("wing_l_f4a",
                CubeListBuilder.create().texOffs(179, 133)
                        .addBox(0.0F, -0.8F, -0.8F, 13.0F, 1.5F, 1.5F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.20F, -0.52F, -0.12F));
        PartDefinition wingLF4b = wingLF4a.addOrReplaceChild("wing_l_f4b",
                CubeListBuilder.create().texOffs(466, 133)
                        .addBox(0.0F, -0.6F, -0.6F, 9.5F, 1.2F, 1.2F),
                PartPose.offsetAndRotation(13.0F, 0.0F, 0.0F, 0.28F, 0.00F, 0.00F));
        PartDefinition wingLF5a = wingLHand.addOrReplaceChild("wing_l_f5a",
                CubeListBuilder.create().texOffs(529, 133)
                        .addBox(0.0F, -0.6F, -0.6F, 9.0F, 1.2F, 1.2F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.25F, -0.85F, -0.15F));
        PartDefinition wingLF5b = wingLF5a.addOrReplaceChild("wing_l_f5b",
                CubeListBuilder.create().texOffs(786, 133)
                        .addBox(0.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(9.0F, 0.0F, 0.0F, 0.34F, 0.00F, 0.00F));
        PartDefinition wingLTipclaw = wingLF1b.addOrReplaceChild("wing_l_tipclaw",
                CubeListBuilder.create().texOffs(958, 133)
                        .addBox(0.0F, -0.5F, -0.5F, 2.6F, 1.0F, 1.0F),
                PartPose.offset(11.0F, 0.0F, 0.0F));
        PartDefinition wingLM1 = wingLHand.addOrReplaceChild("wing_l_m1",
                CubeListBuilder.create().texOffs(286, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 24.5F, 0.6F, 9.3F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -3.2F, 0.00F, 0.17F, -0.05F));
        PartDefinition wingLM2 = wingLHand.addOrReplaceChild("wing_l_m2",
                CubeListBuilder.create().texOffs(71, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 27.4F, 0.6F, 10.4F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -3.5F, 0.00F, -0.08F, -0.10F));
        PartDefinition wingLM3 = wingLHand.addOrReplaceChild("wing_l_m3",
                CubeListBuilder.create().texOffs(420, 0)
                        .addBox(0.0F, -0.3F, 0.0F, 22.1F, 0.6F, 10.2F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -3.5F, 0.00F, -0.37F, -0.15F));
        PartDefinition wingLM4 = wingLHand.addOrReplaceChild("wing_l_m4",
                CubeListBuilder.create().texOffs(0, 55)
                        .addBox(0.0F, -0.3F, 0.0F, 14.7F, 0.6F, 8.6F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -2.9F, 0.00F, -0.69F, -0.20F));
        PartDefinition wingLM5 = wingLHand.addOrReplaceChild("wing_l_m5",
                CubeListBuilder.create().texOffs(180, 55)
                        .addBox(0.0F, -0.3F, 0.0F, 15.5F, 0.6F, 7.0F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -2.4F, 0.00F, 0.60F, -0.25F));
        PartDefinition wingLM6 = wingLHand.addOrReplaceChild("wing_l_m6",
                CubeListBuilder.create().texOffs(874, 55)
                        .addBox(0.0F, -0.3F, 0.0F, 10.5F, 0.6F, 6.4F),
                PartPose.offsetAndRotation(0.4F, 0.0F, -2.2F, 0.00F, -1.13F, -0.30F));
        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(802, 55).mirror()
                        .addBox(-13.0F, -2.4F, -2.6F, 13.0F, 4.8F, 5.2F),
                PartPose.offsetAndRotation(-6.8F, 2.8F, -5.4F, 0.00F, 1.16F, 0.46F));
        PartDefinition wingRDeltoid = wingR.addOrReplaceChild("wing_r_deltoid",
                CubeListBuilder.create().texOffs(283, 90).mirror()
                        .addBox(-7.0F, -3.0F, -2.0F, 7.0F, 6.4F, 6.4F),
                PartPose.offset(-0.8F, 0.6F, -1.8F));
        PartDefinition wingRFore = wingR.addOrReplaceChild("wing_r_fore",
                CubeListBuilder.create().texOffs(161, 90).mirror()
                        .addBox(-13.5F, -1.8F, -2.0F, 13.5F, 3.6F, 4.0F),
                PartPose.offsetAndRotation(-13.0F, 0.0F, 0.0F, 0.00F, 0.64F, -0.28F));
        PartDefinition wingRHand = wingRFore.addOrReplaceChild("wing_r_hand",
                CubeListBuilder.create(),
                PartPose.offsetAndRotation(-13.5F, 0.0F, 0.0F, 0.00F, -0.50F, 0.20F));
        PartDefinition wingRClaw = wingRHand.addOrReplaceChild("wing_r_claw",
                CubeListBuilder.create().texOffs(586, 133).mirror()
                        .addBox(-1.8F, -0.9F, -0.9F, 1.8F, 1.8F, 5.4F),
                PartPose.offsetAndRotation(-0.0F, 0.9F, -0.6F, -0.55F, -0.00F, -0.00F));
        PartDefinition wingRF1a = wingRHand.addOrReplaceChild("wing_r_f1a",
                CubeListBuilder.create().texOffs(720, 90).mirror()
                        .addBox(-14.0F, -1.2F, -1.2F, 14.0F, 2.4F, 2.4F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.05F, -0.30F, 0.03F));
        PartDefinition wingRF1b = wingRF1a.addOrReplaceChild("wing_r_f1b",
                CubeListBuilder.create().texOffs(135, 133).mirror()
                        .addBox(-11.0F, -1.0F, -1.0F, 11.0F, 1.9F, 1.9F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, 0.10F, -0.00F, -0.00F));
        PartDefinition wingRF2a = wingRHand.addOrReplaceChild("wing_r_f2a",
                CubeListBuilder.create().texOffs(650, 90).mirror()
                        .addBox(-16.5F, -1.1F, -1.1F, 16.5F, 2.1F, 2.1F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.10F, -0.05F, 0.06F));
        PartDefinition wingRF2b = wingRF2a.addOrReplaceChild("wing_r_f2b",
                CubeListBuilder.create().texOffs(77, 133).mirror()
                        .addBox(-13.0F, -0.8F, -0.8F, 13.0F, 1.7F, 1.7F),
                PartPose.offsetAndRotation(-16.5F, 0.0F, 0.0F, 0.16F, -0.00F, -0.00F));
        PartDefinition wingRF3a = wingRHand.addOrReplaceChild("wing_r_f3a",
                CubeListBuilder.create().texOffs(863, 90).mirror()
                        .addBox(-16.0F, -0.9F, -0.9F, 16.0F, 1.8F, 1.8F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.15F, 0.22F, 0.09F));
        PartDefinition wingRF3b = wingRF3a.addOrReplaceChild("wing_r_f3b",
                CubeListBuilder.create().texOffs(327, 133).mirror()
                        .addBox(-12.0F, -0.7F, -0.7F, 12.0F, 1.4F, 1.4F),
                PartPose.offsetAndRotation(-16.0F, 0.0F, 0.0F, 0.22F, -0.00F, -0.00F));
        PartDefinition wingRF4a = wingRHand.addOrReplaceChild("wing_r_f4a",
                CubeListBuilder.create().texOffs(210, 133).mirror()
                        .addBox(-13.0F, -0.8F, -0.8F, 13.0F, 1.5F, 1.5F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.20F, 0.52F, 0.12F));
        PartDefinition wingRF4b = wingRF4a.addOrReplaceChild("wing_r_f4b",
                CubeListBuilder.create().texOffs(489, 133).mirror()
                        .addBox(-9.5F, -0.6F, -0.6F, 9.5F, 1.2F, 1.2F),
                PartPose.offsetAndRotation(-13.0F, 0.0F, 0.0F, 0.28F, -0.00F, -0.00F));
        PartDefinition wingRF5a = wingRHand.addOrReplaceChild("wing_r_f5a",
                CubeListBuilder.create().texOffs(550, 133).mirror()
                        .addBox(-9.0F, -0.6F, -0.6F, 9.0F, 1.2F, 1.2F),
                PartPose.offsetAndRotation(-0.0F, 0.0F, 0.0F, -0.25F, 0.85F, 0.15F));
        PartDefinition wingRF5b = wingRF5a.addOrReplaceChild("wing_r_f5b",
                CubeListBuilder.create().texOffs(801, 133).mirror()
                        .addBox(-6.0F, -0.5F, -0.5F, 6.0F, 1.0F, 1.0F),
                PartPose.offsetAndRotation(-9.0F, 0.0F, 0.0F, 0.34F, -0.00F, -0.00F));
        PartDefinition wingRTipclaw = wingRF1b.addOrReplaceChild("wing_r_tipclaw",
                CubeListBuilder.create().texOffs(967, 133).mirror()
                        .addBox(-2.6F, -0.5F, -0.5F, 2.6F, 1.0F, 1.0F),
                PartPose.offset(-11.0F, 0.0F, 0.0F));
        PartDefinition wingRM1 = wingRHand.addOrReplaceChild("wing_r_m1",
                CubeListBuilder.create().texOffs(353, 0).mirror()
                        .addBox(-24.5F, -0.3F, 0.0F, 24.5F, 0.6F, 9.3F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -3.2F, 0.00F, -0.17F, 0.05F));
        PartDefinition wingRM2 = wingRHand.addOrReplaceChild("wing_r_m2",
                CubeListBuilder.create().texOffs(146, 0).mirror()
                        .addBox(-27.4F, -0.3F, 0.0F, 27.4F, 0.6F, 10.4F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -3.5F, 0.00F, 0.08F, 0.10F));
        PartDefinition wingRM3 = wingRHand.addOrReplaceChild("wing_r_m3",
                CubeListBuilder.create().texOffs(485, 0).mirror()
                        .addBox(-22.1F, -0.3F, 0.0F, 22.1F, 0.6F, 10.2F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -3.5F, 0.00F, 0.37F, 0.15F));
        PartDefinition wingRM4 = wingRHand.addOrReplaceChild("wing_r_m4",
                CubeListBuilder.create().texOffs(49, 55).mirror()
                        .addBox(-14.7F, -0.3F, 0.0F, 14.7F, 0.6F, 8.6F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -2.9F, 0.00F, 0.69F, 0.20F));
        PartDefinition wingRM5 = wingRHand.addOrReplaceChild("wing_r_m5",
                CubeListBuilder.create().texOffs(227, 55).mirror()
                        .addBox(-15.5F, -0.3F, 0.0F, 15.5F, 0.6F, 7.0F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -2.4F, 0.00F, -0.60F, 0.25F));
        PartDefinition wingRM6 = wingRHand.addOrReplaceChild("wing_r_m6",
                CubeListBuilder.create().texOffs(907, 55).mirror()
                        .addBox(-10.5F, -0.3F, 0.0F, 10.5F, 0.6F, 6.4F),
                PartPose.offsetAndRotation(-0.4F, 0.0F, -2.2F, 0.00F, 1.13F, 0.30F));
        return mesh;
    }
}
