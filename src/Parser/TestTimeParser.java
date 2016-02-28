package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class TestTimeParser { 
	
	TimeParser obj = new TimeParser();
	
	@Test
	public void testFindTime1(){
		String output = "";
		String testString = "Meet ABCD at 16:00 on 14/05/1234";
		ArrayList<String> outList = obj.findTime(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "16:00";
		assertEquals(expected, output);
	}
	
	@Test
	public void testFindTime2(){
		String output = "";
		String testString = "Meet ABCD 16.00";
		ArrayList<String> outList = obj.findTime(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "16.00";
		assertEquals(expected, output);
	}
	
	@Test
	public void testFindTime3(){
		String output = "";
		String testString = "Meet ABCD at 14:00 15.00  ";
		ArrayList<String> outList = obj.findTime(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "14:0015.00";
		assertEquals(expected, output);
	}
	
	@Test
	public void testFindTime4(){
		String output = "";
		String testString = "Meet ABCD 14/05/1234";
		ArrayList<String> outList = obj.findTime(testString);
		for(String s : outList ) {
			output = output + s; 
		}
		String expected = "";
		assertEquals(expected, output);
	}
}
