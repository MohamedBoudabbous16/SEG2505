package com.example.pcorderapplication.model.orders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.model.users.Requester;

public class Order {

    private int id;
    private OrderStatus status;
    private String message;
    private LocalDateTime creationDateTime;
    private LocalDateTime modificationDateTime;
    private Requester requester;
    private List<Component> components;

    public Order(int id, Requester requester) {
        this.id = id;
        this.requester = requester;
        this.creationDateTime = LocalDateTime.now();
        this.modificationDateTime = this.creationDateTime;
        this.status = new OrderStatus("Pending");
        this.components = new ArrayList<>();
    }
    public Requester getRequester(int id){
        return requester;
    }

    public void addComponent(Component component) {
        components.add(component);
        updateModificationDateTime();
    }

    public void removeComponent(Component component) {
        components.remove(component);
        updateModificationDateTime();
    }

    public void updateStatus(String newStatus) {
        if (status.validateTransition(newStatus)) {
            status.setStatus(newStatus);
            updateModificationDateTime();
        }
    }



    private void updateModificationDateTime() {
        this.modificationDateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        updateModificationDateTime();
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public LocalDateTime getModificationDateTime() {
        return modificationDateTime;
    }

    public int getRequesterId() {
        return requester.getId();
    }

    public List<Component> getComponents() {
        return components;
    }
    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
    public void setModificationDateTime(LocalDateTime modificationDateTime) {
        this.modificationDateTime = modificationDateTime;
    }
}
