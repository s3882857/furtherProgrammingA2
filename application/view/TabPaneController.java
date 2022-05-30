package application.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/*
 * Handles everything to do with the main TabPane located within the WorkSpace.
 * Adds, removes, and moves Nodes/Tasks etc. 
 * This has evolved into a Factory like class for the TabPane.
 */
public class TabPaneController {

	// Static version of this class.
	private static TabPaneController tabPaneController;
	
	/*
	 *  The main tabPane holding all the workspace items.
	 *  Putting this in a singleton enables access to the workspace display
	 *  from various controllers.
	 */
	private TabPane tabPane;
	
	private ObservableList<Node> list;
	
	// Margin on the basket and task
	private int margins = 4;
	
	/*
	 * Instantiate the TabPaneController. Made private as there is only
	 * one version required. See getInstance static.
	 */
	private TabPaneController() {
		
		this.list = FXCollections.observableArrayList();
		
	}
	
	/*
	 * Get a singleton verison of the main TabPane. This holds all the workspace for a user.
	 * All assignments etc are handled through this single point.
	 */
	public synchronized static TabPaneController getInstance() {
		
		if(TabPaneController.tabPaneController==null) {
			
			TabPaneController.tabPaneController = new TabPaneController();
			
		}
		
		return TabPaneController.tabPaneController;
		
	}
	
	
	/*
	 * Add a new tab to the TabPane.
	 */
	public void addTab(String tabName) {
		
		ScrollPane scrollPane = new ScrollPane();
				
		Tab tab = new Tab(tabName);

		GridPane gridPane = new GridPane();

		scrollPane.setContent(gridPane);
		
		tab.setContent(scrollPane);

		this.tabPane.getTabs().add(tab);

		if(!this.tabPane.isVisible()) {
			
			this.tabPane.setVisible(true);
			
		}
		
		scrollPane.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		scrollPane.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		
		setCurrentTab(tab);
		
	}
	

	/*
	 * Add an AnchorPane (Happens when adding a Basket)
	 */
	public  void addAnchorPane(AnchorPane anchorPane) {
		
		if(anchorPane != null) {
			
			int col = getCurrentGridPane().getColumnCount();
			
			getCurrentGridPane().addColumn(col, anchorPane);
			
			Node newNode = getNode(col, 0);
			
			/*
			 *  Set the current coordinate location of the Nodes Task within the data.
			 *  Project Index : Basket index : Task
			 */                                         
			String newDataCoordinate = this.tabPane.getSelectionModel().getSelectedIndex() + ":" + col + ":" + "0";
			
			newNode.setId(newDataCoordinate);
			
			GridPane.setMargin(anchorPane, new Insets(margins,margins,margins,margins));
						
		}
		
	}
	
	/*
	 * Add a Pane (Happens when adding a Task) 
	 */
	public void addPane(Pane pane, int row, int col) {
		
		if(pane != null) {
		
			getCurrentGridPane().add(pane, col, row);
			
			Node newNode = getNode(col, row);

			/*
			 *  Set the current coordinate location of the Nodes Task within the data.
			 *  Project Index : Basket index : Task
			 */                                         
			String newDataCoordinate = this.tabPane.getSelectionModel().getSelectedIndex() + ":" + col + ":" + (row - 1);
			
			newNode.setId(newDataCoordinate);
			
			GridPane.setMargin(pane, new Insets(margins,margins,margins,margins));
			
														
		}
		
	}
	
	/*
	 * Replace a Pane remove old put a new one with new details (Happens after updating the Task) 
	 */
	public void replacePane(Pane pane, int row, int col) {
		
		if(pane != null) {
		
			// Remove the Node.
			Node currentNode = getNode(col,row);
			
			if(currentNode!=null) {
			
				getCurrentGridPane().getChildren().remove(currentNode);
			
			}
			
			getCurrentGridPane().add(pane, col, row);
			
			Node newNode = getNode(col, row);

			/*
			 *  Set the current coordinate location of the Nodes Task within the data.
			 *  Project Index : Basket index : Task
			 */                                         
			String newDataCoordinate = this.tabPane.getSelectionModel().getSelectedIndex() + ":" + col + ":" + (row - 1);
			
			newNode.setId(newDataCoordinate);
			
			GridPane.setMargin(pane, new Insets(margins,margins,margins,margins));
			
														
		}
		
	}	
	
	/*
	 * remove a tab. Delete project.
	 */
	public void removeTab() {
		
		getTabPane().getTabs().remove(getCurrentTab());
		
	}
	
	public void removeTab(Tab tab) {
		
		if(tab==null) {
		
			removeTab();
			
		} else {
			
			getTabPane().getTabs().remove(tab);
			
		}
		
	}
	
	/*
	 * remove a basket/column
	 */
	public void removeColumn(int col) {
		
		Node currentNode = getNode(col,0);
		
		if(currentNode!=null) {
		
			getCurrentGridPane().getChildren().remove(currentNode);
		
		}
				
	}
	
	/*
	 * remove a row column, a Task.
	 */
	public void removeRowItem(int col, int row) {
		
		Node currentNode = getNode(col,row);
		
		if(currentNode!=null) {
		
			getCurrentGridPane().getChildren().remove(currentNode);
		
		}
		
		removeBlankGridPositions(col);
				
	}

