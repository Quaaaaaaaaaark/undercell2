package undercell;

public class FallProt extends Lysosome{
	boolean bonus;
	
	//double radius, int fromX, int fromY, int toX, int toY, double rt, double wait
	public FallProt(int x, double rt, double wait) {
		super(5, x, -50, x, 150, rt, wait);
		bonus = false;
		this.iAddress = mGraphics.randomProteinImage();
	}
	
	public FallProt(int fromX, int toX, double rt, double wait) {
		super(5, fromX, -50, toX, 150, rt, wait);
		bonus = false;
		this.iAddress = mGraphics.randomProteinImage();
	}
	
	public FallProt(int fromX, int fromY, int toX, int toY, double rt, double wait) {
		super(5, fromX, fromY, toX, toY, rt, wait);
		bonus = false;
		this.iAddress = mGraphics.randomProteinImage();
	}
	
	public FallProt(int x, double rt, double wait, boolean isChaser, boolean bonus) {
		this(x, rt, wait);
		if (isChaser) {
			this.chaser = true;
		} else {
			this.circle = true;
			this.bonus = bonus;
		}
		this.iAddress = mGraphics.randomProteinImage();
	}

}
