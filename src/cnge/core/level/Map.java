package cnge.core.level;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import cnge.core.Base;
import cnge.core.Block;
import cnge.core.BlockSet;
import cnge.core.Entity;
import cnge.graphics.Camera;
import cnge.graphics.FBO;
import cnge.graphics.Transform;
import cnge.graphics.texture.Texture;

abstract public class Map<B extends Block> extends Entity {
	
	public static final int DEFAULT_BLOCK = -1;
	public static final int OUTSIDE_MAP = -2;
	
	protected BlockSet<B> blockSet;
	private int[][] tiles;
	private int width;
	private int height;
	
	protected int up;
	protected int left;
	protected int down;
	protected int right;
	
	protected int acr;
	protected int dow;
	
	private Access access;
	
	protected int scale;
	
	private FBO mapBuffer;
	
	public Map(Access a, int s) {
		access = a;
		scale = s;
		mapBuffer = new FBO();
		getOnScreenDims();
	}
	
	public void mapSetup(float x, float y, BlockSet<B> bs, int[][] t) {
		setup(x, y);
		tiles = t;
		width = t.length;
		height = t[0].length;
		blockSet = bs;
		transform.setSize(width * scale, height * scale);
	}
	
	/**
	 * called once per block
	 */
	abstract public void blockRender(int l, int x, int y, float left, float right, float up, float down);
	
	/**
	 * renders the map texture to the main buffer
	 * 
	 * @param t - the texture from the map buffer
	 */
	abstract public void mapRender(Transform t, Texture tx);
	
	public interface Access {
		int access( int[][] a, int x, int y);
	}
	
	public int access(int[][] a, int x, int y) {
		return access.access(a, x, y);
	}
	
	public int mapAccess(int x, int y) {
		return access.access(tiles, x, y);
	}
	
	public BlockSet<B> getBlockSet(){
		return blockSet;
	}
	
	public void getOnScreenDims() {
		Transform t = camera.getTransform();
		acr = (int)Math.ceil(t.getWidth() / scale) + 1;
		dow = (int)Math.ceil(t.getHeight() / scale) + 1;
		mapBuffer.replaceTexture(new Texture(acr * scale, dow * scale));
	}
	
	/**
	 * converts map coordinates to world coordinates
	 * @param x - x in map coordinates
	 * @return x in world coordinates (LEFT SIDE)
	 */
	public float getX(int x) {
		return x * scale * transform.wScale + transform.x;
	}
	
	/**
	 * converts map coordinates to world coordinates
	 * @param y - y in map coordinates
	 * @return y in world coordinates (TOP SIDE)
	 */
	public float getY(int y) {
		return y * scale * transform.hScale + transform.y;
	}
	
	public float getScale() {
		return scale;
	}
	
	public int getUp() {
		return up;
	}
	
	public int getLeft() {
		return left;
	}
	
	public int getDown() {
		return down;
	}
	
	public int getRight() {
		return right;
	}
	
	public int[][] getTies() {
		return tiles;
	}
	
