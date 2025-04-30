
package com.example.workoutlogger.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import com.example.workoutlogger.R;
import com.example.workoutlogger.database.WorkoutDbHelper;
import com.example.workoutlogger.models.Workout;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressActivity extends AppCompatActivity {

    private Spinner exerciseSpinner;
    private LineChart weightChart;
    private TextView personalBestText;
    private WorkoutDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Initialize components
        exerciseSpinner = findViewById(R.id.spinner_exercise);
        weightChart = findViewById(R.id.chart_weight_progress);
        personalBestText = findViewById(R.id.text_personal_best);
        dbHelper = new WorkoutDbHelper(this);

        // Set up chart
        setupChart();

        // Load exercises for spinner
        loadExercises();

        // Set spinner listener
        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedExercise = (String) parent.getItemAtPosition(position);
                loadProgressData(selectedExercise);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void setupChart() {
        // Style the chart
        weightChart.getDescription().setEnabled(false);
        weightChart.setDrawGridBackground(false);

        // X-Axis styling
        XAxis xAxis = weightChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

        // Y-Axis styling
        YAxis leftAxis = weightChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setAxisMinimum(0f);

        // Disable right axis
        weightChart.getAxisRight().setEnabled(false);
    }

    private void loadExercises() {
        // Get all workouts
        List<Workout> workouts = dbHelper.getAllWorkouts();

        // Extract unique exercise names
        Set<String> exercises = new HashSet<>();
        for (Workout workout : workouts) {
            exercises.add(workout.getExercise());
        }

        // Create adapter with exercise names
        List<String> exerciseList = new ArrayList<>(exercises);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, exerciseList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter
        exerciseSpinner.setAdapter(adapter);

        // Select first exercise if available
        if (!exerciseList.isEmpty()) {
            exerciseSpinner.setSelection(0);
        }
    }

    private void loadProgressData(String exerciseName) {
        // Get all workouts
        List<Workout> allWorkouts = dbHelper.getAllWorkouts();

        // Filter by exercise name
        List<Workout> exerciseWorkouts = new ArrayList<>();
        for (Workout workout : allWorkouts) {
            if (workout.getExercise().equals(exerciseName)) {
                exerciseWorkouts.add(workout);
            }
        }

        // If no data found
        if (exerciseWorkouts.isEmpty()) {
            weightChart.clear();
            personalBestText.setText("No data available");
            return;
        }

        // Sort workouts by date (assuming date is in yyyy-MM-dd format)
        Collections.sort(exerciseWorkouts, (w1, w2) -> w1.getDate().compareTo(w2.getDate()));

        // Prepare chart data
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        double maxWeight = 0;

        for (int i = 0; i < exerciseWorkouts.size(); i++) {
            Workout workout = exerciseWorkouts.get(i);
            entries.add(new Entry(i, (float) workout.getWeight()));
            labels.add(workout.getDate());

            // Update max weight
            if (workout.getWeight() > maxWeight) {
                maxWeight = workout.getWeight();
            }
        }

        // Update personal best text
        personalBestText.setText("Personal Best: " + maxWeight + " kg");

        // Create dataset
        LineDataSet dataSet = new LineDataSet(entries, exerciseName + " Progress");
        dataSet.setColor(Color.BLUE);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setValueTextSize(12f);

        // Create line data
        LineData lineData = new LineData(dataSet);

        // Set data to chart
        weightChart.setData(lineData);

        // Set X-axis labels
        weightChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));

        // Refresh chart
        weightChart.invalidate();
    }
}