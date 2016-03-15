package ScheduleHacks;

import java.util.ArrayList;

public class OldCommand {
	
	public enum COMMAND_TYPE {
		ADD_TASK, DELETE_TASK, MODIFY_TASK, COMPLETE_TASK, SEARCH_TASK, VIEW_LIST
	};
	
	private ArrayList<Task> taskList;
	private COMMAND_TYPE commandType;
	private ArrayList<Integer> indexList;
	
	public OldCommand() {
		this.commandType = null;
		this.taskList = null;
		this.indexList = null;
	}

	// Parameterized Constructor
	public OldCommand(COMMAND_TYPE commandFirstWord, ArrayList<Task> newTaskList){
		setCommandType(commandFirstWord);
		setTaskList(newTaskList);
		setIndexList(new ArrayList<Integer>());
	}

	public OldCommand(COMMAND_TYPE commandFirstWord, ArrayList<Task> newTaskList, ArrayList<Integer> newIndexList){
		setCommandType(commandFirstWord);
		setTaskList(newTaskList);
		setIndexList(newIndexList);
	}

	/****************** SETTER METHODS ***********************/
	
	public void setCommandType(COMMAND_TYPE command) {
		this.commandType = command;
	}

	public void setTaskList(ArrayList<Task> newTaskList) {
		this.taskList = newTaskList;
	}

	public void setIndexList(ArrayList<Integer> newIndexList) {
		this.indexList = newIndexList;
	}

	/****************** GETTER METHODS ***********************/
	public COMMAND_TYPE getCommandType(){
		return this.commandType;
	}

	public ArrayList<Task> getTaskList() {
		return this.taskList;
	}

	public ArrayList<Integer> getIndexList() {
		return this.indexList;
	}

	/****************** OTHER METHODS ***********************/
}
