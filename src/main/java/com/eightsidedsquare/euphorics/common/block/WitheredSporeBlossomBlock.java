package com.eightsidedsquare.euphorics.common.block;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;

public class WitheredSporeBlossomBlock extends SporeBlossomBlock
{
    private static final IntProperty WITHER;

    public WitheredSporeBlossomBlock(final AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(WitheredSporeBlossomBlock.WITHER, 0));
    }

    public void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        if (random.nextInt(5) == 0) {
            final BlockPos.Mutable testPos = pos.mutableCopy();
            while (!world.isOutOfHeightLimit(testPos)) {
                testPos.move(Direction.DOWN);
                BlockState testState = world.getBlockState(testPos);
                if (testState.isOf(Blocks.CAULDRON)) {
                    world.setBlockState(testPos, EuphoricsBlocks.RAW_ENDUST_CAULDRON.getDefaultState(), 3);
                    this.wither(state, world, pos);
                    break;
                }
                if (testState.isOf(EuphoricsBlocks.RAW_ENDUST_CAULDRON)) {
                    final int level = testState.get(Properties.LEVEL_3);
                    if (level == 3 && this.wither(state, world, pos)) {
                        world.breakBlock(pos, false);
                        return;
                    }
                    if (level < 3) {
                        testState = testState.with(Properties.LEVEL_3, level + 1);
                        world.setBlockState(testPos, testState, 3);
                        this.wither(state, world, pos);
                        break;
                    }
                    break;
                }
                else {
                    if (!testState.isAir() || testState.isSolidBlock(world, testPos)) {
                        break;
                    }
                    if (!testState.getCollisionShape(world, testPos).isEmpty()) {
                        break;
                    }
                }
            }
        }
    }

    private boolean wither(final BlockState state, final ServerWorld world, final BlockPos pos) {
        final int wither = state.get(WitheredSporeBlossomBlock.WITHER);
        if (!state.isOf(this)) {
            return false;
        }
        if (wither < 3) {
            world.setBlockState(pos, state.with(WitheredSporeBlossomBlock.WITHER, wither + 1), 3);
            return false;
        }
        return true;
    }

    public void randomDisplayTick(final BlockState state, final World world, final BlockPos pos, final Random random) {
        final double x = pos.getX() + random.nextDouble();
        final double y = pos.getY() + 0.7;
        final double z = pos.getZ() + random.nextDouble();
        world.addParticle(EuphoricsParticles.RAW_ENDUST, x, y, z, 0.0, -0.05, 0.0);
    }

    protected void appendProperties(final StateManager.Builder<Block, BlockState> builder) {
        builder.add(WitheredSporeBlossomBlock.WITHER);
    }

    static {
        WITHER = IntProperty.of("wither", 0, 3);
    }
}
