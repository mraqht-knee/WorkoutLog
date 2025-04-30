# Workout Logger

A simple Android application for tracking workout sessions. This app allows users to log exercises with details such as sets, reps, and weight, as well as view their workout history.

## Features

- Add new workouts with exercise details
- Track sets, reps, and weights for each exercise
- View workout history
- Delete previous workouts
- Track progress with graphs and visualizations
- Simple and intuitive user interface

## Screenshots

![image](https://github.com/user-attachments/assets/7bcce15b-48f7-4479-a2f9-ea56dabc1068)

## Technical Details

- **Platform:** Android
- **Minimum SDK:** Android 7.1 (API Level 25)
- **Programming Language:** Java
- **Database:** SQLite
- **Architecture:** Simple MVC pattern with SQLiteOpenHelper

## Project Structure

The app is structured with the following components:

- **Activities:**
  - MainActivity - Home screen with recent workouts
  - AddWorkoutActivity - Form to add new workouts
  - WorkoutHistoryActivity - Complete workout history

- **Database:**
  - WorkoutDbHelper - Manages database operations
  - WorkoutContract - Defines database schema

- **Models:**
  - Workout - Represents a workout session with exercise details

## Installation

1. Clone this repository
2. Open the project in Android Studio
3. Build and run on a device or emulator with API level 25+

## Future Improvements

- Add workout templates
- Add user profiles
- Add support for multiple exercises per workout
- Implement backup and restore functionality


