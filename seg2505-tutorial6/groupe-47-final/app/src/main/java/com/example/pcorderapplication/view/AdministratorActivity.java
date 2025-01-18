package com.example.pcorderapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pcorderapplication.R;
import com.example.pcorderapplication.controller.AdministratorController;
import com.example.pcorderapplication.databinding.ActivityAdministratorBinding;
import com.example.pcorderapplication.model.users.Requester;

import java.util.ArrayList;
import java.util.List;

public class AdministratorActivity extends AppCompatActivity {

    private AdministratorController administratorController;
    private ActivityAdministratorBinding binding;
    private ArrayAdapter<String> requesterListAdapter;
    private List<String> requesterDisplayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdministratorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        administratorController = AdministratorController.getInstance(
                this,
                "Admin",
                "Administrator",
                "admin@gmail.com",
                "admin123"
        );

        requesterDisplayList = new ArrayList<>();
        requesterListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requesterDisplayList);
        binding.requestersListView.setAdapter(requesterListAdapter);

        setupButtonListeners();
        viewAllRequesters();
    }

    private void setupButtonListeners() {
        binding.createRequesterButton.setOnClickListener(v -> createRequester());
        binding.modifyRequesterButton.setOnClickListener(v -> modifyRequester());
        binding.deleteRequesterButton.setOnClickListener(v -> deleteRequester());
        binding.goBackArrow.setOnClickListener(v -> finish());
    }

    private void createRequester() {

        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        /*
        String firstName = binding.firstNameEditText.getText().toString().trim();
        String lastName = binding.lastNameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();

        if (!firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty()) {
            administratorController.createRequester(firstName, lastName, email, "password123");
            Toast.makeText(this, "Requester created: " + firstName + " " + lastName, Toast.LENGTH_SHORT).show();
            viewAllRequesters();
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void modifyRequester() {
        String email = binding.emailEditText.getText().toString().trim();
        String newFirstName = binding.firstNameEditText.getText().toString().trim();
        String newLastName = binding.lastNameEditText.getText().toString().trim();

        if (!email.isEmpty() && !newFirstName.isEmpty() && !newLastName.isEmpty()) {
            administratorController.modifyRequester(newFirstName, newLastName, email);
            Toast.makeText(this, "Requester modified.", Toast.LENGTH_SHORT).show();
            viewAllRequesters();
        } else {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRequester() {
        String email = binding.emailEditText.getText().toString().trim();

        if (!email.isEmpty()) {
            administratorController.deleteRequester(null, null, email);
            Toast.makeText(this, "Requester deleted.", Toast.LENGTH_SHORT).show();
            viewAllRequesters();
        } else {
            Toast.makeText(this, "Please enter an email.", Toast.LENGTH_SHORT).show();
        }
    }

    private void viewAllRequesters() {
        List<Requester> requesters = administratorController.getAllRequesters();
        requesterDisplayList.clear();

        if (requesters != null && !requesters.isEmpty()) {
            for (Requester requester : requesters) {
                requesterDisplayList.add(requester.getFirstName() + " " + requester.getLastName() + " (" + requester.getEmail() + ")");
            }
            requesterListAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No requesters found.", Toast.LENGTH_SHORT).show();
        }
    }
}
