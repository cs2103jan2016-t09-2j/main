package Parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import Parser.Command.COMMAND_TYPE;
import ScheduleHacks.Task;

/*
 * Can Perform basic CRUD functionalities
 */

public class CommandParser {

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
		
		if(CommandParser.hasIndexNumber(commandType)) {
			IndexParser indexParser = new IndexParser(command, taskStatement);
			command.setIndexNumber(indexParser.getIndex());
			taskStatement = indexParser.getTaskDetails();
		}
		if (!CommandParser.hasTaskDetails(commandType)) {
			return null;
		} else {
			if (!taskStatement.isEmpty()) {
				Task newTask = new Task();
				if (commandType.equals(COMMAND_TYPE.ADD_TASK)) {
					newTask = CommandParser.addNewTask(taskStatement);
				} else if (commandType.equals(COMMAND_TYPE.MODIFY_TASK)) {
					newTask = CommandParser.editExistingTask(taskStatement);
				}
				return newTask;
			}
		}
		throw new Exception("Empty Task Description/Index Number");
	}
	
	public static Task addNewTask(String taskStatement) {
		Task newTask = new Task();
		// Getting date and time lists from task statement
		DateParser dateParser = new DateParser(taskStatement);
		ArrayList<LocalDate> dateList = dateParser.getDates();
		TimeParser timeParser = new TimeParser(dateParser.getTaskDetails());
		ArrayList<LocalTime> timeList = timeParser.getTimes();

		if (dateList == null && timeList == null) {
			newTask = addFloatingTaskDetails(taskStatement);
		} else {
			newTask = addScheduledTaskDetails(timeParser.getTaskDetails(), dateList, timeList);
		}
		return newTask;
	}

	public static Task editExistingTask(String taskStatement) {
		Task newTask = new Task();
		DateParser dateParser = new DateParser(taskStatement);
		ArrayList<LocalDate> dateList = dateParser.getDates();
		TimeParser timeParser = new TimeParser(dateParser.getTaskDetails());
		ArrayList<LocalTime> timeList = timeParser.getTimes();
		taskStatement = timeParser.getTaskDetails();

		if (dateList != null && timeList != null) {
			DateTimeParser objDateTime = new DateTimeParser(dateList, timeList);
			objDateTime.arrangeDateTimeList();
			dateList = objDateTime.getDateList();
			timeList = objDateTime.getTimeList();
		}
		setDates(dateList, newTask);
		setTimes(timeList, newTask);

		if (!taskStatement.isEmpty() && taskStatement != null) {
			newTask.setDescription(taskStatement);
		}
		return newTask;
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
		if (commandType.equals(COMMAND_TYPE.COMPLETE_TASK)) {
			return false;
		} else if (commandType.equals(COMMAND_TYPE.DELETE_TASK)) {
			return false;
		} else if (commandType.equals(COMMAND_TYPE.EXIT)) {
			return false;
		} else if (commandType.equals(COMMAND_TYPE.UNDO_TASK)) {
			return false;
		} else if (commandType.equals(COMMAND_TYPE.REDO_TASK)) {
			return false;
		}
		return true;
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
		if (commandType.equals(COMMAND_TYPE.COMPLETE_TASK)) {
			return true;
		} else if (commandType.equals(COMMAND_TYPE.DELETE_TASK)) {
			return true;
		} else if (commandType.equals(COMMAND_TYPE.MODIFY_TASK)) {
			return true;
		}
		return false;
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
	 * This method helps convert a floating task to a scheduled/upcoming task
	 * @param newTask
	 * @return newTask with all adjusted Parameters
	 */
	public static Task convertFloatingToScheduled(Task newTask) {
		ArrayList<LocalDate> dateList = new ArrayList<LocalDate>();
		ArrayList<LocalTime> timeList = new ArrayList<LocalTime>();

		if (newTask.getEndDate() != null) {
			if (newTask.getStartDate() != null) {
				dateList.add(newTask.getStartDate());
			}
			dateList.add(newTask.getEndDate());
		}

		if (newTask.getEndTime() != null) {
			if (newTask.getStartTime() != null) {
				timeList.add(newTask.getStartTime());
			}
			timeList.add(newTask.getEndTime());
		}

		if (!timeList.isEmpty() || !dateList.isEmpty()) {
			DateTimeParser objDateTime = new DateTimeParser(dateList, timeList);
			objDateTime.arrangeDateTimeList();
			newTask.setScheduledTask();
			setDates(objDateTime.getDateList(), newTask);
			setTimes(objDateTime.getTimeList(), newTask);
		}

		return newTask;
	}

	public static void setDates(ArrayList<LocalDate> dateList, Task newTask) {
		if (dateList != null) {
			newTask.setEndDate(dateList.get(dateList.size() - 1));
			if (dateList.size() > 1) {
				newTask.setStartDate(dateList.get(ParserConstants.FIRST_INDEX));
			}
		}
	}

	public static void setTimes(ArrayList<LocalTime> timeList, Task newTask) {
		if (timeList != null) {
			newTask.setEndTime(timeList.get(timeList.size() - 1));
			if (timeList.size() > 1) {
				newTask.setStartTime(timeList.get(ParserConstants.FIRST_INDEX));
			}
		}
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
		userCommand.trim();

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
		int whiteSpacePosition = userCommand.indexOf(ParserConstants.WHITE_SPACE);

		if (whiteSpacePosition != ParserConstants.NO_WHITE_SPACE) {
			return userCommand.substring(whiteSpacePosition).trim();
		}

		return null;
	}

	/**
	 * This method removes the unneccesarry white spaces present in the string.
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
