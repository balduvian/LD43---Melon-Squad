package cnge.core;

import cnge.graphics.Shader;

public class BaseShader extends Shader{

	public BaseShader() {
		super("res/cnge/shaders/base/base.vs", "res/cnge/shaders/base/base.fs");
	}

}