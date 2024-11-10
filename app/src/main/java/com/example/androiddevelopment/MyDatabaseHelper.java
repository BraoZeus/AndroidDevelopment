package com.example.androiddevelopment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "login_details.db";
    private static final int DATABASE_VERSION = 1;

    // User table and columns
    public static final String TABLE_LOGIN = "login";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";

    // Job registrations table and columns
    public static final String TABLE_JOB_REGISTRATIONS = "job_registrations";
    public static final String COLUMN_REGISTRATION_ID = "registration_id";
    public static final String COLUMN_USER_ID = "user_id"; // User ID for job registration
    public static final String COLUMN_JOB_ID = "job_id"; // Job ID for registration

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override

    public void onCreate(SQLiteDatabase db) {
        // Create the login table
        String createLoginTable = "CREATE TABLE " + TABLE_LOGIN + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL, " +
                COLUMN_EMAIL + " TEXT NOT NULL);";
        db.execSQL(createLoginTable);
        Log.d("Database", "Login table created");

        // Create the job registrations table
        String createJobRegistrationsTable = "CREATE TABLE " + TABLE_JOB_REGISTRATIONS + " (" +
                COLUMN_REGISTRATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_JOB_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_LOGIN + "(" + COLUMN_ID + "));";
        db.execSQL(createJobRegistrationsTable);
        Log.d("Database", "Job registrations table created");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_REGISTRATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
    }

    // Method to insert user data
    public boolean insertUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_LOGIN, null, contentValues);
        Log.d("Database", "Insert result: " + result + " for username: " + username); // Log the username
        return result != -1; // returns true if data inserted successfully
    }


    // Method to get user details by ID
    public Cursor getUserDetails(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LOGIN, new String[]{COLUMN_USERNAME, COLUMN_EMAIL},
                COLUMN_ID + "=?", new String[]{String.valueOf(userId)},
                null, null, null);
    }

    // Method to update user profile
    public boolean updateUserProfile(int userId, String username, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_EMAIL, email);
        int result = db.update(TABLE_LOGIN, contentValues, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
        return result > 0; // returns true if update was successful
    }

    // Method to check user credentials
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);

        boolean exists = false;
        if (cursor != null) {
            try {
                exists = cursor.moveToFirst(); // If it moves to the first, user exists
            } finally {
                cursor.close(); // Ensure the cursor is closed
            }
        }
        return exists; // Return whether the user exists
    }



    public int getUserId(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOGIN, new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null);

        int userId = -1; // Default to -1 indicating not found
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(COLUMN_ID);
                    if (columnIndex != -1) {
                        userId = cursor.getInt(columnIndex);
                    }
                }
            } finally {
                cursor.close(); // Ensure the cursor is closed in all scenarios
            }
        }
        return userId; // Return userId, -1 if not found
    }





    // Method to delete a user account
    public boolean deleteUserAccount(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            // First, delete any job registrations for this user
            db.delete(TABLE_JOB_REGISTRATIONS, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});

            // Now, delete the user account
            int result = db.delete(TABLE_LOGIN, COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            return result > 0; // returns true if delete was successful
        } catch (Exception e) {
            // Handle any potential errors
            return false;
        }
    }
}
