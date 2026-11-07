package com.voiddragon;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VoidDragonMod.MODID);

    /** Taming food, growth food and healing item. */
    public static final DeferredItem<Item> VOID_SHARD = ITEMS.register("void_shard",
            () -> new LoreItem(new Item.Properties().rarity(Rarity.EPIC), "item.voiddragon.void_shard.lore"));

    /** Required before an adult dragon will let anybody ride it. */
    public static final DeferredItem<Item> VOID_SADDLE = ITEMS.register("void_saddle",
            () -> new LoreItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), "item.voiddragon.void_saddle.lore"));

    /** Plate armour for the dragon: extra armour points and 25% damage reduction. */
    public static final DeferredItem<Item> VOID_ARMOR = ITEMS.register("void_armor",
            () -> new LoreItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC), "item.voiddragon.void_armor.lore"));

    public static final DeferredItem<DeferredSpawnEggItem> VOID_DRAGON_SPAWN_EGG =
            ITEMS.register("void_dragon_spawn_egg", () -> new DeferredSpawnEggItem(
                    ModEntities.VOID_DRAGON, 0x0B0614, 0x9B30FF, new Item.Properties().rarity(Rarity.RARE)));
}
