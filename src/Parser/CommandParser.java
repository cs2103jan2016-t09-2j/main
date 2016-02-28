package Parser;

//import java.time.LocalDate;
//import java.time.LocalTime;

import java.util.Date;
import java.util.List;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

//import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import ScheduleHacks.Task;

/**
 * Only implementing add task for now
 * 
 * @author Snigdha Singhania
 *
 */

public class CommandParser {

	private static final int NO_WHITE_SPACE = -1;
	private static final int FIRST_INDEX = 0;
	private static final char WHITE_SPACE = ' ';

	private static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";

	public static Command getParsedCommand(String newUserCommand) {
		return parseCommand(cleanupExtraWhitespace(newUserCommand));
	}

	private static Command parseCommand(String userCommand) {
		String commandType = getFirstWord(userCommand);
		String taskStatement = removeFirstWord(userCommand);
		Task taskDetails = setTaskDetails(taskStatement);

		// Why return null everytime ?
		Command parsedCommand = new Command(commandType, taskDetails, null);
		return parsedCommand;
	}

	// incomplete
	private static Task setTaskDetails(String taskStatement) {
		Task newTask = new Task();
		if (!taskStatement.isEmpty()) {
			if (isFloatingTask(taskStatement)) {
				newTask.setDescription(taskStatement);
				newTask.setFloatingTask();
				return newTask;
			} else {

			}
		}
		return null;
	}

	/**
	 * This method checks if the task statement as any dates as a part of its
	 * description. If there are no date/time values in the description, then
	 * task is of floating type.
	 * 
	 * @param taskStatement
	 *            which is the user command input, exclusive of the first word
	 *            (which is the command type)
	 * @return true, if the task is of Floating type false, otherwise.
	 */
	// incomplete
	protected static boolean isFloatingTask(String taskStatement) {
		PrettyTimeParser timeParse = new PrettyTimeParser();
		List<Date> dateList = timeParse.parse(taskStatement);
		if (dateList.isEmpty()) {
			return true;
		}
		isScheduledTask(dateList);
		return false;
	}

	/**
	 * This method is invoked only when the task is not of floating type.
	 * 
	 * @param dateList
	 *            is the list of all dates and times contained in the task
	 *            statement entered by the user as the command
	 * @return true, stating that the purpose of the method is served
	 */
	// incomplete
	private static boolean isScheduledTask(List<Date> dateList) {
		return true;
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
	 * This method removes the unnecesarry white spaces present in the string.
	 * 
	 * @param someText
	 *            is any string with several white spaces.
	 * @return someText excluding the extra unnecessary white spaces.
	 */
	static String cleanupExtraWhitespace(String someText) {
		Pattern extraSpace = Pattern.compile(REGEX_EXTRA_WHITESPACE);
		Matcher regexMatcher = extraSpace.matcher(someText.trim());
		String cleanText = regexMatcher.replaceAll(" ");
		return cleanText;
	}

}
