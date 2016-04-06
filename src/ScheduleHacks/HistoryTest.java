package ScheduleHacks;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import ScheduleHacks.OldCommand;

public class HistoryTest {

	@Test
	public void testUndoList() throws Exception {
		History testHistory = History.getInstance();

		ArrayList<Task> taskList = new ArrayList<Task>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		OldCommand testCmd1 = new OldCommand(OldCommand.COMMAND_TYPE.ADD_TASK, taskList, indexList);
		OldCommand testCmd2 = new OldCommand(OldCommand.COMMAND_TYPE.COMPLETE_TASK, taskList, indexList);

		testHistory.addToUndoList(testCmd1);

		OldCommand retrieveCmd1;

		retrieveCmd1 = testHistory.getFromUndoList();

		assertEquals(retrieveCmd1.getCommandType(), OldCommand.COMMAND_TYPE.DELETE_TASK);

		testHistory.addToUndoList(testCmd2);

		OldCommand retrieveCmd2;

		retrieveCmd2 = testHistory.getFromUndoList();

		assertEquals(retrieveCmd2.getCommandType(), OldCommand.COMMAND_TYPE.INCOMPLETE_TASK);

	}

	@Test
	public void testRedoList() throws Exception {
		History testHistory = History.getInstance();

		ArrayList<Task> taskList = new ArrayList<Task>();
		ArrayList<Integer> indexList = new ArrayList<Integer>();

		OldCommand testCmd = new OldCommand(OldCommand.COMMAND_TYPE.ADD_TASK, taskList, indexList);

		testHistory.addToRedoList(testCmd);

		OldCommand retrieveCmd;

		retrieveCmd = testHistory.getFromRedoList();

		assertEquals(retrieveCmd.getCommandType(), OldCommand.COMMAND_TYPE.DELETE_TASK);

	}

	@Test
	public void testCommandHistory() throws Exception {

		History testHistory = History.getInstance();

		testHistory.setIndexCommandHistory();
		assertEquals(null, testHistory.moveUpCommandHistory());
		assertEquals(null, testHistory.moveDownCommandHistory());

		testHistory.addToCommandHistory("1");
		testHistory.addToCommandHistory("2");
		testHistory.addToCommandHistory("3");
		testHistory.addToCommandHistory("4");

		testHistory.setIndexCommandHistory();
		assertEquals("4", testHistory.moveUpCommandHistory());
		assertEquals("3", testHistory.moveUpCommandHistory());
		assertEquals("2", testHistory.moveUpCommandHistory());
		assertEquals("1", testHistory.moveUpCommandHistory());

		assertEquals("2", testHistory.moveDownCommandHistory());
		assertEquals("3", testHistory.moveDownCommandHistory());
		assertEquals("4", testHistory.moveDownCommandHistory());

	}


}
