package junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import smartBoard.BackEnd;
import smartBoard.User.User;
import smartBoard.WorkSpace.Basket;
import smartBoard.WorkSpace.Project;
import smartBoard.WorkSpace.Task;
import smartBoard.WorkSpace.WorkSpace;

public class JunitWorkSpace {

	private BackEnd backEnd;
	private WorkSpace workspace;
	
	
	@Before
	public void setUp() throws Exception {
			
		this.backEnd = new BackEnd();
		this.backEnd.getUser("johnh");
				
		this.workspace = new WorkSpace();
		this.workspace.createItem("Project", User.getInstance().getProjectLogin());
		
		for(int i = 0; i<5;i++) {
		
			this.workspace.createItem("Basket", "Basket " + (i+1));
			User.getInstance().setCurrentBasket("Basket " + (i+1));
			for(int ii = 0; ii<5;ii++) {
				this.workspace.createItem("Task","Task " + (ii+1));
			}
		
		}
		
		//displayWorkSpaceHelper();
	}

	@After
	public void tearDown() throws Exception {
		
						
	}

	@Test
	public void test() {

		/*
		 * Test Basket manually on its own. Without Facade front end, the WorkSpace. 
		 * The JunitWorkSpace tests all of this in one go. This is just easy to use
		 * to make minor alterations. To test in isolation. 
		 * 
		 * Only testing the findTask method.
		 */
		Basket basket = null;
		
		for(int index = 0;index < 5 ; index++) {
			
			basket = this.workspace.findProject(User.getInstance().getProjectLogin()).findBasket("Basket " + (index+1));
			//System.out.println(basket.findTask("Task " + (index+1)).getName());
			User.getInstance().setCurrentBasket("Basket " + (index+1));
			
			for(int ii = 0; ii<5;ii++) {
			//	System.out.println("Task " + (index+1));
				assertEquals(basket.findTask("Task " + (ii+1)).getName(),"Task " + (ii + 1));
			}		
			//System.out.println(basket.findTask("Task " + (index+1)).getName());
			
		}
		
		//displayWorkSpaceHelper();
		
	}
	
	/*
	 * Helper Methods
	 */
	public void displayWorkSpaceHelper() {
		
		JFrame f=new JFrame();  
		  
		String display = "";
		
//		for (WorkSpaceHub workHub : this.workSpaces) {
			
//			WorkSpace work = (WorkSpace) workHub;
			
		display += "UserName: "    + User.getInstance().getUserName();
	//	display += " Create Date: " + workspace.getCreateDate();
	//	display += " Create Time: " + workspace.getCreateTime();
		display += "\n";
		
		if(workspace.getProjects()!=null) {
			
			for(Project project : workspace.getProjects()) {
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
