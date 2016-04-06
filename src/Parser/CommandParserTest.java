//@@author A0132778W

package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

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

	/*
	 * Boundary case. Empty String.
	 */
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
		assertEquals("16:00", newTask.getStartTime().toString());
		assertEquals(LocalTime.MAX, newTask.getEndTime());
		assertEquals("2016-05-14", newTask.getStartDate().toString());
		assertEquals("2016-08-14", newTask.getEndDate().toString());
	}

	@Test
	public void checkGetParsedCommand2() throws Exception {
		String testString = "+ Meet ABCD   16";
		Command cmd = CommandParser.getParsedCommand(testString);
		Task newTask = cmd.getTaskDetails();
		assertEquals(true, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals(COMMAND_TYPE.ADD_TASK, cmd.getCommandType());
		assertEquals("Meet ABCD 16", newTask.getDescription());
	}

	@Test
	public void checkGetParsedCommand3() throws Exception {
		String testString = "-  16";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.DELETE_TASK, cmd.getCommandType());
		String output = "";
		for (int x : cmd.getIndexList()) {
			output += x;
		}
		assertEquals("16", output);
		assertEquals(null, cmd.getTaskDetails());
	}

	@Test
	public void checkGetParsedCommand4() throws Exception {
		String testString = "done  16";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.COMPLETE_TASK, cmd.getCommandType());
		String output = "";
		for (int x : cmd.getIndexList()) {
			output += x;
		}
		assertEquals("16", output);
		assertEquals(null, cmd.getTaskDetails());
	}

	@Test
	public void checkGetParsedCommand5() throws Exception {
		String testString = "edit  16  21/3/16";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		String output = "";
		for (int x : cmd.getIndexList()) {
			output += x;
		}
		assertEquals("16", output);
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals("21/3/16", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());

	}

	@Test
	public void checkGetParsedCommand6() throws Exception {
		String testString = "e  16  21/3/16 600";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		String output = "";
		for (int x : cmd.getIndexList()) {
			output += x;
		}
		assertEquals("16", output);
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals("21/3/16 600", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
	}

	@Test
	public void checkGetParsedCommand7() throws Exception {
		String testString = "edit 16  600 1800";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		String output = "";
		for (int x : cmd.getIndexList()) {
			output += x;
		}
		assertEquals("16", output);
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals("600 1800", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
	}

	@Test
	public void checkGetParsedCommand8() throws Exception {
		String testString = "edit  16  21/3/16 600 1800 submit work";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.MODIFY_TASK, cmd.getCommandType());
		String output = "";
		for (int x : cmd.getIndexList()) {
			output += x;
		}
		assertEquals("16", output);
		Task newTask = cmd.getTaskDetails();
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
		assertEquals("21/3/16 600 1800 submit work", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
	}

	@Test
	public void checkGetParsedCommand9() throws Exception {
		try {
			String testString = "del 5";
			Command cmd = CommandParser.getParsedCommand(testString);
			assertEquals(COMMAND_TYPE.DELETE_TASK, cmd.getCommandType());
			String output = "";
			for (int x : cmd.getIndexList()) {
				output += x;
			}
			assertEquals("5", output);
			assertEquals(null, cmd.getTaskDetails());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Test
	public void checkGetParsedCommand10() throws Exception {
		String testString = "add hello 1030";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.ADD_TASK, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		Task newTask = cmd.getTaskDetails();
		assertEquals("hello", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		// assertEquals("2016-03-07", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals("10:30", newTask.getEndTime().toString());
	}

	@Test
	public void checkGetParsedCommand11() throws Exception {
		String testString = "undone 11, 13, 12-17";
		String output = "";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.INCOMPLETE_TASK, cmd.getCommandType());
		for (int x : cmd.getIndexList()) {
			output += x;
		}
		assertEquals("11121314151617", output);
		assertEquals(null, cmd.getTaskDetails());
	}

	@Test
	public void checkGetParsedCommand12() throws Exception {
		String testString = "set timetable";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.ADD_TASK, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		Task newTask = cmd.getTaskDetails();
		assertEquals("set timetable", newTask.getDescription());
		assertEquals(true, newTask.isFloatingTask());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
	}

	/**
	 * Boundary testing for undo. In spite of additional string, detects it as
	 * undo command.
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkGetParsedCommand13() throws Exception {
		String testString = "undo extra stuff";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.UNDO_TASK, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		assertEquals(null, cmd.getTaskDetails());
	}

	/**
	 * Boundary testing for redo. In spite of additional string, detects it as
	 * undo command.
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkGetParsedCommand14() throws Exception {
		String testString = "redo    12   121  extra stuff";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.REDO_TASK, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		assertEquals(null, cmd.getTaskDetails());
	}

	/**
	 * Boundary testing for home view. In spite of additional string, detects it
	 * as undo command.
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkGetParsedCommand15() throws Exception {
		String testString = "home  12   121  extra stuff";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.HOME, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		assertEquals(null, cmd.getTaskDetails());
	}

	/**
	 * Viewing overdue tasks
	 * 
	 * @throws Exception
	 */
	@Test
	public void checkGetParsedCommand16() throws Exception {
		String testString = "view  overdue  ";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.VIEW_LIST, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		Task newTask = cmd.getTaskDetails();
		assertEquals(LocalDate.MIN, newTask.getStartDate());
		assertEquals(LocalDate.now(), newTask.getEndDate());
		assertEquals(LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
				newTask.getEndTime().truncatedTo(ChronoUnit.MINUTES));
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetParsedCommand17() throws Exception {
		String testString = "help    ";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.HELP, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		assertEquals(null, cmd.getTaskDetails());
	}

	@Test
	public void checkGetParsedCommand18() throws Exception {
		String testString = "help  Jim with German  ";
		Command cmd = CommandParser.getParsedCommand(testString);
		assertEquals(COMMAND_TYPE.ADD_TASK, cmd.getCommandType());
		assertEquals(null, cmd.getIndexList());
		Task newTask = cmd.getTaskDetails();
		assertEquals("Help Jim with German", newTask.getDescription());
		assertEquals(true, newTask.isFloatingTask());
		assertEquals(false, newTask.isComplete());
	}

	/**
	 * Boundary test case to check if add throws an exception with blank
	 * description.
	 */
	@Test
	public void checkGetParsedCommand19() {
		String testString = "add  ";
		boolean isException = true;
		try {
			CommandParser.getParsedCommand(testString);
			isException = false;
		} catch (Exception e) {
			assertEquals(true, isException);
		}
		assertEquals(true, isException);
	}

	@Test
	public void checkGetCriteria1() throws Exception {
		String testString = "this week";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2016-04-03", newTask.getStartDate().toString());
		assertEquals("2016-04-10", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria2() throws Exception {
		String testString = "next week";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2016-04-10", newTask.getStartDate().toString());
		assertEquals("2016-04-17", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria3() throws Exception {
		String testString = "this  month  ";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2016-04-01", newTask.getStartDate().toString());
		assertEquals("2016-04-30", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria4() throws Exception {
		String testString = "next month";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2016-05-01", newTask.getStartDate().toString());
		assertEquals("2016-05-31", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria5() throws Exception {
		String testString = "this  year  ";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2016-01-01", newTask.getStartDate().toString());
		assertEquals("2016-12-31", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria6() throws Exception {
		String testString = "next year";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2017-01-01", newTask.getStartDate().toString());
		assertEquals("2017-12-31", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria7() throws Exception {
		String testString = "today";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("today", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(LocalDate.now(), newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria8() throws Exception {
		String testString = "tomorrow";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("tomorrow", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(LocalDate.now().plusDays(1), newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria9() throws Exception {
		String testString = "complete";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("complete", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria10() throws Exception {
		String testString = "21/12/12/12";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("21/12/12/12", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria11() throws Exception {
		String testString = "Jan";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2017-01-01", newTask.getStartDate().toString());
		assertEquals("2017-01-31", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria12() throws Exception {
		String testString = "february";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("", newTask.getDescription());
		assertEquals("2017-02-01", newTask.getStartDate().toString());
		assertEquals("2017-02-28", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria13() throws Exception {
		String testString = "21/12/12";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("21/12/12", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2012-12-21", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria14() throws Exception {
		String testString = "21aug";
		Task newTask = CommandParser.getCriteria(testString);
		assertEquals("21aug", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2016-08-21", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	@Test
	public void checkGetCriteria15() throws Exception {
		String testString = " next  3days";
		Task newTask = CommandParser.getCriteria(testString);
		// assertEquals("", newTask.getDescription());
		assertEquals(LocalDate.now(), newTask.getStartDate());
		assertEquals(LocalDate.now().plusDays(3), newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
	}

	// Convert Scheduled to Floating; remove date from task
	@Test
	public void checkEditExistingTask1() throws Exception {
		Task oldTask = new Task("buy dog", null, LocalDate.parse("2016-08-08"), null, LocalTime.MAX);
		oldTask.setScheduledTask();
		String testString = "del dates";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals(null, newTask.getEndDate());
		assertEquals(null, newTask.getStartTime());
		assertEquals(null, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
		assertEquals(true, newTask.isFloatingTask());
		assertEquals(false, newTask.isScheduledTask());
	}

	// Remove Time from Task
	@Test
	public void checkEditExistingTask2() throws Exception {
		Task oldTask = new Task("buy dog", null, LocalDate.parse("2016-08-08"), null, LocalTime.MAX);
		oldTask.setScheduledTask();
		String testString = "del times";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2016-08-08", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(LocalTime.MAX, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
	}

	// Convert Floating to Scheduled
	@Test
	public void checkEditExistingTask3() throws Exception {
		Task oldTask = new Task("buy dog", null, null, null, null);
		oldTask.setFloatingTask();
		String testString = "23 Feb 2017";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2017-02-23", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(LocalTime.MAX, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
	}

	// Convert Floating to Scheduled
	@Test
	public void checkEditExistingTask4() throws Exception {
		Task oldTask = new Task("buy dog", null, null, null, null);
		oldTask.setFloatingTask();
		String testString = "23/2/17";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals(null, newTask.getStartDate());
		assertEquals("2017-02-23", newTask.getEndDate().toString());
		assertEquals(null, newTask.getStartTime());
		assertEquals(LocalTime.MAX, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
	}

	@Test
	public void checkEditExistingTask5() throws Exception {
		Task oldTask = new Task("buy dog", null, null, null, null);
		oldTask.setFloatingTask();
		String testString = "23/2/17 25/2/17";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals("2017-02-23", newTask.getStartDate().toString());
		assertEquals("2017-02-25", newTask.getEndDate().toString());
		assertEquals(LocalTime.MAX, newTask.getStartTime());
		assertEquals(LocalTime.MAX, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
	}

	@Test
	public void checkEditExistingTask6() throws Exception {
		Task oldTask = new Task("buy dog", null, null, null, null);
		oldTask.setFloatingTask();
		String testString = "23/2/17 25/2/17 5pm";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals("2017-02-23", newTask.getStartDate().toString());
		assertEquals("2017-02-25", newTask.getEndDate().toString());
		assertEquals("17:00", newTask.getStartTime().toString());
		assertEquals(LocalTime.MAX, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
	}

	@Test
	public void checkEditExistingTask7() throws Exception {
		Task oldTask = new Task("buy dog", LocalDate.parse("2016-06-08"), LocalDate.parse("2016-08-08"),
				LocalTime.parse("12:00"), LocalTime.parse("16:00"));
		oldTask.setScheduledTask();
		String testString = "3pm 5pm";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals("2016-06-08", newTask.getStartDate().toString());
		assertEquals("2016-08-08", newTask.getEndDate().toString());
		assertEquals("15:00", newTask.getStartTime().toString());
		assertEquals("17:00", newTask.getEndTime().toString());
		assertEquals(false, newTask.isComplete());
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
	}

	@Test
	public void checkEditExistingTask8() throws Exception {
		Task oldTask = new Task("buy dog", LocalDate.parse("2016-06-08"), LocalDate.parse("2016-08-08"),
				LocalTime.parse("12:00"), LocalTime.parse("16:00"));
		oldTask.setScheduledTask();
		String testString = "d times";
		Task newTask = CommandParser.editExistingTask(oldTask, testString);
		assertEquals("buy dog", newTask.getDescription());
		assertEquals("2016-06-08", newTask.getStartDate().toString());
		assertEquals("2016-08-08", newTask.getEndDate().toString());
		assertEquals(LocalTime.MAX, newTask.getStartTime());
		assertEquals(LocalTime.MAX, newTask.getEndTime());
		assertEquals(false, newTask.isComplete());
		assertEquals(false, newTask.isFloatingTask());
		assertEquals(true, newTask.isScheduledTask());
	}

	@Test
	public void checkSetDir() {
		Task task = new Task();
		String testString = "C:\\\\SH";
		task = CommandParser.setDirectory(new Command(), testString);
		assertEquals("C:\\\\SH", task.getDescription());
	}
}
