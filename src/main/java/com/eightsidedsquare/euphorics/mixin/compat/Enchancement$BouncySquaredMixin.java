package com.eightsidedsquare.euphorics.mixin.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.jetbrains.annotations.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.v2.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;

@Mixin(value = { LivingEntity.class }, priority = 1001)
public class Enchancement$BouncySquaredMixin
{
    @TargetHandler(mixin = "moriyashiine.enchancement.mixin.bouncy.LivingEntityMixin", name = "Lmoriyashiine/enchancement/mixin/bouncy/LivingEntityMixin;enchancement$bouncy(FFLnet/minecraft/entity/damage/DamageSource;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V")
    @WrapWithCondition(method = { "@MixinSquared:Handler" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSoundFromEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V") })
    private boolean euphorics$cancelLandSound(final World instance, @Nullable final PlayerEntity player, final Entity entity, final SoundEvent sound, final SoundCategory category, final float volume, final float pitch) {
        return EuphoriaComponent.isOutOfAstralPlane(entity);
    }

    @TargetHandler(mixin = "moriyashiine.enchancement.mixin.bouncy.LivingEntityMixin", name = "Lmoriyashiine/enchancement/mixin/bouncy/LivingEntityMixin;enchancement$bouncy(D)D")
    @WrapWithCondition(method = { "@MixinSquared:Handler" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;playSoundFromEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V") })
    private boolean euphorics$cancelBounceSound(final World instance, @Nullable final PlayerEntity player, final Entity entity, final SoundEvent sound, final SoundCategory category, final float volume, final float pitch) {
        return EuphoriaComponent.isOutOfAstralPlane(entity);
    }

    @TargetHandler(mixin = "moriyashiine.enchancement.mixin.bouncy.LivingEntityMixin", name = "Lmoriyashiine/enchancement/mixin/bouncy/LivingEntityMixin;enchancement$bouncy(D)D")
    @WrapWithCondition(method = { "@MixinSquared:Handler" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I") })
    private <T extends ParticleEffect> boolean euphorics$cancelBounceParticles(final ServerWorld instance, final T particle, final double x, final double y, final double z, final int count, final double deltaX, final double deltaY, final double deltaZ, final double speed) {
        return EuphoriaComponent.isOutOfAstralPlane(this);
    }
}
