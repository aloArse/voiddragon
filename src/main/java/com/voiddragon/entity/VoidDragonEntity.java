package com.voiddragon.entity;

import com.voiddragon.ModItems;
import com.voiddragon.ModTriggers;
import com.voiddragon.VoidDragonConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.Difficulty;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * The Void Dragon — a fully featured, tameable and rideable shadow dragon.
 *
 * <p><b>Progression</b> (bond, 0 → 100): the more you fly, fight and feed your dragon,
 * the deeper the bond and the more of its void powers it lets you use.</p>
 *
 * <ul>
 *   <li>Bond 0 – flight basics, mounted ground travel, Void Breath</li>
 *   <li>Bond 25 – Void Rush (a ramming dash that shreds what it hits)</li>
 *   <li>Bond 45 – true flight (leave the ground, pitch to climb and dive)</li>
 *   <li>Bond 65 – Void Roar (pulls everything in, drowns it in darkness)</li>
 *   <li>Bond 85 – Void Step (short range portal teleport)</li>
 * </ul>
 *
 * <p><b>Growth</b>: hatchling → juvenile → adult. Feeding Void Shards, riding and
 * letting the dragon kill things grows it. Only adults can be saddled and ridden.</p>
 */
public class VoidDragonEntity extends TamableAnimal {

    // ---------------------------------------------------------------- growth / bond

    public static final int GROWTH_HATCHLING = 0;
    public static final int GROWTH_JUVENILE = 1;
    public static final int GROWTH_ADULT = 2;

    /** Growth points needed to reach the next stage. */
    public static final int[] GROWTH_REQUIREMENT = {12, 30};
    public static final float[] GROWTH_SCALE = {0.42F, 0.72F, 1.0F};

    public static final int BOND_RUSH = 25;
    public static final int BOND_FLIGHT = 45;
    public static final int BOND_ROAR = 65;
    public static final int BOND_PORTAL = 85;

    public static final float MAX_ENERGY = 100.0F;

