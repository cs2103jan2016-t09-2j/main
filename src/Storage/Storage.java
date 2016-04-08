//@@author A0125258E
package Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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
	
	/* Singleton */
	private static Storage object;

	/* Logger messages */
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

	/* Assertion message */
	private static final String ASSERTION_NULL_PARAMETER = "Error. Null input passed.";

	/* Constructor */
	private Storage() {
		
		initStorage();
	
	}

	/* Initiates storage directory */
	public void initStorage() {
		if (currentPathName == null || currentPathName.isEmpty()) {
			try {
				File file = new File(storageLocFile);

				if (!file.exists() || file.length() == 0) {
					currentPathName = defaultPathName;
					
				} else {
					BufferedReader br = new BufferedReader(new FileReader(file));
					currentPathName = br.readLine();
					br.close();
				}

			} catch (Exception e) {

				myLogger.log(Level.INFO, LOG_IO_EXCEPTION);
			}
			fileDirectory.createMainDirectory(currentPathName);
		}
	}

	/*
     * This method is the getInstance method for the singleton pattern of
     * Storage. It initializes a new Storage if Storage is null, else returns the
     * current instance of Storage.
     */
	public static Storage getInstance() {
		if (object == null) {
			object = new Storage();
		}
		return object;
	}

	/* This method decides sets the latest directory name */
	public void setCurrentPathName(String inputDirectory) {
		if (inputDirectory != null && !inputDirectory.isEmpty()) {
			if (inputDirectory.equalsIgnoreCase("default")) {
				inputDirectory = defaultPathName;
			}

			String oldDirectoryName = currentPathName;
			currentPathName = inputDirectory;

			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(new File(storageLocFile)));
				bw.write(currentPathName);
				bw.close();
			} catch (IOException e) {
				myLogger.log(Level.INFO, LOG_IO_EXCEPTION);
			}
			fileDirectory.changeDirectory(oldDirectoryName, currentPathName);

		}

	}

	public static String getCurrentPathName() {
		return currentPathName;
	}
	
		
	/********************* Setter Methods **************************/
	public void setScheduledTasksToDo(ArrayList<Task> currentTaskList) {
		scheduledTasksToDo.clear();
		scheduledTasksToDo = currentTaskList;
	}

	public void setScheduledTasksOverDue(ArrayList<Task> currentTaskList) {
		scheduledTasksOverDue.clear();
		scheduledTasksOverDue = currentTaskList;
	}

	public void setScheduledTasksComplete(ArrayList<Task> currentTaskList) {
		scheduledTasksComplete.clear();
		scheduledTasksComplete = currentTaskList;
	}

	public void setFloatingTasksToDo(ArrayList<Task> currentTaskList) {
		floatingTasksToDo.clear();
		floatingTasksToDo = currentTaskList;
	}

	public void setFloatingTasksComplete(ArrayList<Task> currentTaskList) {
		floatingTasksComplete.clear();
		floatingTasksComplete = currentTaskList;
	}

	/********************* Getter Methods **************************/
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

	/* Stores data from ArrayList of different tasks into relevant Json files */
	public void storeToFiles(ArrayList<Task> floatingTasksToDo, ArrayList<Task> floatingTasksComplete,
			ArrayList<Task> scheduledTasksToDo, ArrayList<Task> scheduledTasksComplete,
			ArrayList<Task> scheduledTasksOverDue) {

		try {
			myLogger.log(Level.INFO, LOG_WRITING_ARCHIVE_FILE);
			writeToArchiveFile(floatingTasksComplete, scheduledTasksComplete);
		} catch (Exception e) {
			myLogger.log(Level.WARNING, LOG_ERROR_WRITE_ARCHIVE_FILE);
		}
		try {
			myLogger.log(Level.INFO, LOG_WRITING_CURRENT_FILE);
			writeToCurrentFile(scheduledTasksToDo, floatingTasksToDo, scheduledTasksOverDue);
		} catch (Exception e) {
			myLogger.log(Level.WARNING, LOG_ERROR_WRITE_CURRENT_FILE);
		}
	}

	/*
	 * This method reads the current and archive json files and returns 5 array list of
	 * specifically sorted tasks
	 */
	public void loadToList() {

		setFloatingTasksComplete(new ArrayList<Task>());
		setFloatingTasksToDo(new ArrayList<Task>());
		setScheduledTasksComplete(new ArrayList<Task>());
		setScheduledTasksOverDue(new ArrayList<Task>());
		setScheduledTasksToDo(new ArrayList<Task>());

		try {
			myLogger.log(Level.INFO, LOG_READING_ARCHIVE_FILE);
			readFromArchiveFile();
		} catch (Exception e) {
			myLogger.log(Level.INFO, LOG_ARCHIVE_FILE_NOT_FOUND);
		}
		try {
			myLogger.log(Level.INFO, LOG_READING_CURRENT_FILE);
			readFromCurrentFile();
		} catch (Exception e) {
			myLogger.log(Level.INFO, LOG_CURRENT_FILE_NOT_FOUND);
		}
	}
	
	/* Stores data from ArrayList of completed floating and scheduled tasks into the archive Json file */
	public void writeToArchiveFile(ArrayList<Task> floatingTasksComplete, ArrayList<Task> scheduledTasksComplete) {

		
		assert floatingTasksComplete != null : ASSERTION_NULL_PARAMETER;
		assert scheduledTasksComplete != null : ASSERTION_NULL_PARAMETER;

		File f = new File(currentPathName, archiveFile);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));

			for (Task newTask : floatingTasksComplete) {
				String json = gson.toJson(newTask);
				bw.write(json);
				bw.newLine();
			}

			for (Task newTask : scheduledTasksComplete) {
				String json = gson.toJson(newTask);
				bw.write(json);
				bw.newLine();
			}
			bw.close();
			myLogger.log(Level.INFO, LOG_WROTE_ARCHIVE_FILE);

		} catch (Exception e) {
			myLogger.log(Level.WARNING, LOG_ERROR_WRITE_ARCHIVE_FILE);
		}
	}
	/* Stores data from ArrayList of ToDO scheduled tasks,floating tasks and overdue tasks into the current Json file */
	public void writeToCurrentFile(ArrayList<Task> scheduledTasksToDo, ArrayList<Task> floatingTasksToDo,
			ArrayList<Task> scheduledTasksOverDue) {

		assert scheduledTasksToDo != null : ASSERTION_NULL_PARAMETER;
		assert floatingTasksToDo != null : ASSERTION_NULL_PARAMETER;
		assert scheduledTasksOverDue != null : ASSERTION_NULL_PARAMETER;

		File f = new File(currentPathName, currentFile);
		try {

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
	
	/*
	 * This method reads the archive json file and returns 2 array list of
	 * specifically sorted tasks
	 */
	public void readFromArchiveFile() {
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
		} catch (IOException e) {
			myLogger.log(Level.WARNING, LOG_IO_EXCEPTION);
		}

	}
	
	/*
	 * This method reads the current json files and returns an 3 array list of specifically
	 * sorted tasks
	 */
	public void readFromCurrentFile() {
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
		} catch (IOException e) {
			myLogger.log(Level.WARNING, LOG_IO_EXCEPTION);
		}
	}

}