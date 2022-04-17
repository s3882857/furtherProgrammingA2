package smartBoard.User;
import java.security.InvalidParameterException;


import utilities.ConsoleUtilities;

/*
 * User class to store and encapsulate all user related data. 
 * Stores Profile class, and Photo class.
 * User acts as a facade to all data requirements of the User. 
 */
public class User {

	private static User user;
	
	private String userName;
	private String password;
	private String currentProject;
	private String currentBasket;
	private String currentTask;
		
	private ConsoleUtilities consoleUtil;
	
	private Photo photo;
	private Profile profile;
	
	/*
	 * Private User constructor for Singleton instance creation.
	 */
	private User() {
		
		this.consoleUtil = new ConsoleUtilities();
		
		this.profile = null;
		this.photo   = null;
		this.password = "";
		this.userName = "";
		this.currentProject = "";
		this.currentBasket = "";
		this.currentTask = "";
		
	}
	
	/*
	 * Getters
	 * 
	 */

	/*
	 * Get a User instance singleton. This is the login instance.
	 */
	public synchronized static User getInstance(){
		
		if (User.user==null) {
			
			User.user = new User();
			//throw new SmartBoardLoginException("Login failure. You have been logged out, please re-login.");
			
		}
		
		return User.user;
		
	}
	
	/*
	 * Check the password for match.
	 */
	public boolean passwordCheck(String password) {
		
		if(password.equals(this.password)) {
			return true;
		} else {
			return false;
		}
			
	}
	
	/*
	 * Get userName.
	 */
	public String getUserName() {
		return this.userName;
	}
	
		
	/*
	 * Get users profile
	 */
	public Profile getProfile() {
		return this.profile;
	}
	
	/*
	 * Get users profile photo.
	 */
	public Photo getPhoto() {
		return this.photo;
	}
	
	/*
	 * Get Project the user is currently logged into.
	 */
	public String getProjectLogin() {
		return this.currentProject;
	}
	
	/*
	 * Get Basket the User is currently in. 
	 */
	public String getBasketLogin() {
		return this.currentBasket;
	}
	
	/*
	 * Get Task the User is currently in. 
	 */
	public String getTaskLogin() {
		return this.currentTask;
	}
	
	/*
	 * Setters
	 * 
	 */
	
	/*
	 * Set all user detail information in one.
	 */
	public void setUserDetails(Profile profile) throws InvalidParameterException {
		
		setUserName(profile.getUserName());
		setPassword(profile.getPassword());
		
		this.photo = Photo.getInstance();
		this.photo.setFilePhotoName(profile.getFilePhotoName());
		this.currentProject = profile.getDefaultProject();
		this.profile = profile;
		
	}
	/*
	 * Set userName. 
	 */
	public void setUserName(String userName) throws InvalidParameterException{
		
		if(!this.consoleUtil.isStringFieldEmpty(userName)) {
			
			this.userName = userName;
						
		} else {
			
			throw new InvalidParameterException("Parameter 'userName' is missing.");
		}
	}
	
	/*
	 * Set password.
	 */
	public void setPassword(String password) throws InvalidParameterException{
		
		if(!this.consoleUtil.isStringFieldEmpty(password)) {
			
			this.password = password;
						
		} else {
			
			throw new InvalidParameterException("Parameter 'password' is missing.");
			
		}
	}

	/*
	 * Set the first name through the User for later retrieval and writing back
	 * to the profile file. 
	 */
	public void setFirstName(String firstName) {
		this.profile.setFirstName(firstName);
	}
	
	/*
	 * Set the last name through the User for later retrieval and writing back
	 * to the profile file.  
	 */
	public void setLastName(String lastName) {
		this.profile.setLastname(lastName);
	}
	
	
	
	/*
	 * Set profile.
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/*
	 * Set the name of the profile photo image.
	 */
	public void setPhotoName(String photoName) {
		this.photo.setFilePhotoName(photoName);
	}
	
	/*
	 * Set Project the user is currently logged into. Also sets Profile
	 * so any new info is updated back to file
	 */
	public void setProjectLogin(String project) {
		this.currentProject = project;
		this.profile.setDefaultProject(project);
	}
	
	/*
	 * Set the Basket the user is currently in. 
	 */
	public void setCurrentBasket(String currentBasket) {
		this.currentBasket = currentBasket;
	}
	
	/*
	 * Set the Basket the user is currently in. 
	 */
	public void setCurrentTask(String currentTask) {
		this.currentTask = currentTask;
	}
	
}
