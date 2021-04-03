#version 400 core

in vec3 position;
in vec2 textureCoord;

out vec2 outTextureCoord;

uniform mat4 mvpMatrix;

void main() {
    gl_Position = mvpMatrix * vec4(position, 1.0);
    outTextureCoord = textureCoord;
}
