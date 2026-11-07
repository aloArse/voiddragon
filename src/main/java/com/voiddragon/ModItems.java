package com.voiddragon;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VoidDragonMod.MODID);

    /** Taming / healing item for the Void Dragon. */
    public static final DeferredItem<Item> VOID_SHARD =
            ITEMS.registerSimpleItem("void_shard", new Item.Properties().rarity(Rarity.EPIC));

    public static final DeferredItem<DeferredSpawnEggItem> VOID_DRAGON_SPAWN_EGG =
            ITEMS.register("void_dragon_spawn_egg", () -> new DeferredSpawnEggItem(
                    ModEntities.VOID_DRAGON, 0x0B0614, 0x9B30FF, new Item.Properties()));
}
