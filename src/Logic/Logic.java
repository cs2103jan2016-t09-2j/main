
package Logic;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalDateTime;

import ScheduleHacks.Task;
import ScheduleHacks.OldCommand;
import ScheduleHacks.OldCommand.COMMAND_TYPE;
import ScheduleHacks.History;
import ScheduleHacks.HelpGuide;
import Parser.CommandParser;
import Parser.TimeParser;
import Parser.Command;
import GUI.BottomBottom;
import GUI.TempCLI;
import GUI.TopLeftPanel;
import GUI.TopRightPanel;
import Storage.Storage;

public class Logic {

	private String feedBack;
	private boolean isSearchCommand;
	private boolean isHomeScreen;
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
	private Integer recentIndex;
	
	private static final String FEEDBACK_INVALID_COMMAND = "Invalid Command!";
	private static final String FEEDBACK_INVALID_COMMAND_TYPE = "Invalid command type entered!";
	private static final String FEEDBACK_TASK_ADDED = "Task added successfully";
	private static final String FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_ADDING = "Task entered by user already exists! Task not added!";
	private static final String FEEDBACK_TASK_ADDED_BUT_ENCOUNTERED_CONFLICTS = "Task added successfully but task is conflicting with several existing tasks!";
	private static final String FEEDBACK_TASK_DELETED = "Task Deleted Successfully";
	private static final String FEEDBACK_NON_EXISTENT_TASK_NUM = "Task number entered was not found!";
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

