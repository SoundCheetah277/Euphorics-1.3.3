package com.eightsidedsquare.euphorics.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import com.eightsidedsquare.euphorics.cca.*;
import com.mojang.blaze3d.systems.*;
import org.spongepowered.asm.mixin.injection.*;
import com.eightsidedsquare.euphorics.client.*;
import java.util.*;

import com.eightsidedsquare.euphorics.client.shader.*;
import ladysnake.satin.api.managed.*;
import com.eightsidedsquare.euphorics.core.*;

@Mixin({ AbstractInventoryScreen.class })
public abstract class AbstractInventoryScreenMixin<T extends ScreenHandler> extends HandledScreen<T>
{
    @Unique
    private static final Identifier ASTRAL_PLANE_ICONS;

    public AbstractInventoryScreenMixin(final T handler, final PlayerInventory inventory, final Text title) {
        super(handler, inventory, title);
    }

    @Inject(method = { "render" }, at = { @At("TAIL") })
    private void renderShadeAggroBar(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta, final CallbackInfo ci) {
        if (this.client != null) {
            final Entity cameraEntity = MinecraftClient.getInstance().cameraEntity;
            if (cameraEntity instanceof PlayerEntity player) {
                final EuphoriaComponent component = EuphoricsEntityComponents.EUPHORIA.getNullable(player);
                if (component != null && component.getShadeAggro() > 0) {
                    final float progress = component.getShadeAggro() / 20000.0f * 50.0f;
                    RenderSystem.setShaderTexture(0, AbstractInventoryScreenMixin.ASTRAL_PLANE_ICONS);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, Math.min(progress * 50.0f, 1.0f));
                    this.drawTexture(matrices, 7, this.client.getWindow().getScaledHeight() / 2, 32, 0, 21, 68);
                    RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
                    this.drawAstralIcon(matrices, this.client.getWindow().getScaledHeight() / 2 + 59 - (int)progress, 50 - (int)progress, (int)progress, component.getShadeAggro() >= 12000);
                }
            }
        }
    }

    @Unique
    private void drawAstralIcon(final MatrixStack matrices, final int y, final int v, final int height, final boolean red) {
        final AstralIconShader shader = red ? EuphoricsClient.RED_ASTRAL_ICON : EuphoricsClient.ASTRAL_ICON;
        shader.time.set(EuphoricsClient.ticks + MinecraftClient.getInstance().getTickDelta());
        final ManagedCoreShader shader2 = shader.shader;
        Objects.requireNonNull(shader2);
        RenderSystem.setShader(shader2::getProgram);
        RenderSystem.setShaderTexture(0, AbstractInventoryScreenMixin.ASTRAL_PLANE_ICONS);
        final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        final Matrix4f matrix = matrices.peek().getPositionMatrix();
        final float x2 = (float)(15 + 5);
        final float y2 = (float)(y + height);
        final float z = (float)this.getZOffset();
        final float u2 = 54 / 256.0f;
        final float u3 = (54 + 5) / 256.0f;
        final float v2 = v / 256.0f;
        final float v3 = (v + height) / 256.0f;
        bufferBuilder.vertex(matrix, (float) 15, y2, z).texture(u2, v3).next();
        bufferBuilder.vertex(matrix, x2, y2, z).texture(u3, v3).next();
        bufferBuilder.vertex(matrix, x2, (float)y, z).texture(u3, v2).next();
        bufferBuilder.vertex(matrix, (float) 15, (float)y, z).texture(u2, v2).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
        RenderSystem.setShader( GameRenderer::getPositionTexShader);
    }

    static {
        ASTRAL_PLANE_ICONS = EuphoricsMod.id("textures/gui/astral_plane_icons.png");
    }
}
