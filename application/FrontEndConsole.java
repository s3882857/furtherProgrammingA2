package application;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
//import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import smartBoard.BackEnd;
import smartBoard.User.SmartBoardLoginException;
import smartBoard.User.User;
import utilities.ConsoleUtilities;

/*
 * Front end console screen handling.
 */
public class FrontEndConsole {
	
	private String lineFeed;
	private String lineBreak;
	private Scanner inputsSysIn;
	private String fileName;
	
	private ConsoleUtilities consoleUtil;;
	
	private String formatLine;
	private String formatDollar;
	private String formatString;
	private String formatString2;
	private String formatDecimal;
	private BackEnd backEnd;
	
	public FrontEndConsole(BackEnd backEnd) {
		
		int lineBreakLen = 30;
		this.backEnd   = backEnd;
		this.lineFeed      = "\n";
		this.lineBreak     = new String(new char[lineBreakLen]).replace('\u0000', '-');
		this.inputsSysIn   = new Scanner(System.in);
		this.consoleUtil   = new ConsoleUtilities();
		this.consoleUtil.setLineBreakLength(lineBreakLen);
		
		this.formatLine    = " %2d) %-25s";
		this.formatDecimal = "%4d";
		this.formatString  = "%-25s";
		this.formatString2 = "%-19s";
		this.formatDollar  = "$%-7.2f";
		
				
		this.fileName      = "Discounts.txt";
		
		try {
			login();
			mainMenu();
		}
		catch(IllegalArgumentException iae) {
			System.out.println("Critical error occurred. Contact administrator.");
		}
		catch(SmartBoardLoginException smle) {
			System.out.println("Critical error occurred. Contact administrator.");
		}
		//mainMenu();
		
	}
	
	public static void main(String[] args) {
		
		FrontEndConsole fec = new FrontEndConsole(new BackEnd());
		
	}
	
	public void login() throws IllegalArgumentException, SmartBoardLoginException {
		
		String userName = "";
		String password = "";
		
		boolean validPassword = false;
		
		do {
		
			if(!validPassword) {
				
				userName = this.consoleUtil.getStringInput("Enter User Name: ", inputsSysIn);
				password = this.consoleUtil.getStringInput("Enter password: ", inputsSysIn);
				
			}
			
			this.backEnd.getUser(userName);
			validPassword = this.backEnd.passwordCheck(password);
			
		}	
		while( !validPassword );
			
		if(validPassword) {
			System.out.println("Password Correct");
			this.backEnd.loadWorkSpace();
		}
	}
	
	public void mainMenu() {
		
		
		int menuItem = 1;
		
		String[] menuNames = { menuItem++ + ") Create Project",           
				               menuItem++ + ") Create Basket",
				               menuItem++ + ") Create Task",
				               menuItem++ + ") Exit"}; 
			
		int exitMenu = menuItem - 1;
	   
		String screenDisplay = "";

		String choice = "";
		boolean refresh = true;
		
		do {

			if(refresh) {
				screenDisplay = getMainMenuHeader(menuNames);
				refresh = false;
			}
			
			System.out.print(screenDisplay);

			// Using a string as return value prevents
			// errors later on.
			choice = this.inputsSysIn.nextLine();

			switch (choice) {
			case "1":
				
				if(createProject()) {
					System.out.println("Project successfully created.");
					refresh = true;
				} else {
					System.out.println("Project was not created. Try again.");
				}
				
				//listMenuItems(this.restaurants.getMenuItems(listRestaurants(searchCategoryInput())));
				break;
				
			case "2":
				
				if(createBasket()) {
					System.out.println("Basket successfully created.");
				} else {
					System.out.println("Basket was not created. Try again.");
				}
				//listMenuItems(this.restaurants.getMenuItems(listRestaurants(searchNameInput())));
				break;
				
			case "3":
				
				if(createTask()) {
					System.out.println("Basket successfully created.");
				} else {
					System.out.println("Basket was not created. Try again.");
				}	
				
				break;
				
			default:
				
				if (!choice.equals("4")) {
					System.out.println("Invalid choice.");
				}
				
			}

		}
		// Nice simple display of options to the user until exit entered.
		while (!choice.equals(String.valueOf(exitMenu)));
		
	
	}

