package cnge.core;

import cnge.graphics.Camera;
import cnge.graphics.Transform;
import cnge.graphics.Window;

public abstract class Scene<S extends Scene<S>> {

	protected static Base base;
	
	protected static Window window;
	
	protected static Camera camera;
	protected static Transform cameraTransform;
	
	protected static int screenWidth;
	protected static int screenHeight;
	
	protected AssetBundle<S> assets;
	
	@SuppressWarnings("unchecked")
	public Scene(AssetBundle<S> a) {
		assets = a;
		a.giveScene((S)this);
	}
	
	/*
	 * static stuff that the base gives to the scene
	 */
	
	public Camera getCamera() {
		return camera;
	}
	
	public static void giveStuff(Camera c, Base b, Window w) {
		camera = c;
		cameraTransform = c.getTransform();
		base = b;
		window = w;
	}
	
	public static void giveDims(int w, int h) {
		screenWidth = w;
		screenHeight = h;
	}
	
	public static void changeScene(Scene s) {
		base.setScene(s);
	}
	
	public void setCameraCenter(float x, float y) {
		Transform ct = camera.getTransform();
		ct.setTranslation(x - ct.getWidth() / 2, y - ct.getHeight() / 2);
	}
	
	public float ccx(float x, float w) {
		Transform ct = camera.getTransform();
		return x - (ct.getWidth() / 2) + (w / 2);
	}
	
	public float ccy(float y, float h) {
		Transform ct = camera.getTransform();
		return y - (ct.getHeight() / 2) + (h / 2);
	}
	
	/*
	 * fun stuff to do with entities
	 */
	
	public void createEntity(Entity e, float x, float y) {
		e.setup(x, y);
	}
	
	public void eUpdate(Entity e) {
		e.update();
	}
	
	public void eUpdate_S(Entity e) {
		if(e != null) {
			e.update();
		}
	}
	
	public void eUpdate_OS(Entity e) {
		if(e != null) {
			e.onScreenUpdate();
			if(e.onScreen) {
				e.update();
			}
		} 
	}
	
	public void eUpdate_O(Entity e) {
		e.onScreenUpdate();
		if(e.onScreen) {
			e.update();
		}
	}

	/*
	 * fun rendering stuff to do with entities
	 */
	
	public void eRender(Entity e) {
		e.render();
	}
	
	/**
	 * renders an entity, checking whether its on screen.
	 * USE FOR ENTITIES THAT WILL NEVER BE NULL
	 * 
	 * @param e - the entity to render
	 */
	public void eRender_O(Entity e) {
		if(e.onScreen) {
			e.render();
		}
	}
	
	/**
	 * 
	 */
	public void eRender_S(Entity e) {
		if(e != null) {
			e.render();
		}
	}
	
	/**
	 * renders an entity, but checks if it's null first
	 * 
	 * @param e - the entity to render
	 */
	public void eRender_OS(Entity e) {
		if(e != null && e.onScreen) {
			e.render();
		}
	}
	
	/*
	 * override this stuff to do stuff with the scene
	 */
	
	abstract public void render();
	
	abstract public void update();
	
	/**
	 * no longer do you override this
	 * 
	 * should all be taken care of in the asset bundle
	 */
	public void start() {
		assets.load();
	}
	 
	abstract public void resizeUpdate();
	
	public void cameraLeftLimit(float cx) {
		if(cameraTransform.x < cx) {
			cameraTransform.x = cx;
		}
	}
	
	public void cameraDownLimit(float cy) {
		if(cameraTransform.y + cameraTransform.height > cy) {
			cameraTransform.y = cy - cameraTransform.height;
		}
	}
	
	public void cameraRightLimit(float cx) {
		if(cameraTransform.x + cameraTransform.width > cx) {
			cameraTransform.x = cx - cameraTransform.width;
		}
	}
	
	public void cameraUpLimit(float cy) {
		if(cameraTransform.y < cy) {
			cameraTransform.y = cy;
		}
	}
	
}