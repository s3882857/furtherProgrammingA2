package smartBoard.WorkSpace;

import java.util.Random;

public class Inspiration{

	//private static Inspiration quoteInstance;
	private String[] quotes;
	
	public Inspiration() {
		
		loadInspirationalQuotes();
		
	}
	
/*	public synchronized static Inspiration getInstance() {
		
		if (Inspiration.quoteInstance==null) {
			Inspiration.quoteInstance = new Inspiration();
		}
		
		return Inspiration.quoteInstance;
		
	}*/
	
	private void loadInspirationalQuotes() {
		
	// TO DO... load inspiration quotes from somewhere.
		int numberQuotes = 5;
		
		this.quotes = new String[numberQuotes];
		
		this.quotes[0] = "Live long and prosper";
		this.quotes[1] = "Eat, Live, then Die. Not necessarily in that order";
		this.quotes[2] = "We are watching you";
		this.quotes[3] = "Don't look now, but I think that guy behind you has an axe";
		this.quotes[4] = "The apple does not fall far from the tree, unless it's a really windy day";
	
	}
	
	public String getQuote() {
		
		int maxRange = 5;
		int randomNumber = 0;
		
		Random rand = new Random();
		
		randomNumber = rand.nextInt(maxRange);
		
		return this.quotes[randomNumber];
		
	}
}
