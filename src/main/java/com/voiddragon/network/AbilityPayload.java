package com.voiddragon.network;

import com.voiddragon.VoidDragonMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Client -&gt; server: the rider pressed an ability key.
 * 0 = void breath, 1 = void rush, 2 = flight toggle, 3 = void roar, 4 = void step.
 */
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
