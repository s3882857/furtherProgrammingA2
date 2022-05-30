package application.view;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import smartBoard.User.Profile;
import smartBoard.User.SmartBoardLoginException;
import smartBoard.User.User;
import smartBoard.WorkSpace.Basket;
import smartBoard.WorkSpace.Project;
import smartBoard.WorkSpace.SmartBoardWorkSpaceException;
import smartBoard.WorkSpace.Task;
import smartBoard.WorkSpace.WorkSpaceManager;
import utilities.FXUtilities;

/*
 * Handles all WorkSpace operations between the Workspace data structures
 * Project, Basket and Tasks. Also manages User related functions.
 * Is chiefly the Facade to the Users WorkSpace data.
 */
public class WorkSpaceController {

	// Outermost layout pane
	@FXML
	private BorderPane borderPane;//
	
	// Loaded into the AnchorPane
	@FXML
    private TabPane tabPane;//
	
	@FXML
	private Button btnLogout;//

	@FXML
	private Button btnProfile;//

	@FXML
	private ImageView imgProfileImage;//

	@FXML
	private Label lblQuote;//

	@FXML
	private Label lblProfileName;//
	
	@FXML
	private MenuItem itmNewProject;//

	@FXML
	private MenuItem itmNewBasket;//
	
	@FXML
    private MenuItem mitDelete;//
	
	 @FXML
	private MenuItem itmRenameProject;
	
	@FXML
    private MenuItem mitSetDefault;

    @FXML
    private MenuItem mitUnsetDefault;	 
	    
	@FXML
    private Label lblMessage;//
	
	private Stage profileStage;
	private Stage loginStage;

	private FXUtilities util;
	
	private FactoryStageLoader factoryLoader;

	/*
	 * Instantiate our workspace. The WorkSpace class is the facade for all of the
	 * workspace model items.
	 */
	public WorkSpaceController() {
		
		this.util = new FXUtilities();
		this.factoryLoader = new FactoryStageLoader();
		
	}

	/*
	 * Start the process off. Load all initial user workspace data.
	 */
	@FXML
	public void initialize() {
		
		try {
			
			// Load new workspace.
			WorkSpaceManager.getInstance().loadWorkSpace();
			
			setQuote(WorkSpaceManager.getInstance().getInspirationalQuote());
			
			setProfileName(User.getInstance().getFirstName() + " " + User.getInstance().getLastName());
			
			try {
				setImage(User.getInstance().getProfileImage());
			} catch (IOException ioe) {

				/*
				 * Leave image blank. Leaving it blank should be enough to go on. It didn't
				 * work.
				 */
			}
			
			loadMainWorkSpace();
			
			// Set the default tab to the stored default project.
			TabPaneController.getInstance().setCurrentTab(User.getInstance().getProjectLogin());
													
		} catch( IOException ioe) {
			
			ioe.printStackTrace();
			criticalErrorMessage(ioe);
			
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			criticalErrorMessage(sqle);
			
		} catch (SmartBoardLoginException sble) {
			sble.printStackTrace();
			criticalErrorMessage(sble);
			
		} catch (SmartBoardWorkSpaceException sbwse) {
			
			sbwse.printStackTrace();
			criticalErrorMessage(sbwse);
			
		}
	
	}

	public void setQuote(String quote) {
		this.lblQuote.setText(quote);
	}

	public void setImage(Image img) {

		if(img!=null) {
			this.imgProfileImage.setImage(img);
		}

	}
	
	public void setProfileName(String name) {
		
		this.lblProfileName.setText(name);
		
	}
	
	public void criticalErrorMessage(Exception e) {
		
		this.util.alertMessage("Critical Error","Critical error occurred. \n" + e.getMessage(),AlertType.ERROR , null);
		
	}

	/*
	 * Load Profile maintenance screen.
	 */
	@FXML
	public void loadProfileView() {

		try {
			
			//FactoryStageLoader factoryLoader = new FactoryStageLoader();
		
			this.profileStage = this.factoryLoader.loadWindow(this.profileStage,"SmartBoard - Profile" , ApplicationViews.PROFILE_VIEW);
			this.profileStage.show();
		
		} catch(IOException ex) {
		
			this.util.alertMessage("Critical Error","Critical error occurred. \n" + ex.getMessage(),AlertType.ERROR , null);
		
		}
		
	}

