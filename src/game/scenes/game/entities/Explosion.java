package game.scenes.game.entities;

import cnge.core.Entity;
import cnge.core.animation.Anim2D;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;

import static game.scenes.game.GameAssets.*;

public class Explosion extends Entity {

	public Anim2D anim;
	public int ind;
	
	public Explosion(int i, float scale, float rotation, double length) {
		super();
		ind = i;
		transform.setSize(32, 32);
		transform.setScale(scale, scale);
		transform.rotation = rotation;
		anim = new Anim2D(
			new int[][] {
				{0, 0},
				{1, 0},
				{2, 0},
				{3, 0},
				{4, 0},
				{0, 1},
				{1, 1},
				{2, 1},
				{3, 1},
				{4, 1},
			},
			new double[] {
				length,
				length,
				length,
				length,
				length,
				length,
				length,
				length,
				length,
				length,
			}
		);
		anim.addEvent(0, () -> {
			//kill this mf
			explosions[ind] = null;
		});
	}
	
	public void update() {
		anim.update();
	}

	@Override
	public void render() {
		
		explosionTexture.bind();
		
		tileShader.enable();
		
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(transform)));
		tileShader.setUniforms(explosionTexture.getX(), explosionTexture.getY(), explosionTexture.getZ(anim.getX()), explosionTexture.getW(anim.getY()), 1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}