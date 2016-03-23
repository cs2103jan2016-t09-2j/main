package Storage;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import ScheduleHacks.Task;

public class StorageTest {

	@Test
	public void testStorageInit() {
//		
//
//		
//		 Storage testStorage = Storage.getInstance();
//		
//		 assertEquals(true, currentFile.exists());
//		 assertEquals(true, archiveFile.exists());
//		 assertEquals(true, storageLocFile.exists());
		
	}
	@Test
	public void testWriteAndReadToDoFloatingTask() throws Exception {

		Storage storageTest = Storage.getInstance();
		Gson gson = new Gson();
		Task task1 = new Task("attend soccer practice", null, null, null, null);
		task1.setFloatingTask();
		String newTask = gson.toJson(task1);

		Task t1 = gson.fromJson(newTask,Task.class);
		
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
	public void testWriteAndReadToDoScheduledTask() throws Exception {

		Gson gson = new Gson();
		Task task1 = new Task("attend piano concert", null, LocalDate.parse("2016-03-03"), null, LocalTime.MAX);
		task1.setScheduledTask();
		String newTask = gson.toJson(task1);

		Task t1 = gson.fromJson(newTask,Task.class);
		
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
	public void testChangeDirectory() throws Exception {
		
		String pathName = "C:\\ScheduleHacks";
		String newPathName = "C:\\Schedule";
		File oldFolder = new File(pathName);
		File newFolder = new File(newPathName);
		
		fileDirectory.createMainDirectory(pathName);
		FileUtils.moveDirectory(oldFolder, newFolder);
		
		assertEquals(newPathName,newFolder.getAbsolutePath());
	
	}
	
	
	
	

}
