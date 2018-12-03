package game.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;

import cnge.graphics.Shader;

public class TextShader extends Shader {
	
	private int texLoc;
	private int colorLoc;
	private int outlineLoc;
	private int dimsLoc;
	
	public TextShader() {
		super("res/shaders/text/txt2d.vs", "res/shaders/text/txt2d.fs");
		
		texLoc = glGetUniformLocation(program, "frame");
		colorLoc = glGetUniformLocation(program, "inColor");
		outlineLoc = glGetUniformLocation(program, "outline");
		dimsLoc = glGetUniformLocation(program, "dims");
	}
	
	public void setUniforms(float x, float y, float w, float h, float r, float g, float b, float a) {
		glUniform4f(texLoc, x, y, w, h);
		glUniform4f(colorLoc, r, g, b, a);
	}
	
	public void setOutline(float o, float w, float h) {
		glUniform1f(outlineLoc, o);
		glUniform2f(dimsLoc, w, h);
	}
	
}