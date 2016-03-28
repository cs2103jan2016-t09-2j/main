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
		assertEquals(true, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate2() {
		String testString = "3 june 2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate3() {

		String testString = "3 June 26";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate4() {

		String testString = "28jun16";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate5() {

		String testString = "  3 june   2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate6() {

		String testString = "3-6-2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate7() {

		String testString = "3 janua 2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate8() {

		String testString = "   3 abc 2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate9() {

		String testString = "3-8-19 2016";
		dateObj = new DateParser(testString);
		assertEquals(true, dateObj.addToListIfValidDate(testString, ""));
	}

	/*
	 * @Test public void testIsValidDate10() { String testString = "3-8 2016";
	 * assertEquals(true, dateObj.isValidDate(testString )); }
	 */

	@Test
	public void testIsValidDate10() {

		String testString = "3-8-19-13 2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate11() {

		String testString = "3-8-1-2016";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDate(testString, ""));
	}

	@Test
	public void testIsValidDate12() {

		String testString = "3mar4apr";
		dateObj = new DateParser(testString);
		assertEquals(false, dateObj.addToListIfValidDateWithoutYear(testString, ""));
	}

	String[] date1 = { "22/4/2016", "22/4/16", "22/04/16", "22/04/16", "22/04", "22-4-2016", "22-4-16", "22-04-16",
			"22-04-16", "22-04", "22April2016", "22april16", "22APR2016", "22apr16", "22 April2016", "22 april16",
			"22 APR2016", "22 apr16", "22April 2016", "22april 16", "22APR 2016", "22apr 16", "22 April 2016",
			"22 april 16", "22 APR 2016", "22 apr 16", "22Apr", "22 april", "22 APR", "22april" };

	String[] date2 = { "30/8/2016", "30/8/16", "30/08/16", "30/08/16", "30/08", "30-8-2016", "30-8-16", "30-08-16",
			"30-08-16", "30-08", "30August2016", "30august16", "30AUG2016", "30aUg16", "30 August2016", "30 august16",
			"30 AUG2016", "30 aUg16", "30August 2016", "30august 16", "30AUG 2016", "30aUg 16", "30 August 2016",
			"30 august 16", "30 AUG 2016", "30 aUg 16", "30Aug", "30 august", "30 AUG", "30august" };

	String[] keyword = { "by", "on", "in", "before", "from", "frm", "" };

	@Test
	public void testDateFormat() {
		for (int index1 = 0; index1 < date1.length; index1++) {
			for (int index2 = 0; index2 < date2.length; index2++) {
				for (int index3 = 0; index3 < keyword.length; index3++) {
					dateObj = new DateParser(
							"Date format testing " + keyword[index3] + " " + date1[index1] + " to " + date2[index2]);
					dateObj.findDates();
					String output = "";
					for (LocalDate date : dateObj.getDateList()) {
						output = output + date.toString();
					}
					assertEquals("Date format testing", dateObj.getTaskDetails());
					assertEquals("2016-04-222016-08-30", output);
				}
			}
		}
	}

	@Test
	public void testFinDates1() {
		dateObj = new DateParser("She is getting married on 3 jun 2016   ");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("She is getting married", dateObj.getTaskDetails());
		assertEquals("2016-06-03", output);
	}

	@Test
	public void testFinDates2() {
		dateObj = new DateParser("29feb16 read 100 books");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("read 100 books", dateObj.getTaskDetails());
		assertEquals("2016-02-29", output);
	}

	@Test
	public void testFinDates3() {
		dateObj = new DateParser("3 AUG to 8 march");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("", dateObj.getTaskDetails());
		assertEquals("2016-08-032017-03-08", output);
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
		assertEquals("Meet ABCD at 16:00 and at the hotel", dateObj.getTaskDetails());
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
	public void testFinDates6() {
		dateObj = new DateParser("She is getting married day after  tmr   ");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("She is getting married", dateObj.getTaskDetails());
		assertEquals(LocalDate.now().plusDays(2).toString(), output);
	}

	@Test
	public void testFinDates7() {
		dateObj = new DateParser("She is getting married 7mar8mar ");
		dateObj.findDates();
		String output = "";
		if (dateObj.getDateList() != null) {
			for (LocalDate date : dateObj.getDateList()) {
				output = output + date.toString();
			}
		}
		assertEquals("She is getting married 7mar8mar", dateObj.getTaskDetails());
		assertEquals("", output);
	}

	@Test
	public void testFinDates8() {
		dateObj = new DateParser("See day aftr $tomorrow day aftr tomorrow.");
		dateObj.findDates();
		String output = "";
		if (dateObj.getDateList() != null) {
			for (LocalDate date : dateObj.getDateList()) {
				output = output + date.toString();
			}
		}
		assertEquals("See day aftr $tomorrow .", dateObj.getTaskDetails());
		assertEquals("2016-03-30", output);
	}

	@Test
	public void testFinDates9() {
		dateObj = new DateParser("work to be done frm day aftr tmw to 3 days  ");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("work to be done", dateObj.getTaskDetails());
		assertEquals("2016-03-302016-03-31", output);
	}

	@Test
	public void testFinDates10() {
		dateObj = new DateParser("She is getting married in 3 days  ");
		dateObj.findDates();
		String output = "";
		for (LocalDate date : dateObj.getDateList()) {
			output = output + date.toString();
		}
		assertEquals("She is getting married", dateObj.getTaskDetails());
		assertEquals(LocalDate.now().plusDays(3).toString(), output);
	}

	@Test
	public void testFinDates11() {
		String output = "";
		String testString = "to 3march 8/3/19";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		if (outList != null) {
			for (LocalDate s : outList) {
				output = output + s.toString();
			}
		}
		String expected = "2017-03-032019-03-08";
		assertEquals(expected, output.trim());
		assertEquals("to", dateObj.getTaskDetails());
	}

	@Test
	public void testFinDates12() {
		String output = "";
		String testString = "3march 8/3/19 to";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		if (outList != null) {
			for (LocalDate s : outList) {
				output = output + s.toString();
			}
		}
		String expected = "2017-03-032019-03-08";
		assertEquals(expected, output.trim());
		assertEquals("to", dateObj.getTaskDetails());
	}

	@Test
	public void testFinDates13() {
		String output = "";
		String testString = "to 3march 8/3/19";
		dateObj = new DateParser(testString);
		dateObj.findDates();
		ArrayList<LocalDate> outList = dateObj.getDateList();
		if (outList != null) {
			for (LocalDate s : outList) {
				output = output + s.toString();
			}
		}
		String expected = "2017-03-032019-03-08";
		assertEquals(expected, output.trim());
		assertEquals("to", dateObj.getTaskDetails());
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
		String expected = "2016-03-30";
		assertEquals(expected, output.trim());
		assertEquals("Meet ABCD at the hotel", dateObj.getTaskDetails());
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

	@Test
	public void checkGetUpComingDate() {
		String text = "day after tmw it is";
		dateObj = new DateParser(text);
		String word = dateObj.getUpComingDayWord(text);

		LocalDate date = dateObj.getUpcomingDayDate(word);
		assertEquals(LocalDate.now().plusDays(2), date);
	}

	@Test
	public void checkHasDayDuration() {
		dateObj = new DateParser();
		String text = "3days";
		assertEquals(true, dateObj.hasDayDuration(text));
	}

	@Test
	public void checkGetFirstXWords() {
		String text = "Hello it's me. I was wondering if after all these years";

		dateObj = new DateParser();
		assertEquals(null, dateObj.getFirstXWords(text, 0));
		assertEquals("Hello", dateObj.getFirstXWords(text, 1));
		assertEquals("Hello it's me.", dateObj.getFirstXWords(text, 3));
		assertEquals(null, dateObj.getFirstXWords(text, 18));
	}

}
