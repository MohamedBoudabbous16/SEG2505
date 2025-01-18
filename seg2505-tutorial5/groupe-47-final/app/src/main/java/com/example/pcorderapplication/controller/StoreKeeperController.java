package com.example.pcorderapplication.controller;

import android.content.Context;
import android.util.Log;

import com.example.pcorderapplication.model.database.AccessLocal;
import com.example.pcorderapplication.model.entity.Component;

import java.util.ArrayList;
import java.util.List;

public class StoreKeeperController {
    private static StoreKeeperController instance = null;
    private static AccessLocal accessLocal = null;
    private final List<Component> componentDatabase = new ArrayList<>();

    private StoreKeeperController() {
        super();
    }

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
            Component component = new Component(title, type, subtype, quantity, comment, null, modificationDate);
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
        Component componentToUpdate = new Component(title, type, subtype, quantity, comment, null, modificationDate);
        componentToUpdate.setRequestCount(requestCount);
        return accessLocal.updateComponent(componentToUpdate) >= 0;
    }

    public boolean deleteComponent(String title) {
        return accessLocal.deleteComponent(title) >= 0;
    }

    //methode ajoute pour les tests unitaires elle n'intervient pas dans la fonc
    //fontionnalite de l'application
    public void deleteAllComponents() {
        List<Component> allComponents = accessLocal.getAllComponents(); // Assuming AccessLocal has a method to retrieve all components
        int sizeBefore = allComponents.size();

        for (Component component : allComponents) {
            accessLocal.deleteComponent(component.getTitle());
        }

        Log.i("StoreKeeperController", sizeBefore + " components deleted successfully.");
    }

}
