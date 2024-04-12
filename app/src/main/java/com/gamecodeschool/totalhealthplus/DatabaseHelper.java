package com.gamecodeschool.totalhealthplus;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public String db_name;
    public int db_version;

    // Foods table
    public String foodTableName;
    public String createFoodTableQuery;

    //Users table
    public String createUsersTableQuery;
    public String usersTableName;

    //Exercises to choose from table
    public String createExSelectTableQuery;
    public String exSelectTableName;

    //Activity table
    public String createActivityTableQuery;
    public String activityTableName;

    //Intake table
    public String createIntakeTableQuery;
    public String intakeTableName;

    //Goals table
    public String prevGoalsMetTableName;
    public String createPrevGoalsMetTableQuery;

    //Calculate all current calorie counts for User
    public String calculateCalsFoods;
    public String calculateCalsExercises;


    //Create methods to perform functions such as update, insert, delete, using Cursor class
    //which will allow iteration through returned data. Return long type to check row num
    public DatabaseHelper(Context context) {
        super(context, "totalhealthplus.DB", null, 5);

        db_name = "totalhealthplus.DB";
        db_version = 2;

        //foods to choose from and enter
        foodTableName = "foods";
        createFoodTableQuery =
                "CREATE TABLE " + foodTableName + "(foodID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "FoodDescription VARCHAR(50), " +
                        "FoodCategory VARCHAR(50), " +
                        "CaloriesPerServing INT, " +
                        "WeightPerServingInGrams FLOAT);";

        //users to login
        usersTableName = "users";
        createUsersTableQuery =
                "CREATE TABLE " + usersTableName + "(Username VARCHAR(50) PRIMARY KEY, " +
                        "Password VARCHAR(50), " +
                        "FirstName VARCHAR(50), " +
                        "LastName VARCHAR(50), " +
                        "Age INT, " +
                        "HeightInInches INT, " +
                        "WeightInLbs INT);";

        //insert exercise selections with cals burned per min
        exSelectTableName = "exercise_selections";
        createExSelectTableQuery =
                "CREATE TABLE " + exSelectTableName + "(ExerciseID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "ExerciseDescription VARCHAR(50), " +
                        "CalsBurnedPerMin FLOAT);";

        //record of activities that user does throughout the day
        activityTableName = "daily_activities";
        createActivityTableQuery =
                "CREATE TABLE " + activityTableName + "(ActivityID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Username VARCHAR(50), " +
                        "DateActive date, " +
                        "ExerciseDescription VARCHAR(50), " +
                        "DurationInMin FLOAT, " +
                        "TotalCalsBurned FLOAT, " +
                        "FOREIGN KEY (ExerciseDescription) REFERENCES exercise_selections(ExerciseDescription)" +
                        "FOREIGN KEY (Username) REFERENCES users(Username));";

        intakeTableName = "daily_intake";
        createIntakeTableQuery =
                "CREATE TABLE " + intakeTableName + "(IntakeID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Username VARCHAR(50), " +
                        "Date date, " +
                        "FoodDescription VARCHAR(50), " +
                        "Servings INT, " +
                        "TotalCalsIn INT, " +
                        "FOREIGN KEY (FoodDescription) REFERENCES foods(FoodDescription)," +
                        "FOREIGN KEY (Username) REFERENCES users(Username));";

        prevGoalsMetTableName = "prev_goals_met";
        createPrevGoalsMetTableQuery =
                "CREATE TABLE " + prevGoalsMetTableName + "(GoalID INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Username VARCHAR(50), " +
                        "Date date, " +
                        "Goal INT, " +
                        "GoalMet INT, " +
                        "FOREIGN KEY (Username) REFERENCES users(Username));";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(createFoodTableQuery);
        db.execSQL(createUsersTableQuery);
        db.execSQL(createExSelectTableQuery);
        db.execSQL(createActivityTableQuery);
        db.execSQL(createIntakeTableQuery);
        db.execSQL(createPrevGoalsMetTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + foodTableName);
        db.execSQL("DROP TABLE IF EXISTS " + usersTableName);
        db.execSQL("DROP TABLE IF EXISTS " + exSelectTableName);
        db.execSQL("DROP TABLE IF EXISTS " + activityTableName);
        db.execSQL("DROP TABLE IF EXISTS " + intakeTableName);
        db.execSQL("DROP TABLE IF EXISTS " + prevGoalsMetTableName);
        onCreate(db);

    }

    public long insertFood(String description, String category, int calPerServ, float weightPerServing){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("FoodDescription", description);
        values.put("FoodCategory", category);
        values.put("CaloriesPerServing", calPerServ);
        values.put("WeightPerServingInGrams", weightPerServing);

        long insertingResult = database.insert("foods", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public int deleteFood(String description){

        SQLiteDatabase database = this.getWritableDatabase();

        //Returns number of rows deleted
        return database.delete("foods", "FoodDescription=?", new String[]{description});
    }

    //
    public Cursor selectFoods(){

        SQLiteDatabase testDb = getReadableDatabase();
        String select = "SELECT foods.* FROM foods";
        String result = "";
        Cursor cursor = testDb.rawQuery(select, null);

        return cursor;
    }

    public long insertUser(String un, String pw, String firstName, String lastName, int age, int height, int weight){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Username", un);
        values.put("Password", pw);
        values.put("FirstName", firstName);
        values.put("LastName", lastName);
        values.put("Age", age);
        values.put("HeightInInches", height);
        values.put("WeightInLbs", weight);

        long insertingResult = database.insert("users", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public int deleteUser(String un){

        SQLiteDatabase database = this.getWritableDatabase();

        //Returns number of rows deleted
        return database.delete("users", "Username=?", new String[]{un});
    }

    public String selectUsers(){

        SQLiteDatabase testDb = getReadableDatabase();
        String select = "SELECT users.* FROM users";
        String result = "";
        Cursor cursor = testDb.rawQuery(select, null);

        if(cursor.moveToNext()){

            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("Username"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("Password"));
            @SuppressLint("Range") String firstName = cursor.getString(cursor.getColumnIndex("FirstName"));
            @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex("LastName"));
            @SuppressLint("Range") int age = cursor.getInt(cursor.getColumnIndex("Age"));
            @SuppressLint("Range") int height = cursor.getInt(cursor.getColumnIndex("HeightInInches"));
            @SuppressLint("Range") int weight = cursor.getInt(cursor.getColumnIndex("WeightInLbs"));
            result = username + " " + password + " " + firstName + " " + lastName + " " + age +
                    " " + height + " " + weight;
        }

        return result;
    }

    public boolean checkUser(String username, String password){
        boolean success = false;

        String checkUserQuery = "SELECT Username, Password FROM users WHERE Username = " + "'" + username + "'" +
                " AND Password = " + "'" + password + "'" + ";";

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(checkUserQuery, null);

        if (cursor.moveToNext()){

            @SuppressLint("Range") String usernameNew = cursor.getString(cursor.getColumnIndex("Username"));
            @SuppressLint("Range") String passwordNew = cursor.getString(cursor.getColumnIndex("Password"));

            if (username.equals(usernameNew) && password.equals(passwordNew)){
                success = true;
            }
        }


        return success;
    }

    public long insertExercise(String exerciseDescription, float calsBurnedPerMin){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ExerciseDescription", exerciseDescription);
        values.put("CalsBurnedPerMin", calsBurnedPerMin);

        long insertingResult = database.insert("exercise_selections", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public Cursor selectExercise() {

        SQLiteDatabase testDb = getReadableDatabase();
        String select = "SELECT exercise_selections.* FROM exercise_selections";
        String result = "";

        return testDb.rawQuery(select, null);
    }

    public int deleteExercise(String exerciseDescription){

        SQLiteDatabase database = this.getWritableDatabase();

        //Returns number of rows deleted
        return database.delete("exercise_selections", "ExerciseDescription=?", new String[]{exerciseDescription});
    }

    public long insertActivity(String username, String dateActive, String exDesc, float duration, float totalCalsBurned){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Username", username);
        values.put("DateActive", dateActive);
        values.put("ExerciseDescription", exDesc);
        values.put("DurationInMin", duration);
        values.put("TotalCalsBurned", totalCalsBurned);

        long insertingResult = database.insert("daily_activities", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public int deleteActivity(String date, String exerciseDescription){

        SQLiteDatabase database = this.getWritableDatabase();

        //Returns number of rows deleted
        return database.delete("daily_activities", "DateActive=? AND ExerciseDescription=?",
                new String[]{date, exerciseDescription});
    }

    public long insertFoodIntake(String username, String date, String foodDescription, int servings, int totalCalsIn){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Username", username);
        values.put("Date", date);
        values.put("FoodDescription", foodDescription);
        values.put("Servings", servings);
        values.put("TotalCalsIn", totalCalsIn);

        long insertingResult = database.insert("daily_intake", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public int deleteIntake(String date, String foodDescription, int servings){

        SQLiteDatabase database = this.getWritableDatabase();

        //Returns number of rows deleted
        return database.delete("daily_intake", "Date=? AND FoodDescription=? AND " +
                        "Servings=?",
                new String[]{date, foodDescription, Integer.toString(servings)});
    }

    public long insertCalorieGoal(String username, String date, int calories){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Username", username);
        values.put("Date", date);
        values.put("Goal", calories);
        values.put("GoalMet", 0);

        long insertingResult = database.insert("prev_goals_met", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;

    }

    @SuppressLint("Range")
    public int calculateCalories(String food, int servings){

        int result = 0;
        int caloriesPerServing = 0;

        SQLiteDatabase testDb = getReadableDatabase();

        String calculateCalsQuery = "SELECT CaloriesPerServing FROM foods WHERE FoodDescription = " + "'" + food + "'";
        Cursor cursor = testDb.rawQuery(calculateCalsQuery, null);

        if (cursor.moveToFirst()) {
            caloriesPerServing = cursor.getInt(cursor.getColumnIndex("CaloriesPerServing"));
        }

        cursor.close();
        testDb.close();

        result = servings * caloriesPerServing;

        return result;

    }

    @SuppressLint("Range")
    public int calculateFoodCalsDay(String username, String date){
        int calsFoods = 0;
        calculateCalsFoods = "SELECT SUM(TotalCalsIn) FROM daily_intake " +
                "WHERE Username = " + "'" + username + "'"
                + " AND Date = " + "'" + date + "';";

       SQLiteDatabase db = this.getReadableDatabase();

       Cursor cursor = db.rawQuery(calculateCalsFoods, null);

       if (cursor.moveToFirst()){
           calsFoods = cursor.getInt(cursor.getColumnIndex("TotalCalsIn"));
       }

       cursor.close();
       db.close();

       return calsFoods;
    }

    @SuppressLint("Range")
    public int calculateExerciseCalsDay(String username, String date){
        int calsExercise = 0;
        calculateCalsExercises = "SELECT SUM(TotalCalsBurned) FROM daily_activities " +
                "WHERE Username = " + "'" + username + "'"
                + " AND Date = " + "'" + date + "';";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(calculateCalsExercises, null);

        if (cursor.moveToFirst()){
            calsExercise = cursor.getInt(cursor.getColumnIndex("TotalCalsBurned"));
        }

        cursor.close();
        db.close();

        return calsExercise;
    }



    @SuppressLint("Range")
    public float calculateActivityBurned(String exerciseSpinnerVal, int minSpinnerVal) {

        int result = 0;
        int caloriesPerMin = 0;

        SQLiteDatabase testDb = getReadableDatabase();

        String calculateCalsQuery = "SELECT CalsBurnedPerMin FROM exercise_selections WHERE ExerciseDescription = " + "'" + exerciseSpinnerVal + "'";
        Cursor cursor = testDb.rawQuery(calculateCalsQuery, null);

        if (cursor.moveToFirst()) {
            caloriesPerMin = cursor.getInt(cursor.getColumnIndex("CalsBurnedPerMin"));
        }

        cursor.close();
        testDb.close();

        result = minSpinnerVal * caloriesPerMin;

        return result;
    }


}