#version 400 core

in vec3 position;
in vec2 textureCoord;
in vec3 normal;

out vec2 passTextureCoord;
out vec3 surfaceNormal;
out vec3 toLightVector[4];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4];
uniform float fakeLight;
uniform float density;
uniform float gradient;
uniform float textureRows;
uniform vec2 textureOffset;
uniform vec4 clippingPlane;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_ClipDistance[0] = dot(worldPosition, clippingPlane);
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;
    gl_Position = projectionMatrix * positionRelativeToCamera;
    passTextureCoord = textureCoord / textureRows + textureOffset;

    surfaceNormal = (transformationMatrix * vec4(fakeLight > 0.5 ? vec3(0.0, 1.0, 0.0) : normal, 0.0)).xyz;
    for (int i=0; i<4; i++)
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow(distance * density, gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
