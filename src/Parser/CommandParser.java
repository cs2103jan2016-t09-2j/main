package Parser;
//import java.time.LocalDate;
//import java.time.LocalTime;

import java.util.Date;
import java.util.List;

//import org.ocpsoft.prettytime.PrettyTime;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

//import com.joestelmach.natty.Parser;

import ScheduleHacks.Task;

public class CommandParser {
	
	private static final int NO_WHITE_SPACE = -1;
	private static final int FIRST_INDEX = 0;
	private static final char WHITE_SPACE = ' ';
	
	public static Command getParsedCommand(String newUserCommand){
		return parseCommand(newUserCommand);
	}

	private static Command parseCommand(String userCommand) {
		String commandType = getFirstWord(userCommand);
		String taskStatement = removeFirstWord(userCommand);
		Task taskDetails = setTaskDetails(taskStatement);
		
		Command parsedCommand = new Command(commandType, taskDetails);
		return parsedCommand;
	}
	
	//incomplete
	private static Task setTaskDetails(String taskStatement) {
		Task newTask = new Task();
		
		if (isFloatingTask(taskStatement)) {
			newTask.setDescription(taskStatement);
			newTask.setFloatingTask();
			return newTask;
		} else {
			
		}
		return null;
	}

	//incomplete
	protected static boolean isFloatingTask(String taskStatement) {
		PrettyTimeParser timeParse = new PrettyTimeParser();
		List<Date> dateList = timeParse.parse(taskStatement);
		if(dateList.isEmpty()) {
			return true;
		}
		return false;
	}
	
	

	/**
	 * This method is used to retrieve the first word from the user's command, which is
	 * the command action(Eg., add, delete).
	 * 
	 * @param userCommand
	 *            which is to be executed.
	 * 
	 * @return 
	 * 		the first word of the user command, which is the command type.
	 */
	static String getFirstWord(String userCommand){
		userCommand.trim();
		
		int whiteSpacePosition = userCommand.indexOf(WHITE_SPACE);
		if (whiteSpacePosition == NO_WHITE_SPACE) {
			throw new Error ("Empty User Command");
		}
		
		return userCommand.substring(FIRST_INDEX, whiteSpacePosition);
	}	
	
	/**
	 * This operation removes the first word from the user's command,
	 * which is the command type.
	 * 
	 * @return taskStatement
	 * 					which is exclusive of the command type.
	 */
	static String removeFirstWord(String userCommand) {
		int whiteSpacePosition = userCommand.indexOf(WHITE_SPACE);
		
		if(whiteSpacePosition != NO_WHITE_SPACE) {
			return userCommand.substring(whiteSpacePosition).trim();
		}
		
		return null;
	}

}
