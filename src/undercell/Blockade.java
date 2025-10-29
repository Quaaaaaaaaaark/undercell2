package undercell;

public class Blockade {
	int lane;
	MTimer timer;
	public static int timeToHit;
	public static double removeBound, hitBoundL, hitBoundU;
	public static boolean canChange;
	protected boolean toRemove;
	String image;
	
	public Blockade(int num) {
		lane = num;
		toRemove = false;
		timer = new MTimer();
		timer.start();	
		image = mGraphics.randomProteinImage();
	}
	
	public Blockade(int num, double wait) {
		lane = num;
		toRemove = false;
		timer = new MTimer();
		timer.start(wait);
		image = mGraphics.randomProteinImage();
	}
	
	public double get() {
		return timer.getDouble();
	}

}
