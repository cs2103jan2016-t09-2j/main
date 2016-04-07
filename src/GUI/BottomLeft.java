//@@author A0124635J
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BottomLeft extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static BottomTop bottomTop;
	private BottomBottom bottomBottom;
	String text;
	
	public BottomLeft(){
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/7.2);
		setPreferredSize(size);
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		bottomTop = new BottomTop();
		bottomBottom = new BottomBottom();

		gc.fill = GridBagConstraints.BOTH;

		gc.weightx = 1;
		gc.weighty = 3;
		gc.gridx = 0;
		gc.gridy = 0;
		add(bottomTop,gc);

		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 3;
		add(bottomBottom,gc);

		this.setBackground(new Color(107, 179, 166));
		//this.setBorder(null);
	}

	public String getText(){
		return text;
	}

	public static void setFeedback(String feedBack) {
		bottomTop.setText(feedBack);
	}
}

