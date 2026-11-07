package com.voiddragon;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VoidDragonMod.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.voiddragon"))
                    .icon(() -> new ItemStack(ModItems.VOID_SHARD.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.VOID_SHARD.get());
                        output.accept(ModItems.VOID_DRAGON_SPAWN_EGG.get());
                    })
                    .build());
}
