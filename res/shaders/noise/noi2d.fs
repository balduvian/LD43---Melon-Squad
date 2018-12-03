#version 330 core

uniform vec4 inColor;

in float x;
in float y;
in float width;
in float height;

out vec4 color;

float random(float x, float y){
    return fract(sin(dot(vec2(x, y), vec2(12.9898,78.233))) * 43758.5453);
}

void main(){
	float insideX = mod(x, 1);
	float insideY = mod(y, 1);
	
	int middleX = int(x);
	int middleY = int(y);
	
	//now we go in a full 9 3x3 grid around the current square lookin for more dots
	//i offset goes from -1 to 1
	
	float ret = 0;
	float root2 = sqrt(2); 
	
	for(int i = -1; i < 2; ++i) {
		for(int j = -1; j < 2; ++j) {
			
			float dotX = random(middleX + i, middleY + j);
			float dotY = random(middleX + i + 6.2342, middleY + j - 7.342211);
			
			float distX = (insideX - (i + dotX));

			float distY = (insideY - (j + dotY));
			
			float tot = (1 - (pow(distX, 2) + pow(distY, 2)) ) * 0.5;
			if(tot < 0) {
				tot = 0;
			}
			
			ret += tot;
		}
	}
	if(ret > 1) {
		ret = 1;
	}
	
	color = vec4(ret, ret, ret, 1);
	color *= inColor;
	
}