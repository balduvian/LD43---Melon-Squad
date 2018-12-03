package game;

import static org.lwjgl.opengl.GL11.glClearColor;

import cnge.core.LoadScreen;
import cnge.graphics.Camera;
import cnge.graphics.Window;

public class WinLoadScreen extends LoadScreen {
	
	public WinLoadScreen() {
	}

	@Override
	protected void loadRender(Camera camera, int soFar, int goal) {
		glClearColor(0, 0, 0, 1);
		Window.clear();
		//you get nothing...
		//but black
	}

}