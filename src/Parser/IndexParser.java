package Parser;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import Parser.Command.COMMAND_TYPE;

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
		newTaskDetails = cleanupExtraWhitespace(newTaskDetails);
		this.taskDetails = newTaskDetails;
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
		if (commandType.equals(COMMAND_TYPE.DELETE_TASK) || commandType.equals(COMMAND_TYPE.COMPLETE_TASK)) {
			detectMultipleIndexes();
		} else if (commandType.equals(COMMAND_TYPE.MODIFY_TASK)) {
			detectSingleIndex();
		}
	}

	/**
	 * For delete and complete commands, multiple delete is possible.
	 * 
	 */
	public void detectMultipleIndexes() throws Exception {
		String taskStatement = getTaskDetails();
		String[] tempIndexArray = taskStatement.split(ParserConstants.STRING_COMMA);
		// ArrayList<String> tempIndexList = convertArrayToList(tempIndexArray);
		generateIndexList(tempIndexArray);
		sortIndexList();
	}

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

	public void addRangeToIndexList(String item) throws Exception {
		ArrayList<Integer> indexRange = getRange(item);
		for (int index : indexRange) {
			indexList.add(index);
		}
	}

	private ArrayList<Integer> getRange(String item) throws Exception {
		ArrayList<Integer> indexRange = new ArrayList<Integer>();
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
			indexRange.add(index);
		}

		return indexRange;
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

	public boolean containsIndexRange(String item) {
		return item.contains(ParserConstants.STRING_HYPHEN) || item.contains(ParserConstants.STRING_TO);
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
