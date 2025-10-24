package undercell;

public class NotPipe {
	protected int x, topY, bottomY;
	protected MTimer timer;
	protected double wait;
	protected int stoppedX;
	
	public NotPipe(int topY, int bottomY, double wait) {
		this.topY = topY;
		this.bottomY = bottomY;
		this.wait = wait;
		stoppedX = -1;
		x = -1;
	}
	
	public double get() {
		if (timer == null){
			return -1;
		} else {
			return timer.getDouble();
		}
	}
	
	public void start() {
		timer = new MTimer();
		System.out.println("IM ALIVE");
		timer.start(wait);
	}
	
	public void lock() {
		if (x == -1) {
			stoppedX = 200;
		} else {
			stoppedX = x;
		}
	}
}
