package com.eightsidedsquare.euphorics.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import org.spongepowered.asm.mixin.injection.*;
import com.llamalad7.mixinextras.injector.wrapoperation.*;
import com.eightsidedsquare.euphorics.client.*;
import java.util.*;
import com.mojang.blaze3d.systems.*;
import ladysnake.satin.api.managed.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.eightsidedsquare.euphorics.core.*;

@Mixin({ InGameHud.class })
public abstract class InGameHudMixin extends DrawableHelper
{
    @Unique
    private static final Identifier ASTRAL_PLANE_ICONS;

    @Inject(method = { "drawHeart" }, at = { @At("HEAD") }, cancellable = true)
    private void drawAstralPlaneHeart(final MatrixStack matrices, final InGameHud.HeartType type, final int x, final int y, final int v, final boolean blinking, final boolean halfHeart, final CallbackInfo ci) {
        if (!EuphoriaComponent.isOutOfAstralPlane(MinecraftClient.getInstance().cameraEntity)) {
            if (type.equals(InGameHud.HeartType.CONTAINER)) {
                this.drawTexture(matrices, x, y, 25, 0, 9, 9);
            }
            else {
                this.drawAstralIcon(matrices, x, y, halfHeart ? 9 : 0, 0);
            }
            ci.cancel();
        }
    }

    @WrapOperation(method = { "renderStatusBars" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 4) })
    private void drawAstralPlaneShank(final InGameHud hud, final MatrixStack matrices, final int x, final int y, final int u, final int v, final int width, final int height, final Operation<Void> original) {
        if (!EuphoriaComponent.isOutOfAstralPlane(MinecraftClient.getInstance().cameraEntity)) {
            this.drawAstralIcon(matrices, x, y, 0, 9);
        }
        else {
            original.call(hud, matrices, x, y, u, v, width, height);
        }
    }

    @WrapOperation(method = { "renderStatusBars" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 5) })
    private void drawAstralPlaneHalfShank(final InGameHud hud, final MatrixStack matrices, final int x, final int y, final int u, final int v, final int width, final int height, final Operation<Void> original) {
        if (!EuphoriaComponent.isOutOfAstralPlane(MinecraftClient.getInstance().cameraEntity)) {
            this.drawAstralIcon(matrices, x, y, 9, 9);
        }
        else {
            original.call(hud, matrices, x, y, u, v, width, height);
        }
    }

    @WrapOperation(method = { "renderStatusBars" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 3) })
    private void whiteOutlineShankInAstralPlane(final InGameHud hud, final MatrixStack matrices, final int x, final int y, final int u, final int v, final int width, final int height, final Operation<Void> original) {
        if (!EuphoriaComponent.isOutOfAstralPlane(MinecraftClient.getInstance().cameraEntity)) {
            original.call(hud, matrices, x, y, 25, 27, width, height);
        }
        else {
            original.call(hud, matrices, x, y, u, v, width, height);
        }
    }

    @Unique
    private void drawAstralIcon(final MatrixStack matrices, final int x, final int y, final int u, final int v) {
        EuphoricsClient.ASTRAL_ICON.time.set(EuphoricsClient.ticks + MinecraftClient.getInstance().getTickDelta());
        final ManagedCoreShader shader = EuphoricsClient.ASTRAL_ICON.shader;
        Objects.requireNonNull(shader);
        RenderSystem.setShader(shader::getProgram);
        RenderSystem.setShaderTexture(0, InGameHudMixin.ASTRAL_PLANE_ICONS);
        final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        final Matrix4f matrix = matrices.peek().getPositionMatrix();
        final float x2 = (float)(x + 9);
        final float y2 = (float)(y + 9);
        final float z = (float)this.getZOffset();
        final float u2 = u / 256.0f;
        final float u3 = (u + 9) / 256.0f;
        final float v2 = v / 256.0f;
        final float v3 = (v + 9) / 256.0f;
        bufferBuilder.vertex(matrix, (float)x, y2, z).texture(u2, v3).next();
        bufferBuilder.vertex(matrix, x2, y2, z).texture(u3, v3).next();
        bufferBuilder.vertex(matrix, x2, (float)y, z).texture(u3, v2).next();
        bufferBuilder.vertex(matrix, (float)x, (float)y, z).texture(u2, v2).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.setShader( GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InGameHudMixin.GUI_ICONS_TEXTURE);
    }

    @WrapOperation(method = { "renderCrosshair" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 0) })
    private void drawBlinkIndicator(final InGameHud hud, final MatrixStack matrices, final int x, final int y, final int u, final int v, final int width, final int height, final Operation<Void> original) {
        final Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
        if (cameraEntity instanceof PlayerEntity player) {
            final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.getNullable(player);
            if (component != null && component.isReadyForAstralPlaneJump()) {
                RenderSystem.setShaderTexture(0, InGameHudMixin.ASTRAL_PLANE_ICONS);
                this.drawTexture(matrices, x, y, 0, 32, 15, 15);
                RenderSystem.setShaderTexture(0, InGameHudMixin.GUI_ICONS_TEXTURE);
                return;
            }
        }
        original.call(hud, matrices, x, y, u, v, width, height);
    }

    static {
        ASTRAL_PLANE_ICONS = EuphoricsMod.id("textures/gui/astral_plane_icons.png");
    }
}
