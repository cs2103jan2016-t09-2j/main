//@@author A0124635J
package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

import Logic.Logic;
import ScheduleHacks.History;

public class JButton2 extends JButton implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static Logic logicObj = Logic.getInstance();
	History history = History.getInstance();

	JButton2() {
		setText("ARCHIVE");
		addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
			logicObj.executeCommand("view archive");
			history.removeLastCommandFromHistory();
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