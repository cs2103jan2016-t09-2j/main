import java.time.LocalDate;
import java.time.LocalTime;

class Task {
	
	private String description;
	private boolean isFloatingType, isScheduledType;
	private LocalDate startDate, endDate, dueDate;
	private LocalTime startTime, endTime, dueTime;
	
	public Task()  {
		
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
	
	public void setDueDate(LocalDate dueDate) {
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
	
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime; 
	}
	
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime; 
	}
	
	public void setDueTime(LocalTime dueTime) {
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