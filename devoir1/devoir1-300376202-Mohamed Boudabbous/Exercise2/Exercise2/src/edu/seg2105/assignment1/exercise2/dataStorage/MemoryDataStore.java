package edu.seg2105.assignment1.exercise2.dataStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.seg2105.assignment1.exercise2.entities.Course;
import edu.seg2105.assignment1.exercise2.entities.Employee;
import edu.seg2105.assignment1.exercise2.entities.Student;

/**
 * The MemoryDataStore class is an in-memory implementation of the DataStoreInterface.
 * It stores and manages data for courses, employees, and students using HashMaps.
 * 
 * This implementation is suitable for non-persistent storage, useful in testing
 * or environments where database access is not required. In a real implementation, a database technology would be used.
 * 
 * @autor Hussein Al Osman
 */
public class MemoryDataStore implements DataStoreInterface {

	// Map to store courses with their course codes as keys
	private Map<String, Course> courses;
	// Map to store employees with their IDs as keys
	private Map<String, Employee> employees;
	// Map to store students with their IDs as keys
	private Map<String, Student> students;
	
	/**
	 * Constructs a new MemoryDataStore with empty maps for courses, employees, and students.
	 */
	public MemoryDataStore() {
		courses = new HashMap<>();
		employees = new HashMap<>();
		students = new HashMap<>();
	}
	
	/**
	 * Adds a student to the data store.
	 * 
	 * @param student the student to be added
	 */
	@Override
	public void addStudent(Student student) {
		students.put(student.getId(), student);		
	}
	
	/**
	 * Adds an employee to the data store.
	 * 
	 * @param employee the employee to be added
	 */
	@Override
	public void addEmployee(Employee employee) {
		employees.put(employee.getId(), employee);	
	}
	
	/**
	 * Adds a course to the data store.
	 * 
	 * @param course the course to be added
	 */
	@Override
	public void addCourse(Course course) {
		courses.put(course.getCourseCode(), course);
	}
	
	/**
	 * Updates the information of a student in the data store.
	 * This method does not modify any data, as it is intended for future database implementations.
	 * 
	 * @param student the student to be updated
	 */
	@Override
	public void updateStudent(Student student) {
		// Do nothing... This method is useful for DB implementations
	}

	/**
	 * Updates the information of an employee in the data store.
	 * This method does not modify any data, as it is intended for future database implementations.
	 * 
	 * @param employee the employee to be updated
	 */
	@Override
	public void updateEmployee(Employee employee) {
		// Do nothing... This method is useful for DB implementations
	}
	
	/**
	 * Updates the information of a course in the data store.
	 * This method does not modify any data, as it is intended for future database implementations.
	 * 
	 * @param course the course to be updated
	 */
	@Override
	public void updateCourse(Course course) {
		// Do nothing... This method is useful for DB implementations
	}
	
	/**
	 * Retrieves a student by their ID from the data store.
	 * 
	 * @param id the ID of the student
	 * @return the student associated with the given ID, or null if not found
	 */
	@Override
	public Student getStudent(String id) {
		return students.get(id);
	}
	
	/**
	 * Retrieves an employee by their ID from the data store.
	 * 
	 * @param id the ID of the employee
	 * @return the employee associated with the given ID, or null if not found
	 */
	@Override
	public Employee getEmployee(String id) {
		return employees.get(id);
	}
	
	/**
	 * Retrieves a course by its course code from the data store.
	 * 
	 * @param courseCode the course code of the course
	 * @return the course associated with the given course code, or null if not found
	 */
	@Override
	public Course getCourse(String courseCode) {
		return courses.get(courseCode);
	}
	
	/**
	 * Retrieves all students from the data store.
	 * 
	 * @return a collection of all students
	 */
	@Override
	public Collection<Student> getAllStudents() {
		return students.values();
	}
	
	/**
	 * Retrieves all employees from the data store.
	 * 
	 * @return a collection of all employees
	 */
	@Override
	public Collection<Employee> getAllEmployees() {
		return employees.values();
	}
	
	/**
	 * Retrieves all courses from the data store.
	 * 
	 * @return a collection of all courses
	 */
	@Override
	public Collection<Course> getAllCourses() {
		return courses.values();
	}
	
	/**
	 * Retrieves the total number of students in the data store.
	 * 
	 * @return the number of students
	 */
	@Override
	public int getNumberOfStudents() {
		return students.size();
	}

	/**
	 * Retrieves the total number of employees in the data store.
	 * 
	 * @return the number of employees
	 */
	@Override
	public int getNumberOfEmployees() {
		return employees.size();
	}

}
