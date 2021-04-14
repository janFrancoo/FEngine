#version 400

in vec3 textureCoord;

out vec4 outColor;

uniform samplerCube cubeMap;
uniform samplerCube cubeMap2;
uniform float blendFactor;
uniform vec3 fogColor;
uniform bool celEnable;
uniform float celLevel;

const float lowerLimit = 0.0;
const float upperLimit = 30.0;

void main() {
    vec4 texture1 = texture(cubeMap, textureCoord);
    vec4 texture2 = texture(cubeMap2, textureCoord);
    vec4 finalColor = mix(texture1, texture2, blendFactor);

    if (celEnable) {
        float amount = (finalColor.r + finalColor.g + finalColor.b) / 3.0;
        amount = floor(amount * celLevel) / celLevel;
        finalColor.rgb = amount * fogColor;
    }

    float factor = (textureCoord.y - lowerLimit) / (upperLimit - lowerLimit);
    factor = clamp(factor, 0.0, 1.0);
    outColor = mix(vec4(fogColor, 1.0), finalColor, factor);
}
