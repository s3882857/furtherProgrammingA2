package smartBoard.WorkSpace;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import smartBoard.Connection.SBConnectionManager;
import smartBoard.Connection.SQLScriptor;
import smartBoard.User.SmartBoardLoginException;
import smartBoard.User.User;

/*
 * Acts as a Facade to simplify access to the inner workings of 
 * all of the Workspace data structures. Can also be thought of as the 
 * Back End to the WorkSpace.
 * 
 * The WorkSpaceManager class promotes/enhances assists the WorkSpace super 
 * 
 */
@SuppressWarnings("serial")
public class WorkSpaceManager implements Serializable {

	private static WorkSpaceManager workSpaceManager;
	
	private Inspiration inspiration;
	
	// Database connection.
	private SBConnectionManager connectionSB;
	
	// SQL script generator.
	private SQLScriptor scripts;
	
	// The projects. The workspace base. Effectively the workspace.
	private LinkedHashSet<Project> projects;

	/*
	 * Start the WorkSpaceManager facade.
	 */
	private WorkSpaceManager() {
		
		this.inspiration = new Inspiration();
		this.connectionSB = new SBConnectionManager();
		this.scripts = new SQLScriptor();
		
		this.projects = new LinkedHashSet<Project>();
		
	}
	
	/*
	 * Get a WorkSpaceManager instance singleton. This is the workspace facade for everything within
	 * the workspace.
	 */
	public synchronized static WorkSpaceManager getInstance() {
		
		if(WorkSpaceManager.workSpaceManager==null) {
			WorkSpaceManager.workSpaceManager = new WorkSpaceManager();
		}
		
		return WorkSpaceManager.workSpaceManager;
		
	}
	
