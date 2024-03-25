package com.gamecodeschool.totalhealthplus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener , GoalAdapter.OnItemClickListener{

    private EditText usernameInputEDT, passwordInputEDT, firstNameInputEDT,
            lastNameInputEDT, ageInputEDT, weightInputEDT, heightInputEDT, usernameLoginInput, passwordLoginInput;
    private Button createUserButton1, createUserButton2, createUserButton3, loginButton, signUpButton;


    public static DatabaseHelper databaseHelper;

    private TableLayout browseFoodsTable;

    private boolean loginSuccess;


//    private TableLayout browseFoodsTable, browseExerciseTable;

    public static List<Goal> currentUserGoalList = new ArrayList<>();

    public static GoalAdapter goalAdapter;
    private RecyclerView goalRecyclerView;
    String dateString;

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

//        databaseHelper.insertFood("Beets, 1/2 cup sliced", "Vegetables", 35, 85);


        // Get the current date
        Date currentDate = new Date();

        // Define a date format
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        // Format the current date as a string
        dateString = formatter.format(currentDate);
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

            currentUserGoalList = getGoalsForUser(usernameCheck);
            goalAdapter = new GoalAdapter(currentUserGoalList, this);



            //Go to home page
            setContentView(R.layout.activity_main);

            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            bottomNavigationView.setOnNavigationItemSelectedListener(this);

            // Initialize fragments
            fragmentMap.put(R.id.FoodDashboard, new FoodDashboard());
            fragmentMap.put(R.id.MainDashboard, new MainDashboard());
            fragmentMap.put(R.id.FitnessDashboard, new FitnessDashboard());


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, fragmentMap.get(R.id.MainDashboard))
                    .commit();


            bottomNavigationView.setSelectedItemId(R.id.MainDashboard);

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



    public void seeFoodPrev(View v){
        setContentView(R.layout.food_previous);
    }

    public void seeFoodAdd(View v){setContentView(R.layout.food_add);}

    public void seeFoodBrowse(View view) {
        setContentView(R.layout.food_browse);
        showFoods();

    }

    public void seeFitnessPrev(View v){
        setContentView(R.layout.fitness_previous);
    }
    public void seeFitnessAdd(View v){
        setContentView(R.layout.fitness_add);
    }
    public void seeFitnessBrowse(View v){
        setContentView(R.layout.fitness_browse);
    }
    public void seeMainDash(View v){
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, new MainDashboard())
                .commit();
    }
    public void seeFoodDash(View v){
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, new FoodDashboard())
                .commit();

    }
    public void seeFitnessDash(View v){
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, new FitnessDashboard())
                .commit();
    }

    @Override
    public void onItemClick(Goal goal) {
        GoalDetailsDialog goalDetailsDialog = new GoalDetailsDialog();

        goalDetailsDialog.setGoalEntry(goal);

        goalDetailsDialog.show(getSupportFragmentManager(), "123");
    }

    public List<Goal> getGoalsForUser(String username){

        List<Goal> resultList = new ArrayList<>();
        String getGoalsQuery = "SELECT Date, Goal, GoalMet, GoalCategory FROM prev_goals_met WHERE Username = " + username;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(getGoalsQuery, null)) {
            if (cursor != null && cursor.moveToFirst())
            {
                do
                {

                    String dateStr = cursor.getString(cursor.getColumnIndexOrThrow("Date"));
                    @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("Goal"));
                    @SuppressLint("Range") int goalMet = cursor.getInt(cursor.getColumnIndex("GoalMet"));
                    @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("GoalCategory"));

                    resultList.add(new Goal(dateStr, description, category, goalMet == 1)); // Assuming 1 represents true for goal met
                } while (cursor.moveToNext());
            }
        }
        catch (SQLiteException e)
        {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        finally
        {
            if (db != null)
            {
                db.close(); // Close the database connection
            }
        }

        return resultList;
    }




    @SuppressLint("MissingInflatedId")
    public void PreGoals(View v){
        setContentView(R.layout.fragment_past_goals);

        goalRecyclerView = findViewById(R.id.goalRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        goalRecyclerView.setLayoutManager(mLayoutManager);

        goalRecyclerView.setItemAnimator(new DefaultItemAnimator());
        goalRecyclerView.setAdapter(goalAdapter);
    }

}

