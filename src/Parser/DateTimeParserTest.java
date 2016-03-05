package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DateTimeParserTest {

	@Test
	public void testGetDateList1(){
		String d = "", t ="";
		ArrayList<LocalDate> dates = addDates();
		ArrayList<LocalTime> times = addTimes();
		DateTimeParser obj = new DateTimeParser(dates,times);
		obj.arrangeDateTimeList();
		for(LocalDate date : obj.getDateList()) {
			d = d + date.toString();
		}
		for(LocalTime time : obj.getTimeList()) {
			t = t + time.toString();
		}
		assertEquals("2001-03-132007-03-132016-03-06",d);
		assertEquals("00:3010:3020:30",t);
	}
	
	public ArrayList<LocalDate> addDates(){
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		dates.add(LocalDate.parse("2007-03-13"));
		//dates.add(LocalDate.parse("2007-03-23"));
		dates.add(LocalDate.parse("2001-03-13"));
		return dates;
	}
	
	public ArrayList<LocalTime> addTimes(){
		ArrayList<LocalTime> times = new ArrayList<LocalTime>();
		times.add(LocalTime.parse("10:30"));
		times.add(LocalTime.parse("00:30"));
		times.add(LocalTime.parse("20:30"));
		return times;
	}
}
