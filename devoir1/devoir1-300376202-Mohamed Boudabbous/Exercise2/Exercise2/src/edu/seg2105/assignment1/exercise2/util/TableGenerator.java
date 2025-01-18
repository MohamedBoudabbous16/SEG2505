package edu.seg2105.assignment1.exercise2.util;

/**
 * The TableGenerator class provides functionality to generate formatted table strings.
 * 
 * @author Hussein Al Osman
 */
public class TableGenerator {

	/**
	 * Generates a formatted table string with the given data and headers.
	 * 
	 * @param data the 2D array of data to be displayed in the table
	 * @param headers the array of headers for the table columns
	 * @param columnWidth the width of each column in the table
	 * @return the formatted table string
	 */
	public static String generateTableString(String[][] data, String[] headers, int columnWidth) {
		
		String format = "\t\t\t"; // The tables will be indented
		
		// Specify the spacing of the columns
		for (int column = 0; column < headers.length; column++) {
			format += "%-" + columnWidth + "s";
		}
		format += "\n";

		// Build the string that represents the table
		StringBuilder table = new StringBuilder();
		
		table.append(String.format(format, (Object[]) headers)); // add the headers first
		
		// add the rest of the data
		for (int row = 0; row < data.length; row++) {
			table.append(String.format(format, (Object[]) data[row]));
		}

		return "\n" + table.toString() + "\n";
	}
}
