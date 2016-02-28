package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestDateTimeParser {
	
	@Test
	public void testCleanup1() {
		String testString = "  checking   random text  123   321       ";
		String output = DateTimeParser.cleanup(testString);
		String expected = "checking random text 123 321";
		assertEquals(expected, output);
	}
	
	@Test
	public void testCleanup2() {
		String testString = "textwithoutspace           ";
		String output = DateTimeParser.cleanup(testString);
		String expected = "textwithoutspace";
		assertEquals(expected, output);
	}
	
	@Test
	public void testCleanup3() {
		String testString = "           ";
		String output = DateTimeParser.cleanup(testString);
		String expected = "";
		assertEquals(expected, output);
	}
	

}
