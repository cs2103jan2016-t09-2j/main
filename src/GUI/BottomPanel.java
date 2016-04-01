package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

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
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/7.2);
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
<<<<<<< HEAD
		gc.gridx = 1;
		gc.gridy = 0;
		add(bottomRight,gc);
=======
		gc.gridx = 0;
		gc.gridy = 1;
		add(bottomBottom,gc);
		
		this.setBackground(Color.LIGHT_GRAY);
		this.setBorder(null);

	}

	public String getText(){
		System.out.println(text);
		return text;
	}
>>>>>>> a0c65598ace1bd50eec298795b04f2cff0413b68

	}
}