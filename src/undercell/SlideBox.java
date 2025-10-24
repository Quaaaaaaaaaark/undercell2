package undercell;

public class SlideBox extends HeartBox{
	protected String dir;
	
	public SlideBox() {
		super(92);
		dir = "N";
	}
	
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public void swapDir() {
		switch (dir) {
		case "N":
			dir = "S";
			break;
		case "S":
			dir = "N";
			break;
		case "E":
			dir = "W";
			break;
		case "W":
			dir = "E";
			break;
		}
	}

}
