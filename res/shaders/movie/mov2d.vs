#version 330 core

layout (location = 0) in vec3 vertices;

uniform vec2 dims;
uniform mat4 mvp;

out vec2 position;

void main() {
    gl_Position = mvp*vec4(vertices, 1);
    
    position = vec2(vertices.x, vertices.y);
}