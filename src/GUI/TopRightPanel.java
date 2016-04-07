package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Logic.Logic;
import ScheduleHacks.Task;

public class TopRightPanel extends JPanel {

	private static Logic logicObj = Logic.getInstance();

	private static final long serialVersionUID = 1L;
	private static JTextPane textArea;
	private JScrollPane scrollPane;
	private static int count;
	private static final String FLOATING_HEADER = "TRIVIAL TASKS";

	private static StyledDocument document;

	private static final String CHECK_MARK = "\u2714 ";

	private static SimpleAttributeSet header = new SimpleAttributeSet();
	private static SimpleAttributeSet taskInfo = new SimpleAttributeSet();
	private static SimpleAttributeSet checkMark = new SimpleAttributeSet();

	private static DefaultHighlighter highlighter = new DefaultHighlighter();
	private static DefaultHighlightPainter painter = new DefaultHighlightPainter(Color.YELLOW);

	public TopRightPanel() {
		
		/*
		 * Set the size of the panel that contains the display of Trivial Task
		 */
		Dimension size = getPreferredSize();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double height = screenSize.getHeight();
		size.height = (int) (height / 2.2);
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		/*
		 * Set the layout for the component in this panel
		 */
		setLayout(new GridLayout());
		textArea = new JTextPane();
		scrollPane = new JScrollPane(textArea);
		// textArea.setLineWrap(true);
		// textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		textArea.setHighlighter(highlighter);

		StyleConstants.setFontFamily(header, "Comic Sans");
		StyleConstants.setAlignment(header, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(header, true);

		StyleConstants.setFontFamily(taskInfo, "Comic Sans");
		StyleConstants.setFontSize(taskInfo, 13);
		StyleConstants.setLineSpacing(taskInfo, (float) 0.4);

		StyleConstants.setFontFamily(checkMark, "Comic Sans");
		StyleConstants.setFontSize(checkMark, 16);
		StyleConstants.setBold(checkMark, true);
		StyleConstants.setForeground(checkMark, new Color(0, 153, 0));
		
		add(scrollPane);
		logicObj.firstRun();
	}

	/*
	 * Set normal display screen
	 */
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

	/*
	 * Set search display screen
	 */
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
			int count = 0;
			document = textArea.getStyledDocument();
			document.insertString(document.getLength(), "Search Results (" + indexList.size() + " results)\n\n",
					header);
			document.setParagraphAttributes(0, document.getLength(), header, false);
			int end = document.getLength();
			for (Task task : List) {
				String string = task.getDescription();
				document.insertString(document.getLength(), indexList.get(count) + ". ", taskInfo);
				if (task.isComplete()) {
					document.insertString(document.getLength(), CHECK_MARK, checkMark);
				}
				document.insertString(document.getLength(), string + "\n", taskInfo);
				count++;
			}
			document.setParagraphAttributes(end, document.getLength(), taskInfo, true);
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	public static void printOut(ArrayList<Task> List, ArrayList<Integer> indexList) {
		try {
			highlighter.removeAllHighlights();
			int indexToHighlight = logicObj.getRecentIndexOfTask();
			int count = 0, positionToScroll = -1;
			document = textArea.getStyledDocument();
			document.insertString(document.getLength(), FLOATING_HEADER + "\n\n", header);
			document.setParagraphAttributes(0, document.getLength(), header, true);
			int end = document.getLength();
			if (List != null) {
				for (Task task : List) {
					int startPos = document.getLength();
					String string = task.getDescription();
					document.insertString(document.getLength(), indexList.get(count) + ". ", taskInfo);
					if (task.isComplete()) {
						document.insertString(document.getLength(), CHECK_MARK, checkMark);
					}
					document.insertString(document.getLength(), string + "\n", taskInfo);
					if (logicObj.isHighlightOperation() && indexList.get(count) == indexToHighlight) {
						highlighter.addHighlight(startPos, document.getLength(), painter);
						positionToScroll = document.getLength();
					}
					count++;
				}
			}
			document.setParagraphAttributes(end, document.getLength(), taskInfo, true);
			textArea.setStyledDocument(document);
			if (positionToScroll >= 0) {
				textArea.setCaretPosition(positionToScroll);
			}
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	/*
	 * Clear all the content in display screen
	 */
	public static void clearText() {
		textArea.setText(null);
	}

	/*
	 * Set first display screen when the program start
	 */
	public static void firstSet(ArrayList<Task> firstList, ArrayList<Integer> indexList) {
		clearText();
		count = 0;
		document = textArea.getStyledDocument();
		try {
			document.insertString(document.getLength(), FLOATING_HEADER + "\n\n", header);
			document.setParagraphAttributes(0, document.getLength(), header, false);
			int end = document.getLength();
			if (firstList != null) {
				for (Task task : firstList) {
					String string = task.getDescription();
					document.insertString(document.getLength(), indexList.get(count) + ". ", taskInfo);
					document.insertString(document.getLength(), string + "\n", taskInfo);
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
