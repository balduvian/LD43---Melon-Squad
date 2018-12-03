package cnge.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cnge.graphics.Camera;
import cnge.graphics.texture.TileTexture;

abstract public class Font {
	
	protected static Camera camera;
	
	protected TileTexture texture;
	
	protected int texWidth;
	protected int texHeight;
	protected int cellWidth;
	protected int cellHeight;
	protected int across;
	protected int down;
	protected int total;
	protected int startChar;
	/* name would go here */
	protected int fontHeight;
	protected int defaultWidth;
	//////
	protected int globalOffsetX;
	
	protected int[][] widths;
	protected int[][] widthOffsets;
	protected int[][] xOffsets;
	protected int[][] yOffsets;

	public Font(String texPath, String dataPath) {
		loadData(dataPath);
		texture = new TileTexture(texPath, across, down);
	}
	
	public static void giveCamera(Camera c) {
		camera = c;
	}
	
	/**
	 * called once per char
	 * 
	 * @param cx - the tile x of the char in the texture
	 * @param cy - the tile y of the char in the texture
	 */
	protected abstract void charRender(int cx, int cy, float left, float right, float up, float down, float alpha);
	
	/**
	 * 
	 */
	public void render(char[] sequence, float x, float y, float scale, boolean centered, float alpha) {
		int len = sequence.length;
		x -= globalOffsetX;
		float baseX = x;
		if(centered) {
			float totalWidth = 0;
			for(int i = 0; i < len; ++i) {
				char c = sequence[i];
				totalWidth += widths[c % across][c / across];
			}
			totalWidth *= scale;
			x -= (totalWidth / 2);
		}
		float width = cellWidth * scale;
		float height = cellHeight * scale;
		for(int i = 0; i < len; ++i) {
			char c = sequence[i];
			charRender(c % across, c / across, x, x + width, y, y + height, alpha);
			x += widths[c % across][c / across] * scale;
		}
	}
	
	public void render(char[] sequence, float x, float y, float scale, boolean centered) {
		render(sequence, x, y, scale, centered, 1);
	}
	
	/**
	 * this is so that i don't have to put try catches everywhere
	 * @param is - a file input stream
	 * @return whatever inputstream.read returns
	 */
	private char read(FileInputStream is) {
		try {
			return (char) is.read();
		} catch (IOException ex) {
			System.exit(-1);
			return 0;
		}
	}
	
	/**
	 * converts chars that spell out a base 10 number to the number that it represents.
	 * 
	 * only up to 999, so an array of a length 3
	 * 
	 * @param c - the char array, length = 3
	 * @return the int
	 */
	private int intFromChars(char[] c) {
		return (c[0] - 48) * 100 + (c[1] - 48) * 10 + (c[2] - 48);
	}
	
	private char[] collectLine(FileInputStream is) {
		char[] temp = new char[] {48, 48, 48};
		char now = 0;
		boolean reading = false;
		int along = 0;
		//13 is the new line value
		while ((now = read(is)) != 13) {
			if(now == ',') {
				reading = true;
			} else if (reading) {
				temp[along] = now;
				++along;
			}
		}
		//now we have to rearrange the digits if we didn't read a 3 digit number
		//or else we have 1s or 10s in the 100s place
		//if we have a two digit number
		if(along == 2) {
			temp[2] = temp[1];
			temp[1] = temp[0];
			temp[0] = 48;
		//if we have a one digit number
		} else if (along == 1) {
			temp[2] = temp[0];
			temp[0] = 48;
		}
		return temp;
	}
	
	/**
	 * returns the number at the end of the line the fileInputStream is on
	 * 
	 * @param is - input stream
	 * @return the number at the end of this line
	 */
	private int intLine(FileInputStream is) {
		return intFromChars(collectLine(is));
	}
	
	/**
	 * nothing to gain from this line
	 * 
	 * @param is - input stream to skip a line on
	 */
	private void skipLine(FileInputStream is) {
		while (read(is) != 13);
	}
	
	private void loadData(String path) {
		
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(path));
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
		
		texWidth = intLine(inputStream);
		texHeight = intLine(inputStream);
		cellWidth = intLine(inputStream);
		cellHeight = intLine(inputStream);
		across = texWidth / cellWidth;
		down = texHeight / cellHeight;
		total = across * down;
		startChar = intLine(inputStream);
		skipLine(inputStream); /* SKIP NAME LINE */
		fontHeight = intLine(inputStream);
		defaultWidth = intLine(inputStream);
		
		widths = new int[across][down];
		widthOffsets = new int[across][down];
		xOffsets = new int[across][down];
		yOffsets = new int[across][down];
		
		for(int j = 0; j < down; ++j) {
			for(int i = 0; i < across; ++i) {
				widths[i][j] = intLine(inputStream);
			}
		}
		
		for(int j = 0; j < down; ++j) {
			for(int i = 0; i < across; ++i) {
				widthOffsets[i][j] = intLine(inputStream);
			}
		}
		
		for(int j = 0; j < down; ++j) {
			for(int i = 0; i < across; ++i) {
				xOffsets[i][j] = intLine(inputStream);
			}
		}
		
		for(int j = 0; j < down; ++j) {
			for(int i = 0; i < across; ++i) {
				yOffsets[i][j] = intLine(inputStream);
			}
		}
		
		skipLine(inputStream); /* SKIP GLOBAL WIDTH */
		globalOffsetX = intLine(inputStream);
		
		try {
			inputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			System.exit(-1);
		}
	}
	
}