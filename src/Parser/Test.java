package Parser;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
//import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ScheduleHacks.Task;
//import java.time.format.*;

public class Test {

	private static final String REGEX_DATE = "\\d{1,2}(-|/)\\d{1,2}(-|/)(\\d{4}|\\d{2})";
	private static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{1,2})|(\\d{3,4}))(\\s|$)";

	public static void main(String[] args) throws Exception {
		Test obj = new Test();
		
		ArrayList<Integer> f = new ArrayList<Integer>();
		f.add(5);
		f.add(6);
		ArrayList<Integer> k = obj.checkArrayList(f);
		for(int i : k)
			System.out.println(i);
		System.out.println(LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES).toString());
	}

	ArrayList<Integer> checkArrayList(ArrayList<Integer> a) {
		ArrayList<Integer> x = a;
		return x;
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

	void printDates2() {
		String s = "tuesday";
		LocalDate date = LocalDate.parse(s);
		System.out.println(date.toString());
	}
}
