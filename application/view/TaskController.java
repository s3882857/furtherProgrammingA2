package application.view;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import smartBoard.WorkSpace.Basket;
import smartBoard.WorkSpace.Task;
import smartBoard.WorkSpace.WorkSpaceManager;
import utilities.FXUtilities;

/*
 * Main Task Controller. Handles the coordination between the Task data and the 
 * TaskView. 
 */
public class TaskController {

	@FXML
	private AnchorPane taskPane;//

	@FXML
	private Label lblDelete;//

	@FXML
	private Label lblEdit;//

	@FXML
	private Label lblTaskName;

	@FXML
	private Label lblDue;

	@FXML
	private Label lblDueDateIndicator;

	private Task task;

	private FXUtilities util;

	private Stage stage;
	private Parent root;
	private Scene scene;

	/*
	 * Instantiate a new Task Controller for each task.
	 */
	public TaskController() {

		this.util = new FXUtilities();
		this.stage = new Stage();
		this.root = null;
		this.scene = null;

	}

	/*
	 * Set the name of the task view.
	 */
	public void setTaskName(String taskName) {

		this.lblTaskName.setText(taskName);

	}

	/*
	 * set the data for the task which this controller handles.
	 */
	public void setTask(Task task) {

		this.task = task;
		setTaskName(this.task.getName());
		
		dueDateToggle();

	}

	/*
	 * Delete a Task and the view associated with it.
	 */
	@FXML
	public void deleteTask(MouseEvent event) {

		int row = 0, col = 0;

		updateTaskData(event);

		String basketName = this.task.getParentName();
		String projectName = this.task.getProjectName();

		Basket basket = WorkSpaceManager.getInstance().findBasket(basketName, projectName);

		try {

			// Update the row count, user can remain within the current column.
			row = WorkSpaceManager.getInstance().getTaskIndex(this.task) + 1;
			col = WorkSpaceManager.getInstance().getBasketIndex(basket);

			if (WorkSpaceManager.getInstance().removeTask(this.task)) {

				TabPaneController.getInstance().removeRowItem(col, row);

			}

		} catch (SQLException sqle) {

			this.util.alertMessage("Task Deletion",
					"Record was not deleted. The following error occurred " + sqle.getMessage(), AlertType.INFORMATION,
					null);

		}

	}

	/*
	 * Start the drag a Task View panel process.
	 */
	@FXML
	public void taskOnDragDetected(MouseEvent event) {

		Dragboard db = this.taskPane.startDragAndDrop(TransferMode.ANY);

		ClipboardContent cb = new ClipboardContent();

		Node node = (Node) event.getSource();

		String nodeID = node.getId();

		cb.putString("Source::" + nodeID);
		
		db.setContent(cb);

		event.consume();

	}

