package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ProjectileAttackGoal.class })
public abstract class ProjectileAttackGoalMixin
{
    @Shadow
    @Final
    private MobEntity mob;

    @Inject(method = { "shouldContinue" }, at = { @At("RETURN") }, cancellable = true)
    private void doNotContinueAttackingAstralPlanePlayers(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this.mob.getTarget())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = { "canStart" }, at = { @At("RETURN") }, cancellable = true)
    private void doNotStartAttackingAstralPlanePlayers(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this.mob.getTarget())) {
            cir.setReturnValue(false);
        }
    }
}
