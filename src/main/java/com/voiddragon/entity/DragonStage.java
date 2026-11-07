package com.voiddragon.entity;

/**
 * The five life stages of a Void Dragon.
 *
 * <p>Each stage carries everything the rest of the mod needs to know about it in
 * one place: display name key, growth points required to reach it, the size it
 * renders at (relative to the adult), its combat stats and which rider abilities
 * are unlocked. Keeping the table centralised means the entity, the HUD, the
 * renderer and the language file can never disagree about the progression.</p>
 *
 * <p>Size is a relative render/box multiplier. 1.0 is the "adult" baseline that
 * the model is authored at (about 3 blocks tall, 7 blocks nose to tail). The
 * Dragon God is deliberately enormous — roughly a village across - and is
 * intended to be tamed by a player who has played a long time and killed a lot
 * of dragons, so the player-facing size slider exists for exactly that case.</p>
 */
public enum DragonStage {
    /** نوزاد - a hatchling: small, fragile, curious. */
    INFANT("infant", 0, 0.30F, 60.0D, 5.0D, 2.0D, 0),
    /** جوان - juvenile: still growing, fast, can be ridden but cannot fly. */
    JUVENILE("juvenile", 18, 0.55F, 140.0D, 11.0D, 5.0D, 1),
    /** بالغ - adult: the classic dragon. Flight unlocks here. */
    ADULT("adult", 48, 0.80F, 300.0D, 20.0D, 9.0D, 2),
    /** پادشاه اژدهایان - King of Dragons, semi-divine. */
    KING("king", 110, 1.15F, 520.0D, 32.0D, 14.0D, 3),
    /** خدای اژدهایان - Dragon God, the final stage. */
    GOD("god", 240, 2.60F, 900.0D, 50.0D, 20.0D, 4);

    public final String key;
    /** Growth points that must be earned to reach this stage. */
    public final int requiredGrowth;
    /** Render scale relative to the authored model. */
    public final float baseScale;
    public final double maxHealth;
    public final double attackDamage;
    public final double armor;
    /** Highest ability tier (0 = breath only, 4 = everything). */
    public final int abilityTier;

    DragonStage(String key, int requiredGrowth, float baseScale, double maxHealth,
                double attackDamage, double armor, int abilityTier) {
        this.key = key;
        this.requiredGrowth = requiredGrowth;
        this.baseScale = baseScale;
        this.maxHealth = maxHealth;
        this.attackDamage = attackDamage;
        this.armor = armor;
        this.abilityTier = abilityTier;
    }

    public String translationKey() {
        return "voiddragon.stage." + this.key;
    }

    public static final DragonStage[] VALUES = values();

    public static DragonStage byIndex(int index) {
        return VALUES[Math.max(0, Math.min(VALUES.length - 1, index))];
    }

    /** The stage a dragon with {@code growthPoints} belongs in. */
    public static DragonStage forGrowth(int growthPoints) {
        DragonStage result = INFANT;
        for (DragonStage stage : VALUES) {
            if (growthPoints >= stage.requiredGrowth) {
                result = stage;
            }
        }
        return result;
    }

    public int index() {
        return ordinal();
    }

    public DragonStage next() {
        return ordinal() + 1 < VALUES.length ? VALUES[ordinal() + 1] : null;
    }

    /** Growth points still needed for the next stage (0 at the final stage). */
    public int pointsToNext(int growthPoints) {
        DragonStage next = next();
        return next == null ? 0 : Math.max(0, next.requiredGrowth - growthPoints);
    }

    public boolean canFly() {
        return abilityTier >= 2;
    }

    public boolean canBeSaddled() {
        return abilityTier >= 1;
    }

    /** Wing span / body length in blocks at the given total scale. */
    public static float bodyLengthBlocks(float totalScale) {
        return 7.2F * totalScale;
    }
}
