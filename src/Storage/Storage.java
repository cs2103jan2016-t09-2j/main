package Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileWriter;

import java.time.LocalDate;
import java.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.logging.Logger;
import java.util.logging.Level;

import ScheduleHacks.Task;

public class Storage {

	private static final String toDoScheduledFile = "toDoScheduled";
	private static final String toDoFloatingFile = "toDoFloating";
	private static final String overdueScheduledFile ="overdueScheduled";
	private static final String completeScheduledFile = "completeScheduled";
	private static final String completeFloatingFile = "completeFloating";

	private Gson gson = new Gson();
	private static Logger logger = Logger.getLogger("Storage");

	/*
	* Log messages
	*/
	
	private static final String NULL_INPUT= "Null input passed.";
	
	ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	public ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	public ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	public ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
	
	public Storage() {
		
	}
	public void writeToFile(ArrayList<Task> toDoScheduledList, ArrayList<Task> toDoFloatingList,
			ArrayList<Task> overdueScheduledList, ArrayList<Task> completeScheduledList,
			ArrayList<Task> completeFloatingList) throws IOException {

		writeToDoScheduledFile(toDoScheduledFile);
		writeToDoFloatingFile(toDoFloatingFile);
		writeToOverdueScheduledFile(overdueScheduledFile);
		writeToCompleteScheduledFile(completeScheduledFile);
		writeToCompleteFloatingFile(completeFloatingFile);

	}
	
	public void writeToCompleteFloatingFile(String completeFloatingFile) throws IOException {

		assert floatingTasksComplete != null;
		
		File f = new File(completeFloatingFile);
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		for (Task completeFloatingTask : floatingTasksComplete) {
			if (completeFloatingTask.getDescription() != null) {
				bw.write("Completed Floatings Task: "+ "\t");
				taskWriter(bw, completeFloatingTask);
			}
			bw.close();
		}
	
	}

	public void writeToCompleteScheduledFile(String completeScheduledFile) throws IOException {		
		assert completeScheduledFile != null;

		BufferedWriter bw = new BufferedWriter(new FileWriter(completeScheduledFile));

		for (Task completeScheduledTask : scheduledTasksComplete) {
			if (completeScheduledTask.getDescription() != null) {
				if (completeScheduledTask.getStartTime() != null
						&& completeScheduledTask.getStartTime() != null) {

					bw.write("Completed Scheduled Task: " + "\t");
					taskWriter(bw, completeScheduledTask);
				}
				bw.close();
			}
		}
	}

	public void writeToOverdueScheduledFile(String overdueScheduledFile) throws IOException {

		assert overdueScheduledFile != null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(overdueScheduledFile));

		for (Task overdueScheduledTask : scheduledTasksOverDue) {
			if (overdueScheduledTask.getDescription() != null) {
				if (overdueScheduledTask.getStartTime() != null && overdueScheduledTask.getStartDate() != null) {
					bw.write("Overdued Scheduled Task: " + "\t");
					taskWriter(bw, overdueScheduledTask);
				}
			}
			bw.close();
		}

	}

	public void writeToDoFloatingFile(String toDoFloatingFile) throws IOException {

		assert toDoFloatingFile != null;

		BufferedWriter bw = new BufferedWriter(new FileWriter(toDoFloatingFile));

		for (Task toDoFloatingTask : floatingTasksToDo) {
			if (toDoFloatingTask.getDescription() != null) {
				bw.write("To Do Floating Task: " + "\t");
				taskWriter(bw, toDoFloatingTask);
			}
			bw.close();
		}
		
	}

	public void writeToDoScheduledFile( String toDoScheduledFile) throws IOException {
		
		assert scheduledTasksToDo!= null;

		BufferedWriter bw = new BufferedWriter(new FileWriter(toDoScheduledFile));

		for (Task toDoScheduledTask : scheduledTasksToDo) {
			if (toDoScheduledTask.getDescription() != null) {
				if (toDoScheduledTask.getStartTime() != null && toDoScheduledTask.getStartDate() != null) {
					bw.write("To Do Scheduled Task: " + "\t");
					taskWriter(bw, toDoScheduledTask);
				}
			}
			bw.close();
		}
	
	}

	public void readFromFile(ArrayList<Task> toDoScheduledList, ArrayList<Task> toDoFloatingList,
			ArrayList<Task> overdueScheduledList, ArrayList<Task> completeScheduledList,
			ArrayList<Task> completeFloatingList) throws IOException {

	//	readToDoScheduledFile(toDoScheduledFile);
	//	readToDoFloatingFile(toDoFloatingFile);
	//	readToOverdueScheduledFile(overdueScheduledFile);
	//	readToCompleteScheduledFile(completeScheduledFile);
		readToCompleteFloatingFile(completeFloatingFile);

	}

	public void readToCompleteFloatingFile(String completeFloatingFile) throws JsonSyntaxException, IOException {

		BufferedReader br = new BufferedReader(new FileReader(completeFloatingFile));
		String text = "";

		while ((text = br.readLine()) != null) {
			Task task = gson.fromJson(text, Task.class);
			floatingTasksComplete.add(task);
		}
		br.close();


	}

//	private void readToCompleteScheduledFile(File completeScheduled) throws IOException {
//
//		ArrayList<Task> completeScheduledList = new ArrayList<Task>();
//
//		completeScheduledList = taskReader(completeScheduled);
//
//	}

//	private ArrayList<Task> readToOverdueScheduledFile(File overdueScheduled) throws IOException {
//
//		ArrayList<Task> overdueScheduledFile = new ArrayList<Task>();
//
//		overdueScheduledFile = taskReader(overdueScheduled);
//
//		return null;
//
//	}
//
//	private ArrayList<Task> readToDoFloatingListFile(File toDofFloating) throws FileNotFoundException {
//
//		BufferedReader br = new BufferedReader(new FileReader(toDoFloatingFile));
//		ArrayList<Task> toDoFloatingFile = new ArrayList<Task>();
//
//		return null;
//
//	}
//
//	private ArrayList<Task> readToDoScheduledFile(File toDoScheduled) throws FileNotFoundException {
//
//		BufferedReader br = new BufferedReader(new FileReader(toDoScheduledFile));
//		ArrayList<Task> toDoScheduledList = new ArrayList<Task>();
//
//		return null;
//
//	}

	private void taskWriter(BufferedWriter bw, Task task) throws IOException {

		Gson gsonWrite = new Gson();
		
		String json = gsonWrite.toJson(task) + "\n";
		bw.append(json);
		
		
	}

	private void taskReader(String file) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(file));
		String text = "";

		while ((text = br.readLine()) != null) {
			Task task = gson.fromJson(text, Task.class);
			floatingTasksComplete.add(task);
		}
		br.close();
	}
	

}
