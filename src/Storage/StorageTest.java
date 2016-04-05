package Storage;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ScheduleHacks.Task;

public class StorageTest {

	private static final String currentFile = "currentFile.json";
	private static final String archiveFile = "archiveFile.json";


	File curFile = new File(currentFile);
	File arcFile = new File(archiveFile);
	File testFile = new File("test.json");
	File dataFile = new File("DataLocation.txt");

	private ArrayList<Task> floatingTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> floatingTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksToDo = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksComplete = new ArrayList<Task>();
	private ArrayList<Task> scheduledTasksOverDue = new ArrayList<Task>();

	String settingsStr;

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

//	@Test
//	public void testStorageInit() {
//		curFile.delete();
//		arcFile.delete();
//		dataFile.delete();
//		
//		Storage testStorage = Storage.getInstance();
//		testStorage.initStorage();
//
//		assertEquals(false, curFile.exists());
//		assertEquals(false, arcFile.exists());
//		assertEquals(true, dataFile.exists());
//
//	}

	@SuppressWarnings("null")
	@Test
	public void testFloatingTask() {
		Storage testStorage = Storage.getInstance();
		
		Task task1 = new Task("attend soccer practice", null, null, null, null);
		task1.setFloatingTask();
		
		ArrayList<Task> testFloatingTasksToDo = new ArrayList<Task>();
		testFloatingTasksToDo.add(task1);
		
		testStorage.setFloatingTasksToDo(testFloatingTasksToDo);
		
		testStorage.writeToCurrentFile(new ArrayList<Task>(), testFloatingTasksToDo,
				new ArrayList<Task>());
		testStorage.readFromCurrentFile();
		ArrayList<Task> reetrieve = testStorage.getFloatingTasksToDo();
		
		
		assertEquals(reetrieve.equals(testFloatingTasksToDo),true);
		
	}


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
	public void testReadToDoFloatingTask() throws Exception {

		Gson gson = new Gson();
		Task task1 = new Task("attend soccer practice", null, null, null, null);
		task1.setFloatingTask();
		String newTask = gson.toJson(task1);

		Task t1 = gson.fromJson(newTask, Task.class);

		assertEquals("attend soccer practice", t1.getDescription());
		assertEquals(null, t1.getStartDate());
		assertEquals(null, t1.getEndDate());
		assertEquals(null, t1.getStartTime());
		assertEquals(null, t1.getEndTime());
		assertEquals(false, t1.isComplete());
		assertEquals(true, t1.isFloatingTask());
		assertEquals(false, t1.isScheduledTask());

	}

	@Test
	public void testReadToDoScheduledTask() throws Exception {

		Gson gson = new Gson();
		Task task1 = new Task("attend piano concert", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		task1.setScheduledTask();
		String newTask = gson.toJson(task1);

		Task t1 = gson.fromJson(newTask, Task.class);

		assertEquals("attend piano concert", t1.getDescription());
		assertEquals(null, t1.getStartDate());
		assertEquals("2016-03-03", t1.getEndDate().toString());
		assertEquals(null, t1.getStartTime());
		assertEquals(LocalTime.MAX, t1.getEndTime());
		assertEquals(false, t1.isComplete());
		assertEquals(false, t1.isFloatingTask());
		assertEquals(true, t1.isScheduledTask());

	}

	@Test
	public void testReadCompletedScheduleTask() throws Exception {

		Gson gson = new Gson();
		Task task1 = new Task("attend piano concert", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		task1.setAsComplete();
		task1.setScheduledTask();
		String newTask = gson.toJson(task1);

		Task t1 = gson.fromJson(newTask, Task.class);

		assertEquals("attend piano concert", t1.getDescription());
		assertEquals(null, t1.getStartDate());
		assertEquals("2016-03-03", t1.getEndDate().toString());
		assertEquals(null, t1.getStartTime());
		assertEquals(LocalTime.MAX, t1.getEndTime());
		assertEquals(true, t1.isComplete());
		assertEquals(false, t1.isFloatingTask());
		assertEquals(true, t1.isScheduledTask());

	}

	@Test
	public void testWriteCurrentFile() throws Exception {

		File f = new File("currentFile");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		Gson gson = new Gson();
		Task task1 = new Task("attend soccer practice", null, null, null, null);
		task1.setFloatingTask();
		String newTask = gson.toJson(task1);
		bw.write(newTask);
		bw.close();

		assertEquals(true, f.exists());
		assertEquals("currentFile", f.getName());
		assertNotNull(f);
		assertNotSame(0, f.length());

	}

	@Test
	public void testWriteCurrentFile2() throws Exception {

		File f = new File("currentFile");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		Gson gson = new Gson();
		Task task1 = new Task("attend piano concert", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		task1.setScheduledTask();
		String newTask = gson.toJson(task1);
		bw.write(newTask);
		bw.close();

		assertEquals(true, f.exists());
		assertEquals("currentFile", f.getName());
		assertNotNull(f);
		assertNotSame(0, f.length());
	}

}
