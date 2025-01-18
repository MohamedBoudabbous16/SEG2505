package edu.seg2105.assignment1.exercise2.entities;

/**
 * The Professor class represents a professor, extending the Instructor class and implementing the Teacher interface.
 * It includes methods specific to the role of a professor.
 * 
 * @author Hussein Al Osman
 */
public class Professor extends Instructor {

	private final int max_courses = 5;

	/**
	 * Constructs a new Professor with the specified details.
	 *
	 * @param firstName the first name of the professor
	 * @param lastName the last name of the professor
	 * @param id the unique identifier of the professor
	 * @param salary the salary of the professor
	 */
	public Professor(String firstName, String lastName, String id, double salary) {
		super(firstName, lastName, id, salary);
	}

	/**
	 * Assigns a course to the professor if the maximum number of courses is not exceeded.
	 * 
	 * @param course the course to be assigned
	 * @return true if the course was successfully assigned, false otherwise
	 */
	@Override
	public boolean assignCourse(Course course) {
		if (courses.size() < max_courses) {
			courses.add(course);
			return true;
		} else {
			return false;
		}
	}
}
