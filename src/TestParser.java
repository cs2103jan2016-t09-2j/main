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
}
