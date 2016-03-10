package Storage;

import java.io.File;

public class fileDirectory {

	public static void main(String args[]) {

		

	}

	public static void createMainDirectory(String pathName) {

		File folder = new File(pathName);

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
}
