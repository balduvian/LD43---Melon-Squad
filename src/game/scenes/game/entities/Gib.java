package game.scenes.game.entities;

import cnge.core.Base;
import cnge.core.Entity;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;
import game.scenes.game.GameScene;

import static game.scenes.game.GameAssets.*;

public class Gib extends Entity {

	public int frameX;
	public int frameY;
	
	public float velocityX;
	public float velocityY;
	
	public int ind;
	
	public Gib(int i, int t, float vx, float vy) {
		super();
		ind = i;
		transform.setSize(32, 32);
		velocityX = vx;
		velocityY = vy;
		
		frameX = (int)(Math.random() * 2) + (2 * t);
		
		frameY = (int)(Math.random() * 2);
	}
	
	public void update() {
		velocityY += GameScene.GRAVITY * Base.time;
		transform.move((float)(velocityX * Base.time), (float)(velocityY * Base.time));
		if(!onScreen) {
			gibs[ind] = null;
		}
	}

	@Override
	public void render() {
		gibTexture.bind();
		
		tileShader.enable();
		
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(transform)));
		tileShader.setUniforms(gibTexture.getX(), gibTexture.getY(), gibTexture.getZ(frameX), gibTexture.getW(frameY), 1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}