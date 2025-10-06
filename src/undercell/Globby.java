package undercell;

public class Globby {
	protected String dir;
	protected int dest;
	protected MTimer timer;
	public static double runTime = 1;
	protected boolean currentlyEven;
	protected int hits;
	protected String from;
	protected int fromNum;
	public static String def;
	protected boolean toRemove;
	
	public Globby() {
		currentlyEven = true;
		dir = randomDir();
		hits = 0;
		from = "center";
		fromNum = -1;
		dest = Main.random(1,3);
		toRemove = false;
		timer = new MTimer();
		timer.start();
	}
	
	public Globby(double wait) {
		currentlyEven = true;
		dir = randomDir();
		hits = 0;
		dest = Main.random(1,3);
		from = "center";
		fromNum = -1;
		toRemove = false;
		timer = new MTimer();
		timer.start(wait);
	}
	
	public double get() {
		return timer.getDouble();
	}
	
	public String randomDir() {
		switch (Main.random(0,3)) {
		case 0:
			return "u";
		case 1:
			return "l";
		case 2:
			return "r";
		case 3:
			return "d";
		}
		return null;
	}
	
	public void newDest() {
		from = dir;
		fromNum = dest;
		dest = Main.random(1,3);
		while (from.equals(dir)) {
			dir = randomDir();
		}
	}

}