	/*
	 * Get the Node source value coordinates and re-locate switch with the target.
	 */
	@FXML
	public void taskOnDragDropped(DragEvent event) {

		try {
			String taskSource = event.getDragboard().getString();
			Node targetNode = (Node) event.getTarget();
			String targetNodeID = "";
			
			if(targetNode.getId()!=null) {
				targetNodeID = targetNode.getId(); 
			}
	
			if (!this.util.isStringFieldEmpty(taskSource) && taskSource.contains("Source::") && targetNodeID.contains(":")) {
	
				String sourceID = taskSource.split("::")[1];
	
				Task sourceTask = WorkSpaceManager.getInstance().findTask(sourceID);
				Task targetTask = WorkSpaceManager.getInstance().findTask(targetNode.getId());
	
				// Make sure we are dealing with a Task related Node.
				if (sourceTask != null && targetTask != null) {
	
					
					String[] sourceCoordinates = sourceID.split(":");
					String[] targetCoordinates = targetNode.getId().split(":");
	
					int sourceCol = Integer.parseInt(sourceCoordinates[1]);
					int sourceRow = Integer.parseInt(sourceCoordinates[2]) + 1;
	
					int destCol = Integer.parseInt(targetCoordinates[1]);
					int destRow = Integer.parseInt(targetCoordinates[2]) + 1;
	
					if (destCol != sourceCol || destRow != sourceRow) {
	
						if (WorkSpaceManager.getInstance().swapTasks(sourceTask, targetTask)) {
	
							TabPaneController.getInstance().switchNodes(sourceCol, sourceRow, destCol, destRow);
	
						} else {
	
							this.util.alertMessage("Task swap failed", "Duplicate task name found. Unable to switch tasks",
									AlertType.INFORMATION, null);
	
						}
	
					}
	
					event.consume();
	
				}
	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void taskOnDragOver(DragEvent event) {

		if (event.getDragboard().hasString()) {

			event.acceptTransferModes(TransferMode.ANY);
			event.consume();

		}

	}

	/*
	 * Re-Load and update the Task into the view tab.
	 */
	@FXML
	public void loadTaskView(MouseEvent event) {

		try {

			updateTaskData(event);

			int height = 650, width = 620;

			String title = "SmartBoard - Task View";

			URL url = getClass().getResource(ApplicationViews.TASK_VIEW);

			FXMLLoader loader = new FXMLLoader(url);

			BorderPane borderPane = loader.load();

			this.root = borderPane;

			this.scene = new Scene(this.root, height, width);
			this.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

			// Create a new screen page, make sure only one is open at a time.
			if (this.stage == null) {
				this.stage = new Stage();
			}

			// Find the latest versions. Ensure there the latest.
			Basket basket = WorkSpaceManager.getInstance().findBasket(this.task.getParentName(),
					this.task.getProjectName());
			Task task = WorkSpaceManager.getInstance().findTask(this.task);

			setTaskViewController(loader, basket, task);

			this.stage.setScene(this.scene);

			this.stage.setTitle(title);
			this.stage.setHeight(height);
			this.stage.setWidth(width);

			this.stage.show();

		} catch (IOException ex) {

			this.util.alertMessage("Critical Error", "Critical error occurred. \n" + ex.getMessage(), AlertType.ERROR,
					null);

		}

	}

	/*
	 * Set the parameters for the WorkSpaceAddTaskController.
	 */
	public void setTaskViewController(FXMLLoader loader, Basket basket, Task task) {

		TaskViewController taskViewController = loader.getController();

		taskViewController.initTaskValues(basket, task);

	}

	/*
	 * Update Task Data.
	 */
	public void updateTaskData(MouseEvent event) {

		Basket basket = WorkSpaceManager.getInstance().findBasket(this.task.getParentName(),
				this.task.getProjectName());

		int col = WorkSpaceManager.getInstance().getBasketIndex(basket);
		int row = WorkSpaceManager.getInstance().getTaskIndex(this.task);

		if (col >= 0 && row >= 0) {

			int projectIndex = TabPaneController.getInstance().getTabPane().getSelectionModel().getSelectedIndex();

			String coordinates = projectIndex + ":" + col + ":" + row;

			this.task = WorkSpaceManager.getInstance().findTask(coordinates);

		}

		/*
		 * This won't help, but at this point it is all that can be done. We need the
		 * Node that holds the Task coordinates.
		 */
		String projectName = TabPaneController.getInstance().getCurrentTab().getText();

		if (!projectName.equals(this.task.getProjectName())) {
			this.task.setProjectName(projectName);
		}

	}
	
	
	/*
	 * toggle due date values.
	 */
	public void dueDateToggle() {
		
		long scheduledDay = 0;
		long completedDay = 0;
		long todaysDay    = 0;
		long dueIn        = 0;
		
		if(this.task.getScheduledLocalDate()!=null || this.task.getDateCompletedLocalDate()!= null) {
		
			scheduledDay = this.task.getScheduledLocalDate().toEpochDay();
			todaysDay    = LocalDate.now().toEpochDay();
			dueIn        = scheduledDay - todaysDay;
			
			if(scheduledDay>0 || completedDay > 0) {
				
				String colorMsg ="";
				// Plenty of color levels to re-arrange later on, but just done the
				// standard requested. Duplicate logic, but leave for now. 
				if(this.task.getDateCompletedLocalDate() != null) {
					colorMsg = "green";
				} else if(dueIn==0) {
					colorMsg = "orange";
				} else if(dueIn<0) {
					colorMsg = "orange";
				} else if(dueIn<7) {
					colorMsg = "yellow";
				} else {
					colorMsg = "green";
				}
				
				this.lblDueDateIndicator.setStyle("-fx-background-color:" + colorMsg + ";");
				this.lblDueDateIndicator.setText(this.task.getScheduledLocalDate().toString());
				
				if(this.task.getDateCompletedLocalDate() != null) {
					this.lblDueDateIndicator.setText("Completed - " + this.task.getDateCompletedLocalDate().toString());
				}
				
				this.lblDueDateIndicator.setVisible(true);
				this.lblDue.setVisible(true);
				
			}
		}
	}
}
