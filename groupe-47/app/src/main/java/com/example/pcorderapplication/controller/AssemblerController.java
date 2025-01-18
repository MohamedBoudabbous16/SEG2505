package com.example.pcorderapplication.controller;

import android.content.Context;
import android.util.Log;

import com.example.pcorderapplication.model.database.AccessLocal;
import com.example.pcorderapplication.model.database.OrderRepository;
import com.example.pcorderapplication.model.database.UserRepository;
import com.example.pcorderapplication.model.orders.Order;
import com.example.pcorderapplication.model.users.Assembler;
import com.example.pcorderapplication.model.users.Requester;

import java.util.List;

public class AssemblerController {
    private static AssemblerController instance = null;
    private static AccessLocal accessLocal = null;

    private static OrderRepository orderRepository = null;
    private static UserRepository userRepository = null;


    private Assembler assembler;


    private AssemblerController(Context context) {
        assembler = new Assembler("AssemblerFirstName", "AssemblerLastName", "assembler@example.com", "password");
        orderRepository = new OrderRepository(context);
        userRepository = new UserRepository(context);
    }
    //ce constructeur est ajouté pour permettre l'injection dans les
    //tests
    public AssemblerController(OrderRepository orderRepository, UserRepository userRepository, AccessLocal accessLocal) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.accessLocal = accessLocal;
        assembler = new Assembler("AssemblerFirstName", "AssemblerLastName", "assembler@example.com", "password");
    }


    public static AssemblerController getTestInstance(OrderRepository orderRepository, UserRepository userRepository, AccessLocal accessLocal) {
        if (instance == null) {
            instance = new AssemblerController(orderRepository, userRepository, accessLocal);
        }
        return instance;
    }

    public static AssemblerController getInstance(Context context) {
        if (instance == null) {
            instance = new AssemblerController(context);
        }
        return instance;
    }


    public void login() {
        assembler.login();
    }


    public void logout() {
        assembler.logout();
    }


    public boolean addOrder(Order order) {

        Order existingOrder = orderRepository.findOrderById(order.getId(), userRepository);
        if (existingOrder == null) {
            orderRepository.insertOrder(order);
            Log.i("AssemblerController", "Order added successfully.");
            return true;
        } else {
            Log.i("AssemblerController", "An order with this ID already exists.");
            return false;
        }
    }


    public List<Order> viewAllOrders() {
        return orderRepository.getAllOrders(userRepository);
    }


    public boolean validateOrders() {
        List<Order> orders = viewAllOrders();
        boolean allOrdersValid = true;

        for (Order order : orders) {
            boolean orderIsValid = validateOrderLogic(order);

            if (orderIsValid) {
                acceptOrder(order);
            } else {
                rejectOrder(order, "Validation failed for the order.");
                allOrdersValid = false;
            }
        }
        return allOrdersValid;
    }

    public void acceptOrder(Order order) {
        order.updateStatus("Accepted");
        orderRepository.updateOrder(order);
        Log.i("AssemblerController", "Order " + order.getId() + " accepted.");
    }


    public boolean rejectOrder(Order order, String message) {
        if (order == null) {
            return false;
        }
        order.updateStatus("Rejected");
        order.setMessage(message);
        orderRepository.updateOrder(order);
        Log.i("AssemblerController", "Order " + order.getId() + " rejected because: " + message);
        return true;
    }


    public void completeOrder(Order order) {
        order.updateStatus("Completed");
        orderRepository.updateOrder(order);
        Log.i("AssemblerController", "Order " + order.getId() + " completed.");
    }


    private boolean validateOrderLogic(Order order) {
        return true;
    }
    public Order findOrderById(int id) {
        if (accessLocal != null) {
            Order order = orderRepository.findOrderById(id, userRepository);
            if (order != null) {
                return order;
            } else {
                Log.e("AssemblerController", "Order not found with ID: " + id);
            }
        } else {
            Log.e("AssemblerController", "Database access is not initialized.");
        }
        return null;
    }


}







    /*
    private static AssemblerController instance = null;
    private static Assembler assembler;

    // Constructeur privé pour le Singleton
    private AssemblerController() {
        super();
    }

    // Méthode pour obtenir l'instance unique du contrôleur
    public static AssemblerController getInstance(String firstName, String lastName, String email, String password) {
        if (instance == null) {
            instance = new AssemblerController();
            assembler = new Assembler(firstName, lastName, email, password);
        }
        return instance;
    }

    // Méthode pour ajouter une commande à la liste des commandes de l'Assembler
    public void addOrder(Order order) {
        assembler.addOrder(order);
    }

    // Méthode pour accepter une commande
    public void acceptOrder(Order order) {
        assembler.acceptOrder(order);
    }

    // Méthode pour rejeter une commande avec un message
    public void rejectOrder(Order order, String message) {
        assembler.rejectOrder(order, message);
    }

    // Méthode pour compléter une commande
    public void completeOrder(Order order) {
        assembler.completeOrder(order);
    }


    // Méthode pour visualiser toutes les commandes
    public List<Order> viewAllOrders() {
        return assembler.viewAllOrders();
    }

    // Méthode pour gérer la connexion de l'Assembler
    public void loginAssembler() {
        assembler.login();
    }

    // Méthode pour gérer la déconnexion de l'Assembler
    public void logoutAssembler() {
        assembler.logout();
    }

    // Méthode pour récupérer l'objet Assembler
    public Assembler getAssembler() {
        return assembler;
    }

    */

