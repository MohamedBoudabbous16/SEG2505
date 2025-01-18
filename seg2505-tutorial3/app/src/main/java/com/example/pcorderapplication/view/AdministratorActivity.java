package com.example.pcorderapplication.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcorderapplication.R;
import com.example.pcorderapplication.controller.AdministratorController;
import com.example.pcorderapplication.controller.RequesterController;
import com.example.pcorderapplication.databinding.ActivityAdministratorBinding;
import com.example.pcorderapplication.databinding.ActivityMainBinding;
import com.example.pcorderapplication.model.users.Administrator;
import com.example.pcorderapplication.model.users.Requester;

public class AdministratorActivity extends AppCompatActivity {

    private AdministratorController administratorController;
    private RequesterController requesterController;

    private ActivityAdministratorBinding binding;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        // Initializing Binder
        binding = ActivityAdministratorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Create an Administrator object
        administratorController = AdministratorController.getInstance(getApplicationContext(), "Admin", "Smith", "admin@example.com", "admin123");
        requesterController = RequesterController.getInstance(getApplicationContext(), "John", "Doe", "john.doe@example.com", "request123");

        // Set welcome message directly as text
        Toast.makeText(this, "Welcome, Administrator! You can manage requesters here.", Toast.LENGTH_SHORT).show();

        // Button functionality without string resources
        binding.createRequesterButton.setOnClickListener(v -> createRequester());
        binding.modifyRequesterButton.setOnClickListener(v -> modifyRequester());
        binding.deleteRequesterButton.setOnClickListener(v -> deleteRequester());
        binding.viewAllRequestersButton.setOnClickListener(v -> viewAllRequesters());
    }

    private void createRequester() {
        String firstName = binding.firstNameEditText.getText().toString();
        String lastName = binding.lastNameEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()) {
            administratorController.createRequester(firstName, lastName, email, "password123");
            Toast.makeText(this, "Requester " + firstName + " " + lastName + " has been created.", Toast.LENGTH_SHORT).show();
        } else {
            textView.setText("Please fill in all the fields to create a requester.");
        }
    }

    private void modifyRequester() {
        String newFirstName = binding.firstNameEditText.getText().toString();
        String newLastName = binding.lastNameEditText.getText().toString();
        String newEmail = binding.emailEditText.getText().toString();

        administratorController.modifyRequester(newFirstName, newLastName, newEmail);
        textView.setText("Requester " + newFirstName + " " + newLastName + " has been modified.");
    }

    private void deleteRequester() {
        String firstName = binding.firstNameEditText.getText().toString();
        String lastName = binding.lastNameEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();
        administratorController.deleteRequester(firstName, lastName, email);
        textView.setText("Requester " + firstName + " " + lastName + " has been deleted.");
    }

    private void viewAllRequesters() {
        //TODO view all the requesters
        Toast.makeText(this, "Viewing all requesters...", Toast.LENGTH_SHORT).show();
    }
}
