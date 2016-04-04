/*
 * This method is a part of the backend Logic.
 * Assists in performing search and view operation 
 */

//@@author A0132778W
package Logic;

import java.util.ArrayList;

import ScheduleHacks.Task;

import java.time.LocalDate;

public class Search {

	private static final String STRING_WHITESPACE = " ";

	static int count = 1;
	private ArrayList<Task> matchedTaskList = new ArrayList<Task>();
	private ArrayList<Integer> indexList = new ArrayList<Integer>();

	public ArrayList<Task> getSearchList() {
		return matchedTaskList;
	}

	public ArrayList<Integer> getSearchIndexList() {
		return indexList;
	}

	public void searchTask(Task taskToFind) {
		count = 0;
		Logic obj = Logic.getInstance();

		findMatches(obj.getScheduledTasksOverDue(), taskToFind);
		findMatches(obj.getScheduledTasksToDo(), taskToFind);
		findMatches(obj.getFloatingTasksToDo(), taskToFind);
		findMatches(obj.getScheduledTasksComplete(), taskToFind);
		findMatches(obj.getFloatingTasksComplete(), taskToFind);
	}

	public void findMatches(ArrayList<Task> sourceList, Task taskToFind) {
		if (sourceList != null && !sourceList.isEmpty()) {
			for (Task task : sourceList) {
				count++;
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

	public boolean hasMatchDescription(Task taskToFind, Task taskToCheck) {
		String descToFind = taskToFind.getDescription().toLowerCase();
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

	public boolean dateLiesInRange(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
		return dateToCheck.isEqual(startDate) || dateToCheck.isEqual(endDate)
				|| (dateToCheck.isAfter(startDate) && dateToCheck.isBefore(endDate));
	}

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
