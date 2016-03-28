package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeParserTest {

	TimeParser timeObj = new TimeParser();

	@Test
	public void testAddToListIfValidTime1() {
		String testString = "13.30 at Starbucks";
		String output = "";
		timeObj = new TimeParser(testString);
		timeObj.findTimes();
		for (LocalTime time : timeObj.getTimeList()) {
			output += time.toString();
		}
		String expected ="13:30";
		assertEquals(expected, output);
		assertEquals("at Starbucks", timeObj.getTaskDetails());
	}
	
	@Test
	public void testAddToListIfValidTime2() {
		String testString = "Meet Jim at 230pm";
		String output = "";
		timeObj = new TimeParser(testString);
		timeObj.findTimes();
		for (LocalTime time : timeObj.getTimeList()) {
			output += time.toString();
		}
		String expectedTime ="14:30";
		assertEquals(expectedTime, output);
		assertEquals("Meet Jim", timeObj.getTaskDetails());
	}
	
	@Test
	public void testAddToListIfValidTime3() {
		String testString = "Meet Jim 230pm - 330am";
		String output = "";
		timeObj = new TimeParser(testString);
		timeObj.findTimes();
		for (LocalTime time : timeObj.getTimeList()) {
			output += time.toString();
		}
		String expectedTime ="14:3003:30";
		assertEquals(expectedTime, output);
		assertEquals("Meet Jim", timeObj.getTaskDetails());
	}
	
	@Test
	public void testAddToListIfValidTime4() {
		String testString = "Go to CS2103 Tutorial 9 am";
		String output = "";
		timeObj = new TimeParser(testString);
		timeObj.findTimes();
		for (LocalTime time : timeObj.getTimeList()) {
			output += time.toString();
		}
		String expectedTime ="09:00";
		assertEquals(expectedTime, output);
		assertEquals("Go to CS2103 Tutorial", timeObj.getTaskDetails());
	}
	
	@Test
	public void testFindTime1(){
		String testString = "Meet Darwin 3 hrs";
		String output = "";
		timeObj = new TimeParser(testString);
		timeObj.findTimes();
		
		if(timeObj.getTimeList() != null) {
			for (LocalTime time : timeObj.getTimeList()) {
				output += time.toString();
			}
		}
		assertEquals(LocalTime.now().plusHours(3).truncatedTo(ChronoUnit.MINUTES).toString(), output);
		assertEquals("Meet Darwin", timeObj.getTaskDetails());
	}
}
