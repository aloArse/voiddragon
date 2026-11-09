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

import java.util.ArrayList;
import java.util.List;

/**
 * The Void Dragon model: a HotD-style dragon with a heavy barrel body, a short
 * S-curved neck, a long wedge skull, enormous crimson bat wings, a spade tail
 * and digitigrade legs.
 *
 * <p>Geometry lives in {@link DragonRig} (generated from tools/dragon_spec.py)
 * and the wing anchors in {@link DragonPose} (generated from the same spec), so
 * the offline previews and the game can never drift apart.</p>
 *
 * <p>Animation is additive off the authored rest pose: the geometry is baked with
 * the wings SPREAD, and this class folds them along the flank while the dragon is
 * on the ground and beats them once it is airborne.</p>
 */
public class VoidDragonModel extends EntityModel<VoidDragonEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "void_dragon"), "main");
    public static final int TEXTURE_SIZE = 1024;

    private static final int FINGERS = 5;

    private final ModelPart root;
    private final ModelPart body;
    private final float bodyRestX, bodyRestY, bodyRestZ;

    private final ModelPart[] neck = new ModelPart[3];
    private final float[] neckRestX = new float[3];
    private final ModelPart head, jaw;
    private final float headRestX, jawRestX;

    private final ModelPart[] tail = new ModelPart[7];
    private final float[] tailRestX = new float[7];
    private final float[] tailRestY = new float[7];
    private final ModelPart spadeTop, spadeBot;

    private final ModelPart wingL, wingR, wingLFore, wingRFore, wingLHand, wingRHand;
    private final float wingRestX, wingRestY, wingRestZ;
    private final float foreRestX, foreRestY, foreRestZ;
    private final float handRestX, handRestY, handRestZ;

    private final ModelPart[] wingLFingers = new ModelPart[FINGERS];
    private final ModelPart[] wingRFingers = new ModelPart[FINGERS];
    private final ModelPart[] wingLMembrane = new ModelPart[FINGERS + 1];
    private final ModelPart[] wingRMembrane = new ModelPart[FINGERS + 1];

    private final List<ModelPart> legs = new ArrayList<>(12);
    private final float[] thighRestX = new float[4];
    private final float[] shinRestX = new float[4];
    private final float[] footRestX = new float[4];

    private final ModelPart[] horns = new ModelPart[2];
    private final float[] hornRestX = new float[2];

    public VoidDragonModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        // Everything hangs from the "rig" part that cancels Minecraft's model-space
        // flip (see DragonRig), so the body is one level down from the baked root.
        this.body = root.getChild(DragonRig.RIG).getChild("body");
        this.bodyRestX = body.x;
        this.bodyRestY = body.y;
        this.bodyRestZ = body.z;

        ModelPart previous = body;
        for (int i = 0; i < neck.length; i++) {
            this.neck[i] = previous.getChild("neck" + (i + 1));
            this.neckRestX[i] = neck[i].xRot;
            previous = neck[i];
        }
        this.head = previous.getChild("head");
        this.jaw = head.getChild("jaw");
        this.headRestX = head.xRot;
        this.jawRestX = jaw.xRot;

        previous = body;
        for (int i = 0; i < tail.length; i++) {
            this.tail[i] = previous.getChild("tail" + (i + 1));
            this.tailRestX[i] = tail[i].xRot;
            this.tailRestY[i] = tail[i].yRot;
            previous = tail[i];
        }
        this.spadeTop = tail[tail.length - 1].getChild("spade_top");
        this.spadeBot = tail[tail.length - 1].getChild("spade_bot");

        this.wingL = body.getChild("wing_l");
        this.wingR = body.getChild("wing_r");
        this.wingLFore = wingL.getChild("wing_l_fore");
        this.wingRFore = wingR.getChild("wing_r_fore");
        this.wingRestX = wingL.xRot;
        this.wingRestY = wingL.yRot;
        this.wingRestZ = wingL.zRot;
        this.foreRestX = wingLFore.xRot;
        this.foreRestY = wingLFore.yRot;
        this.foreRestZ = wingLFore.zRot;
        this.wingLHand = wingLFore.getChild("wing_l_hand");
        this.wingRHand = wingRFore.getChild("wing_r_hand");
        this.handRestX = wingLHand.xRot;
        this.handRestY = wingLHand.yRot;
        this.handRestZ = wingLHand.zRot;

        for (int i = 0; i < FINGERS; i++) {
            this.wingLFingers[i] = wingLHand.getChild("wing_l_f" + (i + 1) + "a");
            this.wingRFingers[i] = wingRHand.getChild("wing_r_f" + (i + 1) + "a");
        }
        for (int i = 0; i < FINGERS + 1; i++) {
            this.wingLMembrane[i] = wingLHand.getChild("wing_l_m" + (i + 1));
            this.wingRMembrane[i] = wingRHand.getChild("wing_r_m" + (i + 1));
        }

        String[] sides = {"fl", "fr", "bl", "br"};
        for (int i = 0; i < 4; i++) {
            ModelPart thigh = body.getChild("leg_" + sides[i] + "_thigh");
            ModelPart shin = thigh.getChild("leg_" + sides[i] + "_shin");
            ModelPart foot = shin.getChild("leg_" + sides[i] + "_foot");
            legs.add(thigh);
            legs.add(shin);
            legs.add(foot);
            this.thighRestX[i] = thigh.xRot;
            this.shinRestX[i] = shin.xRot;
            this.footRestX[i] = foot.xRot;
        }

        for (int i = 0; i < 2; i++) {
            ModelPart horn = head.getChild("horn_" + (i == 0 ? "l" : "r") + "1");
            this.horns[i] = horn;
            this.hornRestX[i] = horn.xRot;
        }
        setGearVisible(false, false);
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(DragonRig.createMesh(), TEXTURE_SIZE, TEXTURE_SIZE);
    }

    private void setGearVisible(boolean saddle, boolean armor) {
        for (String name : new String[]{"saddle", "saddle_pommel", "saddle_cantle",
                "saddle_strap_l", "saddle_strap_r"}) {
            body.getChild(name).visible = saddle;
        }
        for (String name : new String[]{"armor_chest", "armor_torso", "armor_hips", "armor_collar"}) {
            body.getChild(name).visible = armor;
        }
    }

    @Override
    public void setupAnim(VoidDragonEntity entity, float limbSwing, float limbSwingAmount, float age,
                          float netHeadYaw, float headPitch) {
        float partial = age - entity.tickCount;
        float fly = entity.getFlyProgress(partial);
        float mouth = entity.getMouthProgress(partial);
        boolean sit = entity.isInSittingPose() && fly < 0.05F;
        float flapRate = 0.30F * entity.getFlapSpeed();
        float flap = Mth.sin(age * flapRate);
        float flapLag = Mth.sin(age * flapRate - 0.9F);
        float breathe = Mth.sin(age * 0.055F);

        setGearVisible(entity.isSaddled(), entity.isArmored());

        // ------------------------------------------------ torso: breathe, bob, bank
        body.x = bodyRestX;
        body.y = bodyRestY + breathe * 0.30F
                - fly * (0.6F + Math.abs(flap) * 1.1F)
                + (sit ? 2.4F : 0.0F);
        body.z = bodyRestZ;
        body.xRot = fly * (flapLag * 0.05F + 0.03F) + (sit ? 0.10F : 0.0F);
        body.yRot = -limbSwing * 0.05F * limbSwingAmount * (1.0F - fly);
        body.zRot = fly * Mth.sin(age * 0.04F) * 0.03F;

        // ------------------------------------------------ neck: s-curve follows the look
        float yaw = netHeadYaw * Mth.DEG_TO_RAD;
        float pitch = headPitch * Mth.DEG_TO_RAD;
        float rear = mouth * 0.20F;   // a roaring / breathing dragon lifts its neck
        for (int i = 0; i < neck.length; i++) {
            ModelPart part = neck[i];
            float weight = 0.16F + i * 0.10F;
            part.yRot = yaw * weight + Mth.sin(age * 0.021F + i * 0.7F) * 0.014F;
            part.xRot = neckRestX[i]
                    + pitch * weight
                    - rear * (0.10F + i * 0.05F)
                    - fly * 0.04F * (1.0F - i * 0.25F)
                    + breathe * 0.008F * (i + 1);
            part.zRot = Mth.sin(age * 0.017F + i) * 0.012F;
        }
        head.yRot = yaw * 0.42F;
        head.xRot = headRestX + pitch * 0.50F + Mth.sin(age * 0.031F) * 0.02F + mouth * 0.12F;
        // the jaw hinges open on its rear edge: 45 degrees at full breath
        jaw.xRot = jawRestX + mouth * 0.78F + Mth.sin(age * 0.8F) * 0.03F * mouth;

        // ------------------------------------------------ tail: travelling lash
        float sway = age * 0.05F;
        float swing = 0.08F + fly * 0.10F;
        for (int i = 0; i < tail.length; i++) {
            ModelPart part = tail[i];
            part.yRot = tailRestY[i] + Mth.sin(sway - i * 0.55F) * swing * (0.5F + i * 0.14F)
                    + Mth.cos(limbSwing * 0.35F) * 0.04F * limbSwingAmount * (1.0F - fly);
            part.xRot = tailRestX[i] + (sit ? 0.06F : 0.0F) + fly * flap * 0.02F;
        }
        spadeTop.xRot = -0.34F + fly * flap * 0.16F + mouth * 0.10F;
        spadeBot.xRot = 0.06F - fly * flap * 0.10F;

        // ------------------------------------------------ wings
        if (fly > 0.02F) {
            animateFlightWings(fly, flap, flapLag);
        } else {
            animateFoldedWings(breathe, mouth, limbSwing, limbSwingAmount);
        }
        for (int i = 0; i < horns.length; i++) {
            horns[i].xRot = hornRestX[i] + Mth.sin(age * 0.03F + i) * 0.012F;
        }

        // ------------------------------------------------ legs
        float stride = Mth.cos(limbSwing * 0.85F);
        float counter = Mth.cos(limbSwing * 0.85F + Mth.PI);
        float[] walk = {stride, counter, counter, stride};
        for (int i = 0; i < 4; i++) {
            ModelPart thigh = legs.get(i * 3);
            ModelPart shin = legs.get(i * 3 + 1);
            ModelPart foot = legs.get(i * 3 + 2);
            boolean front = i < 2;
            float flyBend = front ? -0.40F : 0.34F;
            float sitBend = front ? -0.18F : -0.42F;
            thigh.xRot = thighRestX[i]
                    + Mth.lerp(fly, walk[i] * 0.55F * limbSwingAmount, flyBend)
                    + (sit ? sitBend : 0.0F);
            shin.xRot = shinRestX[i]
                    + Mth.lerp(fly, -walk[i] * 0.25F * limbSwingAmount, front ? 0.22F : 0.30F)
                    + (sit ? (front ? 0.30F : 0.55F) : 0.0F);
            foot.xRot = footRestX[i]
                    + Mth.lerp(fly, -walk[i] * 0.18F * limbSwingAmount, 0.32F)
                    + (sit ? -0.20F : 0.0F);
        }
    }

    /**
     * Grounded: the arm swings back along the ribs and rolls down, the forearm
     * folds the fan forward over the flank, and each finger closes towards the
     * mean of its fan so the bundle keeps pointing the same way. All anchors come
     * from {@link DragonPose}, which tools/gen_model.py derives from the spec.
     */
    private void animateFoldedWings(float breathe, float mouth, float limbSwing, float limbSwingAmount) {
        float unfurl = mouth * DragonPose.ROAR_UNFURL;      // a roar flares them a little
        float idle = Mth.sin(limbSwing * 0.33F) * 0.03F * limbSwingAmount;

        wingL.xRot = wingRestX + DragonPose.FOLD_ROOT_X;
        wingL.yRot = DragonPose.FOLD_ROOT_Y - unfurl * 0.5F + idle;
        wingL.zRot = DragonPose.FOLD_ROOT_Z + breathe * 0.02F + unfurl * 0.3F;
        wingR.xRot = wingL.xRot;
        wingR.yRot = -wingL.yRot;
        wingR.zRot = -wingL.zRot;

        wingLFore.xRot = foreRestX + DragonPose.FOLD_FORE_X;
        wingLFore.yRot = DragonPose.FOLD_FORE_Y + unfurl * 0.3F;
        wingLFore.zRot = DragonPose.FOLD_FORE_Z;
        wingRFore.xRot = wingLFore.xRot;
        wingRFore.yRot = -wingLFore.yRot;
        wingRFore.zRot = -wingLFore.zRot;

        wingLHand.xRot = handRestX + DragonPose.FOLD_HAND_X;
        wingLHand.yRot = DragonPose.FOLD_HAND_Y;
        wingLHand.zRot = DragonPose.FOLD_HAND_Z;
        wingRHand.xRot = wingLHand.xRot;
        wingRHand.yRot = -wingLHand.yRot;
        wingRHand.zRot = -wingLHand.zRot;

        for (int i = 0; i < FINGERS; i++) {
            float angle = Mth.lerp(mouth * 0.35F, DragonPose.FOLDED_FINGER[i], DragonPose.OPEN_FINGER[i]);
            wingLFingers[i].yRot = angle;
            wingRFingers[i].yRot = -angle;
        }
        for (int i = 0; i < FINGERS + 1; i++) {
            float angle = Mth.lerp(mouth * 0.35F, DragonPose.FOLDED_FINGER[FINGERS + i],
                    DragonPose.OPEN_FINGER[FINGERS + i]);
            wingLMembrane[i].yRot = angle;
            wingLMembrane[i].zRot = 0.0F;
            wingRMembrane[i].yRot = -angle;
            wingRMembrane[i].zRot = 0.0F;
        }
    }

    /**
     * Airborne: the arm beats from the authored spread pose, the forearm trails a
     * few degrees behind it and the finger fan opens like cloth being shaken out.
     */
    private void animateFlightWings(float fly, float flap, float flapLag) {
        float sweep = flap * DragonPose.FLAP_ROOT_Z;
        wingL.xRot = wingRestX + flap * 0.06F;
        wingL.yRot = wingRestY;
        wingL.zRot = wingRestZ + sweep;
        wingR.xRot = wingL.xRot;
        wingR.yRot = -wingL.yRot;
        wingR.zRot = -wingL.zRot;

        wingLFore.xRot = foreRestX;
        wingLFore.yRot = foreRestY;
        wingLFore.zRot = foreRestZ + flap * DragonPose.FLAP_FORE_Z;
        wingRFore.xRot = wingLFore.xRot;
        wingRFore.yRot = -wingLFore.yRot;
        wingRFore.zRot = -wingLFore.zRot;

        wingLHand.xRot = handRestX;
        wingLHand.yRot = handRestY;
        wingLHand.zRot = handRestZ;
        wingRHand.xRot = wingLHand.xRot;
        wingRHand.yRot = -wingLHand.yRot;
        wingRHand.zRot = -wingLHand.zRot;

        for (int i = 0; i < FINGERS; i++) {
            float open = Mth.lerp(fly, DragonPose.FOLDED_FINGER[i], DragonPose.OPEN_FINGER[i]);
            wingLFingers[i].yRot = open;
            wingLFingers[i].zRot = -0.04F * i + flapLag * DragonPose.FLAP_FINGER_LAG * (1.0F - i * 0.12F);
            wingRFingers[i].yRot = -open;
            wingRFingers[i].zRot = -wingLFingers[i].zRot;
        }
        for (int i = 0; i < FINGERS + 1; i++) {
            float open = Mth.lerp(fly, DragonPose.FOLDED_FINGER[FINGERS + i],
                    DragonPose.OPEN_FINGER[FINGERS + i]);
            wingLMembrane[i].yRot = open;
            wingLMembrane[i].zRot = flapLag * 0.05F * (i + 1);
            wingRMembrane[i].yRot = -open;
            wingRMembrane[i].zRot = -wingLMembrane[i].zRot;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
