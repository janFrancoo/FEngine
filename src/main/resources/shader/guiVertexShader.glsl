#version 400 core

in vec2 position;

out vec2 passTextureCoord;

uniform mat4 transformationMatrix;

void main() {
    gl_Position = transformationMatrix * vec4(position, 0.0, 1.0);
    passTextureCoord = vec2((position.x + 1.0) / 2.0, 1 - (position.y + 1.0) / 2.0);
}
