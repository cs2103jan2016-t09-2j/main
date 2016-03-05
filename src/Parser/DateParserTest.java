package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

public class DateParserTest {
	
	DateParser ob = new DateParser();
	
	@Test
	public void testGetDateList1(){
		String output = "";
		String testString = "Meet ABCD at 16:00 on 14/05/1234 ";
		ArrayList<String> outList = ob.getDateList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "14/05/1234";
		assertEquals(expected, output.trim());
	}
	
	@Test
	public void testGetDateList2(){
		String output = "";
		String testString = "Meet ABCD at 16:00 on 14/5/34 ";
		ArrayList<String> outList = ob.getDateList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "14/5/34";
		assertEquals(expected, output.trim());
	}
	
	@Test
	public void testGetDateList3(){
		String output = "";
		String testString = "Meet ABCD at 16:00 on 2-1-16 and 3-1-16 ";
		ArrayList<String> outList = ob.getDateList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "2-1-16  3-1-16";
		assertEquals(expected, output.trim());
	}
	
	@Test
	public void testGetDateList4(){
		String output = "";
		String testString = "Meet ABCD at 16:00 on 14/25";
		ArrayList<String> outList = ob.getDateList(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "";
		assertEquals(expected, output);
	}
	
	@Test
	public void testGetLocalDateObject1() {
		String testString = "12/02/2016";
		LocalDate dateObject = ob.getLocalDateObject(testString);
		String output = dateObject.toString();
		String expected = "2016-02-12";
		assertEquals(expected, output);
	}
	
	@Test
	public void testGetLocalDateObject2() {
		String testString = "1/2/2016";
		LocalDate dateObject = ob.getLocalDateObject(testString);
		String output = dateObject.toString();
		String expected = "2016-02-01";
		assertEquals(expected, output);
	}
	
	@Test
	public void testGetLocalDateObject3() {
		String testString = "1/2/12";
		LocalDate dateObject = ob.getLocalDateObject(testString);
		String output = dateObject.toString();
		String expected = "2112-02-01";
		assertEquals(expected, output);
	}
	
	@Test
	public void testGetLocalDateObject4() {
		String testString = "1/2/17";
		LocalDate dateObject = ob.getLocalDateObject(testString);
		String output = dateObject.toString();
		String expected = "2017-02-01";
		assertEquals(expected, output);
	}
}