	/*
	 * Add Project to the WorkSpace.
	 */
	public void add( Project workSpace) throws IllegalArgumentException {
		
		if(workSpace instanceof Project) {
			
			this.projects.add((Project) workSpace );
			
			//this.displayWorkSpaceHelper();
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
		
	}
	
	/*
	 * Add a basket to a specific project
	 */
	public void addBasket( Basket basket, String projectName) throws SmartBoardWorkSpaceException {
		
		try {
		
			findProject(projectName).add(basket);
						
		}
		catch(NullPointerException npe) {
			
			throw new SmartBoardWorkSpaceException("Project " + projectName + " not found when attempting to add Basket " + basket.getName());
			
		}
				
	}
	
	/*
	 * Add a task to a basket within a project.
	 */
	public void addTask( Task task, String projectName, String basketName) throws SmartBoardWorkSpaceException {
		
		try {
			
			findProject(projectName).findBasket(basketName).add(task);
			
		}
		catch(NullPointerException npe) {
			
			throw new SmartBoardWorkSpaceException("Project " + projectName + " or Basket " + basketName + " not found while attempting to add Task " + task.getName());
			
		}
				
	}
	
	/*
	 * Find Project. Exact match.
	 */
	public Project findProject(String searchKey) {
		
		Project projObj = null;
		
		for(Project project : this.projects) {
			
			if(searchKey.equalsIgnoreCase(project.getName())) {
				projObj = project;
				break;
			}
				
		}
		
		return projObj;
		
	}
	
	/*
	 * Find basket within a given project.
	 */
	public Basket findBasket(String searchKey, String projectName) {
		
		Project project = findProject(projectName);
		Basket basket = null;
		
		if (project!=null) {
			basket = project.findBasket(searchKey);
		}
		
		return basket;
		
	}
	
	/*
	 * Find the basket by the index number.
	 */
	public Basket findBasket(int projectIndex, int basketIndex) {
		
		int projectIndexCount = 0;
		int basketIndexCount = 0;
		
		for(Project project : this.projects) {
			
			if(projectIndex==projectIndexCount) {
				
				for(Basket basket : project.getBaskets()) {
					
					if(basketIndexCount==basketIndex) {
						return basket;
					}
					
					basketIndexCount++;
				}
			}
			
			projectIndexCount++;
			
		}
		
		return null;
		
	}
	
	/*
	 * Find a task within a given basket within a given project.
	 */
	public Task findTask(String searchKey, String projectName, String basketName) {
		
		Project project = findProject(projectName);
		Basket basket = null;
		Task task = null;
		
		if(project!=null) {
			
			basket = project.findBasket(basketName);
			
			if(basket!=null) {
				
				task = basket.findTask(searchKey);
				
			}
			
		}
		
		return task;
		
	}
	
	
	/*
	 * Find the task via the node ID/Coordinates of the task.
	 */
	public Task findTask(String coordinates) {
		
		String[] coordinateIndexes = coordinates.split(":");
		
		int pIndex = Integer.parseInt(coordinateIndexes[0]);
		int bIndex = Integer.parseInt(coordinateIndexes[1]);
		int tIndex = Integer.parseInt(coordinateIndexes[2]);
		
		int projectIndexCount = 0;
		int basketIndexCount = 0;
		int taskIndexCount = 0;
		
		for(Project project : this.projects) {
		
			if(projectIndexCount == pIndex) {
				
				for(Basket basket : project.getBaskets()) {
				
					if(basketIndexCount==bIndex) {
						
						for(Task task : basket.getTasks()) {
							
							if(taskIndexCount==tIndex) {
										
								return task;
								
							}
										
							taskIndexCount++;
										
						}
									
					}
					
					basketIndexCount++;
					
				}
					
				
			}
			
			projectIndexCount++;
					
		}
			
		return null;
		
	}
	
	/*
	 * See findTask(String coordinates)
	 */
	public Task findTask(int projectIndex, int basketIndex, int taskIndex) {
		
		return findTask(projectIndex + ":" + basketIndex + ":" + taskIndex);
		
	}
	/*
	 * Find a task using the task object.
	 * 
	 * Use this so we can set the specific instance of this task.
	 * In some cases the task values have changed, which changes the hash value.
	 * The task.equals(task) will fail.
	 * This version will always find the specific instance version of the
	 * task.
	 */
	public Task findTask(Task task) {
		
		return findBasket(task.getParentName(), task.getProjectName()).findTask(task.getName());
		
	}
	
	/*
	 * Get the index position value within the array of the Project
	 */
	public int getProjectIndex(Project projectIn) {
		
		int indexCount = 0;
		
		for(Project project : this.projects) {
			
			if(project.equals(projectIn)) {
				return indexCount;
			}
				
			indexCount++;
			
		}
		
		return -1;
		
	}
	
	/*
	 * Get the index position value of the Basket within the array.
	 */
	public int getBasketIndex(Basket basketIn) {
		
		int indexCount = 0;
		
		for(Project project : this.projects) {
			
			indexCount = 0;
				
			for(Basket basket : project.getBaskets()) {
				
				if(basket.equals(basketIn)) {
					return indexCount;
				}
						
				indexCount++;
						
			}
		}
				
		return -1;
		
	}

	/*
	 * Get the Task index position within its array.
	 */
	public int getTaskIndex(Task taskIn) {
		
		int indexCount = 0;
		
		for(Project project : this.projects) {
			
			for(Basket basket : project.getBaskets()) {
				
				indexCount = 0;
						
				for(Task task : basket.getTasks()) {
						
					if(task.equals(taskIn)) {
						return indexCount;
					}
								
					indexCount++;
								
				}
				
			}
						
		}
		
		return -1;
		
	}

	/*
	 * Switch/swap Tasks.
	 */
	public boolean swapTasks(Task task1, Task task2) {
		
		boolean swapOK = true;
		
		// Swap the reference No's of each task. Reference No is a sorting number.
		String referenceNoTask1 = task1.getReferenceNo();
		String referenceNoTask2 = task2.getReferenceNo();
		
		// Swap the basket names, does not need to happen every time, but it does not cause any
		// damage. Better to do each time.
		String parentNameTask1  = task1.getParentName();
		String parentNameTask2  = task2.getParentName();
		
		String projectNameTask1 = task1.getProjectName();
		String projectNameTask2 = task2.getProjectName();
		
		if(!parentNameTask1.equals(parentNameTask2)) {
		
			if(findBasket(parentNameTask1,projectNameTask1).findTask(task2.getName()) != null) {
				swapOK = false;
			}
			
			if(findBasket(parentNameTask2,projectNameTask2).findTask(task1.getName()) != null) {
				swapOK = false;
			}
			
		}
		
		if(swapOK) {
			// Remove the tasks from there prospective positions.
			findBasket(parentNameTask1,projectNameTask1).removeTask(task1);
			findBasket(parentNameTask2,projectNameTask2).removeTask(task2);
			
			// Swap the actual tasks.
			task1.setReferenceNo(referenceNoTask2);
			task2.setReferenceNo(referenceNoTask1);
			
			task1.setParentName(parentNameTask2);
			task2.setParentName(parentNameTask1);
			
			findBasket(parentNameTask1,projectNameTask1).add(task2);
			findBasket(parentNameTask2,projectNameTask2).add(task1);
			
			sortByReferenceNo();
			
		}
		
		return swapOK;
		
	}
	
	/*
	 * Move a task to another basket.
	 */
	public boolean moveTask(Task task, Basket basket) {
		
		boolean moveOK = true;
		
		// Check that a duplicate task (Same name) does not already exist in the destination basket.
		if(findBasket(basket.getName(),basket.getParentName()).findTask(task.getName()) != null ) {
			moveOK = false;
		}
		
		if(moveOK) {
			
			// Find the Basket the Task is currently in and remove it.
			findBasket(task.getParentName(),task.getProjectName()).removeTask(task);
						
			int numberTasks = findBasket(basket.getName(),basket.getParentName()).getTasks().size();
			
			if(numberTasks==0) {
				
				task.setReferenceNo("1");
				
			} else {
				
				Task lastTask = null;
				
				for(Task tmpTask : findBasket(basket.getName(),basket.getParentName()).getTasks()) {
					lastTask = tmpTask;
				}
				
				String refNo = String.valueOf(Integer.parseInt(lastTask.getReferenceNo()) + 1);
				
				task.setReferenceNo(refNo);
				
			}
			
			task.setParentName(basket.getName());
			
			findBasket(basket.getName(),basket.getParentName()).add(task);
	
			
			
		}
		
		return moveOK;
		
	}
	
	/*
	 * Stream sort all tasks back into reference number order.
	 */
	public void sortByReferenceNo() {
		
		for(Project project : this.projects) {
			
			for(Basket basket : project.getBaskets()) {
				
				ArrayList<Task> sortedTasks = new ArrayList<Task>();
				
				sortedTasks = basket.getTasks().stream().collect(Collectors.toCollection(ArrayList::new));
				
				Collections.sort(sortedTasks,new Comparator<Task>() {
					
					public int compare(Task t1, Task t2) {

						int ref1 = Integer.parseInt(t1.getReferenceNo());
						int ref2 = Integer.parseInt(t2.getReferenceNo());
						
						return ref1 - ref2;
						
					}
					
				});
				
				// Set the instance version of the new sorted list of baskets.
				findBasket(basket.getName(),basket.getParentName()).setTaskList(sortedTasks);
											
			}
		}
		
	}
	
	/*
	 * Getters
	 */
	
	/*
	 * return list of projects belonging to this workspace.
	 */
	public LinkedHashSet<Project> getProjects(){
		return this.projects;
	}
	
	/*
	 * Get an inspirational quote to display the user. This occurs with each login to 
	 * this WorkSpace.
	 */
	public String getInspirationalQuote() {
		
		return this.inspiration.getQuote();
		
	}
	
	/*
	 * Setters.
	 */
	
	/*
	 * Get the tasks current data Coordinates. Project index + ":" + Basket index + ":" + task index
	 */
	public String getTaskCoordinates(Task task) throws NullPointerException {
		
		String taskCoordinates = ""; 
		
		try {
			
			int projectIndex = getProjectIndex(WorkSpaceManager.getInstance().findProject(task.getProjectName()));
			int basketIndex = WorkSpaceManager.getInstance().getBasketIndex(WorkSpaceManager.getInstance().findBasket(task.getParentName(), task.getProjectName()));
			int taskIndex = WorkSpaceManager.getInstance().getTaskIndex(task);
			
			taskCoordinates = String.valueOf(projectIndex) + ":" + String.valueOf(basketIndex) + ":" + String.valueOf(taskIndex);
			
			//findTask(task).setNodeID(taskIdentifier);
			
		}
		catch(NullPointerException npe) {
			throw new NullPointerException("Null pointer returned when allocating Node identifier. Most likely a missing WorkSpace item.");
		}
		
		return taskCoordinates;
		
	}
	
	/*
	 * load WorkSpace for a given user.
	 */
	public void loadWorkSpace() throws SmartBoardLoginException, IllegalArgumentException, SQLException, SmartBoardWorkSpaceException {

		// Make sure we are dealing with a new WorkSpace model for each user.
		this.projects = new LinkedHashSet<Project>();
		
		loadTableData("PROJECT");
		
		loadTableData("BASKET");
		
		loadTableData("TASK");	
		
	}
	

	/*
	 * Load the table data from the database.
	 */
	public void loadTableData(String tableName) throws SQLException, SmartBoardWorkSpaceException {
		
		String userName = User.getInstance().getUserName(), projectName = "", basketName = "";

		ResultSet rs = null;
		String script = "";
		
		this.connectionSB.createSBConnection();
		this.connectionSB.createSBStatement();
				
		String[] keyNames = {"USERNAME"};
		String[] keyValues = {userName};
		
		script = this.scripts.buildBasicSelectQuery(tableName, keyNames, keyValues, "REFERENCENO");
				
		rs = this.connectionSB.executeQuery(script);
						
		while(rs.next()) {

			switch(tableName) {
				
				case "PROJECT":
					
					if(rs.getString(WSQueryColumns.WS_USERNAME).equals(userName)) {			
						add(new Project(rs));
					}
					
					break;
						
				case "BASKET":
						
					if(rs.getString(WSQueryColumns.WS_USERNAME).equals(userName)) {
						
						projectName = rs.getString(WSQueryColumns.WS_PARENT_NAME);
						
						addBasket(new Basket(rs), projectName);
						
					}
							
					break;
						
										
				case "TASK":
						
					if(rs.getString(WSQueryColumns.WS_USERNAME).equals(userName)) {	
						
						basketName = rs.getString(WSQueryColumns.WS_PARENT_NAME);
						projectName = rs.getString(WSQueryColumns.TSK_PROJECT_NAME);
					
						addTask(new Task(rs),projectName,basketName);
							
					}
						
					break;
						
			}
			
		}
		
		this.connectionSB.close();
		
	}
	
	/*
	 * remove/delete project.
	 */
	public boolean removeProject(Project project) throws SQLException{ 
		
		boolean deleted = false;
				
		// Need to deal with this instances project. 
		Project currentProject = findProject(project.getName());
				
		if(currentProject!=null) {

			if(currentProject.numberBaskets()==0) {
				
				// Delete project database.
				currentProject.deleteProject();
				
				// Remove from workspace.
				this.projects.remove(currentProject);
				
				deleted = true;
				
			} 
						
		}

		return deleted;
		
	}
	
	/*
	 * Remove basket from display lists.
	 */
	public boolean removeBasket(Basket basket) throws SQLException { 
		
		boolean deleted = false;
		
		// Need to deal with this instances basket. 
		Basket currentBasket = findProject(basket.getParentName()).findBasket(basket.getName());
		
		if(currentBasket!=null) {
			
			if(currentBasket.numberTasks()==0) {
				
				// Delete project from database
				currentBasket.deleteBasket();
				
				// Remove from current workspace.
				findProject(basket.getParentName()).getBaskets().remove(currentBasket);
				
				deleted = true;
				
			} 
						
		}
		
		return deleted;
				
	}
		
	/*
	 * Remove Task from display lists.
	 */
	public boolean removeTask(Task task) throws SQLException {
		
		boolean deleted = false;
		
		Task currentTask = findProject(task.getProjectName()).findBasket(task.getParentName()).findTask(task.getName());
		
		if(currentTask!=null) {

			// Delete task from database.
			currentTask.deleteTask();
				
			// Remove from current workspace.
			findProject(task.getProjectName()).findBasket(task.getParentName()).getTasks().remove(currentTask);
			
			deleted = true;
							
		}
		
		return deleted;
				
	}
	
	/*
	 * Rename all baskets parent names after renaming Project. Delete each existing Task and Basket
	 * associated with this change from Database. Each record will be recreated as new.
	 * 
	 */
	public void reNameProject(String currentName, String newName) throws SQLException {
		
		findProject(currentName).deleteProject();
		
		for(Basket basket : findProject(currentName).getBaskets()) {
			
			findBasket(basket.getName(), basket.getParentName()).deleteBasket();
			findBasket(basket.getName(), basket.getParentName()).setParentName(newName);
			
			for(Task task : basket.getTasks()) {
				
				findTask(task).deleteTask();
				findTask(task).setProjectName(newName);
				
			}
			
		}
		
		findProject(currentName).setName(newName);
		
	}
	
	/*
	 * Rename all baskets parent names after renaming Project. Delete each existing Task and Basket
	 * associated with this change from Database. Each record will be recreated as new.
	 * 
	 */
	public void reNameBasket(String currentName, String newName, String projectName) throws SQLException {
				
		findBasket(currentName, projectName).deleteBasket();
		
		for(Task task : findBasket(currentName, projectName).getTasks()) {
				
			findTask(task).deleteTask();
			findTask(task).setParentName(newName);
				
		}
							
		findBasket(currentName, projectName).setName(newName);
		
	}
	
	/*
	 * Write workspace.
	 */
	public void writeWorkSpace() throws SQLException {
		
		writeProjects();
		writeBaskets();
		writeTasks();
				
	}

	/*
	 * Write each Project 
	 */
	public void writeProjects() throws SQLException {
		
		boolean firstWrite = true;
		
		for(Project project : this.projects) {
			
			if(firstWrite) {
				project.setItemNumberZero();
				firstWrite = false;
			}
				
			project.open();
			project.writeDB();
			project.close();
						
		}
	}
	
	/*
	 * Write each Basket
	 */
	public void writeBaskets() throws SQLException {
				
		for(Project project : this.projects) {
					
			for(Basket basket : project.getBaskets()) {
			
				basket.open();
				basket.writeDB();
				basket.close();
							
			}
			
		}
		
	}
	

	/*
	 * Write each Task.
	 */
	public void writeTasks() throws SQLException {
		
		for(Project project : this.projects) {
			
			for(Basket basket : project.getBaskets()) {
				
				for(Task task : basket.getTasks()) {
								
					task.open();
					task.writeDB();
					task.close();
										
				}
				
			}
		}
	}
	
	/*
	 * Serialize. Write to object file.
	 */
	public boolean writeObject() {
		
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(User.getInstance().getUserName()+"Projects.dat"));
			oos.writeObject(this.projects);
			oos.close();
		}
		catch(FileNotFoundException fnfe) {
			
		}
		catch(IOException ioe) {
			
		}
		finally {
			
		}
		
