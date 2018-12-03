package cnge.core.morph;

import cnge.core.Base;
import cnge.core.morph.Morph.Interpolator;

public class CustomMorph {
	
	private double timer;
	private double time;
	
	private float start;
	private float end;
	
	private Interpolator interpolator;
	
	public CustomMorph(float s, float e, Interpolator i, double t) {
		timer = 0;
		time = t;
		
		interpolator = i;
		
		start = s;
		end = e;
	}
	
	public float update() {
		timer += Base.time;
		if(timer > time) {
			timer = time;
		}
		float along = (float)(timer / time);
		return interpolator.interpolate(start, end, along);
	}

	public void reset(float s, float e) {
		timer = 0;
		start = s;
		end = e;
	}
	
	public void setTime(double t) {
		time = t;
	}
	
}