package com.eightsidedsquare.euphorics.mixin.client;

import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.common.entity.*;
import net.minecraft.*;

@Mixin({ ClientPlayerInteractionManager.class })
public abstract class ClientPlayerInteractionManagerMixin
{
    @WrapOperation(method = { "interactBlockInternal" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onUse(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;") })
    private ActionResult cancelBlockInteractForAstralPlanePlayers(final BlockState state, final World world, final PlayerEntity player, final Hand hand, final BlockHitResult hit, final Operation<ActionResult> original) {
        if (!EuphoricsEntityComponents.EUPHORIA.get(player).isInAstralPlane()) {
            return original.call(new Object[] { state, world, player, hand, hit });
        }
        return ActionResult.FAIL;
    }

    @WrapOperation(method = { "interactBlockInternal" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;isCoolingDown(Lnet/minecraft/item/Item;)Z") })
    private boolean cancelUseOnBlockForAstralPlanePlayers(final ItemCooldownManager manager, final Item item, final Operation<Boolean> original, final ClientPlayerEntity player, final Hand hand, final BlockHitResult hitResult) {
        return original.call(new Object[] { manager, item }) || !EuphoriaComponent.isOutOfAstralPlane(player);
    }

    @Inject(method = { "interactItem" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelItemInteractionForAstralPlanePlayers(final PlayerEntity player, final Hand hand, final CallbackInfoReturnable<ActionResult> cir) {
        if (!EuphoriaComponent.isOutOfAstralPlane(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = { "attackEntity" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelAttackForAstralPlanePlayers(final PlayerEntity player, final Entity target, final CallbackInfo ci) {
        if (!(target instanceof ShadeEntity) && !EuphoriaComponent.isOutOfAstralPlane(player)) {
            ci.cancel();
        }
    }

    @Inject(method = { "interactEntity" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelInteractEntityForAstralPlanePlayers(final PlayerEntity player, final Entity entity, final Hand hand, final CallbackInfoReturnable<ActionResult> cir) {
        if (!EuphoriaComponent.isOutOfAstralPlane(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(method = { "interactEntityAtLocation" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelInteractEntityAtLocationForAstralPlanePlayers(final PlayerEntity player, final Entity entity, final EntityHitResult hitResult, final Hand hand, final CallbackInfoReturnable<ActionResult> cir) {
        if (!EuphoriaComponent.isOutOfAstralPlane(player)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}
