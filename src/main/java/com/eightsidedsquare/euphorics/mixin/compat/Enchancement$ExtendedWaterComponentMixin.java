package com.eightsidedsquare.euphorics.mixin.compat;

//import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.v2.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;

//@Mixin({ ExtendedWaterComponent.class })
public class Enchancement$ExtendedWaterComponentMixin
{
    @Shadow
    @Final
    private LivingEntity obj;

    @WrapWithCondition(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V") })
    private boolean euphorics$cancelExtinguishSound(final LivingEntity instance, final SoundEvent soundEvent, final float volume, final float pitch) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }

    @WrapWithCondition(method = { "serverTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V") })
    private boolean euphorics$cancelWaterSound(final World instance, final PlayerEntity player, final BlockPos pos, final SoundEvent sound, final SoundCategory category, final float volume, final float pitch) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }

    @WrapWithCondition(method = { "clientTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V") })
    private boolean euphorics$cancelWaterParticles(final World instance, final ParticleEffect parameters, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }
}
