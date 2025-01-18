package com.example.pcorderapplication.controller;

import android.content.Context;

import com.example.pcorderapplication.model.users.Administrator;
import com.example.pcorderapplication.model.users.Requester;

public class AdministratorController {
    private static AdministratorController instance = null;
    private static Administrator administrator;

    // Constructeur privé pour le Singleton
    private AdministratorController() {
        super();
    }

    // Méthode pour obtenir l'instance unique du contrôleur
    public static AdministratorController getInstance(Context context, String firstName, String lastName, String email, String password) {
        if (instance == null) {
            instance = new AdministratorController();
            administrator = new Administrator(firstName, lastName, email, password);
        }
        return instance;
    }

    // Méthode pour créer un Requester
    public void createRequester(String firstName, String lastName, String email, String password) {
        administrator.createRequester(firstName, lastName, email, password);
    }

    // Méthode pour modifier les informations d'un Requester
    public void modifyRequester(String newFirstName, String newLastName, String newEmail) {
        administrator.modifyRequester(newFirstName, newLastName, newEmail);
    }

    // Méthode pour supprimer un Requester
    public void deleteRequester(String firstName, String lastName, String email) {
        administrator.deleteRequester(firstName, lastName, email);
    }

    // Méthode pour gérer la connexion de l'Administrateur
    public void loginAdministrator() {
        administrator.login();
    }

    // Méthode pour gérer la déconnexion de l'Administrateur
    public void logoutAdministrator() {
        administrator.logout();
    }

    // Méthode pour récupérer l'objet Administrator
    public Administrator getAdministrator() {
        return administrator;
    }
}
