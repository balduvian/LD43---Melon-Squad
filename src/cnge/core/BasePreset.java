package cnge.core;

public class BasePreset {

	/** ONLY USED FOR ASPECT MODES, the screen buffer will be a limited size */
	public static final boolean UNIT_GAME_PIXELS = true;
	
	/** ONLY USED FOR ASPECT MODES, the screen buffer will be the same size as the window's pixels */
	public static final boolean UNIT_SCREEN_PIXELS = false; 

	public Base.Framer screenType;
	public boolean pixelType;
	public int width;
	public int height;
	public int limit;
	
	/**
	 * use this one for a game that has a fixed or expandable virtual ratio without bound
	 * @param s - screen mode
	 * @param a - pixel perfect or not
	 * @param vw - virtual displayed width
	 * @param vh - virtual dusplayed height
	 * @param gu - game unit
	 */
	public BasePreset(Base.Framer s, boolean a, int vw, int vh) {
		screenType = s;
		pixelType = a;
		width = vw;
		height = vh;
	}
	
	/**
	 * use this one for an expandable ratio with a limit to maximum width/height
	 * @param s
	 * @param a
	 * @param vw
	 * @param vh
	 * @param gu
	 * @param li - the maximum width/height depending on what screen mode
	 */
	public BasePreset(Base.Framer s, boolean a, int vw, int vh, int li) {
		screenType = s;
		pixelType = a;
		width = vw;
		height = vh;
		limit = li;
	}
	
	/**
	 * use this one for pixel screenspace
	 */
	public BasePreset() {
		screenType = Base.PIXEL_FRAMER;
		pixelType = UNIT_SCREEN_PIXELS;
		width = 1;
		height = 1;
		limit = 1;
	}
}