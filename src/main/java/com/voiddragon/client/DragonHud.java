package com.voiddragon.client;

import com.voiddragon.VoidDragonMod;
import com.voiddragon.entity.DragonStage;
import com.voiddragon.entity.VoidDragonEntity;
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
 * The rider HUD: stage + growth, void energy, bond, the ability bar and the size
 * control.
 *
 * <p>Everything is drawn in a compact block above the hotbar so it stays usable
 * on a phone screen. The two size buttons are deliberately large and sit at the
 * bottom corners of the panel, because the one sure thing about a Dragon God is
 * that it eventually does not fit anywhere.</p>
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
    private static final int GOLD = 0xFFFFD27B;

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

        int barW = Math.min(150, screenW - 48);
        int x = (screenW - barW) / 2;
        int y = screenH - 62;
        int barH = 5;
        int gap = 7;

        // ---- panel
        g.fill(x - 8, y - 26, x + barW + 8, y + barH * 2 + gap + 6, PANEL);
        g.fill(x - 8, y - 26, x + barW + 8, y - 25, PANEL_BORDER);
        g.fill(x - 8, y + barH * 2 + gap + 5, x + barW + 8, y + barH * 2 + gap + 6, PANEL_BORDER);

        // ---- stage name + growth to the next stage
        DragonStage stage = dragon.getStage();
        int colour = stage == DragonStage.GOD ? GOLD : ACCENT;
        Component stageName = Component.translatable(stage.translationKey());
        g.drawString(mc.font, stageName, x, y - 23, colour, false);
        DragonStage next = stage.next();
        if (next != null) {
            int need = next.requiredGrowth - dragon.getGrowth();
            String growth = need + " \u2192 " + Component.translatable(next.translationKey()).getString();
            g.drawString(mc.font, growth, x + barW - mc.font.width(growth), y - 23, GREY, false);
        } else {
            String max = Component.translatable("hud.voiddragon.max_stage").getString() + "  \u2726";
            g.drawString(mc.font, max, x + barW - mc.font.width(max), y - 23, GOLD, false);
        }

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

        // ---- ability bar (six slots)
        int slot = 22;
        int count = 6;
        int totalW = count * slot;
        int ax = (screenW - totalW) / 2;
        int ay = y - 46;
        drawAbility(g, mc, dragon, stage, ax, ay, "breath", "breath", 0, -1, dragon.isBreathing());
        drawAbility(g, mc, dragon, stage, ax + slot, ay, "rush", "rush", 1, dragon.getDashCooldown(), false);
        drawAbility(g, mc, dragon, stage, ax + slot * 2, ay, "flap", "flight", 2,
                dragon.isFlying() ? 0 : -1, dragon.isFlying());
        drawAbility(g, mc, dragon, stage, ax + slot * 3, ay, "roar", "roar", 3, dragon.getRoarCooldown(), false);
        drawAbility(g, mc, dragon, stage, ax + slot * 4, ay, "portal", "portal", 4, dragon.getPortalCooldown(), false);
        drawAbility(g, mc, dragon, stage, ax + slot * 5, ay, "godstorm", "godstorm", 4, -1, false);

        // ---- size control: two big touch targets, and the current multiplier
        drawSizeControl(g, mc, dragon, x, y2 + barH + 4, barW);
    }

    private static void drawSizeControl(GuiGraphics g, Minecraft mc, VoidDragonEntity dragon, int x, int y, int width) {
        int h = 11;
        g.fill(x, y, x + width, y + h, 0xB00B0614);
        g.fill(x, y, x + width, y + 1, PANEL_BORDER);

        // minus / plus hit areas
        g.fill(x + 1, y + 1, x + 13, y + h - 1, 0xFF2A1540);
        g.fill(x + width - 13, y + 1, x + width - 1, y + h - 1, 0xFF2A1540);
        g.drawString(mc.font, "-", x + 5, y + 2, ACCENT, false);
        g.drawString(mc.font, "+", x + width - 10, y + 2, ACCENT, false);

        String label = Component.translatable("hud.voiddragon.size").getString() + " "
                + String.format("%.2f", dragon.getDragonScale()) + "x";
        if (dragon.getSizeOverride() > 0.0F) {
            label = label + " \u2022 " + Component.translatable("hud.voiddragon.manual").getString();
        }
        g.drawString(mc.font, label, x + width / 2 - mc.font.width(label) / 2, y + 2, ACCENT, false);
    }

    private static void drawAbility(GuiGraphics g, Minecraft mc, VoidDragonEntity dragon, DragonStage stage,
                                    int x, int y, String icon, String key, int tier, int cooldown, boolean active) {
        boolean unlocked = stage.abilityTier >= tier;
        int size = 18;

        g.fill(x, y, x + size, y + size, active ? 0xFF3A2255 : PANEL);
        g.fill(x, y, x + size, y + 1, unlocked ? PANEL_BORDER : 0xFF2A2130);
        if (unlocked) {
            g.blit(icon(icon), x + 1, y + 1, 0, 0, 16, 16, 32, 32);
        } else {
            g.drawString(mc.font, "\u2716", x + 5, y + 4, 0xFF5A5468, false);
        }

        if (cooldown > 0) {
            float frac = Mth.clamp(cooldown / 140.0F, 0.0F, 1.0F);
            int h = (int) (size * frac);
            g.fill(x, y + size - h, x + size, y + size, 0xAA000000);
            String secs = String.valueOf(cooldown / 20 + 1);
            g.drawString(mc.font, secs, x + size - mc.font.width(secs) - 1, y + size - 9, 0xFFFFFFFF, true);
        }

        String hotkey = hotkey(key);
        g.drawString(mc.font, hotkey, x + (size - mc.font.width(hotkey)) / 2, y + size + 1,
                unlocked ? 0xFFB0A8C0 : 0xFF574F66, false);
    }

    private static String hotkey(String key) {
        var mapping = switch (key) {
            case "breath" -> ClientModEvents.BREATH;
            case "rush" -> ClientModEvents.RUSH;
            case "flight" -> ClientModEvents.FLIGHT;
            case "roar" -> ClientModEvents.ROAR;
            case "portal" -> ClientModEvents.PORTAL;
            case "godstorm" -> ClientModEvents.STORM;
            default -> ClientModEvents.BREATH;
        };
        String name = mapping.getTranslatedKeyMessage().getString();
        return name.length() > 4 ? name.substring(0, 4) : name;
    }
}
