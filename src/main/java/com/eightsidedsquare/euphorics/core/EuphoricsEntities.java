package com.eightsidedsquare.euphorics.core;

import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import com.eightsidedsquare.euphorics.common.entity.*;
import java.util.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.*;
import java.util.function.*;
import net.minecraft.*;

public class EuphoricsEntities
{
    private static final Map<EntityType<?>, Identifier> ENTITIES;
    public static final EntityType<ShadeEntity> SHADE;

    public static void init() {
        EuphoricsEntities.ENTITIES.keySet().forEach(type -> Registry.register((Registry)Registry.ENTITY_TYPE, (Identifier)EuphoricsEntities.ENTITIES.get(type), (Object)type));
    }

    private static <T extends Entity> EntityType<T> create(final String name, final EntityType<T> type) {
        EuphoricsEntities.ENTITIES.put(type, EuphoricsMod.id(name));
        return type;
    }

    static {
        ENTITIES = new LinkedHashMap<EntityType<?>, Identifier>();
        SHADE = create("shade", (net.minecraft.entity.EntityType<ShadeEntity>)FabricEntityTypeBuilder.createMob().entityFactory(ShadeEntity::new).defaultAttributes((Supplier)ShadeEntity::createAttributes).dimensions(EntityDimensions.changing(0.75f, 1.5f)).spawnGroup(SpawnGroup.MONSTER).spawnRestriction(SpawnRestriction.Location.NO_RESTRICTIONS, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileEntity::canSpawnInDark).build());
    }
}
