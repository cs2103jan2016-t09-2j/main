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

			ArrayList<Task> OList = new ArrayList<Task>();
			ArrayList<Task> SList = new ArrayList<Task>();
			ArrayList<Task> FList = new ArrayList<Task>();

			if (!logicObj.hasSearchList()) { //print the normal display
				OList = logicObj.getScheduledTasksOverDue();
				SList = logicObj.getScheduledTasksToDo();
				FList = logicObj.getFloatingTasksToDo();
			}
			else{ //search display
				ArrayList<Task> searchList = new ArrayList<Task>();
				searchList = logicObj.getSearchTasksList();

				for(Task task : searchList){
					if(task.isFloatingTask()){
						FList.add(task);
					}
					else if(task.isScheduledTask()){
						SList.add(task);
					}
					else{
						OList.add(task);
						System.out.println("Enter this magic circle");
					}
				}
			}

			TopLeftPanel.clearText();
			TopLeftPanel.setText(OList, SList);
			TopRightPanel.clearText();
			TopRightPanel.setText(FList);
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
}
