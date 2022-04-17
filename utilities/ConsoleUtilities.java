package utilities;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.util.Scanner;


/*
 * Console utilities class. Holds useful utility methods 
 * for building console related screen display and handling.
 */
public final class ConsoleUtilities {

	public final String lineFeed = "\n";
	private String lineBreak;
	
	public void setLineBreakLength(int lineBreakLength) {
		
		this.lineBreak = new String(new char[lineBreakLength]).replace('\u0000', '-');
		
	}
	// Created to build a standard routine for displaying fields to the console.
	// Reduces code duplication.
	// A string is returned containing the resultant menu items formatted into
	// a menu selection styled screen layout. Console mode.
	// Created this method to facilitate the generic building
	// of a list of menu styled buttons to display to the user.
	// Reduces code duplication.
	// Method used in multiple places to display menu items.
	// 'menuItems' string array of the menu items to display.
	// 'singleColumn' integer value used to determine to build
	// a command style line or menu items.
	// Default is menu 0, 1 = Command Line
	public String createMenuItems(String[] menuItems, int singleColumn) {
	
		// Iterator of items in menuItems.
		short item = 0;
		String menu = "";
	
		while (item < menuItems.length) {
	
			// This could have been separated into 2 methods.
			// createMenuItems() and createCommandLine()
			// However, this is unnecessary an extra parameter to control
			// whether the display is in a menu or command line format
			// is sufficient.
			if (singleColumn == 1) {
				menu += menuItems[item] + this.lineFeed;
			} else {
				menu += String.format("%-" + 15 + "s", menuItems[item]);
			}
			item += 1;
		}
	
		if (singleColumn == 0) {
			menu += this.lineFeed;
		}
	
		return menu;
	}
	

	// Standard routine uses getInputString and then converts to
	// an Integer value.
	// It also makes sure the number is greater than zero.
	// Basic validation only.
	public int getNumberInput(String promptMessage, Scanner inputsSysIn) {
	    
		int integer = -1;
		String input = "";
	
		while (integer < 0) {
	
			try {
				
				input = getStringInput(promptMessage,inputsSysIn);
				integer = Integer.valueOf(input);

				if (integer < 0) {
					System.out.println("The number cannot be less than zero");
				}
				
				
			} catch (NumberFormatException nfe) {
				System.out.println("Non-Numeric data entered. Value must be numeric");
			}
	
		}
	
		return integer;
	}
	
	// Standard routine to prompt and collect input string.
	// This routine assumes string must contain a value.
	// Will re-prompt until value collected.
	// Safe assumption for this program as all data inputed
	// must contain a value.
	public String getStringInput(String promptMessage, Scanner inputsSysIn) {
	
		String choice = "";
	
		while (isStringFieldEmpty(choice)) {
	
			if (!isStringFieldEmpty(promptMessage)) {
				System.out.print(promptMessage);
			}
	
			choice = inputsSysIn.nextLine();
	
			if (isStringFieldEmpty(choice)) {
				System.out.println("Field cannot be empty. Try again.");
			}
	
		}
	
		return choice;
	}
	
	// Created to build a standard routine for displaying fields to the console.
	// Reduces code duplication.
	// A string is returned with a resultant Headings or Footer styled
	// screen presentation. Report like appearance. Console mode.
	// Print a heading on the screen. Printing headings needs
	// to be independent of each create screen display
	// methods.
	// Called in multiple locations.
	// 'title' A title heading for the banner.
	// 'align' alignment of the title
	// 0=center, 1=right, 2=left.
	@SuppressWarnings("unused")
	public String createScreenBanner(String title, int align) {
	
		String screenDisplay = this.lineBreak + this.lineBreak + this.lineFeed;
		int screenLength = getScreenLength();
		
		// Center align first then right align all else (default) left align.
		if (align == 0) {
			screenDisplay += String.format("%" + screenLength / 2 + "s", title) + this.lineFeed;
		} else if (align == 1) {
			screenDisplay += String.format("%" + screenLength + "s", title) + this.lineFeed;
		} else {
			screenDisplay += String.format("%-" + screenLength + "s", title) + this.lineFeed;
		}
	
		screenDisplay += this.lineBreak + this.lineBreak;
	
		return screenDisplay;
	}
	
	private int getScreenLength() {
		// This roughly centers the title using string.format.
		String screenDisplay = this.lineBreak + this.lineBreak + this.lineFeed;
		return (int) screenDisplay.length();
	}

	/*
	 * Debugging code
	 */
	public void displayArray(String[] theArray) {
	
		String str = "";
		
		for (int i = 0; i < theArray.length; i++) {
			str += " " + theArray[i] + "\n";
		}
		
		str += "Array Length " + theArray.length + "\n";
		
		//System.out.println(str);
		
		JFrame f=new JFrame();  
		JOptionPane.showMessageDialog(f,str);  
		
	}
	
	// Method required in multiple locations. Test a string if
	// blank or null returns true.
	public boolean isStringFieldEmpty(String stringObj) {
	
		// This routine/logic reduces code requirements, a little.
		// Centralizing debugging. Saves repeating same code over and over.
		if (stringObj == null) {
			stringObj = "";
		}
	
		return stringObj.strip().isEmpty();
	}
}
