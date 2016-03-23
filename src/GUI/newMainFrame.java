package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;

public class newMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public newMainFrame(String title) {
		super(title);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		newBottomPanel newbottomPanel = new newBottomPanel();
		newTopPanel newtopPanel = new newTopPanel();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.9;
		gbc.weightx = 1;
		add(newtopPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weighty = 0.3;
		add(newbottomPanel, gbc);
	}
}
