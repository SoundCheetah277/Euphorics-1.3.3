#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float UTime;
uniform vec2 Size;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;
in vec4 normal;

out vec4 fragColor;

float random (vec2 st) {
    return fract(sin(dot(st.xy,
    vec2(12.9898,78.233)))*
    43758.5453123);
}

void main() {
    if (texture(Sampler0, texCoord0).a < 0.1) {
        discard;
    }
    vec2 coord = vec2(floor((texCoord0.x + 0.005 * sin(texCoord0.y * 64 + UTime * 0.5)) * 64.) / 64., floor(texCoord0.y * 1280.) / 1280.);
    vec4 color = vec4(vec3(0.75, 0., 1.) * random(coord * UTime) + vec3(0, 0.75, 1.) * random(coord * UTime * 2), 1);
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
