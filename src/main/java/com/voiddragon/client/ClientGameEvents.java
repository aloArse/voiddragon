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
        if (ClientModEvents.BREATH.isDown()) {
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
    }
}
