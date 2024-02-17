package com.gamecodeschool.totalhealthplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String result = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        databaseHelper = new DatabaseHelper(this);

        databaseHelper.deleteFood("Apple");
        result = databaseHelper.selectFoods();
        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
    }




}