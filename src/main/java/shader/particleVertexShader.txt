#version 400 core

in vec2 position;
in mat4 modelViewMatrix;
in vec4 textureOffsets;
in float blendFactor;

out vec2 textureCoord1;
out vec2 textureCoord2;
out float blend;

uniform mat4 projectionMatrix;
uniform float rows;

void main() {
    vec2 textureCoord = position + vec2(0.5, 0.5);
    textureCoord.y = 1.0 - textureCoord.y;
    textureCoord /= rows;
    textureCoord1 = textureCoord + textureOffsets.xy;
    textureCoord2 = textureCoord + textureOffsets.zw;
    blend = blendFactor;
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);
}
