package com.eightsidedsquare.euphorics.client;

import net.fabricmc.api.*;
import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.entity.EntityType;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.MathHelper;
import com.eightsidedsquare.euphorics.client.shader.*;
import net.fabricmc.fabric.api.blockrenderlayer.v1.*;
import com.eightsidedsquare.euphorics.client.renderer.*;
import net.fabricmc.fabric.api.client.rendering.v1.*;
import net.fabricmc.fabric.api.client.particle.v1.*;
import com.eightsidedsquare.euphorics.client.particle.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import ladysnake.satin.api.event.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;

public class EuphoricsClient implements ClientModInitializer
{
    public static final EuphoriaShader EUPHORIA;
    public static final AstralPlaneShader ASTRAL_PLANE;
    public static final BlinkShader BLINK;
    public static final EntityGlitchShader GLITCH;
    public static final AstralIconShader ASTRAL_ICON;
    public static final AstralIconShader RED_ASTRAL_ICON;
    public static int ticks;
    private static int euphoriaTicks;
    private static int alpha;
    private static boolean wasInAstralPlane;
    public static boolean isInAstralPlane;
    private static int blinkTicks;

    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), EuphoricsBlocks.WITHERED_SPORE_BLOSSOM);
        EntityRendererRegistry.register(EuphoricsEntities.SHADE, ShadeEntityRenderer::new);
        ParticleFactoryRegistry.getInstance().register(EuphoricsParticles.RAW_ENDUST, StarParticle.RawEndustFactory::new);
        ParticleFactoryRegistry.getInstance().register(EuphoricsParticles.ASTRAL_PLANE, StarParticle.AstralPlaneFactory::new);
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            EuphoricsClient.ticks += (client.isPaused() ? 0 : 1);
            if (client.player != null) {
                final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.get(client.player);
                EuphoricsClient.euphoriaTicks = component.getTotalEuphoriaTicks();
                EuphoricsClient.alpha = Math.min(component.getLastUse() - EuphoricsClient.euphoriaTicks, EuphoricsClient.euphoriaTicks);
                EuphoricsClient.wasInAstralPlane = EuphoricsClient.isInAstralPlane;
                EuphoricsClient.isInAstralPlane = (component.isInAstralPlane() || component.getForcedAstralPlaneTicks() > 0);
                if (EuphoricsClient.isInAstralPlane != EuphoricsClient.wasInAstralPlane) {
                    EuphoricsClient.blinkTicks = 8;
                    client.getSoundManager().play(PositionedSoundInstance.master(EuphoricsSounds.EVENT_BLINK, 1.0f));
                }
                if (EuphoricsClient.blinkTicks > 0) {
                    --EuphoricsClient.blinkTicks;
                }
            }
        }));
        ShaderEffectRenderCallback.EVENT.register((tickDelta -> {
            final float time = (EuphoricsClient.ticks + tickDelta) * 0.05f;
            if (EuphoricsClient.isInAstralPlane) {
                EuphoricsClient.ASTRAL_PLANE.time.set(time);
                EuphoricsClient.ASTRAL_PLANE.shader.render(tickDelta);
            }
            if (EuphoricsClient.euphoriaTicks > 0) {
                EuphoricsClient.EUPHORIA.alpha.set(MathHelper.clamp((EuphoricsClient.alpha + tickDelta) * 0.01f, 0.0f, 1.0f));
                EuphoricsClient.EUPHORIA.time.set(time);
                EuphoricsClient.EUPHORIA.starsSampler.set(MinecraftClient.getInstance().getTextureManager().getTexture(EuphoricsClient.EUPHORIA.STARS_TEXTURE_PATH));
                EuphoricsClient.EUPHORIA.shader.render(tickDelta);
            }
            if (EuphoricsClient.blinkTicks > 0) {
                EuphoricsClient.BLINK.time.set((8 - EuphoricsClient.blinkTicks + tickDelta) / 8.0f);
                EuphoricsClient.BLINK.shader.render(tickDelta);
            }
        }));
    }

    static {
        EUPHORIA = new EuphoriaShader();
        ASTRAL_PLANE = new AstralPlaneShader();
        BLINK = new BlinkShader();
        GLITCH = new EntityGlitchShader();
        ASTRAL_ICON = new AstralIconShader(false);
        RED_ASTRAL_ICON = new AstralIconShader(true);
    }
}
