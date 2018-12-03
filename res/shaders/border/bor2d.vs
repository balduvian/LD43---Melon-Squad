#version 330 core

layout (location = 0) in vec3 vertices;

uniform mat4 mvp;
uniform vec4 fillColor;
uniform vec4 borderColor;
uniform float offset;
uniform float borderWidth;
uniform float windowWidth;
uniform float windowHeight;
uniform float left;
uniform float up;
uniform float width;
uniform float height;

out vec4 outFillColor;
out vec4 outBorderColor;
out float outOffset;
out float outBorderWidth;
out float outLeft;
out float outUp;
out float outWidth;
out float outHeight;

void main() {

	outFillColor = fillColor;
	outBorderColor = borderColor;
	if(offset < 0.5) {
	
		float centerx = ((left + width / 2) / windowWidth ) * 2 - 1;
		float centery = ((up + height / 2) / windowHeight ) * 2 - 1;
		
		float xScale = 1 + ( (1 / width) * (borderWidth) );
		float yScale = 1 + ( (1 / height) * (borderWidth) );
		
		gl_Position = ( mvp * vec4(vertices, 1) - vec4(centerx, -centery, 0, 0) ) * vec4(xScale, yScale, 1, 1) + vec4(centerx, -centery, 0, 0);
	}else if(offset < 1.5){
    	gl_Position = mvp*vec4(vertices, 1);
    }else {
    	gl_Position = mvp*vec4(vertices, 1);
    }
    outOffset = offset;
    outBorderWidth = borderWidth;
    outLeft = left;
    outUp = up;
    outWidth = width;
    outHeight = height;
    
}