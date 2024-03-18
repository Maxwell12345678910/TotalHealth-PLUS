package com.gamecodeschool.totalhealthplus;

import static com.gamecodeschool.totalhealthplus.MainActivity.databaseHelper;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowseFood#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowseFood extends Fragment {

    private TableLayout browseFoodsTable;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrowseFood() {
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
    public static BrowseFood newInstance(String param1, String param2) {
        BrowseFood fragment = new BrowseFood();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void showFoods(){
        Cursor cursor = databaseHelper.selectFoods();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int foodID = cursor.getInt(cursor.getColumnIndex("foodID"));
            @SuppressLint("Range") String foodDescription = cursor.getString(cursor.getColumnIndex("FoodDescription"));
            @SuppressLint("Range") String foodCategory = cursor.getString(cursor.getColumnIndex("FoodCategory"));
            @SuppressLint("Range") int calsPerServing = cursor.getInt(cursor.getColumnIndex("CaloriesPerServing"));
            @SuppressLint("Range") float weightPerServ = cursor.getInt(cursor.getColumnIndex("WeightPerServingInGrams"));


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

            browseFoodsTable.addView(newRow);

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
        View rootView = inflater.inflate(R.layout.fragment_browse_food, container, false);

        browseFoodsTable = (TableLayout) rootView.findViewById(R.id.browseFoodsTable);
        showFoods();

        return rootView;
    }
}