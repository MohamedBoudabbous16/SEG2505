package com.example.pcorderapplication.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.pcorderapplication.model.entity.UserInfo;
import com.example.pcorderapplication.view.AdministratorActivity;
import com.example.pcorderapplication.view.AssemblerActivity;
import com.example.pcorderapplication.view.RequesterActivity;
import com.example.pcorderapplication.view.StoreKeeperActivity;

public class MainController {

    private static MainController instance;
    private final Context appContext;
    private final LoginController loginController;


    private MainController(Context context) {
        this.appContext = context.getApplicationContext();
        this.loginController = new LoginController(appContext);
    }
    //pour les tests:
    public MainController(Context context, LoginController loginController) {
        this.appContext = context.getApplicationContext();
        this.loginController = loginController;
    }

    public static MainController getInstance(Context context) {
        if (instance == null) {
            instance = new MainController(context);
        }
        return instance;
    }



    /**
     *  connecte un utilisateur et pars vers l'activité appropriée en fonction
     *  de son type.
     *
     * @param email    L'email de l'utilisateur.
     * @param password Le mot de passe de l'utilisateur.
     */
    public void loginUser(String email, String password) {
        loginController.login(email, password, new LoginController.OnLoginResultListener() {
            @Override
            public void onLoginSuccess(UserInfo user) {
                Toast.makeText(appContext, "Login successful for user: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                Log.i("LoginActivity", "Login successful for user: " + user.getEmail());

                navigateToRoleActivity();
            }

            @Override
            public void onLoginFailure(String errorMessage) {
                Toast.makeText(appContext, "Login failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Login failed: " + errorMessage);

            }
        });
    }

    /**
     * Déconnecte l'utilisateur actuel. Retourne à l'écran de connexion.
     */
    public void logoutUser() {
        loginController.logout();
        Toast.makeText(appContext, "User logged out successfully.", Toast.LENGTH_SHORT).show();
        Log.i("LoginActivity", "User logged out successfully.");

    }


    private void navigateToRoleActivity() {
        if (!loginController.isUserLoggedIn()) {
            Log.e("MainController", "No user is logged in. Unable to navigate.");
            return;
        }

        UserInfo currentUser = loginController.getCurrentUser();
        if (currentUser == null) {
            Log.e("MainController", "Current user is null. Unable to navigate.");
            return;
        }

        String userRole = currentUser.getRole();
        Intent intent;

        switch (userRole) {
            case "Administrator":
                intent = new Intent(appContext, AdministratorActivity.class);
                break;
            case "StoreKeeper":
                intent = new Intent(appContext, StoreKeeperActivity.class);
                break;
            case "Assembler":
                intent = new Intent(appContext, AssemblerActivity.class);
                break;
            case "Requester":
                intent = new Intent(appContext, RequesterActivity.class);
                break;
            default:
                Log.e("MainController", "Unrecognized user role: " + userRole);
                return;
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
        Log.i("MainController", "Navigated to " + userRole + " activity.");
    }

    /**
     * Vérifie si un utilisateur est connecté.
     * retourn true si un utilisateur est connecté, sinon false.
     */
    public boolean isUserLoggedIn() {
        return loginController.isUserLoggedIn();
    }
}