	/*
	 * Exit workspace. Reload login screen.
	 */
	@FXML
	public void exitWorkSpace(ActionEvent event) {

		Stage stage = (Stage) this.btnLogout.getScene().getWindow();
		
		User.getInstance().logout();
		
		boolean exitOK = true;
		
		try {
			
			WorkSpaceManager.getInstance().writeWorkSpace();
			
		}
		catch(SQLException ioe) {

			this.util.alertMessage("Critical Error","Critical error occurred. \n" + ioe.getMessage() + "\nMost likley invalid database characters have been entered.\nPlease edit the item before trying again.",AlertType.ERROR , null);
			exitOK = false;
		}
		
		if(exitOK) {
			
			stage.close();
		
			loadLoginScreen();
			
		}

	}

	/*
	 * Load login screen.
	 */
	public void loadLoginScreen() {

		try {
					
			this.loginStage = this.factoryLoader.loadWindow(this.loginStage,"SmartBoard - Login" , ApplicationViews.LOGIN_VIEW);
			this.loginStage.show();
					
		} catch(IOException ex) {
		
			this.util.alertMessage("Critical Error","Critical error occurred. \n " + ex.getMessage(),AlertType.ERROR , null);
		
		}

	}

	/*
	 * Load main workspace inside a scroll bar. 
	 */
	public void loadMainWorkSpace() throws IOException {
		
		/*
		 *  Before we load a new TabPane make sure there is no data remaining from
		 *  a previous user. It does this by setting the singleton's version to the current
		 *  Scene builder version.
		 */
		TabPaneController.getInstance().setTabPane(this.tabPane);
		
		loadProjects();
				
	}

	/*
	 * load projects to screen.
	 */
	public void loadProjects() throws IOException {
		
		for(Project project : WorkSpaceManager.getInstance().getProjects()) {
			
			TabPaneController.getInstance().addTab(project.getName());
						
			loadBaskets(project);
									
		}
		
	}
	
	/*
	 * Load Baskets to each project to screen.
	 */
	public void loadBaskets(Project currentProject) throws IOException {
		
		int basketNumber = 0;
			
		for(Basket basket : currentProject.getBaskets()) {
		
			//if(!basket.getDeleted()) {
			
				AnchorPane basketAnchorPane = null;
		    	
		  		URL url = getClass().getResource(ApplicationViews.WORKSPACE_ADD_TASK_VIEW);
				
				FXMLLoader loader = new FXMLLoader(url);
				
				basketAnchorPane = loader.load();
				
				TabPaneController.getInstance().addAnchorPane(basketAnchorPane);
				
				setBasketController(loader,basket);
	
				loadTasks(basket,basketNumber);
				
				basketNumber++;
				
			//}
		}
						
	}
	
	public void loadTasks(Basket basket, int basketNumber) throws IOException {
		
		// Start at 1. The first row is the basket name (row 0)
		int taskNumber = 1;
		
		for (Task task : basket.getTasks()) {
			
			Pane taskPane = null;
	    	
	  		URL url = getClass().getResource(ApplicationViews.WORKSPACE_TASK_VIEW);
			
			FXMLLoader loader = new FXMLLoader(url);
			
			taskPane = loader.load();
			
			TabPaneController.getInstance().addPane(taskPane,taskNumber,basketNumber);
			
			setTaskController(loader,task);
			
			taskNumber++;

		}
	}
	
	/*
	 * Add a new project to the workspace.
	 */
	@FXML
	public void addNewProject(ActionEvent event) {

		this.lblMessage.setText("");

		String projectName = this.util.fxInputBox("Create new project", "Enter project name: ");

		try {
			if (WorkSpaceManager.getInstance().findProject(projectName) == null) {
				
				WorkSpaceManager.getInstance().add(new Project(projectName));
				TabPaneController.getInstance().addTab(projectName);
				
			} else {
					
				this.lblMessage.setText("Project Addition Failure. A project with the same name already exists");
					//this.util.alertMessage("Project Addition Failure","A project with the same name already exists", null, null );
					
			}
				
			
		}
		catch(SQLException sqle) {
			
			this.util.alertMessage("Critical Error","A database SQL error occurred. Contact administrator with the below details.\n" + sqle.getMessage() , null, null );
		}
	}

