package com.example.pcorderapplication.model.users;

import android.util.Log;

import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.model.orders.Order;
import java.util.ArrayList;
import java.util.List;
import com.example.pcorderapplication.model.users.StoreKeeper;

public class Assembler extends User {


    private List<Order> orders;//la liste qui contient les différentes commandes qui seront geres par l'assembleur

    private StoreKeeper storeKeeper;
    private List<Order> pendingOrders;
    //constructeur
    public Assembler(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        this.orders = new ArrayList<>();
        this.pendingOrders = new ArrayList<>();
    }
    @Override
    public void login() {
        if (super.login(this.getEmail(), this.getPassword())) {
            Log.i("Assembler", "Assembler " + this.getFirstName() + " logged in successfully.");
        } else {
            Log.i("Assembler", "Failed to log in the assembler.");
        }
    }

    @Override
    public void logout() {
        if (this.isLoggedIn()) {
            super.logout();
            Log.i("Assembler", "Assembler " + this.getFirstName() + " " + this.getLastName() + " logged out successfully.");
        } else {
            Log.i("Assembler", "No Assembler is currently logged in.");
        }
    }


    public List<Order> viewAllOrders() {
        return orders;
    }


    public boolean ValidateOrder() {
        boolean allOrdersValid = true;

        for (Order order : orders) {

            boolean orderIsValid = true;

            if (orderIsValid) {
                acceptOrder(order);
            } else {
                rejectOrder(order, "Validation échouée pour la commande.");
                allOrdersValid = false;
            }
        }
        return allOrdersValid;
    }


    public void addOrder(Order order) {
        orders.add(order);
        Log.i("Assembler", "Order " + order.getId() + " added to the list of orders.");
    }
    public void acceptOrder(Order order) {
        order.updateStatus("Accepted");
        Log.i("Assembler", "Order " + order.getId() + " accepted.");
    }
    public void rejectOrder(Order order, String message) {
        order.updateStatus("Rejected");
        order.setMessage(message);
        Log.i("Assembler", "Order " + order.getId() + " rejected because: " + message);
    }


    /*
        cette methode est elle utile?:public List<Order> viewAllOrders() {
        Log.i("Assembler", "Visualisation of all the orders:");
        for (Order order : orders) {
            Log.i("Assembler", "Order " + order.getId() + " - Status : " + order.getStatus());
        }
        return orders;
    }

    public void acceptOrder(Order order) {
        order.updateStatus("Accepted");
        Log.i("Assembler", "Order " + order.getId() + " accepted.");
    }

    public void rejectOrder(Order order, String message) {
        order.updateStatus("Rejected");
        order.setMessage(message);
        Log.i("Assembler", "Order " + order.getId() + " rejected because: " + message);
    }

    public void completeOrder(Order order) {
        order.updateStatus("Completed");
        Log.i("Assembler", "Order " + order.getId() + " completed.");
    }

    public List<Order> viewAllOrders() {
        Log.i("Assembler", "Visualisation of all the orders:");
        for (Order order : orders) {
            Log.i("Assembler", "Order " + order.getId() + " - Status : " + order.getStatus());
        }
        return orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
        Log.i("Assembler", "Order " + order.getId() + " added to the list of orders.");
    }


     */
}
