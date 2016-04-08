//@@author A0132778W

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
import java.time.temporal.ChronoUnit;
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
		return cleanupExtraWhitespace(this.taskDetails);
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
			try {
				String previousWord = getLastWordInRange(taskDetails, timeMatcher.start());
				String tempString = cleanupExtraWhitespace(taskDetails.substring(timeMatcher.start()));

				if (addToListIfValidTime(tempString, previousWord)) {
				} else {
					if (hasTimeDuration(tempString)) {
						String timeDuration = getTimeDurationWord(tempString);
						if (taskDetailsContains(timeDuration)) {
							addValidTimeToList(getParsedTimeDuration(timeDuration));
							if (isValidKeyWord(previousWord) || isValidRangeKeyWord(previousWord)) {
								removeTimeFromTaskDetails(
										previousWord + ParserConstants.STRING_WHITESPACE + timeDuration);
							} else {
								removeTimeFromTaskDetails(timeDuration);
							}
						}
					}
				}
			} catch (Exception e) {
				// do nothing
			}
		}
	}

	/**
	 * This method checks if the immediate String contains a time. If it is a
	 * Valid Time, it adds it to the timeList.
	 * 
	 * @param statement
	 * @param keyword
	 * @return true, if the immediate String is a valid time; otherwise false.
	 */
	public boolean addToListIfValidTime(String statement, String keyword) {
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

				if (taskDetailsContains(statement) && isValidEnd(end)) {
					addValidTimeToList(parsedTime);
					if (isValidKeyWord(keyword) || isValidRangeKeyWord(keyword)) {
						removeTimeFromTaskDetails(keyword + ParserConstants.STRING_WHITESPACE + statement);
					} else {
						removeTimeFromTaskDetails(statement);
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
	 * This method removes the particular textToRemove from taskDetails.
	 * 
	 * @param textToRemove
	 */
	public void removeTimeFromTaskDetails(String textToRemove) {

		if (taskDetails.startsWith(textToRemove)) {
			taskDetails = CommandParser
					.cleanupExtraWhitespace(taskDetails.replaceFirst(textToRemove, ParserConstants.STRING_WHITESPACE));
		} else {
			taskDetails = CommandParser
					.cleanupExtraWhitespace(taskDetails.replaceFirst(textToRemove, ParserConstants.STRING_WHITESPACE));
		}
	}

	public LocalTime getParsedTimeDuration(String text) {
		try {
			String firstWord = ParserConstants.STRING_EMPTY;
			String secondWord = ParserConstants.STRING_EMPTY;
			if (isFirstWordTimeDuration(text)) {
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
			LocalTime parsedTime = getTimeDuration(firstWord, secondWord);
			return parsedTime;
		} catch (Exception e) {
			// no exception encountered, just a precaution
		}
		return null;
	}

	/**
	 * This method extracts the time duration word from the given textToSearch.
	 * 
	 * @param textToSearch
	 * @return time duration word from textToSearch.
	 */
	public String getTimeDurationWord(String textToSearch) {
		String firstWord = getStartString(getFirstXWords(textToSearch, ParserConstants.ONE_WORD));

		if (isFirstWordTimeDuration(textToSearch)) {
			return firstWord;
		} else {
			return getStartString(getFirstXWords(textToSearch, ParserConstants.TWO_WORDS));
		}
	}

	public LocalTime getTimeDuration(String firstWord, String secondWord) {
		LocalTime parsedTime = getTimeNow();
		int unitsToAdd = Integer.parseInt(firstWord);
		int indexOfKeyword = indexOf(secondWord, ParserConstants.TIME_DURATION);
		if (indexOfKeyword <= ParserConstants.LAST_INDEX_OF_MIN) {
			parsedTime = parsedTime.plusMinutes(unitsToAdd);
		} else {
			parsedTime = parsedTime.plusHours(unitsToAdd);
		}
		return parsedTime;
	}

	/**
	 * Time duration refers to a number followed by either hours or minutes. The
	 * digit and the String may or may not have a whitespace in between.
	 * 
	 * @param tempString
	 * @return true if tempString starts with a time duration word; false
	 *         otherwise.
	 */
	public boolean hasTimeDuration(String tempString) {
		try {
			return isFirstWordTimeDuration(tempString) || isFirstTwoWordsTimeDuration(tempString);
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
	 * This method is used to check the first word of inputString for occurrence
	 * of time duration.
	 * 
	 * @param inputString
	 * @return
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 * @throws IndexOutOfBoundsException
	 */
	public boolean isFirstWordTimeDuration(String inputString)
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
			if (hasInDictionary(ParserConstants.TIME_DURATION, secondWord) && isValidEnd(endString)) {
				Integer.parseInt(firstWord);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to check the first two words of inputString for
	 * occurrence of time duration.
	 * 
	 * @param inputString
	 * @return
	 * @throws NullPointerException
	 * @throws NumberFormatException
	 */
	public boolean isFirstTwoWordsTimeDuration(String inputString) throws NullPointerException, NumberFormatException {
		String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
		String first2Words = getFirstXWords(inputString, ParserConstants.TWO_WORDS);
		String secondWord = first2Words.replace(firstWord, ParserConstants.STRING_WHITESPACE).trim();
		String endString = getEnd(secondWord);

		if (hasInDictionary(ParserConstants.TIME_DURATION, getStartString(secondWord)) && isValidEnd(endString)) {
			Integer.parseInt(firstWord);
			return true;
		}
		return false;
	}

	public void addValidTimeToList(LocalTime parsedTime) {
		timeList.add(parsedTime);
	}

	public LocalTime getTimeNow() {
		return LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
	}

	public boolean isValidKeyWord(String keyword) {
		return hasInDictionary(ParserConstants.TIME_KEYWORD, keyword.trim());
	}

	/**
	 * "to" and hyphen "-" are the range keywords. This method checks if keyword
	 * is a valid range keyword.
	 * 
	 * @param keyword
	 * @return return true if keyword equals "to" or "-"; false otherwise.
	 */
	public boolean isValidRangeKeyWord(String keyword) {
		keyword = keyword.trim();
		return (keyword.equalsIgnoreCase(ParserConstants.STRING_HYPHEN)
				|| keyword.equalsIgnoreCase(ParserConstants.STRING_TO)) && (timeList.size() > ParserConstants.MIN_SIZE);
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
	 * This method is used to determine the last word in text between 0 and
	 * endIndex.
	 * 
	 * @param text
	 * @param endIndex
	 * @return last word of text in the range [0, endIndex] if present;
	 *         otherwise empty String.
	 */
	public String getLastWordInRange(String text, int endIndex) {

		if (text != null && !text.isEmpty() && endIndex > 0) {
			text = text.substring(ParserConstants.FIRST_INDEX, endIndex);
			String[] words = text.split(ParserConstants.STRING_WHITESPACE);
			return words[words.length - 1];
		}
		return ParserConstants.STRING_EMPTY;
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

	/**
	 * This method detects the index of a non-word character in the String word.
	 * A non-word character includes everything other than an English letter, a
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

	public boolean taskDetailsContains(String text) {
		Pattern containPattern = Pattern.compile(ParserConstants.REGEX_VALID_START + text);
		Matcher containMatcher = containPattern.matcher(taskDetails);
		if (containMatcher.find()) {
			return true;
		}
		return false;
	}

	/**
	 * This method checks if any index of dictionary[] equals the wordsToFind
	 * (case insensitive).
	 * 
	 * @param dictionary
	 * @param wordsToFind
	 * @return true if dictionary contains wordToFind; false otherwise.
	 */
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

		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR_MIN));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR_COLON));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR_PERIOD));
		return dateFormatList;
	}
}
