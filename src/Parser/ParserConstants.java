/**
 * A Simple Class of Constants used throughout Package Parser.
 */

//@@author A0132778W

package Parser;

public class ParserConstants {

	// Command Class, primarily
	static final String[] COMMAND_ADD = { "add", "+", "a" };
	static final String[] COMMAND_DELETE = { "delete", "d", "del", "-", "remove", "rm" };
	static final String[] COMMAND_MODIFY = { "edit", "update", "e" };
	static final String[] COMMAND_COMPLETE = { "done", "completed", "archive" };
	static final String[] COMMAND_INCOMPLETE = { "undone" };
	static final String[] COMMAND_VIEW = { "view", "v", "show", "display" };
	static final String[] COMMAND_SEARCH = { "search", "find" };
	static final String[] COMMAND_EXIT = { "exit", "quit", "q" };
	static final String[] COMMAND_UNDO = { "undo", "z" };
	static final String[] COMMAND_REDO = { "redo", "y" };
	static final String[] COMMAND_SET_DIRECTORY = { "set" };
	static final String[] COMMAND_HOME = { "home" };
	static final String[] COMMAND_HELP = { "help" };
	static final String COMMAND_INVALID = null;
	static final String COMMAND_EMPTY = "";

	// Parameters of Task object
	static final String[] PARAMETER_DESCRIPTION = { "description", "detail", "details", "desc" };
	static final String[] PARAMETER_DATE = { "date", "dates" };
	static final String[] PARAMETER_TIME = { "time", "times" };

	// Regular Expressions
	static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";
	static final String REGEX_POSSIBLE_DATE = "(^|\\s)(\\w|-)";
	static final String REGEX_POSSIBLE_DURATION = "^\\d+[A-Za-z]{2,}";
	static final String REGEX_POSSIBLE_TIME = "(^|\\s)\\d";
	// static final String REGEX_POSSIBLE_TIME = "(^|\\s|\\G)\\d";
	static final String REGEX_DIGITS_VALID_END = "(\\s|^|,|-|\\G)\\d+(\\s|$|,|-)";
	static final String REGEX_VALID_START = "(^|\\s)";
	static final String REGEX_DIGITS_AT_START = "^\\d+\\s";
	static final String REGEX_INDEX_DELIMITER = "(,|\\s)";
	static final String REGEX_RANGE_DELIMITER = "\\s*((to)|-)\\s*";
	static final String REGEX_ONLY_DIGITS = "\\b\\d+\\b";
	static final String REGEX_ONLY_WORDS = "\\b\\w+\\b";
	static final String REGEX_COMMA_WITH_SPACES = "\\s*,\\s*";
	static final String REGEX_HYPHEN_WITH_DIGITS = "\\d+-\\d+";
	static final String REGEX_POSSIBLE_DIRECTORY = "([A-Za-z]:\\\\\\\\\\w+)";

	// Position/Index based integer constants
	static final int NO_WHITE_SPACE = -1;
	static final int DEFAULT_INDEX_NUMBER = -1;
	static final int FIRST_INDEX = 0;
	static final int INVALID_SIZE = -1;
	static final int ONE_WORD = 1;
	static final int TWO_WORDS = 2;
	static final int THREE_WORDS = 3;

	// Date related constants
	static final int CENTURY = 100;
	static final int ONE_YEAR = 1;
	static final int FIRST_DAY_OF_MONTH = 1;
	static final int INDEX_DAY_OF_MONTH = 0;
	static final int INDEX_MONTH = 1;
	static final int INDEX_YEAR = 2;
	static final int DAYS_IN_WEEK = 7;
	// Acceptable date formats
	static final String DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_LONG = "d/M/uuuu";
	static final String DATE_FORMAT_HASH_DAY_MONTH_NUM_YEAR_SHORT = "d/M/uu";
	static final String DATE_FORMAT_HASH_DAY_MONTH_NUM = "d/M";
	static final String DATE_FORMAT_HYPHEN_DAY_MONTH_NUM = "d-M";
	static final String DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_LONG = "d-M-uuuu";
	static final String DATE_FORMAT_HYPHEN_DAY_MONTH_NUM_YEAR_SHORT = "d-M-uu";
	static final String DATE_FORMAT_DAY_MONTH_LONG_YEAR_LONG_NOSPACE = "dMMMMuuuu";
	static final String DATE_FORMAT_DAY_MONTH_LONG_YEAR_SHORT_NOSPACE = "dMMMMuu";
	static final String DATE_FORMAT_DAY_MONTH_SHORT_YEAR_LONG_NOSPACE = "dMMMuuuu";
	static final String DATE_FORMAT_DAY_MONTH_SHORT_YEAR_SHORT_NOSPACE = "dMMMuu";
	static final String DATE_FORMAT_DAY_MONTH_LONG_SPACE_YEAR_LONG = "dMMMM uuuu";
	static final String DATE_FORMAT_DAY_MONTH_LONG_SPACE_YEAR_SHORT = "dMMMM uu";
	static final String DATE_FORMAT_DAY_MONTH_SHORT_SPACE_YEAR_LONG = "dMMM uuuu";
	static final String DATE_FORMAT_DAY_MONTH_SHORT_SPACE_YEAR_SHORT = "dMMM uu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_LONG_YEAR_LONG = "d MMMMuuuu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_LONG_YEAR_SHORT = "d MMMMuu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_SHORT_YEAR_LONG = "d MMMuuuu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_SHORT_YEAR_SHORT = "d MMMuu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_LONG_SPACE_YEAR_LONG = "d MMMM uuuu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_LONG_SPACE_YEAR_SHORT = "d MMMM uu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_SHORT_SPACE_YEAR_LONG = "d MMM uuuu";
	static final String DATE_FORMAT_DAY_SPACE_MONTH_SHORT_SPACE_YEAR_SHORT = "d MMM uu";

