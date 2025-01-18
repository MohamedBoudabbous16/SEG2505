package edu.seg2105.assignment1.exercise2.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.seg2105.assignment1.exercise2.backend.UniversityManagementSystem;
import edu.seg2105.assignment1.exercise2.util.InvalidUserInputException;

/**
 * The CommandLineUI class implements the UserInterface and provides command line interaction for the university management system.
 * 
 * @author Hussein Al Osman
 */
public class CommandLineUI implements UserInterface {
	private UniversityManagementSystem system;

	// Flag to specify whether the system should continue to wait for user input or terminate execution
	private boolean running;

	/**
	 * Constructs a new CommandLineUI and initializes the UniversitySystem.
	 */
	public CommandLineUI() {
		running = true;
		
		
	}
	
	public void setUniversityManagementSystem(UniversityManagementSystem system) {
		this.system = system;
	}
	

	
	/**
	 * This method overrides the method in the ChatIF interface. It
	 * displays a message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(Object message) {
		System.out.println("--" + message);
	}

	/**
	 * Accepts and processes user input from the command line.
	 */
	public void acceptInput()throws InvalidUserInputException {
		
		if (system == null) {
			throw new IllegalStateException("UniversityManagementSystem has not been set");
		}

		
		Scanner scanner = new Scanner(System.in);
		


		while (running) {
			System.out.print("> ");
			String command = scanner.nextLine();

			if (!command.equals("")) // Verify that it's not an empty command
				try {
					if (command.startsWith("exit") || command.startsWith("quit"))
						running = false;
					else
						processCommand(command);
				} catch (InvalidUserInputException e) {
					System.out.println("Invalid command: " + e.getMessage());
				}
		}

		scanner.close();
	}

	/**
	 * Processes the user command and delegates to the appropriate method.
	 * 
	 * @param command the user command
	 * @throws InvalidUserInputException if the command is invalid
	 */
	private void processCommand(String command) throws InvalidUserInputException {
		List<String> input = this.tokenize(command);

		if (input.size() < 2)
			throw new InvalidUserInputException("the command name or entity is missing");
		else {
			String commandName = input.remove(0).toUpperCase();
			String entity = input.remove(0).toUpperCase();
			

			// Create commands
			if (commandName.startsWith("CREATE")) {
				if (input.size() < 3)
					throw new InvalidUserInputException("the input is incomplete");
				
				if (entity.startsWith("STUDENT")) {
					// Creating a student entity
					String firstName = input.remove(0);
					String lastName = input.remove(0);
					String program = input.remove(0);
					
					system.processCreateStudent(firstName, lastName, program);
					
				} else if (entity.startsWith("PROFESSOR") ||
						   entity.startsWith("ADMIN") ||
						   entity.startsWith("TA") 
				) {
					// Creating an employee entity

					String firstName = input.remove(0);
					String lastName = input.remove(0);
					String salaryStr = input.remove(0);
					
					// Convert the salary string into a double
					salaryStr = salaryStr.replace(",", "");// remove commas (if they exist)


					// Convert the salary from string to double
					double salary;

					try {
						salary = Double.parseDouble(salaryStr);
					} catch (NumberFormatException e) {
						throw new InvalidUserInputException("the salary entry is invalid");
					}
					
					system.processCreateEmployee(entity, firstName, lastName, salary);
					
					
				} else if (entity.startsWith("COURSE")) {
					// Creating a course entity

					String courseCode = input.remove(0);
					String courseTitle = input.remove(0);
					String courseDescription = input.remove(0);
					
					
					system.processCreateCourse(courseCode, courseTitle, courseDescription);
				}
				else {
					throw new InvalidUserInputException("unrecognized input");
				}
			}
			// List commands
			else if (commandName.startsWith("LIST")) {
				if (entity.startsWith("STUDENT")) {
					system.processListStudents();
				} else if (entity.startsWith("EMPLOYEE") ||
						   entity.startsWith("PROFESSOR") ||
						   entity.startsWith("ADMIN") ||
						   entity.startsWith("TA") 
				) {
					system.processListEmployees(entity);
				} else if (entity.startsWith("COURSE")) {
					system.processListCourses();
				}
				else {
					throw new InvalidUserInputException("unrecognized input");
				}
			}
			// Get commands
			else if (commandName.startsWith("GET")) {
				if (input.size() < 1)
					throw new InvalidUserInputException("the input is incomplete");
				
				String idOrCourseCode = input.remove(0);
				
				if (entity.startsWith("STUDENT")) {

					
					system.processGetStudent(idOrCourseCode);
				} else if (entity.startsWith("EMPLOYEE") ||
						   entity.startsWith("PROFESSOR") ||
						   entity.startsWith("ADMIN") ||
						   entity.startsWith("TA") 
				) {
					system.processGetEmployee(idOrCourseCode);
				} else if (entity.startsWith("COURSE")) {
					system.processGetCourse(idOrCourseCode);
				}
				else {
					throw new InvalidUserInputException("unrecognized input");
				}
			}
			// Assign commands
			else if (commandName.startsWith("ASSIGN")) {
				if (input.size() < 2)
					throw new InvalidUserInputException("the input is incomplete");
				
				if (entity.startsWith("STUDENT")) {

					String id = input.remove(0);
					String courseCode = input.remove(0);
					system.processAssignStudent(id, courseCode);
					
				} else if (entity.startsWith("PROFESSOR")||
							entity.startsWith("TA")
						) {
					

					String id = input.remove(0);
					String courseCode = input.remove(0);
					
					system.processAssignInstructor(id, courseCode);
				}else if (entity.startsWith("ADMIN")){


					String id = input.remove(0);
					String task = input.remove(0);
					
					system.processAssignAdministrator(id, task);
				}
				else {
					throw new InvalidUserInputException("unrecognized input");
				}
			}
			// Unrecognized command
			else {
				throw new InvalidUserInputException("the command is not recognized");
			}
		}
	}

	/**
	 * Tokenizes a command string into a list of arguments.
	 * 
	 * @param command the command string
	 * @return a list of arguments
	 */
	private List<String> tokenize(String command) {
		List<String> tokens = new ArrayList<>();
		Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
		Matcher matcher = pattern.matcher(command);

		while (matcher.find()) {
			String token = matcher.group();
			token = removeQuotes(token); // Remove quotes if they are added

			tokens.add(token);
		}

		return tokens;
	}

	/**
	 * Removes quotes from a string if they exist.
	 * 
	 * @param str the string to process
	 * @return the string without quotes
	 */
	private String removeQuotes(String str) {
		if (str == null || str.length() < 2) {
			return str;
		}
		if (str.startsWith("\"") && str.endsWith("\"")) {
			return str.substring(1, str.length() - 1);
		}
		return str;
	}
}
