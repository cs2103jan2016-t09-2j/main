package Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;

public class DateTimeParser {

	private static final int INVALID_SIZE = -1;
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
		this.dateList = newDateList;
	}
	
	protected void setTimeList(ArrayList<LocalTime> newTimeList) {
		this.timeList = newTimeList;
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
		alist = createDateTimeList(aListSize);
		Collections.sort(alist);
	}

	public ArrayList<LocalDateTime> createDateTimeList(int size) {
		if (getDateList() == null) {
			generateDateList(size);
		}
		if (getTimeList() == null) {
			generateTimeList(size);
		}
		for (int index = 0; index < size; index++) {

		}
		return null;
	}

	private void generateDateList(int size) {
		ArrayList<LocalTime> timeList = getTimeList();

	}

	private void generateTimeList(int size) {
		ArrayList<LocalDate> dateList = getDateList();
		for (int index = 0; index < size; index++) {

		}
	}

	public int getDateTimeListSize(ArrayList<LocalDate> dateList, ArrayList<LocalTime> timeList) {
		if (dateList != null && timeList != null) {
			return (dateList.size() > timeList.size()) ? dateList.size() : timeList.size();
		} else if (dateList == null && timeList != null) {
			return timeList.size();
		} else if (dateList != null && timeList == null) {
			return dateList.size();
		}
		return INVALID_SIZE;
	}
}
