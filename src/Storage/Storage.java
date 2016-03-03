//package Storage;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.List;
//import java.io.FileReader;
//import java.io.FileWriter;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.google.gson.GsonBuilder;
//
//import java.util.logging.Logger;
//import java.util.logging.Level;
//
//import ScheduleHacks.Task;
//
//public class Storage {
//	
//	//constants
//	private static final String CURRENT_FILE = "current.txt";
//    private static final String BACKUP_FILE = "backup.txt";
//    private static final Gson gson = new GsonBuilder().registerTypeAdapter(Task.class, new TaskStreaming<Task>()).create();
//    
//    private File currentFile;
//    private File backUpFile;
//    private BufferedReader reader;
//    private FileWriter writer;
//   
//    private static Logger logger = Logger.getLogger("Storage");
//    
//    private static Storage taskSystem;
//    
//    private static final String LOG_WRITE_SUCCESSFUL = "Successfully written on json";
//	private static final String LOG_WRITE_UNSUCCESSFUL = "Could not write on json";
//	private static final String LOG_WRITING_TO_FILE = "Writing to file";
//	
//	private static final String LOG_CREATED_FILE = "is created";
//	private static final String LOG_ENCODING_TASKLIST = "Encoding taskList into file";
//	private static final String LOG_DECODING_FILE = "Decoding taskList from file";
//	private static final String LOG_NO_SUCH_FILE = "%s does not exist";
//	
//	private static final String LOG_READ_SUCCESSFUL = "json File read successfully";
//	private static final String LOG_READ_UNSUCCESSFUL = "json File cannot be read";
//	private static final String LOG_INPUT_OUTPUT_EXCEPTION = "Input output exception encountered";
//	private static final String LOG_READING_FROM_FILE = "Reading %s";
//	private static final String LOG_EXCEPTION = "%s was thrown";
//
//	private static final String LOG_ADDING_TASKS = "Adding loaded tasks into taskList";
//	
//	public class TaskInitializer{
//		 ArrayList<File> taskList = new ArrayList<File>();
//	}
//
//    public static Storage getInstance() {
//        if (taskSystem == null) {
//            taskSystem = new Storage();
//        }
//        return taskSystem;
//    }
//    
//	protected Storage(){
//		try {
//			currentFile = new File(CURRENT_FILE);
//			if(!currentFile.exists()){
//				currentFile.createNewFile();
//				logger.info(CURRENT_FILE+ " " + LOG_CREATED_FILE);
//			} 
//			
//			backUpFile = new File(BACKUP_FILE);
//			if(!backUpFile.exists()){
//				backUpFile.createNewFile();
//				logger.info(BACKUP_FILE+ " "+ LOG_CREATED_FILE);
//			}	
//		}
//		catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//    public void saveFile (List<Task> taskList, String filePath) {
//        assert taskList != null;
//        
//        logger.info(String.format(LOG_WRITING_TO_FILE, filePath));
//        writeListToFile(taskList, filePath);
//    }
//    
//    
//    private void writeListToFile(List<Task> taskList, String filePath){
//    	
//        FileWriter writer = new FileWriter(filePath);
//        
//		try{
//            TaskInitializer task = new TaskInitializer(taskList);
//
//            logger.info(LOG_ENCODING_TASKLIST);
//            gson.toJson(taskList, writer);                       
//        } 
//		
//        catch (IOException e) {
//            logger.warning( LOG_INPUT_OUTPUT_EXCEPTION);
//        }
//    }
//    
//
//    public void loadFile(List<Task> taskList,String filePath) {
//        assert taskList != null;
//    
//        if (!fileExists(filePath)) {
//            logger.info(String.format(LOG_NO_SUCH_FILE, filePath));
//        }
//
//        logger.info(String.format(LOG_READING_FROM_FILE, filePath));
//        readListFromFile(taskList, filePath);
//
//    }
//    
//    private void readListFromFile(List<Task> taskList,String filePath) {
//    		
//    	FileReader fr = new FileReader(filePath);
//        try{	    	
//            logger.info(String.format(LOG_DECODING_FILE, filePath));
//            TaskInitializer task = gson.fromJson(fr, TaskInitializer.class);
//
//            if (task != null && task.taskList != null ) {
//                    taskList.addAll(task.taskList);              
//            }  
//        }
//        catch (FileNotFoundException e) {
//            logger.severe(LOG_EXCEPTION);
// 
//        }
//        catch (IOException e) {
//            logger.severe(LOG_EXCEPTION);
//            
//        } 
//    }
//    private boolean fileExists(String filePath) {
//        File file = new File(filePath);
//        return file.exists();
//    }
//	
//}
