package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.BuddingAmethystBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.core.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ BuddingAmethystBlock.class })
public abstract class BuddingAmethystBlockMixin
{
    @Inject(method = { "randomTick" }, at = { @At("HEAD") })
    public void randomTick(final BlockState state, final ServerWorld world, final BlockPos pos, final Random random, final CallbackInfo ci) {
        if (random.nextInt(15) == 0) {
            final BlockPos blockPos = pos.offset(Direction.random(random));
            if (world.getBlockState(blockPos).isOf(EuphoricsBlocks.UNCRYSTALLIZED_ENDUST_BLOCK)) {
                world.setBlockState(blockPos, EuphoricsBlocks.CRYSTALLIZED_ENDUST_BLOCK.getDefaultState(), 3);
            }
        }
    }
}
