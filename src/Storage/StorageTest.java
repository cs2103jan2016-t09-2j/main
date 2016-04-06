//@@author A0125258E
package Storage;

import static org.junit.Assert.*;


import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import ScheduleHacks.Task;

public class StorageTest {

	@Test
	public void testSetDirectory() {

		Storage testStorage = Storage.getInstance();

		File testDirectory = new File("C:\\SH");
		testDirectory.mkdir();

		testStorage.setCurrentPathName("C:\\SH");

		assertEquals(testDirectory.getAbsolutePath(), Storage.getCurrentPathName());

	}

	@Test
	public void testChangeDirectory() {

		Storage testStorage = Storage.getInstance();

		testStorage.setCurrentPathName("C:\\SH1");
		testStorage.setCurrentPathName("C:\\SH2");

		assertEquals("C:\\SH2", Storage.getCurrentPathName());

	}

	@Test
	public void testSameDirectory() {

		Storage testStorage = Storage.getInstance();

		testStorage.setCurrentPathName("C:\\sameName");
		testStorage.setCurrentPathName("C:\\sameName");

		assertEquals("C:\\sameName", Storage.getCurrentPathName());

	}

	@Test
	public void testDirectoryAddBranches() {

		Storage testStorage = Storage.getInstance();

		testStorage.setCurrentPathName("C:\\test\\testtest");

		assertEquals("C:\\test\\testtest", Storage.getCurrentPathName());

	}

	@Test
	public void testDirectoryMoreBranches() {

		Storage testStorage = Storage.getInstance();

		testStorage.setCurrentPathName("C:\\test\\testtest\\testtesttest");

		assertEquals("C:\\test\\testtest\\testtesttest", Storage.getCurrentPathName());

	}

	@Test
	public void testNotDirectory() {

		Storage testStorage = Storage.getInstance();

		testStorage.setCurrentPathName("C:SH1");

		assertNotSame("C:\\SH2", Storage.getCurrentPathName());

	}
	
	@Test
	public void testToDoScheduledTask() throws IOException{
		
		Storage testStorage = Storage.getInstance();

	
		Task task1 = new Task("attend piano concert", LocalDate.parse("2016-06-08"), LocalDate.parse("2016-08-08"), LocalTime.parse("12:00"), LocalTime.parse("16:00"));
		task1.setScheduledTask();
		task1.setAsIncomplete();

		ArrayList<Task> testScheduledTasksToDo = new ArrayList<Task>();
		testScheduledTasksToDo.add(task1);

		testStorage.setScheduledTasksToDo(testScheduledTasksToDo);

		testStorage.writeToCurrentFile(testScheduledTasksToDo,new ArrayList<Task>(), new ArrayList<Task>());
		testStorage.readFromCurrentFile();
		ArrayList<Task> retrieveList = testStorage.getScheduledTasksToDo();

		assertEquals(retrieveList.equals(testScheduledTasksToDo), true);
	
	}

	@Test
	public void testToDoFloatingTask() throws IOException{
		Storage testStorage = Storage.getInstance();
		
		testStorage.initStorage();
		
		Task task1 = new Task("attend soccer practice", null, null, null, null);
		task1.setFloatingTask();
		task1.setAsIncomplete();
		task1.isFloatingTask();

		ArrayList<Task> testFloatingTasksToDo = new ArrayList<Task>();
		testFloatingTasksToDo.add(task1);

		testStorage.setFloatingTasksToDo(testFloatingTasksToDo);

		testStorage.writeToCurrentFile(new ArrayList<Task>(), testFloatingTasksToDo, new ArrayList<Task>());
		testStorage.readFromCurrentFile();
		ArrayList<Task> retrieveList = testStorage.getFloatingTasksToDo();
		
		
		assertEquals(retrieveList.equals(testFloatingTasksToDo), true);
		testFloatingTasksToDo.clear();
	}

	
	
