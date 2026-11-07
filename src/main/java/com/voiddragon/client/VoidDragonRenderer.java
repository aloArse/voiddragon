package com.voiddragon.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.voiddragon.VoidDragonMod;
import com.voiddragon.entity.VoidDragonEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class VoidDragonRenderer extends MobRenderer<VoidDragonEntity, VoidDragonModel> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "textures/entity/void_dragon.png");
    private static final ResourceLocation GLOW =
            ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "textures/entity/void_dragon_glow.png");
    private static final ResourceLocation ARMORED =
            ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "textures/entity/void_dragon_armored.png");

    public VoidDragonRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidDragonModel(context.bakeLayer(VoidDragonModel.LAYER)), 1.9F);
        // Fullbright emissive layer: glowing eyes, Void veins, wing veins, horn tips.
        this.addLayer(new EyesLayer<VoidDragonEntity, VoidDragonModel>(this) {
            @Override
            public RenderType renderType() {
                return RenderType.eyes(GLOW);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(VoidDragonEntity entity) {
        return entity.isArmored() ? ARMORED : TEXTURE;
    }

    @Override
    protected void setupRotations(VoidDragonEntity entity, PoseStack poseStack, float bob, float yBodyRot,
                                  float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        // MobRenderer clamps the body pitch to +/-90; a flying dragon banks with its look direction.
        float fly = entity.getFlyProgress(partialTick);
        if (fly > 0.01F) {
            float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
            poseStack.translate(0.0F, 1.2F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch * fly * 0.85F));
            poseStack.translate(0.0F, -1.2F, 0.0F);
        }
    }

    @Override
    protected void scale(VoidDragonEntity entity, PoseStack poseStack, float partialTick) {
        // The model is built at roughly 2.2 blocks tall; match the entity hitbox and the
        // rider attachment height so the saddle lines up with the player's seat.
        poseStack.scale(1.1F, 1.1F, 1.1F);
    }

    @Override
    protected float getFlipDegrees(VoidDragonEntity entity) {
        return 0.0F; // a dragon doesn't tip over when it dies
    }
}
