package Parser;

import java.time.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.ocpsoft.prettytime.*;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import java.time.format.*;

public class Test {
	
	PrettyTimeParser timeParse = new PrettyTimeParser();
	
	private static final String REGEX_DATE = "\\d{1,2}(-|/)\\d{1,2}(-|/)\\d{2,4}";
	private static final String REGEX_TIME = "\\d{1,2}(:|\\.)\\d{1,2}";
	
	
	public static void main(String[] args) {
		Test obj = new Test();
		obj.checkdate("Meet ABCD at 16.00 on 14/05/1234 and 14/08/1273");
		obj.checktime("Meet ABCD at 16:00 on 14/05/1234");
	}
	
	void checkdate(String taskDetails) {
		String date = "";
		Pattern datePattern = Pattern.compile(REGEX_DATE);
		Matcher dateMatcher = datePattern.matcher(taskDetails);
		while(dateMatcher.find()) {
			date = dateMatcher.group();
			System.out.println(date);
		}
	}
	
	void checktime(String str) {
		String time = "";
		Pattern timePattern = Pattern.compile(REGEX_TIME);
		Matcher timeMatcher = timePattern.matcher(str);
		while(timeMatcher.find()) {
			time = timeMatcher.group();
			System.out.println(time);
		}
	}
	
	void printDates1() {
		List<Date> dateList = timeParse.parse("1400  on tuesday");
		//System.out.println(dateList.size());
		for(Date i : dateList) {
			String s = i.toString();
			System.out.println(s);
		}
		/*
			Calendar c = Calendar.getInstance();
			s = c.get(Calendar.YEAR)+"-0"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
			
			LocalDate date = LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
			System.out.println(date.toString());
			
			*/
			//s=s.substring(8,10)+" "+s.substring(4,7)+" "+s.substring(24)+" " + s.substring(11, 16);
			//System.out.println(s);
			/*DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd MMM uuuu");
			DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("HH:mm");
			LocalDate date = LocalDate.parse(s, DateTimeFormatter.ofLocalizedDateTime(dateformat, timeformat));
			System.out.println(date.toString());*/
	}
	
	void printDates2() {
		String s = "tuesday";
		LocalDate date = LocalDate.parse(s);
		System.out.println(date.toString());
	}
}
