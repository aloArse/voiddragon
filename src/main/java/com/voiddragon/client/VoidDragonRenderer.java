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

    public VoidDragonRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidDragonModel(context.bakeLayer(VoidDragonModel.LAYER)), 1.7F);
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
        return TEXTURE;
    }

    @Override
    protected void setupRotations(VoidDragonEntity entity, PoseStack poseStack, float bob, float yBodyRot,
                                  float partialTick, float scale) {
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        float fly = entity.getFlyProgress(partialTick);
        if (fly > 0.01F) {
            // Bank the whole body with the flight pitch, around the body centre.
            float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
            poseStack.translate(0.0F, 1.2F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch * fly));
            poseStack.translate(0.0F, -1.2F, 0.0F);
        }
    }

    @Override
    protected float getFlipDegrees(VoidDragonEntity entity) {
        return 0.0F; // a dragon doesn't tip over when it dies
    }
}
