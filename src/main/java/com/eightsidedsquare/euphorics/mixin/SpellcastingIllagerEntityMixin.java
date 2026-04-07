package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ SpellcastingIllagerEntity.class })
public abstract class SpellcastingIllagerEntityMixin
{
    @Mixin({ SpellcastingIllagerEntity.CastSpellGoal.class })
    abstract static class CastSpellGoalMixin
    {
        @WrapOperation(method = { "canStart" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z") })
        private boolean considerAstralPlanePlayersDead(final LivingEntity entity, final Operation<Boolean> original) {
            return original.call(new Object[] { entity }) && EuphoriaComponent.isOutOfAstralPlane(entity);
        }

        @WrapOperation(method = { "shouldContinue" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isAlive()Z") })
        private boolean considerAstralPlanePlayersDeadAgain(final LivingEntity entity, final Operation<Boolean> original) {
            return original.call(new Object[] { entity }) && EuphoriaComponent.isOutOfAstralPlane(entity);
        }
    }

    @Mixin({ SpellcastingIllagerEntity.LookAtTargetGoal.class })
    abstract static class LookAtTargetGoalMixin
    {
        @WrapOperation(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/SpellcastingIllagerEntity;getTarget()Lnet/minecraft/entity/LivingEntity;", ordinal = 0) })
        private LivingEntity noTargetIfTargetIsInAstralPlane(final SpellcastingIllagerEntity entity, final Operation<LivingEntity> original) {
            final LivingEntity target = original.call(new Object[] { entity });
            return EuphoriaComponent.isOutOfAstralPlane(target) ? target : null;
        }
    }
}
