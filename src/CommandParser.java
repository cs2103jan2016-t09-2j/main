//import java.time.LocalDate;
//import java.time.LocalTime;

//import java.util.Date;
//import java.util.List;

//import org.ocpsoft.prettytime.PrettyTime;
//import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

//import com.joestelmach.natty.Parser;

import ScheduleHacks.Task;
import ScheduleHacks.Command;

class CommandParser {
	
	private static final String[] COMMAND_ADD = {"add"};
	private static final String[] COMMAND_DELETE = {"delete"};
	private static final String[] COMMAND_MODIFY = {"modify"};
	private static final String[] COMMAND_COMPLETE = {"complete"};
	private static final String[] COMMAND_EXIT = {"exit"};
	private static final String   COMMAND_INVALID = null;
	
	private static final int NO_WHITE_SPACE = -1;
	private static final int FIRST_INDEX = 0;
	private static final char WHITE_SPACE = ' ';
	
	static Command getParsedCommand(String newUserCommand){
		return parseCommand(newUserCommand);
	}

	private static Command parseCommand(String userCommand) {
		String commandFirstWord = getFirstWord(userCommand);
		String commandType = determineCommandType(commandFirstWord);
		String taskStatement = removeFirstWord(userCommand);
		Task taskDetails = setTaskDetails(taskStatement);
		
		Command parsedCommand = new Command(commandType, taskDetails);
		return parsedCommand;
	}
	
	//incomplete
	private static Task setTaskDetails(String taskStatement) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This operation helps us to overcome the variations in command type
	 * input by the user.
	 * 
	 * @param commandFirstWord
	 * 					is the first word of the user's command. 
	 * @return commandType
	 * 					groups all similar tasks under the same label
	 * 					thereby making execution easier.
	 */

	private static String determineCommandType(String commandFirstWord) {
/*		if(commandFirstWord.equalsIgnoreCase("add") ||
		   commandFirstWord.equalsIgnoreCase("create") ||
		   commandFirstWord.equalsIgnoreCase("+")){
			return COMMAND_ADD;
		} else if(commandFirstWord.equalsIgnoreCase("delete") ||
				  commandFirstWord.equalsIgnoreCase("remove") ||
				  commandFirstWord.equalsIgnoreCase("clear") ||
				  commandFirstWord.equalsIgnoreCase("-")){
			return COMMAND_DELETE;
		} else if(commandFirstWord.equalsIgnoreCase("modify") ||
				  commandFirstWord.equalsIgnoreCase("edit") ||
				  commandFirstWord.equalsIgnoreCase("change")){
			return COMMAND_MODIFY;
		} else if(commandFirstWord.equalsIgnoreCase("complete") ||
				  commandFirstWord.equalsIgnoreCase("finish") ||
				  commandFirstWord.equalsIgnoreCase("done")){
			return COMMAND_COMPLETE;
		} else if(commandFirstWord.equalsIgnoreCase("exit") ||
				  commandFirstWord.equalsIgnoreCase("close") ||
				  commandFirstWord.equalsIgnoreCase("quit")){
			return COMMAND_EXIT;
		} */
		return COMMAND_INVALID;
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
