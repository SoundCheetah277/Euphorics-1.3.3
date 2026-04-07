package com.eightsidedsquare.euphorics.mixin.client;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.common.entity.*;
import com.eightsidedsquare.euphorics.cca.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;

@Mixin({ EntityRenderDispatcher.class })
public abstract class EntityRenderDispatcherMixin
{
    @Inject(method = { "renderHitbox" }, at = { @At("HEAD") }, cancellable = true)
    private static void legends$hiddenHitboxes(final MatrixStack matrices, final VertexConsumer vertices, final Entity entity, final float tickDelta, final CallbackInfo ci) {
        if (entity instanceof ShadeEntity || !EuphoriaComponent.isOutOfAstralPlane(entity)) {
            ci.cancel();
        }
    }

    @Inject(method = { "renderShadow" }, at = { @At("HEAD") }, cancellable = true)
    private static void legends$hiddenShadows(final MatrixStack matrices, final VertexConsumerProvider vertexConsumers, final Entity entity, final float opacity, final float tickDelta, final WorldView world, final float radius, final CallbackInfo ci) {
        if (entity instanceof ShadeEntity || !EuphoriaComponent.isOutOfAstralPlane(entity)) {
            ci.cancel();
        }
    }
}
