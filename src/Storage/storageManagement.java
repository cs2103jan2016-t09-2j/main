package Storage;


import java.util.Stack;

public class storageManagement {

	private Stack<listState> undoStack;
	private Stack<listState> redoStack;

	public storageManagement() {
		undoStack = new Stack<listState>();
		redoStack = new Stack<listState>();

	}

	public void saveState() {
		undoStack.push(copyListState());

	}

	public void prevState() {

	}

	private listState copyListState() {
		
		return null;
	}

}