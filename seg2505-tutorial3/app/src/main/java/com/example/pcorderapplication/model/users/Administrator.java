package com.example.pcorderapplication.model.users;

import android.util.Log;

//
public class Administrator extends User {

    Requester requester;
    // Constructeur
    public Administrator(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    // Créer un nouvel objet de type Requester
    public void createRequester(String firstName, String lastName, String email, String password) {

        if (firstName != null && lastName != null && email != null && password != null ) {
           // requester = new Requester( firstName,  lastName,  email,  password);
            //TODO Add requester to database
           // Log.i("Administrator", "Requester " + requester.getFirstName() + " " + requester.getLastName() + " has been created.");
        } else {
            Log.i("Administrator", "Failed to create requester: missing information.");
        }
    }

    // Modifier les informations d'un Requester
    public void modifyRequester(String newFirstName, String newLastName, String newEmail) {
        /*if (requester != null) {
            requester.setFirstName(newFirstName);
            requester.setLastName(newLastName);
            requester.setEmail(newEmail);
            Log.i("Administrator", "Requester " + requester.getFirstName() + " information updated.");
        } else {
            Log.i("Administrator", "Failed to modify requester: requester not found.");
        }*/
    }

    // Supprimer un Requester
    public void deleteRequester(String firstName, String lastName, String email) {
        if (true) {
            //TODO complete the delete code from database
            Log.i("Administrator", "Requester " + firstName + " " + lastName + " has been deleted.");
        } else {
            Log.i("Administrator", "Failed to delete requester: requester not found.");
        }
    }

    @Override
    public void login() {
        // Appel de la méthode login() de la classe parente abstraite User
        if (super.login(this.getEmail(), this.getPassword())) {
            Log.i("Administrator", "Administrator " + this.getFirstName() + " logged in successfully.");
        } else {
            Log.i("Administrator", "Failed to log in administrator.");
        }
    }


    @Override
    public void logout() {
        // meme chose mais pour la methode logout()
        super.logout();
        Log.i("Administrator", "Administrator " + this.getFirstName() + " logged out successfully.");
    }
}

