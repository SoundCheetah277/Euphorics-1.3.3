package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.mob.HostileEntity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ CrossbowAttackGoal.class })
public abstract class CrossbowAttackGoalMixin
{
    @Shadow
    @Final
    private HostileEntity actor;

    @Inject(method = { "hasAliveTarget" }, at = { @At("RETURN") }, cancellable = true)
    private void considerAstralPlanePlayersToBeDeadDespiteThemBeingVeryAliveAndWell(final CallbackInfoReturnable<Boolean> cir) {
        if ((boolean)cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane((Entity)this.actor.getTarget())) {
            cir.setReturnValue(false);
        }
    }
}
