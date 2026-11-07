package com.voiddragon.network;

import com.voiddragon.VoidDragonMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Sent client -> server when the rider presses an ability key. 0 = breath, 1 = rush, 2 = flight, 3 = roar. */
public record AbilityPayload(int ability) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<AbilityPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "ability"));

    public static final StreamCodec<ByteBuf, AbilityPayload> CODEC =
            ByteBufCodecs.VAR_INT.map(AbilityPayload::new, AbilityPayload::ability);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
