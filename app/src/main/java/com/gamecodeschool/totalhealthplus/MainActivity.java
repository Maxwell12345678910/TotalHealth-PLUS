package com.gamecodeschool.totalhealthplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInputEDT, passwordInputEDT, firstNameInputEDT, lastNameInputEDT, ageInputEDT, weightInputEDT, heightInputEDT;
    private Button createUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testloginpage);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        usernameInputEDT = (EditText) findViewById(R.id.usernameInput);
        passwordInputEDT = (EditText) findViewById(R.id.passwordInput);
        firstNameInputEDT = (EditText) findViewById(R.id.firstnameInput);
        lastNameInputEDT = (EditText) findViewById(R.id.lastnameInput);
        ageInputEDT = (EditText) findViewById(R.id.ageInput);
        weightInputEDT = (EditText) findViewById(R.id.weightInput);
        heightInputEDT = (EditText) findViewById(R.id.heightInput);

        createUserButton = (Button) findViewById(R.id.button);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = usernameInputEDT.getText().toString();
                String passwordInput = passwordInputEDT.getText().toString();
                String firstNameInput = firstNameInputEDT.getText().toString();
                String lastNameInput = lastNameInputEDT.getText().toString();

                String ageInputString = ageInputEDT.getText().toString();
                int ageInput = Integer.parseInt(ageInputString);
                String weightInputString = weightInputEDT.getText().toString();
                int weightInput = Integer.parseInt(weightInputString);
                String heightInputString = heightInputEDT.getText().toString();
                int heightInput = Integer.parseInt(heightInputString);

                databaseHelper.insertUser(usernameInput, passwordInput, firstNameInput, lastNameInput, ageInput, weightInput, heightInput);
            }
        });
    }
}