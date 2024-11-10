package com.example.androiddevelopment;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user ID from intent
        userId = getIntent().getIntExtra("userId", -1); // Ensure the userId is passed

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a list of dummy jobs
        jobList = new ArrayList<>();
        jobList.add(new Job("Software Engineer", "Develop and maintain software applications."));
        jobList.add(new Job("Data Scientist", "Analyze and interpret complex data."));
        jobList.add(new Job("Product Manager", "Oversee product development and strategy."));

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

