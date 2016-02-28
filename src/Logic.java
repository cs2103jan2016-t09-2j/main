import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.*;

import ScheduleHacks.Task;
import Parser.*;

class Logic {
	
	private Command.COMMAND_TYPE executeCommand;
	private Command existingCommand;
	private Task executeTask;
	private boolean digitFound = false;
	private boolean letterFound = false;
	
	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	
	public void getParsedCommand(String originalDescription){
		existingCommand = new Command(CommandParser.getParsedCommand(originalDescription));
	}
	
	public Command.COMMAND_TYPE getCommand() {
		executeCommand = existingCommand.getCommandType();
		return executeCommand;
	}
	
	public Task getTaskDescription() {
		executeTask = existingCommand.getTaskDetails();
		return executeTask;
	}
	
	public void execute(Command.COMMAND_TYPE executeCommand,Task executeTask) {
		switch (executeCommand) {
		case ADD_TASK:
			addTask(executeTask);
			break;
		case DELETE_TASK:
			deleteTask(executeTask);
			break;
		case MODIFY_TASK:
			modifyTask(executeTask);
			break;
		case COMPLETE_TASK:
			completeTask(executeTask);
			break;
		case EXIT:
			exit();	
		}
	}

	private void addTask(Task executeTask) {
		if (executeTask.isScheduledTask()){
			if ((executeTask.getDueDate().compareTo(executeTask.getStartDate())>0)) {
				scheduledTasksToDo.add(executeTask);
			}
			else if (executeTask.getDueDate().compareTo(executeTask.getStartDate())<0) {
				scheduledTasksOverDue.add(executeTask);
			}
			else {
				if (executeTask.getStartTime().compareTo(executeTask.getDueTime())<0){
					scheduledTasksToDo.add(executeTask);
				}
				else {
					scheduledTasksOverDue.add(executeTask);
				}
			}
		}
		else if (executeTask.isFloatingTask()) {
			floatingTasksToDo.add(executeTask);
		}
	}
	
	private void deleteTask(Task executeTask) {
		if ((executeTask.getDescription().length() == 2)) {
			if(Character.isDigit(executeTask.getDescription().charAt(0))) {
				digitFound = true;
			}
			else if(Character.isLetter(executeTask.getDescription().charAt(1))) {
				letterFound = true;
			}
			if (digitFound && letterFound) {
				int digitOfTask = (int)executeTask.getDescription().charAt(0);
				char firstLetterOfTask = executeTask.getDescription().charAt(1);
				
				if (firstLetterOfTask == 'u') {
					scheduledTasksToDo.remove(digitOfTask-1);//only can remove scheduledTasksToDo using this method
				}
				else if (firstLetterOfTask == 'f') {
					floatingTasksToDo.remove(digitOfTask-1);//only can remove incomplete floating tasks with this method
				}
			}
			digitFound = false;
			letterFound = false;
		}
		
		else {
			if (scheduledTasksToDo.contains(executeTask)) {
				scheduledTasksToDo.remove(executeTask);
			}
			else if (scheduledTasksOverDue.contains(executeTask)) {
				scheduledTasksOverDue.remove(executeTask);
			}
			else if (scheduledTasksComplete.contains(executeTask)) {
				scheduledTasksComplete.remove(executeTask);
			}
			else if (floatingTasksToDo.contains(executeTask)) {
				floatingTasksToDo.remove(executeTask);
			}
			else if (floatingTasksComplete.contains(executeTask)) {
				floatingTasksComplete.remove(executeTask);
			}
			else if (executeTask.getDescription().equalsIgnoreCase("all")) {
				scheduledTasksToDo.clear();//not too sure about this aspect as we
				scheduledTasksOverDue.clear();//are maintaining 5 different sets of ArrayLists
				scheduledTasksComplete.clear();//perhaps the command can be modified to
				floatingTasksToDo.clear();//"clear all upcoming s/completed s/overdue s/incomplete f/complete f" 
				floatingTasksComplete.clear();//so we will know which ArrayList to clear
			}
		}
	}
	
	private void modifyTask(Task executeTask) {
		String modifiedTaskDescription = executeTask.getDescription().replaceAll("\\s","");
		
		if ((Character.isDigit(modifiedTaskDescription.charAt(0))) && (Character.isLetter
			(modifiedTaskDescription.charAt(1)))) {
			switch (modifiedTaskDescription.substring(2,6)) {
				case ("desc"): 
					executeTask.setDescription(modifiedTaskDescription.substring(6));
					break;
				case ("date"):
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
					LocalDate editedDate = (LocalDate) df.parse(modifiedTaskDescription.substring(6));
					executeTask.setDueDate(editedDate);
					break;
				case ("time"):
					DateTimeFormatter tf  = DateTimeFormatter.ofPattern("hh:mm");
					LocalTime editedTime = (LocalTime) tf.parse(modifiedTaskDescription.substring(6));
					executeTask.setDueTime(editedTime);
					break;
			}
		}
	}
	
	private void completeTask(Task executeTask) {
		if (executeTask.isScheduledTask()){
			scheduledTasksComplete.add(executeTask);
			if (scheduledTasksToDo.contains(executeTask)) {
				scheduledTasksToDo.remove(executeTask);
			}
			else if (scheduledTasksOverDue.contains(executeTask)) {
				scheduledTasksOverDue.remove(executeTask);
			}
		}
		else if (executeTask.isFloatingTask()) {
			floatingTasksComplete.add(executeTask);
			if (floatingTasksToDo.contains(executeTask)) {
				floatingTasksToDo.remove(executeTask);
			}
		}
	}
	
	private void exit() {
		System.exit(0);//how to save everything and exit?
	}
}
