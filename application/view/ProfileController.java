package application.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import smartBoard.User.Profile;
import smartBoard.User.User;
import utilities.FXUtilities;


/*
 * Controller for the Profile model. Handles all requests from the view.
 * Contains and handles the Profile model class.
 */
public class ProfileController {

	// Profile image display object. Can be click on to display dialog box.
	// Or dragged over to drop images into.
	@FXML
	private ImageView imgProfile;
	
	
	// Label for an empty image display. Bill board for the 
	// user to see where to drag file to.
	@FXML
	private Label lblImgProfile;
	
	// User name label/prompt
	@FXML
	private Label lblUserName;
	
	// Password label/prompt
	@FXML
    private Label lblPassword;
	
	// First name label/prompt
	@FXML
    private Label lblFirstName;
	
	// Last name label/prompt
	@FXML
    private Label lblLastName;
	
	// Message label. Used to display all error and information messages to the user.
	@FXML
	private Label lblMessage;
	
	// User name input field
	@FXML 
	private TextField txtUserName;
	
	// User password input field
	@FXML
	private TextField txtPassword;
	
	// Users first name input field
	@FXML 
	private TextField txtFirstName;
	
	// Users last name input field
	@FXML 
	private TextField txtLastName;
	
	// Hidden field stores the ImageViews filename.
	@FXML 
	private TextField txtProfileImageFileName;
	
	// Create a new user button. Loads ProfileView.
	@FXML
	private Button btnCreateUser;
	
	// Closes application.
	@FXML
	private Button btnClose;
		
	// Stores miscellaneous code methods. 
	private FXUtilities util;
	
	// Stores the allowed list of characters.
	private String validCharacters;
	
	// The model for this controller. Contains all user related data.
	private Profile profile;
	
	/*
	 * Create a Controller instance for the Profile model.
	 */
	public ProfileController() {
		
		this.util = new FXUtilities();
		this.validCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		
		// Assume profile is non-existent at this point.
		this.profile = null;	
		
	}
	
	/*
	 * Called after instantiation. All required objects are now loaded.
	 * Load Profile model if found.
	 */
	public void initialize() {
		
		/*
		 * This will only be set if the User already exists (Logged in). Otherwise null means a new profile.
		 */
		this.profile = User.getInstance().getProfile();
		
		if(this.profile!=null) {
			
			try {
				setProfileData();
			}
			catch(IOException ioe) {
				this.lblMessage.setText("Unable to read profile image file");
			}
			
		}
			
	}
	
	/*
	 * Set all profile model data to the profile view fields.
	 */
	public void setProfileData() throws IOException{
		
		setUserName(this.profile.getUserName());
		setFirstName(this.profile.getFirstName());
		setLastName(this.profile.getLastName());
		
		// Can throw IOException.
		setImageView(this.profile.getPhoto().getImage());
		setPassword(this.profile.getPassword());
		
	}
	
	/*
	 * Set the userName field.
	 */
	private void setUserName(String userName) {
		
		this.txtUserName.setText(userName);
		this.txtUserName.setDisable(true);
		this.txtPassword.setDisable(true);
		this.btnCreateUser.setText("Save");
				
	}
	
	/*
	 * Set the first name field.
	 */
	private void setFirstName(String firstName) {
		this.txtFirstName.setText(firstName);
	}
	
	/*
	 * Set the last name field.
	 */
	private void setLastName(String lastName) {
		this.txtLastName.setText(lastName);
	}
	
	/*
	 * Set the image. Clear image text.
	 */
	private void setImageView(Image img) {
		
		if(img!=null) {
			
			this.imgProfile.setImage(img);
			
		}
		
		this.lblImgProfile.setText("");
		
	}
	
	/*
	 * Set the file name of the image. 
	 * Hidden field used to upload the image and store it.
	 * See Photo class.
	 */
	public void setProfileImageFileName(String fileName) {
		this.txtProfileImageFileName.setText(fileName);
	}
	
