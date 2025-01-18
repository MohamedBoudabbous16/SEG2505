package edu.seg2105.assignment1.exercise2.backend;

import java.util.Collection;

import edu.seg2105.assignment1.exercise2.UI.UserInterface;
import edu.seg2105.assignment1.exercise2.dataStorage.DataStoreInterface;
import edu.seg2105.assignment1.exercise2.entities.*;

/**
 * The UniversityManagementSystem processes the commands that are received by the user interface.
 * 
 * @author Hussein Al Osman
 */
public class UniversityManagementSystem {


	// The length of the ID generated for students and employees
	public static final int employeeIdLength = 7;
	public static final int studentIdLength = 10;
	

	
	// Reference to the user interface (an object instantiated from a class that implements the UserInterface interface)
	UserInterface ui;
	
	// Reference to a data store mechanism (e.g., database, filesystem, memory...)
	DataStoreInterface store;
	

	/**
	 * Constructs a new UniversitySystem and initializes the data structures.
	 */
	public UniversityManagementSystem(UserInterface ui, DataStoreInterface store) {
		

		this.ui = ui;
		
		this.store = store;
		
	}



	// METHODS THAT PROCESS THE CREATE COMMANDS ----------------------------------------------

	/**
	 * Processes the creation of a new student.
	 * 
	 * @param input the list of arguments for creating a student
	 */
	public void processCreateStudent(String firstName, String lastName, String program) {
		
		// Generate a student ID
		String id = generateID(store.getNumberOfStudents() + 1, studentIdLength);
		

		Student student = new Student(firstName, lastName, id, program);

		store.addStudent(student);
		
		
		ui.display("Student entity created");
		ui.display(student);
	}

	/**
	 * Processes the creation of a new employee.
	 * 
	 * @param entity the type of employee to create
	 * @param input the list of arguments for creating an employee
	 */
	public void processCreateEmployee(String entity, String firstName, String lastName, double salary) {

		

		// Generate an employee ID
		String id = generateID(store.getNumberOfEmployees() + 1, employeeIdLength);

		Employee employee;
		
		// Figure out which employee object to create based on the entity string
		if (entity.startsWith("PROFESSOR"))
			employee = new Professor(firstName, lastName, id, salary);
		else if (entity.startsWith("ADMIN"))
			employee = new Administrator(firstName, lastName, id, salary);
		else {
			employee = new TeachingAssistant(firstName, lastName, id, salary);
		}

		if (employee != null) {
			store.addEmployee(employee);

			ui.display("Employee entity created");
			ui.display(employee);
		}

	}

	/**
	 * Processes the creation of a new course.
	 * 
	 * @param input the list of arguments for creating a course
	 */
	public void processCreateCourse(String courseCode, String courseTitle, String courseDescription) {
		Course course = new Course(courseCode, courseTitle, courseDescription);

		store.addCourse(course);

		ui.display("Course entity created");
		ui.display(course);

	}

	// METHODS THAT PROCESS THE LIST COMMANDS ----------------------------------------------

	/**
	 * Processes the listing of all students.
	 */
	public void processListStudents() {
		Collection<Student> students = store.getAllStudents();
		for (Student student : students) {
			ui.display(student);
		}
	}

	/**
	 * Processes the listing of employees.
	 * 
	 * @param entity the type of employee to list
	 */
	public void processListEmployees(String entity) {
		Collection<Employee> employees = store.getAllEmployees();
		
		for (Employee employee : employees) {
			if (entity.startsWith("EMPLOYEE") || // print all employees if the entity is employee
					entity.startsWith("PROFESSOR") && employee instanceof Professor || // print only professors if the entity is professor
					entity.startsWith("ADMIN") && employee instanceof Administrator ||  // print only administrators if the entity is administor
					entity.startsWith("TA") && employee instanceof TeachingAssistant){

				ui.display(employee);
			}
		}
	}

	/**
	 * Processes the listing of all courses.
	 */
	public void processListCourses() {
		Collection<Course> courses = store.getAllCourses();
		
		for (Course course : courses) {
			ui.display(course);
		}
	}

	// METHODS THAT PROCESS THE GET COMMANDS ----------------------------------------------

