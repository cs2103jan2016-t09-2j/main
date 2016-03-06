package Logic;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import ScheduleHacks.Task;
import Parser.CommandParser;
import Parser.Command;

public class Logic {
	
	private String feedBack;
	
	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	
	private static final String FEEDBACK_INVALID_COMMAND = "Invalid Command!";
	private static final String FEEDBACK_INVALID_COMMAND_TYPE = "Invalid command type entered!";
	private static final String FEEDBACK_TASK_ADDED = "Task Added Successfully";
	private static final String FEEDBACK_CLEAR_ALL_TASKS = "All tasks deleted!";
	private static final String FEEDBACK_TASK_DELETED = "Task Deleted Successfully";
	private static final String FEEDBACK_NON_EXISTENT_TASK_NUM = "Task number entered was not found!";
	private static final String FEEDBACK_NEGATIVE_TASK_NUM = "Task number entered cannot be 0 or negative!";
	private static final String FEEDBACK_TASK_NUM_NOT_FOUND = "Task number is not present! Task not found!";
	private static final String FEEDBACK_TASK_MODIFIED = "Task Edited Successfully";
	private static final String FEEDBACK_TASK_NOT_MODIFIED = "Task was not modified";
	private static final String FEEDBACK_TASK_COMPLETED = "Task Completed Successfully";
	private static final String FEEDBACK_TASK_ALREADY_COMPLETED = "Task entered by user has already been completed!";
	private static final String FEEDBACK_TASK_OVERDUE = "Task has exceeded deadline!";
	