	/*
	 * Add a new Basket to the workspace.
	 */
	@FXML
    public void addNewBasket(ActionEvent event) {
    
		this.lblMessage.setText("");
	    	   	
		try {
	    	// Get current tab name project name
	    	String projectName = TabPaneController.getInstance().getCurrentTab().getText();
	       	
	    	String basketName = this.util.fxInputBox("Create new Basket", "Enter the basket name: ");
	    	Basket basket = WorkSpaceManager.getInstance().findBasket(basketName, projectName);
	    	
	    	// Make sure the basket is not already added.
	       	if(basket == null && !this.util.isStringFieldEmpty(basketName)) {
	    		
	       		addBasket(basket,basketName,projectName);
	    		    	
	    	} else {
	    		
	    		this.lblMessage.setText("Basket already exists. Please try again using another name.");
	    	}
		}
		catch(SQLException sqle ) {
			
			this.util.alertMessage("Critical Error","A database SQL error occurred. Contact administrator with the below details.\n" + sqle.getMessage() , null, null );
			
		}
		catch(IOException ioe) {
			
			this.util.alertMessage("Critical Error","A database Input/Output error occurred. Contact administrator with the below details.\n" + ioe.getMessage() , null, null );
			
		}
		catch(SmartBoardWorkSpaceException sbwse) {
			
			this.util.alertMessage("Critical Error","A WorkSpace error occurred. Contact administrator with the below details.\n" + sbwse.getMessage() , null, null );
			
		}
    
	}

	/*
	 * Add Basket.
	 */
	public void addBasket(Basket basket, String basketName,String projectName) throws SmartBoardWorkSpaceException, IOException, SQLException {
		
		AnchorPane basketAnchorPane = null;
		
		if(basket == null) {
		
			basket = new Basket(basketName,projectName);
			
		} 
   		
   		WorkSpaceManager.getInstance().addBasket(basket, projectName);
		
		// Load the new basket code
		URL url = getClass().getResource(ApplicationViews.WORKSPACE_ADD_TASK_VIEW);
		
		FXMLLoader loader = new FXMLLoader(url);
		
		basketAnchorPane = loader.load();
	
		TabPaneController.getInstance().addAnchorPane(basketAnchorPane);
		
		setBasketController(loader,basket);
		
	}
	
	/*
	 * Set the parameters for the WorkSpaceAddTaskController.
	 */
	public void setBasketController(FXMLLoader loader, Basket basket) {
		
		BasketController basketController = loader.getController(); 
		
		basketController.setBasket(basket);
		
						
	}
	
