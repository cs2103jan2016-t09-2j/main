package Parser;

import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

//import java.time.LocalDate;
import java.time.LocalTime;

public class TimeParser {
	
	private static final String REGEX_TIME = "\\d{1,2}(:|\\.)\\d{1,2}";
	
	 ArrayList<String> findTime(String taskDetails){
		ArrayList<String> timeList = new ArrayList<String>(); 
		Pattern timePattern = Pattern.compile(REGEX_TIME);
		Matcher timeMatcher = timePattern.matcher(taskDetails);
		while(timeMatcher.find()) {
			timeList.add(timeMatcher.group());
		}
		return timeList;
	}
}
