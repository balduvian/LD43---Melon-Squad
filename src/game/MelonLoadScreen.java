package game;

import static org.lwjgl.opengl.GL11.glClearColor;

import cnge.core.LoadScreen;
import cnge.graphics.Camera;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.Window;
import cnge.graphics.shapes.RectShape;
import game.shaders.ColorShader;

public class MelonLoadScreen extends LoadScreen {

	RectShape rect;
	ColorShader shader;
	
	public MelonLoadScreen() {
		rect = new RectShape();
		shader = new ColorShader();
	}

	@Override
	protected void loadRender(Camera camera, int soFar, int goal) {

		glClearColor(0, 0, 0, 1);
		Window.clear();
		
		shader.enable();
		
		shader.setUniforms(1, 1, 1f, 1);
		
		float l = 160;
		float u = 112;
		float along = ((float)soFar / goal);
		float w = 192 * along;
		float h = 64;
		
		shader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrixDims(l, u, w, h)));
		rect.render();
		
		//top
		shader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrixDims(160, 112, 192, 4)));
		rect.render();
		
		//right
		shader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrixDims(348, 112, 4, 64)));
		rect.render();
		
		//down
		shader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrixDims(160, 172, 192, 4)));
		rect.render();
		
		//left
		shader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrixDims(160, 112, 4, 64)));
		rect.render();
		
		Shader.disable();
	}

}