package smartBoard.WorkSpace;

import java.time.LocalDate;
import java.time.LocalTime;

import smartBoard.User.User;

/*
 * Super class for WorkSpace items. Each WorkSpace item needs to store statistical info.
 * Create date, time, item name, and the user name of the person that created the item.
 */
public class WorkSpaceManager {

	// Item sequence number. Not sure yet if I actually need this. 
	// At the moment I do not think it is required. Wait till I get
	// to the writing of the data to think this through completely.
	//private static int itemNumber;
	
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
	
	public WorkSpaceManager(String name) {
		
		this.userName = User.getInstance().getUserName();
		
		this.name = name;
		this.createDate  = LocalDate.now();
		this.createTime  = LocalTime.now();
		this.deleted = false;
		
	}
	
	/*
	 * Get the user name.
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/*
	 * Get date record created. 99/99/9999
	 */
	public String getCreateDate() {
		return this.createDate.getDayOfMonth() + "/" + this.createDate.getMonthValue() + "/" + this.createDate.getYear();
	}
	
	/*
	 * Get time record created. ss-mm-hh
	 */
	public String getCreateTime() {
		//return this.createTime.getSecond() + "-" + this.createTime.getMinute() + "-" + this.createTime.getHour();
		return this.createTime.getHour() + "-" + this.createTime.getMinute() + "-" + this.createTime.getSecond();
	}
	
	/*
	 * Get Name of the item.
	 */
	public String getName() {
		return this.name;
	}
	
	/*
	 * Get deleted status.
	 */
	public boolean getDeleted() {
		return this.deleted;
	}
	
	/*
	 * Set the name of the item.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/*
	 * Set deleted status.
	 */
	public void setDeleted() {
		this.deleted = true;
	}
	
}
