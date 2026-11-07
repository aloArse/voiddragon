package com.voiddragon.client;

import com.voiddragon.ModEntities;
import com.voiddragon.VoidDragonMod;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = VoidDragonMod.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    private static final String CATEGORY = "key.categories.voiddragon";

    public static final KeyMapping BREATH = new KeyMapping("key.voiddragon.breath", GLFW.GLFW_KEY_R, CATEGORY);
    public static final KeyMapping RUSH = new KeyMapping("key.voiddragon.rush", GLFW.GLFW_KEY_G, CATEGORY);
    public static final KeyMapping FLIGHT = new KeyMapping("key.voiddragon.flight", GLFW.GLFW_KEY_X, CATEGORY);
    public static final KeyMapping ROAR = new KeyMapping("key.voiddragon.roar", GLFW.GLFW_KEY_V, CATEGORY);

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(BREATH);
        event.register(RUSH);
        event.register(FLIGHT);
        event.register(ROAR);
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(VoidDragonModel.LAYER, VoidDragonModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.VOID_DRAGON.get(), VoidDragonRenderer::new);
    }
}
