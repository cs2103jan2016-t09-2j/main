package Parser;

import java.time.*;

import java.util.*;
//import org.ocpsoft.prettytime.*;
import org.ocpsoft.prettytime.nlp.PrettyTimeParser;
import java.time.format.*;

public class Test {
	
	PrettyTimeParser timeParse = new PrettyTimeParser();
	
	public static void main(String[] args) {
		Test obj = new Test();
		obj.printDates1();
	}
	
	void printDates1() {
		List<Date> dateList = timeParse.parse("Meet him at 1400  on tuesday");
		System.out.println(dateList.size());
		for(Date i : dateList) {
			String s = i.toString();
			System.out.println(s);
			
			Calendar c = Calendar.getInstance();
			s = c.get(Calendar.YEAR)+"-0"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
			
			LocalDate date = LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE);
			System.out.println(date.toString());
			
			
			//s=s.substring(8,10)+" "+s.substring(4,7)+" "+s.substring(24)+" " + s.substring(11, 16);
			//System.out.println(s);
			/*DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("dd MMM uuuu");
			DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("HH:mm");
			LocalDate date = LocalDate.parse(s, DateTimeFormatter.ofLocalizedDateTime(dateformat, timeformat));
			System.out.println(date.toString());*/
		}
	}
	
	void printDates2() {
		String s = "tuesday";
		LocalDate date = LocalDate.parse(s);
		System.out.println(date.toString());
	}
}
