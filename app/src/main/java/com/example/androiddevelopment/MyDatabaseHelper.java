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
    private static final int DATABASE_VERSION = 2;  // Increment the version number

    // User table and columns
    public static final String TABLE_LOGIN = "login";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_EMAIL = "email";

    // Job table and columns
    public static final String TABLE_JOBS = "jobs";
    public static final String COLUMN_JOB_ID = "job_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";

    // Job registrations table and columns
    public static final String TABLE_JOB_REGISTRATIONS = "job_registrations";
    public static final String COLUMN_REGISTRATION_ID = "registration_id";
    public static final String COLUMN_USER_ID = "user_id"; // User ID for job registration
    public static final String COLUMN_JOB_ID_FK = "job_id"; // Job ID for registration

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

        // Create the jobs table
        String createJobsTable = "CREATE TABLE " + TABLE_JOBS + " (" +
                COLUMN_JOB_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT NOT NULL);";
        db.execSQL(createJobsTable);
        Log.d("Database", "Jobs table created");

        // Create the job registrations table
        String createJobRegistrationsTable = "CREATE TABLE " + TABLE_JOB_REGISTRATIONS + " (" +
                COLUMN_REGISTRATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_JOB_ID_FK + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_LOGIN + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_JOB_ID_FK + ") REFERENCES " + TABLE_JOBS + "(" + COLUMN_JOB_ID + "));";
        db.execSQL(createJobRegistrationsTable);
        Log.d("Database", "Job registrations table created");

        // Insert sample jobs
        insertSampleJobs(db);
    }

    @Override //Drops all tables if they exist and then recreate them. Typically called when the database version changes.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOB_REGISTRATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        onCreate(db);
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

    // Method to insert user data
    public boolean insertUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_LOGIN, null, contentValues);
        Log.d("Database", "Insert result: " + result + " for username: " + username);
        return result != -1; // returns true if data inserted successfully
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

    // Method to get user ID from username
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

    // Method to insert a job into the database
    public boolean insertJob(String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_TITLE, title);
        contentValues.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_JOBS, null, contentValues);
        Log.d("Database", "Insert result: " + result + " for job: " + title);
        return result != -1; // returns true if job inserted successfully
    }

    // Method to fetch all jobs from the database
    public Cursor getAllJobs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_JOBS, new String[]{COLUMN_JOB_ID, COLUMN_TITLE, COLUMN_DESCRIPTION},
                null, null, null, null, null);
    }

    // Method to insert sample jobs if no jobs exist
    private void insertSampleJobs(SQLiteDatabase db) {
        // Insert only if jobs table is empty
        Cursor cursor = db.query(TABLE_JOBS, new String[]{COLUMN_JOB_ID},
                null, null, null, null, null);
        if (cursor != null && cursor.getCount() == 0) {
            Log.d("Database", "Inserting sample jobs");

            // Insert sample jobs
            ContentValues contentValues = new ContentValues();
            contentValues.put(COLUMN_TITLE, "Software Engineer");
            contentValues.put(COLUMN_DESCRIPTION, "Develop and maintain software applications.");
            db.insert(TABLE_JOBS, null, contentValues);

            contentValues.put(COLUMN_TITLE, "Data Scientist");
            contentValues.put(COLUMN_DESCRIPTION, "Analyze and interpret complex data.");
            db.insert(TABLE_JOBS, null, contentValues);

            contentValues.put(COLUMN_TITLE, "Product Manager");
            contentValues.put(COLUMN_DESCRIPTION, "Oversee product development and strategy.");
            db.insert(TABLE_JOBS, null, contentValues);

            cursor.close(); // Close cursor after use
        }
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
