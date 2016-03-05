package Logic;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import ScheduleHacks.Task;
import Parser.CommandParser;
import Parser.Command;

class Logic {
	
	private String feedBack;
	private ArrayList<Task> updatedTaskList;
	
	private static final ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private static final ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private static final ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private static final ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private static final ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	
	private static final String FEEDBACK_INVALID_COMMAND = "Invalid Command!";
	private static final String FEEDBACK_INVALID_COMMAND_TYPE = "Invalid command type entered!";
	private static final String FEEDBACK_TASK_ADDED = "Task Added Successfully";
	private static final String FEEDBACK_CLEAR_ALL_TASKS = "All tasks deleted!";
	private static final String FEEDBACK_TASK_DELETED = "Task Deleted Successfully";
	private static final String FEEDBACK_TASK_NOT_FOUND = "Task not found!";
	private static final String FEEDBACK_NON_EXISTENT_TASK_NUM = "Task number entered was not found!";
	private static final String FEEDBACK_TASK_NUM_NOT_FOUND = "Task number is not present!";
	private static final String FEEDBACK_TASK_MODIFIED = "Task Edited Successfully";
	private static final String FEEDBACK_TASK_NOT_MODIFIED = "Task was not modified";
	private static final String FEEDBACK_NO_MODIFICATION_NUM_FOUND = "No modification number was found!";
	private static final String FEEDBACK_TASK_COMPLETED = "Task Completed Successfully";
	private static final String FEEDBACK_TASK_OVERDUE = "Task has exceeded deadline!";
	
	private void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	
	public String getFeedBack() {
		return feedBack;
	}
	
	private void setTaskList(ArrayList<Task> updatedTaskList) {
		this.updatedTaskList = updatedTaskList;
	}
	
	public ArrayList<Task> getTaskList() {
		return updatedTaskList;
	}
	
	/*this method gets back the parsed Command class from parser*/
	public void retrieveParsedCommand(String originalDescription){
		try {
			Command.COMMAND_TYPE typeCommand = null;

			Command existingCommand = CommandParser.getParsedCommand(originalDescription);
			typeCommand = getCommand(existingCommand);
			Task getTaskToExecute = getTaskDescription(existingCommand);
			execute(typeCommand, existingCommand, getTaskToExecute);
		} catch (Exception e) {
			setFeedBack(FEEDBACK_INVALID_COMMAND);
		}
	}
	
	/*this method gets the specific command type to be executed such as add, delete, modify etc*/
	private Command.COMMAND_TYPE getCommand(Command existingCommand) {
		Command.COMMAND_TYPE executeCommand = null;
		try {
			executeCommand = existingCommand.getCommandType();
			return executeCommand;
		} catch (Exception e) {
			setFeedBack(FEEDBACK_INVALID_COMMAND_TYPE);
			return null;
		}
	}
	
	/*this method retrieves all relevant task details from task class*/
	private Task getTaskDescription(Command existingCommand) {
		Task executeTask = existingCommand.getTaskDetails();
		return executeTask;
	}
	
	/*this method calls the respective execution methods for the respective command types*/
	private void execute(Command.COMMAND_TYPE executeCommand,Command retrievedCommand, Task executeTask) {
		switch (executeCommand) {
		case ADD_TASK:
			addTask(executeTask);
			break;
		case DELETE_TASK:
			deleteTask(executeTask, retrievedCommand);
			break;
		case MODIFY_TASK:
			modifyTask(executeTask, retrievedCommand);
			break;
		case COMPLETE_TASK:
			completeTask(executeTask);
			break;
		case UNDO_TASK:
			undoTask(executeTask);
			break;
		case REDO_TASK:
			redoTask(executeTask);
			break;
		case EXIT:
			exit();	
			break;
		}
	}
	
