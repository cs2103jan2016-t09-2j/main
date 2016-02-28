import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.*;

import ScheduleHacks.Task;
import Parser.*;

class Logic {
	
	private Command existingCommand;
	
	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	
	/*this method gets back the parsed Command class from parser*/
	public void getParsedCommand(String originalDescription){
		existingCommand = new Command(CommandParser.getParsedCommand(originalDescription));
	}
	
	/*this method gets the specific command type to be executed such as add, delete, modify etc*/
	public Command.COMMAND_TYPE getCommand() {
		Command.COMMAND_TYPE executeCommand = existingCommand.getCommandType();
		return executeCommand;
	}
	
	/*this method retrieves all relevant task details from task class*/
	public Task getTaskDescription() {
		Task executeTask = existingCommand.getTaskDetails();
		return executeTask;
	}
	
	/*this method calls the respective execution methods for the respective command types*/
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
	
	/*adds task based on the startdate, enddate, starttime and endtime for scheduled tasks into either
	 scheduledtodo or scheduledoverdue arraylist while floating tasks are auto added into floatingtodo
	 arraylist*/
	private void addTask(Task executeTask) {
		if (executeTask.isScheduledTask()){
			if ((executeTask.getEndDate().compareTo(executeTask.getStartDate()))>0) {
				scheduledTasksToDo.add(executeTask);
			}
			else if ((executeTask.getEndDate().compareTo(executeTask.getStartDate()))<0) {
				scheduledTasksOverDue.add(executeTask);
			}
			else {
				if ((executeTask.getStartTime().compareTo(executeTask.getEndTime()))<0){
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
	
	/*deleteTask can be done either by typing only 2 characters, indicating the task number and task
	 type, or it can be done by typing the task description itself*/
	private void deleteTask(Task executeTask) {
		if ((executeTask.getDescription().length() == 2)) {
			int taskDigit = existingCommand.getTaskIndex();
			
			if (executeTask.isScheduledTask()) {
				scheduledTasksToDo.remove(taskDigit-1);
			}
			else if (executeTask.isFloatingTask()) {
				floatingTasksToDo.remove(taskDigit-1);
			}
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
	
	/*modifies task by editing description, date or time*/
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
					executeTask.setEndDate(editedDate);
					break;
				case ("time"):
					DateTimeFormatter tf  = DateTimeFormatter.ofPattern("hh:mm");
					LocalTime editedTime = (LocalTime) tf.parse(modifiedTaskDescription.substring(6));
					executeTask.setEndTime(editedTime);
					break;
			}
		}
	}
	
	/*adds task into the respective completearraylist and removes that same task from the arraylist that 
	 it is currently residing in*/
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
	
	/*this method shd be called in main at the start of the prog and shd run all the way till system
	 exits. This method automatically shifts scheduledtaskstodo to scheduledtasksoverdue when date and
	 time has exceeded due date and due time specified for scheduled task*/
	public void autoChangeTaskStatus(Task executeTask) {
		if (executeTask.isScheduledTask()) {
			LocalDate dateToday = LocalDate.now();
			LocalTime timeToday = LocalTime.now();
			
			if (scheduledTasksToDo.contains(executeTask)) {
				if (((executeTask.getEndDate()).compareTo(dateToday))<0) {
					scheduledTasksOverDue.add(executeTask);
					scheduledTasksToDo.remove(executeTask);
				}
				else if (((executeTask.getEndDate()).compareTo(dateToday)) == 0) {
					if ((timeToday.compareTo(executeTask.getEndTime())) >= 0) {
						scheduledTasksOverDue.add(executeTask);
						scheduledTasksToDo.remove(executeTask);
					}
				}
			}
		}
	}
	
	/*exiting of program. Shd save all arraylists and updated info abt the tasks before exiting*/
	private void exit() {
		System.exit(0);//how to save everything and exit?
	}
}

