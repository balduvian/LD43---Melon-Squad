#version 330 core

layout (location = 0) in vec3 vertices;
layout (location = 1) in vec2 iTexCoords;

uniform mat4 mvp;
uniform vec4 frame;

out vec2 texCoords;

void main() {
	
	texCoords = iTexCoords * frame.xy + frame.zw;
    gl_Position = mvp*vec4(vertices, 1);
    
}