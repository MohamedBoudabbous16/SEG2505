package edu.seg2105.assignment1.exercise2.entities;

import java.util.ArrayList;
public class TeachingAssistant extends instructor{
    private final int max_courses = 3;

    public TeachingAssistant(String firstName, String lastName, String id, double salary) {
        super(firstName, lastName, id, salary);
        this.courses = new ArrayList<Course>();
    }
        @Override
        public boolean assignCourse(Course course) {
            if (courses.size() < max_courses) {
                courses.add(course);
                return true;
            } else {
                return false;
            }

    }
    public String toString() {
        return "Teaching Assistant information:\n" + "\tFirst name: " + getFirstName() + "\n" + "\tLast name: " + getLastName() + "\n" + "\tEmployee ID: " + getId() + "\n" + "\tSalary: " + getSalary() + "\n" + (courses.size() > 0 ? "\tList of assigned courses:" + getCoursesTable() : "");
    }
}
}
