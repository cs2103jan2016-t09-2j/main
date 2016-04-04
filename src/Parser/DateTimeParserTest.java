//@@author A0132778W

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
		ArrayList<LocalDate> dates = addDates1();
		ArrayList<LocalTime> times = addTimes1();
		DateTimeParser obj = new DateTimeParser(dates,times);
		obj.arrangeDateTimeList();
		for(LocalDate date : obj.getDateList()) {
			d = d + date.toString();
		}
		for(LocalTime time : obj.getTimeList()) {
			t = t + time.toString();
		}
		assertEquals("2007-03-132007-03-232008-03-13",d);
		assertEquals("10:3000:3020:30",t);
	}
	
	public ArrayList<LocalDate> addDates1(){
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		dates.add(LocalDate.parse("2007-03-13"));
		dates.add(LocalDate.parse("2007-03-23"));
		dates.add(LocalDate.parse("2008-03-13"));
		return dates;
	}
	
	public ArrayList<LocalTime> addTimes1(){
		ArrayList<LocalTime> times = new ArrayList<LocalTime>();
		times.add(LocalTime.parse("10:30"));
		times.add(LocalTime.parse("00:30"));
		times.add(LocalTime.parse("20:30"));
		return times;
	}
	

	@Test
	public void testGetDateList2(){
		String d = "", t ="";
		ArrayList<LocalDate> dates = addDates2();
		ArrayList<LocalTime> times = addTimes2();
		DateTimeParser obj = new DateTimeParser(dates,times);
		obj.arrangeDateTimeList();
		for(LocalDate date : obj.getDateList()) {
			d = d + date.toString();
		}
		for(LocalTime time : obj.getTimeList()) {
			t = t + time.toString();
		}
		assertEquals("2007-03-132008-03-132008-03-13",d);
		assertEquals("10:3000:3020:30",t);
	}
	
	public ArrayList<LocalDate> addDates2(){
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		dates.add(LocalDate.parse("2007-03-13"));
		dates.add(LocalDate.parse("2008-03-13"));
		return dates;
	}
	
	public ArrayList<LocalTime> addTimes2(){
		ArrayList<LocalTime> times = new ArrayList<LocalTime>();
		times.add(LocalTime.parse("10:30"));
		times.add(LocalTime.parse("00:30"));
		times.add(LocalTime.parse("20:30"));
		return times;
	}
	
	@Test
	public void testGetDateList3(){
		String d = "", t ="";
		ArrayList<LocalDate> dates = addDates3();
		ArrayList<LocalTime> times = addTimes3();
		DateTimeParser obj = new DateTimeParser(dates,times);
		obj.arrangeDateTimeList();
		for(LocalDate date : obj.getDateList()) {
			d = d + date.toString();
		}
		for(LocalTime time : obj.getTimeList()) {
			t = t + time.toString();
		}
		assertEquals("2007-03-132008-03-132008-03-14",d);
		assertEquals("10:3020:3000:30",t);
	}
	
	public ArrayList<LocalDate> addDates3(){
		ArrayList<LocalDate> dates = new ArrayList<LocalDate>();
		dates.add(LocalDate.parse("2007-03-13"));
		dates.add(LocalDate.parse("2008-03-13"));
		return dates;
	}
	
	public ArrayList<LocalTime> addTimes3(){
		ArrayList<LocalTime> times = new ArrayList<LocalTime>();
		times.add(LocalTime.parse("10:30"));
		times.add(LocalTime.parse("20:30"));
		times.add(LocalTime.parse("00:30"));
		return times;
	}
}
