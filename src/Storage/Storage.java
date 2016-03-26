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
import com.google.gson.JsonSyntaxException;

import java.util.logging.Logger;
import java.util.logging.Level;

import ScheduleHacks.Task;
import ScheduleHacks.LogFile;

public class Storage {

	private static final Gson gson = new Gson();

	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();

	private static final String defaultPathName = "C:\\ScheduleHacks";
	private static String currentPathName;

	LogFile myLogger = new LogFile();

	private static final String storageLocFile = "DataLocation.txt";
	private static final String currentFile = "currentFile.json";
	private static final String archiveFile = "archiveFile.json";

	private static Storage object;

	// Logging messages
	private static final String LOG_IO_EXCEPTION = "Encountered Input Output Exception";
	private static final String LOG_ARCHIVE_FILE_NOT_FOUND = "Archive file not found";
	private static final String LOG_CURRENT_FILE_NOT_FOUND = "Current file not found";
	private static final String LOG_JSONSYNTAX_EXCEPTION = "Encountered JsonSyntax Exception";
	private static final String LOG_WRITING_ARCHIVE_FILE = "Writing to archive file";
	private static final String LOG_WRITING_CURRENT_FILE = "Writing to current file";
	private static final String LOG_ERROR_WRITE_ARCHIVE_FILE = "Unable to write to archive file";
	private static final String LOG_ERROR_WRITE_CURRENT_FILE = "Unable to write to current file";
	private static final String LOG_WROTE_ARCHIVE_FILE = "Write to archive file successful";
	private static final String LOG_WROTE_CURRENT_FILE = "Write to current file successful";
	private static final String LOG_READING_ARCHIVE_FILE = "Reading archive file";
	private static final String LOG_READING_CURRENT_FILE = "Reading current file";

	//Assertion message
	private static final String ASSERTION_NULL_PARAMETER = "Error. Null input passed.";
	
	private Storage() {
		
		initStorage();
		
	}


	private void initStorage() {
		if(currentPathName == null || currentPathName.isEmpty()){
			try {
				File file = new File(storageLocFile);
				
				if(!file.exists()||file.length()==0){
					currentPathName = defaultPathName;
				
				}
				else{
					BufferedReader br = new BufferedReader(new FileReader(file));
					currentPathName =br.readLine();
					br.close();
				}
			
			} catch (Exception e) {
			
				e.printStackTrace();
			}		
			fileDirectory.createMainDirectory(currentPathName);					
		}
	}


	// apply singleton
	public static Storage getInstance() {
		if (object == null) {
			object = new Storage();
		}
		return object;
	}

