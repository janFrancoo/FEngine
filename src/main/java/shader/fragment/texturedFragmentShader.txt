#version 400 core

in vec2 passTextureCoord;

out vec4 outColor;

uniform sampler2D textureSampler;

void main() {
    outColor = texture(textureSampler, passTextureCoord);
}
