package smartBoard.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import smartBoard.WorkSpace.Basket;

public class JunitBasket {

	private ArrayList<Basket> baskets;
	
	
	@Before
	public void setUp() throws Exception {
			
		Basket basketObj = null;
		
		this.baskets = new ArrayList<Basket>();
		
		//BackEnd backEnd = new BackEnd();
		
		//backEnd.getUser("johnh");
		
		//this.user.setUserDetails(User.getInstance().getProfile());
		
		for(int i = 0; i<5;i++) {
			
			//basketObj = new Basket("Basket " + (i+1));
			
			for(int ii = 0; ii<5;ii++) {
			//	basketObj.add(new Task("Task " + (ii+1)));
			}
			
			this.baskets.add(basketObj);
		}
		
		
	}

	@After
	public void tearDown() throws Exception {
		
		//this.user = null;
		this.baskets = null;
		
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
			
			basket = this.baskets.get(index);
						
			assertEquals(basket.findTask("Task " + (index+1)).getName(),"Task " + (index + 1));
			
		
			System.out.println(basket.findTask("Task " + (index+1)).getName());
			
		}
		
	}

}
