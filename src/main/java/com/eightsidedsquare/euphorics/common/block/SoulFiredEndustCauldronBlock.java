package com.eightsidedsquare.euphorics.common.block;

import java.util.*;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractCauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;

public class SoulFiredEndustCauldronBlock extends AbstractCauldronBlock
{
    public SoulFiredEndustCauldronBlock(final AbstractBlock.Settings settings) {
        super(settings, (Map)null);
    }

    public ActionResult onUse(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockHitResult hit) {
        player.giveItemStack(new ItemStack((ItemConvertible)EuphoricsBlocks.SOUL_FIRED_ENDUST.asItem()));
        world.setBlockState(pos, Blocks.CAULDRON.getDefaultState(), 3);
        return ActionResult.success(world.isClient);
    }

    public boolean isFull(final BlockState state) {
        return true;
    }
}
