package com.example.pcorderapplication;

import android.view.View;
import android.widget.EditText;

import androidx.test.espresso.matcher.BoundedMatcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Custom Matcher to check the error text of an EditText (including TextInputEditText).
 */
public class TextInputLayoutErrorMatcher extends BoundedMatcher<View, EditText> {

    private final String expectedError;

    public TextInputLayoutErrorMatcher(String expectedError) {
        super(EditText.class);
        this.expectedError = expectedError;
    }

    @Override
    protected boolean matchesSafely(EditText editText) {
        CharSequence error = editText.getError();
        return error != null && error.toString().equals(expectedError);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("with error text: ").appendValue(expectedError);
    }
}
