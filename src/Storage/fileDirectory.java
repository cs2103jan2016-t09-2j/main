package Storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;
import org.apache.commons.io.FileUtils;

public class fileDirectory {

	private static Logger logger = Logger.getLogger("Storage");

	private static final String LOG_SETTING_FILE_DIRECTORY = "Setting file directory...";
	private static final String LOG_SET_FILE_DIRECTORY = "File directory created";
	private static final String LOG_DIRECTORY_EXISTS = "File directory already exists";
	private static final String LOG_DIRECTORY_ERROR = "File directory can't be created";

	public static void main(String args[]) {

	}

	public static void createMainDirectory(String PathName) {

		File folder = new File(PathName);

		try {
			if (!folder.exists()) {
				folder.mkdir();
				logger.log(Level.INFO, LOG_SET_FILE_DIRECTORY);
			} else {
				logger.log(Level.INFO, LOG_DIRECTORY_EXISTS);
			}
		} catch (Exception E) {
		}

	}

	public static void changeDirectory(String src, String destDir) {
		try {
			File oldFolder = new File(src);
			File newFolder = new File(destDir);

			// System.out.println("Current Working Directory: " +
			// oldFolder.getAbsolutePath());

			//Files.move(oldFolder, newFolder, StandardCopyOption.ATOMIC_MOVE);
			FileUtils.moveDirectory(oldFolder, newFolder);

			// System.out.println("Current Working Directory: " +
			// newFolder.getAbsolutePath());

		}

		catch (Exception e) {
			// logger.log(Level.INFO,LOG_DIRECTORY_ERROR);
			System.out.println(e.getMessage());
		}
	}
}
