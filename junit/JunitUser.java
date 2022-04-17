package junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import smartBoard.BackEnd;
import smartBoard.User.User;

public class JunitUser {

	private BackEnd backEnd;
	

	@Before
	public void setUp() throws Exception {
	
		this.backEnd = new BackEnd();
		this.backEnd.getUser("johnh");
					
		
	}

	@After
	public void tearDown() throws Exception {
		
				
	}

	@Test
	public void test() {
		
		
		
		
		/*System.out.println("Test User " + this.user.getUserName() + " "
		                                + this.user.getProfile().getFirstName() + " "
				                        + this.user.getProfile().getLastName() + " "
				                        + this.user.getPhoto().getFilePhotoName());*/
		
		// Return true as the password is correct.
		assertTrue(User.getInstance().passwordCheck("abc123"));
		
		// Return false as the password is incorrect.
		assertFalse(User.getInstance().passwordCheck("abcabc"));
				
	}

}
