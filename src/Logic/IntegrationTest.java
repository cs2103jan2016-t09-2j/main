package Logic;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import Storage.Storage;
import org.apache.commons.io.FileUtils;

public class IntegrationTest {
	Logic obj = Logic.getInstance();

	@Test
	public void testIntegrated1() throws Exception {

		String testString = "";

		try {
			String testDir = "Test";
			String fileLocation = Storage.getCurrentPathName();
			FileUtils.copyDirectory(new File(fileLocation), new File(testDir));
			// Storage.getInstance().setCurrentPathName(testDir);
			FileUtils.deleteDirectory(new File(fileLocation));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Incorrect Folder Operation");
		}

		obj.startExecution();

		testString = "learn python";
		obj.executeCommand(testString);
		assertEquals("learn python", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksToDo().size());

		testString = "e 1 12:00 12/12/2016";
		obj.executeCommand(testString);
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartTime());
		assertEquals("12:00", obj.getScheduledTasksToDo().get(0).getEndTime().toString());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartDate());
		assertEquals("2016-12-12", obj.getScheduledTasksToDo().get(0).getEndDate().toString());

		testString = "undo";
		obj.executeCommand(testString);
		assertEquals("learn python", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getStartTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getEndTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getStartDate());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getEndDate());

		testString = "redo";
		obj.executeCommand(testString);
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartTime());
		assertEquals("12:00", obj.getScheduledTasksToDo().get(0).getEndTime().toString());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartDate());
		assertEquals("2016-12-12", obj.getScheduledTasksToDo().get(0).getEndDate().toString());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksToDo().size());

		testString = "done";
		obj.executeCommand(testString);
		assertEquals("learn python", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals(1, obj.getScheduledTasksComplete().size());

		testString = "undo";
		obj.startExecution();
		obj.executeCommand(testString);
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());

		testString = "del 1";
		obj.executeCommand(testString);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());

		try {
			String testDir = "Test";
			String fileLocation = Storage.getCurrentPathName();
			FileUtils.copyDirectory(new File(testDir), new File(fileLocation));
			FileUtils.deleteDirectory(new File(testDir));
		} catch (Exception e) {
		}
	}

	// @@Author A0132778W
	/**
	 * Software Integrated boundary testing
	 * 
	 * @throws Exception
	 */
	@Test
	public void testIntegrated2() {

		// String testString = "";

		try {
			String testDir = "Test";
			String fileLocation = Storage.getCurrentPathName();
			FileUtils.copyDirectory(new File(fileLocation), new File(testDir));
			// Storage.getInstance().setCurrentPathName(testDir);
			FileUtils.deleteDirectory(new File(fileLocation));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Incorrect Folder Operation");
		}

		obj.startExecution();

		obj.executeCommand("Watch day after .tmr");
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("Watch day after tmr", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getStartTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getEndTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getStartDate());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getEndDate());

		obj.executeCommand("Watch day after .tmr day aftr tmrw");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("Watch day after tmr", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartTime());
		assertEquals(LocalTime.MAX, obj.getScheduledTasksToDo().get(0).getEndTime());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartDate());
		assertEquals(LocalDate.now().plusDays(2), obj.getScheduledTasksToDo().get(0).getEndDate());

		obj.executeCommand("Class     tdy at   0830");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("Class", obj.getScheduledTasksOverDue().get(0).getDescription());
		assertEquals(null, obj.getScheduledTasksOverDue().get(0).getStartTime());
		assertEquals(LocalTime.of(8, 30), obj.getScheduledTasksOverDue().get(0).getEndTime());
		assertEquals(null, obj.getScheduledTasksOverDue().get(0).getStartDate());
		assertEquals(LocalDate.now(), obj.getScheduledTasksOverDue().get(0).getEndDate());

		// $1500 should not be detected as time.
		// Goes to FLoating Task List.
		obj.executeCommand("Save $1500");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("Save $1500", obj.getFloatingTasksToDo().get(1).getDescription());
		assertEquals(null, obj.getFloatingTasksToDo().get(1).getStartTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(1).getEndTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(1).getStartDate());
		assertEquals(null, obj.getFloatingTasksToDo().get(1).getEndDate());

		obj.executeCommand("12345678");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(3, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("12345678", obj.getFloatingTasksToDo().get(2).getDescription());
		assertEquals(null, obj.getFloatingTasksToDo().get(2).getStartTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(2).getEndTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(2).getStartDate());
		assertEquals(null, obj.getFloatingTasksToDo().get(2).getEndDate());

		// Empty Task Description should not be added
		// Task List size should not change
		obj.executeCommand("add 22dec");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(3, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());

		// Checking for duplication
		// This task already exists. Should not be added to the List.
		obj.executeCommand("12345678");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(3, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());

		// Task Indexes greater than number of tasks.
		// SHould not delete any task.
		obj.executeCommand("del 1-100");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(3, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());

		// Index Number always greater than 0
		// SHould not produce any changes.
		obj.executeCommand("del 0");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(3, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());

		// edit 12345678 to convert from floating to scheduled
		obj.executeCommand("edit 5 at 1030 on 2may20");
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("12345678", obj.getScheduledTasksToDo().get(1).getDescription());
		assertEquals(null, obj.getScheduledTasksToDo().get(1).getStartTime());
		assertEquals(LocalTime.of(10, 30), obj.getScheduledTasksToDo().get(1).getEndTime());
		assertEquals(null, obj.getScheduledTasksToDo().get(1).getStartDate());
		assertEquals(LocalDate.of(2020, 5, 2), obj.getScheduledTasksToDo().get(1).getEndDate());

		// undo last action
		obj.executeCommand("z");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(3, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());

		// marking save $1500 and 12345678 as done
		obj.executeCommand("done 5, 4-5");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(2, obj.getFloatingTasksComplete().size());

		// undone an incorrect index
		obj.executeCommand("undone 1-2");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(2, obj.getFloatingTasksComplete().size());

		// undo the done action
		obj.executeCommand("z");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(3, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		// redo the previous action
		obj.executeCommand("y");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(2, obj.getFloatingTasksComplete().size());
		
		// redo again. Should yield nothing as done action was performed 
		// before the last undo
		obj.executeCommand("redo");
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(2, obj.getFloatingTasksComplete().size());
		
		obj.executeCommand("  help Jim Manage   time     in 10 days  at   0830 to 5pm");
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(2, obj.getFloatingTasksComplete().size());
		assertEquals("Help Jim Manage time", obj.getScheduledTasksToDo().get(1).getDescription());
		assertEquals(LocalTime.of(8, 30), obj.getScheduledTasksToDo().get(1).getStartTime());
		assertEquals(LocalTime.of(17, 00), obj.getScheduledTasksToDo().get(1).getEndTime());
		assertEquals(LocalDate.now().plusDays(10), obj.getScheduledTasksToDo().get(1).getStartDate());
		assertEquals(LocalDate.now().plusDays(10), obj.getScheduledTasksToDo().get(1).getEndDate());

		try {
			String testDir = "Test";
			String fileLocation = Storage.getCurrentPathName();
			FileUtils.copyDirectory(new File(testDir), new File(fileLocation));
			FileUtils.deleteDirectory(new File(testDir));
		} catch (Exception e) {
		}
	}
}
