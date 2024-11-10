package com.example.androiddevelopment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;


import androidx.appcompat.app.AppCompatActivity;
public class ProfileActivity extends AppCompatActivity {

    private TextView textViewName, textViewEmail;
    private Button buttonEdit, buttonDelete;
    private MyDatabaseHelper databaseHelper;
    private int userId; // User ID will be retrieved from intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);

        databaseHelper = new MyDatabaseHelper(this);

        // Get userId from intent
        userId = getIntent().getIntExtra("userId", -1);

        if (userId != -1) {
            loadUserProfile(); // Load user data if userId is valid
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("ProfileActivity", "Error starting EditProfileActivity", e);
                    Toast.makeText(ProfileActivity.this, "Error opening edit profile", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserAccount(); // Delete account
            }
        });
    }

    private void loadUserProfile() {
        Cursor cursor = databaseHelper.getUserDetails(userId);
        if (cursor != null) {
            // Ensure cursor has data and move to first
            if (cursor.moveToFirst()) {
                int usernameIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_USERNAME);
                int emailIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_EMAIL);

                // Check for valid column indices
                if (usernameIndex != -1 && emailIndex != -1) {
                    String username = cursor.getString(usernameIndex);
                    String email = cursor.getString(emailIndex);
                    textViewName.setText(username);
                    textViewEmail.setText(email);
                } else {
                    Toast.makeText(this, "Error: Columns not found in database.", Toast.LENGTH_SHORT).show();
                }
            }
            cursor.close();
        } else {
            Toast.makeText(this, "Error: Unable to retrieve user data.", Toast.LENGTH_SHORT).show();
        }
    }


    private void deleteUserAccount() {
        if (databaseHelper.deleteUserAccount(userId)) {
            Toast.makeText(this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, Login.class); // Change LoginActivity to your actual login activity
            startActivity(intent);
            finish(); // Close ProfileActivity
        } else {
            Toast.makeText(this, "Failed to delete account", Toast.LENGTH_SHORT).show();
        }
    }

}