	private void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}
	
	public String getFeedBack() {
		return feedBack;
	}
	
	private void setScheduledTasksToDo(ArrayList<Task> currentTaskList) { 
		scheduledTasksToDo.clear();
		scheduledTasksToDo = currentTaskList;
	}
	
	private void setScheduledTasksOverDue(ArrayList<Task> currentTaskList) {
		scheduledTasksOverDue.clear();
		scheduledTasksOverDue = currentTaskList;
	}
	
	private void setScheduledTasksComplete(ArrayList<Task> currentTaskList) {
		scheduledTasksComplete.clear();
		scheduledTasksComplete = currentTaskList;
	}
	
	private void setFloatingTasksToDo(ArrayList<Task> currentTaskList) {
		floatingTasksToDo.clear();
		floatingTasksToDo = currentTaskList;
	}
	
	private void setFloatingTasksComplete(ArrayList<Task> currentTaskList) {
		floatingTasksComplete.clear();
		floatingTasksComplete = currentTaskList;
	}
	
	public ArrayList<Task> getScheduledTasksToDo() {
		return scheduledTasksToDo;
	}
	
	public ArrayList<Task> getScheduledTasksOverDue() {
		return scheduledTasksOverDue;
	}
	
	public ArrayList<Task> getScheduledTasksComplete() {
		return scheduledTasksComplete;
	}
	
	public ArrayList<Task> getFloatingTasksToDo() {
		return floatingTasksToDo;
	}
	
	public ArrayList<Task> getFloatingTasksComplete() {
		return floatingTasksComplete;
	}
	
	/*this method is called in CLI by logic obj, hence transmitting string userInput from UI to Logic*/
	/*calls retrieveParsedCommand, from which private methods are called in Logic class*/
	public void startExecution(String userInput) {
		retrieveParsedCommand(userInput);
	}
	
	/*this method gets back the parsed Command class from parser. Proceeds to execute function aft
	 obtaining COMMAND_TYPE and Task classes*/
	private void retrieveParsedCommand(String originalDescription){
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
			completeTask(retrievedCommand);
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
						setScheduledTasksToDo(scheduledTasksToDo);
						
				}
				else if (((executeTask.getEndDate()).compareTo(dateToday))<0) {
					scheduledTasksOverDue.add(executeTask);
					setFeedBack(FEEDBACK_TASK_OVERDUE);
					setScheduledTasksOverDue(scheduledTasksOverDue);
				}
				
				//assuming that endDate is today
				else if ((executeTask.getEndDate().compareTo(dateToday)) == 0) {
					if (((executeTask.getEndTime() != null) && (executeTask.getStartTime() != null)) || 
					   ((executeTask.getEndTime() != null) && (executeTask.getStartTime() == null))) {
						if ((executeTask.getEndTime().compareTo(timeToday))>0){
							scheduledTasksToDo.add(executeTask);
							setFeedBack(FEEDBACK_TASK_ADDED);
							setScheduledTasksToDo(scheduledTasksToDo);
						}
						else {
							scheduledTasksOverDue.add(executeTask);
							setFeedBack(FEEDBACK_TASK_OVERDUE);
							setScheduledTasksOverDue(scheduledTasksOverDue);
						}
					}
					else if ((executeTask.getEndTime() == null) && (executeTask.getStartTime() == null)) {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:MM");
						LocalTime defaultTime = (LocalTime) formatter.parse("23:59");
						
						if (defaultTime.compareTo(timeToday)>0) {
							scheduledTasksToDo.add(executeTask);
							setFeedBack(FEEDBACK_TASK_ADDED);
							setScheduledTasksToDo(scheduledTasksToDo);
						}
						else {
							scheduledTasksOverDue.add(executeTask);
							setFeedBack(FEEDBACK_TASK_ADDED);
							setScheduledTasksOverDue(scheduledTasksOverDue);
						}
					}
				}
			}
		}
			
		else if (executeTask.isFloatingTask()) {
			floatingTasksToDo.add(executeTask);
			setFeedBack(FEEDBACK_TASK_ADDED);
			setFloatingTasksToDo(floatingTasksToDo);
		}
	}
	
	/*deleteTask from scheduledTasksToDo or floatingTasksToDo based on task number*/
	private void deleteTask(Task executeTask, Command retrievedCommand) {
			int taskDigit = retrievedCommand.getIndexNumber();
			
			if (taskDigit > 0) {
				if (taskDigit <= scheduledTasksToDo.size()) {
					scheduledTasksToDo.remove(taskDigit-1);
					setFeedBack(FEEDBACK_TASK_DELETED);
					setScheduledTasksToDo(scheduledTasksToDo);
				}
				else if ((taskDigit > scheduledTasksToDo.size()) && (taskDigit <= 
						scheduledTasksToDo.size() + floatingTasksToDo.size())) {
						floatingTasksToDo.remove(taskDigit - 1 - scheduledTasksToDo.size());
						setFeedBack(FEEDBACK_TASK_DELETED);
						setFloatingTasksToDo(floatingTasksToDo);
				}
				else {
						setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
				}
			}
			else {
				setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM);
			}
			
			if (executeTask.getDescription().equalsIgnoreCase("all")) {
				scheduledTasksToDo.clear();//not too sure about this aspect as we
				scheduledTasksOverDue.clear();//are maintaining 5 different sets of ArrayLists
				scheduledTasksComplete.clear();//perhaps the command can be modified to
				floatingTasksToDo.clear();//"clear all upcoming s/completed s/overdue s/incomplete f/complete f" 
				floatingTasksComplete.clear();//so we will know which ArrayList to clear
				setFeedBack(FEEDBACK_CLEAR_ALL_TASKS);
				setScheduledTasksToDo(scheduledTasksToDo);
				setScheduledTasksOverDue(scheduledTasksOverDue);
				setScheduledTasksComplete(scheduledTasksComplete);
				setFloatingTasksToDo(floatingTasksToDo);
				setFloatingTasksComplete(floatingTasksComplete);
			}
			else if (!(executeTask.getDescription().equalsIgnoreCase("all")) && 
					!(executeTask.getDescription().equals(null))) {
				setFeedBack(FEEDBACK_TASK_NUM_NOT_FOUND);
			}
	}
	
	/*modifies scheduledTasksToDo/floatingTasksToDo by looking at class number. Edits corresponding task
	 based on description, date or time*/
	private void modifyTask(Task executeTask, Command retrievedCommand) {
		int taskDigitToModify = retrievedCommand.getIndexNumber();
		
		if (taskDigitToModify > 0) {
			if (taskDigitToModify <= scheduledTasksToDo.size()) {
				modifyScheduledTasksToDo(scheduledTasksToDo, executeTask, taskDigitToModify);
			}
			else if ((taskDigitToModify > scheduledTasksToDo.size()) && (taskDigitToModify <= scheduledTasksToDo.size() + 
					floatingTasksToDo.size())) {
				modifyFloatingTasksToDo(floatingTasksToDo, executeTask, taskDigitToModify);
			}
			else {
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
			}
		}
		else if (taskDigitToModify <= 0) {
			setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM + " Else, " + FEEDBACK_TASK_NUM_NOT_FOUND);
		}
	}
	
	private void modifyScheduledTasksToDo(ArrayList<Task> ScheduledTasksToDo, Task executeTask, int taskNum) {
		if (executeTask.getDescription() != null) {
			ScheduledTasksToDo.get(taskNum-1).setDescription(executeTask.getDescription());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setScheduledTasksToDo(scheduledTasksToDo);
		}
		else if (executeTask.getEndDate() != null) {
			ScheduledTasksToDo.get(taskNum-1).setEndDate(executeTask.getEndDate());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setScheduledTasksToDo(scheduledTasksToDo);
		}
		else if (executeTask.getEndTime() != null) {
			ScheduledTasksToDo.get(taskNum-1).setEndTime(executeTask.getEndTime());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setScheduledTasksToDo(scheduledTasksToDo);
		}
		else if ((executeTask.getDescription() == null) && (executeTask.getEndDate() == null) && (executeTask.getEndTime() == null)) {
			setFeedBack(FEEDBACK_TASK_NOT_MODIFIED);
		}
		
	}
	
	private void modifyFloatingTasksToDo(ArrayList<Task> FloatingTasksToDo, Task executeTask, int taskNum) {
		if (executeTask.getDescription() != null) {
			FloatingTasksToDo.get(taskNum-1-scheduledTasksToDo.size()).setDescription(executeTask.getDescription());
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setFloatingTasksToDo(floatingTasksToDo);
		}
		else if (executeTask.getEndDate() != null) {
			Task taskToModify = FloatingTasksToDo.get(taskNum-1-scheduledTasksToDo.size());
			
			taskToModify.setEndDate(executeTask.getEndDate());
			scheduledTasksToDo.add(taskToModify);
			floatingTasksToDo.remove(taskToModify);
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setScheduledTasksToDo(scheduledTasksToDo);
			setFloatingTasksToDo(floatingTasksToDo);
		}
		else if (executeTask.getEndTime() != null) {
			Task taskToModify = FloatingTasksToDo.get(taskNum-1-scheduledTasksToDo.size());
			
			taskToModify.setEndTime(executeTask.getEndTime());
			scheduledTasksToDo.add(taskToModify);
			floatingTasksToDo.remove(taskToModify);
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			setScheduledTasksToDo(scheduledTasksToDo);
			setFloatingTasksToDo(floatingTasksToDo);
		}
		else if ((executeTask.getDescription() == null) && (executeTask.getEndDate() == null) && (executeTask.getEndTime() == null)) {
			setFeedBack(FEEDBACK_TASK_NOT_MODIFIED);
		}
	}
	
	/*adds task into the respective completeArrayList and removes that same task from the ArrayList that 
	 it is currently residing in based on task number entered by user*/
	private void completeTask(Command retrievedCommand) {
		int taskToBeCompleted = retrievedCommand.getIndexNumber();
		
		if (taskToBeCompleted > 0) {
			if (taskToBeCompleted <= scheduledTasksToDo.size()) {
				if (scheduledTasksToDo.get(taskToBeCompleted-1).isComplete()) {
					setFeedBack(FEEDBACK_TASK_ALREADY_COMPLETED);
				}
				else {
					scheduledTasksComplete.add(scheduledTasksToDo.get(taskToBeCompleted-1));
					scheduledTasksToDo.get(taskToBeCompleted-1).setAsComplete();
					scheduledTasksToDo.remove(taskToBeCompleted-1);
					setFeedBack(FEEDBACK_TASK_COMPLETED);
					setScheduledTasksToDo(scheduledTasksToDo);
					setScheduledTasksComplete(scheduledTasksComplete);
				}
			}
			else if ((taskToBeCompleted > scheduledTasksToDo.size()) && (taskToBeCompleted
					<= scheduledTasksToDo.size() + floatingTasksToDo.size())) {
				if (floatingTasksToDo.get(taskToBeCompleted-1-scheduledTasksToDo.size()).isComplete()) {
					setFeedBack(FEEDBACK_TASK_ALREADY_COMPLETED);
				}
				else {
					floatingTasksComplete.add(floatingTasksToDo.get(taskToBeCompleted-1-scheduledTasksToDo.size()));
					floatingTasksToDo.get(taskToBeCompleted-1-scheduledTasksToDo.size()).setAsComplete();
					floatingTasksToDo.remove(taskToBeCompleted-1-scheduledTasksToDo.size());
					setFeedBack(FEEDBACK_TASK_COMPLETED);
					setFloatingTasksToDo(floatingTasksToDo);
					setFloatingTasksComplete(floatingTasksComplete);
				}
			}
			else {
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
			}
		}
		else if (taskToBeCompleted <= 0) {
			setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM + " Else, " + FEEDBACK_TASK_NUM_NOT_FOUND);
		}
	}
	
	/*this method shd be called in main at the start of the prog and shd run all the way till system
	 exits. This method automatically shifts scheduledtaskstodo to scheduledtasksoverdue when date and
	 time has exceeded due date and due time specified for scheduled task*/
	public void autoChangeTaskStatus() {
		LocalDate dateToday = LocalDate.now();
		LocalTime timeToday = LocalTime.now();
		
		for (int i=0; i<scheduledTasksToDo.size(); i++) {
			if ((scheduledTasksToDo.get(i).getEndDate().compareTo(dateToday))<0) {
				changeStatusToOverdue(i);	
			}
			else if ((scheduledTasksToDo.get(i).getEndDate().compareTo(dateToday)) == 0) {
				if (scheduledTasksToDo.get(i).getEndTime() != null) {
					if ((scheduledTasksToDo.get(i).getEndTime().compareTo(timeToday)) <= 0) {
						changeStatusToOverdue(i);
					}
				}
				else if (scheduledTasksToDo.get(i).getEndTime() == null) {
					DateTimeFormatter format = DateTimeFormatter.ofPattern("HH:MM");
					LocalTime defaultEndTime = (LocalTime) format.parse("23:59");
					
					if ((defaultEndTime.compareTo(timeToday)) <= 0) {
						changeStatusToOverdue(i);
					}
				}
			}
		}
	}
	
	private void changeStatusToOverdue(int i) {
		scheduledTasksOverDue.add(scheduledTasksToDo.get(i));
		setFeedBack("Task " + scheduledTasksToDo.get(i).getDescription() + 
				" has exceeded deadline");
		scheduledTasksToDo.remove(i);
		setScheduledTasksToDo(scheduledTasksToDo);
		setScheduledTasksOverDue(scheduledTasksOverDue);
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
