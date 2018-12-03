package game.scenes.menu.entities;

import cnge.core.Entity;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;

import static game.scenes.menu.MenuAssets.*;

public class HowToCard extends Entity {

	public static final int CARDS = 5;
	
	public int cardNumber;
	
	public HowToCard() {
		super();
		cardNumber = 0;
		transform.setSize(512, 288);
	}
	
	public void update() {
	
	}

	public void render() {
		howToCardTexture.bind();
		
		textureShader.enable();
		textureShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(transform)));
		
		rect.render();
		
		//now render the slide we're on
		howToCards[cardNumber].bind();
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}