	/*
	 * Move column rows up one. Removes any blank positions.
	 */
	public void removeBlankGridPositions(int col) {
		
		Node currentNode;
		Node nextNode;
	
		int rowMax = getCurrentGridPane().getRowCount();
				
		for(int rowCount = 1; rowCount<rowMax ; rowCount++) {
			
			currentNode = getNode(col,rowCount);
			
			if(currentNode==null) {
				
				nextNode = getNode(col,rowCount+1);
				
				if(nextNode!=null) {

					// Blank out the next node.
					getCurrentGridPane().getChildren().remove(nextNode);
					
					// Set this node to the next node
					getCurrentGridPane().add(nextNode, col, rowCount);
					
				}
				
			}
			
		}
		
	}
	
	/*
	 * Switch nodes.
	 */
	public void switchNodes(int sourceCol, int sourceRow, int destinationCol, int destinationRow) {
		
		Node sourceNode = getNode(sourceCol, sourceRow);
		Node destinationNode = getNode(destinationCol, destinationRow);
		
		// Switch Node ID's
		String destID = destinationNode.getId();
		String sourceID = sourceNode.getId();
		
		destinationNode.setId(sourceID);
		sourceNode.setId(destID);
		
		getChildren().remove(destinationNode);
		
		getChildren().remove(sourceNode);
		
		getCurrentGridPane().add(destinationNode, sourceCol, sourceRow);
		
		getCurrentGridPane().add(sourceNode, destinationCol, destinationRow);
		
	}
	
	/*
	 * Move a node to a vacant position.
	 */
	public void moveNode(int sourceCol, int sourceRow, int destinationCol, int destinationRow) {
		
		Node sourceNode = getNode(sourceCol, sourceRow);
				
		getChildren().remove(sourceNode);
				
		getCurrentGridPane().add(sourceNode, destinationCol,destinationRow);
				
		String newDataCoordinate = this.tabPane.getSelectionModel().getSelectedIndex() + ":" + destinationCol + ":" + (destinationRow - 1);
		
		sourceNode.setId(newDataCoordinate);
		
	}
	
	/*
	 * Get the current GridPane object displayed.
	 */
	public GridPane getCurrentGridPane() {
		
		GridPane gp = null;
				
		// Make sure something exists here.
		if(this.getCurrentTab().getContent() != null) {
			
			// Find the GridPane.
			if(this.getCurrentTab().getContent() instanceof GridPane) {
				
				gp = (GridPane) this.getCurrentTab().getContent();
				
			} else if(this.getCurrentTab().getContent() instanceof AnchorPane) {
				
				AnchorPane anchorPane = (AnchorPane) this.getCurrentTab().getContent();
				
				ScrollPane scrollPane = (ScrollPane) anchorPane.getChildren().get(0);
				
				gp = (GridPane) scrollPane.getContent();
								
			} else if (this.getCurrentTab().getContent() instanceof ScrollPane) {
				
				ScrollPane scrollPane = (ScrollPane) this.getCurrentTab().getContent();
				
				gp = (GridPane) scrollPane.getContent(); 
				
			} else if (this.getCurrentTab().getContent() instanceof Pane) {
				
				Pane pane = (Pane) this.getCurrentTab().getContent();
				
				ScrollPane scrollPane = (ScrollPane) pane.getChildren().get(0);
				
				gp = (GridPane) scrollPane.getContent();
						
			}
			
		}
		
		return gp;
		
	}
	
	/*
	 * Get the current Tab displayed.
	 */
	public Tab getCurrentTab() {
		
		// Will only happen if the tabPane does not have any tabs.
		if(this.tabPane.getSelectionModel().getSelectedItem() != null) {
			
			if(this.tabPane.getSelectionModel().getSelectedItem() instanceof Tab) {
				
				return this.tabPane.getSelectionModel().getSelectedItem();
				
			}
			
		}
		
		return null;
		
	}
	
	/*
	 * Get the current tabPane
	 */
	public TabPane getTabPane() {
		
		return this.tabPane;
		
	}

	/*
	 * Get Node.
	 */
	public Node getNode(int col, int row) {
		
		int nodeRow = 0, nodeCol;
		
		this.list = getCurrentGridPane().getChildren();
		
		for(Node node : this.list) {
			
			nodeCol = GridPane.getColumnIndex(node);
			nodeRow = GridPane.getRowIndex(node);
			
			if(nodeRow==row && nodeCol==col) {
				return node;
			}
			
		}
		
		return null;
		
	}
	
	/*
	 * Get the nodes contain within the GridPane within the current tab.
	 */
	public ObservableList<Node> getChildren() {
		
		return getCurrentGridPane().getChildren();
		
	}

	/*
	 * Set the current tabPane. Only happens once when loading a new workspace for a New User.
	 */
	public void setTabPane(TabPane tabPane) {
		
		this.tabPane = tabPane;
		
	}
	
	/*
	 * Set the current tab to this index number
	 */
	public void setCurrentTab(int tabNumber) {
		
		this.tabPane.getSelectionModel().select(tabNumber);
		
	}

	/*
	 * Set the current tab to this tabs name.
	 */
	public void setCurrentTab(String tabName) {
		
		for(Tab tab : this.tabPane.getTabs()) {
			if(tab.getText().equals(tabName)) {
				this.setCurrentTab(tab);
				break;
			}
		}
				
	}
	
	/*
	 * Set the current tab to this tab object.
	 */
	public void setCurrentTab(Tab tab) {
		
		this.tabPane.getSelectionModel().select(tab);
		
	}
}
