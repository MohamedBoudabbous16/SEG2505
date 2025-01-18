package edu.seg2105.assignment1.exercise2.entities;

import java.util.ArrayList;
import java.util.List;

import edu.seg2105.assignment1.exercise2.util.TableGenerator;

/**
 * The Instructor class represents an instructor, extending the Employee class.
 * It includes attributes and methods specific to instructors.
 * 
 * @author Hussein Al Osman
 */
public abstract class Instructor extends Employee {

    // List of courses assigned to the instructor
    protected List<Course> courses;

    /**
     * Constructs a new Instructor with the specified details.
     *
     * @param firstName the first name of the instructor
     * @param lastName the last name of the instructor
     * @param id the unique identifier of the instructor
     * @param salary the salary of the instructor
     */
    public Instructor(String firstName, String lastName, String id, double salary) {
        super(firstName, lastName, id, salary);
        courses = new ArrayList<Course>();
    }

    /**
     * Gets the list of courses assigned to the instructor.
     *
     * @return the list of courses
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Abstract method to assign a course to the instructor.
     * Subclasses must implement this method to specify constraints on course assignment.
     *
     * @param course the course to be assigned
     * @return a boolean specifying whether the course assignment was successful
     */
    public abstract boolean assignCourse(Course course);

    /**
     * Generates a table string of the courses assigned to the instructor.
     *
     * @return the formatted table string of courses
     */
    public String getCoursesTable() {
        // We will generate a table that corresponds to the list of courses taught by this instructor.
        // The table's columns are Course code, and Course title. 
        // To do so, we will transfer the data in the courses list into a 2D array
        // The length of the array corresponds to the number of elements in the courses list,
        // while the width is 2 (since we have two columns).
        int tableRows = courses.size();
        int tableColumns = 2;

        String[][] data = new String[tableRows][tableColumns]; // This 2D array will contain the data

        String[] headers = new String[tableColumns]; // This array will contain the column headers

        // Fill the first row with column headers
        headers[0] = "Course code";
        headers[1] = "Course title";

        // Fill the rest of the 2D array with the information from the courses list
        for (int i = 0; i < data.length; i++) {
            data[i][0] = courses.get(i).getCourseCode();
            data[i][1] = courses.get(i).getCourseTitle();
        }

        // Use our utility class to generate a string that is formatted like a table
        String tableStr = TableGenerator.generateTableString(data, headers, 20);

        return tableStr;
    }

    /**
     * Returns a string representation of the instructor's information.
     *
     * @return the string representation of the instructor
     */
    @Override
    public String toString() {
    	
    
        return "Professor information:\n"
                + "\tFirst name: " + getFirstName() + "\n"
                + "\tLast name: " + getLastName() + "\n"
                + "\tEmployee ID: " + getId() + "\n"
                + "\tSalary: " + getSalary() + "\n"
                + (courses.size() > 0 ? "\tList of assigned courses:" + getCoursesTable() : "");
                
         
    }
}
