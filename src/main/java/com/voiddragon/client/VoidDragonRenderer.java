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

/**
 * Renders the Void Dragon.
 *
 * <p>The model is authored with its feet exactly on y=0, so scaling is a plain
 * uniform multiply: no part of the dragon can ever dip below the block it stands
 * on. The shadow radius follows the same scale, which keeps a Dragon God's
 * footprint believable instead of a dragon-sized shadow under an infant.</p>
 */
public class VoidDragonRenderer extends MobRenderer<VoidDragonEntity, VoidDragonModel> {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "textures/entity/void_dragon.png");
    private static final ResourceLocation GLOW =
            ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "textures/entity/void_dragon_glow.png");
    private static final ResourceLocation ARMORED =
            ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "textures/entity/void_dragon_armored.png");

    /** The authored model is 1.0 scale = ~3 blocks tall; base shadow radius. */
    private static final float BASE_SHADOW = 2.2F;

    public VoidDragonRenderer(EntityRendererProvider.Context context) {
        super(context, new VoidDragonModel(context.bakeLayer(VoidDragonModel.LAYER)), BASE_SHADOW);
        // Fullbright emissive layer: glowing eyes, void veins, wing ribs, horn tips.
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
        // A flying dragon pitches with its look direction; MobRenderer would clamp it.
        float fly = entity.getFlyProgress(partialTick);
        if (fly > 0.01F) {
            float pitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
            poseStack.mulPose(Axis.XP.rotationDegrees(pitch * fly * 0.55F));
        }
    }

    @Override
    protected void scale(VoidDragonEntity entity, PoseStack poseStack, float partialTick) {
        float scale = entity.getDragonScale();
        // A whisper of squash on the landing frame makes a heavy dragon feel heavy.
        float land = entity.isFlying() ? 0.0F : entity.getFlyProgress(partialTick);
        poseStack.scale(scale, scale * (1.0F - land * 0.02F), scale);
    }

    @Override
    public float getShadowRadius(VoidDragonEntity entity) {
        return BASE_SHADOW * Math.min(3.0F, entity.getDragonScale());
    }

    @Override
    protected float getFlipDegrees(VoidDragonEntity entity) {
        return 0.0F; // a dragon doesn't tip over when it dies
    }
}
