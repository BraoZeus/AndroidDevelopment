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
        super.onCreate(savedInstanceState);//call the superclass constructor
        setContentView(R.layout.activity_registration);//set the layout for the activity

        jobTitleTextView = findViewById(R.id.jobTitle);//find the TextViews in the layout
        jobDescriptionTextView = findViewById(R.id.jobDescription);//find the TextViews in the layout
        Button registerButton = findViewById(R.id.registerButton);//find the Button in the layout

        String jobTitle = getIntent().getStringExtra("jobTitle");//retrieve the intent which started the activity
        String jobDescription = getIntent().getStringExtra("jobDescription");//retrieve the intent which started the activity

        jobTitleTextView.setText(jobTitle);//set the text of the TextViews
        jobDescriptionTextView.setText(jobDescription);//set the text of the TextViews

        // Save the job to SharedPreferences when the button is clicked
        registerButton.setOnClickListener(view -> {
            SharedPreferences preferences = getSharedPreferences("RegisteredJobs", MODE_PRIVATE);//retrieves or creates the SharedPreferences object named "RegisteredJobs"
            SharedPreferences.Editor editor = preferences.edit();//creates an editor object to modify the SharedPreferences
            editor.putString(jobTitle, jobDescription);
            editor.apply();
            finish(); // Close this activity after registering
        });
    }
}
