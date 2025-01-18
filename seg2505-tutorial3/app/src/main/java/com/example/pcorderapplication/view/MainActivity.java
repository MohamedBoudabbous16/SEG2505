package com.example.pcorderapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pcorderapplication.R;
import com.example.pcorderapplication.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    private boolean isTestMode = false;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ActivityMainBinding binding;
    private EditText emailInput;
    private EditText passwordInput;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailInput = binding.emailInputUnique;
        passwordInput = binding.passwordInputUnique;
        Button loginButton = binding.loginButton;

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            logLoginAttempt(email);
            Log.d(TAG, "Attempting to login with email: " + email);

            if (isTestMode) {
                testAuthenticateUser(email, password);
            } else {
                authenticateUser(email, password);
            }
        });

        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(v -> {
            Log.d(TAG, "Navigating to RegisterActivity");
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void authenticateUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Authenticating user with Firebase");

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d(TAG, "Login successful for user: " + user.getEmail());
                            fetchUserRole(user);
                        } else {
                            Log.e(TAG, "Login failed: user is null after authentication");
                            showErrorMessage();
                        }
                    } else {
                        Log.e(TAG, "Authentication failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                        showErrorMessage();
                    }
                });
    }

    private void fetchUserRole(FirebaseUser user) {
        String email = user.getEmail();
        if (email == null) {
            Log.e(TAG, "fetchUserRole failed: user email is null");
            showErrorMessage();
            return;
        }

        Log.d(TAG, "Fetching role for email: " + email);

        db.collection("users").document(email).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String role = documentSnapshot.getString("role");
                        if (role != null) {
                            navigateToActivity(role);
                        } else {
                            Log.e(TAG, "No role found in document for email: " + email);
                            showErrorMessage();
                        }
                    } else {
                        Log.e(TAG, "No document found for email: " + email);
                        showErrorMessage();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to fetch role: " + e.getMessage());
                    showErrorMessage();
                });
    }

    private void navigateToActivity(String role) {
        Intent intent;
        switch (role) {
            case "Administrator":
                intent = new Intent(this, AdministratorActivity.class);
                break;
            case "StoreKeeper":
                intent = new Intent(this, StoreKeeperActivity.class);
                break;
            case "Assembler":
                intent = new Intent(this, AssemblerActivity.class);
                break;
            case "Requester":
                intent = new Intent(this, RequesterActivity.class);
                break;
            default:
                Log.e(TAG, "Unrecognized role: " + role);
                showErrorMessage();
                return;
        }
        Log.d(TAG, "Navigating to activity for role: " + role);
        startActivity(intent);
        finish();
    }

    private void showErrorMessage() {
        Log.e(TAG, "Invalid credentials or other error");
        Toast.makeText(this, getString(R.string.error_invalid_credentials), Toast.LENGTH_SHORT).show();
    }

    private void logLoginAttempt(String email) {
        Log.d(TAG, "Logging login attempt for email: " + email);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "email_password");
        bundle.putString("email_attempted", email);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }


    private void testAuthenticateUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Test mode authentication for email: " + email);

        switch (email) {
            case "admin@example.com":
                if (password.equals("admin123")) {
                    Log.d(TAG, "Test mode login successful for role: Administrator");
                    navigateToActivity("Administrator");
                } else {
                    showErrorMessage();
                }
                break;
            case "storekeeper@example.com":
                if (password.equals("store123")) {
                    Log.d(TAG, "Test mode login successful for role: StoreKeeper");
                    navigateToActivity("StoreKeeper");
                } else {
                    showErrorMessage();
                }
                break;
            case "assembler@example.com":
                if (password.equals("assembler123")) {
                    Log.d(TAG, "Test mode login successful for role: Assembler");
                    navigateToActivity("Assembler");
                } else {
                    showErrorMessage();
                }
                break;
            case "requester@example.com":
                if (password.equals("request123")) {
                    Log.d(TAG, "Test mode login successful for role: Requester");
                    navigateToActivity("Requester");
                } else {
                    showErrorMessage();
                }
                break;
            default:
                Log.e(TAG, "Test mode login failed: invalid credentials");
                showErrorMessage();
                break;
        }
    }
}



