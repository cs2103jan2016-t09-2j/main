package GUI;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class SchedulePanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextArea sTextArea;
	public SchedulePanel() {
        Dimension size = getPreferredSize();
        size.width = 600;
        setPreferredSize(size);
        
        setBorder(BorderFactory.createTitledBorder("Upcoming Tasks"));
        ArrayList<String> outcome = new ArrayList<String>();
        outcome.add("Max of 83 characters");
        outcome.add("bye");
        sTextArea = new JTextArea();
        sTextArea.setEditable(false);
        for(int i = 0; i < outcome.size(); i++){
        	sTextArea.append(outcome.get(i));
        	if(i+1 != outcome.size()){
        		sTextArea.append("\n");
        	}
        }
        add(sTextArea);
    }

	public void setList() {
		sTextArea.setText("hello");
		System.out.println("in schedule");
	}
}
