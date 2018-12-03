package game.shaders;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;

import cnge.graphics.Shader;

public class MovieShader extends Shader {
	
	private int seedLoc;
	
	public MovieShader() {
		super("res/shaders/movie/mov2d.vs", "res/shaders/movie/mov2d.fs");
		
		seedLoc = glGetUniformLocation(program, "seed");
	}
	
	public void setUniforms(float s) {
		
		glUniform1f(seedLoc, s);
	}
	
}