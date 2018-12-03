package game.scenes.win;

import cnge.core.Scene;
import cnge.core.Timer;
import cnge.core.morph.CustomMorph;
import cnge.core.morph.Morph;
import cnge.graphics.Window;
import cnge.core.AssetBundle.SceneLoadAction;
import game.scenes.menu.MenuScene;

import static game.scenes.win.WinAssets.*;
import static org.lwjgl.opengl.GL11.glClearColor;

public class WinScene extends Scene<WinScene> {

	public CustomMorph scroller;
	public float position;
	
	public WinScene() {
		super(
			new WinAssets(
				
				new SceneLoadAction<WinScene>() {
					public void load(WinScene s) {
						winTimer = new Timer(
							11,
							() -> {
								base.setScene(new MenuScene());
							}
						);
						
						s.scroller = new CustomMorph(320, -200, Morph.lINEAR, 12);
								
						s.position = 288;
						
						s.winStart();
					}
				}
				
			)
		);
	}

	public void winStart() {
		winTimer.start();
		winMusic.play(false);
	}
	
	public void render() {
		glClearColor(0, 0, 0, 1);
		Window.clear();
		
		melonFont.render(winText[0], 256, position     , 1, true);
		melonFont.render(winText[1], 256, position + 30, 1, true);
		melonFont.render(winText[2], 256, position + 60, 1, true);
	}

	public void update() {
		position = scroller.update();
		
		winTimer.update();
	}

	public void resizeUpdate() {
	
	}
	
}