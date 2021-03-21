#version 400 core

in vec2 passTextureCoord;

out vec4 outColor;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge = 0.1;

const float borderWidth = 0.7;
const float borderEdge = 0.1;

const vec3 outlineColor = vec3(0.0, 1.0, 0.0);

void main() {
    float distance = 1.0 - texture(fontAtlas, passTextureCoord).a;
    float alpha = 1.0 - smoothstep(width, width + edge, distance);
    float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance);
    float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
    vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);
    outColor = vec4(overallColor, overallAlpha);
}
