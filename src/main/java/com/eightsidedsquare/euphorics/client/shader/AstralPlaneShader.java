package com.eightsidedsquare.euphorics.client.shader;

import ladysnake.satin.api.managed.uniform.*;
import ladysnake.satin.api.managed.*;
import com.eightsidedsquare.euphorics.core.*;

public class AstralPlaneShader
{
    public final ManagedShaderEffect shader;
    public final Uniform1f time;
    
    public AstralPlaneShader() {
        this.shader = ShaderEffectManager.getInstance().manage(EuphoricsMod.id("shaders/post/astral_plane.json"));
        this.time = this.shader.findUniform1f("UTime");
    }
}
