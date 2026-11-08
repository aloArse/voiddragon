package com.voiddragon.client;

import com.voiddragon.VoidDragonMod;
import com.voiddragon.entity.VoidDragonEntity;
import com.voiddragon.network.AbilityPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = VoidDragonMod.MODID, value = Dist.CLIENT)
public class ClientGameEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null || mc.screen != null || !(player.getVehicle() instanceof VoidDragonEntity)) {
            return;
        }
        // Climb is a channel too: hold it to flap upwards. Only sent while
        // airborne, so the same key can stay bound to jump on the ground.
        if (ClientModEvents.ASCEND.isDown() && player.tickCount % 3 == 0
                && player.getVehicle() instanceof VoidDragonEntity dragon && dragon.isFlying()) {
            PacketDistributor.sendToServer(new AbilityPayload(VoidDragonEntity.ABILITY_ASCEND));
        }
        // Breath is a channel: fire while the key is held (throttled to 4x a second).
        if (ClientModEvents.BREATH.isDown() && player.tickCount % 5 == 0) {
            PacketDistributor.sendToServer(new AbilityPayload(0));
        }
        while (ClientModEvents.RUSH.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(1));
        }
        while (ClientModEvents.FLIGHT.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(2));
        }
        while (ClientModEvents.ROAR.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(3));
        }
        while (ClientModEvents.PORTAL.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(4));
        }
        while (ClientModEvents.STORM.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(VoidDragonEntity.ABILITY_GODSTORM));
        }
        while (ClientModEvents.SIZE_UP.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(VoidDragonEntity.ABILITY_SIZE_UP));
        }
        while (ClientModEvents.SIZE_DOWN.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(VoidDragonEntity.ABILITY_SIZE_DOWN));
        }
        while (ClientModEvents.SIZE_AUTO.consumeClick()) {
            PacketDistributor.sendToServer(new AbilityPayload(VoidDragonEntity.ABILITY_SIZE_AUTO));
        }
    }
}
