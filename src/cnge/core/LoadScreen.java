package cnge.core;

import cnge.graphics.Camera;

abstract public class LoadScreen {
	
	private Camera camera;
	
	private int soFar;
	private int goal;
	
	public void render() {
		loadRender(camera, soFar, goal);
	}
	
	abstract protected void loadRender(Camera camera, int soFar, int goal);
	
	public void giveCamera(Camera c) {
		camera = c;
	}
	
	public void startLoad(int g) {
		soFar = 0;
		goal = g;
	}
	
	public void setLoaded() {
		++soFar;
	}
	
}