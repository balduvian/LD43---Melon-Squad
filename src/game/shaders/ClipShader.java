package game.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import org.joml.Matrix4f;

import cnge.graphics.Shader;

public class ClipShader extends Shader {
	
	private int texLoc;
	private int colorLoc;
	private int modelLoc;
	private int planeLoc;
	
	public ClipShader() {
		super("res/shaders/clip/cli2d.vs", "res/shaders/clip/cli2d.fs");
		
		texLoc = glGetUniformLocation(program, "frame");
		colorLoc = glGetUniformLocation(program, "inColor");
		modelLoc = glGetUniformLocation(program, "model");
		planeLoc = glGetUniformLocation(program, "plane");
	}
	
	public void setUniforms(float x, float y, float w, float h, float r, float g, float b, float a) {
		glUniform4f(texLoc, x, y, w, h);
		glUniform4f(colorLoc, r, g, b, a);
	}
	
	public void setPlane(float x, float y, float z, float w) {
		glUniform4f(planeLoc, x, y, z, w);
	}
	
	public void setModelMatrix(Matrix4f model) {
		glUniformMatrix4fv(modelLoc, false, model.get(new float[16]));
	}
	
}