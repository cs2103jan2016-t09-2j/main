package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BottomPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static BottomLeft bottomLeft;
	private BottomRight bottomRight;
	String text;

	public BottomPanel(){
		Dimension size = getPreferredSize();
		size.height = 100;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		bottomLeft = new BottomLeft();
		bottomRight = new BottomRight();

		gc.fill = GridBagConstraints.HORIZONTAL;

		gc.weighty = 0.9;
		gc.gridx = 0;
		gc.gridy = 0;
		add(bottomLeft,gc);

		gc.weighty = 0.3;
		gc.gridx = 1;
		gc.gridy = 0;
		add(bottomRight,gc);

	}
}