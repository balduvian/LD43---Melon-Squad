package cnge.core;

/**
 * YOOOOOOOOOOOOOOOOOOOOOOOOOO
 * @author Emmett
 *
 */
public class Timer {
	
	private double time;
	private double timer;
	
	private boolean going;
	
	private TimerEvent event;
	
	public Timer(double t, TimerEvent e) {
		time = t;
		event = e;
	}
	
	public interface TimerEvent {
		public void event();
	}
	
	public void update() {
		if(going) {
			timer -= Base.time;
			if(timer <= 0) {
				timer = 0;
				going = false;
				event.event();
			}
		}
	}
	
	public void forceEnd() {
		timer = 0;
	}
	
	public void start() {
		timer = time;
		going = true;
	}
	
	public void pause() {
		going = false;
	}
	
	public void resume() {
		going = true;
	}
	
	public void reset() {
		going = false;
		timer = time;
	}
	
	public void setTime(double t) {
		time = t;
	}
	
	public float getTimer() {
		return 1 - (float)(timer / time);
	}

	public boolean paused() {
		return !going;
	}
	
}