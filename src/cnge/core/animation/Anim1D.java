package cnge.core.animation;

/**
 * an animation that returns a frame in the horizontal direction
 * 
 * @author Emmet
 *
 */
public class Anim1D extends Anim{

	private int[] frames;
	
	public Anim1D(int[] fr, double[] f) {
		super(f);
		frames = fr;
	}

	public int getX() {
		return frames[frame];
	}

}