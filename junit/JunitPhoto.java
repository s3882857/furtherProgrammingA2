package junit;



import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smartBoard.BackEnd;
import smartBoard.User.Photo;
import smartBoard.User.User;

public class JunitPhoto {

	//private User user;
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
		
		// The Photo.getInstance() will get the photo from the Photo Singleton class. 
		// This is setup through the User class setup. See setUp()
		assertTrue("Write did not succeed.",Photo.getInstance().startWriteProfilePhoto());
		
	}

}
