/*********************** DO NOT DELETE *************************/

/*
 * This is a temporary User Interface while the GUI prepares the final.
 * DO NOT DELETE THIS FILE.
 * 
 * @author Snigdha Singhania
 */

/*********************** DO NOT DELETE *************************/

package GUI;

import java.util.Scanner;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ScheduleHacks.Task;
import Logic.Logic;

public class TempCLI {

	private static final Scanner scanner = new Scanner(System.in);

	private static final String MESSAGE_WELCOME = "Welcome to ScheduleHacks!";
	private static final String MESSAGE_BYE = "Good Bye!";
	private static Logic logicObj = Logic.getInstance();

	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-uuuu");
	
	public static int count = 1;
	
	
	private String userCommand;

	public static void main(String[] args) {
		showToUser(MESSAGE_WELCOME);
		TempCLI taskManager = new TempCLI();
		taskManager.startScheduleHacks(logicObj);
	}

	private void startScheduleHacks(Logic logicObj) {
		logicObj.startExecution();
		while (true) {
			//printTaskLists(logicObj);
			readInput();
			executeInput(logicObj);
		}
	}
	
	public void setUserCommand(String newCommand) {
		this.userCommand = newCommand;
	}

	public String getUserCommand() {
		return this.userCommand;
	}

	public void printFeedback() {
		System.out.println();
	}

	public void executeInput(Logic logicObj) {
		count = 1;
		logicObj.executeCommand(getUserCommand());
		//logicObj.autoChangeTaskStatus();
		if (!logicObj.hasSearchList()) {
			printTaskLists(logicObj);
		}
		if(!logicObj.getFeedBack().isEmpty())
		showToUser(logicObj.getFeedBack());
		
	}
	
	public void printSearchTaskLists(ArrayList<Task> listToPrint) {
		System.out.println();
		System.out.println("***********************************************************");
		System.out.println("                          SEARCH QUERY ");
		System.out.println("***********************************************************");
		//showTimedTaskListToUser(logicObj.getSearchedTasks());
		
		for (Task task : listToPrint) {
			if (task.isFloatingTask())
				printUntimedTask(task);
			else
				printTimedTask(task);
		}
		
		System.out.println("***********************************************************");
		System.out.println();
	}
	
	public void printTaskLists(Logic logicObj) {
		System.out.println();
		System.out.println("******** OVERDUE TASKS ********");
		showTimedTaskListToUser(logicObj.getScheduledTasksOverDue());
		System.out.println("******** UPCOMING TASKS ********");
		showTimedTaskListToUser(logicObj.getScheduledTasksToDo());
		System.out.println("******** FLOATING TASKS ********");
		showUntimedTaskListToUser(logicObj.getFloatingTasksToDo());
		System.out.println();
	}

	public void showTimedTaskListToUser(ArrayList<Task> taskList) {
		for (Task task : taskList) {
			printTimedTask(task);
		}
	}

	public void showUntimedTaskListToUser(ArrayList<Task> taskList) {
		for (Task task : taskList) {
			printUntimedTask(task);
		}
	}
	
	private void printUntimedTask(Task task) {
		System.out.println((count++) + ". " + task.getDescription());
	}

	private void printTimedTask(Task task) {
		System.out.println((count++) + ". " + task.getDescription());
		if (task.getStartDate() != null && task.getStartTime() != null) {
			System.out.print("\t From ");
			if (!task.getStartTime().equals(LocalTime.MAX)) {
				System.out.print(task.getStartTime().toString() + ", ");
			}
			System.out.println(task.getStartDate().format(dateFormat));
			System.out.print("\t To ");
		} else {
			System.out.print("\t At ");
		}
		if (!task.getEndTime().equals(LocalTime.MAX)) {
			System.out.print(task.getEndTime().toString() + ", ");
		}
		System.out.println(task.getEndDate().format(dateFormat));
	}
	
	public Logic getLogicObj (Logic logicObj) {
		return logicObj;
	}
	public static void exitScheduleHacks() {
		showToUser(MESSAGE_BYE);
		System.exit(0);
	}

	public void readInput() {
		System.out.print("Enter Command here: ");
		setUserCommand(scanner.nextLine());
	}

	public static void showToUser(String someText) {
		System.out.println(someText);
	}
}

