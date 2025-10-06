package undercell;

import java.awt.Color;

public class Quiz {
	protected String a1, a2, a3, a4;
	protected int answer, selected;
	protected int h1, h2, h3, h4;
	protected String success, fail;
	
	public Quiz(String a, String b, String c, String d, int corrNum) {
		a1 = a;
		a2 = b;
		a3 = c;
		a4 = d;
		h1 = -1;
		h2 = -1;
		h3 = -1;
		h4 = -1;
		answer = corrNum;
		selected = 1;
	}
	
	public void addPostQuiz(String success, String fail) {
		this.success = success;
		this.fail = fail;
	}
	
	public Color getColor(int num) {
		if (Main.substate.equals("quiz")) {
			if (num == selected) {
				return Color.yellow;
			}
			return Color.white;
		} else {
			if (answer == num) {
				return Color.green;
			} else if (num == selected) {
				return Color.red;
			} else {
				return Color.white;
			}
		}
	}
	
	public void selectedUp() {
		if (selected == 3 || selected == 4) {
			selected -= 2;
		}
	}
	
	public void selectedDown() {
		if (selected == 1 || selected == 2) {
			selected += 2;
		}
	}
	
	public void selectedLeft() {
		if (selected == 2 || selected == 4) {
			selected --;
		}
	}
	
	public void selectedRight() {
		if (selected == 1 || selected == 3) {
			selected++;
		}
	}
	
	public String getSelected() {
		switch (selected) {
		case 1:
			return a1;
		case 2:
			return a2;
		case 3:
			return a3;
		}
		return a4;
	}
	
	public void saveHeight(String text, int textHeight) {
		if (a1.equals(text)){
			h1 = textHeight;
		} else if (a2.equals(text)) {
			h2 = textHeight;
		} else if (a3.equals(text)) {
			h3 = textHeight;
		} else {
			h4 = textHeight;
		}
	}
	
	public int getH(int num) {
		switch (num) {
		case 1:
			return h1;
		case 2:
			return h2;
		case 3:
			return h3;
		case 4:
			return h4;
		}
		return -1;
	}
	

}