/*
package com.example.pcorderapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcorderapplication.R;
import com.example.pcorderapplication.databinding.ActivityMainBinding;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mAuth;
    private ActivityMainBinding binding;
    private EditText emailInput;
    private EditText passwordInput;
    private boolean isTestMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mAuth = FirebaseAuth.getInstance();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailInput = binding.emailInputUnique;
        passwordInput = binding.passwordInputUnique;
        Button loginButton = binding.loginButton;

        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            logLoginAttempt(email);

            if (isTestMode) {
                testAuthenticateUser(email, password);
            } else {
                authenticateUser(email, password);
            }
        });

        // Ajoutez un OnClickListener pour registerTextView
        TextView registerTextView = findViewById(R.id.registerTextView);
        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }


    private void authenticateUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            navigateToActivity(user);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }

    private void testAuthenticateUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        switch (email) {
            case "admin@example.com":
                if (password.equals("admin123")) {
                    navigateToActivity("Administrator");
                } else {
                    showErrorMessage();
                }
                break;
            case "storekeeper@example.com":
                if (password.equals("store123")) {
                    navigateToActivity("StoreKeeper");
                } else {
                    showErrorMessage();
                }
                break;
            case "assembler@example.com":
                if (password.equals("assembler123")) {
                    navigateToActivity("Assembler");
                } else {
                    showErrorMessage();
                }
                break;
            case "requester@example.com":
                if (password.equals("request123")) {
                    navigateToActivity("Requester");
                } else {
                    showErrorMessage();
                }
                break;
            default:
                showErrorMessage();
                break;
        }
    }

    private void navigateToActivity(String role) {
        Intent intent;
        switch (role) {
            case "Administrator":
                intent = new Intent(this, AdministratorActivity.class);
                break;
            case "StoreKeeper":
                intent = new Intent(this, StoreKeeperActivity.class);
                break;
            case "Assembler":
                intent = new Intent(this, AssemblerActivity.class);
                break;
            case "Requester":
                intent = new Intent(this, RequesterActivity.class);
                break;
            default:
                showErrorMessage();
                return;
        }
        startActivity(intent);
        finish();
    }

    private void navigateToActivity(FirebaseUser user) {
        String email = user.getEmail();
        if (email == null) {
            showErrorMessage();
            return;
        }

        switch (email) {
            case "admin@example.com":
                navigateToActivity("Administrator");
                break;
            case "storekeeper@example.com":
                navigateToActivity("StoreKeeper");
                break;
            case "assembler@example.com":
                navigateToActivity("Assembler");
                break;
            case "requester@example.com":
                navigateToActivity("Requester");
                break;
            default:
                showErrorMessage();
                break;
        }
    }

    private void showErrorMessage() {
        Toast.makeText(this, getString(R.string.error_invalid_credentials), Toast.LENGTH_SHORT).show();
    }

    private void logLoginAttempt(String email) {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "email_password");
        bundle.putString("email_attempted", email);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
    }
}

/*
pour utiliser ce modéle on doit concevoir une Activité pour chaque type d'utilisateur,
je veux plutot faire la meme mais avec un réajustement de l'interface utilisateur
selon le role, voici une solution:

package com.example.pcorderapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.pcorderapplication.R;

import com.example.pcorderapplication.view.RequesterActivity;
import com.example.pcorderapplication.view.StoreKeeperActivity;
import com.example.pcorderapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TODO : To delete later
        binding.emailInputUnique.setText("storekeeper@example.com");
        binding.passwordInputUnique.setText("store123");

        binding.loginButton.setOnClickListener(v -> {
            String email = binding.emailInputUnique.getText().toString().trim();
            String password = binding.emailInputUnique.getText().toString().trim();

            authenticateUser(email, password);
        });
        TextView registerTextView = binding.registerTextView;
        registerTextView.setOnClickListener(v -> {
            // Start RegisterActivity
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void authenticateUser(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        switch (email) {
            case "admin@example.com":
                if (password.equals("admin123")) {
                    handleUserRole("Administrator");
                } else {
                    showErrorMessage();
                }
                break;
            case "storekeeper@example.com":
                if (password.equals("store123")) {
                    handleUserRole("StoreKeeper");
                } else {
                    showErrorMessage();
                }
                break;
            case "assembler@example.com":
                if (password.equals("assembler123")) {
                    handleUserRole("Assembler");
                } else {
                    showErrorMessage();
                }
                break;
            case "requester@example.com":
                if (password.equals("request123")) {
                    handleUserRole("Requester");
                } else {
                    showErrorMessage();
                }
                break;
            default:
                showErrorMessage();
                break;
        }
    }

    private void handleUserRole(String role) {
        Intent intent;
        switch (role) {
            case "Administrator":
                intent = new Intent(this, AdministratorActivity.class);
                break;
            case "StoreKeeper":
                intent = new Intent(this, StoreKeeperActivity.class);
                break;
            case "Assembler":
                intent = new Intent(this, AssemblerActivity.class);
                break;
            case "Requester":
                intent = new Intent(this, RequesterActivity.class);
                break;
            default:
                return;
        }
        startActivity(intent);
    }

    private void showErrorMessage() {
        Toast.makeText(this, getString(R.string.error_invalid_credentials), Toast.LENGTH_SHORT).show();
    }
}
*/

