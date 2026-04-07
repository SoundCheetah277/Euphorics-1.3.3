package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.brain.task.RamImpactTask;
import net.minecraft.entity.passive.GoatEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ RamImpactTask.class })
public abstract class RamImpactTaskMixin
{
    @Inject(method = { "shouldSnapHorn" }, at = { @At("HEAD") })
    private void shouldSnapHorn(final ServerWorld world, final GoatEntity goat, final CallbackInfoReturnable<Boolean> cir) {
        final Vec3d vec3d = goat.getVelocity().multiply(1.0, 0.0, 1.0).normalize();
        final BlockPos pos = new BlockPos(goat.getPos().add(vec3d));
        final BlockState state = world.getBlockState(pos);
        if (state.isOf(EuphoricsBlocks.CRYSTALLIZED_ENDUST_BLOCK)) {
            world.breakBlock(pos, false);
            final int count = goat.getRandom().nextBetween(7, 9);
            Block.dropStack((World)world, pos, new ItemStack((ItemConvertible)EuphoricsItems.CRYSTALLIZED_ENDUST, count));
        }
    }
}
