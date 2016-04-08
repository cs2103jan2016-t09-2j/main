//@@author A0124635J
package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import Logic.Logic;

public class JButton1 extends JButton implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static Logic logicObj = Logic.getInstance();

	public JButton1() {
		setText("HOME");
		addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			logicObj.executeCommand("home");
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	public void keyReleased(KeyEvent e) {
		// unused
	}

	public void keyTyped(KeyEvent e) {
		// unused
	}
}
