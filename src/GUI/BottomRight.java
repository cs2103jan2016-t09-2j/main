package GUI;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import Logic.Logic;

public class BottomRight extends JPanel implements KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logic logicObj = Logic.getInstance();
	
	int count = 0;

	JButton button1 = new JButton("TRIVIAL TASKS");
	JButton button2 = new JButton("UPCOMING TASKS");
	JButton button3 = new JButton("ARCHIVE");
	JButton button4 = new JButton("HOME");
	JButton button5 = new JButton("HELP");
	
	JButton1 b1;
	JButton2 b2;
	JButton3 b3;
	JButton4 b4;
	JButton5 b5;
	
	public BottomRight(){
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/7.2);
		setPreferredSize(size);
		setBorder(null);
		setLayout(new GridBagLayout());
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		
		b1 = new JButton1();
		b2 = new JButton2();
		b3 = new JButton3();
		b4 = new JButton4();
		b5 = new JButton5();
		
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;

		gc.gridy = 0;
		add(b1,gc);
		gc.gridy = 1;
		add(b2,gc);
		gc.gridy = 2;
		add(b3,gc);
		gc.gridy = 3;
		add(b4,gc);
		gc.gridy = 4;
		add(b5,gc);

/*		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;

		gc.gridy = 0;
		add(button1,gc);
		button1.addKeyListener(this);
		gc.gridy = 1;
		add(button2,gc);
		button2.addKeyListener(this);
		gc.gridy = 2;
		add(button3,gc);
		button3.addKeyListener(this);
		gc.gridy = 3;
		add(button4,gc);
		button4.addKeyListener(this);
		gc.gridy = 4;
		add(button5,gc);
		button5.addKeyListener(this); */
	}

	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode == KeyEvent.VK_TAB){
			count++;
			System.out.println("count is" + count);
		}
		if(keyCode == KeyEvent.VK_ENTER){
			logicObj.executeCommand("help");
		}
		
	}

	public void keyReleased(KeyEvent e) {
		
	}

	public void keyTyped(KeyEvent e) {
		
	}
}
