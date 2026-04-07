package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SculkVeinBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.core.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { SculkVeinBlock.class }, priority = 1500)
public abstract class SculkVeinBlockMixin
{
    @WrapOperation(method = { "spreadAtSamePosition" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z", ordinal = 1) })
    private boolean isInTag(final BlockState state, final Block block, final Operation<Boolean> original) {
        return original.call(state, block) || state.isOf(EuphoricsBlocks.UNCRYSTALLIZED_ENDUST_BLOCK);
    }

    @WrapOperation(method = { "convertToBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z") })
    private boolean convertToSculkBlock(final WorldAccess world, final BlockPos pos, final BlockState blockState, final int flags, final Operation<Boolean> original) {
        return original.call(world, pos, blockState, flags);
    }

    @Mixin(value = { SculkVeinBlock.SculkVeinGrowChecker.class }, priority = 1500)
    public abstract static class SculkVeinGrowCheckerMixin
    {
        @Inject(method = { "canGrow(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;)Z" }, at = { @At("HEAD") }, cancellable = true)
        public void canGrow(final BlockView world, final BlockPos pos, final BlockPos growPos, final Direction direction, final BlockState state, final CallbackInfoReturnable<Boolean> cir) {
            final BlockState blockState = world.getBlockState(growPos.offset(direction));
            if (blockState.isOf(EuphoricsBlocks.UNCRYSTALLIZED_ENDUST_BLOCK)) {
                cir.setReturnValue(false);
            }
        }
    }
}
