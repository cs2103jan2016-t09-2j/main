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

import ScheduleHacks.Task;
import Logic.Logic;

public class TempCLI {

	private static final Scanner scanner = new Scanner(System.in);

	private static final String MESSAGE_WELCOME = "Welcome to ScheduleHacks!";
	private static final String MESSAGE_BYE = "Good Bye!";

	public static int count = 1;

	private String userCommand;

	public static void main(String[] args) {
		showToUser(MESSAGE_WELCOME);
		TempCLI taskManager = new TempCLI();
		taskManager.startScheduleHacks();
	}

	private void startScheduleHacks() {
		while (true) {
			readInput();
			executeInput();
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
	public void executeInput() {
		count = 1;
		Logic logicObj = new Logic();
		logicObj.startExecution(getUserCommand());
		showToUser(logicObj.getFeedBack());
		showTimedTaskListToUser(logicObj.getScheduledTasksToDo());
		showUntimedTaskListToUser(logicObj.getFloatingTasksToDo());
	}

	public void showTimedTaskListToUser(ArrayList<Task> taskList) {
		System.out.println("******** SCHEDULED TASKS ********");
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
				System.out.print("\t By ");
			}
			if (!task.getEndTime().equals(LocalTime.MAX)) {
				System.out.print(task.getEndTime().toString() + ", ");
			}
			System.out.println(task.getEndDate());
		}
	}

	public void showUntimedTaskListToUser(ArrayList<Task> taskList) {
		System.out.println("******** FLOATING TASKS ********");
		for (Task task : taskList) {
			System.out.println((count++) + ". " + task.getDescription());
		}
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
