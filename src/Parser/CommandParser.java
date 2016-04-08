//@@author A0132778W

package Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import Parser.Command.COMMAND_TYPE;
import ScheduleHacks.Task;

/**
 * This class is invoked whenever a String input by the user needs to be parsed
 * to retrieve the required details.
 * 
 * This is mostly a facade class which in highly dependant on the DateParser,
 * TimeParser and IndexParser.
 */

public class CommandParser {

	/**
	 * This method is used by the Logic to get the parsed components from the
	 * input user command.
	 * 
	 * @param newUserCommand
	 *            command input by the user
	 * @return parsedCommand Command type object which contains the commandType,
	 *         newTaskDetails, and an index number
	 * @throws Exception
	 */
	public static Command getParsedCommand(String newUserCommand) throws Exception {
		return parseCommand(cleanupExtraWhitespace(newUserCommand));
	}

	private static Command parseCommand(String userCommand) throws Exception {
		Command parsedCommand = new Command();

		String commandType = getFirstWord(userCommand);
		parsedCommand.setCommandType(commandType);

		if (parsedCommand.isFirstWordCommand) {
			userCommand = removeFirstWord(userCommand);
		}
		Task taskDetails = findTaskDetails(parsedCommand, userCommand);
		parsedCommand.setTaskDetails(taskDetails);

		return parsedCommand;
	}

	public static Task findTaskDetails(Command command, String taskStatement) throws Exception {
		COMMAND_TYPE commandType = command.getCommandType();

		if (CommandParser.hasIndexNumber(commandType)) {
			IndexParser indexParser = new IndexParser(command, taskStatement);
			indexParser.findIndexList();
			command.setIndexList(indexParser.getIndexList());
			taskStatement = indexParser.getTaskDetails();
		}
		if (!CommandParser.hasTaskDetails(commandType)) {
			return null;
		} else {
			// if (taskStatement != null && !taskStatement.isEmpty()) {
			Task newTask = new Task();
			switch (commandType) {
			case ADD_TASK:
				newTask = addNewTask(taskStatement);
				break;
			case MODIFY_TASK:
				newTask = sendEditTaskInfoToLogic(taskStatement);
				break;
			case SEARCH_TASK:
				newTask = getCriteria(taskStatement);
				break;
			case VIEW_LIST:
				newTask = getCriteria(taskStatement);
				break;
			case SET_DIRECTORY:
				newTask = setDirectory(command, taskStatement);
				break;
			case HELP:
				newTask = setHelpParameters(command, taskStatement);
				break;
			default:
				// do nothing
				break;
			}
			if (newTask != null) {
				newTask.setDescription(parseDescription(newTask.getDescription()));
			}
			if (command.getCommandType().equals(COMMAND_TYPE.HELP)
					|| (taskStatement != null && !taskStatement.isEmpty())) {
				return newTask;
			} else {
				throw new Exception("Empty Task Description");
			}
			// }
		}
		// throw new Exception("Empty Task Description");
	}

	/**
	 * This method is invoked when the user wants to change directory of his
	 * task list. It parses taskStatement to obtain directory details.
	 * 
	 * @param taskStatement
	 * @return newTask, set with all parameters to specify new directory route.
	 */
	public static Task setDirectory(Command cmd, String taskStatement) {
		Task newTask = new Task();
		taskStatement = cleanupExtraWhitespace(taskStatement);

		if (taskStatement.matches(ParserConstants.REGEX_POSSIBLE_DIRECTORY)
				|| taskStatement.equalsIgnoreCase("default")) {
			newTask.setDescription(taskStatement);
		} else {
			// if parameter provided is not of a directory, it is considered to
			// be an add command.
			try {
				cmd.setCommandType(COMMAND_TYPE.ADD_TASK);
			} catch (Exception e) {
				// do nothing
			}
			newTask = addNewTask("set " + taskStatement);
		}

		return newTask;
	}

	public static Task setHelpParameters(Command cmd, String taskStatement) {
		if (taskStatement == null || taskStatement.isEmpty()) {
			return null;
		}
		Task newTask = new Task();
		try {
			cmd.setCommandType(COMMAND_TYPE.ADD_TASK);
		} catch (Exception e) {
			// do nothing
		}
		newTask = addNewTask("Help " + taskStatement);
		return newTask;
	}

