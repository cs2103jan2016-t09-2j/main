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
		if (dateList.isEmpty()) {
			return null;
		}
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
			String tempString = cleanupExtraWhitespace(taskDetails.substring(dateMatcher.start()));

			addToListIfValidDate(tempString);

			String firstWord = cleanupExtraWhitespace(getFirstWord(tempString));

			if (isDayOfWeek(firstWord)) {
				dateList.add(getDayOfWeekDate(firstWord));
				removeDateFromTaskDetails(firstWord);
			} else if (isUpcomingDayString(tempString)) {
				String upcomingDay = getUpComingDayWord(tempString);
				if (this.taskDetails.contains(upcomingDay)) {
					dateList.add(getUpcomingDayDate(upcomingDay));
					removeDateFromTaskDetails(upcomingDay);
				}
			}
		}
	}

	public void removeDateFromTaskDetails(String statement, ParsePosition parsePos) {
		String validDate = statement.substring(ParserConstants.FIRST_INDEX, parsePos.getIndex());
		taskDetails = cleanupExtraWhitespace(taskDetails.replace(validDate, ParserConstants.STRING_WHITESPACE));
	}

	public void removeDateFromTaskDetails(String textToRemove) {
		this.taskDetails = cleanupExtraWhitespace(taskDetails.replace(textToRemove, ParserConstants.STRING_WHITESPACE));
	}

	/**
	 * This method checks if the immediate String contains a date If it is a
	 * Valid Date, it adds it to the List
	 */
	public boolean addToListIfValidDate(String statement) {
		String end = ParserConstants.STRING_EMPTY;
		statement = CommandParser.cleanupExtraWhitespace(statement);
		for (DateTimeFormatter format : generateDateFormatList()) {
			DateTimeFormatter myFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().append(format)
					.toFormatter(Locale.ENGLISH);
			try {
				ParsePosition index = new ParsePosition(ParserConstants.FIRST_INDEX);
				LocalDate parsedDate = LocalDate.from(myFormatter.parse(statement.trim(), index));
				if (index.getIndex() < statement.length()) {
					end = statement.substring(index.getIndex());
				}

				statement = statement.substring(ParserConstants.FIRST_INDEX, index.getIndex());

				if (taskDetails.contains(statement) && isValidEnd(end)) {
					addDateToList(parsedDate);
					removeDateFromTaskDetails(statement);
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

	/*
	 * public boolean isAcceptableDate(String statement, ParsePosition pos) {
	 * String firstWord =
	 * CommandParser.cleanupExtraWhitespace(getFirstWord(statement)); return
	 * isValidDate(statement, pos) || isUpcomingDayWord(firstWord) ||
	 * isDayOfWeek(firstWord); }
	 */

	public LocalDate getDayOfWeekDate(String dayOfWeek) {
		LocalDate newDate = null;
		if (isDayOfWeek(dayOfWeek)) {
			newDate = LocalDate.now();
			int dayOfWeekNumToday = newDate.getDayOfWeek().getValue();
			int dayOfWeekValue = indexOf(dayOfWeek, ParserConstants.DAYS_OF_WEEK_LONG);
			if (dayOfWeekValue < 0) {
				dayOfWeekValue = indexOf(dayOfWeek, ParserConstants.DAYS_OF_WEEK_SHORT);
			}
			if (dayOfWeekValue < 0) {
				dayOfWeekValue = indexOf(dayOfWeek, ParserConstants.DAYS_OF_WEEK_MEDIUM);
			}
			int daysToAdd = dayOfWeekValue - dayOfWeekNumToday;
			if (daysToAdd <= 0) {
				daysToAdd += 7;
			}
			newDate = newDate.plusDays(daysToAdd);
		}
		return newDate;
	}

	public boolean isDayOfWeek(String expectedDayOfWeek) {
		return isDayOfWeekLong(expectedDayOfWeek) || isDayOfWeekShort(expectedDayOfWeek)
				|| isDayOfWeekMedium(expectedDayOfWeek);
	}

	/**
	 * This method checks if statement contains a day of the week which is
	 * written in full form
	 * 
	 * @param statement
	 * @return true if is either of {"monday", "tuesday", "wednesday",
	 *         "thursday", "friday", "saturday", "sunday"}; false, otherwise
	 */
	public boolean isDayOfWeekLong(String expectedDayOfWeek) {
		return hasInDictionary(ParserConstants.DAYS_OF_WEEK_LONG, expectedDayOfWeek);
	}

	public boolean isDayOfWeekMedium(String expectedDayOfWeek) {
		return hasInDictionary(ParserConstants.DAYS_OF_WEEK_MEDIUM, expectedDayOfWeek);
	}

	/**
	 * This method checks if statement contains a day of the week written in
	 * short
	 * 
	 * @param statement
	 * @return true if is either of { "mon", "tue", "wed", "thu", "fri", "sat",
	 *         "sun" } ;false, otherwise
	 */
	public boolean isDayOfWeekShort(String expectedDayOfWeek) {
		return hasInDictionary(ParserConstants.DAYS_OF_WEEK_SHORT, expectedDayOfWeek);
	}

	/**
	 * This method is used to convert today, tomorrow and overmorrow to their
	 * respective dates
	 * 
	 * @param upcomingDay
	 * @return LocalDate of the upcomingDay relative to today's date
	 */
	public LocalDate getUpcomingDayDate(String upcomingDay) {
		LocalDate todayDate = LocalDate.now();
		int index = indexOf(upcomingDay, ParserConstants.UPCOMING_DAYS);
		if (index >= 0) { /* If upcomingDay is a valid entry */
			index /= 2;
			if (index < 1) {
				return todayDate;
			} else if (index < 3) {
				return todayDate.plusDays(1);
			} else {
				return todayDate.plusDays(2);
			}
		}
		return null;
	}

	public boolean isUpcomingDayString(String textToFind) {

		if (textToFind != null && !textToFind.isEmpty()) {
			String firstWord = getFirstWord(textToFind);
			String firstThreeWords = getFirstThreeWords(textToFind);
			return hasInDictionary(ParserConstants.UPCOMING_DAYS, firstWord)
					|| hasInDictionary(ParserConstants.UPCOMING_DAYS, firstThreeWords);
		}
		return false;
	}

	public String getUpComingDayWord(String textToFind) {
		String firstWord = getFirstWord(textToFind);
		String firstThreeWords = getFirstThreeWords(textToFind);

		if (hasInDictionary(ParserConstants.UPCOMING_DAYS, firstWord)) {
			return firstWord;
		} else if (hasInDictionary(ParserConstants.UPCOMING_DAYS, firstThreeWords)) {
			return firstThreeWords;
		}

		return null;
	}

	public boolean isMonth(String firstWord) {
		return hasInDictionary(ParserConstants.MONTHS_LONG, firstWord)
				|| hasInDictionary(ParserConstants.MONTHS_SHORT, firstWord);
	}

	public int getMonthNum(String month) {
		if (hasInDictionary(ParserConstants.MONTHS_LONG, month)) {
			return indexOf(month, ParserConstants.MONTHS_LONG);
		} else if (hasInDictionary(ParserConstants.MONTHS_SHORT, month)) {
			return indexOf(month, ParserConstants.MONTHS_SHORT);
		} else {
			return -1;
		}
	}

	public int indexOf(String word, String[] array) {
		if (hasInDictionary(array, word)) {
			for (int index = ParserConstants.FIRST_INDEX; index < array.length; index++) {
				if (word.equalsIgnoreCase(array[index])) {
					return index;
				}
			}
		}
		return -1; // if absent
	}

	private boolean isValidEnd(String endText) {
		if (endText.isEmpty()) {
			return true;
		}
		String firstCharacter = endText.charAt(ParserConstants.FIRST_INDEX) + ParserConstants.STRING_EMPTY;
		return hasInDictionary(ParserConstants.VALID_END, firstCharacter);
	}

	public void addDateToList(LocalDate parsedDate) {
		dateList.add(parsedDate);
	}

	public ArrayList<LocalDate> sortDateList(ArrayList<LocalDate> dates) {
		Collections.sort(dates);
		return dates;
	}

	private boolean hasInDictionary(String[] dictionary, String wordsToFind) {
		if (wordsToFind != null && !wordsToFind.isEmpty()) {
			for (String dictionaryWords : dictionary) {
				if (dictionaryWords.equalsIgnoreCase(wordsToFind))
					return true;
			}
		}
		return false;
	}

	String getFirstThreeWords(String inputString) {
		inputString = cleanupExtraWhitespace(inputString) + ParserConstants.STRING_WHITESPACE;

		int splitPosition = getThirdWhiteSpacePos(inputString);

		if (splitPosition == ParserConstants.NO_WHITE_SPACE) {
			return ParserConstants.STRING_EMPTY;
		}
		return inputString.substring(ParserConstants.FIRST_INDEX, splitPosition);
	}

	public int getThirdWhiteSpacePos(String inputString) {
		int numberOfWhiteSpaces = ParserConstants.FIRST_INDEX;

		for (int index = ParserConstants.FIRST_INDEX; index < inputString.length(); index++) {
			if (inputString.charAt(index) == ParserConstants.WHITE_SPACE) {
				numberOfWhiteSpaces++;
			}
			if (numberOfWhiteSpaces == 3) {
				return index;
			}
		}

		return ParserConstants.NO_WHITE_SPACE;
	}

	String getFirstWord(String inputString) {
		inputString = inputString.trim();

		int splitPosition = inputString.indexOf(ParserConstants.WHITE_SPACE);
		if (splitPosition == ParserConstants.NO_WHITE_SPACE) {
			if (inputString.length() > 0) {
				splitPosition = inputString.length();
			} else {
				return ParserConstants.STRING_EMPTY;
			}
		}
		return inputString.substring(ParserConstants.FIRST_INDEX, splitPosition);
	}

	/**
	 * This method removes the unnecessary white spaces present in the string.
	 * 
	 * @param someText
	 *            is any string with several white spaces.
	 * @return someText excluding the extra unnecessary white spaces.
	 */
	public String cleanupExtraWhitespace(String someText) {
		Pattern extraSpace = Pattern.compile(ParserConstants.REGEX_EXTRA_WHITESPACE);
		Matcher regexMatcher = extraSpace.matcher(someText.trim());
		String cleanText = regexMatcher.replaceAll(ParserConstants.STRING_WHITESPACE);
		return cleanText;
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
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_LONG_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_SHORT_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_LONG_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_SHORT_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT));
		return dateFormatList;
	}
}
