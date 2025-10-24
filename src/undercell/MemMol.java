package undercell;

public class MemMol extends Lysosome{
	protected boolean good, bounced;
	
	//ublic Lysosome(double radius, int fromX, int fromY, int toX, int toY, double rt, double wait
	
	public MemMol(boolean good, int fromX, int toX, double wait) {
		super(5, fromX, -5, toX, 105, 2, wait);
		this.good = good;
		bounced = false;
	}
}
