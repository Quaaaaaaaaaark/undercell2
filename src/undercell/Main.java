package undercell;

import javax.swing.JFrame;

import java.awt.KeyboardFocusManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	protected JFrame frame;
	protected mGraphics jonathan;
	protected ArrayList<String> textList;
	protected File file;
	protected Scanner scan;
	public static String status;
	public static String substate;
	protected KeyInput keyStuffs;
	protected MouseInput mouseStuffs;
	public static Quiz nextQuiz;
	public static String postText;
	public static int knowledge;
	public static int prevKnowledge;
	public static int health;
	public static MTimer iTimer;
	
	
	public Main() {
		frame = new JFrame("Framename");
		textList = new ArrayList<String>();
		jonathan = new mGraphics(this, frame, textList);
		jonathan.start();
		knowledge = 0;
		prevKnowledge = 0;
		health = 8;
		Main.status = "mapSelect";
		Main.substate = "menu"; // switch to textbox to have game work
		addToTextList("intfilOpening");
		frame.add(jonathan);
		frame.pack();
		frame.setVisible(true);
		frame.setSize(960,540);
		frame.setResizable(true);
		frame.setLocation(270,190);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		keyStuffs = new KeyInput(this, jonathan);
		mouseStuffs = new MouseInput(jonathan);
		jonathan.addKeyListener(keyStuffs);
		jonathan.addMouseListener(mouseStuffs);
		jonathan.addMouseMotionListener(mouseStuffs);
		jonathan.addMouseWheelListener(mouseStuffs);
		iTimer = new MTimer();
		iTimer.start();
	}
	
	public void addToTextList(String address) {
		file = new File("texts/" + status.substring(0,status.length()-4) + "/" + address + ".json");
		try {
			scan = new Scanner(file);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Ohhhhh boy here it goes again");
		}
		boolean notDone = true;
		while (notDone) {
			String entry = scan.nextLine();
			if (entry.equals("STOP")) {
				notDone = false;
			} else {
				textList.add(entry);
			}
		}
		String next = scan.nextLine();
		if (next.equals("QUIZ")) {
			postText = "quiz";
			nextQuiz = new Quiz(scan.nextLine(), scan.nextLine(), scan.nextLine(), scan.nextLine(), scan.nextInt());
			scan.nextLine();
			nextQuiz.addPostQuiz(scan.nextLine(), scan.nextLine());
		} else if (next.equals("GAME")) {
			postText = "game:" + scan.nextLine();
		} else if (next.equals("WIN")) {
			postText = "win:" + scan.nextLine();
		}
		mGraphics.animCounter = 0;
	}
	
	public void addRandomlyToList(String address) {
		file = new File("texts/" + status.substring(0,status.length()-4) + "/" + address + ".json");
		try {
			scan = new Scanner(file);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ohhhhh boy here it goes again 2: random edition");
		}
		boolean notDone = true;
		ArrayList<String> options = new ArrayList<String>();
		while (notDone) {
			String entry = scan.nextLine();
			if (entry.equals("STOP")) {
				notDone = false;
			} else {
				options.add(entry);
			}
		}
		textList.add(options.get(random(0,options.size()-1)));
		postText = scan.nextLine();
	}
	
	public void nextText() {
		textList.remove(0);
		mGraphics.animCounter = 0;
	}

	public static void main(String[] args) {
		Main main = new Main();

	}
	
	public static boolean canBeHurt() {
		if (Main.status.equals("intfilGame") && iTimer.get() >= 2) {
			return true;
		} else if (Main.status.equals("lysoGame") && iTimer.get() >= 1.5) {
			return true;
		}  else if (iTimer.get() >= 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public static int random(int min, int max) {
		return (int) (Math.random() * (max-min+1)) + min;
	}

}
