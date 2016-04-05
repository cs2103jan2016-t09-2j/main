/*
 * This method is a part of the Logic.
 * Assists in performing search and view operation 
 */

//@@author A0132778W
package Logic;

import java.util.ArrayList;

import ScheduleHacks.Task;

import java.time.LocalDate;

public class Search {

	private static final String STRING_WHITESPACE = " ";

	private static int count = 1;
	private ArrayList<Task> matchedTaskList = new ArrayList<Task>();
	private ArrayList<Integer> indexList = new ArrayList<Integer>();

	/****************** GETTER METHODS ***********************/

	/**
	 * The ArrayList<Task> returned by this method contains all the tasks that
	 * satisfy the search query input by the user.
	 * 
	 * @return matchedTaskList
	 */
	public ArrayList<Task> getSearchList() {
		return matchedTaskList;
	}

	/**
	 * The indexList contains the corresponding indexes of the tasks contained
	 * in the matchedTaskList.
	 * 
	 * @return indexList
	 */
	public ArrayList<Integer> getSearchIndexList() {
		return indexList;
	}

	/********************* OTHER METHODS ***********************/

	/**
	 * This method checks all 5 ArrayList<Task> to check for tasks that satisfy
	 * the conditions specified by taskToFind.
	 * 
	 * @param taskToFind
	 *            contains information regarding the search criteria.
	 */
	public void searchTask(Task taskToFind) {
		count = 0;
		Logic logicObj = Logic.getInstance();

		findMatches(logicObj.getScheduledTasksOverDue(), taskToFind);
		findMatches(logicObj.getScheduledTasksToDo(), taskToFind);
		findMatches(logicObj.getFloatingTasksToDo(), taskToFind);
		findMatches(logicObj.getScheduledTasksComplete(), taskToFind);
		findMatches(logicObj.getFloatingTasksComplete(), taskToFind);
	}

	/**
	 * This method checks the sourceList to see if it contains any task that
	 * satisfies criteria specified by taskToFind.
	 * 
	 * @param sourceList
	 *            contains a task list which is to be searched.
	 * @param taskToFind
	 *            contains information regarding search criteria.
	 */
	public void findMatches(ArrayList<Task> sourceList, Task taskToFind) {
		if (sourceList != null && !sourceList.isEmpty()) {
			for (Task task : sourceList) {
				count++; // count is used to maintain the index of the task

				// matching complete
				if (taskToFind.isComplete() && task.isComplete()) {
					indexList.add(count);
					matchedTaskList.add(task);
					continue;
				}
				// matching description
				if (taskToFind.getDescription() != null && !taskToFind.getDescription().isEmpty()) {
					if (hasMatchDescription(taskToFind, task)) {
						continue;
					}
				}
				// matching only end date
				if (taskToFind.getEndDate() != null && taskToFind.getStartDate() == null) {
					if (hasMatchSingleDate(taskToFind, task)) {
						continue;
					}
				}
				// matching date range
				if (taskToFind.getEndDate() != null && taskToFind.getStartDate() != null) {
					if (hasMatchDateRange(taskToFind, task)) {
						continue;
					}
				}
			}
		}
	}

	/**
	 * This method is invoked to check if taskToCheck has a startDate or endDate
	 * that matches with that specified in taskToFind.
	 * 
	 * @param taskToFind
	 *            contains information that needs to be matched.
	 * @param taskToCheck
	 *            is checked to see if it contains date specified
	 * @return true, if taskToCheck dates match with taskToFind, otherwise
	 *         false.
	 */
	public boolean hasMatchSingleDate(Task taskToFind, Task taskToCheck) {
		LocalDate dateToFind = taskToFind.getEndDate();
		if (taskToCheck.isScheduledTask()) {
			if (taskToCheck.getStartDate() != null && taskToCheck.getStartDate().isEqual(dateToFind)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
				return true;
			}
			if (taskToCheck.getEndDate().isEqual(dateToFind)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to check if the startDate and endDate of taskToCheck
	 * lie within the range specified by taskToFind.
	 * 
	 * @param taskToFind
	 *            contains date range to be matched.
	 * @param taskToCheck
	 *            is checked to see if its dates lie within the range specified
	 *            by taskToFind.
	 * @return true, if taskToCheck dates lie within taskToFind date range,
	 *         otherwise false.
	 */
	public boolean hasMatchDateRange(Task taskToFind, Task taskToCheck) {
		LocalDate startDate = taskToFind.getStartDate();
		LocalDate endDate = taskToFind.getEndDate();

		if (taskToCheck.isScheduledTask()) {
			LocalDate taskEndDate = taskToCheck.getEndDate();
			if (taskToCheck.getStartDate() != null) {
				LocalDate taskStartDate = taskToCheck.getStartDate();
				if (dateLiesInRange(taskStartDate, startDate, endDate)) {
					indexList.add(count);
					matchedTaskList.add(taskToCheck);
					return true;
				} else if (taskStartDate.isBefore(startDate) && taskEndDate.isAfter(endDate)) {
					indexList.add(count);
					matchedTaskList.add(taskToCheck);
					return true;
				}
			}
			if (dateLiesInRange(taskEndDate, startDate, endDate)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks if the description contained in taskToFind is present
	 * in taskToCheck.
	 * 
	 * @param taskToFind
	 *            contains the description to match.
	 * @param taskToCheck
	 *            is check to see if contains parts of description specified in
	 *            taskToFind.
	 * @return true, if taskToCheck description contains words specified in
	 *         taskToFind description, false otherwise.
	 */
	public boolean hasMatchDescription(Task taskToFind, Task taskToCheck) {
		String descToFind = taskToFind.getDescription().toLowerCase();
		// if the description provided for searching is a single letter, all the
		// tasks starting with the particular letter are returned.
		if (descToFind.length() == 1) {
			if (taskToCheck.getDescription().toLowerCase().startsWith(descToFind)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
				return true;
			}
		} else {
			String[] wordList = descToFind.split(STRING_WHITESPACE);
			if (areWordsPresent(wordList, taskToCheck.getDescription().toLowerCase())
					|| descToFind.equalsIgnoreCase(taskToCheck.getDescription())) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method checks if dateToCheck lies with startDate and endDate, both
	 * inclusive.
	 * 
	 * @param dateToCheck
	 * @param startDate
	 * @param endDate
	 * @return true, if dateToCheck lies within the range [startDate, endDate],
	 *         false otherwise.
	 */
	public boolean dateLiesInRange(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
		return dateToCheck.isEqual(startDate) || dateToCheck.isEqual(endDate)
				|| (dateToCheck.isAfter(startDate) && dateToCheck.isBefore(endDate));
	}

	/**
	 * This method checks if the all the words specified in wordList are
	 * included in text. It does not take order into consideration, also ignores
	 * single letters.
	 * 
	 * @param wordList
	 *            contains the list of words that should be present in text.
	 * @param text
	 * @return true if all the words in wordList are contained in text.
	 */
	public boolean areWordsPresent(String[] wordList, String text) {
		if (wordList.length == 0) {
			return false;
		}
		int countOneLetterWords = 0;
		for (String word : wordList) {
			if (word.length() > 1) {
				if (!text.contains(word)) {
					return false;
				}
			} else {
				countOneLetterWords++;
			}
		}
		if (countOneLetterWords == wordList.length) {
			return false;
		}
		return true;
	}
}
