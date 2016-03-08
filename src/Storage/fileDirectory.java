package Storage;

import java.io.File;

public class fileDirectory {
	public static void main(String args[]){
		
		createDirectory();
		
	}
	
	public static void createDirectory(){
		String pathName = "C:\\ScheduleHacksFiles";
		
		File folder  = new File(pathName);
		
		try{
		if(!folder.exists()){
			folder.mkdir();
			System.out.println("Directory created");
		}
		else{
			System.out.println("Directory already exists");
		}
		}
		catch(Exception E){
		}
		
	}
}
