package com.example.androiddevelopment;

import android.content.Intent;
import android.database.Cursor; // Import Cursor
import android.os.Bundle;
import android.util.Log; // Import Log
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail;
    private Button buttonSave;
    private MyDatabaseHelper databaseHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile); // layout file

        // Initialize views
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSave = findViewById(R.id.buttonSave);

        databaseHelper = new MyDatabaseHelper(this);
        userId = getIntent().getIntExtra("userId", -1); // Get userId from intent

        loadUserProfile();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void loadUserProfile() {
        Cursor cursor = databaseHelper.getUserDetails(userId);
        if (cursor != null) {
            int usernameIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_USERNAME);
            int emailIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_EMAIL);

            if (usernameIndex == -1 || emailIndex == -1) {
                Log.e("EditProfileActivity", "Column not found");
                Toast.makeText(this, "Error retrieving user details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cursor.moveToFirst()) {
                String username = cursor.getString(usernameIndex);
                String email = cursor.getString(emailIndex);
                editTextName.setText(username);
                editTextEmail.setText(email);
            }
            cursor.close();
        }
    }

    private void saveUserProfile() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();

        if (databaseHelper.updateUserProfile(userId, name, email)) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity and return to ProfileActivity
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }
}
