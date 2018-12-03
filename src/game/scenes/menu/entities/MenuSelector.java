package game.scenes.menu.entities;

import cnge.core.Entity;
import cnge.core.morph.Morph;
import cnge.graphics.Shader;
import cnge.graphics.texture.Texture;

import static game.scenes.menu.MenuAssets.*;

public class MenuSelector extends Entity {

	public static final int playLeftX = 79;
	public static final int playLeftY = 192;
	public static final int playRightX = 165;
	public static final int playRightY = 192;
	
	public static final int tutorialLeftX = 309;
	public static final int tutorialLeftY = 192;
	public static final int tutorialRightX = 443;
	public static final int tutorialRightY = 192;
	
	public int frame;
	public Morph slideLeft;
	public Morph slideRight;
	
	public boolean slidingLeft;
	public boolean slidingRight;
	
	public MenuSelector(int f) {
		super();
		//a tall boi
		transform.setSize(16, 32);
		frame = f;
		if(frame == 0) {
			slideLeft = new Morph(transform, Morph.COSINE, 0.5).addPositionX(playLeftX).addPositionY(playLeftY);
			slideRight = new Morph(transform, Morph.COSINE, 0.5).addPositionX(tutorialLeftX).addPositionY(tutorialLeftY);
		} else {
			slideLeft = new Morph(transform, Morph.COSINE, 0.5).addPositionX(playRightX).addPositionY(playRightY);
			slideRight = new Morph(transform, Morph.COSINE, 0.5).addPositionX(tutorialRightX).addPositionY(tutorialRightY);
		}
	}
	
	@Override
	public void update() {
		if(slidingLeft) {
			if(slideLeft.update()) {
				slidingLeft = false;
			}
		}
		else if(slidingRight) {
			if(slideRight.update()) {
				slidingRight = false;
			}
		}
	}

	@Override
	public void render() {
		menuSelectorTexture.bind();
		
		tileShader.enable();
		
		tileShader.setMvp(camera.getModelProjectionMatrix(camera.getModelMatrix(transform)));
		tileShader.setUniforms(menuSelectorTexture.getX(), menuSelectorTexture.getY(), menuSelectorTexture.getZ(frame), menuSelectorTexture.getW(), 1, 1, 1, 1);
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();
	}

}