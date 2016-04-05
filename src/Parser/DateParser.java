//@@author A0132778W

package Parser;

import java.util.ArrayList;
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

/*
 * DateParser is used to detect all possible dates in a String, 
 * add it to the dateList, 
 * and remove it from the taskDetails.
 * 
 * Thus aiding in parsing the given String input.
 */
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
		String previousWord = ParserConstants.STRING_EMPTY;
		int startPrevWord = ParserConstants.FIRST_INDEX;

		Pattern datePattern = Pattern.compile(ParserConstants.REGEX_POSSIBLE_DATE);
		Matcher dateMatcher = datePattern.matcher(taskDetails);

		while (dateMatcher.find()) {
			try {
				previousWord = taskDetails.substring(startPrevWord, dateMatcher.start());
				String tempString = cleanupExtraWhitespace(taskDetails.substring(dateMatcher.start()));
				if (!addToListIfValidDate(tempString, previousWord)) {
					String firstWord = cleanupExtraWhitespace(getFirstXWords(tempString, 1));
					if (isDayOfWeek(firstWord)) {
						firstWord = getStartString(firstWord);
						if (taskDetailsContains(firstWord)) {
							dateList.add(getDayOfWeekDate(firstWord));
							if (isValidKeyWord(previousWord) || isValidRangeKeyWord(previousWord)) {
								removeDateFromTaskDetails(previousWord + ParserConstants.STRING_WHITESPACE + firstWord);
							} else {
								removeDateFromTaskDetails(firstWord);
							}
						}
					} else if (isUpcomingDayString(tempString)) {
						String upcomingDay = getUpComingDayWord(tempString);
						if (taskDetailsContains(upcomingDay)) {
							dateList.add(getUpcomingDayDate(upcomingDay));
							if (isValidKeyWord(previousWord) || isValidRangeKeyWord(previousWord)) {
								removeDateFromTaskDetails(
										previousWord + ParserConstants.STRING_WHITESPACE + upcomingDay);
							} else {
								removeDateFromTaskDetails(upcomingDay);
							}
						}
					} else if (hasDayDuration(tempString)) {
						String dayDuration = getDayDurationWord(tempString);
						if (taskDetailsContains(dayDuration)) {
							dateList.add(getParsedDayDurationDate(tempString));
							if (isValidKeyWord(previousWord) || isValidRangeKeyWord(previousWord)) {
								removeDateFromTaskDetails(
										previousWord + ParserConstants.STRING_WHITESPACE + dayDuration);
							} else {
								removeDateFromTaskDetails(dayDuration);
							}
						}
					}
				}
				startPrevWord = dateMatcher.start();
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	/**
	 * This method removes the particular textToRemove from taskDetails.
	 * 
	 * @param textToRemove
	 */
	public void removeDateFromTaskDetails(String textToRemove) {
		textToRemove = cleanupExtraWhitespace(textToRemove);
		if (taskDetails.startsWith(textToRemove)) {
			taskDetails = CommandParser
					.cleanupExtraWhitespace(taskDetails.replaceFirst(textToRemove, ParserConstants.STRING_WHITESPACE));
		} else {
			taskDetails = CommandParser.cleanupExtraWhitespace(taskDetails
					.replace(ParserConstants.STRING_WHITESPACE + textToRemove, ParserConstants.STRING_WHITESPACE));
		}
	}

	/**
	 * This method checks if the immediate String contains a date. If it is a
	 * Valid Date, it adds it to the List.
	 * 
	 * @param statement
	 * @param keyword
	 * @return true, if the immediate String is a valid date; otherwise false.
	 */
	public boolean addToListIfValidDate(String statement, String keyword) {
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

				if (taskDetailsContains(statement) && isValidEnd(end)) {
					addDateToList(parsedDate);
					if (isValidKeyWord(keyword) || isValidRangeKeyWord(keyword)) {
						removeDateFromTaskDetails(keyword + ParserConstants.STRING_WHITESPACE + statement);
					} else {
						removeDateFromTaskDetails(statement);
					}
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
		if (addToListIfValidDateWithoutYear(tempStatement, keyword)) {
			return true;
		}
		return false;
	}

	/**
	 * This method is used to check if statement starts with a possible date
	 * format, without a year. That is, only month and day are specified.
	 * 
	 * @param statement
	 *            to check if starts with a possible date
	 * @param keyword
	 *            to check if it is a date keyword, and remove it in the case.
	 * @return true, if statement starts with a word, otherwise false.
	 */
	public boolean addToListIfValidDateWithoutYear(String statement, String keyword) {
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

				// if the date located is contained in taskDetails(to ensure
				// that random dates are not detected)
				// and if the date string detected ends with either a
				// fullstop(.) or a comma (,) or a space or marks the end of the
				// sentence.
				if (taskDetailsContains(statement) && isValidEnd(end)) {
					addDateToList(parsedDate);
					if (isValidKeyWord(keyword) || isValidRangeKeyWord(keyword)) {
						removeDateFromTaskDetails(keyword + ParserConstants.STRING_WHITESPACE + statement);
					} else {
						removeDateFromTaskDetails(statement);
					}
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

	/**
	 * This method is used to determine the date of the upcoming day of the
	 * week.
	 * 
	 * @param dayOfWeek
	 *            of which date is supposed to be determined.
	 * @return date of the given dayOfWeek.
	 */
	public LocalDate getDayOfWeekDate(String dayOfWeek) {

		dayOfWeek = getStartString(dayOfWeek);
		assert isDayOfWeek(dayOfWeek);

		LocalDate newDate = getTodayDate();

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
			daysToAdd += ParserConstants.DAYS_IN_WEEK;
		}
		newDate = newDate.plusDays(daysToAdd);

		return newDate;
	}

	public boolean isDayOfWeek(String expectedDayOfWeek) {
		expectedDayOfWeek = getStartString(expectedDayOfWeek);

		return (isDayOfWeekLong(expectedDayOfWeek) || isDayOfWeekShort(expectedDayOfWeek)
				|| isDayOfWeekMedium(expectedDayOfWeek)) && isValidEnd(getEnd(expectedDayOfWeek));
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
		LocalDate todayDate = getTodayDate();
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

	/**
	 * Today, tomorrow, day after tomorrow and the like account for upcoming
	 * days. This method checks if textToFind starts with an upcoming day word.
	 * 
	 * @param textToFind
	 * @return true, if textToFind starts with an upcoming day word; false
	 *         otherwise.
	 */
	public boolean isUpcomingDayString(String textToFind) {
		if (textToFind != null && !textToFind.isEmpty()) {
			String firstWord = getFirstXWords(textToFind, ParserConstants.ONE_WORD);
			String firstThreeWords = getFirstXWords(textToFind, ParserConstants.THREE_WORDS);

			return (hasInDictionary(ParserConstants.UPCOMING_DAYS, getStartString(firstWord))
					&& isValidEnd(getEnd(firstWord)))
					|| (hasInDictionary(ParserConstants.UPCOMING_DAYS, getStartString(firstThreeWords))
							&& isValidEnd(getEnd(firstThreeWords)));
		}
		return false;
	}

	/**
	 * This method extracts the upcoming day word from the given String
	 * textToFind.
	 * 
	 * @param textToFind
	 * @return the upcoming day word from textToFind.
	 */
	public String getUpComingDayWord(String textToFind) {
		String firstWord = getStartString(getFirstXWords(textToFind, ParserConstants.ONE_WORD));
		String firstThreeWords = getStartString(getFirstXWords(textToFind, ParserConstants.THREE_WORDS));

		if (hasInDictionary(ParserConstants.UPCOMING_DAYS, firstWord)) {
			return firstWord;
		} else if (hasInDictionary(ParserConstants.UPCOMING_DAYS, firstThreeWords)) {
			return firstThreeWords;
		}

		return null;
	}

	public LocalDate getParsedDayDurationDate(String text) {
		try {
			String firstWord = ParserConstants.STRING_EMPTY;
			String secondWord = ParserConstants.STRING_EMPTY;
			if (isFirstWordDayDuration(text)) {
				int splitPos = -1;
				for (int index = ParserConstants.FIRST_INDEX; index < text.length(); index++) {
					if (!Character.isDigit(text.charAt(index))) {
						splitPos = index;
						break;
					}
				}
				secondWord = text.substring(splitPos);
				firstWord = text.substring(ParserConstants.FIRST_INDEX, splitPos);
			} else {
				firstWord = getFirstXWords(text, ParserConstants.ONE_WORD);
				secondWord = text.replace(firstWord, ParserConstants.STRING_WHITESPACE).trim();
			}
			LocalDate parsedDate = getDayDurationDate(firstWord, secondWord);
			return parsedDate;
		} catch (Exception e) {
			// no exception encountered, just a precaution
		}
		return null;
	}

	/**
	 * This method extracts the day duration word from the given textToSearch.
	 * 
	 * @param textToSearch
	 * @return day duration word from textToSearch.
	 */
	public String getDayDurationWord(String textToSearch) {
		String firstWord = getStartString(getFirstXWords(textToSearch, ParserConstants.ONE_WORD));

		if (isFirstWordDayDuration(textToSearch)) {
			return firstWord;
		} else {
			return getStartString(getFirstXWords(textToSearch, ParserConstants.TWO_WORDS));
		}
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

	/**
	 * Day duration refers to a number followed by either days, weeks, months or
	 * years. The digit and the String may or may not have a whitespace in
	 * between.
	 * 
	 * @param tempString
	 * @return true if tempString starts with a day duration word; false
	 *         otherwise.
	 */
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

	/**
	 * This method is used to check if the first word of inputString is a day
	 * duration word.
	 * 
	 * @param inputString
	 * @return true if inputString first word is day duration; false otherwise.
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 * @throws IndexOutOfBoundsException
	 */
	public boolean isFirstWordDayDuration(String inputString)
			throws NullPointerException, NumberFormatException, IndexOutOfBoundsException {
		String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
		String endString = getEnd(firstWord);
		firstWord = getStartString(firstWord);

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
			if (hasInDictionary(ParserConstants.DAY_DURATION, secondWord) && isValidEnd(endString)) {
				Integer.parseInt(firstWord);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to check the first two words of inputString for
	 * occurrence of day duration.
	 * 
	 * @param inputString
	 * @return true if first two words mark day duration; false otherwise.
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	public boolean isFirstTwoWordsDayDuration(String inputString) throws NullPointerException, NumberFormatException {
		String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
		String first2Words = getFirstXWords(inputString, ParserConstants.TWO_WORDS);
		String secondWord = first2Words.replace(firstWord, ParserConstants.STRING_WHITESPACE).trim();
		String endString = getEnd(secondWord);

		if (hasInDictionary(ParserConstants.DAY_DURATION, getStartString(secondWord)) && isValidEnd(endString)) {
			Integer.parseInt(firstWord);
			return true;
		}
		return false;
	}

	public boolean isMonth(String firstWord) {
		firstWord = getStartString(firstWord);
		return hasInDictionary(ParserConstants.MONTHS_LONG, firstWord)
				|| hasInDictionary(ParserConstants.MONTHS_SHORT, firstWord);
	}

	public int getMonthNum(String month) {
		month = getStartString(month);
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

	/**
	 * This method returns the first occurrence of word in array[].
	 * 
	 * @param word
	 * @param array
	 * @return return the first index of word if it is present in array[];
	 *         otherwise -1.
	 */
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

	/**
	 * Dates can have a comprehensive list of keywords from { "by", "on", "in",
	 * "before", "from", "frm" } preceding them. This method checks if "keyword"
	 * contains any of these date keywords.
	 * 
	 * @param keyword
	 * @return true if it is a valid date keyword; false otherwise.
	 */
	public boolean isValidKeyWord(String keyword) {
		return hasInDictionary(ParserConstants.DATE_KEYWORD, keyword.trim());
	}

	/**
	 * "to" and hyphen "-" are the range keywords. This method checks if keyword
	 * is a valid range keyword.
	 * 
	 * @param keyword
	 * @return return true if keyword equals "to" or "-"; false otherwise.
	 */
	public boolean isValidRangeKeyWord(String keyword) {
		if (keyword == null || keyword.isEmpty()) {
			return false;
		} else {
			keyword = keyword.trim();
			return (keyword.equalsIgnoreCase(ParserConstants.STRING_HYPHEN)
					|| keyword.equalsIgnoreCase(ParserConstants.STRING_TO))
					&& (dateList.size() > ParserConstants.MIN_SIZE);
		}
	}

	/**
	 * A valid end is denoted by a String that ends with a period, comma,
	 * whitespace or is the end of the String.
	 * 
	 * @param endText
	 *            to check if its end is valid.
	 * @return true if endText has valid end; false otherwise.
	 */
	private boolean isValidEnd(String endText) {
		if (endText.isEmpty()) {
			return true;
		}
		String firstCharacter = endText.charAt(ParserConstants.FIRST_INDEX) + ParserConstants.STRING_EMPTY;
		return hasInDictionary(ParserConstants.VALID_END, firstCharacter);
	}

	/**
	 * This method is used to find the end of the String text from the first
	 * occurrence of a non-word character.
	 * 
	 * @param text
	 * @return substring of text, starting from first non-word character until
	 *         the end of text. If no non-word character present, returns an
	 *         empty String.
	 */
	private String getEnd(String text) {
		int indexOfNonWord = getIndexOfNonWordChar(text);
		if (indexOfNonWord > ParserConstants.DEFAULT_INDEX_NUMBER) {
			return text.substring(indexOfNonWord);
		}
		return ParserConstants.STRING_EMPTY;
	}

	/**
	 * This method substring text, until the first occurrence of a non-word
	 * character.
	 * 
	 * @param text
	 * @return substring of text from index 0 until the first occurrence of
	 *         non-word character.
	 */
	private String getStartString(String text) {
		int indexOfNonWord = getIndexOfNonWordChar(text);
		if (indexOfNonWord > ParserConstants.DEFAULT_INDEX_NUMBER) {
			return text.substring(ParserConstants.FIRST_INDEX, indexOfNonWord);
		}
		return text;
	}

	/**
	 * This method checks if any index of dictionary[] equals the wordsToFind
	 * (case insensitive).
	 * 
	 * @param dictionary
	 * @param wordsToFind
	 * @return true if dictionary contains wordToFind; false otherwise.
	 */
	private boolean hasInDictionary(String[] dictionary, String wordsToFind) {
		if (wordsToFind != null && !wordsToFind.isEmpty()) {
			for (String dictionaryWords : dictionary) {
				if (dictionaryWords.equalsIgnoreCase(wordsToFind))
					return true;
			}
		}
		return false;
	}

	public boolean taskDetailsContains(String text) {
		Pattern containPattern = Pattern.compile(ParserConstants.REGEX_VALID_START + text);
		Matcher containMatcher = containPattern.matcher(taskDetails);
		if (containMatcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * This method returns the first 'x' words from the given wordToSplit.
	 * 
	 * @param wordToSplit
	 * @param x,
	 *            number of words to return
	 * @return first x words from wordToSplit, if present; otherwise null.
	 */
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
	 * This method detects the index of a non-word character in the String word.
	 * A non-word character includes everything other than an english letter, a
	 * digit and a whitespace.
	 * 
	 * @param word
	 *            is checked for any non-word character.
	 * @return index, of first non-word character in word.
	 */
	public int getIndexOfNonWordChar(String word) {
		int index = ParserConstants.DEFAULT_INDEX_NUMBER;

		if (word != null) {

			for (index = ParserConstants.FIRST_INDEX; index < word.length(); index++) {
				if (!Character.isLetterOrDigit(word.charAt(index)) && !Character.isWhitespace(word.charAt(index))) {
					break;
				}
			}

			if (index == word.length()) {
				return ParserConstants.DEFAULT_INDEX_NUMBER;
			}
		}
		return index;
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
	 * This method returns an array list of possible date formats with year.
	 * 
	 * @return dateFormatList, contains all acceptable date formats in
	 *         DateTimeFormatter type ArrayList.
	 */
	ArrayList<DateTimeFormatter> generateDateFormatList() {
		ArrayList<DateTimeFormatter> dateFormatList = new ArrayList<DateTimeFormatter>();
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_SHORT));
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

		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_LONG_DAY_SPACE_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_LONG_DAY_SPACE_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_LONG_SPACE_DAY_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_LONG_SPACE_DAY_YEAR_SHORT));

		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_SHORT_DAY_SPACE_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_SHORT_DAY_SPACE_YEAR_SHORT));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_SHORT_SPACE_DAY_SPACE_YEAR_LONG));
		dateFormatList
				.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_SHORT_SPACE_DAY_SPACE_YEAR_SHORT));

		return dateFormatList;
	}

	/**
	 * This method returns an array list of possible date formats without the
	 * year.
	 * 
	 * @return dateFormatList, contains all acceptable date formats without year
	 *         in DateTimeFormatter type ArrayList.
	 */
	ArrayList<DateTimeFormatter> generateDateFormatListWithoutYear() {
		ArrayList<DateTimeFormatter> dateFormatList = new ArrayList<DateTimeFormatter>();
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_LONG_DAY_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_LONG_DAY));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_SHORT_DAY_NOSPACE));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_MONTH_SHORT_DAY));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HASH_DAY_MONTH_NUM));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_HYPHEN_DAY_MONTH_NUM));
		return dateFormatList;
	}
}
