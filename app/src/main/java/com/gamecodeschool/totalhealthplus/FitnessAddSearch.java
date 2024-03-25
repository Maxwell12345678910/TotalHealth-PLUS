package com.gamecodeschool.totalhealthplus;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.gamecodeschool.totalhealthplus.MainActivity.databaseHelper;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodBrowse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FitnessAddSearch extends Fragment {

    private TableLayout FindMyFitness;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String newKeyword = MainActivity.FitnessKeyword;

    public FitnessAddSearch() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrowseFood.
     */
    // TODO: Rename and change types and number
    //  of parameters
    public static FitnessAddSearch newInstance(String param1, String param2) {
        FitnessAddSearch fragment = new FitnessAddSearch();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static boolean containsCharacters(String input, String characters) {
        ArrayList<String> words = new ArrayList<>();
        String curString = "";

        for (char c : input.toCharArray()) {
            if (c != ' ') {
                curString += c;
            }
            else{
                words.add(curString);
                curString = "";
            }
        }

        words.add(curString);

        for(int i = 0; i < words.size(); i++){
            if (words.get(i).contains(characters)){
                return true;
            }
        }

        return false;
    }

    public void FindFitness() {
        Cursor cursor = databaseHelper.selectExercise();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int exerciseID = cursor.getInt(cursor.getColumnIndex("ExerciseID"));
            @SuppressLint("Range") String exerciseDescription = cursor.getString(cursor.getColumnIndex("ExerciseDescription"));
            @SuppressLint("Range") float calsPerMinute = cursor.getInt(cursor.getColumnIndex("CalsBurnedPerMin"));


            if (containsCharacters(exerciseDescription, newKeyword)) {
                TableRow newRow = new TableRow(requireContext());
                newRow.setWeightSum(1);

                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,  // Width
                        ViewGroup.LayoutParams.WRAP_CONTENT,   // Height
                        0.5f
                );

                TextView descView = new TextView(this.requireContext());
                descView.setText(exerciseDescription);
                descView.setLayoutParams(textViewParams);

                TextView calsView = new TextView(this.requireContext());
                calsView.setText("" + calsPerMinute);
                calsView.setLayoutParams(textViewParams);

                newRow.addView(descView);
                newRow.addView(calsView);


                FindMyFitness.addView(newRow);

            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fitness_add, container, false);

        FindMyFitness = (TableLayout) rootView.findViewById(R.id.FindFitness2);
        //FindFoods();

        return rootView;
    }
}
