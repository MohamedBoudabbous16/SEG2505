package com.example.pcorderapplication.model.users;
import java.time.LocalDateTime;
import com.example.pcorderapplication.model.interfaces.Authenticable;
public  abstract class User implements Authenticable {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private boolean isLoggedIn;
    private int id;

    //constructeur
    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.creationDateTime = LocalDateTime.now();
        this.modificationDateTime = LocalDateTime.now();
        this.isLoggedIn = false;

    }

    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            isLoggedIn = true;
            System.out.println(firstName + " " + lastName + " logged in successfully.");
            return true;
        } else {
            System.out.println("Invalid email or password.");
            return false;
        }
    }

    public void logout() {
        if (isLoggedIn) {
            isLoggedIn = false;
            System.out.println(firstName + " " + lastName + " logged out.");
        } else {
            System.out.println("No user is currently logged in.");
        }
    }

    //l'ensemble des getters:
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {return password;}
    public int getId() {return id;}




    //l'ensemble des setters:
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setId(int id) {this.id= id;}

    public void setPassword(String password) {
        this.password = password;
    }
    //2 méthodes qui retournent la date et l'heure de:
    //la création de l'utilisateur
    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }
    //modification de l'utilisateur
    public LocalDateTime getModificationDateTime() {
        return modificationDateTime;
    }

    private void updateModificationDateTime() {
        this.modificationDateTime = LocalDateTime.now();
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
}
