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
	
	ArrayList<String> commandHistory = new ArrayList<String>();
	private static int indexOfCommand = -1;

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
		System.out.println("0. " + tempCmd.getCommandType());
		System.out.println("1. " + cmd.getCommandType());
		undoDeque.addFirst(execute(tempCmd));
		System.out.println("2. " + tempCmd.getCommandType());
		System.out.println("3. " + cmd.getCommandType());
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
			System.out.println("22");
			executeCommand.setCommandType(COMMAND_TYPE.DELETE_TASK);
			break;
		case DELETE_TASK:
			System.out.println("11");
			executeCommand.setCommandType(COMMAND_TYPE.ADD_TASK);
			System.out.println("33");
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
	
	public void addToCommandHistory(String command){
		indexOfCommand++;
		commandHistory.add(command);
	}
	
	public String moveUpCommandHistory() {
		if(indexOfCommand < 0) {
			return "";
		} else {
			return commandHistory.get(indexOfCommand--);
		}
	}
	
	public String moveDownCommandHistory() {
		if(indexOfCommand >= commandHistory.size()) {
			return "";
		}else {
			return commandHistory.get(indexOfCommand++);
		}
	}

	public void keyPressed(KeyEvent e) {

		int key = e.getKeyCode();

		if (key == KeyEvent.VK_UP) {
			System.out.println("helooooooooo");
		}

	}

}
