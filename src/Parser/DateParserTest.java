package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

public class DateParserTest {

	DateParser dateObj;

	@Test
	public void testIsValidDate1() {
		String testString = "3/6/2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate2() {
		String testString = "3 june 2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate3() {
		
		String testString = "3 June 26";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate4() {
		
		String testString = "28 jun 16";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate5() {
		
		String testString = "  3 june   2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate6() {
		
		String testString = "3-6-2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate7() {
		
		String testString = "3 janua 2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate8() {
		
		String testString = "   3 abc 2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testIsValidDate9() {
		
		String testString = "3-8-19 2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString));
	}

	/*
	 * @Test public void testIsValidDate10() { 
	 * String testString = "3-8 2016"; assertEquals(true,
	 * dateObj.isValidDate(testString )); }
	 */
	
	@Test
	public void testIsValidDate10() {
		
		String testString = "3-8-19-13 2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString));
	}
	
	@Test
	public void testIsValidDate11() {
		
		String testString = "3-8-1-2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString));
	}

	@Test
	public void testFinDates1() {
		dateObj = new DateParser("She is getting married on 3 jun 2016   ");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("She is getting married on", dateObj.getTaskDetails());
		assertEquals("2016-06-03", output);
	}

	@Test
	public void testFinDates2() {
		dateObj = new DateParser("She is getting married on 3 june 16   ");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("She is getting married on", dateObj.getTaskDetails());
		assertEquals("2016-06-03", output);
	}

	@Test
	public void testFinDates3() {
		dateObj = new DateParser("3 AUG 16   ");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("", dateObj.getTaskDetails());
		assertEquals("2016-08-03", output);
	}

	@Test
	public void testFinDates4() {
		String output = "";
		String testString = "Meet ABCD at 16:00 on 2-1-16 and 3-1-16 at the hotel ";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		for (LocalDate s : outList) {
			output = output + s.toString();
		}
		String expected = "2016-01-022016-01-03";
		assertEquals(expected, output.trim());
		assertEquals("Meet ABCD at 16:00 on and at the hotel", dateObj.getTaskDetails());
	}

	@Test
	public void testFinDates5() {
		String output = "";
		String testString = "Meet ABCD at 16:00   at the hotel ";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		if (outList != null) {
			for (LocalDate s : outList) {
				output = output + s.toString();
			}
		}
		String expected = "";
		assertEquals(expected, output.trim());
		assertEquals("Meet ABCD at 16:00 at the hotel", dateObj.getTaskDetails());
	}

	@Test
	public void testGetUpComingDayDate1() {
		String output = "";
		String testString = "Meet ABCD tomorrow at the hotel ";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		for (LocalDate s : outList) {
			output = output + s.toString();
		}
		String expected = LocalDate.now().plusDays(1).toString();
		assertEquals(expected, output.trim());
		assertEquals("Meet ABCD at the hotel", dateObj.getTaskDetails());
	}

	@Test
	public void testGetUpComingDayDate2() {
		String output = "";
		String testString = "Meet ABCD today and tomorrow at the hotel ";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		for (LocalDate s : outList) {
			output = output + s.toString();
		}
		String expected = LocalDate.now().toString() + LocalDate.now().plusDays(1).toString();
		assertEquals(expected, output.trim());
		assertEquals("Meet ABCD and at the hotel", dateObj.getTaskDetails());
	}

	@Test
	public void testGetDayOfWeekDate1() {
		String output = "";
		String testString = "Meet ABCD on wednesday at the hotel ";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		for (LocalDate s : outList) {
			output = output + s.toString();
		}
		String expected = "2016-03-23";
		assertEquals(expected, output.trim());
		assertEquals("Meet ABCD on at the hotel", dateObj.getTaskDetails());
	}
	
	@Test
	public void testIsDayOfWeek1() {
		String testString = "wed";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.isDayOfWeek(testString));
	}
	
	@Test
	public void testIsDayOfWeek2() {
		String testString = "wednes";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.isDayOfWeek(testString));
	}
	
	@Test
	public void testIsDayOfWeek3() {
		String testString = "thuRs";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.isDayOfWeek(testString));
	}
	
	@Test
	public void testIsDayOfWeek4() {
		String testString = "th";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.isDayOfWeek(testString));
	}
	
	@Test
	public void testIsDayOfWeek5() {
		String testString = "tues";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.isDayOfWeek(testString));
	}
	
	@Test
	public void testIsDayOfWeek6() {
		String testString = "SUNDAY";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.isDayOfWeek(testString));
	}
	
	

}
