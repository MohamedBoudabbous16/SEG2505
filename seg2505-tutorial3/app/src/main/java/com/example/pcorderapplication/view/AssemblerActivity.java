package com.example.pcorderapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pcorderapplication.R;
import com.example.pcorderapplication.controller.AssemblerController;
import com.example.pcorderapplication.model.orders.Order;
import com.example.pcorderapplication.view.adapter.OrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class AssemblerActivity extends AppCompatActivity {

    private AssemblerController assemblerController;
    private TextView ordersListTextView;
    private EditText filterStatusEditText;
    private EditText searchOrderIdEditText;
    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> currentOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assembler);

        /*
        assemblerController = AssemblerController.getInstance("John", "Doe", "assembler@example.com", "assembler123");

        // UI elements
        ordersListTextView = findViewById(R.id.ordersListTextView);
        filterStatusEditText = findViewById(R.id.filterStatusEditText);
        searchOrderIdEditText = findViewById(R.id.searchOrderIdEditText);
        Button acceptOrderButton = findViewById(R.id.acceptOrderButton);
        Button rejectOrderButton = findViewById(R.id.rejectOrderButton);
        Button completeOrderButton = findViewById(R.id.completeOrderButton);
        Button viewAllOrdersButton = findViewById(R.id.viewAllOrdersButton);
        Button filterOrdersButton = findViewById(R.id.filterOrdersButton);
        Button searchOrderButton = findViewById(R.id.searchOrderButton);
        Button viewStatisticsButton = findViewById(R.id.viewStatisticsButton);

        // RecyclerView setup
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        currentOrders = new ArrayList<>();
        orderAdapter = new OrderAdapter(currentOrders);
        ordersRecyclerView.setAdapter(orderAdapter);

        assemblerController.addOrder(new Order(1, null));

        acceptOrderButton.setOnClickListener(v -> handleAcceptOrder());
        rejectOrderButton.setOnClickListener(v -> handleRejectOrder());
        completeOrderButton.setOnClickListener(v -> handleCompleteOrder());
        viewAllOrdersButton.setOnClickListener(v -> updateOrderList());
        filterOrdersButton.setOnClickListener(v -> filterOrdersByStatus());
        searchOrderButton.setOnClickListener(v -> searchOrderById());
        viewStatisticsButton.setOnClickListener(v -> showOrderStatistics());
        */

    }
/*
    private void handleAcceptOrder() {
        List<Order> orders = assemblerController.viewAllOrders();
        if (!orders.isEmpty()) {
            showOrderDetailsDialog(orders.get(0), () -> {
                assemblerController.acceptOrder(orders.get(0));
                Toast.makeText(this, "Order " + orders.get(0).getId() + " has been accepted.", Toast.LENGTH_SHORT).show();
                updateOrderList();
            });
        } else {
            showToastWithCustomMessage("No orders available to accept.");
        }
    }

    private void handleRejectOrder() {
        List<Order> orders = assemblerController.viewAllOrders();
        if (!orders.isEmpty()) {
            showOrderDetailsDialog(orders.get(0), () -> {
                assemblerController.rejectOrder(orders.get(0), "Issue with the order");
                Toast.makeText(this, "Order " + orders.get(0).getId() + " has been rejected.", Toast.LENGTH_SHORT).show();
                updateOrderList();
            });
        } else {
            showToastWithCustomMessage("No orders available to reject.");
        }
    }

    private void handleCompleteOrder() {
        List<Order> orders = assemblerController.viewAllOrders();
        if (!orders.isEmpty()) {
            showOrderDetailsDialog(orders.get(0), () -> {
                assemblerController.completeOrder(orders.get(0));
                Toast.makeText(this, "Order " + orders.get(0).getId() + " has been completed.", Toast.LENGTH_SHORT).show();
                updateOrderList();
            });
        } else {
            showToastWithCustomMessage("No orders available to complete.");
        }
    }

    private void updateOrderList() {
        List<Order> orders = assemblerController.viewAllOrders();
        if (orders.isEmpty()) {
            ordersListTextView.setText("No orders available.");
        } else {
            currentOrders.clear();
            currentOrders.addAll(orders);
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void filterOrdersByStatus() {
        String filterStatus = filterStatusEditText.getText().toString();
        List<Order> filteredOrders = new ArrayList<>();

        for (Order order : assemblerController.viewAllOrders()) {
            if (order.getStatus().getStatus().equalsIgnoreCase(filterStatus)) {
                filteredOrders.add(order);
            }
        }

        if (filteredOrders.isEmpty()) {
            ordersListTextView.setText("No orders found with status: " + filterStatus);
        } else {
            currentOrders.clear();
            currentOrders.addAll(filteredOrders);
            orderAdapter.notifyDataSetChanged();
        }
    }

    private void searchOrderById() {
        String orderIdString = searchOrderIdEditText.getText().toString();
        if (orderIdString.isEmpty()) {
            showToastWithCustomMessage("Please enter an order ID.");
            return;
        }

        int orderId = Integer.parseInt(orderIdString);
        List<Order> orders = assemblerController.viewAllOrders();
        Order foundOrder = null;

        for (Order order : orders) {
            if (order.getId() == orderId) {
                foundOrder = order;
                break;
            }
        }

        if (foundOrder != null) {
            showOrderDetailsDialog(foundOrder, null);
        } else {
            showToastWithCustomMessage("Order with ID " + orderId + " not found.");
        }
    }

    private void showOrderStatistics() {
        List<Order> orders = assemblerController.viewAllOrders();
        int acceptedCount = 0;
        int rejectedCount = 0;
        int completedCount = 0;

        for (Order order : orders) {
            switch (order.getStatus().getStatus()) {
                case "Accepted":
                    acceptedCount++;
                    break;
                case "Rejected":
                    rejectedCount++;
                    break;
                case "Completed":
                    completedCount++;
                    break;
            }
        }

        String statisticsMessage = "Statistics:\n" +
                "Accepted Orders: " + acceptedCount + "\n" +
                "Rejected Orders: " + rejectedCount + "\n" +
                "Completed Orders: " + completedCount;

        new AlertDialog.Builder(this)
                .setTitle("Order Statistics")
                .setMessage(statisticsMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    private void showOrderDetailsDialog(Order order, Runnable onConfirm) {
        new AlertDialog.Builder(this)
                .setTitle("Order Details")
                .setMessage("Order ID: " + order.getId() + "\n" +
                        "Status: " + order.getStatus().getStatus() + "\n" +
                        "Message: " + (order.getMessage() != null ? order.getMessage() : "No message"))
                .setPositiveButton("OK", (dialog, which) -> {
                    if (onConfirm != null) {
                        onConfirm.run();
                    }
                })
                .show();
    }

    private void showToastWithCustomMessage(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ordersListTextView", ordersListTextView.getText().toString());
        outState.putString("filterStatus", filterStatusEditText.getText().toString());
        outState.putString("searchOrderId", searchOrderIdEditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ordersListTextView.setText(savedInstanceState.getString("ordersListTextView"));
        filterStatusEditText.setText(savedInstanceState.getString("filterStatus"));
        searchOrderIdEditText.setText(savedInstanceState.getString("searchOrderId"));
    }

 */
}

