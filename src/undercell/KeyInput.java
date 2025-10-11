package undercell;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	protected Main main;
	protected int code;
	protected mGraphics jonathan;
	public static boolean left, right, up, down;
	
	public KeyInput(Main main, mGraphics jonathan) {
		this.main = main;
		this.jonathan = jonathan;
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	/*
	Key Codes:
	38 = up
	39 = right
	37 = left
	40 = down
	32 = space
	10 = enter
	*/
	
	public void keyPressed(KeyEvent e) {
		if (Main.substate.equals("textbox")) {
			if (mGraphics.animCounter < main.textList.get(0).length()*1.5) {
				mGraphics.animCounter = mGraphics.round(main.textList.get(0).length()*1.5);
				System.out.println(main.textList.get(0));
			} else {
				main.nextText();
			}
		} else if (Main.substate.equals("quiz")) {
			code = e.getKeyCode();
			System.out.println(code);
			switch (code) {
			case 37:
				Main.nextQuiz.selectedLeft();
				return;
			case 38:
				Main.nextQuiz.selectedUp();
				return;
			case 39:
				Main.nextQuiz.selectedRight();
				return;
			case 40:
				Main.nextQuiz.selectedDown();
				return;
			case 32:
			case 10:
				Main.substate = "quizAnswers";
				jonathan.startAnswersTimer();
				mGraphics.animCounter = 0;
			}
		} else if (Main.substate.equals("game")) {
			switch (Main.status) {
			case "golgiGame":
				code = e.getKeyCode();
				runGolgiGame();
				break;
			case "vesicleGame":
				code = e.getKeyCode();
				runVesicleGame();
				break;
			case "nucleusGame":
				code = e.getKeyCode();
				runNucleusGame();
				break;
			case "globuleGame":
				code = e.getKeyCode();
				runGlobuleGame();
				break;
			case "mitoGame":
				code = e.getKeyCode();
				runMitoGame();
				break;
			case "lysoGame":
				code = e.getKeyCode();
				runLysoGame();
				break;
			case "erGame":
				code = e.getKeyCode();
				runERGame();
				break;
			}
		}
	}
	
	public void runGolgiGame() {
		switch (code) {
		case 37:
			if (jonathan.orbs.get(1).equals("left")) {
				jonathan.orbs.remove(0);
				if (jonathan.orbs.size() == 1) {
					Main.substate = "postgame";
					jonathan.timer.start();
				}
			}
			return;
		case 38:
			if (jonathan.orbs.get(1).equals("up")) {
				jonathan.orbs.remove(0);
				if (jonathan.orbs.size() == 1) {
					Main.substate = "postgame";
					jonathan.timer.start();
				}
			}
			return;
		case 39:
			if (jonathan.orbs.get(1).equals("right")) {
				jonathan.orbs.remove(0);
				if (jonathan.orbs.size() == 1) {
					Main.substate = "postgame";
					jonathan.timer.start();
				}
			}
			return;
		case 40:
			if (jonathan.orbs.get(1).equals("down")) {
				jonathan.orbs.remove(0);
				if (jonathan.orbs.size() == 1) {
					Main.substate = "postgame";
					jonathan.timer.start();
				}
			}
			return;
		}
	}
	
	public void runVesicleGame() {
		switch (code) {
		case 39:
			if (VesicleStats.lane != 3 && VesicleStats.timeSinceMoved > 8) {
				VesicleStats.timeSinceMoved = 0;
				VesicleStats.moveDir = 1;
			}
			return;
		case 37:
			if (VesicleStats.lane != 1 && VesicleStats.timeSinceMoved > 8) {
				VesicleStats.timeSinceMoved = 0;
				VesicleStats.moveDir = -1;
			}
			return;
		}
	}
	
	public void runNucleusGame() {
		boolean found = false;
		switch (code) {
		case 38:
			for (DanceArrow a : jonathan.arrows) {
				if (a.dir.equals("u") && a.get() >= DanceArrow.hitBoundL && a.get() <= DanceArrow.hitBoundU) {
					a.toRemove = true;
					found = true;
				}
			}
			break;
		case 37:
			for (DanceArrow a : jonathan.arrows) {
				if (a.dir.equals("l") && a.get() >= DanceArrow.hitBoundL && a.get() <= DanceArrow.hitBoundU) {
					a.toRemove = true;
					found = true;
				}
			}
			break;
		case 39:
			for (DanceArrow a: jonathan.arrows) {
				if (a.dir.equals("r") && a.get() >= DanceArrow.hitBoundL && a.get() <= DanceArrow.hitBoundU) {
					a.toRemove = true;
					found = true;
				}
			}
			break;
		case 40:
			for (DanceArrow a: jonathan.arrows) {
				if (a.dir.equals("d") && a.get() >= DanceArrow.hitBoundL && a.get() <= DanceArrow.hitBoundU) {
					a.toRemove = true;
					found = true;
				}
			}
			break;
		}
		if (!found) {
			Main.health--;
			if (Main.health == 0) {
				Main.substate = "dead";
			}
		}
	}
	
	public void runGlobuleGame() {
		switch (code) {
		case 37:
			Globby.def = "l";
			break;
		case 38:
			Globby.def = "u";
			break;
		case 39:
			Globby.def = "r";
			break;
		case 40:
			Globby.def = "d";
			break;
		}
	}
	
	public void runMitoGame() {
		switch (code) {
		case 37:
			left = true;
			break;
		case 38:
		case 32:
			if (!jonathan.jumpy.jTimer.started || jonathan.jumpy.jTimer.get() >= 1)
			jonathan.jumpy.jTimer.start();
			break;
		case 39:
			right = true;
			break;
		}
	}
	
	public void runLysoGame() {
		switch (code) {
		case 37: 
			left = true;
			break;
		case 38: 
			up = true;
			break;
		case 39: 
			right = true;
			break;
		case 40: 
			down = true;
			break;
		}
	}
	
	public void runERGame() {
		switch (code) {
		case 37:
			left = true;
			break;
		case 39:
			right = true;
			break;
		}
	}
	
	/*
	Key Codes:
	38 = up
	39 = right
	37 = left
	40 = down
	32 = space
	10 = enter
	*/
	

	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 37:
			left = false;
			break;
		case 38:
			up = false;
			break;
		case 39:
			right = false;
			break;
		case 40:
			down = false;
			break;
		}
	}

}