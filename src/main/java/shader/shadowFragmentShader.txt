#version 400 core

in vec2 outTextureCoord;

out vec4 outColor;

uniform sampler2D modelTexture;

void main() {
    float alpha = texture(modelTexture, outTextureCoord).a;
    if (alpha < 0.5)
        discard;

    outColor = vec4(1.0);
}
