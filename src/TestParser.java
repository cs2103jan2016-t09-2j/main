import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TestParser {
	
	Parser testObject = new Parser();
	
	@Test
	public void testGetFirstWord() {
		String testString = "add life is great";
		String output = testObject.getFirstWord(testString);
		assertEquals(output, "add");
	}
	
	@Test
	public void testRemoveFirstWord() {
		String testString = "add life is great";
		String output = testObject.removeFirstWord(testString);
		assertEquals(output, "life is great");
	}
}
