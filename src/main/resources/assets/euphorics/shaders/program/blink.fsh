#version 150

uniform sampler2D DiffuseSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float BlinkTime;

out vec4 fragColor;


void main() {

    vec4 color = texture(DiffuseSampler, texCoord);

    float t = BlinkTime + 0.45;

    if(distance(texCoord, vec2(0.5, 0.)) < t && distance(texCoord, vec2(0.5, 1.)) < t)
        fragColor = vec4(color.rgb, 1.);
    else
        fragColor = vec4(color.rgb * BlinkTime, 1);
}
