package game.scenes.menu;

import cnge.core.Scene;
import game.scenes.menu.entities.MenuSelector;
import game.scenes.game.GameScene;
import game.scenes.menu.entities.HowToCard;
import game.scenes.menu.entities.MenuBackground;
import game.scenes.menu.entities.Title;
import cnge.core.AssetBundle.SceneLoadAction;

import static game.scenes.menu.MenuAssets.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class MenuScene extends Scene<MenuScene> {

	public int select;
	
	public boolean pressLeft;
	public boolean leftLock;
	public boolean pressRight;
	public boolean rightLock;
	public boolean pressSpace;
	public boolean spaceLock;
	
	public boolean inHowTo;
	
	public MenuScene() {
		super(
			new MenuAssets(
					
				new SceneLoadAction<MenuScene>() {
					public void load(MenuScene s) {
						s.menuStart();
					}
				}
			
			)
		);
		
	}

	public void menuStart() {
		camera.getTransform().setTranslation(0, 0);
		
		select = 0;
		
		createEntity(background = new MenuBackground(), 0, 0);
		createEntity(leftSelect = new MenuSelector(0), MenuSelector.playLeftX, MenuSelector.playLeftY);
		createEntity(rightSelect = new MenuSelector(1), MenuSelector.playRightX, MenuSelector.playRightY);
		createEntity(title = new Title(), 96, 0);
		createEntity(howToCard = new HowToCard(), 0, 0);
		
		menuSong.play(true);
		
		inHowTo = false;
	}
	
	public void render() {
		eRender(background);
		
		eRender(title);
		
		melonFont.render(playText, 128, 192, 2, true);
		
		melonFont.render(tutorialText, 384, 192, 2, true);
		
		eRender(leftSelect);
		eRender(rightSelect);
		
		if(inHowTo) {
			eRender(howToCard);
			
			melonFont.render(spaceTextHowTo, 310, 250, 1, false);
		} else {
			melonFont.render(spaceText, 350, 266, 1, false);
		}
	}

	public void update() {
		
		if(!rightLock) {
			pressRight = window.keyPressed(GLFW_KEY_D);
		} else {
			rightLock = window.keyPressed(GLFW_KEY_D);
		}
		
		if(!leftLock) {
			pressLeft = window.keyPressed(GLFW_KEY_A);
		} else {
			leftLock = window.keyPressed(GLFW_KEY_A);
		}
		
		if(!spaceLock) {
			pressSpace = window.keyPressed(GLFW_KEY_SPACE);
		} else {
			spaceLock = window.keyPressed(GLFW_KEY_SPACE);
		}
		
		eUpdate(title);
		eUpdate(leftSelect);
		eUpdate(rightSelect);
		eUpdate(background);
		
		if(inHowTo) {
			if(pressSpace) {
				spaceLock = true;
				pressSpace = false;
				
				++howToCard.cardNumber;
				if(howToCard.cardNumber == HowToCard.CARDS) {
					inHowTo = false;
				}
			}
		} else {
			if(pressRight) {
				rightLock = true;
				pressRight = false;
				//
				++select;
				select = Math.floorMod(select, 2);
				slide();
			}
			
			if(pressLeft) {
				leftLock = true;
				pressLeft = false;
				//
				--select;
				select = Math.floorMod(select, 2);
				slide();
			}
			
			if(pressSpace) {
				spaceLock = true;
				pressSpace = false;
				//
				if(select == 0) {
					menuSong.stop();
					base.setScene(new GameScene());
				} else {
					inHowTo = true;
					howToCard.cardNumber = 0;
				}
			}
		}
		
	}
	
	public void slide() {
		if(select == 0) {
			leftSelect.slidingLeft = true;
			leftSelect.slidingRight = false;
			leftSelect.slideLeft.reset();
			
			rightSelect.slidingLeft = true;
			rightSelect.slidingRight = false;
			rightSelect.slideLeft.reset();
		} else {
			leftSelect.slidingRight = true;
			leftSelect.slidingLeft = false;
			leftSelect.slideRight.reset();
			
			rightSelect.slidingRight = true;
			rightSelect.slidingLeft = false;
			rightSelect.slideRight.reset();
		}
	}

	public void resizeUpdate() {
		
	}

}