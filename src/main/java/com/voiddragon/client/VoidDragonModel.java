package com.voiddragon.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.voiddragon.VoidDragonMod;
import com.voiddragon.entity.VoidDragonEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * Hand-built dragon model (256x256 texture). The texture layout in
 * assets/voiddragon/textures/entity/void_dragon.png matches the texOffs values below.
 */
public class VoidDragonModel extends EntityModel<VoidDragonEntity> {
    public static final ModelLayerLocation LAYER =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "void_dragon"), "main");

    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart neck1, neck2, neck3, head, jaw;
    private final ModelPart tail1, tail2, tail3, tail4, tailTip;
    private final ModelPart wingL, wingL2, wingR, wingR2;
    private final ModelPart legFL, legFR, legBL, legBR;

    public VoidDragonModel(ModelPart root) {
        super(RenderType::entityCutoutNoCull);
        this.root = root;
        this.body = root.getChild("body");
        this.neck1 = body.getChild("neck1");
        this.neck2 = neck1.getChild("neck2");
        this.neck3 = neck2.getChild("neck3");
        this.head = neck3.getChild("head");
        this.jaw = head.getChild("jaw");
        this.tail1 = body.getChild("tail1");
        this.tail2 = tail1.getChild("tail2");
        this.tail3 = tail2.getChild("tail3");
        this.tail4 = tail3.getChild("tail4");
        this.tailTip = tail4.getChild("tail_tip");
        this.wingL = body.getChild("wing_l");
        this.wingL2 = wingL.getChild("wing_l2");
        this.wingR = body.getChild("wing_r");
        this.wingR2 = wingR.getChild("wing_r2");
        this.legFL = root.getChild("leg_fl");
        this.legFR = root.getChild("leg_fr");
        this.legBL = root.getChild("leg_bl");
        this.legBR = root.getChild("leg_br");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // ---------------- body + dorsal spikes
        PartDefinition body = root.addOrReplaceChild("body",
                CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -7.0F, -13.0F, 16.0F, 14.0F, 26.0F),
                PartPose.offset(0.0F, 4.0F, 0.0F));
        float[] spikeZ = {-9.0F, -4.0F, 1.0F, 6.0F, 11.0F};
        for (int i = 0; i < spikeZ.length; i++) {
            body.addOrReplaceChild("spike_" + i,
                    CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                    PartPose.offset(0.0F, -7.0F, spikeZ[i]));
        }

        // ---------------- neck + head
        PartDefinition neck1 = body.addOrReplaceChild("neck1",
                CubeListBuilder.create().texOffs(0, 40).addBox(-4.0F, -4.0F, -6.0F, 8.0F, 8.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, -4.0F, -12.0F, -0.7F, 0.0F, 0.0F));
        neck1.addOrReplaceChild("spike",
                CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, -4.0F, -3.0F));
        PartDefinition neck2 = neck1.addOrReplaceChild("neck2",
                CubeListBuilder.create().texOffs(28, 40).addBox(-3.5F, -3.5F, -6.0F, 7.0F, 7.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.35F, 0.0F, 0.0F));
        neck2.addOrReplaceChild("spike",
                CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, -3.5F, -3.0F));
        PartDefinition neck3 = neck2.addOrReplaceChild("neck3",
                CubeListBuilder.create().texOffs(54, 40).addBox(-3.0F, -3.0F, -6.0F, 6.0F, 6.0F, 6.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.25F, 0.0F, 0.0F));
        neck3.addOrReplaceChild("spike",
                CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, -3.0F, -3.0F));

        PartDefinition head = neck3.addOrReplaceChild("head",
                CubeListBuilder.create().texOffs(84, 0).addBox(-5.0F, -4.0F, -10.0F, 10.0F, 8.0F, 10.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, -6.0F, 0.5F, 0.0F, 0.0F));
        head.addOrReplaceChild("snout",
                CubeListBuilder.create().texOffs(124, 0).addBox(-3.0F, -2.0F, -6.0F, 6.0F, 4.0F, 6.0F),
                PartPose.offset(0.0F, 1.0F, -10.0F));
        head.addOrReplaceChild("jaw",
                CubeListBuilder.create().texOffs(148, 0).addBox(-4.0F, 0.0F, -8.0F, 8.0F, 2.0F, 8.0F),
                PartPose.offset(0.0F, 3.5F, -2.0F));
        // horns (swept back and outward)
        head.addOrReplaceChild("horn_l",
                CubeListBuilder.create().texOffs(180, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(3.5F, -3.5F, -1.0F, 0.45F, -0.35F, 0.0F));
        head.addOrReplaceChild("horn_r",
                CubeListBuilder.create().texOffs(180, 0).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 8.0F),
                PartPose.offsetAndRotation(-3.5F, -3.5F, -1.0F, 0.45F, 0.35F, 0.0F));
        // brow spikes
        head.addOrReplaceChild("brow_l",
                CubeListBuilder.create().texOffs(180, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(5.0F, 0.0F, -2.0F, 0.1F, -0.9F, 0.0F));
        head.addOrReplaceChild("brow_r",
                CubeListBuilder.create().texOffs(180, 0).mirror().addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 5.0F),
                PartPose.offsetAndRotation(-5.0F, 0.0F, -2.0F, 0.1F, 0.9F, 0.0F));

        // ---------------- tail
        PartDefinition tail1 = body.addOrReplaceChild("tail1",
                CubeListBuilder.create().texOffs(78, 40).addBox(-5.0F, -4.5F, 0.0F, 10.0F, 9.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, -1.0F, 12.0F, -0.05F, 0.0F, 0.0F));
        tail1.addOrReplaceChild("spike",
                CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, -4.5F, 6.0F));
        PartDefinition tail2 = tail1.addOrReplaceChild("tail2",
                CubeListBuilder.create().texOffs(122, 40).addBox(-4.0F, -3.5F, 0.0F, 8.0F, 7.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 12.0F, -0.08F, 0.0F, 0.0F));
        tail2.addOrReplaceChild("spike",
                CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 5.0F, 2.0F),
                PartPose.offset(0.0F, -3.5F, 6.0F));
        PartDefinition tail3 = tail2.addOrReplaceChild("tail3",
                CubeListBuilder.create().texOffs(162, 40).addBox(-3.0F, -2.5F, 0.0F, 6.0F, 5.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 12.0F, -0.05F, 0.0F, 0.0F));
        tail3.addOrReplaceChild("spike",
                CubeListBuilder.create().texOffs(200, 0).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F),
                PartPose.offset(0.0F, -2.5F, 6.0F));
        PartDefinition tail4 = tail3.addOrReplaceChild("tail4",
                CubeListBuilder.create().texOffs(0, 61).addBox(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 12.0F),
                PartPose.offsetAndRotation(0.0F, 0.0F, 12.0F, 0.1F, 0.0F, 0.0F));
        tail4.addOrReplaceChild("tail_tip",
                CubeListBuilder.create().texOffs(198, 40).addBox(-3.0F, -0.5F, 0.0F, 6.0F, 1.0F, 8.0F),
                PartPose.offset(0.0F, 0.0F, 12.0F));

        // ---------------- wings (left extends +x, right mirrored)
        PartDefinition wingL = body.addOrReplaceChild("wing_l",
                CubeListBuilder.create().texOffs(32, 61).addBox(0.0F, -1.5F, -1.5F, 14.0F, 3.0F, 3.0F),
                PartPose.offset(7.0F, -6.0F, -5.0F));
        wingL.addOrReplaceChild("membrane_in",
                CubeListBuilder.create().texOffs(0, 82).addBox(0.0F, -0.5F, 0.0F, 18.0F, 1.0F, 16.0F),
                PartPose.offset(0.0F, 0.5F, 1.5F));
        PartDefinition wingL2 = wingL.addOrReplaceChild("wing_l2",
                CubeListBuilder.create().texOffs(66, 61).addBox(0.0F, -1.0F, -1.0F, 14.0F, 2.0F, 2.0F),
                PartPose.offset(14.0F, 0.0F, 0.0F));
        wingL2.addOrReplaceChild("membrane_out",
                CubeListBuilder.create().texOffs(0, 82).addBox(0.0F, -0.5F, 0.0F, 14.0F, 1.0F, 12.0F),
                PartPose.offset(0.0F, 0.5F, 1.0F));
        wingL2.addOrReplaceChild("claw",
                CubeListBuilder.create().texOffs(180, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(14.0F, 0.0F, 0.0F, -0.4F, 0.0F, 0.0F));

        PartDefinition wingR = body.addOrReplaceChild("wing_r",
                CubeListBuilder.create().texOffs(32, 61).mirror().addBox(-14.0F, -1.5F, -1.5F, 14.0F, 3.0F, 3.0F),
                PartPose.offset(-7.0F, -6.0F, -5.0F));
        wingR.addOrReplaceChild("membrane_in",
                CubeListBuilder.create().texOffs(0, 82).mirror().addBox(-18.0F, -0.5F, 0.0F, 18.0F, 1.0F, 16.0F),
                PartPose.offset(0.0F, 0.5F, 1.5F));
        PartDefinition wingR2 = wingR.addOrReplaceChild("wing_r2",
                CubeListBuilder.create().texOffs(66, 61).mirror().addBox(-14.0F, -1.0F, -1.0F, 14.0F, 2.0F, 2.0F),
                PartPose.offset(-14.0F, 0.0F, 0.0F));
        wingR2.addOrReplaceChild("membrane_out",
                CubeListBuilder.create().texOffs(0, 82).mirror().addBox(-14.0F, -0.5F, 0.0F, 14.0F, 1.0F, 12.0F),
                PartPose.offset(0.0F, 0.5F, 1.0F));
        wingR2.addOrReplaceChild("claw",
                CubeListBuilder.create().texOffs(180, 0).mirror().addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 6.0F),
                PartPose.offsetAndRotation(-14.0F, 0.0F, 0.0F, -0.4F, 0.0F, 0.0F));

        // ---------------- legs
        PartDefinition legFL = root.addOrReplaceChild("leg_fl",
                CubeListBuilder.create().texOffs(98, 61).addBox(-2.5F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F),
                PartPose.offset(6.5F, 10.0F, -8.0F));
        legFL.addOrReplaceChild("foot",
                CubeListBuilder.create().texOffs(142, 61).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));
        PartDefinition legFR = root.addOrReplaceChild("leg_fr",
                CubeListBuilder.create().texOffs(98, 61).mirror().addBox(-2.5F, 0.0F, -2.5F, 5.0F, 14.0F, 5.0F),
                PartPose.offset(-6.5F, 10.0F, -8.0F));
        legFR.addOrReplaceChild("foot",
                CubeListBuilder.create().texOffs(142, 61).mirror().addBox(-2.5F, 0.0F, -5.0F, 5.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));
        PartDefinition legBL = root.addOrReplaceChild("leg_bl",
                CubeListBuilder.create().texOffs(118, 61).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
                PartPose.offset(7.0F, 10.0F, 8.0F));
        legBL.addOrReplaceChild("foot",
                CubeListBuilder.create().texOffs(142, 61).addBox(-2.5F, 0.0F, -5.0F, 5.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));
        PartDefinition legBR = root.addOrReplaceChild("leg_br",
                CubeListBuilder.create().texOffs(118, 61).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 14.0F, 6.0F),
                PartPose.offset(-7.0F, 10.0F, 8.0F));
        legBR.addOrReplaceChild("foot",
                CubeListBuilder.create().texOffs(142, 61).mirror().addBox(-2.5F, 0.0F, -5.0F, 5.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, 12.0F, 0.0F));

        return LayerDefinition.create(mesh, 256, 256);
    }

    @Override
    public void setupAnim(VoidDragonEntity e, float limbSwing, float limbSwingAmount, float age,
                          float netHeadYaw, float headPitch) {
        float partial = age - e.tickCount;
        float fly = e.getFlyProgress(partial);
        float mouthOpen = e.getMouthProgress(partial);
        boolean sit = e.isInSittingPose() && fly < 0.05F;

        float flap = Mth.sin(age * 0.33F);
        float flapLag = Mth.sin(age * 0.33F - 0.9F);
        float breathe = Mth.sin(age * 0.09F);

        // ---- body
        float bodyY = 4.0F + breathe * 0.35F + (sit ? 5.0F : 0.0F);
        body.y = bodyY - fly * Mth.cos(age * 0.33F) * 1.6F;
        body.xRot = 0.0F;

        // ---- neck / head
        float yaw = netHeadYaw * Mth.DEG_TO_RAD;
        float pitch = headPitch * Mth.DEG_TO_RAD;
        neck1.yRot = yaw * 0.2F + Mth.sin(age * 0.04F) * 0.04F;
        neck2.yRot = yaw * 0.25F;
        neck3.yRot = yaw * 0.25F;
        head.yRot = yaw * 0.3F;
        neck1.xRot = -0.7F + fly * 0.4F + pitch * 0.1F + breathe * 0.03F;
        neck2.xRot = 0.35F - fly * 0.2F + pitch * 0.15F;
        neck3.xRot = 0.25F - fly * 0.1F + pitch * 0.2F;
        head.xRot = 0.5F - fly * 0.35F + pitch * 0.5F;
        jaw.xRot = 0.04F + mouthOpen * 0.6F + Mth.sin(age * 1.1F) * 0.04F * mouthOpen + breathe * 0.02F;

        // ---- tail
        float sway = age * 0.07F;
        tail1.yRot = Mth.sin(sway) * 0.12F + Mth.cos(limbSwing * 0.33F) * 0.2F * limbSwingAmount;
        tail2.yRot = Mth.sin(sway - 0.7F) * 0.18F;
        tail3.yRot = Mth.sin(sway - 1.4F) * 0.22F;
        tail4.yRot = Mth.sin(sway - 2.1F) * 0.26F;
        tailTip.yRot = Mth.sin(sway - 2.8F) * 0.3F;
        tail1.xRot = -0.05F + fly * (-0.08F + flap * 0.05F) + (sit ? 0.25F : 0.0F);
        tail2.xRot = -0.08F + fly * flapLag * 0.07F;
        tail3.xRot = -0.05F + fly * flapLag * 0.08F;

        // ---- wings: folded along the back on the ground, spread and flapping in flight
        float wingZ = Mth.lerp(fly, -0.3F + breathe * 0.02F, -0.25F - flap * 0.75F);
        float wingYaw = Mth.lerp(fly, -1.35F, -0.15F);
        float foreYaw = Mth.lerp(fly, 2.5F, 0.35F);
        float foreZ = Mth.lerp(fly, 0.0F, -flapLag * 0.5F);
        wingL.zRot = wingZ;
        wingL.yRot = wingYaw;
        wingL2.yRot = foreYaw;
        wingL2.zRot = foreZ;
        wingR.zRot = -wingZ;
        wingR.yRot = -wingYaw;
        wingR2.yRot = -foreYaw;
        wingR2.zRot = -foreZ;

        // ---- legs
        float walkA = Mth.cos(limbSwing * 0.6662F) * 1.1F * limbSwingAmount;
        float walkB = Mth.cos(limbSwing * 0.6662F + Mth.PI) * 1.1F * limbSwingAmount;
        legFL.xRot = Mth.lerp(fly, walkA, -0.7F + flap * 0.05F);
        legFR.xRot = Mth.lerp(fly, walkB, -0.7F + flap * 0.05F);
        legBL.xRot = Mth.lerp(fly, walkB, 0.8F);
        legBR.xRot = Mth.lerp(fly, walkA, 0.8F);
        legBL.y = 10.0F;
        legBR.y = 10.0F;
        if (sit) {
            legFL.xRot = -0.3F;
            legFR.xRot = -0.3F;
            legBL.xRot = -1.35F;
            legBR.xRot = -1.35F;
            legBL.y = 13.0F;
            legBR.y = 13.0F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, buffer, packedLight, packedOverlay, color);
    }
}
