package com.example.workoutlogger.database;

import android.provider.BaseColumns;

// Define the database schema in a contract class
public final class WorkoutContract {

    // Private constructor to prevent instantiation
    private WorkoutContract() {}

    // Inner class that defines the table contents
    public static class WorkoutEntry implements BaseColumns {
        public static final String TABLE_NAME = "workout";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_EXERCISE = "exercise";
        public static final String COLUMN_SETS = "sets";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_NOTES = "notes";
    }
}