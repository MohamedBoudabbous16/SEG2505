package com.example.pcorderapplication.controller;

import android.content.Context;

import com.example.pcorderapplication.model.database.UserRepository;
import com.example.pcorderapplication.model.entity.UserInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginController {

    private final UserRepository userRepository;
    private final FirebaseAuth mAuth;
    private UserInfo currentUser;

    public LoginController(Context context) {
        this.userRepository = new UserRepository(context);
        this.mAuth = FirebaseAuth.getInstance();
    }

    public LoginController(Context context, UserRepository userRepository, FirebaseAuth mAuth) {
        this.userRepository = userRepository;
        this.mAuth = mAuth;
    }



    public void login(String email, String password, OnLoginResultListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            currentUser = userRepository.findUserByEmail(firebaseUser.getEmail());
                            listener.onLoginSuccess(currentUser);
                        }
                    } else {
                        listener.onLoginFailure("Login failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    public void registerUser(String firstName, String lastName, String email, String password, OnRegisterResultListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            UserInfo newUser = new UserInfo(0, firstName, lastName, email, password, "Requester");
                            userRepository.insertUser(newUser);
                            listener.onRegisterSuccess(newUser);
                        }
                    } else {
                        listener.onRegisterFailure("Registration failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"));
                    }
                });
    }

    public void logout() {
        if (currentUser != null) {
            mAuth.signOut();
            currentUser = null;
        }
    }

    public boolean isUserLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public UserInfo getCurrentUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            currentUser = userRepository.findUserByEmail(firebaseUser.getEmail());
        }
        return currentUser;
    }

    public interface OnLoginResultListener {
        void onLoginSuccess(UserInfo user);
        void onLoginFailure(String errorMessage);
    }

    public interface OnRegisterResultListener {
        void onRegisterSuccess(UserInfo user);
        void onRegisterFailure(String errorMessage);
    }
}
