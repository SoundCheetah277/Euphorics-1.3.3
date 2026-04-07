package com.eightsidedsquare.euphorics.common.packet;

import io.netty.buffer.*;
import net.fabricmc.fabric.api.client.networking.v1.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.*;
import net.minecraft.*;
import net.fabricmc.fabric.api.networking.v1.*;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import com.eightsidedsquare.euphorics.cca.*;
import com.eightsidedsquare.euphorics.core.*;

public class PlayerAstralPlaneJumpC2SPacket
{
    public static final Identifier ID;

    public static void send() {
        ClientPlayNetworking.send(PlayerAstralPlaneJumpC2SPacket.ID, new PacketByteBuf(Unpooled.buffer()));
    }

    public static void handler(final MinecraftServer server, final ServerPlayerEntity player, final ServerPlayNetworkHandler handler, final PacketByteBuf buf, final PacketSender packetSender) {
        final EuphoriaComponent component;
        server.execute(() -> {
            component = (EuphoriaComponent)EuphoricsEntityComponents.EUPHORIA.get((Object)player);
            if (component.isReadyForAstralPlaneJump()) {
                if (component.isInAstralPlane()) {
                    component.exitAstralPlane();
                }
                else {
                    component.jumpToAstralPlane();
                }
                EuphoricsEntityComponents.EUPHORIA.sync((Object)player);
            }
        });
    }

    static {
        ID = EuphoricsMod.id("astral_plane_jump");
    }
}
