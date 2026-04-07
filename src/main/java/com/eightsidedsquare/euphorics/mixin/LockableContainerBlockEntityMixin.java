package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.core.*;
import java.util.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;

@Mixin({ LockableContainerBlockEntity.class })
public abstract class LockableContainerBlockEntityMixin extends BlockEntity implements Inventory
{
    public LockableContainerBlockEntityMixin(final BlockEntityType<?> type, final BlockPos pos, final BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = { "checkUnlocked(Lnet/minecraft/entity/player/PlayerEntity;)Z" }, at = { @At("HEAD") })
    public void checkUnlocked(final PlayerEntity player, final CallbackInfoReturnable<Boolean> cir) {
        if (this.getWorld() != null && this.containsAny(stack -> stack.isOf(EuphoricsItems.RAW_ENDUST))) {
            final BlockState state = this.getCachedState();
            final World world = this.getWorld();
            final Direction d = state.getProperties().stream().filter(property -> property.equals((Object)Properties.FACING) || property.equals((Object)Properties.HORIZONTAL_FACING)).findFirst().flatMap(property -> Optional.ofNullable((Direction)state.get(property))).orElse(Direction.UP);
            if (d != Direction.DOWN) {
                final int size = this.size();
                final List<ItemStack> found = new ArrayList<ItemStack>();
                for (int slot = 0; slot < size; ++slot) {
                    final ItemStack stack2 = this.getStack(slot);
                    if (stack2 != null && stack2.isOf(EuphoricsItems.RAW_ENDUST)) {
                        found.add(stack2.copy());
                        this.setStack(slot, ItemStack.EMPTY);
                    }
                }
                final World World;
                final Direction class_1938;
                found.forEach(stack -> World.spawnEntity(new ItemEntity(World, this.getPos().getX() + 0.5 + class_1938.getOffsetX() * 0.65, this.getPos().getY() + 0.5 + class_1938.getOffsetY() * 0.65, this.getPos().getZ() + 0.5 + class_1938.getOffsetZ() * 0.65, stack)));
            }
        }
    }
}
