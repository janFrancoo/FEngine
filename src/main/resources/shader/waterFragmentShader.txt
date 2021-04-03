#version 400 core

in vec4 clipSpaceCoord;
in vec2 textureCoord;
in vec3 toCameraVector;
in vec3 fromLightVector;
in float visibility;

out vec4 outColor;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform sampler2D depthMap;
uniform float moveFactor;
uniform vec3 lightColor;
uniform vec3 skyColor;

const float waveStrength = 0.04;
const float shineDamper = 20.0;
const float reflectivity = 0.5;
const float near = 0.1;
const float far = 1000.0;

void main() {
    vec2 ndc = (clipSpaceCoord.xy / clipSpaceCoord.w) / 2.0 + 0.5;

    vec2 reflectionTextureCoord = vec2(ndc.x, -ndc.y);
    vec2 refractionTextureCoord = vec2(ndc.x, ndc.y);

    float depth = texture(depthMap, refractionTextureCoord).r;
    float floorDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    depth = gl_FragCoord.z;
    float waterDistance = 2.0 * near * far / (far + near - (2.0 * depth - 1.0) * (far - near));
    float waterDepth = floorDistance - waterDistance;

    vec2 distortedTexCoord = texture(dudvMap, vec2(textureCoord.x + moveFactor, textureCoord.y)).rg * 0.1;
	distortedTexCoord = textureCoord + vec2(distortedTexCoord.x, distortedTexCoord.y + moveFactor);
	vec2 totalDistortion = (texture(dudvMap, distortedTexCoord).rg * 2.0 - 1.0) * waveStrength * clamp(waterDepth / 20.0, 0.0, 1.0);;

    reflectionTextureCoord += totalDistortion;
    reflectionTextureCoord.x = clamp(reflectionTextureCoord.x, 0.001, 0.999);
    reflectionTextureCoord.y = clamp(reflectionTextureCoord.y, -0.999, -0.001);
    refractionTextureCoord += totalDistortion;
    refractionTextureCoord = clamp(refractionTextureCoord, 0.001, 0.999);

    vec4 reflectionColor = texture(reflectionTexture, reflectionTextureCoord);
    vec4 refractionColor = texture(refractionTexture, refractionTextureCoord);

    vec4 normalMapColor = texture(normalMap, distortedTexCoord);
    vec3 normal = vec3(normalMapColor.r * 2.0 - 1, normalMapColor.b * 3.0, normalMapColor.g * 2.0 - 1);
    normal = normalize(normal);

    vec3 viewVector = normalize(toCameraVector);
    float refractiveFactor = dot(viewVector, normal);

    vec3 reflectedLight = reflect(normalize(fromLightVector), normal);
    float specular = max(dot(reflectedLight, viewVector), 0.0);
    specular = pow(specular, shineDamper);
    vec3 specularHighlights = lightColor * specular * reflectivity * clamp(waterDepth / 5.0, 0.0, 1.0);

	outColor = mix(reflectionColor, refractionColor, refractiveFactor);
	outColor = mix(outColor, vec4(0.0, 0.3, 0.5, 1.0), 0.2) + vec4(specularHighlights, 0);
    outColor = mix(vec4(skyColor, 1.0), outColor, visibility);
	outColor.a = clamp(waterDepth / 5.0, 0.0, 1.0);
}