	/**
	 * Processes the get student command.
	 * 
	 * @param input the list of arguments for getting a student
	 */
	public void processGetStudent(String id) {		
		Student student = store.getStudent(id);
		
		if (student == null) 
			ui.display("A student with ID "+id+" does not exist");
		else 
			ui.display(student);
	}

	/**
	 * Processes the get employee command.
	 * 
	 * @param input the list of arguments for getting an employee
	 */
	public void processGetEmployee(String id) {
		
		Employee employee = store.getEmployee(id);
		
		if (employee == null) 
			ui.display("An employee with ID "+id+" does not exist");
		else 
			ui.display(employee);
	}

	/**
	 * Processes the get course command.
	 * 
	 * @param input the list of arguments for getting a course
	 */
	public void processGetCourse(String courseCode) {
		
		Course course = store.getCourse(courseCode);
		
		if (course == null) 
			ui.display("A course with this code "+courseCode+" does not exist");
		else 
			ui.display(course);
	}
	
	// METHODS THAT PROCESS THE ASSIGN COMMANDS ----------------------------------------------

	/**
	 * Processes assigning (i.e., registering) a student to a course.
	 * 
	 * @param input the list of arguments for adding a student to a course
	 */
	public void processAssignStudent(String id, String courseCode) {

		// Find student using the student number
		Student student = store.getStudent(id);

		if (student == null) {
			ui.display("Incorrect student id");
			return;
		}

		// Find the course using the course code
		Course course = store.getCourse(courseCode);

		if (course == null) {
			ui.display("Incorrect course code");
			return;
		}

		course.addStudent(student);
		store.updateCourse(course);

		ui.display("Student added");
		ui.display(course);
	}

	/**
	 * Processes assigning an instructor to a course.
	 * 
	 * @param input the list of arguments for adding a professor to a course
	 */
	public void processAssignInstructor(String id, String courseCode){

		// Find instructor using the employee id
		Instructor instructor = (Instructor) store.getEmployee(id);

		if (instructor == null) {
			ui.display("Incorrect employee id");
			return;
		}

		// Find the course using the course code
		Course course = store.getCourse(courseCode);

		if (course == null) {
			ui.display("Incorrect course code");
			return;
		}

		boolean success = instructor.assignCourse(course);
		
		if (success) { // check if I can assign the course to the instructor
			if (instructor instanceof Professor) { 
				// The instructor is a professor, therefore add them as a professor
				course.setProfessor((Professor)instructor);
				store.updateCourse(course);

				ui.display("Professor added");
				ui.display(course);
			}
			else if (instructor instanceof TeachingAssistant) {
				// The instructor is a professor, therefore add them as a professor
				course.setTeachingAssistant((TeachingAssistant)instructor);
				store.updateCourse(course);

				ui.display("Teaching Assistant added");
				ui.display(course);
			}
		}
		else {
			
			ui.display("The instructor cannot be added to the course.");
		}
	}


	/**
	 * Processes assigning an instructor to a course.
	 * 
	 * @param input the list of arguments for adding a professor to a course
	 */
	public void processAssignAdministrator(String id, String task) {

		// TODO Complete this method so that an administrator can be assigned tasks
		//on trouve l'employer en utulisant l'ID
		Employee employee = store.getEmployee(id);

		if (employee == null) {
			ui.display("Incorrect employee id");
			return;
		}

		// vérifier si l'employer est un Administrateur
		if (employee instanceof Administrator) {
			Administrator admin = (Administrator) employee;
			admin.addTask(task);
			store.updateEmployee(admin);

			ui.display("Task assigned to administrator");
			ui.display(admin);
		} else {
			ui.display("Employee with ID " + id + " is not an administrator");
		}
	}


	
	

	// UTILITY METHODS ----------------------------------------------
	/**
	 * Generates a unique ID with the specified length.
	 * 
	 * @param id the ID number to format
	 * @param length the length of the ID
	 * @return the formatted ID string
	 */
	private String generateID(int id, int length) {
		// Create a format string with the specified length
		String formatString = "%0" + length + "d";
		return String.format(formatString, id);
	}
	
	

}
