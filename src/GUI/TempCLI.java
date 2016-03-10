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
import java.util.ArrayList;
import java.util.TimerTask;

import ScheduleHacks.Task;
import Logic.Logic;

public class TempCLI extends TimerTask {

	private static final Scanner scanner = new Scanner(System.in);

	private static final String MESSAGE_WELCOME = "Welcome to ScheduleHacks!";
	private static final String MESSAGE_BYE = "Good Bye!";
	private static Logic logicObj = new Logic ();

	public static int count = 1;

	private String userCommand;

	public static void main(String[] args) {
		showToUser(MESSAGE_WELCOME);
		TempCLI taskManager = new TempCLI();
		taskManager.startScheduleHacks(logicObj);
	}

	private void startScheduleHacks(Logic logicObj) {
		while (true) {
			//printTaskLists(logicObj);
			readInput();
			executeInput(logicObj);
			run ();
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

	// incomplete
	// what to do to print archive ?
	public void executeInput(Logic logicObj) {
		count = 1;
		logicObj.startExecution(getUserCommand());
		
		printTaskLists(logicObj);
		
		showToUser(logicObj.getFeedBack());
		
	}

	public void printTaskLists(Logic logicObj) {
		System.out.println("******** OVERDUE TASKS ********");
		showTimedTaskListToUser(logicObj.getScheduledTasksOverDue());
		System.out.println("******** UPCOMING TASKS ********");
		showTimedTaskListToUser(logicObj.getScheduledTasksToDo());
		System.out.println("******** FLOATING TASKS ********");
		showUntimedTaskListToUser(logicObj.getFloatingTasksToDo());
	}

	public void showTimedTaskListToUser(ArrayList<Task> taskList) {
		for (Task task : taskList) {
			System.out.println((count++) + ". " + task.getDescription());
			if (task.getStartDate() != null && task.getStartTime() != null) {
				System.out.print("\t From ");
				if (!task.getStartTime().equals(LocalTime.MAX)) {
					System.out.print(task.getStartTime().toString() + ", ");
				}
				System.out.println(task.getStartDate());
				System.out.print("\t To ");
			} else {
				System.out.print("\t At ");
			}
			if (!task.getEndTime().equals(LocalTime.MAX)) {
				System.out.print(task.getEndTime().toString() + ", ");
			}
			System.out.println(task.getEndDate());
		}
	}

	public void showUntimedTaskListToUser(ArrayList<Task> taskList) {
		for (Task task : taskList) {
			System.out.println((count++) + ". " + task.getDescription());
		}
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

	public void run() {
		Logic periodicLogicObj = logicObj;
		periodicLogicObj.autoChangeTaskStatus();
		
	}
}
