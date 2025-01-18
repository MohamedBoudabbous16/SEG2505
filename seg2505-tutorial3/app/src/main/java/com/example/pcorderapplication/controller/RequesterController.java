package com.example.pcorderapplication.controller;

import android.content.Context;
import android.util.Log;

import com.example.pcorderapplication.model.database.AccessLocal;
import com.example.pcorderapplication.model.database.UserRepository;
import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.model.orders.Order;
import com.example.pcorderapplication.model.users.Requester;

import java.util.ArrayList;
import java.util.List;


public class RequesterController {
    private static RequesterController instance = null;
    private static Requester requester;
    private static AccessLocal accessLocal ;

    private RequesterController(Context context, String firstName, String lastName, String email, String password) {
        requester = new Requester(firstName, lastName, email, password);
        accessLocal = new AccessLocal(context);
    }

    public static RequesterController getInstance(Context context, String firstName, String lastName, String email, String password) {
        if (instance == null) {
            instance = new RequesterController(context, firstName, lastName, email, password);
        }
        return instance;
    }
    public static RequesterController getInstance(Context context) {
        if (instance == null) {
            instance = new RequesterController(context);
        }
        return instance;
    }

    private RequesterController(Context context) {
        accessLocal = new AccessLocal(context);
    }


    /*
    public void createOrder(List<Component> components) {
        //meme code mais sans la supression du component
        //si il y'a le componene t on passe à create order et on l'ajoute à la liste de componenet
        //TODO: Le controller demande le request de User à partir RquestComponenet, il va ppeler Request componenet de l'accés local
//meme paramétre createorder: L'assembleur collecte les oredre, controller de requester crée les ordres. (get title et get quantity for each componenet dans parametre
        // cherche component et quntuty on appelle request component, si true on le reserve et ressort de la base, on fait une liste pour les componenet que l'utilisateur a demandé
//on l'envoie ensuite à l'assembleur.   requestComponenet, un seul componenet à la fois, il récupére le boolean et on la joute à l'orderlist. create order à l'intérieur d'un appel request componenet
        if (requester != null){
        requester.createOrder(components);
            Log.i("RequesterController", "Order created for requester.");
        } else {
            Log.e("RequesterController", "Order creation failed: Requester not initialized.");
        }
    }
    */

    public boolean validateComponents(Component c) {
            Component component = accessLocal.findComponentByTitle(c.getTitle());
            if(component!=null)
            {
                int i = -1;
                if( (i = component.getQuantity() - c.getQuantity()) >=0)
                    {
                        component.setQuantity(i);
                        accessLocal.updateComponent(component);
                    }
                else
                    return false;
            }
        return true;
    }

    public boolean createOrder(List<Component> components) {
            List<Component> requestedComponents = new ArrayList<>();
            for (Component component : components) {
                String title = component.getTitle();
                boolean isAvailable = validateComponents(component);
                if (isAvailable) {
                    requestedComponents.add(component);

                } else {
                    Log.e("RequesterController", "Component " + title + " is not available in the requested quantity.");
                }
            }
            if (!requestedComponents.isEmpty()) {
                //requester.createOrder(requestedComponents);
                Log.i("RequesterController", "Order created for requester.");
                return true;
                // Send to assembler if required
            } else {
                Log.e("RequesterController", "Order creation failed: No components were available.");
                return false;
            }
    }

    public void deleteOrder(int orderId) {
        if (requester != null) {
            requester.deleteOrder(orderId);
            Log.i("RequesterController", "Order " + orderId + " deletion requested.");
        } else {
            Log.e("RequesterController", "Order deletion failed: Requester not initialized.");
        }
    }

    public List<Order> viewOwnOrders() {
        if (requester != null) {
            return requester.viewOwnOrders();
        } else {
            Log.e("RequesterController", "View orders failed: Requester not initialized.");
            return null;
        }
    }

    public void loginRequester() {
        if (requester != null) {
            requester.login();
        } else {
            Log.e("RequesterController", "Login failed: Requester not initialized.");
        }
    }

    public void logoutRequester() {
        if (requester != null) {
            requester.logout();
        } else {
            Log.e("RequesterController", "Logout failed: Requester not initialized.");
        }
    }

    public Requester getRequester() {
        return requester;
    }





    public List<Component> requestComponent(String title) {
        // Call the method in AccessLocal
        List<Component> components = accessLocal.handleComponentRequest(title);
        return components;
    }
}


