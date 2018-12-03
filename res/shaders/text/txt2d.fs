#version 330 core

uniform vec4 inColor;

uniform sampler2D sampler;
uniform float outline;
uniform vec2 dims;

in vec2 texCoords;
in vec4 outColor;

out vec4 color;

void main(){
	color = texture(sampler, texCoords);
	if(color.x < 0.5) {
		if(outline > 0.5) {
			bool found = false;
			for(int x = -1; x < 2; ++x) {
				for(int y = -1; y < 2; ++y) {
					if( texture(sampler, vec2(texCoords.x + (x / dims.x), texCoords.y + (y / dims.y) )).x > 0.5) {
						found = true;
						break;
					}
				}
			}
			if(found) {
				color = vec4(0, 0, 0, 1);
			}else {
				discard;
			}
		} else {
			discard;
		}
	}
	color *= outColor;
}