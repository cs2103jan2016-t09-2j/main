/*
 * This is a temporary User Interface while the GUI prepares the final.
 * DO NOT DELETE THIS FILE.
 * 
 * @author Snigdha Singhania
 */

package GUI;

import java.util.Scanner;

public class TempCLI {
	
	private static final Scanner scanner = new Scanner(System.in);
	
	private static final String MESSAGE_WELCOME = "Welcome to ScheduleHacks!";
		
	private String userCommand;
	
	public static void main(String[] args) {
		showToUser(MESSAGE_WELCOME);
		TempCLI taskManager = new TempCLI();
		taskManager.startScheduleHacks();
	}
	
	private void startScheduleHacks() {
		while(true) {
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
	
	public void executeInput() {
		
		
	}
	
	public void readInput() {
		setUserCommand(scanner.nextLine());
	}
	
	public static void showToUser(String someText) {
		System.out.println(someText);
	}
}
