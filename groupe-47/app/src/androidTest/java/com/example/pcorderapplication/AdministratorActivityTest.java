package com.example.pcorderapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertNotNull;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pcorderapplication.view.AdministratorActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AdministratorActivityTest {

    @Test
    public void testAdministratorActivityLaunches() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.example.pcorderapplication", "com.example.pcorderapplication.view.AdministratorActivity");

        try (ActivityScenario<AdministratorActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> assertNotNull(activity));
        }
    }

    @Test
    public void testViewsAreDisplayed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.example.pcorderapplication", "com.example.pcorderapplication.view.AdministratorActivity");

        try (ActivityScenario<AdministratorActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
            onView(withId(R.id.createRequesterButton)).check(matches(isDisplayed()));
            onView(withId(R.id.modifyRequesterButton)).check(matches(isDisplayed()));
            onView(withId(R.id.deleteRequesterButton)).check(matches(isDisplayed()));
            onView(withId(R.id.requestersListView)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testButtonHasCorrectText() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.example.pcorderapplication", "com.example.pcorderapplication.view.AdministratorActivity");

        try (ActivityScenario<AdministratorActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.createRequesterButton)).check(matches(withText("Create Requester")));
            onView(withId(R.id.modifyRequesterButton)).check(matches(withText("Modify Requester")));
            onView(withId(R.id.deleteRequesterButton)).check(matches(withText("Delete Requester")));
        }
    }
}
