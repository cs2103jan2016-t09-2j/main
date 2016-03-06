package Storage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.google.gson.Gson;
import ScheduleHacks.Task;

public class TemporaryStorageTest {
	
	private static ArrayList<Task> completeFloatingList;

	public static void main(String args[]) throws IOException{
	
		 Task test1 = new Task();
		 
		 
		 Storage storageTest= new Storage();
		 storageTest.floatingTasksComplete.add(test1);
		 storageTest.scheduledTasksToDo.add(test1);
		 storageTest.floatingTasksToDo.add(test1);
		 storageTest.scheduledTasksOverDue.add(test1);
		 storageTest.scheduledTasksComplete.add(test1);
		 storageTest.writeToCompleteFloatingFile("completeFloatingFile.json");
		 storageTest.writeToDoScheduledFile("completeToDoScheduleFile.json");
		 storageTest.writeToOverdueScheduledFile("writeToOverdueScheduledFile.json");
		 storageTest.writeToDoFloatingFile("writeToDoFloatingFile.json");
		 storageTest.writeToCompleteScheduledFile("writeToCompleteScheduledFile.json");
		 
		 
//		 Storage storageTest2= new Storage();
//		 storageTest2.readToCompleteFloatingFile("completeFloatingFile.json");
//		 
		 
	 }
}
