package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Logic.Logic;
import ScheduleHacks.Task;

public class TopRightPanel extends JPanel {

	/**
	 * 
	 */
	private static Logic logicObj = Logic.getInstance();
	
	private static final long serialVersionUID = 1L;
	private static JTextArea textArea;
	private JScrollPane scrollPane;
	private static String FLOATING_HEADER = "FLOATING TASKS";
	private static String CENTER_FORMAT = "         ";

	private static final Font TITLE_FONT = new Font("Comic Sans", Font.BOLD, 16);
	private static final Font TASK_FONT = new Font("Comic Sans", Font.PLAIN, 16);

	public TopRightPanel() {
		Dimension size = getPreferredSize();
		size.height = 268;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridLayout());
		textArea = new JTextArea();
		scrollPane = new JScrollPane(textArea);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setFont(TASK_FONT);
		add(scrollPane);
		setText(logicObj.getFloatingTasksToDo(), null, logicObj.getScheduledTasksOverDue().size() + logicObj.getScheduledTasksToDo().size());
		//textArea.append(CENTER_FORMAT + FLOATING_HEADER + "\n");
	}

	public static void setText(ArrayList<Task> FList, ArrayList<Integer> indexList, int UpcomingTaskSize) {
		if (indexList == null || indexList.isEmpty()) {
			indexList = new ArrayList<Integer>();
			for (int index = 1; index <= FList.size(); index++) {
				indexList.add(index + UpcomingTaskSize);
			}
		}
		printOut(FList, indexList);
	}

	public static void printOut(ArrayList<Task> List, ArrayList<Integer> indexList) {
		int count = 0;
		textArea.append(CENTER_FORMAT + FLOATING_HEADER + "\n");
		textArea.append("\n");
		for (Task task : List) {
			String string = task.getDescription();
			textArea.append(indexList.get(count) + ".");
			textArea.append(" " + string + "\n");
			count++;
		}
	}

	public static void clearText() {
		textArea.setText(null);
	}

	public static void setCount(int noOfTask) {
	}
}
