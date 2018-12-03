#version 330 core

uniform float seed;

in vec2 position;

out vec4 color;

float random(float x, float y){
    return fract(sin(dot(vec2(x, y), vec2(12.9898,78.233))) * 43758.5453);
}

void main(){
	
	float x = position.x;
	float y = position.y;
	
	float ret = 1 - ( (pow((x - 0.5), 2) + pow((y - 0.5), 2)) / 0.25 );
	
	ret = pow(ret, 2);
	
	color = vec4(ret, ret, ret, 1);
	
}