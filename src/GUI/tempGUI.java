package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.*;

import Logic.Logic;
import ScheduleHacks.Task;
import ScheduleHacks.History;
import ScheduleHacks.HelpGuide;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import java.awt.Insets;
import java.awt.Label;

public class tempGUI extends JFrame implements KeyListener {

	private static final int WIDTH = 800;
	private static final int HEIGHT = 700;

	private static final String ScheduleHacks = "Schedule Hacks";

	private static final Font TITLE_FONT = new Font("Arial Black", Font.BOLD, 16);
	private static final Font TASK_FONT = new Font("Arial", Font.PLAIN, 16);
	private static final Font INPUT_FONT = new Font("Courier New", Font.BOLD, 16);

	private static Logic logicObj = Logic.getInstance();
	private static History history = History.getInstance();
	private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-uuuu");
	
	JTextField commandTab;
	JTextArea textArea;
	JTextArea textArea2;

	private static int count = 1;

	public static void main(String[] args) {
		// new tempGUI();
		tempGUI uiObj = new tempGUI();
		uiObj.startScheduleHacks();

		// tempGUI uiObj = new tempGUI();

	}

	private tempGUI() {
	}

	private void startScheduleHacks() {
		logicObj.startExecution();
		setFrame();
		while (true) {
			// readInput();
			// executeInput();
		}
	}

