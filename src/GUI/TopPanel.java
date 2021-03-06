//@@author A0124635J
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class TopPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public TopPanel(){
		/*
		 * Set the size of the panel that contains display screen
		 */
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/2.16);
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));
		
		/*
		 * Set the layout for the 2 display screens to be added into this panel
		 */
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		TopLeftPanel topLeftPanel = new TopLeftPanel();
		TopRightPanel topRightPanel = new TopRightPanel();
		
		this.setBackground(new Color(107, 179, 166));
		this.setBorder(null);
		
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
