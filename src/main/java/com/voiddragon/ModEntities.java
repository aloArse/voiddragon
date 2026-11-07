package com.voiddragon;

import com.voiddragon.entity.VoidDragonEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, VoidDragonMod.MODID);

    public static final DeferredHolder<EntityType<?>, EntityType<VoidDragonEntity>> VOID_DRAGON =
            ENTITIES.register("void_dragon", () -> EntityType.Builder.of(VoidDragonEntity::new, MobCategory.CREATURE)
                    .sized(2.2F, 2.4F)
                    .passengerAttachments(1.9F)
                    .clientTrackingRange(12)
                    .updateInterval(2)
                    .fireImmune()
                    .build("void_dragon"));
}
