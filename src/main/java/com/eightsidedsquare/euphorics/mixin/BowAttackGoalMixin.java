package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ BowAttackGoal.class })
public abstract class BowAttackGoalMixin
{
    @Shadow
    @Final
    private HostileEntity actor;

    @Inject(method = { "shouldContinue" }, at = { @At("RETURN") }, cancellable = true)
    private void doNotSnipeTheAstralPlanePlayersPlease(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this.actor.getTarget())) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = { "canStart" }, at = { @At("RETURN") }, cancellable = true)
    private void doNotStartForAstralPlanePlayers(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this.actor.getTarget())) {
            cir.setReturnValue(false);
        }
    }
}
