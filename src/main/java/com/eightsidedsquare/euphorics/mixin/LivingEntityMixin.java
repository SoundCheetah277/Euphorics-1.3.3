package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.eightsidedsquare.euphorics.core.*;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin extends Entity
{
    @Shadow
    public abstract boolean isUsingItem();

    @Shadow
    protected abstract void spawnItemParticles(final ItemStack p0, final int p1);

    public LivingEntityMixin(final EntityType<?> type, final World world) {
        super(type, world);
    }

    @WrapWithCondition(method = { "fall" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I") })
    private boolean cancelParticlesForAstralPlanePlayers(final ServerWorld world, final ParticleEffect effect, final double x, final double y, final double z, final int count, final double deltaX, final double deltaY, final double deltaZ, final double speed) {
        return EuphoriaComponent.isOutOfAstralPlane(this);
    }

    @Inject(method = { "isPushable" }, at = { @At("RETURN") }, cancellable = true)
    private void notPushableInAstralPlane(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = { "pushAway" }, at = { @At("HEAD") }, cancellable = true)
    private void noPushingInAstralPlane(final Entity entity, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(this)) {
            ci.cancel();
        }
    }

    @Inject(method = { "spawnConsumptionEffects" }, at = { @At("TAIL") })
    private void spawnConsumptionParticlesForEndust(final ItemStack stack, final int particleCount, final CallbackInfo ci) {
        if (this.isUsingItem() && (stack.isOf(EuphoricsItems.RAW_ENDUST) || stack.isOf(EuphoricsItems.CRYSTALLIZED_ENDUST))) {
            this.spawnItemParticles(stack, particleCount);
        }
    }

    @WrapOperation(method = { "spawnItemParticles" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V") })
    private void spawnEndustConsumptionParticles(final World world, final ParticleEffect parameters, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ, final Operation<Void> original, final ItemStack stack, final int count) {
        original.call(world, stack.isOf(EuphoricsItems.RAW_ENDUST) ? EuphoricsParticles.RAW_ENDUST : (stack.isOf(EuphoricsItems.CRYSTALLIZED_ENDUST) ? EuphoricsParticles.ASTRAL_PLANE : parameters), x, y, z, velocityX, velocityY, velocityZ);
    }

    @Inject(method = { "getHandSwingDuration" }, at = { @At("HEAD") }, cancellable = true)
    private void moreHandSwingDurationForShades(final CallbackInfoReturnable<Integer> cir) {
        if (this.getType().equals(EuphoricsEntities.SHADE)) {
            cir.setReturnValue(10);
        }
    }

    @Inject(method = { "tickStatusEffects" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/DataTracker;get(Lnet/minecraft/entity/TrackedData;)Ljava/lang/Object;", ordinal = 0) }, cancellable = true)
    private void euphorics$hidePotionParticles(final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(this)) {
            ci.cancel();
        }
    }
}
