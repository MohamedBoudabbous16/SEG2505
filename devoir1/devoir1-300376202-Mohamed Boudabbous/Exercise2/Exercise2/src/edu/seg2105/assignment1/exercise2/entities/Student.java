package edu.seg2105.assignment1.exercise2.entities;

/**
 * The Student class represents a student, extending the Person class.
 * It includes attributes and methods specific to students.
 * 
 * @autor Hussein Al Osman
 */
public class Student extends Person {
    // The program of study the student is enrolled in
    private String program;

    /**
     * Constructs a new Student with the specified details.
     *
     * @param firstName the first name of the student
     * @param lastName the last name of the student
     * @param id the unique identifier of the student
     * @param program the program of study the student is enrolled in
     */
    public Student(String firstName, String lastName, String id, String program) {
        super(firstName, lastName, id);
        this.program = program;
    }
    
    
       

    /**
     * Gets the program of study the student is enrolled in.
     *
     * @return the program of study
     */
    public String getProgram() {
        return program;
    }

    /**
     * Returns a string representation of the student.
     *
     * @return a string representation of the student
     */
    @Override
    public String toString() {
        return "Student information:\n"
                + "\tFirst name: " + getFirstName() + "\n"
                + "\tLast name: " + getLastName() + "\n"
                + "\tStudent ID: " + getId() + "\n"
                + "\tProgram of study: " + getProgram() + "\n";
    }

}
