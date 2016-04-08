//@@author A0124635J
package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BottomRight extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int count = 0;

	private static JButton1 b1;
	private static JButton2 b2;
	private static JButton3 b3;
	private static JButton4 b4;

	public BottomRight() {

		/*
		 * Set the size of the panel that contains all the buttons
		 */
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int) (height / 7.2);
		setPreferredSize(size);
		setBorder(null);

		/*
		 * Set the layout for the all the buttons to be added into this panel
		 */
		setLayout(new GridBagLayout());

		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;

		b1 = new JButton1();
		 b2 = new JButton2();
		b3 = new JButton3();
		b4 = new JButton4();

		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;

		gc.gridy = 0;
		add(b1, gc);
		gc.gridy = 1;
		 add(b2,gc);
		gc.gridy = 2;
		add(b3, gc);
		gc.gridy = 3;
		add(b4, gc);
	}

	public static JButton getButton1() {
		return b1;
	}
	
	public static JButton getButton2() {
		return b2;
	}
	
	public static JButton getButton3() {
		return b3;
	}
	
	public static JButton getButton4() {
		return b4;
	}

}
