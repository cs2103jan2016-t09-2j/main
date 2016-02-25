import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

class Logic {
	
	Command.COMMAND_TYPE executeCommand;
	Parser parsingUserInput;
	Command existingCommand;
	Task executeTask;
	
	private ArrayList <Task> floatingTasksToDo = new ArrayList <Task> ();
	private ArrayList <Task> floatingTasksComplete = new ArrayList <Task> ();
	private ArrayList <Task> scheduledTasksToDo = new ArrayList <Task> ();
	private ArrayList <Task> scheduledTasksComplete = new ArrayList <Task> ();
	private ArrayList <Task> scheduledTasksOverDue = new ArrayList <Task> ();
	private String originalDescription, commandFirstWord;
	private LocalDate start_date, due_date;
	private LocalTime start_time, due_time;
	
	public void passToParser(String originalDescription){
		parsingUserInput = new Parser(originalDescription);
	}
	
	public Command.COMMAND_TYPE getCommand() {
		executeCommand = existingCommand.determineCommandType(commandFirstWord);
		return executeCommand;
	}
	
	public Task getTaskDescription() {
		return executeTask = existingCommand.getTaskDetails();
	}
	
	public void execute(Command.COMMAND_TYPE executeCommand,Task executeTask) {
		switch (executeCommand) {
		case ADD_TASK:
			addTask(executeTask);
			break;
		case DELETE_TASK:
			deleteTask(executeTask);
			break;
		case MODIFY_TASK:
			modifyTask(executeTask);
		case COMPLETE_TASK:
			completeTask(executeTask);
		case EXIT:
			exit();	
		}
	}
	
	private void addTask(Task executeTask) {
		start_date = executeTask.getStartDate();
		due_date = executeTask.getDueDate();
		start_time = executeTask.getStartTime();
		due_time = executeTask.getDueTime();
		
		if (executeTask.isScheduledTask()){
			if ((due_date.compareTo(start_date)>0)) {
				scheduledTasksToDo.add(executeTask);
			}
			else if (due_date.compareTo(start_date)<0) {
				scheduledTasksOverDue.add(executeTask);
			}
			else {
				if (start_time.compareTo(due_time)<0){
					scheduledTasksToDo.add(executeTask);
				}
				else {
					scheduledTasksOverDue.add(executeTask);
				}
			}
		}
		else if (executeTask.isFloatingTask()) {
			/*floating tasks will always automatically be todo unless marked as complete, therefore
			 * should have an if condition marking floating task as complete and in else statement
			 * floating task automatically is added to floatingtasktodo array list
			 */
		}
	}
	
	
	
}
