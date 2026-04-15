package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.brain.sensor.NearestPlayersSensor;
import org.spongepowered.asm.mixin.*;
import java.util.stream.*;
import java.util.function.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ NearestPlayersSensor.class })
public abstract class NearestPlayersSensorMixin
{
    @WrapOperation(method = { "sense" }, at = { @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;", ordinal = 2) })
    private Stream<?> filterOutAstralPlanePlayers(final Stream<?> stream, final Predicate<?> predicate, final Operation<Stream<?>> original) {
        return (original.call(stream, predicate)).filter(obj -> EuphoriaComponent.isOutOfAstralPlane((Entity)obj));
    }
}