	public static Task addNewTask(String taskStatement) {
		Task newTask = new Task();
		// Getting date and time lists from task statement
		DateParser dateParser = new DateParser(taskStatement);
		dateParser.findDates();
		ArrayList<LocalDate> dateList = dateParser.getDateList();

		TimeParser timeParser = new TimeParser(dateParser.getTaskDetails(), dateList);
		timeParser.findTimes();
		ArrayList<LocalTime> timeList = timeParser.getTimeList();

		if (dateList == null && timeList == null) {
			newTask = addFloatingTaskDetails(taskStatement);
		} else {
			newTask = addScheduledTaskDetails(timeParser.getTaskDetails(), dateList, timeList);
		}
		return newTask;
	}

	/**
	 * This method is used to prepare an edit task to send to Logic
	 * 
	 * @param taskStatement
	 * @return Task, with taskStatement as Description
	 */
	public static Task sendEditTaskInfoToLogic(String taskStatement) {
		Task newTask = new Task();
		newTask.setDescription(taskStatement);
		return newTask;
	}

	/**
	 * This method is invoked by Logic, containing the old Task object that
	 * needs to be edited and the edit details
	 * 
	 * @param oldTask
	 * @param editStatement,
	 *            contains edit parameters
	 * @return
	 */
	public static Task editExistingTask(Task oldTask, String editStatement) {
		Task newTask = new Task(oldTask);
		if (requiresDeletingParameters(editStatement)) {
			editStatement = removeFirstWord(editStatement);
			deleteParameters(newTask, getParameterList(editStatement));
		} else {
			newTask = setEditParameters(editStatement, newTask);
		}
		return newTask;
	}

	public static Task getCriteria(String taskStatement) throws Exception {
		Task newTask = new Task();
		taskStatement = cleanupExtraWhitespace(taskStatement);
		String storeTaskStatement = taskStatement;
		String firstWord = getFirstWord(taskStatement);

		if (hasInDictionary(ParserConstants.UPCOMING_PERIOD_KEYWORD, firstWord)) {
			taskStatement = setUpcomingDateRange(taskStatement, newTask, firstWord);
		} else if (firstWord.equalsIgnoreCase(ParserConstants.STRING_OVERDUE)) {
			taskStatement = removeFirstWord(taskStatement);
			setOverdueCriteria(newTask);
		} else {
			taskStatement = getSearchCriteria(taskStatement, newTask);
		}

		try {
			if (taskStatement != null) {
				taskStatement = cleanupExtraWhitespace(taskStatement);
			}
			if (taskStatement == null || taskStatement.isEmpty()) {
				taskStatement = storeTaskStatement;
			}
			newTask.setDescription(taskStatement);
		} catch (Exception e) {
			// do nothing
		}
		return newTask;
	}

	/**
	 * This method is invoked when the user wants to search for overdue tasks.
	 * It sets parameters so as to include every task before the present instant
	 * of time.
	 * 
	 * @param newTask
	 */
	public static void setOverdueCriteria(Task newTask) {
		newTask.setStartDate(LocalDate.MIN);
		newTask.setEndDate(getCurrentDate());
		newTask.setEndTime(LocalTime.now().plusSeconds(5));
		newTask.setAsIncomplete();
	}

	public static String getSearchCriteria(String taskStatement, Task newTask) throws Exception {
		String firstWord = getFirstWord(taskStatement);

		if (hasInDictionary(ParserConstants.COMMAND_COMPLETE, firstWord)) {
			newTask.setAsComplete();
			taskStatement = taskStatement.replace(firstWord, ParserConstants.STRING_WHITESPACE);
		} else {
			DateParser dateObj = new DateParser(taskStatement);
			LocalDate currentDate = getCurrentDate();
			dateObj.findDates();
			if ((new DateParser()).hasDayDuration(taskStatement)) {
				taskStatement = addCriteriaDateDuration(newTask, taskStatement);
			} else if (dateObj.getDateList() != null) {
				int dateListSize = dateObj.getDateList().size();
				newTask.setEndDate(dateObj.getDateList().get(dateListSize - 1));
				if (dateListSize > 1) {
					newTask.setStartDate(dateObj.getDateList().get(ParserConstants.FIRST_INDEX));
				}
				taskStatement = dateObj.getTaskDetails();
			} else if (dateObj.isMonth(firstWord)) {
				// if month
				int monthNum = dateObj.getMonthNum(firstWord);
				int currentMonthNum = currentDate.getMonthValue();
				if (monthNum < currentMonthNum) {
					newTask.setStartDate(
							LocalDate.of(currentDate.getYear() + 1, monthNum, ParserConstants.FIRST_DAY_OF_MONTH));
				} else {
					newTask.setStartDate(
							LocalDate.of(currentDate.getYear(), monthNum, ParserConstants.FIRST_DAY_OF_MONTH));
				}
				newTask.setEndDate(newTask.getStartDate().plusMonths(1).minusDays(1));
				taskStatement = taskStatement.replace(firstWord, ParserConstants.STRING_WHITESPACE);

			}
		}
		return taskStatement;
	}

