package game.scenes.menu.entities;

import cnge.core.Entity;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;

import static game.scenes.menu.MenuAssets.*;

public class Title extends Entity {

	public Title() {
		super();
		transform.setSize(320, 192);
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render() {
		titleTexture.bind();
		
		textureShader.enable();
		
		textureShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(transform)));
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}