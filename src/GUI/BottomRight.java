package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JPanel;

public class BottomRight extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JButton button1 = new JButton("TRIVIAL TASKS");
	JButton button2 = new JButton("UPCOMING TASKS");
	JButton button3 = new JButton("ARCHIVE");
	JButton button4 = new JButton("HOME");
	JButton button5 = new JButton("HELP");
	
	public BottomRight(){
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/7.2);
		setPreferredSize(size);
		setBorder(null);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;

		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;

		gc.gridy = 0;
		add(button1,gc);
		gc.gridy = 1;
		add(button2,gc);
		gc.gridy = 2;
		add(button3,gc);
		gc.gridy = 3;
		add(button4,gc);
		gc.gridy = 4;
		add(button5,gc);
	}
}
