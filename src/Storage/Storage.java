package Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;

import java.util.logging.Logger;
import java.util.logging.Level;

import ScheduleHacks.Task;

public class Storage {

	private static final Gson gson = new Gson();

	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	
	private ArrayList<ArrayList<Task>> archiveList = new ArrayList<ArrayList<Task>>();
	private ArrayList<ArrayList<Task>> currentList = new ArrayList<ArrayList<Task>>();
	
	private static final String archiveFilename = "C:\\ScheduleHacksFile\\Archive\\";
	private static final String ToDoDirectory = "C:\\ScheduleHacksFile\\ToDo\\";
	private static final String oveduedDirectory = "C:\\ScheduleHacksFile\\Overdued\\";
	
	private static final String toDoScheduledFile = "toDoScheduled.json";
	private static final String toDoFloatingFile = "toDoFloating.json";
	private static final String overdueScheduledFile = "overdueScheduled.json";
	private static final String completeScheduledFile = "completeScheduled.json";
	private static final String completeFloatingFile = "completeFloating.json";
	
	
	// create and store files in respective Directory

	public void setDirectory(){
		
		fileDirectory.createAllDirectories();
		
	}
	
	public void storeDirectory(){
		
		
		
	}
	
	//Setter Methods
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

	//Getter Methods
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

	//storage methods
	public void storeToFiles(ArrayList<Task> floatingTasksToDo, ArrayList<Task> floatingTasksComplete,
			ArrayList<Task> scheduledTasksToDo, ArrayList<Task> scheduledTasksComplete,
			ArrayList<Task> scheduledTasksOverDue) throws Exception {

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

//		readFromFile(toDoFloatingFile, floatingTasksToDo);
//		readFromFile(toDoScheduledFile, scheduledTasksToDo);
//		readFromFile(completeFloatingFile, floatingTasksComplete);
//		readFromFile(completeScheduledFile, scheduledTasksComplete);
//		readFromFile(overdueScheduledFile, scheduledTasksOverDue);
		
		readFromArchiveFile(completeScheduledFile, completeFloatingFile,
				floatingTasksComplete,scheduledTasksComplete);
		readFromCurrentFile(toDoScheduledFile, toDoFloatingFile,overdueScheduledFile,
				scheduledTasksToDo,floatingTasksToDo,scheduledTasksOverDue);
		
		loadArchiveList(archiveList,floatingTasksComplete,scheduledTasksComplete);
		loadCurrentList(currentList,scheduledTasksToDo,floatingTasksToDo,scheduledTasksOverDue);
		
		
	}

	public void writeToFile(String fileName, ArrayList<Task> taskList) throws Exception {
		File f = new File(fileName);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		for (Task newTask : taskList) {
			String json = gson.toJson(newTask);
			bw.write(json);
			bw.newLine();
		}

		bw.close();
	}

//	public void readFromFile(String fileName, ArrayList<Task> taskList) throws Exception {
//		try {
//			File file = new File(fileName);
//			if (file.exists()) {
//				String taskDetails = "";
//				BufferedReader br = new BufferedReader(new FileReader(file));
//				while ((taskDetails = br.readLine()) != null) {
//					Task task = gson.fromJson(taskDetails, Task.class);
//					taskList.add(task);
//				}
//				br.close();
//			}
//		} catch (FileNotFoundException f) {
//			System.out.println("File " + fileName + " cannot be found.");
//		}
//	}
	
	public void readFromArchiveFile(String completeScheduled, String completeFloating,
			ArrayList<Task> scheduledTasksComplete,ArrayList<Task> floatingTasksComplete) throws Exception {
		
		try {
			File file1 = new File(completeScheduled);
			File file2 = new File(completeFloating);
			if (file1.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file1));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					scheduledTasksComplete.add(task);
				}
				
				br.close();
			}
			
			if (file2.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file2));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					 floatingTasksComplete.add(task);
				}
				br.close();
			} 
	
		}
			
			catch (FileNotFoundException f) {
				
			System.out.println("File " + completeScheduled + " cannot be found.");
		}

	}
	
	public void readFromCurrentFile(String toDoScheduled, String toDoFloating,String overdueScheduled,
			ArrayList<Task> toDoScheduledFile,ArrayList<Task> toDoFloatingFile,ArrayList<Task> overdueScheduledFile) throws Exception {		
		try {
			File file1 = new File(toDoScheduled);
			File file2 = new File(toDoFloating);
			File file3 = new File(overdueScheduled);
			
			if (file1.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file1));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					toDoScheduledFile.add(task);
				}
				
				br.close();
			}
			
			if (file2.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file2));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					toDoFloatingFile.add(task);
				}
				br.close();
			} 
			
			if (file3.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file3));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					overdueScheduledFile.add(task);
				}
				br.close();
			} 
		}
			
			catch (FileNotFoundException f) {
				
			System.out.println("File " + " cannot be found.");
		}

	}
	
	public void loadArchiveList(ArrayList<ArrayList<Task>> archiveList,ArrayList<Task> scheduledTasksComplete,
			ArrayList<Task> floatingTasksComplete){
		
		archiveList.add(scheduledTasksComplete);
		archiveList.add(floatingTasksComplete);

	}
	
	public void loadCurrentList(ArrayList<ArrayList<Task>> currentList,ArrayList<Task> toDoScheduledTasks,
			ArrayList<Task> toDoFloatingTask, ArrayList<Task> overdueScheduledTask ){
		
		currentList.add(toDoScheduledTasks);
		currentList.add(toDoFloatingTask);
		currentList.add(overdueScheduledTask);
		
	}

}
