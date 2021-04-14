#version 400 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 passTextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;
out vec4 shadowCoord;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];
uniform float density;
uniform float gradient;
uniform vec4 clippingPlane;
uniform mat4 toShadowMapSpace;

const float shadowDistance = 150.0;
const float transitionDistance = 10.0;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    shadowCoord = toShadowMapSpace * worldPosition;
    gl_ClipDistance[0] = dot(worldPosition, clippingPlane);
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCamera;
    passTextureCoord = textureCoord;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    for (int i=0; i<4; i++)
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow(distance * density, gradient));
    visibility = clamp(visibility, 0.0, 1.0);

    distance = distance - (shadowDistance - transitionDistance);
    distance = distance / transitionDistance;
    shadowCoord.w = clamp(1.0 - distance, 0.0, 1.0);
}
