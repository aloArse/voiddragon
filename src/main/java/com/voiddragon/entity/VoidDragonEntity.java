package com.voiddragon.entity;

import com.voiddragon.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustColorTransitionOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToSitGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The Void Dragon: a tameable, rideable shadow dragon.
 *
 * Abilities (rider keys): Void Breath (hold), Void Rush (dash), Flight toggle, Void Roar (AoE collapse).
 * Passives: Shadow Veil (regenerates in darkness, gives the rider Night Vision),
 * immune to the Void, fire, wither and fall damage.
 */
public class VoidDragonEntity extends TamableAnimal {

    private static final EntityDataAccessor<Boolean> DATA_FLYING =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_BREATHING =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SADDLED =
            SynchedEntityData.defineId(VoidDragonEntity.class, EntityDataSerializers.BOOLEAN);

    /** Violet that decays into black: the signature "shadow" particle. */
    private static final DustColorTransitionOptions SHADOW_DUST = new DustColorTransitionOptions(
            new Vector3f(0.50F, 0.08F, 0.90F), new Vector3f(0.02F, 0.0F, 0.07F), 1.7F);

    public static final float MAX_ENERGY = 100.0F;

    // ---- server state ----
    private float energy = MAX_ENERGY;
    private boolean breathLocked;
    private int breathHold;
    private int breathCooldown;
    private int dashCooldown;
    private int roarCooldown;
    private int dashTicks;
    private int flyTicks;
    private final Set<Integer> dashHit = new HashSet<>();

    // ---- client/animation state ----
    private float flyProgress;
    private float flyProgressO;
    private float mouth;
    private float mouthO;

