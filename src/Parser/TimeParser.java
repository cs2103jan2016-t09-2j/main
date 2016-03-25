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
			String tempString = cleanupExtraWhitespace(taskDetails.substring(timeMatcher.start()));

			if (!addToListIfValidTime(tempString)) {

				if (hasTimeDuration(tempString)) {
					addValidTimeToList(getParsedTimeDuration(tempString));
				}
			}
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

	public LocalTime getParsedTimeDuration(String inputString) {
		try {
			String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
			String secondWord = "";
			if (isFirstWordTimeDuration(inputString)) {
				int splitPos = -1;
				for (int index = ParserConstants.FIRST_INDEX; index < firstWord.length(); index++) {
					if (!Character.isDigit(firstWord.charAt(index))) {
						splitPos = index;
						break;
					}
				}
				removeTimeFromTaskDetails(firstWord);
				secondWord = firstWord.substring(splitPos);
				firstWord = firstWord.substring(ParserConstants.FIRST_INDEX, splitPos);
			} else {
				String first2Words = getFirstXWords(inputString, ParserConstants.TWO_WORDS);
				secondWord = first2Words.replace(firstWord, ParserConstants.STRING_WHITESPACE).trim();
				removeTimeFromTaskDetails(first2Words);
			}
			LocalTime parsedTime = getTimeDuration(firstWord, secondWord);
			return parsedTime;
		} catch (Exception e) {
			// no exception encountered, just a precaution
		}
		return null;
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

	public boolean isFirstWordTimeDuration(String inputString)
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
			if (hasInDictionary(ParserConstants.TIME_DURATION, secondWord)) {
				Integer.parseInt(firstWord);
				return true;
			}
		}
		return false;
	}

	public boolean isFirstTwoWordsTimeDuration(String inputString) throws NullPointerException, NumberFormatException {
		String firstWord = getFirstXWords(inputString, ParserConstants.ONE_WORD);
		String first2Words = getFirstXWords(inputString, ParserConstants.TWO_WORDS);
		String secondWord = first2Words.replace(firstWord, ParserConstants.STRING_WHITESPACE).trim();

		if (hasInDictionary(ParserConstants.TIME_DURATION, secondWord)) {
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

		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR_COLON));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.TIME_FORMAT_24HOUR_PERIOD));
		return dateFormatList;
	}
}
