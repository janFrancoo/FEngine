#version 400 core

in vec2 passTextureCoord;
in vec3 surfaceNormal;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 outColor;

uniform sampler2D textureSampler;
uniform sampler2D specularMap;
uniform float useSpecularMap;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;
uniform bool celEnable;
uniform float celLevel;

void main() {
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

    totalDiffuse = max(totalDiffuse, 0.2);

    vec4 textureColor = texture(textureSampler, passTextureCoord);
    if (textureColor.a < 0.5)
        discard;

    if (useSpecularMap > 0.5) {
        vec4 mapInfo = texture(specularMap, passTextureCoord);
        totalSpecular *= mapInfo.r;
    }

    outColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
}
