package Parser;

import java.util.ArrayList;
import java.time.LocalTime;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/*
 * This class contains all possible combinations of time, that the user can
 * input as command into Schedule Hacks.
 * 
 * Accepted Time Formats -> HH:MM HH.MM HHMM
 * 
 * 
 * @author Snigdha Singhania
 */
public class TimeParser {

	private static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{2})|(\\d{3,4}))(\\s|$)";
	
	private static final int FIRST_INDEX = 0;
	//private static final int INVALID_SIZE = -1;
	
	private static final int MAX_MINUTES = 59;
	private static final int MAX_HOUR = 23;

	// Instance Variables
	private String taskDetails;

	/**************** CONSTRUCTORS *********************/
	// Default Constructor
	TimeParser() {
		this("");
	}

	// Parameterized Constructor
	TimeParser(String newTaskDetails) {
		setTaskDetails(newTaskDetails);
	}


	/****************** SETTER METHODS ***********************/
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = newTaskDetails;
	}
	
	/******************* GETTER METHODS ***********************/
	public String getTaskDetails() {
		return this.taskDetails;
	}
	
	/**************** OTHER METHODS ***********************/
	protected ArrayList<LocalTime> getTimes() {
		ArrayList<LocalTime> timeList;
		ArrayList<String> stringTimeList = getTimeList(getTaskDetails());
		if (hasTimeList(stringTimeList)) {
			timeList = getLocalTimeList(stringTimeList);			
		} else {
			timeList = null;
		}
		return timeList;
	}

	ArrayList<String> getTimeList(String taskDetails) {
		ArrayList<String> timeList = new ArrayList<String>();
		Pattern timePattern = Pattern.compile(REGEX_TIME);
		Matcher timeMatcher = timePattern.matcher(taskDetails);
		while (timeMatcher.find()) {
			String newTime = timeMatcher.group();
			if (isValidTime(newTime)) {
				timeList.add(newTime.trim());
			}
		}
		removeFromTaskDetails(timeList);
		return timeList;
	}

	public void removeFromTaskDetails(ArrayList<String> timeList) {
		String taskDetails = getTaskDetails();
		for(String time: timeList) {
			taskDetails = taskDetails.replace(time, ""); 
		}
		setTaskDetails(CommandParser.cleanupExtraWhitespace(taskDetails));
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
		for (int index = time.length() - 1; index >= FIRST_INDEX; index--) {
			if (Character.isDigit(time.charAt(index))) {
				hour = Integer.parseInt(time.substring(FIRST_INDEX, index + 1));
				break;
			}
		}
		return LocalTime.of(hour, minute);
	}
	
	

	/**
	 * This method checks if the time input by the user is in acceptable format and range
	 * or not
	 * 
	 * @param newTime
	 *            is the time to check for validity
	 * @return true if the time is valid, otherwise false
	 */
	public boolean isValidTime(String newTime) {
		if(isValidMinutes(newTime.trim()) && (isValidHour(newTime.trim()))) {
			return true;
		}
		return false;
	}

	public boolean isValidMinutes(String newTime) {
		if (Integer.parseInt(newTime.substring(newTime.length() - 2, newTime.length())) > MAX_MINUTES) {
			return false;
		}
		return true;
	}
	
	public boolean isValidHour(String newTime) {
		int hour = MAX_HOUR + 1;
		int index;
		for(index = FIRST_INDEX; index < newTime.length()-2; index++) {
			if(!Character.isDigit(newTime.charAt(index))) {
				break;
			}
		}
		hour = Integer.parseInt(newTime.substring(FIRST_INDEX,index));
		if(hour > MAX_HOUR) {
			return false;
		}
		return true;
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
