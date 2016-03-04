package Parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.regex.Pattern;

import Parser.Command.COMMAND_TYPE;

import java.util.regex.Matcher;

//import org.ocpsoft.prettytime.PrettyTime;
//import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import ScheduleHacks.Task;

/**
 * Only implementing add task for now
 * 
 * @author Snigdha Singhania
 *
 */

public class CommandParser {

	private static final int NO_WHITE_SPACE = -1;
	private static final int DEFAULT_INDEX_NUMBER = -1;
	private static final int FIRST_INDEX = 0;
	private static final char WHITE_SPACE = ' ';

	private static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";
	private static final String REGEX_DIGITS = "\\d+";

	public static Command getParsedCommand(String newUserCommand) throws Exception {
		return parseCommand(cleanupExtraWhitespace(newUserCommand));
	}

	private static Command parseCommand(String userCommand) throws Exception {
		Command parsedCommand = new Command();

		String commandType = getFirstWord(userCommand);
		parsedCommand.setCommandType(commandType);

		String taskStatement = removeFirstWord(userCommand);
		Task taskDetails = findTaskDetails(parsedCommand, taskStatement);
		parsedCommand.setTaskDetails(taskDetails);

		return parsedCommand;
	}

	// incomplete
	public static Task findTaskDetails(Command command, String taskStatement)throws Exception {
		COMMAND_TYPE commandType = command.getCommandType();
		if (!hasTaskDetails(commandType)) {
			if(hasIndexNumber(commandType)) {
				command.setIndexNumber(findIndexNumber(command, taskStatement));
			}
			return null;
		} else {
			if (!taskStatement.isEmpty()) {
				Task newTask = new Task();
				if(commandType.equals(COMMAND_TYPE.ADD_TASK)) {
					newTask = addNewTask(taskStatement);
				} else if (commandType.equals(COMMAND_TYPE.MODIFY_TASK)) {
					newTask = editExistingTask(taskStatement);
					command.setIndexNumber(findIndexNumber(command, taskStatement));
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
	
	//incomplete
	public static Task editExistingTask(String taskStatement){
		return null;
	}

	public static int findIndexNumber(Command thisCommand, String taskDetails)throws Exception {
		Command.COMMAND_TYPE commandType = thisCommand.getCommandType();
		if (commandType.equals(COMMAND_TYPE.DELETE_TASK) || commandType.equals(COMMAND_TYPE.COMPLETE_TASK)) {
			taskDetails = cleanupExtraWhitespace(taskDetails);
			if (Pattern.matches(REGEX_DIGITS, taskDetails)) {
				int indexNumber = Integer.parseInt(taskDetails);
				return indexNumber;
			}
		}
		return DEFAULT_INDEX_NUMBER;
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
		}
		return true;
	}
	
	/**
	 * This method is used to check if the corresponding command types require
	 * an attached task index number.
	 * 
	 * @param commandType
	 * @return true, if the command type requires index number
	 *         false, otherwise 
	 *         Complete, delete and modify functionalities require index numbers.
	 */
	public static boolean hasIndexNumber(COMMAND_TYPE commandType){
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
		Task newTask = new Task();
		newTask.setDescription(taskStatement);
		newTask.setScheduledTask();
		if (dateList != null) {
			newTask.setEndDate(dateList.get(dateList.size() - 1));
			if (dateList.size() > 1) {
				newTask.setStartDate(dateList.get(FIRST_INDEX));
			}
		}
		if (timeList != null) {
			newTask.setEndTime(timeList.get(timeList.size() - 1));
			if (timeList.size() > 1) {
				newTask.setStartTime(timeList.get(FIRST_INDEX));
			}
		}

		return newTask;
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
	static String getFirstWord(String userCommand) {
		userCommand.trim();

		int whiteSpacePosition = userCommand.indexOf(WHITE_SPACE);
		if (whiteSpacePosition == NO_WHITE_SPACE) {
			throw new Error("Empty User Command");
		}

		return userCommand.substring(FIRST_INDEX, whiteSpacePosition);
	}

	/**
	 * This operation removes the first word from the user's command, which is
	 * the command type.
	 * 
	 * @return taskStatement which is exclusive of the command type.
	 */
	static String removeFirstWord(String userCommand) {
		int whiteSpacePosition = userCommand.indexOf(WHITE_SPACE);

		if (whiteSpacePosition != NO_WHITE_SPACE) {
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
		Pattern extraSpace = Pattern.compile(REGEX_EXTRA_WHITESPACE);
		Matcher regexMatcher = extraSpace.matcher(someText.trim());
		String cleanText = regexMatcher.replaceAll(" ");
		return cleanText;
	}

}
