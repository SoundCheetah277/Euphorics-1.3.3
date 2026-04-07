package com.eightsidedsquare.euphorics.mixin.client;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.*;
import java.util.function.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.common.entity.*;
import com.eightsidedsquare.euphorics.client.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ GameRenderer.class })
public abstract class GameRendererMixin
{
    @WrapOperation(method = { "updateTargetedEntity" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/ProjectileUtil;raycast(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/util/math/Box;Ljava/util/function/Predicate;D)Lnet/minecraft/util/hit/EntityHitResult;") })
    private EntityHitResult canNotHitAstralPlaneEntity(final Entity entity, final Vec3d min, final Vec3d max, final Box box, final Predicate<Entity> predicate, final double distance, final Operation<EntityHitResult> original) {
        final EntityHitResult result = (EntityHitResult)original.call(new Object[] { entity, min, max, box, predicate, distance });
        if (result != null && result.getEntity() != null) {
            final Entity getEntity = result.getEntity();
            if (getEntity instanceof ShadeEntity) {
                final ShadeEntity shadeEntity = (ShadeEntity)getEntity;
                if (shadeEntity.isInAstralPlane()) {
                    return EuphoricsClient.isInAstralPlane ? result : null;
                }
            }
            if (EuphoriaComponent.isOutOfAstralPlane(result.getEntity())) {
                return result;
            }
            return EuphoricsClient.isInAstralPlane ? result : null;
        }
        return result;
    }
}
