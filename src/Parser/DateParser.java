package Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class DateParser {

	// Instance Variables
	private String taskDetails;
	private ArrayList<LocalDate> dateList = new ArrayList<LocalDate>();

	/****************** CONSTRUCTORS **********************/
	DateParser() {
		this(ParserConstants.STRING_EMPTY, new ArrayList<LocalDate>());
	}

	DateParser(String newTaskDetails) {
		this(newTaskDetails, new ArrayList<LocalDate>());
	}

	DateParser(String newTaskDetails, ArrayList<LocalDate> newDateList) {
		setTaskDetails(newTaskDetails);
		setDateList(newDateList);
	}

	/****************** SETTER METHODS ***********************/
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = CommandParser.cleanupExtraWhitespace(newTaskDetails);
	}

	protected void setDateList(ArrayList<LocalDate> dateList) {
		this.dateList = dateList;
	}

	/****************** GETTER METHODS ***********************/
	public String getTaskDetails() {
		return this.taskDetails;
	}

	ArrayList<LocalDate> getDateList() {
		if (dateList.isEmpty())
			return null;
		return this.dateList;
	}

	/****************** OTHER METHODS ***********************/

	/**
	 * This method is used to extract all the dates from taskDetails
	 */
	public void findDates() {
		String taskDetails = getTaskDetails();
		Pattern datePattern = Pattern.compile(ParserConstants.REGEX_POSSIBLE_DATE);
		Matcher dateMatcher = datePattern.matcher(taskDetails);
		while (dateMatcher.find()) {
			String tempString = taskDetails.substring(dateMatcher.start());
			ParsePosition parsePos = new ParsePosition(ParserConstants.FIRST_INDEX);
			if (isValidDate(tempString, parsePos)) {
				removeDateFromTaskDetails(tempString, parsePos);
			}
		}
	}

	public void removeDateFromTaskDetails(String statement, ParsePosition parsePos) {
		String validDate = statement.substring(ParserConstants.FIRST_INDEX, parsePos.getIndex());
		taskDetails = CommandParser
				.cleanupExtraWhitespace(taskDetails.replace(validDate, ParserConstants.STRING_WHITESPACE));
	}

	/**
	 * This method checks if the immediate String contains a date If it is a
	 * Valid Date, it adds it to the List
	 */
	public boolean isValidDate(String statement, ParsePosition parsePos) {
		statement = CommandParser.cleanupExtraWhitespace(statement);
		for (DateTimeFormatter format : generateDateFormatList()) {
			DateTimeFormatter myFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().append(format)
					.toFormatter(Locale.ENGLISH);
			try {
				ParsePosition index = new ParsePosition(ParserConstants.FIRST_INDEX);
				LocalDate parsedDate = LocalDate.from(myFormatter.parse(statement.trim(), index));
				parsePos.setIndex(index.getIndex());
				addValidDateToList(parsedDate);
				return true;
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

	public void addValidDateToList(LocalDate parsedDate) {
		dateList.add(parsedDate);
	}

	public ArrayList<LocalDate> sortDateList(ArrayList<LocalDate> dates) {
		Collections.sort(dates);
		return dates;
	}

	/**
	 * This method returns an array list of possible date formats
	 * 
	 * @return dateFormatList, contains all acceptable date formats in
	 *         DateTimeFormatter type ArrayList
	 * 
	 */
	ArrayList<DateTimeFormatter> generateDateFormatList() {
		ArrayList<DateTimeFormatter> dateFormatList = new ArrayList<DateTimeFormatter>();
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HASH_DAY_MONTH_NUM));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HYPHEN_DAY_MONTH_NUM));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT));
		return dateFormatList;
	}
}
