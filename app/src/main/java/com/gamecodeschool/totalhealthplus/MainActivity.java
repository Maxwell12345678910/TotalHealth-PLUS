package com.gamecodeschool.totalhealthplus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText usernameInputEDT, passwordInputEDT, firstNameInputEDT,
            lastNameInputEDT, ageInputEDT, weightInputEDT, heightInputEDT, usernameLoginInput, passwordLoginInput;
    private Button createUserButton1, createUserButton2, createUserButton3, loginButton;
    private DatabaseHelper databaseHelper;
    private boolean loginSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        databaseHelper = new DatabaseHelper(this);

        //Gets username and password views
        usernameLoginInput = (EditText) findViewById(R.id.usernameInput2);
        passwordLoginInput = (EditText) findViewById(R.id.passwordInput2);

        //Gets login button
        loginButton = (Button) findViewById(R.id.loginButton2);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets text from user inputs when login button is pressed
                String usernameCheck = usernameLoginInput.getText().toString();
                String passwordCheck = passwordLoginInput.getText().toString();
                loginSuccess = databaseHelper.checkUser(usernameCheck, passwordCheck);

                if (loginSuccess){
                    setContentView(R.layout.create_user_page1);

                    usernameInputEDT = (EditText) findViewById(R.id.usernameInput2);
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
                else {
                    //Toast.makeText(this, "Login failed, username or password incorrect", Toast.LENGTH_LONG).show();
                }

            }
        });



    }

}