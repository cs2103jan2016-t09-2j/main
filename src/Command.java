
class Command {

	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, MODIFY_TASK, COMPLETE_TASK, EXIT
	};
	
	private Task taskDescription;
	private COMMAND_TYPE commandType;
	
	Command() {
		taskDescription = null;
		commandType = null;
	}
	
	// Parameterized Constructor that accepts a command word and task details
	Command(String commandFirstWord, Task newTaskDetails) {
		setCommandType(commandFirstWord);
		setTaskDetails(newTaskDetails);
	}
	
	//Parameterized Constructor that accepts another Command
	Command(Command newCommand) {
		this.commandType = newCommand.commandType;
		this.taskDescription = newCommand.taskDescription;
	}
	
	void setCommandType(String commandFirstWord){
		this.commandType = determineCommandType(commandFirstWord);
	}
	
	// It throws an error when the command type is Invalid.
	// Need to change it later on.
	protected COMMAND_TYPE getCommandType(){
		if(commandType == null) {
			throw new Error("Invalid Command Type");
		}
		return this.commandType;
	}

	void setTaskDetails(Task newTaskDetails){
		this.taskDescription = newTaskDetails;
	}
	
	protected Task getTaskDetails() {
		return this.taskDescription;
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
		if(commandFirstWord == null) {
			throw new Error("command type string cannot be null!");
		}
		
		if(commandFirstWord.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD_TASK;
		} else if(commandFirstWord.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE_TASK;
		} else if(commandFirstWord.equalsIgnoreCase("modify")) {
			return COMMAND_TYPE.MODIFY_TASK;
		} else if(commandFirstWord.equalsIgnoreCase("complete")) {
			return COMMAND_TYPE.COMPLETE_TASK;
		} else if(commandFirstWord.equalsIgnoreCase("exit")) {
			return COMMAND_TYPE.EXIT;
		}
		return null;
	}
}
