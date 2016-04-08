//@@author A0116470M
package Logic;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ScheduleHacks.Task;
import ScheduleHacks.OldCommand;
import ScheduleHacks.OldCommand.COMMAND_TYPE;
import ScheduleHacks.History;
import Parser.CommandParser;
import Parser.Command;
import GUI.BottomBottom;
import GUI.TopLeftPanel;
import GUI.TopRightPanel;
import Storage.Storage;

public class Logic {

	private String feedBack;
	private boolean isSearchCommand;
	private boolean isHomeScreen = true;
	private boolean isHighlightOperation;

	private static Logic logicObject = null;

	private Storage storage = Storage.getInstance();
	private History historyObject = History.getInstance();

	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	private ArrayList<Task> trackConflictingTasks = new ArrayList<Task>();
	private Integer recentIndex = -1, recentLocation = -1;
	private Task recentTask = new Task();

	private static final String FEEDBACK_YOUR_COMMAND = "\n\nYour Command: ";
	private static final String FEEDBACK_INVALID_COMMAND = "Invalid Command!";
	private static final String FEEDBACK_INVALID_COMMAND_TYPE = "Invalid command type entered!";
	private static final String FEEDBACK_TASK_ADDED = "Task added successfully";
	private static final String FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_ADDING = "Task entered by user already exists! Task not added!";
	private static final String FEEDBACK_TASK_ADDED_BUT_ENCOUNTERED_CONFLICTS = "Task added successfully but task is conflicting with several existing tasks!";
	private static final String FEEDBACK_TASK_DELETED = "Task Deleted Successfully";
	private static final String FEEDBACK_NON_EXISTENT_TASK_NUM = "Task number was not found!";
	private static final String FEEDBACK_NEGATIVE_TASK_NUM = "Task number entered cannot be 0 or negative!";
	private static final String FEEDBACK_TASK_MODIFIED = "Task Edited Successfully";
	private static final String FEEDBACK_TASK_NOT_MODIFIED = "Task was not modified";
	private static final String FEEDBACK_TASK_MODIFIED_BUT_ENCOUNTERED_CONFLICTS = "Task edited successfully but task is conflicting with several existing tasks!";
	private static final String FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_EDITING = "Modified Task already exists! Task not edited!";
	private static final String FEEDBACK_TASK_COMPLETED = "Task Completed Successfully";
	private static final String FEEDBACK_TASK_INCOMPLETED = "Task Marked Undone Successfully";
	private static final String FEEDBACK_TASK_INCOMPLETED_INVALID = "Invalid Undone Operation!";
	private static final String FEEDBACK_EMPTY_TASK_DESCRIPTION = "Empty Task Description. Adding Unsucessful!";
	private static final String FEEDBACK_UNDO_INVALID = "Invalid Undo!";
	private static final String FEEDBACK_UNDO_VALID = "Last Action Un-Done!";
	private static final String FEEDBACK_REDO_VALID = "Last Action Re-Done!";
	private static final String FEEDBACK_REDO_INVALID = "Invalid Redo!";
	private static final String FEEDBACK_EMPTY_STRING = "";
	private static final String FEEDBACK_SEARCH_VALID = "Search Found";
	private static final String FEEDBACK_SEARCH_INVALID = "Search Not Found";
	private static final String FEEDBACK_HELP_CALLED = "Help sheet activated";

	/****************** CONSTRUCTOR ***********************/
	private Logic() {
	}

	// apply singleton
	public static Logic getInstance() {
		if (logicObject == null) {
			logicObject = new Logic();
		}
		return logicObject;
	}

	/****************** SETTER METHODS ***********************/
	private void setFeedBack(String feedBack) {
		this.feedBack = feedBack;
	}

