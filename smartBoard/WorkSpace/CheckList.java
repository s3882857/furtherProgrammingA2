package smartBoard.WorkSpace;

import java.time.LocalDate;

public class CheckList {

	private String name;
	private boolean complete;
	private LocalDate completeDate;
	private LocalDate scheduleDate;
	
	public CheckList(String name) {
		
		this.name = name;
		this.complete = false;
		
	}
	
	
	/*
	 * Getters
	 */
	
	/*
	 * Get the check list's item name.
	 */
	public String getName() {
		return this.name;
	}
	
	/*
	 * Get complete status.
	 */
	public boolean getCompleteStatus() {
		return this.complete;
	}

	/*
	 * Get the completion date.
	 */
	public String getCompletionDate() {
		
		String completeDate = "";
		
		if (this.completeDate!=null) {
			completeDate = this.completeDate.getDayOfMonth() + "/" + this.completeDate.getMonthValue() + "/" + this.completeDate.getYear();
		}
		
		return completeDate;
		
	}
	
	public String getScheduledDate() {
		
		String scheduleDate = "";
		
		if (this.scheduleDate!=null) {
			scheduleDate = this.scheduleDate.getDayOfMonth() + "/" + this.scheduleDate.getMonthValue() + "/" + this.scheduleDate.getYear();
		}
		
		return scheduleDate;
	}
	
	/*
	 * Setters
	 */
	
	/*
	 * Set check list item as completed.
	 * Sets date completed to current date.
	 */
	public void setComplete() {
		this.complete = true;
		this.completeDate = LocalDate.now();
	}

	/*
	 * Sets the date the item is scheduled to be completed.
	 */
	public void setScheduledDate(int addOnDays) {
		
		LocalDate today = LocalDate.now();
		
		this.scheduleDate = today.plusDays(addOnDays);
		
	}

}
