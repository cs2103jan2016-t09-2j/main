package Parser;

import java.util.ArrayList;
import java.time.LocalTime;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import java.time.LocalDate;

public class TimeParser {

	private static final String REGEX_TIME = "\\d{1,2}(:|\\.)\\d{1,2}";
	
	// incomplete
	protected ArrayList<LocalTime> getTimes(String taskDetails) {
		ArrayList<String> stringTimeList = getTimeList(taskDetails);
		if (hasTimeList(stringTimeList)) {
			ArrayList<LocalTime> timeList = getLocalTimeList(stringTimeList);
			return timeList;
		}
		return null;
	}

	ArrayList<String> getTimeList(String taskDetails) {
		ArrayList<String> timeList = new ArrayList<String>();
		Pattern timePattern = Pattern.compile(REGEX_TIME);
		Matcher timeMatcher = timePattern.matcher(taskDetails);
		while (timeMatcher.find()) {
			timeList.add(timeMatcher.group());
		}
		return timeList;
	}
	

	private ArrayList<LocalTime> getLocalTimeList(ArrayList<String> stringTimeList) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method returns true if the input string has any times, otherwise
	 * false. If no time and date means it is a floating task.
	 * 
	 * @param stringTimeList
	 *            is the list of times contained in the user statement.
	 */
	protected boolean hasTimeList(ArrayList<String> stringTimeList) {
		if (stringTimeList.isEmpty()) {
			return true;
		}
		return false;
	}
}
