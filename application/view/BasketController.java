package application.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import smartBoard.WorkSpace.Basket;
import smartBoard.WorkSpace.Task;
import smartBoard.WorkSpace.WorkSpaceManager;
import utilities.FXUtilities;

/*
 * The main Basket Controller. Handles all operations between the WorkSpaceBasketView and the data
 * Basket.
 */
public class BasketController {

	@FXML
    private SplitMenuButton smbAddTask;//
	
	@FXML
    private MenuItem itmDeleteBasket;//
	
	@FXML
	private MenuItem itmAddTask;//
	
	@FXML
    private MenuItem itmMoveLeft;//

    @FXML
    private MenuItem itmMoveRight;//
    
    @FXML
    private MenuItem itmRenameBasket;
 
    @FXML
    private Label lblBasketName;//
    
    private Basket basket;
   
    private FXUtilities util;
    
    private Stage stage;
    private Scene scene;
    private Parent root;
    
    public BasketController() {
    	
    	this.util = new FXUtilities();
    	    	
    }    
    
    @FXML
    public void initialize() {
    	
    }
    
    public void setBasket(Basket basket) {
    	
    	this.basket = basket;
    	    	
    	setBasketName(basket.getName());
    	
    	updateMenuDisplay();
    	    	
    }
    
    public void setBasketName(String basketName) {
    	
    	this.lblBasketName.setText(basketName);
    	
    }
    
       
    @FXML
    public void onMouseClickDropDown() {
    	
       	updateMenuDisplay();
       	
    }
    
    
    public void updateMenuDisplay() {
    	
    	updateBasketData();
    	
    	int numberBaskets = WorkSpaceManager.getInstance().findProject(this.basket.getParentName()).getBaskets().size();
    	    	
    	int col = WorkSpaceManager.getInstance().getBasketIndex(this.basket);
		if(col==0) {
	    		
    		// Cannot move left
    		this.itmMoveLeft.setDisable(true);
    		
    		// Can move right
    		if(numberBaskets>1) {
    			this.itmMoveRight.setDisable(false);
    		} else {
    			this.itmMoveRight.setDisable(true);
    		}
	    		
	    } else {
	    		
    		// Can always move to the left.
    		this.itmMoveLeft.setDisable(false);
    	    		
    		// Can move right.
    		if(numberBaskets > (col + 1)) {
    			this.itmMoveRight.setDisable(false);
    		} else {
    			this.itmMoveRight.setDisable(true);
    		}
    		
    	}
	    	
    	// Nothing to move. Overrides everything else.
    	if(this.basket.getTasks().size()==0) {
    		this.itmMoveRight.setDisable(true);
    		this.itmMoveLeft.setDisable(true);
    	}
    	
    }
    
    @FXML
    public void deleteBasket() {
    	
    	updateBasketData();
    	
    	int index = WorkSpaceManager.getInstance().getBasketIndex(this.basket);
    	
    	// Sanity check
    	if(index>=0) {
    		
	    	try {
		    	// Remove the Basket from the data. 
				if(WorkSpaceManager.getInstance().removeBasket(this.basket)) {	
		    				
					// Remove from the screen.
					TabPaneController.getInstance().removeColumn(index);
		   	    		
		    	} else {
		    		
		    		this.util.alertMessage("Delete failure","The Basket still contains valid tasks.\nPlease delete all tasks or move them to another Basket before trying again." ,null , null);
		    		
		    	}
	    	}
	    	catch(SQLException sqle) {
	    		this.util.alertMessage("Critical failure","Database SQL Exception occurred while deleting Basket." ,AlertType.ERROR , null);
	    	}
	    	catch(Exception e) {
	    		e.printStackTrace();
	    	}
	    	
    	}
    	
    }
    
    /*
     * Load the task view screen.
     */
    @FXML
    public void loadTaskView() {
    	
    	try {
    		
			int height = 650, width = 600;
			
			String title = "SmartBoard - Task View";
			
			URL url = getClass().getResource(ApplicationViews.TASK_VIEW);
			
			FXMLLoader loader = new FXMLLoader(url);
			
			BorderPane borderPane = loader.load();
			
			this.root  = borderPane;
			
			this.scene = new Scene(this.root,height,width);
			this.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			// Create a new screen page, make sure only one is open at a time.
			if(this.stage==null) {
				this.stage = new Stage();
			}
			
			setTaskViewController(loader,WorkSpaceManager.getInstance().findBasket(this.basket.getName(), this.basket.getParentName()),null);
			
			this.stage.setScene(this.scene);
			
			this.stage.setTitle(title);
			this.stage.setHeight(height);
			this.stage.setWidth(width);
			
			this.stage.show();
			
		
		} catch(IOException ex) {
		
			this.util.alertMessage("Critical Error","Critical error occurred. \n" + ex.getMessage(),AlertType.ERROR , null);
		
		}

    }
    
