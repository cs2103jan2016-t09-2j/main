package GUI;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PopHelp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PopHelp(String title) {
		super(title);
		setLayout(new GridLayout());
		JTextArea textArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		add(scrollPane);
	}
}
