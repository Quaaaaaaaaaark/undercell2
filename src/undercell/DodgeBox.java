package undercell;

public class DodgeBox {
	protected MTimer timer;
	String dir;
	
	public DodgeBox() {
		timer = new MTimer();
		timer.start(-5);
		dir = "N";
	}
	
	public void jump(String dir) {
		timer.start();
		this.dir = dir;
	}

}
