#version 330 core

uniform vec4 inColor;

uniform sampler2D sampler;
uniform float hue;

in vec2 texCoords;

out vec4 color;

void main(){
	color = texture(sampler, texCoords);
	color *= outColor;
}