package smartBoard.User;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.image.Image;
import smartBoard.Connection.SBConnectionManager;
import smartBoard.Connection.SQLScriptor;
import utilities.FXUtilities;

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

	private FXUtilities util;

	private SBConnectionManager connectionSB;
	private SQLScriptor scripts;
	private Profile profile;

	/*
	 * Private User constructor for Singleton instance creation.
	 */
	private User() {

		this.util = new FXUtilities();
		this.scripts = new SQLScriptor();
		this.profile = null;
		this.connectionSB = new SBConnectionManager();

		this.password = "";
		this.userName = "";
		this.currentProject = "";
		this.currentBasket = "";
		this.currentTask = "";

	}

	/*
	 * Get a User instance singleton. This is the login instance.
	 */
	public synchronized static User getInstance() {

		if (User.user == null) {

			User.user = new User();

		}

		return User.user;

	}

	/*
	 * Log out set User to null.
	 */
	public void logout() {

		User.user = null;

	}

	/*
	 * Check the password for match.
	 */
	public boolean passwordCheck(String password) {

		if (password.equals(this.password)) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Logging in. Read user details and load to appropriate classes.
	 */
	public boolean getUser(String userName) throws SQLException {

		boolean userExists = true;
		Profile profile = readProfile(userName);

		if (profile != null) {

			User.getInstance().setUserDetails(profile);

		} else {

			userExists = false;

		}

		return userExists;

	}

	/*
	 * Read a single profile from the database.
	 */
	public Profile readProfile(String userName) throws SQLException {

		Profile foundProfile = null;
		String script = "SELECT * FROM PROFILE WHERE UserName = '" + userName + "'";

		this.connectionSB.createSBConnection();

		this.connectionSB.createSBStatement();

		ResultSet rs = this.connectionSB.executeQuery(script);

		while (rs.next()) {

			foundProfile = new Profile(rs.getString("FirstName"), rs.getString("LastName"), rs.getString("UserName"),
					rs.getString("Password"), rs.getString("DefaultProject"));

		}

		this.connectionSB.close();

		return foundProfile;

	}

	/*
	 * Write Profile to database.
	 */
	public boolean writeProfile(Profile profile) throws SQLException {

		String[] values = new String[5];
		int i = 0;

		this.connectionSB.createSBConnection();
		this.connectionSB.createSBStatement();
		
		String[] keyValues = {profile.getUserName()};
		String[] keyNames  = {"UserName"};

		boolean recordExists = this.connectionSB.existingRecord("PROFILE", keyValues, keyNames);

		// Blank values will be skipped by the build script routine within SQLScriptor
		// class.
		if (!recordExists) {

			values[i++] = profile.getUserName();

		} else {

			values[i++] = "<skip>";

		}

		values[i++] = profile.getPassword();
		values[i++] = profile.getFirstName();
		values[i++] = profile.getLastName();
		values[i++] = profile.getDefaultProject();

		String script = "";

		if (recordExists) {

			script = this.scripts.buildUpdateScript(this.connectionSB, "PROFILE", keyValues, keyNames,values);

			this.connectionSB.executeUpdate(script);

		} else {

			script = this.scripts.buildInsertScript("PROFILE", values);

			this.connectionSB.executeInsert(script);

		}

		this.connectionSB.close();

		// If got to here then no exceptions return successful update.
		return true;

	}

	/*
	 * Get userName.
	 */
	public String getUserName() {
		return this.userName;
	}

	/*
	 * Get First Name from Profile.
	 */
	public String getFirstName() {
		return this.profile.getFirstName();
	}

	/*
	 * Get Last Name from Profile.
	 */
	public String getLastName() {
		return this.profile.getLastName();
	}

	/*
	 * Get users profile
	 */
	public Profile getProfile() {
		return this.profile;
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
	 * Get the image from the User profile.
	 */
	public Image getProfileImage() throws IOException {

		return this.profile.getPhoto().getImage();

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

		this.currentProject = profile.getDefaultProject();
		this.profile = profile;

	}

	/*
	 * Set userName.
	 */
	public void setUserName(String userName) throws InvalidParameterException {

		if (!this.util.isStringFieldEmpty(userName)) {

			this.userName = userName;

		} else {

			throw new InvalidParameterException("Parameter 'userName' is missing.");
		}
	}

	/*
	 * Set password.
	 */
	public void setPassword(String password) throws InvalidParameterException {

		if (!this.util.isStringFieldEmpty(password)) {

			this.password = password;

		} else {

			throw new InvalidParameterException("Parameter 'password' is missing.");

		}
	}

	/*
	 * Set the first name through the User for later retrieval and writing back to
	 * the profile file.
	 */
	public void setFirstName(String firstName) {
		this.profile.setFirstName(firstName);
	}

	/*
	 * Set the last name through the User for later retrieval and writing back to
	 * the profile file.
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
	 * Set Project the user is currently logged into. Also sets Profile so any new
	 * info is updated back to file
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
