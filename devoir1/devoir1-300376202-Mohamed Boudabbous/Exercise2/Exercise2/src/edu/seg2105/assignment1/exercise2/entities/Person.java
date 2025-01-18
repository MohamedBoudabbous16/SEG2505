package edu.seg2105.assignment1.exercise2.entities;

/**
 * The Person class represents a generic person with a first name, last name, and ID.
 * It is an abstract class that requires subclasses to implement the showRole method.
 * 
 * @autor Hussein Al Osman
 */
public abstract class Person {
    // The first name of the person
    private String firstName;
    // The last name of the person
    private String lastName;
    // The unique identifier of the person
    private String id;

    /**
     * Constructs a new Person with the specified details.
     *
     * @param firstName the first name of the person
     * @param lastName the last name of the person
     * @param id the unique identifier of the person
     */
    public Person(String firstName, String lastName, String id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
    }
    
    
   

    /**
     * Gets the first name of the person.
     *
     * @return the first name of the person
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the person.
     *
     * @param firstName the first name to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name of the person.
     *
     * @return the last name of the person
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of the person.
     *
     * @param lastName the last name to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the unique identifier of the person.
     *
     * @return the ID of the person
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the person.
     *
     * @param id the ID to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns a string representation of the person.
     * This method must be implemented by subclasses.
     *
     * @return a string representation of the person
     */
    @Override
    public abstract String toString();
    
}
