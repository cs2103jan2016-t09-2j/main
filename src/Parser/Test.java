package Parser;

import java.time.*;
import java.util.*;
/*import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;*/

import Parser.Command.COMMAND_TYPE;
import ScheduleHacks.Task;
//import java.time.format.*;

public class Test {

	public static void main(String[] args) throws Exception {
		Test obj = new Test();
		Scanner sc = new Scanner(System.in);
		System.out.print("ENter text: ");
		String text = sc.nextLine();
		while (!text.equals("0")) {
			obj.checkParser(text);
			System.out.print("ENter text: ");
			text = sc.nextLine();
		}
		// obj.checkDateParser();
		// System.out.println(LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES).toString());
	}

	private void checkParser(String text) {
		try {
			Command cmd = CommandParser.getParsedCommand(text);
			System.out.println("Commnad Type: " + cmd.getCommandType().toString());
			Task t = cmd.getTaskDetails();
			System.out.println(t.getDescription());
			if (t.getEndDate() != null) {
				if (t.getStartDate() != null)
					System.out.println("Start Date:" + t.getStartDate().toString());
				System.out.println("End Date:" + t.getEndDate().toString());
			}
			if (t.getEndTime() != null) {
				if (t.getStartTime() != null)
					System.out.println("Start Time:" + t.getStartTime().toString());
				System.out.println("End Time:" + t.getEndTime().toString());
			}
			if (cmd.getIndexList() != null) {
				for (int x : cmd.getIndexList()) {
					System.out.println(x);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void checkDateParser() {
		String text = " Holiday 2/3/14 5/12/14";
		DateParser ob = new DateParser(text);
		ob.findDates();
		System.out.println(ob.getTaskDetails());
		for (LocalDate d : ob.getDateList()) {
			System.out.println(d.toString());
		}
	}

	public Task findTaskDetails(Command command, String taskStatement) throws Exception {
		COMMAND_TYPE commandType = command.getCommandType();

		if (CommandParser.hasIndexNumber(commandType)) {
			IndexParser indexParser = new IndexParser(command, taskStatement);
			command.setIndexList(indexParser.getIndexList());
			taskStatement = indexParser.getTaskDetails();
		}
		if (!CommandParser.hasTaskDetails(commandType)) {
			return null;
		} else {
			if (!taskStatement.isEmpty()) {
				Task newTask = new Task();
				if (commandType.equals(COMMAND_TYPE.ADD_TASK)) {
					newTask = CommandParser.addNewTask(taskStatement);
				} else if (commandType.equals(COMMAND_TYPE.MODIFY_TASK)) {
					newTask = CommandParser.editExistingTask(taskStatement);
				}
				return newTask;
			}
		}
		throw new Exception("Empty Task Description/Index Number");
	}

	void checkingcommandparser(String s) throws Exception {
		Command cmd = CommandParser.getParsedCommand(s);
		Task t = cmd.getTaskDetails();
		System.out.println("DESCRIPTION: " + t.getDescription());
		if (t.getStartDate() == null)
			System.out.println("null");
		else
			System.out.println(t.getStartDate().toString());
		if (t.getEndDate() == null)
			System.out.println("null");
		else
			System.out.println(t.getEndDate().toString());
		if (t.getStartTime() == null)
			System.out.println("null");
		else
			System.out.println(t.getStartTime().toString());
		if (t.getEndTime() == null)
			System.out.println("null");
		else
			System.out.println(t.getEndTime().toString());
	}

}
