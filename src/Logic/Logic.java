package Logic;

import java.util.ArrayList;
import java.time.LocalDateTime;

import ScheduleHacks.Task;
import Parser.CommandParser;
import Parser.Command;
import GUI.TempCLI;
import Storage.Storage;

public class Logic {

	private String feedBack;

	private static Logic logicObject = null;

	static Storage storage = Storage.getInstance();

	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	private ArrayList<Task> searchedTasks = new ArrayList<Task>();
	private ArrayList<Task> recentAddedList = new ArrayList<Task>();
	private int recentAddedPosition;

	private static final String FEEDBACK_INVALID_COMMAND = "Invalid Command!";
	private static final String FEEDBACK_INVALID_COMMAND_TYPE = "Invalid command type entered!";
	private static final String FEEDBACK_TASK_ADDED = "Task Added Successfully";
	private static final String FEEDBACK_TASK_NOT_ADDED = "New task being added is overlapping with existing task's timeline to a severe extent! Task not added!";
	private static final String FEEDBACK_TASK_DELETED = "Task Deleted Successfully";
	private static final String FEEDBACK_NON_EXISTENT_TASK_NUM = "Task number entered was not found!";
	private static final String FEEDBACK_NEGATIVE_TASK_NUM = "Task number entered cannot be 0 or negative!";
	private static final String FEEDBACK_TASK_NUM_NOT_FOUND = "Task number is not present! Task not found!";
	private static final String FEEDBACK_TASK_MODIFIED = "Task Edited Successfully";
	private static final String FEEDBACK_TASK_NOT_MODIFIED = "Task was not modified";
	private static final String FEEDBACK_TASK_COMPLETED = "Task Completed Successfully";
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

