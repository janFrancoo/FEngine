#version 400 core

in vec2 position;

out vec4 clipSpaceCoord;
out vec2 textureCoord;
out vec3 toCameraVector;
out vec3 fromLightVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;
uniform float density;
uniform float gradient;

const float tile = 4.0;

void main() {
    vec4 worldPosition = transformationMatrix * vec4(position.x, 0.0, position.y, 1.0);
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	clipSpaceCoord = projectionMatrix * positionRelativeToCamera;
	gl_Position = clipSpaceCoord;
	textureCoord = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tile;
	toCameraVector = cameraPosition - worldPosition.xyz;
	fromLightVector = worldPosition.xyz - lightPosition;
	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow(distance * density, gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}
