//@@author A0132778W

package Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Collections;

public class DateTimeParser {

	private static final int INVALID_SIZE = -1;
	private static final int FIRST_INDEX = 0;
	private static final int ONE_DAY = 1;

	private ArrayList<LocalDate> dateList;
	private ArrayList<LocalTime> timeList;

	/**************** CONSTRUCTORS *********************/
	// Default Constructor
	DateTimeParser() {
		this(null, null);
	}

	// Parameterized Constructor
	DateTimeParser(ArrayList<LocalDate> newDateList, ArrayList<LocalTime> newTimeList) {
		setDateList(newDateList);
		setTimeList(newTimeList);
	}

	/****************** SETTER METHODS ***********************/
	protected void setDateList(ArrayList<LocalDate> newDateList) {
		if (newDateList == null) {
			this.dateList = new ArrayList<LocalDate>();
		} else {
			this.dateList = newDateList;
		}
	}

	protected void setTimeList(ArrayList<LocalTime> newTimeList) {
		if (newTimeList == null) {
			this.timeList = new ArrayList<LocalTime>();
		} else {
			this.timeList = newTimeList;
		}
	}

	/******************* GETTER METHODS ***********************/
	public ArrayList<LocalDate> getDateList() {
		return this.dateList;
	}

	public ArrayList<LocalTime> getTimeList() {
		return this.timeList;
	}

	/**************** OTHER METHODS ***********************/
	public void arrangeDateTimeList() {
		ArrayList<LocalDateTime> alist;
		int aListSize = getDateTimeListSize(getDateList(), getTimeList());
		// System.out.println(aListSize);
		if (aListSize > INVALID_SIZE) {
			alist = createDateTimeList(aListSize);
			Collections.sort(alist);
			removeDuplicates(alist);
			separateDateAndTime(alist);
		}
	}

	/**
	 * This method makes sure that there are no duplicate copies of any date and
	 * time; so as to avoid unfavorable results.
	 */
	public void removeDuplicates(ArrayList<LocalDateTime> aList) {
		for (int index = 0; index < aList.size() - 1; index++) {
			if (aList.get(index).isEqual(aList.get(index + 1))) {
				aList.remove(index);
				index--;
			}
		}
	}

	void separateDateAndTime(ArrayList<LocalDateTime> alist) {
		ArrayList<LocalDate> newDateList = new ArrayList<LocalDate>();
		ArrayList<LocalTime> newTimeList = new ArrayList<LocalTime>();
		for (int index = FIRST_INDEX; index < alist.size(); index++) {
			newDateList.add(alist.get(index).toLocalDate());
			newTimeList.add(alist.get(index).toLocalTime());
		}
		setDateList(newDateList);
		setTimeList(newTimeList);
	}

	/**
	 * This method is invoked to ensure that both date and time lists are of
	 * equal sizes. If unequal, the empty spaces are filled up with appropriate
	 * values.
	 * 
	 * @param size
	 * @return a list of date and time combined.
	 */
	public ArrayList<LocalDateTime> createDateTimeList(int size) {
		ArrayList<LocalDateTime> alist = new ArrayList<LocalDateTime>();
		if (dateList.isEmpty()) {
			generateDateList(size);
		} else if (timeList.isEmpty()) {
			generateTimeList(size);
		} else if (dateList.size() > timeList.size()) {
			generateTimeList(size);
		} else if (dateList.size() < timeList.size()) {
			generateDateList(size);
		}
		for (int index = FIRST_INDEX; index < size; index++) {
			alist.add(LocalDateTime.of(dateList.get(index), timeList.get(index)));
		}
		return alist;
	}

	/**
	 * This method is invoked in the condition that there are more times than
	 * dates in the lists. As a result, the blank spaces in the date list are
	 * filled with either the same dates as the previous ones or the next day,
	 * as per the times provided.
	 * 
	 * @param size
	 *            is the size of the larger time list.
	 */
	public void generateDateList(int size) {
		for (int index = dateList.size(); index < size; index++) {
			LocalDate tempDate;
			LocalTime tempTime;
			if (index == FIRST_INDEX) {
				tempDate = LocalDate.now();
				tempTime = LocalTime.now();
			} else {
				tempDate = dateList.get(index - 1);
				tempTime = timeList.get(index - 1);
			}
			LocalDateTime tempDateTime = LocalDateTime.of(tempDate, timeList.get(index));
			if (tempDateTime.isBefore(LocalDateTime.of(tempDate, tempTime))) {
				tempDate = tempDate.plusDays(ONE_DAY);
			}
			dateList.add(tempDate);
		}
	}

	/**
	 * This method is invoked in the condition that there are more dates than
	 * times in the lists. As a result, the blank spaces in the time list are
	 * filled with MAX time.
	 * 
	 * @param size
	 *            is the size of the larger date list.
	 */
	public void generateTimeList(int size) {
		for (int index = timeList.size(); index < size; index++) {
			timeList.add(LocalTime.MAX);
		}
	}

	/**
	 * This method is used to determine the final size of the date and time
	 * lists.
	 * 
	 * @param dateList
	 * @param timeList
	 * @return the greater of the dateList and timeList size if valid; otherwise
	 *         -1.
	 */
	public int getDateTimeListSize(ArrayList<LocalDate> dateList, ArrayList<LocalTime> timeList) {
		if (dateList == null || timeList == null) {
			return INVALID_SIZE;
		}
		return (dateList.size() > timeList.size()) ? dateList.size() : timeList.size();
	}
}
