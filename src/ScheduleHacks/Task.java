package ScheduleHacks;

import java.time.LocalDate;
import java.time.LocalTime;

public class Task {

	private String description;
	private boolean isFloatingType, isScheduledType, isComplete;
	private LocalDate startDate, endDate;
	private LocalTime startTime, endTime;

	public Task() {
		setDescription(null);
		isFloatingType = false;
		isScheduledType = false;
		isComplete = false;
		setStartDate(null);
		setEndDate(null);
		setStartTime(null);
		setEndTime(null);
	}
	
	public Task(String desc, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
		setDescription(desc);
		isFloatingType = false;
		isScheduledType = false;
		isComplete = false;
		setStartDate(startDate);
		setEndDate(endDate);
		setStartTime(startTime);
		setEndTime(endTime);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return this.description;
	}

	public void setFloatingTask() {
		isFloatingType = true;
		isScheduledType = false;
	}

	public void setScheduledTask() {
		isFloatingType = false;
		isScheduledType = true;
	}

	public void setAsComplete() {
		isComplete = true;
	}

	public void setAsIncomplete() {
		isComplete = false;
	}

	public boolean isFloatingTask() {
		return isFloatingType;
	}

	public boolean isScheduledTask() {
		return isScheduledType;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public LocalDate getEndDate() {
		return this.endDate;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public LocalTime getStartTime() {
		return this.startTime;
	}

	public LocalTime getEndTime() {
		return this.endTime;
	}

	/*//@Override
	public String toString() {
		String taskDetails = "";
		taskDetails += "Description: " + this.getDescription();
		if (this.getEndDate() != null) {
			if (this.getStartDate() != null) {
				taskDetails += " " + this.getStartTime().toString() + ", " + this.getStartDate().toString();
			}
			taskDetails += " " + this.getEndTime().toString() + ", " + this.getEndDate().toString();
		}
		taskDetails += " Complete?" + isComplete();
		return taskDetails;
	}*/
}