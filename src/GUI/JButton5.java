package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

import Logic.Logic;

public class JButton5 extends JButton implements KeyListener{
	
	private static final long serialVersionUID = 1L;
	private static Logic logicObj = Logic.getInstance();

	public JButton5(){
		setText("HELP");
		addKeyListener(this);
	}
	
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER){
			logicObj.executeCommand("help");
		}

	}

	public void keyReleased(KeyEvent e) {
		/*
		 * Not being used
		 */
	}

	public void keyTyped(KeyEvent e) {
		/* 
		 * Not being used
		 */

	}
}
