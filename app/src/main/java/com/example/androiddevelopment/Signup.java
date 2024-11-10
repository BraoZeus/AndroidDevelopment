package com.example.androiddevelopment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

public class Signup extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextEmail;
    private Button buttonSignup;
    private MyDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        buttonSignup = findViewById(R.id.buttonSignup);
        databaseHelper = new MyDatabaseHelper(this);

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();
                String email = editTextEmail.getText().toString();

                if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("Signup", "Attempting to sign up with username: " + username + ", email: " + email); // Log the attempt
                    boolean isInserted = databaseHelper.insertUser(username, password, email);
                    if (isInserted) {
                        Toast.makeText(Signup.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this, Login.class));
                        finish();
                    } else {
                        Toast.makeText(Signup.this, "Signup failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}
