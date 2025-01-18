package com.example.pcorderapplication.model.orders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class OrderStatus {
    private String status;
    private LocalDateTime updatedAt;
    private static final List<String> VALID_STATUSES = Arrays.asList("Pending", "Shipped", "Delivered", "Cancelled");

    public OrderStatus(String status) {
        if (VALID_STATUSES.contains(status)) {
            this.status = status;
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid initial status.");
        }
    }

    public void setStatus(String newStatus) {
        if (validateTransition(newStatus)) {
            this.status = newStatus;
            this.updatedAt = LocalDateTime.now();
        } else {
            throw new IllegalArgumentException("Invalid status transition.");
        }
    }

    public boolean validateTransition(String newStatus) {
        return VALID_STATUSES.contains(newStatus);
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
