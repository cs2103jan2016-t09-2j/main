package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BottomTop extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea feedbackField;
	JScrollPane scrollPane;
	
	public BottomTop(){
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/11.2);
		setPreferredSize(size);

		setLayout(new GridLayout());
        feedbackField = new JTextArea();
        feedbackField.setEditable(false);
        scrollPane = new JScrollPane(feedbackField);
        add(scrollPane);
	}
	
	public void setText(String input){
		feedbackField.setText(input);
	}
	
}
