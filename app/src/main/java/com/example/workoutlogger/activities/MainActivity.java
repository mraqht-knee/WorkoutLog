package com.example.workoutlogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import com.example.workoutlogger.R;
import com.example.workoutlogger.adapters.WorkoutAdapter;
import com.example.workoutlogger.database.WorkoutDbHelper;
import com.example.workoutlogger.models.Workout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutAdapter adapter;
    private WorkoutDbHelper dbHelper;
    private List<Workout> workoutList;


    private static final int ADD_WORKOUT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Initialize the database helper
        dbHelper = new WorkoutDbHelper(this);

        // Set up the RecyclerView
        recyclerView = findViewById(R.id.recycler_workouts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load workouts from database
        loadWorkouts();

        // Set up FAB for adding workouts
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddWorkoutActivity.class);
            startActivityForResult(intent, ADD_WORKOUT_REQUEST);
        });
    }

    // Load workouts from database
    private void loadWorkouts() {
        workoutList = dbHelper.getAllWorkouts();
        adapter = new WorkoutAdapter(workoutList);
        recyclerView.setAdapter(adapter);

        // Set up click listeners
        adapter.setOnItemClickListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Workout workout) {
                // For simplicity, just display some details in a toast
                Toast.makeText(MainActivity.this,
                        "Workout: " + workout.getName() + "\n" +
                                "Notes: " + workout.getNotes(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(Workout workout) {
                // Show delete confirmation dialog
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Workout")
                        .setMessage("Are you sure you want to delete this workout?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            // Delete the workout
                            dbHelper.deleteWorkout(workout.getId());
                            // Reload the list
                            loadWorkouts();
                            Toast.makeText(MainActivity.this, "Workout deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_WORKOUT_REQUEST && resultCode == RESULT_OK) {
            // Reload workouts
            loadWorkouts();
            Toast.makeText(this, "Workout saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_history:
                // Navigate to history activity
                Intent historyIntent = new Intent(this, WorkoutHistoryActivity.class);
                startActivity(historyIntent);
                return true;
            case R.id.menu_progress:
                // Navigate to progress activity
                Intent progressIntent = new Intent(this, ProgressActivity.class);
                startActivity(progressIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
