package com.example.androiddevelopment;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements JobAdapter.OnJobClickListener {

    private RecyclerView recyclerView;
    private JobAdapter jobAdapter;
    private List<Job> jobList;
    private int userId; // Variable to hold user ID
    private MyDatabaseHelper databaseHelper; // Instance of the database helper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database helper
        databaseHelper = new MyDatabaseHelper(this);

        // Get user ID from intent
        userId = getIntent().getIntExtra("userId", -1); // Ensure the userId is passed

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch job list from database
        jobList = new ArrayList<>();

        try {
            Cursor cursor = databaseHelper.getAllJobs(); // Fetching job data

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int titleColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_TITLE);
                    int descriptionColumnIndex = cursor.getColumnIndex(MyDatabaseHelper.COLUMN_DESCRIPTION);

                    // Debugging log
                    Log.d("MainActivity", "Title Column Index: " + titleColumnIndex);
                    Log.d("MainActivity", "Description Column Index: " + descriptionColumnIndex);

                    // Ensure column indices are valid
                    if (titleColumnIndex != -1 && descriptionColumnIndex != -1) {
                        String jobTitle = cursor.getString(titleColumnIndex);
                        String jobDescription = cursor.getString(descriptionColumnIndex);
                        jobList.add(new Job(jobTitle, jobDescription));
                    } else {
                        Log.e("MainActivity", "One or more columns not found in the cursor");
                    }
                } while (cursor.moveToNext());

                cursor.close(); // Don't forget to close the cursor
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error fetching jobs from database", e);
        }

        jobAdapter = new JobAdapter(jobList, this);
        recyclerView.setAdapter(jobAdapter);

        Button buttonProfile = findViewById(R.id.buttonProfile);
        buttonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId); // Pass user ID to ProfileActivity
                startActivity(intent);
            }
        });

        // Set up button to view registered jobs
        Button viewRegisteredJobsButton = findViewById(R.id.viewRegisteredJobsButton);
        viewRegisteredJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisteredJobsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onJobClick(Job job) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        intent.putExtra("jobTitle", job.getTitle());
        intent.putExtra("jobDescription", job.getDescription());
        startActivity(intent);
    }
}
