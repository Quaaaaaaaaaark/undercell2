package undercell;

public class DodgeBar {
	protected MTimer timer;
	protected String dir, stretch;
	protected boolean hasHurt;
	public static double runTime = 2;
	
	public DodgeBar(String dir, String stretch, double wait) {
		this.dir = dir;
		this.stretch = stretch;
		hasHurt = false;
		timer = new MTimer();
		timer.start(wait);
	}
	
	public double get() {
		return timer.getDouble();
	}
	
	public boolean shouldHurt(DodgeBox dodgey) {
		System.out.println(dodgey.timer.getDouble());
		System.out.println(avoidsDir(dodgey.dir));
		if (dodgey.timer.getDouble() >= .1 && dodgey.timer.getDouble() <= .9 && avoidsDir(dodgey.dir)) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean avoidsDir(String d) {
		switch (d) {
			case "N":
				return stretch.equals("S");
			case "S":
				return stretch.equals("N");
			case "E":
				return stretch.equals("W");
			case "W":
				return stretch.equals("E");
		}
		System.out.println("bad stretchy! BAD STRETCHY!");
		return false;
	}
	
	public boolean equals(DodgeBar d) {
		return dir.equals(d.dir) && stretch.equals(d.stretch);
	}

}
