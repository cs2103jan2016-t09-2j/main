package ScheduleHacks;


import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
	
	private String description;
	private boolean isFloatingType, isScheduledType;
	private LocalDate startDate, endDate, dueDate;
	private LocalTime startTime, endTime, dueTime;
	
	public Task()  {
		setDescription(null);
		isFloatingType = false; isScheduledType = false;
		setStartDate(null); setEndDate(null); setDueDate(null);
		setStartTime(null); setEndTime(null); setDueTime(null);
	}
	
	protected void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	protected void setFloatingTask(){
		isFloatingType = true; 
	}
	
	void setScheduledTask(){
		isFloatingType = false; 
		isScheduledType = true;
	}
	
	public boolean isFloatingTask() {
		return isFloatingType;
	}
	
	public boolean isScheduledTask() {
		return isScheduledType;
	}
	
	protected void setStartDate(LocalDate startDate) {
		this.startDate = startDate; 
	}
	
	protected void setEndDate(LocalDate endDate) {
		this.endDate = endDate; 
	}
	
	protected void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate; 
	}
	
	public LocalDate getStartDate() {
		return this.startDate; 
	}
	
	public LocalDate getEndDate() {
		return this.endDate; 
	}
	
	public LocalDate getDueDate() {
		return this.dueDate; 
	}
	
	protected void setStartTime(LocalTime startTime) {
		this.startTime = startTime; 
	}
	
	protected void setEndTime(LocalTime endTime) {
		this.endTime = endTime; 
	}
	
	protected void setDueTime(LocalTime dueTime) {
		this.dueTime = dueTime; 
	}
	
	public LocalTime getStartTime() {
		return this.startTime; 
	}
	
	public LocalTime getEndTime() {
		return this.endTime; 
	}
	
	public LocalTime getDueTime() {
		return this.dueTime; 
	}
}