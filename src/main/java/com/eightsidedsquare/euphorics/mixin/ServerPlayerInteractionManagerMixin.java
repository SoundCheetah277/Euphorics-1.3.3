package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ServerPlayerInteractionManager.class })
public abstract class ServerPlayerInteractionManagerMixin
{
    @WrapOperation(method = { "interactBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onUse(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;") })
    private ActionResult cancelBlockInteractForAstralPlanePlayers(final BlockState state, final World world, final PlayerEntity player, final Hand hand, final BlockHitResult hit, final Operation<ActionResult> original) {
        if (!EuphoricsEntityComponents.EUPHORIA.get(player).isInAstralPlane()) {
            return original.call(state, world, player, hand, hit);
        }
        return ActionResult.FAIL;
    }

    @WrapOperation(method = { "interactBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;isCoolingDown(Lnet/minecraft/item/Item;)Z") })
    private boolean cancelUseOnBlockForAstralPlanePlayers(final ItemCooldownManager manager, final Item item, final Operation<Boolean> original, final ServerPlayerEntity player, final World world, final ItemStack stack, final Hand hand, final BlockHitResult hitResult) {
        return original.call(manager, item) || !EuphoriaComponent.isOutOfAstralPlane(player);
    }

    @Inject(method = { "interactItem" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelItemInteractionForAstralPlanePlayers(final ServerPlayerEntity player, final World world, final ItemStack stack, final Hand hand, final CallbackInfoReturnable<ActionResult> cir) {
        if (!EuphoriaComponent.isOutOfAstralPlane(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
