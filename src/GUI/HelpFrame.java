package GUI;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
//import javax.swing.JTextPane;
//import javax.swing.text.BadLocationException;
//import javax.swing.text.StyledDocument;
 
public class HelpFrame extends JFrame{

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
		collatedHelpList = ScheduleHacks.HelpGuide.getCollatedList();
		setHelpSheet(collatedHelpList);
    }
	
	public void setHelpSheet(ArrayList<String> collatedHelpList){
		System.out.println(collatedHelpList.size());
		textArea.append("hello");
		for(int i = 0; i < collatedHelpList.size(); i++){
			textArea.append(collatedHelpList.get(i));
			System.out.println(collatedHelpList.get(i));
			/*textPane.setText(collatedHelpList.get(i));
			StyledDocument document = (StyledDocument) textPane.getDocument();
		     try {
				document.insertString(document.getLength(), "hi", null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}*/
		}
	}
}
