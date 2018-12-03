package cnge.core;

public class Hitbox {
	
	public float x;
	public float y;
	public float width;
	public float height;
	
	public Hitbox(float i, float j, float w, float h) {
		x = i;
		y = j;
		width = w;
		height = h;
	}
	
	/**
	 * checks if this box is colliding with another box
	 * 
	 * @param p - the other box
	 * @return if it's intersecting the other box or not
	 */
	public boolean intersects(float xOff, float yOff, Hitbox p) {
		return p.x + xOff < x + width && p.x + p.width + xOff > x && p.y + yOff < y + height && p.y + p.height + yOff > width;
	}
	
}