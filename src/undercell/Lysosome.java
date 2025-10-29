package undercell;

public class Lysosome {
	protected MTimer timer;
	protected int x, y, fromX, fromY, toX, toY;
	protected boolean toRemove;
	protected double radius, runTime;
	boolean chaser;
	boolean circle;
	String iAddress;
	
	//standard 16px, large 32px
	//movement 8px/s
	
	public Lysosome(double rad, int fX, int fY, int tX, int tY, double rt) {
		this(rad, fX, fY, tX, tY, rt, 0);
	}
	
	public Lysosome(double radius, int fromX, int fromY, int toX, int toY, double rt, double wait) {
		chaser = false;
		circle = false;
		toRemove = false;
		this.radius = radius;
		this.fromX = fromX;
		this.toX = toX;
		this.fromY = fromY;
		this.toY = toY;
		runTime = rt;
		timer = new MTimer();
		timer.start(wait);
	}
	
	public Lysosome(double radius, double rt, double wait) {
		chaser = true;
		toRemove = false;
		circle = false;
		this.radius = radius;
		runTime = rt;
		int mod = 1;
		if (Main.random(0,1) == 0) {
			mod = -1;
		}
		if (Main.random(0,1) == 0) {
			toX = Main.random(0,100);
			if (Main.random(0,1) == 0) {
				toY = -50;
				fromY = -80;
			} else {
				toY = 150;
				toY = 180;
			}
			fromX = toX + (Main.random(0,25)*mod);
		} else {
			toY = Main.random(0,100);
			if (Main.random(0,1) == 0) {
				toX = -50;
				fromX = -80;
			} else {
				toX = 150;
				fromX = 180;
			}
			fromY = toY + (Main.random(0,25)*mod);
		}
		timer = new MTimer();
		timer.start(wait);
	}
	
	public Lysosome(double radius, double rt, double wait, boolean notImportant) {
		chaser = false;
		toRemove = false;
		circle = true;
		fromX = -100;
		toX = -100;
		fromY = -100;
		toX = -100;
		this.radius = radius;
		runTime = rt;
		timer = new MTimer();
		timer.start(wait);
	}
	
	public double get(){
		return timer.getDouble();
	}

}
