package cnge.core.animation;

import cnge.core.Base;

abstract public class Anim {
	
	protected double timer;
	protected double[] frameTimes;
	protected int frame;
	protected int numFrames;
	protected AnimEvent[] events;
	
	public Anim(double[] f) {
		frameTimes = f;
		numFrames = f.length;
		events = new AnimEvent[numFrames];
		timer = 0;
		frame = 0;
	}
	
	public void addEvent(int i, AnimEvent e) {
		events[i] = e;
	}
	
	abstract public int getX();
	
	public void update() {
		timer += Base.time;
		if(timer >= frameTimes[frame]) {
			++frame;
			frame %= numFrames;
			timer = 0;
			if(events[frame] != null) {
				events[frame].event();
			}
		}
	}
	
	public void reset() {
		frame = 0;
		timer = 0;
	}
}