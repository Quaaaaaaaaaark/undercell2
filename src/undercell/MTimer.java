package undercell;

public class MTimer {
	long val;
	boolean started;
	
	public MTimer() {
		started = false;
	}
	
	public MTimer(boolean start) {
		if (start) {
			start();
			started = true;
		} else {
			started = false;
		}
	}
	
	public void start() {
		val = System.nanoTime();
		started = true;
	}
	
	public void start(double wait) {
		val = System.nanoTime() + (long)(wait*1000000000);
		started = true;
	}
	
	public int get() {
		return (int) ((System.nanoTime() - val)/1000000000);
	}
	
	public int getHalf() {
		return (int) ((System.nanoTime() - val)/500000000);
	}
	
	public int getThird() {
		return (int) ((System.nanoTime() - val)/(1000000000/3));
	}
	
	public double getDouble() {
		return ((double)System.nanoTime() - val)/(1000000000.0);
	}
}
