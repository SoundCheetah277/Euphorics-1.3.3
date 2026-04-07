package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.cca.*;

@Mixin({ ItemEntity.class })
public abstract class ItemEntityMixin extends Entity
{
    public ItemEntityMixin(final EntityType<?> type, final World world) {
        super((EntityType)type, world);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    public void tick(final CallbackInfo ci) {
        ((ReverseGravityComponent)EuphoricsEntityComponents.REVERSE_GRAVITY.get((Object)this)).tick();
    }

    @Inject(method = { "onPlayerCollision" }, at = { @At("HEAD") }, cancellable = true)
    private void astralPlanePlayersDoNotPickUpItems(final PlayerEntity player, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane((Entity)player)) {
            ci.cancel();
        }
    }
}
