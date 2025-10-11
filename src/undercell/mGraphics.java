package undercell;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.TextLayout;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class mGraphics extends JPanel implements Runnable{
	protected JFrame frame;
	protected long time;
	protected Thread thread;
	protected boolean running;
	protected Graphics2D g;
	protected int prevWidth;
	public static int animCounter;
	protected Main main;
	protected ArrayList<String> textList;
	protected MTimer timer;
	protected ArrayList<String> orbs;
	protected ArrayList<Blockade> blockades;
	protected ArrayList<DanceArrow> arrows;
	protected ArrayList<Globby> globbies;
	protected ArrayList<ATP> atp;
	protected ArrayList<Lysosome> lysosomes;
	protected ArrayList<FallProt> fallProteins;
	protected MTimer rTimer;
	protected Platformer jumpy;
	protected HeartBox heart;
	
	public mGraphics(Main main, JFrame frame, ArrayList<String> textList) {
		this.main = main;
		this.frame = frame;
		prevWidth = frame.getWidth();
		this.textList = textList;
		timer = new MTimer();
		rTimer = new MTimer();
		rTimer.start();
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	@Override
	public void run() {
		time = System.nanoTime();
		while (running) {
			if (System.nanoTime() - time > 1000000000/60) {
				time = System.nanoTime();
				this.repaint();
			}
		}
	}
	
	@Override
	public void paintComponent(Graphics bajoodler) {
		super.paintComponent(bajoodler);
		g = (Graphics2D) bajoodler;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		render();
	}
	
	public void render() {
		//System.out.println("sinceLast" + rTimer.getDouble());
		//rTimer.start();
		if ((double)frame.getWidth()/(double)frame.getHeight() != 1550.0/830.0) {
			if (prevWidth != frame.getWidth()) {
				frame.setSize(new Dimension(frame.getWidth(), frame.getWidth()*830/1550));
			} else {
				frame.setSize(new Dimension(frame.getHeight()*1550/830, frame.getHeight()));
			}
			prevWidth = frame.getWidth();
		}
		if (Main.status.equals("golgiGame")) {
			golgiGame();
		} else if (Main.status.equals("vesicleGame")) {
			vesicleGame();
		} else if (Main.status.equals("nucleusGame")) {
			nucleusGame();
		} else if (Main.status.equals("globuleGame")) {
			globuleGame();
		} else if (Main.status.equals("mitoGame")) {
			mitoGame();
		} else if (Main.status.equals("lysoGame")) {
			lysoGame();
		} else if (Main.status.equals("erGame")) {
			erGame();
		}
	}
	
	public void golgiGame() {
		g.setColor(Color.black);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		drawEnemy("golgiapparatus.jpg");
		if (Main.substate.equals("textbox")) {
			if (textList.size() == 0) {
				if (Main.postText.equals("quiz")) {
					Main.substate = "quiz";
				} else if (Main.postText.equals("game:golgi")) {
					Main.substate = "gameCount";
					setUpOrbs();
					timer.start();
				} else if (Main.postText.equals("win:golgi")) {
					Main.substate = "demoEnd";
				}
			} else {
				textBox(textList.get(0));
			}
			drawHealthBar();
		} else if (Main.substate.equals("quiz")) {
			drawQuiz();
			drawHealthBar();
		} else if (Main.substate.equals("quizAnswers")) {
			drawQuiz();
			if (timer.get() >= 2) {
				if (Main.nextQuiz.answer == Main.nextQuiz.selected) {
					Main.prevKnowledge = Main.knowledge;
					Main.knowledge++;
					main.addToTextList(Main.nextQuiz.success);
				} else {
					Main.prevKnowledge = Main.knowledge;
					main.addRandomlyToList(Main.nextQuiz.fail);
				}
				Main.substate = "textbox";
			}
			drawHealthBar();
		} else if (Main.substate.equals("game")) {
			if (timer.get() >= 15) {
				Main.health--;
				if (Main.health == 0) {
					Main.substate = "dead";
				} else {
					Main.substate = "postgame";
				}
			} else {
				drawGolgiGame();
				drawSmallCountdown();
			}
			drawHealthBar();
		} else if (Main.substate.equals("gameCount")) {
			if (timer.get() >= 1) {
				Main.substate = "game";
				timer.start();
			} else {
			drawGolgiGame();
			drawBigCountdown();
			drawHealthBar();
			}
		} else if (Main.substate.equals("postgame")) {
			if (timer.get() >= 1) {
				if (Main.prevKnowledge == Main.knowledge) {
					main.addToTextList("golgiretry" + (Main.knowledge + 1));
				} else {
					main.addToTextList("golgiq" + (Main.knowledge + 1));
				}
				Main.substate = "textbox";
			} else {
				drawGolgiGame();
				drawHealthBar();
			}
		} else if (Main.substate.equals("demoEnd")) {
			g.setColor(Color.green);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("End of Demo", g.getFont(), g.getFontRenderContext());
			g.drawString("End of Demo", frameWidth()/2 - (g.getFontMetrics().stringWidth("End of Demo")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		} else if (Main.substate.equals("dead")) {
			g.setColor(Color.black);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.red);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("YOU HAVE DIED", g.getFont(), g.getFontRenderContext());
			g.drawString("YOU HAVE DIED", frameWidth()/2 - (g.getFontMetrics().stringWidth("YOU HAVE DIED")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		}
	}
	
	public void drawEnemy(String address) {
		if (Main.status.equals("vesicleGame") || Main.status.equals("nucleusGame")) {
			if (Main.substate.equals("game")) {
				return;
			} else if (Main.substate.equals("gameCount")) {
				return;
			} else if (Main.substate.equals("postgame")) {
				return;
			}
		}
		try {
			Image image = ImageIO.read(new File("assets/" + address));
			g.drawImage(image, frameWidth()*5/12, frameHeight()/8, frameWidth()/6, frameHeight()/4, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Stop trying to make me download incomprehensible images of eldritch cell horrors!");
		}
	}
	
	public void doNothing() {
		System.out.println("this method title is a lie");
	}

	public void textBox(String text) {
		if ((!text.contains(":"))||(text.length() >= 14 && text.substring(0,14).equals("QUEST COMPLETE"))) {
			text = "Narrator: " + text;
		}
		String[] textBits = text.split(":");
		String speaker = textBits[0];
		speaker = speaker.toUpperCase();
		if (textBits.length == 3) {
			text = textBits[1].substring(1) + ":" + textBits[2];
		} else {
			text = textBits[1].substring(1);
		}
		int boxX = (frameWidth()/10);
		int boxY = (frameHeight()/2);
		int boxWidth = (frameWidth()/10)*8;
		int boxHeight = frameHeight()*3/8;
		int amount = boxHeight/20;
		int textHeight = ((boxHeight - (boxHeight/10))/5)*2/3;
		g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		g.setColor(Color.white);
		g.fillRect(boxX-amount, boxY - amount, boxWidth + (amount*2), boxHeight + (amount*2));
		if (!speaker.equals("NARRATOR")) {
			int speakerX = boxX + amount;
			int speakerY = boxY - (amount*3);
			int speakerHeight = amount*3;
			g.setFont(new Font("Consolas", Font.BOLD, getSpeakerTextHeight(speakerHeight)));
			int speakerWidth = g.getFontMetrics().stringWidth(speaker) + amount*2;
			g.setColor(Color.black);
			g.fillRect(boxX, speakerY - amount, speakerWidth + (amount*2), speakerHeight + (amount*2));
			g.setColor(Color.white);
			g.fillRect(speakerX, speakerY, speakerWidth, speakerHeight);
			g.setColor(Color.black);
			g.setFont(new Font("Consolas", Font.BOLD, getSpeakerTextHeight(speakerHeight)));
			g.drawString(speaker, speakerX + amount, boxY - (speakerHeight/3));
			g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		}
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
		ArrayList<String> textLines = new ArrayList<String>();
		if (animCounter < text.length()*1.5) {
			animCounter++;
			text = text.substring(0, (int) (animCounter/1.5));
		} else {
			if (animCounter > 0 && animCounter < 500) {
				animCounter = 500;
			} else if (animCounter == 512) {
				animCounter = 500;
			} else {
				animCounter++;
			}
			if (animCounter >= 508) {
				drawTriangle(boxX, boxY, boxWidth, boxHeight);
			}
		}
		while(g.getFontMetrics().stringWidth(text) > boxWidth - (amount*4)) {
			int index = text.length();
			while (g.getFontMetrics().stringWidth(text.substring(0,index)) > boxWidth - (amount*4)) {
				index--;
			}
			if (text.substring(index,index+1).equals(" ")) {
				index++;
			} else {
				while (index != 0 && !text.substring(index - 1, index).equals(" ")) {
					index--;
				}
			}
			textLines.add(text.substring(0,index));
			text = text.substring(index);
		}
		textLines.add(text);
		g.setColor(Color.white);
		int yVal = boxY;
		while (textLines.size() != 0) {
			yVal += 2*textHeight;
			g.drawString(textLines.get(0), boxX + (amount*2), yVal);
			textLines.remove(0);
		}
	}
	
	public void drawEmptyTextBox() {
		int boxX = (frameWidth()/10);
		int boxY = (frameHeight()/2);
		int boxWidth = (frameWidth()/10)*8;
		int boxHeight = frameHeight()*3/8;
		int amount = boxHeight/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, boxWidth + (amount*2), boxHeight + (amount*2));
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
	}
	
	public void drawQuiz() {
		int boxX = (frameWidth()/10);
		int boxY = (frameHeight()/2);
		int boxWidth = (frameWidth()/10)*8;
		int boxHeight = frameHeight()*3/8;
		int amount = boxHeight/20;
		int textHeight = ((boxHeight - (boxHeight/10))/5)*2/3;
		g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		g.setColor(Color.white);
		g.fillRect(boxX-amount, boxY - amount, boxWidth + (amount*2), boxHeight + (amount*2));
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
		String answer = Main.nextQuiz.a1;
		g.setColor(Main.nextQuiz.getColor(1));
		fixHeight(answer, boxWidth, amount, textHeight);
		TextLayout tl = new TextLayout(answer, g.getFont(), g.getFontRenderContext());
		g.drawString(answer, boxX + (boxWidth*3/8) - g.getFontMetrics().stringWidth(answer), boxY + (boxHeight/3) + round(tl.getBounds().getHeight()/2));
		g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		answer = Main.nextQuiz.a2;
		g.setColor(Main.nextQuiz.getColor(2));
		fixHeight(answer, boxWidth, amount, textHeight);
		tl = new TextLayout(answer, g.getFont(), g.getFontRenderContext());
		g.drawString(answer, boxX + (boxWidth*5/8), boxY + (boxHeight/3) + round(tl.getBounds().getHeight()/2));
		g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		answer = Main.nextQuiz.a3;
		g.setColor(Main.nextQuiz.getColor(3));
		fixHeight(answer, boxWidth, amount, textHeight);
		tl = new TextLayout(answer, g.getFont(), g.getFontRenderContext());
		g.drawString(answer, boxX + (boxWidth*3/8) - g.getFontMetrics().stringWidth(answer), boxY + (boxHeight*2/3) + round(tl.getBounds().getHeight()/2));
		g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		answer = Main.nextQuiz.a4;
		g.setColor(Main.nextQuiz.getColor(4));
		fixHeight(answer, boxWidth, amount, textHeight);
		tl = new TextLayout(answer, g.getFont(), g.getFontRenderContext());
		g.drawString(answer, boxX + (boxWidth*5/8), boxY + (boxHeight*2/3) + round(tl.getBounds().getHeight()/2));
		g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		if (Main.substate.equals("quiz")) {
			if (animCounter < 500) {
				animCounter = 500;
			} else if (animCounter == 512) {
				animCounter = 500;
			} else {
				animCounter++;
			}
			if (animCounter >= 508) {
				drawQuizTriangle(Main.nextQuiz.selected, boxX, boxWidth, boxY, boxHeight);
			}
		}
	}
	
	public void drawQuizTriangle(int num, int boxX, int boxWidth, int boxY, int boxHeight) {
		TextLayout tl2 = new TextLayout(Main.nextQuiz.getSelected(), g.getFont(), g.getFontRenderContext());
		int length = round(tl2.getBounds().getHeight());
		if (Main.nextQuiz.getH(num) != -1) {
			length = Main.nextQuiz.getH(num);
		}
		int tx, ty;
		if (num == 1 || num == 2) {
			ty = boxY + (boxHeight/3) - (length/2);
		} else {
			ty = boxY + (boxHeight*2/3) - (length/2);
		}
		if (num == 1 || num == 3) {
			tx = boxX + (boxWidth*3/8) + (length/2);
			leftTriangle(tx, ty, length);
		} else {
			tx = boxX + (boxWidth*5/8) - (length/2) - length;
			rightTriangle(tx, ty, length);
		}
	}
	
	public void leftTriangle(int tx, int ty, int length) {
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = tx;
		x[1] = tx + length;
		x[2] = tx + length;
		y[0] = ty + (length/2);
		y[1] = ty;
		y[2] = ty + length;
		g.setColor(Color.white);
		g.fillPolygon(x, y, 3);
	}
	
	public void rightTriangle(int tx, int ty, int length) {
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = tx;
		x[1] = tx;
		x[2] = tx + length;
		y[0] = ty;
		y[1] = ty + length;
		y[2] = ty + (length/2);
		g.setColor(Color.white);
		g.fillPolygon(x, y, 3);
	}
	
	public void fixHeight(String text, int boxWidth, int amount, int textHeight) {
		while (g.getFontMetrics().stringWidth(text) > (boxWidth*3/8) - amount*4) {
			textHeight--;
			g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		}
		Main.nextQuiz.saveHeight(text, textHeight);
	}
	
	public void drawTriangle(int boxX, int boxY, int boxWidth, int boxHeight) {
		g.setColor(Color.white);
		int[] xPoints = new int[3];
		int[] yPoints = new int[3];
		int triWidth = boxHeight/10;
		int triHeight = (int) Math.sqrt(Math.pow(triWidth, 2) + Math.pow((triWidth/2), 2));
		int topLineY = boxY + boxHeight - (boxHeight/20) - triHeight;
		yPoints[0] = topLineY;
		yPoints[1] = topLineY;
		yPoints[2] = topLineY + triHeight;
		xPoints[0] = boxX + boxWidth - (boxHeight/20) - triWidth;
		xPoints[1] = boxX + boxWidth - (boxHeight/20);
		xPoints[2] = (xPoints[0] + xPoints[1])/2;
		g.fillPolygon(xPoints, yPoints, 3);
	}
	
	public int getSpeakerTextHeight(int speakerHeight) {
		return (speakerHeight/6)*5;
	}
	
	public static int round(double d) {
		return (int) (d + 0.5);
	}
	
	public int frameWidth() {
		return round(frame.getContentPane().getBounds().getWidth());
	}
	
	public int frameHeight() {
		return round(frame.getContentPane().getBounds().getHeight());
	}
	
	public void startAnswersTimer() {
		timer.start();
	}
	
	public void drawGolgiGame() {
		int length = frameHeight()*3/8;
		int boxX = frameWidth()/2 - (length/2);
		int boxY = frameHeight()/2;
		int amount = length/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, length + amount*2, length + amount*2);
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, length, length);
		int orbLength = length/5;
		g.setColor(Color.white);
		g.drawLine(boxX + (length/2) - (orbLength*3/4), boxY + length - (orbLength/2), boxX + (length/2) + (orbLength*3/4), boxY + length - (orbLength/2));
		g.setColor(Color.blue);
		g.drawOval(boxX - length/2, boxY + length - orbLength - amount, orbLength, orbLength);
		g.setColor(Color.green);
		g.drawOval(boxX + length + length/2 - orbLength, boxY + length - orbLength - amount, orbLength, orbLength);
		if (Main.knowledge >= 2) {
			g.setColor(Color.yellow);
			g.drawOval(boxX - length/2, boxY + length - (orbLength*3) - amount, orbLength, orbLength);
		}
		if (Main.knowledge == 3) {
			g.setColor(Color.red);
			g.drawOval(boxX + length + length/2 - orbLength, boxY + length - (orbLength*3) - amount, orbLength, orbLength);
		}
		int realOrbLength = orbLength*9/10;
		int orbEdge = boxX + (length/2) - (realOrbLength/2);
		if (orbs.size() >= 2) {
			g.setColor(orbColor(1));
			g.fillOval(orbEdge, boxY + length - orbLength/2 - realOrbLength/2, realOrbLength, realOrbLength);
			drawArrow(orbs.get(1), boxX + (length/2), boxY + length - orbLength/2, realOrbLength*7/10);
		}
		if (orbs.size() >= 3) {
			g.setColor(orbColor(2));
			g.fillOval(orbEdge, boxY + length - orbLength*3/2 - realOrbLength/2, realOrbLength, realOrbLength);
			drawArrow(orbs.get(2), boxX + (length/2), boxY + length - orbLength*3/2, realOrbLength*7/10);
		}
		if (orbs.size() >= 4) {
			g.setColor(orbColor(3));
			g.fillOval(orbEdge, boxY + length - orbLength*5/2 - realOrbLength/2, realOrbLength, realOrbLength);
			drawArrow(orbs.get(3), boxX + (length/2), boxY + length - orbLength*5/2, realOrbLength*7/10);
		}
		if (orbs.size() >= 5) {
			g.setColor(orbColor(4));
			g.fillOval(orbEdge, boxY + length - orbLength*7/2 - realOrbLength/2, realOrbLength, realOrbLength);
			drawArrow(orbs.get(4), boxX + (length/2), boxY + length - orbLength*7/2, realOrbLength*7/10);
		}
		if (orbs.size() >= 6) {
			g.setColor(orbColor(5));
			g.fillOval(orbEdge, boxY + length - orbLength*9/2 - realOrbLength/2, realOrbLength, realOrbLength);
			drawArrow(orbs.get(5), boxX + (length/2), boxY + length - orbLength*9/2, realOrbLength*7/10);
		}
		if (orbs.get(0) != null) {
			g.setColor(orbColor(0));
			int orbX = getOrbX(orbs.get(0), boxX, length, orbLength);
			int orbY = getOrbY(orbs.get(0), boxY, length, orbLength, amount);
			g.fillOval(orbX, orbY, realOrbLength, realOrbLength);
		}
	}
	
	public int getOrbX(String orb, int boxX, int length, int orbLength) {
		switch (orb) {
		case "left":
		case "up":
			return (boxX - length/2) + (orbLength/2) - ((orbLength*9/10)/2);
		case "down":
		case "right":
			return (boxX + length + length/2 - orbLength) + (orbLength/2) - ((orbLength*9/10)/2);
		}
		return -1;
	}
	
	public int getOrbY(String orb, int boxY, int length, int orbLength, int amount) {
		switch (orb) {
		case "left":
		case "right":
			return (boxY + length - orbLength - amount) + (orbLength/2) - ((orbLength*9/10)/2);
		case "up":
		case "down":
			return (boxY + length - (orbLength*3) - amount) + (orbLength/2) - ((orbLength*9/10)/2);
		}
		return -1;
	}
	
	public Color orbColor(int num) {
		switch (orbs.get(num)) {
		case "left":
			return Color.blue;
		case "right":
			return Color.green;
		case "up":
			return Color.yellow;
		case "down":
			return Color.red;
		}
		return Color.white;
	}
	
	
	
	public void setUpOrbs() {
		orbs = new ArrayList<String>();
		orbs.add(null);
		for (int i = 0; i<30; i++) {
			addNewOrb();
			if (orbs.size() >= 5 && orbs.get(orbs.size()-1).equals(orbs.get(orbs.size()-2)) && orbs.get(orbs.size()-3).equals(orbs.get(orbs.size()-1))&&orbs.get(orbs.size()-4).equals(orbs.get(orbs.size()-1))) {
				orbs.remove(orbs.size()-1);
				i--;
			}
		}
	}
	
	public void addNewOrb() {
		int selected;
		if (Main.knowledge == 3) {
			selected = Main.random(0,3);
		} else if (Main.knowledge == 2) {
			selected = Main.random(0,2);
		} else {
			selected = Main.random(0,1);
		}
		switch (selected) {
		case 0:
			orbs.add("left");
			return;
		case 1:
			orbs.add("right");
			return;
		case 2:
			orbs.add("up");
			return;
		case 3:
			orbs.add("down");
			return;
		}
	}
	
	public void drawBigCountdown() {
		String num = "" + (3-timer.getThird());
		//String num = "3";
		int textHeight = (frameHeight()*3/8)*4/5;
		g.setFont(new Font("Consolas", Font.BOLD, textHeight));
		TextLayout tl = new TextLayout(num, g.getFont(), g.getFontRenderContext());
		g.setColor(new Color(248, 24, 148));
		g.drawString(num, frameWidth()/2 - (g.getFontMetrics().stringWidth(num)/2), frameHeight()*11/16 + round(tl.getBounds().getHeight()/2));
	}
	
	public void drawSmallCountdown() {
		String num = "" + (15 - timer.get());
		int length = frameHeight()*3/8;
		int textHeight = length/10;
		int amount = textHeight/2;
		g.setFont(new Font ("Consolas", Font.BOLD, textHeight));
		TextLayout tl = new TextLayout(num, g.getFont(), g.getFontRenderContext());
		g.setColor(Color.white);
		g.drawString(num, (frameWidth()/2) + (length/2) - amount - g.getFontMetrics().stringWidth("30")/2 - g.getFontMetrics().stringWidth(num)/2, frameHeight()/2 + amount + round(tl.getBounds().getHeight()));
	}
	
	public void drawArrow(String dir, int x, int y, int length) {
		g.setColor(Color.gray);
		switch (dir) {
		case "up":
			upArrow(x,y,length);
			return;
		case "down":
			downArrow(x,y,length);
			return;
		case "left":
			leftArrow(x,y,length);
			return;
		case "right": 
			rightArrow(x,y,length);
			return;
		}
	}
	
	public void upArrow(int x, int y, int length) {
		g.fillRect(x - length/4, y - length/8-1, length/2, length*5/8+1);
		int[] tx = new int[3];
		int[] ty = new int[3];
		tx[0] = x - (length/2);
		tx[1] = x;
		tx[2] = x + (length/2);
		ty[0] = y;
		ty[1] = y - (length/2);
		ty[2] = y;
		g.fillPolygon(tx, ty, 3);
	}
	
	public void downArrow(int x, int y, int length) {
		g.fillRect(x - length/4, y - length/2, length/2, length*5/8+1);
		int[] tx = new int[3];
		int[] ty = new int[3];
		tx[0] = x - (length/2);
		tx[1] = x;
		tx[2] = x + (length/2);
		ty[0] = y;
		ty[1] = y + (length/2);
		ty[2] = y;
		g.fillPolygon(tx, ty, 3);
	}
	
	public void rightArrow(int x, int y, int length) {
		g.fillRect(x - length/2, y - length/4, length*5/8+1, length/2);
		int[] tx = new int[3];
		int[] ty = new int[3];
		tx[0] = x + length/8;
		tx[1] = x + length/8;
		tx[2] = x + length/2;
		ty[0] = y - length/2;
		ty[1] = y + length/2;
		ty[2] = y;
		g.fillPolygon(tx, ty, 3);
	}
	
	public void leftArrow(int x, int y, int length) {
		g.fillRect(x - length/8-1, y - length/4, length*5/8+1, length/2);
		int[] tx = new int[3];
		int[] ty = new int[3];
		tx[0] = x - length/2;
		tx[1] = x - length/8;
		tx[2] = x - length/8;
		ty[0] = y;
		ty[1] = y - length/2;
		ty[2] = y + length/2;
		g.fillPolygon(tx, ty, 3);
	}
	
	public void drawHealthBar() {
		int bottomY = frameHeight()*7/8 + ((frameHeight()*3/8)/20);
		int barHeight = (frameHeight() - bottomY)/4;
		int barWidth = ((frameWidth()/10)*8)*3/4;
		int barX = (frameWidth()/2) - barWidth/2;
		int heartX = barX;
		int barY = bottomY + (frameHeight() - bottomY)/2 - barHeight/2;
		int amount = barHeight/5;
		int heartLength = barHeight + amount*2;
		barX += heartLength*2;
		barWidth -= heartLength*2;
		heartX += heartLength/2;
		g.setColor(Color.white);
		g.fillRect(barX - amount, barY - amount, barWidth + amount*2, barHeight + amount*2);
		g.setColor(Color.black);
		g.fillRect(barX, barY, barWidth, barHeight);
		if (Main.health != 0) {
			g.setColor(Color.red);
			g.fillRect(barX, barY, (int) (barWidth * ((double)Main.health/3)), barHeight);
		}
		try {
			Image image = ImageIO.read(new File("assets/heart-icon.png"));
			g.drawImage(image, heartX, barY - amount, heartLength, heartLength, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("I'm afraid that heart icon has been permanently banned for trying to scam the other assets.");
		}
	}
	
	public void drawHealthBar(int amt) {
		int bottomY = frameHeight()*7/8 + ((frameHeight()*3/8)/20);
		int barHeight = (frameHeight() - bottomY)/4;
		int barWidth = ((frameWidth()/10)*8)*3/4;
		int barX = (frameWidth()/2) - barWidth/2;
		int heartX = barX;
		int barY = bottomY + (frameHeight() - bottomY)/2 - barHeight/2;
		int amount = barHeight/5;
		int heartLength = barHeight + amount*2;
		barX += heartLength*2;
		barWidth -= heartLength*2;
		heartX += heartLength/2;
		g.setColor(Color.white);
		g.fillRect(barX - amount, barY - amount, barWidth + amount*2, barHeight + amount*2);
		g.setColor(Color.black);
		g.fillRect(barX, barY, barWidth, barHeight);
		if (Main.health != 0) {
			g.setColor(Color.red);
			g.fillRect(barX, barY, (int) (barWidth * ((double)Main.health/amt)), barHeight);
		}
		try {
			Image image = ImageIO.read(new File("assets/heart-icon.png"));
			g.drawImage(image, heartX, barY - amount, heartLength, heartLength, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("I'm afraid that heart icon has been permanently banned for trying to scam the other assets.");
		}
	}
	
	public void vesicleGame() {
		g.setColor(Color.black);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		drawEnemy("vesicle.jpg");
		if (Main.substate.equals("textbox")) {
			if (textList.size() == 0) {
				if (Main.postText.equals("quiz")) {
					Main.substate = "quiz";
				} else if (Main.postText.equals("game:vesicle")) {
					Main.substate = "gameCount";
					animCounter = 0;
					setUpVesicle();
					timer.start();
					drawEmptyTextBox();
				} else if (Main.postText.equals("win:vesicle")) {
					Main.substate = "demoEnd";
				}
			} else {
				textBox(textList.get(0));
			}
			drawHealthBar();
		} else if (Main.substate.equals("quiz")) {
			drawQuiz();
			drawHealthBar();
		} else if (Main.substate.equals("quizAnswers")) {
			drawQuiz();
			if (timer.get() >= 2) {
				if (Main.nextQuiz.answer == Main.nextQuiz.selected) {
					Main.prevKnowledge = Main.knowledge;
					Main.knowledge++;
					main.addToTextList(Main.nextQuiz.success);
				} else {
					Main.prevKnowledge = Main.knowledge;
					main.addRandomlyToList(Main.nextQuiz.fail);
				}
				Main.substate = "textbox";
			}
			drawHealthBar();
		} else if (Main.substate.equals("game")) {
			drawEnemySide("vesicle.jpg");
			VesicleStats.timeSinceMoved++;
			if (VesicleStats.timeSinceMoved == 4) {
				VesicleStats.lane += VesicleStats.moveDir;
			}
			drawVesicleGame();
			drawHealthBar();
		} else if (Main.substate.equals("gameCount")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				Main.substate = "game";
				setUpBlockades();
				timer.start();
			} 
			drawMovingEnemySide("vesicle.jpg");
			drawMovingBoxV();
			drawHealthBar();
		} else if (Main.substate.equals("postgame")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				if (Main.prevKnowledge == Main.knowledge) {
					main.addToTextList("vesicleretry" + (Main.knowledge + 1));
				} else {
					main.addToTextList("vesicleq" + (Main.knowledge + 1));
				}
				Main.substate = "textbox";
				drawEnemy("vesicle.jpg");
				drawEmptyTextBox();
				drawHealthBar();
			} else {
			drawMovingEnemySideB("vesicle.jpg");
			drawMovingBoxVB();
			drawHealthBar();
			}
		} else if (Main.substate.equals("demoEnd")) {
			g.setColor(Color.green);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("End of Demo", g.getFont(), g.getFontRenderContext());
			g.drawString("End of Demo", frameWidth()/2 - (g.getFontMetrics().stringWidth("End of Demo")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		} else if (Main.substate.equals("dead")) {
			g.setColor(Color.black);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.red);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("YOU HAVE DIED", g.getFont(), g.getFontRenderContext());
			g.drawString("YOU HAVE DIED", frameWidth()/2 - (g.getFontMetrics().stringWidth("YOU HAVE DIED")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		}
	}
	
	public void setUpVesicle() {
		VesicleStats.timeSinceMoved = 30;
		VesicleStats.lane = 2;
		blockades = new ArrayList<Blockade>();
		Blockade.timeToHit = 2;
		Blockade.removeBound = 2.5;
		Blockade.hitBoundU = 2.25;
		Blockade.hitBoundL = 1.75;
		Blockade.canChange = true;
	}
	
	public void drawVesicleGame() {
		int boxWidth = frameHeight()*3/8;
		int boxHeight = frameHeight()*3/4;
		int boxX = frameWidth()/2;
		int boxY = frameHeight()/8;
		int amount = boxWidth/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, boxWidth + amount*2, boxHeight + amount*2);
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
		int laneLength = round(boxWidth/3);
		g.setColor(Color.lightGray);
		g.drawLine(boxX + laneLength, boxY, boxX + laneLength, boxY + boxHeight);
		g.drawLine(boxX + boxWidth - laneLength, boxY, boxX + boxWidth - laneLength, boxY + boxHeight);
		drawMainVesicle(boxX, laneLength, boxY, boxWidth, boxHeight);
		Graphics tempg = g.create();
		Graphics2D g2 = (Graphics2D) tempg;
		g2.setClip(boxX, boxY, boxWidth, boxHeight);
		if (Main.knowledge == 3 && Blockade.canChange) {
			Blockade.canChange = false;
			Blockade.timeToHit = 1;
			Blockade.removeBound = 1.5;
			Blockade.hitBoundU = 1.13;
			Blockade.hitBoundL = 0.87;
		}
		for (Blockade b: blockades) {
			if (b.get() >= -0.5) {
				drawBlockade(g2, b, laneLength, boxX, boxWidth, boxY, boxHeight);
				if (b.get() >= Blockade.hitBoundL && b.get() <= Blockade.hitBoundU && VesicleStats.lane == b.lane && Main.canBeHurt()) {
					Main.health--;
					if (Main.health == 0) {
						Main.substate = "dead";
					}
					Main.iTimer.start();
					b.toRemove = true;
				}
			}
		}
		for (int i = 0; i<blockades.size(); i++) {
			if (blockades.get(i).toRemove) {
				blockades.remove(i);
				i--;
			}
		}
		if (blockades.size() != 0 && blockades.get(0).get() >= Blockade.removeBound) {
			blockades.remove(0);
		} 
		if (blockades.size() == 0) {
			Main.substate = "postgame";
			timer.start();
			animCounter = 0;
		}
	}
	
	public void drawBlockade(Graphics2D g2, Blockade b, int laneLength, int boxX, int boxWidth, int boxY, int boxHeight) {
		int length = laneLength*4/5;
		int x = -1;
		switch (b.lane) {
		case 1:
			x = boxX + (laneLength/2);
			break;
		case 2:
			x = boxX + (boxWidth/2);
			break;
		case 3:
			x = boxX + boxWidth - (laneLength/2);
			break;
		}
		x -= length/2;
		int y = boxY + round((boxHeight - (boxHeight/7))*b.get()/Blockade.timeToHit);
		try {
			Image image = ImageIO.read(new File("assets/blockade-stand-in.png"));
			g2.drawImage(image, x, y, length, length, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("I lost the game. Also your blockade image isn't getting put in");
		}
	}
	

	public void drawMainVesicle(int boxX, int laneLength, int boxY, int boxLength, int boxHeight) {
		int length = laneLength*4/5;
		int x = -1;
		switch (VesicleStats.lane) {
		case 1:
			x = boxX + (laneLength/2);
			break;
		case 2:
			x = boxX + (boxLength/2);
			break;
		case 3:
			x = boxX + boxLength - (laneLength/2);
			break;
		}
		int y = boxY + boxHeight - (boxHeight/7);
		if (VesicleStats.timeSinceMoved < 4) {
			x += round((laneLength*(double)VesicleStats.timeSinceMoved/8)*(VesicleStats.moveDir));
		} else if (VesicleStats.timeSinceMoved <= 8) {
			x -= (laneLength-laneLength*VesicleStats.timeSinceMoved/8)*(VesicleStats.moveDir);
		}
		x -= length/2;
		
		try {
			Image image = ImageIO.read(new File("assets/vesicle.jpg"));
			g.drawImage(image, x, y, length, length, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("player vesicle image issues. im too tired to make this one weird. just. player vesicle image issues.");
		}
	}
	
	public void drawEnemySide(String address) {
		try {
			Image image = ImageIO.read(new File("assets/" + address));
			g.drawImage(image, frameWidth()/4, frameHeight()*3/8, frameWidth()/6, frameHeight()/4, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Stop moving! You're supposed to stand in one place and fight each other! Dodging is cheating!");
		}
	}
	
	public void drawMovingBoxV() {
		int boxWidth = frameWidth()*4/5 - round(((frameWidth()*4/5) - (frameHeight()*3/8))*(double)animCounter/30);
		int boxHeight = frameHeight()*3/8 + round((frameHeight()*3/8)*((double)animCounter/30));
		int boxX = frameWidth()/10 + round((frameWidth()*4/10)*((double)animCounter/30));
		int boxY = frameHeight()/2 - round((frameHeight()*3/8)*((double)animCounter/30));
		int amount = (frameHeight()*3/8)/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, boxWidth + (amount*2), boxHeight + (amount*2));
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
	}
	
	public void drawMovingBoxVB() {
		int boxWidth = frameHeight()*3/8 + round(((frameWidth()*4/5) - (frameHeight()*3/8))*(double)animCounter/30);
		int boxHeight = frameHeight()*3/4 - round((frameHeight()*3/8)*((double)animCounter/30));
		int boxX = frameWidth()/2 - round((frameWidth()*4/10)*((double)animCounter/30));
		int boxY = frameHeight()/8 + round((frameHeight()*3/8)*((double)animCounter/30));
		int amount = (frameHeight()*3/8)/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, boxWidth + (amount*2), boxHeight + (amount*2));
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
	}
	
	public void drawShrinkingBox() {
		int boxWidth = frameWidth()*4/5 - round(((frameWidth()*4/5) - (frameHeight()*3/8))*(double)animCounter/30);
		int boxHeight = frameHeight()*3/8;
		int boxX = frameWidth()/10 + round(((frameWidth()/2 - (frameHeight()*3/8)/2)-frameWidth()/10)*(double)animCounter/30);
		int boxY = frameHeight()/2;
		int amount = (frameHeight()*3/8)/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, boxWidth + (amount*2), boxHeight + (amount*2));
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
	}
	
	public void drawGrowingBox() {
		int boxWidth = frameHeight()*3/8 + round(((frameWidth()*4/5) - (frameHeight()*3/8))*(double)animCounter/30);
		int boxHeight = frameHeight()*3/8;
		int boxX = (frameWidth()/2-(frameHeight()*3/8)/2) - round(((frameWidth()/2 - (frameHeight()*3/8)/2)-frameWidth()/10)*(double)animCounter/30);
		int boxY = frameHeight()/2;
		int amount = (frameHeight()*3/8)/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, boxWidth + (amount*2), boxHeight + (amount*2));
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
	}
	
	public void drawMovingEnemySide(String address) {
		int x = frameWidth()*5/12 - round(((frameWidth()*5/12) - (frameWidth()/4))*((double)animCounter/30));
		int y = frameHeight()/8 + round((frameHeight()/4)*((double)animCounter/30));
		try {
			Image image = ImageIO.read(new File("assets/" + address));
			g.drawImage(image, x, y, frameWidth()/6, frameHeight()/4, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("error:_location:_motion_reason:_was-tired_punishment:_eternal-torment");
		}
	}
	
	public void drawMovingEnemySideB(String address) {
		int x = frameWidth()/4 + round(((frameWidth()*5/12) - (frameWidth()/4))*((double)animCounter/30));
		int y = frameHeight()*3/8 -  round((frameHeight()/4)*((double)animCounter/30));
		try {
			Image image = ImageIO.read(new File("assets/" + address));
			g.drawImage(image, x, y, frameWidth()/6, frameHeight()/4, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("tnemrot-lanrete_:tnemhsinup_derit-saw_:nosear_noitom_:noitacol_:rorre");
		}
	}
	
	public void setUpBlockades() {
		int bigNum = 8;
		if (Main.knowledge == 3) {
			bigNum = 16;
		}
		if (Main.knowledge >= 2) {
			int extra = Main.random(1,2);
			if (extra == 2) {
				extra++;
			}
			blockades.add(new Blockade(2));
			blockades.add(new Blockade(extra));
			for (int i = 1; i<bigNum; i++) {
				int l1 = Main.random(1,3);
				int l2 = Main.random(1,3);
				if (l1 == l2) {
					i--;
				} else {
					blockades.add(new Blockade(l1, i));
					blockades.add(new Blockade(l2,i));
				}
			}
		} else {
			blockades.add(new Blockade(2));
			for (int i = 1; i<8; i++) {
				blockades.add(new Blockade(Main.random(1,3), i));
			}
		}
	}
	
	public void nucleusGame() {
		g.setColor(Color.black);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		drawEnemy("vesicle.jpg");
		if (Main.substate.equals("textbox")) {
			if (textList.size() == 0) {
				if (Main.postText.equals("quiz")) {
					Main.substate = "quiz";
				} else if (Main.postText.equals("game:nucleus")) {
					Main.substate = "gameCount";
					animCounter = 0;
					timer.start();
					drawEmptyTextBox();
				} else if (Main.postText.equals("win:nucleus")) {
					Main.substate = "demoEnd";
				}
			} else {
				textBox(textList.get(0));
			}
			drawHealthBar(20);
		} else if (Main.substate.equals("quiz")) {
			drawQuiz();
			drawHealthBar(20);
		} else if (Main.substate.equals("quizAnswers")) {
			drawQuiz();
			if (timer.get() >= 2) {
				if (Main.nextQuiz.answer == Main.nextQuiz.selected) {
					Main.prevKnowledge = Main.knowledge;
					Main.knowledge++;
					main.addToTextList(Main.nextQuiz.success);
				} else {
					Main.prevKnowledge = Main.knowledge;
					main.addRandomlyToList(Main.nextQuiz.fail);
				}
				Main.substate = "textbox";
			}
			drawHealthBar(20);
		} else if (Main.substate.equals("game")) {
			for (int i = 0; i<arrows.size(); i++) {
				if (arrows.get(i).toRemove) {
					arrows.remove(i);
					i--;
				}
			}
			drawEnemySide("vesicle.jpg");
			drawNucleusGame();
			drawHealthBar(20);
		} else if (Main.substate.equals("gameCount")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				Main.substate = "game";
				setUpArrows();
				timer.start();
			} 
			drawMovingEnemySide("vesicle.jpg");
			drawMovingBoxV();
			drawHealthBar(20);
		} else if (Main.substate.equals("postgame")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				if (Main.prevKnowledge == Main.knowledge) {
					main.addToTextList("nucleusretry" + (Main.knowledge + 1));
				} else {
					main.addToTextList("nucleusq" + (Main.knowledge + 1));
				}
				Main.substate = "textbox";
				drawEnemy("vesicle.jpg");
				drawEmptyTextBox();
				drawHealthBar(20);
			} else {
			drawMovingEnemySideB("vesicle.jpg");
			drawMovingBoxVB();
			drawHealthBar(20);
			}
		} else if (Main.substate.equals("demoEnd")) {
			g.setColor(Color.green);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("End of Demo", g.getFont(), g.getFontRenderContext());
			g.drawString("End of Demo", frameWidth()/2 - (g.getFontMetrics().stringWidth("End of Demo")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		} else if (Main.substate.equals("dead")) {
			g.setColor(Color.red);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.black);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("YOU HAVE DIED", g.getFont(), g.getFontRenderContext());
			g.drawString("YOU HAVE DIED", frameWidth()/2 - (g.getFontMetrics().stringWidth("YOU HAVE DIED")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		}
	}
	
	public void drawNucleusGame() {
		int boxWidth = frameHeight()*3/8;
		int boxHeight = frameHeight()*3/4;
		int boxX = frameWidth()/2;
		int boxY = frameHeight()/8;
		int amount = boxWidth/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, boxWidth + amount*2, boxHeight + amount*2);
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, boxWidth, boxHeight);
		int laneLength = boxWidth/5;
		int circleLength = laneLength*7/10;
		g.setColor(Color.gray);
		g.drawLine(boxX + laneLength, boxY, boxX + laneLength, boxY + boxHeight);
		g.drawLine(boxX + (laneLength*2), boxY, boxX + (laneLength*2), boxY + boxHeight);
		g.drawLine(boxX + boxWidth - laneLength, boxY, boxX + boxWidth - laneLength, boxY + boxHeight);
		g.drawLine(boxX + boxWidth - laneLength*2, boxY, boxX + boxWidth - laneLength*2, boxY + boxHeight);
		g.setColor(Color.blue);
		g.drawOval(boxX + laneLength - circleLength/2, boxY + boxHeight - boxHeight/7, circleLength, circleLength);
		g.setColor(Color.red);
		g.drawOval(boxX + laneLength*2 - circleLength/2, boxY + boxHeight - boxHeight/7, circleLength, circleLength);
		g.setColor(Color.green);
		g.drawOval(boxX + boxWidth - laneLength - circleLength/2, boxY + boxHeight - boxHeight/7, circleLength, circleLength);
		g.setColor(Color.yellow);
		g.drawOval(boxX + boxWidth - laneLength*2 - circleLength/2, boxY + boxHeight - boxHeight/7, circleLength, circleLength);
		Graphics tempg = g.create();
		Graphics2D g2 = (Graphics2D) tempg;
		g2.setClip(boxX, boxY, boxWidth, boxHeight);
		for (DanceArrow a: arrows) {
			drawArrow(g2, a, circleLength, laneLength, boxX, boxY, boxWidth, boxHeight);
		}
		if (arrows.size() != 0 && arrows.get(0).get() >= DanceArrow.removeBound){
			arrows.remove(0);
			Main.health--;
			if (Main.health == 0) {
				Main.substate = "dead";
				return;
			}
		}
		if (arrows.size() == 0) {
			Main.substate = "postgame";
			animCounter = 0;
			timer.start();
		}
	}
	
	public void drawArrow(Graphics2D g2, DanceArrow a, int length, int laneLength, int boxX, int boxY, int boxWidth, int boxHeight) {
		int x = -1;
		switch (a.dir) {
		case "l":
			x = boxX + laneLength - length/2;
			break;
		case "d":
			x = boxX + laneLength*2 - length/2;
			break;
		case "u":
			x = boxX + boxWidth - laneLength*2 - length/2;
			break;
		case "r":
			x = boxX + boxWidth - laneLength - length/2;
			break;
		}
		int y = boxY + round((boxHeight - boxHeight/7)*a.get()/DanceArrow.timeToReach);
		try {
			Image image = ImageIO.read(new File("assets/danceArrows/dance-arrow-" + a.dir + ".png"));
			g2.drawImage(image, x, y, length, length, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("NOOO! THE DANCING IS TOO POWERFUL. YOU'VE DOOMED US ALL!");
		}
	}
	
	public void setUpArrows() {
		arrows = new ArrayList<DanceArrow>();
		if (Main.knowledge == 1) {
			double[] vals = getDanceStructure1();
			for (int i = 0; i<vals.length; i++) {
				arrows.add(new DanceArrow(randDir(), vals[i]));
			}
		} else if (Main.knowledge == 2) {
			double[] vals = getDanceStructure2();
			System.out.println(vals);
			for (int i = 0; i<vals.length; i++) {
				arrows.add(new DanceArrow(randDir(), vals[i]));
				if (i!=0 && vals[i] == vals[i-1] && arrows.get(arrows.size()-1).dir.equals(arrows.get(arrows.size()-2).dir)) {
					arrows.get(arrows.size()-1).dir = nextDir(arrows.get(arrows.size()-1).dir);
				}
			}
		} else {
			double[] vals = getDanceStructure3();
			for (int i = 0; i<vals.length; i++) {
				arrows.add(new DanceArrow(randDir(), vals[i]));
				if (i != 0 && vals[i] == vals[i-1] && arrows.get(arrows.size()-1).dir.equals(arrows.get(arrows.size()-2).dir)) {
					arrows.get(arrows.size()-1).dir = nextDir(arrows.get(arrows.size()-1).dir);
				}
			}
		}
		for (int i = 3; i<arrows.size(); i++) {
			if (arrows.get(i).dir.equals(arrows.get(i-1).dir) && arrows.get(i).dir.equals(arrows.get(i-2).dir) && arrows.get(i).dir.equals(arrows.get(i-3).dir)) {
				arrows.get(i).dir = randDir();
				i--;
			}
		}
	}
	
	public String randDir() {
		switch (Main.random(0,3)) {
		case 0:
			return "l";
		case 1:
			return "d";
		case 2:
			return "u";
		case 3:
			return "r";
		}
		return null;
	}
	
	public String nextDir(String pDir) {
		switch (pDir) {
		case "l":
			return "r";
		case "r":
			return "l";
		case "u":
			return "d";
		case "d":
			return "u";
		}
		return null;
	}
	
	public double[] getDanceStructure1() {
		switch (Main.random(0,3)) {
		case 0:
			double[] nums = {1,2,3,3.5,4,5,5.5,6,7,7.5,8};
			return nums;
		case 1:
			double[] nums2 = {1,2.0,3.0,3.5,4.0,5.0,6.0,7.0,7.5,8.0};
			return nums2;
		case 2:
			double[] nums3 = {1,2,3,3.5,4,5,6,6.5,7,8};
			return nums3;
		case 3:
			double[] nums4 = {1,2,3,3.5,4,5,5.5,6,6.5,7,8};
			return nums4;
		}
		double[] error = {-1.0};
		return error;
	}
	
	public double[] getDanceStructure2() {
		switch (Main.random(0,5)) {
		case 0:
			double[] nums = {1,2,3,3,4,5,5.5,6,7,8,8};
			return nums;
		case 1:
			double[] nums1 = {1,2,3,3,4,5,5,6,7,7,8};
			return nums1;
		case 2:
			double[] nums2 = {1,2,3,3,4,5,6,6.5,7,7.5,7.5,8};
			return nums2;
		case 3:
			double[] nums3 = {1,2,3,3,4,5,6,6,7,7.5,8};
			return nums3;
		case 4:
			double[] nums4 = {1,2,3,3,4,5,5.5,6,6.5,7,7.5,8};
			return nums4;
		case 5: 
			double[] nums5 = {1,2,3,3,4,5,5,6,6,7,7,8};
			return nums5;
		}
		double[] error = {-1.0};
		return error;
	}
	
	public double[] getDanceStructure3() {
		switch (Main.random(0,7)) {
		case 0:
			double[] nums = {1,2,3,3.33,3.66,4,5,6,6.33,6.66,7,7.66,8};
			return nums;
		case 1:
			double[] nums1 = {1,2,2.66,3,3.66,4,5,5.33,5.66,6,6.66,7,8,8};
			return nums1;
		case 2:
			double[] nums2 = {1,2,2,3,3.66,4,4,5,6,6.66,7,7.66,8,8};
			return nums2;
		case 3:
			double[] nums3 = {1,2,2.66,3,3,4,5,6,6.33,6.66,7,7,8,8,};
			return nums3;
		case 4:
			double[] nums4 = {1,2,2.33,2.66,3,4,4,5,5.66,6,6.33,6.66,7,7.66,8,8};
			return nums4;
		case 5:
			double[] nums5 = {1,1.66,2,3,3.66,4,5,5.66,6,6.66,7,7.33,7.66,8};
			return nums5;
		case 6:
			double[] nums6 = {1,1,2,2,3,3,4,4.33,4.66,5,5.33,5.66,6,6.33,6.66,7,7.33,7.66,8};
			return nums6;
		case 7:
			double[] nums7 = {1,2,3,3,4,5,5.33,5.66,6,7,7,8,8};
			return nums7;
		}
		double[] error = {-1};
		return error;
	}
	
	public void globuleGame() {
		g.setColor(Color.black);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		drawEnemy("vesicle.jpg");
		if (Main.substate.equals("textbox")) {
			if (textList.size() == 0) {
				if (Main.postText.equals("quiz")) {
					Main.substate = "quiz";
				} else if (Main.postText.equals("game:globule")) {
					Main.substate = "gameCount";
					animCounter = 0;
					timer.start();
					drawEmptyTextBox();
				} else if (Main.postText.equals("win:globule")) {
					Main.substate = "demoEnd";
				}
			} else {
				textBox(textList.get(0));
			}
			drawHealthBar();
		} else if (Main.substate.equals("quiz")) {
			drawQuiz();
			drawHealthBar();
		} else if (Main.substate.equals("quizAnswers")) {
			drawQuiz();
			if (timer.get() >= 2) {
				if (Main.nextQuiz.answer == Main.nextQuiz.selected) {
					Main.prevKnowledge = Main.knowledge;
					Main.knowledge++;
					main.addToTextList(Main.nextQuiz.success);
				} else {
					Main.prevKnowledge = Main.knowledge;
					main.addRandomlyToList(Main.nextQuiz.fail);
				}
				Main.substate = "textbox";
			}
			drawHealthBar();
		} else if (Main.substate.equals("game")) {
			drawGlobuleGame();
			drawHealthBar();
		} else if (Main.substate.equals("gameCount")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				Main.substate = "game";
				setUpGlobbies();
				timer.start();
			} 
			drawShrinkingBox();
			drawHealthBar();
		} else if (Main.substate.equals("postgame")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				if (Main.prevKnowledge == Main.knowledge) {
					main.addToTextList("globuleretry" + (Main.knowledge + 1));
				} else {
					main.addToTextList("globuleq" + (Main.knowledge + 1));
				}
				Main.substate = "textbox";
				drawEmptyTextBox();
				drawHealthBar();
			} else {
			drawGrowingBox();
			drawHealthBar();
			}
		} else if (Main.substate.equals("demoEnd")) {
			g.setColor(Color.green);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("End of Demo", g.getFont(), g.getFontRenderContext());
			g.drawString("End of Demo", frameWidth()/2 - (g.getFontMetrics().stringWidth("End of Demo")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		} else if (Main.substate.equals("dead")) {
			g.setColor(Color.red);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.black);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("YOU HAVE DIED", g.getFont(), g.getFontRenderContext());
			g.drawString("YOU HAVE DIED", frameWidth()/2 - (g.getFontMetrics().stringWidth("YOU HAVE DIED")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		}
	}
	
	public void setUpGlobbies() {
		globbies = new ArrayList<Globby>();
		if (Main.knowledge == 3) {
			Globby.runTime = 3;
			globbies.add(new Globby());
			globbies.add(new Globby(1));
			globbies.add(new Globby(2));
		} else if (Main.knowledge == 2) {
			Globby.runTime = 2;
			globbies.add(new Globby());
			globbies.add(new Globby(1));
		} else {
			Globby.runTime = 1;
			globbies.add(new Globby());
		}
		Globby.def = "u";
	}
	
	public void drawGlobuleGame() {
		int length = frameHeight()*3/8;
		int boxX = frameWidth()/2 - (length/2);
		int boxY = frameHeight()/2;
		int amount = length/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, length + amount*2, length + amount*2);
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, length, length);
		g.setColor(Color.blue);
		int inBreak = length/10;
		g.fillRoundRect(boxX + inBreak, boxY + inBreak, length - inBreak*2, length - inBreak*2, inBreak*2, inBreak*2);
		g.setColor(new Color(173, 216, 230));
		g.fillRoundRect(boxX + round(inBreak*1.5), boxY + round(inBreak*1.5), length - inBreak*3, length - inBreak*3, inBreak, inBreak);
		if (globbies.size() == 0) {
			Main.substate = "postgame";
			animCounter = 0;
			timer.start();
			return;
		}
		for (Globby globby : globbies) {
			if (globby.get() >= 0) {
				drawGlobby(globby, boxX, boxY, length, inBreak);
			}
		}
		for (int i = 0; i<globbies.size(); i++) {
			if (globbies.get(i).hits == 10 || globbies.get(i).toRemove) {
				globbies.remove(i);
				i--;
			}
		}
		g.setColor(Color.yellow);
		drawDef(boxX, boxY, length, inBreak);
	}
	
	public void drawGlobby(Globby glob, int boxX, int boxY, int length, int inBreak) {
		if (shouldSwitch(glob)) {
			if (!glob.dir.equals(Globby.def)) {
				Main.health--;
				if (Main.health == 0) {
					Main.substate = "dead";
				}
				glob.toRemove = true;
			}
			glob.hits++;
			System.out.println("hit");
			glob.newDest();
			if (Main.knowledge <= 1) {
			if (glob.currentlyEven) {
				glob.currentlyEven = false;
			} else {
				glob.currentlyEven = true;
			}
			}
		}
		int globSize = (length - inBreak*2)/20;
		int fromX = getGX(glob.from, glob.fromNum, boxX, length, inBreak, globSize);
		int fromY = getGY(glob.from, glob.fromNum, boxY, length, inBreak, globSize);
		int toX = getGX(glob.dir, glob.dest, boxX, length, inBreak, globSize);
		int toY = getGY(glob.dir, glob.dest, boxY, length, inBreak, globSize);
		int x = fromX + round((toX - fromX)*(glob.get()%Globby.runTime)/Globby.runTime);
		int y = fromY + round((toY - fromY)*(glob.get()%Globby.runTime)/Globby.runTime);
		g.setColor(new Color(0,100,0));
		g.fillOval(x, y, globSize, globSize);
	}
	
	public boolean shouldSwitch(Globby glob) {
		switch (Main.knowledge) {
		case 1:
			return ((int)glob.get()%2 == 0 && !glob.currentlyEven) || ((int)glob.get()%2 == 1 && glob.currentlyEven);
		case 2:
			boolean bool;
			if (((int)glob.get())%2 == 0 && !glob.currentlyEven) {
				bool = true;
				glob.currentlyEven = true;
			} else if (((int)glob.get())%2 != 0 && glob.currentlyEven) {
				bool = false;
				glob.currentlyEven = false;
			} else {
				bool = false;
			}
			return bool;
		case 3:
			boolean bool2;
			if (((int)glob.get())%3 == 0 && !glob.currentlyEven) {
				bool2 = true;
				glob.currentlyEven = true;
			} else if (((int)glob.get())%3 != 0 && glob.currentlyEven) {
				bool2 = false;
				glob.currentlyEven = false;
			} else {
				bool2 = false;
			}
			return bool2;
		}
		return false;
	}
	
	public int getGX(String dir, int dest, int boxX, int length, int inBreak, int globSize) {
		if (dir.equals("center")) {
			return boxX + length/2 - globSize/2;
		}
		switch(dir) {
		case "l":
			return boxX + round(inBreak*1.5);
		case "r":
			return boxX + length - round(inBreak*1.5) - globSize;
		case "u":
		case "d":
			int inLength = length - inBreak*3;
			switch (dest) {
			case 1:
				return boxX + round(inBreak*1.5) + inLength*2/5 - globSize/2;
			case 2:
				return boxX + round(inBreak*1.5) + inLength*3/5 - globSize/2;
			case 3:
				return boxX + round(inBreak*1.5) + inLength*4/5 - globSize/2;
			}
		}
		return -1;
	}
	
	public int getGY(String dir, int dest, int boxY, int length, int inBreak, int globSize) {
		if (dir.equals("center")) {
			return boxY + length/2 - globSize/2;
		}
		switch(dir) {
		case "u":
			return boxY + round(inBreak*1.5);
		case "d":
			return boxY + length - round(inBreak*1.5) - globSize;
		case "l":
		case "r":
			int inLength = length - inBreak*3;
			switch (dest) {
			case 1:
				return boxY + round(inBreak*1.5) + inLength*2/5 - globSize/2;
			case 2:
				return boxY + round(inBreak*1.5) + inLength*3/5 - globSize/2;
			case 3:
				return boxY + round(inBreak*1.5) + inLength*4/5 - globSize/2;
			}
		}
		return -1;
	}
	
	public void drawDef(int boxX, int boxY, int length, int inBreak) {
		int x = -1;
		int y = -1;
		int width = 1;
		int height = 1;
		switch (Globby.def) {
		case "u":
			x = boxX + inBreak*2;
			y = boxY + inBreak;
			width = length - inBreak*4;
			height = inBreak/2+1;
			break;
		case "d":
			x = boxX + inBreak*2;
			height = inBreak/2+1;
			y = boxY + inBreak + (length-inBreak*2) - height;
			width = length - inBreak*4;
			break;
		case "l":
			x = boxX + inBreak;
			y = boxY + inBreak*2;
			width = inBreak/2+1;
			height = length - inBreak*4;
			break;
		case "r":
			width = inBreak/2+1;
			x = boxX + inBreak + (length - inBreak*2) - width;
			y = boxY + inBreak*2;
			height = length - inBreak*4;
			break;
		}
		g.fillRect(x,y,width,height);
	}
	
	public void mitoGame() {
		g.setColor(Color.black);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		drawEnemy("vesicle.jpg");
		if (Main.substate.equals("textbox")) {
			if (textList.size() == 0) {
				if (Main.postText.equals("quiz")) {
					Main.substate = "quiz";
				} else if (Main.postText.equals("game:mito")) {
					Main.substate = "gameCount";
					animCounter = 0;
					timer.start();
					drawEmptyTextBox();
				} else if (Main.postText.equals("win:mito")) {
					Main.substate = "demoEnd";
				}
			} else {
				textBox(textList.get(0));
			}
			drawHealthBar();
		} else if (Main.substate.equals("quiz")) {
			drawQuiz();
			drawHealthBar();
		} else if (Main.substate.equals("quizAnswers")) {
			drawQuiz();
			if (timer.get() >= 2) {
				if (Main.nextQuiz.answer == Main.nextQuiz.selected) {
					Main.prevKnowledge = Main.knowledge;
					Main.knowledge++;
					main.addToTextList(Main.nextQuiz.success);
				} else {
					Main.prevKnowledge = Main.knowledge;
					main.addRandomlyToList(Main.nextQuiz.fail);
				}
				Main.substate = "textbox";
			}
			drawHealthBar();
		} else if (Main.substate.equals("game")) {
			updatePlatformer();
			drawMitoGame();
			drawHealthBar();
		} else if (Main.substate.equals("gameCount")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				Main.substate = "game";
				setUpPlatformer();
				setUpATP();
				timer.start();
			} 
			drawShrinkingBox();
			drawHealthBar();
		} else if (Main.substate.equals("postgame")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				if (Main.prevKnowledge == Main.knowledge) {
					main.addToTextList("mitoretry" + (Main.knowledge + 1));
				} else {
					main.addToTextList("mitoq" + (Main.knowledge + 1));
				}
				Main.substate = "textbox";
				drawEmptyTextBox();
				drawHealthBar();
			} else {
			drawGrowingBox();
			drawHealthBar();
			}
		} else if (Main.substate.equals("demoEnd")) {
			g.setColor(Color.green);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("End of Demo", g.getFont(), g.getFontRenderContext());
			g.drawString("End of Demo", frameWidth()/2 - (g.getFontMetrics().stringWidth("End of Demo")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		} else if (Main.substate.equals("dead")) {
			g.setColor(Color.red);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.black);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("YOU HAVE DIED", g.getFont(), g.getFontRenderContext());
			g.drawString("YOU HAVE DIED", frameWidth()/2 - (g.getFontMetrics().stringWidth("YOU HAVE DIED")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		}
	}
	
	public void setUpPlatformer() {
		jumpy = new Platformer();
	}
	
	public void setUpATP() {
		atp = new ArrayList<ATP>();
		switch (Main.knowledge) {
		case 0:
		case 1:
			for (int i = 1; i<9; i++) {
				atp.add(new ATP("l", Main.random(1,10)*5, i));
			}
			break;
		case 2:
			for (double i = 1; i<9; i+= 0.5) {
				atp.add(new ATP("l", Main.random(1,10)*5, i));
			}
			break;
		case 3:
			for (double i = 1; i<9; i+= 0.5) {
				if (Main.random(0,1)==0) {
					atp.add(new ATP("l", Main.random(1,10)*5, i));
				} else {
					atp.add(new ATP("r", Main.random(1,10)*5, i));
				}
			}
		}
	}
	
	public void drawMitoGame() {
		int length = frameHeight()*3/8;
		int boxX = frameWidth()/2 - (length/2);
		int boxY = frameHeight()/2;
		int amount = length/20;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, length + amount*2, length + amount*2);
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, length, length);
		double px = length/100.0;
		drawPlayer(px, boxX, boxY, length);
		if (atp.size() == 0) {
			Main.substate = "postgame";
			animCounter = 0;
			timer.start();
			return;
		}
		Graphics temp = g.create();
		Graphics2D g2 = (Graphics2D) temp;
		g2.setClip(boxX, boxY, length, length);
		for (ATP a : atp) {
			if (a.get() >= 0 && a.get() <= 2.5) {
				drawATP(g2, a, px, boxX, boxY, length);
			}
		}
		for (int i = 0; i<atp.size(); i++) {
			if (atp.get(i).toRemove || atp.get(i).get() > 1.5) {
				atp.remove(i);
				i--;
			}
		}
	}
	
	public void drawPlayer(double px, int boxX, int boxY, int boxLength) {
		int length = round(px*8);
		int x = boxX + round(jumpy.x*px);
		int y = boxY + boxLength - round(jumpy.getY()*px);
		try {
			Image image = ImageIO.read(new File("assets/player-stand-in.jpg"));
			g.drawImage(image, x, y, length, length, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("apparently the tricky bit of coding a platformer is finding the right image for the player. you failed at that.");
		}
	}
	
	public void drawATP(Graphics2D g2, ATP a, double px, int boxX, int boxY, int boxLength) {
		int modifier = 1;
		int distMod = 0;
		if (a.dir.equals("l")) {
			modifier = -1;
			distMod = 100;
		}
		a.x = distMod + modifier*(100*a.get()/1.5);
		int x = boxX + round(px*a.x);
		int y = boxY + boxLength - round(px*a.y);
		int length = round(px*4);
		if (Main.canBeHurt() && ((a.x >= jumpy.x && a.x <= jumpy.x + 8) || (jumpy.x <= a.x+4 && jumpy.x + 8 >= a.x+4)) && ((jumpy.getY() >= a.y && jumpy.getY()-8 <= a.y) || (jumpy.getY() - 8 >= a.y-4 && jumpy.getY() - 8 <= a.y-4))) {
			Main.health--;
			if (Main.health == 0) {
				Main.substate = "dead";
			}
			Main.iTimer.start();
			a.toRemove = true;
		}
		try {
			Image image = ImageIO.read(new File("assets/blockade-stand-in.png"));
			g2.drawImage(image, x, y, length, length, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("THE ATP DO NOT WISH TO BE BEHELD");
		}
	}
	
	public void updatePlatformer() {
		if (KeyInput.left) {
			jumpy.x -= 2;
			if (jumpy.x < 0) {
				jumpy.x = 0;
			}
		}
		if (KeyInput.right) {
			jumpy.x += 2;
			if (jumpy.x > 92) {
				jumpy.x = 92;
			}
		}
	}
	
	public void lysoGame() {
		g.setColor(Color.black);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		drawEnemy("vesicle.jpg");
		if (Main.substate.equals("textbox")) {
			if (textList.size() == 0) {
				if (Main.postText.equals("quiz")) {
					Main.substate = "quiz";
				} else if (Main.postText.equals("game:lyso")) {
					Main.substate = "gameCount";
					animCounter = 0;
					timer.start();
					drawEmptyTextBox();
				} else if (Main.postText.equals("win:lyso")) {
					Main.substate = "demoEnd";
				}
			} else {
				textBox(textList.get(0));
			}
			drawHealthBar(5);
		} else if (Main.substate.equals("quiz")) {
			drawQuiz();
			drawHealthBar(5);
		} else if (Main.substate.equals("quizAnswers")) {
			drawQuiz();
			if (timer.get() >= 2) {
				if (Main.nextQuiz.answer == Main.nextQuiz.selected) {
					Main.prevKnowledge = Main.knowledge;
					Main.knowledge++;
					main.addToTextList(Main.nextQuiz.success);
				} else {
					Main.prevKnowledge = Main.knowledge;
					main.addRandomlyToList(Main.nextQuiz.fail);
				}
				Main.substate = "textbox";
			}
			drawHealthBar(5);
		} else if (Main.substate.equals("game")) {
			updateHeartBox();
			drawLysoGame();
			drawHealthBar(5);
		} else if (Main.substate.equals("gameCount")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				Main.substate = "game";
				heart = new HeartBox();
				setUpLysosomes();
				timer.start();
			} 
			drawShrinkingBox();
			drawHealthBar(5);
		} else if (Main.substate.equals("postgame")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				if (Main.prevKnowledge == Main.knowledge) {
					main.addToTextList("lysoretry" + (Main.knowledge + 1));
				} else {
					main.addToTextList("lysoq" + (Main.knowledge + 1));
				}
				Main.substate = "textbox";
				drawEmptyTextBox();
				drawHealthBar();
			} else {
			drawGrowingBox();
			drawHealthBar(5);
			}
		} else if (Main.substate.equals("demoEnd")) {
			g.setColor(Color.green);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("End of Demo", g.getFont(), g.getFontRenderContext());
			g.drawString("End of Demo", frameWidth()/2 - (g.getFontMetrics().stringWidth("End of Demo")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		} else if (Main.substate.equals("dead")) {
			g.setColor(Color.red);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.black);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("YOU HAVE DIED", g.getFont(), g.getFontRenderContext());
			g.drawString("YOU HAVE DIED", frameWidth()/2 - (g.getFontMetrics().stringWidth("YOU HAVE DIED")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		}
	}
	
	public void setUpLysosomes() {
		lysosomes = new ArrayList<Lysosome>();
		switch(Main.knowledge) {
		case 0:
		case 1:
			if (Main.random(0,1) == 0) {
				addLyso(25, 50, -50, 50, 150, 3, 2);
				int modifier = 1;
				if (Main.random(0,1) == 0) {
					modifier = -1;
				}
				addLyso(25, 50+(100*modifier), 50, 50+(-100*modifier), 50, 3, 4);
				for (int i = 0; i<5; i++) {
					addLyso(10, 10+(i*20), -50, 10+(i*20), 150, 3, 6+i*0.5);
				}
				for (int i = 4; i>0; i--) {
					addLyso(10,90-(i*20), -50, 90-(i*20), 150, 3, 8+(i*0.5));
				}
			} else {
				addLyso(25, 50, -50, 50, 150, 3, 2);
				int modifier = 1;
				if (Main.random(0,1) == 0) {
					modifier = -1;
				}
				addLyso(25, 50+(100*modifier), 50, 50+(-100*modifier), 50, 3, 4);
				for (int i = 0; i<5; i++) {
					addLyso(10, 50+(100*modifier), 10+(i*20), 50+(-100*modifier), 10+(i*20), 3, 6+i*0.5);
				}
				for (int i = 4; i>0; i--) {
					addLyso(10,50+(100*modifier),90-(i*20),50+(-100*modifier), 90-(i*20), 3, 8+(i*0.5));
				}
			}
			break;
		case 2:
			int pos = 1;
			if (Main.random(0,1) == 0) {
				pos = -1;
			}
			int alsoPos = 1;
			if (Main.random(0,1) == 0) {
				alsoPos = -1;
			}
			if (Main.random(0,1) == 0) {
				addLyso(25, 50+(25*alsoPos), 50+(100*pos), 50+(25*alsoPos), 50+(-100*pos), 3, 4);
				addLyso(25, 50+(-25*alsoPos), 50+(100*pos), 50+(-25*alsoPos), 50+(-100*pos), 3, 5);
				addLyso(25, 50+(25*alsoPos), 50+(100*pos), 50+(25*alsoPos), 50+(-100*pos), 3, 6);
			} else {
				addLyso(25, 50+(100*pos), 50+(25*alsoPos), 50 + (-100*pos), 50+(25*alsoPos), 3, 4);
				addLyso(25, 50+(100*pos), 50+(-25*alsoPos), 50 + (-100*pos), 50+(-25*alsoPos), 3, 5);
				addLyso(25, 50+(100*pos), 50+(25*alsoPos), 50 + (-100*pos), 50+(25*alsoPos), 3, 6);
			}
			addChaserLyso(15, 2, 9);
			addChaserLyso(15,2,10);
			addChaserLyso(15, 2, 11);
			addChaserLyso(15, 2, 12);
			addChaserLyso(15, 2, 13);
			addLyso(20, -50, -50, 150, 150, 3, 16);
			addLyso(20, 150, -50, -50, 150, 3, 16);
			break;
		case 3:
			int swap = 1;
			if (Main.random(0,1) == 0) {
				swap = -1;
			}
			if (Main.random(0,1)==0) {
				addLyso(25, 50+(25*swap), -50, 50+(25*swap), 150, 3, 2);
				addLyso(25, 50+(-25*swap), 150, 50+(-25*swap), -50, 3, 2);
				addLyso(25, 50+(25*swap), -50, 50+(25*swap), 150, 3, 4);
				addLyso(25, 50+(-25*swap), 150, 50+(-25*swap), -50, 3, 4);
				addLyso(25, 50+(25*swap), -50, 50+(25*swap), 150, 3, 6);
				addLyso(25, 50+(-25*swap), 150, 50+(-25*swap), -50, 3, 6);
			} else {
				addLyso(25, -50, 50+(25*swap), 150, 50+(25*swap), 3, 2);
				addLyso(25, 150, 50+(-25*swap), -50, 50+(-25*swap), 3, 2);
				addLyso(25, -50, 50+(25*swap), 150, 50+(25*swap), 3, 4);
				addLyso(25, 150, 50+(-25*swap), -50, 50+(-25*swap), 3, 4);
				addLyso(25, -50, 50+(25*swap), 150, 50+(25*swap), 3, 6);
				addLyso(25, 150, 50+(-25*swap), -50, 50+(-25*swap), 3, 6);
			}
			for (int i = 0; i<3; i++) {
				addChaserCirc(5, 4, 8+(i*2));
			}
			swap = 1;
			if (Main.random(0,1) == 0) {
				swap = -1;
			}
			int otherSwap = 1;
			if (Main.random(0,1)==0) {
				otherSwap = -1;
			}
			if (Main.random(0,1) == 0) {
				addLyso(40, 50 + (100*swap), 50 + (10*otherSwap), 50 + (-100*swap), 50 + (10*otherSwap), 5, 20);
			} else {
				addLyso(40, 50 + (10*otherSwap), 50 + (100*swap), 50 + (10*otherSwap), 50 + (-100*swap), 5, 20);
			}
		}
	}
	
	public void addChaserCirc(double rad, double rt, double wait) {
		lysosomes.add(new Lysosome(rad, rt, wait, true));
	}
	
	public void addFallProtCirc(int x, double rt, double wait, boolean bonus) {
		fallProteins.add(new FallProt(x, rt, wait, false, bonus));
	}
	
	public void addLyso(double rad, int fx, int fy, int tx, int ty, double rt, double w) {
		lysosomes.add(new Lysosome(rad, fx, fy, tx, ty, rt, w));
	}
	
	public void addChaserLyso(double rad, double rt, double wait) {
		lysosomes.add(new Lysosome(rad, rt, wait));
	}
	
	
	public void updateHeartBox() {
		if (KeyInput.left) {
			heart.x -= 2;
			if (heart.x < 0) {
				heart.x = 0;
			}
		}
		if (KeyInput.right) {
			heart.x += 2;
			if (heart.x > 92) {
				heart.x = 92;
			}
		}
		if (Main.status.equals("lysoGame") && KeyInput.down) {
			heart.y += 2;
			if (heart.y > 92) {
				heart.y = 92;
			}
		}
		if (Main.status.equals("lysoGame") && KeyInput.up) {
			heart.y -= 2;
			if (heart.y < 0) {
				heart.y = 0;
			}
		}
	}
	
	public void drawLysoGame() {
		int length = frameHeight()*3/8;
		int boxX = frameWidth()/2 - (length/2);
		int boxY = frameHeight()/2;
		int amount = length/20;
		double px = length/100.0;
		int otherAmount = boxY - (frameHeight()*3/8);
		int otherAmtPx = round(otherAmount/px);
		g.setColor(Color.white);
		g.fillRect(boxX - otherAmount, boxY - otherAmount, length + otherAmount*2, length + otherAmount*2);
		g.setColor(Color.black);
		g.fillRect(boxX - otherAmount + amount, boxY - otherAmount + amount, length + otherAmount*2 - amount*2, length + otherAmount*2 - amount*2);
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, length + amount*2, length + amount*2);
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, length, length);
		drawHeart(px, boxX, boxY);
		if (lysosomes.size() == 0) {
			Main.substate = "postgame";
			timer.start();
			animCounter = 0;
			return;
		}
		Graphics tg = g.create();
		Graphics2D g2 = (Graphics2D) tg;
		g2.setClip(boxX - otherAmount + amount, boxY - otherAmount + amount, length + otherAmount*2 - amount*2, length + otherAmount*2 - amount*2);
		for (Lysosome l : lysosomes) {
			drawLysosome(g2, l, px, boxX, boxY);
		}
		for (int i = 0; i<lysosomes.size(); i++) {
			Lysosome l = lysosomes.get(i);
			if (l.get() >= l.runTime && (l.x - l.radius > 100 + otherAmtPx ||l.x + l.radius < 0 - otherAmtPx || l.y - l.radius > 100 + otherAmtPx || l.y + l.radius < 0 - otherAmtPx)) {
				lysosomes.remove(i);
				i--;
			} else if (l.toRemove) {
				lysosomes.remove(i);
				i--;
			}
		}
	}
	
	public void drawHeart(double px, int boxX, int boxY) {
		int length = round(px*8);
		int x = boxX + round(px*heart.x);
		int y = boxY + round(px*heart.y);
		try {
			Image image = ImageIO.read(new File("assets/player-stand-in.jpg"));
			g.drawImage(image, x, y, length, length, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("The wrath of Toby Fox be upon ye!");
		}
	}
	
	public void drawLysosome(Graphics2D g2, Lysosome l, double px, int boxX, int boxY) {
		if (l.chaser && l.get() >= 0) {
			setChaser(l);
		} else if (l.chaser) {
			return;
		}
		if (l.circle && l.get() >= 0) {
			if (l instanceof FallProt) {
				addCircleFallProts(l.fromX, l.runTime, ((FallProt)l).bonus);
			} else {
				addCircles(l.radius, l.runTime);
			}
			l.circle = false;
			l.toRemove = true;
		} else if (l.circle) {
			return;
		}
		//l.x = Math.min(l.toX, l.fromX) + round((Math.abs(l.toX - l.fromX))*l.get()/l.runTime);
		//l.y = Math.min(l.toY, l.fromY) + round((Math.abs(l.toY - l.fromY))*l.get()/l.runTime);
		l.x = l.fromX + round((l.toX - l.fromX)*l.get()/l.runTime);
		l.y = l.fromY + round((l.toY - l.fromY)*l.get()/l.runTime);
		int length = round(px*l.radius*2);
		int x = boxX + round(l.x * px) - length/2;
		int y = boxY + round(l.y*px) - length/2;
		if (Main.canBeHurt() && Math.hypot(Math.abs(l.x-(heart.x+4)), Math.abs(l.y - (heart.y+4))) <= l.radius + 4) {
			Main.health--;
			if (Main.health == 0) {
				Main.substate = "dead";
				return;
			}
			Main.iTimer.start();
		}
		try {
			Image image;
			if (l instanceof FallProt) {
				image = ImageIO.read(new File("assets/blockade-stand-in.png"));
			} else {
				image = ImageIO.read(new File("assets/lysosome-stand-in.png"));
			}
			g2.drawImage(image, x, y, length, length, null);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("lies.\n\n\nosomes.\n\n\n");
		}
	}
	
	
	
	public void setChaser(Lysosome l) {
		if (l instanceof FallProt) {
			l.fromX = l.toX;
			l.toX = heart.x;
			l.chaser = false;
			l.timer.start();
			return;
		}
		l.chaser = false;
		l.fromX = l.toX;
		l.fromY = l.toY;
		l.toX = heart.x;
		l.toY = heart.y;
		l.timer.start();
	}
		
	
	public void addCircles(double rad, double rt) {
		int distX, distY;
		for (int i = 0; i<12; i++) {
			distX = round(100*Math.cos(Math.toRadians(30*i)));
			distY = round(100*Math.sin(Math.toRadians(30*i)));
			addLyso(rad, heart.x + distX, heart.y + distY, heart.x, heart.y, rt, 0);
		}
	}
	
	public void addCircleFallProts(int x, double rt, boolean shouldChange) {
		int distX, distY;
		double extra = 0;
		int max = 24;
		if (shouldChange) {
			extra = 7.5;
			max = 23;
		}
		for (int i = 12; i<=max; i++) {
			distX = round(100*Math.cos(Math.toRadians(15*i + extra)));
			distY = round(100*Math.sin(Math.toRadians(15*i + extra)));
			fallProteins.add(new FallProt(x, 0, x+distX, -1*distY, rt, 0));
		}
	}
	
	public void erGame() {
		g.setColor(Color.black);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());
		drawEnemy("vesicle.jpg");
		if (Main.substate.equals("textbox")) {
			if (textList.size() == 0) {
				if (Main.postText.equals("quiz")) {
					Main.substate = "quiz";
				} else if (Main.postText.equals("game:er")) {
					Main.substate = "gameCount";
					animCounter = 0;
					timer.start();
					drawEmptyTextBox();
				} else if (Main.postText.equals("win:er")) {
					Main.substate = "demoEnd";
				}
			} else {
				textBox(textList.get(0));
			}
			drawHealthBar(8);
		} else if (Main.substate.equals("quiz")) {
			drawQuiz();
			drawHealthBar(8);
		} else if (Main.substate.equals("quizAnswers")) {
			drawQuiz();
			if (timer.get() >= 2) {
				if (Main.nextQuiz.answer == Main.nextQuiz.selected) {
					Main.prevKnowledge = Main.knowledge;
					Main.knowledge++;
					main.addToTextList(Main.nextQuiz.success);
				} else {
					Main.prevKnowledge = Main.knowledge;
					main.addRandomlyToList(Main.nextQuiz.fail);
				}
				Main.substate = "textbox";
			}
			drawHealthBar(8);
		} else if (Main.substate.equals("game")) {
			updateHeartBox();
			drawERGame();
			drawHealthBar(8);
		} else if (Main.substate.equals("gameCount")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				Main.substate = "game";
				heart = new HeartBox(92);
				setUpFallingProteins();
				timer.start();
			} 
			drawShrinkingBox();
			drawHealthBar(8);
		} else if (Main.substate.equals("postgame")) {
			if (animCounter < 30) {
				animCounter++;
			}
			if (timer.get() >= 1) {
				if (Main.prevKnowledge == Main.knowledge) {
					main.addToTextList("erretry" + (Main.knowledge + 1));
				} else {
					main.addToTextList("erq" + (Main.knowledge + 1));
				}
				Main.substate = "textbox";
				drawEmptyTextBox();
				drawHealthBar(8);
			} else {
			drawGrowingBox();
			drawHealthBar(8);
			}
		} else if (Main.substate.equals("demoEnd")) {
			g.setColor(Color.green);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.white);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("End of Demo", g.getFont(), g.getFontRenderContext());
			g.drawString("End of Demo", frameWidth()/2 - (g.getFontMetrics().stringWidth("End of Demo")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		} else if (Main.substate.equals("dead")) {
			g.setColor(Color.red);
			g.fillRect(0,0,frame.getWidth(), frame.getHeight());
			g.setColor(Color.black);
			g.setFont(new Font("Consolas", Font.BOLD, frameHeight()/4));
			TextLayout tl = new TextLayout("YOU HAVE DIED", g.getFont(), g.getFontRenderContext());
			g.drawString("YOU HAVE DIED", frameWidth()/2 - (g.getFontMetrics().stringWidth("YOU HAVE DIED")/2), frame.getHeight()/2 - round(tl.getBounds().getHeight()/2));
		}
	}
	
	public void drawERGame() {
		int length = frameHeight()*3/8;
		int boxX = frameWidth()/2 - (length/2);
		int boxY = frameHeight()/2;
		int amount = length/20;
		double px = length/100.0;
		g.setColor(Color.white);
		g.fillRect(boxX - amount, boxY - amount, length + amount*2, length + amount*2);
		g.setColor(Color.black);
		g.fillRect(boxX, boxY, length, length);
		drawHeart(px, boxX, boxY);
		if (fallProteins.size() == 0) {
			Main.substate = "postgame";
			timer.start();
			animCounter = 0;
			return;
		}
		Graphics tg = g.create();
		Graphics2D g2 = (Graphics2D) tg;
		g2.setClip(boxX, boxY, length, length);
		for (int i = 0; i<fallProteins.size(); i++) {
			if (fallProteins.get(i).get() >= 0) {
				drawLysosome(g2, fallProteins.get(i), px, boxX, boxY);
			}
		}
		for (int i = 0; i<fallProteins.size(); i++) {
			FallProt p = fallProteins.get(i);
			if (p.get() >= p.runTime && ((p.y - p.radius > 100|| p.y + p.radius < 0) || (p.x - p.radius > 100 || p.x + p.radius <0))) {
				fallProteins.remove(i);
				i--;
			} else if (p.toRemove) {
				fallProteins.remove(i);
				i--;
			}
		}
	}
	
	public void setUpFallingProteins() {
		fallProteins = new ArrayList<FallProt>();
		switch (Main.knowledge) {
		case 0:
		case 1:
			addFP(50, 3, 1);
			ArrayList<Integer> locs;
			for (int i = 2; i<=6; i++) {
				locs = getLocs();
				for (int j = 0; j<i; j++) {
					addFP(locs.remove(Main.random(0,locs.size()-1)), 3, i);
				}
			}
			for (int i = 0; i<5; i++) {
				addChaserFP(5 + 10*Main.random(0,9), 3, 8 + i);
			}
			addFallProtCirc(50, 3, 14, false);
			break;
		case 2:
			for (int i = 0; i<9; i++) {
				addFP(15 + i*10, 4, 1);
			}
			for (int i = 0; i<9; i++) {
				addFP(5 + i*10, 3, 3 + i/5.0);
			}
			for (int i = 0; i<9; i++) {
				addFP(95 - 10*i, 3, 5.5 + i/5.0);
			}
			for (int i = 0; i < 3; i++) {
				addFallProtCirc(25*Main.random(1,3), 3, 8 + i*2, false);
			}
			for (int i = 0; i<5; i++) {
				addChaserFP(Main.random(5,95), 2, 15 + i/5.0);
			}
			for (int i = 0; i<5; i++) {
				addChaserFP(Main.random(5,95), 2, 17 + i/5.0);
			}
			for (int i = 0; i<5; i++) {
				addChaserFP(Main.random(5,95), 2, 19 + i/5.0);
			}
			for (int i = 0; i<5; i++) {
				addChaserFP(Main.random(5,95), 2, 21 + i/5.0);
			}
			for (int i = 0; i<5; i++) {
				addChaserFP(Main.random(5,95), 2, 23 + i/5.0);
			}
			break;
		case 3:
			for (int i = 0; i<5; i++) {
				for (int j = 0; j<10; j++) {
					addChaserFP(5 + j*10, 3, 2 + i);
				}
			}
			addFP(0, 3, 8);
			addFP(100, 3, 8);
			addFP(10, 3, 8.2);
			addFP(90, 3, 8.2);
			addFP(20, 3, 8.4);
			addFP(80, 3, 8.4);
			addFP(30, 3, 8.6);
			addFP(70, 3, 8.6);
			int center = 50;
			double counter = 8.8;
			while (center > 10) {
				addFP(center + 20, 3, counter);
				addFP(center - 20, 3, counter);
				center -= 5;
				counter += 0.2;
			}
			while (center < 90) {
				addFP(center + 20, 3, counter);
				addFP(center - 20, 3, counter);
				center +=5;
				counter += 0.2;
			}
			while (center > 30) {
				addFP(center + 20, 3, counter);
				addFP(center - 20, 3, counter);
				center -=5;
				counter += 0.2;
			}
			while (center < 70) {
				addFP(center + 20, 3, counter);
				addFP(center - 20, 3, counter);
				center +=5;
				counter += 0.2;
			}
			while (center >= 50) {
				addFP(center + 20, 3, counter);
				addFP(center - 20, 3, counter);
				center -=5;
				counter += 0.2;
			}
			addFallProtCirc(50, 4, counter + 1, false);
			addFallProtCirc(50, 4, counter + 2, true);
			addFallProtCirc(50, 4, counter + 3, false);
			addFallProtCirc(50, 4, counter + 4, true);
			addFallProtCirc(50, 4, counter + 5, false);
			addFallProtCirc(20, 4, counter + 7, false);
			addFallProtCirc(80, 4, counter + 7, false);
		}
	}
	
	public ArrayList<Integer> getLocs(){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int m = 5; m<=95; m+=10) {
			temp.add(m);
		}
		return temp;
	}
	
	public void addFP(int x, double rt, double wait) {
		fallProteins.add(new FallProt(x, rt, wait));
	}
	
	public void addChaserFP(int x, double rt, double wait) {
		fallProteins.add(new FallProt(x, rt, wait, true, false));
	}
	
}
