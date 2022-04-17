package smartBoard;

import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import smartBoard.User.Photo;
import smartBoard.User.Profile;
import smartBoard.User.SmartBoardLoginException;
import smartBoard.User.User;
import smartBoard.WorkSpace.Basket;
import smartBoard.WorkSpace.Project;
import smartBoard.WorkSpace.Task;
import smartBoard.WorkSpace.WorkSpace;
import smartBoard.WorkSpace.WorkSpaceInitiator;

/*
 * Back end holds/stores all data relating to the Smart Board application.
 * 
 */
public class BackEnd {
	
	private HashSet<Profile> userProfiles;
	//private User user;
	private WorkSpace workSpace;
	
	
	public BackEnd() {
	
		this.userProfiles = new HashSet<Profile>();
		
		readUserFile("fileNameMaybe");
		
		this.workSpace = null;
		
		// getUser("johnh");
		//loadWorkSpace();	
		
		//displayUserHelper();
		
		//displayWorkSpaceHelper();
		
	}
	
	/*
	 * Logging in. Read user details and load to appropriate classes.
	 */
	public void getUser(String userName) {
		
		User.getInstance().setUserDetails(findUserProfile(userName));
				
	}
	
	// Finds the User from list of Profiles.
	private Profile findUserProfile(String userName) {
		
		Profile retProfile = null;
		
		for (Profile profileObj : this.userProfiles) {
			if(userName.equals(profileObj.getUserName())) {
				retProfile = profileObj;
				break;
			}
		}
				
		return retProfile;
		
	}
	
	/*
	 * Load all Users from file/database into list for quick easy reference.
	 */
	public void readUserFile(String fileName) {
		
		// Hard-coded for now.
		this.userProfiles.add(new Profile("John","Hurst","johnh","abc123","johnh_javaFX.png","John's Project 1"));
		this.userProfiles.add(new Profile("Jane","Hurst","janeh","abc456","janeh_javaFX.png","Jane's Project 1"));
		this.userProfiles.add(new Profile("Jack","Smith","jacks","1234","jacks_javaFX.png","Jack's Project 1"));
		
	}
	/*
	 * Check the password is correct.
	 */
	public boolean passwordCheck(String password) {
		
		return User.getInstance().passwordCheck(password);
		
	}
	
	
	/*
	 * load WorkSpace for a given user.
	 */
	public void loadWorkSpace() throws SmartBoardLoginException, IllegalArgumentException {
		
		WorkSpace workSpace = new WorkSpace();
				
		workSpace.createItem("Project","Learn Java");
		
		User.getInstance().setProjectLogin("Learn Java");
		
		workSpace.createItem("Basket","To Do");
		
		workSpace.createItem("Basket","Complete");
		
		User.getInstance().setCurrentBasket("To Do");
		workSpace.createItem("Task","Learn and understand how and why using an abstract class is important");
		
		workSpace.setDescription( "Very long desc");
		
		User.getInstance().setCurrentBasket("Complete");
		workSpace.createItem("Task","Learn and understand how and why using class hierarchies is important");
						
		workSpace.setDescription("Another Very long desc");
		
		this.workSpace = workSpace;
		
	}
	
	/*
	 * Helper Methods
	 */
	public void displayUserHelper() {
		
		JFrame f=new JFrame();  
		  
		String display = "";
		
		display += "UserName: "    + User.getInstance().getUserName();
		display += "Password: "    + "Cannot be extracted. Protected.";
	//	display += " FirstName: "  + user.getProfile().getFirstName();
	//	display += " LastName: "   + user.getProfile().getLastName();
		display += " Photo Name: " + User.getInstance().getPhoto().getFilePhotoName();
		display += "\n";
							
		JOptionPane.showMessageDialog(f,display);
		
		f = null;
	}
	
	/*
	 * Add Project. Also reset login project to the new project.
	 */
	public boolean addProject(String projectName) throws SmartBoardLoginException {
		
		try {
			this.workSpace.createItem("Project", projectName);
			User.getInstance().setProjectLogin(projectName);
		}
		catch (SmartBoardLoginException smle) {
			//System.out.println("Your login has expired. Please exit, then login to continue.");
			throw new SmartBoardLoginException();
		}
		
		return true;
		
	}
	
	/*
	 * Add Basket.
	 */
	public boolean addBasket(String name) throws SmartBoardLoginException {
		
		try {
			this.workSpace.createItem("Basket", name);
			User.getInstance().setCurrentBasket(name);
		}
		catch (SmartBoardLoginException smle) {
			//System.out.println("Your login has expired. Please exit, then login to continue.");
			throw new SmartBoardLoginException();
		}
		
		return true;
		
	}
	
	/*
	 * Add Basket.
	 */
	public boolean addTask(String name) throws SmartBoardLoginException {
		
		try {
			this.workSpace.createItem("Task", name);
		}
		catch (SmartBoardLoginException smle) {
			//System.out.println("Your login has expired. Please exit, then login to continue.");
			throw new SmartBoardLoginException();
		}
		
		return true;
		
	}
	
	public boolean setDescription( String description) throws SmartBoardLoginException {
		
		try {
			this.workSpace.setDescription(description);
		}
		catch(SmartBoardLoginException sble) {
			throw new SmartBoardLoginException();
		}
		
		return true;
		
	}
	/*
	 * Helper Methods
	 */
	public void displayWorkSpaceHelper() {
		
		JFrame f=new JFrame();  
		  
		String display = "";
	
		display += "UserName: "    + User.getInstance().getUserName();
		//display += " Create Date: " + workSpace.getCreateDate();
		//display += " Create Time: " + workSpace.getCreateTime();
		display += "\n";
		
		if(workSpace.getProjects()!=null) {
			
			for(Project project : workSpace.getProjects()) {
				display += "Project: "    + project.getName();
				display += "\n";
				
				if(project.getBaskets()!=null) {
					
					for(Basket basket : project.getBaskets()) {
						display += "Basket: "    + basket.getName();
						display += "\n";
					
						if(basket.getTasks()!=null) {
							
							for(Task task : basket.getTasks()) {
								display += "Task: "    + task.getName();
								display += " Description: " + task.getDescription();
								display += "\n";						
															
							}
						}
					}
				}
			}
		}
		
		JOptionPane.showMessageDialog(f,display);
		
		f = null;
	}
}