    public void setTaskController(FXMLLoader loader, Task task) {
    	
   		TaskController taskController = loader.getController();
		
   		taskController.setTask(task);
   						
    }
    
	/*
	 * Set the parameters for the WorkSpaceAddTaskController.
	 */
	public void setTaskViewController(FXMLLoader loader, Basket basket, Task task) {
		
		TaskViewController taskViewController = loader.getController(); 
		
		taskViewController.initTaskValues(basket,task);		
						
	}
	
    /*
     * Move the last task to the left column/basket.
     */
    @FXML
    public void moveLeft(ActionEvent event) {
    	
    	move(-1);
    	
    }

    /*
     * Move the last task to the right column/basket.
     */
    @FXML
    public void moveRight(ActionEvent event) {
    	
    	move(1);
    	
    }

    public void move(int leftRight) {
    	
    	int newCol = 0, newRow = 0;
    	
    	updateBasketData();
    	
    	int col = WorkSpaceManager.getInstance().getBasketIndex(this.basket);
    	
    	if(leftRight<0) {
    		
    		newCol = col - 1;
    		    		
    	} else {
    		
    		newCol = col + 1;
    		
    	}
    	
       	if(newCol>=0) {
       		
       		Task lastTask = null;
        	int lastRow = 0;
        	
        	for(Task task : this.basket.getTasks()) {
        		
        		lastTask = task;
        		lastRow++;
        		
        	}
    	
    		if(lastTask!=null) {
	    
    			Basket sideBasket = WorkSpaceManager.getInstance().findProject(lastTask.getProjectName()).findBasket(newCol);
	    	
    			newRow = sideBasket.getTasks().size();
	    		
	    		if(newRow<=0) {
	    			newRow = 1;
	    		} else if(newRow>0) {
	    			newRow++;
	    		}
	    		
	    		if(WorkSpaceManager.getInstance().moveTask(lastTask, sideBasket)) {
		    	
	    			TabPaneController.getInstance().moveNode(col, lastRow, newCol,newRow);
		    	
	    			this.basket.removeTask(lastTask);
	    			
	    		} else {
	    			
	    			this.util.alertMessage("Task Move Failure","Duplicate Task found. Unable to move task to destination.", AlertType.INFORMATION,null);
	    		}
	    		
		    	//WorkSpaceManager.getInstance().displayWorkSpaceHelper(1);
    		}
    		
	    }
    }
    
	@FXML
	public void renameBasket(ActionEvent event) {
		
		String newBasketName = this.util.fxInputBox("Basket Maintenance", "Enter new basket name: ");
	
		if(!this.util.isStringFieldEmpty(newBasketName)) {
			
			updateBasketData();
			
			String currentBasketName = this.basket.getName();
			int col = WorkSpaceManager.getInstance().getBasketIndex(this.basket);
									
			try {
					
				if(WorkSpaceManager.getInstance().findBasket(TabPaneController.getInstance().getTabPane().getSelectionModel().getSelectedIndex(), col) != null) {
		
					/*
					 *  This will delete the current basket from the database and its children. When the user logs out
					 *  the database will be updated with the new records with all the same details. It will
					 *  rename all of the children.   
					 */
					WorkSpaceManager.getInstance().reNameBasket(currentBasketName, newBasketName,this.basket.getParentName());
					this.lblBasketName.setText(newBasketName);
					this.basket.setName(newBasketName);
						
				}
				
			}
			catch (SQLException sqle) {
				
				this.util.alertMessage("Basket Maintenance Failure","An SQL database Exception occurred.\n" + sqle.getMessage(), null, null );
				
			}
			
		}
	}
	
	/*
	 * Make sure all Basket data is current.
	 */
	public void updateBasketData() {
		
    	String parentName = TabPaneController.getInstance().getCurrentTab().getText();
    	
    	if(!parentName.equals(this.basket.getParentName())) {
    		this.basket.setParentName(parentName);
    	}
    	
	}
}

