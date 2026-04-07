package com.eightsidedsquare.euphorics.common.world;

import java.util.function.*;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.SpawnHelper;
import net.minecraft.world.spawner.Spawner;
import com.eightsidedsquare.euphorics.core.*;
import com.eightsidedsquare.euphorics.common.entity.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.cca.*;

public class ShadeSpawner implements Spawner
{
    private int cooldown;
    private static final int MAX_SHADES_NEAR_PLAYER = 3;

    public int spawn(final ServerWorld world, final boolean spawnMonsters, final boolean spawnAnimals) {
        if (!spawnMonsters || --this.cooldown > 0) {
            return 0;
        }
        this.cooldown = 200 + world.random.nextInt(200);
        int count = 0;
        final Random random = world.random;
        for (final PlayerEntity player : world.getPlayers(this::canSpawnForPlayer)) {
            final BlockPos spawnPos = player.getBlockPos().add(random.nextBetween(-32, 32), random.nextBetween(-4, 12), random.nextBetween(-32, 32));
            final LivingEntity LivingEntity;
            if (SpawnHelper.isClearForSpawn(world, spawnPos, world.getBlockState(spawnPos), world.getFluidState(spawnPos), EuphoricsEntities.SHADE) && world.getEntitiesByClass(ShadeEntity.class, player.getBoundingBox().expand(80.0), shade -> !shade.cannotDespawn() && !shade.isPersistent() && shade.getTarget() == LivingEntity).size() < 3) {
                final ShadeEntity shadeEntity = EuphoricsEntities.SHADE.create(world);
                if (shadeEntity == null) {
                    continue;
                }
                shadeEntity.refreshPositionAndAngles(spawnPos, 0.0f, 0.0f);
                shadeEntity.setVelocity(0.0, 0.0, 0.0);
                shadeEntity.initialize(world, world.getLocalDifficulty(spawnPos), SpawnReason.NATURAL, null, null);
                shadeEntity.setTarget(player);
                world.spawnEntityAndPassengers(shadeEntity);
                ++count;
            }
        }
        return count;
    }

    private boolean canSpawnForPlayer(final ServerPlayerEntity player) {
        final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.getNullable(player);
        return component != null && component.getShadeAggro() >= 6000;
    }
}
