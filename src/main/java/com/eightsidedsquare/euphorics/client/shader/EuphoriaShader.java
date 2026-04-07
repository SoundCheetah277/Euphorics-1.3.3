package com.eightsidedsquare.euphorics.client.shader;

import ladysnake.satin.api.managed.uniform.*;
import net.minecraft.*;
import ladysnake.satin.api.managed.*;
import net.minecraft.util.Identifier;
import com.eightsidedsquare.euphorics.core.*;

public class EuphoriaShader
{
    public final ManagedShaderEffect shader;
    public final Uniform1f time;
    public final Uniform1f alpha;
    public final SamplerUniformV2 starsSampler;
    public final Identifier STARS_TEXTURE_PATH;

    public EuphoriaShader() {
        this.shader = ShaderEffectManager.getInstance().manage(EuphoricsMod.id("shaders/post/euphoria.json"));
        this.time = this.shader.findUniform1f("UTime");
        this.alpha = this.shader.findUniform1f("Alpha");
        this.starsSampler = this.shader.findSampler("Stars");
        this.STARS_TEXTURE_PATH = EuphoricsMod.id("textures/misc/stars.png");
    }
}