	public String getMainMenuHeader(String[] menuNames) {
		
		String screenDisplay = "";
		
		screenDisplay += this.consoleUtil.createScreenBanner("Smart Board - " + User.getInstance().getProjectLogin(), 0);
		screenDisplay += this.lineFeed;
		screenDisplay += this.consoleUtil.createMenuItems(menuNames, 1); 
		screenDisplay += this.lineBreak + this.lineBreak;
		screenDisplay += this.lineFeed;
		screenDisplay += "Enter Selection : ";
		
		return screenDisplay;
		
	}
	public boolean createProject() {
		
		boolean created = true;
		String projectName = this.consoleUtil.getStringInput("Enter a Project name: ", this.inputsSysIn);

		try {
			this.backEnd.addProject(projectName);
		}
		catch(SmartBoardLoginException sble) {
			created = false;
			System.out.println("Your login has expired. Please exit, then login to continue.");
		}
		
		return created;
		
	}
	
	public boolean createBasket() {
		
		boolean created = true;
		
		String projectName = this.consoleUtil.getStringInput("Enter the project name to put the task into", this.inputsSysIn);
		
		User.getInstance().setProjectLogin(projectName);
		
		String basketName = this.consoleUtil.getStringInput("Enter a Basket name: ", this.inputsSysIn);
		
		try {
			this.backEnd.addBasket(basketName);
		}
		catch(SmartBoardLoginException sble) {
			created = false;
			System.out.println("Your login has expired. Please exit, then login to continue.");
		}
		
		return created;
	}
	
	public boolean createTask() {
		
		boolean created = true;
		
		String projectName = this.consoleUtil.getStringInput("Enter the project name to put the task into", this.inputsSysIn);
		
		User.getInstance().setProjectLogin(projectName);
		
		String basketName = this.consoleUtil.getStringInput("Enter the basket name to put the task into", this.inputsSysIn);
		
		User.getInstance().setCurrentBasket(basketName);

		String taskName = this.consoleUtil.getStringInput("Enter the name of the task", this.inputsSysIn);
		String description = this.consoleUtil.getStringInput("Enter task description", this.inputsSysIn);
		
		
		try {
			this.backEnd.addTask(taskName);
			this.backEnd.setDescription(description);
		}
		catch(SmartBoardLoginException smle) {
			created = false;
			System.out.println("Your login has expired. Please exit, then login to continue.");
		}
		
		return created;
		
	}
	/*public ArrayList<ListableUtilities> searchCategoryInput() {
		
		String searchKey = this.consoleUtil.getStringInput("Enter the restaurant category to search: ",this.inputsSysIn);
		ArrayList<ListableUtilities> itemsFound = new ArrayList<ListableUtilities>();
		
		if(!this.consoleUtil.isStringFieldEmpty(searchKey)) {
			
			itemsFound = this.restaurants.searchRestaurants(searchKey,"category");
			
			if(itemsFound.size() <= 0 ) {
				
				System.out.println("No food categories found matching '" + searchKey + "'");
				
			}
			
		} else {
			
			System.out.println("Nothing selected");
			
		}
		
		return itemsFound;
		
	} */
	

	/*public ArrayList<ListableUtilities> searchNameInput(){
		
		String searchKey = this.consoleUtil.getStringInput("Enter the name of the restaurant: ",this.inputsSysIn);
		
		ArrayList<ListableUtilities> itemsFound = new ArrayList<ListableUtilities>();
		
		if(!this.consoleUtil.isStringFieldEmpty(searchKey)) {
			itemsFound = this.restaurants.searchRestaurants(searchKey,"name");
			
			if(itemsFound.size() <= 0 ) {
				
				System.out.println("No Restaurants found matching '" + searchKey + "'");
				
			}
			
		} else {
			
			System.out.println("Nothing selected");
			
		}
		
		return itemsFound;
		
	}*/
	
	/*
	 * Collects user input for the MenuItem's quantity ordered. 
	 * Re-prompts menu items list each time until back to main menu
	 */
	/*public void listMenuItems(ArrayList<ListableUtilities> arrayList) {
		
		int arrayIndex = -1, quantity = 0;
		MenuItem menuItem = null;
		
		do {
			
			arrayIndex = listItemsControl(arrayList);
			
			if( arrayIndex < arrayList.size() && arrayIndex >= 0) {
				
				quantity = this.consoleUtil.getNumberInput("Enter the quantity to order: ",this.inputsSysIn);
	
				if( quantity > 0 ) {
					
					menuItem = (MenuItem) arrayList.get(arrayIndex);
					this.order(menuItem, quantity);
					
				}
					
			}
			
		}
		while (arrayIndex >= 0);
		
	}*/
		
