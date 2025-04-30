package com.example.workoutlogger.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.workoutlogger.R;
import com.example.workoutlogger.models.Workout;

import java.util.List;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {

    private List<Workout> workoutList;
    private OnItemClickListener listener;

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(Workout workout);
        void onItemLongClick(Workout workout);
    }

    // Constructor
    public WorkoutAdapter(List<Workout> workoutList) {
        this.workoutList = workoutList;
    }

    // Set click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        // Get workout at this position
        Workout workout = workoutList.get(position);

        // Set the workout data to the views
        holder.textWorkoutName.setText(workout.getName());
        holder.textDate.setText(workout.getDate());
        holder.textExercise.setText(workout.getExercise());

        // Format the sets, reps, and weight into a string
        String details = workout.getSets() + " sets × " +
                workout.getReps() + " reps × " +
                workout.getWeight() + " kg";
        holder.textDetails.setText(details);
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    // Update the data in the adapter
    public void updateWorkouts(List<Workout> newWorkouts) {
        this.workoutList = newWorkouts;
        notifyDataSetChanged();
    }

    // ViewHolder class
    public class WorkoutViewHolder extends RecyclerView.ViewHolder {
        public TextView textWorkoutName, textDate, textExercise, textDetails;

        public WorkoutViewHolder(@NonNull View itemView) {
            super(itemView);
            textWorkoutName = itemView.findViewById(R.id.text_workout_name);
            textDate = itemView.findViewById(R.id.text_date);
            textExercise = itemView.findViewById(R.id.text_exercise);
            textDetails = itemView.findViewById(R.id.text_details);

            // Set click listeners
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(workoutList.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemLongClick(workoutList.get(position));
                    return true;
                }
                return false;
            });
        }
    }
}