package junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smartBoard.BackEnd;
import smartBoard.User.User;

public class JunitProfile {

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
		
	//	assertEquals(this.user.getProfile().getFirstName(),"John");
		
	//	assertEquals(this.user.getProfile().getLastName(),"Hurst");
	}

}
