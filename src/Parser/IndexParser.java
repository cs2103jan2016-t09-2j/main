package Parser;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

//import java.util.ArrayList;

import Parser.Command.COMMAND_TYPE;

public class IndexParser {

	// Instance Variables
	private String taskDetails;
	private Command command;

	/**************** CONSTRUCTORS *********************/
	// Default Constructor
	IndexParser() {
		this(null, ParserConstants.STRING_EMPTY);
	}

	// Parameterized Constructor
	IndexParser(Command newCommandDetails, String newTaskDetails) {
		setTaskDetails(newTaskDetails);
		setCommandDetails(newCommandDetails);
	}

	/****************** SETTER METHODS ***********************/
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = newTaskDetails;
	}

	public void setCommandDetails(Command newCommandDetails) {
		this.command = newCommandDetails;
	}

	/******************* GETTER METHODS ***********************/
	public String getTaskDetails() {
		return this.taskDetails;
	}

	public Command getCommand() {
		return this.command;
	}

	public int getIndex() throws Exception {
		int index = findIndexNumber(getCommand(), getTaskDetails());
		return index;
	}

	/**************** OTHER METHODS ***********************/
	/*
	 * public ArrayList<Integer> getIndexList(){ ArrayList<Integer> indexList =
	 * getIndexList(getTaskDetails()); if(hasIndexList(indexList)) { return
	 * indexList; } return null; }
	 * 
	 * public ArrayList<Integer> getIndexList(Command thisCommand, String
	 * taskDetails) { ArrayList<Integer> indexList = new ArrayList<Integer>(); }
	 */

	public int findIndexNumber(Command thisCommand, String taskDetails) throws Exception {
		Command.COMMAND_TYPE commandType = thisCommand.getCommandType();
		if (commandType.equals(COMMAND_TYPE.DELETE_TASK) || commandType.equals(COMMAND_TYPE.COMPLETE_TASK)) {
			taskDetails = CommandParser.cleanupExtraWhitespace(taskDetails);
			if (Pattern.matches(ParserConstants.REGEX_DIGITS, taskDetails)) {
				int indexNumber = Integer.parseInt(taskDetails);
				//setTaskDetails(taskDetails.replace(arg0, ParserConstants.STRING_WHITESPACE));
				return indexNumber;
			}
		} else if (commandType.equals(COMMAND_TYPE.MODIFY_TASK)) {
			taskDetails = CommandParser.cleanupExtraWhitespace(taskDetails);
			Pattern indexPattern = Pattern.compile(ParserConstants.REGEX_DIGITS_AT_START);
			Matcher indexMatcher = indexPattern.matcher(taskDetails);
			if (indexMatcher.find()) {
				int startIndex = indexMatcher.start();
				int endIndex = indexMatcher.end();
				int indexNumber = Integer.parseInt(taskDetails.substring(startIndex, endIndex).trim());
				removeIndexAtStartFromTaskDetails();
				return indexNumber;
			}
		}
		return ParserConstants.DEFAULT_INDEX_NUMBER;
	}

	public void removeIndexAtStartFromTaskDetails() {
		String taskStatement = getTaskDetails();
		taskStatement = taskStatement.replaceFirst(ParserConstants.REGEX_DIGITS_AT_START,
				ParserConstants.STRING_WHITESPACE);
		taskStatement = CommandParser.cleanupExtraWhitespace(taskStatement);
		setTaskDetails(taskStatement);
	}
}
