package Logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class IntegrationTest2 {
	Logic obj = Logic.getInstance();

	@Test
	public void testIntegrated2()throws Exception {
		
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
		
		String testString005 = "e 31/12/2016 23:59";
		obj.executeCommand(testString005);
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartTime());
		assertEquals("23:59", obj.getScheduledTasksToDo().get(0).getEndTime().toString());
		assertEquals(null, obj.getScheduledTasksToDo().get(0).getStartDate());
		assertEquals("2016-12-31",obj.getScheduledTasksToDo().get(0).getEndDate().toString());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		
		String testString006 = "done 1";
		obj.executeCommand(testString006);
		assertEquals("learn python", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksComplete().size());
		
		String testString007 = "home";
		obj.executeCommand(testString007);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(true, obj.getFeedBack().contains("Home Screen Display"));
		
		String testString008 = "view all";
		obj.executeCommand(testString008);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(1, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString009 = "e 1 12:00 12/12/2016";
		obj.executeCommand(testString009);
		assertEquals(true, obj.getFeedBack().contains("Task number was not found!" +"\n"+ "Task was not modified"));
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(1, obj.getScheduledTasksComplete().size());
		
		String testString010 = "undone 1";
		obj.executeCommand(testString010);
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("learn python", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString011 = "del 1";
		obj.executeCommand(testString011);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString012 = "add learn html5 by 23:59 on 31/07/2016";
		obj.executeCommand(testString012);
		assertEquals("learn html5", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-07-31", obj.getScheduledTasksToDo().get(0).getEndDate().toString());
		assertEquals("23:59", obj.getScheduledTasksToDo().get(0).getEndTime().toString());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString013 = "learn css by 23:59 by 31/08/2016";
		obj.executeCommand(testString013);
		assertEquals("learn css", obj.getScheduledTasksToDo().get(1).getDescription());
		assertEquals("2016-08-31", obj.getScheduledTasksToDo().get(1).getEndDate().toString());
		assertEquals("23:59", obj.getScheduledTasksToDo().get(1).getEndTime().toString());
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString014 = "add learn javascript";
		obj.executeCommand(testString014);
		assertEquals("learn javascript", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString015 = "a learn php";
		obj.executeCommand(testString015);
		assertEquals("learn php", obj.getFloatingTasksToDo().get(1).getDescription());
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString016 = "add learn bootstrap by 23:59 on 31/09/2016";
		obj.executeCommand(testString016);
		assertEquals("2016-09-30", obj.getScheduledTasksToDo().get(2).getEndDate().toString());
		assertEquals("23:59", obj.getScheduledTasksToDo().get(2).getEndTime().toString());
		assertEquals(3, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		
		String testString017 = "del 1-6";
		obj.executeCommand(testString017);
		assertEquals(3, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals(true, obj.getFeedBack().contains("Task number was not found!"));
		
		String testString018 = "done 1-6";
		obj.executeCommand(testString018);
		assertEquals(3, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals(true, obj.getFeedBack().contains("Task number was not found!"));
		
		String testString019 = "undone 1-6";
		obj.executeCommand(testString019);
		assertEquals(3, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(0, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals(true, obj.getFeedBack().contains("Invalid Undone Operation!"));
		
		String testString020 = "done 1-3";
		obj.executeCommand(testString020);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(3, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("learn bootstrap", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals("learn css", obj.getScheduledTasksComplete().get(1).getDescription());
		assertEquals("learn html5", obj.getScheduledTasksComplete().get(2).getDescription());
		
		String testString021 = "del 1-2";
		obj.executeCommand(testString021);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(3, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("learn bootstrap", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals("learn css", obj.getScheduledTasksComplete().get(1).getDescription());
		assertEquals("learn html5", obj.getScheduledTasksComplete().get(2).getDescription());
		
		String testString022 = "redo";
		obj.executeCommand(testString022);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(3, obj.getScheduledTasksComplete().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals(true, obj.getFeedBack().contains("Invalid Redo!"));
		
		String testString023 = "undo";
		obj.executeCommand(testString023);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(3, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("learn bootstrap", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals("learn css", obj.getScheduledTasksComplete().get(1).getDescription());
		assertEquals("learn html5", obj.getScheduledTasksComplete().get(2).getDescription());
		assertEquals("learn javascript", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals("learn php", obj.getFloatingTasksToDo().get(1).getDescription());
		
		String testString024 = "undone 3,5";
		obj.executeCommand(testString024);
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(1, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("learn html5", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("learn bootstrap", obj.getScheduledTasksToDo().get(1).getDescription());
		assertEquals("learn css", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals("learn javascript", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals("learn php", obj.getFloatingTasksToDo().get(1).getDescription());
		
		String testString025 = "HeLp";
		obj.executeCommand(testString025);
		assertEquals(true, obj.getFeedBack().contains("Help sheet activated"));
		
		String testString026 = "edit 13 ajax";
		obj.executeCommand(testString026);
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals(1, obj.getScheduledTasksComplete().size());
		assertEquals(2, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksComplete().size());
		assertEquals("learn html5", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("learn bootstrap", obj.getScheduledTasksToDo().get(1).getDescription());
		assertEquals("learn css", obj.getScheduledTasksComplete().get(0).getDescription());
		assertEquals("learn javascript", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals("learn php", obj.getFloatingTasksToDo().get(1).getDescription());
		
	}
	
}

