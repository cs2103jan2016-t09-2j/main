//@@author A0125258E
package Storage;

import java.io.File;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;

import ScheduleHacks.LogFile;

public class fileDirectory {

	static LogFile myLogger = new LogFile();

	private static final String LOG_SET_FILE_DIRECTORY = "File directory created";
	private static final String LOG_DIRECTORY_EXISTS = "File directory already exists";
	private static final String LOG_DIRECTORY_ERROR = "File directory can't be created";
	
	
	
	/**
	 * Sets the current directory where the Json files which contains the data are stored.
	 * 
	 * @param The current directory in which the Json files are saved.
	 * @return	void
	 */
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
			myLogger.log(Level.INFO, LOG_DIRECTORY_ERROR);
		}
	}
	
	
	/**
	 * Moves the Json files from the current directory to the new directory specified.
	 * 
	 * @param The new directory in which the Json files are to be saved.
	 * @return	void
	 */
	public static void changeDirectory(String src, String destDir) {
		try {
			File oldFolder = new File(src);
			File newFolder = new File(destDir);

			FileUtils.moveDirectory(oldFolder, newFolder);

		}

		catch (Exception e) {
			myLogger.log(Level.INFO, LOG_DIRECTORY_ERROR);
		}
	}
}
