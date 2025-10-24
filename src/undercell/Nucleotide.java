package undercell;

public class Nucleotide {
	public static MTimer timer;
	public static int timeToReach = 12;
	protected String type;
	public static int baseSelected;
	
	public Nucleotide(String type) {
		this.type = type;
	}
	
	public String get() {
		return type;
	}
	
	public boolean matches(String text) {
		switch (type) {
		case "A":
			return text.equals("T");
		case "C":
			return text.equals("G");
		case "U":
			return text.equals("A");
		case "G":
			return text.equals("C");
		}
		System.out.println("Got some nucleotide matching issues. Classic. Call the nucleotide repair protein");
		return false;
	}

}
