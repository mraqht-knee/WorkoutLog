package com.example.workoutlogger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.workoutlogger.models.Workout;

import java.util.ArrayList;
import java.util.List;

public class WorkoutDbHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "workout_logger.db";

    // SQL command to create the table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + WorkoutContract.WorkoutEntry.TABLE_NAME + " (" +
                    WorkoutContract.WorkoutEntry._ID + " INTEGER PRIMARY KEY," +
                    WorkoutContract.WorkoutEntry.COLUMN_NAME + " TEXT," +
                    WorkoutContract.WorkoutEntry.COLUMN_DATE + " TEXT," +
                    WorkoutContract.WorkoutEntry.COLUMN_EXERCISE + " TEXT," +
                    WorkoutContract.WorkoutEntry.COLUMN_SETS + " INTEGER," +
                    WorkoutContract.WorkoutEntry.COLUMN_REPS + " INTEGER," +
                    WorkoutContract.WorkoutEntry.COLUMN_WEIGHT + " REAL," +
                    WorkoutContract.WorkoutEntry.COLUMN_NOTES + " TEXT)";

    // SQL command to delete the table
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WorkoutContract.WorkoutEntry.TABLE_NAME;

    public WorkoutDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy
        // is to simply discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    // Method to add a new workout to the database
    public long addWorkout(Workout workout) {
        // Get the database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(WorkoutContract.WorkoutEntry.COLUMN_NAME, workout.getName());
        values.put(WorkoutContract.WorkoutEntry.COLUMN_DATE, workout.getDate());
        values.put(WorkoutContract.WorkoutEntry.COLUMN_EXERCISE, workout.getExercise());
        values.put(WorkoutContract.WorkoutEntry.COLUMN_SETS, workout.getSets());
        values.put(WorkoutContract.WorkoutEntry.COLUMN_REPS, workout.getReps());
        values.put(WorkoutContract.WorkoutEntry.COLUMN_WEIGHT, workout.getWeight());
        values.put(WorkoutContract.WorkoutEntry.COLUMN_NOTES, workout.getNotes());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(WorkoutContract.WorkoutEntry.TABLE_NAME, null, values);

        // Close the database connection
        db.close();

        return newRowId;
    }

    // Method to retrieve all workouts from the database
    public List<Workout> getAllWorkouts() {
        List<Workout> workoutList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + WorkoutContract.WorkoutEntry.TABLE_NAME +
                " ORDER BY " + WorkoutContract.WorkoutEntry.COLUMN_DATE + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list
        if (cursor.moveToFirst()) {
            do {
                Workout workout = new Workout(
                        cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry._ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.COLUMN_EXERCISE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.COLUMN_SETS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.COLUMN_REPS)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.COLUMN_WEIGHT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(WorkoutContract.WorkoutEntry.COLUMN_NOTES))
                );
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        cursor.close();
        db.close();

        return workoutList;
    }

    // Method to delete a workout from the database
    public void deleteWorkout(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(WorkoutContract.WorkoutEntry.TABLE_NAME,
                WorkoutContract.WorkoutEntry._ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }
}