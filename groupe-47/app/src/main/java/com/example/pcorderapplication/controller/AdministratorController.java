package com.example.pcorderapplication.controller;

import android.content.Context;

import com.example.pcorderapplication.model.users.Administrator;
import com.example.pcorderapplication.model.users.Requester;

import java.util.List;

public class AdministratorController {
    private static AdministratorController instance;
    private final Administrator administrator;

    public AdministratorController(Context context, String firstName, String lastName, String email, String password) {
        this.administrator = new Administrator(firstName, lastName, email, password);
        this.administrator.initializeRepository(context);
    }

    public static AdministratorController getInstance(Context context, String firstName, String lastName, String email, String password) {
        if (instance == null) {
            instance = new AdministratorController(context, firstName, lastName, email, password);
        } else {
            instance.administrator.initializeRepository(context);
        }
        return instance;
    }

    public void createRequester(String firstName, String lastName, String email, String password) {
        administrator.createRequester(firstName, lastName, email, password);
    }

    public void modifyRequester(String newFirstName, String newLastName, String email) {
        administrator.modifyRequester(newFirstName, newLastName, email);
    }

    public void deleteRequester(String firstName, String lastName, String email) {
        administrator.deleteRequester(firstName, lastName, email);
    }

    public List<Requester> getAllRequesters() {
        return administrator.getAllRequesters();
    }
}