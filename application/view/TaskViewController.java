package application.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import smartBoard.WorkSpace.Basket;
import smartBoard.WorkSpace.Task;
import smartBoard.WorkSpace.WorkSpaceManager;
import utilities.FXUtilities;

/*
 * Handles the operation and interaction between the Task model and the 
 * TaskView.
 */
public class TaskViewController {

	@FXML
	private Button btnCancel;

	@FXML
	private Button btnOK;

	@FXML
	private Label lblAddCheckList;

	@FXML
	private Label lblDescription;

	@FXML
	private Label lblDueDate;

	@FXML
	private Label lblDelete;
	
	@FXML
	private Label lblDueDateTwo;
	 
	@FXML
	private Label lblTask;
	
	@FXML
	private CheckBox chkComplete;

	@FXML
	private DatePicker datePicker;

	@FXML
    private Label lblStatus;
	    

	@FXML
	private TextArea txaDescription;

	@FXML
	private TextField txtTaskName;

	@FXML
	private Label lblMessage;

	private Basket basketOwner;
	
	
	private Task task;

	private FXUtilities util;

	private boolean existingTask;
	
	private int col;
	
	
	/*
	 * Instantiate a TaskView Controller for each task's view.
	 */
	public TaskViewController() {

		this.util = new FXUtilities();
		this.basketOwner = null;
		this.existingTask = false;

	}

	/*
	 * Add a due date if found. Resets fields to display if necessary.
	 */
	@FXML
	public void addDueDate() {
		
		this.lblMessage.setText("");
		
		setDueDateVisible();
		
	}
	
	/*
	 * exit the controller.
	 */
	@FXML
	public void exitTaskEntry(ActionEvent event) {

		close();

	}

	/*
	 * Save all Task data or create a new Task.
	 * Performed with every 'Save' click.
	 */
	@FXML
	public void saveTask(ActionEvent event) {

		this.lblMessage.setText("");
		
		int col = 0, row = 0;
		boolean closeOK = true;

		updateBasket();
		
		if (this.existingTask) {

			setTaskData();
			row = WorkSpaceManager.getInstance().getTaskIndex(this.task) + 1;
			
		} else {

			if (findTask() == null) {

				this.task = new Task(this.txtTaskName.getText(), this.txaDescription.getText(),this.basketOwner.getName(), this.basketOwner.getParentName());
				setTaskData();
				row = WorkSpaceManager.getInstance().findBasket(this.basketOwner.getName(), this.basketOwner.getParentName()).getTasks().size() + 1;
				WorkSpaceManager.getInstance().findBasket(this.basketOwner.getName(), this.basketOwner.getParentName()).add(this.task);
				
			} else {

				this.lblMessage.setText("A task with the same name already exists. Please try again.");
				closeOK = false;
				
			}

		}

		if (this.task != null) {

			try {

				col = WorkSpaceManager.getInstance().getBasketIndex(this.basketOwner);

				URL url = getClass().getResource(ApplicationViews.WORKSPACE_TASK_VIEW);

				FXMLLoader loader = new FXMLLoader(url);

				AnchorPane pane = loader.load();

				setTaskController(loader, task);

				
				if (this.existingTask) {
					
					TabPaneController.getInstance().replacePane(pane, row, col);

				} else {

					TabPaneController.getInstance().addPane(pane, row, col);

				}

			} catch (IOException ex) {

				this.util.alertMessage("Add Task Failure", "Critical error occurred. \n " + ex.getMessage(),
						AlertType.ERROR, null);

				closeOK = false;
				
			}

		}

		if(closeOK) {
			close();
		}

	}

	/*
	 * Initialize all object values required. Basket, Task, screen values.
	 */
	public void initTaskValues(Basket basket, Task task) {

		if (basket != null) {

			this.basketOwner = basket;
			this.col = WorkSpaceManager.getInstance().getBasketIndex(basket);

		}

		if (task != null) {

			this.task = task;
			this.existingTask = true;
			this.txtTaskName.setText(task.getName());
			this.txaDescription.setText(task.getDescription());
			
			
			
			if(this.task.isComplete()) {
				this.chkComplete.selectedProperty().set(true);
			}
			
			this.datePicker.setValue(this.task.getScheduledLocalDate());
						
			long scheduledDay = 0;
			long todaysDay    = 0;
			long dueIn        = 0;
			
			if(this.task.getScheduledLocalDate()!=null || this.task.getDateCompletedLocalDate()!= null) {
			
				scheduledDay = this.task.getScheduledLocalDate().toEpochDay();
				todaysDay    = LocalDate.now().toEpochDay();
				dueIn        = scheduledDay - todaysDay;
							
			}
				
			if(scheduledDay>0 || this.task.getDateCompletedLocalDate()!= null) {
				
				String statusMsg = "";
			
				if(this.task.getDateCompletedLocalDate()!= null) {
					statusMsg = "Complete";
				} else if(dueIn==0) {
					statusMsg = "Due Today";
				} else if(dueIn<0) {
					statusMsg = "Overdue";				
				} else if(dueIn<7) {
					statusMsg = "Critical";
				} else {
					statusMsg = "On Track";
				}

				this.lblStatus.setText(statusMsg);
				
				setDueDateVisible();
				
			}
							
		}

	}

