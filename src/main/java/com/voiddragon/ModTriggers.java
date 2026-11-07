package com.voiddragon;

import com.mojang.serialization.Codec;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

/**
 * Custom advancement triggers used by the mod's own advancement tree.
 *
 * <ul>
 *   <li>{@code voiddragon:bond_max} — the player's dragon reached a full bond.</li>
 *   <li>{@code voiddragon:dragon_grown} — the player's dragon grew to the adult stage.</li>
 * </ul>
 */
public class ModTriggers {

    public static final DeferredRegister<SimpleCriterionTrigger<?>> TRIGGERS =
            DeferredRegister.create(Registries.TRIGGER_TYPE, VoidDragonMod.MODID);

    public static final DeferredHolder<SimpleCriterionTrigger<?>, DragonTrigger> BOND_MAX =
            TRIGGERS.register("bond_max", DragonTrigger::new);
    public static final DeferredHolder<SimpleCriterionTrigger<?>, DragonTrigger> DRAGON_GROWN =
            TRIGGERS.register("dragon_grown", DragonTrigger::new);
    public static final DeferredHolder<SimpleCriterionTrigger<?>, DragonTrigger> DRAGON_TAMED =
            TRIGGERS.register("dragon_tamed", DragonTrigger::new);

    /** A trigger with no conditions — firing is enough to award it. */
    public static class DragonTrigger extends SimpleCriterionTrigger<DragonTrigger.Instance> {

        @Override
        public Codec<Instance> codec() {
            return Instance.CODEC;
        }

        public void trigger(ServerPlayer player) {
            this.trigger(player, instance -> true);
        }

        public record Instance(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {
            public static final Codec<Instance> CODEC = EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                    .xmap(Instance::new, Instance::player).codec();

            @Override
            public Optional<ContextAwarePredicate> player() {
                return this.player;
            }
        }

        /** Helper for datapack/JSON use. */
        public static Criterion<Instance> criterion() {
            return new Criterion<>(BOND_MAX.get(), new Instance(Optional.empty()));
        }
    }
}
