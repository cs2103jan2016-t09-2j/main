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
	private BottomBottom bottomBottom;
	private static BottomTop bottomTop;
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

<<<<<<< HEAD
		bottomLeft = new BottomLeft();
		bottomRight = new BottomRight();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 3;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		add(bottomLeft, gc);
=======
		//bottomLeft = new BottomLeft();
		//bottomRight = new BottomRight();
		bottomBottom = new BottomBottom();
		bottomTop = new BottomTop();
		gc.fill = GridBagConstraints.HORIZONTAL;
>>>>>>> 1388e364baa35054d5bd027f4153185e31b1edb0
		
		gc.weightx = 1;
		gc.gridx = 3;
		gc.gridy = 0;
		add(bottomRight, gc);
		
		/*bottomBottom = new BottomBottom();
		bottomTop = new BottomTop();
		gc.fill = GridBagConstraints.HORIZONTAL;
		
		gc.weightx = 3;
		gc.weighty = 3;
		gc.gridx = 0;
		gc.gridy = 0;
		add(bottomTop,gc);

		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 3;
		add(bottomBottom,gc);
		
		gc.fill = GridBagConstraints.VERTICAL;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 3;
		gc.gridy = 0;
		add(button1,gc);
		
		gc.gridy = 1;
		add(button2);
		
		gc.gridy = 2;
		add(button3);
		
		gc.gridy = 3;
		add(button4);
		
		gc.gridy = 4;
		add(button5);
		
		this.setBackground(new Color(107, 179, 166));
		this.setBorder(null); */

	}

	public String getText(){
		System.out.println(text);
		return text;
	}
	
	public static void setFeedback(String feedback){
		bottomTop.setText(feedback);

	}
}