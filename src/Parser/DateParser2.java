package Parser;

import java.util.ArrayList;
import java.util.Locale;
import java.text.ParsePosition;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class DateParser2 {

	// Instance Variables
	private String taskDetails;
	private ArrayList<LocalDate> dateList = new ArrayList<LocalDate>();;

	/**************** CONSTRUCTORS *********************/
	DateParser2() {
		this(ParserConstants.STRING_EMPTY);
	}

	DateParser2(String newTaskDetails) {
		setTaskDetails(newTaskDetails);
	}

	/****************** SETTER METHOD ***********************/
	protected void setTaskDetails(String newTaskDetails) {
		this.taskDetails = newTaskDetails;
	}

	protected void setDateList(ArrayList<LocalDate> dateList) {
		this.dateList = dateList;
	}

	/****************** GETTER METHOD ***********************/
	public String getTaskDetails() {
		return this.taskDetails;
	}

	ArrayList<LocalDate> getDateList() {
		return this.dateList;
	}

	/****************** OTHER METHODS ***********************/

	/**
	 * This method checks if the immediate String contains a date
	 */
	public boolean isValidDate(String statement) {
		for (DateTimeFormatter format : generateDateFormatList()) {
			DateTimeFormatter myFormatter = new DateTimeFormatterBuilder().parseCaseInsensitive()
					.append(format).toFormatter(Locale.ENGLISH);
			try {
				LocalDate parsedDate = LocalDate.from(myFormatter.parse(statement.trim(), new ParsePosition(0)));
				return true;
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
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT_YEAR_SHORT));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_LONG));
		dateFormatList.add(DateTimeFormatter.ofPattern(ParserConstants.DATE_FORMAT_DAY_MONTH_SHORT));
		return dateFormatList;
	}
}
