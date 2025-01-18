package com.example.pcorderapplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.pcorderapplication.view.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testLoginSuccess() {
        // Saisir un email valide
        onView(withId(R.id.emailInputUnique))
                .perform(typeText("assembler@example.com"), closeSoftKeyboard());

        // Saisir un mot de passe valide
        onView(withId(R.id.passwordInputUnique))
                .perform(typeText("assembler123"), closeSoftKeyboard());

        // Appuyer sur le bouton de connexion
        onView(withId(R.id.loginButton))
                .perform(click());

        // Vérifier que le texte d'accueil s'affiche
        onView(withText("Welcome, Assembler! Manage your orders here."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginFailure() {
        // Saisir un email invalide
        onView(withId(R.id.emailInputUnique))
                .perform(typeText("invalid@example.com"), closeSoftKeyboard());

        // Saisir un mot de passe invalide
        onView(withId(R.id.passwordInputUnique))
                .perform(typeText("wrongpassword"), closeSoftKeyboard());

        // Appuyer sur le bouton de connexion
        onView(withId(R.id.loginButton))
                .perform(click());

        // Vérifier que le message d'erreur s'affiche
        onView(withText(R.string.error_invalid_credentials))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToRegisterActivity() {
        // Cliquer sur le lien "Register"
        onView(withId(R.id.registerTextView))
                .perform(click());

        // Vérifier que l'écran de registre s'affiche
        onView(withId(R.id.editTextFirstName))
                .check(matches(isDisplayed()));
    }
}
/*package com.example.pcorderapplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.example.pcorderapplication.view.MainActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testLoginSuccess() {
        onView(withId(R.id.emailInputUnique))
                .perform(typeText("assembler@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.passwordInputUnique))
                .perform(typeText("assembler123"), closeSoftKeyboard());

        onView(withId(R.id.loginButton))
                .perform(click());

        onView(withText("Welcome, Assembler! Manage your orders here."))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testLoginFailure() {
        onView(withId(R.id.emailInputUnique))
                .perform(typeText("invalid@example.com"), closeSoftKeyboard());

        onView(withId(R.id.passwordInputUnique))
                .perform(typeText("wrongpassword"), closeSoftKeyboard());

        onView(withId(R.id.loginButton))
                .perform(click());

        onView(withText(R.string.error_invalid_credentials))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateToRegisterActivity() {
        onView(withId(R.id.registerTextView))
                .perform(click());

        onView(withId(R.id.editTextFirstName))
                .check(matches(isDisplayed()));
    }
}
*/
