package Storage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import ScheduleHacks.Task;

public class StorageTest {

	@Test
	public void testStorageInit() {
		// saveFile.delete();
		// configFile.delete();
		//
		//
		// Storage testStorage = Storage.getInstance();
		//
		// assertEquals(true, saveFile.exists());
		// assertEquals(true, configFile.exists());
		// assertEquals(true, userPrefFile.exists());
	}

	@Test
	public void testWriteToFile() throws Exception {

		Storage storageTest = Storage.getInstance();

		Task task1 = new Task("attend soccer practice", null, null, null, null);
		task1.setFloatingTask();

		ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
		ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
		ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
		ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
		ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();

		floatingTasksToDo.add(task1);
		storageTest.storeToFiles(floatingTasksToDo, floatingTasksComplete, scheduledTasksToDo, scheduledTasksComplete,
				scheduledTasksOverDue);


		//File file1 = new File("currentFile.json");

		assertTrue(true,storageTest.storeToFiles(floatingTasksToDo, floatingTasksComplete, scheduledTasksToDo, scheduledTasksComplete,
				scheduledTasksOverDue);

	}

}