	public int get(int x, int y) {
		return tiles[x][y];
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setBlock(int x, int y, int b) {
		tiles[x][y] = b;
	}
	
	public boolean onScreenUpdate() {
		Transform ct = camera.getTransform();
		
		if(!(onScreen = alwaysOn)) {
			float ex = transform.x;
			float ey = transform.y;
			float ew = transform. getWidth();
			float eh = transform.getHeight();
			
			float cx = ct.x;
			float cy = ct.y;
			float cw = ct. getWidth();
			float ch = ct.getHeight();
			
			onScreen = (ex + ew > cx) && (ex < cx + cw) && (ey + eh > cy) && (ey < cy + ch);
		}
		
		left  = (int)Math.floor( ( ct.x - transform.x) / (scale * transform.wScale) );
		up    = (int)Math.floor( ( ct.y - transform.y) / (scale * transform.wScale) );
		   
		right = left + acr;
		down  = up + dow;
		
		return onScreen;
	}
	
	/**
	 * gets the map coordinate correspoding to a world coordinate
	 * 
	 * @param x - x pposition in the world
	 * 
	 * @return the integer x position in map coordinates 
	 */
	public int atX(float x) {
		return (int)((x-transform.x) * width / transform.width);
	}
	
	/**
	 * gets the map coordinate correspoding to a world coordinate
	 * 
	 * @param y - y pposition in the world
	 * 
	 * @return the integer y position in map coordinates 
	 */
	public int atY(float y) {
		return (int)((y-transform.y) * height / transform.height);
	}
	
	public FBO getMapBuffer() {
		return mapBuffer;
	}
	
	/**
	 * accesses the map only within its bounds
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public int boundedAccess(int[][] a, int x, int y) {
		int width_m = a.length - 1;
		int height_m = a[0].length - 1;
		if(x < 0) {
			return OUTSIDE_MAP;
		} else if (x > width_m) {
			return OUTSIDE_MAP;
		}
		if(y < 0) {
			return OUTSIDE_MAP;
		} else if (y > height_m) {
			return OUTSIDE_MAP;
		}
		return a[x][y];
	}
	
	/**
	 * accesses the map but each edge repeats
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public static int edgeAccess(int[][] a, int x, int y) {
		int width_m = a.length - 1;
		int height_m = a[0].length - 1;
		if(x < 0) {
			x = 0;
		} else if (x > width_m) {
			x = width_m;
		}
		if(y < 0) {
			y = 0;
		} else if (y > height_m) {
			y = height_m;
		}
		return a[x][y];
	}
	
	/**
	 * accesses the map but it extends horizontally in both directions
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public static int horzEdgeAccess(int[][] a, int x, int y) {
		int width_m = a.length - 1;
		int height_m = a[0].length - 1;
		if(x < 0) {
			x = 0;
		} else if (x > width_m) {
			x = width_m;
		}
		if(y < 0) {
			return OUTSIDE_MAP;
		} else if (y > height_m) {
			return OUTSIDE_MAP;
		}
		return a[x][y];
	}
	
	/**
	 * accesses the map but it extends vertically in both directions
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public static int vertEdgeAccess(int[][] a, int x, int y) {
		int width_m = a.length - 1;
		int height_m = a[0].length - 1;
		if(x < 0) {
			return OUTSIDE_MAP;
		} else if (x > width_m) {
			return OUTSIDE_MAP;
		}
		if(y < 0) {
			y = 0;
		} else if (y > height_m) {
			y = height_m;
		}
		return a[x][y];
	}
	
	/**
	 * accesses the map but it's cloned horizontally
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * @throws AccessException
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public static int repeatHorzAccess(int[][] a, int x, int y) {
		int width = a.length;
		int height_m = a[0].length - 1;
		x %= width;
		if(y < 0) {
			return OUTSIDE_MAP;
		} else if (y > height_m) {
			return OUTSIDE_MAP;
		}
		return a[x][y];
	}
	
	/**
	 * accesses the map but it's cloned everywhere
	 * 
	 * @param x - map coordinate
	 * @param y - map coordinate
	 * 
	 * @return the block there
	 * 
	 * @see atX()
	 * @see atY()
	 */
	public static int repeatAllAccess(int[][] a, int x, int y) {
		int width = a.length;
		int height = a[0].length;
		x %= width;
		y %= height;
		return a[x][y];
	}
	
	public void render(int layer) {
		
		mapBuffer.enable();
		camera.setDims(acr * scale, dow * scale);
		
		//clear the buffer
		glClearColor(0, 0, 0, 0);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		for(int x = 0; x < acr; ++x) {
			for(int y = 0; y < dow; ++y) {
				blockRender(layer, x + left, y + up, x * scale, (x + 1) * scale, y * scale, (y + 1) * scale);
			}
		}
		
		Base.screenBuffer.enable();
		camera.defaultDims();
        
		mapRender(new Transform(left * scale, up * scale, acr * scale, dow * scale), mapBuffer.getTexture());
	}
	
	/**
	 * the default inherited render method from entity is not used
	 * 
	 * @deprecated FUCK FUCK FUCK FUCK DONT USE THIS METHOD
	 * 
	 * THIS WILL CLOSE DOWN YOUR PROGRAM
	 */
	public void render() {
		System.err.println("I TOLD YOU NOT TO USE THIS YOU SKEKER");
		System.exit(-3);
	}
	
}