package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestParser {

	@Test
	public void testGetFirstWord() {
		String testString = "add life is great";
		String output = CommandParser.getFirstWord(testString);
		assertEquals(output, "add");
	}

	@Test
	public void testRemoveFirstWord() {
		String testString = "add life is great";
		String output = CommandParser.removeFirstWord(testString);
		assertEquals(output, "life is great");
	}
	
	@Test
	public void testCleanupExtraWhitespace1() {
		String testString = "  checking   random text  123   321       ";
		String output = CommandParser.cleanupExtraWhitespace(testString);
		String expected = "checking random text 123 321";
		assertEquals(expected, output);
	}
	
	@Test
	public void testCleanupExtraWhitespace2() {
		String testString = "textwithoutspace           ";
		String output = CommandParser.cleanupExtraWhitespace(testString);
		String expected = "textwithoutspace";
		assertEquals(expected, output);
	}
	
	@Test
	public void testCleanupExtraWhitespace3() {
		String testString = "           ";
		String output = CommandParser.cleanupExtraWhitespace(testString);
		String expected = "";
		assertEquals(expected, output);
	}
}