			/*
			 * executeCommand("view 2days"); setFeedBack(
			 * "Welcome to Schedule Hacks! \n"); setFeedBack(
			 * "Tasks Due Today and Tomorrow Displayed \n");
			 * 
			 * // remove if not needed /* for (int i = 0; i <
			 * scheduledTasksOverDue.size(); i++) {
			 * addTask(scheduledTasksOverDue.remove(i), true); }
			 */

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
			System.out.print(1);
			Command existingCommand = CommandParser.getParsedCommand(originalDescription);
			System.out.print(2);
			typeCommand = getCommand(existingCommand);
			System.out.print(3);
			Task getTaskToExecute = getTaskDescription(existingCommand);
			System.out.print(4);
			for (int i=0; i<scheduledTasksOverDue.size(); i++) {
				addTask(scheduledTasksOverDue.remove(i), true);
			}
			execute(typeCommand, existingCommand, getTaskToExecute);
			trackConflictingTasks.clear();
			autoChangeTaskStatus();
			storage.storeToFiles(getFloatingTasksToDo(), getFloatingTasksComplete(), getScheduledTasksToDo(),
					getScheduledTasksComplete(), getScheduledTasksOverDue());
		} catch (Exception e) {
			//e.printStackTrace();
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
		isHomeScreen = false;
		isHighlightOperation = false;

		switch (executeCommand) {
		case ADD_TASK:
			addTask(executeTask, false);
			historyObject.clearRedoStack();
			isHighlightOperation = true;
			break;
		case DELETE_TASK:
			deleteTask(retrievedCommand.getIndexList(), false);
			historyObject.clearRedoStack();
			break;
		case MODIFY_TASK:
			editTask(retrievedCommand.getIndexList(), executeTask.getDescription(), false);
			historyObject.clearRedoStack();
			isHighlightOperation = true;
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
		// ArrayList<String> helpInstruction = (new
		// HelpGuide()).getCollatedList();
		// display help guide
	}

	/*
	 * adds task based on the startdate, enddate, starttime and endtime for
	 * scheduled tasks into either scheduledtodo or scheduledoverdue arraylist
	 * while floating tasks are auto added into floatingtodo arraylist
	 */
	private int addTask(Task executeTask, boolean isUndoOperation) {
		int indexOfTask = -1;

	if(executeTask.getDescription()==null ||executeTask.getDescription().isEmpty() ) {
			setFeedBack(FEEDBACK_EMPTY_TASK_DESCRIPTION);
			return indexOfTask;
		}
		indexOfTask = duplicationCheckProcedures(executeTask);
		setRecentIndexOfTask(indexOfTask);
		
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

		if (LocalDateTime.of(executeTask.getEndDate(), executeTask.getEndTime()).isBefore(LocalDateTime.now())) {
			int result = standardAddScheduledTaskProcedures(scheduledTasksOverDue, executeTask, position);
			position = result;
		} else {
			int result = standardAddScheduledTaskProcedures(scheduledTasksToDo, executeTask, position);
			position = result + scheduledTasksOverDue.size();
		}
		return position + 1;
	}
	
	private int standardAddScheduledTaskProcedures(ArrayList<Task> scheduledTaskList, Task taskAdd, int positionToAdd) {
		boolean overLapWithTask = true;
		
		positionToAdd = sortTaskList(scheduledTaskList, taskAdd);
		overLapWithTask = compareWithScheduledTasks(taskAdd, scheduledTaskList);
		scheduledTaskList.add(positionToAdd, taskAdd);
		if (overLapWithTask) {
			if (trackConflictingTasks.size() == 1) {
				setFeedBack(FEEDBACK_TASK_ADDED + " but new task is conflicting with " +trackConflictingTasks.get(0).getDescription());
			}
			else {
				setFeedBack(FEEDBACK_TASK_ADDED_BUT_ENCOUNTERED_CONFLICTS);
			}
		} else {
			setFeedBack(FEEDBACK_TASK_ADDED);
		}
		return positionToAdd;
	}
	
	private int duplicationCheckProcedures(Task currentTask) {
		ArrayList<Boolean> duplicate = new ArrayList<Boolean> ();
		boolean checkForDuplication = true;
		int currentIndexOfTask = -1;
		
		if (currentTask.isScheduledTask()) {
			checkForDuplication = checkForScheduledDuplication(currentTask, scheduledTasksOverDue);
			duplicate.add(checkForDuplication);
			checkForDuplication = checkForScheduledDuplication(currentTask, scheduledTasksToDo);
			duplicate.add(checkForDuplication);
		} else if (currentTask.isFloatingTask()) {
			checkForDuplication = checkForFloatingDuplication(currentTask, floatingTasksToDo);
			duplicate.add(checkForDuplication);
		}
		
		if (currentTask.isScheduledTask()) {
			for (int i=0; i<2; i++) {
				if (duplicate.size() == 1) {
					if (duplicate.get(0) == false) {
						System.out.print(13);
						currentIndexOfTask = addTaskInOrder(currentTask);
						} else if (duplicate.get(0)) {
							setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_ADDING);
						}
				} else {
					if (duplicate.get(i)) {
						setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_ADDING);
						break;
					} else if (duplicate.get(i) == false) {
						duplicate.remove(i);
					}
				}
			}
		} else if (currentTask.isFloatingTask()) {
			if (duplicate.get(0) == false) {
				floatingTasksToDo.add(currentTask);
				currentIndexOfTask = scheduledTasksOverDue.size() + scheduledTasksToDo.size() + floatingTasksToDo.size();
				setFeedBack(FEEDBACK_TASK_ADDED);
			} else if (duplicate.get(0)) {
				setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_ADDING);
			}
		}
		duplicate.clear();
		return currentIndexOfTask;
	}
	
	private boolean checkForScheduledDuplication(Task relevantTask, ArrayList<Task> scheduledTasks) {
		for (int i=0; i<scheduledTasks.size(); i++) {
			int tracker = 0;
			if (relevantTask.getDescription().equals(scheduledTasks.get(i).getDescription())) {
				tracker++;
			}
			if (((relevantTask.getStartDate() != null) && (scheduledTasks.get(i).getStartDate() != null) && 
					(relevantTask.getStartDate().equals(scheduledTasks.get(i).getStartDate()))) || 
					(relevantTask.getStartDate() == null) && (scheduledTasks.get(i).getStartDate() == null)) {
				tracker++;
			}
			if (((relevantTask.getStartTime() != null) && (scheduledTasks.get(i).getStartTime() != null) && 
					(relevantTask.getStartTime().equals(scheduledTasks.get(i).getStartTime()))) || 
					(relevantTask.getStartTime() == null) && (scheduledTasks.get(i).getStartTime() == null)) {
				tracker++;
			}
			if (relevantTask.getEndDate().equals(scheduledTasks.get(i).getEndDate())) {
				tracker++;
			}
			if (((relevantTask.getEndTime() != null) && (scheduledTasks.get(i).getEndTime() != null) && 
			(relevantTask.getEndTime().equals(scheduledTasks.get(i).getEndTime()))) || 
			(relevantTask.getEndTime() == null) && (scheduledTasks.get(i).getEndTime() == null)){
				tracker++;
			}
			if (tracker == 5) {
				return true;
			}
		}
		return false;
	}
	
	private boolean checkForFloatingDuplication(Task relevantTask, ArrayList<Task> floatingTasks) {
		for (int i=0; i<floatingTasks.size(); i++) {
			if (relevantTask.getDescription().equals(floatingTasks.get(i).getDescription())) {
				return true;
			}
		}
		return false;
	}

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
			
		for (int i=0; i<relevantTaskList.size(); i++) {
			relevantTaskEndDateTime = LocalDateTime.of(relevantTaskList.get(i).getEndDate(), relevantTaskList.get(i).getEndTime());
			if (relevantTaskList.get(i).getStartDate() != null) {
				relevantTaskStartDateTime = LocalDateTime.of(relevantTaskList.get(i).getStartDate(), relevantTaskList.get(i).getStartTime());
			}
			
			if (relevantTaskStartDateTime != null) {
				if ((taskStartDateTime != null) && (taskEndDateTime.isAfter(relevantTaskStartDateTime)) && (taskStartDateTime.isBefore(relevantTaskEndDateTime))) {
					trackBlock++;
					noteConflictingTask(relevantTaskList.get(i));
				} else if ((taskStartDateTime == null) && (taskEndDateTime.isAfter(relevantTaskStartDateTime)) && (taskEndDateTime.isBefore(relevantTaskEndDateTime))) {
					trackBlock++;
					noteConflictingTask(relevantTaskList.get(i));
				}
			} else if (relevantTaskStartDateTime == null) {
				if ((taskStartDateTime != null) && (taskEndDateTime.isAfter(relevantTaskEndDateTime)) && (taskStartDateTime.isBefore(relevantTaskEndDateTime))) {
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
	
	private void noteConflictingTask(Task conflict) {
		trackConflictingTasks.add(conflict);
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

			if (taskEndDateTime.isBefore(selectedTaskEndDateTime)) {
				return i;
			} else if (taskEndDateTime.isEqual(selectedTaskEndDateTime)) {
				if ((selectedTaskStartDateTime != null) && (taskStartDateTime != null)) {
					if (taskStartDateTime.isBefore(selectedTaskStartDateTime)) {
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
		int lastAddedIndex = -1;

		if (taskDigit == null){
			lastAddedIndex = getRecentIndexOfTask();
			deleteSingleTask(lastAddedIndex, true);
		} else {
			for (int i = taskDigit.size() - 1; i >= 0; i--) {
				if (taskDigit.get(i) > 0) {
					if (taskDigit.get(i) <= scheduledTasksOverDue.size()) {
						removedTask = scheduledTasksOverDue.remove(taskDigit.get(i) - 1);
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);
					} else if (taskDigit.get(i) <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()) {
						removedTask = scheduledTasksToDo.remove(taskDigit.get(i) - 1 - scheduledTasksOverDue.size());
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);

					} else if (taskDigit.get(i) <= scheduledTasksToDo.size() + floatingTasksToDo.size()
							+ scheduledTasksOverDue.size()) {
						removedTask = floatingTasksToDo.remove(
								taskDigit.get(i) - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);

					} else if (taskDigit.get(i) <= scheduledTasksToDo.size() + floatingTasksToDo.size()
							+ scheduledTasksOverDue.size() + scheduledTasksComplete.size()) {
						removedTask = scheduledTasksComplete.remove(taskDigit.get(i) - 1 - scheduledTasksToDo.size()
								- scheduledTasksOverDue.size() - floatingTasksToDo.size());
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);
					} else if (taskDigit.get(i) <= scheduledTasksToDo.size() + floatingTasksToDo.size()
							+ scheduledTasksOverDue.size() + scheduledTasksComplete.size()
							+ floatingTasksComplete.size()) {
						removedTask = floatingTasksComplete
								.remove(taskDigit.get(i) - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size()
										- floatingTasksToDo.size() - scheduledTasksComplete.size());
						taskList.add(0, removedTask);
						setFeedBack(FEEDBACK_TASK_DELETED);
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
		Task taskOriginal = null;
		int indexToEdit = -1;
		boolean duplicatedScheduledOverDue = false, duplicated = false;

		/*
		 * if (indexList == null ||indexList.isEmpty()) { ArrayList<Task>
		 * listToDelete = getRecentAddedList(); int positionToDelete =
		 * getRecentAddedPosition(); listToDelete.remove(positionToDelete);
		 * setFeedBack(FEEDBACK_TASK_DELETED); return; }
		 */
		if (indexList == null) {
			indexToEdit = getRecentIndexOfTask();
		} else {
			indexToEdit = indexList.get(0);
			System.out.print(indexToEdit);
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
				setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM + "\n");
			}
		}

		if (taskToEdit == null) {
			setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM + "\n" + FEEDBACK_TASK_NOT_MODIFIED);
		} else {
			Task editedTask = CommandParser.editExistingTask(taskToEdit, editInfo);	
			if(editedTask.isScheduledTask()) {
				duplicatedScheduledOverDue = checkForScheduledDuplication(editedTask, scheduledTasksOverDue);
				duplicated = checkForScheduledDuplication(editedTask, scheduledTasksToDo);
					
				if ((duplicatedScheduledOverDue) || (duplicated)) {
					editedTask = taskOriginal;
					setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_EDITING);
				}
			} else if (editedTask.isFloatingTask()) {
				duplicated = checkForFloatingDuplication(editedTask, floatingTasksToDo);
					
				if (duplicated) {
					editedTask = taskOriginal;
					setFeedBack(FEEDBACK_DUPLICATE_TASK_FOUND_WHEN_EDITING);
				}
			}
			if ((duplicated == false) && (duplicatedScheduledOverDue == false)){
				setFeedBack(FEEDBACK_TASK_MODIFIED);
			}
			int newIndex = addTask(editedTask, true);
			
			if (getFeedBack().equals(FEEDBACK_TASK_ADDED)) {
				setFeedBack(FEEDBACK_TASK_MODIFIED);
			} else if (trackConflictingTasks.size() == 1) {
				setFeedBack(FEEDBACK_TASK_MODIFIED + " but new task is conflicting with " +trackConflictingTasks.get(0).getDescription());
			} else {
				setFeedBack(FEEDBACK_TASK_MODIFIED_BUT_ENCOUNTERED_CONFLICTS);
			}
			if (!isUndoOperation) {
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
	 * number entered by user
	 */
	private void completeTask(ArrayList<Integer> taskIndex, boolean isUndoOperation) {
		// undo parameter
		ArrayList<Task> taskList = new ArrayList<Task>();
		int indexToComplete = -1;

		if (taskIndex.isEmpty()) {
			indexToComplete = getRecentIndexOfTask();
			taskIndex.add(indexToComplete);
		}
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

	public Task markAsComplete(ArrayList<Task> incompleteList, ArrayList<Task> completeList, int taskNum) {
		Task completeTask = incompleteList.remove(taskNum);
		completeTask.setAsComplete();
		completeList.add(completeTask);
		setFeedBack(FEEDBACK_TASK_COMPLETED);
		return completeTask;
	}

	public void incompleteTask(ArrayList<Integer> indexList, boolean isUndoOperation) {
		// undo parameter
		ArrayList<Task> taskList = new ArrayList<Task>();

		boolean isAborted = false;

		int minIndex = scheduledTasksOverDue.size() + scheduledTasksToDo.size() + floatingTasksToDo.size() + 1;
		int maxIndex = minIndex + scheduledTasksComplete.size() + floatingTasksComplete.size() - 1;

		// System.out.println(minIndex+"*"+maxIndex);

		if (indexList != null && !indexList.isEmpty()) {
			for (int index = indexList.size() - 1; index >= 0; index--) {
				if ((indexList.get(index) >= minIndex) && (indexList.get(index) <= maxIndex)) {
					if (indexList.get(index) < minIndex + scheduledTasksComplete.size()) {
						Task taskToMark = scheduledTasksComplete.get(indexList.get(index) - minIndex);
						markTaskIncomplete(taskToMark);
						taskList.add(taskToMark);
					} else {
						Task taskToMark = floatingTasksComplete
								.get(indexList.get(index) - minIndex - scheduledTasksComplete.size());
						markTaskIncomplete(taskToMark);
						taskList.add(taskToMark);
					}
					setFeedBack(FEEDBACK_TASK_INCOMPLETED);
				} else {
					isAborted = true;
					setFeedBack(FEEDBACK_TASK_INCOMPLETED_INVALID);
				}
			}
			if (!isUndoOperation) {
				// for undo functionality
				OldCommand recentCommand = new OldCommand(COMMAND_TYPE.INCOMPLETE_TASK, taskList, indexList);
				historyObject.addToUndoList(recentCommand);
			}

			if (isAborted && !taskList.isEmpty()) {
				undoTask();
				setFeedBack(FEEDBACK_TASK_INCOMPLETED_INVALID);
			}
		}
	}

	public void markTaskIncomplete(Task task) {
		task.setAsIncomplete();
		if (task.isFloatingTask()) {
			floatingTasksComplete.remove(task);
			addTask(task, true);
		} else if (task.isScheduledTask()) {
			scheduledTasksComplete.remove(task);
			addTask(task, true);
		}
	}

	/*
	 * this method shd be called in main at the start of the prog and shd run
	 * all the way till system exits. This method automatically shifts
	 * scheduledtaskstodo to scheduledtasksoverdue when date and time has
	 * exceeded due date and due time specified for scheduled task
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

	private void changeStatusToOverdue(int i) {
		scheduledTasksOverDue.add(scheduledTasksToDo.get(i));
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
			markTaskIncomplete(task);
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
		setFeedBack(FEEDBACK_TASK_DELETED);
	}


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
			/*
			 * for(Task task: searchTaskList)
			 * System.out.println(task.getDescription());
			 */
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
