package com.eightsidedsquare.euphorics.common.block;

import com.google.common.collect.*;
import net.minecraft.block.*;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import com.eightsidedsquare.euphorics.cca.*;
import org.jetbrains.annotations.*;
import net.minecraft.*;

public class EndustLayerBlock extends FallingBlock
{
    private static final IntProperty LAYERS;
    protected static final ImmutableList<VoxelShape> LAYERS_TO_SHAPE;

    public EndustLayerBlock(final AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(EndustLayerBlock.LAYERS, 1));
    }

    public void randomDisplayTick(final BlockState state, final World world, final BlockPos pos, final Random random) {
    }

    public void scheduledTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        if (canFallThrough(world.getBlockState(pos.up())) && pos.getY() >= world.getBottomY()) {
            final FallingBlockEntity fallingBlockEntity = FallingBlockEntity.spawnFromBlock(world, pos, state);
            final ReverseGravityComponent component = EuphoricsEntityComponents.REVERSE_GRAVITY.get(fallingBlockEntity);
            component.setReverseGravity(true);
            this.configureFallingBlockEntity(fallingBlockEntity);
        }
    }

    protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
        builder.add(EndustLayerBlock.LAYERS);
    }

    public VoxelShape getOutlineShape(final BlockState state, final BlockView world, final BlockPos pos, final ShapeContext context) {
        return EndustLayerBlock.LAYERS_TO_SHAPE.get(state.get(EndustLayerBlock.LAYERS) - 1);
    }

    public boolean canPathfindThrough(final BlockState state, final BlockView world, final BlockPos pos, final NavigationType type) {
        return false;
    }

    public boolean hasSidedTransparency(final BlockState state) {
        return true;
    }

    public float getAmbientOcclusionLightLevel(final BlockState state, final BlockView world, final BlockPos pos) {
        return (state.get(EndustLayerBlock.LAYERS) == 8) ? 0.2f : 1.0f;
    }

    public boolean canReplace(final BlockState state, final ItemPlacementContext context) {
        final Integer i = state.get(EndustLayerBlock.LAYERS);
        return (context.getPlayer() == null || !context.getPlayer().isSneaking()) && ((context.getStack().isOf(this.asItem()) && i < 8) || i == 1);
    }

    @Nullable
    public BlockState getPlacementState(final ItemPlacementContext ctx) {
        final BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(EndustLayerBlock.LAYERS, Math.min(8, blockState.get(EndustLayerBlock.LAYERS) + 1));
        }
        return super.getPlacementState(ctx);
    }

    static {
        LAYERS = Properties.LAYERS;
        LAYERS_TO_SHAPE = ImmutableList.of(Block.createCuboidShape(0.0, 14.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 12.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 10.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 6.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 4.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 2.0, 0.0, 16.0, 16.0, 16.0), Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 16.0));
    }
}
