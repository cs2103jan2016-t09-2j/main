package Parser;

import java.util.ArrayList;
import java.time.LocalTime;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import java.time.LocalDate;

/**
 * This class contains all possible combinations of time, that the user can
 * input as command into Schedule Hacks.
 * 
 * Accepted Time Formats -> HH:MM HH.MM HHMM
 * 
 * 
 * @author Snigdha Singhania
 */
public class TimeParser {

	// private static final String REGEX_TIME =
	// "(\\s\\d{1,2}(:|\\.)\\d{2}\\s)|(\\s\\d{3,4}\\s)";
	private static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{1,2})|(\\d{3,4}))(\\s|$)";

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

	public String getTaskDetails() {
		return this.taskDetails;
	}

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
			timeList.add(newTime);
		}
		removeTimesFromTaskDetails();
		return timeList;
	}

	public void removeTimesFromTaskDetails() {
		this.taskDetails = taskDetails.replaceAll(REGEX_TIME, " ");
		this.taskDetails = CommandParser.cleanupExtraWhitespace(taskDetails);
	}

	private ArrayList<LocalTime> getLocalTimeList(ArrayList<String> stringTimeList) {
		ArrayList<LocalTime> localTimeList = new ArrayList<LocalTime>();
		for (String time : stringTimeList) {
			localTimeList.add(getLocalTimeObject(time));
		}
		return localTimeList;
	}

	/**
	 * This method converts String time to LocalTime object
	 * 
	 * @param time
	 *            which is contained in the usercommand
	 * @return LocalTime Object
	 */
	protected LocalTime getLocalTimeObject(String time) {
		int hour = 0, minute = 00;
		time = time.trim();
		minute = Integer.parseInt(time.substring(time.length() - 2));
		time = time.substring(0, time.length() - 2);
		for (int index = time.length() - 1; index >= 0; index--) {
			if (Character.isDigit(time.charAt(index))) {
				hour = Integer.parseInt(time.substring(0, index + 1));
				break;
			}
		}
		return LocalTime.of(hour, minute);
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
			return false;
		}
		return true;
	}
}
