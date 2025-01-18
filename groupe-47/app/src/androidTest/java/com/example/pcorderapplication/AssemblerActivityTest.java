package com.example.pcorderapplication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pcorderapplication.view.AssemblerActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class AssemblerActivityTest {

    @Test
    public void testAssemblerActivityLaunches() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.example.pcorderapplication", "com.example.pcorderapplication.view.AssemblerActivity");

        try (ActivityScenario<AssemblerActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.titleTextView)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testButtonsAreDisplayed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.example.pcorderapplication", "com.example.pcorderapplication.view.AssemblerActivity");

        try (ActivityScenario<AssemblerActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.acceptOrderButton)).check(matches(isDisplayed()));
            onView(withId(R.id.rejectOrderButton)).check(matches(isDisplayed()));
            onView(withId(R.id.completeOrderButton)).check(matches(isDisplayed()));
            onView(withId(R.id.updateOrderButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testRecyclerViewIsDisplayed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClassName("com.example.pcorderapplication", "com.example.pcorderapplication.view.AssemblerActivity");

        try (ActivityScenario<AssemblerActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.ordersRecyclerView)).check(matches(isDisplayed()));
        }
    }
}
