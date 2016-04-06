//@@author A0132778W

package Parser;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * IndexParser is used to parse the given String taskDetails and retrieve all
 * the indexes contained in it.
 * 
 * Indexes are specified only after delete, done, undone and edit commands.
 * 
 * In addition, edit allows for only one index number, whereas done, undone and
 * delete support multiple indexes.
 * 
 * Multiple indexes can be separated by either a comma, or a whitespace. A range
 * of digits, separated by a hyphen to keyword "to" can also be used.
 */
public class IndexParser {

	// Instance Variables
	private String taskDetails;
	private Command command;
	private ArrayList<Integer> indexList;

	/**************** CONSTRUCTORS *********************/
	// Default Constructor
	IndexParser() {
		this(null, ParserConstants.STRING_EMPTY);
	}

	// Parameterized Constructor
	IndexParser(Command newCommandDetails, String newTaskDetails) {
		this(newCommandDetails, newTaskDetails, new ArrayList<Integer>());
	}

	IndexParser(Command newCommandDetails, String newTaskDetails, ArrayList<Integer> newIndexList) {
		setTaskDetails(newTaskDetails);
		setCommandDetails(newCommandDetails);
		setIndexList(newIndexList);
	}

	/****************** SETTER METHODS ***********************/
	protected void setTaskDetails(String newTaskDetails) {
		if (newTaskDetails == null || newTaskDetails.isEmpty()) {
			this.taskDetails = ParserConstants.STRING_EMPTY;
		} else {
			newTaskDetails = cleanupExtraWhitespace(newTaskDetails);
			this.taskDetails = newTaskDetails;
		}
	}

	public void setCommandDetails(Command newCommandDetails) {
		this.command = newCommandDetails;
	}

	public void setIndexList(ArrayList<Integer> newIndexList) {
		this.indexList = newIndexList;
	}

	/******************* GETTER METHODS ***********************/
	public String getTaskDetails() {
		return this.taskDetails;
	}

	public Command getCommand() {
		return this.command;
	}

	public ArrayList<Integer> getIndexList() {
		if (indexList.isEmpty()) {
			return null;
		}
		return this.indexList;
	}

	/**************** OTHER METHODS ***********************/

	public void findIndexList() throws Exception {
		Command.COMMAND_TYPE commandType = command.getCommandType();
		switch (commandType) {
		case DELETE_TASK:
			detectMultipleIndexes();
			removeDuplicates();
			break;
		case COMPLETE_TASK:
			detectMultipleIndexes();
			removeDuplicates();
			break;
		case INCOMPLETE_TASK:
			detectMultipleIndexes();
			removeDuplicates();
			break;
		case MODIFY_TASK:
			detectSingleIndex();
			break;
		default:
			// return
			break;
		}
	}

	/**
	 * Delete, Done and Undone support multiple indexes. This method helps parse
	 * the given input String and generate the index list.
	 * 
	 * @throws Exception
	 */
	public void detectMultipleIndexes() throws Exception {
		if (taskDetails == null || taskDetails.isEmpty()) {
			return;
		}
		String taskStatement = formatDelimiters(getTaskDetails());
		String[] tempIndexArray = taskStatement.split(ParserConstants.REGEX_INDEX_DELIMITER);
		// ArrayList<String> tempIndexList = convertArrayToList(tempIndexArray);
		generateIndexList(tempIndexArray);
		sortIndexList();
	}

	/**
	 * The Edit command provided by the user can contain only a single index
	 * number. This method is used to retrieve that single index, which should
	 * be at the start of the String taskDetails.
	 */
	public void detectSingleIndex() {
		Pattern indexPattern = Pattern.compile(ParserConstants.REGEX_DIGITS_AT_START);
		Matcher indexMatcher = indexPattern.matcher(taskDetails);
		if (indexMatcher.find()) {
			int startIndex = indexMatcher.start();
			int endIndex = indexMatcher.end();
			addIndexToList(taskDetails.substring(startIndex, endIndex).trim());
			removeIndexAtStartFromTaskDetails();
		}
	}

	public ArrayList<String> convertArrayToList(String[] tempArray) throws Exception {
		return new ArrayList<String>(Arrays.asList(tempArray));
	}

