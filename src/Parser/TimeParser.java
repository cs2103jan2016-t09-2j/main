package Parser;

import java.util.ArrayList;
import java.time.LocalTime;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import java.time.LocalDate;

public class TimeParser {

	private static final String REGEX_TIME = "\\d{1,2}(:|\\.)\\d{1,2}";
	
	// Instance Variables
	private String taskDetails;
	
	// Default Constructor
	TimeParser() {
		this("");
	}
	
	// Parameterized Constructor
	TimeParser(String newTaskDetails) {
		setTaskDetails(newTaskDetails);
	}
	
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = newTaskDetails;
	}
	
	public String getTaskDetails(){
		return this.taskDetails;
	}

	// incomplete
	protected ArrayList<LocalTime> getTimes() {
		ArrayList<String> stringTimeList = getTimeList(getTaskDetails());
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
			String newTime = timeMatcher.group();
			//int startIndex = timeMatcher.start();
			//int endIndex = timeMatcher.end();
			timeList.add(newTime);
		}
		removeTimesFromTaskDetails();
		return timeList;
	}
	

	private void removeTimesFromTaskDetails() {
		this.taskDetails = taskDetails.replaceAll(REGEX_TIME, " ");
		this.taskDetails = CommandParser.cleanupExtraWhitespace(taskDetails);
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
	private boolean hasTimeList(ArrayList<String> stringTimeList) {
		if (stringTimeList.isEmpty()) {
			return true;
		}
		return false;
	}
}