	/*
	 * Set the parameters for the TaskController.
	 */
	//public void setTaskController(FXMLLoader loader, Task task, int col, int row) {
	public void setTaskController(FXMLLoader loader, Task task) {
		
		try {
			TaskController taskController = loader.getController();
			
			taskController.setTask(task);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Delete/remove project from workspace.
	 */
	@FXML
	public void deleteProject(ActionEvent event) {
		
		this.lblMessage.setText("");
		    	
    	Project project = WorkSpaceManager.getInstance().findProject(TabPaneController.getInstance().getCurrentTab().getText());
	    
    	try {
	    	if(WorkSpaceManager.getInstance().removeProject(project)) {
	    		    		
	    		TabPaneController.getInstance().removeTab(TabPaneController.getInstance().getCurrentTab());
	    			
	    	} else {
	    			
	    		this.lblMessage.setText("Project Deletion Failure. Project cannot be deleted. Project information still exists.");
	    		//this.util.alertMessage("Project Deletion Failure","Project cannot be deleted. Project information still exists. Please delete all project items first before trying again."
	    		//			,null , null);
	    			
	    	}
    	}
    	catch(SQLException sqle) {
    		this.util.alertMessage("Critical Failure","Database SQLException occurred while attempting to delete a Project.\n" + sqle.getMessage(),AlertType.ERROR , null);
    	}
	}	
	
	@FXML
    public void renameProject(ActionEvent event) {

		this.lblMessage.setText("");

		String currentProjectName = TabPaneController.getInstance().getCurrentTab().getText();
		
		String newProjectName = this.util.fxInputBox("Project Maintenance", "Enter new project name: ");

		try {
			if (WorkSpaceManager.getInstance().findProject(currentProjectName) == null) {
				
				// Cannot happen, unless there is data corruption. 
				this.lblMessage.setText("The current project you are attempting to amend does not exist");
				
			} else {
				
				if(WorkSpaceManager.getInstance().findProject(newProjectName) == null) {
	
					/*
					 *  This will delete the current project from the database only. When the user logs out
					 *  the database will be updated with the new records with all the same details. It will
					 *  rename all of the children and delete the old records from the database.   
					 */
					WorkSpaceManager.getInstance().reNameProject(currentProjectName, newProjectName);
					TabPaneController.getInstance().getCurrentTab().setText(newProjectName);
					
				} else {
					
					this.lblMessage.setText("The project name already exists. Please try again.");
					//this.util.alertMessage("Project Maintenance Failure","A project with the same name already exists", null, null );
					
				}
				
			}
		}
		catch (SQLException sqle) {
			
			this.util.alertMessage("Project Maintenance Failure","An SQL database Exception occurred.\n" + sqle.getMessage(), null, null );
			
		}
    }
	
    @FXML
    public void setTabDefault(ActionEvent event) {

    	try {
    	
    		Profile profile = User.getInstance().readProfile(User.getInstance().getUserName());
    		
    		profile.setDefaultProject(TabPaneController.getInstance().getCurrentTab().getText());
    		
    		User.getInstance().writeProfile(profile);
    		
    	}
    	catch(SQLException ioe) {
    		
    		this.util.alertMessage("Critical error", "A critical SQL database error occurred. Contact Administrator.\n" + ioe.getMessage(), AlertType.ERROR, null);
    		
    	}
    	
    	// set default to off, unset default to on.
    	this.mitSetDefault.setDisable(false);
    	this.mitUnsetDefault.setDisable(true);
    	
		//System.out.println("this.mitUnsetDefault " + this.mitUnsetDefault.disableProperty());
		//System.out.println("this.mitSetDefault " + this.mitSetDefault.disableProperty());
		
    }

    @FXML
    public void unsetTabDefault(ActionEvent event) {

     	try {
        	
    		Profile profile = User.getInstance().readProfile(User.getInstance().getUserName());
    		
    		profile.setDefaultProject("");
    		
    		User.getInstance().writeProfile(profile);
    		
    	}
    	catch(SQLException ioe) {
    		
    		this.util.alertMessage("Critical error", "A critical SQL database error occurred. Contact Administrator.\n" + ioe.getMessage(), AlertType.ERROR, null);
    		
    	}
     	
     	System.out.println("Set default to true, set unset default to false");
     	// Set default to on, Unset default to off
    	this.mitSetDefault.setDisable(true);
    	this.mitUnsetDefault.setDisable(false);
    	
		//System.out.println("this.mitUnsetDefault " + this.mitUnsetDefault.disableProperty());
		//System.out.println("this.mitSetDefault " + this.mitSetDefault.disableProperty());
    }
    
    @FXML
    public void refreshProjectMenu() {

    	
    	String selectedTab = TabPaneController.getInstance().getCurrentTab().getText();
    	String currentDefaultProject = "";
    	try {
        	
    		Profile profile = User.getInstance().readProfile(User.getInstance().getUserName());
    		
    		currentDefaultProject = profile.getDefaultProject();
    	
    		
    		if(currentDefaultProject.equals(selectedTab)){
    			//System.out.println("TRUE");
    		//	System.out.println("equals currentDefaultProject " + currentDefaultProject);
    			// Unset default to on, set default to off
    			this.mitUnsetDefault.setDisable(true);
    			this.mitSetDefault.setDisable(false);
    			
    		//	System.out.println("this.mitUnsetDefault " + this.mitUnsetDefault.disableProperty());
    		//	System.out.println("this.mitSetDefault " + this.mitSetDefault.disableProperty());
    			
    		} else {
    			
    			//System.out.println("FALSE");
    			//System.out.println("NOT equals currentDefaultProject " + currentDefaultProject);
    			// Unset default to off, set default to on
    			this.mitUnsetDefault.setDisable(false);
    			this.mitSetDefault.setDisable(true);
    			//System.out.println("this.mitUnsetDefault " + this.mitUnsetDefault.disableProperty());
    			//System.out.println("this.mitSetDefault " + this.mitSetDefault.disableProperty());
    		}
    		
    	}
    	catch(SQLException ioe) {
    		
    		this.util.alertMessage("Critical error", "A critical SQL database error occurred. Contact Administrator.\n" + ioe.getMessage(), AlertType.ERROR, null);
    		
    	}
    	    	
    }
    
}