	/*
	 * Set the password field.
	 */
	private void setPassword(String password) {
		this.txtPassword.setText(password);
	}
	
	/*
	 * Validate the user name. Make sure there are no
	 * inappropriate characters.
	 */
	@FXML
	public void validateUserName(Event e) {
	
		validateUsrName();
		
	}
	
	/*
	 * Generic validation. Make sure there are no
	 * inappropriate characters.
	 */
	public boolean validateUsrName(){
		
		String userName = this.txtUserName.getText();
		boolean valid = true;
		
		if(!validateCharacters(userName, this.validCharacters)) {
			
			valid = false;
			this.lblMessage.setText("Invalid characters entered at field '" + this.txtUserName.getId().substring(3) + "'");
			
		} else {
			
			
			this.lblMessage.setText("");
			
		}
			
		return valid;
		
	}
	
	/*
	 * Generic validation method. 
	 * Makes sure there are no inappropriate characters entered. 
	 */
	@FXML
	public void validateName(Event event) {
		
		TextField txtField = null;
		
		if(event.getSource() instanceof TextField) {
			txtField = (TextField) event.getSource();
			validateNme(txtField);				
		}
		
	}
	
	/*
	 * Generic validation method. 
	 * Makes sure there are no inappropriate characters entered. 
	 */
	public boolean validateNme(TextField textField) {
		
		boolean valid = true;
		
		if(!this.validateCharacters(textField.getText(), this.validCharacters + "- '")) {
			this.lblMessage.setText("Invalid characters entered at field '" + textField.getId().substring(3) + "'");
			valid = false;
		} else {
			this.lblMessage.setText("");
		}
		
		return valid;
	}
	
	/*
	 * Validate characters.
	 */
	private boolean validateCharacters(String data, String validCharacters) {
		
		return this.util.validCharacters(data, validCharacters);
		
	}
	
	/*
	 * Load a File Dialog box. Retrieves an image file name. 
	 */
	@FXML
	public void loadFileDialogBox(Event event) {
		
		//System.out.println("# Button clicked");
		
		FileChooser fileChoose = new FileChooser();
		File dir = new File("../");
		FileInputStream fis = null;
		
		fileChoose.setTitle("Find an Image to display");
		fileChoose.setInitialDirectory(dir);
		
		List<String> list = new ArrayList<>();
		list.add("*.png");
		list.add("*.jpeg");
		list.add("*.jpg");
		ExtensionFilter extFilter = new FileChooser.ExtensionFilter("File Types", list);

		fileChoose.getExtensionFilters().add(extFilter);
		
		File file = fileChoose.showOpenDialog(null);
		
		try {
			
			// Only do something if a valid file is selected. 
			if(file!=null && file.exists()) {
								
				fis = new FileInputStream(file);
				
				this.imgProfile.setImage(new Image(fis));
				this.txtProfileImageFileName.setText(file.getAbsolutePath());
				this.lblImgProfile.setText("");	
				
				fis.close();
		
			} 
			
		}
		catch(IOException everything) {
		
			this.lblMessage.setTextFill(Color.RED);
			this.lblMessage.setText("Something went wrong. Try again.");
			try {
				fis.close();
			}
			catch(IOException ioe) {
				
			}
		}
		
	}
	
