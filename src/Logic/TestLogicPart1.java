package Logic;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import Parser.Command;
import Parser.Command.COMMAND_TYPE;
import ScheduleHacks.Task;

public class TestLogicPart1 {
	
	Logic obj = new Logic();
	
	@Test
	public void testGetCommandType1() {
		String testString = "add attend NUSSU fair 14/03/2016";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE output = obj.getCommand();
		assertEquals(COMMAND_TYPE.ADD_TASK, output);
	}
	
	@Test
	public void testGetCommandType2() {
		String testString = "edit 1u desc submit resume during NUSSU fair";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE output = obj.getCommand();
		assertEquals(COMMAND_TYPE.MODIFY_TASK, output);
	}
	
	@Test
	public void testGetCommandType3() {
		String testString = "done submit resume during NUSSU fair";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE output = obj.getCommand();
		assertEquals(COMMAND_TYPE.COMPLETE_TASK, output);
		
	}
	
	@Test
	public void testGetCommandType4() {
		String testString = "clear 1u";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE output = obj.getCommand();
		assertEquals(COMMAND_TYPE.DELETE_TASK, output);
		
	}
	
	@Test
	public void testGetCommandType5() {
		String testString = "exit";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE output = obj.getCommand();
		assertEquals(COMMAND_TYPE.EXIT, output);
	}
	
	@Test
	public void testAddTask1() {
		String testString = "add study for mid-term 16:00 17.00 14/03/2016";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE executeCommand = obj.getCommand();
		Task executeTask = obj.getTaskDescription();
		obj.execute(executeCommand,executeTask, testString);
		assertEquals(false, executeTask.isFloatingTask());
		assertEquals(true, executeTask.isScheduledTask());
		assertEquals("study for mid-term", executeTask.getDescription());
		assertEquals("16:00", executeTask.getStartTime().toString());
		assertEquals("17:00", executeTask.getEndTime().toString());
		assertEquals(null, executeTask.getStartDate());
		assertEquals("2016-03-14", executeTask.getEndDate().toString());
	}
	
	@Test
	public void testAddTask2() {
		String testString = "create visit NTUC fairprice 16:00 21/03/16";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE executeCommand = obj.getCommand();
		Task executeTask = obj.getTaskDescription();
		obj.execute(executeCommand,executeTask, testString);
		assertEquals(false, executeTask.isFloatingTask());
		assertEquals(true, executeTask.isScheduledTask());
		assertEquals("visit NTUC fairprice", executeTask.getDescription());
		assertEquals(null, executeTask.getStartTime());
		assertEquals("16:00", executeTask.getEndTime().toString());
		assertEquals(null, executeTask.getStartDate());
		assertEquals("2016-03-21", executeTask.getEndDate().toString());
	}
	
	@Test
	public void testAddTask3() {
		String testString = "+ attend CS2103 lecture 16:00 14/03/2016 21/03/16";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE executeCommand = obj.getCommand();
		Task executeTask = obj.getTaskDescription();
		obj.execute(executeCommand,executeTask, testString);
		assertEquals(false, executeTask.isFloatingTask());
		assertEquals(true, executeTask.isScheduledTask());
		assertEquals("attend CS2103 lecture", executeTask.getDescription());
		assertEquals(null, executeTask.getStartTime());
		assertEquals("16:00", executeTask.getEndTime().toString());
		assertEquals("2016-03-14", executeTask.getStartDate().toString());
		assertEquals("2016-03-21", executeTask.getEndDate().toString());
	}
	
	@Test
	public void testAddTask4() {
		String testString = "+ create 3D cake";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE executeCommand = obj.getCommand();
		Task executeTask = obj.getTaskDescription();
		obj.execute(executeCommand,executeTask, testString);
		assertEquals(true, executeTask.isFloatingTask());
		assertEquals(false, executeTask.isScheduledTask());
		assertEquals("create 3D cake", executeTask.getDescription());
		assertEquals(null, executeTask.getStartTime());
		assertEquals(null, executeTask.getEndTime());
		assertEquals(null, executeTask.getStartDate());
		assertEquals(null, executeTask.getEndDate());
	}
	
	@Test
	public void testAddTask5() {
		String testString = "add add hip hop dance programme";
		obj.retrieveParsedCommand(testString);
		Command.COMMAND_TYPE executeCommand = obj.getCommand();
		Task executeTask = obj.getTaskDescription();
		obj.execute(executeCommand,executeTask, testString);
		assertEquals(true, executeTask.isFloatingTask());
		assertEquals(false, executeTask.isScheduledTask());
		assertEquals("add hip hop dance programme", executeTask.getDescription());
		assertEquals(null, executeTask.getStartTime());
		assertEquals(null, executeTask.getEndTime());
		assertEquals(null, executeTask.getStartDate());
		assertEquals(null, executeTask.getEndDate());
	}

}
