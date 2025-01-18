package com.example.pcorderapplication.model.database.tools;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pcorderapplication.model.entity.UserInfo;

public class DatabaseSQLite extends SQLiteOpenHelper {

    // Nom de la base de données et version
    private static final String DATABASE_NAME = "pcorderapp.db";
    private static final int DATABASE_VERSION = 1;

    // Nom des tables
    public static final String TABLE_USERS = "users";
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_COMPONENTS = "components";

    // Colonnes de la table users
    public static final String USER_ID = "id";
    public static final String USER_FIRSTNAME = "first_name";
    public static final String USER_LASTNAME = "last_name";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_ROLE = "role";
    public static final String USER_CREATED_AT = "created_at";
    public static final String USER_MODIFIED_AT = "modified_at";

    // Colonnes de la table orders (simplifiée, à ajuster selon la structure des commandes)
    public static final String ORDER_ID = "id";
    public static final String ORDER_STATUS = "status";
    public static final String ORDER_USER_ID = "user_id";
    public static final String ORDER_CREATED_AT = "creation_date";
    public static final String ORDER_MODIFIED_AT = "modification_date";

    // Colonnes de la table components (simplifiée)
    public static final String COMPONENT_TYPE = "type";
    public static final String COMPONENT_SUBTYPE = "subtype";
    public static final String COMPONENT_TITLE = "title";
    public static final String COMPONENT_COMMENT = "comment";
    public static final String COMPONENT_QUANTITY = "quantity";
    public static final String COMPONENT_CREATION_DATE = "creation_date";
    public static final String COMPONENT_MODIFICATION_DATE = "modification_date";




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

    // Constructeur
    public DatabaseSQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Création des tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_FIRSTNAME + " TEXT,"
                + USER_LASTNAME + " TEXT,"
                + USER_EMAIL + " TEXT UNIQUE,"
                + USER_PASSWORD + " TEXT,"
                + USER_ROLE + " TEXT,"
                + USER_CREATED_AT + " TEXT,"
                + USER_MODIFIED_AT + " TEXT"
                + ")";

        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_ORDERS_TABLE = "CREATE TABLE " + TABLE_ORDERS + "("
                + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ORDER_STATUS + " TEXT,"
                + ORDER_USER_ID + " INTEGER,"
                + ORDER_CREATED_AT + " TEXT,"
                + ORDER_MODIFIED_AT + " TEXT,"
                + "FOREIGN KEY(" + ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + USER_ID + ")"
                + ")";
        db.execSQL(CREATE_ORDERS_TABLE);

        String CREATE_COMPONENTS_TABLE = "CREATE TABLE " + TABLE_COMPONENTS + "("
                + COMPONENT_TITLE + " TEXT PRIMARY KEY,"
                + COMPONENT_TYPE + " TEXT,"
                + COMPONENT_SUBTYPE + " TEXT,"
                + COMPONENT_QUANTITY + " INTEGER,"
                + COMPONENT_COMMENT + " TEXT,"
                + COMPONENT_CREATION_DATE + " TEXT,"
                + COMPONENT_MODIFICATION_DATE + " TEXT"
                + ")";

        db.execSQL(CREATE_COMPONENTS_TABLE);

        Log.i("DatabaseSQLite", "Tables created successfully.");
    }

    // Méthode appelée lors de la mise à jour de la base de données
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Supprimer les tables existantes si elles existent
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPONENTS);
        // Recréer les tables
        onCreate(db);
    }


}
