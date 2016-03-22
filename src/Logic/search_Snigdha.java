package Logic;

import java.util.ArrayList;

import ScheduleHacks.Task;

import java.time.LocalDate;
import java.time.LocalTime;

public class search_Snigdha {

	static int count = 1;

	public void searchTask(Task taskToFind) {

		Logic obj = Logic.getInstance();

		ArrayList<Task> matchedTaskList = new ArrayList<Task>();

		findMatches(obj.getScheduledTasksOverDue(), matchedTaskList, taskToFind);
		findMatches(obj.getScheduledTasksToDo(), matchedTaskList, taskToFind);
		findMatches(obj.getFloatingTasksToDo(), matchedTaskList, taskToFind);

		findMatches(obj.getScheduledTasksComplete(), matchedTaskList, taskToFind);
		findMatches(obj.getFloatingTasksComplete(), matchedTaskList, taskToFind);

		printList(matchedTaskList);

	}

	public void findMatches(ArrayList<Task> sourceList, ArrayList<Task> destinationList, Task taskToFind) {

		/*if (taskToFind.isComplete()) {
			matchComplete(sourceList, destinationList);
		}*/

		for (Task task : sourceList) {
			// matching description
			if (taskToFind.getDescription() != null && !taskToFind.getDescription().isEmpty()) {
				matchDescription(destinationList, taskToFind, task);
			}

			// matching complete
			if (taskToFind.isComplete() && task.isComplete()) {
				destinationList.add(task);
			}

			// matching only end date
			if (taskToFind.getEndDate() != null && taskToFind.getStartDate() == null) {
				matchSingleDate(destinationList, taskToFind, task);
			}

			// matching date range
			if (taskToFind.getEndDate() != null && taskToFind.getStartDate() != null) {
				matchDateRange(destinationList, taskToFind, task);
			}
		}
	}

	private void matchComplete(ArrayList<Task> sourceList, ArrayList<Task> destinationList) {
		// TODO Auto-generated method stub
		
	}

	public void matchSingleDate(ArrayList<Task> destinationList, Task taskToFind, Task taskToCheck) {
		LocalDate dateToFind = taskToFind.getEndDate();
		if (taskToCheck.isScheduledTask()) {
			if (taskToCheck.getStartDate() != null && taskToCheck.getStartDate().isEqual(dateToFind)) {
				destinationList.add(taskToCheck);
			}
			if (taskToCheck.getEndDate().isEqual(dateToFind)) {
				destinationList.add(taskToCheck);
			}
		}
	}

	public void matchDateRange(ArrayList<Task> destinationList, Task taskToFind, Task taskToCheck) {
		LocalDate startDate = taskToFind.getStartDate();
		LocalDate endDate = taskToFind.getEndDate();

		if (taskToCheck.isScheduledTask()) {
			LocalDate taskEndDate = taskToCheck.getEndDate();
			if (taskToCheck.getStartDate() != null) {
				LocalDate taskStartDate = taskToCheck.getStartDate();
				if (dateLiesInRange(taskStartDate, startDate, endDate)) {
					destinationList.add(taskToCheck);
					return;
				} else if (taskStartDate.isBefore(startDate) && taskEndDate.isAfter(endDate)) {
					destinationList.add(taskToCheck);
					return;
				}
			}
			if (dateLiesInRange(taskEndDate, startDate, endDate)) {
				destinationList.add(taskToCheck);
			}
		}
	}

	public void matchDescription(ArrayList<Task> destinationList, Task taskToFind, Task taskToCheck) {
		String descToFind = taskToFind.getDescription().toLowerCase();
		if (taskToCheck.getDescription().toLowerCase().contains(descToFind)) {
			destinationList.add(taskToCheck);
		}
	}

	public void printList(ArrayList<Task> listToPrint) {
		count = 1;
		for (Task task : listToPrint) {
			if (task.isFloatingTask())
				printUntimedTask(task);
			else
				printTimedTask(task);
		}
	}

	private void printUntimedTask(Task task) {
		System.out.println((count++) + ". " + task.getDescription());
	}

	private void printTimedTask(Task task) {
		System.out.println((count++) + ". " + task.getDescription());
		if (task.getStartDate() != null && task.getStartTime() != null) {
			System.out.print("\t From ");
			if (!task.getStartTime().equals(LocalTime.MAX)) {
				System.out.print(task.getStartTime().toString() + ", ");
			}
			System.out.println(task.getStartDate());
			System.out.print("\t To ");
		} else {
			System.out.print("\t At ");
		}
		if (!task.getEndTime().equals(LocalTime.MAX)) {
			System.out.print(task.getEndTime().toString() + ", ");
		}
		System.out.println(task.getEndDate());
	}

	public boolean dateLiesInRange(LocalDate dateToCheck, LocalDate startDate, LocalDate endDate) {
		return dateToCheck.isEqual(startDate) || dateToCheck.isEqual(endDate)
				|| (dateToCheck.isAfter(startDate) && dateToCheck.isBefore(endDate));
	}
}
