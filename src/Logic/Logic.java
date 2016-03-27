package Logic;

import java.util.ArrayList;
import java.time.LocalDateTime;

import ScheduleHacks.Task;
import ScheduleHacks.OldCommand;
import ScheduleHacks.OldCommand.COMMAND_TYPE;
import ScheduleHacks.History;
import Parser.CommandParser;
import Parser.Command;
import GUI.TempCLI;
import Storage.Storage;

public class Logic {

	private String feedBack;
	private boolean isSearchCommand;

	private static Logic logicObject = null;

	private Storage storage = Storage.getInstance();
	private History historyObject = History.getInstance();

	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	private ArrayList<Task> recentAddedList = new ArrayList<Task>();
	private ArrayList<Task> searchTasks = new ArrayList<Task>();
	private int recentAddedPosition;
	private LocalDateTime blockedStartDateTime = LocalDateTime.of(2099, 12, 31, 23, 59);
	private LocalDateTime blockedEndDateTime = LocalDateTime.of(2199, 12, 31, 23, 59);

	private static final String FEEDBACK_INVALID_COMMAND = "Invalid Command!";
	private static final String FEEDBACK_INVALID_COMMAND_TYPE = "Invalid command type entered!";
	private static final String FEEDBACK_TASK_ADDED = "Task Added Successfully";
	private static final String FEEDBACK_TASK_DELETED = "Task Deleted Successfully";
	private static final String FEEDBACK_NON_EXISTENT_TASK_NUM = "Task number entered was not found!";
	private static final String FEEDBACK_NEGATIVE_TASK_NUM = "Task number entered cannot be 0 or negative!";
	private static final String FEEDBACK_TASK_MODIFIED = "Task Edited Successfully";
	private static final String FEEDBACK_TASK_NOT_MODIFIED = "Task was not modified";
	private static final String FEEDBACK_TASK_COMPLETED = "Task Completed Successfully";
	private static final String FEEDBACK_UNDO_INVALID = "Invalid Undo!";
	private static final String FEEDBACK_UNDO_VALID = "Last Action Un-Done!";
	private static final String FEEDBACK_REDO_VALID = "Last Action Re-Done!";
	private static final String FEEDBACK_REDO_INVALID = "Invalid Redo!";
	private static final String FEEDBACK_EMPTY_STRING = "";
	private static final String FEEDBACK_SEARCH_VALID = "Search Found";
	private static final String FEEDBACK_SEARCH_INVALID = "Search Not Found";
	private static final String FEEDBACK_START_DATE_LATER_THAN_DEADLINE = "Start Date of Task cannot be later than Due Date of Task!";
	private static final String FEEDBACK_INSTANCE_START_DATE_EXCEEDS_DEADLINE = "Task starts and ends on same day. Start Time of Task cannot be later or equals to End Time of Task";

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

