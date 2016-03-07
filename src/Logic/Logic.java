package Logic;

import java.util.ArrayList;
import java.time.LocalDateTime;

import ScheduleHacks.Task;
import Parser.CommandParser;
import Parser.Command;
import GUI.TempCLI;

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

	/****************** SETTER METHODS ***********************/
	private void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
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

	/****************** GETTER METHODS ***********************/
	public String getFeedBack() {
		return feedBack;
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

	/****************** OTHER METHODS ***********************/
	/*
	 * this method is called in CLI by logic obj, hence transmitting string
	 * userInput from UI to Logic
	 */
	/*
	 * calls retrieveParsedCommand, from which private methods are called in
	 * Logic class
	 */
	public void startExecution(String userInput) {
		retrieveParsedCommand(userInput);
	}

	/*
	 * this method gets back the parsed Command class from parser. Proceeds to
	 * execute function aft obtaining COMMAND_TYPE and Task classes
	 */
	private void retrieveParsedCommand(String originalDescription) {
		try {
			Command.COMMAND_TYPE typeCommand = null;

			Command existingCommand = CommandParser.getParsedCommand(originalDescription);
			typeCommand = getCommand(existingCommand);
			Task getTaskToExecute = getTaskDescription(existingCommand);
			execute(typeCommand, existingCommand, getTaskToExecute);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			setFeedBack(FEEDBACK_INVALID_COMMAND);
		}
	}

	/*
	 * this method gets the specific command type to be executed such as add,
	 * delete, modify etc
	 */
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

	/* this method retrieves all relevant task details from task class */
	private Task getTaskDescription(Command existingCommand) {
		Task executeTask = existingCommand.getTaskDetails();
		return executeTask;
	}

	/*
	 * this method calls the respective execution methods for the respective
	 * command types
	 */
	private void execute(Command.COMMAND_TYPE executeCommand, Command retrievedCommand, Task executeTask) {
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

	/*
	 * adds task based on the startdate, enddate, starttime and endtime for
	 * scheduled tasks into either scheduledtodo or scheduledoverdue arraylist
	 * while floating tasks are auto added into floatingtodo arraylist
	 */
	private void addTask(Task executeTask) {
		LocalDateTime nowDateTime = LocalDateTime.now();

		if (executeTask.isScheduledTask()) {
			LocalDateTime taskDateTime = LocalDateTime.of(executeTask.getEndDate(), executeTask.getEndTime());
			if ((taskDateTime.compareTo(nowDateTime)) >= 0) {
				addTaskInOrder(scheduledTasksToDo, executeTask);
				setFeedBack(FEEDBACK_TASK_ADDED);
				/* setScheduledTasksToDo(scheduledTasksToDo); */
			} else {
				addTaskInOrder(scheduledTasksOverDue, executeTask);
				setFeedBack(FEEDBACK_TASK_OVERDUE);
				/* setScheduledTasksOverDue(scheduledTasksOverDue); */
			}
		} else if (executeTask.isFloatingTask()) {
			floatingTasksToDo.add(executeTask);
			setFeedBack(FEEDBACK_TASK_ADDED);
			/* setFloatingTasksToDo(floatingTasksToDo); */
		}
	}

	private void addTaskInOrder(ArrayList<Task> taskListToSort, Task executeTask) {
		int position = sortTaskList(taskListToSort, executeTask);
		taskListToSort.add(position, executeTask);
	}

	private int sortTaskList(ArrayList<Task> taskList, Task task) {
		LocalDateTime taskStartDateTime = null;
		LocalDateTime taskEndDateTime = LocalDateTime.of(task.getEndDate(), task.getEndTime());
		if (task.getStartDate() != null) {
			taskStartDateTime = LocalDateTime.of(task.getStartDate(), task.getStartTime());
		}
		int taskPosition = taskList.size();

		for (int i = 0; i < taskList.size(); i++) {
			LocalDateTime selectedTaskStartDateTime = null;
			if (taskList.get(i).getStartDate() != null) {
				selectedTaskStartDateTime = LocalDateTime.of(taskList.get(i).getStartDate(),
						taskList.get(i).getStartTime());
			}
			LocalDateTime selectedTaskEndDateTime = LocalDateTime.of(taskList.get(i).getEndDate(),
					taskList.get(i).getEndTime());

			if ((taskEndDateTime.compareTo(selectedTaskEndDateTime)) < 0) {
				return i;
			} else if ((taskEndDateTime.compareTo(selectedTaskEndDateTime)) == 0) {
				if (selectedTaskStartDateTime != null && taskStartDateTime != null) {
					if ((taskStartDateTime.compareTo(selectedTaskStartDateTime)) < 0) {
						return i;
					}
				}
			}
		}
		return taskPosition;
	}

	/*
	 * deleteTask from scheduledTasksToDo or floatingTasksToDo based on task
	 * number
	 */
	private void deleteTask(Task executeTask, Command retrievedCommand) {
		int taskDigit = retrievedCommand.getIndexNumber();

		if (taskDigit > 0) {
			if (taskDigit <= scheduledTasksOverDue.size()) {
				scheduledTasksOverDue.remove(taskDigit - 1);
				setFeedBack(FEEDBACK_TASK_DELETED);
				/* setScheduledTasksOverDue(scheduledTasksOverDue); */
			} else if (taskDigit <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()) {
				scheduledTasksToDo.remove(taskDigit - 1 - scheduledTasksOverDue.size());
				setFeedBack(FEEDBACK_TASK_DELETED);
				/* setScheduledTasksToDo(scheduledTasksToDo); */
			} else if (taskDigit <= scheduledTasksToDo.size() + floatingTasksToDo.size()
					+ scheduledTasksOverDue.size()) {
				floatingTasksToDo.remove(taskDigit - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
				setFeedBack(FEEDBACK_TASK_DELETED);
				/* setFloatingTasksToDo(floatingTasksToDo); */
			} else {
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
			}
		} else {
			setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM);
		}
	}

	/*
	 * modifies scheduledTasksToDo/floatingTasksToDo by looking at class number.
	 * Edits corresponding task based on description, date or time
	 */
	private void modifyTask(Task executeTask, Command retrievedCommand) {
		int taskDigitToModify = retrievedCommand.getIndexNumber() - 1;

		if (taskDigitToModify >= 0) {
			if (taskDigitToModify <= scheduledTasksOverDue.size()) {
				modifyScheduledTask(executeTask, taskDigitToModify, scheduledTasksOverDue);
			} else if (taskDigitToModify <= scheduledTasksToDo.size() + scheduledTasksOverDue.size()) {
				taskDigitToModify -= (scheduledTasksOverDue.size() + scheduledTasksToDo.size());
				modifyScheduledTask(executeTask, taskDigitToModify, scheduledTasksToDo);
			} else if (taskDigitToModify <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()
					+ floatingTasksToDo.size()) {
				taskDigitToModify -= (scheduledTasksOverDue.size() + scheduledTasksToDo.size()
						+ floatingTasksToDo.size());
				modifyFloatingTasksToDo(executeTask, taskDigitToModify);
			} else {
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
			}
		} else {
			setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM + " Else, " + FEEDBACK_TASK_NUM_NOT_FOUND);
		}
	}

	private void modifyScheduledTask(Task executeTask, int taskNum, ArrayList<Task> listToModify) {
		Task taskToEdit = listToModify.remove(taskNum);

		if (executeTask.getDescription() != null) {
			taskToEdit.setDescription(executeTask.getDescription());
			/* setScheduledTasksToDo(scheduledTasksToDo); */
		}
		if (executeTask.getEndDate() != null) {
			taskToEdit.setEndDate(executeTask.getEndDate());
			/* setScheduledTasksToDo(scheduledTasksToDo); */
		}
		if (executeTask.getEndTime() != null) {
			taskToEdit.setEndTime(executeTask.getEndTime());
			/* setScheduledTasksToDo(scheduledTasksToDo); */
		}
		if (executeTask.getStartDate() != null) {
			taskToEdit.setStartDate(executeTask.getStartDate());
			/* setScheduledTasksToDo(scheduledTasksToDo); */
		}
		if (executeTask.getStartTime() != null) {
			taskToEdit.setStartTime(executeTask.getStartTime());
			/* setScheduledTasksToDo(scheduledTasksToDo); */
		}
		addTaskInOrder(listToModify, taskToEdit);
		setFeedBack(FEEDBACK_TASK_MODIFIED);
		if ((executeTask.getDescription() == null) && (executeTask.getEndDate() == null)
				&& (executeTask.getEndTime() == null) && (executeTask.getStartDate() == null)
				&& (executeTask.getStartTime() == null)) {
			setFeedBack(FEEDBACK_TASK_NOT_MODIFIED);
		}

	}

	private void modifyFloatingTasksToDo(Task executeTask, int taskNum) {
		Task taskToEdit = floatingTasksToDo.get(taskNum);

		if (executeTask.getDescription() != null) {
			taskToEdit.setDescription(executeTask.getDescription());
		}
		if (executeTask.getEndDate() != null) {
			taskToEdit.setEndDate(executeTask.getEndDate());
		}
		if (executeTask.getEndTime() != null) {
			taskToEdit.setEndTime(executeTask.getEndTime());
		}
		if (executeTask.getStartDate() != null) {
			taskToEdit.setStartDate(executeTask.getStartDate());
		}
		if (executeTask.getStartTime() != null) {
			taskToEdit.setStartTime(executeTask.getStartTime());
		}

		if ((executeTask.getDescription() == null) && (executeTask.getEndDate() == null)
				&& (executeTask.getEndTime() == null) && (executeTask.getStartDate() == null)
				&& (executeTask.getStartTime() == null)) {
			setFeedBack(FEEDBACK_TASK_NOT_MODIFIED);
		}

		else if ((taskToEdit.getDescription().equals(executeTask.getDescription()))
				&& ((taskToEdit.getEndDate()) == null) && ((taskToEdit.getEndTime()) == null)
				&& ((taskToEdit.getStartDate()) == null) && ((taskToEdit.getStartTime()) == null)) {
			setFeedBack(FEEDBACK_TASK_MODIFIED);
			/* setFloatingTasksToDo(floatingTasksToDo) */
		} else {
			taskToEdit = CommandParser.convertFloatingToScheduled(taskToEdit);
			changeFloatingToScheduledProcedures(taskToEdit);
		}
	}

	private void changeFloatingToScheduledProcedures(Task taskToModify) {
		floatingTasksToDo.remove(taskToModify);
		addTaskInOrder(scheduledTasksToDo, taskToModify);
		setFeedBack(FEEDBACK_TASK_MODIFIED);
		/*
		 * setScheduledTasksToDo(scheduledTasksToDo);
		 * setFloatingTasksToDo(floatingTasksToDo);
		 */
	}

	/*
	 * adds task into the respective completeArrayList and removes that same
	 * task from the ArrayList that it is currently residing in based on task
	 * number entered by user
	 */
	private void completeTask(Command retrievedCommand) {
		int taskToBeCompleted = retrievedCommand.getIndexNumber();

		if (taskToBeCompleted > 0) {
			if (taskToBeCompleted <= scheduledTasksToDo.size()) {
				if (scheduledTasksToDo.get(taskToBeCompleted - 1).isComplete()) {
					setFeedBack(FEEDBACK_TASK_ALREADY_COMPLETED);
				} else {
					scheduledTasksComplete.add(scheduledTasksToDo.get(taskToBeCompleted - 1));
					scheduledTasksToDo.get(taskToBeCompleted - 1).setAsComplete();
					scheduledTasksToDo.remove(taskToBeCompleted - 1);
					setFeedBack(FEEDBACK_TASK_COMPLETED);
					/*
					 * setScheduledTasksToDo(scheduledTasksToDo);
					 * setScheduledTasksComplete(scheduledTasksComplete);
					 */
				}
			} else if ((taskToBeCompleted > scheduledTasksToDo.size())
					&& (taskToBeCompleted <= scheduledTasksToDo.size() + floatingTasksToDo.size())) {
				if (floatingTasksToDo.get(taskToBeCompleted - 1 - scheduledTasksToDo.size()).isComplete()) {
					setFeedBack(FEEDBACK_TASK_ALREADY_COMPLETED);
				} else {
					floatingTasksComplete.add(floatingTasksToDo.get(taskToBeCompleted - 1 - scheduledTasksToDo.size()));
					floatingTasksToDo.get(taskToBeCompleted - 1 - scheduledTasksToDo.size()).setAsComplete();
					floatingTasksToDo.remove(taskToBeCompleted - 1 - scheduledTasksToDo.size());
					setFeedBack(FEEDBACK_TASK_COMPLETED);
					/*
					 * setFloatingTasksToDo(floatingTasksToDo);
					 * setFloatingTasksComplete(floatingTasksComplete);
					 */
				}
			} else {
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
			}
		} else {
			setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM + " Else, " + FEEDBACK_TASK_NUM_NOT_FOUND);
		}
	}

	/*
	 * this method shd be called in main at the start of the prog and shd run
	 * all the way till system exits. This method automatically shifts
	 * scheduledtaskstodo to scheduledtasksoverdue when date and time has
	 * exceeded due date and due time specified for scheduled task
	 */
	public void autoChangeTaskStatus() {
		LocalDateTime presentDateTime = LocalDateTime.now();

		for (int i = 0; i < scheduledTasksToDo.size(); i++) {
			LocalDateTime scheduledEndDateTime = LocalDateTime.of(scheduledTasksToDo.get(i).getEndDate(),
					scheduledTasksToDo.get(i).getEndTime());

			if ((scheduledEndDateTime.compareTo(presentDateTime)) < 0) {
				changeStatusToOverdue(i);
			}
		}
	}

	private void changeStatusToOverdue(int i) {
		addTaskInOrder(scheduledTasksOverDue, scheduledTasksToDo.get(i));
		setFeedBack("Task " + scheduledTasksToDo.get(i).getDescription() + " has exceeded deadline");
		scheduledTasksToDo.remove(i);
		/*
		 * setScheduledTasksToDo(scheduledTasksToDo);
		 * setScheduledTasksOverDue(scheduledTasksOverDue);
		 */
	}

	private void undoTask(Task executeTask) {

	}

	private void redoTask(Task executeTask) {

	}

	/*
	 * exiting of program. Shd save all arraylists and updated info abt the
	 * tasks before exiting
	 */
	private void exit() {
		// Temporary CLI Exit. Need to change later.
		TempCLI.exitScheduleHacks();
		// System.exit(0);//how to save everything and exit?
	}
}
