package com.gamecodeschool.totalhealthplus;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainDashboard extends Fragment {
    int setCalGoal = 2000;
    TextView curCalsDisp;
    TextView goalCalsDisp;
    int totalProgress = 0; // Track the total progress separately
    ProgressBar progressBar;
    Button setGoalButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_dashboard, container, false);


        //set up the progressbar and its text displays
        progressBar = view.findViewById(R.id.progBar);
        curCalsDisp = view.findViewById(R.id.curCalDisp); //left text
        goalCalsDisp = view.findViewById(R.id.calGoalDisp); // right text
        updateProgressBar(0);progressBar.setMax(setCalGoal);//init default vals
        goalCalsDisp.setText(String.valueOf(setCalGoal));


        //set up the button goalSetButton
        setGoalButton = view.findViewById(R.id.goalSetButton);
        setGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog();
            }
        });


        Button testButton = view.findViewById(R.id.tempEat);
        testButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateProgressBar2();
                    }
                }
        );



        return view;

    }


    //dont forget to also update the display for the new cur cals
    public void updateProgressBar(int increment) {

        totalProgress += increment; // Accumulate the progress
        progressBar.setProgress(totalProgress);
        curCalsDisp.setText(String.valueOf(totalProgress));
    }


    public void updateProgressBar2() {
        totalProgress+= 50; // Increment the total progress by 50
        progressBar.setProgress(totalProgress);
        curCalsDisp.setText(String.valueOf(totalProgress));
    }






    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Text");

        // Set up the input
        final EditText input = new EditText(getActivity());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String userInput = input.getText().toString().trim();
            if (isValidInput(userInput)) {
                setCalGoal = Integer.parseInt(userInput);
                progressBar.setMax(Integer.parseInt(userInput));
                goalCalsDisp.setText(userInput);
            } else {
                // if the user inputs a non-number then display error
                Toast.makeText(getActivity(), "Invalid input. Please enter a positive number.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) ->
                dialog.cancel()
        );

        builder.show();
    }

    // Method to validate user input
    private boolean isValidInput(String input) {
        // Check if the input is a positive number
        try {
            int number = Integer.parseInt(input);
            return number >= 0;
        } catch (NumberFormatException e) {
            return false; // If parsing fails, input is not a number
        }
    }

}