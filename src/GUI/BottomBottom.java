package GUI;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;
import Logic.Logic;
import ScheduleHacks.Task;

public class BottomBottom extends JPanel implements KeyListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField commandField;
	private ArrayList<Task> OList = new ArrayList<Task>();
	private ArrayList<Task> SList = new ArrayList<Task>();
	private ArrayList<Task> FList = new ArrayList<Task>();
	private static ArrayList<Task> searchOList = new ArrayList<Task>();
	private static ArrayList<Task> searchSList = new ArrayList<Task>();
	private static ArrayList<Task> searchFList = new ArrayList<Task>();
	
	public BottomBottom(){
		setLayout(new GridLayout(1,1));
		commandField = new JTextField();	
		add(commandField);
		commandField.addKeyListener(this);
	}
	
	public void keyReleased(KeyEvent arg0) {
		//not used
	}

	public void keyTyped(KeyEvent arg0) {
		//not used
	}
	
    public void keyPressed(KeyEvent e){
    	int keyCode = e.getKeyCode();
    	
    	if(keyCode == KeyEvent.VK_ENTER){
    		String input = commandField.getText();
    		commandField.setText("");
 
    		Logic logicObj = Logic.getInstance();
    		logicObj.executeCommand(input);
			System.out.println(input);
			BottomPanel.setFeedback(logicObj.getFeedBack());
			
/*			if(input.equalsIgnoreCase("Help")){
				HelpFrame helpFrame = new HelpFrame();
			}*/

			if (!logicObj.hasSearchList()) { //print the normal display
				System.out.println("Printing normal");
				OList = logicObj.getScheduledTasksOverDue();
				SList = logicObj.getScheduledTasksToDo();
				FList = logicObj.getFloatingTasksToDo();
				TopLeftPanel.clearText();
				TopLeftPanel.setText(OList, SList);
				TopRightPanel.clearText();
				TopRightPanel.setText(FList);
			}
    	}
    	
    	if(keyCode == KeyEvent.VK_UP){
    		BottomPanel.setFeedback("You have pressed the up arrow");
    	}
    	
    	if(keyCode == KeyEvent.VK_DOWN){
    		BottomPanel.setFeedback("You have pressed the down arrow");
    	}
    	
    	if(keyCode == KeyEvent.VK_ESCAPE){
    		System.exit(0);
    	}
    }
    
    public static void setSearchResult(ArrayList<Task> searchResult){
    	clearArrayList();
    	for(Task task : searchResult){
			if(task.isFloatingTask()){
				searchFList.add(task);
			}
			else if(task.isScheduledTask()){
				searchSList.add(task);
			}
			else{
				searchOList.add(task);
			}
    	}
    	setPanel(searchFList,searchSList,searchOList);
    }
    
    public static void clearPanel(){
    	TopLeftPanel.clearText();
    	TopRightPanel.clearText();
    }
    
    public static void setPanel(ArrayList<Task> searchFList, ArrayList<Task> searchSList, ArrayList<Task> searchOList){
    	clearPanel();
    	TopLeftPanel.setText(searchOList, searchSList);
		TopRightPanel.setText(searchFList);
    }
    
    public static void clearArrayList(){
    	searchFList.clear();
    	searchOList.clear();
    	searchSList.clear();
    }
}
