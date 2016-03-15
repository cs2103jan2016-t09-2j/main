package ScheduleHacks;

import java.util.Stack;

public class History {
	
	private static History object = null;
	
	Stack<CommandState> undoStack;
	Stack<CommandState> redoStack;
	
	private History() {
		undoStack = new Stack<CommandState>();
		redoStack = new Stack<CommandState>();

	}
	
	public static History getInstance() {
		if(object == null) {
			object = new History();
		}
		return object;
	}

	public void saveState() {
		undoStack.push(copyListState());

	}

	public void prevState() {

	}

	private CommandState copyListState() {
		
		return null;
	}

}
