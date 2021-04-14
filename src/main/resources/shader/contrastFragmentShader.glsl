#version 140

in vec2 textureCoords;

out vec4 outColor;

uniform sampler2D colorTexture;

const float contrast = 0.3;

void main() {
	outColor = texture(colorTexture, textureCoords);
    outColor.rgb = (outColor.rgb - 0.5) * (1.0 + contrast) + 0.5;
}
