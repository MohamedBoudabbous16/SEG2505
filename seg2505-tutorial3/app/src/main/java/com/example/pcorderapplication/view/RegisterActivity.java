package com.example.pcorderapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcorderapplication.R;
import com.example.pcorderapplication.controller.LoginController;
import com.example.pcorderapplication.model.entity.UserInfo;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonRegister;
    private LoginController loginController;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginController = new LoginController(getApplicationContext());
        db = FirebaseFirestore.getInstance();

        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.loginButton);

        buttonRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (!validateInput(firstName, lastName, email, password)) {
            return;
        }

        loginController.registerUser(firstName, lastName, email, password, new LoginController.OnRegisterResultListener() {
            @Override
            public void onRegisterSuccess(UserInfo user) {
                Toast.makeText(RegisterActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                saveUserRole(user);
            }

            @Override
            public void onRegisterFailure(String errorMessage) {
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String firstName, String lastName, String email, String password) {
        if (TextUtils.isEmpty(firstName)) {
            editTextFirstName.setError("Please enter a first name");
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            editTextLastName.setError("Please enter a last name");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter an email");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter a password");
            return false;
        }
        return true;
    }

    private void saveUserRole(UserInfo userInfo) {
        String email = userInfo.getEmail();
        if (email == null) return;

        Map<String, Object> userRole = new HashMap<>();
        userRole.put("role", "Requester");

        db.collection("users").document(email)
                .set(userRole)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Role set to 'Requester' successfully!", Toast.LENGTH_SHORT).show();
                    navigateToMainActivity();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to set role: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
