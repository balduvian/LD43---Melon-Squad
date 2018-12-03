package game.scenes.game.entities;

import cnge.core.Entity;
import cnge.core.Timer;
import cnge.core.morph.CustomMorph;
import cnge.core.morph.Morph;
import cnge.graphics.Shader;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

import static game.scenes.game.GameAssets.*;

public class CarrotTransition extends Entity {

	public Morph morph;
	public Timer timer;
	
	public CarrotTransition(boolean grow) {
		super();
		transform.setSize(32, 32);
		
		if(grow) {
			transform.setScale(0, 0);
			morph = new Morph(transform, Morph.UNDERCIRCLE, 1).addScaleW(51).addScaleH(51);
		} else {
			transform.setScale(51, 51);
			morph = new Morph(transform, Morph.OVERCIRCLE, 2).addScaleW(0).addScaleH(0);
		}
		
		timer = new Timer(
			3, 
			() -> {
				carrotTransition = null;
			}
		);
		
		timer.start();
	}
	
	public void update() {
		morph.update();
		timer.update();
		
		Transform ct = camera.getTransform();
		
		transform.x = (ct.getWidth() / 2) - transform.width / 2;
		
		transform.y = 0;
	}

	@Override
	public void render() {
		carrotCutout.bind();
		
		tileShader.enable();
		
		//fill up the mf screen
		tileShader.setMvp(camera.getModelViewProjectionMatrix(camera.getModelMatrix(camera.getTransform())));
		
		Transform ct = camera.getTransform();
		
		float texWidth = (ct.width / transform.getWidth() );
		float texHeight = (ct.height / transform.getHeight());
		
		tileShader.setUniforms(
				( texWidth  ), 
				( texHeight ), 
				( -texWidth / 2 + 0.5f), 
				( -texHeight / 2 + 0.5f),
				1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}