		return true;
		
	}
	
	/*
	 * Helper Methods
	 */
	public void displayWorkSpaceHelper(int simple) {

		JFrame f = new JFrame();

		String display = "";
		int i = 1;

		if (getProjects() != null) {

			for (Project project : getProjects()) {
				
				display += i++ + " Project: " + project.getName() + "  ";
				
				if(simple==0) {
					display += "Date: " + project.getCreateDate() + "  ";
					display += "Time: " + project.getCreateTime() + "  ";
					display += "UserName: " + project.getUserName() + "  ";
					
				}
				display += "ReferenceNo : " + project.getReferenceNo() + "  ";
				display += "\n";

				if (project.getBaskets() != null) {

					for (Basket basket : project.getBaskets()) {
						
						display += i++ + " Basket: " + basket.getName() + "  ";
						
						if(simple==0) {
							display += "Date: " + basket.getCreateDate() + "  ";
							display += "Time: " + basket.getCreateTime() + "  ";
							display += "UserName: " + basket.getUserName() + "  ";
					
						}
						display += "ReferenceNo: " + basket.getReferenceNo() + "  ";
						display += "\n";

						if (basket.getTasks() != null) {

							for (Task task : basket.getTasks()) {
								
								display += i++ + " Task: " + task.getName() + "  ";
								
								if(simple==0) {
									display += "Description: " + task.getDescription() + "  ";
									display += "Date: " + task.getCreateDate() + "  ";
									display += "Time: " + task.getCreateTime() + "  ";
									display += "UserName: " + task.getUserName() + "  ";
					
								}
								display += "ReferenceNo: " + task.getReferenceNo() + "  ";
								display += "\n";
							}
							
						}
					}
				}
			}
		}

		JOptionPane.showMessageDialog(f, display);

		f = null;
	}
	
	public void displayReferenceNoHelper() {
		
		for(Project project : this.projects) {
			
			System.out.println("Project " + project.getName() + " Ref No " + project.getReferenceNo());
			
			for(Basket basket : project.getBaskets()) {
				
				System.out.println("Basket " + basket.getName() + " Ref No " + basket.getReferenceNo());
				
				for(Task task : basket.getTasks()) {
					
					System.out.println("Task " + task.getName() + " Ref No " + task.getReferenceNo());
					
				}
			}
		}
	}
}
