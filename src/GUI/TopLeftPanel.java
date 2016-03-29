package GUI;import java.awt.Dimension;
import java.awt.GridLayout;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ScheduleHacks.Task;

public class TopLeftPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int count = 1;
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-uuuu");
	private static JTextArea textArea;
	private JScrollPane scrollPane;
	private static String SCHEDULE_HEADER = "SCHEDULED TASKS";
	private static String OVERDUE_HEADER = "OVERDUE TASKS";
	private static String CENTER_FORMAT = "\t" + "                       ";
	
	public TopLeftPanel(){
		Dimension size = getPreferredSize();
		size.height = 268;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridLayout());
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		add(scrollPane);
		textArea.append(CENTER_FORMAT + OVERDUE_HEADER + "\n");
		textArea.append(CENTER_FORMAT + SCHEDULE_HEADER + "\n");
	}

	public static void clearText() {
		textArea.setText(null);
	}

	public static void setText(ArrayList<Task> OList, ArrayList<Task> SList){
		printOutSO(OList, "overdue");
		printOutSO(SList, "schedule");
		TopRightPanel.setCount(count);
		count = 1;
	}
	
	public static void printOutSO(ArrayList<Task> List, String type){
		if(type.equalsIgnoreCase("schedule")){
			textArea.append(CENTER_FORMAT + SCHEDULE_HEADER + "\n");
		}
		else{
			textArea.append(CENTER_FORMAT + OVERDUE_HEADER + "\n");
		}
		
		for (Task task : List) {
			String string = task.getDescription();
			textArea.append(count + ". " + string + "\n");
			if (task.getStartDate() != null && task.getStartTime() != null) {
				textArea.append("\t From ");
				if (!task.getStartTime().equals(LocalTime.MAX)) {
					textArea.append(task.getStartTime().toString() + ", ");
				}
				textArea.append(task.getStartDate().format(dateFormat));
				textArea.append("\t To ");
			} else {
				textArea.append("\t At ");
			}
			if (!task.getEndTime().equals(LocalTime.MAX)) {
				textArea.append(task.getEndTime().toString() + ", ");
			}
			textArea.append(task.getEndDate().format(dateFormat));
			textArea.append("\n");
			count++;
		}
	}
}
