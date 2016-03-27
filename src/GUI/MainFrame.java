package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MainFrame(String title) {
		super(title);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
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
