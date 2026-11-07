package com.voiddragon;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

/**
 * Void Dragon — a tameable, rideable dragon for Minecraft 1.21.1 (NeoForge).
 *
 * <p>Features: taming with Void Shards, bonded growth stages (hatchling → adult),
 * saddle + void armour, flight, void breath beam, void rush, void roar,
 * short-range void portal, dragon egg hatching and a fully animated custom model.</p>
 */
@Mod(VoidDragonMod.MODID)
public class VoidDragonMod {
    public static final String MODID = "voiddragon";

    public VoidDragonMod(IEventBus modEventBus, ModContainer modContainer) {
        ModEntities.ENTITIES.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModTabs.TABS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, VoidDragonConfig.SPEC);
    }
}