    private static final EntityDataAccessor<Integer> DATA_GROWTH =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_BOND =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_FLYING =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_BREATHING =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SADDLED =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_ARMORED =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);
    /** Synced so the rider HUD can draw bars and cooldowns. */
    private static final EntityDataAccessor<Float> DATA_ENERGY =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_LOCKED =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_DASH_CD =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ROAR_CD =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PORTAL_CD =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.INT);

    /** Violet that decays into black: the signature "shadow" particle. */
    private static final DustColorTransitionOptions SHADOW_DUST = new DustColorTransitionOptions(
            new Vector3f(0.52F, 0.09F, 0.92F), new Vector3f(0.02F, 0.0F, 0.07F), 1.8F);
    /** Pale teal-white magic used for healing / hatching. */
    private static final DustColorTransitionOptions VOID_SPARK = new DustColorTransitionOptions(
            new Vector3f(0.75F, 0.55F, 1.0F), new Vector3f(0.15F, 0.05F, 0.35F), 1.1F);

    // ---------------------------------------------------------------- server state

    private float energy = MAX_ENERGY;
    private int growthPoints;
    private boolean breathLocked;
    private int breathHold;
    private int breathCooldown;
    private int dashCooldown;
    private int roarCooldown;
    private int portalCooldown;
    private int dashTicks;
    private int flyTicks;
    private int bondTimer;
    private boolean growthStatsApplied;
    private int followTeleportCooldown;
    private int idleSoundTimer;
    private final Set<Integer> dashHit = new HashSet<>();

    // ---------------------------------------------------------------- client animation state

    private float flyProgress;
    private float flyProgressO;
    private float mouth;
    private float mouthO;
    private float flapSpeed = 1.0F;

    public VoidDragonEntity(EntityType<? extends VoidDragonEntity> type, Level level) {
        super(type, level);
        this.xpReward = 160;
    }

    /**
     * Wild Void Dragons may only spawn in the End, on solid ground, in a spot with
     * some head room; peaceful difficulty keeps them out entirely. The biome
     * modifier in data/voiddragon/neoforge/biome_modifier decides where they show up.
     */
    public static boolean checkDragonSpawnRules(EntityType<VoidDragonEntity> type, ServerLevelAccessor level,
                                                net.minecraft.world.entity.MobSpawnType spawnType, BlockPos pos,
                                                net.minecraft.util.RandomSource random) {
        if (level.getDifficulty() == net.minecraft.world.Difficulty.PEACEFUL) {
            return false;
        }
        if (!level.getLevel().dimension().equals(Level.END)) {
            return false;
        }
        if (!net.minecraft.world.entity.SpawnPlacements.checkSpawnRules(type, level, spawnType, pos, random)) {
            return false;
        }
        // Needs 4 blocks of air above the ground and a floor to stand on.
        for (int i = 0; i < 4; i++) {
            if (!level.getBlockState(pos.above(i)).canBeReplaced()) {
                return false;
            }
        }
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), net.minecraft.core.Direction.UP);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 220.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 16.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.95D)
                .add(Attributes.STEP_HEIGHT, 1.6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new VoidBreathGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.3D, 12.0F, 3.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 14.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_GROWTH, GROWTH_HATCHLING);
        builder.define(DATA_BOND, 0);
        builder.define(DATA_FLYING, false);
        builder.define(DATA_BREATHING, false);
        builder.define(DATA_SADDLED, false);
        builder.define(DATA_ARMORED, false);
        builder.define(DATA_ENERGY, MAX_ENERGY);
        builder.define(DATA_LOCKED, false);
        builder.define(DATA_DASH_CD, 0);
        builder.define(DATA_ROAR_CD, 0);
        builder.define(DATA_PORTAL_CD, 0);
    }

    // ---------------------------------------------------------------- save / load

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Growth", getGrowth());
        tag.putInt("GrowthPoints", this.growthPoints);
        tag.putInt("Bond", getBond());
        tag.putFloat("Energy", this.energy);
        tag.putBoolean("Saddled", isSaddled());
        tag.putBoolean("Armored", isArmored());
        tag.putBoolean("Flying", isFlying());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setGrowth(tag.getInt("Growth"));
        this.growthPoints = tag.getInt("GrowthPoints");
        setBond(tag.getInt("Bond"));
        this.energy = tag.contains("Energy") ? tag.getFloat("Energy") : MAX_ENERGY;
        setSaddled(tag.getBoolean("Saddled"));
        setArmored(tag.getBoolean("Armored"));
        if (tag.getBoolean("Flying")) {
            setFlying(true);
        }
        applyGrowthAttributes();
    }

    // ---------------------------------------------------------------- state accessors

    public int getGrowth() {
        return this.entityData.get(DATA_GROWTH);
    }

    public void setGrowth(int growth) {
        int clamped = Mth.clamp(growth, GROWTH_HATCHLING, GROWTH_ADULT);
        if (clamped != getGrowth()) {
            this.entityData.set(DATA_GROWTH, clamped);
            this.growthPoints = 0;
            this.applyGrowthAttributes();
            this.refreshDimensions();
        }
    }

    public int getGrowthPoints() {
        return this.growthPoints;
    }

    public boolean isAdultDragon() {
        return getGrowth() >= GROWTH_ADULT;
    }

    /** Feeds growth points, returns true when the dragon grows up a stage. */
    public boolean addGrowth(int points) {
        if (isAdultDragon()) {
            return false;
        }
        this.growthPoints += points;
        int needed = GROWTH_REQUIREMENT[Mth.clamp(getGrowth(), 0, GROWTH_REQUIREMENT.length - 1)];
        if (this.growthPoints >= needed) {
            setGrowth(getGrowth() + 1);
            return true;
        }
        return false;
    }

    public int getBond() {
        return this.entityData.get(DATA_BOND);
    }

    public void setBond(int bond) {
        this.entityData.set(DATA_BOND, Mth.clamp(bond, 0, 100));
    }

    /** Raises the bond, announcing newly unlocked powers to the owner. */
    public void addBond(int amount) {
        if (!isTame() || amount == 0) {
            return;
        }
        int before = getBond();
        if (before >= 100) {
            return;
        }
        int after = Mth.clamp(before + amount, 0, 100);
        setBond(after);
        if (this.level() instanceof ServerLevel && getOwner() instanceof ServerPlayer owner) {
            announceUnlocks(owner, before, after);
            if (before < 100 && after >= 100) {
                owner.displayClientMessage(Component.translatable("message.voiddragon.bond_max")
                        .withStyle(ChatFormatting.LIGHT_PURPLE), false);
                this.level().playSound(null, blockPosition(), SoundEvents.ENDER_DRAGON_GROWL,
                        SoundSource.NEUTRAL, 1.4F, 1.2F);
                ModTriggers.BOND_MAX.get().trigger(owner);
            }
        }
    }

    private void announceUnlocks(ServerPlayer owner, int before, int after) {
        int[][] unlocks = {{BOND_RUSH, 1}, {BOND_FLIGHT, 2}, {BOND_ROAR, 3}, {BOND_PORTAL, 4}};
        String[] names = {"voiddragon.ability.rush", "voiddragon.ability.flight",
                "voiddragon.ability.roar", "voiddragon.ability.portal"};
        for (int i = 0; i < unlocks.length; i++) {
            if (before < unlocks[i][0] && after >= unlocks[i][0]) {
                owner.displayClientMessage(Component.translatable("message.voiddragon.unlocked",
                        Component.translatable(names[i])).withStyle(ChatFormatting.LIGHT_PURPLE), false);
                this.level().playSound(null, blockPosition(), SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.NEUTRAL, 1.0F, 1.4F);
            }
        }
    }

    public boolean hasBond(int threshold) {
        return getBond() >= threshold;
    }

    public boolean isFlying() {
        return this.entityData.get(DATA_FLYING);
    }

    public void setFlying(boolean flying) {
        this.entityData.set(DATA_FLYING, flying);
        this.setNoGravity(flying);
        if (!flying) {
            this.flyTicks = 0;
        }
    }

    public boolean isBreathing() {
        return this.entityData.get(DATA_BREATHING);
    }

    private void setBreathing(boolean b) {
        this.entityData.set(DATA_BREATHING, b);
    }

    public boolean isSaddled() {
        return this.entityData.get(DATA_SADDLED);
    }

    public void setSaddled(boolean saddled) {
        this.entityData.set(DATA_SADDLED, saddled);
    }

    public boolean isArmored() {
        return this.entityData.get(DATA_ARMORED);
    }

    public void setArmored(boolean armored) {
        this.entityData.set(DATA_ARMORED, armored);
    }

    /** Synced: safe to call on the client (the rider HUD uses it). */
    public float getEnergy() {
        return this.entityData.get(DATA_ENERGY);
    }

    public int getDashCooldown() {
        return this.entityData.get(DATA_DASH_CD);
    }

    public int getRoarCooldown() {
        return this.entityData.get(DATA_ROAR_CD);
    }

    public int getPortalCooldown() {
        return this.entityData.get(DATA_PORTAL_CD);
    }

    public boolean isBreathLocked() {
        return this.entityData.get(DATA_LOCKED);
    }

    private void syncVitals() {
        this.entityData.set(DATA_ENERGY, this.energy);
        this.entityData.set(DATA_LOCKED, this.breathLocked);
        this.entityData.set(DATA_DASH_CD, this.dashCooldown);
        this.entityData.set(DATA_ROAR_CD, this.roarCooldown);
        this.entityData.set(DATA_PORTAL_CD, this.portalCooldown);
    }

    // ---------------------------------------------------------------- client animation accessors

    public float getFlyProgress(float partialTick) {
        return Mth.lerp(partialTick, this.flyProgressO, this.flyProgress);
    }

    public float getMouthProgress(float partialTick) {
        return Mth.lerp(partialTick, this.mouthO, this.mouth);
    }

    public float getFlapSpeed() {
        return this.flapSpeed;
    }

    /** Base render scale — hatchlings and juveniles are visibly smaller. */
    @Override
    public float getScale() {
        return GROWTH_SCALE[Mth.clamp(getGrowth(), 0, GROWTH_SCALE.length - 1)];
    }

    public Vec3 getMouthPos() {
        Vec3 look = this.calculateViewVector(this.getXRot(), this.getYHeadRot());
        double up = this.getBbHeight() * (isFlying() ? 0.58D : 0.80D);
        return this.position().add(0.0D, up, 0.0D).add(look.scale(2.6D * getScale()));
    }

    // ---------------------------------------------------------------- growth driven stats

    private void applyGrowthAttributes() {
        int growth = getGrowth();
        setBaseIfPresent(Attributes.MAX_HEALTH, growth == GROWTH_HATCHLING ? 45.0D : growth == GROWTH_JUVENILE ? 120.0D : 220.0D);
        setBaseIfPresent(Attributes.MOVEMENT_SPEED, growth == GROWTH_HATCHLING ? 0.34D : growth == GROWTH_JUVENILE ? 0.32D : 0.30D);
        setBaseIfPresent(Attributes.ATTACK_DAMAGE, growth == GROWTH_HATCHLING ? 4.0D : growth == GROWTH_JUVENILE ? 9.0D : 16.0D);
        setBaseIfPresent(Attributes.ARMOR, growth == GROWTH_HATCHLING ? 2.0D : growth == GROWTH_JUVENILE ? 5.0D : 8.0D);
        if (this.getHealth() > this.getMaxHealth()) {
            this.setHealth(this.getMaxHealth());
        }
    }

    private void setBaseIfPresent(net.minecraft.core.Holder<net.minecraft.world.entity.ai.attributes.Attribute> attribute, double value) {
        AttributeInstance instance = this.getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    private void applyArmorBonus() {
        AttributeInstance armor = this.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            double base = getGrowth() == GROWTH_HATCHLING ? 2.0D : getGrowth() == GROWTH_JUVENILE ? 5.0D : 8.0D;
            armor.setBaseValue(base + (isArmored() ? 10.0D : 0.0D));
        }
    }

    // ---------------------------------------------------------------- interaction

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (this.level().isClientSide) {
            boolean juice = stack.is(ModItems.VOID_SHARD.get()) || stack.is(ModItems.VOID_SADDLE.get())
                    || stack.is(ModItems.VOID_ARMOR.get());
            return juice ? InteractionResult.sidedSuccess(true) : InteractionResult.PASS;
        }

        // ---- Taming
        if (!this.isTame()) {
            if (!stack.is(ModItems.VOID_SHARD.get())) {
                return InteractionResult.PASS;
            }
            stack.consume(1, player);
            int chance = this.getHealth() < this.getMaxHealth() * 0.35F ? 2 : 4;
            if (this.random.nextInt(chance) == 0) {
                this.tame(player);
                if (player instanceof ServerPlayer sp) {
                    ModTriggers.DRAGON_TAMED.get().trigger(sp);
                }
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.setBond(10);
                this.level().broadcastEntityEvent(this, (byte) 7);
                this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1.4F, 1.2F);
                if (player instanceof ServerPlayer sp) {
                    ModTriggers.DRAGON_TAMED.get().trigger(sp);
                    sp.displayClientMessage(Component.translatable("message.voiddragon.tamed")
                            .withStyle(ChatFormatting.LIGHT_PURPLE), false);
                }
                if (this.level() instanceof ServerLevel sl) {
                    sl.sendParticles(VOID_SPARK, getX(), getY() + this.getBbHeight() * 0.6D, getZ(),
                            40, 1.2D, 1.0D, 1.2D, 0.05D);
                }
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
                addBond(0);
            }
            return InteractionResult.sidedSuccess(false);
        }

        if (!this.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }

        // ---- Feeding / healing
        if (stack.is(ModItems.VOID_SHARD.get())) {
            boolean healed = this.getHealth() < this.getMaxHealth();
            boolean grew = addGrowth(4);
            if (healed) {
                this.heal(40.0F);
            }
            stack.consume(1, player);
            addBond(3);
            this.playSound(SoundEvents.GENERIC_EAT, 0.9F, 1.1F);
            if (this.level() instanceof ServerLevel sl) {
                sl.sendParticles(VOID_SPARK, getX(), getY() + this.getBbHeight() * 0.7D, getZ(),
                        18, 0.7D, 0.6D, 0.7D, 0.02D);
                sl.sendParticles(ParticleTypes.HEART, getX(), getY() + this.getBbHeight(), getZ(),
                        3, 0.5D, 0.4D, 0.5D, 0.0D);
            }
            if (grew) {
                onGrewUp(player);
            }
            return InteractionResult.sidedSuccess(false);
        }

        // ---- Equipment
        if (stack.is(ModItems.VOID_SADDLE.get()) && !this.isSaddled()) {
            if (!this.isAdultDragon()) {
                player.displayClientMessage(Component.translatable("message.voiddragon.too_young")
                        .withStyle(ChatFormatting.GRAY), true);
                return InteractionResult.sidedSuccess(false);
            }
            stack.consume(1, player);
            this.setSaddled(true);
            this.playSound(SoundEvents.HORSE_SADDLE, 1.2F, 0.7F);
            return InteractionResult.sidedSuccess(false);
        }

        if (stack.is(ModItems.VOID_ARMOR.get()) && this.isSaddled() && !this.isArmored()) {
            stack.consume(1, player);
            this.setArmored(true);
            applyArmorBonus();
            this.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE.value(), 1.2F, 0.6F);
            if (this.level() instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.ENCHANT, getX(), getY() + this.getBbHeight() * 0.5D, getZ(),
                        30, 1.4D, 1.0D, 1.4D, 0.4D);
            }
            return InteractionResult.sidedSuccess(false);
        }

        // ---- Sneak + empty hand strips equipment back off
        if (stack.isEmpty() && player.isShiftKeyDown() && (this.isSaddled() || this.isArmored())) {
            if (this.isArmored()) {
                this.setArmored(false);
                this.spawnAtLocation(ModItems.VOID_ARMOR.get());
            }
            if (this.isSaddled()) {
                this.setSaddled(false);
                this.spawnAtLocation(ModItems.VOID_SADDLE.get());
            }
            applyArmorBonus();
            this.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE.value(), 1.0F, 0.9F);
            return InteractionResult.sidedSuccess(false);
        }

        // ---- Empty hand: toggle sit / stand
        if (stack.isEmpty() && !this.isSaddled()) {
            boolean sit = !this.isInSittingPose();
            this.setOrderedToSit(sit);
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            this.playSound(sit ? SoundEvents.ENDER_DRAGON_AMBIENT : SoundEvents.ENDER_DRAGON_GROWL, 0.7F, 1.3F);
            return InteractionResult.sidedSuccess(false);
        }

        // ---- Mount
        if (this.isSaddled() && this.isAdultDragon() && !this.isVehicle() && !this.isInSittingPose()) {
            this.setOrderedToSit(false);
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
            this.addBond(1);
            return InteractionResult.sidedSuccess(false);
        }

        if (this.isSaddled() && !this.isAdultDragon()) {
            player.displayClientMessage(Component.translatable("message.voiddragon.too_young")
                    .withStyle(ChatFormatting.GRAY), true);
            return InteractionResult.sidedSuccess(false);
        }

        return InteractionResult.PASS;
    }

    private void onGrewUp(Player player) {
        if (!(this.level() instanceof ServerLevel sl)) {
            return;
        }
        this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 3.0F, 0.7F);
        sl.sendParticles(ParticleTypes.EXPLOSION, getX(), getY() + this.getBbHeight() * 0.5D, getZ(),
                1, 0, 0, 0, 0);
        sl.sendParticles(VOID_SPARK, getX(), getY() + this.getBbHeight() * 0.5D, getZ(),
                80, 1.6D, 1.2D, 1.6D, 0.1D);
        sl.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY() + this.getBbHeight() * 0.5D, getZ(),
                60, 1.2D, 1.0D, 1.2D, 0.4D);
        if (getGrowth() >= GROWTH_ADULT && player instanceof net.minecraft.server.level.ServerPlayer sp) {
            ModTriggers.DRAGON_GROWN.get().trigger(sp);
        }
        player.displayClientMessage(Component.translatable("message.voiddragon.grew",
                Component.translatable(getGrowth() == GROWTH_JUVENILE ? "voiddragon.stage.juvenile" : "voiddragon.stage.adult"))
                .withStyle(ChatFormatting.LIGHT_PURPLE), false);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    public boolean canMate(Animal other) {
        return false;
    }

    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob other) {
        return null;
    }

    @Override
    public boolean canBeLeashed() {
        return isTame();
    }

    // ---------------------------------------------------------------- riding

    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof Player player && this.isTame() && this.isOwnedBy(player)) {
            return player;
        }
        return null;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().isEmpty();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void setTarget(LivingEntity target) {
        // Never attack the owner, their allies or other pets of the owner.
        if (target instanceof Player player && this.isTame() && this.isOwnedBy(player)) {
            return;
        }
        if (target != null && this.getOwner() != null) {
            LivingEntity owner = this.getOwner();
            if (target == owner || target.isAlliedTo(owner)) {
                return;
            }
            if (target instanceof TamableAnimal tamed && tamed.isTame() && owner.getUUID().equals(tamed.getOwnerUUID())) {
                return;
            }
        }
        // While ridden, the rider's brain replaces the dragon's own AI targeting.
        if (this.isVehicle() && target != null) {
            super.setTarget(null);
            return;
        }
        super.setTarget(target);
    }

    // ---------------------------------------------------------------- movement

    @Override
    public void travel(Vec3 travelVector) {
        if (this.getControllingPassenger() instanceof Player rider && this.isControlledByLocalInstance()) {
            this.setYRot(rider.getYRot());
            this.yRotO = this.getYRot();
            float pitchLimit = 55.0F;
            this.setXRot(Mth.clamp(rider.getXRot() * 0.7F, -pitchLimit, pitchLimit));
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yBodyRotO = this.getYRot();
            this.yHeadRot = this.getYRot();
            this.yHeadRotO = this.getYRot();

            float strafe = rider.xxa * 0.6F;
            float forward = rider.zza;
            if (forward <= 0.0F) {
                forward *= 0.3F;
            }

            if (this.isFlying()) {
                Vec3 look = rider.getLookAngle();
                double yawRad = Math.toRadians(this.getYRot());
                Vec3 left = new Vec3(Math.cos(yawRad), 0.0D, Math.sin(yawRad));
                boolean sprinting = rider.isSprinting();
                double accel = sprinting ? 0.085D : 0.06D;
                double maxSpeed = sprinting ? 3.1D : 2.3D;

                Vec3 motion = this.getDeltaMovement().scale(0.90D);
                motion = motion.add(look.scale(forward * accel)).add(left.scale(strafe * accel));
                // Shift dives, jump climbs: vertical authority on top of the look vector.
                if (rider.isSprinting() && !rider.onGround() && rider.getDeltaMovement().y > 0.0D) {
                    motion = motion.add(0.0D, 0.07D, 0.0D);
                } else if (rider.isShiftKeyDown()) {
                    motion = motion.add(0.0D, -0.09D, 0.0D);
                }
                if (forward == 0.0F && strafe == 0.0F) {
                    // Hover: gently bob in place instead of falling out of the sky.
                    motion = motion.scale(0.90D).add(0.0D, Math.sin(this.tickCount * 0.12D) * 0.004D, 0.0D);
                }
                if (motion.lengthSqr() > maxSpeed * maxSpeed) {
                    motion = motion.normalize().scale(maxSpeed);
                }
                this.setDeltaMovement(motion);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.horizontalCollision || this.verticalCollision) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.55D));
                }
                this.fallDistance = 0.0F;
            } else {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (rider.isSprinting() ? 1.45F : 1.0F));
                super.travel(new Vec3(strafe, travelVector.y, forward));
                Vec3 dm = this.getDeltaMovement();
                if (dm.y < -0.4D) { // dragons glide down, they do not plummet
                    this.setDeltaMovement(dm.x, -0.4D, dm.z);
                }
            }
            return;
        }
        super.travel(travelVector);
    }

    // ---------------------------------------------------------------- abilities (server side)

    /** Called by the network handler when the rider presses an ability key. */
    public void handleAbility(ServerPlayer rider, int ability) {
        if (this.getControllingPassenger() != rider) {
            return;
        }
        switch (ability) {
            case 0 -> requestBreath();
            case 1 -> startRush(rider);
            case 2 -> toggleFlight(rider);
            case 3 -> roar(rider);
            case 4 -> voidStep(rider);
            default -> {
            }
        }
    }

    public void requestBreath() {
        if (!this.breathLocked && this.energy > 0.0F) {
            this.breathHold = 3;
        }
    }

    private int scaleCooldown(int ticks) {
        return Math.max(1, (int) Math.round(ticks * VoidDragonConfig.INSTANCE.abilityCooldownScale.get()));
    }

    private void toggleFlight(ServerPlayer rider) {
        if (!VoidDragonConfig.INSTANCE.allowFlight.get()) {
            rider.displayClientMessage(Component.translatable("message.voiddragon.flight_disabled")
                    .withStyle(ChatFormatting.GRAY), true);
            return;
        }
        if (!hasBond(BOND_FLIGHT)) {
            rider.displayClientMessage(Component.translatable("message.voiddragon.need_bond",
                    BOND_FLIGHT).withStyle(ChatFormatting.GRAY), true);
            return;
        }
        if (this.isFlying()) {
            this.setFlying(false);
            this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 1.6F, 0.8F);
            return;
        }
        this.setFlying(true);
        this.flyTicks = 0;
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.75D, 0.0D));
        this.hurtMarked = true;
        this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 2.5F, 0.8F);
        if (this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY() + 0.3D, getZ(), 60, 1.8D, 0.2D, 1.8D, 0.35D);
            sl.sendParticles(ParticleTypes.SQUID_INK, getX(), getY() + 0.3D, getZ(), 25, 1.8D, 0.2D, 1.8D, 0.2D);
        }
    }

    private void startRush(ServerPlayer rider) {
        if (this.dashCooldown > 0 || !(this.level() instanceof ServerLevel sl)) {
            return;
        }
        if (!hasBond(BOND_RUSH)) {
            rider.displayClientMessage(Component.translatable("message.voiddragon.need_bond", BOND_RUSH)
                    .withStyle(ChatFormatting.GRAY), true);
            return;
        }
        Vec3 look = rider.getLookAngle();
        Vec3 dir;
        double power;
        if (this.isFlying()) {
            dir = look;
            power = 1.7D;
        } else {
            dir = new Vec3(look.x, look.y * 0.4D + 0.14D, look.z).normalize();
            power = 3.0D;
        }
        this.setDeltaMovement(dir.scale(power));
        this.hurtMarked = true;
        this.dashTicks = 16;
        this.dashCooldown = scaleCooldown(140);
        this.dashHit.clear();

        sl.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.NEUTRAL, 2.0F, 0.6F);
        sl.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 2.0F, 0.5F);
        Vec3 c = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
        sl.sendParticles(ParticleTypes.REVERSE_PORTAL, c.x, c.y, c.z, 90, 1.2D, 0.9D, 1.2D, 0.5D);
        sl.sendParticles(SHADOW_DUST, c.x, c.y, c.z, 40, 1.3D, 1.0D, 1.3D, 0.0D);
    }

    private void rushTick(ServerLevel sl) {
        Vec3 c = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
        sl.sendParticles(SHADOW_DUST, c.x, c.y, c.z, 14, 1.0D, 0.8D, 1.0D, 0.0D);
        sl.sendParticles(ParticleTypes.PORTAL, c.x, c.y, c.z, 14, 1.1D, 0.9D, 1.1D, 0.9D);
        sl.sendParticles(ParticleTypes.SQUID_INK, c.x, c.y, c.z, 4, 0.9D, 0.7D, 0.9D, 0.04D);

        LivingEntity rider = this.getControllingPassenger();
        LivingEntity attacker = rider != null ? rider : this;
        Vec3 dm = this.getDeltaMovement();
        float damage = 10.0F + getGrowth() * 3.0F;
        for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.6D), this::canHit)) {
            if (this.dashHit.add(e.getId())) {
                e.invulnerableTime = 0;
                e.hurt(sl.damageSources().indirectMagic(this, attacker), damage);
                e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 160, 0));
                e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                e.setDeltaMovement(e.getDeltaMovement().add(dm.x * 0.6D, 0.45D, dm.z * 0.6D));
                e.hurtMarked = true;
                sl.sendParticles(ParticleTypes.SONIC_BOOM, e.getX(), e.getY() + e.getBbHeight() * 0.5D, e.getZ(), 1, 0, 0, 0, 0);
            }
        }
    }

    private void roar(ServerPlayer rider) {
        if (this.roarCooldown > 0 || !(this.level() instanceof ServerLevel sl)) {
            return;
        }
        if (!hasBond(BOND_ROAR)) {
            rider.displayClientMessage(Component.translatable("message.voiddragon.need_bond", BOND_ROAR)
                    .withStyle(ChatFormatting.GRAY), true);
            return;
        }
        this.roarCooldown = scaleCooldown(400);
        Vec3 c = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
        LivingEntity attacker = rider;
        double radius = 14.0D + getGrowth() * 2.0D;

        sl.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 6.0F, 0.5F);
        sl.playSound(null, getX(), getY(), getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 3.0F, 0.6F);

        for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(radius), this::canHit)) {
            if (e instanceof Player && !VoidDragonConfig.INSTANCE.roarAffectsPlayers.get()) {
                continue;
            }
            Vec3 pull = c.subtract(e.position());
            double dist = pull.length();
            if (dist > 0.1D) {
                pull = pull.normalize().scale(Math.min(1.4D, 0.25D + dist * 0.08D));
                e.setDeltaMovement(e.getDeltaMovement().add(pull.x, 0.4D, pull.z));
                e.hurtMarked = true;
            }
            e.invulnerableTime = 0;
            e.hurt(sl.damageSources().indirectMagic(this, attacker), 12.0F);
            e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 220, 0));
            e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 220, 1));
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 1));
        }

        // Void collapse visuals: portal particles start on a wide ring and fall into the dragon.
        for (int ring = 0; ring < 3; ring++) {
            int count = 56;
            double height = 0.4D + ring * 0.8D;
            for (int i = 0; i < count; i++) {
                double a = i * (Math.PI * 2.0D / count) + ring * 0.35D;
                sl.sendParticles(ParticleTypes.PORTAL, c.x, getY() + height, c.z, 0,
                        Math.cos(a), 0.0D, Math.sin(a), 7.0D + ring * 2.5D);
            }
        }
        sl.sendParticles(ParticleTypes.REVERSE_PORTAL, c.x, c.y, c.z, 140, 0.6D, 0.6D, 0.6D, 0.9D);
        sl.sendParticles(SHADOW_DUST, c.x, c.y, c.z, 70, 3.0D, 1.2D, 3.0D, 0.0D);
        sl.sendParticles(ParticleTypes.SONIC_BOOM, c.x, c.y, c.z, 1, 0, 0, 0, 0);
    }

    /** Blink forward along the rider's line of sight, through anything, into a safe spot. */
    private void voidStep(ServerPlayer rider) {
        if (this.portalCooldown > 0 || !(this.level() instanceof ServerLevel sl)) {
            return;
        }
        if (!hasBond(BOND_PORTAL)) {
            rider.displayClientMessage(Component.translatable("message.voiddragon.need_bond", BOND_PORTAL)
                    .withStyle(ChatFormatting.GRAY), true);
            return;
        }
        Vec3 dir = rider.getLookAngle();
        Vec3 from = this.position();
        Vec3 dest = null;
        for (double d = 44.0D; d >= 5.0D; d -= 1.5D) {
            Vec3 probe = from.add(dir.scale(d));
            Vec3 found = findSafeSpot(sl, probe);
            if (found != null) {
                dest = found;
                break;
            }
        }
        if (dest == null) {
            rider.displayClientMessage(Component.translatable("message.voiddragon.portal_failed")
                    .withStyle(ChatFormatting.GRAY), true);
            this.portalCooldown = 20;
            return;
        }

        this.portalCooldown = scaleCooldown(600);
        Vec3 origin = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
        sl.sendParticles(ParticleTypes.REVERSE_PORTAL, origin.x, origin.y, origin.z, 120, 1.0D, 1.2D, 1.0D, 0.7D);
        sl.sendParticles(SHADOW_DUST, origin.x, origin.y, origin.z, 60, 1.2D, 1.2D, 1.2D, 0.0D);
        sl.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 2.5F, 0.6F);

        List<Entity> riders = new ArrayList<>(this.getPassengers());
        this.ejectPassengers();
        this.teleportTo(dest.x, dest.y, dest.z);
        for (Entity passenger : riders) {
            passenger.teleportTo(dest.x, dest.y + this.getBbHeight() * 0.65D, dest.z);
            passenger.startRiding(this, true);
        }
        this.setDeltaMovement(Vec3.ZERO);
        this.hurtMarked = true;

        Vec3 arrival = dest.add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
        sl.sendParticles(ParticleTypes.REVERSE_PORTAL, arrival.x, arrival.y, arrival.z, 120, 1.0D, 1.2D, 1.0D, 0.7D);
        sl.sendParticles(ParticleTypes.SONIC_BOOM, arrival.x, arrival.y, arrival.z, 1, 0, 0, 0, 0);
        sl.playSound(null, dest.x, dest.y, dest.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 2.5F, 1.1F);
    }

    /**
     * Moves the dragon to a free spot near {@code target} (used by the Void Shard recall).
     * Returns false when nothing nearby is free.
     */
    public boolean teleportNear(ServerLevel level, Vec3 target) {
        this.ejectPassengers();
        this.setFlying(false);
        Vec3 spot = findSafeSpot(level, target);
        if (spot == null) {
            return false;
        }
        Vec3 origin = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
        this.teleportTo(spot.x, spot.y, spot.z);
        this.setDeltaMovement(Vec3.ZERO);
        this.hurtMarked = true;
        this.getNavigation().stop();
        this.setOrderedToSit(true);
        level.sendParticles(ParticleTypes.REVERSE_PORTAL, origin.x, origin.y, origin.z, 60, 1.0D, 1.0D, 1.0D, 0.4D);
        level.sendParticles(ParticleTypes.REVERSE_PORTAL, spot.x, spot.y + this.getBbHeight() * 0.5D, spot.z,
                60, 1.0D, 1.0D, 1.0D, 0.4D);
        level.playSound(null, spot.x, spot.y, spot.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL, 1.6F, 0.8F);
        return true;
    }

    /** Finds a spot near {@code probe} where the dragon fits; null when there is none. */
    private Vec3 findSafeSpot(ServerLevel sl, Vec3 probe) {
        double height = this.getBbHeight();
        for (double dy = 2.0D; dy >= -6.0D; dy -= 1.0D) {
            Vec3 candidate = probe.add(0.0D, dy, 0.0D);
            AABB box = new AABB(candidate.x - this.getBbWidth() * 0.5D, candidate.y,
                    candidate.z - this.getBbWidth() * 0.5D, candidate.x + this.getBbWidth() * 0.5D,
                    candidate.y + height, candidate.z + this.getBbWidth() * 0.5D);
            if (sl.noCollision(this, box)) {
                BlockPos below = BlockPos.containing(candidate.x, candidate.y - 0.2D, candidate.z);
                if (!sl.getBlockState(below).isAir() || this.isFlying()) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private boolean canHit(LivingEntity e) {
        if (e == this || !e.isAlive() || this.hasPassenger(e)) {
            return false;
        }
        if (e instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
        }
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            if (e == owner || e.isAlliedTo(owner)) {
                return false;
            }
            if (e instanceof TamableAnimal tamed && tamed.isTame() && owner.getUUID().equals(tamed.getOwnerUUID())) {
                return false;
            }
        }
        return true;
    }

    private void fireBreath(ServerLevel sl) {
        LivingEntity rider = this.getControllingPassenger();
        LivingEntity attacker = rider != null ? rider : this;
        Vec3 origin = this.getMouthPos();
        Vec3 dir;
        if (rider != null) {
            dir = rider.getLookAngle();
        } else if (this.getTarget() != null) {
            LivingEntity t = this.getTarget();
            dir = t.position().add(0.0D, t.getBbHeight() * 0.6D, 0.0D).subtract(origin).normalize();
        } else {
            dir = this.calculateViewVector(this.getXRot(), this.getYHeadRot());
        }

        double range = 17.0D + getGrowth() * 4.0D;
        Vec3 end = origin.add(dir.scale(range));
        BlockHitResult hit = sl.clip(new ClipContext(origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        boolean hitBlock = hit.getType() != HitResult.Type.MISS;
        double len = hitBlock ? Math.sqrt(hit.getLocation().distanceToSqr(origin)) : range;

        // The beam: a widening column of violet-black smoke with portal sparks.
        float thickness = 0.7F + getGrowth() * 0.25F;
        for (double d = 0.8D; d < len; d += 0.8D) {
            Vec3 p = origin.add(dir.scale(d));
            double spread = (0.08D + d * 0.028D) * thickness;
            sl.sendParticles(SHADOW_DUST, p.x, p.y, p.z, 2, spread, spread, spread, 0.0D);
            if (this.random.nextInt(3) == 0) {
                sl.sendParticles(ParticleTypes.PORTAL, p.x, p.y, p.z, 2, spread, spread, spread, 0.3D);
            }
            if (this.random.nextInt(6) == 0) {
                sl.sendParticles(ParticleTypes.SQUID_INK, p.x, p.y, p.z, 1, spread, spread, spread, 0.03D);
            }
            if (VoidDragonConfig.INSTANCE.voidBreathBreaksBlocks.get() && this.random.nextInt(4) == 0) {
                BlockPos pos = BlockPos.containing(p);
                BlockState state = sl.getBlockState(pos);
                if (isFragile(state)) {
                    sl.destroyBlock(pos, false, this);
                }
            }
        }

        Vec3 tip = origin.add(dir.scale(len));
        if (hitBlock) {
            sl.sendParticles(ParticleTypes.PORTAL, tip.x, tip.y, tip.z, 10, 0.6D, 0.6D, 0.6D, 0.8D);
            sl.sendParticles(ParticleTypes.SQUID_INK, tip.x, tip.y, tip.z, 4, 0.5D, 0.5D, 0.5D, 0.08D);
            sl.sendParticles(SHADOW_DUST, tip.x, tip.y, tip.z, 6, 0.6D, 0.6D, 0.6D, 0.0D);
            if (this.tickCount % 8 == 0) {
                sl.playSound(null, tip.x, tip.y, tip.z, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 0.8F, 0.5F);
            }
        }

        if (this.tickCount % 4 == 0) {
            AABB box = new AABB(origin, tip).inflate(1.6D);
            for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, box, this::canHit)) {
                Optional<Vec3> hitPos = e.getBoundingBox().inflate(0.5D).clip(origin, tip);
                if (hitPos.isPresent()) {
                    e.invulnerableTime = 0;
                    e.hurt(sl.damageSources().indirectMagic(this, attacker), 3.0F + getGrowth());
                    e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 120, 0));
                    e.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
                }
            }
        }
    }

    private static boolean isFragile(BlockState state) {
        return state.is(BlockTags.LEAVES) || state.is(BlockTags.CROPS) || state.is(BlockTags.FLOWERS)
                || state.is(BlockTags.REPLACEABLE_BY_TREES) || state.is(BlockTags.REPLACEABLE)
                || state.is(Blocks.GLASS) || state.is(Blocks.GLASS_PANE) || state.is(Blocks.COBWEB)
                || state.is(Blocks.SNOW) || state.is(Blocks.FIRE) || state.is(Blocks.SOUL_FIRE)
                || state.is(Blocks.SHORT_GRASS) || state.is(Blocks.TALL_GRASS) || state.is(Blocks.VINE);
    }

    // ---------------------------------------------------------------- tick

    @Override
    public void tick() {
        super.tick();

        this.flyProgressO = this.flyProgress;
        this.flyProgress = Mth.clamp(this.flyProgress + (this.isFlying() ? 0.07F : -0.07F), 0.0F, 1.0F);
        this.mouthO = this.mouth;
        this.mouth = Mth.clamp(this.mouth + (this.isBreathing() ? 0.2F : -0.12F), 0.0F, 1.0F);
        this.flapSpeed = this.isFlying() ? (this.getControllingPassenger() != null && this.getControllingPassenger().isSprinting() ? 1.7F : 1.0F) : 1.0F;

        if (this.level().isClientSide) {
            clientTick();
        } else if (this.level() instanceof ServerLevel sl) {
            serverTick(sl);
        }
    }

    private void serverTick(ServerLevel sl) {
        // Entity attributes only exist after the attribute event fired, so grow-driven
        // stats are applied lazily on the first server tick.
        if (!this.growthStatsApplied) {
            this.growthStatsApplied = true;
            applyGrowthAttributes();
        }
        if (this.breathCooldown > 0) this.breathCooldown--;
        if (this.dashCooldown > 0) this.dashCooldown--;
        if (this.roarCooldown > 0) this.roarCooldown--;
        if (this.portalCooldown > 0) this.portalCooldown--;
        if (this.followTeleportCooldown > 0) this.followTeleportCooldown--;

        LivingEntity rider = this.getControllingPassenger();

        // ---- Dismount with sneak on the ground (in the air, sneak means "dive").
        if (rider instanceof Player player && player.isShiftKeyDown() && !this.isFlying() && this.onGround()) {
            player.stopRiding();
        }

        // ---- Flight bookkeeping
        if (this.isFlying()) {
            this.flyTicks++;
            if (!this.isVehicle() || this.onGround() && this.flyTicks > 30) {
                this.setFlying(false);
            }
            if (this.flyTicks % 12 == 0) {
                this.playSound(SoundEvents.ENDER_DRAGON_FLAP, 1.2F, 0.8F + this.random.nextFloat() * 0.2F);
            }
        } else {
            this.flyTicks = 0;
        }

        // ---- Void Breath channel
        if (this.breathHold > 0 && this.energy > 0.0F) {
            if (!this.isBreathing()) {
                this.setBreathing(true);
                this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 3.0F, 0.6F);
            }
            this.breathHold--;
            this.energy -= 1.2F;
            fireBreath(sl);
            if (this.energy <= 0.0F) {
                this.energy = 0.0F;
                this.breathHold = 0;
                this.breathLocked = true;
            }
        } else {
            if (this.isBreathing()) {
                this.setBreathing(false);
            }
            float regen = this.getBond() >= 100 ? 0.55F : 0.35F;
            this.energy = Math.min(MAX_ENERGY, this.energy + regen);
            if (this.breathLocked && this.energy >= 30.0F) {
                this.breathLocked = false;
            }
        }

        if (this.dashTicks > 0) {
            this.dashTicks--;
            rushTick(sl);
        }

        // ---- Shadow Veil: the dragon mends itself in darkness
        if (this.tickCount % 20 == 0 && this.getHealth() < this.getMaxHealth()
                && sl.getMaxLocalRawBrightness(this.blockPosition()) <= 4) {
            this.heal(2.0F + getGrowth());
            sl.sendParticles(ParticleTypes.SCULK_SOUL, getX(), getY() + this.getBbHeight() * 0.6D, getZ(),
                    3, 0.8D, 0.8D, 0.8D, 0.02D);
        }

        // ---- Bond grows while you ride, and slowly while it follows you
        if (isTame() && ++this.bondTimer >= 600) {
            this.bondTimer = 0;
            if (rider != null) {
                addBond(2);
            } else if (!this.isInSittingPose()) {
                addBond(1);
            }
            if (!isAdultDragon() && VoidDragonConfig.INSTANCE.naturalGrowthSeconds.get() > 0) {
                addGrowth(1);
            }
        }

        // ---- Rider perks
        if (rider != null) {
            if (this.tickCount % 40 == 0) {
                rider.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 320, 0, true, false, false));
            }
            if (this.tickCount % 4 == 0) {
                syncVitals();
            }
        }

        syncVitals();

        // ---- Loyalty: teleport to the owner when left far behind
        if (isTame() && !this.isVehicle() && !this.isInSittingPose()
                && this.getOwner() instanceof ServerPlayer owner && !owner.isSpectator()) {
            double dist = this.distanceToSqr(owner);
            if (dist > 48.0D * 48.0D && this.followTeleportCooldown == 0) {
                this.followTeleportCooldown = 100;
                Vec3 safe = findSafeSpot(sl, owner.position());
                if (safe != null) {
                    sl.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY() + 1.0D, getZ(), 40, 1.0D, 1.0D, 1.0D, 0.4D);
                    this.teleportTo(safe.x, safe.y, safe.z);
                    sl.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY() + 1.0D, getZ(), 40, 1.0D, 1.0D, 1.0D, 0.4D);
                    this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.4F, 0.7F);
                }
            }
        }

        // ---- Ambient void growl while idle
        if (--this.idleSoundTimer <= 0) {
            this.idleSoundTimer = 300 + this.random.nextInt(400);
            if (this.getRandom().nextFloat() < 0.35F && !this.isSleeping()) {
                this.playSound(SoundEvents.ENDER_DRAGON_AMBIENT, 0.6F, 0.7F + this.random.nextFloat() * 0.3F);
            }
        }
    }

    private void clientTick() {
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        double fx = -Mth.sin(yaw);
        double fz = Mth.cos(yaw);
        double sx = Math.cos(yaw);
        double sz = Math.sin(yaw);
        float scale = getScale();

        // Void aura: portal motes drifting around the whole body.
        for (int i = 0; i < 2; i++) {
            this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.9D * scale), this.getRandomY(),
                    this.getRandomZ(0.9D * scale),
                    (this.random.nextDouble() - 0.5D) * 0.6D, (this.random.nextDouble() - 0.5D) * 0.6D,
                    (this.random.nextDouble() - 0.5D) * 0.6D);
        }

        // Shadow tail trail.
        if (this.tickCount % 2 == 0) {
            double tx = this.getX() - fx * 2.8D * scale + (this.random.nextDouble() - 0.5D) * 0.4D;
            double tz = this.getZ() - fz * 2.8D * scale + (this.random.nextDouble() - 0.5D) * 0.4D;
            this.level().addParticle(SHADOW_DUST, tx, this.getY() + 0.8D * scale + this.random.nextDouble() * 0.5D,
                    tz, 0.0D, 0.01D, 0.0D);
            if (this.tickCount % 6 == 0) {
                this.level().addParticle(ParticleTypes.SQUID_INK, tx, this.getY() + 0.9D * scale, tz, 0.0D, 0.02D, 0.0D);
            }
        }

        // Wing-tip ribbons while airborne.
        float fly = this.getFlyProgress(0.0F);
        if (fly > 0.2F) {
            double flapY = Math.sin(this.tickCount * 0.33D) * 1.3D * scale;
            for (int side = -1; side <= 1; side += 2) {
                double wx = this.getX() + sx * side * 3.4D * scale - fx * 0.6D;
                double wz = this.getZ() + sz * side * 3.4D * scale - fz * 0.6D;
                double wy = this.getY() + 1.8D * scale + flapY;
                this.level().addParticle(SHADOW_DUST, wx, wy, wz, 0.0D, -0.02D, 0.0D);
                if (this.tickCount % 3 == 0) {
                    this.level().addParticle(ParticleTypes.REVERSE_PORTAL, wx, wy, wz, 0.0D, -0.03D, 0.0D);
                }
            }
        }

        // Charging maw while breathing.
        if (this.isBreathing()) {
            Vec3 m = this.getMouthPos();
            Vec3 look = this.calculateViewVector(this.getXRot(), this.getYHeadRot());
            for (int i = 0; i < 3; i++) {
                this.level().addParticle(SHADOW_DUST, m.x, m.y, m.z,
                        look.x * 0.3D + (this.random.nextDouble() - 0.5D) * 0.1D,
                        look.y * 0.3D + (this.random.nextDouble() - 0.5D) * 0.1D,
                        look.z * 0.3D + (this.random.nextDouble() - 0.5D) * 0.1D);
            }
        }

        // Gentle smoke from the nostrils of a content dragon.
        if (this.isInSittingPose() && this.tickCount % 14 == 0) {
            Vec3 m = this.getMouthPos();
            this.level().addParticle(ParticleTypes.SMOKE, m.x, m.y, m.z, 0.0D, 0.01D, 0.0D);
        }
    }

    // ---------------------------------------------------------------- damage / death / misc

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() == this.getControllingPassenger()) {
            return false;
        }
        // Void armour soaks a share of incoming damage.
        if (isArmored() && !source.is(DamageTypes.GENERIC_KILL)) {
            amount *= 0.75F;
        }
        boolean result = super.hurt(source, amount);
        if (result && this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.SQUID_INK, getX(), getY() + 1.4D, getZ(), 10, 0.9D, 0.8D, 0.9D, 0.08D);
            sl.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 1.4D, getZ(), 12, 0.9D, 0.8D, 0.9D, 0.6D);
            // A hurt dragon fights back — and grows a little stronger for it.
            if (source.getEntity() instanceof LivingEntity attacker && canHit(attacker)) {
                this.setTarget(attacker);
            }
        }
        return result;
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean hit = super.doHurtTarget(target);
        if (hit) {
            this.addBond(2);
            if (!isAdultDragon()) {
                addGrowth(1);
            }
            if (target instanceof LivingEntity living) {
                living.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 100, 0));
            }
        }
        return hit;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.WITHER) || source.is(DamageTypes.FALL) || source.is(DamageTypes.IN_WALL)
                || source.is(DamageTypes.DRAGON_BREATH) || super.isInvulnerableTo(source);
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        if (effect.getEffect() == MobEffects.WITHER || effect.getEffect() == MobEffects.DARKNESS) {
            return false;
        }
        return super.canBeAffected(effect);
    }

    /** Void Walker: falling out of the world just throws the dragon back up. */
    @Override
    protected void onBelowWorld() {
        if (this.level().isClientSide) {
            return;
        }
        this.setPos(this.getX(), this.level().getMinBuildHeight() + 6.0D, this.getZ());
        this.setDeltaMovement(0.0D, 0.8D, 0.0D);
        this.hurtMarked = true;
        this.fallDistance = 0.0F;
        if (this.isVehicle()) {
            this.setFlying(true);
        }
    }

    @Override
    public void die(DamageSource source) {
        if (!this.level().isClientSide) {
            if (this.isSaddled()) {
                this.spawnAtLocation(ModItems.VOID_SADDLE.get());
            }
            if (this.isArmored()) {
                this.spawnAtLocation(ModItems.VOID_ARMOR.get());
            }
        }
        super.die(source);
    }

    @Override
    protected void tickDeath() {
        if (this.level() instanceof ServerLevel sl) {
            if (this.deathTime % 2 == 0) {
                sl.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 1.4D, getZ(), 0,
                        this.random.nextGaussian(), this.random.nextGaussian() * 0.5D, this.random.nextGaussian(), 4.0D);
                sl.sendParticles(ParticleTypes.SQUID_INK, getX(), getY() + 1.4D, getZ(), 6, 1.2D, 0.8D, 1.2D, 0.05D);
            }
            if (this.deathTime == 19) {
                sl.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY() + 1.4D, getZ(), 220, 0.5D, 0.5D, 0.5D, 1.0D);
                sl.sendParticles(SHADOW_DUST, getX(), getY() + 1.4D, getZ(), 120, 2.5D, 1.4D, 2.5D, 0.0D);
                sl.sendParticles(ParticleTypes.SONIC_BOOM, getX(), getY() + 1.4D, getZ(), 1, 0, 0, 0, 0);
            }
        }
        super.tickDeath();
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(5.0D * getScale(), 2.0D, 5.0D * getScale());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    /** Tamed dragons must not fill up the mob cap of the chunk they live in. */
    @Override
    public boolean isPersistenceRequired() {
        return this.isTame() || super.isPersistenceRequired();
    }

    // ---------------------------------------------------------------- sounds

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAdultDragon() ? SoundEvents.ENDER_DRAGON_AMBIENT : SoundEvents.ENDER_DRAGON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENDER_DRAGON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENDER_DRAGON_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 1.4F + getGrowth() * 0.4F;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * (isAdultDragon() ? 0.8F : 1.35F);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.7F + 0.2F * getGrowth(), 0.6F);
    }

    // ---------------------------------------------------------------- AI: ranged Void Breath

    static class VoidBreathGoal extends Goal {
        private final VoidDragonEntity dragon;
        private int time;

        VoidBreathGoal(VoidDragonEntity dragon) {
            this.dragon = dragon;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = dragon.getTarget();
            if (target == null || !target.isAlive() || dragon.isVehicle() || dragon.breathCooldown > 0
                    || dragon.breathLocked || dragon.energy < 60.0F) {
                return false;
            }
            if (!(target instanceof Enemy) && !(target instanceof Player)) {
                return false;
            }
            double dist = dragon.distanceToSqr(target);
            return dist > 36.0D && dist < 20.0D * 20.0D && dragon.getSensing().hasLineOfSight(target);
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = dragon.getTarget();
            return time > 0 && target != null && target.isAlive() && !dragon.breathLocked && !dragon.isVehicle();
        }

        @Override
        public void start() {
            time = 40 + dragon.random.nextInt(30);
        }

        @Override
        public void tick() {
            LivingEntity target = dragon.getTarget();
            if (target != null) {
                dragon.getLookControl().setLookAt(target, 30.0F, 30.0F);
            }
            dragon.requestBreath();
            time--;
        }

        @Override
        public void stop() {
            dragon.breathHold = 0;
            dragon.breathCooldown = 80 + dragon.random.nextInt(60);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}
