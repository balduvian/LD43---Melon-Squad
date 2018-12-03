//gatcuddy <emmettglaser@gmail.com>

package cnge.core;

import static org.lwjgl.opengl.GL11.glClearColor;

import static org.lwjgl.opengl.GL11.*;

import cnge.graphics.ALManagement;
import cnge.graphics.Camera;
import cnge.graphics.FBO;
import cnge.graphics.Shader;
import cnge.graphics.Shape;
import cnge.graphics.Window;
import cnge.graphics.texture.Texture;
import cnge.graphics.texture.TexturePreset;

/**
 * @author Emmet
 */
abstract public class Base {
	
	public static int fps;
	public static double time;
	public static long nanos;
	
	public static Window window;
	protected Camera camera;
	protected ALManagement audio;

	private int frameRate;
	
	private Scene scene;
	private LoadScreen loadScreen;
	
	private boolean loading;
	
	private BaseShader baseShader;
	
	public static FBO screenBuffer;
	
	private boolean fullWidth;
	
	private Framer screenType;
	
	private boolean gamePixelType;
	
	/**
	 * FRAME WIDTH IS THE WIDTH IN SCREEN SPACE
	 */
	private int frameWidth;
	private int frameHeight;
	
	/**
	 * GAME WIDTH IS THE WIDTH IN VIRTUAL SPACE
	 */
	private int gameWidth;
	private int gameHeight;
	private int gameLimit;
	
	private Shape rect;
	
	private boolean beingResized;
	
	/**
	 * The base of the entire program. Create one in your own main method
	 * 
	 * @param win - the window
	 * @param sm - screen mode for the window |
	 * 
	 * {@link #SCREEN_PIXEL} |
	 * {@link #ASPECT_STRETCH_HORZ} |
	 * {@link #ASPECT_STRETCH_VERT} |
	 * {@link #ASPECT_FIXED} |
	 * 
	 * @param am - aspect mode modifiers |
	 * 
	 * {@link #UNIT_GAME_PIXELS} |
	 * {@link #UNIT_SCREEN_PIXELS} |
	 * 
	 * @return the base
	 */
	public Base(Window win, BasePreset set) {
		window = win;
		
		frameRate = window.getRefreshRate() + 10;
		
		camera = new Camera(1, 1);
		
		screenBuffer = new FBO(new Texture());
		
		baseShader = new BaseShader();
		
		Scene.giveStuff(camera, this, window);
		AssetBundle.giveBase(this);
		Shape.giveCamera(camera);
		FBO.giveStuff(window, camera);
		Entity.giveCamera(camera);
		Font.giveCamera(camera);
		
		rect = Shape.RECT;
		
		audio = new ALManagement();
		
		screenType = set.screenType;
		gamePixelType = set.pixelType;
		gameWidth = set.width;
		gameHeight = set.height;
		gameLimit = set.limit;
		
		camera.giveDefaults(gameWidth, gameHeight);
		
		window.setResize(
			(w, h) -> {
				reFrame(w, h);
			}
		);
		
		reFrame(window.getWidth(), window.getHeight());
	}
	
	/**
	 * this is the last thing you call in main to start the game
	 * 
	 * @param s - the scene to start on
	 */
	public void start(Scene s) {
		scene = s;
		
		Entity.giveScene(s);
		
		scene.start();
		
		gameLoop();
		
	}
	
	/**
	 * changes the current scene
	 * 
	 * @param s - the scene to change to
	 */
	public void setScene(Scene s) {
		scene = s;
		Entity.giveScene(s);
		scene.start();
	}
	
	public void setLoading(LoadScreen s, boolean l) {
		loadScreen = s;
		loading = l;
	}
	
	public void loadLoadScreen(LoadScreen s) {
		s.giveCamera(camera);
	}
	
	public interface Framer {
		public void reFrame(Base b, int w, int h);
	}
	
	public static final Framer PIXEL_FRAMER = new Framer() {
		public void reFrame(Base b, int w, int h) {
			b.frameWidth = w;
			b.frameHeight = h;
		}
	};
	
