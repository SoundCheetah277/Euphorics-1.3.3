package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PickaxeItem;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ Item.class })
public abstract class ItemMixin
{
    @Inject(method = { "useOnBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void useOnBlock(final ItemUsageContext ctx, final CallbackInfoReturnable<ActionResult> cir) {
        final ItemStack stack = ctx.getStack();
        final Item getItem = stack.getItem();
        if (getItem instanceof PickaxeItem toolItem) {
            if (toolItem.getMaterial().getMiningLevel() >= 3) {
                final PlayerEntity player = ctx.getPlayer();
                final World world = ctx.getWorld();
                final BlockPos pos = ctx.getBlockPos();
                final BlockState state = world.getBlockState(pos);
                if (state.isOf(Blocks.END_PORTAL_FRAME) && ctx.getSide() != Direction.DOWN && world.getBlockState(pos.up()).isAir() && player != null) {
                    stack.damage(1, (LivingEntity)player, p -> p.sendToolBreakStatus(ctx.getHand()));
                    world.playSound(null, pos, EuphoricsSounds.ITEM_PICKAXE_SCRAPE, SoundCategory.BLOCKS, 1.0f, 1.5f);
                    world.setBlockState(pos, EuphoricsBlocks.SCRAPED_END_PORTAL_FRAME.getStateWithProperties(state));
                    world.createAndScheduleBlockTick(pos, EuphoricsBlocks.SCRAPED_END_PORTAL_FRAME, 100);
                    final FallingBlockEntity entity = new FallingBlockEntity(world, pos.getX() + 0.5, (double)pos.getY(), pos.getZ() + 0.5, EuphoricsBlocks.RAW_ENDUST_LAYER.getDefaultState());
                    final ReverseGravityComponent component = EuphoricsEntityComponents.REVERSE_GRAVITY.get(entity);
                    component.setReverseGravity(true);
                    world.spawnEntity(entity);
                    if (world.isClient) {
                        final Random random = ctx.getWorld().random;
                        final Vec3d hit = ctx.getHitPos();
                        for (int i = 0; i < 8; ++i) {
                            final double velocityX = (random.nextDouble() - 0.5) / 10.0;
                            final double velocityY = random.nextDouble() / 3.0 + 0.1;
                            final double velocityZ = (random.nextDouble() - 0.5) / 10.0;
                            world.addParticle(EuphoricsParticles.RAW_ENDUST, hit.getX(), hit.getY(), hit.getZ(), velocityX, velocityY, velocityZ);
                        }
                    }
                    cir.setReturnValue(ActionResult.success(ctx.getWorld().isClient));
                }
            }
        }
    }
}