	/*adds task based on the startdate, enddate, starttime and endtime for scheduled tasks into either
	 scheduledtodo or scheduledoverdue arraylist while floating tasks are auto added into floatingtodo
	 arraylist*/
	private void addTask(Task executeTask) {
		LocalDate dateToday = LocalDate.now();
		LocalTime timeToday = LocalTime.now();
		
		if (executeTask.isScheduledTask()){
			if (((executeTask.getEndDate() != null) && (executeTask.getStartDate() != null)) ||
			((executeTask.getEndDate() != null) && (executeTask.getStartDate() == null))) {
				if (((executeTask.getEndDate()).compareTo(dateToday))>0) {
						scheduledTasksToDo.add(executeTask);
						setFeedBack(FEEDBACK_TASK_ADDED);
						setTaskList(scheduledTasksToDo);
						
				}
				else if (((executeTask.getEndDate()).compareTo(dateToday))<0) {
					scheduledTasksOverDue.add(executeTask);
					setFeedBack(FEEDBACK_TASK_OVERDUE);
					setTaskList(scheduledTasksOverDue);
				}
				
				//assuming that endDate is today
				else if ((executeTask.getEndDate().compareTo(dateToday)) == 0) {
					if (((executeTask.getEndTime() != null) && (executeTask.getStartTime() != null)) || 
					   ((executeTask.getEndTime() != null) && (executeTask.getStartTime() == null))) {
						if ((executeTask.getEndTime().compareTo(timeToday))>0){
							scheduledTasksToDo.add(executeTask);
							setFeedBack(FEEDBACK_TASK_ADDED);
							setTaskList(scheduledTasksToDo);
						}
						else {
							scheduledTasksOverDue.add(executeTask);
							setFeedBack(FEEDBACK_TASK_OVERDUE);
							setTaskList(scheduledTasksOverDue);
						}
					}
					else if ((executeTask.getEndTime() == null) && (executeTask.getStartTime() == null)) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:MM");
						LocalTime defaultTime = (LocalTime) formatter.parse("23:59");
						
						if (defaultTime.compareTo(timeToday)>0) {
							scheduledTasksToDo.add(executeTask);
							setFeedBack(FEEDBACK_TASK_ADDED);
							setTaskList(scheduledTasksToDo);
						}
						else {
							scheduledTasksOverDue.add(executeTask);
							setFeedBack(FEEDBACK_TASK_ADDED);
							setTaskList(scheduledTasksOverDue);
						}
					}
				}
			}
		}
			
