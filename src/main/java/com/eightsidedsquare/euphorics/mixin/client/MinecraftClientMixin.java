package com.eightsidedsquare.euphorics.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.*;
import org.jetbrains.annotations.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.eightsidedsquare.euphorics.common.packet.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ MinecraftClient.class })
public abstract class MinecraftClientMixin
{
    @Shadow
    @Nullable
    public ClientPlayerEntity player;

    @WrapOperation(method = { "handleInputEvents" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V") })
    private void customInputEvent(final ClientPlayNetworkHandler handler, final Packet<?> packet, final Operation<Void> original) {
        if (this.player != null && this.player.isSneaking()) {
            final EuphoriaComponent component = (EuphoriaComponent)EuphoricsEntityComponents.EUPHORIA.get((Object)this.player);
            if (component.isReadyForAstralPlaneJump()) {
                PlayerAstralPlaneJumpC2SPacket.send();
                return;
            }
        }
        original.call(new Object[] { handler, packet });
    }
}
