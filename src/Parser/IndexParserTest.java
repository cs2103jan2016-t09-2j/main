//@@author A0132778W

package Parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class IndexParserTest {

	IndexParser indexObj;

	@Test
	public void testFindIndex1() throws Exception {
		Command cmd = new Command("del", null);
		String testString = "1, 3 , 5";
		String output = "";
		indexObj = new IndexParser(cmd, testString);
		indexObj.findIndexList();
		for (int x : indexObj.getIndexList()) {
			output += x;
		}
		assertEquals("135", output);
	}

	@Test
	public void testFindIndex2() throws Exception {
		Command cmd = new Command("d", null);
		String testString = "1, 3 TO 5";
		String output = "";
		indexObj = new IndexParser(cmd, testString);
		indexObj.findIndexList();
		for (int x : indexObj.getIndexList()) {
			output += x;
		}
		assertEquals("1345", output);
	}

	@Test
	public void testFindIndex3() throws Exception {
		Command cmd = new Command("done", null);
		String testString = "1, 3 , 5";
		String output = "";
		indexObj = new IndexParser(cmd, testString);
		indexObj.findIndexList();
		for (int x : indexObj.getIndexList()) {
			output += x;
		}
		assertEquals("135", output);
	}

	@Test
	public void testFindIndex4() throws Exception {
		Command cmd = new Command("del", null);
		String testString = "2, 1-3";
		String output = "";
		indexObj = new IndexParser(cmd, testString);
		indexObj.findIndexList();
		for (int x : indexObj.getIndexList()) {
			output += x;
		}
		assertEquals("123", output);
	}

	@Test
	public void testFindIndex5() throws Exception {
		Command cmd = new Command("e", null);
		String testString = "60 23 sep 16";
		String output = "";
		indexObj = new IndexParser(cmd, testString);
		indexObj.findIndexList();
		for (int x : indexObj.getIndexList()) {
			output += x;
		}
		assertEquals("60", output);
		assertEquals("23 sep 16", indexObj.getTaskDetails());
	}

	@Test
	public void testFindIndex6(){
		boolean isException = true;
		try {
			Command cmd = new Command("delete", null);
			String testString = "error";
			indexObj = new IndexParser(cmd, testString);
			indexObj.findIndexList();
			isException = false;
		} catch (Exception e) {
			assertEquals(true, isException);
		}
		assertEquals(true, isException);
	}
}
