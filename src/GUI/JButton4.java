//@@author A0124635J
package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import Logic.Logic;

public class JButton4 extends JButton implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static Logic logicObj = Logic.getInstance();

	private static final String EXIT_LABEL = "EXIT";

	public JButton4() {
		setText(EXIT_LABEL);
		addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_ENTER) {
			logicObj.executeCommand(EXIT_LABEL);
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