	/*
	 * Write the Profile to the database. The User model performs the actual write.
	 */
	@FXML
	public void writeProfile(Event event) {
		
		if(validateAllProfileFields()) {
		
			String userName = this.txtUserName.getText();
			String password = this.txtPassword.getText();
			
			boolean writeProfile = true;
			
			if(!this.util.isStringFieldEmpty(password) && !this.util.isStringFieldEmpty(userName)) {
							
				try {
					
					// Trying to create a new user that already exists in the database.
					if(!this.txtUserName.isDisabled()) {
						
						Profile profileDB = User.getInstance().readProfile(userName);
						
						if(profileDB!=null) {
							this.lblMessage.setText("A user profile already exists for this userName");
							writeProfile = false;
						}
						
					}
					
					if(writeProfile) {

						if(this.profile==null) {
							
							this.profile = new Profile(this.txtFirstName.getText(),this.txtLastName.getText(),userName,password,"");
												
						} else {
					
							this.profile.setFirstName(this.txtFirstName.getText());
							this.profile.setLastname(this.txtLastName.getText());
																			
						}
						
						/*
						 *  The image itself and/or filename are not stored in the database. (root directory/Users)
						 *  This will write the image to root/Users/ [userName]_[filename].[file suffix] using the 
						 *  Photo class.
						 */
						this.profile.setNewImage(this.txtProfileImageFileName.getText());
						
						if(User.getInstance().writeProfile(this.profile)) {
							
							User.getInstance().setProfile(this.profile);
							
							this.lblMessage.setTextFill(Color.GREEN);
							this.lblMessage.setText("Profile successfully updated");
							
						}
																								
					}
					
				}
				catch(SQLException sqle) {
					sqle.printStackTrace();
					this.lblMessage.setText("SQL Database error occurred. Contact Administrator for assistance");
				}
				catch(IOException ioe) {
					this.lblMessage.setText("Database Connection error occurred. Contact Administrator for assistance");
				}
				
					
			} else {
				
				this.lblMessage.setText("Username and/or password must not be empty");
				
			}
		}
	}
	
	/*
	 * Close this window.
	 */
	@FXML 
	public void close() {
		Stage stage = (Stage) this.btnClose.getScene().getWindow();
	    stage.close();
	}
	
	/*
	 * Validate all fields.
	 */
	public boolean validateAllProfileFields() {
		
		/*
		 *  Technically there are more fields than this, but
		 *  1, 3, and 4 are the only ones that are (so far) 
		 */
		
		int numFields = 4;
		boolean valid = true;
		
		for(int i = 1; i <= numFields ; i++) {
			
			switch(i) {
			case 1:
				valid = validateUsrName();
				break;
			
			case 3:
				
				valid = validateNme(this.txtFirstName);
				break;
			case 4:
				
				valid = validateNme(this.txtLastName);
				break;
			
			}
			
			if(valid==false) {
				break;
			}
			
		}
		
		return valid;
		
	}
	
	/*
	 * When the cursor moves over the imageView while dragging data. 
	 * Set the mode to 'ANY' if the dragBoard contains files.
	 * Initiates and prepares for transfer.
	 */
	@FXML 
	public void onDragOverImage(DragEvent event) {
		
		// Check if the clip-board has a file.
		if(event.getDragboard().hasFiles()) {
			
			// Set the event ready to send data.
			event.acceptTransferModes(TransferMode.ANY);
			
		}
		
		
	}
	
	/*
	 * When the user releases the mouse button over the ImageView while carrying files.
	 * Inspect the dragBoard find any files relevant and attempt to load into the 
	 * ImageView. 
	 */
	@FXML
	public void onDragDroppedImage(DragEvent event) {
		
		List<File> files = event.getDragboard().getFiles();
		this.lblMessage.setText("");
		
		try {
		
			String fileName = files.get(0).getAbsolutePath();
			String fileSuffix = fileName.split("\\.")[1];
			
			if(fileSuffix.equalsIgnoreCase("png") || fileSuffix.equalsIgnoreCase("jpg") || fileSuffix.equalsIgnoreCase("jpeg")) {
				
				FileInputStream fis = new FileInputStream(files.get(0));
				
				Image image = new Image(fis);
							
				this.imgProfile.setImage(image);
			
				this.txtProfileImageFileName.setText(fileName);
			
				this.lblImgProfile.setText("");
							
				event.consume();
				
			} else {
				
				this.lblMessage.setText("Incorrect file type or data is corrupted");
				
			}
			
		}
		catch(IOException ioe) {
			
			ioe.printStackTrace();
			this.lblMessage.setText("Incorrect file type or data is corrupted");
			
		}
		
	}
}
