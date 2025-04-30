package com.example.workoutlogger.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.adapters.WorkoutAdapter;
import com.example.workoutlogger.database.WorkoutDbHelper;
import com.example.workoutlogger.models.Workout;

import java.util.List;

public class WorkoutHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private WorkoutDbHelper dbHelper;
    private List<Workout> workoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_history);

        // Initialize the database helper
        dbHelper = new WorkoutDbHelper(this);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load workouts
        loadWorkouts();

        // Set up back button in action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Workout History");
        }
    }

    private void loadWorkouts() {
        workoutList = dbHelper.getAllWorkouts();
        adapter = new WorkoutAdapter(workoutList);
        recyclerView.setAdapter(adapter);

        // Set click listeners
        adapter.setOnItemClickListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Workout workout) {
                // Show details in a toast
                Toast.makeText(WorkoutHistoryActivity.this,
                        "Workout: " + workout.getName() + "\n" +
                                "Exercise: " + workout.getExercise() + "\n" +
                                "Date: " + workout.getDate(),
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onItemLongClick(Workout workout) {
                // Show delete confirmation dialog
                new AlertDialog.Builder(WorkoutHistoryActivity.this)
                        .setTitle("Delete Workout")
                        .setMessage("Are you sure you want to delete this workout?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Delete the workout
                            dbHelper.deleteWorkout(workout.getId());
                            // Reload the list
                            loadWorkouts();
                            Toast.makeText(WorkoutHistoryActivity.this, "Workout deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}