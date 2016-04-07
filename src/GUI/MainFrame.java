//@@author A0124635J
package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public MainFrame(String title) {
		
		/*
		 * Set the name of the window
		 */
		super(title);
		
		/*
		 * Set the layout for other components to be added into the window
		 */
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		/* 
		 * Add 2 main panels into the window 
		 * TopPanel will be the display screen to show all the tasks
		 * BottomPanel will be the user input field to get commands from user and shortcut buttons as well
		 */
		
		BottomPanel bottomPanel = new BottomPanel();
		TopPanel topPanel = new TopPanel();
		
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.9;
		gbc.weightx = 1;
		add(topPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 0.3;
		add(bottomPanel, gbc);
	}
}
