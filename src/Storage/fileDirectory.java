package Storage;

import java.io.File;

public class fileDirectory {
	public static void main(String args[]){
		
		createMainDirectory();
		createArchiveSubDirectory();
		createOverduedSubDirectory();
	}
	
	public static void createMainDirectory(){
		String pathName = "C:\\ScheduleHacksFiles";
		
		File folder  = new File(pathName);
		
		try{
		if(!folder.exists()){
			folder.mkdir();
			System.out.println("Main directory created");
		}
		else{
			System.out.println("Main directory already exists");
		}
		}
		catch(Exception E){
		}
		
	}
	
	public static void createArchiveSubDirectory(){
		String pathName = "C:\\ScheduleHacksFiles\\Archive";
		
		File folder  = new File(pathName);
		
		try{
		if(!folder.exists()){
			folder.mkdir();
			System.out.println("Archive Subdirectory created");
		}
		else{
			System.out.println("Archive subdirectory already exists");
		}
		}
		catch(Exception E){
		}
	}
		public static void createToDoSubDirectory(){
			String pathName = "C:\\ScheduleHacksFiles\\ToDo";
			
			File folder  = new File(pathName);
			
			try{
			if(!folder.exists()){
				folder.mkdir();
				System.out.println("ToDo Subdirectory created");
			}
			else{
				System.out.println("ToDo subdirectory already exists");
			}
			}
			catch(Exception E){
			}
		}
			public static void createOverduedSubDirectory(){
				String pathName = "C:\\ScheduleHacksFiles\\Overdued";
				
				File folder  = new File(pathName);
				
				try{
				if(!folder.exists()){
					folder.mkdir();
					System.out.println("Overdued subirectory created");
				}
				else{
					System.out.println("Overdued subirectory already exists");
				}
				}
				catch(Exception E){
				}
	
}
}
