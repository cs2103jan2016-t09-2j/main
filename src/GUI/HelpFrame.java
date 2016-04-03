package GUI;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ScheduleHacks.HelpGuide;
//import javax.swing.JTextPane;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.StyledDocument;

public class HelpFrame extends JFrame{ //implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// private static JTextPane textPane;
	/*private static JTextPane textArea;
	private static JScrollPane scrollPane;
	private static ArrayList<String> collatedHelpList;

	private static StyledDocument document;

	//private static SimpleAttributeSet header = new SimpleAttributeSet();
	private static SimpleAttributeSet helpInfo = new SimpleAttributeSet();*/

	HelpPanel1 help1;
	HelpPanel2 help2;
	public HelpFrame(String title) {
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		help1 = new HelpPanel1();
		help2 = new HelpPanel2();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		add(help1, gc);

		gc.gridx = 1;
		gc.gridy = 0;
		add(help2, gc);
		/*super(title);
		setLayout(new GridLayout());
		textArea = new JTextPane();
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		add(scrollPane);
		collatedHelpList = new ArrayList<String>();
		collatedHelpList = (new HelpGuide()).getCollatedList();
		
		textArea.setBackground(Color.BLACK);
/*
		StyleConstants.setFontFamily(header, "Comic Sans");
		StyleConstants.setAlignment(header, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(header, true);*/

		/*StyleConstants.setFontFamily(helpInfo, "Comic Sans");
		StyleConstants.setFontSize(helpInfo, 13);
		StyleConstants.setLineSpacing(helpInfo, (float) 0.4);
		StyleConstants.setBold(helpInfo, true);
		StyleConstants.setForeground(helpInfo, Color.WHITE);

		setHelpSheet(collatedHelpList);
		textArea.addKeyListener(this);*/
	}

	/*public void setHelpSheet(ArrayList<String> collatedHelpList) {
		try {
			document = textArea.getStyledDocument();

			for (int i = 0; i < collatedHelpList.size(); i++) {
				document.insertString(document.getLength(), collatedHelpList.get(i), helpInfo);
			}
			document.setParagraphAttributes(0, document.getLength(), helpInfo, true);
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	public void keyReleased(KeyEvent arg0) {
		// not used
	}

	public void keyTyped(KeyEvent arg0) {
		// not used
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ESCAPE) {
			setVisible(false);
		}
	}*/
}
