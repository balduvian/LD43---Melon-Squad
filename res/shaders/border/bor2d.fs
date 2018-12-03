#version 330 core

in vec4 outFillColor;
in vec4 outBorderColor;
in float outOffset;
in float outBorderWidth;
in float outLeft;
in float outUp;
in float outWidth;
in float outHeight;
layout (origin_upper_left) in vec4 gl_FragCoord;

out vec4 color;

//
// draws a 1 pixel wide border inside a box
// with a fill color and a border color
//

void main() {

	float x = gl_FragCoord.x;
	float y = gl_FragCoord.y;
	
	if(x < outLeft + outBorderWidth) 
	{
		color = outBorderColor;
	}
	else if(x > outLeft + outWidth - outBorderWidth)
	{
		color = outBorderColor;
	}
	else if(y < outUp + outBorderWidth)
	{
		color = outBorderColor;
	}
	else if(y > outUp + outHeight - outBorderWidth)
	{
		color = outBorderColor;
	}
	else
	{
		color = outFillColor;
	}
	
	//wouldya look at those brackets	
}