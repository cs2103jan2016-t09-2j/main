package Parser;

import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.time.LocalDate;
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

	//private static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";

	// Instance Variables

	DateParser() {
		this("");
	}

	DateParser(String taskDetails) {

	}
	
	//incomplete
	protected ArrayList<LocalDate> getDates(String taskDetails) {
		ArrayList<String> stringDateList = getDateList(taskDetails);
		ArrayList<LocalDate> dateList = getRealDateList(stringDateList);
		return dateList;
	}

	private ArrayList<String> getDateList(String taskDetails) {
		ArrayList<String> dateList = new ArrayList<String>();
		Pattern datePattern = Pattern.compile(REGEX_DATE);
		Matcher dateMatcher = datePattern.matcher(taskDetails);
		while (dateMatcher.find()) {
			dateList.add(dateMatcher.group());
		}
		return dateList;
	}
	
	private ArrayList<LocalDate> getRealDateList(ArrayList<String> stringDateList) {
		return null;
	}
}
