package edu.seg2105.assignment1.exercise2.entities;


/**
 * The Employee class represents an employee, extending the Person class.
 * It includes additional attributes and methods specific to employees.
 * 
 * @autor Hussein Al Osman
 */
public abstract class Employee extends Person {

    // The salary of the employee
    private double salary;

    /**
     * Constructs a new Employee with the specified details.
     *
     * @param firstName the first name of the employee
     * @param lastName the last name of the employee
     * @param id the unique identifier of the employee
     * @param salary the salary of the employee
     */
    public Employee(String firstName, String lastName, String id, double salary) {
        // Call the constructor of the superclass Person
        super(firstName, lastName, id);

        // Set the salary of the employee
        this.salary = salary;
    }

    /**
     * Gets the salary of the employee.
     *
     * @return the salary of the employee
     */
    public double getSalary() {
        return salary;
    }
    
	/**
	 * Returns a string representation of the employee information.
	 * 
	 * @return the string representation of the administrator
	 */
	@Override
	public String toString() {
		

		return "Employee information:\n"
				+ "\tFirst name: " + getFirstName() + "\n"
				+ "\tLast name: " + getLastName() + "\n"
				+ "\tEmployee ID: " + getId() + "\n"
				+ "\tSalary: " + getSalary() + "\n";
				
		
	}

}
