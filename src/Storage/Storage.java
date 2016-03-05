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

	private static final File toDoScheduledFile = new File("toDoScheduled");
	private static final File toDoFloatingFile = new File("toDoFloating");
	private static final File overdueScheduledFile = new File("overdueScheduled");
	private static final File completeScheduledFile = new File("completeScheduled");
	private static final File completeFloatingFile = new File("completeFloating");

	private Gson gson = new Gson();

	public void writeToFile(ArrayList<Task> toDoScheduledList, ArrayList<Task> toDoFloatingList,
			ArrayList<Task> overdueScheduledList, ArrayList<Task> completeScheduledList,
			ArrayList<Task> completeFloatingList) throws IOException {
		
		writeToDoScheduledFile(toDoScheduledList);
		writeToDoFloatingListFile(toDoFloatingList);
		writeToOverdueScheduledFile(overdueScheduledList);
		writeToCompleteScheduledFile(completeScheduledList);
		writeToCompleteFloatingFile(completeFloatingList);

	}

	private void writeToCompleteFloatingFile(ArrayList<Task> completeFloatingList) throws IOException {
		// TODO Auto-generated method stub
	
		assert completeFloatingList != null;   
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(completeFloatingFile));
		
		for(Task completeFloatingTask: completeFloatingList){
			if(completeFloatingTask.getDescription() != null){
				bw.write("Complete Floating Task: " + completeFloatingTask.getDescription());
			}
			taskWriter(bw,completeFloatingTask);
		}
	}

	private void writeToCompleteScheduledFile(ArrayList<Task> completeScheduledList) throws IOException {
		// TODO Auto-generated method stub
		assert completeScheduledList != null;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(completeScheduledFile));
		
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
			taskWriter(bw,completeScheduledTask);

		}
	}

	private void writeToOverdueScheduledFile(ArrayList<Task> overdueScheduledList) throws IOException {
	
		assert overdueScheduledList != null;
		BufferedWriter bw = new BufferedWriter(new FileWriter(overdueScheduledFile));
		
		for(Task overdueScheduledTask: overdueScheduledList){
			if( overdueScheduledTask.getDescription() != null){
				bw.write("Overdued Scheduled Task: " + overdueScheduledTask.getDescription());
					if(overdueScheduledTask.getStartTime() != null && overdueScheduledTask.getStartDate() != null){
					
						bw.write(overdueScheduledTask.getStartTime().toString());
						bw.write(overdueScheduledTask.getEndTime().toString());
						bw.write(overdueScheduledTask.getStartDate().toString());
						bw.write(overdueScheduledTask.getEndDate().toString());
				}			
			}
			taskWriter(bw,overdueScheduledTask);
		}
		
		
	}

	private void writeToDoFloatingListFile(ArrayList<Task> toDoFloatingList) throws IOException {
	
		assert toDoFloatingList != null;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(completeScheduledFile));
		
		for(Task toDoFloatingTask: toDoFloatingList){
			if(toDoFloatingTask.getDescription() != null){			
				bw.write("To Do Floating Task: " + toDoFloatingTask.getDescription());
			}
			taskWriter(bw,toDoFloatingTask);
		}
	}

private void writeToDoScheduledFile(ArrayList<Task> toDoScheduledList) throws IOException {
		// TODO Auto-generated method stub
		assert toDoScheduledList != null;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(toDoScheduledFile));
		
		for(Task toDoScheduledTask: toDoScheduledList){
			if(toDoScheduledTask.getDescription() != null){
				bw.write("To Do Scheduled Task: " + toDoScheduledTask.getDescription());
				if(toDoScheduledTask.getStartTime() != null && toDoScheduledTask.getStartDate() != null){
					
					bw.write(toDoScheduledTask.getStartTime().toString());
					bw.write(toDoScheduledTask.getEndTime().toString());
					bw.write(toDoScheduledTask.getStartDate().toString());
					bw.write(toDoScheduledTask.getEndDate().toString());
				}				
			}
			taskWriter(bw,toDoScheduledTask);
		}
	}

	
	public void readFromFile(ArrayList<Task> toDoScheduledList, ArrayList<Task> toDoFloatingList,
			ArrayList<Task> overdueScheduledList, ArrayList<Task> completeScheduledList,
			ArrayList<Task> completeFloatingList) throws IOException {
		
		readToDoScheduledFile(toDoScheduledFile);
		readToDoFloatingListFile(toDoFloatingFile);
		readToOverdueScheduledFile(overdueScheduledFile);
		readToCompleteScheduledFile(completeScheduledFile);
		readToCompleteFloatingFile(completeFloatingFile);

		
	}	

	private ArrayList<Task> readToCompleteFloatingFile(File completeScheduled) throws FileNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader(completeScheduledFile));
		return null;
	}

	private ArrayList<Task> readToCompleteScheduledFile(File completeScheduled) throws FileNotFoundException {
	
		BufferedReader br = new BufferedReader(new FileReader(completeScheduledFile));
		return null;
	}
	
	

	private ArrayList<Task> readToOverdueScheduledFile(File overdueScheduled) throws FileNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader(overdueScheduledFile));
		return null;
		
	}

	private ArrayList<Task>readToDoFloatingListFile(File toDofFloating) throws FileNotFoundException {
	
		BufferedReader br = new BufferedReader(new FileReader(toDoFloatingFile));
		return null;
	
		
	}

	private ArrayList<Task> readToDoScheduledFile(File toDoScheduled) throws FileNotFoundException {
		
		BufferedReader br = new BufferedReader(new FileReader(toDoScheduledFile));
		return null;
		
	}

	
	 private void taskWriter(BufferedWriter bw ,Task task) throws IOException{
	
	 Gson gsonWrite = new Gson();
	 String json = gsonWrite.toJson(task) + "\t";
	 bw.write(json);
	
	 }
	 private ArrayList<Task> taskReader(BufferedReader br ,Task task) throws IOException{
				
		        String text = "";
		        ArrayList<Task> taskList = new ArrayList<Task>();
		        
		        while ((text = br.readLine()) != null) {
		                task = gson.fromJson(text, Task.class);
		                taskList.add(task);
		        }
		         		        
		        return taskList;
		    
		 }
	
	

}
