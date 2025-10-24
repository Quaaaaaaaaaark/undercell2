package undercell;

public class ShootyProt extends Lysosome{
	protected boolean oxidized;
	//(double rad, int fX, int fY, int tX, int tY, double rt)
	
	public ShootyProt(int fX, int tX, double rt, double wait) {
		super(4, fX, 0, tX, 100, rt, wait);
		oxidized = false;
	}
	
	public ShootyProt (double rad, int fX, int tX, double rt, double wait) {
		super(rad, fX, 0, tX, 100, rt, wait);
		oxidized = false;
	}
	

}
