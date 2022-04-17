package smartBoard.WorkSpace;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;

import smartBoard.User.SmartBoardLoginException;
import smartBoard.User.User;
import utilities.ConsoleUtilities;

/*
 * WorkSpace for storing all items and tasks relating to a User.
 * Acts as a Facade to simplify access to the inner workings of 
 * all data structures.
 */
@SuppressWarnings("serial")
public class WorkSpace implements Serializable {

	// Serial Id used for class Serialization
	//private static final long serialVersionUID = 1L;
	
	// The user name of the item. Who it belongs to. Who created this.
	//private String userName;
	
	private WorkSpaceInitiator workSpaceInit; 
	private Inspiration inspiration;
	//private ConsoleUtilities consoleUtils;
	
	private LinkedHashSet<Project> projects;
	
	/*
	 * Start with a user name. Read in from file or DB.
	 */
	public WorkSpace() {
		
		this.workSpaceInit = new WorkSpaceInitiator();
		this.inspiration = new Inspiration();
		//this.consoleUtils = new ConsoleUtilities();
		
		this.projects = new LinkedHashSet<Project>();
		
		
	}
	
		
	/* The following 2 methods perform the same functions. 
	 * 
	 * Create a new Item to be added to the WorkSpace. 
	 * 
	 * (createItem creates a Project or Basket) Only requires 2 parameters.
	 * (createTask creates a Task) Requires 2 parameters. 
	 *                                                             
	 * The item is added to the current project the User is logged into. If it is a 
	 * Project item then add it to the WorkSpace itself.
	 * The Project the item belongs to is determined by the User login. User object retains
	 * the logged into project. User is static. 
	 */
	/*public boolean createItem(String workSpaceItemType, String name) throws SmartBoardLoginException {
		
		return createWorkSpaceObj(workSpaceItemType, name, "");
			
	}
	
	public boolean createTask( String name, String basketName) throws SmartBoardLoginException, IllegalArgumentException  {
		
		return createWorkSpaceObj("Task", name, basketName);
		
	}
	
	private boolean createWorkSpaceObj(String workSpaceItemType, String name, String basketName) throws SmartBoardLoginException, IllegalArgumentException {
		
		String projectName = User.getInstance().getProjectLogin();
		
		WorkSpaceHub workSpaceItem = this.workSpaceInit.createWorkSpaceItem(workSpaceItemType, name);
		
		if(workSpaceItem instanceof Project && workSpaceItemType.equalsIgnoreCase("Project")) {
			
			this.add((Project)workSpaceItem);
						
		} else if(workSpaceItem instanceof Basket  && workSpaceItemType.equalsIgnoreCase("Basket")) {
		
			findProject(projectName).add(workSpaceItem);
			
		} else if(workSpaceItem instanceof Task  && workSpaceItemType.equalsIgnoreCase("Task") && !this.consoleUtils.isStringFieldEmpty(basketName)) {
	
			findProject(projectName).findBasket(basketName).add(workSpaceItem);
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
		
		return true;
		
	}*/
	
	/*  
	 * Create a new Item to be added to the WorkSpace. 
	 *                                                             
	 * The item is added to the current project, and/or basket, and/or task the User is logged into. 
	 * The current project, basket, and task is maintained within the User static facade class.
	 *  
	 */
	public boolean createItem(String workSpaceItemType, String name) throws SmartBoardLoginException {
		
		return createWorkSpaceObj(workSpaceItemType, name);
			
	}
	
/*	public boolean createTask( String name, String basketName) throws SmartBoardLoginException, IllegalArgumentException  {
		
		return createWorkSpaceObj("Task", name, basketName);
		
	}*/
	
	private boolean createWorkSpaceObj(String workSpaceItemType, String name) throws SmartBoardLoginException, IllegalArgumentException {
		
		String projectName = User.getInstance().getProjectLogin();
		String basketName = User.getInstance().getBasketLogin();
		
		WorkSpaceHub workSpaceItem = this.workSpaceInit.createWorkSpaceItem(workSpaceItemType, name);
		
		if(workSpaceItem instanceof Project && workSpaceItemType.equalsIgnoreCase("Project")) {
			
			this.add((Project)workSpaceItem);
			User.getInstance().setProjectLogin(name);
						
		} else if(workSpaceItem instanceof Basket  && workSpaceItemType.equalsIgnoreCase("Basket")) {
		
			findProject(projectName).add(workSpaceItem);
			User.getInstance().setCurrentBasket(name);
			
		} else if(workSpaceItem instanceof Task  && workSpaceItemType.equalsIgnoreCase("Task") ) {
	
			findProject(projectName).findBasket(basketName).add(workSpaceItem);
			User.getInstance().setCurrentTask(name);
			
		} else {
			
			throw new IllegalArgumentException();
			
		}
		
		return true;
		
	}
	
	/*
	 * Add Project to the WorkSpace.
	 */
	public void add( WorkSpaceHub workSpace) throws IllegalArgumentException {
		
		if(workSpace instanceof Project) {
			
			this.projects.add((Project) workSpace );
			
		} else {
			
			throw new IllegalArgumentException();
			
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
	 * Getters
	 */
	
	/*
	 * return list of projects belonging to this workspace.
	 */
	public LinkedHashSet<Project> getProjects(){
		return this.projects;
	}
	
	/*
	 * get UserName.
	 */
	/*public String getUserName() {
		return this.userName;
	}*/

	/*
	 * Get the name of this workspace. UserName is used for this.
	 */
	/*@Override
	public String getName() {
		return getUserName();
	}*/
	
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
	 * Set user Name
	 */
	/*public void setUserName(String userName) {
		this.userName = userName;
	}*/
	
	/*
	 * Set Task name description.
	 */
	public void setDescription( String descripiton ) throws SmartBoardLoginException {
		
		String projectName = User.getInstance().getProjectLogin();
		String basketName = User.getInstance().getBasketLogin();
		String taskName = User.getInstance().getTaskLogin();
		
		findProject(projectName).findBasket(basketName).findTask(taskName).setDescription(descripiton);
		
	}
	
}
