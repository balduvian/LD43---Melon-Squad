package cnge.graphics.texture;

/**
 * this is used to pass in parameters into a texture in a modular way, where you only need to give what's different than the default option
 * 
 * make you set the defaults to this class in main or per scene
 * 
 * @author Emmet
 */
public class TexturePreset {
	
	public static boolean defaultClampHorz;
	public static boolean defaultClampVert;
	public static boolean defaultNearest;
	
	public boolean clampHorz;
	public boolean clampVert;
	public boolean nearest;
	
	//TODO big and skeker
	
	/**
	 * use this to set the default parameters for textures
	 * 
	 * @param fw - the default frames wide
	 * @param ft - the default frames tall
	 * @param ch - the default clamp horizontal
	 * @param cv - the default clamp vertical
	 * @param nr - the default nearest neighbor interpolation
	 */
	public static void setDefaults(boolean ch, boolean cv, boolean nr) {
		defaultClampHorz = ch;
		defaultClampVert = cv;
		defaultNearest = nr;
	}
	
	/**
	 * constructs a new texture preset, automatically set with default values
	 * 
	 * now add the setting methods after this to set parameters that are other than default
	 */
	public TexturePreset() {
		clampHorz = defaultClampHorz;
		clampVert = defaultClampVert;
		nearest = defaultNearest;
	}
	
	public TexturePreset clampHorz(boolean ch) {
		clampHorz = ch;
		return this;
	}
	
	public TexturePreset clampVert(boolean cv) {
		clampVert = cv;
		return this;
	}
	
	public TexturePreset nearest(boolean nr) {
		nearest = nr;
		return this;
	}
}