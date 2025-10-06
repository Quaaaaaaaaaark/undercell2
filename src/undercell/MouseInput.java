package undercell;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseInput extends MouseAdapter{
	protected mGraphics jonathan;
	
	public MouseInput(mGraphics jon) {
		jonathan = jon;
	}
	
	public void mousePressed(MouseEvent e) {
		System.out.println("clicked");
		jonathan.requestFocus();
	}

}
