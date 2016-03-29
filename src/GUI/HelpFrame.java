package GUI;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 
public class HelpFrame {
 
    public static void main(String[] args) {
 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	JFrame popFrame = new PopHelp("HELP");
            	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            	double width = screenSize.getWidth();
            	double height = screenSize.getHeight();
            	System.out.println("Width size: " + width);
            	System.out.println("Height size: " + height);
            	int width1 = (int) (width / 2);
            	int height1 = (int) (height / 1.92);
            	popFrame.setSize(width1, height1);
            	//newFrame.setSize(600, 400);
                popFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                popFrame.setVisible(true);
                popFrame.setResizable(false);
            }
        });
    }
}
