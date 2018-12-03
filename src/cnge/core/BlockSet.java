package cnge.core;

/**
 * this class contains all the blocks a map or level will use
 *
 * @author Plane Emmett
 * 
 * @param <B> - the type of block in this set
 */
public class BlockSet<B extends Block>{
	
	/**
	 * when there is no block in a space in the map, a -1, this block is put there
	 */
	private B defaultBlock;
	/**
	 * the list of blocks, each with a position in the list
	 */
	private B[] blocks;
	//how many blocks (exclusing default)
	private int numBlocks;
	
	/**entities are numbered 0 - n
	 * 
	 * these are their color codes
	 * 
	 * you should differenciate them with constants
	 */
	private int[] entities;
	//how many
	private int numEntities;
	
	/**
	 * constructs a blockset, auto generates ids for blocks
	 * 
	 * @param eColors = the colors representing entities that can be loaded
	 * @param bDefault - the default case block
	 * @param bs - the rest of the blocks
	 */
	@SafeVarargs
	public BlockSet(int[] eColors, B bDefault, B... bs) {
		entities = eColors;
		numEntities = eColors.length;
		defaultBlock = bDefault;
		blocks = bs;
		numBlocks = blocks.length;
		for(int i = 0; i < numBlocks; ++i) {
			blocks[i].setID(i);
		}
	}
	
	/**
	 * gets a block based off its id in this blockset
	 * 
	 * @param id - the block id
	 * @return the block of the ID, the defaultblock if -1
	 */
	public B get(int id) {
		if(id == -1) {
			return defaultBlock;
		}else {
			return blocks[id];
		}
	}
	
	/**
	 * gets an entity's color from loadable entity list
	 * 
	 * @param id - the entity's id (a constant)
	 * 
	 * @return the entity load color of this ID
	 */
	public int getEntity(int id) {
		if(id == -1) {
			return -1;
		}else {
			return entities[id];
		}
	}
	
	/**
	 * gets the number of blocks in this blockSet
	 * 
	 * @return numBlocks
	 */
	public int getLength() {
		return numBlocks;
	}
	
	/**
	 * gets the number of entities
	 * @return
	 */
	public int getEntityLength() {
		return numEntities;
	}
	
}