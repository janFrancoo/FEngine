#version 400 core

in vec2 blurTextureCoord[11];

out vec4 outColor;

uniform sampler2D originalTexture;

void main() {
    outColor = vec4(0.0);
    outColor += texture(originalTexture, blurTextureCoord[0]) * 0.0093;
    outColor += texture(originalTexture, blurTextureCoord[1]) * 0.028002;
    outColor += texture(originalTexture, blurTextureCoord[2]) * 0.065984;
    outColor += texture(originalTexture, blurTextureCoord[3]) * 0.121703;
    outColor += texture(originalTexture, blurTextureCoord[4]) * 0.175713;
    outColor += texture(originalTexture, blurTextureCoord[5]) * 0.198596;
    outColor += texture(originalTexture, blurTextureCoord[6]) * 0.175713;
    outColor += texture(originalTexture, blurTextureCoord[7]) * 0.121703;
    outColor += texture(originalTexture, blurTextureCoord[8]) * 0.065984;
    outColor += texture(originalTexture, blurTextureCoord[9]) * 0.028002;
    outColor += texture(originalTexture, blurTextureCoord[10]) * 0.0093;
}
