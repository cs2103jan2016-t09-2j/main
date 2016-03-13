package Parser;

import java.util.ArrayList;
import java.util.Locale;
import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import Parser.ParserConstants;

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

	// Instance Variables
	private String taskDetails;
	private ArrayList<LocalTime> timeList;
	private ArrayList<LocalDate> dateList;

	/**************** CONSTRUCTORS *********************/
	// Default Constructor
	TimeParser() {
		this(ParserConstants.STRING_EMPTY, new ArrayList<LocalTime>(), new ArrayList<LocalDate>());
	}

	// Parameterized Constructor
	TimeParser(String newTaskDetails) {
		this(newTaskDetails, new ArrayList<LocalTime>(), new ArrayList<LocalDate>());
	}

	TimeParser(String newTaskDetails, ArrayList<LocalTime> newTimeList, ArrayList<LocalDate> newDateList) {
		setTaskDetails(newTaskDetails);
		setTimeList(newTimeList);
		setDateList(newDateList);
	}

	/****************** SETTER METHODS ***********************/
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = newTaskDetails;
	}

	protected void setTimeList(ArrayList<LocalTime> timeList) {
		this.timeList = timeList;
	}

	protected void setDateList(ArrayList<LocalDate> dateList) {
		this.dateList = dateList;
	}

	/******************* GETTER METHODS ***********************/
	public String getTaskDetails() {
		return this.taskDetails;
	}

	public ArrayList<LocalTime> getTimeList() {
		if (timeList.isEmpty()) {
			return null;
		}
		return this.timeList;
	}

	public ArrayList<LocalDate> getDateList() {
		if (dateList.isEmpty()) {
			return null;
		}
		return this.dateList;
	}

	/**************** OTHER METHODS ***********************/

	/**
	 * This method is used to extract all the times from taskDetails
	 */
	public void findTimes() {
		String taskDetails = getTaskDetails();
		Pattern timePattern = Pattern.compile(ParserConstants.REGEX_POSSIBLE_TIME);
		Matcher timeMatcher = timePattern.matcher(taskDetails);
		while (timeMatcher.find()) {
			String tempString = CommandParser.cleanupExtraWhitespace(taskDetails.substring(timeMatcher.start()));
			addToListIfValidTime(tempString);
		}
	}

	/**
	 * This method checks if the immediate String contains a time. If it is a
	 * Valid Time, it adds it to the timeList.
	 */
	public boolean addToListIfValidTime(String statement) {
		String end = ParserConstants.STRING_EMPTY;
		for (DateTimeFormatter format : generateTimeFormatList()) {
			DateTimeFormatter myFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().append(format)
					.toFormatter(Locale.ENGLISH);
			try {
				ParsePosition index = new ParsePosition(ParserConstants.FIRST_INDEX);
				LocalTime parsedTime = LocalTime.from(myFormatter.parse(statement.trim(), index));

				if (index.getIndex() < statement.length()) {
					end = statement.substring(index.getIndex());
				}
				statement = statement.substring(ParserConstants.FIRST_INDEX, index.getIndex());

				if (taskDetails.contains(statement) && isValidEnd(end)) {
					addValidTimeToList(parsedTime);
					removeTimeFromTaskDetails(statement);
					return true;
				}
			} catch (IndexOutOfBoundsException e) {
				// do nothing
			} catch (DateTimeParseException e) {
				// do nothing
			} catch (DateTimeException e) {
				// do nothing
			}
		}
		return false;
	}

	public void removeTimeFromTaskDetails(String textToRemove) {
		taskDetails = CommandParser
				.cleanupExtraWhitespace(taskDetails.replace(textToRemove, ParserConstants.STRING_WHITESPACE));
	}

	private boolean isValidEnd(String endText) {
		if (endText.isEmpty()) {
			return true;
		}
		String firstCharacter = endText.charAt(ParserConstants.FIRST_INDEX) + ParserConstants.STRING_EMPTY;
		return hasInDictionary(ParserConstants.VALID_END, firstCharacter);
	}

	public void addValidTimeToList(LocalTime parsedTime) {
		timeList.add(parsedTime);
	}

	private boolean hasInDictionary(String[] dictionary, String wordToFind) {
		if (wordToFind != null && !wordToFind.isEmpty()) {
			for (String dictionaryWords : dictionary) {
				if (dictionaryWords.equalsIgnoreCase(wordToFind))
					return true;
			}
		}
		return false;
	}

	/**
	 * This method returns an array list of possible time formats
	 * 
	 * @return dateFormatList, contains all acceptable time formats in
	 *         DateTimeFormatter type ArrayList
	 * 
	 */
	public static ArrayList<DateTimeFormatter> generateTimeFormatList() {
		ArrayList<DateTimeFormatter> dateFormatList = new ArrayList<DateTimeFormatter>();
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_MIN_AMPM_WITHOUT_SPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_MIN_AMPM_WITH_SPACE));
		dateFormatList.add(
				DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_MIN_AMPM_WITH_PERIOD_WITHOUT_SPACE));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_MIN_AMPM_WITH_PERIOD_WITH_SPACE));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_MIN_AMPM_WITH_COLOM_WITHOUT_SPACE));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_MIN_AMPM_WITH_COLON_WITH_SPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_AMPM_WITHOUT_SPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_12HOUR_AMPM_WITH_SPACE));

		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR_COLON));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR_PERIOD));
		return dateFormatList;
	}
}
