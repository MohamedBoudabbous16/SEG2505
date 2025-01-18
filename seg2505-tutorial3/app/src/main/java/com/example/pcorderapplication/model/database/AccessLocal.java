package com.example.pcorderapplication.model.database;

import static com.example.pcorderapplication.model.database.tools.DatabaseSQLite.COMPONENT_TITLE;
import static com.example.pcorderapplication.model.database.tools.DatabaseSQLite.TABLE_COMPONENTS;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pcorderapplication.model.database.tools.DatabaseSQLite;
import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.model.users.Requester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AccessLocal {

    // Database configuration
    private static final String DATABASE_NAME = "pcorderapp.db";
    private static final int DATABASE_VERSION = 1;

    private static final int NUM_USER_ID = 0;
    private static final int NUM_USER_FIRSTNAME = 1;
    private static final int NUM_USER_LASTNAME = 2;
    private static final int NUM_USER_EMAIL = 3;
    private static final int NUM_USER_PASSWORD = 4;
    private static final int NUM_USER_ROLE = 5;
    private static final int NUM_USER_CREATED_AT = 6;
    private static final int NUM_USER_MODIFIED_AT = 7;

    // Colonnes de la table orders (simplifiée, à ajuster selon la structure des commandes)

    private static final int NUM_ORDER_ID = 0;
    private static final int NUM_ORDER_STATUS = 1;
    private static final int NUM_ORDER_USER_ID = 2;// FK vers users

    // Colonnes de la table components (simplifiée)
    private static final int NUM_COMPONENT_TITLE = 0;
    private static final int NUM_COMPONENT_TYPE = 1;
    private static final int NUM_COMPONENT_SUBTYPE = 2;
    private static final int NUM_COMPONENT_QUANTITY = 3;
    private static final int NUM_COMPONENT_COMMENT = 4;
    private static final int NUM_COMPONENT_CREATION_DATE = 5;
    private static final int NUM_COMPONENT_MODIFICATION_DATE = 6;
    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_COMPONENTS = "components";

    // Column names for components table
    public static final String COMPONENT_TYPE = "type";
    public static final String COMPONENT_SUBTYPE = "subtype";
    public static final String COMPONENT_QUANTITY = "quantity";
    public static final String COMPONENT_COMMENT = "comment";
    public static final String COMPONENT_CREATION_DATE = "creation_date";
    public static final String COMPONENT_MODIFICATION_DATE = "modification_date";
    public static final String COMPONENT_REQUEST_COUNT = "request_count";

    // SQLite database instance
    private SQLiteDatabase bd;
    private DatabaseSQLite databaseSQLite;

    // Constructor
    public AccessLocal(Context context) {
        databaseSQLite = new DatabaseSQLite(context);
    }

    // Open database for write
    public void openForWrite() {
        bd = databaseSQLite.getWritableDatabase();
    }

    // Open database for read
    public void openForRead() {
        bd = databaseSQLite.getReadableDatabase();
    }

    // Close database
    public void close() {
        bd.close();
    }

    // Insert a new component
    public long addComponent(Component component) {
        openForWrite();
        ContentValues content = new ContentValues();
        content.put(COMPONENT_TITLE, component.getTitle());
        content.put(COMPONENT_TYPE, component.getType());
        content.put(COMPONENT_SUBTYPE, component.getSubtype());
        content.put(COMPONENT_QUANTITY, component.getQuantity());
        content.put(COMPONENT_COMMENT, component.getComment());
        content.put(COMPONENT_CREATION_DATE, component.getCreation_Date());
        content.put(COMPONENT_MODIFICATION_DATE, component.getModification_date());
        long res = bd.insert(TABLE_COMPONENTS, null, content);
        Log.i("database", "Component inserted successfully.");
        close();
        return res;
    }

    // Handle component request
    public List<Component> handleComponentRequest(String title) {
        openForWrite();
        List<Component> suggestions = new ArrayList<>();

        Component component = findComponentByTitle(title);

        if (component != null) {
            component.setRequestCount(component.getRequestCount() + 1);
            updateComponentRequestCount(component);

            if (component.getQuantity() > 0) {
                return Collections.singletonList(component);
            } else {
                suggestions = getSimilarComponents(component.getType(), title);
            }
        } else {
            suggestions = getSimilarComponentsByType(title);
        }

        close();
        return suggestions;
    }

    private void updateComponentRequestCount(Component component) {
        ContentValues content = new ContentValues();
        content.put(COMPONENT_REQUEST_COUNT, component.getRequestCount());

        String whereClause = COMPONENT_TITLE + " = ?";
        String[] whereArgs = new String[]{component.getTitle()};

        bd.update(TABLE_COMPONENTS, content, whereClause, whereArgs);
    }

    private List<Component> getSimilarComponents(String type, String excludedTitle) {
        List<Component> components = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_COMPONENTS + " WHERE " + COMPONENT_TYPE + " = ? AND " + COMPONENT_TITLE + " != ? ORDER BY " + COMPONENT_REQUEST_COUNT + " DESC LIMIT 5";
        Cursor cursor = bd.rawQuery(query, new String[]{type, excludedTitle});
        components = cursorToComponentList(cursor);
        cursor.close();
        return components;
    }

    private List<Component> getSimilarComponentsByType(String title) {
        List<Component> components = new ArrayList<>();
        String type = "defaultType";
        String query = "SELECT * FROM " + TABLE_COMPONENTS + " WHERE " + COMPONENT_TYPE + " = ? ORDER BY " + COMPONENT_REQUEST_COUNT + " DESC LIMIT 5";
        Cursor cursor = bd.rawQuery(query, new String[]{type});
        components = cursorToComponentList(cursor);
        cursor.close();
        return components;
    }

    private List<Component> cursorToComponentList(Cursor cursor) {
        List<Component> components = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(NUM_COMPONENT_TITLE);
                String type = cursor.getString(NUM_COMPONENT_TYPE);
                String subtype = cursor.getString(NUM_COMPONENT_SUBTYPE);
                int quantity = cursor.getInt(NUM_COMPONENT_QUANTITY);
                String comment = cursor.isNull(NUM_COMPONENT_COMMENT) ? null : cursor.getString(NUM_COMPONENT_COMMENT);
                String creationDate = cursor.isNull(NUM_COMPONENT_CREATION_DATE) ? "" : cursor.getString(NUM_COMPONENT_CREATION_DATE);
                String modificationDate = cursor.isNull(NUM_COMPONENT_MODIFICATION_DATE) ? "" : cursor.getString(NUM_COMPONENT_MODIFICATION_DATE);
                int requestCount = cursor.getCount();

                Component component = new Component(title, type, subtype, quantity, comment, creationDate, modificationDate);
                component.setRequestCount(requestCount);

                components.add(component);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return components;
    }

    public Component findComponentByTitle(String title) // version 1
    {
        Component component = null;
        openForRead();
        String req = "select * from "+ TABLE_COMPONENTS + " where "+ COMPONENT_TITLE + " = \""+ title+ "\"";
        Cursor curseur = bd.rawQuery(req, null); // lire ligne par ligne
        curseur.moveToLast(); // se possitionner sur la dernière ligne du table
        if (!curseur.isAfterLast()) {
            String type = curseur.getString(NUM_COMPONENT_TYPE);
            String subtype = curseur.getString(NUM_COMPONENT_SUBTYPE);
            int quantity = curseur.getInt(NUM_COMPONENT_QUANTITY);
            String comment = curseur.getString(NUM_COMPONENT_COMMENT);
            String dateCreation = curseur.getString(NUM_COMPONENT_CREATION_DATE);
            String dateModification = curseur.getString(NUM_COMPONENT_MODIFICATION_DATE);
            component = new Component(title,type,subtype,quantity,comment,dateCreation,dateModification);
        }
        close();
        return component;
    }

    public long updateComponent(Component component) {
        openForWrite();
        ContentValues content = new ContentValues();
        content.put(COMPONENT_TITLE, component.getTitle());
        content.put(COMPONENT_TYPE, component.getType());
        content.put(COMPONENT_SUBTYPE, component.getSubtype());
        content.put(COMPONENT_QUANTITY, component.getQuantity());
        content.put(COMPONENT_COMMENT, component.getComment());
        content.put(COMPONENT_MODIFICATION_DATE, component.getModification_date());
        String whereClause = COMPONENT_TITLE + " = ?";
        String[] whereArgs = new String[]{component.getTitle()};
        long res = bd.update(TABLE_COMPONENTS, content, whereClause, whereArgs);
        if (res == -1) {
            Log.e("DB_ERROR", "Update failed");
        } else {
            Log.d("DB_SUCCESS", "Updated successfully, rows affected: " + res);
        }
        close();
        return res;
    }

    public List<Component> getAllComponents() {
        openForRead();
        Cursor c = bd.query(TABLE_COMPONENTS, new String[]{COMPONENT_TITLE, COMPONENT_TYPE, COMPONENT_SUBTYPE, COMPONENT_QUANTITY}, null, null, null, null, null);
        List<Component> components = cursorToComponentList(c);
        close();
        return components;
    }

    public boolean requestComponent(String title, int quantity) {
        Component component = findComponentByTitle(title);
        if (component != null) {
            if (quantity <= component.getQuantity()) {
                component.setQuantity(component.getQuantity() - quantity);
                updateComponent(component);
                return true;
            } else {
                Log.i("database", "We're out of this item in our stock.");
            }
        } else {
            Log.i("database", "Component not found.");
        }
        return false;
    }

    public int deleteComponent(String title) {
        openForWrite();
        String whereClause = COMPONENT_TITLE + " = ?";
        String[] whereArgs = new String[]{title};
        int rowsDeleted = bd.delete(TABLE_COMPONENTS, whereClause, whereArgs);
        close();
        return rowsDeleted;
    }

    public void addRequester(Requester requester) {
        openForWrite();
        ContentValues values = new ContentValues();
        values.put("first_name", requester.getFirstName());
        values.put("last_name", requester.getLastName());
        values.put("email", requester.getEmail());
        values.put("password", requester.getPassword());
        values.put("role", "Requester");
        bd.insert(TABLE_USERS, null, values);
        close();
    }

    public ArrayList<String> upload() {
        ArrayList<String> componentArrayList = new ArrayList<>();
        List<Component> componentsList = getAllComponents();
        for(Component c : componentsList) {
            String componentDetails = String.format("%s (%s, %s) - %d", c.getTitle(), c.getType(), c.getSubtype(), c.getQuantity());
            componentArrayList.add(componentDetails);
        }
        return componentArrayList;
    }

    public ArrayList<String> uploadRequest() {
        openForRead();
        ArrayList<String> componentArrayList = new ArrayList<>();

        Cursor cursor = bd.query(TABLE_COMPONENTS,
                new String[]{COMPONENT_TITLE, COMPONENT_TYPE, COMPONENT_SUBTYPE, COMPONENT_QUANTITY, COMPONENT_COMMENT, COMPONENT_CREATION_DATE, COMPONENT_MODIFICATION_DATE, COMPONENT_REQUEST_COUNT},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                // Retrieve component data
                String title = cursor.getString(NUM_COMPONENT_TITLE);
                String type = cursor.getString(NUM_COMPONENT_TYPE);
                String subtype = cursor.getString(NUM_COMPONENT_SUBTYPE);
                int quantity = cursor.getInt(NUM_COMPONENT_QUANTITY);
                String comment = cursor.isNull(NUM_COMPONENT_COMMENT) ? "" : cursor.getString(NUM_COMPONENT_COMMENT);
                String creationDate = cursor.isNull(NUM_COMPONENT_CREATION_DATE) ? "" : cursor.getString(NUM_COMPONENT_CREATION_DATE);
                String modificationDate = cursor.isNull(NUM_COMPONENT_MODIFICATION_DATE) ? "" : cursor.getString(NUM_COMPONENT_MODIFICATION_DATE);
                int requestCount = cursor.getCount();

                // Format the component's details into a string
                String componentDetails = String.format(
                        "Title: %s\nType: %s\nSubtype: %s\nQuantity: %d\nComment: %s\nCreation Date: %s\nModification Date: %s\nRequest Count: %d",
                        title, type, subtype, quantity, comment, creationDate, modificationDate, requestCount
                );

                // Add the formatted string to the list
                componentArrayList.add(componentDetails);
            } while (cursor.moveToNext());
        }

        cursor.close();
        close();
        return componentArrayList;
    }

}
