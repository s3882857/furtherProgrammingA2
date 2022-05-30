package utilities;

import java.time.LocalDate;
import java.util.regex.PatternSyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Utilities {

	/*
	 * Debugging code
	 */
	public void displayArray(String[] theArray) {

		String str = "";

		for (int i = 0; i < theArray.length; i++) {
			str += " " + theArray[i] + "\n";
		}

		str += "Array Length " + theArray.length + "\n";

		// System.out.println(str);

		JFrame f = new JFrame();
		JOptionPane.showMessageDialog(f, str);

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

	// Method required in multiple places
	public boolean validCharacters(String stringToCheck, String validCharacters) {

		int i = 0;
		String charToCheck = "";
		boolean validCharacter = true;

		// Validates a string using a string list to determine what characters are
		// present.
		while (i < stringToCheck.length()) {

			charToCheck = String.valueOf(stringToCheck.charAt(i)).toUpperCase();

			if (!validCharacters.contains(charToCheck)) {
				validCharacter = false;
				i = stringToCheck.length();
			}

			i += 1;

		}
		return validCharacter;

	}
	
	/*
	 * Convert date string to LocalDate object.
	 * Format YYYY-MM-DD
	 */
	public LocalDate convertDate(String dateIn) {
		
		LocalDate newDate = null;
		
		if(!this.isStringFieldEmpty(dateIn)) {
		
			try {
				
				String[] formattedDate = dateIn.split("-");
				
				int year = Integer.parseInt(formattedDate[0]);
				int month = Integer.parseInt(formattedDate[1]);
				int day = Integer.parseInt(formattedDate[2]);
				
				newDate = LocalDate.of(year, month, day);
				
			}
			catch(NumberFormatException nfe) {
				
				newDate = LocalDate.now();
				
			}
			catch(PatternSyntaxException e) {
				
				newDate = LocalDate.now();
				
			}
		}
				
		return newDate;
	}
}
