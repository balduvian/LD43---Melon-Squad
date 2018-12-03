package cnge.core.level;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

import cnge.core.Block;
import cnge.core.BlockSet;

public abstract class Level<M extends Map, B extends Block> {

	private int sections;
	
	protected int[][][] tiles;
	protected int[][][] entityTiles;
	
	protected int[] widths;
	protected int[] heights;
	
	private String[] mapImages;
	
	protected BlockSet<B> blockSet;
	
	/**
	 * constructs a new mapgroup for the scene
	 * 
	 * @param ip - the paths to the map loader images
	 * @param bs - the blockset for the maps
	 */
	public Level(String[] ip, BlockSet<B> bs) {
		sections = ip.length;
		mapImages = ip;
		blockSet = bs;
	}
	
	/**
	 * constructs a new mapgroup for the scene with only 1 section
	 * 
	 * @param ip - the paths to the map loader images
	 * @param bs - the blockset for the maps
	 */
	public Level(String ip, BlockSet<B> bs) {
		sections = 1;
		mapImages = new String[] {ip};
		blockSet = bs;
	}
	
	/**
	 * BOIIIIIIIIIII TODO
	 */
	public M createMaps(int i, float x, float y, Object... params) {
		int w = widths[i];
		int h = heights[i];
		int[][] take = tiles[i];
		int[][] give = new int[w][h];
		/*
		 * we make a clone array that we give to the map instance
		 */
		for(int j = 0; j < w; ++j) {
			for(int k = 0; k < h; ++k) {
				give[j][k] = take[j][k];
			}
		}
		M create = mapCreate(i, params);
		create.mapSetup(x, y, blockSet, give);
		return create;
	}
	
	/**
	 * use this instead of the {@link EntityGroup} one
	 * 
	 * creates an instance of the single map section belonging to this group
	 * 
	 * @param x - the x position of the map
	 * @param y - the y position of the map
	 * @param l - the layer the map is on
	 * 
	 * @return returns the map, NULL if could not create
	 */
	public M createMap(float x, float y, Object... params) {
		int w = widths[0];
		int h = heights[0];
		int[][] take = tiles[0];
		int[][] give = new int[w][h];
		/*
		 * we make a clone array that we give to the map instance
		 */
		for(int j = 0; j < w; ++j) {
			for(int k = 0; k < h; ++k) {
				give[j][k] = take[j][k];
			}
		}
		M create = mapCreate(0, params);
		create.mapSetup(x, y, blockSet, give);
		return create;
	}
	
	public BlockSet<B> getBlockSet() {
		return blockSet;
	}
	
	abstract public M mapCreate(int i, Object... params);
	
	/**
	 * when the map needs to actually be used, call this one. Loads in the map,
	 * 
	 * how much time will it take? Who knows
	 */
	public void load() {
		tiles = new int[sections][][];
		entityTiles = new int[sections][][];
		widths = new int[sections];
		heights = new int[sections];
		
		Thread[] tList = new Thread[sections];
		for(int i = 0; i < sections; ++i) {
			(tList[i] = new Thread(new LoaderThread(i))).run();
		}
		
		for(int i = 0; i < sections; ++i) {
			try {
				tList[i].join();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		
		onLevelLoad();
	}
	
	abstract public void onLevelLoad();
	
	private class LoaderThread implements Runnable {
		int number;
		
		public LoaderThread(int n) {
			number = n;
		}
		
		public void run() {
			
			BufferedImage b = null;
			try {
				b = ImageIO.read(new File(mapImages[number]));
			}catch(Exception ex) {
				ex.printStackTrace();
				System.exit(-1);
			}
			
			int width = b.getWidth();
			int height = b.getHeight(); 
			
			int[] data = new int[width * height];
			b.getRGB(0, 0, width, height, data, 0, width);
			
			tiles[number] = new int[width][height];
			entityTiles[number] = new int[width][height];
			
			//first fill out with -1 (Spooky)
			for(int j = 0; j < width; ++j) {
				for(int k = 0; k < height; ++k) {
					tiles[number][j][k] = -1;
					entityTiles[number][j][k] = -1;
				}
			}
			
			//place the blocks
			int bs = blockSet.getLength();
			Thread[] tList = new Thread[bs];
			for(int i = 0; i < bs; ++i) {
				(tList[i] = new Thread(new PlacerThread(i, width, height, data, tiles[number]))).run();
			}
			//kill threads
			for(int i = 0; i < bs; ++i) {
				try {
					tList[i].join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			
			
			//now we go through and place entities
			bs = blockSet.getEntityLength();
			tList = new Thread[bs];
			for(int i = 0; i < bs; ++i) {
				(tList[i] = new Thread(new EntityThread(i, width, height, data, entityTiles[number]))).run();
			}
			//kill threads
			for(int i = 0; i < bs; ++i) {
				try {
					tList[i].join();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
			
			widths[number] = width;
			heights[number] = height;
		}
	}
	
	private class PlacerThread implements Runnable {
		int block;
		
		int width;
		int height;
		int[] data;
		int[][] array;
		
		public PlacerThread(int b, int w, int h, int[] d, int[][] a) {
			block = b;
			
			width = w;
			height = h;
			data = d;
			array = a;
		}
		
		public void run() {
			int color = blockSet.get(block).colorCode;
			for(int j = 0; j < height; ++j) {
				for(int i = 0; i < width; ++i) {
					
					//now we have to isolate the red channel in the color
					//blocks are in the red channel of course
					int dc = (data[j * width + i] >> 16) & 0xff;
					
					if(dc == color) {
						array[i][j] = block;
					}
				}
			}
		}
	}
	
	private class EntityThread implements Runnable {
		int entity;
		
		int width;
		int height;
		int[] data;
		int[][] array;
		
		public EntityThread(int b, int w, int h, int[] d, int[][] a) {
			entity = b;
			
			width = w;
			height = h;
			data = d;
			array = a;
		}
		
		public void run() {
			int color = blockSet.getEntity(entity);

			for(int j = 0; j < height; ++j) {
				for(int i = 0; i < width; ++i) {
					
					//isolate the green channel to get entities
					int dc = (data[j * width + i] >> 8) & 0xff;
	
					if(dc == color) {
						array[i][j] = entity;
					}
				}
			}
		}
	}
	
}