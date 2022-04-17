package junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smartBoard.WorkSpace.Inspiration;

public class JUnitInspiration {
	
	private Inspiration inspiration;
	
	@Before
	public void setUp() throws Exception {
		this.inspiration = new Inspiration();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		
		/*
		 * 	this.quotes[0] = "Live long and prosper";
			this.quotes[1] = "Eat, Live, then Die. Not necessarily in that order";
			this.quotes[2] = "We are watching you";
			this.quotes[3] = "Don't look now, but I think that guy behind you has an axe";
			this.quotes[4] = "The apple does not fall far from the tree, unless it's a really windy day";
		 */
		
		//System.out.println(this.inspiration.getQuote());
		
		// This is not really a re test use case. However I used this to test regardless.
		// You have to run it a few times until the quote comes up. It is random.
		// It is a get method and not really appropriate for testing in Junit. 
		// I need some way to verify it so here it is.
		
		String quote = this.inspiration.getQuote();
		System.out.println("Inspirational quote of the day is \"" + quote + "\"");
		
		assertEquals(quote,"We are watching you");
		
		
	}

}
