package ScheduleHacks;

import java.util.Stack;

import ScheduleHacks.OldCommand.COMMAND_TYPE;

public class History {

	private static History object = null;

	Stack<OldCommand> undoStack;
	Stack<OldCommand> redoStack;

	private History() {
		undoStack = new Stack<OldCommand>();
		redoStack = new Stack<OldCommand>();
	}

	public static History getInstance() {
		if (object == null) {
			object = new History();
		}
		return object;
	}

	public void addToUndoList(OldCommand cmd) {
		OldCommand tempCmd = new OldCommand(cmd.getCommandType(), cmd.getTaskList(), cmd.getIndexList());
		undoStack.push(execute(tempCmd));
	}

	public void addToRedoList(OldCommand cmd) {
		OldCommand tempCmd = new OldCommand(cmd.getCommandType(), cmd.getTaskList(), cmd.getIndexList());
		redoStack.push(execute(tempCmd));
	}

	public OldCommand getFromUndoList() throws Exception {
		if(undoStack.isEmpty()) {
			throw new Exception ("Empty Undo Stack");
		}
		OldCommand getUndo = undoStack.pop();
		addToRedoList(getUndo);
		return getUndo;
	}

	public OldCommand getFromRedoList()throws Exception {
		if(redoStack.isEmpty()) {
			throw new Exception ("Empty Redo Stack");
		}
		OldCommand getRedo = redoStack.pop();
		return getRedo;
	}

	public void clearRedoStack() {
		redoStack.clear();
	}

	private OldCommand execute(OldCommand executeCommand) {

		COMMAND_TYPE commandType = executeCommand.getCommandType();

		switch (commandType) {
		case ADD_TASK:
			executeCommand.setCommandType(COMMAND_TYPE.DELETE_TASK);
			break;
		case DELETE_TASK:
			executeCommand.setCommandType(COMMAND_TYPE.ADD_TASK);
			break;
		case MODIFY_TASK:
			/*
			 * For modify the Task list contains two tasks The first one is the
			 * oldTask The second one is the newTask
			 */
			executeCommand.setCommandType(COMMAND_TYPE.MODIFY_TASK);
			break;
		case COMPLETE_TASK:
			executeCommand.setCommandType(COMMAND_TYPE.INCOMPLETE_TASK);
			break;
		default:
			break;
		}

		return executeCommand;
	}

}
