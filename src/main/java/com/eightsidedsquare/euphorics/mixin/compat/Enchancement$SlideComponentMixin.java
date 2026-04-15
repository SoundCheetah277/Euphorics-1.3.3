package com.eightsidedsquare.euphorics.mixin.compat;
/*
//import moriyashiine.enchancement.common.component.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.*;
//import moriyashiine.enchancement.mixin.slide.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.v2.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;

//@Mixin({ SlideComponent.class })
public class Enchancement$SlideComponentMixin
{
    @Shadow
    @Final
    private PlayerEntity obj;

    @WrapWithCondition(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/mixin/slide/EntityAccessor;enchancement$spawnSprintingParticles()V") }, remap = false)
    private boolean euphorics$cancelSprintParticles(final EntityAccessor instance) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }

    @WrapWithCondition(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;emitGameEvent(Lnet/minecraft/world/event/GameEvent;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/world/event/GameEvent$Emitter;)V") })
    private boolean euphorics$cancelGameEvent(final World instance, final GameEvent gameEvent, final Vec3d vec3d, final GameEvent.Emitter emitter) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }

    @WrapWithCondition(method = { "slamTick" }, at = { @At(value = "INVOKE", target = "Ljava/lang/Runnable;run()V") }, remap = false)
    private boolean euphorics$cancelSlamLand(final Runnable instance) {
        return EuphoriaComponent.isOutOfAstralPlane(this.obj);
    }
}*/
