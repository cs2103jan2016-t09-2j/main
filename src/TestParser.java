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
	public void testFloatingTask1() {
		String testString = "add life is great";
		assertEquals(true, CommandParser.isFloatingTask(testString));
	}
	
	@Test
	public void testFloatingTask2() {
		String testString = "add Meet Robin Hood at 4pm on thurs";
		assertEquals(false, CommandParser.isFloatingTask(testString));
	}
	
	@Test
	public void testFloatingTask3() {
		String testString = "add Meet Robin Hood thurs";
		assertEquals(false, CommandParser.isFloatingTask(testString));
	}
}
