package com.eightsidedsquare.euphorics.client.model;

import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.*;
import com.eightsidedsquare.euphorics.common.entity.*;
import net.minecraft.*;
import software.bernie.geckolib3.core.event.predicate.*;
import software.bernie.geckolib3.core.*;
import software.bernie.geckolib3.model.provider.data.*;
import software.bernie.geckolib3.core.processor.*;
import com.eightsidedsquare.euphorics.core.*;

public class ShadeEntityModel extends AnimatedGeoModel<ShadeEntity>
{
    private static final Identifier MODEL;
    private static final Identifier TEXTURE;
    private static final Identifier ANIMATION;

    public void setCustomAnimations(final ShadeEntity animatable, final int instanceId, final AnimationEvent animationEvent) {
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        final IBone head = this.getAnimationProcessor().getBone("head");
        final EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * 0.017453292f);
            head.setRotationY(extraData.netHeadYaw * 0.017453292f);
        }
    }

    public Identifier getModelResource(final ShadeEntity object) {
        return ShadeEntityModel.MODEL;
    }

    public Identifier getTextureResource(final ShadeEntity object) {
        return ShadeEntityModel.TEXTURE;
    }

    public Identifier getAnimationResource(final ShadeEntity animatable) {
        return ShadeEntityModel.ANIMATION;
    }

    static {
        MODEL = EuphoricsMod.id("geo/shade.geo.json");
        TEXTURE = EuphoricsMod.id("textures/entity/shade/shade.png");
        ANIMATION = EuphoricsMod.id("animations/shade.animation.json");
    }
}
