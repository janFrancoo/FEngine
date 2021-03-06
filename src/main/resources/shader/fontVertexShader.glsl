#version 400 core

in vec2 position;
in vec2 textureCoord;

out vec2 passTextureCoord;

uniform vec2 translation;

void main() {
    gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
    passTextureCoord = textureCoord;
}