	/*
	 * Control point for restaurant object retrieval. Calls listItemsControl to display
	 * all possible restaurant choices. If no valid restaurant is selected returns null.
	 */
	/*public Restaurant listRestaurants(ArrayList<ListableUtilities> arrayList) {
		
		Restaurant restaurant = null;
		
		if(arrayList!=null) {
			
			int arrayIndex = listItemsControl(arrayList);
					
			if(arrayIndex < arrayList.size() && arrayIndex >= 0 ) {
				
				restaurant = (Restaurant) arrayList.get(arrayIndex);
								
			}
		}
		
		return restaurant;
		
	}*/
	
	/*
	 * Generic control point for listing items conforming to the ListUtilities 
	 * Interface type. Controls forward and back options.
	 */
	/*public int listItemsControl(ArrayList<ListableUtilities> arrayList) {
		
		int arrayIndex = -1, displayNumber = 4, startIndex = 0, 
			endIndex = startIndex + displayNumber - 1;
													
		do {
			
			if(endIndex>=arrayList.size()) {
				endIndex = arrayList.size() - 1;
			}
						
			arrayIndex = listItems(startIndex,endIndex,arrayList);
			
			// back
			if(arrayIndex==-2) {
				
				arrayIndex = -1;
				startIndex -= displayNumber;
				endIndex   -= displayNumber;
												
			}
			
			// forward
			if(arrayIndex==-1) {
				
				arrayIndex = -1;
				startIndex += displayNumber;
				endIndex   += displayNumber;
				
			}
									
		}
		// Keep user in loop until the back option entered at menu start point (startIndex < 0) or potentially a valid index has NOT been entered.
		while ( !( arrayIndex>=0 ) && startIndex < 0 );
		
		return arrayIndex;
		
	}*/
	
	/*
	 * List's items that utilize the ListableUtilities Interface type.
	 * Uses a range to display startIndex and endIndex.
	 * Displays next and back options at the end of the list.
	 */
	/*public int listItems(int startIndex, int endIndex, ArrayList<ListableUtilities> arrayList) {	
		
		int arrayIndex = -2;
				
		// Check there is something to display.
		if(arrayList.size() > 0) {
		
			int totalItems = endIndex - startIndex + 1;
			String[] menuNames   = new String[totalItems];
			String screenDisplay = "";
			int indexCount       = 0, next = 0,back = 0, lineNumber = 1, selectedLine = 0;
			
			@SuppressWarnings("unused")
			boolean exitLoop     = false;
			
			screenDisplay += this.consoleUtil.createScreenBanner("Menu items", 0);
			screenDisplay += this.lineFeed;
			screenDisplay += this.lineFeed;
					
			do {
				
				indexCount = 0;
				
				for(int i = startIndex; i <= endIndex; i++) {
				
					if(i>arrayList.size()) {
						break;
					}
						
					menuNames[indexCount++] = arrayList.get(i).formatLine(lineNumber++);
																	
				}

				screenDisplay += this.consoleUtil.createMenuItems(menuNames, 1);
				screenDisplay += this.lineFeed;
				
			    System.out.print(screenDisplay);
				
			    screenDisplay = "";
				indexCount = 0;
				
				// More records exist than what is currently displayed.
				if(arrayList.size() > (endIndex + 1) ) {
				
					next = lineNumber++;
					menuNames   = new String[2];
					menuNames[indexCount++] = String.format(this.formatLine,next,"Next     -->");
										
				} else {
					
					menuNames   = new String[1];
					
				}
				
				back = lineNumber++;
				menuNames[indexCount++] = String.format(this.formatLine,back,"<-- Go Back ");
								
				screenDisplay += this.consoleUtil.createMenuItems(menuNames, 1);
				screenDisplay += this.lineFeed;
				screenDisplay += this.lineBreak + this.lineBreak;
				screenDisplay += this.lineFeed;
				screenDisplay += "Enter Selection : ";
								
				System.out.print(screenDisplay);
		
				selectedLine = this.consoleUtil.getNumberInput("",this.inputsSysIn);
				arrayIndex   = selectedLine - 1;
				
				// exitLoop flag created to simplify logic. Main exit loop trigger is
				// when the user enters a valid array item. This logic is separate. 
				// That is, the 'back' and 'next' options are not representative of valid
				// data, but legitimate exit values only. Additional escape logic.  
				if((selectedLine == back && back !=0 && selectedLine == next)) {
					exitLoop = true;
				}
			
			}
			// User has selected a valid line number (MenuItem) or some other escape logic has been triggered (exitLoop).
			while((arrayIndex < startIndex || arrayIndex > endIndex) && (exitLoop = false));
			
			if(selectedLine == back && back != 0) {
				arrayIndex = -2;
			} else if(selectedLine == next) {
				arrayIndex = -1;
			}
			
		}
		
		return arrayIndex;
		
	}*/
	
