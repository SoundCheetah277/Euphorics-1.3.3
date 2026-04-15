package com.eightsidedsquare.euphorics.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.FallingBlockEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.*;
import net.minecraft.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ FallingBlockEntity.class })
public abstract class FallingBlockEntityMixin extends Entity
{
    public FallingBlockEntityMixin(final EntityType<?> type, final World world) {
        super(type, world);
    }

    @Inject(method = { "tick" }, at = { @At("HEAD") })
    public void tick(final CallbackInfo ci) {
        (EuphoricsEntityComponents.REVERSE_GRAVITY.get(this)).tick();
    }
}
