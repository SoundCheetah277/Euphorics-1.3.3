package com.eightsidedsquare.euphorics.cca;

import dev.onyxstudios.cca.api.v3.entity.*;
import dev.onyxstudios.cca.api.v3.component.*;
import net.minecraft.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class EuphoricsEntityComponents implements EntityComponentInitializer
{
    public static final ComponentKey<EuphoriaComponent> EUPHORIA;
    public static final ComponentKey<ReverseGravityComponent> REVERSE_GRAVITY;

    public void registerEntityComponentFactories(final EntityComponentFactoryRegistry registry) {
        registry.beginRegistration( PlayerEntity.class, EuphoricsEntityComponents.EUPHORIA).respawnStrategy(RespawnCopyStrategy.LOSSLESS_ONLY).end(EuphoriaComponent::new);
        registry.beginRegistration(Entity.class, EuphoricsEntityComponents.REVERSE_GRAVITY).end(ReverseGravityComponent::new);
    }

    static {
        EUPHORIA = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("euphorics", "euphoria"), EuphoriaComponent.class);
        REVERSE_GRAVITY = ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("euphorics", "reverse_gravity"), ReverseGravityComponent.class);
    }
}
