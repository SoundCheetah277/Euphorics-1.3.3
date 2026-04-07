package com.eightsidedsquare.euphorics.mixin.compat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.*;
import moriyashiine.enchancement.common.component.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;
import com.llamalad7.mixinextras.injector.v2.*;

@Mixin({ GaleComponent.class })
public class Enchancement$GaleComponentMixin
{
    @Inject(method = { "addGaleParticles" }, at = { @At("HEAD") }, cancellable = true)
    private static void euphorics$cancelGaleParticles(final Entity entity, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(entity)) {
            ci.cancel();
        }
    }

    @WrapWithCondition(method = { "handle" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V") })
    private static boolean euphorics$cancelDashSound(final PlayerEntity instance, final SoundEvent sound, final float volume, final float pitch) {
        return EuphoriaComponent.isOutOfAstralPlane(instance);
    }
}