	public void setTaskController(FXMLLoader loader, Task task) {

		TaskController taskController = loader.getController();

		taskController.setTask(task);

	}

	/*
	 * Close the task view screen
	 */
	public void close() {
		Stage stage = (Stage) this.txtTaskName.getScene().getWindow();
		stage.close();
	}
	
	public void setTaskData() {
		
		// Delete from the database if the name changes.
		if (!this.task.getName().equals(this.txtTaskName.getText())) {

			try {

				this.task.deleteTask();

			} catch (SQLException e) {

				this.util.alertMessage("Delete Task Failure", "An SQL Database error occurred. Contact Administrator.\n" + e.getMessage(),
						AlertType.ERROR, null);

			}

		}

		if(this.chkComplete.isSelected()) {
			
		} else {
			
		}
			
		this.task.setComplete(this.chkComplete.isSelected());
		
		if(this.datePicker.getValue()!=null) {
			
		} else {
			
		}
		
		this.task.setScheduledDate(this.datePicker.getValue());
		this.task.setName(this.txtTaskName.getText());
		this.task.setDescription(this.txaDescription.getText());
		
	}
	
	public Task findTask() {
		
		Task task = WorkSpaceManager.getInstance().findTask(this.txtTaskName.getText(), this.basketOwner.getParentName(), this.basketOwner.getName());
		
		return task;
		
	}
	
	/*===============================================================================================================
	 * Update the current basket object. Precautionary only. The Basket is always passed through as 
	 * an initial parameter and will not change in the meantime.
	 * 
	 * However, this is done in the interest of portability in case this logic is used elsewhere in 
	 * the future.
	 * 
	 * ==============================================================================================================
	 * However, this solution, below, will not update the correct basket in all circumstances. Especially, if it is 
	 * removed from the current context. Programmer user beware.
	 * ==============================================================================================================
	 */
	public void updateBasket() {
		
		Basket basket = WorkSpaceManager.getInstance().findBasket(this.basketOwner.getName(), this.basketOwner.getParentName());
		
		// Update basket based on last known name values.
		if(basket!=null) {
	
			this.basketOwner = basket;
			
		} else {
			
			// Update based on last known column value.
			basket = WorkSpaceManager.getInstance().findBasket(TabPaneController.getInstance().getTabPane().getSelectionModel().getSelectedIndex(), this.col);
		
			if(basket!=null) {
			
				this.basketOwner = basket;
			
			}
			
		}
						
	}
	
	/*
	 * Set the Due Date fields to visible.
	 */
	public void setDueDateVisible() {
	
		this.chkComplete.setVisible(true);
		this.datePicker.setVisible(true);
		this.lblStatus.setVisible(true);
		
		if(!this.util.isStringFieldEmpty(this.datePicker.toString())) {
			
			this.lblDelete.setVisible(true);
			this.lblDueDateTwo.setVisible(true);
			this.lblDueDate.setVisible(false);
			this.lblStatus.setVisible(true);
			
		} else {
			
			this.lblDelete.setVisible(false);
			this.lblDueDateTwo.setVisible(false);
			this.lblDueDate.setVisible(true);
			this.lblStatus.setVisible(false);
			
		}

	}
	
	@FXML
	public void clearDueDates(MouseEvent event) {

		this.chkComplete.setVisible(false);
		this.datePicker.setVisible(false);
		this.lblStatus.setVisible(false);
		this.lblDueDate.setVisible(true);
		this.lblDueDateTwo.setVisible(false);
		this.lblDelete.setVisible(false);
		
		this.chkComplete.setSelected(false);
		this.datePicker.setValue(null);
		this.lblStatus.setText("");
		
    }
	
}
