package GUI;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ScheduleHacks.HelpGuide;

public class HelpPanel1 extends JPanel implements KeyListener{

	private static final long serialVersionUID = 1L;

	JTextPane textArea;
	ArrayList<String> collatedHelpList;
	private static StyledDocument document;
	private static SimpleAttributeSet helpInfo = new SimpleAttributeSet();
	JFrame helpFrame;

	public HelpPanel1(JFrame HelpFrame){
		helpFrame = HelpFrame;
		setLayout(new GridLayout());
		textArea = new JTextPane();
		textArea.setEditable(false);
		add(textArea);
		collatedHelpList = new ArrayList<String>();
		collatedHelpList = (new HelpGuide()).getCollatedList();

		textArea.setBackground(Color.BLACK);

		StyleConstants.setFontFamily(helpInfo, "Comic Sans");
		StyleConstants.setFontSize(helpInfo, 13);
		StyleConstants.setLineSpacing(helpInfo, (float) 0.4);
		StyleConstants.setBold(helpInfo, true);
		StyleConstants.setForeground(helpInfo, Color.WHITE);

		setHelpSheet(collatedHelpList);
		textArea.addKeyListener(this);
	}

	public void setHelpSheet(ArrayList<String> collatedHelpList) {
		try {
			document = textArea.getStyledDocument();
			for (int i = 0; i < 44; i++) {
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
			helpFrame.dispose();
		}
	}
}
