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
    private List <Goal> goalEntries;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(Goal Goal);
    }

    public GoalAdapter(List <Goal> newGoalEntries, OnItemClickListener newListener){
        goalEntries = new ArrayList<>();

        for (Goal entry: newGoalEntries){
            goalEntries.add(entry);
        }
        this.listener = newListener;

        notifyDataSetChanged();
    }

    // Method to add a new top score record
    public void addGoal(Goal scoreToAdd) {
        goalEntries.add(scoreToAdd);
        notifyDataSetChanged(); // Notify adapter that dataset has changed
    }

    public void updateGoals(List<Goal> newScoreEntries) {
        goalEntries.clear();

        for (Goal entry : newScoreEntries){
            goalEntries.add(entry);
        }

        notifyDataSetChanged();
    }

    public void deleteGoal(Goal entryToRemove) {

        if (goalEntries.size() > 3){
            int positionRemoved = 0;

            for (Goal goal:goalEntries){

                if (goal == entryToRemove){
                    goalEntries.remove(entryToRemove);
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
        Goal goal = goalEntries.get(position);

        holder.bind(goal, listener);
        holder.dateView.setText(goal.getDate());

        //Foods
        if (goal.getCategory().equals("Foods")){
            holder.dateView.setBackgroundColor(Color.GREEN);

        }
        //Exercises
        else if (goal.getCategory().equals("Exercise")){
            holder.dateView.setBackgroundColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return goalEntries.size();
    }

    public class ListItemHolder extends RecyclerView.ViewHolder {
        TextView dateView;
        public ListItemHolder(View view){
            super(view);
            dateView = view.findViewById(R.id.dateListView);
        }

        public void bind(Goal goal, OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(goal));
        }
    }
}
