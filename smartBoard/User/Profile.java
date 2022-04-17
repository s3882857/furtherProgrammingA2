package smartBoard.User;

public class Profile {

	private String userName;
	private String password;
	private String lastName;
	private String firstName;
	private String filePhotoName;
	private String defaultProject;
	
	public Profile(String firstName, String lastName,String userName, String password,String filePhotoName,String defaultProject) {
				
		setFirstName(firstName);
		setLastname(lastName);
		
		setUserName(userName);
		setPassword(password);
		setFilePhotoName(filePhotoName);
		setDefaultProject(defaultProject);

	}
	
	/*
	 * Getters
	 */
	
	/*
	 * Get Last Name.
	 */
	public String getLastName() {
		return this.lastName;
	}
	
	/*
	 * Get first name.
	 */
	public String getFirstName() {
		return this.firstName;
	}
	
	/*
	 * Get user name.
	 */
	public String getUserName() {
		return this.userName;
	}
	
	/*
	 * Get password.
	 */
	public String getPassword() {
		return this.password;
	}
	
	/*
	 * Get file photo name.
	 */
	public String getFilePhotoName() {
		return this.filePhotoName;
	}
	
	/*
	 * Get default project User is auto logged into.
	 */
	public String getDefaultProject() {
		return this.defaultProject;
	}
	
	/*
	 * Setters
	 */
	
	/*
	 * Set last name.
	 */
	public void setLastname(String lastName) {
		this.lastName = lastName;
	}
	
	/*
	 * Set first name.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	/*
	 * Set userName.
	 */
	public void setUserName(String userName) {
		
		this.userName = userName;
	}
	
	/*
	 * Set password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/*
	 * Set stored file photo name.
	 */
	public void setFilePhotoName(String filePhotoName) {
		this.filePhotoName = filePhotoName;
	}
	
	/*
	 * Set default project user is logged into.
	 */
	public void setDefaultProject(String defaultProject) {
		this.defaultProject = defaultProject;
	}
		
}
