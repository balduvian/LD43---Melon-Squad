package game.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform4f;

import cnge.graphics.Shader;

public class HueShader extends Shader {
	
	private int texLoc;
	private int hueLoc;
	
	public HueShader() {
		super("res/shaders/hue/hue2d.vs", "res/shaders/hue/hue2d.fs");
		
		texLoc = glGetUniformLocation(program, "frame");
		hueLoc = glGetUniformLocation(program, "inHue");
	}
	
	public void setUniforms(float x, float y, float w, float h, float u) {
		glUniform4f(texLoc, x, y, w, h);
		glUniform1f(hueLoc, u);
	}
	
}