	private void generateIndexList(String[] tempList) throws Exception {
		for (String item : tempList) {
			item = cleanupExtraWhitespace(item);
			if (containsIndexRange(item)) {
				addRangeToIndexList(item);
			} else if (isOnlyDigits(item)) {
				addIndexToList(item);
			} else {
				throw new Exception("Invalid Index List");
			}
		}
	}

	public void sortIndexList() {
		Collections.sort(indexList);
	}

	/**
	 * This method helps in generating the entire array of indexes out of the
	 * indexes specified as a range.
	 * 
	 * @param item
	 * @throws Exception
	 */
	private void addRangeToIndexList(String item) throws Exception {
		String[] range = new String[2];
		if (item.contains(ParserConstants.STRING_HYPHEN)) {
			range = item.split(ParserConstants.STRING_HYPHEN);
		} else if (item.contains(ParserConstants.STRING_TO)) {
			range = item.split(ParserConstants.STRING_TO);
		}

		if (range.length != 2) {
			throw new Exception("Invalid Index List");
		}

		for (int index = ParserConstants.FIRST_INDEX; index < range.length; index++) {
			range[index] = cleanupExtraWhitespace(range[index]);
		}

		for (int index = Integer.parseInt(range[ParserConstants.FIRST_INDEX]); index <= Integer
				.parseInt(range[range.length - 1]); index++) {
			indexList.add(index);
		}
	}

	private boolean isOnlyDigits(String item) {
		item = cleanupExtraWhitespace(item);
		return item.matches(ParserConstants.REGEX_ONLY_DIGITS);
	}

	private void addIndexToList(String item) {
		indexList.add(Integer.parseInt(item));
	}

	public void removeIndexAtStartFromTaskDetails() {
		String taskStatement = getTaskDetails();
		taskStatement = taskStatement.replaceFirst(ParserConstants.REGEX_DIGITS_AT_START,
				ParserConstants.STRING_WHITESPACE);
		taskStatement = CommandParser.cleanupExtraWhitespace(taskStatement);
		setTaskDetails(taskStatement);
	}

	/**
	 * This method makes sure that there are no duplicate copies of any index;
	 * so as to avoid unfavorable results.
	 */
	public void removeDuplicates() {
		for (int index = 0; index < indexList.size() - 1; index++) {
			if (indexList.get(index) == indexList.get(index + 1)) {
				indexList.remove(index);
				index--;
			}
		}
	}

	/**
	 * A specific string contains an index range only if contains the "to"
	 * keyword or a hyphen. This method is used to check if item contains an
	 * index range or not.
	 * 
	 * @param item
	 * @return true, if item contains an index range; false otherwise.
	 */
	public boolean containsIndexRange(String item) {
		item = item.toLowerCase();
		return item.contains(ParserConstants.STRING_HYPHEN) || item.contains(ParserConstants.STRING_TO);
	}

	/**
	 * This method is used to format the delimiters used to parse indexes. It
	 * replaces all the whitespace and comma combinations to a single comma
	 * without a whitespace and all range delimiters to a hyphen.
	 * 
	 * @param textToFormat
	 * @return textToFormat; after it has been correctly formatted.
	 */
	public String formatDelimiters(String textToFormat) {
		textToFormat = textToFormat.toLowerCase();
		textToFormat = textToFormat.replaceAll(ParserConstants.REGEX_COMMA_WITH_SPACES, ParserConstants.STRING_COMMA)
				.replaceAll(ParserConstants.REGEX_RANGE_DELIMITER, ParserConstants.STRING_HYPHEN);
		return textToFormat;
	}

	/**
	 * This method removes the unnecessary white spaces present in the string.
	 * 
	 * @param someText
	 *            is any string with several white spaces.
	 * @return someText excluding the extra unnecessary white spaces.
	 */
	private static String cleanupExtraWhitespace(String someText) {
		Pattern extraSpace = Pattern.compile(ParserConstants.REGEX_EXTRA_WHITESPACE);
		Matcher regexMatcher = extraSpace.matcher(someText.trim());
		String cleanText = regexMatcher.replaceAll(ParserConstants.STRING_WHITESPACE);
		return cleanText;
	}
}
