package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.eightsidedsquare.euphorics.core.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ LookAtEntityGoal.class })
public abstract class LookAtEntityGoalMixin
{
    @Shadow
    @Nullable
    protected Entity target;
    @Shadow
    @Final
    protected MobEntity mob;

    @Inject(method = { "canStart" }, at = { @At("RETURN") }, cancellable = true)
    private void doNotLookAtAstralPlanePlayers(final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this.target) && !this.mob.getType().equals(EuphoricsEntities.SHADE)) {
            cir.setReturnValue(false);
        }
    }
}
