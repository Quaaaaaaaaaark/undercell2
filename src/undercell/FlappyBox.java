package undercell;

public class FlappyBox {
	protected int x, y, savedY;
	protected MTimer timer;
	public static double speed = 2;
	protected int ouchY;
	
	public FlappyBox() {
		x = 21;
		y = 46;
		timer = new MTimer();
	}
	
	public double get() {
		return timer.getDouble();
	}

}
