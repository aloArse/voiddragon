package com.voiddragon;

import com.voiddragon.entity.VoidDragonEntity;
import com.voiddragon.network.AbilityPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = VoidDragonMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.VOID_DRAGON.get(), VoidDragonEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(AbilityPayload.TYPE, AbilityPayload.CODEC, (payload, context) ->
                context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player
                            && player.getVehicle() instanceof VoidDragonEntity dragon) {
                        dragon.handleAbility(player, payload.ability());
                    }
                }));
    }
}
