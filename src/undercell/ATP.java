package undercell;

public class ATP {
	protected MTimer timer;
	protected String dir;
	protected double x;
	protected int y;
	protected boolean toRemove;
	
	public ATP(String dir, int y) {
		toRemove = false;
		this.dir = dir;
		if (this.dir.equals("r")) {
			x = -8;
		} else {
			x = 100;
		}
		this.y = y;
		timer = new MTimer();
		timer.start();
	}

	public ATP(String dir, int y, double wait) {
		toRemove = false;
		this.dir = dir;
		this.y = y;
		timer = new MTimer();
		timer.start(wait);
	}
	
	public double get() {
		return timer.getDouble();
	}
	
}
