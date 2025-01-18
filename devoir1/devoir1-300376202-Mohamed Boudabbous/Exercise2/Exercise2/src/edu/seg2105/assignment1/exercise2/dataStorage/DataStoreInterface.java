package edu.seg2105.assignment1.exercise2.dataStorage;

import java.util.Collection;

import edu.seg2105.assignment1.exercise2.entities.Course;
import edu.seg2105.assignment1.exercise2.entities.Employee;
import edu.seg2105.assignment1.exercise2.entities.Student;

public interface DataStoreInterface {
	
	
	// Store a Student
    public abstract void addStudent(Student student);

    // Store an Employee (Professor or Administrator)
    public abstract void addEmployee(Employee employee);

    // Store Course information
    public abstract void addCourse(Course course);
        
	// Update a Student information
    public abstract void updateStudent(Student student);

    // Update an Employee (Professor or Administrator) information
    public abstract void updateEmployee(Employee employee);

    // Update a Course information
    public abstract void updateCourse(Course course);
    
    // Retrieve a specific Student by ID
    public abstract Student getStudent(String id);

    // Retrieve a specific Employee by ID
    public abstract Employee getEmployee(String id);

    // Retrieve a specific Course by course code
    public abstract Course getCourse(String courseCode);

    // Retrieve all Students
    public abstract Collection<Student> getAllStudents();

    // Retrieve all Employees
    public abstract Collection<Employee> getAllEmployees();

    // Retrieve all Courses
    public abstract Collection<Course> getAllCourses();
   
    // Get the number of students stored in the system
    public abstract int getNumberOfStudents();
    
    // Get the number of employees stored in the system
    public abstract int getNumberOfEmployees();
}
