package undercell;

public class VesicleStats {
	
	public static int lane;
	public static int timeSinceMoved;
	public static int moveDir;
	
	public static void updateTimeSinceMoved() {
		timeSinceMoved++;
		if (timeSinceMoved == 4) {
			lane += moveDir;
		}
	}

}
