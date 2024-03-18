package com.gamecodeschool.totalhealthplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private EditText usernameInputEDT, passwordInputEDT, firstNameInputEDT,
            lastNameInputEDT, ageInputEDT, weightInputEDT, heightInputEDT, usernameLoginInput, passwordLoginInput;
    private Button createUserButton1, createUserButton2, createUserButton3, loginButton, signUpButton;
    public static DatabaseHelper databaseHelper;
    private boolean loginSuccess;

    private TableLayout browseFoodsTable, browseExerciseTable;


    String result = "";

    private BottomNavigationView bottomNavigationView;
    private SparseArray<Fragment> fragmentMap = new SparseArray<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_page);

        databaseHelper = new DatabaseHelper(this);

        initializeLogin();
    }

    public void calGoal(View v){
        setContentView(R.layout.fragment_add_to_food);
    }
    public void BFood(View v){

        setContentView(R.layout.fragment_browse_food);
        showFoods();

    }
    public void BExercise(View v) {
        setContentView(R.layout.fragment_browse_exercise);
        showExercise();
    }
    public void PreGoals(View v){
        setContentView(R.layout.fragment_past_goals);
    }
    public void backToMain(View v){

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        // Initialize fragments
        fragmentMap.put(R.id.person, new FirstFragment());
        fragmentMap.put(R.id.home, new SecondFragment());
        fragmentMap.put(R.id.settings, new ThirdFragment());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, fragmentMap.get(R.id.home))
                .commit();
    }

    public void createUser(){
        setContentView(R.layout.create_user_page1);
        //Initialize username and password edit texts to be able to grab input
        usernameInputEDT = (EditText) findViewById(R.id.usernameInput2);
        passwordInputEDT = (EditText) findViewById(R.id.passwordInput);

        //Initialize create user button 1
        createUserButton1 = (Button) findViewById(R.id.createUserButton1);

        createUserButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pull in the username and password input creation before changing views to next screen
                String usernameInput = usernameInputEDT.getText().toString();
                String passwordInput = passwordInputEDT.getText().toString();

                setContentView(R.layout.create_user_page2);

                //Initialize firstName and lastName edit texts to grab input later
                firstNameInputEDT = (EditText) findViewById(R.id.firstnameInput);
                lastNameInputEDT = (EditText) findViewById(R.id.lastnameInput);

                createUserButton2 = (Button) findViewById(R.id.createUserButton2);

                createUserButton2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.create_user_page3);

                        //Grab firstName and lastName input
                        String firstNameInput = firstNameInputEDT.getText().toString();
                        String lastNameInput = lastNameInputEDT.getText().toString();

                        //Initialize edit texts for age, weight, height
                        ageInputEDT = (EditText) findViewById(R.id.ageInput);
                        weightInputEDT = (EditText) findViewById(R.id.weightInput);
                        heightInputEDT = (EditText) findViewById(R.id.heightInput);

                        createUserButton3 = (Button) findViewById(R.id.createUserButton3);


                        createUserButton3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Grab user input for age, weight, height and parse to integer before
                                //adding data to db
                                String ageInputString = ageInputEDT.getText().toString();
                                int ageInput = Integer.parseInt(ageInputString);
                                String weightInputString = weightInputEDT.getText().toString();
                                int weightInput = Integer.parseInt(weightInputString);
                                String heightInputString = heightInputEDT.getText().toString();
                                int heightInput = Integer.parseInt(heightInputString);

                                databaseHelper.insertUser(usernameInput, passwordInput, firstNameInput, lastNameInput, ageInput, weightInput, heightInput);

                                setContentView(R.layout.login_page);
                                initializeLogin();
                            }
                        });
                    }
                });


            }
        });
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = fragmentMap.get(item.getItemId());
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, selectedFragment)
                    .commit();
            return true;
        }
        return false;
    }

    public void checkLogin(){

        String usernameCheck = usernameLoginInput.getText().toString();
        String passwordCheck = passwordLoginInput.getText().toString();
        try {
            loginSuccess = databaseHelper.checkUser(usernameCheck, passwordCheck);
        }
        catch (SQLiteException e){
            Log.d("Error", e.getMessage());
        }


        if (loginSuccess){
            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
            //Go to home page
            setContentView(R.layout.activity_main);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            // Initialize fragments
            fragmentMap.put(R.id.person, new FirstFragment());
            fragmentMap.put(R.id.home, new SecondFragment());
            fragmentMap.put(R.id.settings, new ThirdFragment());


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, fragmentMap.get(R.id.home))
                    .commit();
        }
        else {
            Toast.makeText(MainActivity.this, "Login failed, username or password incorrect", Toast.LENGTH_LONG).show();
        }

    }

    public void initializeLogin(){
        //Gets username and password views
        usernameLoginInput = (EditText) findViewById(R.id.usernameInput2);
        passwordLoginInput = (EditText) findViewById(R.id.passwordInput2);
        loginButton = (Button) findViewById(R.id.loginButton2);
        signUpButton = (Button) findViewById(R.id.signUpButton);

        // Check for null objects
        if (usernameLoginInput != null && passwordLoginInput != null && loginButton != null && signUpButton != null) {
            // Set click listeners
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkLogin();
                }
            });

            signUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createUser();
                }
            });
        } else {
            Log.e("initializeLogin", "One or more views are null");
        }
    }

    public void seeAddGoal(View view) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, new AddToFood())
                .commit();
    }

    public void showFoods(){

        browseFoodsTable = findViewById(R.id.browseFoodsTable);
        Cursor cursor = databaseHelper.selectFoods();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int foodID = cursor.getInt(cursor.getColumnIndex("foodID"));
            @SuppressLint("Range") String foodDescription = cursor.getString(cursor.getColumnIndex("FoodDescription"));
            @SuppressLint("Range") String foodCategory = cursor.getString(cursor.getColumnIndex("FoodCategory"));
            @SuppressLint("Range") int calsPerServing = cursor.getInt(cursor.getColumnIndex("CaloriesPerServing"));
            @SuppressLint("Range") float weightPerServ = cursor.getInt(cursor.getColumnIndex("WeightPerServingInGrams"));


            TableRow newRow = new TableRow(this);
            newRow.setWeightSum(1);

            TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
                    0, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Height
                    0.25f // Weight
            );

            TextView descView = new TextView(this);
            descView.setText(foodDescription);
            descView.setLayoutParams(textViewParams);

            TextView catView = new TextView(this);
            catView.setText(foodCategory);
            catView.setLayoutParams(textViewParams);

            TextView calsView = new TextView(this);
            calsView.setText("" + calsPerServing);
            calsView.setLayoutParams(textViewParams);

            TextView weightView = new TextView(this);
            weightView.setText("" + weightPerServ);
            weightView.setLayoutParams(textViewParams);

            newRow.addView(descView);
            newRow.addView(catView);
            newRow.addView(calsView);
            newRow.addView(weightView);

            browseFoodsTable.addView(newRow);
        }
    }

    public void showExercise(){

        browseExerciseTable = findViewById(R.id.browseExerciseTable);
        Cursor cursor = databaseHelper.selectExercise();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int exerciseID = cursor.getInt(cursor.getColumnIndex("ExerciseID"));
            @SuppressLint("Range") String exerciseDescription = cursor.getString(cursor.getColumnIndex("ExerciseDescription"));
            @SuppressLint("Range") float calsPerMinute = cursor.getInt(cursor.getColumnIndex("CalsBurnedPerMin"));


            TableRow newRow = new TableRow(this);
            newRow.setWeightSum(1);

            TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
                    0, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Height
                    0.5f // Weight
            );

            TextView descView = new TextView(this);
            descView.setText(exerciseDescription);
            descView.setLayoutParams(textViewParams);

            TextView calsView = new TextView(this);
            calsView.setText("" + calsPerMinute);
            calsView.setLayoutParams(textViewParams);

            newRow.addView(descView);
            newRow.addView(calsView);

            browseExerciseTable.addView(newRow);
        }
    }
}