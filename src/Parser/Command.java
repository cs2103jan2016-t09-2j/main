package Parser;
import ScheduleHacks.Task;

public class Command {

	public enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, MODIFY_TASK, COMPLETE_TASK, EXIT
	};
	
	private static final String[] COMMAND_ADD = {"add", "create", "+", "a"};
	private static final String[] COMMAND_DELETE = {"delete", "d", "-", "clear", "remove"};
	private static final String[] COMMAND_MODIFY = {"modify", "edit", "update", "change"};
	private static final String[] COMMAND_COMPLETE = {"complete", "done", "finish", 
								                      "completed", "finished"};
	private static final String[] COMMAND_EXIT = {"exit", "close", "quit"};
	private static final String   COMMAND_INVALID = null;
	
	private Task taskDescription;
	private COMMAND_TYPE commandType;
	private Integer taskIndex;
	
	public Command() {
		this.taskDescription = null;
		this.commandType = null;
		this.taskIndex = null;
	}
	
	// Parameterized Constructor that accepts a command word and task details
	public Command(String commandFirstWord, Task newTaskDetails, Integer newTaskIndex) {
		setCommandType(commandFirstWord);
		setTaskDetails(newTaskDetails);
		setTaskIndex(newTaskIndex);
	}

	//Parameterized Constructor that accepts another Command
	public Command(Command newCommand) {
		this.commandType = newCommand.commandType;
		this.taskDescription = newCommand.taskDescription;
		this.taskIndex = newCommand.taskIndex;
	}
	
	private void setCommandType(String commandFirstWord){
		this.commandType = determineCommandType(commandFirstWord);
	}
	
	// It throws an error when the command type is Invalid.
	// Need to change it later on.
	public COMMAND_TYPE getCommandType(){
		if(commandType == null) {
			throw new Error("Invalid Command Type");
		}
		return this.commandType;
	}

	private void setTaskDetails(Task newTaskDetails){
		this.taskDescription = newTaskDetails;
	}
	
	public Task getTaskDetails() {
		return this.taskDescription;
	}
	
	private void setTaskIndex(Integer newTaskIndex) {
		this.taskIndex = newTaskIndex;
	}
	
	public Integer getTaskIndex() {
		return this.taskIndex;
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
	private COMMAND_TYPE determineCommandType(String commandFirstWord) {
		if(commandFirstWord.equals(COMMAND_INVALID)) {
			throw new Error("command type string cannot be null!");
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
		return null;
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
