package game;

import cnge.core.Block;
import cnge.graphics.texture.TileTexture;

public class MelonBlock extends Block {

	public boolean solid;
	
	public TileTexture texture;
	public int texX;
	public int texY;
	
	/**
	 * 
	 * @param cc - color code
	 * @param l - layer
	 * @param s - solid
	 * @param t - texture
	 * @param x - texture x
	 * @param y - texture y
	 */
	public MelonBlock(int cc, int l, boolean s, TileTexture t, int x, int y) {
		super(cc, l);
		solid = s;
		texture = t;
		texX = x;
		texY = y;
	}

}