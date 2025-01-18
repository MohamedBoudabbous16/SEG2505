package com.example.pcorderapplication.model.users;

import android.util.Log;

import com.example.pcorderapplication.model.orders.Order;
import java.util.ArrayList;
import java.util.List;

public class Assembler /*extends User*/ {

    /*
    private List<Order> orders;//la liste conteant les différentes commandes est gérée par l'assembleur

    //constructeur
    public Assembler(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        this.orders = new ArrayList<>();
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
        super.logout();
        Log.i("Assembler", "The assembler " + this.getFirstName() + " logged out successfully.");
    }

     */
}
