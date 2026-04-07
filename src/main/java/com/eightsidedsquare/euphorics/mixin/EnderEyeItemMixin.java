package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.core.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;

@Mixin({ EnderEyeItem.class })
public abstract class EnderEyeItemMixin
{
    @Inject(method = { "useOnBlock" }, at = { @At("HEAD") }, cancellable = true)
    private void cannotFillFrameWhenScrapedFrameIsNear(final ItemUsageContext ctx, final CallbackInfoReturnable<ActionResult> cir) {
        final BlockPos pos = ctx.getBlockPos();
        final World world = ctx.getWorld();
        final BlockState state = world.getBlockState(pos);
        if (state.isOf(Blocks.END_PORTAL_FRAME) && !(boolean)state.get(Properties.EYE) && BlockPos.stream(pos.add(-4, 0, -4), pos.add(4, 0, 4)).anyMatch(blockPos -> world.getBlockState(blockPos).isOf(EuphoricsBlocks.SCRAPED_END_PORTAL_FRAME))) {
            cir.setReturnValue( ActionResult.PASS);
        }
    }

    @WrapOperation(method = { "use" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z") })
    private boolean doNotThrowEnderEyeWhenUsedOnScrapedEndPortalFrame(final BlockState state, final Block block, final Operation<Boolean> original) {
        return original.call(new Object[] { state, block }) || state.isOf(EuphoricsBlocks.SCRAPED_END_PORTAL_FRAME);
    }
}
