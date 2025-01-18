package com.example.pcorderapplication.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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
    private ArrayList<String> orderList = new ArrayList<>(); // Initialisation pour éviter NullPointerException
    private ArrayAdapter<String> adapter;
    List<Component> components = new ArrayList<>();

    private int counter = 0;
    private List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequesterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requesterController = RequesterController.getInstance(this);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderList);
        ListView listView = binding.ordersListView;
        listView.setAdapter(adapter);

       /* StringBuilder ordersText = new StringBuilder();
        for (String order : orderList) {
            ordersText.append(order).append("\n");
        }*/

        Toast.makeText(this, "Welcome, Requester! You can manage your orders here.", Toast.LENGTH_SHORT).show();
        Log.i("RequesterActivity", "Welcome, Requester! You can manage your orders here.");

        loadOrders();

        binding.deleteOrderButton.setOnClickListener(v -> deleteOrder());

        binding.createOrderButton.setOnClickListener(v -> {
            String title = binding.editTextTitle.getText().toString().trim();
            if (!title.isEmpty()) {
                createOrder();
            } else {
                Toast.makeText(this, "Please enter a component title", Toast.LENGTH_SHORT).show();
                Log.w("ComponentValidation", "Please enter a component title");
            }
        });
        binding.goBackArrow.setOnClickListener(v -> goBack());
    }

    private void goBack() {
        finish();
    }

    private void createOrder() {

        String title = binding.editTextTitle.getText().toString().trim();
        Integer quantity = parseQuantity(binding.editTextQuantity.getText().toString().trim());

        if (title.isEmpty() || quantity == null || quantity <= 0) {
            Toast.makeText(this, "Please enter a valid title and quantity", Toast.LENGTH_SHORT).show();
            Log.w("InputValidation", "Please enter a valid title and quantity");
            return;
        }

        Component component = requesterController.findComponent(title);
        components.add(component);

        if (requesterController.createOrder(components)) {
            Toast.makeText(this, "Component '" + title + "' is available.", Toast.LENGTH_SHORT).show();
            Log.i("ComponentCheck", "Component '" + title + "' is available.");


            counter++;
            String componentDetails = String.format("%d : %s (%s, %s) - %d", counter, title, component.getType(), component.getSubtype(), quantity);
            orderList.add(componentDetails);
            adapter.notifyDataSetChanged();

        } else {
            displaySuggestions(components);
        }

        Toast.makeText(this, "Order creation request accepted.", Toast.LENGTH_SHORT).show();


        resetFields();
    }

    private void deleteOrder() {
        String orderIdStr = binding.editTextTitle.getText().toString().trim();
        int orderId;

        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid order ID for deletion", Toast.LENGTH_SHORT).show();
            Log.w("OrderValidation", "Please enter a valid order ID for deletion");
            return;
        }

        requesterController.deleteOrder(orderId);
        Toast.makeText(this, "Order deletion request accepted.", Toast.LENGTH_SHORT).show();
        Log.i("OrderManagement", "Order deletion request accepted.");
        counter--;

        orderList.removeIf(s -> s.startsWith(orderIdStr));
        adapter.notifyDataSetChanged();
        resetFields();
    }

    private void loadOrders() {
       /* orders = requesterController.viewOwnOrders();
        orderList.clear(); // Pas de NullPointerException car orderList est initialisée

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
            Log.i("OrderDisplay", "No orders to display.");
        }*/
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Component Not Available")
                .setMessage(suggestions.toString())
                .setPositiveButton("OK", null)
                .show();
    }
}
