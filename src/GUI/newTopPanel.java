package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ScheduleHacks.Task;

public class newTopPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int count = 1;
	private static boolean isNotEmpty = false;

	private static JTextArea textArea;
	private JScrollPane scrollPane;
	
	public newTopPanel(){
		Dimension size = getPreferredSize();
		size.height = 270;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridLayout());
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		add(scrollPane);
	}

	public static void setText(String string) {
		textArea.append(string + "\n");
		textArea.append("Bye");
	}

	public static void setText(ArrayList<Task> OList, ArrayList<Task> SList,ArrayList<Task> FList){
		printOut(OList);
		printOut(SList);
		printOut(FList);
	}

	public static void printOut(ArrayList<Task> List){
		if(isNotEmpty){
			textArea.setText(null);
			count = 1;
		}
		for (Task task : List) {
			isNotEmpty = true;
			String string = task.getDescription();
			textArea.append(count + ". " + string + "\n");
			count++;
		}

	}
}
