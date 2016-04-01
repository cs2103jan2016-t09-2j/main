package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class BottomTop extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea feedbackField;
	
	public BottomTop(){
		setLayout(new BorderLayout());
		//Dimension size = getPreferredSize();
		//size.height = 70;
		//setPreferredSize(size);
		
		setLayout(new GridLayout());
        feedbackField = new JTextArea();
        feedbackField.setEditable(false);
        add(feedbackField);
	}
	
	public void setText(String input){
		feedbackField.setText(input);
	}
	
}
