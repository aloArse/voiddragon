package com.voiddragon.client;

import com.voiddragon.VoidDragonMod;
import com.voiddragon.entity.VoidDragonEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

/**
 * A compact rider HUD: void energy, bond progress and the ability bar with live
 * cooldowns. Kept small and anchored above the hotbar so it stays readable on a
 * phone screen, where an inventory-based UI would be painful to use.
 */
@EventBusSubscriber(modid = VoidDragonMod.MODID, value = Dist.CLIENT)
public class DragonHud {

    private static final int ACCENT = 0xFFB57BFF;
    private static final int ACCENT_DIM = 0xFF4B2A72;
    private static final int PANEL = 0xCC0B0614;
    private static final int PANEL_BORDER = 0xFF3A2255;
    private static final int GREY = 0xFF7A7488;
    private static final int GREEN = 0xFF7BE07B;
    private static final int RED = 0xFFFF6B6B;

    private static ResourceLocation icon(String name) {
        return ResourceLocation.fromNamespaceAndPath(VoidDragonMod.MODID, "textures/gui/ability_" + name + ".png");
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui || mc.screen != null) {
            return;
        }
        if (!(mc.player.getVehicle() instanceof VoidDragonEntity dragon)) {
            return;
        }
        GuiGraphics g = event.getGuiGraphics();
        int screenW = g.guiWidth();
        int screenH = g.guiHeight();

        int barW = Math.min(150, screenW - 40);
        int x = (screenW - barW) / 2;
        int y = screenH - 62;
        int barH = 5;
        int gap = 7;

        // ---- panel
        g.fill(x - 6, y - 16, x + barW + 6, y + barH * 2 + gap + 6, PANEL);
        g.fill(x - 6, y - 16, x + barW + 6, y - 15, PANEL_BORDER);
        g.fill(x - 6, y + barH * 2 + gap + 5, x + barW + 6, y + barH * 2 + gap + 6, PANEL_BORDER);

        // ---- void energy
        float energy = dragon.getEnergy() / VoidDragonEntity.MAX_ENERGY;
        g.drawString(mc.font, Component.translatable("hud.voiddragon.energy"),
                x, y - 13, dragon.isBreathLocked() ? RED : ACCENT, false);
        g.fill(x, y, x + barW, y + barH, 0xFF150D22);
        g.fill(x, y, x + (int) (barW * Mth.clamp(energy, 0.0F, 1.0F)), y + barH,
                dragon.isBreathLocked() ? 0xFF8B1E1E : ACCENT);
        g.fill(x, y, x + barW, y + 1, ACCENT_DIM);

        // ---- bond
        int bond = dragon.getBond();
        int y2 = y + barH + gap - 4;
        String bondLabel = Component.translatable("hud.voiddragon.bond").getString() + " " + bond + "%"
                + (bond >= 100 ? "  \u2726" : "");
        g.drawString(mc.font, bondLabel, x, y2 - 10, bond >= 100 ? GREEN : ACCENT, false);
        g.fill(x, y2, x + barW, y2 + barH, 0xFF150D22);
        g.fill(x, y2, x + (int) (barW * bond / 100.0F), y2 + barH, bond >= 100 ? GREEN : 0xFF7A3FD6);

        // ---- ability bar
        int slot = 26;
        int count = 4;
        int totalW = count * slot;
        int ax = (screenW - totalW) / 2;
        int ay = y - 34;
        drawAbility(g, mc, dragon, ax, ay, "flap", Component.translatable("key.voiddragon.flight"),
                VoidDragonEntity.BOND_FLIGHT, dragon.isFlying() ? 0 : -1, dragon.isFlying());
        drawAbility(g, mc, dragon, ax + slot, ay, "rush", Component.translatable("key.voiddragon.rush"),
                VoidDragonEntity.BOND_RUSH, dragon.getDashCooldown(), false);
        drawAbility(g, mc, dragon, ax + slot * 2, ay, "roar", Component.translatable("key.voiddragon.roar"),
                VoidDragonEntity.BOND_ROAR, dragon.getRoarCooldown(), false);
        drawAbility(g, mc, dragon, ax + slot * 3, ay, "portal", Component.translatable("key.voiddragon.portal"),
                VoidDragonEntity.BOND_PORTAL, dragon.getPortalCooldown(), false);

        // ---- breath hint (always available)
        Component breath = Component.translatable("key.voiddragon.breath");
        int bw = mc.font.width(breath) + 8;
        g.fill(ax - bw - 6, ay + 3, ax - 10, ay + 19, 0xB00B0614);
        g.drawString(mc.font, breath, ax - bw - 2, ay + 7,
                dragon.isBreathLocked() ? GREY : 0xFFB57BFF, false);
    }

    private static void drawAbility(GuiGraphics g, Minecraft mc, VoidDragonEntity dragon, int x, int y,
                                    String icon, Component label, int requiredBond, int cooldown, boolean active) {
        boolean unlocked = dragon.getBond() >= requiredBond;
        int size = 20;

        g.fill(x, y, x + size, y + size, active ? 0xFF3A2255 : PANEL);
        g.fill(x, y, x + size, y + 1, unlocked ? PANEL_BORDER : 0xFF2A2130);
        if (unlocked) {
            g.blit(icon(icon), x + 2, y + 2, 0, 0, 16, 16, 32, 32);
        } else {
            g.drawString(mc.font, "\u2716", x + 6, y + 6, 0xFF5A5468, false);
        }

        if (cooldown > 0) {
            float frac = Mth.clamp(cooldown / 140.0F, 0.0F, 1.0F);
            int h = (int) (size * frac);
            g.fill(x, y + size - h, x + size, y + size, 0xAA000000);
            String secs = String.valueOf(cooldown / 20 + 1);
            g.drawString(mc.font, secs, x + size - mc.font.width(secs) - 1, y + size - 9, 0xFFFFFFFF, true);
        }

        // hotkey hint under the icon
        String key = hotkey(icon);
        g.drawString(mc.font, key, x + (size - mc.font.width(key)) / 2, y + size + 2,
                unlocked ? 0xFFB0A8C0 : 0xFF574F66, false);
        if (!unlocked) {
            String need = String.valueOf(requiredBond);
            g.drawString(mc.font, need, x + (size - mc.font.width(need)) / 2, y + size + 10, 0xFF574F66, false);
        }
    }

    private static String hotkey(String icon) {
        var mapping = switch (icon) {
            case "flap" -> ClientModEvents.FLIGHT;
            case "rush" -> ClientModEvents.RUSH;
            case "roar" -> ClientModEvents.ROAR;
            case "portal" -> ClientModEvents.PORTAL;
            default -> ClientModEvents.BREATH;
        };
        String name = mapping.getTranslatedKeyMessage().getString();
        // keep the little key badge inside the slot
        return name.length() > 4 ? name.substring(0, 4) : name;
    }
}
