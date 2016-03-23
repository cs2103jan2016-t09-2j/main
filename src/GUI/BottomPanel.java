package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class BottomPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BottomTop bottomTop;
	private BottomBottom bottomBottom;

	private ArrayList<Object> panels;
//	private static Logic logicObj = new Logic ();
	
	public BottomPanel(){
		Dimension size = getPreferredSize();
		size.height = 100;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		bottomTop = new BottomTop();
		bottomBottom = new BottomBottom();
		bottomBottom.addDetailListener(new DetailListener(){
			public void detailEventOccurred(DetailEvent event){
				String text = event.getText();
				bottomTop.setText(text);
				actionDone(panels);
			}
		});
		
		gc.weighty = 0.9;
		gc.gridx = 0;
		gc.gridy = 0;
		add(bottomTop,gc);
		
		gc.weighty = 0.3;
		gc.gridx = 0;
		gc.gridy = 1;
		add(bottomBottom,gc);
		
	}
	
	private void actionDone(ArrayList<Object> panels){

			FloatPanel floatPanel = (FloatPanel) panels.get(0);
			floatPanel.setList();
			OverduePanel overduePanel = (OverduePanel) panels.get(1);
			overduePanel.setList();
			SchedulePanel schedulePanel = (SchedulePanel) panels.get(2);
			schedulePanel.setList();
	}

	public void setPanel(ArrayList<Object> panels) {
		this.panels = panels;
	}
	
/*	public void setLogicObject(Logic logicObj){
		this.logicObj = logicObj;
	}*/
	
}
