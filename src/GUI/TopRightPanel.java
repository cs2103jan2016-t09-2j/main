package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ScheduleHacks.Task;

public class TopRightPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JTextArea textArea;
	private JScrollPane scrollPane;
	private static int count;
	private static String FLOATING_HEADER = "FLOATING TASKS";
	private static String CENTER_FORMAT = "         ";

	public TopRightPanel(){
		Dimension size = getPreferredSize();
		size.height = 268;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridLayout());
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		add(scrollPane);
		textArea.append(CENTER_FORMAT + FLOATING_HEADER + "\n");
	}

	public static void setText(ArrayList<Task> FList){
		printOut(FList);
		count = 1;
	}

	public static void printOut(ArrayList<Task> List){
		String combinedString = "";
		int total_length;
		textArea.append(CENTER_FORMAT + FLOATING_HEADER + "\n");
		for (Task task : List) {
			String string = task.getDescription();
			textArea.append(count + ".");
			if(string.length() < 19){
				textArea.append(" " + string + "\n");
			}
			else{
				String[] arr = string.split(" ");
				for(int i = 0; i < arr.length; i++){
					total_length = combinedString.length() + arr[i].length();
					if(total_length < 17){
						combinedString += " ";
						combinedString += arr[i];
					}
					else{
						textArea.append(combinedString + "\n");
						combinedString = "    ";
						combinedString += arr[i];
					}
					if(i == arr.length - 1){
						textArea.append(combinedString + "\n");
						combinedString = "";
					}
				}
			}
			count++;
		}
	}

	public static void clearText() {
		textArea.setText(null);
	}

	public static void setCount(int noOfTask){
		count = noOfTask;
	}
}
