package com.voiddragon.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.voiddragon.VoidDragonMod;
import com.voiddragon.entity.DragonStage;
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
 * The animated Void Dragon.
 *
 * <p>All 123 cubes of geometry are generated into {@link DragonRig} from
 * <code>tools/dragon_spec.py</code>; this class only moves them. Every joint that
 * matters for acting is cached with its rest pose so animations stay additive
 * and the model never drifts out of its bind pose after a few hundred ticks.</p>
 *
 * <p>Animations: idle breathing with a lazy tail sway, a four-beat walk with
 * counter-rotating shoulders and hips, tucked legs and beating wings in flight,
 * a hunched sit, a full-body roar (neck rears, jaw opens, wings flare), a breath
 * channel (jaw + throat), banking in turns and a per-stage "aura" pose where
 * older dragons hold their head higher.</p>
 */
public class VoidDragonModel extends EntityModel<VoidDragonEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "void_dragon"), "main");
    public static final int TEXTURE_SIZE = 1024;

    private final ModelPart root;
    private final ModelPart body;
    /** Rest offsets of the torso pivot, so animation stays additive. */
    private final float bodyRestY;
    private final float bodyRestX;
    private final float bodyRestZ;
    private final ModelPart jaw;
    private final ModelPart head;
    private final float headRestX;
    private final float jawRestX;

    private final ModelPart[] neck = new ModelPart[5];
    private final float[] neckRestX = new float[5];
    private final ModelPart[] tail = new ModelPart[7];
    private final float[] tailRestX = new float[7];
    private final float[] tailRestY = new float[7];
    private final ModelPart spadeTop, spadeBot;

    private final ModelPart wingL, wingR, wingLFore, wingRFore;
    /** Wing finger count; the rig builds one membrane panel per gap between them. */
    private static final int FINGERS = 5;

    private final ModelPart wingLHand, wingRHand;
    /** First phalanx of each of the five wing fingers, both sides. */
    private final ModelPart[] wingLFingers = new ModelPart[FINGERS];
    private final ModelPart[] wingRFingers = new ModelPart[FINGERS];
    /** One membrane panel per gap between fingers, plus cove and trailing sail. */
    private final ModelPart[] wingLMembrane = new ModelPart[FINGERS + 1];
    private final ModelPart[] wingRMembrane = new ModelPart[FINGERS + 1];
    private final float wingRestZRot, wingRestYRot, wingRestXRot;
    private final float foreRestZRot, foreRestXRot, foreRestYRot;

    private final List<ModelPart> legs = new ArrayList<>();   // thigh, shin, foot triples
    private final float[] thighRestX = new float[4];
    private final float[] shinRestX = new float[4];
    private final float[] footRestX = new float[4];

    /** One crown horn per side. */
    private final ModelPart[] horns = new ModelPart[2];
    private final float[] hornRestX = new float[2];

    public VoidDragonModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.body = root.getChild("body");
        this.bodyRestX = body.x;
        this.bodyRestY = body.y;
        this.bodyRestZ = body.z;

        ModelPart previous = body;
        for (int i = 0; i < neck.length; i++) {
            this.neck[i] = previous.getChild("neck" + (i + 1));
            this.neckRestX[i] = neck[i].xRot;
            previous = neck[i];
        }
        this.head = neck[neck.length - 1].getChild("head");
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
        this.wingRestZRot = wingL.zRot;
        this.wingRestYRot = wingL.yRot;
        this.wingRestXRot = wingL.xRot;
        this.foreRestZRot = wingLFore.zRot;
        this.foreRestXRot = wingLFore.xRot;
        this.foreRestYRot = wingLFore.yRot;
        this.wingLHand = wingLFore.getChild("wing_l_hand");
        this.wingRHand = wingRFore.getChild("wing_r_hand");
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
            String side = i == 0 ? "l" : "r";
            ModelPart horn = head.getChild("horn_" + side + "1");
            this.horns[i] = horn;
            this.hornRestX[i] = horn.xRot;
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

        // one crown horn per side; the old three-segment chains were replaced by a
        // single tapered box in tools/dragon_spec.py
        for (int i = 0; i < 2; i++) {
            String side = i == 0 ? "l" : "r";
            ModelPart horn = head.getChild("horn_" + side + "1");
            this.horns[i] = horn;
            this.hornRestX[i] = horn.xRot;
        }
        setGearVisible(false, false);
    }

    public static LayerDefinition createBodyLayer() {
        return LayerDefinition.create(DragonRig.createMesh(), TEXTURE_SIZE, TEXTURE_SIZE);
    }

    private void setGearVisible(boolean saddle, boolean armor) {
        String[] gear = {"saddle", "saddle_pommel", "saddle_cantle", "saddle_strap_l", "saddle_strap_r"};
        for (String name : gear) {
            ModelPart part = body.getChild(name);
            part.visible = saddle;
        }
        for (String name : new String[]{"armor_chest", "armor_torso", "armor_hips", "armor_collar"}) {
            ModelPart part = body.getChild(name);
            part.visible = armor;
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

        DragonStage stage = entity.getStage();
        float stageLift = stage.ordinal() * 0.035F;   // older dragons hold their head higher

        setGearVisible(entity.isSaddled(), entity.isArmored());

        // ------------------------------------------------ torso: breathe, bob, bank
        body.x = bodyRestX;
        // additive: the rig places the torso pivot high above the feet
        body.y = bodyRestY + breathe * 0.30F - fly * (0.6F + Math.abs(flap) * 0.9F) + (sit ? 2.6F : 0.0F);
        body.z = bodyRestZ;
        body.xRot = fly * (flapLag * 0.05F + 0.03F) + (sit ? 0.10F : 0.0F);
        body.yRot = -limbSwing * 0.06F * limbSwingAmount * (1.0F - fly);
        body.zRot = fly * Mth.sin(age * 0.04F) * 0.03F;

        // ------------------------------------------------ neck: s-curve follows the look
        float yaw = netHeadYaw * Mth.DEG_TO_RAD;
        float pitch = headPitch * Mth.DEG_TO_RAD;
        float rear = mouth * 0.22F;   // a roaring / breathing dragon rears its neck up
        for (int i = 0; i < neck.length; i++) {
            ModelPart part = neck[i];
            float weight = 0.10F + i * 0.055F;
            part.yRot = yaw * weight + Mth.sin(age * 0.021F + i * 0.7F) * 0.014F;
            part.xRot = neckRestX[i]
                    + pitch * weight
                    - rear * (0.10F + i * 0.03F)
                    - stageLift * (1.0F - i * 0.12F)
                    - fly * 0.035F * (1.0F - i * 0.2F)
                    + breathe * 0.008F * (i + 1);
            part.zRot = Mth.sin(age * 0.017F + i) * 0.01F;
        }
        head.yRot = yaw * 0.30F;
        head.xRot = headRestX + pitch * 0.45F + Mth.sin(age * 0.031F) * 0.02F
                + mouth * 0.10F - stageLift * 0.4F;
        jaw.xRot = jawRestX + mouth * 0.62F + Mth.sin(age * 0.8F) * 0.03F * mouth;

        // ------------------------------------------------ tail: travelling lash
        float sway = age * 0.05F;
        float swing = 0.09F + fly * 0.10F;
        for (int i = 0; i < tail.length; i++) {
            ModelPart part = tail[i];
            part.yRot = tailRestY[i] + Mth.sin(sway - i * 0.55F) * swing * (0.5F + i * 0.12F)
                    + Mth.cos(limbSwing * 0.35F) * 0.04F * limbSwingAmount * (1.0F - fly);
            part.xRot = tailRestX[i] + (sit ? 0.06F : 0.0F) + fly * flap * 0.02F;
        }
        spadeTop.xRot = -0.34F + fly * flap * 0.16F + mouth * 0.10F;
        spadeBot.xRot = 0.05F - fly * flap * 0.10F;

        // ------------------------------------------------ wings
        if (fly > 0.02F) {
            animateFlightWings(fly, flap, flapLag);
        } else {
            animateFoldedWings(fly, breathe, mouth, limbSwing, limbSwingAmount);
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

    private void animateFoldedWings(float fly, float breathe, float mouth, float limbSwing, float limbSwingAmount) {
        // Wings fold up over the back like a bat's umbrella, tucked against the spine.
        float unfurl = mouth * 0.30F;                       // a roar flares them a little
        float idle = Mth.sin(limbSwing * 0.33F) * 0.03F * limbSwingAmount;
        wingL.yRot = wingRestYRot - unfurl * 0.8F + idle;
        wingL.zRot = wingRestZRot - unfurl * 0.5F + breathe * 0.02F;
        // keep the authored roll that turns the membrane into a fan instead of a plank
        wingL.xRot = wingRestXRot;
        wingLFore.xRot = foreRestXRot;
        wingLFore.yRot = foreRestYRot + unfurl * 0.2F;
        wingLFore.zRot = foreRestZRot + unfurl * 0.6F;

        wingR.yRot = -wingL.yRot;
        wingR.zRot = -wingL.zRot;
        wingR.xRot = wingL.xRot;
        wingRFore.xRot = wingLFore.xRot;
        wingRFore.yRot = -wingLFore.yRot;
        wingRFore.zRot = -wingLFore.zRot;

        for (int i = 0; i < FINGERS + 1; i++) {
            wingLMembrane[i].zRot = 0.0F;
            wingRMembrane[i].zRot = 0.0F;
        }
    }

    private void animateFlightWings(float fly, float flap, float flapLag) {
        // Down-stroke sweeps the arm out and the leading edge forward; the fingers
        // trail a few degrees behind so the membrane looks like cloth, not plastic.
        float sweep = 0.34F * flap;
        float armRoll = -0.10F + sweep;
        float foreRoll = 0.06F + flap * 0.12F;
        wingL.xRot = wingRestXRot - 0.05F + flap * 0.10F;
        wingL.zRot = Mth.lerp(fly, wingRestZRot, armRoll);
        wingL.yRot = Mth.lerp(fly, wingRestYRot, -0.42F);
        wingLFore.xRot = foreRestXRot;
        wingLFore.zRot = Mth.lerp(fly, foreRestZRot, foreRoll);
        wingLFore.yRot = Mth.lerp(fly, foreRestYRot, -0.26F);
        // each finger lags a little more than the one before it, so the fan
        // opens like cloth being shaken out rather than moving as one plate
        for (int i = 0; i < FINGERS; i++) {
            wingLFingers[i].zRot = -0.04F * i + flapLag * 0.09F * (1.0F - i * 0.12F);
        }
        for (int i = 0; i < FINGERS + 1; i++) {
            wingLMembrane[i].zRot = flapLag * 0.05F * (i + 1);
        }

        wingR.xRot = wingL.xRot;
        wingR.zRot = -wingL.zRot;
        wingR.yRot = -wingL.yRot;
        wingRFore.xRot = wingLFore.xRot;
        wingRFore.zRot = -wingLFore.zRot;
        wingRFore.yRot = -wingLFore.yRot;
        for (int i = 0; i < FINGERS; i++) {
            wingRFingers[i].zRot = -wingLFingers[i].zRot;
        }
        for (int i = 0; i < FINGERS + 1; i++) {
            wingRMembrane[i].zRot = -wingLMembrane[i].zRot;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
