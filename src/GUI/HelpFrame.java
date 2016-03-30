package GUI;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ScheduleHacks.HelpGuide;
//import javax.swing.JTextPane;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.StyledDocument;
 
public class HelpFrame extends JFrame implements KeyListener{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//private static JTextPane textPane;
	private static JTextArea textArea;
	private static JScrollPane scrollPane;
	private static ArrayList<String> collatedHelpList;
	
	public HelpFrame(String title){
    	super(title);
    	setLayout(new GridLayout());
    	textArea = new JTextArea();
    	scrollPane = new JScrollPane(textArea);
    	textArea.setEditable(false);
    	/*textPane = new JTextPane();
		scrollPane = new JScrollPane(textPane);
		textPane.setEditable(false); */
		add(scrollPane);
		collatedHelpList = new ArrayList<String>();
		collatedHelpList = (new HelpGuide()).getCollatedList();
		setHelpSheet(collatedHelpList);
		textArea.addKeyListener(this);
    }
	
	public void setHelpSheet(ArrayList<String> collatedHelpList){
		for(int i = 0; i < collatedHelpList.size(); i++){
			textArea.append(collatedHelpList.get(i));
			/*textPane.setText(collatedHelpList.get(i));
			StyledDocument document = (StyledDocument) textPane.getDocument();
		     try {
				document.insertString(document.getLength(), "hi", null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}*/
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
	}
}
