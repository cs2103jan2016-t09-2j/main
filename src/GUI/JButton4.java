package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import Logic.Logic;

public class JButton4 extends JButton implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logic logicObj = Logic.getInstance();

	public JButton4(){
		setText("HOME");
		addKeyListener(this);
	}
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER){
			logicObj.executeCommand("home");
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
