package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.core.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ TargetPredicate.class })
public abstract class TargetPredicateMixin
{
    @Inject(method = { "test" }, at = { @At("RETURN") }, cancellable = true)
    private void doNotTargetAstralPlanePlayers(final LivingEntity baseEntity, final LivingEntity targetEntity, final CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && baseEntity != null && !baseEntity.getType().equals(EuphoricsEntities.SHADE) && !EuphoriaComponent.isOutOfAstralPlane(targetEntity)) {
            cir.setReturnValue(false);
        }
    }
}
