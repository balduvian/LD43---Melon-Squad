package game.scenes.menu.entities;

import static game.scenes.menu.MenuAssets.*;

import cnge.core.Base;
import cnge.core.Entity;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

public class MenuBackground extends Entity {

	public static final float parallax = 0.2f;
	
	public MenuBackground() {
		super();
		transform.setSize(512, 288);
	}
	
	public void update() {
		transform.moveX((float)(Base.time * 4));
	}

	public void render() {
		skyTexture.bind();
		
		tileShader.enable();
		
		Transform ct = camera.getTransform();
		
		tileShader.setUniforms((ct.width / transform.width), (ct.height / transform.height), (float)(ct.x * parallax - transform.x) / ct.getWidth(), (float)(ct.y * parallax - transform.y) / ct.getHeight(), 1, 1, 1, 1);
		
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(camera.getTransform())));
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}