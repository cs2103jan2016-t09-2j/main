package Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
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

	private static final String defaultPathName = "C:\\ScheduleHacks";
	private static final String setPathName = setDirectoryName();
	private static final String usedPathName = currentDirectoryName();
	

	private static final String currentFile = "currentFile.json";
	private static final String archiveFile = "archiveFile.json";

	public Storage(){
	
	}
	
	// create and store files in respective Directory
	public static String setDirectoryName() {
		
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("set directory: ");
		String fileName = sc.nextLine();

		return fileName;
	}
	
	
	public String changeDirectoryName(String currentName){
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("change directory: ");
		String destDir = sc.nextLine();
		
		fileDirectory.changeDirectory(currentName,destDir);
		return destDir;
	}
	
	//decides current directory name
	public static String currentDirectoryName(){
		
		if(setPathName != null && !setPathName.isEmpty()){		
			fileDirectory.createMainDirectory(setPathName);
			return setPathName;		
		}
		
		else{
			fileDirectory.createMainDirectory(defaultPathName);
			return defaultPathName;
		}	
	}
	
	/*
	 Setter Methods
	*/
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

	/*
	/ Getter Methods
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

	// storage methods
	public void storeToFiles(ArrayList<Task> floatingTasksToDo, ArrayList<Task> floatingTasksComplete,
			ArrayList<Task> scheduledTasksToDo, ArrayList<Task> scheduledTasksComplete,
			ArrayList<Task> scheduledTasksOverDue) throws Exception {

		writeToArchiveFile(floatingTasksComplete, scheduledTasksComplete);
		writeToCurrentFile(scheduledTasksToDo, floatingTasksToDo, scheduledTasksOverDue);
	}

	public void loadToList() throws Exception {

		setFloatingTasksComplete(new ArrayList<Task>());
		setFloatingTasksToDo(new ArrayList<Task>());
		setScheduledTasksComplete(new ArrayList<Task>());
		setScheduledTasksOverDue(new ArrayList<Task>());
		setScheduledTasksToDo(new ArrayList<Task>());

		readFromArchiveFile(floatingTasksComplete, scheduledTasksComplete);
		readFromCurrentFile(scheduledTasksToDo, floatingTasksToDo, scheduledTasksOverDue);
	}

	public void writeToArchiveFile(ArrayList<Task> scheduledTasksComplete, ArrayList<Task> floatingTasksComplete)
			throws Exception {

		File f1 = new File(usedPathName,archiveFile);
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(f1));

		for (Task newTask1 : scheduledTasksComplete) {
			String json1 = gson.toJson(newTask1);
			bw.write(json1);
			bw.newLine();
		}

		for (Task newTask2 : scheduledTasksComplete) {
			String json1 = gson.toJson(newTask2);
			bw.write(json1);
			bw.newLine();
		}
		bw.close();
	}

	public void writeToCurrentFile(ArrayList<Task> toDoScheduledFile, ArrayList<Task> toDoFloatingFile,
			ArrayList<Task> overdueScheduledFile) throws Exception {

		File f = new File(usedPathName,currentFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		for (Task newTask : toDoScheduledFile) {
			String json = gson.toJson(newTask);
			bw.write(json);
			bw.newLine();
		}

		for (Task newTask : toDoFloatingFile) {
			String json = gson.toJson(newTask);
			bw.write(json);
			bw.newLine();
		}

		for (Task newTask : overdueScheduledFile) {
			String json = gson.toJson(newTask);
			bw.write(json);
			bw.newLine();
		}

		bw.close();

	}

	public void readFromArchiveFile(ArrayList<Task> scheduledTasksComplete, ArrayList<Task> floatingTasksComplete)
			throws Exception {

		try {

			File file = new File(usedPathName,archiveFile);
			
			if (file.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					if (task.isFloatingTask()) {
						floatingTasksComplete.add(task);
					} else {
						scheduledTasksComplete.add(task);
					}		
				}
				br.close();
			}

		}

		catch (FileNotFoundException f) {

			System.out.println("File " + archiveFile + " cannot be found.");
		}

	}

	public void readFromCurrentFile(ArrayList<Task> toDoScheduledFile, ArrayList<Task> toDoFloatingFile,
			ArrayList<Task> overdueScheduledFile) throws Exception {
		try {
			File file = new File(usedPathName,currentFile);

			if (file.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					if (task.isFloatingTask()) {
						toDoFloatingFile.add(task);
					} else {
						LocalDateTime present = LocalDateTime.now();
						LocalDateTime endDateTime = LocalDateTime.of(task.getEndDate(), task.getEndTime());
						if (endDateTime.isBefore(present)) {
							overdueScheduledFile.add(task);
						} else {
							toDoScheduledFile.add(task);
						}
					}

				}
				br.close();
			}
		}

		catch (FileNotFoundException f) {
			System.out.println("File " + currentFile + " cannot be found.");
		}

	}

}
