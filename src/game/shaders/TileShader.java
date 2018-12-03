package game.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;

import cnge.graphics.Shader;

public class TileShader extends Shader {
	
	private int texLoc;
	private int colorLoc;
	
	public TileShader() {
		super("res/shaders/tile/til2d.vs", "res/shaders/tile/til2d.fs");
		
		texLoc = glGetUniformLocation(program, "frame");
		colorLoc = glGetUniformLocation(program, "inColor");
	}
	
	public void setUniforms(float x, float y, float w, float h, float r, float g, float b, float a) {
		glUniform4f(texLoc, x, y, w, h);
		glUniform4f(colorLoc, r, g, b, a);
	}
	
}