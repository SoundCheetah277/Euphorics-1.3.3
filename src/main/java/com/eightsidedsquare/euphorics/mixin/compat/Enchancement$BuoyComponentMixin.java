package com.eightsidedsquare.euphorics.mixin.compat;

import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import com.llamalad7.mixinextras.injector.v2.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ BuoyComponent.class })
public class Enchancement$BuoyComponentMixin
{
    @Shadow
    @Final
    private PlayerEntity obj;

    @WrapWithCondition(method = { "clientTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V") })
    private boolean euphorics$cancelBuoyParticles(final World instance, final ParticleEffect parameters, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }
}