	public static String setUpcomingDateRange(String taskStatement, Task newTask, String firstWord) {
		try {
			DateParser dateObj = new DateParser(taskStatement);
			LocalDate currentDate = getCurrentDate();
			String secondWord = getFirstWord(removeFirstWord(taskStatement));

			if (secondWord.equalsIgnoreCase("month")) {
				LocalDate newDate = currentDate.plusMonths(indexOf(firstWord, ParserConstants.UPCOMING_PERIOD_KEYWORD));

				newTask.setStartDate(
						LocalDate.of(newDate.getYear(), newDate.getMonth(), ParserConstants.FIRST_DAY_OF_MONTH));

				newTask.setEndDate(newTask.getStartDate().plusMonths(1).minusDays(1));

				taskStatement = taskStatement.replaceFirst(firstWord + " " + secondWord,
						ParserConstants.STRING_WHITESPACE);
			} else if (secondWord.equalsIgnoreCase("week")) {
				newTask.setStartDate(dateObj.getDayOfWeekDate("Sunday").minusDays(ParserConstants.DAYS_IN_WEEK
						- indexOf(firstWord, ParserConstants.UPCOMING_PERIOD_KEYWORD) * ParserConstants.DAYS_IN_WEEK));

				newTask.setEndDate(newTask.getStartDate().plusDays(ParserConstants.DAYS_IN_WEEK));

				taskStatement = taskStatement.replaceFirst(firstWord + " " + secondWord,
						ParserConstants.STRING_WHITESPACE);
			} else if (secondWord.equalsIgnoreCase("year")) {

				newTask.setStartDate(LocalDate
						.of(currentDate.getYear() + indexOf(firstWord, ParserConstants.UPCOMING_PERIOD_KEYWORD), 1, 1));

				newTask.setEndDate(LocalDate.of(
						currentDate.getYear() + indexOf(firstWord, ParserConstants.UPCOMING_PERIOD_KEYWORD), 12, 31));

				taskStatement = taskStatement.replaceFirst(firstWord + " " + secondWord,
						ParserConstants.STRING_WHITESPACE);
			} else if ((new DateParser()).hasDayDuration(removeFirstWord(taskStatement))) {
				taskStatement = removeFirstWord(taskStatement);
				taskStatement = addCriteriaDateDuration(newTask, taskStatement);
			}
		} catch (Exception e) {
			// do nothing
		}
		return taskStatement;
	}

	public static String addCriteriaDateDuration(Task newTask, String taskStatement) {
		DateParser dateObj = new DateParser(taskStatement);
		String dayDuration = dateObj.getDayDurationWord(taskStatement);
		newTask.setStartDate(getCurrentDate());
		newTask.setEndDate(dateObj.getParsedDayDurationDate(dayDuration));
		return taskStatement.replace(dayDuration, ParserConstants.STRING_EMPTY);
	}

	/**
	 * This method sets all the elements of the task object matching the
	 * parametersToDelete list as Empty.
	 * 
	 * @param taskStatement
	 * @param newTask
	 */
	public static Task setEditParameters(String taskStatement, Task oldTask) {
		DateParser dateParser = new DateParser(taskStatement);
		dateParser.findDates();
		ArrayList<LocalDate> dateList = dateParser.getDateList();

		TimeParser timeParser = new TimeParser(dateParser.getTaskDetails(), dateList);
		timeParser.findTimes();
		ArrayList<LocalTime> timeList = timeParser.getTimeList();
		taskStatement = timeParser.getTaskDetails();

		if (oldTask.isFloatingTask()) {
			oldTask = editFloatingTask(taskStatement, oldTask, dateList, timeList);
		} else if (oldTask.isScheduledTask()) {
			oldTask = editScheduledTask(taskStatement, oldTask, dateList, timeList);
		}

		return oldTask;
	}

