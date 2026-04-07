package com.eightsidedsquare.euphorics.common.block;

import net.minecraft.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;

public class UncrystallizedEndustBlock extends Block
{
    public UncrystallizedEndustBlock(final AbstractBlock.Settings settings) {
        super(settings);
    }

    public void onStacksDropped(final BlockState state, final ServerWorld world, final BlockPos pos, final ItemStack stack, final boolean dropExperience) {
        super.onStacksDropped(state, world, pos, stack, dropExperience);
        if (dropExperience) {
            this.dropExperienceWhenMined(world, pos, stack, (IntProvider) ConstantIntProvider.create(1));
        }
    }
}
