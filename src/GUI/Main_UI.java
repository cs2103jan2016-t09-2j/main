package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 
public class Main_UI {
 
    public static void main(String[] args) {
 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	JFrame newFrame = new MainFrame("Schedule Hacks");
            	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            	double width = screenSize.getWidth();
            	double height = screenSize.getHeight();
            	System.out.println("Width size: " + width);
            	System.out.println("Height size: " + height);
            	int width1 = (int) (width / 2);
            	int height1 = (int) (height / 1.92);
            	newFrame.setSize(width1, height1);
            	//newFrame.setSize(600, 400);
            	newFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("SHIcon.png"));
                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                newFrame.setVisible(true);
                newFrame.setResizable(false);
            }
        });
    }
}