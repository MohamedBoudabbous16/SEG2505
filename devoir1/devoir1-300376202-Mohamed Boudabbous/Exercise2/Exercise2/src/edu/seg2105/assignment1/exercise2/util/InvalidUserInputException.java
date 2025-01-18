package edu.seg2105.assignment1.exercise2.util;

/**
 * The InvalidUserInputException class represents an exception that is thrown
 * when the user enters invalid (e.g., missing) input into the system through the UI
 * 
 * @autor Hussein Al Osman
 */
public class InvalidUserInputException extends Exception {

    // Serial version UID for serialization
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new InvalidUserInputException with no detail message.
     */
    public InvalidUserInputException() {
        super();
    }

    /**
     * Constructs a new InvalidUserInputException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidUserInputException(String message) {
        super(message);
    }
}
