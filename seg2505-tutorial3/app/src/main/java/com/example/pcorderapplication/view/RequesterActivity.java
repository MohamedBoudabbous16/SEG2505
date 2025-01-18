package com.example.pcorderapplication.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcorderapplication.controller.RequesterController;
import com.example.pcorderapplication.databinding.ActivityRequesterBinding;
import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.model.orders.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequesterActivity extends AppCompatActivity {

    private ActivityRequesterBinding binding;
    private RequesterController requesterController;
    private ArrayList<String> orderList;
    List<Component> components = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequesterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requesterController = RequesterController.getInstance(this);

        // Initialize the order list and adapter
        orderList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderList);

        // Optional: Add initial empty items if needed
        orderList.add("");
        orderList.add("");
        orderList.add("");

        // Display the orders in a TextView
        StringBuilder ordersText = new StringBuilder();
        for (String order : orderList) {
            ordersText.append(order).append("\n");
        }

        TextView ordersTextView = binding.ordersListTextView;
        ordersTextView.setText(ordersText.toString());

        Toast.makeText(this, "Welcome, Requester! You can manage your orders here.", Toast.LENGTH_SHORT).show();

        // Load existing orders
        loadOrders();

        // Set up button click listeners
        binding.createOrderButton.setOnClickListener(v -> createOrder());
        binding.deleteOrderButton.setOnClickListener(v -> deleteOrder());


        // Additional initialization for component requests
        binding.createOrderButton.setOnClickListener(v -> {
            String title = binding.editTextTitle.getText().toString().trim();
            if (!title.isEmpty()) {
                createOrder();
            } else {
                Toast.makeText(this, "Please enter a component title", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createOrder() {

        String title = binding.editTextTitle.getText().toString().trim();
        Integer quantity = parseQuantity(binding.editTextQuantity.getText().toString().trim());

        if (title.isEmpty() || quantity == null || quantity <= 0) {
            Toast.makeText(this, "Please enter a valid title and quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        String type = getType();
        String subtype = getSubtype();
        String comment = getComment();
        String creationDate = getCurrentDateTime();
        String modificationDate = creationDate;


        components.add(new Component(title, type, subtype, quantity, comment, creationDate, modificationDate));

        if (requesterController.createOrder(components)) {
                // The requested component is available
                Toast.makeText(this, "Component '" + title + "' is available.", Toast.LENGTH_SHORT).show();

            } else {
                // Display suggested components
                displaySuggestions(components);
            }

        Toast.makeText(this, "Order creation request sent.", Toast.LENGTH_SHORT).show();

        loadOrders();
        resetFields();
    }

    private void deleteOrder() {
        String orderIdStr = binding.editTextTitle.getText().toString().trim();
        int orderId;

        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid order ID for deletion", Toast.LENGTH_SHORT).show();
            return;
        }

        requesterController.deleteOrder(orderId);
        Toast.makeText(this, "Order deletion request sent.", Toast.LENGTH_SHORT).show();

        loadOrders();
        resetFields();
    }

    private void loadOrders() {
        orders = requesterController.viewOwnOrders();
        orderList.clear();

        if (orders != null && !orders.isEmpty()) {
            for (Order order : orders) {
                @SuppressLint("DefaultLocale")
                String orderDetails = String.format(
                        "Order ID: %d\nComponents: %s\nStatus: %s\nDate: %s",
                        order.getId(),
                        order.getComponents().toString(),
                        order.getStatus(),
                        order.getCreationDateTime()
                );
                orderList.add(orderDetails);
            }
        } else {
            Toast.makeText(this, "No orders to display.", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }

    private Integer parseQuantity(String quantityStr) {
        try {
            return Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getType() {
        return "defaultType";
    }

    private String getSubtype() {
        return "defaultSubtype";
    }

    private String getComment() {
        return "";
    }

    private void resetFields() {
        binding.editTextTitle.setText("");
        binding.editTextQuantity.setText("");
    }

    /*
    private void handleComponentRequest(String title) {
        List<Component> components = requesterController.requestComponent(title);
        if (components != null && !components.isEmpty()) {
            if (components.size() == 1 && components.get(0).getTitle().equals(title)) {
                // The requested component is available
                Toast.makeText(this, "Component '" + title + "' is available.", Toast.LENGTH_SHORT).show();
                // Proceed with order placement or other logic
            } else {
                // Display suggested components
                displaySuggestions(components);
            }
        } else {
            Toast.makeText(this, "No components found.", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void displaySuggestions(List<Component> components) {
        StringBuilder suggestions = new StringBuilder("Suggested Components:\n");
        for (Component c : components) {
            suggestions.append("- ").append(c.getTitle()).append(" (").append(c.getType()).append(")\n");
        }
        // Display the suggestions in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Component Not Available")
                .setMessage(suggestions.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
