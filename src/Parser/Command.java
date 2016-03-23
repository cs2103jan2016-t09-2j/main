package Parser;

import java.util.ArrayList;

import ScheduleHacks.Task;

public class Command {

	public enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, MODIFY_TASK, COMPLETE_TASK, SEARCH_TASK, VIEW_LIST, UNDO_TASK, REDO_TASK, SET_DIRECTORY, HOME, EXIT
	};

	public boolean isFirstWordCommand;

	/************** INSTANCE VARIABLES ********************/
	private Task taskDescription;
	private COMMAND_TYPE commandType;
	private ArrayList<Integer> indexList;

	/****************** CONSTRUCTORS **********************/

	public Command() {
		this.commandType = null;
		this.taskDescription = null;
		this.indexList = null;
	}

	// Parameterized Constructor that accepts a command word and task details
	public Command(String commandFirstWord, Task newTaskDetails) throws Exception {
		setCommandType(commandFirstWord);
		setTaskDetails(newTaskDetails);
		setIndexList(new ArrayList<Integer>());
	}

	public Command(String commandFirstWord, Task newTaskDetails, ArrayList<Integer> newIndexList) throws Exception {
		setCommandType(commandFirstWord);
		setTaskDetails(newTaskDetails);
		setIndexList(newIndexList);
	}

	// Parameterized Constructor that accepts another Command
	public Command(Command newCommand) {
		this.commandType = newCommand.commandType;
		this.taskDescription = newCommand.taskDescription;
		this.indexList = newCommand.indexList;
	}

	/****************** SETTER METHODS ***********************/
	public void setCommandType(String commandFirstWord) throws Exception {
		this.commandType = determineCommandType(commandFirstWord);
	}

	public void setCommandType(COMMAND_TYPE commandFirstWord) throws Exception {
		this.commandType = commandFirstWord;
	}

	public void setTaskDetails(Task newTaskDetails) {
		this.taskDescription = newTaskDetails;
	}

	public void setIndexList(ArrayList<Integer> newIndexList) {
		this.indexList = newIndexList;
	}

	/****************** GETTER METHODS ***********************/

	// It throws an error when the command type is Invalid.
	// Need to change it later on.
	public COMMAND_TYPE getCommandType() throws Exception {
		if (commandType == null) {
			throw new Exception("Invalid Command Type");
		}
		return this.commandType;
	}

	public Task getTaskDetails() {
		return this.taskDescription;
	}

	public ArrayList<Integer> getIndexList() {
		return this.indexList;
	}

	/****************** OTHER METHODS ***********************/

	/**
	 * This operation is used to find what does the user want to do to the Task.
	 * 
	 * @param commandFirstWord
	 *            is the first word of the UserCommand. It has been reassigned
	 *            by the Parser to account for variations. Eg.; both "add" and
	 *            "create" are reassigned as "add"
	 * @return the commandType, so that necessary actions can be performed.
	 */
	private COMMAND_TYPE determineCommandType(String commandFirstWord) throws Exception {
		if (commandFirstWord.equals(ParserConstants.COMMAND_INVALID)) {
			throw new Exception("command type string cannot be null!");
		}
		if (commandFirstWord.equals(ParserConstants.COMMAND_EMPTY)) {
			throw new Exception("command type string cannot be empty!");
		}

		isFirstWordCommand = true;
		if (hasInDictionary(ParserConstants.COMMAND_ADD, commandFirstWord)) {
			return COMMAND_TYPE.ADD_TASK;
		} else if (hasInDictionary(ParserConstants.COMMAND_DELETE, commandFirstWord)) {
			return COMMAND_TYPE.DELETE_TASK;
		} else if (hasInDictionary(ParserConstants.COMMAND_MODIFY, commandFirstWord)) {
			return COMMAND_TYPE.MODIFY_TASK;
		} else if (hasInDictionary(ParserConstants.COMMAND_COMPLETE, commandFirstWord)) {
			return COMMAND_TYPE.COMPLETE_TASK;
		} else if (hasInDictionary(ParserConstants.COMMAND_SEARCH, commandFirstWord)) {
			return COMMAND_TYPE.SEARCH_TASK;
		} else if (hasInDictionary(ParserConstants.COMMAND_VIEW, commandFirstWord)) {
			return COMMAND_TYPE.VIEW_LIST;
		} else if (hasInDictionary(ParserConstants.COMMAND_UNDO, commandFirstWord)) {
			return COMMAND_TYPE.UNDO_TASK;
		} else if (hasInDictionary(ParserConstants.COMMAND_REDO, commandFirstWord)) {
			return COMMAND_TYPE.REDO_TASK;
		} else if (hasInDictionary(ParserConstants.COMMAND_SET_DIRECTORY, commandFirstWord)) {
			return COMMAND_TYPE.SET_DIRECTORY;
		}else if (hasInDictionary(ParserConstants.COMMAND_HOME, commandFirstWord)) {
			return COMMAND_TYPE.HOME;
		} else if (hasInDictionary(ParserConstants.COMMAND_EXIT, commandFirstWord)) {
			return COMMAND_TYPE.EXIT;
		} else {
			isFirstWordCommand = false;
			return COMMAND_TYPE.ADD_TASK;
		}
	}

	/**
	 * This operation helps us to overcome the variations in command type input
	 * by the user.
	 * 
	 * @param commandFirstWord
	 *            is the first word of the user's command.
	 * @param commandDictionary
	 *            is the String dictionary passed for determineCommandType()
	 * 
	 * @return true if the first word from the user's command is contained in
	 *         the respective dictionary, otherwise false
	 */
	private boolean hasInDictionary(String[] commandDictionary, String commandFirstWord) {
		for (String command : commandDictionary) {
			if (command.equalsIgnoreCase(commandFirstWord))
				return true;
		}
		return false;
	}
}
