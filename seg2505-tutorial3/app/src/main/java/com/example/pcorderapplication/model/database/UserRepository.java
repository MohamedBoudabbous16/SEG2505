package com.example.pcorderapplication.model.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pcorderapplication.model.users.*;
import com.example.pcorderapplication.model.database.tools.DatabaseSQLite;
import com.example.pcorderapplication.model.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private final DatabaseSQLite dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseSQLite(context);
    }

    public void insertUser(UserInfo user) {
        if (findUserByEmail(user.getEmail()) != null) {
            Log.i("UserRepository", "The user with this email already exists.");
        } else {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseSQLite.USER_FIRSTNAME, user.getFirstName());
            values.put(DatabaseSQLite.USER_LASTNAME, user.getLastName());
            values.put(DatabaseSQLite.USER_EMAIL, user.getEmail());
            values.put(DatabaseSQLite.USER_PASSWORD, user.getPassword());
            values.put(DatabaseSQLite.USER_ROLE, user.getRole());

            long newRowId = db.insert(DatabaseSQLite.TABLE_USERS, null, values);
            if (newRowId == -1) {
                Log.i("UserRepository", "Failed to insert user.");
            } else {
                Log.i("UserRepository", "User inserted successfully with ID: " + newRowId);
            }
            db.close();
        }
    }

    // Méthode principale pour rechercher un utilisateur par email et rôle
    public User findUserByEmailAndRole(String email, String role) {
        switch (role) {
            case "Requester":
                return findRequesterByEmail(email);
            case "Administrator":
                return findAdminByEmail(email);
            case "StoreKeeper":
                return findStoreKeeperByEmail(email);
            //case "Assembler":
             //   return findAssemblerByEmail(email);
            default:
                return null;
        }
    }

    // Méthode principale pour rechercher un utilisateur par ID et rôle
    public User findUserByIdAndRole(int userId, String role) {
        switch (role) {
            case "Requester":
                return findRequesterById(userId);
            case "Administrator":
                return findAdminById(userId);
            case "StoreKeeper":
                return findStoreKeeperById(userId);
            //case "Assembler":
              //  return findAssemblerById(userId);
            default:
                return null;
        }
    }

    // Sous-méthodes spécifiques pour chaque type d'utilisateur par email
    private Requester findRequesterByEmail(String email) {
        UserInfo userInfo = findUserByEmail(email, "Requester");
        if (userInfo != null) {
            return new Requester(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getEmail(), userInfo.getPassword());
        }
        return null;
    }

    private Administrator findAdminByEmail(String email) {
        UserInfo userInfo = findUserByEmail(email, "Administrator");
        if (userInfo != null) {
            return new Administrator(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getEmail(), userInfo.getPassword());
        }
        return null;
    }

    private StoreKeeper findStoreKeeperByEmail(String email) {
        UserInfo userInfo = findUserByEmail(email, "StoreKeeper");
        if (userInfo != null) {
            return new StoreKeeper(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getEmail(), userInfo.getPassword());
        }
        return null;
    }
    /*

    private Assembler findAssemblerByEmail(String email) {
       UserInfo userInfo = findUserByEmail(email, "StoreKeeper");
        if (userInfo != null) {
            return new Assembler(userInfo.getFirstName(), userInfo.getLastName(), userInfo.getEmail(), userInfo.getPassword());
        }
        return null;
    }

     */

    // Sous-méthodes spécifiques pour chaque type d'utilisateur par ID
    private Requester findRequesterById(int userId) {
        return (Requester) findUserById(userId, "Requester");
    }

    private Administrator findAdminById(int userId) {
        return (Administrator) findUserById(userId, "Administrator");
    }

    private StoreKeeper findStoreKeeperById(int userId) {
        return (StoreKeeper) findUserById(userId, "StoreKeeper");
    }
     /*
    private Assembler findAssemblerById(int userId) {
        return (Assembler) findUserById(userId, "Assembler");
    }
    */

    // Méthode générique pour trouver un utilisateur par email et rôle
    public UserInfo findUserByEmail(String email) {
        return findUserByEmail(email, null);
    }
    public UserInfo findUserByEmail(String email, String role) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseSQLite.TABLE_USERS + " WHERE " + DatabaseSQLite.USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_ID));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_FIRSTNAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_LASTNAME));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_PASSWORD));
            String userRole = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_ROLE));
            cursor.close();

            if (role == null || role.equals(userRole)) {
                return new UserInfo(id, firstName, lastName, email, password, userRole);
            }
        }
        cursor.close();
        return null;
    }

    // Méthode générique pour trouver un utilisateur par ID et rôle
    public User findUserById(int userId, String role) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseSQLite.TABLE_USERS + " WHERE " + DatabaseSQLite.USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            String retrievedRole = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_ROLE));
            if (retrievedRole.equals(role)) {
                return createUserInstance(cursor);
            }
        }
        cursor.close();
        return null;
    }

    // Méthode pour créer une instance de l'utilisateur en fonction de son rôle
    private User createUserInstance(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_ID));
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_FIRSTNAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_LASTNAME));
        String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_EMAIL));
        String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_PASSWORD));
        String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_ROLE));

        switch (role) {
            case "Requester":
                return new Requester(firstName, lastName, email, password);
            case "Administrator":
                return new Administrator(firstName, lastName, email, password);
            case "StoreKeeper":
                return new StoreKeeper(firstName, lastName, email, password);
            //case "Assembler":
             //   return new Assembler(firstName, lastName, email, password);
            default:
                return null;
        }
    }

    public List<UserInfo> getAllUsers() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<UserInfo> users = new ArrayList<>();

        String[] projection = {
                DatabaseSQLite.USER_ID,
                DatabaseSQLite.USER_FIRSTNAME,
                DatabaseSQLite.USER_LASTNAME,
                DatabaseSQLite.USER_EMAIL,
                DatabaseSQLite.USER_PASSWORD,
                DatabaseSQLite.USER_ROLE
        };

        Cursor cursor = db.query(
                DatabaseSQLite.TABLE_USERS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_ID));
            String firstName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_FIRSTNAME));
            String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_LASTNAME));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_EMAIL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_PASSWORD));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseSQLite.USER_ROLE));

            UserInfo user = new UserInfo(id, firstName, lastName, email, password, role);
            users.add(user);
        }
        cursor.close();

        return users;
    }
}
