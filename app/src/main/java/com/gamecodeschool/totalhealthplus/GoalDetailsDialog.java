package com.gamecodeschool.totalhealthplus;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

public class GoalDetailsDialog extends DialogFragment {
    View dialogView;
    ListView goalsListView;
    CalorieGoal calorieGoal;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        dialogView = inflater.inflate(R.layout.prev_goal_details, null);
        goalsListView = dialogView.findViewById(R.id.goalsList);


        //Retrieve text views and set values
        TextView view = dialogView.findViewById(R.id.textView9);
        view.setText(calorieGoal.getDescription());

        builder.setView(dialogView).setMessage("Goal Details");

        Button btnOK = (Button) dialogView.findViewById(R.id.goalBtnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return builder.create();
    }

    public void setGoalEntry(CalorieGoal newCalorieGoalEntry){
        this.calorieGoal = newCalorieGoalEntry;
    }
}
