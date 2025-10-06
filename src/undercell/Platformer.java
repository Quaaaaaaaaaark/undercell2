package undercell;

public class Platformer {
	protected MTimer jTimer;
	protected int x;
	//heart width - 8px
	//screen width - 100px
	//movement - 2px per frame
	
	public Platformer() {
		x = 46;
		jTimer = new MTimer();
	}
	
	//Failed attempt to use logs before I realized that parabolas exist
	/*
	public double getY() {
		if (!jTimer.started || jTimer.getDouble() >= 1) {
			return 8;
		}
		if (jTimer.getDouble() <= 0.5) {
			double val = 8 + 47.0874 + (10.2249*Math.log(jTimer.getDouble()));
			System.out.println(jTimer.get() + Math.log(jTimer.get()));
			return val;
		} else {
			double val2 = 8 + 47.0874 + (10.2249*Math.log(jTimer.getDouble()*-1 + 1));
			System.out.println("option 2, " + val2);
			return val2;
		}
	}
	*/
	
	public double getY() {
		if (!jTimer.started || jTimer.getDouble() >= 1) {
			return 8;
		}
		double u = jTimer.getDouble();
		return 8 + ((-160*Math.pow(u,2))+(160*u));
	}

}
