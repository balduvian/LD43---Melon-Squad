package game.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniform2f;

import cnge.graphics.Shader;

public class StenShader extends Shader {
	
	private int positionloc;
	private int dimsLoc;
	private int colorLoc;
	
	public StenShader() {
		super("res/shaders/sten/stn2d.vs", "res/shaders/sten/stn2d.fs");
		
		positionloc = glGetUniformLocation(program, "position");
		dimsLoc = glGetUniformLocation(program, "dims");
		colorLoc = glGetUniformLocation(program, "inColor");
	}
	
	public void setUniforms(float x, float y, float w, float h, float cw, float ch, float r, float g, float b, float a) {
		glUniform2f(positionloc, x, y);
		glUniform4f(dimsLoc, w, h, cw, ch);
		glUniform4f(colorLoc, r, g, b, a);
	}
	
}