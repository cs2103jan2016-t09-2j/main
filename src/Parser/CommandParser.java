package Parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.regex.Pattern;
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
		if (!taskStatement.isEmpty()) {
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
		return null;
	}

	protected static Task addFloatingTaskDetails(String taskStatement) {
		Task newTask = new Task();
		newTask.setDescription(taskStatement);
		newTask.setFloatingTask();
		return newTask;
	}

	// incomplete
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
