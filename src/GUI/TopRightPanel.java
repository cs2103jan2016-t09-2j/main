package GUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Logic.Logic;
import ScheduleHacks.Task;

public class TopRightPanel extends JPanel {

	/**
	 * 
	 */
	private static Logic logicObj = Logic.getInstance();

	private static final long serialVersionUID = 1L;
	private static JTextPane textArea;
	private JScrollPane scrollPane;
	private static int count;
	private static String FLOATING_HEADER = "TRIVIAL TASKS";

	private static StyledDocument document;

	private static SimpleAttributeSet header = new SimpleAttributeSet();
	private static SimpleAttributeSet taskInfo = new SimpleAttributeSet();

	public TopRightPanel() {
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int)(height/2.2);
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridLayout());
		textArea = new JTextPane();
		scrollPane = new JScrollPane(textArea);
		// textArea.setLineWrap(true);
		// textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
	
		StyleConstants.setFontFamily(header, "Comic Sans");
		StyleConstants.setAlignment(header, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(header, true);
		
		StyleConstants.setFontFamily(taskInfo, "Comic Sans");
		StyleConstants.setFontSize(taskInfo, 13);
		StyleConstants.setLineSpacing(taskInfo, (float) 0.4);
		
		add(scrollPane);
		logicObj.firstRun();
	}

	public static void setText(ArrayList<Task> FList, ArrayList<Integer> indexList, int UpcomingTaskSize) {
		if (FList == null) {
			FList = new ArrayList<Task>();
		}
		if (indexList == null || indexList.isEmpty()) {
			indexList = new ArrayList<Integer>();
			for (int index = 1; index <= FList.size(); index++) {
				indexList.add(index + UpcomingTaskSize);
			}
		}
		printOut(FList, indexList);
	}

	public static void setSearchText(ArrayList<Task> FList, ArrayList<Integer> indexList, int UpcomingTaskSize) {
		if (FList == null) {
			FList = new ArrayList<Task>();
		}
		if (indexList == null || indexList.isEmpty()) {
			indexList = new ArrayList<Integer>();
			for (int index = 1; index <= FList.size(); index++) {
				indexList.add(index + UpcomingTaskSize);
			}
		}
		printSearchQuery(FList, indexList);
	}

	public static void printSearchQuery(ArrayList<Task> List, ArrayList<Integer> indexList) {
		try {
			document = textArea.getStyledDocument();
			document.insertString(document.getLength(), "Search Results (" + indexList.size() + " results)\n\n",
					header);
			document.setParagraphAttributes(0, document.getLength(), header, false);
			int end = document.getLength();
			for (Task task : List) {
				String string = task.getDescription();
				document.insertString(document.getLength(), indexList.get(count) + ". " + string + "\n", taskInfo);
				count++;
			}
			document.setParagraphAttributes(end, document.getLength(), taskInfo,true);
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	public static void printOut(ArrayList<Task> List, ArrayList<Integer> indexList) {
		try {
			int count = 0;
			document = textArea.getStyledDocument();
			document.insertString(document.getLength(), FLOATING_HEADER + "\n\n", header);
			document.setParagraphAttributes(0, document.getLength(), header, false);
			int end = document.getLength();
			if (List != null) {
				for (Task task : List) {
					String string = task.getDescription();
					document.insertString(document.getLength(), indexList.get(count) + ".",taskInfo);
					document.insertString(document.getLength(), " " + string + "\n", taskInfo);
					count++;
				}
			}
			document.setParagraphAttributes(end, document.getLength(), taskInfo,true);
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	public static void clearText() {
		textArea.setText(null);
	}

	public static void firstSet(ArrayList<Task> firstList, ArrayList<Integer> indexList) {
		clearText();
		count = 0;
		document = textArea.getStyledDocument();
		try {
			document.insertString(document.getLength(),FLOATING_HEADER + "\n\n", header);
			document.setParagraphAttributes(0, document.getLength(), header, false);
			int end = document.getLength();
			if (firstList != null) {
				for (Task task : firstList) {
					String string = task.getDescription();
					document.insertString(document.getLength(),indexList.get(count) + ". " + string + "\n", taskInfo);
					count++;
				}
			}
			document.setParagraphAttributes(end, document.getLength(), taskInfo, true);
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}
}
