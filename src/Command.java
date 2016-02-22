
class Command {

	enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, CHANGE_TASK, COMPLETE_TASK, EXIT
	};
	
	Task taskDescription;
	
	Command() {
		taskDescription = null;
	}
	
	void setCommandType(){
		// still working on this part
	}
	
	void setTaskDetails(Task newTaskDetails){
		this.taskDescription = newTaskDetails;
	}
	
	Task getTaskDetails() {
		return this.taskDescription;
	}
}
