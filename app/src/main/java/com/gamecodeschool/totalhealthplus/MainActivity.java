package com.gamecodeschool.totalhealthplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        databaseHelper = new DatabaseHelper(this);
//        use db browser to view data but runnig again creates duplicates in db
//        databaseHelper.insertExercise("running", 67);
        databaseHelper.insertFood("Hardboiled Egg","Protein", 70, 50);

        result = databaseHelper.selectFoods();
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        Log.d("RESULT WAS: ", result);
    }




}