package Logic;

import static org.junit.Assert.*;

import org.junit.Test;

import Parser.Command;
import Parser.CommandParser;
import Parser.Command.COMMAND_TYPE;
import ScheduleHacks.Task;

public class SearchTest {
	Logic obj = Logic.getInstance();
	

	public void searchTest1() throws Exception {
		String testString25 = "search X-Men";
		Command existingCommand25 = CommandParser.getParsedCommand(testString25);
		COMMAND_TYPE typeCommand25 = obj.getCommand(existingCommand25);
		Task executeTask25 = obj.getTaskDescription(existingCommand25);
		obj.execute(typeCommand25, existingCommand25, executeTask25);
		assertEquals("Search Found", obj.getFeedBack());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("go for X-Men Apocalypse", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-05-01", obj.getScheduledTasksToDo().get(0).getEndDate().toString());
		
		String testString26 = "view 1 May 16";
		Command existingCommand26 = CommandParser.getParsedCommand(testString26);
		COMMAND_TYPE typeCommand26 = obj.getCommand(existingCommand26);
		Task executeTask26 = obj.getTaskDescription(existingCommand26);
		obj.execute(typeCommand26, existingCommand26, executeTask26);
		assertEquals("Search Found", obj.getFeedBack());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("go for X-Men Apocalypse", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-05-01", obj.getScheduledTasksToDo().get(0).getEndDate().toString());
		
		String testString27 = "view April";
		Command existingCommand27 = CommandParser.getParsedCommand(testString27);
		COMMAND_TYPE typeCommand27 = obj.getCommand(existingCommand27);
		Task executeTask27 = obj.getTaskDescription(existingCommand27);
		obj.execute(typeCommand27, existingCommand27, executeTask27);
		assertEquals("Search Not Found", obj.getFeedBack());
		assertEquals(1, obj.getScheduledTasksToDo().size());
		assertEquals("go for X-Men Apocalypse", obj.getScheduledTasksToDo().get(0).getDescription());
		assertEquals("2016-05-01", obj.getScheduledTasksToDo().get(0).getEndDate().toString());
	}
}
