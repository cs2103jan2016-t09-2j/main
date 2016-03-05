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
		assert toDoScheduledList != null;
		assert toDoFloatingList != null;
		assert overdueScheduledList != null; 
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(currentFile));
		
		for(Task toDoScheduledTask: toDoScheduledList){
			if(toDoScheduledTask.getDescription() != null){
				bw.write("To Do Scheduled Task: " + toDoScheduledTask.getDescription());
				if(toDoScheduledTask.getStartTime().toString() != null && toDoScheduledTask.getStartTime().toString() != null){
					
					bw.write(toDoScheduledTask.getStartTime().toString());
					bw.write(toDoScheduledTask.getEndTime().toString());
					bw.write(toDoScheduledTask.getStartDate().toString());
					bw.write(toDoScheduledTask.getEndDate().toString());
				}				
			}	
		}
		for(Task toDoFloatingTask: toDoFloatingList){
			if(toDoFloatingTask.getDescription() != null){
				if(toDoFloatingTask.getDescription() != null){
					bw.write("To Do Floating Task: " + toDoFloatingTask.getDescription());
				}
			}
		}
		
		for(Task overdueScheduledTask: overdueScheduledList){
			if( overdueScheduledTask.getDescription() != null){
				bw.write("Overdued Scheduled Task: " + overdueScheduledTask.getDescription());
					if(overdueScheduledTask.getStartTime().toString() != null && overdueScheduledTask.getStartTime().toString() != null){
					
						bw.write(overdueScheduledTask.getStartTime().toString());
						bw.write(overdueScheduledTask.getEndTime().toString());
						bw.write(overdueScheduledTask.getStartDate().toString());
						bw.write(overdueScheduledTask.getEndDate().toString());
				}			
			}
			
		}
		
		
	}

	private void writeToArchiveFile(ArrayList<Task> completeScheduledList, ArrayList<Task> completeFloatingList) throws IOException {
		
		assert completeScheduledList != null;
		assert completeFloatingList != null;   
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(archiveFile));
		
		for(Task completeScheduledTask: completeScheduledList){
			if( completeScheduledTask.getDescription() != null){
				bw.write("Overdued Scheduled Task: " + completeScheduledTask.getDescription());
					if(completeScheduledTask.getStartTime().toString() != null && completeScheduledTask.getStartTime().toString() != null){
					
						bw.write(completeScheduledTask.getStartTime().toString());
						bw.write(completeScheduledTask.getEndTime().toString());
						bw.write(completeScheduledTask.getStartDate().toString());
						bw.write(completeScheduledTask.getEndDate().toString());
				}			
			}

		}
		for(Task completeFloatingTask: completeFloatingList){
			if(completeFloatingTask.getDescription() != null){
				bw.write("Complete Floating Task: " + completeFloatingTask.getDescription());
			}
		}
		
		
	}

	public void readFromCurrentFile(ArrayList<Task> taskLists) {

	}

	public void readFromArchiveFile(ArrayList<Task> taskLists) {

	}
	
	
	 private void taskWriter(BufferedWriter bw ,Task task) throws IOException{
	
	 Gson gsonWrite = new Gson();
	 String json = gsonWrite.toJson(task) + "\t";
	 bw.write(json);
	
	 }
	
	

}
