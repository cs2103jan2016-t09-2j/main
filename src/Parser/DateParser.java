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

	private static final String REGEX_DATE = "\\d{1,2}(-|/)\\d{1,2}(-|/)\\d{2,4}";

	// private static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";

	// Instance Variables

	DateParser() {
		this("");
	}

	DateParser(String taskDetails) {

	}

	// incomplete
	protected ArrayList<LocalDate> getDates(String taskDetails) {
		ArrayList<String> stringDateList = getDateList(taskDetails);
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
		return dateList;
	}
	
	/**
	 * This method returns true if the input string has any dates,
	 * otherwise false.
	 * If no time and date means it is a floating task.
	 * 
	 * @param stringDateList
	 * 				is the list of dates contained in the user statement.
	 */
	protected boolean hasDateList(ArrayList<String> stringDateList) {
		if(stringDateList.isEmpty()) {
			return true;
		}
		return false;
	}

	private ArrayList<LocalDate> getLocalDateList(ArrayList<String> stringDateList) {
		ArrayList<LocalDate> localDateList = new ArrayList<LocalDate>();
		for (String date : stringDateList) {
			localDateList.add(LocalDate.of(getYear(date), getMonth(date), getDayOfMonth(date)));
		}
		return localDateList;
	}

	private int getYear(String date) {
		// TODO Auto-generated method stub
		return 0;
	}

	private int getMonth(String date) {
		// TODO Auto-generated method stub
		return 1;
	}

	private int getDayOfMonth(String date) {
		// TODO Auto-generated method stub
		return 0;
	}
}
