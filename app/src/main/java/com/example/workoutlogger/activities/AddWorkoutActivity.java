package com.example.workoutlogger.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.workoutlogger.R;
import com.example.workoutlogger.database.WorkoutDbHelper;
import com.example.workoutlogger.models.Workout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddWorkoutActivity extends AppCompatActivity {

    private EditText editWorkoutName, editExercise, editSets, editReps, editWeight, editNotes;
    private TextView textDate;
    private Button buttonChangeDate, buttonSave;
    private WorkoutDbHelper dbHelper;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_workout);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Initialize UI elements
        editWorkoutName = findViewById(R.id.edit_workout_name);
        editExercise = findViewById(R.id.edit_exercise);
        editSets = findViewById(R.id.edit_sets);
        editReps = findViewById(R.id.edit_reps);
        editWeight = findViewById(R.id.edit_weight);
        editNotes = findViewById(R.id.edit_notes);
        textDate = findViewById(R.id.text_date);
        buttonChangeDate = findViewById(R.id.button_change_date);
        buttonSave = findViewById(R.id.button_save);

        // Initialize database helper
        dbHelper = new WorkoutDbHelper(this);

        // Set up date
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        updateDateText();

        // Set up date picker button
        buttonChangeDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    AddWorkoutActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateText();
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

        // Set up save button
        buttonSave.setOnClickListener(v -> saveWorkout());
    }

    private void updateDateText() {
        String formattedDate = dateFormat.format(calendar.getTime());
        textDate.setText("Date: " + formattedDate);
    }

    private void saveWorkout() {
        // Get input values
        String workoutName = editWorkoutName.getText().toString().trim();
        String exercise = editExercise.getText().toString().trim();
        String notesText = editNotes.getText().toString().trim();

        // Validate inputs
        if (workoutName.isEmpty() || exercise.isEmpty()) {
            Toast.makeText(this, "Please fill in workout name and exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse sets, reps, and weight
        int sets, reps;
        double weight;

        try {
            sets = Integer.parseInt(editSets.getText().toString().trim());
            reps = Integer.parseInt(editReps.getText().toString().trim());
            weight = Double.parseDouble(editWeight.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for sets, reps, and weight", Toast.LENGTH_SHORT).show();
            return;
        }

        // Format date as string
        String date = dateFormat.format(calendar.getTime());

        // Create workout object
        Workout workout = new Workout(
                workoutName,
                date,
                exercise,
                sets,
                reps,
                weight,
                notesText
        );

        // Save to database
        long id = dbHelper.addWorkout(workout);

        if (id > 0) {
            // Set result and finish
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Failed to save workout", Toast.LENGTH_SHORT).show();
        }
    }
}
