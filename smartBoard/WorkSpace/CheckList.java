package smartBoard.WorkSpace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class CheckList extends WorkSpace {

	
	private boolean complete;
	private LocalDate completeDate;
	private LocalDate scheduleDate;
	private String name;
	
	public CheckList(String name) {
		super();
		this.complete = false;
		this.name = name;
	}
	
	public CheckList(ResultSet rs) {
		super();
		// Add more things here.
	}
	
	@Override
	public void open() throws SQLException {
		
	    super.openSBConnection();
		
	}

	@Override
	public void close() throws SQLException {
		
		super.closeSBConnection();
		
	}
	
	@Override
	public void writeDB() throws SQLException {
					
	}
	/*
	 * Getters
	 */
	
	
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
	
	public String getName() {
		return this.name;
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
