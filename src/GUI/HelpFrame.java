package GUI;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;

public class HelpFrame extends JFrame{

	private static final long serialVersionUID = 1L;

	HelpPanel1 help1;
	HelpPanel2 help2;
	public HelpFrame(String title) {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		JFrame helpFrame = this;
		
		help1 = new HelpPanel1(helpFrame);
		help2 = new HelpPanel2(helpFrame);
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		add(help1, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		add(help2, gc);
		this.setTitle(title);
	}
}
