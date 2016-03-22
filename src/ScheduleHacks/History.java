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
		if(object == null) {
			object = new History();
		}
		return object;
	}

	public void addToUndoList(OldCommand cmd) {
		undoStack.push(execute(cmd));
	}

	public OldCommand getFromUndoList() {
		redoStack.push(undoStack.pop());
		return undoStack.pop();
	}
	
//	public void addToRedoList(OldCommand cmd) {
//		redoStack.push(undoStack.push(execute(cmd)));
//	}
	
	public OldCommand getFromRedoList() {
		undoStack.push(redoStack.pop());
		return redoStack.pop();
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
