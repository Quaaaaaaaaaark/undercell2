package undercell;

import javax.swing.plaf.synth.SynthInternalFrameUI;

public class DanceArrow {
	protected String dir;
	protected MTimer timer;
	public static double timeToReach = 2;
	public static double hitBoundU = 2.2;
	public static double hitBoundL = 1.8;
	public static double removeBound = 2.5;
	protected boolean toRemove;
	
	public DanceArrow(String dir) {
		this.dir = dir;
		toRemove = false;
		timer = new MTimer();
		timer.start();
	}
	
	public DanceArrow(String dir, double wait) {
		this.dir = dir;
		toRemove = false;
		timer = new MTimer();
		timer.start(wait);
	}
	
	public double get() {
		return timer.getDouble();
	}

}
