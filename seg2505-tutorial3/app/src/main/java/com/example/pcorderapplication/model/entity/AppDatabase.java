package com.example.pcorderapplication.model.entity;

import android.content.Context;

import com.example.pcorderapplication.model.database.UserRepository;

public class AppDatabase {
    private static AppDatabase instance;

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ComponentRepository componentRepository;

    //constructeur:
    private AppDatabase() {
        this.userRepository = new UserRepository(null);
        this.orderRepository = new OrderRepository();
        this.componentRepository = new ComponentRepository();
    }

    //Méthode pour garantir qu'un seul objet (ou instance) sera crée de Appdatabase:
    public static AppDatabase getInstance() {
        if (instance == null){
            instance = new AppDatabase();
        }
        return instance;
    }
    //les Getters pour les différents repositories (tableaux):
    public UserRepository getUserRepository() {
        return userRepository;
    }

    public OrderRepository getOrderRepository() {
        return orderRepository;
    }

    public ComponentRepository getComponentRepository() {
        return componentRepository;
    }
}

