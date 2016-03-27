package GUI;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

public class BottomBottom extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private EventListenerList listenerList = new EventListenerList();
	private JTextField commandField;
	private String command;
	
	public BottomBottom(){
		setLayout(new GridLayout(1,1));
		commandField = new JTextField();
		//commandField = new JTextField(89);	
		add(commandField);
		
		commandField.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				command = commandField.getText();
				commandField.setText("");
				fireDetailEvent(new DetailEvent(this, command));
			}
		});
	}
	
	public void fireDetailEvent(DetailEvent event) {
        Object[] listeners = listenerList.getListenerList();
        
        for(int i=0; i < listeners.length; i += 2) {
            if(listeners[i] == DetailListener.class) {
                ((DetailListener)listeners[i+1]).detailEventOccurred(event);
            }
        }
    }

    public void addDetailListener(DetailListener listener) {
        listenerList.add(DetailListener.class, listener);
    }

 /*   public void removeDetailListener(DetailListener listener) {
        listenerList.remove(DetailListener.class, listener);
    }*/
}
