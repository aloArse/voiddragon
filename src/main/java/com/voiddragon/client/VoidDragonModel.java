package com.voiddragon.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.voiddragon.VoidDragonMod;
import com.voiddragon.entity.VoidDragonEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * The Void Dragon model.
 *
 * <p>Geometry (69 cubes: neck chain, head, jaw, horns, tail, two-segment wings,
 * quadruped legs, saddle and armour shell) is generated into {@link DragonRig}
 * from <code>tools/dragon_spec.py</code>; this class only drives the animation.</p>
 */
public class VoidDragonModel extends EntityModel<VoidDragonEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "void_dragon"), "main");
    public static final int TEXTURE_SIZE = 512;

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart[] neck = new ModelPart[4];
    private final ModelPart head, jaw;
    private final ModelPart[] tail = new ModelPart[6];
    private final ModelPart tailFinTop, tailFinBot;
    private final ModelPart wingL, wingLFore, wingLFinger, wingR, wingRFore, wingRFinger;
    private final ModelPart[] legs = new ModelPart[4];      // thigh
    private final ModelPart[] shins = new ModelPart[4];
    private final ModelPart[] feet = new ModelPart[4];
    private final ModelPart saddle, armor, armorChest;

    /** Resting offsets, captured once so the animation can be additive. */
    private final float[] legRestX = new float[4];
    private final float[] shinRestX = new float[4];
    private final float[] footRestX = new float[4];
    private final float[] neckRestX = new float[4];
    private final float headRestX;
    private final float jarRestX;

    public VoidDragonModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.body = root.getChild("body");

        this.neck[0] = body.getChild("neck1");
        this.neck[1] = neck[0].getChild("neck2");
        this.neck[2] = neck[1].getChild("neck3");
        this.neck[3] = neck[2].getChild("neck4");
        this.head = neck[3].getChild("head");
        this.jaw = head.getChild("jaw");

        this.tail[0] = body.getChild("tail1");
        for (int i = 1; i < 6; i++) {
            this.tail[i] = tail[i - 1].getChild("tail" + (i + 1));
        }
        this.tailFinTop = tail[5].getChild("tail_fin_top");
        this.tailFinBot = tail[5].getChild("tail_fin_bot");

        this.wingL = body.getChild("wing_l");
        this.wingLFore = wingL.getChild("wing_l_fore");
        this.wingLFinger = wingL.getChild("wing_l_finger");
        this.wingR = body.getChild("wing_r");
        this.wingRFore = wingR.getChild("wing_r_fore");
        this.wingRFinger = wingR.getChild("wing_r_finger");

        String[] thigh = {"leg_fl_thigh", "leg_fr_thigh", "leg_bl_thigh", "leg_br_thigh"};
        String[] shin = {"leg_fl_shin", "leg_fr_shin", "leg_bl_shin", "leg_br_shin"};
        String[] foot = {"leg_fl_foot", "leg_fr_foot", "leg_bl_foot", "leg_br_foot"};
        for (int i = 0; i < 4; i++) {
            this.legs[i] = body.getChild(thigh[i]);
            this.shins[i] = legs[i].getChild(shin[i]);
            this.feet[i] = shins[i].getChild(foot[i]);
            this.legRestX[i] = legs[i].xRot;
            this.shinRestX[i] = shins[i].xRot;
            this.footRestX[i] = feet[i].xRot;
        }
        for (int i = 0; i < 4; i++) {
            this.neckRestX[i] = neck[i].xRot;
        }
        this.headRestX = head.xRot;
        this.jarRestX = jaw.xRot;

        this.saddle = body.getChild("saddle");
        this.armor = body.getChild("armor");
        this.armorChest = body.getChild("armor_chest");
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(DragonRig.createMesh(), TEXTURE_SIZE, TEXTURE_SIZE);
    }

    @Override
    public void setupAnim(VoidDragonEntity entity, float limbSwing, float limbSwingAmount, float age,
                          float netHeadYaw, float headPitch) {
        float partial = age - entity.tickCount;
        float fly = entity.getFlyProgress(partial);
        float mouth = entity.getMouthProgress(partial);
        boolean sit = entity.isInSittingPose() && fly < 0.05F;
        float flapRate = 0.34F * entity.getFlapSpeed();
        float flap = Mth.sin(age * flapRate);
        float flapLag = Mth.sin(age * flapRate - 0.8F);
        float breathe = Mth.sin(age * 0.075F);

        this.saddle.visible = entity.isSaddled();
        this.armor.visible = entity.isArmored();
        this.armorChest.visible = entity.isArmored();

        // ---- torso: breathing bob, sinking when sitting, lift while flying
        body.y = 20.0F + breathe * 0.35F + (sit ? 4.5F : 0.0F) - fly * Math.abs(flap) * 1.4F;
        body.xRot = fly * Mth.sin(age * flapRate - 0.4F) * 0.03F;

        // ---- neck: follow the look direction, arch in flight
        float yaw = netHeadYaw * Mth.DEG_TO_RAD;
        float pitch = headPitch * Mth.DEG_TO_RAD;
        for (int i = 0; i < 4; i++) {
            ModelPart part = neck[i];
            part.yRot = yaw * (0.16F + i * 0.06F) + Mth.sin(age * 0.03F + i * 0.4F) * 0.02F;
            part.xRot = neckRestX[i]
                    + pitch * (0.06F + i * 0.05F)
                    + breathe * 0.02F * (i + 1)
                    - fly * (0.05F + i * 0.02F);
        }
        head.yRot = yaw * 0.28F;
        head.xRot = headRestX + pitch * 0.5F + Mth.sin(age * 0.05F) * 0.02F;
        jaw.xRot = jarRestX + mouth * 0.55F + Mth.sin(age * 0.9F) * 0.03F * mouth + breathe * 0.015F;

        // ---- tail: travelling sway, lashes harder in flight
        float sway = age * 0.06F;
        float swingAmount = 0.10F + fly * 0.10F;
        for (int i = 0; i < 6; i++) {
            ModelPart part = tail[i];
            part.yRot = Mth.sin(sway - i * 0.62F) * swingAmount * (0.6F + i * 0.14F)
                    + Mth.cos(limbSwing * 0.33F) * 0.05F * limbSwingAmount * (1.0F - fly);
            part.xRot += 0.0F;
        }
        tail[0].xRot = -0.04F + (sit ? 0.30F : 0.0F) + fly * flapLag * 0.05F;
        tailFinTop.xRot = -0.22F + fly * flap * 0.12F;
        tailFinBot.xRot = 0.15F - fly * flap * 0.08F;

        // ---- wings: folded along the back on the ground, spread and beating in flight
        float armYaw = Mth.lerp(fly, -1.95F, -0.10F);
        float armRoll = Mth.lerp(fly, 0.12F + breathe * 0.02F, -0.22F - flap * 0.72F);
        float foreYaw = Mth.lerp(fly, -2.85F, 0.26F);
        float fingerYaw = Mth.lerp(fly, -0.35F, 0.30F + flapLag * 0.18F);
        float foreRoll = Mth.lerp(fly, 0.0F, -flapLag * 0.35F);

        wingL.yRot = armYaw;
        wingL.zRot = armRoll;
        wingLFore.yRot = foreYaw;
        wingLFore.zRot = foreRoll;
        wingLFinger.yRot = fingerYaw;

        wingR.yRot = -armYaw;
        wingR.zRot = -armRoll;
        wingRFore.yRot = -foreYaw;
        wingRFore.zRot = -foreRoll;
        wingRFinger.yRot = -fingerYaw;

        // ---- legs: walk cycle on the ground, tucked while flying, sprawled when sitting
        float walkA = Mth.cos(limbSwing * 0.9F) * 0.55F * limbSwingAmount;
        float walkB = Mth.cos(limbSwing * 0.9F + Mth.PI) * 0.55F * limbSwingAmount;
        float[] walk = {walkA, walkB, walkB, walkA};
        for (int i = 0; i < 4; i++) {
            float flyBend = i < 2 ? -0.32F : 0.30F;
            float rest = legs[i].xRot;
            legs[i].xRot = rest + Mth.lerp(fly, walk[i], flyBend) + (sit ? (i < 2 ? -0.15F : -0.30F) : 0.0F);
            shins[i].xRot = shinRestX[i] + Mth.lerp(fly, walk[i] * -0.35F, i < 2 ? 0.15F : 0.25F);
            feet[i].xRot = footRestX[i] + Mth.lerp(fly, -walk[i] * 0.25F, 0.35F);
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
