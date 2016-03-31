package GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ScheduleHacks.Task;
import Logic.Logic;

public class TopLeftPanel extends JPanel {

	/**
	 * 
	 */

	private static Logic logicObj = Logic.getInstance();

	private static final long serialVersionUID = 1L;
	private static int count = 1;
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-uuuu");
	private static JTextPane textArea;
	private JScrollPane scrollPane;
	private static String SCHEDULE_HEADER = "SCHEDULED TASKS";
	private static String OVERDUE_HEADER = "OVERDUE TASKS";
	private static String CENTER_FORMAT = "\t" + "                       ";

	private static StyledDocument document;

	private static SimpleAttributeSet header = new SimpleAttributeSet();
	private static SimpleAttributeSet taskInfo = new SimpleAttributeSet();

	public TopLeftPanel() {
		Dimension size = getPreferredSize();
		size.height = 268;
		setPreferredSize(size);
		setBorder(BorderFactory.createTitledBorder(""));

		setLayout(new GridLayout());
		textArea = new JTextPane();
		scrollPane = new JScrollPane(textArea);
		textArea.setEditable(false);
		// textArea.setLineWrap(true);
		// textArea.setWrapStyleWord(true);

		StyleConstants.setFontFamily(header, "Comic Sans");
		StyleConstants.setAlignment(header, StyleConstants.ALIGN_CENTER);
		StyleConstants.setBold(header, true);

		StyleConstants.setFontFamily(taskInfo, "Comic Sans");
		StyleConstants.setFontSize(taskInfo, 13);

		add(scrollPane);
	}

	public static void clearText() {
		textArea.setText(null);
	}

	public static void setText(ArrayList<Task> OList, ArrayList<Task> SList, ArrayList<Integer> indexList) {
		// TopRightPanel.setCount(count);
		int index;
		if (indexList == null || indexList.isEmpty()) {
			indexList = new ArrayList<Integer>();
			for (index = 0; index < OList.size() + SList.size(); index++) {
				indexList.add(index + 1);
			}
		}
		printOutSO(OList, "overdue", new ArrayList<Integer>(indexList.subList(0, OList.size())));
		printOutSO(SList, "schedule", new ArrayList<Integer>(indexList.subList(OList.size(), indexList.size())));
	}

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
			document.setParagraphAttributes(0, document.getLength(), header, false);
			count = 0;
			for (Task task : List) {
				// System.out.println(task.getDescription());
				String string = task.getDescription();
				document.insertString(document.getLength(), indexList.get(count) + ". " + string + "\n", taskInfo);
				if (task.getStartDate() != null && task.getStartTime() != null) {
					document.insertString(document.getLength(), "\t From ", taskInfo);
					if (!task.getStartTime().equals(LocalTime.MAX)) {
						document.insertString(document.getLength(), task.getStartTime().toString() + ", ", taskInfo);
					}
					document.insertString(document.getLength(), task.getStartDate().format(dateFormat), taskInfo);
					document.insertString(document.getLength(), "\t To ", taskInfo);
				} else {
					document.insertString(document.getLength(), "\t At ", taskInfo);
				}
				if (!task.getEndTime().equals(LocalTime.MAX)) {
					document.insertString(document.getLength(), task.getEndTime().toString() + ", ", taskInfo);
				}
				document.insertString(document.getLength(), task.getEndDate().format(dateFormat) + "\n", taskInfo);
				count++;
			}
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	public static void printOutSO(ArrayList<Task> List, String type, ArrayList<Integer> indexList) {
		int count = 0;
		try {
			document = textArea.getStyledDocument();
			if (type.equalsIgnoreCase("schedule")) {
				document.insertString(document.getLength(), SCHEDULE_HEADER + "\n\n", header);
			} else {
				document.insertString(document.getLength(), OVERDUE_HEADER + "\n\n", header);
			}
			document.setParagraphAttributes(0, document.getLength(), header, true);
			int end = document.getLength();
			for (Task task : List) {
				String string = task.getDescription();
				document.insertString(document.getLength(), indexList.get(count) + ". " + string + "\n", taskInfo);
				if (task.getStartDate() != null && task.getStartTime() != null) {
					document.insertString(document.getLength(), "\t From ", taskInfo);
					if (!task.getStartTime().equals(LocalTime.MAX)) {
						document.insertString(document.getLength(), task.getStartTime().toString() + ", ", taskInfo);
					}
					document.insertString(document.getLength(), task.getStartDate().format(dateFormat), taskInfo);
					document.insertString(document.getLength(), "\t To ", taskInfo);
				} else {
					document.insertString(document.getLength(), "\t At ", taskInfo);
				}
				if (!task.getEndTime().equals(LocalTime.MAX)) {
					document.insertString(document.getLength(), task.getEndTime().toString() + ", ", taskInfo);
				}
				document.insertString(document.getLength(), task.getEndDate().format(dateFormat), taskInfo);
				document.insertString(document.getLength(), "\n", taskInfo);
				count++;
			}
			document.insertString(document.getLength(), "\n", taskInfo);
			document.setParagraphAttributes(end, document.getLength(), taskInfo, true);
			textArea.setStyledDocument(document);
		} catch (BadLocationException e) {
			// do nothing
		}
	}

	public static void firstSet(ArrayList<Task> firstList, ArrayList<Integer> indexList) {
		try {
			clearText();
			document = textArea.getStyledDocument();
			LocalDate today = LocalDate.now();

			count = 0;
			document.insertString(document.getLength(), "DUE TODAY" + "\n", header);
			document.insertString(document.getLength(), "\n", header);
			document.setParagraphAttributes(0, document.getLength(), header, false);
			if (firstList != null) {
				for (Task task : firstList) {
					if (!task.getEndDate().isAfter(today)) {
						String string = task.getDescription();
						document.insertString(document.getLength(), indexList.get(count) + ". " + string + "\n",
								taskInfo);
						if (task.getStartDate() != null && task.getStartTime() != null) {
							document.insertString(document.getLength(), "\t From ", taskInfo);
							if (!task.getStartTime().equals(LocalTime.MAX)) {
								document.insertString(document.getLength(), task.getStartTime().toString() + ", ",
										taskInfo);
							}
							document.insertString(document.getLength(), task.getStartDate().format(dateFormat),
									taskInfo);
							document.insertString(document.getLength(), "\t To ", taskInfo);
						} else {
							document.insertString(document.getLength(), "\t At ", taskInfo);
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
			document.insertString(document.getLength(), "\n", taskInfo);
			int end = document.getLength();

			document.insertString(document.getLength(), "DUE TOMORROW" + "\n", header);
			document.insertString(document.getLength(), "\n", header);
			document.setParagraphAttributes(end, document.getLength(), header, true);
			end  = document.getLength();
			if (firstList != null) {
				for (int index = count; index < firstList.size(); index++) {
					Task task = firstList.get(index);
					String string = task.getDescription();
					document.insertString(document.getLength(), indexList.get(count) + ". " + string + "\n", taskInfo);
					if (task.getStartDate() != null && task.getStartTime() != null) {
						document.insertString(document.getLength(), "\t From ", taskInfo);
						if (!task.getStartTime().equals(LocalTime.MAX)) {
							document.insertString(document.getLength(), task.getStartTime().toString() + ", ",
									taskInfo);
						}
						document.insertString(document.getLength(), task.getStartDate().format(dateFormat), taskInfo);
						document.insertString(document.getLength(), "\t To ", taskInfo);
					} else {
						document.insertString(document.getLength(), "\t At ", taskInfo);
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