package com.eightsidedsquare.euphorics.common.block;

import java.util.function.*;
import java.util.*;

import com.eightsidedsquare.euphorics.core.EuphoricsBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.*;

public class RawEndustCauldronBlock extends LeveledCauldronBlock
{
    public RawEndustCauldronBlock(final AbstractBlock.Settings settings) {
        super(settings, (Predicate)null, (Map)null);
    }

    public ActionResult onUse(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockHitResult hit) {
        return ActionResult.PASS;
    }

    public void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random) {
        if (isSoulFire(world.getBlockState(pos.down())) && this.isFull(state) && random.nextInt(30) == 0) {
            world.setBlockState(pos, EuphoricsBlocks.SOUL_FIRED_ENDUST_CAULDRON.getDefaultState(), 3);
        }
    }

    public static boolean isSoulFire(final BlockState state) {
        return state.isOf(Blocks.SOUL_FIRE) || state.isOf(Blocks.SOUL_CAMPFIRE);
    }

    protected boolean canBeFilledByDripstone(final Fluid fluid) {
        return false;
    }

    public void precipitationTick(final BlockState state, final World world, final BlockPos pos, final Biome.Precipitation precipitation) {
    }
}
