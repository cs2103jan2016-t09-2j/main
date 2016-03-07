package Storage;

import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;

import ScheduleHacks.Task;

public class SnigdhaTempStorage {

	private static final Gson gson = new Gson();

	// My ArrayLists
	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();

	// Files to Store and Restore data
	private static final String toDoScheduledFile = "toDoScheduled";
	private static final String toDoFloatingFile = "toDoFloating";
	private static final String overdueScheduledFile = "overdueScheduled";
	private static final String completeScheduledFile = "completeScheduled";
	private static final String completeFloatingFile = "completeFloating";

	/****************** SETTER METHODS ***********************/
	private void setScheduledTasksToDo(ArrayList<Task> currentTaskList) {
		scheduledTasksToDo.clear();
		scheduledTasksToDo = currentTaskList;
	}

	private void setScheduledTasksOverDue(ArrayList<Task> currentTaskList) {
		scheduledTasksOverDue.clear();
		scheduledTasksOverDue = currentTaskList;
	}

	private void setScheduledTasksComplete(ArrayList<Task> currentTaskList) {
		scheduledTasksComplete.clear();
		scheduledTasksComplete = currentTaskList;
	}

	private void setFloatingTasksToDo(ArrayList<Task> currentTaskList) {
		floatingTasksToDo.clear();
		floatingTasksToDo = currentTaskList;
	}

	private void setFloatingTasksComplete(ArrayList<Task> currentTaskList) {
		floatingTasksComplete.clear();
		floatingTasksComplete = currentTaskList;
	}

	/****************** GETTER METHODS ***********************/
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

	/****************** OTHER METHODS ***********************/
	public void storeToFiles(ArrayList<Task> floatingTasksToDo, ArrayList<Task> floatingTasksComplete,
			ArrayList<Task> scheduledTasksToDo, ArrayList<Task> scheduledTasksComplete,
			ArrayList<Task> scheduledTasksOverDue) throws Exception {
		/*setFloatingTasksToDo(floatingTasksToDo);
		setFloatingTasksComplete(floatingTasksComplete);
		setScheduledTasksToDo(scheduledTasksToDo);
		setScheduledTasksComplete(scheduledTasksComplete);
		setScheduledTasksOverDue(scheduledTasksOverDue);*/

		writeToFile(toDoFloatingFile, floatingTasksToDo);
		writeToFile(toDoScheduledFile, scheduledTasksToDo);
		writeToFile(completeFloatingFile, floatingTasksComplete);
		writeToFile(completeScheduledFile, scheduledTasksComplete);
		writeToFile(overdueScheduledFile, scheduledTasksOverDue);
	}
	
	public void loadToList() throws Exception{
		setFloatingTasksComplete(new ArrayList<Task>());
		setFloatingTasksToDo(new ArrayList<Task>());
		setScheduledTasksComplete(new ArrayList<Task>());
		setScheduledTasksOverDue(new ArrayList<Task>());
		setScheduledTasksToDo(new ArrayList<Task>());

		readFromFile(toDoFloatingFile, floatingTasksToDo);
		readFromFile(toDoScheduledFile, scheduledTasksToDo);
		readFromFile(completeFloatingFile, floatingTasksComplete);
		readFromFile(completeScheduledFile, scheduledTasksComplete);
		readFromFile(overdueScheduledFile, scheduledTasksOverDue);
		
	}

	public void writeToFile(String fileName, ArrayList<Task> taskList) throws Exception {
		File f = new File(fileName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		for (Task newTask : taskList) {
			String json1 = gson.toJson(newTask) + "\n";
			bw.append(json1);
		}

		bw.close();
	}

	public void readFromFile(String fileName, ArrayList<Task> taskList) throws Exception {
		try {
			File file = new File(fileName);
			if (file.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					taskList.add(task);
				}
				br.close();
			}
		} catch (FileNotFoundException f) {
			System.out.println("File " + fileName + " cannot be found.");
		}
	}
/*
	public static void main(String[] args) throws Exception {
		// SnigdhaTempStorage obj = new SnigdhaTempStorage();
		// obj.writeToFile(fileName, taskList);
		Gson gson = new Gson();

		Task newTask = new Task();
		newTask.setDescription("Hello hi");
		newTask.setAsIncomplete();
		// newTask.setEndDate(LocalDate.now());
		// newTask.setEndTime(LocalTime.now());
		String json1 = gson.toJson(newTask);
		System.out.println(json1);
	}*/
}
