package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import com.eightsidedsquare.euphorics.cca.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.common.entity.*;
import net.minecraft.*;

@Mixin({ PlayerEntity.class })
public abstract class PlayerEntityMixin extends LivingEntity
{
    protected PlayerEntityMixin(final EntityType<? extends LivingEntity> entityType, final World world) {
        super(entityType, world);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    private void tick(final CallbackInfo ci) {
        EuphoricsEntityComponents.EUPHORIA.get(this).tick();
    }

    @Inject(method = { "addExhaustion" }, at = { @At("HEAD") }, cancellable = true)
    private void addExhaustion(final float exhaustion, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(this)) {
            ci.cancel();
        }
    }

    @Inject(method = { "isInvulnerableTo" }, at = { @At("RETURN") }, cancellable = true)
    private void invulnerableInAstralPlane(final DamageSource damageSource, final CallbackInfoReturnable<Boolean> cir) {
        if (!(boolean)cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this) && !damageSource.isOutOfWorld() && !(damageSource.getAttacker() instanceof ShadeEntity)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = { "isBlockBreakingRestricted" }, at = { @At("RETURN") }, cancellable = true)
    private void noBlockBreakingForAstralPlanePlayers(final World world, final BlockPos pos, final GameMode gameMode, final CallbackInfoReturnable<Boolean> cir) {
        if (!(boolean)cir.getReturnValue() && !EuphoriaComponent.isOutOfAstralPlane(this)) {
            cir.setReturnValue(true);
        }
    }
}
