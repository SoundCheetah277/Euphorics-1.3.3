package com.eightsidedsquare.euphorics.core;

import com.eightsidedsquare.euphorics.common.packet.*;
import net.fabricmc.fabric.api.networking.v1.*;

public class EuphoricsNetworking
{
    public static void init() {
        ServerPlayNetworking.registerGlobalReceiver(PlayerAstralPlaneJumpC2SPacket.ID, PlayerAstralPlaneJumpC2SPacket::handler);
    }
}
