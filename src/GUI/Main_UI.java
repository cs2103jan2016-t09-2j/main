package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main_UI {

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame newFrame = new MainFrame("Schedule Hacks");
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				double width = screenSize.getWidth();
				double height = screenSize.getHeight();
				int width1 = (int) (width / 2);
				int height1 = (int) (height / 1.92);
				newFrame.setSize(width1, height1);
				// newFrame.setSize(600, 400);
				newFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("SHIcon.png"));
				newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				newFrame.setVisible(true);
				newFrame.setResizable(false);

				// Make textField get the focus whenever frame is activated.
				newFrame.addWindowFocusListener(new WindowAdapter() {
					public void windowGainedFocus(WindowEvent e) {
						BottomBottom.getCommandField().requestFocusInWindow();
					}
				});
			}
		});
	}
}