package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalTime;
import java.util.ArrayList;

public class TestTimeParser { 
	
	TimeParser obj = new TimeParser();
	
	@Test
	public void testFindTime1(){
		String output = "";
		String testString = "Meet ABCD at 16:00 on 14/05/1234";
		ArrayList<String> outList = obj.getTimeList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "16:00";
		assertEquals(expected, output.trim());
	}
	
	@Test
	public void testFindTime2(){
		String output = "";
		String testString = "Meet ABCD 16.00 ";
		ArrayList<String> outList = obj.getTimeList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "16.00";
		assertEquals(expected, output.trim());
	}
	
	@Test
	public void testFindTime3(){
		String output = "";
		String testString = "Meet ABCD at 14:00 15.00 ";
		obj = new TimeParser(testString);
		ArrayList<String> outList = obj.getTimeList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "14:0015.00";
		assertEquals(expected, output.trim());
		assertEquals("Meet ABCD at", obj.getTaskDetails());
	}
	
	@Test
	public void testFindTime4(){
		String output = "";
		String testString = "Meet ABCD 14/05/1234";
		obj = new TimeParser(testString);
		ArrayList<String> outList = obj.getTimeList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "";
		assertEquals(expected, output);
		assertEquals("Meet ABCD 14/05/1234", obj.getTaskDetails());
	}
	
	@Test
	public void testFindTime5(){
		String output = "";
		String testString = "Meet ABCD 14/05/1234 23.59 24.55";
		ArrayList<String> outList = obj.getTimeList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "23.59";
		assertEquals(expected, output);
	}
	
	@Test
	public void testFindTime6(){
		String output = "";
		String testString = "Meet ABCD 14/05/1234 23.59 24.55";
		obj = new TimeParser(testString);
		ArrayList<String> outList = obj.getTimeList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "23.59";
		assertEquals(expected, output);
		assertEquals("Meet ABCD 14/05/1234 24.55", obj.getTaskDetails());
	}
	
	@Test
	public void testGetLocalTimeObject1() {
		String testString = "12.30";
		LocalTime timeObj = obj.getLocalTimeObject(testString);
		String output = timeObj.toString();
		String expected = "12:30";
		assertEquals(expected, output);
	}
	
	@Test
	public void testGetLocalTimeObject2() {
		String testString = "2.30";
		LocalTime timeObj = obj.getLocalTimeObject(testString);
		String output = timeObj.toString();
		String expected = "02:30";
		assertEquals(expected, output);
	}
	
	@Test
	public void testGetLocalTimeObject3() {
		String testString = "1430";
		LocalTime timeObj = obj.getLocalTimeObject(testString);
		String output = timeObj.toString();
		String expected = "14:30";
		assertEquals(expected, output);
	}
	
	@Test
	public void testIsValidTime1() {
		String testString = "1460";
		TimeParser timeObj = new TimeParser();
		assertEquals(false, timeObj.isValidTime(testString));
	}
	
	@Test
	public void testIsValidTime2() {
		String testString = "2460";
		TimeParser timeObj = new TimeParser();
		assertEquals(false, timeObj.isValidTime(testString));
	}
}
