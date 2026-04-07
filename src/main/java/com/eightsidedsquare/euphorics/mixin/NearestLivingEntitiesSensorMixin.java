package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.LivingTargetCache;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.NearestLivingEntitiesSensor;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.*;
import java.util.*;
import com.llamalad7.mixinextras.sugar.*;
import com.eightsidedsquare.euphorics.cca.*;
import java.util.function.*;
import net.minecraft.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ NearestLivingEntitiesSensor.class })
public abstract class NearestLivingEntitiesSensorMixin
{
    @WrapOperation(method = { "sense" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/brain/Brain;remember(Lnet/minecraft/entity/ai/brain/MemoryModuleType;Ljava/lang/Object;)V", ordinal = 1) })
    private void doNotSenseAstralPlanePlayers(final Brain<?> brain, final MemoryModuleType<?> memoryModuleType, final Object cache, final Operation<Void> original, final ServerWorld world, final LivingEntity owner, @Local final List<LivingEntity> list) {
        original.call(brain, memoryModuleType, cache);
    }
}
