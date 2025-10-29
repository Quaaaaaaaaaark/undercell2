package undercell;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter{
	protected Main main;
	protected int code;
	protected mGraphics jonathan;
	public static boolean left, right, up, down, space;
	public static int mostRecent;
	
	public KeyInput(Main main, mGraphics jonathan) {
		this.main = main;
		this.jonathan = jonathan;
		left = false;
		right = false;
		up = false;
		down = false;
		space = false;
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
		System.out.println("key");
		if (Main.substate.equals("textbox")) {
			if (main.textList.size() != 0 && mGraphics.animCounter < main.textList.get(0).length()*1.5) {
				mGraphics.animCounter = mGraphics.round(main.textList.get(0).length()*1.5);
			} else {
				main.nextText();
			}
		} else if (Main.substate.equals("menu")) {
			code = e.getKeyCode();
			System.out.println("Test");
			switch (code) {
			
			case 38:
				jonathan.menu_selector_i = jonathan.menu_selector_i - 1;
				return;
			
			case 40:
				jonathan.menu_selector_i = jonathan.menu_selector_i + 1;
				return;
			case 10:
				if (jonathan.menu_selector_i == 13) {
					Main.status = "credits";
					return;
				}
				Main.status = "textbox";
				switch(jonathan.menu_selector_i) {
				case 0:
					Main.status = "golgiGame";
					break;
				case 1:
					Main.status = "riboGame";
					break;
				case 2:
					Main.status = "globuleGame";
					break;
				case 3:
					Main.status = "mitoGame";
					break;
				case 4:
					Main.status = "lysoGame";
					break;
				case 5:
					Main.status = "erGame";
					break;
				case 6:
					Main.status = "nucleusGame";
					break;
				case 7:
					Main.status = "vacuoleGame";
					break;
				case 8:
					Main.status = "membraneGame";
					break;
				case 9:
					Main.status = "cytoGame";
					break;
				case 10:
					Main.status = "peroxiGame";
					break;
				case 11:
					Main.status = "microfilGame";
					break;
				case 12:
					Main.status = "intfilGame";
					break;
				
				}
				
				return;
			}
			
		} else if (Main.substate.equals("quiz")) {
			code = e.getKeyCode();
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
		} else if (Main.substate.equals("gameWait") && Main.status.equals("microfilGame")) {
			if (e.getKeyCode() == 32 || e.getKeyCode() == 10 || e.getKeyCode() == 38) {
				Main.substate = "game";
				jonathan.startFlappyPipes();
				jonathan.flappy.savedY = 46;
				jonathan.flappy.timer.start();
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
			case "riboGame":
				code = e.getKeyCode();
				runRiboGame();
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
			case "nucleusGame":
				code = e.getKeyCode();
				runNucleusGame();
				break;
			case "vacuoleGame":
				code = e.getKeyCode();
				runVacuoleGame();
				break;
			case "membraneGame":
				code = e.getKeyCode();
				runMembraneGame();
				break;
			case "cytoGame":
				code = e.getKeyCode();
				runCytoGame();
				break;
			case "peroxiGame":
				code = e.getKeyCode();
				runPeroxiGame();
				break;
			case "microtubuleGame":
				code = e.getKeyCode();
				runMicrotubuleGame();
				break;
			case "microfilGame":
				code = e.getKeyCode();
				runMicrofilGame();
				break;
			case "intfilGame":
				code = e.getKeyCode();
				runIntFilGame();
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
	
	public void runRiboGame() {
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
	
	public void runNucleusGame() {
		switch (code) {
		case 37:
			if (Nucleotide.baseSelected != 0) {
				Nucleotide.baseSelected--;
			}
			break;
		case 39:
			if (Nucleotide.baseSelected != jonathan.endNucleotides.size() - 2) {
				Nucleotide.baseSelected++;
			}
			break;
		case 32:
		case 10:
			String temp = jonathan.endNucleotides.get(Nucleotide.baseSelected);
			jonathan.endNucleotides.set(Nucleotide.baseSelected, jonathan.endNucleotides.get(Nucleotide.baseSelected+1));
			jonathan.endNucleotides.set(Nucleotide.baseSelected + 1, temp);
		}
	}
	
	public void runVacuoleGame() {
		if (jonathan.dodgey.timer.get() < 1) {
			return;
		}
		String dir = "N";
		switch (code) {
		case 37:
			dir = "W";
			break;
		case 38:
			dir = "N";
			break;
		case 39:
			dir = "E";
			break;
		case 40:
			dir = "S";
			break;
		}
		jonathan.dodgey.jump(dir);
	}
	
	public void runMembraneGame() {
		switch (code) {
		case 37: 
			left = true;
			break;
		case 39:
			right = true;
			break;
		}
	}
	
	public void runCytoGame() {
		switch (code) {
		case 37:
			jonathan.heart.setDir("W");
			break;
		case 38:
			jonathan.heart.setDir("N");
			break;
		case 39:
			jonathan.heart.setDir("E");
			break;
		case 40:
			jonathan.heart.setDir("S");
			break;
		}
	}
	
	public void runPeroxiGame() {
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
		case 32:
		case 10:
			space = true;
			break;
		}
	}
	
	public void runMicrotubuleGame() {
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
	
	public void runMicrofilGame() {
		switch (code) {
		case 38:
		case 32:
		case 10:
			jonathan.flappy.savedY = jonathan.flappy.y;
			jonathan.flappy.timer.start();
			break;
		}
	}
	
	public void runIntFilGame() {
		mostRecent = code;
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
			if (mostRecent == 37) {
				mostRecent = -1;
			}
			break;
		case 38:
			up = false;
			if (mostRecent == 38) {
				mostRecent = -1;
			}
			break;
		case 39:
			right = false;
			if (mostRecent == 39) {
				mostRecent = -1;
			}
			break;
		case 40:
			down = false;
			if (mostRecent == 40) {
				mostRecent = -1;
			}
			break;
		case 32:
		case 10:
			space = false;
		}
	}

}