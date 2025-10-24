package undercell;

public class HeartBox {
	protected int x, y;
	
	public HeartBox() {
		x = 46;
		y = 46;
	}
	
	public HeartBox(int y) {
		x = 46;
		this.y = y;
	}
	
	public HeartBox(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setDir(String d) {
		
	}
	
	public void swapDir() {
		
	}
	
	public void addDegrees(int toAdd) {
		
	}
	
	public int getDegrees() {
		System.out.println("Ohhhhh boy this is some absurd trig I do NOT want to deal with");
		return -1;
	}

}
