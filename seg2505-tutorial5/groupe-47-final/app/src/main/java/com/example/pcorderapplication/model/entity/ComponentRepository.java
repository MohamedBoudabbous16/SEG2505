package com.example.pcorderapplication.model.entity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ComponentRepository {

    private final List<Component> componentDatabase;

    // Constructeur
    public ComponentRepository() {
        componentDatabase = new ArrayList<>();
    }




 /*   public void updateComponent(Component component) {
        for (int i = 0; i < componentDatabase.size(); i++) {
            if (componentDatabase.get(i).getId() == component.getId()) {
                componentDatabase.set(i, component); // Remplace le composant existant par le nouveau
                Log.i("ComponentRepository", "Component updated successfully.");
                return;
            }
        }
        Log.i("ComponentRepository", "Component not found.");
    }*/



    // Méthodes pour;
    // supprimer un composant
    public void deleteComponent(Component component) {
        if (componentDatabase.remove(component)) {
            Log.i("ComponentRepository", "Component deleted successfully.");
        } else {
            Log.i("ComponentRepository", "Component not found.");
        }
    }
    //elle efface tous les composants, cette methode a ete
    //concue pour la realisation des tests instrumentalisée
    // Method to delete all components
    public void deleteAllComponents() {
        int sizeBeforeClear = componentDatabase.size();
        componentDatabase.clear();
        Log.i("ComponentRepository", sizeBeforeClear + " components deleted successfully.");
    }

    // obtenir tous les composants
    public List<Component> getAllComponents() {
        return componentDatabase;
    }
}
