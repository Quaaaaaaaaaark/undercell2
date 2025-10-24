package undercell;

public class GridBox{
	protected int x, y, prevX, prevY, useX, useY;
	protected boolean moving;
	protected MTimer timer;
	
	public GridBox() {
		x = 2;
		y = 2;
		prevX = x;
		prevY = y;
		useX = x;
		useY = y;
		moving = false;
		timer = new MTimer();
	}
	
	public void start() {
		timer.start();
	}
	
	public double get() {
		return timer.getDouble();
	}

}
