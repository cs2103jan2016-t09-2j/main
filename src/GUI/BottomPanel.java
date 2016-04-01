package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BottomPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private static BottomLeft bottomLeft;
	//private BottomRight bottomRight;
	private BottomLeft bottomLeft;
	private static BottomRight bottomRight;
	JButton button1 = new JButton("TRIVAL");
	JButton button2 = new JButton("UPCOMING");
	JButton button3 = new JButton("ARCHIEVE");
	JButton button4 = new JButton("HOME");
	JButton button5 = new JButton("HELP");
	String text;

	public BottomPanel(){
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/7.2);
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		bottomLeft = new BottomLeft();
		bottomRight = new BottomRight();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 3;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		add(bottomLeft, gc);

		
		gc.weightx = 1;
		gc.gridx = 3;
		gc.gridy = 0;
		add(bottomRight, gc);
	}
}