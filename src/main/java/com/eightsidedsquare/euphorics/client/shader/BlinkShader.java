package com.eightsidedsquare.euphorics.client.shader;

import ladysnake.satin.api.managed.uniform.*;
import ladysnake.satin.api.managed.*;
import com.eightsidedsquare.euphorics.core.*;

public class BlinkShader
{
    public final ManagedShaderEffect shader;
    public final Uniform1f time;

    public BlinkShader() {
        this.shader = ShaderEffectManager.getInstance().manage(EuphoricsMod.id("shaders/post/blink.json"));
        this.time = this.shader.findUniform1f("BlinkTime");
    }
}