    public VoidDragonEntity(EntityType<? extends VoidDragonEntity> type, Level level) {
        super(type, level);
        this.xpReward = 120;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 180.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.30D)
                .add(Attributes.ATTACK_DAMAGE, 14.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.5D)
                .add(Attributes.FOLLOW_RANGE, 40.0D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.9D)
                .add(Attributes.STEP_HEIGHT, 1.6D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToSitGoal(this));
        this.goalSelector.addGoal(2, new VoidBreathGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.2D, 10.0F, 3.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Monster.class, 10, true, false,
                entity -> this.isTame()));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_FLYING, false);
        builder.define(DATA_BREATHING, false);
        builder.define(DATA_SADDLED, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddled", isSaddled());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setSaddled(tag.getBoolean("Saddled"));
    }

    // ------------------------------------------------------------------ state accessors

    public boolean isFlying() {
        return this.entityData.get(DATA_FLYING);
    }

    public void setFlying(boolean flying) {
        this.entityData.set(DATA_FLYING, flying);
        this.setNoGravity(flying);
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

    public float getFlyProgress(float partialTick) {
        return Mth.lerp(partialTick, flyProgressO, flyProgress);
    }

    public float getMouthProgress(float partialTick) {
        return Mth.lerp(partialTick, mouthO, mouth);
    }

    public Vec3 getMouthPos() {
        Vec3 look = this.calculateViewVector(this.getXRot(), this.getYHeadRot());
        return this.position().add(0.0D, this.getBbHeight() * 0.82D, 0.0D).add(look.scale(2.6D));
    }

    // ------------------------------------------------------------------ interaction

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!this.isTame()) {
            if (stack.is(ModItems.VOID_SHARD.get())) {
                if (!this.level().isClientSide) {
                    stack.consume(1, player);
                    if (this.random.nextInt(4) == 0) {
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.setOrderedToSit(true);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            return InteractionResult.PASS;
        }

        if (!this.isOwnedBy(player)) {
            return InteractionResult.PASS;
        }

        if (stack.is(ModItems.VOID_SHARD.get()) && this.getHealth() < this.getMaxHealth()) {
            if (!this.level().isClientSide) {
                stack.consume(1, player);
                this.heal(40.0F);
                this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 1.0F, 1.4F);
                this.level().broadcastEntityEvent(this, (byte) 7);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (stack.is(Items.SADDLE) && !this.isSaddled()) {
            if (!this.level().isClientSide) {
                stack.consume(1, player);
                this.setSaddled(true);
                this.playSound(SoundEvents.HORSE_SADDLE, 1.0F, 0.7F);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        if (this.isSaddled() && !this.isVehicle()) {
            if (!this.level().isClientSide) {
                this.setOrderedToSit(false);
                player.setYRot(this.getYRot());
                player.setXRot(this.getXRot());
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof Player player && this.isTame() && this.isOwnedBy(player)) {
            return player;
        }
        return null;
    }

    @Override
    public void setTarget(LivingEntity target) {
        // While a player rides, the rider is the brain.
        if (this.isVehicle() && target != null) {
            super.setTarget(null);
            return;
        }
        super.setTarget(target);
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

    // ------------------------------------------------------------------ rider-controlled movement (client authoritative)

    @Override
    public void travel(Vec3 travelVector) {
        if (this.getControllingPassenger() instanceof Player rider && this.isControlledByLocalInstance()) {
            this.setYRot(rider.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(Mth.clamp(rider.getXRot() * 0.6F, -45.0F, 45.0F));
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = this.getYRot();
            this.yHeadRot = this.getYRot();

            float strafe = rider.xxa * 0.5F;
            float forward = rider.zza;
            if (forward <= 0.0F) {
                forward *= 0.25F;
            }

            if (this.isFlying()) {
                Vec3 look = rider.getLookAngle();
                double yawRad = Math.toRadians(this.getYRot());
                Vec3 left = new Vec3(Math.cos(yawRad), 0.0D, Math.sin(yawRad));
                double accel = 0.055D;

                Vec3 motion = this.getDeltaMovement().scale(0.91D);
                motion = motion.add(look.scale(forward * accel)).add(left.scale(strafe * accel));
                if (forward == 0.0F && strafe == 0.0F) {
                    motion = motion.scale(0.93D).add(0.0D, Math.sin(this.tickCount * 0.15D) * 0.004D, 0.0D);
                }
                if (motion.lengthSqr() > 2.5D * 2.5D) {
                    motion = motion.normalize().scale(2.5D);
                }
                this.setDeltaMovement(motion);
                this.move(MoverType.SELF, this.getDeltaMovement());
                if (this.horizontalCollision || this.verticalCollision) {
                    this.setDeltaMovement(this.getDeltaMovement().scale(0.6D));
                }
            } else {
                this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
                super.travel(new Vec3(strafe, travelVector.y, forward));
                Vec3 dm = this.getDeltaMovement();
                if (dm.y < -0.35D) { // dragons glide, they do not plummet
                    this.setDeltaMovement(dm.x, -0.35D, dm.z);
                }
            }
            return;
        }
        super.travel(travelVector);
    }

    // ------------------------------------------------------------------ abilities (server side)

    /** Called by the network handler when the rider presses an ability key. */
    public void handleAbility(ServerPlayer rider, int ability) {
        if (this.getControllingPassenger() != rider) {
            return;
        }
        switch (ability) {
            case 0 -> requestBreath();
            case 1 -> startRush(rider);
            case 2 -> toggleFlight();
            case 3 -> roar(rider);
            default -> {
            }
        }
    }

    public void requestBreath() {
        if (!this.breathLocked && this.energy > 0.0F) {
            this.breathHold = 3;
        }
    }

    private void toggleFlight() {
        if (this.isFlying()) {
            this.setFlying(false);
            return;
        }
        this.setFlying(true);
        this.flyTicks = 0;
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.6D, 0.0D));
        this.hurtMarked = true;
        this.playSound(SoundEvents.ENDER_DRAGON_GROWL, 2.5F, 0.8F);
        if (this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.REVERSE_PORTAL, getX(), getY() + 0.3D, getZ(), 60, 1.6D, 0.2D, 1.6D, 0.35D);
            sl.sendParticles(ParticleTypes.SQUID_INK, getX(), getY() + 0.3D, getZ(), 25, 1.6D, 0.2D, 1.6D, 0.2D);
        }
    }

    private void startRush(Player rider) {
        if (this.dashCooldown > 0 || !(this.level() instanceof ServerLevel sl)) {
            return;
        }
        Vec3 look = rider.getLookAngle();
        Vec3 dir;
        double power;
        if (this.isFlying()) {
            dir = look;
            power = 1.4D;
        } else {
            dir = new Vec3(look.x, 0.12D, look.z).normalize();
            power = 3.2D;
        }
        this.setDeltaMovement(dir.scale(power));
        this.hurtMarked = true;
        this.dashTicks = 14;
        this.dashCooldown = 120;
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
        for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.3D), this::canHit)) {
            if (this.dashHit.add(e.getId())) {
                e.invulnerableTime = 0;
                e.hurt(sl.damageSources().indirectMagic(this, attacker), 12.0F);
                e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 160, 0));
                e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1));
                e.setDeltaMovement(e.getDeltaMovement().add(dm.x * 0.6D, 0.4D, dm.z * 0.6D));
                e.hurtMarked = true;
                sl.sendParticles(ParticleTypes.SONIC_BOOM, e.getX(), e.getY() + e.getBbHeight() * 0.5D, e.getZ(), 1, 0, 0, 0, 0);
            }
        }
    }

    private void roar(LivingEntity rider) {
        if (this.roarCooldown > 0 || !(this.level() instanceof ServerLevel sl)) {
            return;
        }
        this.roarCooldown = 360;
        Vec3 c = this.position().add(0.0D, this.getBbHeight() * 0.5D, 0.0D);
        LivingEntity attacker = rider != null ? rider : this;

        sl.playSound(null, getX(), getY(), getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 6.0F, 0.5F);
        sl.playSound(null, getX(), getY(), getZ(), SoundEvents.WARDEN_SONIC_BOOM, SoundSource.HOSTILE, 3.0F, 0.6F);

        for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(14.0D), this::canHit)) {
            Vec3 pull = c.subtract(e.position());
            double dist = pull.length();
            if (dist > 0.1D) {
                pull = pull.normalize().scale(Math.min(1.3D, 0.25D + dist * 0.08D));
                e.setDeltaMovement(e.getDeltaMovement().add(pull.x, 0.35D, pull.z));
                e.hurtMarked = true;
            }
            e.invulnerableTime = 0;
            e.hurt(sl.damageSources().indirectMagic(this, attacker), 10.0F);
            e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 200, 0));
            e.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
            e.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
        }

        // Void collapse visuals: portal particles start on a wide ring and fall into the dragon.
        for (int ring = 0; ring < 3; ring++) {
            int count = 48;
            double height = 0.4D + ring * 0.7D;
            for (int i = 0; i < count; i++) {
                double a = i * (Math.PI * 2.0D / count) + ring * 0.35D;
                sl.sendParticles(ParticleTypes.PORTAL, c.x, getY() + height, c.z, 0,
                        Math.cos(a), 0.0D, Math.sin(a), 6.0D + ring * 2.5D);
            }
        }
        sl.sendParticles(ParticleTypes.REVERSE_PORTAL, c.x, c.y, c.z, 140, 0.6D, 0.6D, 0.6D, 0.9D);
        sl.sendParticles(SHADOW_DUST, c.x, c.y, c.z, 70, 3.0D, 1.2D, 3.0D, 0.0D);
        sl.sendParticles(ParticleTypes.SONIC_BOOM, c.x, c.y, c.z, 1, 0, 0, 0, 0);
    }

    private boolean canHit(LivingEntity e) {
        if (e == this || !e.isAlive() || this.hasPassenger(e)) {
            return false;
        }
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            if (e == owner || e.isAlliedTo(owner)) {
                return false;
            }
            if (e instanceof TamableAnimal tamed && tamed.isOwnedBy(owner)) {
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

        double range = 20.0D;
        Vec3 end = origin.add(dir.scale(range));
        BlockHitResult hit = sl.clip(new ClipContext(origin, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        boolean hitBlock = hit.getType() != HitResult.Type.MISS;
        double len = hitBlock ? hit.getLocation().distanceTo(origin) : range;

        // The beam: a widening column of violet-black smoke with portal sparks.
        for (double d = 0.8D; d < len; d += 0.8D) {
            Vec3 p = origin.add(dir.scale(d));
            double spread = 0.08D + d * 0.028D;
            sl.sendParticles(SHADOW_DUST, p.x, p.y, p.z, 2, spread, spread, spread, 0.0D);
            if (this.random.nextInt(3) == 0) {
                sl.sendParticles(ParticleTypes.PORTAL, p.x, p.y, p.z, 2, spread, spread, spread, 0.3D);
            }
            if (this.random.nextInt(6) == 0) {
                sl.sendParticles(ParticleTypes.SQUID_INK, p.x, p.y, p.z, 1, spread, spread, spread, 0.03D);
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
            AABB box = new AABB(origin, tip).inflate(1.5D);
            for (LivingEntity e : sl.getEntitiesOfClass(LivingEntity.class, box, this::canHit)) {
                Optional<Vec3> hitPos = e.getBoundingBox().inflate(0.5D).clip(origin, tip);
                if (hitPos.isPresent()) {
                    e.invulnerableTime = 0;
                    e.hurt(sl.damageSources().indirectMagic(this, attacker), 3.0F);
                    e.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 120, 0));
                    e.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 0));
                }
            }
        }
    }

    // ------------------------------------------------------------------ tick

    @Override
    public void tick() {
        super.tick();

        this.flyProgressO = this.flyProgress;
        this.flyProgress = Mth.clamp(this.flyProgress + (this.isFlying() ? 0.08F : -0.08F), 0.0F, 1.0F);
        this.mouthO = this.mouth;
        this.mouth = Mth.clamp(this.mouth + (this.isBreathing() ? 0.2F : -0.12F), 0.0F, 1.0F);

        if (this.level().isClientSide) {
            clientTick();
        } else if (this.level() instanceof ServerLevel sl) {
            serverTick(sl);
        }
    }

    private void serverTick(ServerLevel sl) {
        if (this.breathCooldown > 0) this.breathCooldown--;
        if (this.dashCooldown > 0) this.dashCooldown--;
        if (this.roarCooldown > 0) this.roarCooldown--;

        LivingEntity rider = this.getControllingPassenger();

        // Flight bookkeeping.
        if (this.isFlying()) {
            this.flyTicks++;
            if (!this.isVehicle() || (this.onGround() && this.flyTicks > 15)) {
                this.setFlying(false);
            }
        } else {
            this.flyTicks = 0;
        }

        // Void Breath channel.
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
            this.energy = Math.min(MAX_ENERGY, this.energy + 0.35F);
            if (this.breathLocked && this.energy >= 30.0F) {
                this.breathLocked = false;
            }
        }

        if (this.dashTicks > 0) {
            this.dashTicks--;
            rushTick(sl);
        }

        // Shadow Veil: the dragon mends itself in darkness.
        if (this.tickCount % 20 == 0 && this.getHealth() < this.getMaxHealth()
                && sl.getMaxLocalRawBrightness(this.blockPosition()) <= 4) {
            this.heal(2.0F);
            sl.sendParticles(ParticleTypes.SCULK_SOUL, getX(), getY() + 1.5D, getZ(), 3, 0.8D, 0.8D, 0.8D, 0.02D);
        }

        if (rider != null) {
            if (this.tickCount % 40 == 0) {
                rider.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 320, 0, true, false, false));
            }
            if (this.tickCount % 5 == 0 && rider instanceof ServerPlayer sp) {
                sp.displayClientMessage(buildStatus(), true);
            }
        }
    }

    private MutableComponent buildStatus() {
        int filled = Mth.clamp(Math.round(this.energy / 10.0F), 0, 10);
        MutableComponent bar = Component.literal("Void ").withStyle(ChatFormatting.DARK_PURPLE)
                .append(Component.literal("|".repeat(filled)).withStyle(this.breathLocked ? ChatFormatting.RED : ChatFormatting.LIGHT_PURPLE))
                .append(Component.literal("|".repeat(10 - filled)).withStyle(ChatFormatting.DARK_GRAY));
        bar.append(Component.literal("   Rush ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(this.dashCooldown == 0 ? "ready" : (this.dashCooldown / 20 + 1) + "s")
                        .withStyle(this.dashCooldown == 0 ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_GRAY));
        bar.append(Component.literal("   Roar ").withStyle(ChatFormatting.GRAY))
                .append(Component.literal(this.roarCooldown == 0 ? "ready" : (this.roarCooldown / 20 + 1) + "s")
                        .withStyle(this.roarCooldown == 0 ? ChatFormatting.LIGHT_PURPLE : ChatFormatting.DARK_GRAY));
        return bar;
    }

    private void clientTick() {
        float yaw = this.yBodyRot * Mth.DEG_TO_RAD;
        double fx = -Mth.sin(yaw);
        double fz = Mth.cos(yaw);
        double sx = Mth.cos(yaw);
        double sz = Mth.sin(yaw);

        // Void aura: portal motes drifting around the whole body.
        for (int i = 0; i < 2; i++) {
            this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.9D), this.getRandomY(), this.getRandomZ(0.9D),
                    (this.random.nextDouble() - 0.5D) * 0.6D, (this.random.nextDouble() - 0.5D) * 0.6D,
                    (this.random.nextDouble() - 0.5D) * 0.6D);
        }

        // Shadow tail trail.
        if (this.tickCount % 2 == 0) {
            double tx = this.getX() - fx * 2.8D + (this.random.nextDouble() - 0.5D) * 0.4D;
            double tz = this.getZ() - fz * 2.8D + (this.random.nextDouble() - 0.5D) * 0.4D;
            this.level().addParticle(SHADOW_DUST, tx, this.getY() + 0.8D + this.random.nextDouble() * 0.5D, tz, 0.0D, 0.01D, 0.0D);
            if (this.tickCount % 6 == 0) {
                this.level().addParticle(ParticleTypes.SQUID_INK, tx, this.getY() + 0.9D, tz, 0.0D, 0.02D, 0.0D);
            }
        }

        // Wing-tip ribbons while airborne.
        float fly = this.getFlyProgress(0.0F);
        if (fly > 0.2F) {
            double flapY = Math.sin(this.tickCount * 0.33D) * 1.3D;
            for (int side = -1; side <= 1; side += 2) {
                double wx = this.getX() + sx * side * 3.4D - fx * 0.6D;
                double wz = this.getZ() + sz * side * 3.4D - fz * 0.6D;
                double wy = this.getY() + 1.8D + flapY;
                this.level().addParticle(SHADOW_DUST, wx, wy, wz, 0.0D, -0.02D, 0.0D);
                if (this.tickCount % 3 == 0) {
                    this.level().addParticle(ParticleTypes.REVERSE_PORTAL, wx, wy, wz, 0.0D, -0.03D, 0.0D);
                }
            }
            if (this.tickCount % 22 == 0) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), SoundEvents.ENDER_DRAGON_FLAP,
                        this.getSoundSource(), 1.6F, 0.7F + this.random.nextFloat() * 0.2F, false);
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
    }

    // ------------------------------------------------------------------ damage / death / misc

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() != null && source.getEntity() == this.getControllingPassenger()) {
            return false;
        }
        boolean result = super.hurt(source, amount);
        if (result && this.level() instanceof ServerLevel sl) {
            sl.sendParticles(ParticleTypes.SQUID_INK, getX(), getY() + 1.4D, getZ(), 10, 0.9D, 0.8D, 0.9D, 0.08D);
            sl.sendParticles(ParticleTypes.PORTAL, getX(), getY() + 1.4D, getZ(), 12, 0.9D, 0.8D, 0.9D, 0.6D);
        }
        return result;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return source.is(DamageTypes.WITHER) || source.is(DamageTypes.FALL) || source.is(DamageTypes.IN_WALL)
                || super.isInvulnerableTo(source);
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
        if (!this.level().isClientSide && this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
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
        return this.getBoundingBox().inflate(4.5D, 2.0D, 4.5D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDER_DRAGON_AMBIENT;
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
    public int getAmbientSoundInterval() {
        return 200;
    }

    @Override
    protected float getSoundVolume() {
        return 2.0F;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.RAVAGER_STEP, 0.7F, 0.6F);
    }

    // ------------------------------------------------------------------ AI: ranged Void Breath

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
            double dist = dragon.distanceToSqr(target);
            return dist > 36.0D && dist < 18.0D * 18.0D && dragon.getSensing().hasLineOfSight(target);
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
