package Parser;

public class ParserConstants {

	// Regular Expressions
	static final String REGEX_EXTRA_WHITESPACE = "\\s{2,}";
	static final String REGEX_TIME = "(^|\\s|\\G)((\\d{1,2}(:|\\.)\\d{2})|(\\d{3,4}))(\\s|$)";
	static final String REGEX_DATE = "(\\s|^|\\G)\\d{1,2}(-|/)\\d{1,2}(-|/)(\\d{4}|\\d{2})(\\s|$)";
	static final String REGEX_DIGITS = "(\\s|^|,|-|\\G)\\d+(\\s|$|,|-)";
	static final String REGEX_DIGITS_AT_START = "^\\d+\\s";
	

	// Position/Index based integer constants
	static final int NO_WHITE_SPACE = -1;
	static final int DEFAULT_INDEX_NUMBER = -1;
	static final int FIRST_INDEX = 0;
	static final int INVALID_SIZE = -1;
	
	// date related constants
	static final int CENTURY = 100;
	static final int INDEX_DAY_OF_MONTH = 0;
	static final int INDEX_MONTH = 1;
	static final int INDEX_YEAR = 2;
	
	// time related constants
	static final int MAX_MINUTES = 59;
	static final int MAX_HOUR = 23;
	
	static final char WHITE_SPACE = ' ';
	
	static final String STRING_EMPTY = "";
	static final String STRING_WHITESPACE = " ";

	
	
	
}
