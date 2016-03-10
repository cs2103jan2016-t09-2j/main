package Storage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ScheduleHacks.Task;



public class StorageTest {

	@Test
	public void testWriteToFile() throws Exception {

		Storage storageTest = new Storage();

		Task test1 = new Task("attend soccer practice", null, null, null, null);
		Task test2 = new Task("go to the gym", null, null, null, null);
		
		ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
		ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
		ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();
		floatingTasksToDo.add(test1);
	//	floatingTasksToDo.add(test2);
		
		storageTest.writeToCurrentFile(scheduledTasksToDo,floatingTasksToDo,scheduledTasksOverDue);
		storageTest.readFromCurrentFile(scheduledTasksToDo,floatingTasksToDo,scheduledTasksOverDue);
		
		
		
	}

}
