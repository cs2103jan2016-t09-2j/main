package GUI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 
public class Main_UI {
 
    public static void main(String[] args) {
 
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	JFrame newFrame = new newMainFrame("Schedule Hacks");
            	newFrame.setSize(600, 400);
                newFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                newFrame.setVisible(true);
                newFrame.setResizable(false);
            	
                /*JFrame frame = new MainFrame("Schedule Hacks");
                frame.setSize(1000, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setResizable(false);
                */
            }
        });
    }
}