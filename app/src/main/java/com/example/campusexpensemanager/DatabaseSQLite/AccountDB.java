package com.example.campusexpensemanager.DatabaseSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "account.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMM_ID = "_id";
    public static final String COLUMM_FULLNAME = "fullname";
    public static final String COLUMM_STUDENTID = "studentid";
    public static final String COLUMM_EMAIL = "email";
    public static final String COLUMM_PASSWORD = "password";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMM_FULLNAME + " TEXT, " +
                    COLUMM_STUDENTID + " TEXT, " +
                    COLUMM_EMAIL + " TEXT, " +
                    COLUMM_PASSWORD + " TEXT " +
                    "); ";

    public AccountDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

}
