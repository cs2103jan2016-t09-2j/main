package Logic;

import java.util.ArrayList;

import ScheduleHacks.Task;
import GUI.TempCLI;

import java.time.LocalDate;

public class Search {

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
		Logic obj = Logic.getInstance();

		findMatches(obj.getScheduledTasksOverDue(), taskToFind);
		findMatches(obj.getScheduledTasksToDo(), taskToFind);
		findMatches(obj.getFloatingTasksToDo(), taskToFind);
		findMatches(obj.getScheduledTasksComplete(), taskToFind);
		findMatches(obj.getFloatingTasksComplete(), taskToFind);
	}

	public void findMatches(ArrayList<Task> sourceList, Task taskToFind) {

		/*
		 * if (taskToFind.isComplete()) { matchComplete(sourceList,
		 * destinationList); }
		 */

		for (Task task : sourceList) {
			// matching description

			if (taskToFind.getDescription() != null && !taskToFind.getDescription().isEmpty()) {
				matchDescription(taskToFind, task);
			}

			// matching complete
			if (taskToFind.isComplete() && task.isComplete()) {
				indexList.add(count);
				matchedTaskList.add(task);
			}

			// matching only end date
			if (taskToFind.getEndDate() != null && taskToFind.getStartDate() == null) {
				matchSingleDate(taskToFind, task);
			}

			// matching date range
			if (taskToFind.getEndDate() != null && taskToFind.getStartDate() != null) {
				matchDateRange(taskToFind, task);
			}

			count++;
		}
	}

	public void matchSingleDate(Task taskToFind, Task taskToCheck) {
		LocalDate dateToFind = taskToFind.getEndDate();
		if (taskToCheck.isScheduledTask()) {
			if (taskToCheck.getStartDate() != null && taskToCheck.getStartDate().isEqual(dateToFind)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
			}
			if (taskToCheck.getEndDate().isEqual(dateToFind)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
			}
		}
	}

	public void matchDateRange(Task taskToFind, Task taskToCheck) {
		LocalDate startDate = taskToFind.getStartDate();
		LocalDate endDate = taskToFind.getEndDate();

		if (taskToCheck.isScheduledTask()) {
			LocalDate taskEndDate = taskToCheck.getEndDate();
			if (taskToCheck.getStartDate() != null) {
				LocalDate taskStartDate = taskToCheck.getStartDate();
				if (dateLiesInRange(taskStartDate, startDate, endDate)) {
					indexList.add(count);
					matchedTaskList.add(taskToCheck);
					return;
				} else if (taskStartDate.isBefore(startDate) && taskEndDate.isAfter(endDate)) {
					indexList.add(count);
					matchedTaskList.add(taskToCheck);
					return;
				}
			}
			if (dateLiesInRange(taskEndDate, startDate, endDate)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
			}
		}
	}

	public void matchDescription(Task taskToFind, Task taskToCheck) {
		String descToFind = taskToFind.getDescription().toLowerCase();
		if (descToFind.length() == 1) {
			if (taskToCheck.getDescription().toLowerCase().startsWith(descToFind)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
			}
		} else {
			if (taskToCheck.getDescription().toLowerCase().contains(descToFind)) {
				indexList.add(count);
				matchedTaskList.add(taskToCheck);
			}
		}
	}

	public boolean dateLiesInRange(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
		return dateToCheck.isEqual(startDate) || dateToCheck.isEqual(endDate)
				|| (dateToCheck.isAfter(startDate) && dateToCheck.isBefore(endDate));
	}
}
