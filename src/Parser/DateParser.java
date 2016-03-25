package Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class DateParser {

	// Instance Variables
	private String taskDetails;
	private ArrayList<LocalDate> dateList = new ArrayList<LocalDate>();

	// private String tempProspectiveDate = ParserConstants.STRING_EMPTY;

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
		return cleanupExtraWhitespace(this.taskDetails);
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

			if (!addToListIfValidDate(tempString)) {

				String firstWord = cleanupExtraWhitespace(getFirstXWords(tempString, 1));
				if (isDayOfWeek(firstWord)) {
					dateList.add(getDayOfWeekDate(firstWord));
					removeDateFromTaskDetails(firstWord);
				} else if (isUpcomingDayString(tempString)) {
					String upcomingDay = getUpComingDayWord(tempString);
					if (this.taskDetails.contains(upcomingDay)) {
						dateList.add(getUpcomingDayDate(upcomingDay));
						removeDateFromTaskDetails(upcomingDay);
					}
				} else if (hasDayDuration(tempString)) {
					dateList.add(getParsedDayDurationDate(tempString));
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
		String tempStatement = statement;
		String end = ParserConstants.STRING_EMPTY;
		statement = cleanupExtraWhitespace(statement);
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
		if (addToListIfValidDateWithoutYear(tempStatement)) {
			return true;
		}
		return false;
	}

	public boolean addToListIfValidDateWithoutYear(String statement) {
		String end = ParserConstants.STRING_EMPTY;
		statement = cleanupExtraWhitespace(statement);
		for (DateTimeFormatter format : generateDateFormatListWithoutYear()) {
			DateTimeFormatter myFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive().append(format)
					.toFormatter(Locale.ENGLISH);
			try {
				ParsePosition index = new ParsePosition(ParserConstants.FIRST_INDEX);
				MonthDay monthDay = MonthDay.from(myFormatter.parse(statement.trim(), index));
				LocalDate parsedDate = monthDay.atYear(getCurrentYear());
				if (parsedDate.isBefore(getTodayDate())) {
					parsedDate = parsedDate.plusYears(ParserConstants.ONE_YEAR);
				}
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
			String firstWord = getFirstXWords(textToFind, ParserConstants.ONE_WORD);
			String firstThreeWords = getFirstXWords(textToFind, ParserConstants.THREE_WORDS);
			return hasInDictionary(ParserConstants.UPCOMING_DAYS, firstWord)
					|| hasInDictionary(ParserConstants.UPCOMING_DAYS, firstThreeWords);
		}
		return false;
	}

	public String getUpComingDayWord(String textToFind) {
		String firstWord = getFirstXWords(textToFind, ParserConstants.ONE_WORD);
		String firstThreeWords = getFirstXWords(textToFind, ParserConstants.THREE_WORDS);

		if (hasInDictionary(ParserConstants.UPCOMING_DAYS, firstWord)) {
			return firstWord;
		} else if (hasInDictionary(ParserConstants.UPCOMING_DAYS, firstThreeWords)) {
			return firstThreeWords;
		}

		return null;
	}

	public LocalDate getParsedDayDurationDate(String inputString) {
		try {
			String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
			String secondWord = "";
			if (isFirstWordDayDuration(inputString)) {
				int splitPos = -1;
				for (int index = ParserConstants.FIRST_INDEX; index < firstWord.length(); index++) {
					if (!Character.isDigit(firstWord.charAt(index))) {
						splitPos = index;
						break;
					}
				}
				removeDateFromTaskDetails(firstWord);
				secondWord = firstWord.substring(splitPos);
				firstWord = firstWord.substring(ParserConstants.FIRST_INDEX, splitPos);
			} else {
				String first2Words = getFirstXWords(inputString, ParserConstants.TWO_WORDS);
				secondWord = first2Words.replace(firstWord, ParserConstants.STRING_WHITESPACE).trim();
				removeDateFromTaskDetails(first2Words);
			}
			LocalDate parsedDate = getDayDurationDate(firstWord, secondWord);
			return parsedDate;
		} catch (Exception e) {
			// no exception encountered, just a precaution
		}
		return null;
	}

	public LocalDate getDayDurationDate(String firstWord, String secondWord) {
		LocalDate parsedDate = getTodayDate();
		int unitsToAdd = Integer.parseInt(firstWord);
		int indexOfKeyword = indexOf(secondWord, ParserConstants.DAY_DURATION);

		if (indexOfKeyword <= ParserConstants.LAST_INDEX_OF_DAY) {
			parsedDate = parsedDate.plusDays(unitsToAdd);
		} else if (indexOfKeyword <= ParserConstants.LAST_INDEX_OF_WEEK) {
			parsedDate = parsedDate.plusWeeks(unitsToAdd);
		} else if (indexOfKeyword <= ParserConstants.LAST_INDEX_OF_MONTH) {
			parsedDate = parsedDate.plusMonths(unitsToAdd);
		} else {
			parsedDate = parsedDate.plusYears(unitsToAdd);
		}
		return parsedDate;
	}

	public boolean hasDayDuration(String tempString) {
		try {
			return isFirstWordDayDuration(tempString) || isFirstTwoWordsDayDuration(tempString);
		} catch (NullPointerException e) {
			// do nothing
		} catch (NumberFormatException e) {
			// do nothing
		} catch (IndexOutOfBoundsException e) {
			// do nothing
		}
		return false;
	}

	public boolean isFirstWordDayDuration(String inputString)
			throws NullPointerException, NumberFormatException, IndexOutOfBoundsException {
		String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
		if (firstWord.matches(ParserConstants.REGEX_POSSIBLE_DURATION)) {
			int splitPos = -1;
			for (int index = ParserConstants.FIRST_INDEX; index < firstWord.length(); index++) {
				if (!Character.isDigit(firstWord.charAt(index))) {
					splitPos = index;
					break;
				}
			}
			String secondWord = firstWord.substring(splitPos);
			firstWord = firstWord.substring(ParserConstants.FIRST_INDEX, splitPos);
			if (hasInDictionary(ParserConstants.DAY_DURATION, secondWord)) {
				Integer.parseInt(firstWord);
				return true;
			}
		}
		return false;
	}

	public boolean isFirstTwoWordsDayDuration(String inputString) throws NullPointerException, NumberFormatException {
		String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
		String first2Words = getFirstXWords(inputString, ParserConstants.TWO_WORDS);
		String secondWord = first2Words.replace(firstWord, ParserConstants.STRING_WHITESPACE).trim();

		if (hasInDictionary(ParserConstants.DAY_DURATION, secondWord)) {
			Integer.parseInt(firstWord);
			return true;
		}
		return false;
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
			return ParserConstants.DEFAULT_INDEX_NUMBER;
		}
	}

	public int getCurrentYear() {
		LocalDate today = LocalDate.now();
		return today.getYear();
	}

	public LocalDate getTodayDate() {
		return LocalDate.now();
	}

	public int indexOf(String word, String[] array) {
		if (hasInDictionary(array, word)) {
			for (int index = ParserConstants.FIRST_INDEX; index < array.length; index++) {
				if (word.equalsIgnoreCase(array[index])) {
					return index;
				}
			}
		}
		return ParserConstants.DEFAULT_INDEX_NUMBER; // if absent
	}

	public void addDateToList(LocalDate parsedDate) {
		dateList.add(parsedDate);
	}

	public ArrayList<LocalDate> sortDateList(ArrayList<LocalDate> dates) {
		Collections.sort(dates);
		return dates;
	}

	private boolean isValidEnd(String endText) {
		if (endText.isEmpty()) {
			return true;
		}
		String firstCharacter = endText.charAt(ParserConstants.FIRST_INDEX) + ParserConstants.STRING_EMPTY;
		return hasInDictionary(ParserConstants.VALID_END, firstCharacter);
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

	public String getFirstXWords(String wordToSplit, int x) {

		if (wordToSplit != null && !wordToSplit.isEmpty() && x > 0) {
			String[] words = wordToSplit.split(ParserConstants.STRING_WHITESPACE);

			if (words.length >= x) {
				String firstXWords = ParserConstants.STRING_EMPTY;
				for (int index = ParserConstants.FIRST_INDEX; index < x; index++) {
					firstXWords += ParserConstants.STRING_WHITESPACE + words[index];
				}
				return cleanupExtraWhitespace(firstXWords);
			}
		}

		return null;
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
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_SPACE_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_SPACE_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_SPACE_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_SPACE_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_LONG_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_LONG_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_SHORT_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_SHORT_YEAR_SHORT));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_LONG_SPACE_YEAR_LONG));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_LONG_SPACE_YEAR_SHORT));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_SHORT_SPACE_YEAR_LONG));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_SPACE_MONTH_SHORT_SPACE_YEAR_SHORT));

		return dateFormatList;
	}

	ArrayList<DateTimeFormatter> generateDateFormatListWithoutYear() {
		ArrayList<DateTimeFormatter> dateFormatList = new ArrayList<DateTimeFormatter>();
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT));
		return dateFormatList;
	}
}
