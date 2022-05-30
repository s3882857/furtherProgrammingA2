package smartBoard.User;

import java.io.IOException;

import utilities.FXUtilities;

/*
 * Store all data relating to the users profile.
 */
public class Profile {

	private String userName;
	private String password;
	private String lastName;
	private String firstName;
	private String defaultProject;
	private Photo image;
	
	private FXUtilities util;

	/*
	 * Create a new User.
	 */
	public Profile(String firstName, String lastName, String userName, String password, String defaultProject) {

		setFirstName(firstName);
		setLastname(lastName);
		setUserName(userName);
		setPassword(password);
		setDefaultProject(defaultProject);

		this.util = new FXUtilities();
		this.image = new Photo();
		
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
	/*public String getFilePhotoName() {
		return this.filePhotoName;
	}*/

	/*
	 * Get default project User is auto logged into.
	 */
	public String getDefaultProject() {
		return this.defaultProject;
	}

	/*
	 * Get users profile photo.
	 */
	public Photo getPhoto() {
		return this.image;
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
	/*public void setFilePhotoName(String filePhotoName) {
		this.filePhotoName = filePhotoName;
	}*/

	/*
	 * Set default project user is logged into.
	 */
	public void setDefaultProject(String defaultProject) {
		this.defaultProject = defaultProject;
	}
	
	/*
	 * Set the name of the profile photo image. 
	 * Photo class will create the image and store it
	 */
	public void setNewImage(String photoName) throws IOException {
		
		if(!this.util.isStringFieldEmpty(photoName)) {
			
			this.image.setNewImage(photoName,this.userName);
			
		}
		
	}

}
