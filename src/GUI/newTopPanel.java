package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class newTopPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static JTextArea textArea;
	public newTopPanel(){
		Dimension size = getPreferredSize();
		size.height = 270;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));
		
		setLayout(new GridLayout());
		textArea = new JTextArea();
		textArea.setEditable(false);
		add(textArea);
	}

	public static void setText(String string) {
		textArea.append(string + "\n");
		textArea.append("Bye");
	}
	
	public static <E> void setText(ArrayList<E> OList, ArrayList<E> SList,ArrayList<E> FList){
		printOut(OList);
		printOut(SList);
		printOut(FList);
	}
	
	public static <E> void printOut(ArrayList <E> List){
		for(int i = 0; i <List.size(); i++){
			String string = (String) List.get(i);
			textArea.append(string + "\n");
		}
	}
}
