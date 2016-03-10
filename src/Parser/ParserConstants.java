package Parser;

public class ParserConstants {

	// Command Class, primarily
	static final String[] COMMAND_ADD = { "add", "create", "+", "a" };
	static final String[] COMMAND_DELETE = { "delete", "d", "del", "-", "clear", "remove" };
	static final String[] COMMAND_MODIFY = { "modify", "edit", "update", "change", "e" };
	static final String[] COMMAND_COMPLETE = { "complete", "done", "finish", "completed", "finished", "archive" };
	static final String[] COMMAND_VIEW = { "view", "v", "show", "display" };
	static final String[] COMMAND_SEARCH = { "search", "find" };
	static final String[] COMMAND_EXIT = { "exit", "quit", "q" };
	static final String[] COMMAND_UNDO = { "undo", "u", "z" };
	static final String[] COMMAND_REDO = { "redo" };
	static final String COMMAND_INVALID = null;
	static final String COMMAND_EMPTY = "";

	// Regular Expressions
	static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";
	static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{2})|(\\d{3,4}))(\\s|$)";
	static final String REGEX_DATE = "(\\s|^|\\G)\\d{1,2}(-|/)\\d{1,2}(-|/)(\\d{4}|\\d{2})(\\s|$)";
	static final String REGEX_POSSIBLE_DATE = "\\b\\w";
	static final String REGEX_DIGITS = "(\\s|^|,|-|\\G)\\d+(\\s|$|,|-)";
	static final String REGEX_DIGITS_AT_START = "^\\d+\\s";

	// Position/Index based integer constants
	static final int NO_WHITE_SPACE = -1;
	static final int DEFAULT_INDEX_NUMBER = -1;
	static final int FIRST_INDEX = 0;
	static final int INVALID_SIZE = -1;

	// Date related constants
	static final int CENTURY = 100;
	static final int INDEX_DAY_OF_MONTH = 0;
	static final int INDEX_MONTH = 1;
	static final int INDEX_YEAR = 2;
	// Acceptable date formats
	static final String DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_LONG = "d/M/uuuu";
	static final String DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_SHORT = "d/M/uu";
	static final String DATE_FORMAT_HASH_DAY_MONTH_NUM = "d/M";
	static final String DATE_FORMAT_HYPHEN_DAY_MONTH_NUM = "d-M";
	static final String DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_LONG = "d-M-uuuu";
	static final String DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_SHORT = "d-M-uu";
	static final String DATE_FORMAT_DAY_MONTH_LONG_YEAR_LONG = "d MMMM uuuu";
	static final String DATE_FORMAT_DAY_MONTH_LONG_YEAR_SHORT = "d MMMM uu";
	static final String DATE_FORMAT_DAY_MONTH_SHORT_YEAR_LONG = "d MMM uuuu";
	static final String DATE_FORMAT_DAY_MONTH_SHORT_YEAR_SHORT = "d MMM uu";
	static final String DATE_FORMAT_DAY_MONTH_SHORT = "d MMM";
	static final String DATE_FORMAT_DAY_MONTH_LONG = "d MMMM";
	/*
	 * static final String DATE_FORMAT_DAY_MONTH_LONG_COMMA_YEAR_LONG =
	 * "d MMMM, uuuu"; static final String
	 * DATE_FORMAT_DAY_MONTH_LONG_COMMA_YEAR_SHORT = "d MMMM, uu"; static final
	 * String DATE_FORMAT_DAY_MONTH_SHORT_COMMA_YEAR_LONG = "d MMM, uuuu";
	 * static final String DATE_FORMAT_DAY_MONTH_SHORT_COMMA_YEAR_SHORT =
	 * "d MMM, uu";
	 */
	static final String[] UPCOMING_DAYS = { "today", "tomorrow", "overmorrow", "tmr", "tmw", "tmrw" };
	// Days of the week
	static final String[] DAYS_OF_WEEK_SHORT = { "", "mon", "tue", "wed", "thu", "fri", "sat", "sun" };
	static final String[] DAYS_OF_WEEK_LONG = { "", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday" };

	// Time related constants
	static final int MAX_MINUTES = 59;
	static final int MAX_HOUR = 23;

	static final char WHITE_SPACE = ' ';

	static final String STRING_EMPTY = "";
	static final String STRING_WHITESPACE = " ";

	// Number in words
	static final String[] NUM_TO_WORDS = { "", "one", "two", "three", "four", "five" };

	// Months
	static final String[] MONTHS_SHORT = { "", "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct",
			"nov", "dec" };
	static final String[] MONTHS_LONG = { "", "january", "february", "march", "april", "may", "june", "july", "august",
			"september", "october", "november", "december" };

}