	/**
	 * This method is used to edit parameters of initial Scheduled tasks
	 * oldTask.
	 * 
	 * @param taskStatement
	 * @param oldTask
	 * @param dateList
	 * @param timeList
	 * @return
	 */
	public static Task editScheduledTask(String taskStatement, Task oldTask, ArrayList<LocalDate> dateList,
			ArrayList<LocalTime> timeList) {

		ArrayList<LocalDate> oldDateList = new ArrayList<LocalDate>();
		ArrayList<LocalTime> oldTimeList = new ArrayList<LocalTime>();

		if (oldTask.getStartDate() != null) {
			oldDateList.add(oldTask.getStartDate());
		}
		oldDateList.add(oldTask.getEndDate());

		if (oldTask.getStartTime() != null) {
			oldTimeList.add(oldTask.getStartTime());
		}
		oldTimeList.add(oldTask.getEndTime());

		if (dateList != null && timeList != null) {
			DateTimeParser objDateTime = new DateTimeParser(dateList, timeList);
			objDateTime.arrangeDateTimeList();
			dateList = objDateTime.getDateList();
			timeList = objDateTime.getTimeList();
		} else if (timeList != null || dateList != null) {
			if (timeList != null) {
				if (timeList.size() > oldTimeList.size()) {
					dateList = new ArrayList<LocalDate>();
					dateList.add(oldTask.getEndDate());
					if (LocalDateTime.of(oldTask.getEndDate(), timeList.get(ParserConstants.FIRST_INDEX))
							.isAfter(LocalDateTime.of(oldTask.getEndDate(), timeList.get(1)))) {
						dateList.add(oldTask.getEndDate().plusDays(1));
					} else {
						dateList.add(oldTask.getEndDate());
					}

				} else if (timeList.size() < oldTimeList.size()) {
					oldTask.setStartDate(null);
				}
			} else {
				if (dateList.size() > oldDateList.size()) {
					timeList = new ArrayList<LocalTime>();
					timeList.add(LocalTime.MAX);
					timeList.add(oldTask.getEndTime());
				} else if (dateList.size() < oldDateList.size()) {
					oldTask.setStartDate(null);
					oldTask.setStartTime(null);
					oldTask.setEndTime(LocalTime.MAX);
					/*
					 * if (LocalDateTime.of(dateList.get(0),
					 * oldTask.getStartTime())
					 * .isAfter(LocalDateTime.of(dateList.get(0),
					 * oldTask.getEndTime()))) {
					 * dateList.add(dateList.get(0).plusDays(1)); } else {
					 * dateList.add(dateList.get(0)); }
					 */
				}
			}
		}

		if (dateList != null) {
			oldTask.setEndDate(null);
			oldTask.setStartDate(null);
		}
		if (timeList != null) {
			oldTask.setEndTime(null);
			oldTask.setStartTime(null);
		}

		setDates(dateList, oldTask);
		setTimes(timeList, oldTask);

		if (taskStatement != null && !taskStatement.isEmpty()) {
			oldTask.setDescription(taskStatement);
		}

		return oldTask;
	}

	public static Task editFloatingTask(String taskStatement, Task oldTask, ArrayList<LocalDate> dateList,
			ArrayList<LocalTime> timeList) {
		if (dateList != null || timeList != null) {
			if (taskStatement != null && !taskStatement.isEmpty()) {
				oldTask = addScheduledTaskDetails(taskStatement, dateList, timeList);
			} else {
				oldTask = addScheduledTaskDetails(oldTask.getDescription(), dateList, timeList);
			}
		} else {
			if (taskStatement != null && !taskStatement.isEmpty()) {
				oldTask.setDescription(taskStatement);
			}
		}

		return oldTask;
	}

	/**
	 * This method sets all the elements of the task object matching the
	 * parametersToDelete list as Empty.
	 * 
	 * @param oldTask
	 * @param parametersToDelete
	 */
	public static void deleteParameters(Task oldTask, ArrayList<String> parametersToDelete) {
		for (String parameter : parametersToDelete) {
			if (hasInDictionary(ParserConstants.PARAMETER_DATE, parameter)) {
				oldTask.setEndDate(null);
				oldTask.setStartDate(null);
				oldTask.setStartTime(null);
				oldTask.setEndTime(null);
				oldTask.setFloatingTask();
			} else if (hasInDictionary(ParserConstants.PARAMETER_TIME, parameter)) {
				if (oldTask.getStartTime() != null) {
					oldTask.setStartTime(LocalTime.MAX);
				}
				oldTask.setEndTime(LocalTime.MAX);
			} else if (hasInDictionary(ParserConstants.PARAMETER_DESCRIPTION, parameter)) {
				oldTask.setDescription(ParserConstants.STRING_EMPTY);
			}
		}
	}

