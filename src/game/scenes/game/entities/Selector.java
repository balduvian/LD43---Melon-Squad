package game.scenes.game.entities;

import static game.scenes.game.GameAssets.rect;
import static game.scenes.game.GameAssets.*;

import cnge.core.Entity;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

public class Selector extends Entity {

	public static final int MODE_IDLE = 0;
	public static final int MODE_ACTIVE = 1;
	public static final int MODE_DEAD = 2;
	
	public float alpha;
	
	public int mode;
	
	public Selector() {
		super();
		transform.setSize(32, 32);
		mode = MODE_IDLE;
	}
	
	@Override
	public void update() {
		
	}

	@Override
	public void render() {
		selectorTexture.bind();
		
		tileShader.enable();
		
		if(mode == MODE_ACTIVE) {
			tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix( transform.destinationScale(1.25f, 1.25f).destinationTranslate(0, 16) )));
		}else {
			tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(transform)));
		}
		tileShader.setUniforms(selectorTexture.getX(), selectorTexture.getY(), selectorTexture.getZ(mode), selectorTexture.getW(), 1, 1, 1, alpha);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}
