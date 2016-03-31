package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class TopPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TopPanel(){
		Dimension size = getPreferredSize();
		size.height = 500;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		TopLeftPanel topLeftPanel = new TopLeftPanel();
		TopRightPanel topRightPanel = new TopRightPanel();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.9;
		add(topLeftPanel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.45;
		add(topRightPanel, gbc);
	}
}
