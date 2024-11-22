package com.example.androiddevelopment;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class RegisteredJobsActivity extends AppCompatActivity {

    private ListView registeredJobsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_jobs);

        registeredJobsListView = findViewById(R.id.registeredJobsListView);

        // Display registered jobs
        displayRegisteredJobs();

        registeredJobsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String jobTitle = (String) parent.getItemAtPosition(position);

                // Show job details and delete confirmation dialog
                showJobDetailsDialog(jobTitle);
            }
        });
    }

    private void displayRegisteredJobs() {
        SharedPreferences preferences = getSharedPreferences("RegisteredJobs", MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();

        // Create a list of job titles
        String[] jobTitles = new String[allEntries.size()];
        int index = 0;
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            jobTitles[index++] = entry.getKey();
        }

        // Set the adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, jobTitles);
        registeredJobsListView.setAdapter(adapter);
    }

    private void showJobDetailsDialog(String jobTitle) {
        SharedPreferences preferences = getSharedPreferences("RegisteredJobs", MODE_PRIVATE);
        String jobDescription = preferences.getString(jobTitle, "No Description Available");

        // Show job details in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(jobTitle)
                .setMessage(jobDescription)
                .setPositiveButton(R.string.delete_registration_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Show confirmation dialog
                        showConfirmationDialog(jobTitle);
                    }
                })
                .setNegativeButton(R.string.no_button, null)
                .show();
    }

    private void showConfirmationDialog(final String jobTitle) {
        // Show confirmation dialog to delete the registration
        new AlertDialog.Builder(this)
                .setTitle(R.string.confirmation_dialog_title)
                .setMessage(R.string.confirmation_dialog_message)
                .setPositiveButton(R.string.yes_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Delete the registered job from SharedPreferences
                        deleteRegisteredJob(jobTitle);
                    }
                })
                .setNegativeButton(R.string.no_button, null)
                .show();
    }

    private void deleteRegisteredJob(String jobTitle) {
        SharedPreferences preferences = getSharedPreferences("RegisteredJobs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(jobTitle);
        editor.apply();

        // Refresh the job list after deletion
        displayRegisteredJobs();
    }
}
