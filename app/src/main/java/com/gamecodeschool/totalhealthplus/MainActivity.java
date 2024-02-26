package com.gamecodeschool.totalhealthplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInputEDT, passwordInputEDT, firstNameInputEDT, lastNameInputEDT, ageInputEDT, weightInputEDT, heightInputEDT;
    private Button createUserButton1, createUserButton2, createUserButton3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_page1);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        usernameInputEDT = (EditText) findViewById(R.id.usernameInput);
        passwordInputEDT = (EditText) findViewById(R.id.passwordInput);

        createUserButton1 = (Button) findViewById(R.id.createUserButton1);

        createUserButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = usernameInputEDT.getText().toString();
                String passwordInput = passwordInputEDT.getText().toString();

                setContentView(R.layout.create_user_page2);
                firstNameInputEDT = (EditText) findViewById(R.id.firstnameInput);
                lastNameInputEDT = (EditText) findViewById(R.id.lastnameInput);

                String firstNameInput = firstNameInputEDT.getText().toString();
                String lastNameInput = lastNameInputEDT.getText().toString();

                createUserButton2 = (Button) findViewById(R.id.createUserButton2);

                createUserButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.create_user_page3);

                        ageInputEDT = (EditText) findViewById(R.id.ageInput);
                        weightInputEDT = (EditText) findViewById(R.id.weightInput);
                        heightInputEDT = (EditText) findViewById(R.id.heightInput);

                        createUserButton3 = (Button) findViewById(R.id.createUserButton3);

                        createUserButton3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
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
                });


            }
        });

    }

}