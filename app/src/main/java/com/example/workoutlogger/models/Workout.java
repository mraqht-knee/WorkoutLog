package com.example.workoutlogger.models;

// A simple model class to represent a workout
public class Workout {
    private int id;
    private String name;
    private String date;
    private String exercise;
    private int sets;
    private int reps;
    private double weight;
    private String notes;

    // Constructor with ID (used when reading from database)
    public Workout(int id, String name, String date, String exercise, int sets, int reps, double weight, String notes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.notes = notes;
    }

    // Constructor without ID (used when creating a new workout)
    public Workout(String name, String date, String exercise, int sets, int reps, double weight, String notes) {
        this.name = name;
        this.date = date;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.notes = notes;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}