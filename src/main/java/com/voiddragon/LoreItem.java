package com.voiddragon;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * A simple item that shows a lore line under its name — used for all of the
 * mod's gear so the tooltip explains what the item does in-game.
 */
public class LoreItem extends Item {
    private final String loreKey;

    public LoreItem(Properties properties, String loreKey) {
        super(properties);
        this.loreKey = loreKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable(this.loreKey).withStyle(net.minecraft.ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