		else if (executeTask.isFloatingTask()) {
			floatingTasksToDo.add(executeTask);
			setFeedBack(FEEDBACK_TASK_ADDED);
			setTaskList(floatingTasksToDo);
		}
	}
	
	/*deleteTask can be done either by typing only 2 characters, indicating the task number and task
	 type, or it can be done by typing the task descript*/
	private void deleteTask(Task executeTask, Command retrievedCommand) {
			int taskDigit = retrievedCommand.getIndexNumber();
			
			if ((taskDigit != -1) || (taskDigit != 0)) {
				if (executeTask.isScheduledTask()) {
					if (taskDigit <= scheduledTasksToDo.size()) {
						scheduledTasksToDo.remove(taskDigit-1);
						setFeedBack(FEEDBACK_TASK_DELETED);
						setTaskList(scheduledTasksToDo);
					}
					else {
						setFeedBack(FEEDBACK_TASK_NOT_FOUND);
					}
				}
				else if (executeTask.isFloatingTask()) {
					if ((taskDigit > scheduledTasksToDo.size()) && (taskDigit <= scheduledTasksToDo.size() + 
							floatingTasksToDo.size())) {
							floatingTasksToDo.remove(taskDigit - 1 - scheduledTasksToDo.size());
							setFeedBack(FEEDBACK_TASK_DELETED);
							setTaskList(floatingTasksToDo);
					}
					else {
						setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
					}
				}
			}
		
		else {
			if (executeTask.getDescription().equalsIgnoreCase("all")) {
				scheduledTasksToDo.clear();//not too sure about this aspect as we
				scheduledTasksOverDue.clear();//are maintaining 5 different sets of ArrayLists
				scheduledTasksComplete.clear();//perhaps the command can be modified to
				floatingTasksToDo.clear();//"clear all upcoming s/completed s/overdue s/incomplete f/complete f" 
				floatingTasksComplete.clear();//so we will know which ArrayList to clear
				setFeedBack(FEEDBACK_CLEAR_ALL_TASKS);
				setTaskList(scheduledTasksToDo);
				setTaskList(scheduledTasksOverDue);
				setTaskList(scheduledTasksComplete);
				setTaskList(floatingTasksToDo);
				setTaskList(floatingTasksComplete);
			}
			else {
				setFeedBack(FEEDBACK_TASK_NUM_NOT_FOUND);
			}
		}
	}
	
	/*modifies task by editing description, date or time*/
	private void modifyTask(Task executeTask, Command retrievedCommand) {
		int taskDigitToModify = retrievedCommand.getIndexNumber();
		
		if ((taskDigitToModify != -1) || (taskDigitToModify != 0)) {
			if (taskDigitToModify <= scheduledTasksToDo.size()) {
				modifyScheduledTasksToDo(scheduledTasksToDo, executeTask, taskDigitToModify);
			}
			else if ((taskDigitToModify > scheduledTasksToDo.size()) && (taskDigitToModify <= scheduledTasksToDo.size() + 
					floatingTasksToDo.size())) {
				modifyFloatingTasksToDo(floatingTasksToDo, executeTask, taskDigitToModify);
			}
		}
		else {
			setFeedBack(FEEDBACK_NO_MODIFICATION_NUM_FOUND);
		}
	}
	
	private void modifyScheduledTasksToDo(ArrayList<Task> ScheduledTasksToDo, Task executeTask, int taskNum) {
		if (executeTask.getDescription() != null) {
			ScheduledTasksToDo.get(taskNum-1).setDescription(executeTask.getDescription());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setTaskList(scheduledTasksToDo);
		}
		else if (executeTask.getEndDate() != null) {
			ScheduledTasksToDo.get(taskNum-1).setEndDate(executeTask.getEndDate());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setTaskList(scheduledTasksToDo);
		}
		else if (executeTask.getEndTime() != null) {
			ScheduledTasksToDo.get(taskNum-1).setEndTime(executeTask.getEndTime());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setTaskList(scheduledTasksToDo);
		}
		else if ((executeTask.getDescription() == null) && (executeTask.getEndDate() == null) && (executeTask.getEndTime() == null)) {
			setFeedBack(FEEDBACK_TASK_NOT_MODIFIED);
		}
		
	}
	
	private void modifyFloatingTasksToDo(ArrayList<Task> FloatingTasksToDo, Task executeTask, int taskNum) {
		if (executeTask.getDescription() != null) {
			FloatingTasksToDo.get(taskNum-1-scheduledTasksToDo.size()).setDescription(executeTask.getDescription());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setTaskList(floatingTasksToDo);
		}
		else if (executeTask.getEndDate() != null) {
			Task taskToModify = FloatingTasksToDo.get(taskNum-1-scheduledTasksToDo.size());
			
			taskToModify.setEndDate(executeTask.getEndDate());
			scheduledTasksToDo.add(taskToModify);
			floatingTasksToDo.remove(taskToModify);
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setTaskList(scheduledTasksToDo);
			setTaskList(floatingTasksToDo);
		}
		else if (executeTask.getEndTime() != null) {
			Task taskToModify = FloatingTasksToDo.get(taskNum-1-scheduledTasksToDo.size());
			
			taskToModify.setEndTime(executeTask.getEndTime());
			scheduledTasksToDo.add(taskToModify);
			floatingTasksToDo.remove(taskToModify);
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setTaskList(scheduledTasksToDo);
			setTaskList(floatingTasksToDo);
		}
		else if ((executeTask.getDescription() == null) && (executeTask.getEndDate() == null) && (executeTask.getEndTime() == null)) {
			setFeedBack(FEEDBACK_TASK_NOT_MODIFIED);
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
	
	private void undoTask(Task executeTask) {
		
	}
	
	private void redoTask(Task executeTask) {
		
	}
	
	/*exiting of program. Shd save all arraylists and updated info abt the tasks before exiting*/
	private void exit() {
		System.exit(0);//how to save everything and exit?
	}
}
