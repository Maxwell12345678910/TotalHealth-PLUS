package com.gamecodeschool.totalhealthplus;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.ListItemHolder> {
    private List <CalorieGoal> calorieGoalEntries;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(CalorieGoal CalorieGoal);
    }

    public GoalAdapter(List <CalorieGoal> newCalorieGoalEntries, OnItemClickListener newListener){
        calorieGoalEntries = new ArrayList<>();

        for (CalorieGoal entry: newCalorieGoalEntries){
            calorieGoalEntries.add(entry);
        }
        this.listener = newListener;

        notifyDataSetChanged();
    }

    // Method to add a new top score record
    public void addGoal(CalorieGoal scoreToAdd) {
        calorieGoalEntries.add(scoreToAdd);
        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    public void updateGoals(List<CalorieGoal> newScoreEntries) {
        calorieGoalEntries.clear();

        for (CalorieGoal entry : newScoreEntries){
            calorieGoalEntries.add(entry);
        }

        notifyDataSetChanged();
    }

    public void deleteGoal(CalorieGoal entryToRemove) {

        if (calorieGoalEntries.size() > 3){
            int positionRemoved = 0;

            for (CalorieGoal calorieGoal : calorieGoalEntries){

                if (calorieGoal == entryToRemove){
                    calorieGoalEntries.remove(entryToRemove);
                    break;
                }
                positionRemoved++;
            }

            notifyItemRemoved(positionRemoved);
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public GoalAdapter.ListItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.goallistitem, parent, false);
        return new ListItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalAdapter.ListItemHolder holder, int position) {
        CalorieGoal calorieGoal = calorieGoalEntries.get(position);

        holder.bind(calorieGoal, listener);
        holder.dateView.setText(calorieGoal.getDate());


        //Foods
        if (calorieGoal.getCategory().equals("Foods")){
            holder.dateView.setBackgroundColor(Color.GREEN);

        }
        //Exercises
        else if (calorieGoal.getCategory().equals("Exercise")){
            holder.dateView.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return calorieGoalEntries.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {
        TextView dateView;
        public ListItemHolder(View view){
            super(view);
            dateView = view.findViewById(R.id.dateListView);
        }

        public void bind(CalorieGoal calorieGoal, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(calorieGoal));
        }
    }
}
