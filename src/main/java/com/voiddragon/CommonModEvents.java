package com.voiddragon;

import com.voiddragon.entity.VoidDragonEntity;
import com.voiddragon.network.AbilityPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.util.RandomSource;
import net.minecraft.core.BlockPos;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = VoidDragonMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CommonModEvents {

    @SubscribeEvent
    public static void onSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        // Wild dragons only spawn in the End, on the ground of the outer islands.
        event.register(ModEntities.VOID_DRAGON.get(), net.minecraft.world.entity.SpawnPlacementTypes.ON_GROUND,
                net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                VoidDragonEntity::checkDragonSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.VOID_DRAGON.get(), VoidDragonEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1").optional();
        registrar.playToServer(AbilityPayload.TYPE, AbilityPayload.CODEC, (payload, context) ->
                context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer player
                            && player.getVehicle() instanceof VoidDragonEntity dragon) {
                        dragon.handleAbility(player, payload.ability());
                    }
                }));
    }

    /** Right-click a Dragon Egg with a Void Shard to hatch a Void Dragon hatchling. */
    @EventBusSubscriber(modid = VoidDragonMod.MODID)
    public static class GameEvents {

        @SubscribeEvent
        public static void onEggHatch(PlayerInteractEvent.RightClickBlock event) {
            if (!VoidDragonConfig.INSTANCE.hatchDragonEggs.get()) {
                return;
            }
            Player player = event.getEntity();
            if (!event.getItemStack().is(ModItems.VOID_SHARD.get())) {
                return;
            }
            if (!event.getLevel().getBlockState(event.getPos()).is(Blocks.DRAGON_EGG)) {
                return;
            }
            if (!(event.getLevel() instanceof ServerLevel level)) {
                return;
            }
            BlockPos pos = event.getPos();
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);

            VoidDragonEntity hatchling = ModEntities.VOID_DRAGON.get().create(level);
            if (hatchling == null) {
                return;
            }
            hatchling.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, player.getYRot(), 0.0F);
            hatchling.setGrowth(VoidDragonEntity.GROWTH_HATCHLING);
            hatchling.setTame(true, true);
            hatchling.setOwnerUUID(player.getUUID());
            hatchling.setBond(15);
            level.addFreshEntity(hatchling);

            level.removeBlock(pos, false);
            event.getItemStack().consume(1, player);
            level.playSound(null, pos, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.NEUTRAL, 1.6F, 1.5F);
            level.sendParticles(ParticleTypes.REVERSE_PORTAL, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D,
                    90, 0.9D, 0.9D, 0.9D, 0.45D);
            level.sendParticles(ParticleTypes.SQUID_INK, pos.getX() + 0.5D, pos.getY() + 0.6D, pos.getZ() + 0.5D,
                    24, 0.8D, 0.8D, 0.8D, 0.05D);
            if (player instanceof ServerPlayer sp) {
                ModTriggers.DRAGON_TAMED.get().trigger(sp);
                sp.displayClientMessage(net.minecraft.network.chat.Component
                        .translatable("message.voiddragon.hatched")
                        .withStyle(net.minecraft.ChatFormatting.LIGHT_PURPLE), false);
            }
        }
    }
}
