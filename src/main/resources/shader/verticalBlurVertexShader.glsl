#version 400 core

in vec2 position;

out vec2 blurTextureCoord[11];

uniform float targetHeight;

void main() {
    gl_Position = vec4(position, 0.0, 1.0);
    vec2 centerTexCoord = position * 0.5 + 0.5;

    float pixelSize = 1.0 / targetHeight;

    for (int i = -5; i < 5; i++)
        blurTextureCoord[i + 5] = centerTexCoord + vec2(0.0, pixelSize * i);
}
