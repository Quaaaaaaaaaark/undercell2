package undercell;

public class CytoBlock extends Lysosome{
	
	//ouble radius, int fromX, int fromY, int toX, int toY, double rt, double wait
	
	public CytoBlock(double radius, int fromX, int fromY, int toX, int toY, double rt, double wait) {
		super(radius, fromX, fromY, toX, toY, rt, wait);
		this.iAddress = mGraphics.randomProteinImage();
	}

}
