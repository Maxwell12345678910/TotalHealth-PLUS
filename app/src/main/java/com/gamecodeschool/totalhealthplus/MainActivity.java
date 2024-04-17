package com.gamecodeschool.totalhealthplus;

import static com.gamecodeschool.totalhealthplus.FoodAddSearch.containsCharacters;

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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Spinner;
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

    Spinner minutesSpinner;
    private Spinner foodSpinner; //need this global for dynamic use of below vars
    Spinner servingsSpinner;//populating this dynamically now
    Spinner exerciseSpinner; //populating this dynamically now
    private EditText usernameInputEDT, passwordInputEDT, firstNameInputEDT,
            lastNameInputEDT, ageInputEDT, weightInputEDT, heightInputEDT, usernameLoginInput, passwordLoginInput;
    private Button createUserButton1, createUserButton2, createUserButton3, loginButton, signUpButton;


    public static DatabaseHelper databaseHelper;

    private TableLayout browseFoodsTable;

    private boolean loginSuccess;

    public static List<CalorieGoal> currentUserCalorieGoalList = new ArrayList<>();

    public static GoalAdapter goalAdapter;
    private RecyclerView goalRecyclerView;
    public static String dateString;
    public static String FoodKeyword; // this is the keyword they type in for search
    public static String FitnessKeyword;
    private EditText SearchKeyword;
    private TableLayout findMyFood;
    private TableLayout findMyFitness;
    private TableLayout FindFitness;
    private TableLayout intakeTable;
    private TableLayout activityTable;
    private BottomNavigationView bottomNavigationView;
    private SparseArray<Fragment> fragmentMap = new SparseArray<>();
    private ArrayList<String> foodSpinnerList;
    private ArrayList<String> exerciseSpinnerList;
    public static String currentUsername;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        currentUsername = "";

        setContentView(R.layout.login_page);

        databaseHelper = new DatabaseHelper(this);
        initializeLogin();

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.FoodDashboard) {
            seeFoodDash(null); // Replace null with appropriate View instance if needed
            return true;
        } else if (item.getItemId() == R.id.MainDashboard) {
            seeMainDash(null); // Replace null with appropriate View instance if needed
            return true;
        } else if (item.getItemId() == R.id.FitnessDashboard) {
            seeFitnessDash(null); // Replace null with appropriate View instance if needed
            return true;
        } else {
            return false;
        }
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
            currentUsername = usernameCheck;

            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();

            currentUserCalorieGoalList = getGoalsForUser(usernameCheck);

            goalAdapter = new GoalAdapter(currentUserCalorieGoalList, this);

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

    public void startFoodSearch(View v) {

        SearchKeyword = (EditText) findViewById(R.id.FindFoodSubmit);
        String word = SearchKeyword.getText().toString();

        findMyFood = findViewById(R.id.FindFoods2);

        if (findMyFood.getChildCount() > 1){

            for (int i = findMyFood.getChildCount() - 1; i > 0; i--){
                findMyFood.removeViewAt(i);
            }

        }

        fillFoodsTable(word);
        //Log.d(TAG, "Text sent: " + FoodKeyword);
    }

    public void startExerciseSearch(View v) {

        SearchKeyword = (EditText) findViewById(R.id.findFitnessSubmit);
        String word = SearchKeyword.getText().toString();

        findMyFitness = findViewById(R.id.FindFitness2);

        if (findMyFitness.getChildCount() > 1){

            for (int i = findMyFitness.getChildCount() - 1; i > 0; i--){
                findMyFitness.removeViewAt(i);
            }

        }

        fillExercisesTable(word);
        //Log.d(TAG, "Text sent: " + FoodKeyword);
    }

    public void fillExercisesTable(String keyword){
        Cursor cursor = databaseHelper.selectExercises();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int exerciseID = cursor.getInt(cursor.getColumnIndex("ExerciseID"));
            @SuppressLint("Range") String exerciseDescription = cursor.getString(cursor.getColumnIndex("ExerciseDescription"));
            @SuppressLint("Range") float calsBurnedPerMin = cursor.getInt(cursor.getColumnIndex("CalsBurnedPerMin"));

            if (containsCharacters(exerciseDescription, keyword) || keyword.equals("")) {

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
                calsView.setText("" + calsBurnedPerMin);
                calsView.setLayoutParams(textViewParams);

                newRow.addView(descView);
                newRow.addView(calsView);

                findMyFitness.addView(newRow);

            }
        }
        cursor.close();
    }

    public void fillFoodsTable(String keyword){

        Cursor cursor = databaseHelper.selectFoods();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int foodID = cursor.getInt(cursor.getColumnIndex("foodID"));
            @SuppressLint("Range") String foodDescription = cursor.getString(cursor.getColumnIndex("FoodDescription"));
            @SuppressLint("Range") String foodCategory = cursor.getString(cursor.getColumnIndex("FoodCategory"));
            @SuppressLint("Range") int calsPerServing = cursor.getInt(cursor.getColumnIndex("CaloriesPerServing"));
            @SuppressLint("Range") float weightPerServ = cursor.getInt(cursor.getColumnIndex("WeightPerServingInGrams"));

            if (containsCharacters(foodDescription, keyword) || keyword.equals("")) {

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

                findMyFood.addView(newRow);

            }
        }
        cursor.close();
    }

    public void fillIntakeTable(){

        intakeTable = (TableLayout) findViewById(R.id.intakeTable);

        if (intakeTable.getChildCount() > 0){

            for (int i = intakeTable.getChildCount() - 1; i > 0; i--){
                intakeTable.removeViewAt(i);
            }

        }

        Cursor cursor = databaseHelper.selectIntakes();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int intakeID = cursor.getInt(cursor.getColumnIndex("IntakeID"));
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("Username"));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("Date"));
            @SuppressLint("Range") String foodDescription = cursor.getString(cursor.getColumnIndex("FoodDescription"));
            @SuppressLint("Range") int servings = cursor.getInt(cursor.getColumnIndex("Servings"));
            @SuppressLint("Range") int calories = cursor.getInt(cursor.getColumnIndex("TotalCalsIn"));


            TableRow newRow = new TableRow(this);
            newRow.setWeightSum(1);

            TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
                    0, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Height
                    0.25f // Weight
            );

            TextView dateView = new TextView(this);
            dateView.setText(date);
            dateView.setLayoutParams(textViewParams);

            TextView descView = new TextView(this);
            descView.setText(foodDescription);
            descView.setLayoutParams(textViewParams);

            TextView calsView = new TextView(this);
            calsView.setText("" + calories);
            calsView.setLayoutParams(textViewParams);

            TextView servView = new TextView(this);
            servView.setText("" + servings);
            servView.setLayoutParams(textViewParams);

            newRow.addView(dateView);
            newRow.addView(descView);
            newRow.addView(calsView);
            newRow.addView(servView);

            intakeTable.addView(newRow);
        }
        cursor.close();
    }


    public void findExercises(String keyword){

        Cursor cursor = databaseHelper.selectExercises();

        while (cursor.moveToNext()) {

            @SuppressLint("Range") int exerciseID = cursor.getInt(cursor.getColumnIndex("ExerciseID"));
            @SuppressLint("Range") String exerciseDescription = cursor.getString(cursor.getColumnIndex("ExerciseDescription"));
            @SuppressLint("Range") float calsPerMinute = cursor.getInt(cursor.getColumnIndex("CalsBurnedPerMin"));

            if (containsCharacters(exerciseDescription, keyword) || keyword.equals("")) {

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

                //FindFitness.removeAllViews();
                FindFitness.addView(newRow);

            }
        }
    }

    public void seeFoodBrowse(View v){
        setContentView(R.layout.food_browse);
    }
    public void seeFoodLibraryAdd(View v){
        setContentView(R.layout.food_add_library);
    }

    public void submitFoodLibraryAdd(View v){

        EditText foodName = findViewById(R.id.foodLibraryName);
        EditText foodCals = findViewById(R.id.foodLibraryCals);
        EditText foodCategory = findViewById(R.id.foodLibraryCategory);
        EditText foodWeight = findViewById(R.id.foodLibraryWeight);

        try
        {
            databaseHelper.insertFood(foodName.getText().toString(), foodCategory.getText().toString(),
                    Integer.parseInt(foodCals.getText().toString()), Float.parseFloat(foodWeight.getText().toString()));
            Toast.makeText(this, "Food insertion successful", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Please enter valid values for fields", Toast.LENGTH_SHORT).show();
        }

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

        bottomNavigationView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Menu menu = bottomNavigationView.getMenu();
                MenuItem menuItem = menu.getItem(1); // Index starts from 0
                menuItem.setChecked(true);
            }
        }, 0);
    }

    public void seeFoodDash(View v){
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, new FoodDashboard())
                .commit();

    }

    public void seePrevIntake(View v){
        setContentView(R.layout.food_past);

        fillIntakeTable();
    }

    public void seePrevActivity(View v){
        setContentView(R.layout.fitness_past);

        fillActivityTable();
    }

    public void fillActivityTable(){
        activityTable = (TableLayout) findViewById(R.id.activityTable);

        if (activityTable.getChildCount() > 0){

            for (int i = activityTable.getChildCount() - 1; i > 0; i--){
                activityTable.removeViewAt(i);
            }

        }

        Cursor cursor = databaseHelper.selectActivities();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int activityID = cursor.getInt(cursor.getColumnIndex("ActivityID"));
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("Username"));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("DateActive"));
            @SuppressLint("Range") String exerciseDescription = cursor.getString(cursor.getColumnIndex("ExerciseDescription"));
            @SuppressLint("Range") int duration = cursor.getInt(cursor.getColumnIndex("DurationInMin"));
            @SuppressLint("Range") int calsBurned = cursor.getInt(cursor.getColumnIndex("TotalCalsBurned"));


            TableRow newRow = new TableRow(this);
            newRow.setWeightSum(1);

            TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(
                    0, // Width
                    ViewGroup.LayoutParams.WRAP_CONTENT, // Height
                    0.33f // Weight
            );

            TextView dateView = new TextView(this);
            dateView.setText(date);
            dateView.setLayoutParams(textViewParams);

            TextView descView = new TextView(this);
            descView.setText(exerciseDescription);
            descView.setLayoutParams(textViewParams);

            TextView calsView = new TextView(this);
            calsView.setText("" + calsBurned);
            calsView.setLayoutParams(textViewParams);

            newRow.addView(dateView);
            newRow.addView(descView);
            newRow.addView(calsView);

            activityTable.addView(newRow);
        }
        cursor.close();
    }

    public void seeFitnessDash(View v){
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.flFragment, new FitnessDashboard())
                .commit();

        bottomNavigationView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Menu menu = bottomNavigationView.getMenu();
                MenuItem menuItem = menu.getItem(2); // Index starts from 0
                menuItem.setChecked(true);
            }
        }, 0);
    }

    public void seeFitnessLibraryAdd(View v){
        setContentView(R.layout.fitness_add_library);

    }

    public void submitFitnessLibraryAdd(View v){

        EditText fitnessName = findViewById(R.id.fitnessLibraryName);
        EditText fitnessCals = findViewById(R.id.fitnessLibraryCals);

        try
        {
            databaseHelper.insertExercise(fitnessName.getText().toString(), Float.parseFloat(fitnessCals.getText().toString()));
            Toast.makeText(this, "Exercise insertion successful", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Please enter valid values for fields", Toast.LENGTH_SHORT).show();
        }

    }

    public void seeAddCompletedExercise(View v){
        setContentView(R.layout.fitness_add_completed_exercise);

        //set an onclick listener for the exercise spinner so we can dynamilcally populate the minutes spinner below it after the user selects an exercise
        exerciseSpinner = findViewById(R.id.exerciseSpinner);
        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateMinutesSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { //do nothing
            }
        });

        populateSpinnersExercise();
    }



    public void seeAddFoodIntake(View v){

        setContentView(R.layout.food_add_today_intake);

        // Set onClick listener on foodSpinner
        foodSpinner = findViewById(R.id.foodSpinner);

        //this makes it so that data is set in the bottom spinner every time the user sets the data for the top spinner
        foodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Call populateServingsSpinner method when the user selects an item in the food spinner
                populateServingsSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        populateSpinnersFood();

    }

    @Override
    public void onItemClick(CalorieGoal calorieGoal) {
        GoalDetailsDialog goalDetailsDialog = new GoalDetailsDialog();

        goalDetailsDialog.setGoalEntry(calorieGoal);

        goalDetailsDialog.show(getSupportFragmentManager(), "123");
    }

    public List<CalorieGoal> getGoalsForUser(String username){

        List<CalorieGoal> resultList = new ArrayList<>();
        String getGoalsQuery = "SELECT Date, Goal, GoalMet FROM prev_goals_met WHERE Username = " + username;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        try (Cursor cursor = db.rawQuery(getGoalsQuery, null)) {
            if (cursor != null && cursor.moveToFirst())
            {
                do
                {

                    String dateStr = cursor.getString(cursor.getColumnIndexOrThrow("Date"));
                    @SuppressLint("Range") int calories = cursor.getInt(cursor.getColumnIndex("Goal"));
                    @SuppressLint("Range") int goalMet = cursor.getInt(cursor.getColumnIndex("GoalMet"));

                    resultList.add(new CalorieGoal(dateStr, calories, goalMet == 1)); // Assuming 1 represents true for goal met
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
    public void seePreGoals(View v){
        setContentView(R.layout.fragment_past_goals);

        goalRecyclerView = findViewById(R.id.goalRecyclerView);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        goalRecyclerView.setLayoutManager(mLayoutManager);

        goalRecyclerView.setItemAnimator(new DefaultItemAnimator());
        goalRecyclerView.setAdapter(goalAdapter);
    }

    public void populateSpinnersFood(){

        foodSpinnerList = new ArrayList<>();

        populateFoodSpinnerList();

        // Create an ArrayAdapter using the foods data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, foodSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Get reference to foods selection spinner
        Spinner spinner = findViewById(R.id.foodSpinner);

        // Set the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    private void populateMinutesSpinner() {
        minutesSpinner = findViewById(R.id.minutesSpinner);
        //get the name of the exercise the user selects
        exerciseSpinner = findViewById(R.id.exerciseSpinner);
        String selectedExercise = exerciseSpinner.getSelectedItem().toString(); //use to dynamically call cals/min burn

        float burnedCals5 = databaseHelper.calculateActivityBurned(selectedExercise, 5);
        float burnedCals10 = databaseHelper.calculateActivityBurned(selectedExercise, 10);
        float burnedCals20 = databaseHelper.calculateActivityBurned(selectedExercise, 20);
        float burnedCals30 = databaseHelper.calculateActivityBurned(selectedExercise, 30);
        float burnedCals60 = databaseHelper.calculateActivityBurned(selectedExercise, 60);
        float burnedCals120 = databaseHelper.calculateActivityBurned(selectedExercise, 120);



        String[] minuteIncrements =
                {
                    "005" + " minutes : " + burnedCals5 + " cals burned" ,
                    "010" + " minutes : " + burnedCals10 + " cals burned" ,
                    "020" + " minutes : " + burnedCals20 + " cals burned" ,
                    "030" + " minutes : " + burnedCals30 + " cals burned" ,
                    "060" + " minutes : " + burnedCals60 + " cals burned" ,
                    "090" + " minutes : " + databaseHelper.calculateActivityBurned(selectedExercise,90) + " cals burned" ,
                    "120" + " minutes : " + burnedCals120 + " cals burned" ,
                };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,minuteIncrements);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minutesSpinner.setAdapter(adapter);
    }

    public void populateServingsSpinner() {
        servingsSpinner = findViewById(R.id.servingsSpinner);
        String foodSpinnerVal = foodSpinner.getSelectedItem().toString(); //name of food user selected from TOP spinner

        //get the number of grams per 1 serving next line is just an example but after that i use this calculation to populate the string array for dropdown menu items
        //float grams = databaseHelper.calculateGrams(1,foodSpinnerVal);
        String[] servingIncrements = {"1 Serving - " + "   " + databaseHelper.calculateGrams(1,foodSpinnerVal) + " grams" + "  -     " + databaseHelper.calculateCalories(foodSpinnerVal,1) + "   cals"
                , "2 Servings : " + "   " + databaseHelper.calculateGrams(2,foodSpinnerVal) + " grams" + "  -     " + databaseHelper.calculateCalories(foodSpinnerVal,2) + "   cals"
                , "3 Servings : " + "   " + databaseHelper.calculateGrams(3,foodSpinnerVal) + " grams"+ "  -     " + databaseHelper.calculateCalories(foodSpinnerVal,3) + "   cals"
                , "4 Servings : " + "   " + databaseHelper.calculateGrams(4,foodSpinnerVal) + " grams"+ "  -     " + databaseHelper.calculateCalories(foodSpinnerVal,4) + "   cals"
                , "5 Servings : " + "   " + databaseHelper.calculateGrams(5,foodSpinnerVal) + " grams"+ "  -     " + databaseHelper.calculateCalories(foodSpinnerVal,5) + "   cals"
                };

        // Create an ArrayAdapter using the array
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, servingIncrements);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the servingsSpinner
        servingsSpinner.setAdapter(adapter);
    }

    public void populateFoodSpinnerList(){

        Cursor cursor = databaseHelper.selectFoods();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int foodID = cursor.getInt(cursor.getColumnIndex("foodID"));
            @SuppressLint("Range") String foodDescription = cursor.getString(cursor.getColumnIndex("FoodDescription"));
            @SuppressLint("Range") String foodCategory = cursor.getString(cursor.getColumnIndex("FoodCategory"));
            @SuppressLint("Range") int calsPerServing = cursor.getInt(cursor.getColumnIndex("CaloriesPerServing"));
            @SuppressLint("Range") float weightPerServ = cursor.getInt(cursor.getColumnIndex("WeightPerServingInGrams"));

            foodSpinnerList.add(foodDescription);
        }
    }

    public void submitFoodIntake(View view){

        Spinner foodSpinner = findViewById(R.id.foodSpinner);
        servingsSpinner = findViewById(R.id.servingsSpinner);


        String foodSpinnerVal = foodSpinner.getSelectedItem().toString(); //name of food user selected from TOP spinner
        int servingSpinnerVal = Integer.parseInt(servingsSpinner.getSelectedItem().toString().substring(0,1)); //num of servings the user chose from SERVINGS spinner


        databaseHelper.insertFoodIntake(currentUsername, dateString, foodSpinnerVal, servingSpinnerVal, databaseHelper.calculateCalories(foodSpinnerVal, servingSpinnerVal));
        Toast.makeText(this, "Intake inserted successfully.", Toast.LENGTH_SHORT).show();

        //pass data from db helper to call to populateServingsSpinner
        //cals per serving, do the math in the method for each increment  - databaseHelper.calculateCalories(foodSpinnerVal, servingSpinnerVal)
        //grams per servings - databaseHelper.


    }

    public void populateSpinnersExercise(){

        exerciseSpinnerList = new ArrayList<>();

        populateExerciseSpinnerList();

        // Create an ArrayAdapter using the foods data
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exerciseSpinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Get reference to foods selection spinner
        exerciseSpinner = findViewById(R.id.exerciseSpinner);

        // Set the adapter to the spinner
        exerciseSpinner.setAdapter(adapter);
    }

    public void populateExerciseSpinnerList(){

        Cursor cursor = databaseHelper.selectExercises();

        while (cursor.moveToNext()){

            @SuppressLint("Range") int exerciseID = cursor.getInt(cursor.getColumnIndex("ExerciseID"));
            @SuppressLint("Range") String exerciseDescription = cursor.getString(cursor.getColumnIndex("ExerciseDescription"));
            @SuppressLint("Range") float calsPerMin = cursor.getFloat(cursor.getColumnIndex("CalsBurnedPerMin"));

            exerciseSpinnerList.add(exerciseDescription);
        }
    }

    public void submitExercise(View view){

        exerciseSpinner = findViewById(R.id.exerciseSpinner);
        minutesSpinner = findViewById(R.id.minutesSpinner);

        String exerciseSpinnerVal = exerciseSpinner.getSelectedItem().toString();
//        int minSpinnerVal = Integer.parseInt(minutesSpinner.getSelectedItem().toString()); // WE GOTTA CHANGE THIS TO AN SUBSTRING SO THAT WE ONLY GET THE first 2 or 3 chars (minute value) from the string that popuates the array that fils the minutes spinner
        int minSpinnerVal = Integer.parseInt(minutesSpinner.getSelectedItem().toString().substring(0, Math.min(minutesSpinner.getSelectedItem().toString().length(), 3))); // Extracting the first 2 or 3 chars (minute value)


        databaseHelper.insertActivity(currentUsername, dateString, exerciseSpinnerVal, (float) minSpinnerVal, databaseHelper.calculateActivityBurned(exerciseSpinnerVal, minSpinnerVal));
        Toast.makeText(this, "Activity inserted successfully.", Toast.LENGTH_SHORT).show();
    }


}

