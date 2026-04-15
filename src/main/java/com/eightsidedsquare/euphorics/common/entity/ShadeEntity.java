package com.eightsidedsquare.euphorics.common.entity;

import java.util.*;
import net.fabricmc.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import software.bernie.geckolib3.util.*;
import com.eightsidedsquare.euphorics.core.*;
import com.eightsidedsquare.euphorics.cca.*;
import software.bernie.geckolib3.core.manager.*;
import software.bernie.geckolib3.core.controller.*;
import software.bernie.geckolib3.core.event.predicate.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.core.builder.*;
import java.util.function.*;
import net.minecraft.*;

public class ShadeEntity extends HostileEntity implements IAnimatable
{
    protected static final UUID ATTACK_BOOST_ID;
    protected static final EntityAttributeModifier ATTACK_BOOST;
    private static final TargetPredicate TARGET_PREDICATE;
    private static final TrackedData<Boolean> IN_ASTRAL_PLANE;
    final AnimationFactory factory;
    @Environment(EnvType.CLIENT)
    public boolean glitching;
    private int attackTicks;
    private int despawnTime;

    public ShadeEntity(final EntityType<? extends HostileEntity> entityType, final World world) {
        super(entityType, world);
        this.factory = GeckoLibUtil.createFactory(this);
        this.glitching = false;
        this.attackTicks = 0;
        this.despawnTime = 1200;
        this.moveControl = new FlightMoveControl(this, 20, true);
    }

    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this) {
            @Override
            public boolean canStart() {
                return super.canStart() && this.mob.getTarget() == null;
            }

            @Override
            public boolean shouldContinue() {
                return super.shouldContinue() && this.mob.getTarget() == null;
            }

            @Override
            public void tick() {
                if (this.mob.getTarget() != null) {
                    this.stop();
                    return;
                }
                super.tick();
            }
        });
        this.goalSelector.add(2, new ShadeAttackGoal(this));
        this.goalSelector.add(3, new LookAtPlayerGoal(this));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 0.8));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.targetSelector.add(1, new TrackPlayersGoal(this));
    }

    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(ShadeEntity.IN_ASTRAL_PLANE, false);
    }

    public float getPathfindingFavor(final BlockPos pos, final WorldView world) {
        return world.getBlockState(pos).isAir() ? 10.0f : 0.0f;
    }

    protected EntityNavigation createNavigation(final World world) {
        return new BirdNavigation(this, world) {
            public boolean isValidPosition(final BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }
        };
    }

    public boolean isInAstralPlane() {
        return this.dataTracker.get(ShadeEntity.IN_ASTRAL_PLANE);
    }

    public void setInAstralPlane(final boolean inAstralPlane) {
        this.dataTracker.set(ShadeEntity.IN_ASTRAL_PLANE, inAstralPlane);
    }

    public void writeCustomDataToNbt(final NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("in_astral_plane", this.isInAstralPlane());
    }

    public void readCustomDataFromNbt(final NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setInAstralPlane(nbt.getBoolean("in_astral_plane"));
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6000000238418579).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6000000238418579).add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0);
    }

    public boolean collides() {
        return !this.isInAstralPlane() && super.collides();
    }

    public void tick() {
        super.tick();
        if (this.attackTicks > 0) {
            --this.attackTicks;
        }
        if (this.attackTicks <= 0) {
            this.setAttacking(false);
        }
        final World world = this.world;
        if (world instanceof ServerWorld serverWorld) {
            if (this.random.nextFloat() < 0.01f) {
                serverWorld.spawnParticles((ParticleEffect)EuphoricsParticles.ASTRAL_PLANE, this.getX(), this.getY() + this.getHeight() / 2.0f, this.getZ(), 1, 0.125, 0.325, 0.125, 0.025);
            }
        }
        if (!this.world.isClient && this.isInAstralPlane() && this.getTarget() != null && this.random.nextFloat() < 0.05f) {
            this.teleportTo(this.getTarget());
        }
        final EntityAttributeInstance instance = this.getAttributeInstance(EntityAttributes.GENERIC_FLYING_SPEED);
        if (this.getTarget() != null) {
            this.despawnTime = 1200;
            if (instance != null && !instance.hasModifier(ShadeEntity.ATTACK_BOOST)) {
                instance.addTemporaryModifier(ShadeEntity.ATTACK_BOOST);
            }
        }
        else {
            --this.despawnTime;
            if (this.despawnTime <= 0) {
                this.discard();
            }
            if (instance != null) {
                instance.removeModifier(ShadeEntity.ATTACK_BOOST);
            }
        }
    }

    public void toggleAstralPlane() {
        final World world = this.world;
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles((ParticleEffect)EuphoricsParticles.ASTRAL_PLANE, this.getX(), this.getY() + this.getHeight() / 2.0f, this.getZ(), 100, 0.125, 0.325, 0.125, 0.025);
        }
        this.playSound(EuphoricsSounds.ENTITY_SHADE_TELEPORT, 1.0f, 1.5f);
        this.setInAstralPlane(!this.isInAstralPlane());
    }

    public boolean tryAttack(final Entity target) {
        if (this.isInAstralPlane() && EuphoriaComponent.isOutOfAstralPlane(target)) {
            return false;
        }
        final boolean attacked = super.tryAttack(target);
        if (attacked) {
            this.playSound(EuphoricsSounds.ENTITY_SHADE_ATTACK, this.getSoundVolume(), this.getSoundPitch());
            if (target instanceof PlayerEntity player) {
                final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.getNullable(player);
                if (component == null) {
                    return true;
                }
                component.setForcedAstralPlaneTicks(Math.min(300, component.getForcedAstralPlaneTicks() + 100));
                EuphoricsEntityComponents.EUPHORIA.sync(player);
            }
        }
        return attacked;
    }

    public boolean damage(final DamageSource source, final float amount) {
        final boolean damaged = super.damage(source, amount);
        if (damaged) {
            final Entity getAttacker = source.getAttacker();
            if (getAttacker instanceof PlayerEntity player) {
                final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.getNullable(player);
                if (component == null) {
                    return true;
                }
                component.setShadeAggro(Math.min(20000, component.getShadeAggro() + 2000));
                EuphoricsEntityComponents.EUPHORIA.sync(player);
            }
        }
        return super.damage(source, amount);
    }

    public boolean isPushable() {
        return !this.isInAstralPlane();
    }

    protected void pushAway(final Entity entity) {
        if (!this.isInAstralPlane()) {
            super.pushAway(entity);
        }
    }

    public Text getDisplayName() {
        return Team.decorateName(this.getScoreboardTeam(), this.getName()).styled(style -> style.withHoverEvent(this.getHoverEvent()).withInsertion(this.getUuidAsString())).formatted(Formatting.OBFUSCATED);
    }

    public boolean handleFallDamage(final float fallDistance, final float damageMultiplier, final DamageSource damageSource) {
        return false;
    }

    public boolean canBreatheInWater() {
        return true;
    }

    protected void fall(final double heightDifference, final boolean onGround, final BlockState state, final BlockPos landedPosition) {
    }

    protected boolean canStartRiding(final Entity entity) {
        return false;
    }

    void teleportTo(final Entity entity) {
        this.teleportTo(entity.getX() + (this.random.nextDouble() - 0.5) * 32.0, entity.getY() + (this.random.nextDouble() - 0.5) * 32.0, entity.getZ() + (this.random.nextDouble() - 0.5) * 32.0);
    }

    protected void teleportTo(final double x, final double y, final double z) {
        final BlockPos pos = new BlockPos(x, y, z);
        if (this.world.getBlockState(pos).getMaterial().blocksMovement() && this.world.getBlockState(pos.up()).getMaterial().blocksMovement()) {
            return;
        }
        this.teleport(x, y, z, false);
    }

    public void registerControllers(final AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller", 3.0f, this::animationController));
    }

    private PlayState animationController(final AnimationEvent<ShadeEntity> event) {
        if (this.handSwinging) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shade.attack", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shade.moving", ILoopType.EDefaultLoopTypes.LOOP));
        }
        else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.shade.idle", ILoopType.EDefaultLoopTypes.LOOP));
        }
        return PlayState.CONTINUE;
    }

    protected SoundEvent getHurtSound(final DamageSource source) {
        return EuphoricsSounds.ENTITY_SHADE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return EuphoricsSounds.ENTITY_SHADE_DEATH;
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }

    private static boolean shouldAttack(final LivingEntity entity) {
        if (entity instanceof PlayerEntity player) {
            if (!player.isSpectator() && !player.getAbilities().creativeMode) {
                final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.getNullable(player);
                return component != null && component.getShadeAggro() >= 6000;
            }
        }
        return false;
    }

    static {
        ATTACK_BOOST_ID = UUID.fromString("CB3F55D4-645C-4F48-A497-9C13A34DB5CF");
        ATTACK_BOOST = new EntityAttributeModifier(ShadeEntity.ATTACK_BOOST_ID, "Attack Boost", 0.30000001192092896, EntityAttributeModifier.Operation.ADDITION);
        TARGET_PREDICATE = TargetPredicate.createNonAttackable().ignoreVisibility().ignoreDistanceScalingFactor().setPredicate(ShadeEntity::shouldAttack);
        IN_ASTRAL_PLANE = DataTracker.registerData(ShadeEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    }

    static class TrackPlayersGoal extends TrackTargetGoal
    {
        private PlayerEntity target;
        private final ShadeEntity mob;

        public TrackPlayersGoal(final ShadeEntity mob) {
            super(mob, false);
            this.mob = mob;
        }

        public void start() {
            this.mob.setTarget(this.target);
            super.start();
        }

        public boolean canStart() {
            final PlayerEntity getClosestPlayer = this.mob.world.getClosestPlayer(ShadeEntity.TARGET_PREDICATE, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ());
            this.target = getClosestPlayer;
            return getClosestPlayer != null;
        }
    }

    static class ShadeAttackGoal extends MeleeAttackGoal
    {
        private final ShadeEntity mob;

        public ShadeAttackGoal(final ShadeEntity mob) {
            super(mob, 1.0, false);
            this.mob = mob;
        }

        public boolean canStart() {
            final LivingEntity target = this.mob.getTarget();
            if (target instanceof PlayerEntity player) {
                final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.getNullable(player);
                return component != null && component.getShadeAggro() >= 12000;
            }
            return super.canStart();
        }

        public void tick() {
            super.tick();
            final double distance = (this.mob.getTarget() == null) ? 0.0 : this.mob.squaredDistanceTo(this.mob.getTarget());
            if (!this.mob.world.isClient && !this.mob.isInAstralPlane() && this.mob.getTarget() != null && this.mob.random.nextFloat() < 0.025f && distance >= 576.0) {
                this.mob.toggleAstralPlane();
            }
            if (!this.mob.world.isClient && this.mob.isInAstralPlane() && this.mob.getTarget() != null && distance < 576.0 && distance > 64.0) {
                this.mob.toggleAstralPlane();
            }
        }
    }

    static class LookAtPlayerGoal extends LookAtEntityGoal
    {
        private final ShadeEntity mob;

        public LookAtPlayerGoal(final ShadeEntity mob) {
            super(mob, PlayerEntity.class, 96.0f, 0.1f);
            this.mob = mob;
        }

        public boolean canStart() {
            if (this.mob.isInAstralPlane() && this.mob.getTarget() != null && this.mob.squaredDistanceTo(this.mob.getTarget()) < 64.0) {
                return false;
            }
            if (this.mob.getTarget() != null) {
                this.target = this.mob.getTarget();
                return true;
            }
            return false;
        }

        public void start() {
            super.start();
            if (this.mob.isInAstralPlane()) {
                this.mob.toggleAstralPlane();
            }
        }

        public boolean shouldContinue() {
            return super.shouldContinue() && !this.mob.isInAstralPlane();
        }

        public void tick() {
            super.tick();
            if (!this.mob.isInAstralPlane() && this.mob.getTarget() != null && this.mob.squaredDistanceTo(this.mob.getTarget()) < 64.0) {
                this.mob.toggleAstralPlane();
                this.mob.teleportTo(this.mob.getTarget());
            }
        }
    }
}
