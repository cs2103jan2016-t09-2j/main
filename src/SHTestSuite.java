import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import Parser.*;
import Storage.*;
import ScheduleHacks.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  CommandParserTest.class,
  DateParserTest.class,
  TimeParserTest.class,
  IndexParserTest.class,
  DateTimeParserTest.class,
  HistoryTest.class,
  StorageTest.class
})
public class SHTestSuite {   
}  		