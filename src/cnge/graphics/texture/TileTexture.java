package cnge.graphics.texture;

public class TileTexture extends Texture {
	
	private float frameWidth;
	private float frameHeight;
	
	public TileTexture(String path, int fw, int ft, TexturePreset tp) {
		init(path, tp.clampHorz, tp.clampVert, tp.nearest);
		frameWidth = 1f/fw;
		frameHeight = 1f/ft;
	}
	
	public TileTexture(String path, int fw, TexturePreset tp) {
		init(path, tp.clampHorz, tp.clampVert, tp.nearest);
		frameWidth = 1f/fw;
		frameHeight = 1;
	}
	
	public TileTexture(String path, int fw, int ft) {
		init(path, TexturePreset.defaultClampHorz, TexturePreset.defaultClampVert, TexturePreset.defaultNearest);
		frameWidth = 1f/fw;
		frameHeight = 1f/ft;
	}
	
	public TileTexture(String path, int fw) {
		init(path, TexturePreset.defaultClampHorz, TexturePreset.defaultClampVert, TexturePreset.defaultNearest);
		frameWidth = 1f/fw;
		frameHeight = 1;
	}
	
	public float getX() {
		return frameWidth;
	}
	
	public float getY() {
		return frameHeight;
	}
	
	public float getZ(int frame) {
		return frame*frameWidth;
	}
	
	public float getW(int frame) {
		return frame*frameHeight;
	}
	
	public float getW() {
		return 0;
	}
	
}