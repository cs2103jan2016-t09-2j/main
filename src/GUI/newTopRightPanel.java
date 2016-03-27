package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ScheduleHacks.Task;

public class newTopRightPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JTextArea textArea;
	private JScrollPane scrollPane;
	private static int count;
	private static String FLOATING_HEADER = "FLOATING TASKS";
	private static String CENTER_FORMAT = "         ";

	public newTopRightPanel(){
		Dimension size = getPreferredSize();
		size.height = 268;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridLayout());
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		add(scrollPane);
	}

	public static void setText(ArrayList<Task> FList){
		printOut(FList);
		count = 1;
	}

	public static void printOut(ArrayList<Task> List){

		textArea.append(CENTER_FORMAT + FLOATING_HEADER + "\n");
		for (Task task : List) {
			String string = task.getDescription();
			if(string.length() < 17){
				textArea.append(count + ". " + string + "\n");
				count++;
			}
			else{
				String[] string_split = string.split(" ");
			}
		}
	}

	public static void clearText() {
		textArea.setText(null);
	}

	public static void setCount(int noOfTask){
		count = noOfTask;
	}
}
