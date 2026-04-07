package com.eightsidedsquare.euphorics.core;

import net.fabricmc.api.*;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.*;
import com.eightsidedsquare.euphorics.common.world.*;
import net.fabricmc.fabric.api.event.lifecycle.v1.*;
import net.minecraft.*;

public class EuphoricsMod implements ModInitializer
{
    public static final String MOD_ID = "euphorics";

    public void onInitialize() {
        GeckoLib.initialize();
        EuphoricsBlocks.init();
        EuphoricsItems.init();
        EuphoricsEntities.init();
        EuphoricsSounds.init();
        EuphoricsParticles.init();
        EuphoricsCommands.init();
        EuphoricsNetworking.init();
        final ShadeSpawner shadeSpawner = new ShadeSpawner();
        ServerTickEvents.END_WORLD_TICK.register((world -> shadeSpawner.spawn(world, world.getServer().isMonsterSpawningEnabled(), world.getServer().shouldSpawnAnimals())));
    }

    public static Identifier id(final String path) {
        return new Identifier("euphorics", path);
    }
}