	public static final Framer ASPECT_FRAMER = new Framer() {
		public void reFrame(Base b, int w, int h) {
			float windowRatio = (float)window.getWidth() / window.getHeight();
			
			float gameRatio = (float)b.gameWidth / b.gameHeight;
			
			if(windowRatio < gameRatio) {
				b.fullWidth = true;
				b.frameWidth = window.getWidth();
				b.frameHeight = (int)(window.getHeight() * (windowRatio/gameRatio));
			}else {
				b.fullWidth = false;
				b.frameWidth = (int)(window.getWidth() * (gameRatio/windowRatio));
				b.frameHeight = window.getHeight();
			}
		}
	};
	
	public static final Framer STRETCH_FRAMER = new Framer() {
		public void reFrame(Base b, int w, int h) {
			b.fullWidth = false;
			
			float windowRatio = (float)window.getWidth() / window.getHeight();
			
			b.frameHeight = h;
			
			b.gameWidth = (int)Math.round(windowRatio * b.gameHeight);
			if(b.gameWidth > b.gameLimit) {
				b.gameWidth = b.gameLimit;
				b.frameWidth = (int)Math.round(window.getWidth() * ( (float)(b.gameWidth / b.gameHeight) / (windowRatio) ));
			}else {
				b.frameWidth = w;
			}
		}
	};
	
	/**
	 * called when the window is resized, sets the renderable frame inside the window based on the screen mode
	 * 
	 * @param w - auto put in, the width of the window
	 * @param h - auto put in the height of the window
	 */
	private void reFrame(int w, int h) {
		
		beingResized = true;
		time = 0;
		
		screenType.reFrame(this, w, h);
		
		if(gamePixelType) {
			screenBuffer.replaceTexture(new Texture(gameWidth, gameHeight, new TexturePreset().nearest(true)));
		} else {
			screenBuffer.replaceTexture(new Texture(frameWidth, frameHeight, new TexturePreset().nearest(true)));
		}
		
		camera.setDims(gameWidth, gameHeight);
		
		if(scene != null) {
			Scene.giveDims(w, h);
			scene.resizeUpdate();
		}
	}
	
	public void gameLoop() {
		long usingFPS = 1000000000 / frameRate;
		long last = System.nanoTime();
		long lastSec = last;
		int frames = 0;
		while(!window.shouldClose()) {
			long now = System.nanoTime();
			if(now-last > usingFPS) {
				nanos = (now-last);
				
				//resize time lock
				//beingResized will be enabled by the reframe method
				if(beingResized) {
					time = 0;
				} else {
					time = (nanos)/1000000000d;
				}
				beingResized = false;
				
				update();
				render();
				
				last = now; 
				++frames;
			}
			if(now-lastSec > 1000000000) {
				fps = frames; 
				frames = 0;
				lastSec = now;
				System.out.println(fps);
			}
		}
		audio.destroy();
	}
	
	public void update() {
		window.update();
		
		if(!loading) {
			scene.update();
		}
		
		camera.update();
	}
	
	public void render() {
		
		//start up that mf game screenBuffer
		screenBuffer.enable();
		
		glClearColor(1, 0, 1, 1);
		Window.clear(); //clear the game screenBuffer
		
		if(loading) {
			loadScreen.render();
		} else {
			scene.render();
		}
		
		FBO.enableDefault();
		//close down that mf screenBuffer
		
		//clear the bars with black
		glClearColor(0, 0, 0, 1);
		Window.clear();
		
		screenBuffer.getTexture().bind();
		
		baseShader.enable();
		
		baseShader.setMvp(camera.ndcFullMatrix());
		
		if(fullWidth) {
			glViewport(0, window.getHeight()/2 - frameHeight/2, frameWidth, frameHeight);
		} else {
			glViewport(window.getWidth()/2 - frameWidth/2, 0, frameWidth, frameHeight);
		}
		
		rect.render();
		
		Shader.disable();
		
		Texture.unbind();

		window.swap();
	}
	
}