//package Storage;
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonSyntaxException;
//
//
//public class storageTest {
//	
//	private static ArrayList<Task> completeFloatingList;
//
//	public static void main(String args[]) throws IOException{
//	
//		 Task test1 = new Task("Hiii","Hola");
//		 
//		 
//		 Storage storageTest= new Storage();
//		 storageTest.floatingTasksComplete.add(test1);
//		 storageTest.scheduledTasksToDo.add(test1);
//		 storageTest.floatingTasksToDo.add(test1);
//		 storageTest.scheduledTasksOverDue.add(test1);
//		 storageTest.scheduledTasksComplete.add(test1);
//		 storageTest.writeToCompleteFloatingFile("completeFloatingFile.json");
//		 storageTest.writeToDoScheduledFile("completeToDoScheduleFile.json");
//		 storageTest.writeToOverdueScheduledFile("writeToOverdueScheduledFile.json");
//		 storageTest.writeToDoFloatingFile("writeToDoFloatingFile.json");
//		 storageTest.writeToCompleteScheduledFile("writeToCompleteScheduledFile.json");
//		 
//		System.out.println("Floating task list complete before: " + storageTest.floatingTasksComplete);
//		 
//		storageTest.readToCompleteFloatingFile("completeFloatingFile.json");
//		
//		System.out.println("Floating task list complete after: " + storageTest.floatingTasksComplete);
//
//
//		System.out.println("To do floating task list complete before: " + storageTest.floatingTasksToDo);
//		 
//		storageTest.readToDoFloatingListFile("writeToDoFloatingFile.json");
//		
//		System.out.println("To do floating task list complete after: " + storageTest.floatingTasksToDo);
		
//		System.out.println("Overdue scheduled task list complete before: " + storageTest.scheduledTasksOverDue);
//		 
//		storageTest.readToDoScheduledFile("writeToCompleteScheduledFile.json");
//		
//		System.out.println("To do floating  task list complete after: " + storageTest.scheduledTasksOverDue);
		
//	 
//	 }
//}
