package Parser;
import java.util.*;
//import org.ocpsoft.prettytime.*;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;

public class Test {
	
	PrettyTimeParser timeParse = new PrettyTimeParser();
	
	public static void main(String[] args) {
		Test obj = new Test();
		obj.printDates();
	}
	
	void printDates() {
		
		List<Date> dateList = timeParse.parse("Meet him at 1400 after two weeks");
		
		for(Date i : dateList) {
			System.out.println(i.toString());
		}
	}
}