	/**
	 * This method is used to check if the corresponding command types require
	 * an attached task information.
	 * 
	 * @param commandType
	 * @return true, if the command type requires task information false,
	 *         otherwise Only add and modify functionalities require task
	 *         details.
	 */
	public static boolean hasTaskDetails(COMMAND_TYPE commandType) {
		switch (commandType) {
		case COMPLETE_TASK:
			return false;
		case INCOMPLETE_TASK:
			return false;
		case DELETE_TASK:
			return false;
		case UNDO_TASK:
			return false;
		case REDO_TASK:
			return false;
		case HOME:
			return false;
		/*
		 * case HELP: return false;
		 */
		case EXIT:
			return false;
		default:
			return true;
		}
	}

	/**
	 * This method is used to check if the corresponding command types require
	 * an attached task index number.
	 * 
	 * @param commandType
	 * @return true, if the command type requires index number false, otherwise
	 *         Complete, delete and modify functionalities require index
	 *         numbers.
	 */
	public static boolean hasIndexNumber(COMMAND_TYPE commandType) {
		switch (commandType) {
		case COMPLETE_TASK:
			return true;
		case INCOMPLETE_TASK:
			return true;
		case DELETE_TASK:
			return true;
		case MODIFY_TASK:
			return true;
		default:
			return false;
		}
	}

	protected static Task addFloatingTaskDetails(String taskStatement) {
		Task newTask = new Task();
		newTask.setDescription(taskStatement);
		newTask.setFloatingTask();
		return newTask;
	}

	protected static Task addScheduledTaskDetails(String taskStatement, ArrayList<LocalDate> dateList,
			ArrayList<LocalTime> timeList) {
		DateTimeParser objDateTime = new DateTimeParser(dateList, timeList);
		objDateTime.arrangeDateTimeList();

		Task newTask = new Task();
		newTask.setDescription(taskStatement);
		newTask.setScheduledTask();
		setDates(objDateTime.getDateList(), newTask);
		setTimes(objDateTime.getTimeList(), newTask);

		return newTask;
	}

	/**
	 * This method is used to get rid of unnecessary special characters in the
	 * task description. Only currency symbols before dates and times are
	 * allowed.
	 * 
	 * @param description
	 * @return task description after removing all special characters.
	 */
	public static String parseDescription(String description) {
		if (description != null && !description.isEmpty()) {
			String parsedDescription = ParserConstants.STRING_EMPTY;
			String[] words = description.split(ParserConstants.STRING_WHITESPACE);
			for (String word : words) {
				char firstChar = word.charAt(ParserConstants.FIRST_INDEX);
				if (Character.isLetterOrDigit(firstChar)
						|| (Character.getType(firstChar) == Character.CURRENCY_SYMBOL)) {
					parsedDescription += ParserConstants.STRING_WHITESPACE + word;
					continue;
				}
				parsedDescription += ParserConstants.STRING_WHITESPACE + getDescriptionSansSpecialChars(word);
			}
			return cleanupExtraWhitespace(parsedDescription);
		}
		return ParserConstants.STRING_EMPTY;
	}

	public static String getDescriptionSansSpecialChars(String description) {
		String tempWordStore = description.substring(ParserConstants.FIRST_INDEX + 1);
		DateParser dateObj = new DateParser(tempWordStore);
		dateObj.findDates();
		TimeParser timeObj = new TimeParser(tempWordStore);
		timeObj.findTimes();
		if (dateObj.getDateList() != null || timeObj.getTimeList() != null) {
			return tempWordStore;
		} else {
			return description;
		}
	}

	/**
	 * This method is used to check if any delete keywords are present in an
	 * edit statement.
	 * 
	 * @param taskStatement
	 * @return returns true if delete keywords are present in taskStatement;
	 *         false otherwise.
	 */
	public static boolean requiresDeletingParameters(String taskStatement) {
		taskStatement = cleanupExtraWhitespace(taskStatement);
		try {
			String firstWord = getFirstWord(taskStatement);
			if (hasInDictionary(ParserConstants.COMMAND_DELETE, firstWord)) {
				getSecondWord(taskStatement); /* Just to check if not null */
				return true;
			}
		} catch (Exception e) {
			// do nothing
			// if exception, means no conversion
			// thus return false
		}
		return false;
	}

	/**
	 * This method splits up the words in taskStatement at the whitespace.
	 * 
	 * @param taskStatement
	 * @return an arrayList of words contained in taskStatement
	 */
	public static ArrayList<String> getParameterList(String taskStatement) {
		taskStatement = cleanupExtraWhitespace(taskStatement);
		String[] parameters = taskStatement.split(ParserConstants.STRING_WHITESPACE);
		return new ArrayList<String>(Arrays.asList(parameters));
	}

