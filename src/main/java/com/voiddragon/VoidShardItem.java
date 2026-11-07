package com.voiddragon;

import com.voiddragon.entity.VoidDragonEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.List;

/**
 * The Void Shard. Besides taming, healing and growing dragons, sneaking and
 * right-clicking with it calls your nearest dragon to your side — a lifesaver on
 * touch controls where typing {@code /tp} is not an option.
 */
public class VoidShardItem extends LoreItem {

    private static final double RECALL_RANGE = 96.0D;

    public VoidShardItem(Properties properties) {
        super(properties, "item.voiddragon.void_shard.lore");
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(stack);
        }
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        List<VoidDragonEntity> dragons = level.getEntitiesOfClass(VoidDragonEntity.class,
                player.getBoundingBox().inflate(RECALL_RANGE),
                dragon -> dragon.isTame() && player.getUUID().equals(dragon.getOwnerUUID()));
        if (dragons.isEmpty()) {
            player.displayClientMessage(Component.translatable("message.voiddragon.no_dragon")
                    .withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.fail(stack);
        }

        VoidDragonEntity dragon = dragons.stream()
                .min(Comparator.comparingDouble(d -> d.distanceToSqr(player)))
                .orElseThrow();
        if (dragon.isVehicle()) {
            player.displayClientMessage(Component.translatable("message.voiddragon.dragon_busy")
                    .withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.fail(stack);
        }

        if (!(level instanceof ServerLevel server) || !dragon.teleportNear(server, player.position().add(2.5D, 0.0D, 0.0D))) {
            player.displayClientMessage(Component.translatable("message.voiddragon.recall_blocked")
                    .withStyle(ChatFormatting.GRAY), true);
            return InteractionResultHolder.fail(stack);
        }
        player.getCooldowns().addCooldown(this, 200);
        player.displayClientMessage(Component.translatable("message.voiddragon.recalled")
                .withStyle(ChatFormatting.LIGHT_PURPLE), true);
        return InteractionResultHolder.success(stack);
    }
}
