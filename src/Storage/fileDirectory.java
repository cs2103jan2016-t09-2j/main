package Storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;

import ScheduleHacks.LogFile;

public class fileDirectory {

	static LogFile myLogger = new LogFile();

	private static final String LOG_SET_FILE_DIRECTORY = "File directory created";
	private static final String LOG_DIRECTORY_EXISTS = "File directory already exists";
	private static final String LOG_DIRECTORY_ERROR = "File directory can't be created";

	public static void createMainDirectory(String PathName) {

		File folder = new File(PathName);

		try {
			if (!folder.exists()) {
				folder.mkdir();
				myLogger.log(Level.INFO, LOG_SET_FILE_DIRECTORY);
			} else {
				myLogger.log(Level.INFO, LOG_DIRECTORY_EXISTS);
			}
		} catch (Exception E) {
		}
	}

	public static void changeDirectory(String src, String destDir) {
		try {
			File oldFolder = new File(src);
			File newFolder = new File(destDir);

		  //Files.move(oldFolder, newFolder, StandardCopyOption.ATOMIC_MOVE);
			FileUtils.moveDirectory(oldFolder, newFolder);
	
		}

		catch (Exception e) {
			 myLogger.log(Level.INFO,LOG_DIRECTORY_ERROR);
		}
	}
}
