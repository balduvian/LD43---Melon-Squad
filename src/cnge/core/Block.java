package cnge.core;

/**
 * used by the map to define types of tiles.
 * 
 * this superclass is very basic, so you should extend it with your own class in game
 * 
 * @author Emmet
 */
public class Block {
	
	/**
	 * when loading maps from images, which color cooresponds to this block
	 */
	public int colorCode;
	
	/**
	 * which layer this block renders on
	 */
	public int layer;
	
	/**
	 * an integer that identifies the block, fill in with a known constant
	 */
	public int id;
	
	/**
	 * constructor for all blocks
	 * @param cc - color code for block type
	 * @param l - layer for block type
	 */
	public Block(int cc, int l) {
		colorCode = cc;
		layer = l;
	}
	
	/**
	 * this should be automatically done by the blockset
	 */
	public void setID(int i) {
		id = i;
	}
	
}