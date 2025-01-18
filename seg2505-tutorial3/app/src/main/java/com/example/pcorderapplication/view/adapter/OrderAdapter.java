package com.example.pcorderapplication.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pcorderapplication.R;
import com.example.pcorderapplication.model.orders.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;

    // Constructeur pour l'adapter
    public OrderAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflater le layout pour un item de commande
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Liaison des données avec l'interface utilisateur
        Order order = orders.get(position);
   //    holder.orderIdTextView.setText("Order ID: " + order.getId());
   //    holder.orderStatusTextView.setText("Status: " + order.getStatus().getStatus())
   //    holder.orderMessageTextView.setText("Message: " + (order.getMessage() != null ? order.getMessage() : "No message"));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    // Classe interne pour le ViewHolder de chaque item
    public static class OrderViewHolder extends RecyclerView.ViewHolder {

        public TextView orderIdTextView;
        public TextView orderStatusTextView;
        public TextView orderMessageTextView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.orderIdTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            orderMessageTextView = itemView.findViewById(R.id.orderMessageTextView);
        }
    }
}
