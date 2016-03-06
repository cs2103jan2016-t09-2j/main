package Parser;

import java.util.ArrayList;
import java.util.Collections;

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

	// Instance Variables
	private String taskDetails;

	/**************** CONSTRUCTORS *********************/
	DateParser() {
		this(ParserConstants.STRING_EMPTY);
	}

	DateParser(String newTaskDetails) {
		setTaskDetails(newTaskDetails);
	}

	/****************** SETTER METHOD ***********************/
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = newTaskDetails;
	}

	/****************** GETTER METHOD ***********************/
	public String getTaskDetails() {
		return this.taskDetails;
	}

	/****************** OTHER METHODS ***********************/
	protected ArrayList<LocalDate> getDates() {
		ArrayList<String> stringDateList = getDateList(getTaskDetails());
		if (hasDateList(stringDateList)) {
			ArrayList<LocalDate> dateList = getLocalDateList(stringDateList);
			// dateList = sortDateList(dateList);
			return dateList;
		}
		return null;
	}

	ArrayList<String> getDateList(String taskDetails) {
		ArrayList<String> dateList = new ArrayList<String>();
		Pattern datePattern = Pattern.compile(ParserConstants.REGEX_DATE);
		Matcher dateMatcher = datePattern.matcher(taskDetails);
		while (dateMatcher.find()) {
			dateList.add(dateMatcher.group());
		}
		removeDatesFromTaskDetails();
		return dateList;
	}

	protected void removeDatesFromTaskDetails() {
		this.taskDetails = taskDetails.replaceAll(ParserConstants.REGEX_DATE, ParserConstants.STRING_WHITESPACE);
		this.taskDetails = CommandParser.cleanupExtraWhitespace(taskDetails);
	}

	/**
	 * This method returns true if the input string has any dates, otherwise
	 * false. If no time and date means it is a floating task.
	 * 
	 * @param stringDateList
	 *            is the list of dates contained in the user statement.
	 */
	private boolean hasDateList(ArrayList<String> stringDateList) {
		if (stringDateList.isEmpty()) {
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
		int[] dateMonthYear = { 1, 1, 1 }; // {dayOfMonth, month, year}
		for (int index = ParserConstants.FIRST_INDEX, counter = 0, beginIndex = ParserConstants.FIRST_INDEX; index < date
				.length(); index++) {
			if (!Character.isDigit(date.charAt(index))) {
				dateMonthYear[counter++] = Integer.parseInt(date.substring(beginIndex, index));
				beginIndex = index + 1;
			}
			if (counter == ParserConstants.INDEX_YEAR) {
				dateMonthYear[counter] = Integer.parseInt(date.substring(beginIndex));
			}
		}
		int dayOfMonth = dateMonthYear[ParserConstants.INDEX_DAY_OF_MONTH];
		int month = dateMonthYear[ParserConstants.INDEX_MONTH];
		int year = dateMonthYear[ParserConstants.INDEX_YEAR];
		year = getValidYear(year, month, dayOfMonth);
		return LocalDate.of(year, month, dayOfMonth);
	}

	public ArrayList<LocalDate> sortDateList(ArrayList<LocalDate> dates) {
		Collections.sort(dates);
		return dates;
	}

	public static int getValidYear(int year, int month, int dayOfMonth) {
		if ((year / ParserConstants.CENTURY) == 0) {
			LocalDate todayDate = LocalDate.now();
			int thisYear = todayDate.getYear();
			year += (thisYear / ParserConstants.CENTURY) * ParserConstants.CENTURY;
			LocalDate newDate = LocalDate.of(year, month, dayOfMonth);
			if (newDate.isBefore(todayDate)) {
				year = year + ParserConstants.CENTURY;
			}
		}
		return year;
	}
}
