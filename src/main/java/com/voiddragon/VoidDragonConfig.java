package com.voiddragon;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Server-side tunables. Everything a pack author might want to tweak lives here,
 * so the mod stays friendly next to other mods.
 */
public class VoidDragonConfig {

    public static final ModConfigSpec SPEC;
    public static final VoidDragonConfig INSTANCE;

    static {
        Pair<VoidDragonConfig, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(VoidDragonConfig::new);
        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
    }

    public final ModConfigSpec.BooleanValue allowFlight;
    public final ModConfigSpec.BooleanValue voidBreathBreaksBlocks;
    public final ModConfigSpec.BooleanValue roarAffectsPlayers;
    public final ModConfigSpec.BooleanValue hatchDragonEggs;
    public final ModConfigSpec.BooleanValue wildDragonsInTheEnd;
    public final ModConfigSpec.DoubleValue abilityCooldownScale;
    public final ModConfigSpec.IntValue naturalGrowthSeconds;

    private VoidDragonConfig(ModConfigSpec.Builder builder) {
        builder.push("gameplay");
        allowFlight = builder
                .comment("Allow tamed adult Void Dragons to fly (F while riding).")
                .define("allowFlight", true);
        voidBreathBreaksBlocks = builder
                .comment("Let the Void Breath beam wear away fragile blocks (leaves, glass, crops...).")
                .define("voidBreathBreaksBlocks", true);
        roarAffectsPlayers = builder
                .comment("The Void Roar hurts and darkens other players, not only monsters.")
                .define("roarAffectsPlayers", true);
        wildDragonsInTheEnd = builder
                .comment("Wild Void Dragons slowly spawn in the End so they can be found in survival.")
                .define("wildDragonsInTheEnd", true);
        hatchDragonEggs = builder
                .comment("Right-clicking a Dragon Egg with a Void Shard hatches a Void Dragon hatchling.")
                .define("hatchDragonEggs", true);
        abilityCooldownScale = builder
                .comment("Multiplier on all ability cooldowns. 1.0 = default, 0.5 = twice as fast.")
                .defineInRange("abilityCooldownScale", 1.0D, 0.05D, 10.0D);
        naturalGrowthSeconds = builder
                .comment("Seconds of being ridden/fed that a hatchling needs per growth level "
                        + "(0 disables passive growth; shards and kills always work).")
                .defineInRange("naturalGrowthSeconds", 90, 0, 36000);
        builder.pop();
    }
}
