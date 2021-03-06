//@@author A0125258E
package ScheduleHacks;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import ScheduleHacks.OldCommand.COMMAND_TYPE;

public class History {

	/* Singleton */
	private static History object;

	Deque<OldCommand> undoDeque;
	Deque<OldCommand> redoDeque;

	ArrayList<String> commandHistory;
	private static int indexOfCommand = 0;

	private History() {
		undoDeque = new ArrayDeque<OldCommand>();
		redoDeque = new ArrayDeque<OldCommand>();
		commandHistory = new ArrayList<String>();
	}

	/*
	 * This method is the getInstance method for the singleton pattern of
	 * History. It initializes a new History if History is null, else returns
	 * the current instance of History.
	 */
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

	/*
	 * This method stores the history of commands that users type into the user
	 * interface
	 */
	public void addToCommandHistory(String command) {
		commandHistory.add(new String(command));
	}

	public void removeLastCommandFromHistory() {
		if (indexOfCommand > 0) {
			commandHistory.remove(indexOfCommand - 1);
		}
		setIndexCommandHistory();
	}

	/*
	 * Subsequent methods retrieves the index of the tasks contained in the
	 * command history ArrayList
	 */
	public String moveUpCommandHistory() {
		if (indexOfCommand <= 0 || commandHistory == null || commandHistory.size() < indexOfCommand) {
			indexOfCommand = -1;
			return null;
		} else {
			indexOfCommand--;
			return commandHistory.get(indexOfCommand);
		}
	}

	public String moveDownCommandHistory() {
		indexOfCommand++;
		if (indexOfCommand < commandHistory.size()) {
			return commandHistory.get(indexOfCommand);
		} else {
			setIndexCommandHistory();
			return null;
		}
	}

	public void setIndexCommandHistory() {
		if (commandHistory != null) {
			indexOfCommand = commandHistory.size();
		} else {
			indexOfCommand = 0;
		}
	}

}
