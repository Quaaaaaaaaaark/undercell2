package undercell;

public class CircleBox extends HeartBox{
	protected int degrees;
	
	public CircleBox() {
		super(46, 21);
		degrees = 90;
	}
	
	public void addDegrees(int toAdd) {
		degrees += toAdd;
		if (degrees < 0) {
			degrees += 360;
		} else if (degrees >= 360) {
			degrees -= 360;
		}
	}
	
	public int getDegrees() {
		return degrees;
	}

}
