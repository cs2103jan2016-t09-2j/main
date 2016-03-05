package Storage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileWriter;

import java.time.LocalDate;
import java.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.GsonBuilder;

import java.util.logging.Logger;
import java.util.logging.Level;

import ScheduleHacks.Task;

public class Storage {

	private static final String currentFile = "CurrentFile";
	private static final String archiveFile = "ArchiveFile";

	private Gson gson = new Gson();

	public void writeToFile(ArrayList<Task> toDoScheduledList, ArrayList<Task> toDoFloatingList,
			ArrayList<Task> overdueScheduledList, ArrayList<Task> completeScheduledList,
			ArrayList<Task> completeFloatingList) throws IOException {

		writeToCurrentFile(toDoScheduledList, toDoFloatingList, overdueScheduledList);
		writeToArchiveFile(completeScheduledList, completeFloatingList);

	}

	private void writeToCurrentFile(ArrayList<Task> toDoScheduledList, ArrayList<Task> toDoFloatingList,
			ArrayList<Task> overdueScheduledList) throws IOException {

		
	}

	private void writeToArchiveFile(ArrayList<Task> completeScheduledList, ArrayList<Task> completeFloatingList) {

	}

	public void readFromCurrentFile(ArrayList<Task> taskLists) {

	}
	public void readFromArchiveFile(ArrayList<Task> taskLists) {

	}

	// private void addParameters(Task addParameters, BufferedWriter bw) throws
	// IOException {
	//
	// bw.write(addParameters.getStartTime().toString());
	// bw.write(addParameters.getEndTime().toString());
	// bw.write(addParameters.getStartDate().toString());
	// bw.write(addParameters.getEndDate().toString());
	// bw.write(addParameters.getDescription());
	// }
	//
	//
	//
	// private void taskWriter(BufferedWriter bw ,Task task){
	//
	// Gson gsonWrite = new Gson();
	// String json = gsonWrite.toJson(task) + "\n";
	// bw.write(json);
	//
	// }
	//
	//

}
