#version 330 core

layout (location = 0) in vec3 vertices;

uniform vec2 position;
uniform vec4 dims;
uniform mat4 mvp;

out float x;
out float y;
out float width;
out float height;

void main() {
    gl_Position = mvp*vec4(vertices, 1);
    
    width = dims.x;
    height = dims.y;
    float cellWidth = dims.z;
    float cellHeight = dims.z;
    x = (position.x + (vertices.x * width)) / cellWidth;
    y = (position.y + (vertices.y * height)) / cellHeight;
}