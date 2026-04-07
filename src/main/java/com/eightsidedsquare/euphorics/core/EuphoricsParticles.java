package com.eightsidedsquare.euphorics.core;

import java.util.*;
import net.fabricmc.fabric.api.particle.v1.*;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class EuphoricsParticles
{
    private static final Map<ParticleType<?>, Identifier> PARTICLES;
    public static final DefaultParticleType RAW_ENDUST;
    public static final DefaultParticleType ASTRAL_PLANE;

    public static void init() {
        EuphoricsParticles.PARTICLES.keySet().forEach(particle -> Registry.register(Registry.PARTICLE_TYPE, EuphoricsParticles.PARTICLES.get(particle), particle));
    }

    private static <T extends ParticleType<?>> T create(final String name, final T type) {
        EuphoricsParticles.PARTICLES.put(type, EuphoricsMod.id(name));
        return type;
    }

    static {
        PARTICLES = new LinkedHashMap<ParticleType<?>, Identifier>();
        RAW_ENDUST = create("raw_endust", FabricParticleTypes.simple(true));
        ASTRAL_PLANE = create("astral_plane", FabricParticleTypes.simple(true));
    }
}
