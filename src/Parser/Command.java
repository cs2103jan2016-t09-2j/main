package Parser;
import ScheduleHacks.Task;

public class Command {

	public enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, MODIFY_TASK, COMPLETE_TASK, EXIT
	};
	
	private static final int DEFAULT_INDEX_NUMBER = -1;
	//private static final int FIRST_INDEX = 0;
	
	private static final String[] COMMAND_ADD = {"add", "create", "+", "a"};
	private static final String[] COMMAND_DELETE = {"delete", "d", "-", "clear", "remove"};
	private static final String[] COMMAND_MODIFY = {"modify", "edit", "update", "change"};
	private static final String[] COMMAND_COMPLETE = {"complete", "done", "finish", 
								                      "completed", "finished"};
	private static final String[] COMMAND_EXIT = {"exit", "close", "quit"};
	private static final String   COMMAND_INVALID = null;
	
	private Task taskDescription;
	private COMMAND_TYPE commandType;
	private int indexNumber;
	
	public Command() {
		this.commandType = null;
		this.taskDescription = null;
		this.indexNumber = DEFAULT_INDEX_NUMBER;
	}
	
	// Parameterized Constructor that accepts a command word and task details
	public Command(String commandFirstWord, Task newTaskDetails)throws Exception{
		setCommandType(commandFirstWord);
		setTaskDetails(newTaskDetails);
		setIndexNumber(DEFAULT_INDEX_NUMBER);
	}
	
	
	
	public Command(String commandFirstWord, Task newTaskDetails, int newTaskIndex)throws Exception{
		setCommandType(commandFirstWord);
		setTaskDetails(newTaskDetails);
		setIndexNumber(newTaskIndex);
	}

	//Parameterized Constructor that accepts another Command
	public Command(Command newCommand) {
		this.commandType = newCommand.commandType;
		this.taskDescription = newCommand.taskDescription;
		//this.taskIndex = newCommand.taskIndex;
	}
	
	public void setCommandType(String commandFirstWord)throws Exception{
		this.commandType = determineCommandType(commandFirstWord);
	}
	
	public void setCommandType(COMMAND_TYPE commandFirstWord)throws Exception{
		this.commandType = commandFirstWord;
	}
	
	// It throws an error when the command type is Invalid.
	// Need to change it later on.
	public COMMAND_TYPE getCommandType()throws Exception{
		if(commandType == null) {
			throw new Exception("Invalid Command Type");
		}
		return this.commandType;
	}

	public void setTaskDetails(Task newTaskDetails){
		this.taskDescription = newTaskDetails;
	}
	
	public Task getTaskDetails() {
		return this.taskDescription;
	}
	
	public void setIndexNumber(int index) {
		this.indexNumber = index;
	}
	
	public int getIndexNumber() {
		return this.indexNumber;
	}
	
	/**
	 * This operation is used to find what does the user want to do to the Task.
	 * 
	 * @param commandFirstWord
	 *            is the first word of the UserCommand. It has been reassigned by
	 *            the Parser to account for variations. 
	 *            Eg.; both "add" and "create" are reassigned as "add"
	 * @return the commandType, so that necessary actions can be performed.
	 */
	private COMMAND_TYPE determineCommandType(String commandFirstWord)throws Exception {
		if(commandFirstWord.equals(COMMAND_INVALID)) {
			throw new Exception("command type string cannot be null!");
		}
		
		if(hasInDictionary(COMMAND_ADD, commandFirstWord)) {
			return COMMAND_TYPE.ADD_TASK;
		} else if(hasInDictionary(COMMAND_DELETE, commandFirstWord)) {
			return COMMAND_TYPE.DELETE_TASK;
		} else if(hasInDictionary(COMMAND_MODIFY, commandFirstWord)) {
			return COMMAND_TYPE.MODIFY_TASK;
		} else if(hasInDictionary(COMMAND_COMPLETE, commandFirstWord)) {
			return COMMAND_TYPE.COMPLETE_TASK;
		} else if(hasInDictionary(COMMAND_EXIT, commandFirstWord)) {
			return COMMAND_TYPE.EXIT;
		}
		throw new Exception("Invalid Command Type.");
	}

	/**
	 * This operation helps us to overcome the variations in command type
	 * input by the user.
	 * 
	 * @param commandFirstWord
	 * 					is the first word of the user's command. 
	 * @param commandDictionary
	 * 					is the String dictionary passed for determineCommandType()
	 * 
	 * @return 
	 * 			true if the first word from the user's command is contained in the
	 *          respective dictionary, otherwise false
	 */
	private boolean hasInDictionary(String[] commandDictionary, String commandFirstWord) {
		for(String command: commandDictionary) {
			if(command.equalsIgnoreCase(commandFirstWord))
				return true;
		}
		return false;
	}
}
