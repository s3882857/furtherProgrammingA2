package smartBoard.WorkSpace;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashSet;

public class Task extends WorkSpace implements Comparable<Task> {

	private String description;
	private String projectName;


	private LocalDate scheduledDate;
	private LocalDate dateCompleted;
		
	private LinkedHashSet<CheckList> checkLists;
	
	private final static String TABLENAME = "TASK";
	
	public Task(String taskName, String description, String basketName, String projectName) {
			
		super();
		
		this.description = description;
		this.checkLists = new LinkedHashSet<CheckList>();
	
		super.setName(taskName);
	
		super.setParentName(basketName);

		this.projectName = projectName;
				
	}
	
	public Task(ResultSet rs) throws SQLException {
		
		this.checkLists = new LinkedHashSet<CheckList>();
		
		/*  Unfortunately cannot use the super(rs) call here as the column numbers for each
		 *  type of workspace item are different even though the fields are the same index value
		 *  within the individual table. 
		 */
		readDB(rs);
		
	}
	
	/*
	 * Read Task item from database table Task from ResultSet.
	 */
	public void readDB(ResultSet rs) throws SQLException {
		
		super.readDB(rs);
						
		setDescription(rs.getString(WSQueryColumns.TSK_DESCRIPTION));	
		
		super.setParentName(rs.getString(WSQueryColumns.WS_PARENT_NAME));
		
		setProjectName(rs.getString(WSQueryColumns.TSK_PROJECT_NAME));
		setCompletedDate(rs.getString(WSQueryColumns.TSK_COMPLETED_DATE));
		setScheduledDate(rs.getString(WSQueryColumns.TSK_SCHEDULED_DATE));
				
	}
	
	/*
	 * Add a CheckList item to the Task.
	 */
	public void add(WorkSpace checkList) throws IllegalArgumentException {
		
		if (checkList instanceof CheckList) {
			
			this.checkLists.add((CheckList)checkList);
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/*
	 * Get column values.
	 */
	public String[] getColumnValues() {
		
		String[] columnValues = super.getColumnValues();
				
		columnValues[6]  = super.getParentName();
		columnValues[7]  = getProjectName();
		columnValues[8]  = getDescription();
		columnValues[9]  = getScheduledDateString();
		columnValues[10] = getDateCompletedString();
		
		return columnValues;
		
	}
	
	/*
	 * Write basket to Database.
	 */
	public void writeDB() throws SQLException {
		
		if(!super.isRecordWritten()) {
			
			String scriptValue = "";
			String[] columnValues = getColumnValues();
					
			String[] keyValues = {getName(),getUserName(),getParentName()};
			String[] keyNames  = {KEYNAME,KEYNAME2,KEYNAME3};
			
			boolean recordExists = super.getConnection().existingRecord(TABLENAME, keyValues, keyNames);
							
			if(recordExists) {
			
				scriptValue = super.getSQLScriptor().buildUpdateScript(super.getConnection(), TABLENAME, keyValues, keyNames, columnValues );
				super.getConnection().executeUpdate(scriptValue);
			
			} else {
			
				scriptValue = super.getSQLScriptor().buildInsertScript(TABLENAME, columnValues);
				super.getConnection().executeInsert(scriptValue);
			
			}
			
			super.recordWritten();
			
		}
		
	}

	/*
	 * delete task.
	 */
	public void deleteTask() throws SQLException {

		open();
		
		String[] keyNames = {KEYNAME,KEYNAME2,KEYNAME3};
		String[] keyValues = {getName(),getUserName(),getParentName()};
		
		super.getConnection().executeDelete(super.getSQLScriptor().buildDeleteScript(TABLENAME, keyNames, keyValues));
		
		close();
		
	}
	
	/*
	 * Getters
	 */
	
	/*
	 * Get task description.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/*
	 * Get the projectName that this task belongs to.
	 */
	public String getProjectName() {
		return this.projectName;
	}

	public int compareTo(Task workspace) {
	
		return super.getName().compareTo(workspace.getName());
	
	}

	@Override
	public void open() throws SQLException {
		
	    super.openSBConnection();
		
	}

	@Override
	public void close() throws SQLException {
		
		super.closeSBConnection();
		
	}
	
	/*
	 * Setters	
	 */
	
	/*
	 * Set description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/*
	 * set the Project name that this task belongs to.
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	
	/*
	 * get the scheduled Date.
	 */
	public String getScheduledDateString() {
		
		if(this.scheduledDate != null) {
			return this.scheduledDate.toString();
		}
		else {
			return "";
		}
		
	}
	
	public LocalDate getScheduledLocalDate() {
		
		return this.scheduledDate;
		
	}

	public void setScheduledDate(LocalDate scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public void setScheduledDate(String dateIn) {
		
		this.scheduledDate = super.util.convertDate(dateIn);
				
	}
	
	public boolean isComplete() {
		
		if(this.dateCompleted!=null) {
			return true;
		}
		
		return false;
	}

	public String getDateCompletedString() {
		
		if(this.dateCompleted!=null) {
			return this.dateCompleted.toString();
		} else {
			return "";
		}
				
	}
	
	public LocalDate getDateCompletedLocalDate() {
		
		return this.dateCompleted;
		
	}
	
	public void setCompletedDate(String dateIn) {
		
		LocalDate dateCheck = super.util.convertDate(dateIn);
				
		if(dateCheck != null) {
		
			this.dateCompleted = dateCheck;
			
		} else {
		
			this.dateCompleted = null;
			
		}
		
	}
	
	public void setComplete(boolean complete) {
		
		if(complete) {
			this.dateCompleted = LocalDate.now();
		} else {
			this.dateCompleted = null;
		}
		
	}

	public LinkedHashSet<CheckList> getCheckLists() {
		return checkLists;
	}

	
}