	private void setMostRecentTaskAdded(ArrayList<Task> addedList, int addedPosition) {
		recentAddedList = addedList;
		recentAddedPosition = addedPosition;
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

	private ArrayList<Task> getRecentAddedList() {
		return recentAddedList;
	}
	
	public ArrayList<Task> getSearchTasksList() {
		return searchTasks;
	}

	private int getRecentAddedPosition() {
		return recentAddedPosition;
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

	public void startExecution() {
		try {
			storage.loadToList();
			setFloatingTasksComplete(storage.getFloatingTasksComplete());
			setFloatingTasksToDo(storage.getFloatingTasksToDo());
			setScheduledTasksComplete(storage.getScheduledTasksComplete());
			setScheduledTasksToDo(storage.getScheduledTasksToDo());
			setScheduledTasksOverDue(storage.getScheduledTasksOverDue());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void executeCommand(String userInput) {
		try {
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
			storage.storeToFiles(getFloatingTasksToDo(), getFloatingTasksComplete(), getScheduledTasksToDo(),
					getScheduledTasksComplete(), getScheduledTasksOverDue());
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

		isSearchCommand = false;

		switch (executeCommand) {
		case ADD_TASK:
			addTask(executeTask, false);
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
			setFeedBack(FEEDBACK_EMPTY_STRING);
			break;
		case BLOCK_SLOT:
			blockTask(retrievedCommand);
			break;
		case HELP:
			// do the needful
			// incomplete
			setFeedBack(FEEDBACK_EMPTY_STRING);
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

	/*
	 * adds task based on the startdate, enddate, starttime and endtime for
	 * scheduled tasks into either scheduledtodo or scheduledoverdue arraylist
	 * while floating tasks are auto added into floatingtodo arraylist
	 */
	private int addTask(Task executeTask, boolean isUndoOperation) {

		int indexOfTask = -1;

		if (executeTask.isScheduledTask()) {
			indexOfTask = addTaskInOrder(executeTask);
		} else if (executeTask.isFloatingTask()) {
			floatingTasksToDo.add(executeTask);
			setMostRecentTaskAdded(floatingTasksToDo, floatingTasksToDo.size() - 1);
			indexOfTask = scheduledTasksOverDue.size() + scheduledTasksToDo.size() + floatingTasksToDo.size();
			setFeedBack(FEEDBACK_TASK_ADDED);
		}

		if (!isUndoOperation) {
			ArrayList<Task> taskList = new ArrayList<Task>();
			ArrayList<Integer> indexList = new ArrayList<Integer>();
			taskList.add(executeTask);
			indexList.add(indexOfTask);
			OldCommand recentCommand = new OldCommand(COMMAND_TYPE.ADD_TASK, taskList, indexList);
			historyObject.addToUndoList(recentCommand);
		}
		return indexOfTask;
	}

	private int addTaskInOrder(Task executeTask) {
		int position = -1;
		boolean overLapWithBlock = true;

		if (LocalDateTime.of(executeTask.getEndDate(), executeTask.getEndTime()).isBefore(LocalDateTime.now())) {
			position = sortTaskList(scheduledTasksOverDue, executeTask);
			scheduledTasksOverDue.add(position, executeTask);
			setMostRecentTaskAdded(scheduledTasksOverDue, position);
			setFeedBack(FEEDBACK_TASK_ADDED);
		} else {
			overLapWithBlock = compareWithBlockedRange(executeTask);
			if (overLapWithBlock == false) {
				position = sortTaskList(scheduledTasksToDo, executeTask);
				scheduledTasksToDo.add(position, executeTask);
				setMostRecentTaskAdded(scheduledTasksToDo, position);
				setFeedBack(FEEDBACK_TASK_ADDED);
				position = position + scheduledTasksOverDue.size();
			} else {
				setFeedBack("Task not added as it inteferes with blocked slot of "+blockedStartDateTime+"to "+blockedEndDateTime);
			}
		}

		return position + 1;
	}

	private void blockTask(Command retrievedCommand) {
		Task slotToBlock = retrievedCommand.getTaskDetails();
		blockedEndDateTime = LocalDateTime.of(slotToBlock.getEndDate(), slotToBlock.getEndTime());

		if (slotToBlock.getStartDate() != null) {
			blockedStartDateTime = LocalDateTime.of(slotToBlock.getStartDate(), slotToBlock.getStartTime());
		} else {
			blockedStartDateTime = LocalDateTime.now();
		}
		setFeedBack("Slot blocked from " + blockedStartDateTime + "to " + blockedEndDateTime);
	}

	private boolean compareWithBlockedRange(Task executeTask) {
		LocalDateTime taskEndDateTime = LocalDateTime.of(executeTask.getEndDate(), executeTask.getEndTime());
		LocalDateTime taskStartDateTime = null;

		if (executeTask.getStartDate() != null) {
			taskStartDateTime = LocalDateTime.of(executeTask.getStartDate(), executeTask.getStartTime());
		}
		if ((taskStartDateTime != null) && (taskEndDateTime.compareTo(blockedStartDateTime)>0) && (taskStartDateTime.compareTo(blockedEndDateTime)<0)) {
			return true;
		} else if ((taskStartDateTime == null) && (taskEndDateTime.compareTo(blockedStartDateTime)>0) && (taskEndDateTime.compareTo(blockedEndDateTime)<0)) {
			return true;
		} else {
			return false;
		}
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
				if ((selectedTaskStartDateTime != null) && (taskStartDateTime != null)) {
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
	private void deleteTask(ArrayList<Integer> taskDigit, boolean isUndoOperation) {
		// Creating undo parameter
		ArrayList<Task> taskList = new ArrayList<Task>();

		Task removedTask = null;

		if (taskDigit.isEmpty()) {
			ArrayList<Task> listToDelete = getRecentAddedList();
			int positionToDelete = getRecentAddedPosition();
			listToDelete.remove(positionToDelete);
			setFeedBack(FEEDBACK_TASK_DELETED);
		} else {
			for (int i = taskDigit.size() - 1; i >= 0; i--) {
				if (taskDigit.get(i) > 0) {
					if (taskDigit.get(i) <= scheduledTasksOverDue.size()) {
						removedTask = scheduledTasksOverDue.remove(taskDigit.get(i) - 1);
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);

						/* setScheduledTasksOverDue(scheduledTasksOverDue); */
					} else if (taskDigit.get(i) <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()) {
						removedTask = scheduledTasksToDo.remove(taskDigit.get(i) - 1 - scheduledTasksOverDue.size());
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);
						/* setScheduledTasksToDo(scheduledTasksToDo); */
					} else if (taskDigit.get(i) <= scheduledTasksToDo.size() + floatingTasksToDo.size()
					+ scheduledTasksOverDue.size()) {
						removedTask = floatingTasksToDo.remove(
								taskDigit.get(i) - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);
						/* setFloatingTasksToDo(floatingTasksToDo); */
					} else {
						setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
					}
				} else {
					setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM);
				}
			}

			// for undo functionality

			if (!isUndoOperation) {
				OldCommand recentCommand = new OldCommand(COMMAND_TYPE.DELETE_TASK, taskList, taskDigit);
				historyObject.addToUndoList(recentCommand);
			}
		}
	}

	public void editTask(ArrayList<Integer> indexList, String editInfo, boolean isUndoOperation) {
		Task taskToEdit = null;

		/*
		 * if (indexList == null ||indexList.isEmpty()) { ArrayList<Task>
		 * listToDelete = getRecentAddedList(); int positionToDelete =
		 * getRecentAddedPosition(); listToDelete.remove(positionToDelete);
		 * setFeedBack(FEEDBACK_TASK_DELETED); return; }
		 */
		int indexToEdit = indexList.get(0);

		if (indexToEdit > 0) {
			if (indexToEdit <= scheduledTasksOverDue.size()) {
				taskToEdit = scheduledTasksOverDue.remove(indexToEdit - 1);
			} else if (indexToEdit <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()) {
				taskToEdit = scheduledTasksToDo.remove(indexToEdit - 1 - scheduledTasksOverDue.size());
			} else if (indexToEdit <= scheduledTasksToDo.size() + floatingTasksToDo.size()
			+ scheduledTasksOverDue.size()) {
				taskToEdit = floatingTasksToDo
						.remove(indexToEdit - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
			} else {
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM + "\n");
			}
		}

		if (taskToEdit == null) {
			setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM + "\n" + FEEDBACK_TASK_NOT_MODIFIED);
		} else {
			Task editedTask = CommandParser.editExistingTask(taskToEdit, editInfo);
			int newIndex = addTask(editedTask, true);

			if (!isUndoOperation) {
				ArrayList<Task> taskList = new ArrayList<Task>();
				taskList.add(taskToEdit);
				taskList.add(editedTask);
				indexList.add(newIndex);
				OldCommand recentCommand = new OldCommand(COMMAND_TYPE.MODIFY_TASK, taskList, indexList);
				historyObject.addToUndoList(recentCommand);

			}
			setFeedBack(FEEDBACK_TASK_MODIFIED);
		}
	}

	/*
	 * adds task into the respective completeArrayList and removes that same
	 * task from the ArrayList that it is currently residing in based on task
	 * number entered by user
	 */
	private void completeTask(ArrayList<Integer> taskIndex, boolean isUndoOperation) {
		// undo parameter
		ArrayList<Task> taskList = new ArrayList<Task>();

		if (taskIndex.isEmpty()) {
			ArrayList<Task> listToComplete = getRecentAddedList();
			int positionToComplete = getRecentAddedPosition();

			if (listToComplete.get(positionToComplete).isScheduledTask()) {
				Task completedTask = markAsComplete(listToComplete, scheduledTasksComplete, positionToComplete);
				taskList.add(0, completedTask);
			} else if (listToComplete.get(positionToComplete).isFloatingTask()) {
				Task completedTask = markAsComplete(listToComplete, floatingTasksComplete, positionToComplete);
				taskList.add(0, completedTask);
			}
			setFeedBack(FEEDBACK_TASK_COMPLETED);
		} else {
			for (int i = taskIndex.size() - 1; i >= 0; i--) {
				int taskToComplete = taskIndex.get(i) - 1;
				if (taskToComplete >= 0) {
					if (taskToComplete < scheduledTasksOverDue.size()) {
						Task completedTask = markAsComplete(scheduledTasksOverDue, scheduledTasksComplete,
								taskToComplete);
						taskList.add(0, completedTask);
					} else if (taskToComplete < scheduledTasksToDo.size() + scheduledTasksOverDue.size()) {
						taskToComplete -= (scheduledTasksOverDue.size());
						Task completedTask = markAsComplete(scheduledTasksToDo, scheduledTasksComplete, taskToComplete);
						taskList.add(0, completedTask);
					} else if (taskToComplete < scheduledTasksOverDue.size() + scheduledTasksToDo.size()
					+ floatingTasksToDo.size()) {
						taskToComplete -= (scheduledTasksToDo.size() + scheduledTasksOverDue.size());
						Task completedTask = markAsComplete(floatingTasksToDo, floatingTasksComplete, taskToComplete);
						taskList.add(0, completedTask);
					} else {
						setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
					}
				} else {
					setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM);
				}
			}

			if (!isUndoOperation) {
				// for undo functionality
				OldCommand recentCommand = new OldCommand(COMMAND_TYPE.COMPLETE_TASK, taskList, taskIndex);
				historyObject.addToUndoList(recentCommand);
			}
		}
	}

	public Task markAsComplete(ArrayList<Task> incompleteList, ArrayList<Task> completeList, int taskNum) {
		Task completeTask = incompleteList.remove(taskNum);
		completeTask.setAsComplete();
		completeList.add(completeTask);
		setFeedBack(FEEDBACK_TASK_COMPLETED);
		return completeTask;
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
		scheduledTasksOverDue.add(scheduledTasksOverDue.size(), scheduledTasksToDo.get(i));
		setFeedBack("Task " + scheduledTasksToDo.get(i).getDescription() + " has exceeded deadline");
		scheduledTasksToDo.remove(i);
		/*
		 * setScheduledTasksToDo(scheduledTasksToDo);
		 * setScheduledTasksOverDue(scheduledTasksOverDue);
		 */
	}

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
				completeTask(toUndo.getIndexList(), true);
				break;
			case INCOMPLETE_TASK:
				markAsIncompleteList(toUndo.getTaskList(), toUndo.getIndexList());
				break;
			case MODIFY_TASK:
				/*
				 * For modify the Task list contains two tasks. The first one is
				 * the oldTask and the second one is the newTask. Delete new,
				 * add old.
				 */
				deleteSingleTask(toUndo.getIndexList().get(1), true);
				addTask(toUndo.getTaskList().get(0), true);
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
				completeTask(toRedo.getIndexList(), true);
				break;
			case INCOMPLETE_TASK:
				markAsIncompleteList(toRedo.getTaskList(), toRedo.getIndexList());
				break;
			case MODIFY_TASK:
				/*
				 * For modify the Task list contains two tasks. The first one is
				 * the oldTask and the second one is the newTask. Delete old,
				 * add new.
				 */
				deleteSingleTask(toRedo.getIndexList().get(0), true);
				addTask(toRedo.getTaskList().get(1), true);
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
			addTask(newTask, true);
		}
	}

	public void markAsIncompleteList(ArrayList<Task> taskList, ArrayList<Integer> indexList) {
		for (Task task : taskList) {
			if (task.isFloatingTask()) {
				floatingTasksComplete.remove(task);
				addTask(task, true);
			} else if (task.isScheduledTask()) {
				scheduledTasksComplete.remove(task);
				addTask(task, true);
			}
		}
	}

	public void deleteSingleTask(int index, boolean isUndoOperation) {
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
	}

	private void searchTask(Task taskToFind) {

		if(taskToFind.getDescription().equalsIgnoreCase("all")) {
			execute(Command.COMMAND_TYPE.HOME, null, null);
			return;
		}

		isSearchCommand = true;

		search_Snigdha obj = new search_Snigdha();
		searchTasks = obj.searchTask(taskToFind);

		if(searchTasks.size() > 0){
			setFeedBack(FEEDBACK_SEARCH_VALID);
		}
		else{
			setFeedBack(FEEDBACK_SEARCH_INVALID);
		}

		setFeedBack(FEEDBACK_EMPTY_STRING);
	}

	public boolean hasSearchList() {
		return isSearchCommand;
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
