package ScheduleHacks;

import java.awt.event.KeyEvent;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import ScheduleHacks.OldCommand.COMMAND_TYPE;

public class History {

	private static History object = null;

	Deque<OldCommand> undoDeque;
	Deque<OldCommand> redoDeque;
	
	ArrayList<String> stringHistory = new ArrayList<String>();

	private History() {
		undoDeque = new ArrayDeque<OldCommand>();
		redoDeque = new ArrayDeque<OldCommand>();
	}

	public static History getInstance() {
		if (object == null) {
			object = new History();
		}
		return object;
	}

	public void addToUndoList(OldCommand cmd) {
		OldCommand tempCmd = new OldCommand(cmd.getCommandType(), cmd.getTaskList(), cmd.getIndexList());
		undoDeque.addFirst(execute(tempCmd));
	}

	public void addToRedoList(OldCommand cmd) {
		OldCommand tempCmd = new OldCommand(cmd.getCommandType(), cmd.getTaskList(), cmd.getIndexList());
		redoDeque.addFirst(execute(tempCmd));
	}

	public OldCommand getFromUndoList() throws Exception {
		if (undoDeque.isEmpty()) {
			throw new Exception("Empty Undo Stack");
		}
		OldCommand getUndo = undoDeque.removeFirst();
		addToRedoList(getUndo);
		return getUndo;
	}

	public OldCommand getFromRedoList() throws Exception {
		if (redoDeque.isEmpty()) {
			throw new Exception("Empty Redo Stack");
		}
		OldCommand getRedo = redoDeque.removeFirst();
		addToUndoList(getRedo);
		return getRedo;
	}

	public void clearRedoStack() {
		redoDeque.clear();
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
		case INCOMPLETE_TASK:
			executeCommand.setCommandType(COMMAND_TYPE.COMPLETE_TASK);
			break;
		default:
			break;
		}

		return executeCommand;
	}
	
	public void commandHistory(String command){
	
		stringHistory.add("");
		String display = command;
		
	}
	

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_UP) {
			System.out.println("helooooooooo");
		}

	}

}
