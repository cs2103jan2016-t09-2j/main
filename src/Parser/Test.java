package Parser;

import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import ScheduleHacks.Task;
//import java.time.format.*;

public class Test {

	private static final String REGEX_DATE = "\\d{1,2}(-|/)\\d{1,2}(-|/)(\\d{4}|\\d{2})";
	private static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{1,2})|(\\d{3,4}))(\\s|$)";

	public static void main(String[] args)throws Exception {
		Test obj = new Test();
		// obj.checkdate("Meet ABCD at 16.00 on 14/05/1234 and 14/08/1273
		// 1/3/12");
		//obj.checktime("1600 1700 on 14/05/11234");
		//obj.checkingcommandparser("aa bb cc 22/3/10 23:09 10:09");
		obj.sortDates();
	}
	
	void sortDates() {
		ArrayList<LocalDate> date = new ArrayList<LocalDate>();
		date.add(LocalDate.of(2015, 12, 15));
		date.add(LocalDate.of(2012, 12, 15));
		Collections.sort(date);
		for(LocalDate i : date) {
			System.out.println(i.toString());
		}
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

	void checkingcommandparser(String s)throws Exception {
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