	private void setSearchedTasks(ArrayList<Task> currentTaskList) {
		searchedTasks.clear();
		searchedTasks = currentTaskList;
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

	private int getRecentAddedPosition() {
		return recentAddedPosition;
	}

	public ArrayList<Task> getSearchedTasks() {
		return searchedTasks;
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
		try {
			storage.loadToList();
			setFloatingTasksComplete(storage.getFloatingTasksComplete());
			setFloatingTasksToDo(storage.getFloatingTasksToDo());
			setScheduledTasksComplete(storage.getScheduledTasksComplete());
			setScheduledTasksToDo(storage.getScheduledTasksToDo());
			setScheduledTasksOverDue(storage.getScheduledTasksOverDue());
			retrieveParsedCommand(userInput);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
		switch (executeCommand) {
		case ADD_TASK:
			addTask(executeTask);
			break;
		case DELETE_TASK:
			deleteTask(retrievedCommand);
			break;
		case MODIFY_TASK:
			// modifyTask(executeTask, retrievedCommand);
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
		case VIEW_LIST:
			searchTask(executeTask);
			break;
		case SEARCH_TASK:
			searchTask(executeTask);
			break;
		case SET_DIRECTORY:
			setNewDirectoryPath(executeTask);
			break;
		case EXIT:
			exit();
			break;
		}
	}

	public void setNewDirectoryPath(Task executeTask) {
		try {
		storage.setCurrentPathName(executeTask.getDescription());
		} catch(Exception e) {
			System.out.println("Invalid Directory Path!!");
		}
	}
	
	/*
	 * adds task based on the startdate, enddate, starttime and endtime for
	 * scheduled tasks into either scheduledtodo or scheduledoverdue arraylist
	 * while floating tasks are auto added into floatingtodo arraylist
	 */
	private void addTask(Task executeTask) {
		if (executeTask.isScheduledTask()) {
			addTaskInOrder(executeTask);
		} else if (executeTask.isFloatingTask()) {
			floatingTasksToDo.add(executeTask);
			setMostRecentTaskAdded(floatingTasksToDo, floatingTasksToDo.size() - 1);
			setFeedBack(FEEDBACK_TASK_ADDED);
		}
	}

	private void addTaskInOrder(Task executeTask) {
		int position;
		boolean outcome = false;

		if (LocalDateTime.of(executeTask.getEndDate(), executeTask.getEndTime()).isBefore(LocalDateTime.now())) {
			position = sortTaskList(scheduledTasksOverDue, executeTask);
			scheduledTasksOverDue.add(position, executeTask);
			setMostRecentTaskAdded(scheduledTasksOverDue, position);
			setFeedBack(FEEDBACK_TASK_ADDED);
		} else {
			outcome = addTaskConsideration(scheduledTasksToDo, executeTask);
			if (outcome == true) {
				position = sortTaskList(scheduledTasksToDo, executeTask);
				scheduledTasksToDo.add(position, executeTask);
				setMostRecentTaskAdded(scheduledTasksToDo, position);
				setFeedBack(FEEDBACK_TASK_ADDED);
			} else {
				setFeedBack(FEEDBACK_TASK_NOT_ADDED);
			}
		}
	}

	private boolean addTaskConsideration(ArrayList<Task> listOfTasks, Task taskToAdd) {
		Long timeClashInMins = 0L;
		int tracker = 0;
		ArrayList<Integer> result = new ArrayList<Integer>();
		ArrayList<Integer> trackPosition = new ArrayList<Integer>();
		ArrayList<Integer> trackClashedTimings = new ArrayList<Integer>();

		for (int i = 0; i < listOfTasks.size(); i++) {
			LocalDateTime taskDueDateTime = LocalDateTime.of(listOfTasks.get(i).getEndDate(),
					listOfTasks.get(i).getEndTime());

			if (listOfTasks.get(i).getStartDate() != null) {
				LocalDateTime taskCommencingDateTime = LocalDateTime.of(listOfTasks.get(i).getStartDate(),
						listOfTasks.get(i).getStartTime());
				Long periodInMins = java.time.Duration.between(taskCommencingDateTime, taskDueDateTime).toMinutes();

				if (taskToAdd.getStartDate() != null) {
					LocalDateTime addingTaskStartDateTime = LocalDateTime.of(taskToAdd.getStartDate(),
							taskToAdd.getStartTime());
					LocalDateTime addingTaskDueDateTime = LocalDateTime.of(taskToAdd.getEndDate(),
							taskToAdd.getEndTime());
					Long newTaskPeriodInMins = java.time.Duration
							.between(addingTaskStartDateTime, addingTaskDueDateTime).toMinutes();

					if (((addingTaskDueDateTime.compareTo(taskCommencingDateTime)) > 0)
							&& ((addingTaskDueDateTime.compareTo(taskDueDateTime)) <= 0)) {
						if (addingTaskStartDateTime.compareTo(taskCommencingDateTime) <= 0) {
							tracker++;
							if ((listOfTasks.get(i).getOverLapped()) == false) {
								timeClashInMins = java.time.Duration
										.between(taskCommencingDateTime, addingTaskDueDateTime).toMinutes();
								result.add(compareClashInTasks(periodInMins.intValue(), newTaskPeriodInMins.intValue(),
										timeClashInMins.intValue()));
								if (result.get(result.size() - 1) == 1) {
									trackPosition.add(i);
									trackClashedTimings.add(timeClashInMins.intValue());
									tracker++;
								}
							}
						} else if (addingTaskStartDateTime.compareTo(taskCommencingDateTime) > 0) {
							tracker++;
							if ((listOfTasks.get(i).getOverLapped()) == false) {
								timeClashInMins = java.time.Duration
										.between(addingTaskStartDateTime, addingTaskDueDateTime).toMinutes();
								result.add(compareClashInTasks(periodInMins.intValue(), newTaskPeriodInMins.intValue(),
										timeClashInMins.intValue()));
								if (result.get(result.size() - 1) == 1) {
									trackPosition.add(i);
									trackClashedTimings.add(timeClashInMins.intValue());
									tracker++;
								}
							}
						}
					} else if (((addingTaskDueDateTime.compareTo(taskCommencingDateTime)) > 0)
							&& ((addingTaskDueDateTime.compareTo(taskDueDateTime)) > 0)) {
						if (addingTaskStartDateTime.compareTo(taskCommencingDateTime) <= 0) {
							tracker++;
							if ((listOfTasks.get(i).getOverLapped()) == false) {
								timeClashInMins = java.time.Duration.between(taskCommencingDateTime, taskDueDateTime)
										.toMinutes();
								result.add(compareClashInTasks(periodInMins.intValue(), newTaskPeriodInMins.intValue(),
										timeClashInMins.intValue()));
								if (result.get(result.size() - 1) == 1) {
									trackPosition.add(i);
									trackClashedTimings.add(timeClashInMins.intValue());
									tracker++;
								}
							}
						} else if (((addingTaskStartDateTime.compareTo(taskCommencingDateTime)) > 0)
								&& ((addingTaskStartDateTime.compareTo(taskDueDateTime)) < 0)) {
							tracker++;
							if ((listOfTasks.get(i).getOverLapped()) == false) {
								timeClashInMins = java.time.Duration.between(addingTaskStartDateTime, taskDueDateTime)
										.toMinutes();
								result.add(compareClashInTasks(periodInMins.intValue(), newTaskPeriodInMins.intValue(),
										timeClashInMins.intValue()));
								if (result.get(result.size() - 1) == 1) {
									trackPosition.add(i);
									trackClashedTimings.add(timeClashInMins.intValue());
									tracker++;
								}
							}
						}
					}
				}
			}
		}
		if ((result.size() == 1) && (result.get(0) == 1) && (tracker == 2)) {
			scheduledTasksToDo.get(trackPosition.get(0)).setAsOverLapped(true);
			taskToAdd.setAsOverLapped(true);
			return true;
		} else if ((result.size() == 0) && (tracker == 0)) {
			return true;
		} else {
			return false;
		}
	}

	private int compareClashInTasks(int existingTaskDuration, int newTaskDuration, int timeClash) {

		if ((existingTaskDuration <= 60) || (newTaskDuration <= 60)) {
			if (timeClash > 0) {
				return -1;
			} else {
				return 1;
			}
		} else if ((existingTaskDuration <= 120) || (newTaskDuration <= 120)) {
			if ((timeClash >= 0) && (timeClash <= 30)) {
				return 1;
			} else {
				return -1;
			}
		} else if ((existingTaskDuration <= 180) || (newTaskDuration <= 180)) {
			if ((timeClash >= 0) && (timeClash <= 60)) {
				return 1;
			} else {
				return -1;
			}
		} else if ((existingTaskDuration <= 240) || (newTaskDuration <= 240)) {
			if ((timeClash >= 0) && (timeClash <= 90)) {
				return 1;
			} else {
				return -1;
			}
		} else {
			return 1;
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
	private void deleteTask(Command retrievedCommand) {
		ArrayList<Integer> taskDigit = retrievedCommand.getIndexList();

		if (taskDigit.isEmpty()) {
			ArrayList<Task> listToDelete = getRecentAddedList();
			int positionToDelete = getRecentAddedPosition();
			listToDelete.remove(positionToDelete);
			setFeedBack(FEEDBACK_TASK_DELETED);
		} else {
			for (int i = taskDigit.size() - 1; i >= 0; i--) {
				if (taskDigit.get(i) > 0) {
					if (taskDigit.get(i) <= scheduledTasksOverDue.size()) {
						scheduledTasksOverDue.remove(taskDigit.get(i) - 1);
						setFeedBack(FEEDBACK_TASK_DELETED);
						/* setScheduledTasksOverDue(scheduledTasksOverDue); */
					} else if (taskDigit.get(i) <= scheduledTasksOverDue.size() + scheduledTasksToDo.size()) {
						if ((scheduledTasksToDo.get(taskDigit.get(i) - 1 - scheduledTasksOverDue.size())
								.getOverLapped()) == true) {
							int keepTrack = 0;
							for (int j = 0; j <= (taskDigit.get(i) - 1 - scheduledTasksOverDue.size()); j++) {
								if ((scheduledTasksToDo.get(j).getOverLapped()) == true) {
									keepTrack++;
								}
							}
							if ((keepTrack % 2) == 0) {
								scheduledTasksToDo.get(taskDigit.get(i) - 2 - scheduledTasksOverDue.size())
										.setAsOverLapped(false);
							} else if ((keepTrack % 2) == 1) {
								scheduledTasksToDo.get(taskDigit.get(i) - scheduledTasksOverDue.size())
										.setAsOverLapped(false);
							}
						}
						scheduledTasksToDo.remove(taskDigit.get(i) - 1 - scheduledTasksOverDue.size());
						setFeedBack(FEEDBACK_TASK_DELETED);
						/* setScheduledTasksToDo(scheduledTasksToDo); */
					} else if (taskDigit.get(i) <= scheduledTasksToDo.size() + floatingTasksToDo.size()
							+ scheduledTasksOverDue.size()) {
						floatingTasksToDo.remove(
								taskDigit.get(i) - 1 - scheduledTasksToDo.size() - scheduledTasksOverDue.size());
						setFeedBack(FEEDBACK_TASK_DELETED);
						/* setFloatingTasksToDo(floatingTasksToDo); */
					} else {
						setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
					}
				} else {
					setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM);
				}
			}
		}
	}

	/*
	 * modifies scheduledTasksToDo/floatingTasksToDo by looking at class number.
	 * Edits corresponding task based on description, date or time
	 */
	/*
	 * private void modifyTask(Task executeTask, Command retrievedCommand) {
	 * ArrayList <Integer> taskDigitToModify = retrievedCommand.getIndexList();
	 * int maxIndex = scheduledTasksOverDue.size() + scheduledTasksToDo.size() +
	 * floatingTasksToDo.size();
	 * 
	 * if (taskDigitToModify == null) { ArrayList<Task> listToModify =
	 * getRecentAddedList(); int positionToModify = getRecentAddedPosition(); }
	 * else { for (int i=0; i<taskDigitToModify.size(); i++) { int modifyTaskNum
	 * = taskDigitToModify.get(i) - 1; if (modifyTaskNum >= 0 && modifyTaskNum <
	 * maxIndex) { if (modifyTaskNum < scheduledTasksOverDue.size()) {
	 * modifyScheduledTask(executeTask, modifyTaskNum, scheduledTasksOverDue); }
	 * else if (modifyTaskNum < scheduledTasksToDo.size() +
	 * scheduledTasksOverDue.size()) { modifyTaskNum -=
	 * scheduledTasksOverDue.size(); modifyScheduledTask(executeTask,
	 * modifyTaskNum, scheduledTasksToDo); } else { modifyTaskNum -=
	 * (scheduledTasksOverDue.size() + scheduledTasksToDo.size());
	 * modifyFloatingTasksToDo(executeTask, modifyTaskNum); } } else if
	 * (modifyTaskNum >= maxIndex) { setFeedBack(FEEDBACK_TASK_NUM_NOT_FOUND); }
	 * else { setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM); } } } }
	 * 
	 * private void modifyScheduledTask(Task newTask, int taskNum,
	 * ArrayList<Task> taskList) { Task oldTask = taskList.remove(taskNum);
	 * 
	 * if (newTask.getDescription() != null) {
	 * oldTask.setDescription(newTask.getDescription()); } if
	 * (newTask.getEndDate() != null) {
	 * oldTask.setEndDate(newTask.getEndDate()); } if (newTask.getEndTime() !=
	 * null) { oldTask.setEndTime(newTask.getEndTime()); } if
	 * (newTask.getStartDate() != null) {
	 * oldTask.setStartDate(newTask.getStartDate()); } if
	 * (newTask.getStartTime() != null) {
	 * oldTask.setStartTime(newTask.getStartTime()); }
	 * 
	 * addTaskInOrder(oldTask); setFeedBack(FEEDBACK_TASK_MODIFIED);
	 * 
	 * if ((newTask.getDescription() == null) && (newTask.getEndDate() == null)
	 * && (newTask.getEndTime() == null) && (newTask.getStartDate() == null) &&
	 * (newTask.getStartTime() == null)) {
	 * setFeedBack(FEEDBACK_TASK_NOT_MODIFIED); }
	 * 
	 * }
	 * 
	 * private void modifyFloatingTasksToDo(Task executeTask, int taskNum) {
	 * Task taskToEdit = floatingTasksToDo.get(taskNum);
	 * 
	 * if (executeTask.getDescription() != null) {
	 * taskToEdit.setDescription(executeTask.getDescription()); } if
	 * (executeTask.getEndDate() != null) {
	 * taskToEdit.setEndDate(executeTask.getEndDate()); } if
	 * (executeTask.getEndTime() != null) {
	 * taskToEdit.setEndTime(executeTask.getEndTime()); } if
	 * (executeTask.getStartDate() != null) {
	 * taskToEdit.setStartDate(executeTask.getStartDate()); } if
	 * (executeTask.getStartTime() != null) {
	 * taskToEdit.setStartTime(executeTask.getStartTime()); }
	 * 
	 * if ((executeTask.getDescription() == null) && (executeTask.getEndDate()
	 * == null) && (executeTask.getEndTime() == null) &&
	 * (executeTask.getStartDate() == null) && (executeTask.getStartTime() ==
	 * null)) { setFeedBack(FEEDBACK_TASK_NOT_MODIFIED); }
	 * 
	 * else if
	 * ((taskToEdit.getDescription().equals(executeTask.getDescription())) &&
	 * ((taskToEdit.getEndDate()) == null) && ((taskToEdit.getEndTime()) ==
	 * null) && ((taskToEdit.getStartDate()) == null) &&
	 * ((taskToEdit.getStartTime()) == null)) {
	 * setFeedBack(FEEDBACK_TASK_MODIFIED);
	 * //setFloatingTasksToDo(floatingTasksToDo) } else { taskToEdit =
	 * CommandParser.convertFloatingToScheduled(taskToEdit);
	 * changeFloatingToScheduled(taskToEdit); } }
	 * 
	 * private void changeFloatingToScheduled(Task taskToModify) {
	 * floatingTasksToDo.remove(taskToModify); addTaskInOrder(taskToModify);
	 * setFeedBack(FEEDBACK_TASK_MODIFIED); //
	 * setScheduledTasksToDo(scheduledTasksToDo);
	 * setFloatingTasksToDo(floatingTasksToDo); // }
	 */

	/*
	 * adds task into the respective completeArrayList and removes that same
	 * task from the ArrayList that it is currently residing in based on task
	 * number entered by user
	 */
	private void completeTask(Command retrievedCommand) {
		ArrayList<Integer> taskIndex = retrievedCommand.getIndexList();

		if (taskIndex.isEmpty()) {
			ArrayList<Task> listToComplete = getRecentAddedList();
			int positionToComplete = getRecentAddedPosition();

			if (listToComplete.get(positionToComplete).isScheduledTask()) {
				markAsComplete(listToComplete, scheduledTasksComplete, positionToComplete);
			} else if (listToComplete.get(positionToComplete).isFloatingTask()) {
				markAsComplete(listToComplete, floatingTasksComplete, positionToComplete);
			}
			setFeedBack(FEEDBACK_TASK_COMPLETED);
		} else {
			for (int i = taskIndex.size() - 1; i >= 0; i--) {
				int taskToComplete = taskIndex.get(i) - 1;
				if (taskToComplete >= 0) {
					if (taskToComplete < scheduledTasksOverDue.size()) {
						markAsComplete(scheduledTasksOverDue, scheduledTasksComplete, taskToComplete);
					} else if (taskToComplete < scheduledTasksToDo.size() + scheduledTasksOverDue.size()) {
						if ((scheduledTasksToDo.get(taskIndex.get(i) - 1 - scheduledTasksOverDue.size())
								.getOverLapped()) == true) {
							int keepTrack = 0;
							for (int j = 0; j <= (taskIndex.get(i) - 1 - scheduledTasksOverDue.size()); j++) {
								if ((scheduledTasksToDo.get(j).getOverLapped()) == true) {
									keepTrack++;
								}
							}
							if ((keepTrack % 2) == 0) {
								scheduledTasksToDo.get(taskIndex.get(i) - 2 - scheduledTasksOverDue.size())
										.setAsOverLapped(false);
							} else if ((keepTrack % 2) == 1) {
								scheduledTasksToDo.get(taskIndex.get(i) - scheduledTasksOverDue.size())
										.setAsOverLapped(false);
							}
						}
						taskToComplete -= (scheduledTasksOverDue.size());
						markAsComplete(scheduledTasksToDo, scheduledTasksComplete, taskToComplete);
					} else if (taskToComplete < scheduledTasksOverDue.size() + scheduledTasksToDo.size()
							+ floatingTasksToDo.size()) {
						taskToComplete -= (scheduledTasksToDo.size() + scheduledTasksOverDue.size());
						markAsComplete(floatingTasksToDo, floatingTasksComplete, taskToComplete);
					} else {
						setFeedBack(FEEDBACK_NON_EXISTENT_TASK_NUM);
					}
				} else {
					setFeedBack(FEEDBACK_NEGATIVE_TASK_NUM);
				}
			}
		}
	}

	public void markAsComplete(ArrayList<Task> incompleteList, ArrayList<Task> completeList, int taskNum) {
		Task completeTask = incompleteList.remove(taskNum);
		completeTask.setAsComplete();
		completeList.add(completeTask);
		setFeedBack(FEEDBACK_TASK_COMPLETED);
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

	private void undoTask(Task executeTask) {

	}

	private void redoTask(Task executeTask) {

	}

	private void searchTask(Task taskToFind) {
		search_Snigdha obj = new search_Snigdha();

		obj.searchTask(taskToFind);
		
		/*
		 * getTasksToFind(scheduledTasksOverDue, taskToFind);
		 * getTasksToFind(scheduledTasksToDo, taskToFind);
		 * getTasksToFind(floatingTasksToDo, taskToFind);
		 * getTasksToFind(scheduledTasksComplete, taskToFind);
		 * getTasksToFind(floatingTasksComplete, taskToFind);
		 */
	}

	private void getTasksToFind(ArrayList<Task> searchTaskList, Task taskToFind) {
		ArrayList<Task> searchedTasks = new ArrayList<Task>();
		ArrayList<Task> comparisonTaskList = searchTaskList;

		if (taskToFind.getDescription() != null) {
			for (int i = 0; i < comparisonTaskList.size(); i++) {
				if ((comparisonTaskList.get(i).getDescription().toLowerCase())
						.contains(taskToFind.getDescription().toLowerCase())) {
					searchedTasks.add(searchTaskList.get(i));
				}
			}
		} else if (taskToFind.getEndDate() != null) {
			for (int i = 0; i < comparisonTaskList.size(); i++) {
				if ((comparisonTaskList.get(i).getEndDate().toString()).contains(taskToFind.getEndDate().toString())) {
					searchedTasks.add(searchTaskList.get(i));
				} else if ((comparisonTaskList.get(i).getStartDate().toString())
						.contains(taskToFind.getEndDate().toString())) {
					searchedTasks.add(searchTaskList.get(i));
				}
			}
		} else if (taskToFind.getEndTime() != null) {
			for (int i = 0; i < searchTaskList.size(); i++) {
				if ((comparisonTaskList.get(i).getEndTime().toString()).contains(taskToFind.getEndTime().toString())) {
					searchedTasks.add(searchTaskList.get(i));
				} else if ((comparisonTaskList.get(i).getStartTime().toString())
						.contains(taskToFind.getEndTime().toString())) {
					searchedTasks.add(searchTaskList.get(i));
				}
			}
		}
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
