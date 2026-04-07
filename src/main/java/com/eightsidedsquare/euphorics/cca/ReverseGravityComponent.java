package com.eightsidedsquare.euphorics.cca;

import dev.onyxstudios.cca.api.v3.component.sync.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import com.eightsidedsquare.euphorics.core.*;
import java.util.*;
import net.minecraft.*;
import org.jetbrains.annotations.*;

public class ReverseGravityComponent implements AutoSyncedComponent
{
    private final Entity entity;
    private boolean reverseGravity;

    public ReverseGravityComponent(final Entity entity) {
        this.entity = entity;
        this.reverseGravity = false;
    }

    public void tick() {
        if (!this.reverseGravity) {
            final Entity entity = this.entity;
            if (entity instanceof ItemEntity itemEntity) {
                if (itemEntity.getStack().isOf(EuphoricsItems.RAW_ENDUST)) {
                    this.reverseGravity = true;
                }
            }
        }
        if (this.reverseGravity) {
            if (!this.entity.hasNoGravity()) {
                this.entity.setNoGravity(true);
            }
            this.entity.addVelocity(0.0, 0.01, 0.0);
            final World world = this.entity.world;
            final BlockPos abovePos = this.entity.getBlockPos().up();
            final BlockState aboveState = world.getBlockState(abovePos);
            final Entity entity2 = this.entity;
            if (entity2 instanceof FallingBlockEntity fallingBlockEntity) {
                if (fallingBlockEntity.verticalCollision && !fallingBlockEntity.isOnGround()) {
                    final BlockState state = fallingBlockEntity.getBlockState();
                    final BlockPos pos = new BlockPos(fallingBlockEntity.getBlockX(), fallingBlockEntity.getY() + 0.949999988079071, fallingBlockEntity.getBlockZ());
                    final BlockState stateAtPos = world.getBlockState(pos);
                    if (state.isOf(EuphoricsBlocks.RAW_ENDUST_LAYER) && stateAtPos.isOf(EuphoricsBlocks.RAW_ENDUST_LAYER) && (int)stateAtPos.get(Properties.LAYERS) < 8) {
                        final int blockLayers = stateAtPos.get(Properties.LAYERS);
                        final int entityLayers = state.get(Properties.LAYERS);
                        world.setBlockState(pos, state.with(Properties.LAYERS, Math.min(8, entityLayers + blockLayers)));
                        if (blockLayers + entityLayers - 8 > 0 && world.getBlockState(pos.down()).isAir()) {
                            world.setBlockState(pos.down(), state.with(Properties.LAYERS, Math.min(8, blockLayers + entityLayers - 8)));
                        }
                    }
                    else if (stateAtPos.isOf(Blocks.SPORE_BLOSSOM)) {
                        world.setBlockState(pos, EuphoricsBlocks.WITHERED_SPORE_BLOSSOM.getDefaultState(), 3);
                        if (!world.isClient) {
                            ((ServerWorld)world).spawnParticles((ParticleEffect)new BlockStateParticleEffect(ParticleTypes.BLOCK, state), fallingBlockEntity.getX(), fallingBlockEntity.getY() + 0.95, fallingBlockEntity.getZ(), 100, 0.25, 0.05, 0.25, 0.0);
                        }
                    }
                    else if (Block.isFaceFullSquare(aboveState.getSidesShape(world, abovePos), Direction.DOWN) && fallingBlockEntity.getBlockStateAtPos().isAir()) {
                        world.setBlockState(fallingBlockEntity.getBlockPos(), state, 3);
                    }
                    else if (fallingBlockEntity.dropItem && world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS) && world instanceof ServerWorld serverWorld) {
                        final List<ItemStack> list = state.getDroppedStacks(new LootContext.Builder(serverWorld).parameter(LootContextParameters.ORIGIN, this.entity.getPos()).parameter(LootContextParameters.TOOL, ItemStack.EMPTY));
                        for (final ItemStack stack : list) {
                            fallingBlockEntity.dropStack(stack);
                        }
                    }
                    fallingBlockEntity.discard();
                    return;
                }
            }
            if (world.isOutOfHeightLimit(this.entity.getBlockPos())) {
                this.entity.discard();
            }
        }
    }

    public boolean hasReverseGravity() {
        return this.reverseGravity;
    }

    public void setReverseGravity(final boolean reverseGravity) {
        this.reverseGravity = reverseGravity;
    }

    public void readFromNbt(@NotNull final NbtCompound nbt) {
        this.setReverseGravity(nbt.getBoolean("ReverseGravity"));
    }

    public void writeToNbt(@NotNull final NbtCompound nbt) {
        nbt.putBoolean("ReverseGravity", this.hasReverseGravity());
    }
}
