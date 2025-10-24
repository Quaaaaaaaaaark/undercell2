package undercell;

public class IFChaser extends Lysosome{
	protected String dir;
	protected int lane;
	protected MTimer timer;
	protected boolean hasHurt;
	
	public IFChaser(int lane, String dir, double rt, double wait) {
		super(12,0,0,0,0,rt,wait);
		hasHurt = false;
		this.lane = lane;
		this.dir = dir;
		if (dir.equals("u") || dir.equals("d")) {
			fromX = 25*lane;
			toX = fromX;
			if (dir.equals("u")) {
				fromY = 100;
				toY = 0;
			} else {
				fromY = 0;
				toY = 100;
			}
		} else {
			fromY = 25*lane;
			toY = fromY;
			if (dir.equals("r")) {
				fromX = 0;
				toX = 100;
			} else {
				fromX = 100;
				toX = 0;
			}
		}
	}

}
