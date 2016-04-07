//@@author A0132778W

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import Parser.*;
import Storage.*;
import ScheduleHacks.*;
import Logic.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  LogicTest.class,
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