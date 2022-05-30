package application.view;


import java.io.IOException;
//import java.net.URL;
import java.sql.SQLException;
import javafx.event.Event;
import javafx.fxml.FXML;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import smartBoard.User.User;
import utilities.FXUtilities;

/*
 * Login Controller. Handles all operation and interactions between the LoginView
 * and the User singleton data classes.
 */
public class LoginController {
	
	@FXML
	private TextField txtUserName;
	
	@FXML
	private TextField txtPassword;
	
	@FXML
	private Label lblUserName;
	
	
	@FXML
	private Label lblPassword;
	
	@FXML
	private Button btnLogIn;
	
	@FXML
	private Button btnClose;
	
	@FXML
	private Label lblNewUser;
	
	@FXML
	private Label lblMessage;
	
    private Stage profileStage;

    private Stage workspaceStage;

    private FXUtilities util;
	
    /*
     * Create a login controller for the User.
     * User is the facade for all user details.
     */
	public LoginController() {
		
		this.util = new FXUtilities();
	
	}
	
	/*
	 * User clicks Login. Checks login validatity. 
	 */
	@FXML
	public void loginButtonClicked() {
		
		this.lblMessage.setText("");
		
		String userName = "";
		String password = "";
		
		if(this.txtUserName.getText() != null) {
			userName = this.txtUserName.getText();
		}
		
		if (this.txtPassword.getText() != null) {
			password = this.txtPassword.getText();
		}
		
		boolean passwordCorrect = false;
	
		if(!this.util.isStringFieldEmpty(userName) && !this.util.isStringFieldEmpty(password)) {
			
			try {
			    // User is static this sets it, without having to do anything else.
				if(User.getInstance().getUser(userName)) {
				
					passwordCorrect = User.getInstance().passwordCheck(password);
					
					if(passwordCorrect) {
						
						this.lblMessage.setTextFill(Color.BLACK);
						this.lblMessage.setText("Please wait. Logging in...");
					
						loadWorkSpaceScreen();
						
					} else {
						
						this.lblMessage.setText("Incorrect username password combination");
						
					}
					
				} else {
					
					this.lblMessage.setText("Unable to find a valid profile for this username");
					
				}
			}
			
			catch(SQLException sqle) {
				
				this.lblMessage.setText(sqle.getMessage());
				
			}
			
		}
		
				
	}
	
	/*
	 * Close and exit everything.
	 */
	@FXML
	public void closeButtonClicked() {
		
		System.exit(0);
		
	}
	
	/*
	 * Starts the process for creating a new User. Loads the Profile screen.
	 */
	@FXML
	public void createNewUser(Event event) {
		
		loadProfileScreen();
		
	}
	
	/*
	 * Load Profile screen.
	 */
	@FXML
	public void loadProfileScreen() {
			
		try {
			
			FactoryStageLoader factoryLoader = new FactoryStageLoader();
		
			this.profileStage = factoryLoader.loadWindow(workspaceStage,"SmartBoard - Profile" , ApplicationViews.PROFILE_VIEW);
			this.profileStage.show();
		
		} catch(IOException ex) {
		
			this.util.alertMessage("Profile Screen Load","Critical error occurred. \n " + ex.getMessage(),AlertType.ERROR , null);
		
		}
		
	}

	/*
	 * Load the WorkSpace main screen
	 */
	@FXML
	public void loadWorkSpaceScreen() {
			
		try {
		
			FactoryStageLoader factoryLoader = new FactoryStageLoader();
		
			this.workspaceStage = factoryLoader.loadWindow(this.workspaceStage,"SmartBoard - Workspace" , ApplicationViews.WORKSPACE_VIEW);
			this.workspaceStage.show();
		
			close();
			
		} catch(IOException ex) {
		
			this.util.alertMessage("WorkSpace Screen Load","Critical error occurred. \n" + ex.getMessage(),AlertType.ERROR , null);
		
		}
	}
	
	/*
	 * Close the login screen
	 */
	public void close() {
		Stage stage = (Stage) this.txtUserName.getScene().getWindow();
	    stage.close();
	}

}	