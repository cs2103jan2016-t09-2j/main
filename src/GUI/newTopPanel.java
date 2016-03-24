package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class newTopPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public newTopPanel(){
		Dimension size = getPreferredSize();
		size.height = 270;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		newTopLeftPanel newtopleftPanel = new newTopLeftPanel();
		newTopRightPanel newtoprightPanel = new newTopRightPanel();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.9;
		add(newtopleftPanel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.3;
		add(newtoprightPanel, gbc);
	}
}
