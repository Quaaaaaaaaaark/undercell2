package undercell;

public class Oxidizer {
	protected int x, y, startY;
	protected MTimer timer;
	protected boolean toRemove;
	
	public Oxidizer(int x, int sY) {
		this.x = x;
		startY = sY;
		y = sY;
		timer = new MTimer();
		timer.start();
	}
	
	public double get() {
		return timer.getDouble();
	}

}
