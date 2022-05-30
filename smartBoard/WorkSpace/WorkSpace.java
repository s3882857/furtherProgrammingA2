package smartBoard.WorkSpace;

import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Date;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.PatternSyntaxException;

import smartBoard.Connection.SBConnectionManager;
import smartBoard.Connection.SQLScriptor;
import smartBoard.User.User;
import utilities.FXUtilities;

/*
 * WorkSpace for storing all items and tasks relating to a User.
 * Super base class for all WorkSpace items. Project, Basket, Task, etc.
 */
public abstract class WorkSpace {

	private String referenceNo;
	// User that created the item.
	private String userName;
	
	// Name of the item.
	private String name;
	
	// Create date and time. 
	private LocalDate createDate;
	private LocalTime createTime;
	
	// Item deleted status. No records are deleted instead a status is set to prevent
	// it being displayed to the user if this is true.
	private boolean deleted;
	
	// Parent name 
	private String parentName;
	
	// Item sequence number. Used to write each workspace item back to the database.
	// whilst preserving the items order. Writes a new referenceNo each time.
	private static int itemNumber;
	
	// Data has been written.
	private boolean written;
	
	// Database connection.
	private SBConnectionManager connectionSB;
	
	// Script generator.
	private SQLScriptor script;
	
	// Utilities
	public FXUtilities util;
	
	final static String KEYNAME = "NAME";
	final static String KEYNAME2 = "USERNAME";
	final static String KEYNAME3 = "PARENTNAME";
	/*
	 * Create a new workspace item. 
	 */
	public WorkSpace() {
		
		this.userName = User.getInstance().getUserName();
		
		this.name = "";
		this.parentName = "";
		this.createDate  = LocalDate.now();
		this.createTime  = LocalTime.now();
		this.deleted = false;
		
		this.connectionSB = new SBConnectionManager();
		this.script = new SQLScriptor();
		this.util = new FXUtilities();
				
			
		this.written = false;
		
	}
		
	// Make sure for each record every connection is opened and closed.
	public abstract void open() throws SQLException;
	
	/*
	 * Header read for all Tables.
	 */
	public void readDB(ResultSet rs) throws SQLException {
		
		setName(rs.getString(WSQueryColumns.WS_NAME));
		setCreateDate(rs.getString(WSQueryColumns.WS_CREATE_DATE));
		setCreateTime(rs.getString(WSQueryColumns.WS_CREATE_tIME));
		setUserName(rs.getString(WSQueryColumns.WS_USERNAME));
		setDeleted(Boolean.parseBoolean(rs.getString(WSQueryColumns.WS_DELETED)));
		setReferenceNo(rs.getString(WSQueryColumns.WS_REFERENCE_NO));
		
	}
		
	public abstract void writeDB() throws SQLException;
	
	public String[] getColumnValues() {

		String[] columnValues = new String[11];

		int i = 0;

		columnValues[i++] = getName();          
		columnValues[i++] = getCreateDate();
		columnValues[i++] = getCreateTime();
		columnValues[i++] = getUserName();
		columnValues[i++] = String.valueOf(getItemNumber());
		columnValues[i++] = String.valueOf(getDeleted());
		columnValues[i++] = "<skip>";
		columnValues[i++] = "<skip>";
		columnValues[i++] = "<skip>";
		columnValues[i++] = "<skip>";
		columnValues[i++] = "<skip>";

		return columnValues;
		
	}
	
	// Make sure for each record every connection is opened and closed.
	public abstract void close() throws SQLException;
	
	/*
	 * Get the referenceNo
	 */
	public String getReferenceNo() {
		
		// If there is no referenceNo allocated it means this is a new WorkSpace item.
		if(this.util.isStringFieldEmpty(this.referenceNo)) {
			this.referenceNo = String.valueOf(this.getItemNumber());
		}
		
		return this.referenceNo;
		
	}
	
	/*
	 * Get the user name.
	 */
	public String getUserName() {
		
		return this.userName;
		
	}
	
	public String getName() {
		
		return this.name;
		
	}
	
	public String getParentName() {
		
		return this.parentName;
		
	}
	/*
	 * Get date record created. 99/99/9999
	 */
	public String getCreateDate() {
		
		return this.createDate.toString();
		
	}
	
	/*
	 * Get time record created. ss-mm-hh
	 */
	public String getCreateTime() {
	
		return this.createTime.toString();
		
	}
	
	/*
	 * Get deleted status.
	 */
	public boolean getDeleted() {
		
		return this.deleted;
		
	}
	
	/*
	 * Get written flag.
	 */
	public boolean isRecordWritten() {
		return this.written;
	}
	
	/*
	 * Get item number. Static value. Keeps all items unique and remembers there display order.
	 */
	public int getItemNumber() {
		
		if(WorkSpace.itemNumber<=0) {
			WorkSpace.itemNumber = 1;
		}
		
		return incrementItemNumber();
		
	}
	
	private int incrementItemNumber() {
		return WorkSpace.itemNumber++;
	}
	
	/*
	 * Standard database open and start for all workspace items.
	 */
	public void openSBConnection() throws SQLException {
		
		this.connectionSB.createSBConnection();
		this.connectionSB.createSBStatement();
		
	}
	
	/*
	 * Standard database close for all workspace items.
	 */
	public void closeSBConnection() throws SQLException {
		
		this.connectionSB.close();
		
	}
	
	/*
	 * Clean database after amending Project Name or task name.
	 */
	public void updateChildren(String newParentName) throws SQLException {
		
	
		
	}
	
	
	/*
	 * Get database connection.
	 */
	public SBConnectionManager getConnection() {
		
		return this.connectionSB;
		
	}
	
	/*
	 * Get a script maker/generator for all workspace items
	 */
	public SQLScriptor getSQLScriptor() {
		
		return this.script;
		
	}
	
	
	public void recordWritten() {
		this.written = true;
	}
	
	/*
	 * Set deleted status.
	 */
	public void setDeleted(boolean yesNo) {
		
		this.deleted = yesNo;
		
	}
	
	public void setCreateDate(String createDate) {
		
		this.createDate = this.util.convertDate(createDate);
			
	}
	
	public void setCreateTime(String createTime) {
		
		LocalTime newTime = null;
		
		try {
		
			
			String[] formattedTime = createTime.split(":");
			
			int hour = Integer.parseInt(formattedTime[0]);
			
			int minute = Integer.parseInt(formattedTime[1]);
					
			String[] formattedSecond = formattedTime[2].split("\\.");
						
			int second = Integer.parseInt(formattedSecond[0]);
						
			newTime = LocalTime.of(hour, minute, second);
								
		}
		catch(NumberFormatException nfe) {
			
			newTime = LocalTime.now();
			
		}
		catch(PatternSyntaxException e) {
			
			newTime = LocalTime.now();
			
		}
		
		this.createTime = newTime;
		
	}
	
	public void setUserName(String userName) {
		
		this.userName = userName;
		
	}
	
	public void setReferenceNo(String referenceNo) {
		
		this.referenceNo = referenceNo;
		WorkSpace.itemNumber = Integer.parseInt(this.referenceNo);
		
	}
	
	public void setName(String name) {
		
		this.name = name;
		
	}
	
	public void setParentName(String parentName) {
		
		this.parentName = parentName;
		
	}
	
	public void setItemNumberZero() {
		
		WorkSpace.itemNumber = 0;
		
	}
	
}