	// decides current directory name
	public void setCurrentPathName(String inputDirectory) {	
		if(inputDirectory != null && !inputDirectory.isEmpty()){
			if(inputDirectory.equalsIgnoreCase("default")){
				inputDirectory = defaultPathName;
			}
	
			String oldDirectoryName = currentPathName;
			currentPathName = inputDirectory;
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(storageLocFile )));
				bw.write(currentPathName);
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fileDirectory.changeDirectory(oldDirectoryName, currentPathName);	
				
		}
		
	}
	
	public static String getCurrentPathName(){
		return currentPathName;
	}
	
	
	/*
	 * Setter Methods
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

	// storage methods
	public void storeToFiles(ArrayList<Task> floatingTasksToDo, ArrayList<Task> floatingTasksComplete,
			ArrayList<Task> scheduledTasksToDo, ArrayList<Task> scheduledTasksComplete,
			ArrayList<Task> scheduledTasksOverDue) {

		try {
			myLogger.log(Level.INFO, LOG_WRITING_ARCHIVE_FILE);
			writeToArchiveFile(floatingTasksComplete, scheduledTasksComplete);
		} catch (Exception e1) {
			myLogger.log(Level.WARNING, LOG_ERROR_WRITE_ARCHIVE_FILE);
			e1.printStackTrace();
		}
		try {
			myLogger.log(Level.INFO, LOG_WRITING_CURRENT_FILE);
			writeToCurrentFile(scheduledTasksToDo, floatingTasksToDo, scheduledTasksOverDue);
		} catch (Exception e) {
			myLogger.log(Level.WARNING, LOG_ERROR_WRITE_CURRENT_FILE);
			e.printStackTrace();
		}
	}

	public void loadToList() {

		setFloatingTasksComplete(new ArrayList<Task>());
		setFloatingTasksToDo(new ArrayList<Task>());
		setScheduledTasksComplete(new ArrayList<Task>());
		setScheduledTasksOverDue(new ArrayList<Task>());
		setScheduledTasksToDo(new ArrayList<Task>());

		try {
			myLogger.log(Level.INFO, LOG_READING_ARCHIVE_FILE);
			readFromArchiveFile(floatingTasksComplete, scheduledTasksComplete);
		} catch (Exception e) {
			myLogger.log(Level.INFO, LOG_ARCHIVE_FILE_NOT_FOUND);
			e.printStackTrace();
		}
		try {
			myLogger.log(Level.INFO, LOG_READING_CURRENT_FILE);
			readFromCurrentFile(scheduledTasksToDo, floatingTasksToDo, scheduledTasksOverDue);
		} catch (Exception e) {
			myLogger.log(Level.INFO, LOG_CURRENT_FILE_NOT_FOUND);
			e.printStackTrace();
		}
	}

	public void writeToArchiveFile(ArrayList<Task> scheduledTasksComplete, ArrayList<Task> floatingTasksComplete) {
		
		assert scheduledTasksComplete != null : ASSERTION_NULL_PARAMETER;
		assert floatingTasksComplete != null : ASSERTION_NULL_PARAMETER;
		
		File f1 = new File(currentPathName, archiveFile);
		try {
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
			myLogger.log(Level.INFO, LOG_WROTE_ARCHIVE_FILE);
			
		} catch (Exception e) {
			myLogger.log(Level.WARNING, LOG_ERROR_WRITE_ARCHIVE_FILE);
		}
	}

	public void writeToCurrentFile(ArrayList<Task> scheduledTasksToDo, ArrayList<Task> floatingTasksToDo,
			ArrayList<Task> scheduledTasksOverDue) {
		
		assert scheduledTasksToDo != null : ASSERTION_NULL_PARAMETER;
		assert floatingTasksToDo!= null : ASSERTION_NULL_PARAMETER;
		assert scheduledTasksOverDue != null : ASSERTION_NULL_PARAMETER;
		
		File f = new File(currentPathName, currentFile);
		try {
			//File f = new File(currentPathName, currentFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));

			for (Task newTask : scheduledTasksToDo) {
				String json = gson.toJson(newTask);
				bw.write(json);
				bw.newLine();
			}

			for (Task newTask : floatingTasksToDo) {
				String json = gson.toJson(newTask);
				bw.write(json);
				bw.newLine();
			}

			for (Task newTask : scheduledTasksOverDue) {
				String json = gson.toJson(newTask);
				bw.write(json);
				bw.newLine();
			}

			bw.close();
			myLogger.log(Level.INFO, LOG_WROTE_CURRENT_FILE);
		} catch (Exception e) {
			myLogger.log(Level.WARNING, LOG_ERROR_WRITE_CURRENT_FILE);

		}
	}

	public void readFromArchiveFile(ArrayList<Task> scheduledTasksComplete, ArrayList<Task> floatingTasksComplete) {

		try {

			File file = new File(currentPathName, archiveFile);

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
			myLogger.log(Level.WARNING, LOG_ARCHIVE_FILE_NOT_FOUND);
		} catch (JsonSyntaxException e) {
			myLogger.log(Level.WARNING, LOG_JSONSYNTAX_EXCEPTION);
			e.printStackTrace();
		} catch (IOException e) {
			myLogger.log(Level.WARNING, LOG_IO_EXCEPTION);
			e.printStackTrace();
		}

	}

	public void readFromCurrentFile(ArrayList<Task> scheduledTasksToDo, ArrayList<Task> floatingTasksToDo,
			ArrayList<Task> scheduledTasksOverDue) {
		try {
			File file = new File(currentPathName, currentFile);

			if (file.exists()) {
				String taskDetails = "";
				BufferedReader br = new BufferedReader(new FileReader(file));
				while ((taskDetails = br.readLine()) != null) {
					Task task = gson.fromJson(taskDetails, Task.class);
					if (task.isFloatingTask()) {
						floatingTasksToDo.add(task);
					} else {
						LocalDateTime present = LocalDateTime.now();
						LocalDateTime endDateTime = LocalDateTime.of(task.getEndDate(), task.getEndTime());
						if (endDateTime.isBefore(present)) {
							scheduledTasksOverDue.add(task);
						} else {
							scheduledTasksToDo.add(task);
						}
					}

				}
				br.close();
			}
		}

		catch (FileNotFoundException f) {

			myLogger.log(Level.WARNING, LOG_CURRENT_FILE_NOT_FOUND);
		} catch (JsonSyntaxException e) {
			myLogger.log(Level.WARNING, LOG_JSONSYNTAX_EXCEPTION);
			e.printStackTrace();
		} catch (IOException e) {
			myLogger.log(Level.WARNING, LOG_IO_EXCEPTION);
			e.printStackTrace();
		}
	}

}