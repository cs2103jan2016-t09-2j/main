package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Logic.Logic;
import ScheduleHacks.Task;

public class newBottomPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private BottomTop bottomTop;
	private BottomBottom bottomBottom;
	String text;

	public newBottomPanel(){
		Dimension size = getPreferredSize();
		size.height = 100;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));
		Logic logicObj = Logic.getInstance();

		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		bottomTop = new BottomTop();
		bottomBottom = new BottomBottom();
		bottomBottom.addDetailListener(new DetailListener(){
			public void detailEventOccurred(DetailEvent event){
				text = event.getText();

				logicObj.executeCommand(text);
				System.out.println(text);
				bottomTop.setText(logicObj.getFeedBack());
				
				ArrayList<Task> OList = new ArrayList<Task>();
				ArrayList<Task> SList = new ArrayList<Task>();
				ArrayList<Task> FList = new ArrayList<Task>();
				OList = logicObj.getScheduledTasksOverDue();
				SList = logicObj.getScheduledTasksToDo();
				FList = logicObj.getFloatingTasksToDo();
				newTopLeftPanel.clearText();
				newTopLeftPanel.setText(OList, SList);
				newTopRightPanel.clearText();
				newTopRightPanel.setText(FList);

			}
		});

		gc.fill = GridBagConstraints.HORIZONTAL;

		gc.weighty = 0.9;
		gc.weightx = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		add(bottomTop,gc);

		gc.weighty = 0.3;
		gc.gridx = 0;
		gc.gridy = 1;
		add(bottomBottom,gc);

	}

	public String getText(){
		System.out.println(text);
		return text;
	}

	public void setFeedback(String feedBack) {
		bottomTop.setText(text);
	}
}

