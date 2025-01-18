package com.example.pcorderapplication.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pcorderapplication.controller.AssemblerController;
import com.example.pcorderapplication.databinding.ActivityAssemblerBinding;
import com.example.pcorderapplication.model.database.OrderRepository;
import com.example.pcorderapplication.model.database.UserRepository;
import com.example.pcorderapplication.model.orders.Order;
import com.example.pcorderapplication.model.users.Requester;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssemblerActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private ActivityAssemblerBinding binding;
    private AssemblerController assemblerController;
    private ArrayList<String> orderList;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssemblerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        assemblerController = AssemblerController.getInstance(getApplicationContext());
        orderRepository = new OrderRepository(getApplicationContext());
        userRepository = new UserRepository(getApplicationContext());

        orderList = new ArrayList<>();
        RecyclerView recyclerView = binding.ordersRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                return new RecyclerView.ViewHolder(view) {};
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView textView = holder.itemView.findViewById(android.R.id.text1);
                textView.setText(orderList.get(position));
            }

            @Override
            public int getItemCount() {
                return orderList.size();
            }
        };

        recyclerView.setAdapter(adapter);

        dataUpload();

        Toast.makeText(this, "Welcome, Assembler! Manage your orders here.", Toast.LENGTH_SHORT).show();

        binding.acceptOrderButton.setOnClickListener(v -> addOrder());
        binding.updateOrderButton.setOnClickListener(v -> updateOrder());
        binding.rejectOrderButton.setOnClickListener(v -> deleteOrder());
        binding.completeOrderButton.setOnClickListener(v -> completeOrder());

        binding.goBackArrow.setOnClickListener(v -> goBack());
    }

    private void goBack() {
        finish();
    }

    private void addOrder() {
        String orderIdStr = binding.searchOrderIdEditText.getText().toString().trim();
        String requesterIdStr = binding.editTextRequesterId.getText().toString().trim();

        if (orderIdStr.isEmpty() || requesterIdStr.isEmpty()) {
            Toast.makeText(this, "Order ID and Requester ID are required!", Toast.LENGTH_SHORT).show();
            Log.w("OrderValidation", "Order ID and Requester ID are required!");
            return;
        }

        int orderId;
        int requesterId;
        try {
            orderId = Integer.parseInt(orderIdStr);
            requesterId = Integer.parseInt(requesterIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Order ID or Requester ID format!", Toast.LENGTH_SHORT).show();
            Log.e("OrderValidation", "Invalid Order ID or Requester ID format!");

            return;
        }

        Requester requester = userRepository.findRequesterById(requesterId);
        if (requester == null) {
            Toast.makeText(this, "Requester not found!", Toast.LENGTH_SHORT).show();
            Log.w("RequesterManagement", "Requester not found!");
            return;
        }

        Order order = new Order(orderId, requester);

        if (assemblerController.addOrder(order)) {
            Toast.makeText(this, "Order added successfully", Toast.LENGTH_LONG).show();
            Log.i("OrderManagement", "Order added successfully.");
            refreshOrderList();
        } else {
            Log.e("AssemblerController", "Error adding order to database");
        }

        resetFields();
    }

    private void updateOrder() {
        String orderIdStr = binding.searchOrderIdEditText.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();
        String status = binding.editTextStatus.getText().toString().trim();
        String requesterIdStr = binding.editTextRequesterId.getText().toString().trim();

        if (orderIdStr.isEmpty() || description.isEmpty() || status.isEmpty() || requesterIdStr.isEmpty()) {
            Toast.makeText(this, "All fields are required for update!", Toast.LENGTH_SHORT).show();
            Log.w("UpdateValidation", "All fields are required for update!");
            return;
        }

        int id;
        int requesterId;
        try {
            id = Integer.parseInt(orderIdStr);
            requesterId = Integer.parseInt(requesterIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid ID format!", Toast.LENGTH_SHORT).show();
            Log.e("IDValidation", "Invalid ID format!");
            return;
        }

        Requester requester = userRepository.findRequesterById(requesterId);
        if (requester == null) {
            Toast.makeText(this, "Requester not found!", Toast.LENGTH_SHORT).show();
            Log.w("RequesterManagement", "Requester not found!");
            return;
        }

        Order order = new Order(id, requester);
        order.setMessage(description);
        order.updateStatus(status);
        order.setModificationDateTime(LocalDateTime.now());

        if (orderRepository.updateOrder(order)) {
            Toast.makeText(this, "Order updated successfully", Toast.LENGTH_LONG).show();
            Log.i("OrderManagement", "Order updated successfully.");
            refreshOrderList();
        } else {
            Log.e("AssemblerController", "Error updating order in database");
        }

        resetFields();
    }

    private void deleteOrder() {
        String orderIdStr = binding.searchOrderIdEditText.getText().toString().trim();

        if (orderIdStr.isEmpty()) {
            Toast.makeText(this, "Order ID is required for deletion!", Toast.LENGTH_SHORT).show();
            Log.w("OrderManagement", "Order ID is required for deletion!");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Order ID format!", Toast.LENGTH_SHORT).show();
            Log.e("OrderValidation", "Invalid Order ID format!");
            return;
        }

        Order order = assemblerController.findOrderById(id);
        String message = binding.editTextMessage.getText().toString().trim();
        if (order == null) {
            Toast.makeText(this, "Order not found!", Toast.LENGTH_SHORT).show();
            Log.w("OrderManagement", "Order not found!");
            return;
        }

        if (assemblerController.rejectOrder(order, message)) {
            Toast.makeText(this, "Order deleted successfully", Toast.LENGTH_LONG).show();
            Log.i("OrderManagement", "Order deleted successfully.");
            refreshOrderList();
        } else {
            Log.e("AssemblerController", "Error deleting order from database");
        }

        resetFields();
    }

    private void completeOrder() {
        String orderIdStr = binding.searchOrderIdEditText.getText().toString().trim();

        if (orderIdStr.isEmpty()) {
            Toast.makeText(this, "Order ID is required to mark as complete!", Toast.LENGTH_SHORT).show();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Order ID format!", Toast.LENGTH_SHORT).show();
            Log.w("OrderManagement", "Order ID is required to mark as complete!");
            return;
        }

        Order order = assemblerController.findOrderById(id);
        if (order == null) {
            Toast.makeText(this, "Order not found!", Toast.LENGTH_SHORT).show();
            Log.w("OrderManagement", "Order not found!");
            return;
        }

        assemblerController.completeOrder(order);

        Toast.makeText(this, "Order marked as completed", Toast.LENGTH_LONG).show();
        Log.i("OrderManagement", "Order marked as completed.");
        refreshOrderList();

        resetFields();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void dataUpload() {
        orderList.clear();
        List<Order> orders = null;
        orders = orderRepository.getAllOrders(userRepository);
        if(orders != null) {
            for (Order order : orders) {
                orderList.add(formatOrder(order));
            }
            adapter.notifyDataSetChanged();
        }
    }

    private String formatOrder(Order order) {
        return String.format("ID: %d - %s", order.getId(), order.getStatus());
    }

    private void refreshOrderList() {
        dataUpload();
    }

    private void resetFields() {
        binding.searchOrderIdEditText.setText("");
        binding.editTextDescription.setText("");
        binding.editTextStatus.setText("");
        binding.editTextRequesterId.setText("");
        binding.editTextMessage.setText("");

        binding.searchOrderIdEditText.setEnabled(true);
        binding.editTextDescription.setEnabled(true);
        binding.editTextStatus.setEnabled(true);
        binding.editTextRequesterId.setEnabled(true);
        binding.editTextMessage.setEnabled(true);
    }
}






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


/*
package com.example.pcorderapplication.view;



import com.example.pcorderapplication.model.database.UserRepository;
import  com.example.pcorderapplication.model.entity.OrderRepository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pcorderapplication.controller.AssemblerController;
import com.example.pcorderapplication.databinding.ActivityAssemblerBinding;
import com.example.pcorderapplication.model.orders.Order;
import com.example.pcorderapplication.model.users.Requester;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AssemblerActivity extends AppCompatActivity {

    private UserRepository userRepository;
    private OrderRepository orderRepository;
    private ActivityAssemblerBinding binding;
    private AssemblerController assemblerController;
    private ArrayList<String> orderList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssemblerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        assemblerController = AssemblerController.getInstance(getApplicationContext());


        orderList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderList);
        RecyclerView recyclerView = binding.ordersRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.Adapter adapter = new RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(android.R.layout.simple_list_item_1, parent, false);
                return new RecyclerView.ViewHolder(view) {
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                TextView textView = (TextView) holder.itemView.findViewById(android.R.id.text1);
                textView.setText(orderList.get(position));
            }

            @Override
            public int getItemCount() {
                return orderList.size();
            }
        };

        recyclerView.setAdapter(adapter);


        dataUpload();
        Toast.makeText(this, "Welcome, Assembler! Manage your orders here.", Toast.LENGTH_SHORT).show();


        binding.acceptOrderButton.setOnClickListener(v -> addOrder());
        binding.updateOrderButton.setOnClickListener(v -> updateOrder());
        binding.rejectOrderButton.setOnClickListener(v -> deleteOrder());
        binding.completeOrderButton.setOnClickListener(v -> completeOrder());
    }

    private void addOrder() {
        String orderIdStr = binding.searchOrderIdEditText.getText().toString().trim();
        String requesterIdStr = binding.editTextRequesterId.getText().toString().trim();

        if (orderIdStr.isEmpty() || requesterIdStr.isEmpty()) {
            Toast.makeText(this, "Order ID and Requester ID are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        int orderId;
        int requesterId;
        try {
            orderId = Integer.parseInt(orderIdStr);
            requesterId = Integer.parseInt(requesterIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Order ID or Requester ID format!", Toast.LENGTH_SHORT).show();
            return;
        }

        Context context = null;
        UserRepository userRepository = new UserRepository(context);
        Requester requester = userRepository.findRequesterById(requesterId);
        if (requester == null) {
            Toast.makeText(this, "Requester not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        Order order = new Order(orderId, requester);

        if (assemblerController.addOrder(order)) {
            Toast.makeText(this, "Order added successfully", Toast.LENGTH_LONG).show();
            refreshOrderList();
        } else {
            Log.e("AssemblerController", "Error adding order to database");
        }

        resetFields();
    }


    private void updateOrder() {
        String orderId = binding.searchOrderIdEditText.getText().toString().trim();
        String description = binding.editTextDescription.getText().toString().trim();
        String status = binding.editTextStatus.getText().toString().trim();
        int requesterIdStr = Integer.parseInt(binding.editTextRequesterId.getText().toString().trim());

        if (orderId.isEmpty() || description.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "All fields are required for update!", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(orderId);
        String modificationDate = getCurrentDateTime();
        Requester requester = userRepository.findRequesterById(requesterIdStr);
        Order order = new Order(id, requester);

        if (orderRepository.updateOrder(order)) {
            Toast.makeText(this, "Order updated successfully", Toast.LENGTH_LONG).show();
            refreshOrderList();
        } else {
            Log.e("AssemblerController", "Error updating order in database");
        }

        resetFields();
    }

    private void deleteOrder() {
        int orderId = Integer.parseInt(binding.searchOrderIdEditText.getText().toString().trim());
        String orderIdStr = binding.searchOrderIdEditText.getText().toString().trim();

        if (orderIdStr.isEmpty()) {
            Toast.makeText(this, "Order ID is required for deletion!", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = orderId;

        Order order = assemblerController.findOrderById(id);
        String message = binding.editTextMessage.getText().toString().trim();
        if (assemblerController.rejectOrder(order, message)) {
            Toast.makeText(this, "Order deleted successfully", Toast.LENGTH_LONG).show();
            refreshOrderList();
        } else {
            Log.e("AssemblerController", "Error deleting order from database");
        }

        resetFields();
    }

    private void completeOrder() {
        String orderIdStr = binding.searchOrderIdEditText.getText().toString().trim();

        if (orderIdStr.isEmpty()) {
            Toast.makeText(this, "Order ID is required to mark as complete!", Toast.LENGTH_SHORT).show();
            return;
        }

        int id;
        try {
            id = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid Order ID format!", Toast.LENGTH_SHORT).show();
            return;
        }

        Order order = assemblerController.findOrderById(id);
        if (order == null) {
            Toast.makeText(this, "Order not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        assemblerController.completeOrder(order);

        Toast.makeText(this, "Order marked as completed", Toast.LENGTH_LONG).show();
        refreshOrderList();

        resetFields();
    }


    @SuppressLint("SuspiciousIndentation")
    private void dataUpload() {
        List<Order> orders = orderRepository.getAllOrders();
        for (Order order : orders) {
            orderList.add(formatOrder(order));
        }
        adapter.notifyDataSetChanged();
    }

    private String formatOrder(Order order) {
        return String.format("ID: %d - %s (%s)", order.getId(),  order.getStatus());
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void refreshOrderList() {
        orderList.clear();
        dataUpload();
    }

    private void resetFields() {
        binding.searchOrderIdEditText.setText("");
        binding.editTextDescription.setText("");
        binding.editTextStatus.setText("");
        binding.editTextRequesterId.setText("");
        binding.editTextMessage.setText("");


        binding.searchOrderIdEditText.setEnabled(true);
        binding.editTextDescription.setEnabled(true);
        binding.editTextStatus.setEnabled(true);
        binding.editTextRequesterId.setEnabled(true);
        binding.editTextMessage.setEnabled(true);
    }
}
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


