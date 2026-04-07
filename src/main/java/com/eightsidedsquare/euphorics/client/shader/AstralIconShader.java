package com.eightsidedsquare.euphorics.client.shader;

import ladysnake.satin.api.managed.uniform.*;
import ladysnake.satin.api.managed.*;
import net.minecraft.client.render.VertexFormats;
import com.eightsidedsquare.euphorics.core.*;
import net.minecraft.*;

public class AstralIconShader
{
    public final ManagedCoreShader shader;
    public final Uniform1f time;

    public AstralIconShader(final boolean red) {
        this.shader = ShaderEffectManager.getInstance().manageCoreShader(EuphoricsMod.id(red ? "red_astral_icon" : "astral_icon"), VertexFormats.POSITION_TEXTURE);
        this.time = this.shader.findUniform1f("UTime");
    }
}
