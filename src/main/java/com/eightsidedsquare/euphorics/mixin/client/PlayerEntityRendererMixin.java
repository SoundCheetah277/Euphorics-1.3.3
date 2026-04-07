package com.eightsidedsquare.euphorics.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.client.*;
import com.eightsidedsquare.euphorics.cca.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ PlayerEntityRenderer.class })
public abstract class PlayerEntityRendererMixin
{
    @Inject(method = { "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelRenderForAstralPlanePlayers(final AbstractClientPlayerEntity player, final float f, final float g, final MatrixStack matrixStack, final VertexConsumerProvider vertexConsumerProvider, final int i, final CallbackInfo ci) {
        if (!EuphoricsClient.isInAstralPlane && !EuphoriaComponent.isOutOfAstralPlane((Entity)player)) {
            ci.cancel();
        }
    }
}
