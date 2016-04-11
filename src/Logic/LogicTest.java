package Logic;

import static org.junit.Assert.*;

import java.io.File;
import java.time.LocalDate;

import org.junit.Test;

import Parser.Command;
import Parser.Command.COMMAND_TYPE;
import Parser.CommandParser;
import ScheduleHacks.Task;

import Storage.Storage;
import org.apache.commons.io.FileUtils;

public class LogicTest {

	Logic obj = Logic.getInstance();

	@Test
	public void testLogic() throws Exception {

		try {
			String testDir = "Test";
			String fileLocation = Storage.getCurrentPathName();
			FileUtils.copyDirectory(new File(fileLocation), new File(testDir));
			// Storage.getInstance().setCurrentPathName(testDir);
			FileUtils.deleteDirectory(new File(fileLocation));
		} catch (Exception e) {
			e.printStackTrace();
		}

		String testString = "add study for mid-term 1600 1700 04 apr 16";
		Command existingCommand = CommandParser.getParsedCommand(testString);
		COMMAND_TYPE typeCommand = obj.getCommand(existingCommand);
		Task executeTask = obj.getTaskDescription(existingCommand);
		obj.execute(typeCommand, existingCommand, executeTask);
		assertEquals(false, executeTask.isFloatingTask());
		assertEquals(true, executeTask.isScheduledTask());
		assertEquals("study for mid-term", executeTask.getDescription());
		assertEquals("16:00", executeTask.getStartTime().toString());
		assertEquals("17:00", executeTask.getEndTime().toString());
		assertEquals("2016-04-04", executeTask.getStartDate().toString());
		assertEquals("2016-04-04", executeTask.getEndDate().toString());

		String testString2 = "cs2103 test cases today 2300";
		Command existingCommand2 = CommandParser.getParsedCommand(testString2);
		COMMAND_TYPE typeCommand2 = obj.getCommand(existingCommand2);
		Task executeTask2 = obj.getTaskDescription(existingCommand2);
		obj.execute(typeCommand2, existingCommand2, executeTask2);
		assertEquals(false, executeTask2.isFloatingTask());
		assertEquals(true, executeTask2.isScheduledTask());
		assertEquals("cs2103 test cases", executeTask2.getDescription());
		assertEquals(null, executeTask2.getStartTime());
		assertEquals("23:00", executeTask2.getEndTime().toString());
		assertEquals(null, executeTask2.getStartDate());
		assertEquals(LocalDate.now(), executeTask2.getEndDate());

		String testString3 = "cycle tmr 1600 1800";
		Command existingCommand3 = CommandParser.getParsedCommand(testString3);
		COMMAND_TYPE typeCommand3 = obj.getCommand(existingCommand3);
		Task executeTask3 = obj.getTaskDescription(existingCommand3);
		obj.execute(typeCommand3, existingCommand3, executeTask3);
		assertEquals(false, executeTask3.isFloatingTask());
		assertEquals(true, executeTask3.isScheduledTask());
		assertEquals("cycle", executeTask3.getDescription());
		assertEquals("16:00", executeTask3.getStartTime().toString());
		assertEquals("18:00", executeTask3.getEndTime().toString());
		assertEquals(LocalDate.now().plusDays(1), executeTask3.getStartDate());
		assertEquals(LocalDate.now().plusDays(1), executeTask3.getEndDate());

		String testString4 = "dance lessons tmr 1700 1830";
		Command existingCommand4 = CommandParser.getParsedCommand(testString4);
		COMMAND_TYPE typeCommand4 = obj.getCommand(existingCommand4);
		Task executeTask4 = obj.getTaskDescription(existingCommand4);
		obj.execute(typeCommand4, existingCommand4, executeTask4);
		assertEquals(false, executeTask4.isFloatingTask());
		assertEquals(true, executeTask4.isScheduledTask());
		assertEquals("dance lessons", executeTask4.getDescription());
		assertEquals("17:00", executeTask4.getStartTime().toString());
		assertEquals("18:30", executeTask4.getEndTime().toString());
		assertEquals(LocalDate.now().plusDays(1), executeTask4.getStartDate());
		assertEquals(LocalDate.now().plusDays(1), executeTask4.getEndDate());
		assertEquals("Task added successfully but new task is conflicting with cycle", obj.getFeedBack());

		String testString5 = "cycle tmr 1600 1800";
		Command existingCommand5 = CommandParser.getParsedCommand(testString5);
		COMMAND_TYPE typeCommand5 = obj.getCommand(existingCommand5);
		Task executeTask5 = obj.getTaskDescription(existingCommand5);
		obj.execute(typeCommand5, existingCommand5, executeTask5);
		assertEquals(false, executeTask5.isFloatingTask());
		assertEquals(true, executeTask5.isScheduledTask());
		assertEquals(3, obj.getScheduledTasksToDo().size());
		assertEquals("Task entered by user already exists! Task not added!", obj.getFeedBack());

		String testString05 = "swim tmr 1630-1730";
		Command existingCommand05 = CommandParser.getParsedCommand(testString05);
		COMMAND_TYPE typeCommand05 = obj.getCommand(existingCommand05);
		Task executeTask05 = obj.getTaskDescription(existingCommand05);
		obj.execute(typeCommand05, existingCommand05, executeTask05);
		assertEquals(false, executeTask05.isFloatingTask());
		assertEquals(true, executeTask05.isScheduledTask());
		assertEquals("swim", executeTask05.getDescription());
		assertEquals("16:30", executeTask05.getStartTime().toString());
		assertEquals("17:30", executeTask05.getEndTime().toString());
		assertEquals(LocalDate.now().plusDays(1), executeTask05.getStartDate());
		assertEquals(LocalDate.now().plusDays(1), executeTask05.getEndDate());
		assertEquals("Task added successfully but task is conflicting with several existing tasks!", obj.getFeedBack());

		String testString6 = "del";
		Command existingCommand6 = CommandParser.getParsedCommand(testString6);
		COMMAND_TYPE typeCommand6 = obj.getCommand(existingCommand6);
		Task executeTask6 = obj.getTaskDescription(existingCommand6);
		obj.execute(typeCommand6, existingCommand6, executeTask6);
		assertEquals(obj.getScheduledTasksToDo().size(), 3);
		assertEquals("cs2103 test cases", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("cycle", obj.getScheduledTasksToDo().get(1).getDescription());
		assertEquals("dance lessons", obj.getScheduledTasksToDo().get(2).getDescription());

		String testString7 = "del 1,4";
		Command existingCommand7 = CommandParser.getParsedCommand(testString7);
		COMMAND_TYPE typeCommand7 = obj.getCommand(existingCommand7);
		Task executeTask7 = obj.getTaskDescription(existingCommand7);
		obj.execute(typeCommand7, existingCommand7, executeTask7);
		assertEquals(2, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksOverDue().size());
		assertEquals("cs2103 test cases", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("cycle", obj.getScheduledTasksToDo().get(1).getDescription());

		String testString8 = "edit 1 dogfood app 2200 on 30 apr";
		Command existingCommand8 = CommandParser.getParsedCommand(testString8);
		COMMAND_TYPE typeCommand8 = obj.getCommand(existingCommand8);
		Task executeTask8 = obj.getTaskDescription(existingCommand8);
		obj.execute(typeCommand8, existingCommand8, executeTask8);
		assertEquals("dogfood app", obj.getScheduledTasksToDo().get(1).getDescription());
		assertEquals(LocalDate.of(2016, 4, 30), obj.getScheduledTasksToDo().get(1).getEndDate());
		assertEquals("22:00", obj.getScheduledTasksToDo().get(1).getEndTime().toString());

		String testString9 = "e tmr 1900 2100";
		Command existingCommand9 = CommandParser.getParsedCommand(testString9);
		COMMAND_TYPE typeCommand9 = obj.getCommand(existingCommand9);
		Task executeTask9 = obj.getTaskDescription(existingCommand9);
		obj.execute(typeCommand9, existingCommand9, executeTask9);
		assertEquals(LocalDate.now().plusDays(1), obj.getScheduledTasksToDo().get(1).getEndDate());
		assertEquals("19:00", obj.getScheduledTasksToDo().get(1).getStartTime().toString());
		assertEquals("21:00", obj.getScheduledTasksToDo().get(1).getEndTime().toString());

		String testString10 = "e delete date";
		Command existingCommand10 = CommandParser.getParsedCommand(testString10);
		COMMAND_TYPE typeCommand10 = obj.getCommand(existingCommand10);
		Task executeTask10 = obj.getTaskDescription(existingCommand10);
		obj.execute(typeCommand10, existingCommand10, executeTask10);
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals("dogfood app", obj.getFloatingTasksToDo().get(0).getDescription());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getEndDate());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getStartTime());
		assertEquals(null, obj.getFloatingTasksToDo().get(0).getEndTime());
		assertEquals(true, obj.getFloatingTasksToDo().get(0).isFloatingTask());

		String testString11 = "done";
		Command existingCommand11 = CommandParser.getParsedCommand(testString11);
		COMMAND_TYPE typeCommand11 = obj.getCommand(existingCommand11);
		Task executeTask11 = obj.getTaskDescription(existingCommand11);
		obj.execute(typeCommand11, existingCommand11, executeTask11);
		assertEquals(1, obj.getFloatingTasksComplete().size());
		assertEquals("dogfood app", obj.getFloatingTasksComplete().get(0).getDescription());

		String testString12 = "done 1";
		Command existingCommand12 = CommandParser.getParsedCommand(testString12);
		COMMAND_TYPE typeCommand12 = obj.getCommand(existingCommand12);
		Task executeTask12 = obj.getTaskDescription(existingCommand12);
		obj.execute(typeCommand12, existingCommand12, executeTask12);
		assertEquals(1, obj.getScheduledTasksComplete().size());
		assertEquals("cycle", obj.getScheduledTasksComplete().get(0).getDescription());

		String testString13 = "undone";
		Command existingCommand13 = CommandParser.getParsedCommand(testString13);
		COMMAND_TYPE typeCommand13 = obj.getCommand(existingCommand13);
		Task executeTask13 = obj.getTaskDescription(existingCommand13);
		obj.execute(typeCommand13, existingCommand13, executeTask13);
		assertEquals(1, obj.getFloatingTasksToDo().size());
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals("dogfood app", obj.getFloatingTasksToDo().get(0).getDescription());

		String testString14 = "undone 2";
		Command existingCommand14 = CommandParser.getParsedCommand(testString14);
		COMMAND_TYPE typeCommand14 = obj.getCommand(existingCommand14);
		Task executeTask14 = obj.getTaskDescription(existingCommand14);
		obj.execute(typeCommand14, existingCommand14, executeTask14);
		assertEquals(1, obj.getScheduledTasksToDo().size());

		String testString15 = "undo";
		Command existingCommand15 = CommandParser.getParsedCommand(testString15);
		COMMAND_TYPE typeCommand15 = obj.getCommand(existingCommand15);
		Task executeTask15 = obj.getTaskDescription(existingCommand15);
		obj.execute(typeCommand15, existingCommand15, executeTask15);
		assertEquals(1, obj.getScheduledTasksComplete().size());

		String testString16 = "redo";
		Command existingCommand16 = CommandParser.getParsedCommand(testString16);
		COMMAND_TYPE typeCommand16 = obj.getCommand(existingCommand16);
		Task executeTask16 = obj.getTaskDescription(existingCommand16);
		obj.execute(typeCommand16, existingCommand16, executeTask16);
		assertEquals(1, obj.getScheduledTasksToDo().size());

		String testString17 = "redo";
		Command existingCommand17 = CommandParser.getParsedCommand(testString17);
		COMMAND_TYPE typeCommand17 = obj.getCommand(existingCommand17);
		Task executeTask17 = obj.getTaskDescription(existingCommand17);
		obj.execute(typeCommand17, existingCommand17, executeTask17);
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("Invalid Redo!", obj.getFeedBack());

		String testString18 = "undo";
		Command existingCommand18 = CommandParser.getParsedCommand(testString18);
		COMMAND_TYPE typeCommand18 = obj.getCommand(existingCommand18);
		Task executeTask18 = obj.getTaskDescription(existingCommand18);
		obj.execute(typeCommand18, existingCommand18, executeTask18);
		assertEquals(1, obj.getScheduledTasksComplete().size());

		String testString19 = "e 04/05/2016";
		Command existingCommand19 = CommandParser.getParsedCommand(testString19);
		COMMAND_TYPE typeCommand19 = obj.getCommand(existingCommand19);
		Task executeTask19 = obj.getTaskDescription(existingCommand19);
		obj.execute(typeCommand19, existingCommand19, executeTask19);
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("2016-05-04", obj.getScheduledTasksToDo().get(0).getEndDate().toString());

		String testString20 = "del 1-2";
		Command existingCommand20 = CommandParser.getParsedCommand(testString20);
		COMMAND_TYPE typeCommand20 = obj.getCommand(existingCommand20);
		Task executeTask20 = obj.getTaskDescription(existingCommand20);
		obj.execute(typeCommand20, existingCommand20, executeTask20);
		assertEquals(0, obj.getScheduledTasksToDo().size());
		assertEquals(0, obj.getFloatingTasksToDo().size());
		assertEquals("Task Deleted Successfully", obj.getFeedBack());

		String testString21 = "go for XMen Apocalypse 01/05/2016";
		Command existingCommand21 = CommandParser.getParsedCommand(testString21);
		COMMAND_TYPE typeCommand21 = obj.getCommand(existingCommand21);
		Task executeTask21 = obj.getTaskDescription(existingCommand21);
		obj.execute(typeCommand21, existingCommand21, executeTask21);
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("go for XMen Apocalypse", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-05-01", obj.getScheduledTasksToDo().get(0).getEndDate().toString());

		String testString22 = "home";
		Command existingCommand22 = CommandParser.getParsedCommand(testString22);
		COMMAND_TYPE typeCommand22 = obj.getCommand(existingCommand22);
		Task executeTask22 = obj.getTaskDescription(existingCommand22);
		obj.execute(typeCommand22, existingCommand22, executeTask22);
		assertEquals("Home Screen Display", obj.getFeedBack());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("go for XMen Apocalypse", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-05-01", obj.getScheduledTasksToDo().get(0).getEndDate().toString());

		String testString23 = "view all";
		Command existingCommand23 = CommandParser.getParsedCommand(testString23);
		COMMAND_TYPE typeCommand23 = obj.getCommand(existingCommand23);
		Task executeTask23 = obj.getTaskDescription(existingCommand23);
		obj.execute(typeCommand23, existingCommand23, executeTask23);
		assertEquals("", obj.getFeedBack());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("go for XMen Apocalypse", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-05-01", obj.getScheduledTasksToDo().get(0).getEndDate().toString());

		String testString24 = "help";
		Command existingCommand24 = CommandParser.getParsedCommand(testString24);
		COMMAND_TYPE typeCommand24 = obj.getCommand(existingCommand24);
		Task executeTask24 = obj.getTaskDescription(existingCommand24);
		obj.execute(typeCommand24, existingCommand24, executeTask24);
		assertEquals("Help sheet activated", obj.getFeedBack());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("go for XMen Apocalypse", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-05-01", obj.getScheduledTasksToDo().get(0).getEndDate().toString());

		try {
			String testDir = "Test";
			String fileLocation = Storage.getCurrentPathName();
			FileUtils.copyDirectory(new File(testDir), new File(fileLocation));
			FileUtils.deleteDirectory(new File(testDir));
		} catch (Exception e) {
		}
	}
}
