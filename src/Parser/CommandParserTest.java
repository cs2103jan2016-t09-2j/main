package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import Parser.Command.COMMAND_TYPE;

import ScheduleHacks.Task;

public class CommandParserTest {

	@Test
	public void testGetFirstWord1() throws Exception {
		String testString = "add life is great";
		String output = CommandParser.getFirstWord(testString);
		assertEquals(output, "add");
	}
	
	@Test
	public void testGetFirstWord2() throws Exception {
		String testString = "Schedule";
		String output = CommandParser.getFirstWord(testString);
		assertEquals(output, "Schedule");
	}

	@Test
	public void testRemoveFirstWord() {
		String testString = "add life is great";
		String output = CommandParser.removeFirstWord(testString);
		assertEquals(output, "life is great");
	}
	
	@Test
	public void testCleanupExtraWhitespace1() {
		String testString = "  checking   random text  123   321       ";
		String output = CommandParser.cleanupExtraWhitespace(testString);
		String expected = "checking random text 123 321";
		assertEquals(expected, output);
	}
	
	@Test
	public void testCleanupExtraWhitespace2() {
		String testString = "textwithoutspace           ";
		String output = CommandParser.cleanupExtraWhitespace(testString);
		String expected = "textwithoutspace";
		assertEquals(expected, output);
	}
	
	@Test
	public void testCleanupExtraWhitespace3() {
		String testString = "           ";
		String output = CommandParser.cleanupExtraWhitespace(testString);
		String expected = "";
		assertEquals(expected, output);
	}
	
	@Test
	public void checkGetParsedCommand1() throws Exception {
		String testString = "+ Meet ABCD   16.00 14/05/16 14/08/16 ";
		Command cmd = CommandParser.getParsedCommand(testString);
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
		assertEquals(COMMAND_TYPE.ADD_TASK, cmd.getCommandType());
		assertEquals("Meet ABCD", newTask.getDescription());
		assertEquals("16:00", newTask.getEndTime().toString());
		assertEquals("2016-05-14", newTask.getStartDate().toString());
		assertEquals("2016-08-14", newTask.getEndDate().toString());
	}
	
	@Test
	public void checkGetParsedCommand2()throws Exception {
		String testString = "+ Meet ABCD   16";
		Command cmd = CommandParser.getParsedCommand(testString);
		Task newTask = cmd.getTaskDetails();
		assertEquals(true, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals(COMMAND_TYPE.ADD_TASK, cmd.getCommandType());
		assertEquals("Meet ABCD 16", newTask.getDescription());
	}
	
	@Test
	public void checkGetParsedCommand3()throws Exception {
		String testString = "-  16";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.DELETE_TASK, cmd.getCommandType());
		assertEquals(16, cmd.getIndexNumber());
		assertEquals(null, cmd.getTaskDetails());
	}
	
	@Test
	public void checkGetParsedCommand4()throws Exception {
		String testString = "done  16";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.COMPLETE_TASK, cmd.getCommandType());
		assertEquals(16, cmd.getIndexNumber());
		assertEquals(null, cmd.getTaskDetails());
	}
	
	@Test
	public void checkGetParsedCommand5()throws Exception {
		String testString = "modify  16  21/3/16";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		assertEquals(16, cmd.getIndexNumber());
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals(null, newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2016-03-21", newTask.getEndDate().toString());
		
	}
	
	@Test
	public void checkGetParsedCommand6()throws Exception {
		String testString = "modify  16  21/3/16 600";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		assertEquals(16, cmd.getIndexNumber());
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals(null, newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2016-03-21", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals("06:00", newTask.getEndTime().toString());
	}
	
	@Test
	public void checkGetParsedCommand7()throws Exception {
		String testString = "modify  16  21/3/16 600 1800";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		assertEquals(16, cmd.getIndexNumber());
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals(null, newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2016-03-21", newTask.getEndDate().toString());
		assertEquals("06:00", newTask.getStartTime().toString());
		assertEquals("18:00", newTask.getEndTime().toString());
	}
	
	@Test
	public void checkGetParsedCommand8()throws Exception {
		String testString = "modify  16  21/3/16 600 1800 submit work";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		assertEquals(16, cmd.getIndexNumber());
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals("submit work", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2016-03-21", newTask.getEndDate().toString());
		assertEquals("06:00", newTask.getStartTime().toString());
		assertEquals("18:00", newTask.getEndTime().toString());
	}
	
	@Test
	public void checkGetParsedCommand9()throws Exception {
		String testString = "del 178";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.DELETE_TASK, cmd.getCommandType());
		assertEquals(178, cmd.getIndexNumber());
		assertEquals(null, cmd.getTaskDetails());
	}
}
