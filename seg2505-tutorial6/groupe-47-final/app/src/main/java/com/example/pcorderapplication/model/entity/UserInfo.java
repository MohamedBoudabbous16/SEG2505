package com.example.pcorderapplication.model.entity;
import java.time.LocalDateTime;


public class UserInfo {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;


    //constructeur
    public UserInfo(int id, String firstName, String lastName, String email, String password, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    //getters
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }



    //setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateModifiedAt();
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateModifiedAt();
    }

    public void setRole(String role) {
        this.role = role;
        updateModifiedAt();
    }

    public void setEmail(String email) {
        this.email = email;
        updateModifiedAt();
    }

    public void setPassword(String password) {
        this.password = password;
        updateModifiedAt();
    }



    private void updateModifiedAt() {
        this.modifiedAt = LocalDateTime.now();
    }
}
