#version 400 core

in vec2 textureCoord1;
in vec2 textureCoord2;
in float blend;

out vec4 outColor;

uniform sampler2D particleTexture;

void main() {
    vec4 color1 = texture(particleTexture, textureCoord1);
    vec4 color2 = texture(particleTexture, textureCoord2);
    outColor = mix(color1, color2, blend);
}
