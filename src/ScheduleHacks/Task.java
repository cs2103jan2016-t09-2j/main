package ScheduleHacks;


import java.time.LocalDate;
import java.time.LocalTime;

public class Task {
	
	private String description;
	private boolean isFloatingType, isScheduledType;
	private LocalDate startDate, endDate;
	private LocalTime startTime, endTime;
	
	public Task()  {
		setDescription(null);
		isFloatingType = false; isScheduledType = false;
		setStartDate(null); setEndDate(null);
		setStartTime(null); setEndTime(null);
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setFloatingTask(){
		isFloatingType = true; 
	}
	
	public void setScheduledTask(){
		isFloatingType = false; 
		isScheduledType = true;
	}
	
	public boolean isFloatingTask() {
		return isFloatingType;
	}
	
	public boolean isScheduledTask() {
		return isScheduledType;
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
}