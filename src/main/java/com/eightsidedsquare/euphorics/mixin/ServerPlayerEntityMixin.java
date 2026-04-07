package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import com.mojang.authlib.*;
import org.jetbrains.annotations.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.common.entity.*;
import com.eightsidedsquare.euphorics.cca.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ServerPlayerEntity.class })
public abstract class ServerPlayerEntityMixin extends PlayerEntity
{
    public ServerPlayerEntityMixin(final World world, final BlockPos pos, final float yaw, final GameProfile gameProfile, @Nullable final PlayerPublicKey publicKey) {
        super(world, pos, yaw, gameProfile, publicKey);
    }

    @Inject(method = { "attack" }, at = { @At("HEAD") }, cancellable = true)
    private void cancelAttackForAstralPlanePlayers(final Entity target, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane((Entity)this)) {
            if (target instanceof ShadeEntity) {
                return;
            }
            if (target instanceof PlayerEntity) {
                final PlayerEntity player = (PlayerEntity)target;
                if (!EuphoriaComponent.isOutOfAstralPlane((Entity)player)) {
                    EuphoricsEntityComponents.EUPHORIA.maybeGet((Object)player).ifPresent(EuphoriaComponent::kickFromAstralPlane);
                    return;
                }
            }
            ci.cancel();
        }
    }
}
