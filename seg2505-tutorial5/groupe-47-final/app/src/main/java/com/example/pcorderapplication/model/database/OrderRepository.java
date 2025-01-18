package com.example.pcorderapplication.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pcorderapplication.model.database.tools.DatabaseSQLite;
import com.example.pcorderapplication.model.orders.Order;
import com.example.pcorderapplication.model.users.Requester;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    private final DatabaseSQLite dbHelper;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public OrderRepository(Context context) {
        dbHelper = new DatabaseSQLite(context);
    }

    //  insére une commande
    public void insertOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseSQLite.ORDER_STATUS, order.getStatus().getStatus());
        values.put(DatabaseSQLite.ORDER_USER_ID, order.getRequesterId());
        values.put(DatabaseSQLite.ORDER_CREATED_AT, order.getCreationDateTime().format(formatter));
        values.put(DatabaseSQLite.ORDER_MODIFIED_AT, order.getModificationDateTime().format(formatter));

        long newRowId = db.insert(DatabaseSQLite.TABLE_ORDERS, null, values);
        if (newRowId == -1) {
            Log.i("OrderRepository", "Failed to insert order.");
        } else {
            Log.i("OrderRepository", "Order inserted successfully with ID: " + newRowId);
        }
        db.close();
    }

    //  met à jour une commande
    public boolean updateOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseSQLite.ORDER_STATUS, order.getStatus().getStatus());
        values.put(DatabaseSQLite.ORDER_MODIFIED_AT, order.getModificationDateTime().format(formatter));

        String selection = DatabaseSQLite.ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(order.getId())};

        int count = db.update(DatabaseSQLite.TABLE_ORDERS, values, selection, selectionArgs);
        if (count == 0) {
            Log.i("OrderRepository", "Order not found for update.");
        } else {
            Log.i("OrderRepository", "Order updated successfully.");
        }
        db.close();
        return false;
    }

    // supprime une commande
    public void deleteOrder(Order order) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseSQLite.ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(order.getId())};

        int deletedRows = db.delete(DatabaseSQLite.TABLE_ORDERS, selection, selectionArgs);
        if (deletedRows == 0) {
            Log.i("OrderRepository", "Order not found for deletion.");
        } else {
            Log.i("OrderRepository", "Order deleted successfully.");
        }
        db.close();
    }

    //  récupére toutes les commandes
    public List<Order> getAllOrders(UserRepository userRepository) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<Order> orders = new ArrayList<>();

        String[] projection = {
                DatabaseSQLite.ORDER_ID,
                DatabaseSQLite.ORDER_STATUS,
                DatabaseSQLite.ORDER_USER_ID,
                //DatabaseSQLite.ORDER_CREATED_AT,
                //DatabaseSQLite.ORDER_MODIFIED_AT
        };

        Cursor cursor = db.query(
                DatabaseSQLite.TABLE_ORDERS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_ID));
            String statusStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_STATUS));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_USER_ID));
            String creationDateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_CREATED_AT));
            String modificationDateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_MODIFIED_AT));

            Requester requester = (Requester) userRepository.findUserById(userId, "Requester");

            if (requester != null) {
                Order order = new Order(id, requester);
                order.updateStatus(statusStr);
                order.setCreationDateTime(LocalDateTime.parse(creationDateStr, formatter));
                order.setModificationDateTime(LocalDateTime.parse(modificationDateStr, formatter));
                orders.add(order);
            } else {
                Log.i("OrderRepository", "Requester with ID " + userId + " not found.");
            }
        }

        cursor.close();
        db.close();
        return orders;
    }

    //nouvelle classe
    public Order findOrderById(int id, UserRepository userRepository) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                DatabaseSQLite.ORDER_ID,
                DatabaseSQLite.ORDER_STATUS,
                DatabaseSQLite.ORDER_USER_ID,
                DatabaseSQLite.ORDER_CREATED_AT,
                DatabaseSQLite.ORDER_MODIFIED_AT
        };

        String selection = DatabaseSQLite.ORDER_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                DatabaseSQLite.TABLE_ORDERS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Order order = null;

        if (cursor.moveToFirst()) {
            String statusStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_STATUS));
            int userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_USER_ID));
            String creationDateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_CREATED_AT));
            String modificationDateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.ORDER_MODIFIED_AT));
            Requester requester = userRepository.findRequesterById(userId);

            if (requester != null) {
                order = new Order(id, requester);
                order.updateStatus(statusStr);
                order.setCreationDateTime(LocalDateTime.parse(creationDateStr, formatter));
                order.setModificationDateTime(LocalDateTime.parse(modificationDateStr, formatter));
            } else {
                Log.i("OrderRepository", "Requester avec l'ID " + userId + " introuvable.");
            }
        } else {
            Log.i("OrderRepository", "Commande avec l'ID " + id + " introuvable.");
        }

        cursor.close();
        db.close();
        return order;
    }
}
