package com.example.pcorderapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.view.View;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pcorderapplication.view.RegisterActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisterActivityTest {

    @Rule
    public ActivityScenarioRule<RegisterActivity> activityRule =
            new ActivityScenarioRule<>(RegisterActivity.class);


    /*
    @Test
    public void testSuccessfulRegistrationFlow() {
        // Simulate valid user input
        onView(withId(R.id.editTextFirstName))
                .perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.editTextLastName))
                .perform(typeText("Doe"), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail))
                .perform(typeText("johndoe@example.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword))
                .perform(typeText("Password123"), closeSoftKeyboard());

        // Click the register button
        onView(withId(R.id.loginButton)).perform(click());

        // Wait for the Toast to appear (use Thread.sleep temporarily if necessary)
        try {
            Thread.sleep(2000); // Temporary delay (replace with IdlingResource for better synchronization)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify the Toast message
        onView(withText("User registered successfully!"))
                .inRoot(new ToastMatcher()) // Use the custom ToastMatcher
                .check(matches(isDisplayed()));
    }*/

    @Test
    public void testValidationFailsForEmptyFields() {
        // Leave all fields empty and click the button
        onView(withId(R.id.loginButton)).perform(click());

        // Verify the error message for the first field (first name)
        onView(withId(R.id.editTextFirstName))
                .check(matches(hasErrorText("Please enter a first name")));
    }

    // Custom matcher to verify the error text of a TextInputEditText
    private static Matcher<View> hasErrorText(final String expectedError) {
        return new TextInputLayoutErrorMatcher(expectedError);
    }
}
