package com.example.pcorderapplication.model.users;

import android.util.Log;
import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.model.orders.Order;

import java.util.ArrayList;
import java.util.List;

public class Requester extends User {

    private List<Order> orders;

    public Requester(String firstName, String lastName, String email, String password) {
        super(firstName, lastName, email, password);
        this.orders = new ArrayList<>();
    }

    public void createOrder(List<Component> components) {
        if (validateComponents(components)) {
            Order newOrder = new Order(orders.size() + 1, this);
            for (Component component : components) {
                newOrder.addComponent(component);
            }
            orders.add(newOrder);
            Log.i("Requester", "Order " + newOrder.getId() + " has been created.");
        } else {
            Log.i("Requester", "Failed to create order: Required components are missing.");
        }
    }

    public boolean validateComponents(List<Component> components) {
        boolean hasCPU = components.stream().anyMatch(c -> c.getType().equals("CPU"));
        boolean hasRAM = components.stream().anyMatch(c -> c.getType().equals("RAM"));
        boolean hasStorage = components.stream().anyMatch(c -> c.getType().equals("Storage"));
        return hasCPU && hasRAM && hasStorage;
    }

    public List<Order> viewOwnOrders() {
        Log.i("Requester", "Displaying all orders:");
        for (Order order : orders) {
            Log.i("Requester", "Order " + order.getId() + " - Status : " + order.getStatus().getStatus());
        }
        return orders;
    }

    public void deleteOrder(int orderId) {
        Order orderToDelete = orders.stream()
                .filter(order -> order.getId() == orderId)
                .findFirst()
                .orElse(null);
        if (orderToDelete != null && orderToDelete.getStatus().getStatus().equals("Pending")) {
            orders.remove(orderToDelete);
            Log.i("Requester", "Order " + orderId + " deleted.");
        } else if (orderToDelete == null) {
            Log.i("Requester", "The order " + orderId + " doesn't exist in your orders.");
        } else {
            Log.i("Requester", "The order " + orderId + " cannot be deleted as it is not in 'Pending' status.");
        }
    }

    @Override
    public void login() {
        if (super.login(this.getEmail(), this.getPassword())) {
            Log.i("Requester", "Requester " + this.getFirstName() + " " + this.getLastName() + " logged in successfully.");
        } else {
            Log.i("Requester", "Login failed for requester: invalid email or password.");
        }
    }

    @Override
    public void logout() {
        if (this.isLoggedIn()) {
            super.logout();
            Log.i("Requester", "Requester " + this.getFirstName() + " " + this.getLastName() + " logged out successfully.");
        } else {
            Log.i("Requester", "No requester is currently logged in.");
        }
    }
}