	private void appendFeedBack(String textToAppend) {
		this.feedBack = this.feedBack.concat(textToAppend);
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

	private void setRecentIndexOfTask(Integer recentIndex) {
		this.recentIndex = recentIndex;
	}

	private void setRecentTaskDetails(Task recentTask) {
		this.recentTask = recentTask;
	}

	private void setRecentLocation(int location) {
		recentLocation = location;
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

	public Integer getRecentIndexOfTask() {
		return recentIndex;
	}

	public Task getRecentTaskDetails() {
		return recentTask;
	}

	public int getRecentLocation() {
		return recentLocation;
	}

	public boolean isHomeScreen() {
		return isHomeScreen;
	}

	public boolean isHighlightOperation() {
		return isHighlightOperation;
	}

	/****************** OTHER METHODS ***********************/
	/*
	 * this method is called in CLI by logic obj, hence transmitting string
	 * userInput from UI to Logic
	 */
	public void startExecution() {
		try {
			storage.loadToList();
			setFloatingTasksComplete(storage.getFloatingTasksComplete());
			setFloatingTasksToDo(storage.getFloatingTasksToDo());
			setScheduledTasksComplete(storage.getScheduledTasksComplete());
			setScheduledTasksToDo(storage.getScheduledTasksToDo());
			setScheduledTasksOverDue(storage.getScheduledTasksOverDue());
			isHomeScreen = true;
			for (int i = 0; i < scheduledTasksOverDue.size(); i++) {
				addTask(scheduledTasksOverDue.remove(i), true, 0);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void firstRun() {
		isHomeScreen = true;
		ArrayList<Task> firstList = new ArrayList<Task>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		firstList = setDueTodayTomorrowList(firstList, indexList);
		TopLeftPanel.firstSet(firstList, indexList);
		indexList = setFloatingIndexList();
		TopRightPanel.firstSet(floatingTasksToDo, indexList);
	}

	public ArrayList<Task> setDueTodayTomorrowList(ArrayList<Task> firstList, ArrayList<Integer> indexList) {
		if (scheduledTasksOverDue != null) {
			for (Task task : scheduledTasksOverDue) {
				if (task.getEndDate().isEqual(LocalDate.now())) {
					firstList.add(task);
					indexList.add(scheduledTasksOverDue.indexOf(task) + 1);
				}
			}
		}

		int count = scheduledTasksOverDue.size() + 1;

		if (scheduledTasksToDo != null) {
			LocalDate tmw = LocalDate.now().plusDays(1);
			for (Task task : scheduledTasksToDo) {
				if (task.getEndDate().isAfter(tmw)) {
					break;
				}
				firstList.add(task);
				indexList.add(count++);
			}
		}
		return firstList;
	}

	public ArrayList<Integer> setFloatingIndexList() {
		ArrayList<Integer> index = new ArrayList<Integer>();
		int count = scheduledTasksOverDue.size() + scheduledTasksToDo.size();
		for (int i = 1; i <= floatingTasksToDo.size(); i++) {
			index.add(i + count);
		}
		return index;
	}

	/*
	 * calls retrieveParsedCommand, from which private methods are called in
	 * Logic class
	 */
	public void executeCommand(String userInput) {
		try {
			historyObject.addToCommandHistory(userInput);
			historyObject.setIndexCommandHistory();
			retrieveParsedCommand(userInput);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * this method gets back the parsed Command class from parser. Proceeds to
	 * execute function aft obtaining COMMAND_TYPE and Task classes
	 */
	public void retrieveParsedCommand(String originalDescription) {
		try {
			Command.COMMAND_TYPE typeCommand = null;
			Command existingCommand = CommandParser.getParsedCommand(originalDescription);
			typeCommand = getCommand(existingCommand);
			Task getTaskToExecute = getTaskDescription(existingCommand);
			execute(typeCommand, existingCommand, getTaskToExecute);
			appendFeedBack(FEEDBACK_YOUR_COMMAND + originalDescription);
			trackConflictingTasks.clear();
			autoChangeTaskStatus();
			storage.storeToFiles(getFloatingTasksToDo(), getFloatingTasksComplete(), getScheduledTasksToDo(),
					getScheduledTasksComplete(), getScheduledTasksOverDue());
		} catch (Exception e) {
			// e.printStackTrace();
			setFeedBack(FEEDBACK_INVALID_COMMAND);
		}
	}

	/*
	 * this method gets the specific command type to be executed such as add,
	 * delete, modify etc
	 */
	public Command.COMMAND_TYPE getCommand(Command existingCommand) {
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
	public Task getTaskDescription(Command existingCommand) {
		Task executeTask = existingCommand.getTaskDetails();
		return executeTask;
	}

	/*
	 * this method calls the respective execution methods for the respective
	 * command types
	 */
	public void execute(Command.COMMAND_TYPE executeCommand, Command retrievedCommand, Task executeTask) {

		isSearchCommand = false;
		isHomeScreen = false;
		isHighlightOperation = false;

		switch (executeCommand) {
		case ADD_TASK:
			isHighlightOperation = true;
			addTask(executeTask, false, 1);
			historyObject.clearRedoStack();
			break;
		case DELETE_TASK:
			deleteTask(retrievedCommand.getIndexList(), false);
			historyObject.clearRedoStack();
			break;
		case MODIFY_TASK:
			editTask(retrievedCommand.getIndexList(), executeTask.getDescription(), false);
			historyObject.clearRedoStack();
			break;
		case COMPLETE_TASK:
			completeTask(retrievedCommand.getIndexList(), false);
			historyObject.clearRedoStack();
			break;
		case INCOMPLETE_TASK:
			incompleteTask(retrievedCommand.getIndexList(), false);
			historyObject.clearRedoStack();
			break;
		case UNDO_TASK:
			undoTask();
			break;
		case REDO_TASK:
			redoTask();
			break;
		case VIEW_LIST:
			searchTask(executeTask);
			historyObject.clearRedoStack();
			break;
		case SEARCH_TASK:
			searchTask(executeTask);
			historyObject.clearRedoStack();
			break;
		case SET_DIRECTORY:
			setNewDirectoryPath(executeTask);
			historyObject.clearRedoStack();
			break;
		case HOME:
			firstRun();
			setFeedBack("Home Screen Display");
			historyObject.clearRedoStack();
			break;
		case VIEW_ALL:
			setFeedBack(FEEDBACK_EMPTY_STRING);
			historyObject.clearRedoStack();
			break;
		case HELP:
			setHelpInstructions();
			setFeedBack(FEEDBACK_HELP_CALLED);
			historyObject.clearRedoStack();
			break;
		case EXIT:
			exit();
			historyObject.clearRedoStack();
			break;
		}
	}

	public void setNewDirectoryPath(Task executeTask) {
		try {
			storage.setCurrentPathName(executeTask.getDescription());
			setFeedBack("Directory Path Sucessfully Changed!");
		} catch (Exception e) {
			setFeedBack("Invalid Directory Path!!");
		}
	}

	public void setHelpInstructions() {
		BottomBottom.setHelp();
	}

	/*
	 * adds task based on the startdate, enddate, starttime and endtime for
	 * scheduled tasks into either scheduledtodo or scheduledoverdue arraylist
	 * while floating tasks are auto added into floatingtodo arraylist
	 */
	private int addTask(Task executeTask, boolean isUndoOperation, int setRecentTask) {
		int indexOfTask = -1;

		if (executeTask.isComplete()) {
			if (executeTask.isFloatingTask()) {
				floatingTasksComplete.add(executeTask);
			} else {
				scheduledTasksComplete.add(executeTask);
			}
			return indexOfTask;
		}

		if (executeTask.getDescription() == null || executeTask.getDescription().isEmpty()) {
			isHighlightOperation = false;
			setFeedBack(FEEDBACK_EMPTY_TASK_DESCRIPTION);
			return indexOfTask;
		}
		if (setRecentTask == 1) {
			indexOfTask = duplicationCheckProcedures(executeTask);
			setRecentTaskDetails(executeTask);
			setRecentIndexOfTask(indexOfTask);
		} else if (setRecentTask == 0) {
			indexOfTask = duplicationCheckProcedures(executeTask);
			updateRecentIndexOfTask();
		}

		if (!isUndoOperation && indexOfTask >= 0) {
			ArrayList<Task> taskList = new ArrayList<Task>();
			ArrayList<Integer> indexList = new ArrayList<Integer>();
			taskList.add(executeTask);
			indexList.add(indexOfTask);
			OldCommand recentCommand = new OldCommand(COMMAND_TYPE.ADD_TASK, taskList, indexList);
			historyObject.addToUndoList(recentCommand);
		}
		return indexOfTask;
	}

	/*
	 * checks whether new task being added already exists in scheduledtodo,
	 * scheduledoverdue and floatingtodo methods. If task exists in one of these
	 * 3 arraylists, task is not added. If duplicate is not found, invokes
	 * addTaskInOrder method to get the position to add the new task in its
	 * respective arraylist
	 */
	private int duplicationCheckProcedures(Task currentTask) {
		ArrayList<Boolean> duplicate = new ArrayList<Boolean>();
		boolean checkForDuplication = true;
		// boolean isDuplicate;
		int currentIndexOfTask = -1;

		if (currentTask.isScheduledTask()) {
			LocalDateTime currentTaskEndDateTime = LocalDateTime.of(currentTask.getEndDate(), currentTask.getEndTime());

			if (currentTaskEndDateTime.isBefore(LocalDateTime.now())) {
				checkForDuplication = checkForScheduledDuplication(currentTask, scheduledTasksOverDue);
				duplicate.add(checkForDuplication);
			} else {
				checkForDuplication = checkForScheduledDuplication(currentTask, scheduledTasksToDo);
				duplicate.add(checkForDuplication);
			}
		} else if (currentTask.isFloatingTask()) {
			checkForDuplication = checkForFloatingDuplication(currentTask, floatingTasksToDo);
			duplicate.add(checkForDuplication);
		}

		if (currentTask.isScheduledTask()) {
			if (duplicate.size() == 1) {
				if (duplicate.get(0) == false) {
					currentIndexOfTask = addTaskInOrder(currentTask);
				} else if (duplicate.get(0)) {
					isHighlightOperation = false;
					setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_ADDING);
				}
			}
		} else if (currentTask.isFloatingTask()) {
			if (duplicate.get(0) == false) {
				floatingTasksToDo.add(currentTask);
				currentIndexOfTask = scheduledTasksOverDue.size() + scheduledTasksToDo.size()
						+ floatingTasksToDo.size();
				setFeedBack(FEEDBACK_TASK_ADDED);
			} else if (duplicate.get(0)) {
				isHighlightOperation = false;
				setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_ADDING);
			}
		}
		duplicate.clear();
		return currentIndexOfTask;

	}

	/*
	 * it calls standardAddScheduledTaskProcedures method to determine which
	 * position should the scheduledTask be added to. Returns this position to
	 * duplicationCheckProcedures method
	 */
	private int addTaskInOrder(Task executeTask) {
		int position = -1;

		if (LocalDateTime.of(executeTask.getEndDate(), executeTask.getEndTime()).isBefore(LocalDateTime.now())) {
			int result = standardAddScheduledTaskProcedures(scheduledTasksOverDue, executeTask, position);
			position = result;
		} else {
			int result = standardAddScheduledTaskProcedures(scheduledTasksToDo, executeTask, position);
			position = result + scheduledTasksOverDue.size();
		}
		return position + 1;
	}

	/*
	 * gets position to add for scheduled task from sortTaskList method checks
	 * whether scheduled task is overlapping with any other scheduled task in
	 * same scheduled arraylist. if overlapping occurs, appropriate feedback is
	 * set. position for new task to be added passed to addTaskInOrder method
	 */
	private int standardAddScheduledTaskProcedures(ArrayList<Task> scheduledTaskList, Task taskAdd, int positionToAdd) {
		boolean overLapWithTask = true;

		positionToAdd = sortTaskList(scheduledTaskList, taskAdd);
		overLapWithTask = compareWithScheduledTasks(taskAdd, scheduledTaskList);
		scheduledTaskList.add(positionToAdd, taskAdd);
		if (overLapWithTask) {
			if (trackConflictingTasks.size() == 1) {
				setFeedBack(FEEDBACK_TASK_ADDED + " but new task is conflicting with "
						+ trackConflictingTasks.get(0).getDescription());
			} else {
				setFeedBack(FEEDBACK_TASK_ADDED_BUT_ENCOUNTERED_CONFLICTS);
			}
		} else {
			setFeedBack(FEEDBACK_TASK_ADDED);
		}
		return positionToAdd;
	}

	/*
	 * checks whether new task slated for adding is already existing in either
	 * scheduledtodo or scheduledoverdue arraylists. Returns true if duplicate
	 * is found and false if no copy is found.
	 */
	private boolean checkForScheduledDuplication(Task relevantTask, ArrayList<Task> scheduledTasks) {
		for (int i = 0; i < scheduledTasks.size(); i++) {
			int tracker = 0;
			if (relevantTask.getDescription().equals(scheduledTasks.get(i).getDescription())) {
				tracker++;
			}
			if (((relevantTask.getStartDate() != null) && (scheduledTasks.get(i).getStartDate() != null)
					&& (relevantTask.getStartDate().equals(scheduledTasks.get(i).getStartDate())))
					|| (relevantTask.getStartDate() == null) && (scheduledTasks.get(i).getStartDate() == null)) {
				tracker++;
			}
			if (((relevantTask.getStartTime() != null) && (scheduledTasks.get(i).getStartTime() != null)
					&& (relevantTask.getStartTime().equals(scheduledTasks.get(i).getStartTime())))
					|| (relevantTask.getStartTime() == null) && (scheduledTasks.get(i).getStartTime() == null)) {
				tracker++;
			}
			if (relevantTask.getEndDate().equals(scheduledTasks.get(i).getEndDate())) {
				tracker++;
			}
			if (((relevantTask.getEndTime() != null) && (scheduledTasks.get(i).getEndTime() != null)
					&& (relevantTask.getEndTime().equals(scheduledTasks.get(i).getEndTime())))
					|| (relevantTask.getEndTime() == null) && (scheduledTasks.get(i).getEndTime() == null)) {
				tracker++;
			}
			if (tracker == 5) {
				setRecentLocation(i);
				return true;
			}
		}
		return false;
	}

	/*
	 * checks whether new task slated for adding is already existing in
	 * floatingTasksToDo arraylist. Returns true if duplicate is found and false
	 * if no copy is found.
	 */
	private boolean checkForFloatingDuplication(Task relevantTask, ArrayList<Task> floatingTasks) {
		for (int i = 0; i < floatingTasks.size(); i++) {
			if (relevantTask.getDescription().equalsIgnoreCase(floatingTasks.get(i).getDescription())) {
				setRecentLocation(i);
				return true;
			}
		}
		return false;
	}

	/*
	 * checks if new task is conflicting with all the existing tasks in that
	 * particular arraylist in which the new task is going to be added. returns
	 * true if new task is conflicting with any of the existing tasks and false
	 * otherwise and this outcome is passed to standardAddScheduledTask
	 * Procedures method. Invokes noteConflictingTask method to take note of
	 * conflicting tasks' indexes
	 */
	private boolean compareWithScheduledTasks(Task taskForAdd, ArrayList<Task> relevantTaskList) {
		LocalDateTime taskEndDateTime = null, taskStartDateTime = null;
		LocalDateTime relevantTaskEndDateTime = null, relevantTaskStartDateTime = null;
		int trackBlock = 0;

		if (taskForAdd.getEndDate() != null) {
			taskEndDateTime = LocalDateTime.of(taskForAdd.getEndDate(), taskForAdd.getEndTime());
		}
		if (taskForAdd.getStartDate() != null) {
			taskStartDateTime = LocalDateTime.of(taskForAdd.getStartDate(), taskForAdd.getStartTime());
		}

		for (int i = 0; i < relevantTaskList.size(); i++) {
			relevantTaskEndDateTime = LocalDateTime.of(relevantTaskList.get(i).getEndDate(),
					relevantTaskList.get(i).getEndTime());
			relevantTaskStartDateTime = null;
			if (relevantTaskList.get(i).getStartDate() != null) {
				relevantTaskStartDateTime = LocalDateTime.of(relevantTaskList.get(i).getStartDate(),
						relevantTaskList.get(i).getStartTime());
			}

			if (relevantTaskStartDateTime != null) {
				if ((taskStartDateTime != null) && (taskEndDateTime.isAfter(relevantTaskStartDateTime))
						&& (taskStartDateTime.isBefore(relevantTaskEndDateTime))) {
					trackBlock++;
					noteConflictingTask(relevantTaskList.get(i));
				} else if ((taskStartDateTime == null) && (taskEndDateTime.isAfter(relevantTaskStartDateTime))
						&& (taskEndDateTime.isBefore(relevantTaskEndDateTime))) {
					trackBlock++;
					noteConflictingTask(relevantTaskList.get(i));
				}
			} else if (relevantTaskStartDateTime == null) {
				if ((taskStartDateTime != null) && (taskEndDateTime.isAfter(relevantTaskEndDateTime))
						&& (taskStartDateTime.isBefore(relevantTaskEndDateTime))) {
					trackBlock++;
					noteConflictingTask(relevantTaskList.get(i));
				}
			}
		}

		if (trackBlock == 0) {
			return false;
		} else {
			return true;
		}
	}

	/* takes note of the indexes for conflicting tasks */
	private void noteConflictingTask(Task conflict) {
		trackConflictingTasks.add(conflict);
	}

	/*
	 * sorts scheduled tasks to be added based on end date, followed by
	 * startdatetime instance
	 */
	private int sortTaskList(ArrayList<Task> taskList, Task task) {
		LocalDateTime taskStartDateTime = null;
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

			if (task.getEndDate().isBefore(taskList.get(i).getEndDate())) {
				return i;
			} else if (task.getEndDate().equals(taskList.get(i).getEndDate())) {
				if ((selectedTaskStartDateTime != null) && (taskStartDateTime != null)) {
					if (taskStartDateTime.isBefore(selectedTaskStartDateTime)) {
						return i;
					}
				} else if ((selectedTaskStartDateTime == null) && (taskStartDateTime != null)) {
					return i;
				} else if ((task.getEndTime().isBefore(taskList.get(i).getEndTime()))
						&& (selectedTaskStartDateTime == null)) {
					return i;
				} else if ((task.getEndTime().isBefore(taskList.get(i).getEndTime()))
						&& (selectedTaskStartDateTime == null) && (taskStartDateTime == null)) {
					return i;
				}
			}
		}
		return taskPosition;
	}

	/*
	 * deleteTask from scheduledTasksToDo or floatingTasksToDo based on task
	 * number. Multiple tasks can be deleted
	 */
	private void deleteTask(ArrayList<Integer> taskDigit, boolean isUndoOperation) {
		// Creating undo parameter
		ArrayList<Task> taskList = new ArrayList<Task>();
		Task removedTask = null;
		int lastAddedIndex = -1;
		boolean isAborted = false;

		if (taskDigit == null || taskDigit.isEmpty()) {
			taskDigit = new ArrayList<Integer>();
			lastAddedIndex = getRecentIndexOfTask();
			taskDigit.add(lastAddedIndex);
		}
		for (int i = taskDigit.size() - 1; i >= 0; i--) {
			if (taskDigit.get(i) > 0) {
				if (taskDigit.get(i) <= scheduledTasksOverDue.size()) {
					removedTask = scheduledTasksOverDue.remove(taskDigit.get(i) - 1);
					taskList.add(0, removedTask);
					setFeedBack(FEEDBACK_TASK_DELETED);
				} else if (taskDigit.get(i) <= (scheduledTasksOverDue.size() + scheduledTasksToDo.size())) {
					removedTask = scheduledTasksToDo.remove(taskDigit.get(i) - 1 - scheduledTasksOverDue.size());
					taskList.add(0, removedTask);
					setFeedBack(FEEDBACK_TASK_DELETED);

				} else if (taskDigit.get(
						i) <= (scheduledTasksToDo.size() + floatingTasksToDo.size() + scheduledTasksOverDue.size())) {
					removedTask = floatingTasksToDo
							.remove(taskDigit.get(i) - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
					taskList.add(0, removedTask);
					setFeedBack(FEEDBACK_TASK_DELETED);

				} else if (taskDigit.get(i) <= (scheduledTasksToDo.size() + floatingTasksToDo.size()
						+ scheduledTasksOverDue.size() + scheduledTasksComplete.size())) {

					removedTask = scheduledTasksComplete.remove(taskDigit.get(i) - 1 - scheduledTasksToDo.size()
							- scheduledTasksOverDue.size() - floatingTasksToDo.size());

					taskList.add(0, removedTask);
					setFeedBack(FEEDBACK_TASK_DELETED);
				} else if (taskDigit
						.get(i) <= (scheduledTasksToDo.size() + floatingTasksToDo.size() + scheduledTasksOverDue.size()
								+ scheduledTasksComplete.size() + floatingTasksComplete.size())) {
					removedTask = floatingTasksComplete.remove(taskDigit.get(i) - 1 - scheduledTasksToDo.size()
							- scheduledTasksOverDue.size() - floatingTasksToDo.size() - scheduledTasksComplete.size());
					taskList.add(0, removedTask);
					setFeedBack(FEEDBACK_TASK_DELETED);
				} else {
					isAborted = true;
					setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
					break;
				}
			} else {
				isAborted = true;
				setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM);
				break;
			}
		}

		// for undo functionality
		if (!isUndoOperation) {
			OldCommand recentCommand = new OldCommand(COMMAND_TYPE.DELETE_TASK, taskList, taskDigit);
			historyObject.addToUndoList(recentCommand);
		}

		if (isAborted && !isUndoOperation) {
			undoTask();
			setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
		}
		updateRecentIndexOfTask();
	}

	/*
	 * this method is invoked by addTask, deleteTask, editTask methods to sort
	 * out the new index of most recent task dealt with by the user. checks all
	 * 5 arraylists and updates index accordingly
	 */
	private void updateRecentIndexOfTask() {
		Task mostRecentTask = getRecentTaskDetails();
		boolean outcome = false;

		if (mostRecentTask.isScheduledTask()) {
			LocalDateTime mostRecentTaskEndDateTime = LocalDateTime.of(mostRecentTask.getEndDate(),
					mostRecentTask.getEndTime());

			if (mostRecentTaskEndDateTime.isBefore(LocalDateTime.now())) {
				outcome = checkForScheduledDuplication(mostRecentTask, scheduledTasksOverDue);
				if (outcome) {
					setRecentIndexOfTask(getRecentLocation() + 1);
					setRecentTaskDetails(scheduledTasksOverDue.get(getRecentLocation()));
				}
			} else {
				outcome = checkForScheduledDuplication(mostRecentTask, scheduledTasksToDo);
				if (outcome) {
					setRecentIndexOfTask(getRecentLocation() + 1 + scheduledTasksOverDue.size());
					setRecentTaskDetails(scheduledTasksToDo.get(getRecentLocation()));
				}
			}
		} else if (mostRecentTask.isFloatingTask()) {
			outcome = checkForFloatingDuplication(mostRecentTask, floatingTasksToDo);
			if (outcome) {
				setRecentIndexOfTask(getRecentLocation() + 1 + scheduledTasksOverDue.size());
				setRecentTaskDetails(floatingTasksToDo.get(getRecentLocation()));
			}
		}
		if (!outcome) {
			if (mostRecentTask.isScheduledTask()) {
				outcome = checkForScheduledDuplication(mostRecentTask, scheduledTasksComplete);
				if (outcome) {
					setRecentIndexOfTask(getRecentLocation() + 1 + scheduledTasksOverDue.size()
							+ scheduledTasksToDo.size() + floatingTasksToDo.size());
					setRecentTaskDetails(scheduledTasksComplete.get(getRecentLocation()));
				}
			} else if (mostRecentTask.isFloatingTask()) {
				outcome = checkForFloatingDuplication(mostRecentTask, floatingTasksComplete);
				if (outcome) {
					setRecentIndexOfTask(getRecentLocation() + 1 + scheduledTasksOverDue.size()
							+ scheduledTasksToDo.size() + floatingTasksToDo.size() + scheduledTasksComplete.size());
					setRecentTaskDetails(floatingTasksComplete.get(getRecentLocation()));
				}
			}
		}
		if (!outcome) {
			setRecentIndexOfTask(-1);
		}
	}

	/*
	 * modifies tasks based on description, startdate, starttime, enddate,
	 * endtime. Tasks to be edited are removed, modified are checked for
	 * duplication before being
	 */
	public void editTask(ArrayList<Integer> indexList, String editInfo, boolean isUndoOperation) {
		Task taskToEdit = null;
		Task taskOriginal = null;
		int indexToEdit = -1;
		boolean duplicatedScheduledOverDue = false, duplicated = false;

		isHighlightOperation = true;

		if (indexList == null || indexList.isEmpty()) {
			indexToEdit = getRecentIndexOfTask();
			indexList = new ArrayList<Integer>();
			indexList.add(indexToEdit);
		} else {
			indexToEdit = indexList.get(0);
		}

		if (indexToEdit > 0) {
			if (indexToEdit <= scheduledTasksOverDue.size()) {
				taskOriginal = scheduledTasksOverDue.get(indexToEdit - 1);
				taskToEdit = scheduledTasksOverDue.remove(indexToEdit - 1);
			} else if (indexToEdit <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()) {
				taskOriginal = scheduledTasksToDo.get(indexToEdit - 1 - scheduledTasksOverDue.size());
				taskToEdit = scheduledTasksToDo.remove(indexToEdit - 1 - scheduledTasksOverDue.size());
			} else if (indexToEdit <= scheduledTasksToDo.size() + floatingTasksToDo.size()
					+ scheduledTasksOverDue.size()) {
				taskOriginal = floatingTasksToDo
						.get(indexToEdit - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
				taskToEdit = floatingTasksToDo
						.remove(indexToEdit - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
			} else {
				isHighlightOperation = false;
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM + "\n");
			}
		}

		if (taskToEdit == null) {
			setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM + "\n" + FEEDBACK_TASK_NOT_MODIFIED);
		} else {
			Task editedTask = CommandParser.editExistingTask(taskToEdit, editInfo);
			if (editedTask.isScheduledTask()) {
				LocalDateTime editedTaskEndDateTime = LocalDateTime.of(editedTask.getEndDate(),
						editedTask.getEndTime());

				if (editedTaskEndDateTime.isBefore(LocalDateTime.now())) {
					duplicatedScheduledOverDue = checkForScheduledDuplication(editedTask, scheduledTasksOverDue);
				} else {
					duplicated = checkForScheduledDuplication(editedTask, scheduledTasksToDo);
				}
				if ((duplicatedScheduledOverDue) || (duplicated)) {
					editedTask = taskOriginal;
					isHighlightOperation = false;
					setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_EDITING);
				}
			} else if (editedTask.isFloatingTask()) {
				duplicated = checkForFloatingDuplication(editedTask, floatingTasksToDo);

				if (duplicated) {
					editedTask = taskOriginal;
					isHighlightOperation = false;
					setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_EDITING);
				}
			}
			int newIndex = addTask(editedTask, true, 1);

			if ((duplicated) || (duplicatedScheduledOverDue)) {
				setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_EDITING);
			} else if ((!duplicated) && (!duplicatedScheduledOverDue)) {
				if (getFeedBack().equals(FEEDBACK_TASK_ADDED)) {
					setFeedBack(FEEDBACK_TASK_MODIFIED);
				} else if (trackConflictingTasks.size() == 1) {
					setFeedBack(FEEDBACK_TASK_MODIFIED + " but new task is conflicting with "
							+ trackConflictingTasks.get(0).getDescription());
				} else {
					setFeedBack(FEEDBACK_TASK_MODIFIED_BUT_ENCOUNTERED_CONFLICTS);
				}
			}

			if (!isUndoOperation && newIndex >= 0 && !duplicated) {
				ArrayList<Task> taskList = new ArrayList<Task>();
				taskList.add(taskToEdit);
				taskList.add(editedTask);
				indexList.add(newIndex);
				OldCommand recentCommand = new OldCommand(COMMAND_TYPE.MODIFY_TASK, taskList, indexList);
				historyObject.addToUndoList(recentCommand);
			}
		}
	}

	/*
	 * adds task into the respective completeArrayList and removes that same
	 * task from the ArrayList that it is currently residing in based on task
	 * number entered by user. Multiple tasks can be completed at once
	 */
	private void completeTask(ArrayList<Integer> taskIndex, boolean isUndoOperation) {
		// undo parameter
		ArrayList<Task> taskList = new ArrayList<Task>();
		int indexToComplete = -1;
		boolean isAborted = false;

		if (taskIndex == null || taskIndex.isEmpty()) {
			taskIndex = new ArrayList<Integer>();
			indexToComplete = getRecentIndexOfTask();
			taskIndex.add(indexToComplete);
		}
		for (int i = taskIndex.size() - 1; i >= 0; i--) {
			int taskToComplete = taskIndex.get(i) - 1;
			if (taskToComplete >= 0) {
				if (taskToComplete < scheduledTasksOverDue.size()) {
					Task completedTask = markAsComplete(scheduledTasksOverDue, scheduledTasksComplete, taskToComplete);
					taskList.add(0, completedTask);
				} else if (taskToComplete < (scheduledTasksToDo.size() + scheduledTasksOverDue.size())) {
					taskToComplete = taskToComplete - (scheduledTasksOverDue.size());
					Task completedTask = markAsComplete(scheduledTasksToDo, scheduledTasksComplete, taskToComplete);
					taskList.add(0, completedTask);
				} else if (taskToComplete < (scheduledTasksOverDue.size() + scheduledTasksToDo.size()
						+ floatingTasksToDo.size())) {
					taskToComplete = taskToComplete - (scheduledTasksToDo.size() + scheduledTasksOverDue.size());
					Task completedTask = markAsComplete(floatingTasksToDo, floatingTasksComplete, taskToComplete);
					taskList.add(0, completedTask);
				} else {
					isAborted = true;
					setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
					break;
				}
			} else {
				isAborted = true;
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
				break;
			}
		}

		if (!isUndoOperation) {
			// for undo functionality
			OldCommand recentCommand = new OldCommand(COMMAND_TYPE.COMPLETE_TASK, taskList, taskIndex);
			historyObject.addToUndoList(recentCommand);
		}

		if (isAborted) {
			undoTask();
			setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
		}
		updateRecentIndexOfTask();
	}

	public Task markAsComplete(ArrayList<Task> incompleteList, ArrayList<Task> completeList, int taskNum) {
		Task completeTask = incompleteList.remove(taskNum);
		completeTask.setAsComplete();
		completeList.add(completeTask);
		setFeedBack(FEEDBACK_TASK_COMPLETED);
		return completeTask;
	}

	/*
	 * Tasks to incomplete are moved from their counterpart completed arraylists
	 * to the relevant incomplete arraylists. Multiple tasks can be incompleted
	 * at once
	 */
	public void incompleteTask(ArrayList<Integer> indexList, boolean isUndoOperation) {
		// undo parameter
		ArrayList<Task> taskList = new ArrayList<Task>();

		boolean isAborted = false;

		int minIndex = scheduledTasksOverDue.size() + scheduledTasksToDo.size() + floatingTasksToDo.size() + 1;
		int maxIndex = minIndex + scheduledTasksComplete.size() + floatingTasksComplete.size() - 1;

		if (indexList == null || indexList.isEmpty()) {
			indexList = new ArrayList<Integer>();
			int indexToIncomplete = getRecentIndexOfTask();
			indexList.add(indexToIncomplete);
		}
		if (indexList != null && !indexList.isEmpty()) {
			for (int index = indexList.size() - 1; index >= 0; index--) {
				if (indexList.get(index) == getRecentIndexOfTask()) {
					setRecentIndexOfTask(-1);
				}
				if ((indexList.get(index) >= minIndex) && (indexList.get(index) <= maxIndex)) {
					if (indexList.get(index) < (minIndex + scheduledTasksComplete.size())) {
						Task taskToMark = scheduledTasksComplete.remove(indexList.get(index) - minIndex);
						taskToMark.setAsIncomplete();
						addTask(taskToMark, true, 0);
						taskList.add(0, taskToMark);
					} else {
						Task taskToMark = floatingTasksComplete
								.remove(indexList.get(index) - minIndex - scheduledTasksComplete.size());
						taskToMark.setAsIncomplete();
						addTask(taskToMark, true, 0);
						taskList.add(0, taskToMark);
					}
					setFeedBack(FEEDBACK_TASK_INCOMPLETED);
				} else {
					isAborted = true;
					setFeedBack(FEEDBACK_TASK_INCOMPLETED_INVALID);
					break;
				}
			}
			if (!isUndoOperation) {
				// for undo functionality
				OldCommand recentCommand = new OldCommand(COMMAND_TYPE.INCOMPLETE_TASK, taskList, indexList);
				historyObject.addToUndoList(recentCommand);
			}

			if (isAborted) {
				undoTask();
				setFeedBack(FEEDBACK_TASK_INCOMPLETED_INVALID);
			}
		}
	}

	/* set task as complete */
	public void markTaskComplete(Task task) {
		task.setAsComplete();
		if (task.isFloatingTask()) {
			floatingTasksToDo.remove(task);
			floatingTasksComplete.add(task);
		} else if (task.isScheduledTask()) {
			scheduledTasksOverDue.remove(task);
			scheduledTasksToDo.remove(task);
			scheduledTasksComplete.add(task);
		}
	}

	/* set task as incomplete */
	public void markTaskIncomplete(Task task) {
		task.setAsIncomplete();
		// System.out.println(task.getDescription());
		if (task.isFloatingTask()) {
			floatingTasksComplete.remove(task);
			addTask(task, true, 0);
		} else if (task.isScheduledTask()) {
			scheduledTasksComplete.remove(task);
			addTask(task, true, 0);
		}
	}

	/*
	 * This method automatically shifts scheduledtaskstodo to
	 * scheduledtasksoverdue when date and time has exceeded due date and due
	 * time specified for scheduled task
	 */
	private void autoChangeTaskStatus() {
		LocalDateTime presentDateTime = LocalDateTime.now();

		for (int i = 0; i < scheduledTasksToDo.size(); i++) {
			LocalDateTime scheduledEndDateTime = LocalDateTime.of(scheduledTasksToDo.get(i).getEndDate(),
					scheduledTasksToDo.get(i).getEndTime());

			if ((scheduledEndDateTime.compareTo(presentDateTime)) < 0) {
				changeStatusToOverdue(i);
			}
		}
	}

	/* moves overdue task from scheduledtodo to scheduledoverdue */
	private void changeStatusToOverdue(int i) {
		scheduledTasksOverDue.add(scheduledTasksToDo.get(i));
		setFeedBack("Task " + scheduledTasksToDo.get(i).getDescription() + " has exceeded deadline");
		scheduledTasksToDo.remove(i);
	}

	// @@author A0132778W
	/**
	 * Reverts back the previous action performed by the user.
	 */
	public void undoTask() {
		try {
			OldCommand toUndo = historyObject.getFromUndoList();
			OldCommand.COMMAND_TYPE cmdType = toUndo.getCommandType();

			switch (cmdType) {
			case ADD_TASK:
				addTaskList(toUndo.getTaskList());
				break;
			case DELETE_TASK:
				deleteTask(toUndo.getIndexList(), true);
				break;
			case COMPLETE_TASK:
				markAsCompleteListUndoOp(toUndo.getTaskList(), toUndo.getIndexList());
				break;
			case INCOMPLETE_TASK:
				markAsIncompleteListUndoOp(toUndo.getTaskList(), toUndo.getIndexList());
				break;
			case MODIFY_TASK:
				/*
				 * For modify the Task list contains two tasks. The first one is
				 * the oldTask and the second one is the newTask. Delete new,
				 * add old.
				 */
				deleteSingleTask(toUndo.getIndexList().get(1));
				addTask(toUndo.getTaskList().get(0), true, 1);
				break;
			default:
				// go back to original home screen
				// incomplete
			}
			setFeedBack(FEEDBACK_UNDO_VALID);
		} catch (Exception e) {
			setFeedBack(FEEDBACK_UNDO_INVALID);
		}
	}

	/**
	 * This method is used to revert the previous undo action. Only works if
	 * there is/are prior undo(s) before the redo(s).
	 */
	public void redoTask() {
		try {
			OldCommand toRedo = historyObject.getFromRedoList();
			OldCommand.COMMAND_TYPE cmdType = toRedo.getCommandType();

			switch (cmdType) {
			case ADD_TASK:
				addTaskList(toRedo.getTaskList());
				break;
			case DELETE_TASK:
				deleteTask(toRedo.getIndexList(), true);
				break;
			case COMPLETE_TASK:
				markAsCompleteListUndoOp(toRedo.getTaskList(), toRedo.getIndexList());
				break;
			case INCOMPLETE_TASK:
				markAsIncompleteListUndoOp(toRedo.getTaskList(), toRedo.getIndexList());
				break;
			case MODIFY_TASK:
				/*
				 * For modify the Task list contains two tasks. The first one is
				 * the oldTask and the second one is the newTask. Delete old,
				 * add new.
				 */
				deleteSingleTask(toRedo.getIndexList().get(0));
				addTask(toRedo.getTaskList().get(1), true, 1);
				break;
			default:
				// go back to original home screen
				// incomplete
			}
			setFeedBack(FEEDBACK_REDO_VALID);
		} catch (Exception e) {
			setFeedBack(FEEDBACK_REDO_INVALID);
		}
	}

	public void addTaskList(ArrayList<Task> taskList) {
		for (Task newTask : taskList) {
			addTask(newTask, true, 0);
		}
	}

	public void markAsCompleteListUndoOp(ArrayList<Task> taskList, ArrayList<Integer> indexList) {
		for (Task task : taskList) {
			markTaskComplete(task);
		}
	}

	public void markAsIncompleteListUndoOp(ArrayList<Task> taskList, ArrayList<Integer> indexList) {
		for (Task task : taskList) {
			markTaskIncomplete(task);
		}
	}

	public void deleteSingleTask(int index) {
		if (index > 0) {
			if (index <= scheduledTasksOverDue.size()) {
				scheduledTasksOverDue.remove(index - 1);
			} else if (index <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()) {
				scheduledTasksToDo.remove(index - 1 - scheduledTasksOverDue.size());
			} else if (index <= scheduledTasksToDo.size() + floatingTasksToDo.size() + scheduledTasksOverDue.size()) {
				floatingTasksToDo.remove(index - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
			} else {
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
			}
		}
		setFeedBack(FEEDBACK_TASK_DELETED);
	}

	/*
	 * search tasks based on parameters indicated by user. Parameters can
	 * include dates or task descriptions or letters with which task names start
	 */
	private void searchTask(Task taskToFind) {
		if ((taskToFind.getDescription() != null) && !(taskToFind.getDescription().isEmpty())) {
			if (taskToFind.getDescription().equalsIgnoreCase("all")) {
				execute(Command.COMMAND_TYPE.VIEW_ALL, null, null);
				return;
			}
		}

		isSearchCommand = true;
		ArrayList<Task> searchTaskList = new ArrayList<Task>();
		ArrayList<Integer> searchIndexList = new ArrayList<Integer>();

		if (taskToFind.getStartDate() != null && taskToFind.getStartDate() == LocalDate.MIN) {
			searchTaskList = new ArrayList<Task>(getScheduledTasksOverDue());
			for (int i = 0; i < scheduledTasksOverDue.size(); i++) {
				searchIndexList.add(i + 1);
			}
		} else {
			Search obj = new Search();
			obj.searchTask(taskToFind);
			searchTaskList = obj.getSearchList();
			searchIndexList = obj.getSearchIndexList();
		}

		if (searchTaskList.size() > 0) {
			setFeedBack(FEEDBACK_SEARCH_VALID);
			BottomBottom.setSearchResult(searchTaskList, searchIndexList);
			// (new TempCLI()).printSearchTaskLists(searchTaskList,
			// searchIndexList);
		} else {
			BottomBottom.failedSearch();
			setFeedBack(FEEDBACK_SEARCH_INVALID);
		}
	}

	public boolean hasSearchList() {
		return isSearchCommand;
	}

	/**
	 * Refreshing screen every minute. This method is invoked by the UI every
	 * minute.
	 */
	public void refresh() {
		if (!isSearchCommand) {
			if (isHomeScreen) {
				executeCommand("home");
			} else {
				executeCommand("view all");
			}
			historyObject.removeLastCommandFromHistory();
		}
	}

	/*
	 * exiting of program. exits once all data is saved into json files in
	 * storage
	 */
	private void exit() {
		System.exit(0);
	}
}
