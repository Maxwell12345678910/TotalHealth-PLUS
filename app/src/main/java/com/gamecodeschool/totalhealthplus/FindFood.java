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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FoodBrowse#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FindFood extends Fragment {

    private TableLayout FindMyFoods;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String newKeyword = MainActivity.Keyword;

    public FindFood() {
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
    // TODO: Rename and change types and number of parameters
    public static FindFood newInstance(String param1, String param2) {
        FindFood fragment = new FindFood();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static boolean containsCharacters(String input, String characters) {

        if (input == null || characters == null) {
            return false;
        }

        // Convert the input string and characters string to lowercase for case-insensitive comparison
        input = input.toLowerCase();
        Log.d(TAG, "Text sent: " + input);
        characters = characters.toLowerCase();
        Log.d(TAG, "Text sent: " + characters);

        // Iterate through each character in the characters string
        for (int i = 0; i < characters.length(); i++) {
            char c = characters.charAt(i);
            // Check if the input string contains the current character
            if (input.indexOf(c) == -1) {
                return false; // Return false if character not found
            }
        }
        return true; // Return true if all characters are found
    }

    public void FindFoods() {
        Cursor cursor = databaseHelper.selectFoods();

        while (cursor.moveToNext()) {

            @SuppressLint("Range") int foodID = cursor.getInt(cursor.getColumnIndex("foodID"));
            @SuppressLint("Range") String foodDescription = cursor.getString(cursor.getColumnIndex("FoodDescription"));
            @SuppressLint("Range") String foodCategory = cursor.getString(cursor.getColumnIndex("FoodCategory"));
            @SuppressLint("Range") int calsPerServing = cursor.getInt(cursor.getColumnIndex("CaloriesPerServing"));
            @SuppressLint("Range") float weightPerServ = cursor.getInt(cursor.getColumnIndex("WeightPerServingInGrams"));

            if (containsCharacters(foodDescription.toString(), newKeyword)) {
                TableRow newRow = new TableRow(requireContext());
                newRow.setWeightSum(1);

                LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,  // Width
                        ViewGroup.LayoutParams.WRAP_CONTENT,   // Height
                        0.25f
                );


            TextView descView = new TextView(this.requireContext());
            descView.setText(foodDescription);
            descView.setLayoutParams(textViewParams);

            TextView catView = new TextView(this.requireContext());
            catView.setText(foodCategory);
            catView.setLayoutParams(textViewParams);

            TextView calsView = new TextView(this.requireContext());
            calsView.setText("" + calsPerServing);
            calsView.setLayoutParams(textViewParams);

            TextView weightView = new TextView(this.requireContext());
            weightView.setText("" + weightPerServ);
            weightView.setLayoutParams(textViewParams);

            newRow.addView(descView);
            newRow.addView(catView);
            newRow.addView(calsView);
            newRow.addView(weightView);

            FindMyFoods.addView(newRow);

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
        View rootView = inflater.inflate(R.layout.food_add, container, false);

        FindMyFoods = (TableLayout) rootView.findViewById(R.id.FindFoods2);
        //FindFoods();

        return rootView;
    }
}
