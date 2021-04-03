#version 400 core

in vec2 passTextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoord;

out vec4 outColor;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform bool celEnable;
uniform float celLevel;

const int pcfLevel = 2;
const float totalTexels = (pcfLevel * 2.0 + 1.0) * (pcfLevel * 2.0 + 1.0);

void main() {
    float mapSize = 4096.0;
    float texelSize = 1.0 / mapSize;
    float total = 0.0;

    for (int x = -pcfLevel; x <= pcfLevel; x++) {
        for (int y = -pcfLevel; y <= pcfLevel; y++) {
            float objectNearestLight = texture(shadowMap, shadowCoord.xy + vec2(x, y) * texelSize).r;
            if (shadowCoord.z > objectNearestLight + 0.002)
                total += 1.0;
        }
    }

    total /= totalTexels;
    float lightFactor = 1.0 - (total * shadowCoord.w);

    vec4 blendMapColor = texture(blendMap, passTextureCoord);
    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
    vec2 tiledCoord = passTextureCoord * 40.0;
    vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoord) * backTextureAmount;
    vec4 rTextureColor = texture(rTexture, tiledCoord) * blendMapColor.r;
    vec4 gTextureColor = texture(gTexture, tiledCoord) * blendMapColor.g;
    vec4 bTextureColor = texture(bTexture, tiledCoord) * blendMapColor.b;
    vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;

    vec3 unitSurfaceNormal = normalize(surfaceNormal);
    vec3 unitToCameraVector = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i=0; i<4; i++) {
        float distance = length(toLightVector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        vec3 unitToLightVector = normalize(toLightVector[i]);
        float nDot = dot(unitSurfaceNormal, unitToLightVector);
        float brightness = max(nDot, 0.0);
        vec3 lightDirection = -unitToLightVector;
        vec3 reflectedLight = reflect(lightDirection, unitSurfaceNormal);
        float specularFactor = dot(reflectedLight, unitToCameraVector);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);
        if (celEnable) {
            float level = floor(brightness * celLevel);
            brightness = level / celLevel;
            level = floor(dampedFactor * celLevel);
            dampedFactor = level / celLevel;
        }
        totalDiffuse += (brightness * lightColor[i]) / attFactor;
        totalSpecular += (dampedFactor * reflectivity * lightColor[i]) / attFactor;
    }

    totalDiffuse = max(totalDiffuse * lightFactor, 0.2);

    outColor = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}
