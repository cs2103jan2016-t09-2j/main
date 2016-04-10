package Logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegrationTest {
	Logic obj = Logic.getInstance();

	@Test
	public void testIntegrated()throws Exception {
		
		String testString001 = "learn python";
		obj.executeCommand(testString001);
		assertEquals("learn python", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		
		String testString002 = "e 1 12:00 12/12/2016";
		obj.executeCommand(testString002);
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		
		String testString003 = "undo";
		obj.executeCommand(testString003);
		assertEquals("learn python", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		
		String testString004 = "redo";
		obj.executeCommand(testString004);
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartTime());
		assertEquals("12:00", obj.getScheduledTasksToDo().get(0).getEndTime().toString());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartDate());
		assertEquals("2016-12-12",obj.getScheduledTasksToDo().get(0).getEndDate().toString());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		
		String testString005 = "done";
		obj.executeCommand(testString005);
		assertEquals("learn python", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksComplete().size());
		
		String testString006 = "undone";
		obj.startExecution();
		obj.executeCommand(testString006);
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		
		String testString007 = "home";
		obj.executeCommand(testString007);
		
		String testString008 = "view all";
		obj.executeCommand(testString008);
		
		String testString009 = "help";
		obj.executeCommand(testString009);
		
		String testString010 = "del 1";
		obj.executeCommand(testString010);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
	}
	
}
