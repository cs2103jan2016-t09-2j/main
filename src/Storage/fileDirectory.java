package Storage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class fileDirectory {

	public static void main(String args[]) {

		

	}

	public static void createMainDirectory(String PathName) {
		
		File folder = new File(PathName);
		
		try {
			if (!folder.exists()) {
				folder.mkdir();
				System.out.println("Main directory created");
			} else {
				System.out.println("Main directory already exists");
			}
		} catch (Exception E) {
		}	
	
	}
	
	public static void changeDirectory(String src, String destDir) {
		try {
			File oldFolder = new File(src);
			File newFolder = new File(destDir);

			Files.move(oldFolder.toPath(), newFolder.toPath(), StandardCopyOption.ATOMIC_MOVE);
		}

		catch (Exception e) {
		}
	}
}