	static final String DATE_FORMAT_MONTH_LONG_DAY_SPACE_YEAR_LONG = "MMMMd uuuu";
	static final String DATE_FORMAT_MONTH_LONG_SPACE_DAY_YEAR_LONG = "MMMM d uuuu";
	static final String DATE_FORMAT_MONTH_SHORT_DAY_SPACE_YEAR_LONG = "MMMd uuuu";
	static final String DATE_FORMAT_MONTH_SHORT_SPACE_DAY_SPACE_YEAR_LONG = "MMM d uuuu";

	static final String DATE_FORMAT_DAY_MONTH_LONG_NOSPACE = "dMMMM";
	static final String DATE_FORMAT_DAY_MONTH_SHORT_NOSPACE = "dMMM";
	static final String DATE_FORMAT_DAY_MONTH_SHORT = "d MMM";
	static final String DATE_FORMAT_DAY_MONTH_LONG = "d MMMM";
	static final String DATE_FORMAT_MONTH_LONG_DAY_NOSPACE = "MMMMd";
	static final String DATE_FORMAT_MONTH_LONG_DAY = "MMMM d";
	static final String DATE_FORMAT_MONTH_SHORT_DAY_NOSPACE = "MMMd";
	static final String DATE_FORMAT_MONTH_SHORT_DAY = "MMM d";

	static final String[] DAY_DURATION = { "day", "days", "week", "weeks", "month", "months", "year", "years", "yr",
			"yrs" };
	static final int LAST_INDEX_OF_DAY = 1;
	static final int LAST_INDEX_OF_WEEK = 3;
	static final int LAST_INDEX_OF_MONTH = 5;
	static final int LAST_INDEX_OF_YEAR = 9;
	static final String[] DATE_KEYWORD = { "by", "on", "in", "before", "from", "frm" };

	static final String[] UPCOMING_DAYS = { "today", "tdy", "tomorrow", "tmr", "tmw", "tmrw", "overmorrow",
			"day after tomorrow", "day after tmr", "day after tmw", "day after tmrw", "day aftr tomorrow",
			"day aftr tmr", "day aftr tmw", "day aftr tmrw" };
	static final String[] UPCOMING_PERIOD_KEYWORD = { "this", "next", /* "next to next" */ };
	// Days of the week
	static final String[] DAYS_OF_WEEK_SHORT = { "", "mon", "tue", "wed", "thu", "fri", "sat", "sun" };
	static final String[] DAYS_OF_WEEK_MEDIUM = { "", "", "tues", "wednes", "thurs", "", "", "" };
	static final String[] DAYS_OF_WEEK_LONG = { "", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday",
			"sunday" };

	static final int MIN_SIZE = 1;

	// Time related constants
	static final int MAX_MINUTES = 59;
	static final int MAX_HOUR = 23;
	static final int TWELVE_HOURS = 12;
	// Acceptable Time formats
	static final String TIME_FORMAT_12HOUR_MIN_AMPM_WITHOUT_SPACE = "hmma";
	static final String TIME_FORMAT_12HOUR_MIN_AMPM_WITH_SPACE = "hmm a";
	static final String TIME_FORMAT_12HOUR_MIN_AMPM_WITH_PERIOD_WITHOUT_SPACE = "h.mma";
	static final String TIME_FORMAT_12HOUR_MIN_AMPM_WITH_PERIOD_WITH_SPACE = "h.mm a";
	static final String TIME_FORMAT_12HOUR_MIN_AMPM_WITH_COLOM_WITHOUT_SPACE = "h:mma";
	static final String TIME_FORMAT_12HOUR_MIN_AMPM_WITH_COLON_WITH_SPACE = "h:mm a";
	static final String TIME_FORMAT_12HOUR_AMPM_WITHOUT_SPACE = "ha";
	static final String TIME_FORMAT_12HOUR_AMPM_WITH_SPACE = "h a";
	static final String TIME_FORMAT_24HOUR_COLON = "H:mm";
	static final String TIME_FORMAT_24HOUR_PERIOD = "H.mm";
	static final String TIME_FORMAT_24HOUR_MIN = "HHmm";

	static final String[] TIME_DURATION = { "mins", "min", "minutes", "minute", "hr", "hrs", "hours", "hour" };
	static final int LAST_INDEX_OF_MIN = 3;
	static final int LAST_INDEX_OF_HOUR = 7;
	static final String[] TIME_KEYWORD = { "by", "at", "in", "before", "from", "frm" };

	static final char WHITE_SPACE = ' ';

	static final String STRING_EMPTY = "";
	static final String STRING_WHITESPACE = " ";
	static final String STRING_COMMA = ",";
	static final String STRING_HYPHEN = "-";
	static final String STRING_HYPHEN_WHITESPACE = " - ";
	static final String STRING_TO = "to";
	static final String STRING_OVERDUE = "overdue";

	// Number in words
	static final String[] NUM_TO_WORDS = { "", "one", "two", "three", "four", "five" };

	// Months
	static final String[] MONTHS_SHORT = { "", "jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct",
			"nov", "dec" };
	static final String[] MONTHS_LONG = { "", "january", "february", "march", "april", "may", "june", "july", "august",
			"september", "october", "november", "december" };

	static final String[] VALID_END = { ",", " ", ".", "" };

}
