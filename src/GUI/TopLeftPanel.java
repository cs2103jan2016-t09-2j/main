//@@author A0124635J
package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Logic.Logic;

import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;

import ScheduleHacks.Task;

public class TopLeftPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static int count = 1;
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("EEE, dd MMM uuuu");
	private static JTextPane textArea;
	private JScrollPane scrollPane;
	private static final String SCHEDULE_HEADER = "UPCOMING TASKS";
	private static final String OVERDUE_HEADER = "OVERDUE TASKS";

	private static StyledDocument document;

	private static SimpleAttributeSet header = new SimpleAttributeSet();
	private static SimpleAttributeSet taskInfo = new SimpleAttributeSet();
	private static SimpleAttributeSet checkMark = new SimpleAttributeSet();
	private static SimpleAttributeSet exclaimMark = new SimpleAttributeSet();

	private static DefaultHighlighter highlighter = new DefaultHighlighter();
	private static DefaultHighlightPainter painter = new DefaultHighlightPainter(Color.YELLOW);

	private static final String CHECK_MARK = "\u2714 ";
	private static final String EXCLAMATION_MARK = "\u25cf  ";

	private static Logic logicObj = Logic.getInstance();

	public TopLeftPanel() {
		
		/*
		 * Set the size of the panel that contains the display of Overdue and Schedule Task
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
		textArea.setEditable(false);
		textArea.setHighlighter(highlighter);
		scrollPane = new JScrollPane(textArea);

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

		StyleConstants.setFontFamily(exclaimMark, "Comic Sans");
		StyleConstants.setFontSize(exclaimMark, 12);
		// StyleConstants.setBold(exclaimMark, true);
		StyleConstants.setForeground(exclaimMark, new Color(255, 0, 0));

		add(scrollPane);
		
		/*
		 * implement scroll bar (not done yet)
		 */
		JScrollBar vertical = scrollPane.getVerticalScrollBar();
		InputMap im = vertical.getInputMap(JComponent.WHEN_FOCUSED);
		im.put(KeyStroke.getKeyStroke("DOWN"), "positiveUnitIncrement");
		im.put(KeyStroke.getKeyStroke("UP"), "negativeUnitIncrement");
	}

	/*
	 * Clear all the content in display screen
	 */
	public static void clearText() {
		textArea.setText(null);
	}

	/*
	 * Set normal display screen
	 */
	public static void setText(ArrayList<Task> OList, ArrayList<Task> SList, ArrayList<Integer> indexList) {
		// TopRightPanel.setCount(count);
		int index;
		highlighter.removeAllHighlights();
		if (indexList == null || indexList.isEmpty()) {
			indexList = new ArrayList<Integer>();
			for (index = 0; index < OList.size() + SList.size(); index++) {
				indexList.add(index + 1);
			}
		}
		printOutSO(OList, "overdue", new ArrayList<Integer>(indexList.subList(0, OList.size())));
		printOutSO(SList, "schedule", new ArrayList<Integer>(indexList.subList(OList.size(), indexList.size())));
	}

	/*
	 * Set search display screen
	 */
	public static void setSearchText(ArrayList<Task> SList, ArrayList<Integer> indexList) {
		int index;
		if (SList == null) {
			SList = new ArrayList<Task>();
		}
		if (indexList == null || indexList.isEmpty()) {
			indexList = new ArrayList<Integer>();
			for (index = 0; index < SList.size(); index++) {
				indexList.add(index + 1);
			}
		}
		printSearchQuery(SList, indexList);
	}

	public static void printSearchQuery(ArrayList<Task> List, ArrayList<Integer> indexList) {
		try {
			document = textArea.getStyledDocument();
			document.insertString(document.getLength(), "Search Results (" + indexList.size() + " results)\n\n",
					header);
			document.setParagraphAttributes(0, document.getLength(), header, true);
			int end = document.getLength();
			count = 0;
			for (Task task : List) {
				// System.out.println(task.getDescription());
				String string = task.getDescription();
				document.insertString(document.getLength(), indexList.get(count) + ". ", taskInfo);
				if (task.isComplete()) {
					document.insertString(document.getLength(), CHECK_MARK, checkMark);
				} else {
					if (LocalDateTime.of(task.getEndDate(), task.getEndTime()).isBefore(LocalDateTime.now())) {
						document.insertString(document.getLength(), EXCLAMATION_MARK, exclaimMark);
					}
				}
				document.insertString(document.getLength(), string + "\n", taskInfo);
				if (task.getStartDate() != null && task.getStartTime() != null) {
					document.insertString(document.getLength(), "\t From ", taskInfo);
					if (!task.getStartTime().equals(LocalTime.MAX)) {
						document.insertString(document.getLength(), task.getStartTime().toString() + ", ", taskInfo);
					}
					document.insertString(document.getLength(), task.getStartDate().format(dateFormat) + "\n",
							taskInfo);
					document.insertString(document.getLength(), "\t To ", taskInfo);
				} else {
					document.insertString(document.getLength(), "\t By ", taskInfo);
				}
				if (!task.getEndTime().equals(LocalTime.MAX)) {
					document.insertString(document.getLength(), task.getEndTime().toString() + ", ", taskInfo);
				}
				document.insertString(document.getLength(), task.getEndDate().format(dateFormat) + "\n", taskInfo);
				count++;
			}
			document.setParagraphAttributes(end, document.getLength(), taskInfo, true);
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	public static void printOutSO(ArrayList<Task> List, String type, ArrayList<Integer> indexList) {
		int indexToHighlight = logicObj.getRecentIndexOfTask();
		// System.out.println(indexToHighlight+ "*" +
		// logicObj.isHighlightOperation());
		int count = 0;
		int end, positionToScroll = -1;
		try {
			document = textArea.getStyledDocument();
			end = document.getLength();
			if (type.equalsIgnoreCase("schedule")) {
				document.insertString(document.getLength(), SCHEDULE_HEADER + "\n\n", header);
			} else {
				document.insertString(document.getLength(), OVERDUE_HEADER + "\n\n", header);
			}
			document.setParagraphAttributes(end, document.getLength(), header, true);
			end = document.getLength();
			for (Task task : List) {
				String string = task.getDescription();
				int startPos = document.getLength();
				document.insertString(document.getLength(), indexList.get(count) + ". ", taskInfo);
				if (task.isComplete()) {
					document.insertString(document.getLength(), CHECK_MARK, checkMark);
				} else {
					if (type.equalsIgnoreCase("overdue")) {
						document.insertString(document.getLength(), EXCLAMATION_MARK, exclaimMark);
					}
				}
				document.insertString(document.getLength(), string + "\n", taskInfo);
				if (task.getStartDate() != null && task.getStartTime() != null) {
					document.insertString(document.getLength(), "\t From ", taskInfo);
					if (!task.getStartTime().equals(LocalTime.MAX)) {
						document.insertString(document.getLength(), task.getStartTime().toString() + ", ", taskInfo);
					}
					document.insertString(document.getLength(), task.getStartDate().format(dateFormat) + "\n",
							taskInfo);
					document.insertString(document.getLength(), "\t To ", taskInfo);
				} else {
					document.insertString(document.getLength(), "\t By ", taskInfo);
				}
				if (!task.getEndTime().equals(LocalTime.MAX)) {
					document.insertString(document.getLength(), task.getEndTime().toString() + ", ", taskInfo);
				}

				document.insertString(document.getLength(), task.getEndDate().format(dateFormat), taskInfo);
				int endPos = document.getLength();
				document.insertString(document.getLength(), "\n", taskInfo);
				if (logicObj.isHighlightOperation() && indexList.get(count) == indexToHighlight) {
					highlighter.addHighlight(startPos, endPos, painter);
					positionToScroll = (int) (startPos + endPos) / 2;
				}
				count++;
			}
			document.insertString(document.getLength(), "\n", taskInfo);
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
	 * Set first display screen when the program start
	 */
	public static void firstSet(ArrayList<Task> firstList, ArrayList<Integer> indexList) {
		try {
			clearText();
			document = textArea.getStyledDocument();
			LocalDate today = LocalDate.now();
			count = 0;
			int end;
			document.insertString(document.getLength(), "DUE TODAY \n", header);
			document.insertString(document.getLength(), "\n", header);
			document.setParagraphAttributes(0, document.getLength(), header, true);
			end = document.getLength();
			if (firstList != null) {
				for (Task task : firstList) {
					if (!task.getEndDate().isAfter(today)) {
						String string = task.getDescription();
						document.insertString(document.getLength(), indexList.get(count) + ". ", taskInfo);
						if (LocalDateTime.of(task.getEndDate(), task.getEndTime()).isBefore(LocalDateTime.now())) {
							document.insertString(document.getLength(), EXCLAMATION_MARK, exclaimMark);
						}
						document.insertString(document.getLength(), string + "\n", taskInfo);
						if (task.getStartDate() != null && task.getStartTime() != null) {
							document.insertString(document.getLength(), "\t From ", taskInfo);
							if (!task.getStartTime().equals(LocalTime.MAX)) {
								document.insertString(document.getLength(), task.getStartTime().toString() + ", ",
										taskInfo);
							}
							document.insertString(document.getLength(), task.getStartDate().format(dateFormat),
									taskInfo);
							document.insertString(document.getLength(), "\n", taskInfo);
							document.insertString(document.getLength(), "\t To ", taskInfo);
						} else {
							document.insertString(document.getLength(), "\t By ", taskInfo);
						}
						if (!task.getEndTime().equals(LocalTime.MAX)) {
							document.insertString(document.getLength(), task.getEndTime().toString() + ", ", taskInfo);
						}
						document.insertString(document.getLength(), task.getEndDate().format(dateFormat), taskInfo);
						document.insertString(document.getLength(), "\n", taskInfo);
						count++;
					} else {
						break;
					}
				}
			}
			document.setParagraphAttributes(end, document.getLength(), taskInfo, true);
			document.insertString(document.getLength(), "\n", taskInfo);
			end = document.getLength();

			document.insertString(document.getLength(), "DUE TOMORROW" + "\n", header);
			document.insertString(document.getLength(), "\n", header);
			document.setParagraphAttributes(end, document.getLength(), header, true);
			end = document.getLength();
			if (firstList != null) {
				for (int index = count; index < firstList.size(); index++) {
					Task task = firstList.get(index);
					String string = task.getDescription();
					document.insertString(document.getLength(), indexList.get(count) + ". ", taskInfo);
					if (task.isComplete()) {
						document.insertString(document.getLength(), CHECK_MARK, checkMark);
					}
					document.insertString(document.getLength(), string + "\n", taskInfo);
					// document.insertString(document.getLength(),
					// indexList.get(count) + ". " + string + "\n", taskInfo);
					if (task.getStartDate() != null && task.getStartTime() != null) {
						document.insertString(document.getLength(), "\t From ", taskInfo);
						if (!task.getStartTime().equals(LocalTime.MAX)) {
							document.insertString(document.getLength(), task.getStartTime().toString() + ", ",
									taskInfo);
						}
						document.insertString(document.getLength(), task.getStartDate().format(dateFormat) + "\n",
								taskInfo);
						document.insertString(document.getLength(), "\t To ", taskInfo);
					} else {
						document.insertString(document.getLength(), "\t By ", taskInfo);
					}
					if (!task.getEndTime().equals(LocalTime.MAX)) {
						document.insertString(document.getLength(), task.getEndTime().toString() + ", ", taskInfo);
					}
					document.insertString(document.getLength(), task.getEndDate().format(dateFormat), taskInfo);
					document.insertString(document.getLength(), "\n", taskInfo);
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