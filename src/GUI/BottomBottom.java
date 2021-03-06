//@@author A0124635J

package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.TimerTask;
//import java.util.Timer;

import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Logic.Logic;
import ScheduleHacks.History;
import ScheduleHacks.Task;

public class BottomBottom extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;

	private static final int ONE_MINUTE = 60000;

	private static JTextField commandField = new JTextField();
	private ArrayList<Task> OList = new ArrayList<Task>();
	private ArrayList<Task> SList = new ArrayList<Task>();
	private ArrayList<Task> FList = new ArrayList<Task>();
	private static ArrayList<Task> searchOList = new ArrayList<Task>();
	private static ArrayList<Task> searchSList = new ArrayList<Task>();
	private static ArrayList<Task> searchFList = new ArrayList<Task>();
	History history = History.getInstance();
	private static final Font INPUT_FONT = new Font("Courier New", Font.BOLD, 16);
	private static Logic logicObj = Logic.getInstance();
	static Timer timer;

	public BottomBottom() {
		/*
		 * Set the layout for the component in user input field
		 */
		setLayout(new GridLayout(1, 1));
		commandField.setFont(INPUT_FONT);
		add(commandField);
		commandField.addKeyListener(this);

		/*
		 * To load the stored data for the first display to user
		 */
		logicObj.startExecution();

		// And From your main() method or any other method
		timer = new Timer(ONE_MINUTE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				new Refresh();
			}
		});
		timer.setInitialDelay(0);
		timer.start();
	}

	public static JTextField getCommandField() {
		return commandField;
	}

	/*
	 * Create the Help window when it is called
	 */
	public static void HelpPopUp() {
		/*
		 * Set the size and the name of the window
		 */
		JFrame Help = new HelpFrame("HELP GUIDE");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		int width1 = (int) (width / 2);
		int height1 = (int) (height / 1.2);
		Help.setSize(width1, height1);
		Help.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		Help.setVisible(true);
		Help.setResizable(false);
	}

	public void keyReleased(KeyEvent arg0) {
		/*
		 * Not being used
		 */
	}

	public void keyTyped(KeyEvent arg0) {
		/*
		 * Not being used
		 */
	}

	/*
	 * Find what key is behind pressed and do the necessary actions
	 */
	public void keyPressed(KeyEvent e) {
		boolean isUpdateCall = false;
		int keyCode = e.getKeyCode();

		/*
		 * When ENTER key is pressed, input will be read and start execution
		 */
		if (keyCode == KeyEvent.VK_ENTER) {
			isUpdateCall = startExecution();
		}

		/*
		 * UP and DOWN key will display command history for the user
		 */
		if (keyCode == KeyEvent.VK_UP) {
			setHistoryPreviousCommand();
		}

		if (keyCode == KeyEvent.VK_DOWN) {
			setHistoryNextCommand();
		}

		/*
		 * ESCAPE key will close the window
		 */
		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

		/*
		 * CTRL-Z will undo previous command
		 */
		if (keyCode == KeyEvent.VK_Z && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			isUpdateCall = undoPreviousCommand();
		}

		/*
		 * CTRL-Y will re-do the previous command
		 */
		if (keyCode == KeyEvent.VK_Y && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			isUpdateCall = redoPreviousCommand();
		}

		/*
		 * CTRL_R will refresh the display screen to show the latest display information
		 */
		if (keyCode == KeyEvent.VK_R && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
			isUpdateCall = refreshDisplay();
		}

		if (isUpdateCall) {
			updateDisplayScreen();
		}

		timer.setInitialDelay(ONE_MINUTE);
		timer.restart();
	}

	private void updateDisplayScreen() {
		if (!logicObj.hasSearchList() && !logicObj.isHomeScreen()) {
			// normal display
			OList = new ArrayList<Task>(logicObj.getScheduledTasksOverDue());
			SList = new ArrayList<Task>(logicObj.getScheduledTasksToDo());
			FList = new ArrayList<Task>(logicObj.getFloatingTasksToDo());
			TopLeftPanel.clearText();
			TopLeftPanel.setText(OList, SList, null);
			TopRightPanel.clearText();
			TopRightPanel.setText(FList, null, OList.size() + SList.size());
		}
	}

	private boolean refreshDisplay() {
		boolean isUpdateCall;
		logicObj.refresh();
		isUpdateCall = true;
		return isUpdateCall;
	}

	private boolean redoPreviousCommand() {
		boolean isUpdateCall;
		logicObj.executeCommand("redo");
		BottomLeft.setFeedback(logicObj.getFeedBack());
		isUpdateCall = true;
		history.removeLastCommandFromHistory();
		return isUpdateCall;
	}

	private boolean undoPreviousCommand() {
		boolean isUpdateCall;
		logicObj.executeCommand("undo");
		BottomLeft.setFeedback(logicObj.getFeedBack());
		isUpdateCall = true;
		history.removeLastCommandFromHistory();
		return isUpdateCall;
	}

	private void setHistoryNextCommand() {
		BottomLeft.setFeedback("Next Command");
		commandField.setText(history.moveDownCommandHistory());
	}

	private void setHistoryPreviousCommand() {
		BottomLeft.setFeedback("Previous Command");
		commandField.setText(history.moveUpCommandHistory());
	}

	private boolean startExecution() {
		boolean isUpdateCall;
		String input = commandField.getText();
		commandField.setText("");

		logicObj.executeCommand(input);
		BottomLeft.setFeedback(logicObj.getFeedBack());
		isUpdateCall = true;
		return isUpdateCall;
	}

	/*
	 * Create the Help window to show to user
	 */
	public static void setHelp() {
		HelpPopUp();
	}

	/*
	 * Classified the search results based on their categories
	 */
	public static void setSearchResult(ArrayList<Task> searchTaskList, ArrayList<Integer> searchIndexList) {
		clearArrayList();
		for (Task task : searchTaskList) {
			if (task.isFloatingTask()) {
				searchFList.add(task);
			} else {
				searchSList.add(task);
			}
		}
		setSearchPanel(searchFList, searchSList, searchIndexList);
	}

	/*
	 * Set the display screen with all the possible search results
	 */
	public static void setSearchPanel(ArrayList<Task> timedList, ArrayList<Task> trivialList,
			ArrayList<Integer> searchIndexList) {

		clearPanel();
		ArrayList<Integer> UpcomingTaskIndex = new ArrayList<Integer>(searchIndexList.subList(0, searchSList.size()));
		ArrayList<Integer> FloatingTaskIndex = new ArrayList<Integer>(
				searchIndexList.subList(searchSList.size(), searchIndexList.size()));

		TopLeftPanel.setSearchText(searchSList, UpcomingTaskIndex);
		TopRightPanel.setSearchText(searchFList, FloatingTaskIndex, UpcomingTaskIndex.size());
	}

	public static void failedSearch() {
		clearPanel();
		TopLeftPanel.setSearchText(new ArrayList<Task>(), null);
		TopRightPanel.setSearchText(new ArrayList<Task>(), null, 0);
	}

	/*
	 * Clear off all the contents in the display screen
	 */
	public static void clearPanel() {
		TopLeftPanel.clearText();
		TopRightPanel.clearText();
	}

	/*
	 * Set the display panel with the correct elements within the ArrayList
	 */
	public static void setPanel(ArrayList<Task> searchFList, ArrayList<Task> searchSList, ArrayList<Task> searchOList,
			ArrayList<Integer> searchIndexList) {

		clearPanel();
		ArrayList<Integer> UpcomingTaskIndex = new ArrayList<Integer>(
				searchIndexList.subList(0, searchOList.size() + searchSList.size()));
		ArrayList<Integer> FloatingTaskIndex = new ArrayList<Integer>(
				searchIndexList.subList(searchOList.size() + searchSList.size(), searchIndexList.size()));

		TopLeftPanel.setText(searchOList, searchSList, UpcomingTaskIndex);
		TopRightPanel.setText(searchFList, FloatingTaskIndex, UpcomingTaskIndex.size());
	}

	/*
	 * Clear off all the elements within the ArrayList
	 */
	public static void clearArrayList() {
		searchFList = new ArrayList<Task>();
		searchOList = new ArrayList<Task>();
		searchSList = new ArrayList<Task>();
	}
}

// @@author A0132778W

/**
 * This class runs every minute to ensure that tasks are always transferred to
 * Overdue List as and when needed.
 */
class Refresh {
	Refresh() {
		Logic logicObj = Logic.getInstance();
		logicObj.refresh();
		if (!logicObj.isHomeScreen() && !logicObj.hasSearchList()) {
			// normal display
			ArrayList<Task> OList = new ArrayList<Task>(logicObj.getScheduledTasksOverDue());
			ArrayList<Task> SList = new ArrayList<Task>(logicObj.getScheduledTasksToDo());
			ArrayList<Task> FList = new ArrayList<Task>(logicObj.getFloatingTasksToDo());
			TopLeftPanel.clearText();
			TopLeftPanel.setText(OList, SList, null);
			TopRightPanel.clearText();
			TopRightPanel.setText(FList, null, OList.size() + SList.size());
		}
	}
}