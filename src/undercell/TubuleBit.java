package undercell;

public class TubuleBit extends Lysosome{
	
	//(double rad, int fX, int fY, int tX, int tY, double rt)
	
	public TubuleBit(int deg, double rt, double wait) {
		super(10, 50 + round(Math.cos(Math.toRadians(deg))*50), 50 - round(Math.sin(Math.toRadians(deg))*50), 50,50, rt/2, wait);
		this.iAddress = mGraphics.randomProteinImage();
	}
	
	public TubuleBit (double rad, int deg, double rt, double wait) {
		super(rad, 50 + round(Math.cos(Math.toRadians(deg))*50), 50 - round(Math.sin(Math.toRadians(deg))*50), 50,50, rt/2, wait);
		this.iAddress = mGraphics.randomProteinImage();
		}
	
	public static int round(double d) {
		return (int) (d+0.5);
	}

}
