package smartBoard.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;


import smartBoard.User.User;

import smartBoard.WorkSpace.Project;

public class JunitProject {

	//private User user;
	private ArrayList<Project> projects;
	

	@Before
	public void setUp() throws Exception {
			
		Project projectObj = null;
		
	//	this.backEnd = new BackEnd();
		User.getInstance().getUser("johnh");
	
		this.projects = new ArrayList<Project>();
				
		for(int i = 0; i<5;i++) {
			
			projectObj = new Project("Project " + (i+1));
			
			for(int ii = 0; ii<5;ii++) {
				//projectObj.add(new Basket("Basket " + (ii+1)));
			}
			
			this.projects.add(projectObj);
		}
		
		
	}

	@After
	public void tearDown() throws Exception {
		
		this.projects = null;
		
	}

	@Test
	public void test() {

		/*
		 * Test Project manually on its own. Without Facade front end, the WorkSpace. 
		 * The JunitWorkSpace tests all of this in one go. This is just easy to use
		 * to make minor alterations. To test in isolation. 
		 * 
		 * Only testing the findBasket method.
		 */
		Project project = null;
		
		for(int index = 0;index < 5 ; index++) {
			
			project = this.projects.get(index);
						
			assertEquals(project.findBasket("Basket " + (index+1)).getName(),"Basket " + (index + 1));
					
			//System.out.println(basket.findTask("Task " + (index+1)).getName());
			
		}
		
	}

}
