package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.core.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ MeleeAttackGoal.class })
public abstract class MeleeAttackGoalMixin
{
    @Shadow
    @Final
    protected PathAwareEntity mob;

    @Inject(method = { "shouldContinue" }, at = { @At("RETURN") }, cancellable = true)
    private void doNotMeleeAttackAstralPlanePlayers(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this.mob.getTarget()) && !this.mob.getType().equals(EuphoricsEntities.SHADE)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = { "canStart" }, at = { @At("RETURN") }, cancellable = true)
    private void peaceAndLoveToMyAstralPlaneHomies(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this.mob.getTarget()) && !this.mob.getType().equals(EuphoricsEntities.SHADE)) {
            cir.setReturnValue(false);
        }
    }
}