	public void setFrame() {
		this.setSize(WIDTH, HEIGHT);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(ScheduleHacks);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("SHIcon.png"));
		setPanel();
		this.setVisible(true);
		// Make textField get the focus whenever frame is activated.
		this.addWindowFocusListener(new WindowAdapter() {
			public void windowGainedFocus(WindowEvent e) {
				commandTab.requestFocusInWindow();
			}
		});
	}

	public void setPanel() {

		JPanel DisplayPanel = new JPanel();
		DisplayPanel.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();

		setTopHeaders(DisplayPanel, constraints);
		setTaskLists(DisplayPanel, constraints);
		setCommandInputField(DisplayPanel, constraints);
		setTabButtons(DisplayPanel, constraints);
		setFeedbackArea(DisplayPanel, constraints);
		// setFeedbackPanel();

		this.add(DisplayPanel);
	}

	public void setTopHeaders(JPanel DisplayPanel, GridBagConstraints constraints) {
		JLabel TimedTaskLabel = getHeaderLabel("Upcoming Tasks");
		JLabel UntimedTaskLabel = getHeaderLabel("Trivial Tasks");

		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 3;
		constraints.weighty = 1;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.BOTH;
		DisplayPanel.add(TimedTaskLabel, constraints);

		constraints.weightx = 1;
		constraints.gridx = 4;
		DisplayPanel.add(UntimedTaskLabel, constraints);
	}

	public void setTaskLists(JPanel DisplayPanel, GridBagConstraints constraints) {
		JTextArea UpcomingTasks = getTaskListArea(logicObj.getScheduledTasksToDo());
		JTextArea FloatingTasks = getTaskListArea(logicObj.getFloatingTasksToDo());

		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.weightx = 3;
		constraints.weighty = 15;
		DisplayPanel.add(UpcomingTasks, constraints);

		constraints.weightx = 1;
		constraints.gridx = 4;
		DisplayPanel.add(FloatingTasks, constraints);
	}

	public void setCommandInputField(JPanel DisplayPanel, GridBagConstraints constraints) {
		commandTab = new JTextField("", 4);
		// commandTab.setLineWrap(true);
		// commandTab.setWrapStyleWord(true);
		commandTab.requestFocus();
		commandTab.setEditable(true);
		commandTab.setFont(INPUT_FONT);
		commandTab.addKeyListener(this);

		constraints.gridx = 1;
		constraints.gridy = 12;
		constraints.weightx = 3;
		constraints.weighty = 2;
		DisplayPanel.add(commandTab, constraints);
	}
	
	public void setTabButtons(JPanel DisplayPanel, GridBagConstraints constraints) {
		JButton UpComingButton = new JButton("Upcoming Tasks");
		JButton TrivialButton = new JButton("Trivial Tasks");
		JButton HomeButton = new JButton("Home");
		JButton HelpButton = new JButton("Help");
		
		constraints.gridx = 4;
		constraints.gridy = 12;
		constraints.weightx = 1;
		constraints.weighty = 2;
		DisplayPanel.add(UpComingButton, constraints);
		
		constraints.gridy = 14;
		DisplayPanel.add(TrivialButton, constraints);
		
		constraints.gridy = 16;
		DisplayPanel.add(HomeButton, constraints);
		
		constraints.gridy = 18;
		DisplayPanel.add(HelpButton, constraints);
	}	
	
	public void setFeedbackArea(JPanel DisplayPanel, GridBagConstraints constraints) {
		textArea = new JTextArea("Feedback Panel");
		textArea2 = new JTextArea("Task Info Panel");
		// JTextArea textArea = new JTextArea(logicObj.getFeedBack());

		// textArea.append("\n");
		// textArea.append(str);

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(TASK_FONT);
		textArea.setEditable(false);
		
		textArea2.setLineWrap(true);
		textArea2.setWrapStyleWord(true);
		textArea2.setFont(TASK_FONT);
		textArea2.setEditable(false);

		constraints.gridx = 1;
		constraints.gridy = 14;
		constraints.weightx = 3;
		constraints.weighty = 2;
		DisplayPanel.add(textArea, constraints);
		
		constraints.gridy = 16;
		DisplayPanel.add(textArea2, constraints);
	}

	public JLabel getHeaderLabel(String text) {
		JLabel textLabel = new JLabel(text, JLabel.CENTER);
		textLabel.setFont(TITLE_FONT);
		return textLabel;
	}

	public JTextArea getTaskListArea(ArrayList<Task> taskList) {
		JTextArea textArea = new JTextArea("");
		JScrollPane scrollbar = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		for (Task task : taskList) {
			textArea.append((count++) + ". " + task.getDescription() + "\n");
			if (task.isScheduledTask()) {
				appendDateTime(textArea, task);
			}
		}

		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(TASK_FONT);
		textArea.setEditable(false);
		// textArea.add(scrollbar);
		return textArea;
	}

	public void appendDateTime(JTextArea textArea, Task task) {
		if (task.getStartDate() != null && task.getStartTime() != null) {
			textArea.append("\t From ");
			if (!task.getStartTime().equals(LocalTime.MAX)) {
				textArea.append(task.getStartTime().toString() + ", ");
			}
			textArea.append(task.getStartDate().format(dateFormat) + "\n");
			textArea.append("\t To ");
		} else {
			textArea.append("\t At ");
		}
		if (!task.getEndTime().equals(LocalTime.MAX)) {
			textArea.append(task.getEndTime().toString() + ", ");
		}
		textArea.append(task.getEndDate().format(dateFormat) + "\n");
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();

		if (keyCode == KeyEvent.VK_ENTER) {
			String input = commandTab.getText();
			commandTab.setText("");

			logicObj.executeCommand(input);
			textArea.setText(logicObj.getFeedBack());
			textArea2.setText(input);

			/*if (!logicObj.hasSearchList()) { // print the normal display
				OList = new ArrayList<Task>(logicObj.getScheduledTasksOverDue());
				SList = new ArrayList<Task>(logicObj.getScheduledTasksToDo());
				FList = new ArrayList<Task>(logicObj.getFloatingTasksToDo());
				TopLeftPanel.clearText();
				TopLeftPanel.setText(OList, SList, null);
				TopRightPanel.clearText();
				TopRightPanel.setText(FList, null, OList.size() + SList.size());
			}*/
		}

		if (keyCode == KeyEvent.VK_UP) {
			textArea.setText("Previous Command");
			commandTab.setText(history.moveUpCommandHistory());
		}
		if (keyCode == KeyEvent.VK_DOWN) {
			textArea.setText("Next Command");
			commandTab.setText(history.moveDownCommandHistory());
		}
		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
