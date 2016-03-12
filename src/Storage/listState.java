package Storage;


import java.util.ArrayList;
import ScheduleHacks.Task;

public class listState {
	ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();

	public listState() {
		
	}

	public void setScheduledTasksToDo(ArrayList<Task> scheduledTasksToDo) {
		this.scheduledTasksToDo = scheduledTasksToDo;
	}

	public void setScheduledTasksOverDue(ArrayList<Task> scheduledTasksOverDue) {
		this.scheduledTasksOverDue = scheduledTasksOverDue;
	}

	public void setScheduledTasksComplete(ArrayList<Task> scheduledTasksComplete) {
		this.scheduledTasksComplete = scheduledTasksComplete;
	}

	public void setFloatingTasksToDo(ArrayList<Task> floatingTasksToDo) {
		this.floatingTasksToDo = floatingTasksToDo;
	}

	public void setFloatingTasksComplete(ArrayList<Task> floatingTasksComplete) {
		this.floatingTasksComplete = floatingTasksComplete;
	}

	/*
	 * / Getter Methods
	 */
	
	public ArrayList<Task> getScheduledTasksToDo() {
		return scheduledTasksToDo;
	}

	public ArrayList<Task> getScheduledTasksOverDue() {
		return scheduledTasksOverDue;
	}

	public ArrayList<Task> getScheduledTasksComplete() {
		return scheduledTasksComplete;
	}

	public ArrayList<Task> getFloatingTasksToDo() {
		return floatingTasksToDo;
	}

	public ArrayList<Task> getFloatingTasksComplete() {
		return floatingTasksComplete;
	}

}
