package com.example.androiddevelopment; // Ensure this matches your package structure

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    private TextView jobTitleTextView;
    private TextView jobDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration); // Ensure this matches your XML layout

        jobTitleTextView = findViewById(R.id.jobTitle);
        jobDescriptionTextView = findViewById(R.id.jobDescription);
        Button registerButton = findViewById(R.id.registerButton);

        String jobTitle = getIntent().getStringExtra("jobTitle");
        String jobDescription = getIntent().getStringExtra("jobDescription");

        jobTitleTextView.setText(jobTitle);
        jobDescriptionTextView.setText(jobDescription);

        // Save the job to SharedPreferences when the button is clicked
        registerButton.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("RegisteredJobs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(jobTitle, jobDescription);
            editor.apply();
            finish(); // Close this activity after registering
        });
    }
}
