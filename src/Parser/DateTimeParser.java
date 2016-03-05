package Parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

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
		}
		this.dateList = newDateList;
	}

	protected void setTimeList(ArrayList<LocalTime> newTimeList) {
		if (newTimeList == null) {
			this.timeList = new ArrayList<LocalTime>();
		}
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
		separateDateAndTime(alist);
	}
	
	void separateDateAndTime(ArrayList<LocalDateTime> alist) {
		
	}

	public ArrayList<LocalDateTime> createDateTimeList(int size) {
		ArrayList<LocalDateTime> alist = new ArrayList<LocalDateTime>();
		if (dateList.isEmpty()) {
			generateDateList(size);
		} else if (timeList.isEmpty()) {
			generateTimeList(size);
		} else if (dateList.size() > timeList.size()) {
			generateTimeList(dateList.size() - timeList.size());
		} else if (dateList.size() < timeList.size()) {
			generateDateList(timeList.size() - dateList.size());
		}
		for (int index = FIRST_INDEX; index < size; index++) {
			alist.add(LocalDateTime.of(dateList.get(index), timeList.get(index)));
		}
		Collections.sort(alist);
		return alist;
	}

	public void generateDateList(int size) {
		for (int index = dateList.size(); index < size; index++) {
			LocalDate tempDate = LocalDate.now();
			LocalDateTime tempDateTime = LocalDateTime.of(tempDate, timeList.get(index));
			if (tempDateTime.isBefore(LocalDateTime.now())) {
				tempDate = tempDate.plusDays(ONE_DAY);
			}
			dateList.add(tempDate);
		}
	}

	public void generateTimeList(int size) {
		for (int index = timeList.size(); index < size; index++) {
			timeList.add(LocalTime.MAX.truncatedTo(ChronoUnit.MINUTES));
		}
	}

	public int getDateTimeListSize(ArrayList<LocalDate> dateList, ArrayList<LocalTime> timeList) {
		if (dateList == null || timeList == null) {
			return INVALID_SIZE;
		}
		return (dateList.size() > timeList.size()) ? dateList.size() : timeList.size();
	}
}
