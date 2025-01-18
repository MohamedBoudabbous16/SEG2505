package com.example.pcorderapplication.model.entity;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import com.example.pcorderapplication.model.orders.Order;
//à ce moment du projet la classe Order (et tout ce qui est dans le package orders)
// n'ont pas été implémenté encore
//c'est normal qu'il ya des erreurs
public class OrderRepository {
 /*   private final List<Order> orderDatabase;
    //constructeur
    public OrderRepository() {
        orderDatabase = new ArrayList<>();
    }
    //Méthodes pour:
    //insérerer une commande

    public void insertOrder(Order order) {
        orderDatabase.add(order);
        Log.i("OrderRepository", "Order inserted successfully.");
    }
    //mettre à jour une commande
    public void updateOrder(Order order) {
        for (int i = 0; i < orderDatabase.size(); i++) {
            if (orderDatabase.get(i).getId() == order.getId()) {
                orderDatabase.set(i, order);
                Log.i("OrderRepository", "Order updated successfully.");
                return;
            }
        }
        Log.i("OrderRepository", "Order not found.");
    }
    //supprimer une commande
    public void deleteOrder(Order order) {
        if (orderDatabase.remove(order)) {
            Log.i("OrderRepository", "Order deleted successfully.");
        } else {
            Log.i("OrderRepository", "Order not found.");
        }
    }
    //avoir toutes les commandes
    public List<Order> getAllOrders() {
        return orderDatabase;
    }*/
}
