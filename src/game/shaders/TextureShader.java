package game.shaders;

import cnge.graphics.Shader;

public class TextureShader extends Shader {
	
	public TextureShader() {
		super("res/shaders/texture/tex2d.vs", "res/shaders/texture/tex2d.fs");
	}
	
}