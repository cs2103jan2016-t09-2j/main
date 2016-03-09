package Parser;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
//import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Parser.Command.COMMAND_TYPE;
import ScheduleHacks.Task;
//import java.time.format.*;

public class Test {

	//private static final String REGEX_DATE = "\\d{1,2}(-|/)\\d{1,2}(-|/)(\\d{4}|\\d{2})";
	//private static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{1,2})|(\\d{3,4}))(\\s|$)";

	public static void main(String[] args) throws Exception {
		Test obj = new Test();
		
		//System.out.println(LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES).toString());
	}
	
	public Task findTaskDetails(Command command, String taskStatement) throws Exception {
		COMMAND_TYPE commandType = command.getCommandType();
		
		if(CommandParser.hasIndexNumber(commandType)) {
			IndexParser indexParser = new IndexParser(command, taskStatement);
			command.setIndexNumber(indexParser.getIndex());
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
