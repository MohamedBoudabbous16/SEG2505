package com.example.pcorderapplication.model.users;

import android.content.Context;
import android.util.Log;

import com.example.pcorderapplication.model.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

//
public class Administrator extends User {

    Requester requester;
    private UserRepository userRepository;
    private List<Requester> requesterList = new ArrayList<>();
    // Constructeur
    public Administrator(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
    }

    public void initializeRepository(Context context) {
        this.userRepository = new UserRepository(context);
    }
    private void ensureRepositoryInitialized() {
        if (userRepository == null) {
            throw new IllegalStateException("UserRepository is not initialized. Call initializeRepository(context) first.");
        }
    }
    public void createRequester(String firstName, String lastName, String email, String password) {
        try {
            ensureRepositoryInitialized();

            if (firstName == null || lastName == null || email == null || password == null) {
                Log.i("Administrator", "Failed to create requester: missing information.");
                return;
            }

            Requester existingRequester = userRepository.findRequesterByEmail(email);
            if (existingRequester != null) {
                Log.i("Administrator", "Requester with email " + email + " already exists.");
                return;
            }

            Requester requester = new Requester(firstName, lastName, email, password);
            userRepository.addRequester(requester);
            Log.i("Administrator", "Requester " + requester.getFirstName() + " " + requester.getLastName() + " has been created and added to the database.");
        } catch (Exception e) {
            Log.e("Administrator", "Failed to create requester due to an error: " + e.getMessage());
        }
    }

    /*public void createRequester(String firstName, String lastName, String email, String password) {
        ensureRepositoryInitialized();

        if (firstName == null || lastName == null || email == null || password == null) {
            Log.i("Administrator", "Failed to create requester: missing information.");
            return;
        }

        Requester existingRequester = userRepository.findRequesterByEmail(email);
        if (existingRequester != null) {
            Log.i("Administrator", "Requester with email " + email + " already exists.");
            return;
        }

        Requester requester = new Requester(firstName, lastName, email, password);
        userRepository.addRequester(requester);
        Log.i("Administrator", "Requester " + requester.getFirstName() + " " + requester.getLastName() + " has been created and added to the database.");
    }*/


    /*public void createRequester(String firstName, String lastName, String email, String password) {
        ensureRepositoryInitialized();
        Requester existingRequester = userRepository.findRequesterByEmail(email);
        if (firstName != null && lastName != null && email != null && password != null) {
            Requester requester = new Requester(firstName, lastName, email, password);

            if (userRepository != null) {
                // Ajouter le requester à la base de données
                userRepository.addRequester(requester);
                Log.i("Administrator", "Requester " + requester.getFirstName() + " " + requester.getLastName() + " has been created and added to the database.");
            } else {
                Log.e("Administrator", "UserRepository is not initialized. Cannot add requester to the database.");
            }
        } else {
            Log.i("Administrator", "Failed to create requester: missing information.");
        }
    }
*/
    // Créer un nouvel objet de type Requester
    /*public void createRequester(String firstName, String lastName, String email, String password) {

        if (firstName != null && lastName != null && email != null && password != null ) {
            requester = new Requester( firstName,  lastName,  email,  password);
            //TODO Add requester to database
            Log.i("Administrator", "Requester " + requester.getFirstName() + " " + requester.getLastName() + " has been created.");

        } else {
            Log.i("Administrator", "Failed to create requester: missing information.");
        }
    }

     */

    // Modifie les informations d'un objet de type Requester
    public void modifyRequester(String newFirstName, String newLastName, String newEmail) {
       if (requester != null) {
            requester.setFirstName(newFirstName);
            requester.setLastName(newLastName);
            requester.setEmail(newEmail);
            Log.i("Administrator", "Requester " + requester.getFirstName() + " information updated.");
        } else {
            Log.i("Administrator", "Failed to modify requester: requester not found.");
        }
    }
    public void deleteRequester(String firstName, String lastName, String email) {
        ensureRepositoryInitialized();

        if (email == null || email.isEmpty()) {
            Log.i("Administrator", "Failed to delete requester: email is missing.");
            return;
        }

        Requester requester = userRepository.findRequesterByEmail(email);
        if (requester != null) {
            userRepository.deleteRequester(email);
            Log.i("Administrator", "Requester " + firstName + " " + lastName + " has been deleted from the database.");
        } else {
            Log.i("Administrator", "Failed to delete requester: requester with email " + email + " not found.");
        }
    }


    /*public void deleteRequester(String firstName, String lastName, String email) {
        ensureRepositoryInitialized();
        Requester requester = userRepository.findRequesterByEmail(email);

        if (requester != null) {
            userRepository.deleteRequester(email);
            Log.i("Administrator", "Requester " + firstName + " " + lastName + " has been deleted from the database.");
        } else {
            Log.i("Administrator", "Failed to delete requester: requester not found.");
        }
    }

     */


    // Supprimer un objet de type Requester
    /*public void deleteRequester(String firstName, String lastName, String email) {
        if (true) {
            //TODO complete the delete code from database
            Log.i("Administrator", "Requester " + firstName + " " + lastName + " has been deleted.");
        } else {
            Log.i("Administrator", "Failed to delete requester: requester not found.");
        }
    }
    */


    @Override
    public void login() {
        // Appel de la méthode login() de User (une classe abstraite qui est le parent de Administrator
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
    public List<Requester> getAllRequesters() {
        ensureRepositoryInitialized();

        List<Requester> requesters = userRepository.getAllRequesters();
        if (requesters.isEmpty()) {
            Log.i("Administrator", "No requesters found in the database.");
        } else {
            Log.i("Administrator", requesters.size() + " requesters retrieved from the database.");
        }

        return requesters;
    }

    /*public List<Requester> getAllRequesters() {
        ensureRepositoryInitialized();
        return userRepository.getAllRequesters();
    }
     */

}


