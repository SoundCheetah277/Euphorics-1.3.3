package com.eightsidedsquare.euphorics.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import com.eightsidedsquare.euphorics.common.entity.*;
import com.eightsidedsquare.euphorics.client.model.*;
import software.bernie.geckolib3.model.*;
import software.bernie.geckolib3.renderers.geo.*;
import com.eightsidedsquare.euphorics.client.*;
import com.eightsidedsquare.euphorics.cca.*;
import java.util.*;
import net.minecraft.*;
import com.eightsidedsquare.euphorics.core.*;

public class ShadeEntityRenderer extends GeoEntityRenderer<ShadeEntity>
{
    public ShadeEntityRenderer(final EntityRendererFactory.Context ctx) {
        super(ctx, new ShadeEntityModel());
        this.addLayer(new ShadeLayerRenderer(this));
    }

    public boolean shouldRender(final ShadeEntity entity, final Frustum frustum, final double x, final double y, final double z) {
        final Optional<EuphoriaComponent> component = EuphoricsEntityComponents.EUPHORIA.maybeGet(MinecraftClient.getInstance().player);
        return super.shouldRender(entity, frustum, x, y, z) && (EuphoricsClient.isInAstralPlane || (!entity.isInAstralPlane() && component.isPresent() && component.get().getShadeAggro() > 0));
    }

    public RenderLayer getRenderType(final ShadeEntity entity, final float partialTicks, final MatrixStack stack, final VertexConsumerProvider renderTypeBuffer, final VertexConsumer vertexBuilder, final int packedLightIn, final Identifier textureLocation) {
        final RenderLayer layer = RenderLayer.getEntityCutoutNoCull(this.getTextureResource(entity));
        if (!entity.glitching) {
            return layer;
        }
        EuphoricsClient.GLITCH.time.set(EuphoricsClient.ticks + partialTicks + entity.getId());
        return EuphoricsClient.GLITCH.shader.getRenderLayer(layer);
    }

    static class ShadeLayerRenderer extends GeoLayerRenderer<ShadeEntity>
    {
        private static final Identifier OVERLAY;

        public ShadeLayerRenderer(final IGeoRenderer<ShadeEntity> entityRendererIn) {
            super(entityRendererIn);
        }

        public RenderLayer getRenderType(final Identifier texture) {
            return RenderLayer.getEntityTranslucent(texture);
        }

        public void render(final MatrixStack matrixStackIn, final VertexConsumerProvider bufferIn, final int packedLightIn, final ShadeEntity entity, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch) {
            final int overlay = OverlayTexture.getUv(0.0f, entity.hurtTime > 0 || entity.deathTime > 0);
            this.getRenderer().render(this.getEntityModel().getModel(this.getEntityModel().getModelResource(entity)), entity, partialTicks, this.getRenderType(ShadeLayerRenderer.OVERLAY), matrixStackIn, bufferIn, bufferIn.getBuffer(this.getRenderType(ShadeLayerRenderer.OVERLAY)), 15728880, overlay, 1.0f, 1.0f, 1.0f, entity.glitching ? 0.5f : 1.0f);
        }

        static {
            OVERLAY = EuphoricsMod.id("textures/entity/shade/shade_eyes.png");
        }
    }
}
