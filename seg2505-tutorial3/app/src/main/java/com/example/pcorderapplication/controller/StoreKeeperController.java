package com.example.pcorderapplication.controller;

import android.content.Context;
import android.util.Log;

import com.example.pcorderapplication.model.database.AccessLocal;
import com.example.pcorderapplication.model.entity.Component;

import java.util.ArrayList;
import java.util.List;

public class StoreKeeperController {
    private static StoreKeeperController instance = null;

    // Database attribute
    private static AccessLocal accessLocal = null;
    private final List<Component> componentDatabase = new ArrayList<>();

    // Private constructor for Singleton
    private StoreKeeperController() {
        super();
    }

    // Method to get the unique instance of the controller
    public static StoreKeeperController getInstance(Context context) {
        if (instance == null) {
            instance = new StoreKeeperController();
            accessLocal = new AccessLocal(context);
        }
        return instance;
    }

    public Component findComponentByTitle(String title) {
        return accessLocal.findComponentByTitle(title);
    }

    public boolean addComponent(String title, String type, String subtype, int quantity, String comment, int requestCount, String modificationDate) {

        if (findComponentByTitle(title) == null) {
            // Create the Component using an existing constructor (without requestCount and creationDate)
            Component component = new Component(title, type, subtype, quantity, comment, null, modificationDate);

            // Set requestCount separately
            component.setRequestCount(requestCount);

            if (accessLocal.addComponent(component) >= 0) {
                return true;
            }
        } else {
            Log.i("database", "This title exists in the Component table");
        }
        return false;
    }

    public ArrayList<String> upload() {
        return accessLocal.upload();
    }

    public boolean updateComponent(String title, String type, String subtype, int quantity, String comment, int requestCount, String modificationDate) {
        // Create the Component using an existing constructor (without requestCount and creationDate)
        Component componentToUpdate = new Component(title, type, subtype, quantity, comment, null, modificationDate);

        // Set requestCount separately
        componentToUpdate.setRequestCount(requestCount);

        return accessLocal.updateComponent(componentToUpdate) >= 0;
    }

    public boolean deleteComponent(String title) {
        return accessLocal.deleteComponent(title) >= 0;
    }
}
