package com.eightsidedsquare.euphorics.client.shader;

import ladysnake.satin.api.managed.uniform.*;
import ladysnake.satin.api.managed.*;
import net.minecraft.client.render.VertexFormats;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;

public class EntityGlitchShader
{
    public final ManagedCoreShader shader;
    public final Uniform1f time;

    public EntityGlitchShader() {
        this.shader = ShaderEffectManager.getInstance().manageCoreShader(EuphoricsMod.id("rendertype_entity_glitch"), VertexFormats.POSITION_COLOR_TEXTURE_OVERLAY_LIGHT_NORMAL);
        this.time = this.shader.findUniform1f("UTime");
    }
}
