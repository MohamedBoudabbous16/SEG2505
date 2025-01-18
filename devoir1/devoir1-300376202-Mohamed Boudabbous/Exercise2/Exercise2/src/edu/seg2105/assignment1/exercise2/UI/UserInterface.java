package edu.seg2105.assignment1.exercise2.UI;

import edu.seg2105.assignment1.exercise2.util.InvalidUserInputException;

/**
 * The UserInterface interface defines the methods for user interaction in the university management system.
 * It includes methods for accepting user input and displaying messages.
 * 
 * @author Hussein Al Osman
 */
public interface UserInterface {

	/**
	 * Accepts and processes user input.
	 */
	public void acceptInput() throws InvalidUserInputException;

	/**
	 * Displays a message to the user.
	 * 
	 * @param message the message to be displayed
	 */
	public void display(Object message);
}
