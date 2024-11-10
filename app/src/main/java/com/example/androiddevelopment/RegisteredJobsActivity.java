package com.example.androiddevelopment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class RegisteredJobsActivity extends AppCompatActivity {

    private TextView registeredJobsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_jobs);

        registeredJobsTextView = findViewById(R.id.registeredJobsTextView);
        displayRegisteredJobs();
    }

    private void displayRegisteredJobs() {
        SharedPreferences preferences = getSharedPreferences("RegisteredJobs", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        StringBuilder jobsStringBuilder = new StringBuilder();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            jobsStringBuilder.append("Job Title: ").append(entry.getKey())
                    .append("\nDescription: ").append(entry.getValue()).append("\n\n");
        }

        registeredJobsTextView.setText(jobsStringBuilder.toString());
    }
}
