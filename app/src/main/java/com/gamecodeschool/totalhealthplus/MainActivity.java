package com.gamecodeschool.totalhealthplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;
    String result = "";

    private EditText usernameInputEDT, passwordInputEDT, firstNameInputEDT, lastNameInputEDT, ageInputEDT, weightInputEDT, heightInputEDT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_view);
        databaseHelper = new DatabaseHelper(this);
    }




}