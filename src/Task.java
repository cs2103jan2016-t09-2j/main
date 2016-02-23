import java.time.LocalDate;
import java.time.LocalTime;

class Task {
	
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
	
	protected String getDescription(){
		return this.description;
	}
	
	protected void setFloatingTask(){
		isFloatingType = true; 
	}
	
	protected void setScheduledTask(){
		isFloatingType = false; 
		isScheduledType = true;
	}
	
	protected boolean isFloatingTask() {
		return isFloatingType;
	}
	
	protected boolean isScheduledTask() {
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
	
	protected LocalDate getStartDate() {
		return this.startDate; 
	}
	
	protected LocalDate getEndDate() {
		return this.endDate; 
	}
	
	protected LocalDate getDueDate() {
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
	
	protected LocalTime getStartTime() {
		return this.startTime; 
	}
	
	protected LocalTime getEndTime() {
		return this.endTime; 
	}
	
	protected LocalTime getDueTime() {
		return this.dueTime; 
	}
}