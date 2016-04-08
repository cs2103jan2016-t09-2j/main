//@@author A0124635J
package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BottomPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	private BottomLeft bottomLeft;
	private static BottomRight bottomRight;

	public BottomPanel(){
		 
		/*
		 * Set the size of the panel that contains BottomLeft panel and BottomRight panel
		 */
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/7.2);
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));
		
		/*
		 * Set the layout for the two panels to be added into this panel
		 */
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		bottomLeft = new BottomLeft();
		bottomRight = new BottomRight();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 3.7;
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