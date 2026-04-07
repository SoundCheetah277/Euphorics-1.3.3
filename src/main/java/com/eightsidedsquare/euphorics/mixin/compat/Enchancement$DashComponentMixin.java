package com.eightsidedsquare.euphorics.mixin.compat;

import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.v2.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;

@Mixin({ DashComponent.class })
public class Enchancement$DashComponentMixin
{
    @Shadow
    @Final
    private PlayerEntity obj;

    @WrapWithCondition(method = { "clientTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V") })
    private boolean euphorics$cancelDashParticles(final World instance, final ParticleEffect parameters, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }

    @WrapWithCondition(method = { "handle" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V") })
    private static boolean euphorics$cancelDashSound(final PlayerEntity instance, final SoundEvent sound, final float volume, final float pitch) {
        return EuphoriaComponent.isOutOfAstralPlane(instance);
    }
}
