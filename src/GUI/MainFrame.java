package GUI;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.ArrayList;
import javax.swing.JFrame;

//import ScheduleHacks.Task;
//import Logic.Logic;


public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SchedulePanel schedulePanel;
	private FloatPanel floatPanel;
	private OverduePanel overduePanel;
	private BottomPanel bottomPanel;
	
//	private static Logic logicObj = new Logic ();

	public MainFrame(String title) {
		super(title);

		setLayout(new BorderLayout());

		schedulePanel = new SchedulePanel();
		floatPanel = new FloatPanel();
		overduePanel = new OverduePanel();
		bottomPanel = new BottomPanel();

		Container c = getContentPane();

		c.add(floatPanel, BorderLayout.EAST);
		c.add(schedulePanel, BorderLayout.CENTER);
		c.add(overduePanel, BorderLayout.WEST);
		c.add(bottomPanel, BorderLayout.SOUTH);
		
		ArrayList<Object> panels = new ArrayList<>();
		panels.add(floatPanel);
		panels.add(overduePanel);
		panels.add(schedulePanel);
		bottomPanel.setPanel(panels);
		
//		bottomPanel.setLogicObject(logicObj);
	}
}
