package Parser;

import java.time.*;

//import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.ocpsoft.prettytime.*;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

import ScheduleHacks.Task;
//import java.time.format.*;

public class Test {

	PrettyTimeParser timeParse = new PrettyTimeParser();

	private static final String REGEX_DATE = "\\d{1,2}(-|/)\\d{1,2}(-|/)(\\d{4}|\\d{2})";
	private static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{1,2})|(\\d{3,4}))(\\s|$)";

	public static void main(String[] args) {
		Test obj = new Test();
		// obj.checkdate("Meet ABCD at 16.00 on 14/05/1234 and 14/08/1273
		// 1/3/12");
		//obj.checktime("1600 1700 on 14/05/11234");
		obj.checkingcommandparser("add Meet ABCD 00.09 12.40 9/2/17");
		//System.out.println("End");
	}

	void checkdate(String taskDetails) {
		String date = "";
		Pattern datePattern = Pattern.compile(REGEX_DATE);
		Matcher dateMatcher = datePattern.matcher(taskDetails);
		while (dateMatcher.find()) {
			date = dateMatcher.group();
			System.out.println(date);
		}
	}

	void checktime(String str) {
		String time = "";
		Pattern timePattern = Pattern.compile(REGEX_TIME);
		Matcher timeMatcher = timePattern.matcher(str);
		while (timeMatcher.find()) {
			time = timeMatcher.group();
			System.out.println(time);
		}
	}

	void checkingcommandparser(String s) {
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

	void printDates2() {
		String s = "tuesday";
		LocalDate date = LocalDate.parse(s);
		System.out.println(date.toString());
	}
}
