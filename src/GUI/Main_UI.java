//@@author A0124635J
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import Logic.Logic;
import ScheduleHacks.Task;

public class Main_UI {

	static JFrame newFrame = new MainFrame("Schedule Hacks");

	public static void main(String[] args) {

		long period = 100;
		// And From your main() method or any other method
		Timer timer = new Timer();
		timer.schedule(new TabFlow(), 0, period);

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {

				/*
				 * Create the main window of the User Interface
				 */
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				double width = screenSize.getWidth();
				double height = screenSize.getHeight();
				int width1 = (int) (width / 2.4);
				int height1 = (int) (height / 1.54);
				newFrame.setSize(width1, height1);
				newFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("SHIcon.png"));
				newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				newFrame.setVisible(true);
				newFrame.setResizable(false);
				newFrame.setLocationRelativeTo(null);
				newFrame.getContentPane().setBackground(new Color(107, 179, 166));

				/*
				 * Make textField get the focus whenever frame is activated
				 */
				newFrame.addWindowFocusListener(new WindowAdapter() {
					public void windowGainedFocus(WindowEvent e) {
						BottomBottom.getCommandField().requestFocusInWindow();
					}
				});
			}
		});
	}

	public static JFrame getFrame() {
		return newFrame;
	}
}

// @@author A0132778W

/**
 * This class regularly checks which component of the JFrame has the focus and
 * places a border around it.
 */
class TabFlow extends TimerTask {
	Color prevColor = null;
	JComponent prevFocus = null;
	JComponent thisFocus = null;

	public void run() {
		thisFocus = (JComponent) Main_UI.getFrame().getFocusOwner();
		if (thisFocus != null) {
			if (prevFocus == null || !thisFocus.equals(prevFocus)) {
				if (prevFocus != null) {
					prevFocus.setBackground(prevColor);
				}
				// keeps track of the previous component.
				prevColor = thisFocus.getBackground();
				prevFocus = thisFocus;
				thisFocus.setBackground(new Color(255, 255, 224));
			}
		}
	}
}