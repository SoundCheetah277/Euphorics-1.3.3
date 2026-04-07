package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import org.spongepowered.asm.mixin.injection.*;
import com.llamalad7.mixinextras.injector.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin({ Entity.class })
public abstract class EntityMixin
{
    @Inject(method = { "occludeVibrationSignals" }, at = { @At("HEAD") }, cancellable = true)
    private void occludeVibrationsIfInAstralPlane(final CallbackInfoReturnable<Boolean> cir) {
        if (!EuphoriaComponent.isOutOfAstralPlane(this)) {
            cir.setReturnValue(true);
        }
    }

    @WrapWithCondition(method = { "move" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onSteppedOn(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/Entity;)V") })
    private boolean noStepEffectsForTheAstralPlanePlayers(final Block block, final World world, final BlockPos pos, final BlockState state, final Entity entity) {
        return EuphoriaComponent.isOutOfAstralPlane(entity);
    }

    @WrapWithCondition(method = { "checkBlockCollision" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onEntityCollision(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)V") })
    private boolean noEntityCollisionForAstralPlanePlayers(final BlockState state, final World world, final BlockPos pos, final Entity entity) {
        return EuphoriaComponent.isOutOfAstralPlane(entity);
    }

    @Inject(method = { "shouldSpawnSprintingParticles" }, at = { @At("RETURN") }, cancellable = true)
    private void cancelSprintParticlesForAstralPlanePlayers(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this)) {
            cir.setReturnValue(false);
        }
    }

    @WrapWithCondition(method = { "fall" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/event/GameEvent$Emitter;)V") })
    private boolean cancelFallGameEventForAstralPlanePlayers(final World world, final GameEvent event, final Vec3d emitterPos, final GameEvent.Emitter emitter) {
        return EuphoriaComponent.isOutOfAstralPlane(this);
    }

    @WrapWithCondition(method = { "fall" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V") })
    private boolean cancelLandEffectsForAstralPlanePlayers(final Block block, final World world, final BlockState state, final BlockPos pos, final Entity entity, final float fallDistance) {
        return EuphoriaComponent.isOutOfAstralPlane(this);
    }

    @Inject(method = { "onSwimmingStart" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelSwimEffects(final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(this)) {
            ci.cancel();
        }
    }

    @Inject(method = { "playStepSound" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelStepSounds(final BlockPos pos, final BlockState state, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(this)) {
            ci.cancel();
        }
    }

    @Inject(method = { "playSwimSound" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelSwimSounds(final float volume, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(this)) {
            ci.cancel();
        }
    }
}
