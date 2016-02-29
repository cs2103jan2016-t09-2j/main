package Parser;

import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.time.LocalDate;
//import java.time.Month;
//import java.time.LocalTime;

/**
 * This class contains all possible combinations of date and time, that the user
 * can input as command into Schedule Hacks.
 * 
 * Accepted Date Formats -> DD/MM/YYYY DD/MM/YY DD-MM-YYYY DD-MM-YY
 * 
 * Accepted Time Formats -> HH:MM HH.MM
 * 
 * 
 * @author Snigdha Singhania
 */
class DateParser {

	private static final String REGEX_DATE = "\\d{1,2}(-|/)\\d{1,2}(-|/)(\\d{4}|\\d{2})";

	// private static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";

	// Instance Variables
	private String taskDetails;

	DateParser() {
		this("");
	}

	DateParser(String newTaskDetails) {
		setTaskDetails(newTaskDetails);
	}
	
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = newTaskDetails;
	}
	
	public String getTaskDetails(){
		return this.taskDetails;
	}

	protected ArrayList<LocalDate> getDates() {
		ArrayList<String> stringDateList = getDateList(getTaskDetails());
		if (hasDateList(stringDateList)) {
			ArrayList<LocalDate> dateList = getLocalDateList(stringDateList);
			return dateList;
		}
		return null;
	}

	ArrayList<String> getDateList(String taskDetails) {
		ArrayList<String> dateList = new ArrayList<String>();
		Pattern datePattern = Pattern.compile(REGEX_DATE);
		Matcher dateMatcher = datePattern.matcher(taskDetails);
		while (dateMatcher.find()) {
			dateList.add(dateMatcher.group());
		}
		removeDatesFromTaskDetails();
		return dateList;
	}
	
	protected void removeDatesFromTaskDetails() {
		this.taskDetails = taskDetails.replaceAll(REGEX_DATE, " ");
		this.taskDetails = CommandParser.cleanupExtraWhitespace(taskDetails);
	}
	
	/**
	 * This method returns true if the input string has any dates,
	 * otherwise false.
	 * If no time and date means it is a floating task.
	 * 
	 * @param stringDateList
	 * 				is the list of dates contained in the user statement.
	 */
	private boolean hasDateList(ArrayList<String> stringDateList) {
		if(stringDateList.isEmpty()) {
			return false;
		}
		return true;
	}

	private ArrayList<LocalDate> getLocalDateList(ArrayList<String> stringDateList) {
		ArrayList<LocalDate> localDateList = new ArrayList<LocalDate>();
		for (String date : stringDateList) {
			localDateList.add(getLocalDateObject(date));
		}
		return localDateList;
	}
	
	protected LocalDate getLocalDateObject(String date) {
		date = date.trim();
		int[] dateMonthYear = {1,1,1}; //{dayOfMonth, month, year}
		for(int index = 0, counter = 0, beginIndex = 0; index < date.length(); index++) {
			if(!Character.isDigit(date.charAt(index))) {
				dateMonthYear[counter++] = Integer.parseInt(date.substring(beginIndex, index));
				beginIndex = index + 1;
			}
			if(counter == 2) {
				dateMonthYear[counter] = Integer.parseInt(date.substring(beginIndex));
			}
		}
		return LocalDate.of(dateMonthYear[2], dateMonthYear[1], dateMonthYear[0]);
	}
}
