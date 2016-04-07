//@@author A0124635J
package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

import Logic.Logic;

public class JButton5 extends JButton implements KeyListener{
	/**
	 * 
	 */
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

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
