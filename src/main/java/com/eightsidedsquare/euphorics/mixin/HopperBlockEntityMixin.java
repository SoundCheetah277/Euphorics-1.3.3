package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.Inventory;
import org.spongepowered.asm.mixin.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ HopperBlockEntity.class })
public abstract class HopperBlockEntityMixin
{
    @Inject(method = { "extract(Lnet/minecraft/inventory/Inventory;Lnet/minecraft/entity/ItemEntity;)Z" }, at = { @At("HEAD") }, cancellable = true)
    private static void doNotExtractItemsThatFlyUpBecauseTheyDoNotGoDown(final Inventory inventory, final ItemEntity entity, final CallbackInfoReturnable<Boolean> cir) {
        if (EuphoricsEntityComponents.REVERSE_GRAVITY.get(entity).hasReverseGravity()) {
            cir.setReturnValue(false);
        }
    }
}
