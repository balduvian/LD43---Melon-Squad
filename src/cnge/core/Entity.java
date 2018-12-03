package cnge.core;

import cnge.graphics.Camera;
import cnge.graphics.Transform;

abstract public class Entity {
	
	protected boolean alwaysOn;
	protected boolean onScreen;
	
	protected int index;
	
	protected Transform transform;
	
	protected static Scene scene;
	protected static Camera camera;
	
	public Entity() {
		onScreen = true;
		transform = new Transform();
	}
	
	/**
	 * this is called immeadiately after the entity is constructed.
	 * this is so that you don't have to put these params in the custom entity constructors
	 *
	 * @param g - the entity group this entity belongs to
	 */
	public void setup(float x, float y) {
		transform.setTranslation(x, y);
	}
	
	public static void giveCamera(Camera c) {	
		camera = c;
	}
	
	public static void giveScene(Scene s) {
		scene = s;
	}
	
	/*
	 * the big abstract methods
	 */
	abstract public void update();
	
	abstract public void render();
	
	public void setAlwaysOn(boolean a) {
		alwaysOn = a;
	}
	
	public Transform getTransform() {
		return transform;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean onScreenUpdate() {
		if(alwaysOn) {
			return true;
		}else {
			float ex = transform.x;
			float ey = transform.y;
			float ew = transform. getWidth();
			float eh = transform.getHeight();
			
			Transform cTransform = camera.getTransform();
			
			float cx = cTransform.x;
			float cy = cTransform.y;
			float cw = cTransform. getWidth();
			float ch = cTransform.getHeight();
			
			onScreen = (ex + ew > cx) && (ex < cx + cw) && (ey + eh > cy) && (ey < cy + ch);
			return onScreen;
		}
	}
	
	public void setOnScreen(boolean o) {
		onScreen = o;
	}
	
	public boolean getOnScreen() {
		return onScreen;
	}
	
}