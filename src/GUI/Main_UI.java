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
            	Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
            	double width = screensize.getWidth();
            	double height = screensize.getHeight();
            	System.out.println(width);
            	System.out.println(height);
            	newFrame.setSize(600, 400);
                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                newFrame.setVisible(true);
                newFrame.setResizable(false);
            }
        });
    }
}