	@Test
	public void testOverduedTask()throws IOException{
		Storage testStorage = Storage.getInstance();

		Task task1 = new Task("attend piano concert", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		task1.setScheduledTask();
		task1.setAsIncomplete();
		task1.isScheduledTask();

		ArrayList<Task> testOverduedTasks = new ArrayList<Task>();
		testOverduedTasks.add(task1);

		testStorage.setScheduledTasksOverDue(testOverduedTasks);
		LocalDateTime present = LocalDateTime.now();
		LocalDateTime endDateTime = LocalDateTime.of(task1.getEndDate(), task1.getEndTime());

		testStorage.writeToCurrentFile(new ArrayList<Task>(), new ArrayList<Task>(),testOverduedTasks);
		testStorage.readFromCurrentFile();
		ArrayList<Task> retrieveList = testStorage.getScheduledTasksOverDue();
		
		assertEquals(retrieveList.equals(testOverduedTasks), true);
		assertEquals(endDateTime.isBefore(present), true);

	}
	
	
	@Test
	public void testCompleteFloatingTask()throws Exception {
		Storage testStorage = Storage.getInstance();

		Task task1 = new Task("attend soccer practice", null, null, null, null);
		task1.setFloatingTask();
		task1.setAsComplete();
		task1.isComplete();
		task1.isFloatingTask();

		ArrayList<Task> testFloatingTaskComplete = new ArrayList<Task>();
		testFloatingTaskComplete.add(task1);

		testStorage.setFloatingTasksComplete(testFloatingTaskComplete);

		testStorage.writeToArchiveFile(testFloatingTaskComplete, new ArrayList<Task>());
		testStorage.readFromArchiveFile();
		ArrayList<Task> retrieveList = testStorage.getFloatingTasksComplete();

		assertEquals(retrieveList.equals(testFloatingTaskComplete), true);
		 testFloatingTaskComplete.clear();
	}

	@Test
	public void testCompletedScheduleTask()throws Exception{
		Storage testStorage = Storage.getInstance();

		Task task1 = new Task("attend piano concert", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		task1.setScheduledTask();
		task1.setAsComplete();
		task1.isComplete();
		task1.isScheduledTask();

		ArrayList<Task> testScheduledTaskComplete = new ArrayList<Task>();
		testScheduledTaskComplete .add(task1);

		testStorage.setScheduledTasksComplete(testScheduledTaskComplete);

		testStorage.writeToArchiveFile(new ArrayList<Task>(),testScheduledTaskComplete );
		testStorage.readFromArchiveFile();
		ArrayList<Task> retrieveList = testStorage.getScheduledTasksComplete();

		assertEquals(retrieveList.equals(testScheduledTaskComplete), true);

	}
	
	@Test
	public void testStoreAndLoad()throws Exception {
		Storage testStorage = Storage.getInstance();

		Task task1 = new Task("attend soccer practice", null, null, null, null);
		Task task2 = new Task("attend piano lessons", null, null, null, null);
		Task task3 = new Task("watch movie", LocalDate.parse("2016-04-03"), LocalDate.parse("2016-04-03"), null, LocalTime.MAX);
		Task task4 = new Task("read book", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		Task task5 = new Task("attend piano concert", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		
		task1.setFloatingTask();
		task1.setAsIncomplete();
		task1.isComplete();
		task1.isFloatingTask();
		
		task2.setFloatingTask();
		task2.setAsComplete();
		task2.isFloatingTask();

		task3.setScheduledTask();
		task3.setAsIncomplete();
		task3.isScheduledTask();

		task4.setScheduledTask();
		task4.setAsComplete();
		task4.isComplete();
		task4.isScheduledTask();
		
		task5.setScheduledTask();
		task5.setAsIncomplete();
		task5.isScheduledTask();
		
		LocalDateTime present = LocalDateTime.now();
		LocalDateTime endDateTime = LocalDateTime.of(task5.getEndDate(), task5.getEndTime());

		ArrayList<Task> testFloatingTasksToDo = new ArrayList<Task>();
		ArrayList<Task> testFloatingTasksComplete = new ArrayList<Task>();
		ArrayList<Task> testScheduledTasksToDo = new ArrayList<Task>();
		ArrayList<Task> testScheduledTasksComplete = new ArrayList<Task>();
		ArrayList<Task> testScheduledTasksOverDue = new ArrayList<Task>();
		
		testFloatingTasksToDo.add(task1);
		testFloatingTasksComplete.add(task2);
		testScheduledTasksToDo.add(task3);
		testScheduledTasksComplete.add(task4);
		testScheduledTasksOverDue.add(task5);
		
		testStorage.setFloatingTasksToDo(testFloatingTasksToDo);
		testStorage.setFloatingTasksComplete(testFloatingTasksComplete);
		testStorage.setScheduledTasksToDo( testScheduledTasksToDo);
		testStorage.setFloatingTasksComplete(testScheduledTasksComplete);
		testStorage.setScheduledTasksOverDue(testScheduledTasksOverDue);
		

		testStorage.storeToFiles( testFloatingTasksToDo,testFloatingTasksComplete, testScheduledTasksToDo,testScheduledTasksComplete,testScheduledTasksOverDue);
		testStorage.loadToList();
		ArrayList<Task> retrieveList1 = testStorage.getFloatingTasksToDo();
		ArrayList<Task> retrieveList2 = testStorage.getFloatingTasksComplete();
		ArrayList<Task> retrieveList3 = testStorage.getScheduledTasksToDo();
		ArrayList<Task> retrieveList4 = testStorage.getFloatingTasksComplete();
		ArrayList<Task> retrieveList5 = testStorage.getScheduledTasksOverDue();

		assertEquals(retrieveList1.equals(testFloatingTasksToDo), true);
		assertEquals(retrieveList2.equals(testFloatingTasksComplete), true);
		assertEquals(retrieveList3.equals( testScheduledTasksToDo), true);
		assertEquals(retrieveList4.equals(testScheduledTasksComplete), true);
		assertEquals(retrieveList5.equals(testScheduledTasksOverDue), true);
		assertEquals(endDateTime.isBefore(present), true);
		
	}

}
