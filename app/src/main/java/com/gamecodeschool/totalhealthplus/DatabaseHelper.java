package com.gamecodeschool.totalhealthplus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;

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

    //Create methods to perform functions such as update, insert, delete, using Cursor class
    //which will allow iteration through returned data. Return long type to check row num
    public DatabaseHelper(Context context) {
        super(context, "totalhealthplus.DB", null, 1);

        db_name = "totalhealthplus.DB";
        db_version = 1;

        foodTableName = "foods";
        createFoodTableQuery =
                "CREATE TABLE " + foodTableName + "(foodID int PRIMARY KEY AUTOINCREMENT, " +
                        "FoodDescription varchar(50), " +
                        "FoodCategory varchar(50), " +
                        "CaloriesPerServing int, " +
                        "WeightPerServing(g) float);";

        usersTableName = "users";
        createUsersTableQuery =
                "CREATE TABLE " + usersTableName + "(Username varchar(50) PRIMARY KEY, " +
                        "Password varchar(50), " +
                        "FirstName varchar(50), " +
                        "LastName varchar(50), " +
                        "Age int, " +
                        "Height(in) int, " +
                        "Weight(lbs) int);";

        exSelectTableName = "exercise_selections";
        createExSelectTableQuery =
                "CREATE TABLE " + exSelectTableName + "(ExerciseID int PRIMARY KEY AUTOINCREMENT, " +
                        "ExerciseDescription varchar(50), " +
                        "CalsBurned(/min) float);";

        activityTableName = "daily_activities";
        createActivityTableQuery =
                "CREATE TABLE " + activityTableName + "(ActivityID int PRIMARY KEY AUTOINCREMENT, " +
                        "DateActive varchar(50), " +
                        "ExerciseDescription varchar(50), " +
                        "Duration(min) float, " +
                        "TotalCalsBurned float);";

        /*intakeTableName = "daily_intake";
        createIntakeTableQuery =
                "CREATE TABLE " + intakeTableName + "(MealID int PRIMARY KEY AUTOINCREMENT, " +
                        "Date date, " +
                        "Servings int, " +
                        ");";*/

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createFoodTableQuery);
        db.execSQL(createUsersTableQuery);
        db.execSQL(createExSelectTableQuery);
        db.execSQL(createActivityTableQuery);
        //db.execSQL(createIntakeTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + foodTableName);
        db.execSQL("DROP TABLE IF EXISTS " + usersTableName);
        db.execSQL("DROP TABLE IF EXISTS " + exSelectTableName);
        db.execSQL("DROP TABLE IF EXISTS " + activityTableName);
        //db.execSQL("DROP TABLE IF EXISTS " + intakeTableName);
        onCreate(db);

    }

    public long insertFood(String description, String category, int calPerServ, float weightPerServing){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("FoodDescription", description);
        values.put("FoodCategory", category);
        values.put("CaloriesPerServing", calPerServ);
        values.put("WeightPerServing(g)", weightPerServing);

        long insertingResult = database.insert("foods", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public int deleteFood(String description){

        SQLiteDatabase database = this.getWritableDatabase();
        database.delete("foods", "FoodDescription=?", new String[]{description});



        return 1;
    }

    public long insertUser(String un, String pw, String firstName, String lastName, int age, int height, int weight){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Username", un);
        values.put("Password", pw);
        values.put("FirstName", firstName);
        values.put("LastName", lastName);
        values.put("Age", age);
        values.put("Height(in)", height);
        values.put("Weight(lbs)", weight);

        long insertingResult = database.insert("users", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public long insertExercise(String exerciseDescription, float calsBurnedPerMin){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ExerciseDescription", exerciseDescription);
        values.put("CalsBurned(/min)", calsBurnedPerMin);

        long insertingResult = database.insert("exercise_selections", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }

    public long insertActivity(String dateActive, String exDesc, float duration, float totalCalsBurned){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("DateActive", dateActive);
        values.put("ExerciseDescription", exDesc);
        values.put("Duration(min)", duration);
        values.put("TotalCalsBurned", totalCalsBurned);

        long insertingResult = database.insert("daily_activities", null, values);

        database.close();

        //Will return number of row if successful, -1 otherwise
        return insertingResult;
    }
}