	/*
	 * Sets a MenuItem to an order. Starts a new order.
	 */
	/*public void order(MenuItem menuItem, int quantity) {
		
		if (this.order==null) {
			
			this.order = new Order(menuItem,quantity,this.restaurants);
			
		} else {
			
			this.order.addMenuItem(menuItem, quantity);
			
		}
			
	}*/
	
	/*
	 * The Checkout process.
	 * Lists through all the order's MenuItems displaying then under each restaurant.
	 * Displays totals at the end. 
	 */
	/*public void checkout() {
		
		int quantity               = 0;
		MenuItem menuItem          = null;
		String previousRestaurant  = "";
		String currentRestaurant   = "";
		double deliveryFee         = -1;
				
		String screenDisplay       = this.consoleUtil.createScreenBanner("You have ordered the following items", 0);
		screenDisplay             += this.lineFeed;
				
		// Force all calculation and accumulation values to be refreshed.
		// SetDefaultValues sets total values to 0 forcing a rebuild.
		this.order.setDefaultValues();
		
		OrderLine orderLine      = null;
		Iterator<OrderLine> iter = this.order.getItemsOrdered().iterator();
		
		while(iter.hasNext()) {
			
			orderLine = iter.next();
			
			menuItem  = orderLine.getMenuItem();
			quantity  = orderLine.getQuantity();
			currentRestaurant = menuItem.getRestaurantName();
						
			if(!currentRestaurant.equalsIgnoreCase(previousRestaurant) && !previousRestaurant.equals("")) {
				
				screenDisplay     += String.format(this.formatString + this.formatString + this.formatDollar,"Delivery Fee","",deliveryFee);
				screenDisplay     += this.lineFeed;
				screenDisplay     += this.lineBreak + this.lineBreak;
				screenDisplay     += this.lineFeed;
				screenDisplay     += currentRestaurant;
				screenDisplay     += this.lineFeed;
				
			} else if(previousRestaurant.equals("")) {
				
				screenDisplay     += currentRestaurant;
				screenDisplay     += this.lineFeed;
				
			}
			
			previousRestaurant = currentRestaurant;
			
			screenDisplay += String.format(" " + this.formatDecimal + " " + this.formatString + this.formatString2 + this.formatDollar,quantity,menuItem.getName(),"",orderLine.getLineTotalPrice());
			screenDisplay += this.lineFeed;
			
			deliveryFee    = this.restaurants.getRestaurant(previousRestaurant).getDeliveryFee();
			
		}
		
		screenDisplay += String.format(this.formatString + this.formatString + this.formatDollar,"Delivery Fee","",deliveryFee);
		screenDisplay += this.lineFeed;
		screenDisplay += this.lineBreak + this.lineBreak;
		screenDisplay += this.lineFeed;
		
		screenDisplay += String.format(this.formatString + this.formatString + this.formatDollar,"Order Price:","",order.getSubTotal());
		screenDisplay += this.lineFeed;
		screenDisplay += String.format(this.formatString + this.formatString + this.formatDollar,"Delivery Fee:","",order.getTotalDeliveryFee());
		screenDisplay += this.lineFeed;
		screenDisplay += String.format(this.formatString + this.formatString + this.formatDollar,"You have saved:","",order.getAmountSaved());
		screenDisplay += this.lineFeed;
		screenDisplay += String.format(this.formatString + this.formatString + this.formatDollar,"Total amount to pay:","",order.getTotalPrice());
		screenDisplay += this.lineFeed;
		
		System.out.println(screenDisplay);
		
		this.order = null;
		
	}*/
	// Create a delay before redisplaying next screen.
	/*public void enterContinue() {
		this.inputsSysIn.nextLine();
	}*/
}