#version 330 core

layout (location = 0) in vec3 vertices;

uniform mat4 mvp;

void main() {
    gl_Position = mvp*vec4(vertices, 1);
}