	/**
	 * This method sets the first date of dateList as StartDate and last date of
	 * the list as endDate of the given newTask object. If only one date is
	 * present in the dateList, it is assigned as the endDate.
	 * 
	 * @param dateList
	 * @param newTask
	 */
	public static void setDates(ArrayList<LocalDate> dateList, Task newTask) {
		if (dateList != null) {
			newTask.setEndDate(dateList.get(dateList.size() - 1));
			if (dateList.size() > 1) {
				newTask.setStartDate(dateList.get(ParserConstants.FIRST_INDEX));
			}
		}
	}

	/**
	 * This method sets the first time of timeList as StartTime and last time of
	 * the list as endTime of the given newTask object. If only one time is
	 * present in the timeList, it is assigned as the endTime.
	 * 
	 * @param timeList
	 * @param newTask
	 */
	public static void setTimes(ArrayList<LocalTime> timeList, Task newTask) {
		if (timeList != null) {
			newTask.setEndTime(timeList.get(timeList.size() - 1));
			if (timeList.size() > 1) {
				newTask.setStartTime(timeList.get(ParserConstants.FIRST_INDEX));
			}
		}
	}

	/**
	 * This method gets today's date.
	 * 
	 * @return today's date as LocalDate object.
	 */
	public static LocalDate getCurrentDate() {
		return LocalDate.now();
	}

	/**
	 * This method is used to retrieve the first word from the user's command,
	 * which is the command action(Eg., add, delete).
	 * 
	 * @param userCommand
	 *            which is to be executed.
	 * 
	 * @return the first word of the user command, which is the command type.
	 */
	static String getFirstWord(String userCommand) throws Exception {
		userCommand = userCommand.trim();

		int splitPosition = userCommand.indexOf(ParserConstants.WHITE_SPACE);
		if (splitPosition == ParserConstants.NO_WHITE_SPACE) {
			if (userCommand.length() > 0) {
				splitPosition = userCommand.length();
			} else {
				throw new Exception("Empty User Command");
			}
		}
		return userCommand.substring(ParserConstants.FIRST_INDEX, splitPosition);
	}

	/**
	 * This operation removes the first word from the user's command, which is
	 * the command type.
	 * 
	 * @return taskStatement which is exclusive of the command type.
	 */
	static String removeFirstWord(String userCommand) {
		userCommand = cleanupExtraWhitespace(userCommand);
		int whiteSpacePosition = userCommand.indexOf(ParserConstants.WHITE_SPACE);

		if (whiteSpacePosition != ParserConstants.NO_WHITE_SPACE) {
			return userCommand.substring(whiteSpacePosition).trim();
		}

		return null;
	}

	public static String getSecondWord(String statement) throws Exception {
		return getFirstWord(removeFirstWord(statement));
	}

	/**
	 * This method returns the index of word in array[].
	 * 
	 * @param word
	 * @param array
	 * @return index if word is present in array[], otherwise -1.
	 */
	public static int indexOf(String word, String[] array) {
		if (hasInDictionary(array, word)) {
			for (int index = ParserConstants.FIRST_INDEX; index < array.length; index++) {
				if (word.equalsIgnoreCase(array[index])) {
					return index;
				}
			}
		}
		return -1; // if absent
	}

	/**
	 * This method checks if wordToFind is present in dictionary[].
	 * 
	 * @param dictionary
	 * @param wordToFind
	 * @return true if present, else false.
	 */
	private static boolean hasInDictionary(String[] dictionary, String wordToFind) {
		if (wordToFind != null && !wordToFind.isEmpty()) {
			for (String dictionaryWords : dictionary) {
				if (dictionaryWords.equalsIgnoreCase(wordToFind))
					return true;
			}
		}
		return false;
	}

	/**
	 * This method removes the unnecessary white spaces present in the string.
	 * 
	 * @param someText
	 *            is any string with several white spaces.
	 * @return someText excluding the extra unnecessary white spaces.
	 */
	public static String cleanupExtraWhitespace(String someText) {
		Pattern extraSpace = Pattern.compile(ParserConstants.REGEX_EXTRA_WHITESPACE);
		Matcher regexMatcher = extraSpace.matcher(someText.trim());
		String cleanText = regexMatcher.replaceAll(ParserConstants.STRING_WHITESPACE);
		return cleanText;
	}

}
