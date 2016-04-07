package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BottomTop extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private JTextArea feedbackField;
	JScrollPane scrollPane;
	
	public BottomTop(){
		
		/*
		 * Set the size of the panel that contains feedback field
		 */
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/11.2);
		setPreferredSize(size);

		/*
		 * Set the layout for the component in this panel
		 */
		setLayout(new GridLayout());
        feedbackField = new JTextArea();
        feedbackField.setEditable(false);
        scrollPane = new JScrollPane(feedbackField);
        add(scrollPane);
	}
	
	/*
	 * Set the feedback to show to user in the feedback field
	 */
	public void setText(String input){
		feedbackField.setText(input);
	}
	
}
