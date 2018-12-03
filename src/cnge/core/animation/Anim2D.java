package cnge.core.animation;

/**
 * an animation that returns a frame in a 2d tile sheet
 * 
 * @author Emmet
 *
 */
public class Anim2D extends Anim {

	int[][] frames;
	
	public Anim2D(int[][] fr, double[] f) {
		super(f);
		frames = fr;
	}

	public int getX() {
		return frames[frame][0];
	}
	
	public int getY() {
		return frames[frame][1];
	}

}