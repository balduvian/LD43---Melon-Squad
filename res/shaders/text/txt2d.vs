#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 iTexCoords;

uniform mat4 mvp;
uniform vec4 frame; 
uniform vec4 inColor;

out vec2 texCoords;
out vec4 outColor;

void main() {
	
	texCoords = iTexCoords * frame.xy + frame.zw;
    gl_Position = mvp*vec4(vertices, 1);
    outColor = inColor;
  
    
}