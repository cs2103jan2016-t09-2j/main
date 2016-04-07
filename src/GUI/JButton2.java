//@@author A0124635J
package GUI;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;

public class JButton2 extends JButton implements KeyListener{
	
	private static final long serialVersionUID = 1L;

	public JButton2(){
		setText("UPCOMING TASKS");
		addKeyListener(this);
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_ENTER){
			System.out.println("Enter there");
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
