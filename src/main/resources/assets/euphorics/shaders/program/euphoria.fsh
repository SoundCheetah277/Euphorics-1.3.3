#version 150
// Noise: @patriciogv - 2015

uniform sampler2D DiffuseSampler;

uniform sampler2D Stars;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 InSize;
uniform vec2 OutSize;

uniform float UTime;
uniform float Alpha;

out vec4 fragColor;

vec4 permute(vec4 x){ return mod(((x*34.0)+1.0)*x, 289.0); }
vec4 taylorInvSqrt(vec4 r){ return 1.79284291400159 - 0.85373472095314 * r; }

float snoise(vec3 v){
    const vec2  C = vec2(1.0/6.0, 1.0/3.0);
    const vec4  D = vec4(0.0, 0.5, 1.0, 2.0);

    // First corner
    vec3 i  = floor(v + dot(v, C.yyy));
    vec3 x0 =   v - i + dot(i, C.xxx);

    // Other corners
    vec3 g = step(x0.yzx, x0.xyz);
    vec3 l = 1.0 - g;
    vec3 i1 = min(g.xyz, l.zxy);
    vec3 i2 = max(g.xyz, l.zxy);

    //  x0 = x0 - 0. + 0.0 * C
    vec3 x1 = x0 - i1 + 1.0 * C.xxx;
    vec3 x2 = x0 - i2 + 2.0 * C.xxx;
    vec3 x3 = x0 - 1. + 3.0 * C.xxx;

    // Permutations
    i = mod(i, 289.0);
    vec4 p = permute(permute(permute(
    i.z + vec4(0.0, i1.z, i2.z, 1.0))
    + i.y + vec4(0.0, i1.y, i2.y, 1.0))
    + i.x + vec4(0.0, i1.x, i2.x, 1.0));

    // Gradients
    // ( N*N points uniformly over a square, mapped onto an octahedron.)
    float n_ = 1.0/7.0;// N=7
    vec3  ns = n_ * D.wyz - D.xzx;

    vec4 j = p - 49.0 * floor(p * ns.z *ns.z);//  mod(p,N*N)

    vec4 x_ = floor(j * ns.z);
    vec4 y_ = floor(j - 7.0 * x_);// mod(j,N)

    vec4 x = x_ *ns.x + ns.yyyy;
    vec4 y = y_ *ns.x + ns.yyyy;
    vec4 h = 1.0 - abs(x) - abs(y);

    vec4 b0 = vec4(x.xy, y.xy);
    vec4 b1 = vec4(x.zw, y.zw);

    vec4 s0 = floor(b0)*2.0 + 1.0;
    vec4 s1 = floor(b1)*2.0 + 1.0;
    vec4 sh = -step(h, vec4(0.0));

    vec4 a0 = b0.xzyw + s0.xzyw*sh.xxyy;
    vec4 a1 = b1.xzyw + s1.xzyw*sh.zzww;

    vec3 p0 = vec3(a0.xy, h.x);
    vec3 p1 = vec3(a0.zw, h.y);
    vec3 p2 = vec3(a1.xy, h.z);
    vec3 p3 = vec3(a1.zw, h.w);

    //Normalise gradients
    vec4 norm = taylorInvSqrt(vec4(dot(p0, p0), dot(p1, p1), dot(p2, p2), dot(p3, p3)));
    p0 *= norm.x;
    p1 *= norm.y;
    p2 *= norm.z;
    p3 *= norm.w;

    // Mix final noise value
    vec4 m = max(0.6 - vec4(dot(x0, x0), dot(x1, x1), dot(x2, x2), dot(x3, x3)), 0.0);
    m = m * m;
    return 42.0 * dot(m*m, vec4(dot(p0, x0), dot(p1, x1),
    dot(p2, x2), dot(p3, x3)));
}

mat2 rotateMat(float radians) {
    return mat2(
        cos(radians), -sin(radians),
        sin(radians), cos(radians)
    );
}

void main() {
    vec4 color = texture(DiffuseSampler, texCoord);

    vec2 st = gl_FragCoord.xy / OutSize.xy;

    float bgR = pow(snoise(vec3(st * 5 + vec2(4000), UTime * 0.1)) * 0.25 + 0.4, 3);
    float bgG = pow(snoise(vec3(st * 5 + vec2(5000), UTime * 0.1)) * 0.25 + 0.4, 3);
    float bgB = snoise(vec3(st * 3 + vec2(6000), UTime * 0.1)) * 0.1 + 0.2 + bgG * 0.5;
    vec3 backgroundColor = vec3(bgR, bgG, bgB);

    float r = 75. / 1000.;

    float d = Alpha * min(max(2 * min(1., pow((1.5 + r * 5) * distance(vec2(0.5, 0.5), st) - 0.1, 3)) + snoise(vec3(st * 6 + vec2(3000), UTime * 0.25)) * 0.5 - 0.5, 0.3 * r), 1.);
    color.rgb = mix(color.rgb, backgroundColor, d);

    vec3 starColor = backgroundColor * vec3(1.5, 1.5, 1);

    int max = 6;
    for(int i = 0; i < max; i++) {
        float p = (i / (max + 0.));
        color.rgb += texture(Stars, gl_FragCoord.xy / OutSize.x * rotateMat(1.570795) + vec2(i * 3.14159, UTime * 0.025 * (p + 0.05))).rgb * starColor * d;
    }

    fragColor = vec4(color.rgb, 1.);
}
